package funorb.shatteredplans;

import funorb.cache.CacheFile;

import java.io.Closeable;
import java.io.IOException;

public final class CacheFiles implements Closeable {
  public final CacheFile random;
  public final CacheFile data;
  public final CacheFile masterIndex;
  public final CacheFile[] indexes;

  public CacheFiles() {
    try {
      this.random = new CacheFile(CacheFile.path("random.dat", null), 25L);
      this.data = new CacheFile(CacheFile.path("main_file_cache.dat2"), 0x12c00000L);
      this.masterIndex = new CacheFile(CacheFile.path("main_file_cache.idx255"), 0x100000L);
      this.indexes = new CacheFile[15];
      for (int i = 0; i < this.indexes.length; ++i) {
        //noinspection resource
        this.indexes[i] = new CacheFile(CacheFile.path("main_file_cache.idx" + i), 0x100000L);
      }
    } catch (final IOException e) {
      throw new RuntimeException(e);
    }
  }


  @Override
  public void close() throws IOException {
    this.random.close();
    this.data.close();
    this.masterIndex.close();
    for (final CacheFile index : this.indexes) {
      index.close();
    }
  }
}
