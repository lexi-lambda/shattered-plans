package funorb.client.intro;

public final class sr_ {
  public byte _o = 0;
  public short[] _P;
  public short[] _g;
  public short[] _M;
  public short[] _b;
  public short[] _C;
  public short[] _w;
  public short[] _s;
  public short[] _v;
  public short[] _y;
  public short _x;
  public short[] _I;
  public short[] _z;
  public byte[] _p;
  public short[] _B;
  public short _u;
  public short[] _i;
  public int _k;
  public int _E;
  public short[] _f;
  public int _t;
  public int _j;
  public int _r;
  public short _e;
  public short[] _L;
  public short[] _J;
  public short[] _n;
  public short[] _G;
  public int _O;
  private boolean _d = false;

  private void a150() {
    this._d = false;
  }

  public void a115(final int var1, final int var2, final int var3) {
    for (int var5 = 0; this._x > var5; ++var5) {
      final short[] var10000 = this._w;
      var10000[var5] = (short) (var10000[var5] + var2);
      final short[] z = this._z;
      z[var5] = (short) (z[var5] + var3);
      final short[] f = this._f;
      f[var5] = (short) (f[var5] + var1);
    }

    this.a150();
  }

  public void a487() {
    if (!this._d) {
      this._d = true;
      short var2 = 32767;
      short var3 = 32767;
      short var4 = 32767;
      short var5 = -32768;
      short var6 = -32768;
      short var7 = -32768;

      for (int var8 = 0; var8 < this._x; ++var8) {
        final short var9 = this._w[var8];
        final short var10 = this._z[var8];
        if (var2 > var9) {
          var2 = var9;
        }

        if (var9 > var5) {
          var5 = var9;
        }

        if (var6 < var10) {
          var6 = var10;
        }

        if (var10 < var3) {
          var3 = var10;
        }

        final short var11 = this._f[var8];
        if (var4 > var11) {
          var4 = var11;
        }

        if (var7 < var11) {
          var7 = var11;
        }
      }

      this._k = var2;
      this._t = var6;
      this._j = var3;
      this._E = var4;
      this._O = var5;
      this._r = var7;
    }
  }

  public void a050() {
    for (int var6 = 0; var6 < this._x; ++var6) {
      this._w[var6] = (short) (6 * this._w[var6]);
      this._z[var6] = (short) (this._z[var6] * 6);
      this._f[var6] = (short) (this._f[var6] * 6);
    }

    this.a150();

  }
}
