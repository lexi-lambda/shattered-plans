package funorb.audio;

import funorb.io.Buffer;

import java.util.Arrays;
import java.util.Random;

public final class Synth {
  private static final double _120TH_ROOT_OF_2 = 1.0057929410678534D;

  private static final int[] NOISE = new int[0x8000];
  private static final int[] SINE = new int[0x8000];
  private static final int[] buf;
  private static final int[] harmVolScaled_idk;
  private static final int[] harmDelScaled_idk;
  private static final int[] harmFreq_idk;
  private static final int[] harmPhase_idk;
  private static final int[] harmFreqMin_idk;

  static {
    final Random rng = new Random(0L);
    for (int i = 0; i < 0x8000; ++i) {
      NOISE[i] = (rng.nextInt() & 2) - 1;
    }
    for (int i = 0; i < 0x8000; ++i) {
      SINE[i] = (int) (Math.sin((double) i * Math.PI / 0x4000) * 0x4000);
    }

    buf = new int[220500];
    harmDelScaled_idk = new int[5];
    harmFreq_idk = new int[5];
    harmVolScaled_idk = new int[5];
    harmFreqMin_idk = new int[5];
    harmPhase_idk = new int[5];
  }

  private final int[] harmVol_idk = new int[]{0, 0, 0, 0, 0};
  private final int[] harmSemis_idk = new int[]{0, 0, 0, 0, 0};
  private final int[] harmDel_idk = new int[]{0, 0, 0, 0, 0};
  public int posMs = 0;
  public int lengthMs = 500;
  private SynthMystery _k;
  private int echoTime = 0;
  private SynthEnvelope envGapOn;
  private SynthEnvelope envBaseFreq;
  private SynthEnvelope envBaseAmp;
  private SynthEnvelope envFmRate;
  private SynthEnvelope envFmRange;
  private SynthEnvelope envAmRate;
  private SynthEnvelope envAmRange;
  private int echoAmount = 100;
  private SynthEnvelope osc8_;
  private SynthEnvelope envGapOff;

  public int[] generateS16(final int len, final int lenMs) {
    Arrays.fill(buf, 0, len, 0);

    if (lenMs < 10) {
      return buf;
    }

    final double samplesPerMs = (double) len / ((double) lenMs + 0.0D);

    this.envBaseFreq.reset();
    this.envBaseAmp.reset();
    int osc2RangeScaled = 0;
    int osc2MinScaled = 0;
    int oscFmPhase = 0;
    if (this.envFmRate != null) {
      this.envFmRate.reset();
      this.envFmRange.reset();
      osc2RangeScaled = (int) ((double) (this.envFmRate.max - this.envFmRate.min) * 32.768D / samplesPerMs);
      osc2MinScaled = (int) ((double) this.envFmRate.min * 32.768D / samplesPerMs);
    }

    int osc4RangeScaled = 0;
    int osc4MinScaled = 0;
    int oscAmpPhase = 0;
    if (this.envAmRate != null) {
      this.envAmRate.reset();
      this.envAmRange.reset();
      osc4RangeScaled = (int) ((double) (this.envAmRate.max - this.envAmRate.min) * 32.768D / samplesPerMs);
      osc4MinScaled = (int) ((double) this.envAmRate.min * 32.768D / samplesPerMs);
    }

    for (int harm_idk = 0; harm_idk < 5; ++harm_idk) {
      if (this.harmVol_idk[harm_idk] != 0) {
        harmPhase_idk[harm_idk] = 0;
        harmDelScaled_idk[harm_idk] = (int) ((double) this.harmDel_idk[harm_idk] * samplesPerMs);
        harmVolScaled_idk[harm_idk] = (this.harmVol_idk[harm_idk] << 14) / 100;
        harmFreq_idk[harm_idk] = (int) ((double) (this.envBaseFreq.max - this.envBaseFreq.min) * 32.768D *
          Math.pow(_120TH_ROOT_OF_2, this.harmSemis_idk[harm_idk]) / samplesPerMs);
        harmFreqMin_idk[harm_idk] = (int) ((double) this.envBaseFreq.min * 32.768D / samplesPerMs);
      }
    }

    for (int i = 0; i < len; ++i) {
      int baseFreq = this.envBaseFreq.next(len);
      int baseAmp = this.envBaseAmp.next(len);
      int rate;
      int range;
      if (this.envFmRate != null) {
        rate = this.envFmRate.next(len);
        range = this.envFmRange.next(len);
        baseFreq += this.sample(this.envFmRate.waveform, oscFmPhase, range) >> 1;
        oscFmPhase += (rate * osc2RangeScaled >> 16) + osc2MinScaled;
      }

      if (this.envAmRate != null) {
        rate = this.envAmRate.next(len);
        range = this.envAmRange.next(len);
        baseAmp = baseAmp * ((this.sample(this.envAmRate.waveform, oscAmpPhase, range) >> 1) + 0x8000) >> 15;
        oscAmpPhase += (rate * osc4RangeScaled >> 16) + osc4MinScaled;
      }

      for (int harm_idk = 0; harm_idk < 5; ++harm_idk) {
        if (this.harmVol_idk[harm_idk] != 0) {
          int index = i + harmDelScaled_idk[harm_idk];
          if (index < len) {
            buf[index] += this.sample(
              this.envBaseFreq.waveform,
              harmPhase_idk[harm_idk],
              baseAmp * harmVolScaled_idk[harm_idk] >> 15
            );
            harmPhase_idk[harm_idk] += (baseFreq * harmFreq_idk[harm_idk] >> 16) + harmFreqMin_idk[harm_idk];
          }
        }
      }
    }

    if (this.envGapOff != null) {
      this.envGapOff.reset();
      this.envGapOn.reset();
      int gapAccum = 0;
      boolean gapOn = true;

      for (int i = 0; i < len; ++i) {
        final int gapOffThresh = this.envGapOff.next(len);
        final int gapOnThresh = this.envGapOn.next(len);
        final int gapThresh;
        if (gapOn) {
          gapThresh = this.envGapOff.min + ((this.envGapOff.max - this.envGapOff.min) * gapOffThresh >> 8);
        } else {
          gapThresh = this.envGapOff.min + ((this.envGapOff.max - this.envGapOff.min) * gapOnThresh >> 8);
        }

        gapAccum += 256;
        if (gapAccum >= gapThresh) {
          gapAccum = 0;
          gapOn = !gapOn;
        }

        if (gapOn) {
          buf[i] = 0;
        }
      }
    }

    if (this.echoTime > 0 && this.echoAmount > 0) {
      final int delay = (int) ((double) this.echoTime * samplesPerMs);
      for (int i = delay; i < len; ++i) {
        buf[i] += buf[i - delay] * this.echoAmount / 100;
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
          int var16 = (int) ((long) buf[var14 + var12] * (long) SynthMystery.someAmp >> 16);

          for (int var17 = 0; var17 < var12; ++var17) {
            var16 += (int) ((long) buf[var14 + var12 - 1 - var17] * (long) SynthMystery.someAmps[0][var17] >> 16);
          }

          for (int var17 = 0; var17 < var14; ++var17) {
            var16 -= (int) ((long) buf[var14 - 1 - var17] * (long) SynthMystery.someAmps[1][var17] >> 16);
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
            int var16 = (int) ((long) buf[var14 + var12] * (long) SynthMystery.someAmp >> 16);

            for (int var17 = 0; var17 < var12; ++var17) {
              var16 += (int) ((long) buf[var14 + var12 - 1 - var17] * (long) SynthMystery.someAmps[0][var17] >> 16);
            }

            for (int var17 = 0; var17 < var13; ++var17) {
              var16 -= (int) ((long) buf[var14 - 1 - var17] * (long) SynthMystery.someAmps[1][var17] >> 16);
            }

            buf[var14] = var16;
            var11 = this.osc8_.next(len + 1);
            ++var14;
          }

          if (var14 >= len - var12) {
            while (var14 < len) {
              int var16 = 0;

              for (int var17 = var14 + var12 - len; var17 < var12; ++var17) {
                var16 += (int) ((long) buf[var14 + var12 - 1 - var17] * (long) SynthMystery.someAmps[0][var17] >> 16);
              }

              for (int var17 = 0; var17 < var13; ++var17) {
                var16 -= (int) ((long) buf[var14 - 1 - var17] * (long) SynthMystery.someAmps[1][var17] >> 16);
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
    this.envBaseFreq = new SynthEnvelope();
    this.envBaseFreq.load(buf);
    this.envBaseAmp = new SynthEnvelope();
    this.envBaseAmp.load(buf);

    final int peek1 = buf.readUByte();
    if (peek1 != 0) {
      --buf.pos;
      this.envFmRate = new SynthEnvelope();
      this.envFmRate.load(buf);
      this.envFmRange = new SynthEnvelope();
      this.envFmRange.load(buf);
    }

    final int peek2 = buf.readUByte();
    if (peek2 != 0) {
      --buf.pos;
      this.envAmRate = new SynthEnvelope();
      this.envAmRate.load(buf);
      this.envAmRange = new SynthEnvelope();
      this.envAmRange.load(buf);
    }

    final int peek3 = buf.readUByte();
    if (peek3 != 0) {
      --buf.pos;
      this.envGapOff = new SynthEnvelope();
      this.envGapOff.load(buf);
      this.envGapOn = new SynthEnvelope();
      this.envGapOn.load(buf);
    }

    for (int i = 0; i < 10; ++i) {
      final int peek4 = buf.readVariable8_16();
      if (peek4 == 0) {
        break;
      }

      this.harmVol_idk[i] = peek4;
      this.harmSemis_idk[i] = buf.readBiasedVariable8_16();
      this.harmDel_idk[i] = buf.readVariable8_16();
    }

    this.echoTime = buf.readVariable8_16();
    this.echoAmount = buf.readVariable8_16();
    this.lengthMs = buf.readUShort();
    this.posMs = buf.readUShort();
    this._k = new SynthMystery();
    this.osc8_ = new SynthEnvelope();
    this._k.load(buf, this.osc8_);
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
