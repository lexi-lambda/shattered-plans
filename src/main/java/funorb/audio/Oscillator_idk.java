package funorb.audio;

import funorb.io.Buffer;

import java.util.Arrays;
import java.util.Random;

public final class Oscillator_idk {
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
  private OscillatorConfig_idk osc1;
  private int _f = 0;
  private OscillatorConfig_idk osc2;
  private OscillatorConfig_idk osc3;
  private OscillatorConfig_idk osc4;
  private OscillatorConfig_idk osc5;
  private int _b = 100;
  private OscillatorConfig_idk osc6;
  private OscillatorConfig_idk osc7;
  private OscillatorConfig_idk osc8;
  private OscillatorConfig_idk osc9;

  public int[] a111(final int var1, final int var2) {
    Arrays.fill(_u, 0, var1, 0);
    if (var2 >= 10) {
      final double var3 = (double) var1 / ((double) var2 + 0.0D);
      this.osc5.reset();
      this.osc9.reset();
      int var5 = 0;
      int var6 = 0;
      int var7 = 0;
      if (this.osc8 != null) {
        this.osc8.reset();
        this.osc4.reset();
        var5 = (int) ((double) (this.osc8._i - this.osc8._d) * 32.768D / var3);
        var6 = (int) ((double) this.osc8._d * 32.768D / var3);
      }

      int var8 = 0;
      int var9 = 0;
      int var10 = 0;
      if (this.osc3 != null) {
        this.osc3.reset();
        this.osc1.reset();
        var8 = (int) ((double) (this.osc3._i - this.osc3._d) * 32.768D / var3);
        var9 = (int) ((double) this.osc3._d * 32.768D / var3);
      }

      for (int i = 0; i < 5; ++i) {
        if (this._y[i] != 0) {
          _c[i] = 0;
          _q[i] = (int) ((double) this._h[i] * var3);
          _p[i] = (this._y[i] << 14) / 100;
          _t[i] = (int) ((double) (this.osc5._i - this.osc5._d) * 32.768D * Math.pow(1.0057929410678534D, this._x[i]) / var3);
          _w[i] = (int) ((double) this.osc5._d * 32.768D / var3);
        }
      }

      for (int i = 0; i < var1; ++i) {
        int var12 = this.osc5.next(var1);
        int var13 = this.osc9.next(var1);
        int var14;
        int var15;
        if (this.osc8 != null) {
          var14 = this.osc8.next(var1);
          var15 = this.osc4.next(var1);
          var12 += this.sample(this.osc8.waveform, var7, var15) >> 1;
          var7 += (var14 * var5 >> 16) + var6;
        }

        if (this.osc3 != null) {
          var14 = this.osc3.next(var1);
          var15 = this.osc1.next(var1);
          var13 = var13 * ((this.sample(this.osc3.waveform, var10, var15) >> 1) + 0x8000) >> 15;
          var10 += (var14 * var8 >> 16) + var9;
        }

        for (var14 = 0; var14 < 5; ++var14) {
          if (this._y[var14] != 0) {
            var15 = i + _q[var14];
            if (var15 < var1) {
              _u[var15] += this.sample(this.osc5.waveform, _c[var14], var13 * _p[var14] >> 15);
              _c[var14] += (var12 * _t[var14] >> 16) + _w[var14];
            }
          }
        }
      }

      if (this.osc7 != null) {
        this.osc7.reset();
        this.osc2.reset();
        int var11 = 0;
        boolean var19 = true;

        for (int i = 0; i < var1; ++i) {
          final int var15 = this.osc7.next(var1);
          final int var16 = this.osc2.next(var1);
          final int var12;
          if (var19) {
            var12 = this.osc7._d + ((this.osc7._i - this.osc7._d) * var15 >> 8);
          } else {
            var12 = this.osc7._d + ((this.osc7._i - this.osc7._d) * var16 >> 8);
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
        this.osc6.reset();
        int var11 = this.osc6.next(var1 + 1);
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
            var11 = this.osc6.next(var1 + 1);
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
              var11 = this.osc6.next(var1 + 1);
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
                this.osc6.next(var1 + 1);
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
    this.osc5 = new OscillatorConfig_idk();
    this.osc5.initialize(buffer);
    this.osc9 = new OscillatorConfig_idk();
    this.osc9.initialize(buffer);
    final int var2 = buffer.readUByte();
    if (var2 != 0) {
      --buffer.pos;
      this.osc8 = new OscillatorConfig_idk();
      this.osc8.initialize(buffer);
      this.osc4 = new OscillatorConfig_idk();
      this.osc4.initialize(buffer);
    }

    final int var2a = buffer.readUByte();
    if (var2a != 0) {
      --buffer.pos;
      this.osc3 = new OscillatorConfig_idk();
      this.osc3.initialize(buffer);
      this.osc1 = new OscillatorConfig_idk();
      this.osc1.initialize(buffer);
    }

    final int j137 = buffer.readUByte();
    if (j137 != 0) {
      --buffer.pos;
      this.osc7 = new OscillatorConfig_idk();
      this.osc7.initialize(buffer);
      this.osc2 = new OscillatorConfig_idk();
      this.osc2.initialize(buffer);
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
    this.osc6 = new OscillatorConfig_idk();
    this._k.a086(buffer, this.osc6);
  }

  private int sample(final int type, final int phase, final int volume) {
    if (type == Waveform.SQUARE) {
      return ((phase & 0x7fff) < 0x4000) ? volume : -volume;
    } else if (type == Waveform.SINE) {
      return (SINE[phase & 0x7fff] * volume) >> 14;
    } else if (type == Waveform.SAWTOOTH) {
      return (((phase & 0x7fff) * volume) >> 14) - volume;
    } else if (type == Waveform.NOISE) {
      return NOISE[(phase / 2607) & 0x7fff] * volume;
    } else {
      return 0;
    }
  }

  @SuppressWarnings("WeakerAccess")
  private static final class Waveform {
    public static final int SQUARE = 1;
    public static final int SINE = 2;
    public static final int SAWTOOTH = 3;
    public static final int NOISE = 4;
  }
}
