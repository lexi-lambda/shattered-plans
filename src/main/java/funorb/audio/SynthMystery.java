package funorb.audio;

import funorb.io.Buffer;

public final class SynthMystery {
  public static final int[][] _e = new int[2][8];
  private static final float[][] _f = new float[2][8];
  public static int _g;
  private static float _h;
  public final int[] _d = new int[2];
  private final int[] _b = new int[2];
  private final int[][][] _a = new int[2][2][4];
  private final int[][][] _c = new int[2][2][4];

  private static float a251(final float var0) {
    final float var1 = 32.703197F * (float) Math.pow(2.0D, var0);
    return var1 * 3.1415927F / 11025.0F;
  }

  public void load(final Buffer buf, final SynthEnvelope env) {
    final int var3 = buf.readUByte();

    this._d[0] = var3 >> 4;
    this._d[1] = var3 & 15;

    if (var3 == 0) {
      this._b[1] = 0;
      this._b[0] = 0;
      return;
    }

    this._b[0] = buf.readUShort();
    this._b[1] = buf.readUShort();
    final int var4 = buf.readUByte();

    int var5;
    int var6;
    for (var5 = 0; var5 < 2; ++var5) {
      for (var6 = 0; var6 < this._d[var5]; ++var6) {
        this._a[var5][0][var6] = buf.readUShort();
        this._c[var5][0][var6] = buf.readUShort();
      }
    }

    for (var5 = 0; var5 < 2; ++var5) {
      for (var6 = 0; var6 < this._d[var5]; ++var6) {
        if ((var4 & 1 << var5 * 4 << var6) == 0) {
          this._a[var5][1][var6] = this._a[var5][0][var6];
          this._c[var5][1][var6] = this._c[var5][0][var6];
        } else {
          this._a[var5][1][var6] = buf.readUShort();
          this._c[var5][1][var6] = buf.readUShort();
        }
      }
    }

    if (var4 != 0 || this._b[1] != this._b[0]) {
      env.loadPoints(buf);
    }
  }

  public int a197(final int var1, final float var2) {
    float var3;
    if (var1 == 0) {
      var3 = (float) this._b[0] + (float) (this._b[1] - this._b[0]) * var2;
      var3 *= 0.0030517578F;
      _h = (float) Math.pow(0.1D, var3 / 20.0F);
      _g = (int) (_h * 65536.0F);
    }

    if (this._d[var1] == 0) {
      return 0;
    } else {
      var3 = this.b427(var1, 0, var2);
      _f[var1][0] = -2.0f * var3 * (float) Math.cos(this.a427(var1, 0, var2));
      _f[var1][1] = var3 * var3;

      int var4;
      for (var4 = 1; var4 < this._d[var1]; ++var4) {
        var3 = this.b427(var1, var4, var2);
        final float var5 = -2.0f * var3 * (float) Math.cos(this.a427(var1, var4, var2));
        final float var6 = var3 * var3;
        _f[var1][var4 * 2 + 1] = _f[var1][var4 * 2 - 1] * var6;
        _f[var1][var4 * 2] = _f[var1][var4 * 2 - 1] * var5 + _f[var1][var4 * 2 - 2] * var6;

        for (int var7 = var4 * 2 - 1; var7 >= 2; --var7) {
          final float[] var10000 = _f[var1];
          var10000[var7] += _f[var1][var7 - 1] * var5 + _f[var1][var7 - 2] * var6;
        }

        final float[] var10000 = _f[var1];
        var10000[1] += _f[var1][0] * var5 + var6;
        var10000[0] += var5;
      }

      if (var1 == 0) {
        for (var4 = 0; var4 < this._d[0] * 2; ++var4) {
          final float[] var10000 = _f[0];
          var10000[var4] *= _h;
        }
      }

      for (var4 = 0; var4 < this._d[var1] * 2; ++var4) {
        _e[var1][var4] = (int) (_f[var1][var4] * 65536.0F);
      }

      return this._d[var1] * 2;
    }
  }

  private float a427(final int var1, final int var2, final float var3) {
    float var4 = (float) this._a[var1][0][var2] + var3 * (float) (this._a[var1][1][var2] - this._a[var1][0][var2]);
    var4 *= 1.2207031E-4F;
    return a251(var4);
  }

  private float b427(final int var1, final int var2, final float var3) {
    float var4 = (float) this._c[var1][0][var2] + var3 * (float) (this._c[var1][1][var2] - this._c[var1][0][var2]);
    var4 *= 0.0015258789F;
    return 1.0F - (float) Math.pow(10.0D, -var4 / 20.0F);
  }
}
