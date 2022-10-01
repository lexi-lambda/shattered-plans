package funorb.audio;

import funorb.io.Buffer;

import java.util.Arrays;
import java.util.Random;

public final class pk_ {
  private static final int[] NOISE = new int[0x8000];
  private static final int[] SINE = new int[0x8000];
  private static final int[] _u;
  private static final int[] _p;
  private static final int[] _q;
  private static final int[] _t;
  private static final int[] _c;
  private static final int[] _w;

  static {
    final Random var0 = new Random(0L);
    for (int i = 0; i < 0x8000; ++i) {
      NOISE[i] = (var0.nextInt() & 2) - 1;
    }
    for (int i = 0; i < 0x8000; ++i) {
      SINE[i] = (int) (Math.sin((double) i * Math.PI / 0x4000) * 0x4000);
    }

    _u = new int[220500];
    _q = new int[5];
    _t = new int[5];
    _p = new int[5];
    _w = new int[5];
    _c = new int[5];
  }

  private final int[] _y = new int[]{0, 0, 0, 0, 0};
  private final int[] _x = new int[]{0, 0, 0, 0, 0};
  private final int[] _h = new int[]{0, 0, 0, 0, 0};
  public int _s = 0;
  public int _a = 500;
  private fh_ _k;
  private pn_ _o;
  private int _f = 0;
  private pn_ _r;
  private pn_ _g;
  private pn_ _d;
  private pn_ _m;
  private int _b = 100;
  private pn_ _n;
  private pn_ _e;
  private pn_ _l;
  private pn_ _i;

  public int[] a111(final int var1, final int var2) {
    Arrays.fill(_u, 0, var1, 0);
    if (var2 >= 10) {
      final double var3 = (double) var1 / ((double) var2 + 0.0D);
      this._m.reset();
      this._i.reset();
      int var5 = 0;
      int var6 = 0;
      int var7 = 0;
      if (this._l != null) {
        this._l.reset();
        this._d.reset();
        var5 = (int) ((double) (this._l._i - this._l._d) * 32.768D / var3);
        var6 = (int) ((double) this._l._d * 32.768D / var3);
      }

      int var8 = 0;
      int var9 = 0;
      int var10 = 0;
      if (this._g != null) {
        this._g.reset();
        this._o.reset();
        var8 = (int) ((double) (this._g._i - this._g._d) * 32.768D / var3);
        var9 = (int) ((double) this._g._d * 32.768D / var3);
      }

      for (int i = 0; i < 5; ++i) {
        if (this._y[i] != 0) {
          _c[i] = 0;
          _q[i] = (int) ((double) this._h[i] * var3);
          _p[i] = (this._y[i] << 14) / 100;
          _t[i] = (int) ((double) (this._m._i - this._m._d) * 32.768D * Math.pow(1.0057929410678534D, this._x[i]) / var3);
          _w[i] = (int) ((double) this._m._d * 32.768D / var3);
        }
      }

      for (int i = 0; i < var1; ++i) {
        int var12 = this._m.next(var1);
        int var13 = this._i.next(var1);
        int var14;
        int var15;
        if (this._l != null) {
          var14 = this._l.next(var1);
          var15 = this._d.next(var1);
          var12 += this.sample(this._l.sampleType, var7, var15) >> 1;
          var7 += (var14 * var5 >> 16) + var6;
        }

        if (this._g != null) {
          var14 = this._g.next(var1);
          var15 = this._o.next(var1);
          var13 = var13 * ((this.sample(this._g.sampleType, var10, var15) >> 1) + 0x8000) >> 15;
          var10 += (var14 * var8 >> 16) + var9;
        }

        for (var14 = 0; var14 < 5; ++var14) {
          if (this._y[var14] != 0) {
            var15 = i + _q[var14];
            if (var15 < var1) {
              _u[var15] += this.sample(this._m.sampleType, _c[var14], var13 * _p[var14] >> 15);
              _c[var14] += (var12 * _t[var14] >> 16) + _w[var14];
            }
          }
        }
      }

      if (this._e != null) {
        this._e.reset();
        this._r.reset();
        int var11 = 0;
        boolean var19 = true;

        for (int i = 0; i < var1; ++i) {
          final int var15 = this._e.next(var1);
          final int var16 = this._r.next(var1);
          final int var12;
          if (var19) {
            var12 = this._e._d + ((this._e._i - this._e._d) * var15 >> 8);
          } else {
            var12 = this._e._d + ((this._e._i - this._e._d) * var16 >> 8);
          }

          var11 += 256;
          if (var11 >= var12) {
            var11 = 0;
            var19 = !var19;
          }

          if (var19) {
            _u[i] = 0;
          }
        }
      }

      if (this._f > 0 && this._b > 0) {
        final int var11 = (int) ((double) this._f * var3);
        for (int i = var11; i < var1; ++i) {
          _u[i] += _u[i - var11] * this._b / 100;
        }
      }

      if (this._k._d[0] > 0 || this._k._d[1] > 0) {
        this._n.reset();
        int var11 = this._n.next(var1 + 1);
        int var12 = this._k.a197(0, (float) var11 / 65536.0F);
        int var13 = this._k.a197(1, (float) var11 / 65536.0F);
        if (var1 >= var12 + var13) {
          int var14 = 0;
          final int var15 = Math.min(var13, var1 - var12);

          while (var14 < var15) {
            int var16 = (int) ((long) _u[var14 + var12] * (long) fh_._g >> 16);

            for (int var17 = 0; var17 < var12; ++var17) {
              var16 += (int) ((long) _u[var14 + var12 - 1 - var17] * (long) fh_._e[0][var17] >> 16);
            }

            for (int var17 = 0; var17 < var14; ++var17) {
              var16 -= (int) ((long) _u[var14 - 1 - var17] * (long) fh_._e[1][var17] >> 16);
            }

            _u[var14] = var16;
            var11 = this._n.next(var1 + 1);
            ++var14;
          }

          int var15a = 128;
          while (true) {
            if (var15a > var1 - var12) {
              var15a = var1 - var12;
            }

            while (var14 < var15a) {
              int var16 = (int) ((long) _u[var14 + var12] * (long) fh_._g >> 16);

              for (int var17 = 0; var17 < var12; ++var17) {
                var16 += (int) ((long) _u[var14 + var12 - 1 - var17] * (long) fh_._e[0][var17] >> 16);
              }

              for (int var17 = 0; var17 < var13; ++var17) {
                var16 -= (int) ((long) _u[var14 - 1 - var17] * (long) fh_._e[1][var17] >> 16);
              }

              _u[var14] = var16;
              var11 = this._n.next(var1 + 1);
              ++var14;
            }

            if (var14 >= var1 - var12) {
              while (var14 < var1) {
                int var16 = 0;

                for (int var17 = var14 + var12 - var1; var17 < var12; ++var17) {
                  var16 += (int) ((long) _u[var14 + var12 - 1 - var17] * (long) fh_._e[0][var17] >> 16);
                }

                for (int var17 = 0; var17 < var13; ++var17) {
                  var16 -= (int) ((long) _u[var14 - 1 - var17] * (long) fh_._e[1][var17] >> 16);
                }

                _u[var14] = var16;
                this._n.next(var1 + 1);
                ++var14;
              }
              break;
            }

            var12 = this._k.a197(0, (float) var11 / 65536.0F);
            var13 = this._k.a197(1, (float) var11 / 65536.0F);
            var15a += 128;
          }
        }
      }

      for (int i = 0; i < var1; ++i) {
        if (_u[i] < 0xffff8000) {
          _u[i] = 0xffff8000;
        }
        if (_u[i] > 0x7fff) {
          _u[i] = 0x7fff;
        }
      }
    }
    return _u;
  }

  public void initialize(final Buffer buffer) {
    this._m = new pn_();
    this._m.initialize(buffer);
    this._i = new pn_();
    this._i.initialize(buffer);
    final int var2 = buffer.readUByte();
    if (var2 != 0) {
      --buffer.pos;
      this._l = new pn_();
      this._l.initialize(buffer);
      this._d = new pn_();
      this._d.initialize(buffer);
    }

    final int var2a = buffer.readUByte();
    if (var2a != 0) {
      --buffer.pos;
      this._g = new pn_();
      this._g.initialize(buffer);
      this._o = new pn_();
      this._o.initialize(buffer);
    }

    final int j137 = buffer.readUByte();
    if (j137 != 0) {
      --buffer.pos;
      this._e = new pn_();
      this._e.initialize(buffer);
      this._r = new pn_();
      this._r.initialize(buffer);
    }

    for (int i = 0; i < 10; ++i) {
      final int var4 = buffer.readVariable8_16();
      if (var4 == 0) {
        break;
      }

      this._y[i] = var4;
      this._x[i] = buffer.d410();
      this._h[i] = buffer.readVariable8_16();
    }

    this._f = buffer.readVariable8_16();
    this._b = buffer.readVariable8_16();
    this._a = buffer.readUShort();
    this._s = buffer.readUShort();
    this._k = new fh_();
    this._n = new pn_();
    this._k.a086(buffer, this._n);
  }

  private int sample(final int type, final int time, final int volume) {
    if (type == SampleType.SQUARE) {
      return ((time & 0x7fff) < 0x4000) ? volume : -volume;
    } else if (type == SampleType.SINE) {
      return (SINE[time & 0x7fff] * volume) >> 14;
    } else if (type == SampleType.SAWTOOTH) {
      return (((time & 0x7fff) * volume) >> 14) - volume;
    } else if (type == SampleType.NOISE) {
      return NOISE[(time / 2607) & 0x7fff] * volume;
    } else {
      return 0;
    }
  }

  @SuppressWarnings("WeakerAccess")
  private static final class SampleType {
    public static final int SQUARE = 1;
    public static final int SINE = 2;
    public static final int SAWTOOTH = 3;
    public static final int NOISE = 4;
  }
}
