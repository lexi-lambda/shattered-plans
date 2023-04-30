package funorb.cache;

import funorb.util.Whirlpool;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.zip.CRC32;

public final class PageLoader {
  private static final CRC32 CRC_32 = new CRC32();

  private final int pageId;
  private final @NotNull BufferedPageCache pageCache;
  private final @Nullable BufferedPageCache masterIndexCache;
  private final @NotNull PageSource pageSource;
  private final @NotNull CacheWorker cacheWorker;
  private final int pageCrc;
  private final byte@NotNull[] pageExpectedWhirlpool;
  private final int pageVersion;

  private PageIndex index;
  private WorkItem indexItem;
  private final Map<Integer, WorkItem> dataItems = new HashMap<>();
  private Collection<Integer> pendingGroups = new ArrayList<>();
  private @Nullable GroupStatus[] groupStatuses;
  private int nextGroupId = 0;

  public PageLoader(final int pageId,
                    final @NotNull BufferedPageCache pageCache,
                    final @Nullable BufferedPageCache masterIndexCache,
                    final @NotNull PageSource pageSource,
                    final @NotNull CacheWorker cacheWorker,
                    final int pageCrc,
                    final byte@NotNull[] pageExpectedWhirlpool,
                    final int pageVersion) {
    this.pageId = pageId;
    this.pageCache = pageCache;
    this.masterIndexCache = masterIndexCache;
    this.pageSource = pageSource;
    this.cacheWorker = cacheWorker;
    this.pageCrc = pageCrc;
    this.pageExpectedWhirlpool = pageExpectedWhirlpool;
    this.pageVersion = pageVersion;
    if (this.masterIndexCache != null) {
      this.indexItem = this.cacheWorker.tryRead(this.masterIndexCache, this.pageId);
    }
  }

  public void tick() {
    if (this.pendingGroups != null) {
      if (this.tryGetOrCreateIndex() == null) {
        return;
      }

      boolean isLoaded = true;
      for (final Iterator<Integer> it = this.pendingGroups.iterator(); it.hasNext(); ) {
        final Integer groupId = it.next();
        if (this.groupStatuses[groupId] == null) {
          this.tryLoadGroup(LoadMethod.READ_FROM_CACHE, groupId);
        }
        if (this.groupStatuses[groupId] == null) {
          isLoaded = false;
        } else {
          it.remove();
        }
      }

      while (this.nextGroupId < this.index.itemCounts.length) {
        if (this.index.itemCounts[this.nextGroupId] != 0) {
          if (this.cacheWorker.pendingCount >= 250) {
            isLoaded = false;
            break;
          }

          if (this.groupStatuses[this.nextGroupId] == null) {
            this.tryLoadGroup(LoadMethod.READ_FROM_CACHE, this.nextGroupId);
          }

          if (this.groupStatuses[this.nextGroupId] == null) {
            isLoaded = false;
            this.pendingGroups.add(this.nextGroupId);
          }
        }
        ++this.nextGroupId;
      }

      if (isLoaded) {
        this.nextGroupId = 0;
        this.pendingGroups = null;
      }
    }
  }

  public @Nullable PageIndex tryGetOrCreateIndex() {
    if (this.index != null) {
      return this.index;
    }
    if (this.indexItem == null) {
      this.indexItem = this.pageSource.enqueueLoad(ResourceLoader.PageId.INDEX, this.pageId, (byte) 0);
    }
    if (!this.indexItem.isLoaded()) {
      return null;
    }

    final byte[] data = this.indexItem.getData();
    if (this.indexItem instanceof CacheWorker.WorkItem) {
      try {
        if (data == null) {
          throw new RuntimeException();
        }
        this.index = new PageIndex(data, this.pageCrc, this.pageExpectedWhirlpool);
        if (this.pageVersion != this.index.pageVersion) {
          throw new RuntimeException();
        }
      } catch (final RuntimeException e) {
        this.index = null;
        this.indexItem = this.pageSource.enqueueLoad(ResourceLoader.PageId.INDEX, this.pageId, (byte) 0);
        return null;
      }
    } else {
      try {
        if (data == null) {
          throw new RuntimeException();
        }
        this.index = new PageIndex(data, this.pageCrc, this.pageExpectedWhirlpool);
      } catch (final RuntimeException e) {
        this.pageSource.closeDueToError();
        this.index = null;
        this.indexItem = this.pageSource.enqueueLoad(ResourceLoader.PageId.INDEX, this.pageId, (byte) 0);
        return null;
      }

      if (this.masterIndexCache != null) {
        this.cacheWorker.enqueueWrite(this.masterIndexCache, this.pageId, data);
      }
    }

    this.indexItem = null;
    this.groupStatuses = new GroupStatus[this.index.groupCount];
    return this.index;
  }

  public int percentLoaded(final int groupId) {
    final WorkItem item = this.dataItems.get(groupId);
    return item != null ? item.percentLoaded() : 0;
  }

  public byte@Nullable[] tryGetLoadedData(final int groupId) {
    final WorkItem item = this.tryLoadGroup(LoadMethod.TRY_CACHE_ELSE_FETCH, groupId);
    if (item == null) {
      return null;
    }
    this.dataItems.remove(groupId);
    return item.getData();
  }

  private @Nullable WorkItem tryLoadGroup(final @NotNull LoadMethod how, final int groupId) {
    WorkItem item = this.dataItems.get(groupId);
    if (how == LoadMethod.TRY_CACHE_ELSE_FETCH
        && item instanceof final CacheWorker.WorkItem cacheItem
        && !cacheItem.isTryWithoutWrite
        && !item.isLoaded()) {
      this.dataItems.remove(groupId);
      item = null;
    }

    if (item == null) {
      item = switch (how) {
        case TRY_CACHE_ELSE_FETCH ->
            this.groupStatuses[groupId] == GroupStatus.NOT_IN_CACHE
                ? this.pageSource.enqueueLoad(this.pageId, groupId, (byte) 2)
                : this.cacheWorker.tryRead(this.pageCache, groupId);
        case READ_FROM_CACHE ->
            this.cacheWorker.enqueueRead(this.pageCache, groupId);
      };
      this.dataItems.put(groupId, item);
    }

    if (!item.isLoaded()) {
      return null;
    }

    final byte[] data = item.getData();
    if (item instanceof final CacheWorker.WorkItem cacheItem) {
      try {
        if (data == null || data.length <= 2) {
          throw new RuntimeException();
        }

        CRC_32.reset();
        CRC_32.update(data, 0, data.length - 2);
        if ((int) CRC_32.getValue() != this.index.groupCrcs[groupId]) {
          throw new RuntimeException();
        }
        if (this.index.groupWhirlpoolHashes != null && this.index.groupWhirlpoolHashes[groupId] != null) {
          Whirlpool.checkHash(data, 0, data.length - 2, this.index.groupWhirlpoolHashes[groupId]);
        }
        final int var12 = ((data[data.length - 2] & 255) << 8) + (data[data.length - 1] & 255);
        if (var12 != (this.index._v[groupId] & 0xffff)) {
          throw new RuntimeException();
        }

        if (this.groupStatuses[groupId] != GroupStatus.IN_CACHE) {
          this.groupStatuses[groupId] = GroupStatus.IN_CACHE;
        }
        if (!cacheItem.isTryWithoutWrite) {
          this.dataItems.remove(groupId);
        }
        return cacheItem;
      } catch (final Exception e) {
        this.groupStatuses[groupId] = GroupStatus.NOT_IN_CACHE;
        this.dataItems.remove(groupId);
        if (cacheItem.isTryWithoutWrite) {
          this.dataItems.put(groupId, this.pageSource.enqueueLoad(this.pageId, groupId, (byte) 2));
        }
        return null;
      }
    } else {
      if (data == null || data.length <= 2) {
        throw new RuntimeException();
      }

      CRC_32.reset();
      CRC_32.update(data, 0, data.length - 2);
      if ((int) CRC_32.getValue() != this.index.groupCrcs[groupId]) {
        throw new RuntimeException();
      }
      if (this.index.groupWhirlpoolHashes != null && this.index.groupWhirlpoolHashes[groupId] != null) {
        Whirlpool.checkHash(data, 0, data.length - 2, this.index.groupWhirlpoolHashes[groupId]);
      }

      this.pageSource.failureCount = 0;
      this.pageSource.errorCode = null;
      data[data.length - 2] = (byte) (this.index._v[groupId] >>> 8);
      data[data.length - 1] = (byte) this.index._v[groupId];
      this.cacheWorker.enqueueWrite(this.pageCache, groupId, data);
      if (this.groupStatuses[groupId] != GroupStatus.IN_CACHE) {
        this.groupStatuses[groupId] = GroupStatus.IN_CACHE;
      }

      return item;
    }
  }

  private enum GroupStatus {
    IN_CACHE, NOT_IN_CACHE
  }

  private enum LoadMethod {
    TRY_CACHE_ELSE_FETCH, READ_FROM_CACHE
  }

  public abstract static sealed class WorkItem permits PageSource.WorkItem, CacheWorker.WorkItem {
    private volatile boolean isLoaded = false;

    public abstract byte[] getData();
    public abstract int percentLoaded();

    public final boolean isLoaded() {
      return this.isLoaded;
    }

    public final void setLoaded() {
      this.isLoaded = true;
    }
  }
}
