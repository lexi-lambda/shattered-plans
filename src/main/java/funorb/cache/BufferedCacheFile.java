package funorb.cache;

import java.io.EOFException;
import java.io.IOException;

public final class BufferedCacheFile {
  private final CacheFile cacheFile;
  private final byte[] buffer;
  private int _a;
  private long physicalPos = 0L;
  private long _h;
  private long logicalPos = 0L;
  private long _l;
  private long _i = -1L;

  public BufferedCacheFile(final CacheFile cacheFile, final int bufferSize) throws IOException {
    this.cacheFile = cacheFile;
    this._h = this._l = cacheFile.length();
    this.buffer = new byte[bufferSize];
  }

  public void write(final byte[] data, final int offset, final int len) throws IOException {
    try {
      if ((long) len + this.logicalPos > this._h) {
        this._h = this.logicalPos + (long) len;
      }

      if (len > 0) {
        if (this.physicalPos != this.logicalPos) {
          this.cacheFile.seek(this.logicalPos);
          this.physicalPos = this.logicalPos;
        }

        this.cacheFile.write(data, offset, len);
        this.physicalPos += len;
        if (this.physicalPos > this._l) {
          this._l = this.physicalPos;
        }

        final long var6;
        if (this.logicalPos >= this._i && this.logicalPos < this._i + (long) this._a) {
          var6 = this.logicalPos;
        } else if (this._i >= this.logicalPos && this._i < this.logicalPos + (long) len) {
          var6 = this._i;
        } else {
          var6 = -1;
        }

        final long var8;
        if (this.logicalPos + (long) len > this._i && (long) this._a + this._i >= this.logicalPos + (long) len) {
          var8 = (long) len + this.logicalPos;
        } else if (this._i + (long) this._a > this.logicalPos && this.logicalPos + (long) len >= (long) this._a + this._i) {
          var8 = (long) this._a + this._i;
        } else {
          var8 = -1;
        }

        if (var6 > -1L && var6 < var8) {
          final int var10 = (int) (var8 - var6);
          System.arraycopy(data, (int) (var6 + (long) offset - this.logicalPos), this.buffer, (int) (var6 - this._i), var10);
        }

        this.logicalPos += len;
      }
    } catch (final IOException e) {
      this.physicalPos = -1L;
      throw e;
    }
  }

  private void pageIn() throws IOException {
    if (this.logicalPos != this.physicalPos) {
      this.cacheFile.seek(this.logicalPos);
      this.physicalPos = this.logicalPos;
    }
    this._i = this.logicalPos;

    for (this._a = 0; this._a < this.buffer.length;) {
      final int len = Math.min(this.buffer.length - this._a, 200000000);
      final int bytesRead = this.cacheFile.read(this.buffer, this._a, len);
      if (bytesRead == -1) {
        break;
      }
      
      this._a += bytesRead;
      this.physicalPos += bytesRead;
    }
  }

  public void read(final byte[] dest, final int len) throws IOException {
    int lenLeft = len;
    try {
      if (dest.length < len) {
        throw new ArrayIndexOutOfBoundsException(len - 1);
      }

      int var4 = 0;
      if (this._i <= this.logicalPos && (long) this._a + this._i > this.logicalPos) {
        final int var9 = Math.min((int) ((long) this._a - this.logicalPos + this._i), lenLeft);
        System.arraycopy(this.buffer, (int) (this.logicalPos - this._i), dest, var4, var9);
        this.logicalPos += var9;
        lenLeft -= var9;
        var4 += var9;
      }

      if (lenLeft <= this.buffer.length) {
        if (lenLeft > 0) {
          this.pageIn();
          final int var10 = Math.min(lenLeft, this._a);
          System.arraycopy(this.buffer, 0, dest, var4, var10);
          this.logicalPos += var10;
          lenLeft -= var10;
        }
      } else {
        this.cacheFile.seek(this.logicalPos);
        int var10;
        for (this.physicalPos = this.logicalPos; lenLeft > 0; var4 += var10) {
          var10 = this.cacheFile.read(dest, var4, lenLeft);
          if (var10 == -1) {
            break;
          }

          lenLeft -= var10;
          this.physicalPos += var10;
          this.logicalPos += var10;
        }
      }

    } catch (final IOException e) {
      this.physicalPos = -1L;
      throw e;
    }

    if (lenLeft > 0) {
      throw new EOFException();
    }
  }

  public void read(final byte[] dest) throws IOException {
    this.read(dest, dest.length);
  }

  public long length() {
    return this._h;
  }

  public void seek(final long pos) throws IOException {
    if (pos >= 0L) {
      this.logicalPos = pos;
    } else {
      throw new IOException();
    }
  }

  public void close() throws IOException {
    this.cacheFile.close();
  }
}
