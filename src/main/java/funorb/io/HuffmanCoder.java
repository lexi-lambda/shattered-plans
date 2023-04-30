package funorb.io;

import funorb.Strings;
import funorb.cache.ResourceLoader;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public final class HuffmanCoder {
  public static HuffmanCoder instance;
  private final byte[] codes;
  private final int[] _a;
  private int[] _g;

  public static void initialize(final ResourceLoader loader) {
    assert instance == null;
    instance = new HuffmanCoder(Objects.requireNonNull(loader.getResource("", "huffman")));
  }

  private HuffmanCoder(final byte[] codes) {
    this.codes = codes;
    this._a = new int[codes.length];
    this._g = new int[8];
    final int[] var3 = new int[33];
    int var4 = 0;

    for (int i = 0; i < codes.length; ++i) {
      final byte var6 = codes[i];
      if (var6 != 0) {
        final int var7 = 1 << (32 - var6);
        final int var8 = var3[var6];
        this._a[i] = var8;

        final int var9;
        if ((var8 & var7) == 0) {
          var9 = var8 | var7;

          for (int j = var6 - 1; j >= 1; --j) {
            final int var11 = var3[j];
            if (var8 != var11) {
              break;
            }

            final int var12 = 1 << (32 - j);
            if ((var11 & var12) != 0) {
              var3[j] = var3[j - 1];
              break;
            }

            var3[j] = var12 | var11;
          }
        } else {
          var9 = var3[var6 - 1];
        }
        var3[var6] = var9;

        for (int j = var6 + 1; j <= 32; ++j) {
          if (var8 == var3[j]) {
            var3[j] = var9;
          }
        }

        int var10 = 0;
        for (int j = 0; j < var6; ++j) {
          final int var12 = Integer.MIN_VALUE >>> j;
          if ((var12 & var8) == 0) {
            ++var10;
          } else {
            if (this._g[var10] == 0) {
              this._g[var10] = var4;
            }

            var10 = this._g[var10];
          }

          if (var10 >= this._g.length) {
            final int[] var13 = new int[this._g.length * 2];
            System.arraycopy(this._g, 0, var13, 0, this._g.length);
            this._g = var13;
          }

        }

        if (var10 >= var4) {
          var4 = 1 + var10;
        }
        this._g[var10] = ~i;
      }
    }
  }

  private int decode(final byte[] src, final int srcOffset, final int len, final byte[] dest) {
    if (len == 0) {
      return 0;
    }

    int var6 = 0;
    int var7 = 0;
    int var8 = srcOffset;
    while (true) {
      final byte var9 = src[var8];
      if (var9 >= 0) {
        ++var7;
      } else {
        var7 = this._g[var7];
      }

      int var10;
      if ((var10 = this._g[var7]) < 0) {
        dest[var6++] = (byte) (~var10);
        if (var6 >= len) {
          break;
        }

        var7 = 0;
      }

      if ((64 & var9) == 0) {
        ++var7;
      } else {
        var7 = this._g[var7];
      }

      if ((var10 = this._g[var7]) < 0) {
        dest[var6++] = (byte) (~var10);
        if (len <= var6) {
          break;
        }

        var7 = 0;
      }

      if ((32 & var9) == 0) {
        ++var7;
      } else {
        var7 = this._g[var7];
      }

      if ((var10 = this._g[var7]) < 0) {
        dest[var6++] = (byte) (~var10);
        if (len <= var6) {
          break;
        }

        var7 = 0;
      }

      if ((var9 & 16) == 0) {
        ++var7;
      } else {
        var7 = this._g[var7];
      }

      if ((var10 = this._g[var7]) < 0) {
        dest[var6++] = (byte) (~var10);
        if (var6 >= len) {
          break;
        }

        var7 = 0;
      }

      if ((var9 & 8) == 0) {
        ++var7;
      } else {
        var7 = this._g[var7];
      }

      if ((var10 = this._g[var7]) < 0) {
        dest[var6++] = (byte) (~var10);
        if (var6 >= len) {
          break;
        }

        var7 = 0;
      }

      if ((4 & var9) == 0) {
        ++var7;
      } else {
        var7 = this._g[var7];
      }

      if ((var10 = this._g[var7]) < 0) {
        dest[var6++] = (byte) (~var10);
        if (len <= var6) {
          break;
        }

        var7 = 0;
      }

      if ((2 & var9) == 0) {
        ++var7;
      } else {
        var7 = this._g[var7];
      }

      if ((var10 = this._g[var7]) < 0) {
        dest[var6++] = (byte) (~var10);
        if (len <= var6) {
          break;
        }

        var7 = 0;
      }

      if ((var9 & 1) == 0) {
        ++var7;
      } else {
        var7 = this._g[var7];
      }

      if ((var10 = this._g[var7]) < 0) {
        dest[var6++] = (byte) (~var10);
        if (var6 >= len) {
          break;
        }

        var7 = 0;
      }

      ++var8;
    }

    return 1 + var8 - srcOffset;
  }

  private int encode(final byte[] dest, final int destOffset, final byte[] src, final int len) {
    int var7 = 0;
    int var8 = destOffset << 3;

    for (int srcPos = 0; srcPos < len; ++srcPos) {
      final int byteToEncode = src[srcPos] & 255;
      final int var10 = this._a[byteToEncode];
      final byte var11 = this.codes[byteToEncode];
      if (var11 == 0) {
        throw new RuntimeException(String.valueOf(byteToEncode));
      }

      int destPos = var8 >> 3;
      int var13 = var8 & 0b111;
      var7 &= -var13 >> 31;
      final int var14 = destPos + ((var11 + var13 - 1) >> 3);
      var8 += var11;
      var13 += 24;
      dest[destPos] = (byte) (var7 = var7 | (var10 >>> var13));
      if (destPos < var14) {
        ++destPos;
        var13 -= 8;
        dest[destPos] = (byte) (var7 = var10 >>> var13);
        if (destPos < var14) {
          var13 -= 8;
          ++destPos;
          dest[destPos] = (byte) (var7 = var10 >>> var13);
          if (destPos < var14) {
            var13 -= 8;
            ++destPos;
            dest[destPos] = (byte) (var7 = var10 >>> var13);
            if (destPos < var14) {
              var13 -= 8;
              ++destPos;
              dest[destPos] = (byte) (var7 = var10 << -var13);
            }
          }
        }
      }
    }

    return (var8 + 0b111 >> 3) - destOffset;
  }

  @SuppressWarnings("SameParameterValue")
  public synchronized @NotNull String readEncoded(final @NotNull Buffer buffer, final int maxLen) {
    final int len = Math.min(buffer.readVariable8_16(), maxLen);
    final byte[] data = new byte[len];
    buffer.pos += this.decode(buffer.data, buffer.pos, len, data);
    return Strings.decode1252String(data, 0, len);
  }

  public synchronized @NotNull String readEncoded(final @NotNull Packet buffer, final int len) {
    final int startPos = buffer.readerIndex();
    final int strLen = buffer.readVariable8_16();
    final byte[] encoded = new byte[len - (buffer.readerIndex() - startPos)];
    buffer.readBytes(encoded);
    final byte[] decoded = new byte[strLen];
    this.decode(encoded, 0, strLen, decoded);
    return Strings.decode1252String(decoded);
  }

  @SuppressWarnings("SameParameterValue")
  public synchronized void writeEncoded(final @NotNull CipheredBuffer buffer, final @NotNull String str) {
    final byte[] data = Strings.encode1252String(str);
    buffer.writeVariable8_16(data.length);
    buffer.pos += this.encode(buffer.data, buffer.pos, data, data.length);
  }

  public synchronized void writeEncoded(final @NotNull Packet buffer, final @NotNull String str) {
    final byte[] decoded = Strings.encode1252String(str);
    buffer.writeVariable8_16(decoded.length);
    final byte[] encoded = new byte[256];
    final int len = this.encode(encoded, 0, decoded, decoded.length);
    buffer.writeBytes(encoded, len);
  }
}
