package funorb.audio;

import funorb.io.Buffer;

import java.util.Arrays;
import java.util.Random;

public final class Oscillator {
  private static final int[] NOISE = new int[0x8000];
  private static final int[] SINE = new int[0x8000];
  private static final int[] buf;
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

    buf = new int[220500];
    _q = new int[5];
    _t = new int[5];
    _p = new int[5];
    _w = new int[5];
    _c = new int[5];
  }

  private final int[] _y = new int[]{0, 0, 0, 0, 0};
  private final int[] _x = new int[]{0, 0, 0, 0, 0};
  private final int[] _h = new int[]{0, 0, 0, 0, 0};
  public int delayMs = 0;
  public int durMs = 500;
  private fh_ _k;
  private OscillatorState osc5_;
  private int _f = 0;
  private OscillatorState osc7_;
  private OscillatorState osc4_;
  private OscillatorState osc3_;
  private OscillatorState osc0_;
  private int _b = 100;
  private OscillatorState osc8_;
  private OscillatorState osc6_;
  private OscillatorState osc2_;
  private OscillatorState osc1_;

  public int[] generateS16(final int len, final int lenMs) {
    Arrays.fill(buf, 0, len, 0);

    if (lenMs < 10) {
      return buf;
    }

    final double samplesPerMs = (double) len / ((double) lenMs + 0.0D);

    this.osc0_.reset();
    this.osc1_.reset();
    int var5 = 0;
    int var6 = 0;
    int var7 = 0;
    if (this.osc2_ != null) {
      this.osc2_.reset();
      this.osc3_.reset();
      var5 = (int) ((double) (this.osc2_._i - this.osc2_._d) * 32.768D / samplesPerMs);
      var6 = (int) ((double) this.osc2_._d * 32.768D / samplesPerMs);
    }

    int var8 = 0;
    int var9 = 0;
    int var10 = 0;
    if (this.osc4_ != null) {
      this.osc4_.reset();
      this.osc5_.reset();
      var8 = (int) ((double) (this.osc4_._i - this.osc4_._d) * 32.768D / samplesPerMs);
      var9 = (int) ((double) this.osc4_._d * 32.768D / samplesPerMs);
    }

    for (int i = 0; i < 5; ++i) {
      if (this._y[i] != 0) {
        _c[i] = 0;
        _q[i] = (int) ((double) this._h[i] * samplesPerMs);
        _p[i] = (this._y[i] << 14) / 100;
        _t[i] = (int) ((double) (this.osc0_._i - this.osc0_._d) * 32.768D * Math.pow(1.0057929410678534D, this._x[i]) / samplesPerMs);
        _w[i] = (int) ((double) this.osc0_._d * 32.768D / samplesPerMs);
      }
    }

    for (int i = 0; i < len; ++i) {
      int var12 = this.osc0_.next(len);
      int var13 = this.osc1_.next(len);
      int var14;
      int var15;
      if (this.osc2_ != null) {
        var14 = this.osc2_.next(len);
        var15 = this.osc3_.next(len);
        var12 += this.sample(this.osc2_.waveform, var7, var15) >> 1;
        var7 += (var14 * var5 >> 16) + var6;
      }

      if (this.osc4_ != null) {
        var14 = this.osc4_.next(len);
        var15 = this.osc5_.next(len);
        var13 = var13 * ((this.sample(this.osc4_.waveform, var10, var15) >> 1) + 0x8000) >> 15;
        var10 += (var14 * var8 >> 16) + var9;
      }

      for (var14 = 0; var14 < 5; ++var14) {
        if (this._y[var14] != 0) {
          var15 = i + _q[var14];
          if (var15 < len) {
            buf[var15] += this.sample(this.osc0_.waveform, _c[var14], var13 * _p[var14] >> 15);
            _c[var14] += (var12 * _t[var14] >> 16) + _w[var14];
          }
        }
      }
    }

    if (this.osc6_ != null) {
      this.osc6_.reset();
      this.osc7_.reset();
      int var11 = 0;
      boolean var19 = true;

      for (int i = 0; i < len; ++i) {
        final int var15 = this.osc6_.next(len);
        final int var16 = this.osc7_.next(len);
        final int var12;
        if (var19) {
          var12 = this.osc6_._d + ((this.osc6_._i - this.osc6_._d) * var15 >> 8);
        } else {
          var12 = this.osc6_._d + ((this.osc6_._i - this.osc6_._d) * var16 >> 8);
        }

        var11 += 256;
        if (var11 >= var12) {
          var11 = 0;
          var19 = !var19;
        }

        if (var19) {
          buf[i] = 0;
        }
      }
    }

    if (this._f > 0 && this._b > 0) {
      final int var11 = (int) ((double) this._f * samplesPerMs);
      for (int i = var11; i < len; ++i) {
        buf[i] += buf[i - var11] * this._b / 100;
      }
    }

    if (this._k._d[0] > 0 || this._k._d[1] > 0) {
      this.osc8_.reset();
      int var11 = this.osc8_.next(len + 1);
      int var12 = this._k.a197(0, (float) var11 / 65536.0F);
      int var13 = this._k.a197(1, (float) var11 / 65536.0F);
      if (len >= var12 + var13) {
        int var14 = 0;
        final int var15 = Math.min(var13, len - var12);

        while (var14 < var15) {
          int var16 = (int) ((long) buf[var14 + var12] * (long) fh_._g >> 16);

          for (int var17 = 0; var17 < var12; ++var17) {
            var16 += (int) ((long) buf[var14 + var12 - 1 - var17] * (long) fh_._e[0][var17] >> 16);
          }

          for (int var17 = 0; var17 < var14; ++var17) {
            var16 -= (int) ((long) buf[var14 - 1 - var17] * (long) fh_._e[1][var17] >> 16);
          }

          buf[var14] = var16;
          var11 = this.osc8_.next(len + 1);
          ++var14;
        }

        int var15a = 128;
        while (true) {
          if (var15a > len - var12) {
            var15a = len - var12;
          }

          while (var14 < var15a) {
            int var16 = (int) ((long) buf[var14 + var12] * (long) fh_._g >> 16);

            for (int var17 = 0; var17 < var12; ++var17) {
              var16 += (int) ((long) buf[var14 + var12 - 1 - var17] * (long) fh_._e[0][var17] >> 16);
            }

            for (int var17 = 0; var17 < var13; ++var17) {
              var16 -= (int) ((long) buf[var14 - 1 - var17] * (long) fh_._e[1][var17] >> 16);
            }

            buf[var14] = var16;
            var11 = this.osc8_.next(len + 1);
            ++var14;
          }

          if (var14 >= len - var12) {
            while (var14 < len) {
              int var16 = 0;

              for (int var17 = var14 + var12 - len; var17 < var12; ++var17) {
                var16 += (int) ((long) buf[var14 + var12 - 1 - var17] * (long) fh_._e[0][var17] >> 16);
              }

              for (int var17 = 0; var17 < var13; ++var17) {
                var16 -= (int) ((long) buf[var14 - 1 - var17] * (long) fh_._e[1][var17] >> 16);
              }

              buf[var14] = var16;
              this.osc8_.next(len + 1);
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

    for (int i = 0; i < len; ++i) {
      if (buf[i] < -32768) {
        buf[i] = -32768;
      }
      if (buf[i] > 32767) {
        buf[i] = 32767;
      }
    }
    return buf;
  }

  public void initialize(final Buffer buf) {
    this.osc0_ = new OscillatorState();
    this.osc0_.initialize(buf);
    this.osc1_ = new OscillatorState();
    this.osc1_.initialize(buf);

    final int peek1 = buf.readUByte();
    if (peek1 != 0) {
      --buf.pos;
      this.osc2_ = new OscillatorState();
      this.osc2_.initialize(buf);
      this.osc3_ = new OscillatorState();
      this.osc3_.initialize(buf);
    }

    final int peek2 = buf.readUByte();
    if (peek2 != 0) {
      --buf.pos;
      this.osc4_ = new OscillatorState();
      this.osc4_.initialize(buf);
      this.osc5_ = new OscillatorState();
      this.osc5_.initialize(buf);
    }

    final int peek3 = buf.readUByte();
    if (peek3 != 0) {
      --buf.pos;
      this.osc6_ = new OscillatorState();
      this.osc6_.initialize(buf);
      this.osc7_ = new OscillatorState();
      this.osc7_.initialize(buf);
    }

    for (int i = 0; i < 10; ++i) {
      final int peek4 = buf.readVariable8_16();
      if (peek4 == 0) {
        break;
      }

      this._y[i] = peek4;
      this._x[i] = buf.readBiasedVariable8_16();
      this._h[i] = buf.readVariable8_16();
    }

    this._f = buf.readVariable8_16();
    this._b = buf.readVariable8_16();
    this.durMs = buf.readUShort();
    this.delayMs = buf.readUShort();
    this._k = new fh_();
    this.osc8_ = new OscillatorState();
    this._k.a086(buf, this.osc8_);
  }

  private int sample(final int type, final int phase, final int amplitude) {
    if (type == Waveform.SQUARE) {
      return ((phase & 0x7fff) < 0x4000) ? amplitude : -amplitude;
    } else if (type == Waveform.SINE) {
      return (SINE[phase & 0x7fff] * amplitude) >> 14;
    } else if (type == Waveform.SAWTOOTH) {
      return (((phase & 0x7fff) * amplitude) >> 14) - amplitude;
    } else if (type == Waveform.NOISE) {
      return NOISE[(phase / 2607) & 0x7fff] * amplitude;
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
