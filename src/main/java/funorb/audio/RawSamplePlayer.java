package funorb.audio;

import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.Iterator;

public final class RawSamplePlayer extends AudioSource {
  private static final double INV_0x2000 = 1.220703125E-4D;

  private final boolean isLooped_idk;
  private final int loopStart_idfk;
  private final int loopEnd_idfk;
  private int loopDirection_idk;

  private int vol_p14;
  private int pan_p14; // left=0.0/0, center=0.5/0x2000, right=1.0/0x4000. negative values are "surround"

  private int amp_p14;
  private int ampL_p14;
  private int ampR_p14;
  private int ampRate_p14;
  private int ampRateR_p14;
  private int ampRateL_p14;
  private int ampTime;

  private int playhead_p8;
  private int speed_p8;

  private RawSamplePlayer(final RawSampleS8 raw, final int speed_p8, final int vol_p14, final int pan_p14) {
    this.rawSample = raw;
    this.loopStart_idfk = raw.loopStart_idfk;
    this.loopEnd_idfk = raw.loopEnd_idfk;
    this.isLooped_idk = raw.isLooped_idk;
    this.speed_p8 = speed_p8;
    this.vol_p14 = vol_p14;
    this.pan_p14 = pan_p14;
    this.playhead_p8 = 0;
    this.resetAmp();
  }

  private static int xferBackPitched(final byte[] var2, final int[] var3, int var4, int var5, final int var6, final int var7, final int var9, final int var10, final RawSamplePlayer var11, final int var12, final int var13) {
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

    var11.playhead_p8 = var4;
    return var5 >> 1;
  }

  public static RawSamplePlayer start(final RawSampleS8 sampleData, final int pitchX, final int volX, final int panX) {
    return (sampleData.data_s8 == null || sampleData.data_s8.length == 0)
      ? null
      : new RawSamplePlayer(sampleData, pitchX, volX, panX);
  }

  private static int xfer(
    final byte[] src_s8,
    final int[] dst_s16p8,
    int srcOff_p8,
    int dstOff,
    int ampL_p14,
    int ampR_p14,
    final int dstLen,
    int srcEnd_p8,
    final RawSamplePlayer player
  ) {
    int srcOff = srcOff_p8 >> 8;
    int srcEnd = srcEnd_p8 >> 8;
    int ampL_p16 = ampL_p14 << 2;
    int ampR_p16 = ampR_p14 << 2;
    int dstEnd = dstOff + srcEnd - srcOff;
    if (dstEnd > dstLen) {
      dstEnd = dstLen;
    }

    dstOff <<= 1;
    dstEnd <<= 1;

    byte src;
    dstEnd -= 6;
    while (dstOff < dstEnd) {
      src = src_s8[srcOff++];
      dst_s16p8[dstOff++] += src * ampL_p16;
      dst_s16p8[dstOff++] += src * ampR_p16;
      src = src_s8[srcOff++];
      dst_s16p8[dstOff++] += src * ampL_p16;
      dst_s16p8[dstOff++] += src * ampR_p16;
      src = src_s8[srcOff++];
      dst_s16p8[dstOff++] += src * ampL_p16;
      dst_s16p8[dstOff++] += src * ampR_p16;
      src = src_s8[srcOff++];
      dst_s16p8[dstOff++] += src * ampL_p16;
      dst_s16p8[dstOff++] += src * ampR_p16;
    }

    dstEnd += 6;
    while (dstOff < dstEnd) {
      src = src_s8[srcOff++];
      dst_s16p8[dstOff++] += src * ampL_p16;
      dst_s16p8[dstOff++] += src * ampR_p16;
    }

    player.playhead_p8 = srcOff << 8;
    return dstOff >> 1;
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
    final RawSamplePlayer var12
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

    var12.amp_p14 += var12.ampRate_p14 * (var9 - index);
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

    var12.ampL_p14 = ampL >> 2;
    var12.ampR_p14 = ampR >> 2;
    var12.playhead_p8 = var3 << 8;
    return index >> 1;
  }

  private static int xferRamped(
    final byte[] src_s8,
    final int[] dst_s16p8,
    int playhead_p8,
    int dstOff,
    int ampL_p14,
    int ampR_p14,
    int ampRateL_p14,
    int ampRateR_p14,
    final int dstLen,
    int srcEnd_p8,
    final RawSamplePlayer playback
  ) {
    int playhead = playhead_p8 >> 8;
    int srcEnd = srcEnd_p8 >> 8;
    int ampL_p16 = ampL_p14 << 2;
    int ampR_p16 = ampR_p14 << 2;
    int ampRateL_p16 = ampRateL_p14 << 2;
    int ampRateR_p16 = ampRateR_p14 << 2;
    int dstEnd = dstOff + srcEnd - playhead;

    if (dstEnd > dstLen) {
      dstEnd = dstLen;
    }

    playback.amp_p14 += playback.ampRate_p14 * (dstEnd - dstOff);
    dstOff <<= 1;
    dstEnd <<= 1;

    byte src;
    dstEnd -= 6;
    while (dstOff < dstEnd) {
      src = src_s8[playhead++];
      dst_s16p8[dstOff++] += src * ampL_p16;
      dst_s16p8[dstOff++] += src * ampR_p16;
      ampL_p16 += ampRateL_p16;
      ampR_p16 += ampRateR_p16;

      src = src_s8[playhead++];
      dst_s16p8[dstOff++] += src * ampL_p16;
      dst_s16p8[dstOff++] += src * ampR_p16;
      ampL_p16 += ampRateL_p16;
      ampR_p16 += ampRateR_p16;

      src = src_s8[playhead++];
      dst_s16p8[dstOff++] += src * ampL_p16;
      dst_s16p8[dstOff++] += src * ampR_p16;
      ampL_p16 += ampRateL_p16;
      ampR_p16 += ampRateR_p16;

      src = src_s8[playhead++];
      dst_s16p8[dstOff++] += src * ampL_p16;
      dst_s16p8[dstOff++] += src * ampR_p16;
      ampL_p16 += ampRateL_p16;
      ampR_p16 += ampRateR_p16;
    }

    dstEnd += 6;
    while (dstOff < dstEnd) {
      src = src_s8[playhead++];
      dst_s16p8[dstOff++] += src * ampL_p16;
      dst_s16p8[dstOff++] += src * ampR_p16;
      ampL_p16 += ampRateL_p16;
      ampR_p16 += ampRateR_p16;
    }

    playback.ampL_p14 = ampL_p16 >> 2;
    playback.ampR_p14 = ampR_p16 >> 2;
    playback.playhead_p8 = playhead << 8;

    return dstOff >> 1;
  }

  private static int xferPitched(
    final byte[] src_s8,
    final int[] dst_s16p8,
    int playhead_p8,
    int dstOff,
    final int ampL_p14,
    final int ampR_p14,
    final int dstLen,
    final int srcEnd_p8,
    final RawSamplePlayer playback,
    final int speed_p8,
    final int stitch_s8
  ) {
    int dstEnd = dstOff + (srcEnd_p8 - playhead_p8 + speed_p8 - 257) / speed_p8;
    if (speed_p8 == 0 || dstEnd > dstLen) {
      dstEnd = dstLen;
    }

    dstOff <<= 1;

    byte src;
    int out_s16;
    int playhead;
    dstEnd <<= 1;
    while (dstOff < dstEnd) {
      playhead = playhead_p8 >> 8;
      src = src_s8[playhead];
      out_s16 = (src << 8) + (src_s8[playhead + 1] - src) * (playhead_p8 & 255);
      dst_s16p8[dstOff++] += out_s16 * ampL_p14 >> 6;
      dst_s16p8[dstOff++] += out_s16 * ampR_p14 >> 6;
      playhead_p8 += speed_p8;
    }

    dstEnd = (dstOff >> 1) + (srcEnd_p8 - playhead_p8 + speed_p8 - 1) / speed_p8;
    if (speed_p8 == 0 || dstEnd > dstLen) {
      dstEnd = dstLen;
    }

    dstEnd <<= 1;

    while (dstOff < dstEnd) {
      src = src_s8[playhead_p8 >> 8];
      out_s16 = (src << 8) + (stitch_s8 - src) * (playhead_p8 & 255);
      dst_s16p8[dstOff++] += out_s16 * ampL_p14 >> 6;
      dst_s16p8[dstOff++] += out_s16 * ampR_p14 >> 6;
      playhead_p8 += speed_p8;
    }

    playback.playhead_p8 = playhead_p8;
    return dstOff >> 1;
  }

  public static RawSamplePlayer a638(final RawSampleS8 sample, final int vol_p8) {
    if (sample.data_s8 == null || sample.data_s8.length == 0) {
      return null;
    } else {
      int speed_p8 = (int) ((long) sample.sampleRate * 256L * (long) 100 / (100L * SampledAudioChannelS16.SAMPLE_RATE));
      return new RawSamplePlayer(sample, speed_p8, vol_p8 << 6, 0x2000);
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
    final RawSamplePlayer playback,
    final int pitchX,
    final int var15
  ) {
    playback.amp_p14 -= playback.ampRate_p14 * destIndex;
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
    playback.amp_p14 += playback.ampRate_p14 * destIndex;
    playback.ampL_p14 = ampL;
    playback.ampR_p14 = ampR;
    playback.playhead_p8 = sourceIndex;
    return destIndex;
  }

  private static int xferBack(final byte[] var1, final int[] var2, int var3, int var4, int var5, int var6, final int var8, int var9, final RawSamplePlayer var10) {
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

    var10.playhead_p8 = var3 << 8;
    return var4 >> 1;
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
    final RawSamplePlayer var13,
    final int var14,
    final int var15
  ) {
    var13.amp_p14 -= var13.ampRate_p14 * index;
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
    var13.amp_p14 += var13.ampRate_p14 * index;
    var13.ampL_p14 = ampL;
    var13.ampR_p14 = ampR;
    var13.playhead_p8 = var4;
    return index;
  }

  private static int calcAmpL(final int vol_p14, final int pan_p14) {
    return pan_p14 < 0 ? vol_p14 : (int) ((double) vol_p14 * Math.sqrt((double) (0x4000 - pan_p14) * INV_0x2000)+ 0.5D);
  }

  private static int calcAmpR(final int vol_p14, final int pan_p14) {
    return pan_p14 < 0 ? -vol_p14 : (int) ((double) vol_p14 * Math.sqrt((double) pan_p14 * INV_0x2000) + 0.5D);
  }

  private synchronized void setVolAndPan_p14(final int vol_p14, final int pan_p14) {
    this.vol_p14 = vol_p14;
    this.pan_p14 = pan_p14;
    this.ampTime = 0;
    this.resetAmp();
  }

  private int writeAudioDataForwards_idk(final int[] dest_s16p8, int dstOff, final int srcEnd_p8, final int dstLen, final int stitch_s8) {
    while (true) {
      if (this.ampTime > 0) {
        int dstEnd = dstOff + this.ampTime;
        if (dstEnd > dstLen) {
          dstEnd = dstLen;
        }

        this.ampTime += dstOff;
        if (this.speed_p8 == 256 && (this.playhead_p8 & 255) == 0) {
          dstOff = xferRamped(
            this.rawSample.data_s8,
            dest_s16p8,
            this.playhead_p8,
            dstOff,
            this.ampL_p14,
            this.ampR_p14,
            this.ampRateL_p14,
            this.ampRateR_p14,
            dstEnd,
            srcEnd_p8,
            this
          );
        } else {
          dstOff = xferRampedPitched(
            this.rawSample.data_s8,
            dest_s16p8,
            this.playhead_p8,
            dstOff,
            this.ampL_p14,
            this.ampR_p14,
            this.ampRateL_p14,
            this.ampRateR_p14,
            dstEnd,
            srcEnd_p8,
            this,
            this.speed_p8,
            stitch_s8
          );
        }

        this.ampTime -= dstOff;
        if (this.ampTime != 0) {
          return dstOff;
        }

        if (this.updateAmpRate()) {
          return dstLen;
        }

      } else {
        if (this.speed_p8 == 256 && (this.playhead_p8 & 255) == 0) {
          return xfer(
            this.rawSample.data_s8,
            dest_s16p8,
            this.playhead_p8,
            dstOff,
            this.ampL_p14,
            this.ampR_p14,
            dstLen,
            srcEnd_p8,
            this
          );
        } else {
          return xferPitched(
            this.rawSample.data_s8,
            dest_s16p8,
            this.playhead_p8,
            dstOff,
            this.ampL_p14,
            this.ampR_p14,
            dstLen,
            srcEnd_p8,
            this,
            this.speed_p8,
            stitch_s8
          );
        }
      }
    }
  }

  public synchronized int getVol_p14() {
    return this.vol_p14 == Integer.MIN_VALUE ? 0 : this.vol_p14;
  }

  public synchronized void a093(final int var1, final int volX) {
    this.a326(var1, volX, this.getPan_p14());
  }

  public synchronized void setPlayhead_p8(int playhead_p8) {
    final int maxPlayhead = this.rawSample.data_s8.length << 8;
    if (playhead_p8 < -1) {
      playhead_p8 = -1;
    }
    if (playhead_p8 > maxPlayhead) {
      playhead_p8 = maxPlayhead;
    }
    this.playhead_p8 = playhead_p8;
  }

  public synchronized void g150(int var1) {
    if (var1 == 0) {
      this.setVol_p14(0);
      this.unlink();
    } else if (this.ampL_p14 == 0 && this.ampR_p14 == 0) {
      this.ampTime = 0;
      this.vol_p14 = 0;
      this.amp_p14 = 0;
      this.unlink();
    } else {
      int var2 = -this.amp_p14;
      if (this.amp_p14 > var2) {
        var2 = this.amp_p14;
      }

      if (-this.ampL_p14 > var2) {
        var2 = -this.ampL_p14;
      }

      if (this.ampL_p14 > var2) {
        var2 = this.ampL_p14;
      }

      if (-this.ampR_p14 > var2) {
        var2 = -this.ampR_p14;
      }

      if (this.ampR_p14 > var2) {
        var2 = this.ampR_p14;
      }

      if (var1 > var2) {
        var1 = var2;
      }

      this.ampTime = var1;
      this.vol_p14 = Integer.MIN_VALUE;
      this.ampRate_p14 = -this.amp_p14 / var1;
      this.ampRateL_p14 = -this.ampL_p14 / var1;
      this.ampRateR_p14 = -this.ampR_p14 / var1;
    }
  }

  private void resetAmp() {
    this.amp_p14 = this.vol_p14;
    this.ampL_p14 = calcAmpL(this.vol_p14, this.pan_p14);
    this.ampR_p14 = calcAmpR(this.vol_p14, this.pan_p14);
  }

  public synchronized boolean volDeltaNonZero() {
    return this.ampTime != 0;
  }

  public synchronized void setPitchXNegAbs_idk() {
    this.speed_p8 = (this.speed_p8 ^ this.speed_p8 >> 31) + (this.speed_p8 >>> 31);
    this.speed_p8 = -this.speed_p8;
  }

  @Override
  public synchronized void processAndDiscard(int len) {
    if (this.ampTime > 0) {
      if (len >= this.ampTime) {
        if (this.vol_p14 == Integer.MIN_VALUE) {
          this.vol_p14 = 0;
          this.ampR_p14 = 0;
          this.ampL_p14 = 0;
          this.amp_p14 = 0;
          this.unlink();
          len = this.ampTime;
        }

        this.ampTime = 0;
        this.resetAmp();
      } else {
        this.amp_p14 += this.ampRate_p14 * len;
        this.ampL_p14 += this.ampRateL_p14 * len;
        this.ampR_p14 += this.ampRateR_p14 * len;
        this.ampTime -= len;
      }
    }

    final RawSampleS8 sampleData = this.rawSample;
    final int loopStart_idfk = this.loopStart_idfk << 8;
    final int loopEnd_idfk = this.loopEnd_idfk << 8;
    final int sampleLength = sampleData.data_s8.length << 8;
    final int loopLength_idk = loopEnd_idfk - loopStart_idfk;
    if (loopLength_idk <= 0) {
      this.loopDirection_idk = 0;
    }

    if (this.playhead_p8 < 0) {
      if (this.speed_p8 <= 0) {
        this.j797();
        this.unlink();
        return;
      }

      this.playhead_p8 = 0;
    }

    if (this.playhead_p8 >= sampleLength) {
      if (this.speed_p8 >= 0) {
        this.j797();
        this.unlink();
        return;
      }

      this.playhead_p8 = sampleLength - 1;
    }

    this.playhead_p8 += this.speed_p8 * len;
    if (this.loopDirection_idk < 0) {
      if (this.isLooped_idk) {
        if (this.speed_p8 < 0) {
          if (this.playhead_p8 >= loopStart_idfk) {
            return;
          }

          this.playhead_p8 = loopStart_idfk + loopStart_idfk - 1 - this.playhead_p8;
          this.speed_p8 = -this.speed_p8;
        }

        while (this.playhead_p8 >= loopEnd_idfk) {
          this.playhead_p8 = loopEnd_idfk + loopEnd_idfk - 1 - this.playhead_p8;
          this.speed_p8 = -this.speed_p8;
          if (this.playhead_p8 >= loopStart_idfk) {
            return;
          }

          this.playhead_p8 = loopStart_idfk + loopStart_idfk - 1 - this.playhead_p8;
          this.speed_p8 = -this.speed_p8;
        }

      } else if (this.speed_p8 < 0) {
        if (this.playhead_p8 >= loopStart_idfk) {
          return;
        }

        this.playhead_p8 = loopEnd_idfk - 1 - (loopEnd_idfk - 1 - this.playhead_p8) % loopLength_idk;
      } else {
        if (this.playhead_p8 < loopEnd_idfk) {
          return;
        }

        this.playhead_p8 = loopStart_idfk + (this.playhead_p8 - loopStart_idfk) % loopLength_idk;
      }
    } else {
      if (this.loopDirection_idk > 0) {
        label121:
        {
          if (this.speed_p8 < 0) {
            if (this.playhead_p8 >= loopStart_idfk) {
              return;
            }

            this.playhead_p8 = loopStart_idfk + loopStart_idfk - 1 - this.playhead_p8;
            this.speed_p8 = -this.speed_p8;
            if (--this.loopDirection_idk == 0) {
              break label121;
            }
          }

          do {
            if (this.playhead_p8 < loopEnd_idfk) {
              return;
            }

            this.playhead_p8 = loopEnd_idfk + loopEnd_idfk - 1 - this.playhead_p8;
            this.speed_p8 = -this.speed_p8;
            if (--this.loopDirection_idk == 0) {
              break;
            }

            if (this.playhead_p8 >= loopStart_idfk) {
              return;
            }

            this.playhead_p8 = loopStart_idfk + loopStart_idfk - 1 - this.playhead_p8;
            this.speed_p8 = -this.speed_p8;
          } while (--this.loopDirection_idk != 0);
        }
      }

      if (this.speed_p8 < 0) {
        if (this.playhead_p8 < 0) {
          this.playhead_p8 = -1;
          this.j797();
          this.unlink();
        }
      } else if (this.playhead_p8 >= sampleLength) {
        this.playhead_p8 = sampleLength;
        this.j797();
        this.unlink();
      }
    }
  }

  public synchronized boolean isPlayheadOutOfBounds() {
    return this.playhead_p8 < 0 || this.playhead_p8 >= this.rawSample.data_s8.length << 8;
  }

  public synchronized void a326(int time, final int volX, final int panX) {
    if (time == 0) {
      this.setVolAndPan_p14(volX, panX);
    } else {
      final int ampL = calcAmpL(volX, panX);
      final int ampR = calcAmpR(volX, panX);
      if (this.ampL_p14 == ampL && this.ampR_p14 == ampR) {
        this.ampTime = 0;
      } else {
        int var6 = volX - this.amp_p14;
        if (this.amp_p14 - volX > var6) {
          var6 = this.amp_p14 - volX;
        }

        if (ampL - this.ampL_p14 > var6) {
          var6 = ampL - this.ampL_p14;
        }

        if (this.ampL_p14 - ampL > var6) {
          var6 = this.ampL_p14 - ampL;
        }

        if (ampR - this.ampR_p14 > var6) {
          var6 = ampR - this.ampR_p14;
        }

        if (this.ampR_p14 - ampR > var6) {
          var6 = this.ampR_p14 - ampR;
        }

        if (time > var6) {
          time = var6;
        }

        this.ampTime = time;
        this.vol_p14 = volX;
        this.pan_p14 = panX;
        this.ampRate_p14 = (volX - this.amp_p14) / time;
        this.ampRateL_p14 = (ampL - this.ampL_p14) / time;
        this.ampRateR_p14 = (ampR - this.ampR_p14) / time;
      }
    }
  }

  @Override
  public @NotNull Iterator<AudioSource> iterator() {
    return Collections.emptyIterator();
  }

  @Override
  public int returns_0_1_or_2() {
    return this.vol_p14 == 0 && this.ampTime == 0 ? 0 : 1;
  }

  public synchronized void setPitchX(final int var1) {
    if (this.speed_p8 < 0) {
      this.speed_p8 = -var1;
    } else {
      this.speed_p8 = var1;
    }

  }

  @Override
  public synchronized void processAndWrite(final int[] dataS16P8, final int offset, final int len) {
    if (this.vol_p14 == 0 && this.ampTime == 0) {
      this.processAndDiscard(len);
      return;
    }

    final int loopStart = this.loopStart_idfk << 8;
    final int loopEnd = this.loopEnd_idfk << 8;
    final int sampleLength = this.rawSample.data_s8.length << 8;
    final int loopLength = loopEnd - loopStart;
    if (loopLength <= 0) {
      this.loopDirection_idk = 0;
    }

    int pos = offset;
    final int end = len + offset;
    if (this.playhead_p8 < 0) {
      if (this.speed_p8 <= 0) {
        this.j797();
        this.unlink();
        return;
      }

      this.playhead_p8 = 0;
    }

    if (this.playhead_p8 >= sampleLength) {
      if (this.speed_p8 >= 0) {
        this.j797();
        this.unlink();
        return;
      }

      this.playhead_p8 = sampleLength - 1;
    }

    if (this.loopDirection_idk < 0) {
      if (this.isLooped_idk) {
        if (this.speed_p8 < 0) {
          pos = this.writeAudioDataBackwards_idk(dataS16P8, offset, loopStart, end, this.rawSample.data_s8[this.loopStart_idfk]);
          if (this.playhead_p8 >= loopStart) {
            return;
          }

          this.playhead_p8 = loopStart + loopStart - 1 - this.playhead_p8;
          this.speed_p8 = -this.speed_p8;
        }

        while (true) {
          pos = this.writeAudioDataForwards_idk(dataS16P8, pos, loopEnd, end, this.rawSample.data_s8[this.loopEnd_idfk - 1]);
          if (this.playhead_p8 < loopEnd) {
            return;
          }

          this.playhead_p8 = loopEnd + loopEnd - 1 - this.playhead_p8;
          this.speed_p8 = -this.speed_p8;
          pos = this.writeAudioDataBackwards_idk(dataS16P8, pos, loopStart, end, this.rawSample.data_s8[this.loopStart_idfk]);
          if (this.playhead_p8 >= loopStart) {
            return;
          }

          this.playhead_p8 = loopStart + loopStart - 1 - this.playhead_p8;
          this.speed_p8 = -this.speed_p8;
        }
      } else if (this.speed_p8 < 0) {
        while (true) {
          pos = this.writeAudioDataBackwards_idk(dataS16P8, pos, loopStart, end, this.rawSample.data_s8[this.loopEnd_idfk - 1]);
          if (this.playhead_p8 >= loopStart) {
            return;
          }

          this.playhead_p8 = loopEnd - 1 - (loopEnd - 1 - this.playhead_p8) % loopLength;
        }
      } else {
        while (true) {
          pos = this.writeAudioDataForwards_idk(dataS16P8, pos, loopEnd, end, this.rawSample.data_s8[this.loopStart_idfk]);
          if (this.playhead_p8 < loopEnd) {
            return;
          }

          this.playhead_p8 = loopStart + (this.playhead_p8 - loopStart) % loopLength;
        }
      }
    } else {
      if (this.loopDirection_idk > 0) {
        if (this.isLooped_idk) {
          label131:
          {
            if (this.speed_p8 < 0) {
              pos = this.writeAudioDataBackwards_idk(dataS16P8, offset, loopStart, end, this.rawSample.data_s8[this.loopStart_idfk]);
              if (this.playhead_p8 >= loopStart) {
                return;
              }

              this.playhead_p8 = loopStart + loopStart - 1 - this.playhead_p8;
              this.speed_p8 = -this.speed_p8;
              if (--this.loopDirection_idk == 0) {
                break label131;
              }
            }

            do {
              pos = this.writeAudioDataForwards_idk(dataS16P8, pos, loopEnd, end, this.rawSample.data_s8[this.loopEnd_idfk - 1]);
              if (this.playhead_p8 < loopEnd) {
                return;
              }

              this.playhead_p8 = loopEnd + loopEnd - 1 - this.playhead_p8;
              this.speed_p8 = -this.speed_p8;
              if (--this.loopDirection_idk == 0) {
                break;
              }

              pos = this.writeAudioDataBackwards_idk(dataS16P8, pos, loopStart, end, this.rawSample.data_s8[this.loopStart_idfk]);
              if (this.playhead_p8 >= loopStart) {
                return;
              }

              this.playhead_p8 = loopStart + loopStart - 1 - this.playhead_p8;
              this.speed_p8 = -this.speed_p8;
            } while (--this.loopDirection_idk != 0);
          }
        } else {
          int var10;
          if (this.speed_p8 < 0) {
            while (true) {
              pos = this.writeAudioDataBackwards_idk(dataS16P8, pos, loopStart, end, this.rawSample.data_s8[this.loopEnd_idfk - 1]);
              if (this.playhead_p8 >= loopStart) {
                return;
              }

              var10 = (loopEnd - 1 - this.playhead_p8) / loopLength;
              if (var10 >= this.loopDirection_idk) {
                this.playhead_p8 += loopLength * this.loopDirection_idk;
                this.loopDirection_idk = 0;
                break;
              }

              this.playhead_p8 += loopLength * var10;
              this.loopDirection_idk -= var10;
            }
          } else {
            while (true) {
              pos = this.writeAudioDataForwards_idk(dataS16P8, pos, loopEnd, end, this.rawSample.data_s8[this.loopStart_idfk]);
              if (this.playhead_p8 < loopEnd) {
                return;
              }

              var10 = (this.playhead_p8 - loopStart) / loopLength;
              if (var10 >= this.loopDirection_idk) {
                this.playhead_p8 -= loopLength * this.loopDirection_idk;
                this.loopDirection_idk = 0;
                break;
              }

              this.playhead_p8 -= loopLength * var10;
              this.loopDirection_idk -= var10;
            }
          }
        }
      }

      if (this.speed_p8 < 0) {
        this.writeAudioDataBackwards_idk(dataS16P8, pos, 0, end, 0);
        if (this.playhead_p8 < 0) {
          this.playhead_p8 = -1;
          this.j797();
          this.unlink();
        }
      } else {
        this.writeAudioDataForwards_idk(dataS16P8, pos, sampleLength, end, 0);
        if (this.playhead_p8 >= sampleLength) {
          this.playhead_p8 = sampleLength;
          this.j797();
          this.unlink();
        }
      }
    }
  }

  @Override
  public int someP8_idk() {
    final int var1 = (this.amp_p14 * 3) >> 6;
    int var2 = (var1 ^ (var1 >> 31)) + (var1 >>> 31);
    if (this.loopDirection_idk == 0) {
      var2 -= var2 * this.playhead_p8 / (this.rawSample.data_s8.length << 8);
    } else if (this.loopDirection_idk >= 0) {
      var2 -= var2 * this.loopStart_idfk / this.rawSample.data_s8.length;
    }
    return Math.min(var2, 255);
  }

  private void j797() {
    if (this.ampTime != 0) {
      if (this.vol_p14 == Integer.MIN_VALUE) {
        this.vol_p14 = 0;
      }

      this.ampTime = 0;
      this.resetAmp();
    }
  }

  public synchronized int getPitchX() {
    return this.speed_p8 < 0 ? -this.speed_p8 : this.speed_p8;
  }

  private int writeAudioDataBackwards_idk(final int[] dest, int index, final int loopPoint, final int end, final int loopSample) {
    while (true) {
      if (this.ampTime > 0) {
        int var6 = index + this.ampTime;
        if (var6 > end) {
          var6 = end;
        }

        this.ampTime += index;
        if (this.speed_p8 == -256 && (this.playhead_p8 & 255) == 0) {
          index = xferBackRamped(this.rawSample.data_s8, dest, this.playhead_p8, index, this.ampL_p14, this.ampR_p14, this.ampRateL_p14, this.ampRateR_p14, var6, loopPoint, this);
        } else {
          index = xferBackRampedPitched(this.rawSample.data_s8, dest, this.playhead_p8, index, this.ampL_p14, this.ampR_p14, this.ampRateL_p14, this.ampRateR_p14, var6, loopPoint, this, this.speed_p8, loopSample);
        }

        this.ampTime -= index;
        if (this.ampTime != 0) {
          return index;
        }

        if (!this.updateAmpRate()) {
          continue;
        }

        return end;
      }

      if (this.speed_p8 == -256 && (this.playhead_p8 & 255) == 0) {
        return xferBack(this.rawSample.data_s8, dest, this.playhead_p8, index, this.ampL_p14, this.ampR_p14, end, loopPoint, this);

      }

      return xferBackPitched(this.rawSample.data_s8, dest, this.playhead_p8, index, this.ampL_p14, this.ampR_p14, end, loopPoint, this, this.speed_p8, loopSample);

    }
  }

  @SuppressWarnings("BooleanMethodIsAlwaysInverted")
  private boolean updateAmpRate() {
    int vol_p14 = this.vol_p14;
    final int ampL_p14;
    final int ampR_p14;

    if (vol_p14 == Integer.MIN_VALUE) {
      ampR_p14 = 0;
      ampL_p14 = 0;
      vol_p14 = 0;
    } else {
      ampL_p14 = calcAmpL(vol_p14, this.pan_p14);
      ampR_p14 = calcAmpR(vol_p14, this.pan_p14);
    }

    if (this.amp_p14 == vol_p14 && this.ampL_p14 == ampL_p14 && this.ampR_p14 == ampR_p14) {
      if (this.vol_p14 == Integer.MIN_VALUE) {
        this.vol_p14 = 0;
        this.unlink();
        return true;
      } else {
        this.resetAmp();
        return false;
      }
    }

    if (this.amp_p14 < vol_p14) {
      this.ampRate_p14 = 1;
      this.ampTime = vol_p14 - this.amp_p14;
    } else if (this.amp_p14 > vol_p14) {
      this.ampRate_p14 = -1;
      this.ampTime = this.amp_p14 - vol_p14;
    } else {
      this.ampRate_p14 = 0;
    }

    if (this.ampL_p14 < ampL_p14) {
      this.ampRateL_p14 = 1;
      if (this.ampTime == 0 || this.ampTime > ampL_p14 - this.ampL_p14) {
        this.ampTime = ampL_p14 - this.ampL_p14;
      }
    } else if (this.ampL_p14 > ampL_p14) {
      this.ampRateL_p14 = -1;
      if (this.ampTime == 0 || this.ampTime > this.ampL_p14 - ampL_p14) {
        this.ampTime = this.ampL_p14 - ampL_p14;
      }
    } else {
      this.ampRateL_p14 = 0;
    }

    if (this.ampR_p14 < ampR_p14) {
      this.ampRateR_p14 = 1;
      if (this.ampTime == 0 || this.ampTime > ampR_p14 - this.ampR_p14) {
        this.ampTime = ampR_p14 - this.ampR_p14;
      }
    } else if (this.ampR_p14 > ampR_p14) {
      this.ampRateR_p14 = -1;
      if (this.ampTime == 0 || this.ampTime > this.ampR_p14 - ampR_p14) {
        this.ampTime = this.ampR_p14 - ampR_p14;
      }
    } else {
      this.ampRateR_p14 = 0;
    }

    return false;
  }

  public synchronized int getPan_p14() {
    return this.pan_p14 < 0 ? -1 : this.pan_p14;
  }

  public synchronized void setVol_p14(final int vol_p14) {
    this.setVolAndPan_p14(vol_p14, this.getPan_p14());
  }

  public synchronized void f150() {
    this.loopDirection_idk = -1;
  }
}
