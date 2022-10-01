package funorb.util;

import java.util.Arrays;
import java.util.stream.IntStream;

public final class Whirlpool {
  public static final int HASH_BYTES = 64;

  private static final long[][] _kel = new long[8][256];
  private static final long[] _kek = new long[11];
  static {
    final String data = "\u1823\uC6E8\u87B8\u014F\u36A6\uD2F5\u796F\u9152\u60BC\u9B8E\uA30C\u7B35\u1DE0\uD7C2\u2E4B\uFE57\u1577\u37E5\u9FF0\u4ADA\u58C9\u290A\uB1A0\u6B85\uBD5D\u10F4\uCB3E\u0567\ue427\u418B\uA77D\u95D8\uFBEE\u7C66\udd17\u479E\uCA2D\uBF07\uAD5A\u8333\u6302\uAA71\uC819\u49D9\uf2e3\u5B88\u9A26\u32B0\ue90f\uD580\uBECD\u3448\uFF7A\u905F\u2068\u1aae\uB454\u9322\u64F1\u7312\u4008\uC3EC\udba1\u8D3D\u9700\uCF2B\u7682\uD61B\uB5AF\u6A50\u45F3\u30EF\u3F55\uA2EA\u65BA\u2FC0\ude1c\ufd4d\u9275\u068A\uB2E6\u0E1F\u62D4\uA896\uF9C5\u2559\u8472\u394C\u5E78\u388C\uD1A5\ue261\uB321\u9C1E\u43C7\uFC04\u5199\u6D0D\ufadf\u7E24\u3BAB\uCE11\u8F4E\uB7EB\u3C81\u94F7\uB913\u2CD3\ue76e\uC403\u5644\u7FA9\u2ABB\uC153\udc0b\u9D6C\u3174\uf646\uAC89\u14E1\u163A\u6909\u70B6\uD0ED\uCC42\u98A4\u285C\uf886";
    for (int i = 0; i < 256; ++i) {
      final char c = data.charAt(i / 2);
      final long b = (i & 1) != 0 ? (long) (c & 0xff) : (long) (c >>> 8);
      long var4 = b << 1;
      if (var4 >= 0x100L) {
        var4 ^= 285L;
      }

      long var6 = var4 << 1;
      if (var6 >= 0x100L) {
        var6 ^= 285L;
      }

      final long var8 = b ^ var6;
      long var10 = var6 << 1;
      if (var10 >= 256L) {
        var10 ^= 285L;
      }

      final long var12 = var10 ^ b;
      _kel[0][i] = var12 | (var8 << 16 | (var10 << 24 | (b << 32 | (b << 56 | b << 48 | var6 << 40))) | var4 << 8);

      for (int var14 = 1; var14 < 8; ++var14) {
        _kel[var14][i] = _kel[var14 - 1][i] >>> 8 | _kel[var14 - 1][i] << 56;
      }
    }

    _kek[0] = 0L;
    for (int i = 1; i < 11; ++i) {
      final int var15 = (i - 1) * 8;
      _kek[i] = (_kel[0][var15] & 0xff00000000000000L)
          ^ (_kel[1][var15 + 1] & 0xff000000000000L)
          ^ (_kel[2][var15 + 2] & 0xff0000000000L)
          ^ (_kel[3][var15 + 3] & 0xff00000000L)
          ^ (_kel[4][var15 + 4] & 0xff000000L)
          ^ (_kel[5][var15 + 5] & 0xff0000L)
          ^ (_kel[6][var15 + 6] & 0xff00L)
          ^ (_kel[7][var15 + 7] & 0xffL);
    }
  }

  private final long[] _c = new long[8];
  private final byte[] _a = new byte[64];
  private final long[] _f = new long[8];
  private final long[] _e = new long[8];
  private final long[] _h = new long[8];
  private final long[] _m = new long[8];
  private final byte[] _k = new byte[32];
  private int _d = 0;
  private int _o = 0;

  private Whirlpool() {}

  @SuppressWarnings("WeakerAccess")
  public static byte[] hash(final byte[] data, final int offset, final int len) {
    final byte[] data2;
    if (offset <= 0) {
      data2 = data;
    } else {
      data2 = new byte[len];
      System.arraycopy(data, offset, data2, 0, len);
    }

    final Whirlpool var7 = new Whirlpool();
    var7.initialize();
    var7.a140(data2, len * 8L);
    final byte[] result = new byte[HASH_BYTES];
    var7.a251(result);
    return result;
  }

  private static boolean hashMatches(final byte[] data, final int dataOffset, final int dataLen, final byte[] expectedHash, final int expectedHashOffset) {
    final byte[] hash = hash(data, dataOffset, dataLen);
    return IntStream.range(0, HASH_BYTES).allMatch(i -> hash[i] == expectedHash[i + expectedHashOffset]);
  }

  @SuppressWarnings("SameParameterValue")
  public static void checkHash(final byte[] data, final int offset, final int len, final byte[] expectedHash) {
    assert expectedHash.length == HASH_BYTES;
    checkHash(data, offset, len, expectedHash, 0);
  }

  public static void checkHash(final byte[] data, final int dataOffset, final int dataLen, final byte[] expectedHash, final int expectedHashOffset) {
    if (!hashMatches(data, dataOffset, dataLen, expectedHash, expectedHashOffset)) {
      throw new RuntimeException("whirlpool hash mismatch");
    }
  }

  private void initialize() {
    this._o = 0;
    this._d = 0;
    this._a[0] = 0;
    Arrays.fill(this._k, (byte) 0);
    Arrays.fill(this._c, 0L);
  }

  private void a140(final byte[] data, long var2) {
    int var5 = 0;
    final int var6 = 8 - ((int) var2 & 7) & 7;
    final int var7 = this._d & 7;
    long var9 = var2;

    int var12 = 0;
    for (int i = 31; i >= 0; --i) {
      var12 += (this._k[i] & 0xff) + ((int) var9 & 0xff);
      this._k[i] = (byte) var12;
      var9 >>>= 8;
      var12 >>>= 8;
    }

    int var8;
    while (var2 > 8L) {
      var8 = 255 & data[var5] << var6 | (data[1 + var5] & 255) >>> -var6 + 8;

      this._a[this._o] = (byte) ((int) this._a[this._o] | var8 >>> var7);
      ++this._o;
      this._d += 8 - var7;
      if (this._d == 512) {
        this.a423();
        this._o = 0;
        this._d = 0;
      }

      final int var1 = var8 << -var7 + 8;
      this._a[this._o] = (byte) (255 & var1);
      var2 -= 8L;
      this._d += var7;
      ++var5;
    }

    if (var2 > 0L) {
      var8 = data[var5] << var6 & 255;
      this._a[this._o] = (byte) ((int) this._a[this._o] | var8 >>> var7);
    } else {
      var8 = 0;
    }

    if ((long) var7 + var2 >= 8L) {
      var2 -= -var7 + 8;
      ++this._o;
      this._d += 8 - var7;
      if (this._d == 512) {
        this.a423();
        this._o = 0;
        this._d = 0;
      }

      final int var0 = var8 << -var7 + 8;
      this._a[this._o] = (byte) (var0 & 255);
      this._d += (int) var2;
    } else {
      this._d = (int) ((long) this._d + var2);
    }
  }

  private void a251(final byte[] var3) {
    this._a[this._o] = (byte) ((int) this._a[this._o] | 128 >>> (7 & this._d));
    ++this._o;
    if (this._o > 32) {
      while (true) {
        if (this._o >= 64) {
          this.a423();
          this._o = 0;
          break;
        }

        this._a[this._o++] = 0;
      }
    }

    while (this._o < 32) {
      this._a[this._o++] = 0;
    }

    System.arraycopy(this._k, 0, this._a, 32, 32);
    this.a423();
    int var4 = 0;

    for (int i = 0; var4 < 8; i += 8) {
      final long var7 = this._c[var4];
      var3[i] = (byte) ((int) (var7 >>> 56));
      var3[i + 1] = (byte) ((int) (var7 >>> 48));
      var3[i + 2] = (byte) ((int) (var7 >>> 40));
      var3[i + 3] = (byte) ((int) (var7 >>> 32));
      var3[i + 4] = (byte) ((int) (var7 >>> 24));
      var3[i + 5] = (byte) ((int) (var7 >>> 16));
      var3[i + 6] = (byte) ((int) (var7 >>> 8));
      var3[i + 7] = (byte) ((int) var7);
      ++var4;
    }

  }

  private void a423() {
    int var2 = 0;

    int var3;
    for (var3 = 0; var2 < 8; var3 += 8) {
      this._m[var2] = (long) this._a[5 + var3] << 16 & 16711680L ^ (((long) this._a[var3 + 3] & 255L) << 32 ^ (((long) this._a[1 + var3] & 255L) << 48 ^ (long) this._a[var3] << 56 ^ (long) this._a[2 + var3] << 40 & 280375465082880L) ^ 4278190080L & (long) this._a[4 + var3] << 24) ^ ((long) this._a[6 + var3] & 255L) << 8 ^ 255L & (long) this._a[7 + var3];
      ++var2;
    }

    for (var2 = 0; var2 < 8; ++var2) {
      final long var21 = this._e[var2] = this._c[var2];
      this._f[var2] = this._m[var2] ^ var21;
    }

    for (var2 = 1; var2 <= 10; ++var2) {
      int var4;
      int var5;
      for (var3 = 0; var3 < 8; ++var3) {
        this._h[var3] = 0L;
        var4 = 0;

        for (var5 = 56; var4 < 8; ++var4) {
          final int var1 = -var4 + var3;
          final long var21 = _kel[var4][255 & (int) (this._e[7 & var1] >>> var5)];
          this._h[var3] = this._h[var3] ^ var21;
          var5 -= 8;
        }
      }

      for (var3 = 0; var3 < 8; ++var3) {
        this._e[var3] = this._h[var3];
      }

      this._e[0] = this._e[0] ^ _kek[var2];

      for (var3 = 0; var3 < 8; ++var3) {
        this._h[var3] = this._e[var3];
        var4 = 0;

        for (var5 = 56; var4 < 8; ++var4) {
          this._h[var3] = this._h[var3] ^ _kel[var4][(int) (this._f[var3 - var4 & 7] >>> var5) & 255];
          var5 -= 8;
        }
      }

      for (var3 = 0; var3 < 8; ++var3) {
        this._f[var3] = this._h[var3];
      }
    }

    int i = 0;

    while (i < 8) {
      this._c[i] = this._c[i] ^ (this._f[i] ^ this._m[i]);
      ++i;
    }
  }
}
