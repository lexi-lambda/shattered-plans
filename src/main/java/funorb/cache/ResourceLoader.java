package funorb.cache;

import funorb.io.Buffer;
import funorb.io.ByteContainer;
import funorb.io.DirectByteContainer;
import funorb.shatteredplans.CacheFiles;
import org.intellij.lang.annotations.MagicConstant;

import java.io.IOException;

public final class ResourceLoader {
  public static ResourceLoader COMMON_STRINGS;
  public static ResourceLoader COMMON_SPRITES;
  public static ResourceLoader COMMON_FONTS;
  public static ResourceLoader HUFFMAN_CODES;
  public static ResourceLoader SHATTERED_PLANS_STRINGS_1;
  public static ResourceLoader SHATTERED_PLANS_SPRITES;
  public static ResourceLoader SHATTERED_PLANS_JPEGS;
  public static ResourceLoader SHATTERED_PLANS_FONTS;
  public static ResourceLoader SHATTERED_PLANS_SFX_1;
  public static ResourceLoader SHATTERED_PLANS_SFX_2;
  public static ResourceLoader SHATTERED_PLANS_MUSIC_1;
  public static ResourceLoader SHATTERED_PLANS_MUSIC_2;
  public static ResourceLoader SHATTERED_PLANS_STRINGS_2;
  public static ResourceLoader QUICK_CHAT_DATA;
  public static ResourceLoader JAGEX_LOGO_ANIMATION;

  @SuppressWarnings("WeakerAccess")
  public static final class PageId {
    public static final int COMMON_STRINGS = 0;
    public static final int COMMON_SPRITES = 1;
    public static final int COMMON_FONTS = 2;
    public static final int HUFFMAN_CODES = 3;
    public static final int SHATTERED_PLANS_STRINGS_1 = 4;
    public static final int SHATTERED_PLANS_SPRITES = 5;
    public static final int SHATTERED_PLANS_JPEGS = 6;
    public static final int SHATTERED_PLANS_FONTS = 7;
    public static final int SHATTERED_PLANS_SFX_1 = 8;
    public static final int SHATTERED_PLANS_SFX_2 = 9;
    public static final int SHATTERED_PLANS_MUSIC_1 = 10;
    public static final int SHATTERED_PLANS_MUSIC_2 = 11;
    public static final int SHATTERED_PLANS_STRINGS_2 = 12;
    public static final int QUICK_CHAT_DATA = 13;
    public static final int JAGEX_LOGO_ANIMATION = 14;
    public static final int INDEX = 255;
  }

  public static BufferedCacheFile bufferedCacheDat2;
  public static BufferedCacheFile[] bufferedCacheFiles;

  private final PageLoader pageLoader;
  public boolean releaseGroupsOnGet;
  public boolean releaseItemsOnGet;
  private Object[] loadedGroupData;
  private Object[][] loadedItems;
  private PageIndex index;

  private ResourceLoader(final PageLoader pageLoader, final boolean releaseItemsOnGet) {
    this.releaseItemsOnGet = releaseItemsOnGet;
    this.releaseGroupsOnGet = true;
    this.pageLoader = pageLoader;
  }

  public static ResourceLoader create(final MasterIndexLoader masterIndexLoader,
                                      final CacheFiles cacheFiles,
                                      @MagicConstant(valuesFromClass = PageId.class) final int pageId) {
    return create(cacheFiles, masterIndexLoader, pageId, true);
  }

  public static ResourceLoader create(final CacheFiles cacheFiles,
                                      final MasterIndexLoader masterIndexLoader,
                                      @MagicConstant(valuesFromClass = PageId.class) final int pageId,
                                      final boolean releaseItemsOnGet) {
    try {
      BufferedPageCache masterIndexCache = null;
      if (bufferedCacheDat2 == null) {
        bufferedCacheDat2 = new BufferedCacheFile(cacheFiles.data, 5200);
        masterIndexCache = new BufferedPageCache(PageId.INDEX, bufferedCacheDat2, new BufferedCacheFile(cacheFiles.masterIndex, 12000));
      }

      if (bufferedCacheFiles == null) {
        bufferedCacheFiles = new BufferedCacheFile[cacheFiles.indexes.length];
      }
      if (bufferedCacheFiles[pageId] == null) {
        bufferedCacheFiles[pageId] = new BufferedCacheFile(cacheFiles.indexes[pageId], 12000);
      }
      final BufferedPageCache pageCache = new BufferedPageCache(pageId, bufferedCacheDat2, bufferedCacheFiles[pageId]);

      final PageLoader entry = masterIndexLoader.getPageLoader(pageId, masterIndexCache, pageCache);
      return new ResourceLoader(entry, releaseItemsOnGet);
    } catch (final IOException e) {
      throw new RuntimeException(e);
    }
  }

  //<editor-fold desc="Byte containers">
  private static byte[] asBytes(final Object o) {
    if (o == null) {
      return null;
    } else if (o instanceof final byte[] bs) {
      return bs;
    } else if (o instanceof final ByteContainer bc) {
      return bc.toByteArray();
    } else {
      throw new IllegalArgumentException();
    }
  }

  private static Object questionablyOptimizeBytes(final byte[] bs) {
    if (bs == null) {
      return null;
    } else if (bs.length <= 136) {
      return bs;
    } else {
      final DirectByteContainer bc = new DirectByteContainer();
      bc.put(bs);
      return bc;
    }
  }
  //</editor-fold>

  //<editor-fold desc="Load progress">
  public synchronized boolean isIndexLoaded() {
    if (this.index == null) {
      this.index = this.pageLoader.tryGetOrCreateIndex();
      if (this.index == null) {
        return false;
      }
      this.loadedGroupData = new Object[this.index.groupCount];
      this.loadedItems = new Object[this.index.groupCount][];
    }
    return true;
  }

  public synchronized int percentLoaded() {
    if (!this.isIndexLoaded()) {
      return 0;
    }

    int loadedSoFar = 0;
    int totalNeeded = 0;
    for (int i = 0; i < this.loadedGroupData.length; ++i) {
      if (this.index.itemCounts[i] > 0) {
        loadedSoFar += this.percentLoaded(i);
        totalNeeded += 100;
      }
    }

    if (totalNeeded == 0) {
      return 100;
    } else {
      return loadedSoFar * 100 / totalNeeded;
    }
  }
  public int percentLoaded(final String group) {
    if (this.isIndexLoaded()) {
      return this.percentLoaded(this.index.groupDirectory.lookup(group));
    } else {
      return 0;
    }
  }

  public synchronized int percentLoaded(final int groupId) {
    if (!this.hasGroup(groupId)) {
      return 0;
    } else if (this.loadedGroupData[groupId] == null) {
      return this.pageLoader.percentLoaded(groupId);
    } else {
      return 100;
    }
  }
  //</editor-fold>

  //<editor-fold desc="Querying the directory">
  public int groupCount() {
    return this.isIndexLoaded() ? this.index.groupSizes.length : -1;
  }

  public int itemCount(final int groupId) {
    if (this.hasGroup(groupId)) {
      return this.index.groupSizes[groupId];
    } else {
      return 0;
    }
  }

  @SuppressWarnings("SameParameterValue")
  public int lookupGroup(final String group) {
    if (this.isIndexLoaded()) {
      final int var3 = this.index.groupDirectory.lookup(group);
      return this.hasGroup(var3) ? var3 : -1;
    } else {
      return -1;
    }
  }

  public int lookupItem(final int groupId, final String item) {
    if (!this.hasGroup(groupId)) {
      return -1;
    }
    final int itemId = this.index.itemDirectories[groupId].lookup(item);
    if (!this.hasResource(groupId, itemId)) {
      return -1;
    }
    return itemId;
  }

  @SuppressWarnings({"BooleanMethodIsAlwaysInverted", "SameParameterValue"})
  public boolean hasGroup(final String groupName) {
    return this.isIndexLoaded() && this.index.groupDirectory.lookup(groupName) >= 0;
  }

  private synchronized boolean hasGroup(final int groupId) {
    return this.isIndexLoaded() && groupId >= 0 && groupId < this.index.groupSizes.length && this.index.groupSizes[groupId] != 0;
  }

  private synchronized boolean hasResource(final int groupId, final int itemId) {
    return this.isIndexLoaded() && groupId >= 0 && itemId >= 0 && groupId < this.index.groupSizes.length && itemId < this.index.groupSizes[groupId];
  }

  @SuppressWarnings({"BooleanMethodIsAlwaysInverted", "SameParameterValue"})
  public boolean hasResource(final String group, final String item) {
    if (this.isIndexLoaded()) {
      final int groupId = this.index.groupDirectory.lookup(group);
      if (groupId < 0) {
        return false;
      } else {
        final int itemId = this.index.itemDirectories[groupId].lookup(item);
        return itemId >= 0;
      }
    } else {
      return false;
    }
  }
  //</editor-fold>

  //<editor-fold desc="Loading resources">
  public synchronized boolean loadAllGroups() {
    if (!this.isIndexLoaded()) {
      return false;
    }
    boolean allLoaded = true;
    for (int i = 0; i < this.index.groupIndexes.length; ++i) {
      final int j = this.index.groupIndexes[i];
      if (this.loadedGroupData[j] == null) {
        this.doLoadGroupData(j);
        if (this.loadedGroupData[j] == null) {
          allLoaded = false;
        }
      }
    }
    return allLoaded;
  }

  public boolean loadGroupData(final String group) {
    if (this.isIndexLoaded()) {
      return this.loadGroupData(this.index.groupDirectory.lookup(group));
    } else {
      return false;
    }
  }

  private synchronized boolean loadGroupData(final int groupId) {
    if (!this.hasGroup(groupId)) {
      return false;
    } else if (this.loadedGroupData[groupId] == null) {
      this.doLoadGroupData(groupId);
      return this.loadedGroupData[groupId] != null;
    } else {
      return true;
    }
  }

  @SuppressWarnings("SameParameterValue")
  public void loadGroupDataForItem(final String group, final String item) {
    if (this.isIndexLoaded()) {
      final int groupId = this.index.groupDirectory.lookup(group);
      if (this.hasGroup(groupId)) {
        final int itemId = this.index.itemDirectories[groupId].lookup(item);
        this.loadGroupDataForItem(groupId, itemId);
      }
    }
  }

  public synchronized void loadGroupDataForItem(final int groupId, final int itemId) {
    if (this.hasResource(groupId, itemId) &&
        (this.loadedItems[groupId] == null || this.loadedItems[groupId][itemId] == null) &&
        this.loadedGroupData[groupId] == null) {
      this.doLoadGroupData(groupId);
    }
  }

  public synchronized byte[] getResource(final String group, final String item) {
    if (!this.isIndexLoaded()) {
      return null;
    }
    final int groupId = this.index.groupDirectory.lookup(group);
    if (!this.hasGroup(groupId)) {
      return null;
    }
    final int itemId = this.index.itemDirectories[groupId].lookup(item);
    return this.getResource(groupId, itemId);
  }

  public byte[] getResource(final int groupId, final int itemId) {
    synchronized (this) {
      if (!this.hasResource(groupId, itemId)) {
        return null;
      }

      byte[] data = null;
      if (this.loadedItems[groupId] == null || this.loadedItems[groupId][itemId] == null) {
        if (!this.tryLoadGroupItems(groupId)) {
          this.doLoadGroupData(groupId);
          if (!this.tryLoadGroupItems(groupId)) {
            return null;
          }
        }
      }

      if (this.loadedItems[groupId] == null) {
        throw new RuntimeException("");
      }

      if (this.loadedItems[groupId][itemId] != null) {
        data = asBytes(this.loadedItems[groupId][itemId]);
        if (data == null) {
          throw new RuntimeException("");
        }
      }

      if (data != null) {
        if (this.releaseItemsOnGet) {
          this.loadedItems[groupId][itemId] = null;
          if (this.index.groupSizes[groupId] == 1) {
            this.loadedItems[groupId] = null;
          }
        }
      }

      return data;
    }
  }

  public synchronized byte[] getSingletonResource(final int id) {
    if (!this.isIndexLoaded()) {
      return null;
    } else if (this.index.groupSizes.length == 1) {
      return this.getResource(0, id);
    } else if (this.hasGroup(id)) {
      if (this.index.groupSizes[id] == 1) {
        return this.getResource(id, 0);
      } else {
        throw new RuntimeException();
      }
    } else {
      return null;
    }
  }

  private synchronized void doLoadGroupData(final int groupId) {
    if (this.releaseGroupsOnGet) {
      this.loadedGroupData[groupId] = this.pageLoader.tryGetLoadedData(groupId);
    } else {
      this.loadedGroupData[groupId] = questionablyOptimizeBytes(this.pageLoader.tryGetLoadedData(groupId));
    }
  }

  @SuppressWarnings("BooleanMethodIsAlwaysInverted")
  private synchronized boolean tryLoadGroupItems(final int groupId) {
    if (!this.hasGroup(groupId)) {
      return false;
    }
    if (this.loadedGroupData[groupId] == null) {
      return false;
    }

    final int itemCount = this.index.itemCounts[groupId];
    final int[] itemIdMap = this.index.itemIdMaps[groupId];
    if (this.loadedItems[groupId] == null) {
      this.loadedItems[groupId] = new Object[this.index.groupSizes[groupId]];
    }

    final Object[] loadedItems = this.loadedItems[groupId];
    boolean alreadyLoaded = true;
    for (int i = 0; i < itemCount; ++i) {
      final int itemId;
      if (itemIdMap == null) {
        itemId = i;
      } else {
        itemId = itemIdMap[i];
      }
      if (loadedItems[itemId] == null) {
        alreadyLoaded = false;
        break;
      }
    }

    if (!alreadyLoaded) {
      final byte[] groupData = PageIndex.decompress(asBytes(this.loadedGroupData[groupId]));
      if (this.releaseGroupsOnGet) {
        this.loadedGroupData[groupId] = null;
      }

      if (itemCount > 1) {
        final int lastPos = groupData.length - 1;
        final int itemBlocks = groupData[lastPos] & 255;
        final int lengthsStartPos = lastPos - (4 * itemBlocks * itemCount);
        final Buffer groupBuffer = new Buffer(groupData);
        groupBuffer.pos = lengthsStartPos;
        final int[] itemPosns = new int[itemCount];

        for (int i = 0; i < itemBlocks; ++i) {
          int var16 = 0;
          for (int j = 0; j < itemCount; ++j) {
            var16 += groupBuffer.readInt();
            itemPosns[j] += var16;
          }
        }

        final byte[][] itemData = new byte[itemCount][];

        for (int i = 0; i < itemCount; ++i) {
          itemData[i] = new byte[itemPosns[i]];
          itemPosns[i] = 0;
        }

        groupBuffer.pos = lengthsStartPos;
        int groupPos = 0;
        for (int i = 0; i < itemBlocks; ++i) {
          int len = 0;
          for (int j = 0; j < itemCount; ++j) {
            len += groupBuffer.readInt();
            System.arraycopy(groupData, groupPos, itemData[j], itemPosns[j], len);
            itemPosns[j] += len;
            groupPos += len;
          }
        }

        for (int i = 0; i < itemCount; ++i) {
          final int itemId;
          if (itemIdMap == null) {
            itemId = i;
          } else {
            itemId = itemIdMap[i];
          }

          if (this.releaseItemsOnGet) {
            loadedItems[itemId] = itemData[i];
          } else {
            loadedItems[itemId] = questionablyOptimizeBytes(itemData[i]);
          }
        }
      } else {
        final int itemId;
        if (itemIdMap == null) {
          itemId = 0;
        } else {
          itemId = itemIdMap[0];
        }

        if (this.releaseItemsOnGet) {
          loadedItems[itemId] = groupData;
        } else {
          loadedItems[itemId] = questionablyOptimizeBytes(groupData);
        }
      }
    }
    return true;
  }
  //</editor-fold>
}
