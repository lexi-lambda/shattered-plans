package funorb.cache;

import funorb.io.Buffer;
import funorb.util.Whirlpool;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public final class MasterIndexLoader {
  private final @NotNull PageSource pageSource;
  private final @NotNull CacheWorker cacheWorker;

  private PageSource.WorkItem loadRequest;
  private Buffer buffer;
  private PageLoader[] pageLoaders;

  public MasterIndexLoader(final @NotNull PageSource pageSource, final @NotNull CacheWorker cacheWorker) {
    this.pageSource = pageSource;
    this.cacheWorker = cacheWorker;
    this.loadRequest = this.pageSource.enqueueLoad(ResourceLoader.PageId.INDEX, 255, (byte) 0);
  }

  public boolean loadIndex() {
    if (this.buffer != null) {
      return true;
    }
    if (this.loadRequest == null) {
      this.loadRequest = this.pageSource.enqueueLoad(ResourceLoader.PageId.INDEX, 255, (byte) 0);
    }
    if (!this.loadRequest.isLoaded()) {
      return false;
    }

    final Buffer buffer = new Buffer(this.loadRequest.getData());
    buffer.pos = 5;
    final int partCount = buffer.readUByte();
    buffer.pos += partCount * 72;
    final byte[] remoteHash = new byte[buffer.data.length - buffer.pos];
    buffer.readBytes(remoteHash, remoteHash.length);

    if (remoteHash.length != 65) {
      throw new AssertionError();
    }
    Whirlpool.checkHash(buffer.data, 5, buffer.pos - remoteHash.length - 5, remoteHash, 1);

    this.pageLoaders = new PageLoader[partCount];
    this.buffer = buffer;
    return true;
  }

  public PageLoader getPageLoader(final int pageId, final @Nullable BufferedPageCache masterIndexCache, final @NotNull BufferedPageCache pageCache) {
    if (this.buffer == null) {
      throw new IllegalStateException();
    }
    if (pageId < 0 || this.pageLoaders.length <= pageId) {
      throw new RuntimeException();
    }
    if (this.pageLoaders[pageId] == null) {
      this.buffer.pos = 6 + pageId * 72;
      final int crc = this.buffer.readInt();
      final int version = this.buffer.readInt();
      final byte[] whirlpoolHash = new byte[Whirlpool.HASH_BYTES];
      this.buffer.readBytes(whirlpoolHash);
      final PageLoader entry = new PageLoader(pageId, pageCache, masterIndexCache, this.pageSource, this.cacheWorker, crc, whirlpoolHash, version);
      this.pageLoaders[pageId] = entry;
      return entry;
    } else {
      return this.pageLoaders[pageId];
    }
  }

  public void tick() {
    if (this.pageLoaders != null) {
      for (final PageLoader pageLoader : this.pageLoaders) {
        if (pageLoader != null) {
          pageLoader.tryGetOrCreateIndex();
        }
      }
      for (final PageLoader pageLoader : this.pageLoaders) {
        if (pageLoader != null) {
          pageLoader.tick();
        }
      }
    }
  }
}
