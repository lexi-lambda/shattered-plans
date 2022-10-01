package funorb.cache;

import funorb.shatteredplans.client.MessagePumpThread;

import java.io.Closeable;
import java.io.EOFException;
import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public final class CacheFile implements Closeable {
  private static final Path BASE_DIRECTORY = Paths.get(System.getProperty("user.home"), ".alterorb");
  private static final Path CACHES_DIRECTORY = BASE_DIRECTORY.resolve("caches");

  private final long maxSize;
  private long pos;
  private RandomAccessFile file;

  public CacheFile(final File file, long maxSize) throws IOException {
    if (maxSize == -1L) {
      maxSize = Long.MAX_VALUE;
    }

    if (maxSize < file.length()) {
      //noinspection ResultOfMethodCallIgnored
      file.delete();
    }

    this.file = new RandomAccessFile(file, "rw");
    this.maxSize = maxSize;
    this.pos = 0L;
    final int var5 = this.file.read();
    if (var5 != -1) {
      this.file.seek(0L);
      this.file.write(var5);
    }

    this.file.seek(0L);
  }

  public static File path(final String var0, final String var2) {
    try {
      final var path = cacheFilePath(var2, var0);
      if (!Files.exists(path)) {
        Files.createDirectories(path.getParent());
      }
      return path.toFile();
    } catch (final IOException e) {
      throw new RuntimeException(e);
    }
  }

  public static File path(final String var0) {
    return path(var0, MessagePumpThread.GAME_ID);
  }

  private static Path cacheFilePath(final String subDirectory, final String file) {
    if (subDirectory != null) {
      return CACHES_DIRECTORY.resolve(subDirectory).resolve(file);
    }
    return CACHES_DIRECTORY.resolve(file);
  }

  public int read(final byte[] buffer, final int offset, final int len) throws IOException {
    final int bytesRead = this.file.read(buffer, offset, len);
    if (bytesRead > 0) {
      this.pos += bytesRead;
    }
    return bytesRead;
  }

  public long length() throws IOException {
    return this.file.length();
  }

  @Override
  public void close() throws IOException {
    if (this.file != null) {
      this.file.close();
      this.file = null;
    }
  }

  @Override
  protected void finalize() throws Throwable {
    if (this.file != null) {
      System.out.println();
      this.close();
    }
  }

  public void seek(final long pos) throws IOException {
    this.file.seek(pos);
    this.pos = pos;
  }

  public void write(final byte[] data, final int offset, final int len) throws IOException {
    if (this.pos + (long) len <= this.maxSize) {
      this.file.write(data, offset, len);
      this.pos += len;
    } else {
      this.file.seek(this.maxSize);
      this.file.write(1);
      throw new EOFException();
    }
  }
}
