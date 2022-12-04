package funorb.audio;

import funorb.util.BitMath;

public final class vb_ {
  public final int _a;
  private final int _c;
  private final int[] _b;
  private float[][] _d;
  private int[] _f;

  public vb_() {
    SomeBufferReader_idk.readBits(24);
    this._a = SomeBufferReader_idk.readBits(16);
    this._c = SomeBufferReader_idk.readBits(24);
    this._b = new int[this._c];
    final boolean var1 = SomeBufferReader_idk.readBit() != 0;
    int var2;
    int var3;
    int var5;
    if (var1) {
      var2 = 0;

      for (var3 = SomeBufferReader_idk.readBits(5) + 1; var2 < this._c; ++var3) {
        final int var4 = SomeBufferReader_idk.readBits(BitMath.lastSet(this._c - var2));

        for (var5 = 0; var5 < var4; ++var5) {
          this._b[var2++] = var3;
        }
      }
    } else {
      final boolean var14 = SomeBufferReader_idk.readBit() != 0;

      for (var3 = 0; var3 < this._c; ++var3) {
        if (var14 && SomeBufferReader_idk.readBit() == 0) {
          this._b[var3] = 0;
        } else {
          this._b[var3] = SomeBufferReader_idk.readBits(5) + 1;
        }
      }
    }

    this.b797();
    var2 = SomeBufferReader_idk.readBits(4);
    if (var2 > 0) {
      final float var15 = SomeBufferReader_idk.parseAsFloat(SomeBufferReader_idk.readBits(32));
      final float var16 = SomeBufferReader_idk.parseAsFloat(SomeBufferReader_idk.readBits(32));
      var5 = SomeBufferReader_idk.readBits(4) + 1;
      final boolean var6 = SomeBufferReader_idk.readBit() != 0;
      final int var7;
      if (var2 == 1) {
        var7 = a080(this._c, this._a);
      } else {
        var7 = this._c * this._a;
      }

      final int[] _e = new int[var7];

      int var8;
      for (var8 = 0; var8 < var7; ++var8) {
        _e[var8] = SomeBufferReader_idk.readBits(var5);
      }

      this._d = new float[this._c][this._a];
      float var9;
      int var10;
      int var11;
      if (var2 == 1) {
        for (var8 = 0; var8 < this._c; ++var8) {
          var9 = 0.0F;
          var10 = 1;

          for (var11 = 0; var11 < this._a; ++var11) {
            final int var12 = var8 / var10 % var7;
            final float var13 = (float) _e[var12] * var16 + var15 + var9;
            this._d[var8][var11] = var13;
            if (var6) {
              var9 = var13;
            }

            var10 *= var7;
          }
        }
      } else {
        for (var8 = 0; var8 < this._c; ++var8) {
          var9 = 0.0F;
          var10 = var8 * this._a;

          for (var11 = 0; var11 < this._a; ++var11) {
            final float var17 = (float) _e[var10] * var16 + var15 + var9;
            this._d[var8][var11] = var17;
            if (var6) {
              var9 = var17;
            }

            ++var10;
          }
        }
      }
    }

  }

  private static int a080(final int var0, final int var1) {
    int var2 = (int) Math.pow(var0, 1.0D / (double) var1) + 1;
    while (a776em(var2, var1) > var0) {
      --var2;
    }
    return var2;
  }

  private static int a776em(int var0, int var1) {
    int var2;
    for (var2 = 1; var1 > 1; var0 *= var0) {
      if ((var1 & 1) != 0) {
        var2 *= var0;
      }

      var1 >>= 1;
    }

    if (var1 == 1) {
      return var2 * var0;
    } else {
      return var2;
    }
  }

  public float[] c932() {
    return this._d[this.a784()];
  }

  public int a784() {
    int var1 = 0;
    while (this._f[var1] >= 0) {
      var1 = SomeBufferReader_idk.readBit() != 0 ? this._f[var1] : var1 + 1;
    }
    return ~this._f[var1];
  }

  private void b797() {
    final int[] var1 = new int[this._c];
    final int[] var2 = new int[33];

    int var3;
    int var4;
    int var5;
    int var6;
    int var7;
    int var8;
    int var10;
    for (var3 = 0; var3 < this._c; ++var3) {
      var4 = this._b[var3];
      if (var4 != 0) {
        var5 = 1 << 32 - var4;
        var6 = var2[var4];
        var1[var3] = var6;
        int var9;
        if ((var6 & var5) == 0) {
          var7 = var6 | var5;

          for (var8 = var4 - 1; var8 >= 1; --var8) {
            var9 = var2[var8];
            if (var9 != var6) {
              break;
            }

            var10 = 1 << 32 - var8;
            if ((var9 & var10) != 0) {
              var2[var8] = var2[var8 - 1];
              break;
            }

            var2[var8] = var9 | var10;
          }
        } else {
          var7 = var2[var4 - 1];
        }

        var2[var4] = var7;

        for (var8 = var4 + 1; var8 <= 32; ++var8) {
          var9 = var2[var8];
          if (var9 == var6) {
            var2[var8] = var7;
          }
        }
      }
    }

    this._f = new int[8];
    int var11 = 0;

    for (var3 = 0; var3 < this._c; ++var3) {
      var4 = this._b[var3];
      if (var4 != 0) {
        var5 = var1[var3];
        var6 = 0;

        for (var7 = 0; var7 < var4; ++var7) {
          var8 = Integer.MIN_VALUE >>> var7;
          if ((var5 & var8) == 0) {
            ++var6;
          } else {
            if (this._f[var6] == 0) {
              this._f[var6] = var11;
            }

            var6 = this._f[var6];
          }

          if (var6 >= this._f.length) {
            final int[] var12 = new int[this._f.length * 2];

            for (var10 = 0; var10 < this._f.length; ++var10) {
              var12[var10] = this._f[var10];
            }

            this._f = var12;
          }

        }

        this._f[var6] = ~var3;
        if (var6 >= var11) {
          var11 = var6 + 1;
        }
      }
    }

  }
}
