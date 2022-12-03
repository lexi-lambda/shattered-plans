package funorb.audio;

import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.Iterator;

public final class AudioSamplePlayback_idk extends AudioSource_idk {
  private final int loopStart_idfk;
  private final boolean _o;
  private final int loopEnd_idfk;
  private int ampL;
  private int panX;
  private int volActualX;
  private int ampRateR;
  private int ampRateL;
  private int playhead;
  private int volX;
  private int volDeltaX;
  private int loopDirection_idk;
  private int pitchX;
  private int volActualRateX;
  private int ampR;

  private AudioSamplePlayback_idk(final AudioSampleData_idk var1, final int pitchX, final int volX, final int panX) {
    this.sampleData = var1;
    this.loopStart_idfk = var1._l;
    this.loopEnd_idfk = var1._k;
    this._o = var1._i;
    this.pitchX = pitchX;
    this.volX = volX;
    this.panX = panX;
    this.playhead = 0;
    this.resetPlayback();
  }

  private static int xferBackPitched(final byte[] var2, final int[] var3, int var4, int var5, final int var6, final int var7, final int var9, final int var10, final AudioSamplePlayback_idk var11, final int var12, final int var13) {
    int var8;
    if (var12 == 0 || (var8 = var5 + (var10 + 256 - var4 + var12) / var12) > var9) {
      var8 = var9;
    }

    var5 <<= 1;

    int var10001;
    int var0;
    int var1;
    for (var8 <<= 1; var5 < var8; var4 += var12) {
      var1 = var4 >> 8;
      final byte var14 = var2[var1 - 1];
      var0 = (var14 << 8) + (var2[var1] - var14) * (var4 & 255);
      var10001 = var5++;
      var3[var10001] += var0 * var6 >> 6;
      var10001 = var5++;
      var3[var10001] += var0 * var7 >> 6;
    }

    if (var12 == 0 || (var8 = (var5 >> 1) + (var10 - var4 + var12) / var12) > var9) {
      var8 = var9;
    }

    var8 <<= 1;

    for (var1 = var13; var5 < var8; var4 += var12) {
      var0 = (var1 << 8) + (var2[var4 >> 8] - var1) * (var4 & 255);
      var10001 = var5++;
      var3[var10001] += var0 * var6 >> 6;
      var10001 = var5++;
      var3[var10001] += var0 * var7 >> 6;
    }

    var11.playhead = var4;
    return var5 >> 1;
  }

  public static AudioSamplePlayback_idk start(final AudioSampleData_idk sampleData, final int pitchX, final int volX, final int panX) {
    return (sampleData.data == null || sampleData.data.length == 0)
      ? null
      : new AudioSamplePlayback_idk(sampleData, pitchX, volX, panX);
  }

  private static int xfer(final byte[] var1, final int[] var2, int var3, int var4, int var5, int var6, final int var8, int var9, final AudioSamplePlayback_idk var10) {
    var3 >>= 8;
    var9 >>= 8;
    var5 <<= 2;
    var6 <<= 2;
    int var7;
    if ((var7 = var4 + var9 - var3) > var8) {
      var7 = var8;
    }

    var4 <<= 1;
    var7 <<= 1;

    int var10001;
    byte var11;
    for (var7 -= 6; var4 < var7; var2[var10001] += var11 * var6) {
      var11 = var1[var3++];
      var10001 = var4++;
      var2[var10001] += var11 * var5;
      var10001 = var4++;
      var2[var10001] += var11 * var6;
      var11 = var1[var3++];
      var10001 = var4++;
      var2[var10001] += var11 * var5;
      var10001 = var4++;
      var2[var10001] += var11 * var6;
      var11 = var1[var3++];
      var10001 = var4++;
      var2[var10001] += var11 * var5;
      var10001 = var4++;
      var2[var10001] += var11 * var6;
      var11 = var1[var3++];
      var10001 = var4++;
      var2[var10001] += var11 * var5;
      var10001 = var4++;
    }

    for (var7 += 6; var4 < var7; var2[var10001] += var11 * var6) {
      var11 = var1[var3++];
      var10001 = var4++;
      var2[var10001] += var11 * var5;
      var10001 = var4++;
    }

    var10.playhead = var3 << 8;
    return var4 >> 1;
  }

  private static int xferBackRamped(
    final byte[] sampleData,
    final int[] dest,
    int var3,
    int index,
    int ampL,
    int ampR,
    int ampRateL,
    int ampRateR,
    final int var10,
    int var11,
    final AudioSamplePlayback_idk var12
  ) {
    var3 >>= 8;
    var11 >>= 8;
    ampL <<= 2;
    ampR <<= 2;
    ampRateL <<= 2;
    ampRateR <<= 2;
    int var9;
    if ((var9 = index + var3 - (var11 - 1)) > var10) {
      var9 = var10;
    }

    var12.volActualX += var12.volActualRateX * (var9 - index);
    index <<= 1;
    var9 <<= 1;

    byte sample;
    var9 -= 6;
    while (index < var9) {
      sample = sampleData[var3--];
      dest[index++] += sample * ampL;
      dest[index++] += sample * ampR;
      ampL += ampRateL;
      ampR += ampRateR;
      sample = sampleData[var3--];
      dest[index++] += sample * ampL;
      dest[index++] += sample * ampR;
      ampL += ampRateL;
      ampR += ampRateR;
      sample = sampleData[var3--];
      dest[index++] += sample * ampL;
      dest[index++] += sample * ampR;
      ampL += ampRateL;
      ampR += ampRateR;
      sample = sampleData[var3--];
      dest[index++] += sample * ampL;
      dest[index++] += sample * ampR;
      ampL += ampRateL;
      ampR += ampRateR;
    }

    var9 += 6;
    while (index < var9) {
      sample = sampleData[var3--];
      dest[index++] += sample * ampL;
      dest[index++] += sample * ampR;
      ampL += ampRateL;
      ampR += ampRateR;
    }

    var12.ampL = ampL >> 2;
    var12.ampR = ampR >> 2;
    var12.playhead = var3 << 8;
    return index >> 1;
  }

  private static int xferRamped(final byte[] var1, final int[] var2, int var3, int var4, int var5, int var6, int var7, int var8, final int var10, int var11, final AudioSamplePlayback_idk var12) {
    var3 >>= 8;
    var11 >>= 8;
    var5 <<= 2;
    var6 <<= 2;
    var7 <<= 2;
    var8 <<= 2;
    int var9;
    if ((var9 = var4 + var11 - var3) > var10) {
      var9 = var10;
    }

    var12.volActualX += var12.volActualRateX * (var9 - var4);
    var4 <<= 1;
    var9 <<= 1;

    byte var13;
    int var10001;
    for (var9 -= 6; var4 < var9; var6 += var8) {
      var13 = var1[var3++];
      var10001 = var4++;
      var2[var10001] += var13 * var5;
      var5 += var7;
      var10001 = var4++;
      var2[var10001] += var13 * var6;
      var6 += var8;
      var13 = var1[var3++];
      var10001 = var4++;
      var2[var10001] += var13 * var5;
      var5 += var7;
      var10001 = var4++;
      var2[var10001] += var13 * var6;
      var6 += var8;
      var13 = var1[var3++];
      var10001 = var4++;
      var2[var10001] += var13 * var5;
      var5 += var7;
      var10001 = var4++;
      var2[var10001] += var13 * var6;
      var6 += var8;
      var13 = var1[var3++];
      var10001 = var4++;
      var2[var10001] += var13 * var5;
      var5 += var7;
      var10001 = var4++;
      var2[var10001] += var13 * var6;
    }

    for (var9 += 6; var4 < var9; var6 += var8) {
      var13 = var1[var3++];
      var10001 = var4++;
      var2[var10001] += var13 * var5;
      var5 += var7;
      var10001 = var4++;
      var2[var10001] += var13 * var6;
    }

    var12.ampL = var5 >> 2;
    var12.ampR = var6 >> 2;
    var12.playhead = var3 << 8;
    return var4 >> 1;
  }

  private static int xferPitched(final byte[] var2, final int[] var3, int var4, int var5, final int var6, final int var7, final int var9, final int var10, final AudioSamplePlayback_idk var11, final int var12, final int var13) {
    int var8;
    if (var12 == 0 || (var8 = var5 + (var10 - var4 + var12 - 257) / var12) > var9) {
      var8 = var9;
    }

    var5 <<= 1;

    byte var14;
    int var10001;
    int var0;
    int var1;
    for (var8 <<= 1; var5 < var8; var4 += var12) {
      var1 = var4 >> 8;
      var14 = var2[var1];
      var0 = (var14 << 8) + (var2[var1 + 1] - var14) * (var4 & 255);
      var10001 = var5++;
      var3[var10001] += var0 * var6 >> 6;
      var10001 = var5++;
      var3[var10001] += var0 * var7 >> 6;
    }

    if (var12 == 0 || (var8 = (var5 >> 1) + (var10 - var4 + var12 - 1) / var12) > var9) {
      var8 = var9;
    }

    var8 <<= 1;

    for (var1 = var13; var5 < var8; var4 += var12) {
      var14 = var2[var4 >> 8];
      var0 = (var14 << 8) + (var1 - var14) * (var4 & 255);
      var10001 = var5++;
      var3[var10001] += var0 * var6 >> 6;
      var10001 = var5++;
      var3[var10001] += var0 * var7 >> 6;
    }

    var11.playhead = var4;
    return var5 >> 1;
  }

  public static AudioSamplePlayback_idk a638(final AudioSampleData_idk var0, final int volume) {
    if (var0.data == null || var0.data.length == 0) {
      return null;
    } else {
      return new AudioSamplePlayback_idk(var0, (int) ((long) var0.sampleRate * 256L * (long) 100 / (100L * SampledAudioChannel.SAMPLES_PER_SECOND)), volume << 6, 0x2000);
    }
  }

  private static int xferRampedPitched(
    final byte[] sampleData,
    final int[] dest,
    int sourceIndex,
    int destIndex,
    int ampL,
    int ampR,
    final int ampRateL,
    final int ampRateR,
    final int var11,
    final int var12,
    final AudioSamplePlayback_idk playback,
    final int pitchX,
    final int var15
  ) {
    playback.volActualX -= playback.volActualRateX * destIndex;
    int var10;
    if (pitchX == 0 || (var10 = destIndex + (var12 - sourceIndex + pitchX - 257) / pitchX) > var11) {
      var10 = var11;
    }

    destIndex <<= 1;

    byte var16;
    int var10001;
    int var0;
    int var1;
    for (var10 <<= 1; destIndex < var10; sourceIndex += pitchX) {
      var1 = sourceIndex >> 8;
      var16 = sampleData[var1];
      var0 = (var16 << 8) + (sampleData[var1 + 1] - var16) * (sourceIndex & 255);
      var10001 = destIndex++;
      dest[var10001] += var0 * ampL >> 6;
      ampL += ampRateL;
      var10001 = destIndex++;
      dest[var10001] += var0 * ampR >> 6;
      ampR += ampRateR;
    }

    if (pitchX == 0 || (var10 = (destIndex >> 1) + (var12 - sourceIndex + pitchX - 1) / pitchX) > var11) {
      var10 = var11;
    }

    var10 <<= 1;

    for (var1 = var15; destIndex < var10; sourceIndex += pitchX) {
      var16 = sampleData[sourceIndex >> 8];
      var0 = (var16 << 8) + (var1 - var16) * (sourceIndex & 255);
      var10001 = destIndex++;
      dest[var10001] += var0 * ampL >> 6;
      ampL += ampRateL;
      var10001 = destIndex++;
      dest[var10001] += var0 * ampR >> 6;
      ampR += ampRateR;
    }

    destIndex >>= 1;
    playback.volActualX += playback.volActualRateX * destIndex;
    playback.ampL = ampL;
    playback.ampR = ampR;
    playback.playhead = sourceIndex;
    return destIndex;
  }

  private static int xferBack(final byte[] var1, final int[] var2, int var3, int var4, int var5, int var6, final int var8, int var9, final AudioSamplePlayback_idk var10) {
    var3 >>= 8;
    var9 >>= 8;
    var5 <<= 2;
    var6 <<= 2;
    int var7;
    if ((var7 = var4 + var3 - (var9 - 1)) > var8) {
      var7 = var8;
    }

    var4 <<= 1;
    var7 <<= 1;

    int var10001;
    byte var11;
    for (var7 -= 6; var4 < var7; var2[var10001] += var11 * var6) {
      var11 = var1[var3--];
      var10001 = var4++;
      var2[var10001] += var11 * var5;
      var10001 = var4++;
      var2[var10001] += var11 * var6;
      var11 = var1[var3--];
      var10001 = var4++;
      var2[var10001] += var11 * var5;
      var10001 = var4++;
      var2[var10001] += var11 * var6;
      var11 = var1[var3--];
      var10001 = var4++;
      var2[var10001] += var11 * var5;
      var10001 = var4++;
      var2[var10001] += var11 * var6;
      var11 = var1[var3--];
      var10001 = var4++;
      var2[var10001] += var11 * var5;
      var10001 = var4++;
    }

    for (var7 += 6; var4 < var7; var2[var10001] += var11 * var6) {
      var11 = var1[var3--];
      var10001 = var4++;
      var2[var10001] += var11 * var5;
      var10001 = var4++;
    }

    var10.playhead = var3 << 8;
    return var4 >> 1;
  }

  private static int calcAmpR(final int volume, final int var1) {
    return var1 < 0 ? -volume : (int) ((double) volume * Math.sqrt((double) var1 * 1.220703125E-4D) + 0.5D);
  }

  private static int xferBackRampedPitched(
    final byte[] sampleData,
    final int[] dest,
    int var4,
    int index,
    int ampL,
    int ampR,
    final int ampRateL,
    final int ampRateR,
    final int var11,
    final int var12,
    final AudioSamplePlayback_idk var13,
    final int var14,
    final int var15
  ) {
    var13.volActualX -= var13.volActualRateX * index;
    int var10;
    if (var14 == 0 || (var10 = index + (var12 + 256 - var4 + var14) / var14) > var11) {
      var10 = var11;
    }

    index <<= 1;

    int sample;
    int var1;
    for (var10 <<= 1; index < var10; var4 += var14) {
      var1 = var4 >> 8;
      final byte var16 = sampleData[var1 - 1];
      sample = (var16 << 8) + (sampleData[var1] - var16) * (var4 & 255);
      dest[index++] += sample * ampL >> 6;
      dest[index++] += sample * ampR >> 6;
      ampL += ampRateL;
      ampR += ampRateR;
    }

    if (var14 == 0 || (var10 = (index >> 1) + (var12 - var4 + var14) / var14) > var11) {
      var10 = var11;
    }

    var10 <<= 1;

    for (var1 = var15; index < var10; var4 += var14) {
      sample = (var1 << 8) + (sampleData[var4 >> 8] - var1) * (var4 & 255);
      dest[index++] += sample * ampL >> 6;
      dest[index++] += sample * ampR >> 6;
      ampL += ampRateL;
      ampR += ampRateR;
    }

    index >>= 1;
    var13.volActualX += var13.volActualRateX * index;
    var13.ampL = ampL;
    var13.ampR = ampR;
    var13.playhead = var4;
    return index;
  }

  private static int calcAmpL(final int volX, final int panX) {
    return panX < 0 ? volX : (int) ((double) volX * Math.sqrt((double) (16384 - panX) * 1.220703125E-4D) + 0.5D);
  }

  private synchronized void setVolAndPanX(final int volX, final int panX) {
    this.volX = volX;
    this.panX = panX;
    this.volDeltaX = 0;
    this.resetPlayback();
  }

  private int writeAudioDataForwards_idk(final int[] dest, int index, final int loopPoint, final int end, final int loopSample) {
    while (true) {
      if (this.volDeltaX > 0) {
        int var6 = index + this.volDeltaX;
        if (var6 > end) {
          var6 = end;
        }

        this.volDeltaX += index;
        if (this.pitchX == 256 && (this.playhead & 255) == 0) {
          index = xferRamped(this.sampleData.data, dest, this.playhead, index, this.ampL, this.ampR, this.ampRateL, this.ampRateR, var6, loopPoint, this);
        } else {
          index = xferRampedPitched(this.sampleData.data, dest, this.playhead, index, this.ampL, this.ampR, this.ampRateL, this.ampRateR, var6, loopPoint, this, this.pitchX, loopSample);
        }

        this.volDeltaX -= index;
        if (this.volDeltaX != 0) {
          return index;
        }

        if (!this.h801()) {
          continue;
        }

        return end;
      }

      if (this.pitchX == 256 && (this.playhead & 255) == 0) {
        return xfer(this.sampleData.data, dest, this.playhead, index, this.ampL, this.ampR, end, loopPoint, this);

      }

      return xferPitched(this.sampleData.data, dest, this.playhead, index, this.ampL, this.ampR, end, loopPoint, this, this.pitchX, loopSample);

    }
  }

  public synchronized int getVolume() {
    return this.volX == Integer.MIN_VALUE ? 0 : this.volX;
  }

  public synchronized void a093(final int var1, final int var2) {
    this.a326(var1, var2, this.panClampedX());
  }

  public synchronized void h150(int var1) {
    final int var2 = this.sampleData.data.length << 8;
    if (var1 < -1) {
      var1 = -1;
    }

    if (var1 > var2) {
      var1 = var2;
    }

    this.playhead = var1;
  }

  public synchronized void g150(int var1) {
    if (var1 == 0) {
      this.setVolume(0);
      this.unlink();
    } else if (this.ampL == 0 && this.ampR == 0) {
      this.volDeltaX = 0;
      this.volX = 0;
      this.volActualX = 0;
      this.unlink();
    } else {
      int var2 = -this.volActualX;
      if (this.volActualX > var2) {
        var2 = this.volActualX;
      }

      if (-this.ampL > var2) {
        var2 = -this.ampL;
      }

      if (this.ampL > var2) {
        var2 = this.ampL;
      }

      if (-this.ampR > var2) {
        var2 = -this.ampR;
      }

      if (this.ampR > var2) {
        var2 = this.ampR;
      }

      if (var1 > var2) {
        var1 = var2;
      }

      this.volDeltaX = var1;
      this.volX = Integer.MIN_VALUE;
      this.volActualRateX = -this.volActualX / var1;
      this.ampRateL = -this.ampL / var1;
      this.ampRateR = -this.ampR / var1;
    }
  }

  private void resetPlayback() {
    this.volActualX = this.volX;
    this.ampL = calcAmpL(this.volX, this.panX);
    this.ampR = calcAmpR(this.volX, this.panX);
  }

  public synchronized boolean volDeltaNonZero() {
    return this.volDeltaX != 0;
  }

  public synchronized void setPitchXNegAbs_idk() {
    this.pitchX = (this.pitchX ^ this.pitchX >> 31) + (this.pitchX >>> 31);
    this.pitchX = -this.pitchX;
  }

  @Override
  public synchronized void processAndDiscard(int len) {
    if (this.volDeltaX > 0) {
      if (len >= this.volDeltaX) {
        if (this.volX == Integer.MIN_VALUE) {
          this.volX = 0;
          this.ampR = 0;
          this.ampL = 0;
          this.volActualX = 0;
          this.unlink();
          len = this.volDeltaX;
        }

        this.volDeltaX = 0;
        this.resetPlayback();
      } else {
        this.volActualX += this.volActualRateX * len;
        this.ampL += this.ampRateL * len;
        this.ampR += this.ampRateR * len;
        this.volDeltaX -= len;
      }
    }

    final AudioSampleData_idk sampleData = this.sampleData;
    final int loopStart_idfk = this.loopStart_idfk << 8;
    final int loopEnd_idfk = this.loopEnd_idfk << 8;
    final int sampleLength = sampleData.data.length << 8;
    final int loopLength_idk = loopEnd_idfk - loopStart_idfk;
    if (loopLength_idk <= 0) {
      this.loopDirection_idk = 0;
    }

    if (this.playhead < 0) {
      if (this.pitchX <= 0) {
        this.j797();
        this.unlink();
        return;
      }

      this.playhead = 0;
    }

    if (this.playhead >= sampleLength) {
      if (this.pitchX >= 0) {
        this.j797();
        this.unlink();
        return;
      }

      this.playhead = sampleLength - 1;
    }

    this.playhead += this.pitchX * len;
    if (this.loopDirection_idk < 0) {
      if (this._o) {
        if (this.pitchX < 0) {
          if (this.playhead >= loopStart_idfk) {
            return;
          }

          this.playhead = loopStart_idfk + loopStart_idfk - 1 - this.playhead;
          this.pitchX = -this.pitchX;
        }

        while (this.playhead >= loopEnd_idfk) {
          this.playhead = loopEnd_idfk + loopEnd_idfk - 1 - this.playhead;
          this.pitchX = -this.pitchX;
          if (this.playhead >= loopStart_idfk) {
            return;
          }

          this.playhead = loopStart_idfk + loopStart_idfk - 1 - this.playhead;
          this.pitchX = -this.pitchX;
        }

      } else if (this.pitchX < 0) {
        if (this.playhead >= loopStart_idfk) {
          return;
        }

        this.playhead = loopEnd_idfk - 1 - (loopEnd_idfk - 1 - this.playhead) % loopLength_idk;
      } else {
        if (this.playhead < loopEnd_idfk) {
          return;
        }

        this.playhead = loopStart_idfk + (this.playhead - loopStart_idfk) % loopLength_idk;
      }
    } else {
      if (this.loopDirection_idk > 0) {
        label121:
        {
          if (this.pitchX < 0) {
            if (this.playhead >= loopStart_idfk) {
              return;
            }

            this.playhead = loopStart_idfk + loopStart_idfk - 1 - this.playhead;
            this.pitchX = -this.pitchX;
            if (--this.loopDirection_idk == 0) {
              break label121;
            }
          }

          do {
            if (this.playhead < loopEnd_idfk) {
              return;
            }

            this.playhead = loopEnd_idfk + loopEnd_idfk - 1 - this.playhead;
            this.pitchX = -this.pitchX;
            if (--this.loopDirection_idk == 0) {
              break;
            }

            if (this.playhead >= loopStart_idfk) {
              return;
            }

            this.playhead = loopStart_idfk + loopStart_idfk - 1 - this.playhead;
            this.pitchX = -this.pitchX;
          } while (--this.loopDirection_idk != 0);
        }
      }

      if (this.pitchX < 0) {
        if (this.playhead < 0) {
          this.playhead = -1;
          this.j797();
          this.unlink();
        }
      } else if (this.playhead >= sampleLength) {
        this.playhead = sampleLength;
        this.j797();
        this.unlink();
      }
    }
  }

  public synchronized boolean g801() {
    return this.playhead < 0 || this.playhead >= this.sampleData.data.length << 8;
  }

  public synchronized void a326(int var1, final int volX, final int panX) {
    if (var1 == 0) {
      this.setVolAndPanX(volX, panX);
    } else {
      final int var4 = calcAmpL(volX, panX);
      final int var5 = calcAmpR(volX, panX);
      if (this.ampL == var4 && this.ampR == var5) {
        this.volDeltaX = 0;
      } else {
        int var6 = volX - this.volActualX;
        if (this.volActualX - volX > var6) {
          var6 = this.volActualX - volX;
        }

        if (var4 - this.ampL > var6) {
          var6 = var4 - this.ampL;
        }

        if (this.ampL - var4 > var6) {
          var6 = this.ampL - var4;
        }

        if (var5 - this.ampR > var6) {
          var6 = var5 - this.ampR;
        }

        if (this.ampR - var5 > var6) {
          var6 = this.ampR - var5;
        }

        if (var1 > var6) {
          var1 = var6;
        }

        this.volDeltaX = var1;
        this.volX = volX;
        this.panX = panX;
        this.volActualRateX = (volX - this.volActualX) / var1;
        this.ampRateL = (var4 - this.ampL) / var1;
        this.ampRateR = (var5 - this.ampR) / var1;
      }
    }
  }

  @Override
  public @NotNull Iterator<AudioSource_idk> iterator() {
    return Collections.emptyIterator();
  }

  @Override
  public int a784() {
    return this.volX == 0 && this.volDeltaX == 0 ? 0 : 1;
  }

  public synchronized void d150(final int var1) {
    if (this.pitchX < 0) {
      this.pitchX = -var1;
    } else {
      this.pitchX = var1;
    }

  }

  @Override
  public synchronized void processAndWrite(final int[] dest, final int offset, final int len) {
    if (this.volX == 0 && this.volDeltaX == 0) {
      this.processAndDiscard(len);
      return;
    }

    final int loopStart = this.loopStart_idfk << 8;
    final int loopEnd = this.loopEnd_idfk << 8;
    final int sampleLength = this.sampleData.data.length << 8;
    final int loopLength = loopEnd - loopStart;
    if (loopLength <= 0) {
      this.loopDirection_idk = 0;
    }

    int pos = offset;
    final int end = len + offset;
    if (this.playhead < 0) {
      if (this.pitchX <= 0) {
        this.j797();
        this.unlink();
        return;
      }

      this.playhead = 0;
    }

    if (this.playhead >= sampleLength) {
      if (this.pitchX >= 0) {
        this.j797();
        this.unlink();
        return;
      }

      this.playhead = sampleLength - 1;
    }

    if (this.loopDirection_idk < 0) {
      if (this._o) {
        if (this.pitchX < 0) {
          pos = this.writeAudioDataBackwards_idk(dest, offset, loopStart, end, this.sampleData.data[this.loopStart_idfk]);
          if (this.playhead >= loopStart) {
            return;
          }

          this.playhead = loopStart + loopStart - 1 - this.playhead;
          this.pitchX = -this.pitchX;
        }

        while (true) {
          pos = this.writeAudioDataForwards_idk(dest, pos, loopEnd, end, this.sampleData.data[this.loopEnd_idfk - 1]);
          if (this.playhead < loopEnd) {
            return;
          }

          this.playhead = loopEnd + loopEnd - 1 - this.playhead;
          this.pitchX = -this.pitchX;
          pos = this.writeAudioDataBackwards_idk(dest, pos, loopStart, end, this.sampleData.data[this.loopStart_idfk]);
          if (this.playhead >= loopStart) {
            return;
          }

          this.playhead = loopStart + loopStart - 1 - this.playhead;
          this.pitchX = -this.pitchX;
        }
      } else if (this.pitchX < 0) {
        while (true) {
          pos = this.writeAudioDataBackwards_idk(dest, pos, loopStart, end, this.sampleData.data[this.loopEnd_idfk - 1]);
          if (this.playhead >= loopStart) {
            return;
          }

          this.playhead = loopEnd - 1 - (loopEnd - 1 - this.playhead) % loopLength;
        }
      } else {
        while (true) {
          pos = this.writeAudioDataForwards_idk(dest, pos, loopEnd, end, this.sampleData.data[this.loopStart_idfk]);
          if (this.playhead < loopEnd) {
            return;
          }

          this.playhead = loopStart + (this.playhead - loopStart) % loopLength;
        }
      }
    } else {
      if (this.loopDirection_idk > 0) {
        if (this._o) {
          label131:
          {
            if (this.pitchX < 0) {
              pos = this.writeAudioDataBackwards_idk(dest, offset, loopStart, end, this.sampleData.data[this.loopStart_idfk]);
              if (this.playhead >= loopStart) {
                return;
              }

              this.playhead = loopStart + loopStart - 1 - this.playhead;
              this.pitchX = -this.pitchX;
              if (--this.loopDirection_idk == 0) {
                break label131;
              }
            }

            do {
              pos = this.writeAudioDataForwards_idk(dest, pos, loopEnd, end, this.sampleData.data[this.loopEnd_idfk - 1]);
              if (this.playhead < loopEnd) {
                return;
              }

              this.playhead = loopEnd + loopEnd - 1 - this.playhead;
              this.pitchX = -this.pitchX;
              if (--this.loopDirection_idk == 0) {
                break;
              }

              pos = this.writeAudioDataBackwards_idk(dest, pos, loopStart, end, this.sampleData.data[this.loopStart_idfk]);
              if (this.playhead >= loopStart) {
                return;
              }

              this.playhead = loopStart + loopStart - 1 - this.playhead;
              this.pitchX = -this.pitchX;
            } while (--this.loopDirection_idk != 0);
          }
        } else {
          int var10;
          if (this.pitchX < 0) {
            while (true) {
              pos = this.writeAudioDataBackwards_idk(dest, pos, loopStart, end, this.sampleData.data[this.loopEnd_idfk - 1]);
              if (this.playhead >= loopStart) {
                return;
              }

              var10 = (loopEnd - 1 - this.playhead) / loopLength;
              if (var10 >= this.loopDirection_idk) {
                this.playhead += loopLength * this.loopDirection_idk;
                this.loopDirection_idk = 0;
                break;
              }

              this.playhead += loopLength * var10;
              this.loopDirection_idk -= var10;
            }
          } else {
            while (true) {
              pos = this.writeAudioDataForwards_idk(dest, pos, loopEnd, end, this.sampleData.data[this.loopStart_idfk]);
              if (this.playhead < loopEnd) {
                return;
              }

              var10 = (this.playhead - loopStart) / loopLength;
              if (var10 >= this.loopDirection_idk) {
                this.playhead -= loopLength * this.loopDirection_idk;
                this.loopDirection_idk = 0;
                break;
              }

              this.playhead -= loopLength * var10;
              this.loopDirection_idk -= var10;
            }
          }
        }
      }

      if (this.pitchX < 0) {
        this.writeAudioDataBackwards_idk(dest, pos, 0, end, 0);
        if (this.playhead < 0) {
          this.playhead = -1;
          this.j797();
          this.unlink();
        }
      } else {
        this.writeAudioDataForwards_idk(dest, pos, sampleLength, end, 0);
        if (this.playhead >= sampleLength) {
          this.playhead = sampleLength;
          this.j797();
          this.unlink();
        }
      }
    }
  }

  @Override
  public int c784() {
    final int var1 = (this.volActualX * 3) >> 6;
    int var2 = (var1 ^ (var1 >> 31)) + (var1 >>> 31);
    if (this.loopDirection_idk == 0) {
      var2 -= var2 * this.playhead / (this.sampleData.data.length << 8);
    } else if (this.loopDirection_idk >= 0) {
      var2 -= var2 * this.loopStart_idfk / this.sampleData.data.length;
    }
    return Math.min(var2, 255);
  }

  private void j797() {
    if (this.volDeltaX != 0) {
      if (this.volX == Integer.MIN_VALUE) {
        this.volX = 0;
      }

      this.volDeltaX = 0;
      this.resetPlayback();
    }

  }

  public synchronized int f784() {
    return this.pitchX < 0 ? -this.pitchX : this.pitchX;
  }

  private int writeAudioDataBackwards_idk(final int[] dest, int index, final int loopPoint, final int end, final int loopSample) {
    while (true) {
      if (this.volDeltaX > 0) {
        int var6 = index + this.volDeltaX;
        if (var6 > end) {
          var6 = end;
        }

        this.volDeltaX += index;
        if (this.pitchX == -256 && (this.playhead & 255) == 0) {
          index = xferBackRamped(this.sampleData.data, dest, this.playhead, index, this.ampL, this.ampR, this.ampRateL, this.ampRateR, var6, loopPoint, this);
        } else {
          index = xferBackRampedPitched(this.sampleData.data, dest, this.playhead, index, this.ampL, this.ampR, this.ampRateL, this.ampRateR, var6, loopPoint, this, this.pitchX, loopSample);
        }

        this.volDeltaX -= index;
        if (this.volDeltaX != 0) {
          return index;
        }

        if (!this.h801()) {
          continue;
        }

        return end;
      }

      if (this.pitchX == -256 && (this.playhead & 255) == 0) {
        return xferBack(this.sampleData.data, dest, this.playhead, index, this.ampL, this.ampR, end, loopPoint, this);

      }

      return xferBackPitched(this.sampleData.data, dest, this.playhead, index, this.ampL, this.ampR, end, loopPoint, this, this.pitchX, loopSample);

    }
  }

  @SuppressWarnings("BooleanMethodIsAlwaysInverted")
  private boolean h801() {
    int volume = this.volX;
    final int ampL;
    final int ampR;
    if (volume == Integer.MIN_VALUE) {
      ampR = 0;
      ampL = 0;
      volume = 0;
    } else {
      ampL = calcAmpL(volume, this.panX);
      ampR = calcAmpR(volume, this.panX);
    }

    if (this.volActualX == volume && this.ampL == ampL && this.ampR == ampR) {
      if (this.volX == Integer.MIN_VALUE) {
        this.volX = 0;
        this.unlink();
        return true;
      } else {
        this.resetPlayback();
        return false;
      }
    } else {
      if (this.volActualX < volume) {
        this.volActualRateX = 1;
        this.volDeltaX = volume - this.volActualX;
      } else if (this.volActualX > volume) {
        this.volActualRateX = -1;
        this.volDeltaX = this.volActualX - volume;
      } else {
        this.volActualRateX = 0;
      }

      if (this.ampL < ampL) {
        this.ampRateL = 1;
        if (this.volDeltaX == 0 || this.volDeltaX > ampL - this.ampL) {
          this.volDeltaX = ampL - this.ampL;
        }
      } else if (this.ampL > ampL) {
        this.ampRateL = -1;
        if (this.volDeltaX == 0 || this.volDeltaX > this.ampL - ampL) {
          this.volDeltaX = this.ampL - ampL;
        }
      } else {
        this.ampRateL = 0;
      }

      if (this.ampR < ampR) {
        this.ampRateR = 1;
        if (this.volDeltaX == 0 || this.volDeltaX > ampR - this.ampR) {
          this.volDeltaX = ampR - this.ampR;
        }
      } else if (this.ampR > ampR) {
        this.ampRateR = -1;
        if (this.volDeltaX == 0 || this.volDeltaX > this.ampR - ampR) {
          this.volDeltaX = this.ampR - ampR;
        }
      } else {
        this.ampRateR = 0;
      }

      return false;
    }
  }

  public synchronized int panClampedX() {
    return this.panX < 0 ? -1 : this.panX;
  }

  public synchronized void setVolume(final int volume) {
    this.setVolAndPanX(volume, this.panClampedX());
  }

  public synchronized void f150() {
    this.loopDirection_idk = -1;
  }
}
