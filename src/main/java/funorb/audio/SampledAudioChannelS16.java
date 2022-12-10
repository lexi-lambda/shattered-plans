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

public final class SampledAudioChannelS16 implements Closeable {
  public static final int SAMPLE_RATE = 22050;
  private static final int TOTAL_SAMPLES_PER_WRITE = 512;

  private final AudioFormat format = new AudioFormat(SAMPLE_RATE, 16, 2, true, false);
  private final byte[] buffer = new byte[1024];

  private final AudioSource[] sourcesHeads_idk = new AudioSource[8];
  private final AudioSource[] sourcesTail_idk = new AudioSource[8];

  public int[] dataS16P8;
  public int lineSize;
  public int _b;
  private SourceDataLine line;
  private int lineBufferSizeInInts;
  private boolean isShutdown = false;
  private long fillMillis = PseudoMonotonicClock.currentTimeMillis();
  private AudioSource source;
  private int written_idk = 0;
  private int _l = 0;
  private boolean _n = true;
  private int _e = 0;
  private int toDiscard_idk = 0;
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

  private static void resetSource_idk(final AudioSource source) {
    source.enabled = false;
    if (source.rawSample != null) {
      source.rawSample.someCounter_idk = 0;
    }
    source.forEach(SampledAudioChannelS16::resetSource_idk);
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
    for (int i = 0; i < TOTAL_SAMPLES_PER_WRITE; ++i) {
      int sample = this.dataS16P8[i];
      if (((sample + 0x80_00_00) & 0xff_00_00_00) != 0) {
        sample = 0x7fffff ^ (sample >> 31);
      }
      this.buffer[i * 2] = (byte) (sample >> 8);
      this.buffer[i * 2 + 1] = (byte) (sample >> 16);
    }

    this.line.write(this.buffer, 0, TOTAL_SAMPLES_PER_WRITE * 2);
  }

  public void openLine(final int size) throws LineUnavailableException {
    try {
      final Info info = new Info(SourceDataLine.class, this.format, size * 4);
      this.line = (SourceDataLine) AudioSystem.getLine(info);
      this.line.open();
      this.line.start();
      this.lineBufferSizeInInts = size;
    } catch (final LineUnavailableException e) {
      if (popcount(size) == 1) {
        this.line = null;
        throw e;
      } else {
        this.openLine(BitMath.nextLowestPowerOf2(size));
      }
    }
  }

  private void fillAudioBuffer(final int[] dataS16P8) {
    Arrays.fill(dataS16P8, 0, TOTAL_SAMPLES_PER_WRITE, 0);

    this.toDiscard_idk -= 256;
    if (this.source != null && this.toDiscard_idk <= 0) {
      this.toDiscard_idk += 1378;
      resetSource_idk(this.source);
      this.sourcesAppend(this.source, this.source.someP8_idk());
      int breakAtOrAbove32 = 0;
      int bitSet = 255;

      AudioSource lastSource_idk;
      outerLoop:
      for (int var6 = 7; bitSet != 0; --var6) {
        int bits;
        int someThreshold_idk;
        if (var6 < 0) {
          bits = var6 & 3;
          someThreshold_idk = -(var6 >> 2);
        } else {
          bits = var6;
          someThreshold_idk = 0;
        }

        for (int var9 = (bitSet >>> bits) & 0x11111111; var9 != 0; var9 >>>= 4, bits += 4, someThreshold_idk++) {
          if ((var9 & 1) == 0) {
            continue;
          }

          bitSet &= ~(1 << bits);
          lastSource_idk = null;
          AudioSource source = this.sourcesHeads_idk[bits];

          while (source != null) {
            final RawSampleS8 sampleData = source.rawSample;
            if (sampleData != null && sampleData.someCounter_idk > someThreshold_idk) {
              bitSet |= 1 << bits;
              lastSource_idk = source;
              source = source.nextSource_idk;
            } else {
              source.enabled = true;
              final int _0_1_or_2 = source.returns_0_1_or_2();
              breakAtOrAbove32 += _0_1_or_2;
              if (sampleData != null) {
                sampleData.someCounter_idk += _0_1_or_2;
              }

              if (breakAtOrAbove32 >= 32) {
                break outerLoop;
              }

              final int someP8_idk = source.lastP8_idk;
              source.forEach(s -> this.sourcesAppend(s, someP8_idk * s.someP8_idk() >> 8));

              final AudioSource nextSource = source.nextSource_idk;
              source.nextSource_idk = null;
              if (lastSource_idk == null) {
                this.sourcesHeads_idk[bits] = nextSource;
              } else {
                lastSource_idk.nextSource_idk = nextSource;
              }

              if (nextSource == null) {
                this.sourcesTail_idk[bits] = lastSource_idk;
              }

              source = nextSource;
            }
          }
        }
      }

      for (int window = 0; window < 8; ++window) {
        AudioSource source = this.sourcesHeads_idk[window];
        this.sourcesTail_idk[window] = null;
        this.sourcesHeads_idk[window] = null;
        while (source != null) {
          final AudioSource next = source.nextSource_idk;
          source.nextSource_idk = null;
          source = next;
        }
      }
    }

    if (this.toDiscard_idk < 0) {
      this.toDiscard_idk = 0;
    }

    if (this.source != null) {
      this.source.processAndWrite(dataS16P8, 0, 256);
    }

    this.fillMillis = PseudoMonotonicClock.currentTimeMillis();
  }

  private void processAndDiscard256() {
    this.toDiscard_idk -= 256;
    if (this.toDiscard_idk < 0) {
      this.toDiscard_idk = 0;
    }
    if (this.source != null) {
      this.source.processAndDiscard(256);
    }
  }

  private void sourcesAppend(final AudioSource source, final int someP8_idk) {
    final int partition = someP8_idk >> 5;
    final AudioSource prev = this.sourcesTail_idk[partition];
    if (prev == null) {
      this.sourcesHeads_idk[partition] = source;
    } else {
      prev.nextSource_idk = source;
    }
    this.sourcesTail_idk[partition] = source;
    source.lastP8_idk = someP8_idk;
  }

  public synchronized void doSomethingThatSeemsRelatedToAudio() {
    if (this.isShutdown) {
      return;
    }

    long now = PseudoMonotonicClock.currentTimeMillis();
    try {
      if (this.fillMillis + 6000L < now) {
        this.fillMillis = now - 6000L;
      }
      while (this.fillMillis + 5000L < now) {
        this.processAndDiscard256();
        this.fillMillis += 256000 / SAMPLE_RATE;
        now = PseudoMonotonicClock.currentTimeMillis();
      }
    } catch (final Exception e) {
      this.fillMillis = now;
    }

    if (this.dataS16P8 == null) {
      return;
    }

    try {
      if (this._p != 0L) {
        if (now < this._p) {
          return;
        }

        this.openLine(this.lineSize);
        this._p = 0L;
        this._n = true;
      }

      int written_idk = this.bufferedInts();
      if (this.written_idk - written_idk > this._l) {
        this._l = this.written_idk - written_idk;
      }

      int toWrite_idk = this._b + this._u;
      if (toWrite_idk + 256 > 16384) {
        toWrite_idk = 16128;
      }

      if (toWrite_idk + 256 > this.lineSize) {
        this.lineSize += 1024;
        if (this.lineSize > 16384) {
          this.lineSize = 16384;
        }
        this.close();
        this.openLine(this.lineSize);
        written_idk = 0;
        this._n = true;
        if (toWrite_idk + 256 > this.lineSize) {
          toWrite_idk = this.lineSize - 256;
          this._u = toWrite_idk - this._b;
        }
      }

      while (written_idk < toWrite_idk) {
        this.fillAudioBuffer(this.dataS16P8);
        this.performWrite();
        written_idk += 256;
      }

      if (now > this._q) {
        if (this._n) {
          this._n = false;
        } else {
          if (this._l == 0 && this._e == 0) {
            this.close();
            this._p = now + 2000L;
            return;
          }

          this._u = Math.min(this._e, this._l);
          this._e = this._l;
        }

        this._l = 0;
        this._q = now + 2000L;
      }

      this.written_idk = written_idk;
    } catch (final Exception e) {
      this.close();
      this._p = now + 2000L;
    }
  }

  public synchronized void setSource(final AudioSource source) {
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
    this.dataS16P8 = null;
    this.isShutdown = true;
  }
}
