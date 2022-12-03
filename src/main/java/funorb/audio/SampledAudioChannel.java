package funorb.audio;

import funorb.client.JagexBaseApplet;
import funorb.util.BitMath;
import funorb.util.PseudoMonotonicClock;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine.Info;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;
import java.io.Closeable;
import java.util.Arrays;

public final class SampledAudioChannel implements Closeable {
  public static final int SAMPLES_PER_SECOND = 22050;
  private static final int BYTES_PER_CHANNEL_PER_WRITE = 512;

  private final AudioFormat format = new AudioFormat(SAMPLES_PER_SECOND, 16, 2, true, false);
  private final byte[] buffer = new byte[1024];
  private final AudioSource_idk[] _o = new AudioSource_idk[8];
  private final AudioSource_idk[] _s = new AudioSource_idk[8];
  public int[] data;
  public int _g;
  public int _b;
  private SourceDataLine line;
  private int lineBufferSizeInInts;
  private boolean isShutdown = false;
  private long _m = PseudoMonotonicClock.currentTimeMillis();
  private AudioSource_idk source;
  private int _r = 0;
  private int _l = 0;
  private boolean _n = true;
  private int _e = 0;
  private int _a = 0;
  private long _p = 0L;
  private int _u;
  private long _q = 0L;

  private static int popcount(final int var1) {
    final int var2 = (var1 & 0x55555555) + ((var1 >>> 1) & 0xd5555555);
    final int var3 = (var2 & 0x33333333) + ((var2 & 0xcccccccc) >>> 2);
    final int var4 = (var3 + (var3 >>> 4)) & 0xf0f0f0f;
    final int var5 = var4 + (var4 >>> 8);
    final int var6 = var5 + (var5 >>> 16);
    return var6 & 255;
  }

  private static void b446(final AudioSource_idk var0) {
    var0.enabled = false;
    if (var0.sampleData != null) {
      var0.sampleData._h = 0;
    }
    var0.forEach(SampledAudioChannel::b446);
  }

  @Override
  public void close() {
    if (this.line != null) {
      this.line.close();
      this.line = null;
    }
  }

  private int bufferedInts() {
    return this.lineBufferSizeInInts - (this.line.available() / 4);
  }

  private void performWrite() {
    for (int i = 0; i < BYTES_PER_CHANNEL_PER_WRITE; ++i) {
      int var3 = this.data[i];
      if (((var3 + 0x80_00_00) & 0xff_00_00_00) != 0) {
        var3 = 0x7fffff ^ (var3 >> 31);
      }
      this.buffer[i * 2] = (byte) (var3 >> 8);
      this.buffer[i * 2 + 1] = (byte) (var3 >> 16);
    }

    this.line.write(this.buffer, 0, BYTES_PER_CHANNEL_PER_WRITE * 2);
  }

  public void open(final int size) throws LineUnavailableException {
    try {
      final Info var2 = new Info(SourceDataLine.class, this.format, size * 4);
      this.line = (SourceDataLine) AudioSystem.getLine(var2);
      this.line.open();
      this.line.start();
      this.lineBufferSizeInInts = size;
    } catch (final LineUnavailableException var3) {
      if (popcount(size) == 1) {
        this.line = null;
        throw var3;
      } else {
        this.open(BitMath.nextLowestPowerOf2(size));
      }
    }
  }

  private void fillAudioBuffer(final int[] data) {
    Arrays.fill(data, 0, BYTES_PER_CHANNEL_PER_WRITE, 0);
    this._a -= 256;
    if (this.source != null && this._a <= 0) {
      this._a += 1378;
      b446(this.source);
      this.a607(this.source, this.source.c784());
      int var4 = 0;
      int var5 = 255;

      AudioSource_idk var10;
      label106:
      for (int var6 = 7; var5 != 0; --var6) {
        int var7;
        int var8;
        if (var6 < 0) {
          var7 = var6 & 3;
          var8 = -(var6 >> 2);
        } else {
          var7 = var6;
          var8 = 0;
        }

        for (int var9 = (var5 >>> var7) & 0x11111111; var9 != 0; var9 >>>= 4) {
          if ((var9 & 1) != 0) {
            var5 &= ~(1 << var7);
            var10 = null;
            AudioSource_idk var11 = this._o[var7];

            label100:
            while (true) {
              while (true) {
                if (var11 == null) {
                  break label100;
                }

                final AudioSampleData_idk var12 = var11.sampleData;
                if (var12 != null && var12._h > var8) {
                  var5 |= 1 << var7;
                  var10 = var11;
                  var11 = var11._h;
                } else {
                  var11.enabled = true;
                  final int var13 = var11.a784();
                  var4 += var13;
                  if (var12 != null) {
                    var12._h += var13;
                  }

                  if (var4 >= 32) {
                    break label106;
                  }

                  final int var15 = var11._i;
                  var11.forEach(var14 -> this.a607(var14, var15 * var14.c784() >> 8));

                  final AudioSource_idk var18 = var11._h;
                  var11._h = null;
                  if (var10 == null) {
                    this._o[var7] = var18;
                  } else {
                    var10._h = var18;
                  }

                  if (var18 == null) {
                    this._s[var7] = var10;
                  }

                  var11 = var18;
                }
              }
            }
          }

          var7 += 4;
          ++var8;
        }
      }

      for (int var6 = 0; var6 < 8; ++var6) {
        AudioSource_idk var16 = this._o[var6];
        this._s[var6] = null;

        for (this._o[var6] = null; var16 != null; var16 = var10) {
          var10 = var16._h;
          var16._h = null;
        }
      }
    }

    if (this._a < 0) {
      this._a = 0;
    }

    if (this.source != null) {
      this.source.processAndWrite(data, 0, 256);
    }

    this._m = PseudoMonotonicClock.currentTimeMillis();
  }

  private void b150() {
    this._a -= 256;
    if (this._a < 0) {
      this._a = 0;
    }
    if (this.source != null) {
      this.source.processAndDiscard(256);
    }
  }

  private void a607(final AudioSource_idk var1, final int var2) {
    final int i = var2 >> 5;
    final AudioSource_idk var4 = this._s[i];
    if (var4 == null) {
      this._o[i] = var1;
    } else {
      var4._h = var1;
    }

    this._s[i] = var1;
    var1._i = var2;
  }

  public synchronized void doSomethingThatSeemsRelatedToAudio() {
    if (!this.isShutdown) {
      long var1 = PseudoMonotonicClock.currentTimeMillis();

      try {
        if (var1 > this._m + 6000L) {
          this._m = var1 - 6000L;
        }

        while (var1 > this._m + 5000L) {
          this.b150();
          this._m += 256000 / SAMPLES_PER_SECOND;
          var1 = PseudoMonotonicClock.currentTimeMillis();
        }
      } catch (final Exception var6) {
        this._m = var1;
      }

      if (this.data != null) {
        try {
          if (this._p != 0L) {
            if (var1 < this._p) {
              return;
            }

            this.open(this._g);
            this._p = 0L;
            this._n = true;
          }

          int var3 = this.bufferedInts();
          if (this._r - var3 > this._l) {
            this._l = this._r - var3;
          }

          int var4 = this._b + this._u;
          if (var4 + 0x100 > 0x4000) {
            var4 = 0x3f00;
          }

          if (var4 + 0x100 > this._g) {
            this._g += 0x400;
            if (this._g > 0x4000) {
              this._g = 0x4000;
            }

            this.close();
            this.open(this._g);
            var3 = 0;
            this._n = true;
            if (var4 + 256 > this._g) {
              var4 = this._g - 256;
              this._u = var4 - this._b;
            }
          }

          while (var3 < var4) {
            this.fillAudioBuffer(this.data);
            this.performWrite();
            var3 += 256;
          }

          if (var1 > this._q) {
            if (this._n) {
              this._n = false;
            } else {
              if (this._l == 0 && this._e == 0) {
                this.close();
                this._p = var1 + 2000L;
                return;
              }

              this._u = Math.min(this._e, this._l);
              this._e = this._l;
            }

            this._l = 0;
            this._q = var1 + 2000L;
          }

          this._r = var3;
        } catch (final Exception var5) {
          this.close();
          this._p = var1 + 2000L;
        }
      }
    }
  }

  public synchronized void setSource(final AudioSource_idk source) {
    this.source = source;
  }

  public synchronized void shutdown() {
    if (AudioThread.instance != null) {
      boolean allChannelsShutdown = true;

      for (int i = 0; i < AudioThread.NUM_CHANNELS; ++i) {
        if (AudioThread.instance.channels[i] == this) {
          AudioThread.instance.channels[i] = null;
        }

        if (AudioThread.instance.channels[i] != null) {
          allChannelsShutdown = false;
        }
      }

      if (allChannelsShutdown) {
        AudioThread.instance.shutdownRequested = true;

        while (AudioThread.instance.isRunning) {
          JagexBaseApplet.maybeSleep(50L);
        }

        AudioThread.instance = null;
      }
    }

    this.close();
    this.data = null;
    this.isShutdown = true;
  }
}
