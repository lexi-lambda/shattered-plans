package funorb.audio;

import funorb.cache.ResourceLoader;
import funorb.io.Buffer;
import funorb.util.BitMath;

import java.io.IOException;
import java.util.stream.IntStream;

public final class FmtVorbis {
  public static vb_[] _L;
  private static float[] _k;
  private static to_[] _o;
  private static int[] _J;
  private static int[] _D;
  private static fq_[] _O;
  private static float[] _u;
  private static float[] _C;
  private static boolean _x = false;
  private static byte[] buffer;
  private static float[] _z;
  private static boolean[] _t;
  private static float[] _y;
  private static float[] _j;
  private static int _r;
  private static float[] _l;
  private static int curBit;
  private static int[] _F;
  private static int _E;
  private static kn_[] _h;
  private static int curByte;
  private int sampleLength;
  private int loopEnd;
  private boolean isLooped;
  private int loopStart;
  private int _i;
  private int _s;
  private boolean _A;
  private byte[][] sections;
  private int _M;
  private int sampleRate;
  private byte[] sampleData;
  private float[] _n;
  private int sectionIndex;

  private FmtVorbis(final byte[] data) throws IOException {
    this.load(data);
  }

  public static float parseAsFloat(final int value) {
    int significand = value & 0x1fffff;
    final int sign = value & Integer.MIN_VALUE;
    final int exponent = (value & 0x7fe00000) >> 21;
    if (sign != 0) {
      significand = -significand;
    }
    return (float) ((double) significand * Math.pow(2.0D, exponent - 788));
  }

  public static int readBit() {
    final int bit = buffer[curByte] >> curBit & 1;
    ++curBit;
    curByte += curBit >> 3;
    curBit &= 7;
    return bit;
  }

  public static void b604(final byte[] section) {
    setBuffer(section);
    _E = 1 << readBits(4);
    _r = 1 << readBits(4);
    _k = new float[_r];

    int var1;
    int var2;
    int var3;
    int var4;
    int var5;
    for (var1 = 0; var1 < 2; ++var1) {
      var2 = var1 != 0 ? _r : _E;
      var3 = var2 >> 1;
      var4 = var2 >> 2;
      var5 = var2 >> 3;
      final float[] var6 = new float[var3];

      for (int var7 = 0; var7 < var4; ++var7) {
        var6[2 * var7] = (float) Math.cos((double) (4 * var7) * Math.PI / (double) var2);
        var6[2 * var7 + 1] = -((float) Math.sin((double) (4 * var7) * Math.PI / (double) var2));
      }

      final float[] var13 = new float[var3];

      for (int var8 = 0; var8 < var4; ++var8) {
        var13[2 * var8] = (float) Math.cos((double) (2 * var8 + 1) * Math.PI / (double) (2 * var2));
        var13[2 * var8 + 1] = (float) Math.sin((double) (2 * var8 + 1) * Math.PI / (double) (2 * var2));
      }

      final float[] var14 = new float[var4];

      for (int var9 = 0; var9 < var5; ++var9) {
        var14[2 * var9] = (float) Math.cos((double) (4 * var9 + 2) * Math.PI / (double) var2);
        var14[2 * var9 + 1] = -((float) Math.sin((double) (4 * var9 + 2) * Math.PI / (double) var2));
      }

      final int var10 = BitMath.lastSet(var5 - 1);
      final int[] var15 = IntStream.range(0, var5).map(var11 -> reverseBits(var11, var10)).toArray();

      if (var1 == 0) {
        _l = var6;
        _z = var13;
        _C = var14;
        _D = var15;
      } else {
        _j = var6;
        _y = var13;
        _u = var14;
        _F = var15;
      }
    }

    var1 = readBits(8) + 1;
    _L = new vb_[var1];

    for (var2 = 0; var2 < var1; ++var2) {
      _L[var2] = new vb_();
    }

    var2 = readBits(6) + 1;

    for (var3 = 0; var3 < var2; ++var3) {
      readBits(16);
    }

    var2 = readBits(6) + 1;
    _h = new kn_[var2];

    for (var3 = 0; var3 < var2; ++var3) {
      _h[var3] = new kn_();
    }

    var3 = readBits(6) + 1;
    _o = new to_[var3];

    for (var4 = 0; var4 < var3; ++var4) {
      _o[var4] = new to_();
    }

    var4 = readBits(6) + 1;
    _O = new fq_[var4];

    for (var5 = 0; var5 < var4; ++var5) {
      _O[var5] = new fq_();
    }

    var5 = readBits(6) + 1;
    _t = new boolean[var5];
    _J = new int[var5];

    for (int var12 = 0; var12 < var5; ++var12) {
      _t[var12] = readBit() != 0;
      readBits(16);
      readBits(16);
      _J[var12] = readBits(8);
    }

    _x = true;
  }

  @SuppressWarnings("SameParameterValue")
  public static FmtVorbis a968(final ResourceLoader loader, final String group, final String item) {
    if (a521(loader)) {
      final byte[] var3 = loader.getResource(group, item);
      if (var3 == null) {
        return null;
      } else {
        FmtVorbis var4 = null;

        try {
          var4 = new FmtVorbis(var3);
        } catch (final IOException var6) {
          var6.printStackTrace();
        }

        return var4;
      }
    } else {
      loader.loadGroupDataForItem(group, item);
      return null;
    }
  }

  private static boolean a521(final ResourceLoader loader) {
    if (!_x) {
      final byte[] data = loader.getResource(0, 0);
      if (data == null) {
        return false;
      }

      b604(data);
    }

    return true;
  }

  private static void setBuffer(final byte[] section) {
    buffer = section;
    curByte = 0;
    curBit = 0;
  }

  public static int readBits(int numBits) {
    int result = 0;

    int bitsUsed;
    int bitsLeft;
    for (bitsUsed = 0; 8 - curBit <= numBits; numBits -= bitsLeft) {
      bitsLeft = 8 - curBit;
      final int mask = (1 << bitsLeft) - 1;
      result += (buffer[curByte] >> curBit & mask) << bitsUsed;
      curBit = 0;
      ++curByte;
      bitsUsed += bitsLeft;
    }

    if (numBits > 0) {
      bitsLeft = (1 << numBits) - 1;
      result += (buffer[curByte] >> curBit & bitsLeft) << bitsUsed;
      curBit += numBits;
    }

    return result;
  }

  public static FmtVorbis load(final ResourceLoader loader, final int groupId, final int itemId) {
    if (a521(loader)) {
      final byte[] var3 = loader.getResource(groupId, itemId);
      if (var3 == null) {
        return null;
      } else {
        FmtVorbis var4 = null;

        try {
          var4 = new FmtVorbis(var3);
        } catch (final IOException var6) {
          var6.printStackTrace();
        }

        return var4;
      }
    } else {
      loader.loadGroupDataForItem(groupId, itemId);
      return null;
    }
  }

  private static int reverseBits(int value, int count) {
    int i = 0;
    for (; count > 0; --count) {
      i = (i << 1) | (1 & value);
      value >>>= 1;
    }
    return i;
  }

  public RawSampleS8 toRawSample() {
    if (this.sampleData == null) {
      this._M = 0;
      this._n = new float[_r];
      this.sampleData = new byte[this.sampleLength];
      this._s = 0;
      this.sectionIndex = 0;
    }

    for (; this.sectionIndex < this.sections.length; ++this.sectionIndex) {

      final float[] var2 = this.e875(this.sectionIndex);
      if (var2 != null) {
        int var3 = this._s;
        int var4 = var2.length;
        if (var4 > this.sampleLength - var3) {
          var4 = this.sampleLength - var3;
        }

        for (int var5 = 0; var5 < var4; ++var5) {
          int var6 = (int) (128.0F + var2[var5] * 128.0F);
          if ((var6 & -256) != 0) {
            var6 = ~var6 >> 31;
          }

          this.sampleData[var3++] = (byte) (var6 - 128);
        }

        this._s = var3;
      }
    }

    this._n = null;
    final byte[] sampleData = this.sampleData;
    this.sampleData = null;
    return new RawSampleS8(this.sampleRate, sampleData, this.loopStart, this.loopEnd, this.isLooped);
  }

  private float[] e875(final int sectionIndex) {
    setBuffer(this.sections[sectionIndex]);
    readBit();
    final int var2 = readBits(BitMath.lastSet(_J.length - 1));
    final boolean var3 = _t[var2];
    final int var4 = var3 ? _r : _E;
    boolean var5 = false;
    boolean var6 = false;
    if (var3) {
      var5 = readBit() != 0;
      var6 = readBit() != 0;
    }

    final int var7 = var4 >> 1;
    final int var8;
    final int var9;
    final int var10;
    if (var3 && !var5) {
      var8 = (var4 >> 2) - (_E >> 2);
      var9 = (var4 >> 2) + (_E >> 2);
      var10 = _E >> 1;
    } else {
      var8 = 0;
      var9 = var7;
      var10 = var4 >> 1;
    }

    final int var11;
    final int var12;
    final int var13;
    if (var3 && !var6) {
      var11 = var4 - (var4 >> 2) - (_E >> 2);
      var12 = var4 - (var4 >> 2) + (_E >> 2);
      var13 = _E >> 1;
    } else {
      var11 = var7;
      var12 = var4;
      var13 = var4 >> 1;
    }

    final fq_ var14 = _O[_J[var2]];
    final int var16 = var14._d;
    int var17 = var14._b[var16];
    final boolean var15 = !_h[var17].b801();

    for (var17 = 0; var17 < var14._c; ++var17) {
      final to_ var18 = _o[var14._a[var17]];
      final float[] var19 = _k;
      var18.a623(var19, var4 >> 1, var15);
    }

    int var41;
    if (!var15) {
      var17 = var14._d;
      var41 = var14._b[var17];
      _h[var41].a331(_k, var4 >> 1);
    }

    int var42;
    if (var15) {
      for (var17 = var4 >> 1; var17 < var4; ++var17) {
        _k[var17] = 0.0F;
      }
    } else {
      final int i = var4 >> 1;
      var41 = var4 >> 2;
      var42 = var4 >> 3;
      final float[] var20 = _k;

      int var21;
      for (var21 = 0; var21 < i; ++var21) {
        var20[var21] *= 0.5F;
      }

      for (var21 = i; var21 < var4; ++var21) {
        var20[var21] = -var20[var4 - var21 - 1];
      }

      final float[] var46 = var3 ? _j : _l;
      final float[] var22 = var3 ? _y : _z;
      final float[] var23 = var3 ? _u : _C;
      final int[] var24 = var3 ? _F : _D;

      int var25;
      float var26;
      float var27;
      float var28;
      float var29;
      for (var25 = 0; var25 < var41; ++var25) {
        var26 = var20[4 * var25] - var20[var4 - 4 * var25 - 1];
        var27 = var20[4 * var25 + 2] - var20[var4 - 4 * var25 - 3];
        var28 = var46[2 * var25];
        var29 = var46[2 * var25 + 1];
        var20[var4 - 4 * var25 - 1] = var26 * var28 - var27 * var29;
        var20[var4 - 4 * var25 - 3] = var26 * var29 + var27 * var28;
      }

      float var30;
      float var31;
      for (var25 = 0; var25 < var42; ++var25) {
        var26 = var20[i + 3 + 4 * var25];
        var27 = var20[i + 1 + 4 * var25];
        var28 = var20[4 * var25 + 3];
        var29 = var20[4 * var25 + 1];
        var20[i + 3 + 4 * var25] = var26 + var28;
        var20[i + 1 + 4 * var25] = var27 + var29;
        var30 = var46[i - 4 - 4 * var25];
        var31 = var46[i - 3 - 4 * var25];
        var20[4 * var25 + 3] = (var26 - var28) * var30 - (var27 - var29) * var31;
        var20[4 * var25 + 1] = (var27 - var29) * var30 + (var26 - var28) * var31;
      }

      var25 = BitMath.lastSet(var4 - 1);

      int var47;
      int var48;
      int var49;
      int var50;
      for (var47 = 0; var47 < var25 - 3; ++var47) {
        var48 = var4 >> var47 + 2;
        var49 = 8 << var47;

        for (var50 = 0; var50 < 2 << var47; ++var50) {
          final int var51 = var4 - var48 * 2 * var50;
          final int var52 = var4 - var48 * (2 * var50 + 1);

          for (int var32 = 0; var32 < var4 >> var47 + 4; ++var32) {
            final int var33 = 4 * var32;
            final float var34 = var20[var51 - 1 - var33];
            final float var35 = var20[var51 - 3 - var33];
            final float var36 = var20[var52 - 1 - var33];
            final float var37 = var20[var52 - 3 - var33];
            var20[var51 - 1 - var33] = var34 + var36;
            var20[var51 - 3 - var33] = var35 + var37;
            final float var38 = var46[var32 * var49];
            final float var39 = var46[var32 * var49 + 1];
            var20[var52 - 1 - var33] = (var34 - var36) * var38 - (var35 - var37) * var39;
            var20[var52 - 3 - var33] = (var35 - var37) * var38 + (var34 - var36) * var39;
          }
        }
      }

      for (var47 = 1; var47 < var42 - 1; ++var47) {
        var48 = var24[var47];
        if (var47 < var48) {
          var49 = 8 * var47;
          var50 = 8 * var48;
          var30 = var20[var49 + 1];
          var20[var49 + 1] = var20[var50 + 1];
          var20[var50 + 1] = var30;
          var30 = var20[var49 + 3];
          var20[var49 + 3] = var20[var50 + 3];
          var20[var50 + 3] = var30;
          var30 = var20[var49 + 5];
          var20[var49 + 5] = var20[var50 + 5];
          var20[var50 + 5] = var30;
          var30 = var20[var49 + 7];
          var20[var49 + 7] = var20[var50 + 7];
          var20[var50 + 7] = var30;
        }
      }

      for (var47 = 0; var47 < i; ++var47) {
        var20[var47] = var20[2 * var47 + 1];
      }

      for (var47 = 0; var47 < var42; ++var47) {
        var20[var4 - 1 - 2 * var47] = var20[4 * var47];
        var20[var4 - 2 - 2 * var47] = var20[4 * var47 + 1];
        var20[var4 - var41 - 1 - 2 * var47] = var20[4 * var47 + 2];
        var20[var4 - var41 - 2 - 2 * var47] = var20[4 * var47 + 3];
      }

      for (var47 = 0; var47 < var42; ++var47) {
        var27 = var23[2 * var47];
        var28 = var23[2 * var47 + 1];
        var29 = var20[i + 2 * var47];
        var30 = var20[i + 2 * var47 + 1];
        var31 = var20[var4 - 2 - 2 * var47];
        final float var53 = var20[var4 - 1 - 2 * var47];
        final float var54 = var28 * (var29 - var31) + var27 * (var30 + var53);
        var20[i + 2 * var47] = (var29 + var31 + var54) * 0.5F;
        var20[var4 - 2 - 2 * var47] = (var29 + var31 - var54) * 0.5F;
        final float v = var28 * (var30 + var53) - var27 * (var29 - var31);
        var20[i + 2 * var47 + 1] = (var30 - var53 + v) * 0.5F;
        var20[var4 - 1 - 2 * var47] = (-var30 + var53 + v) * 0.5F;
      }

      for (var47 = 0; var47 < var41; ++var47) {
        var20[var47] = var20[2 * var47 + i] * var22[2 * var47] + var20[2 * var47 + 1 + i] * var22[2 * var47 + 1];
        var20[i - 1 - var47] = var20[2 * var47 + i] * var22[2 * var47 + 1] - var20[2 * var47 + 1 + i] * var22[2 * var47];
      }

      for (var47 = 0; var47 < var41; ++var47) {
        var20[var4 - var41 + var47] = -var20[var47];
      }

      for (var47 = 0; var47 < var41; ++var47) {
        var20[var47] = var20[var41 + var47];
      }

      for (var47 = 0; var47 < var41; ++var47) {
        var20[var41 + var47] = -var20[var41 - var47 - 1];
      }

      for (var47 = 0; var47 < var41; ++var47) {
        var20[i + var47] = var20[var4 - var47 - 1];
      }

      float[] var10000;
      for (var47 = var8; var47 < var9; ++var47) {
        var27 = (float) Math.sin(((double) (var47 - var8) + 0.5D) / (double) var10 * 0.5D * Math.PI);
        var10000 = _k;
        var10000[var47] *= (float) Math.sin(1.5707963267948966D * (double) var27 * (double) var27);
      }

      for (var47 = var11; var47 < var12; ++var47) {
        var27 = (float) Math.sin(((double) (var47 - var11) + 0.5D) / (double) var13 * 0.5D * Math.PI + (Math.PI / 2));
        var10000 = _k;
        var10000[var47] *= (float) Math.sin(1.5707963267948966D * (double) var27 * (double) var27);
      }
    }

    float[] var43 = null;
    if (this._M > 0) {
      var41 = this._M + var4 >> 2;
      var43 = new float[var41];
      int var45;
      if (!this._A) {
        for (var42 = 0; var42 < this._i; ++var42) {
          var45 = (this._M >> 1) + var42;
          var43[var42] += this._n[var45];
        }
      }

      if (!var15) {
        for (var42 = var8; var42 < var4 >> 1; ++var42) {
          var45 = var43.length - (var4 >> 1) + var42;
          var43[var45] += _k[var42];
        }
      }
    }

    final float[] var44 = this._n;
    this._n = _k;
    _k = var44;
    this._M = var4;
    this._i = var12 - (var4 >> 1);
    this._A = var15;
    return var43;
  }

  private void load(final byte[] data) throws IOException {
    final Buffer buf = new Buffer(data);
    this.sampleRate = buf.readInt();
    this.sampleLength = buf.readInt();
    this.loopStart = buf.readInt();
    this.loopEnd = buf.readInt();
    if (this.loopEnd < 0) {
      this.loopEnd = ~this.loopEnd;
      this.isLooped = true;
    }

    final int count = buf.readInt();
    if (count < 0) {
      throw new IOException();
    } else {
      this.sections = new byte[count][];

      for (int i = 0; i < count; ++i) {
        int total = 0;

        int x;
        do {
          x = buf.readUByte();
          total += x;
        } while (x >= 255);

        final byte[] section = new byte[total];
        buf.readBytes(section, total);
        this.sections[i] = section;
      }

    }
  }

  public void b720() {
    this._M = 0;
    this._n = new float[_r];
    for (int i = 0; i < this.sections.length; ++i) {
      this.e875(i);
    }
  }
}
