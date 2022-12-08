package funorb.audio;

import funorb.cache.ResourceLoader;
import funorb.io.Buffer;
import funorb.util.BitMath;

import java.io.IOException;
import java.util.stream.IntStream;

public final class VorbisFormat {
  public static VorbisCodebook[] codebooks;
  private static float[] _k;
  private static VorbisResidues[] residues;
  private static int[] modeMapping;
  private static int[] _D;
  private static VorbisMapping[] mappings;
  private static float[] _u;
  private static float[] _C;
  private static boolean setupFinished_idk = false;
  private static byte[] buffer;
  private static float[] _z;
  private static boolean[] modeBlockFlag;
  private static float[] _y;
  private static float[] _j;
  private static int blocksize1;
  private static float[] _l;
  private static int curBit;
  private static int[] _F;
  private static int blocksize0;
  private static VorbisFloor1[] floors;
  private static int curByte;
  private int sampleLength;
  private int loopEnd;
  private boolean isLooped;
  private int loopStart;
  private int _i;
  private int outputOffset;
  private boolean _A;
  private byte[][] packets;
  private int _M;
  private int sampleRate;
  private byte[] sampleData;
  private float[] prevWindow;
  private int packet;

  private VorbisFormat(final byte[] data) throws IOException {
    this.load(data);
  }

  public static float float32Unpack(final int value) {
    int significand = value & 0x1fffff;
    final int sign = value & Integer.MIN_VALUE;
    final int exponent = (value & 0x7fe00000) >> 21;
    if (sign != 0) {
      significand = -significand;
    }
    return (float) ((double) significand * Math.pow(2.0D, exponent - 788));
  }

  public static int readBit() {
    final int bit = buffer[curByte] >> curBit & 1;
    ++curBit;
    curByte += curBit >> 3;
    curBit &= 7;
    return bit;
  }

  public static void readIdentificationAndSetup(final byte[] section) {
    setCurrentPacket(section);
    blocksize0 = 1 << readBits(4);
    blocksize1 = 1 << readBits(4);
    _k = new float[blocksize1];

    for (int i = 0; i < 2; ++i) {
      int var2 = i != 0 ? blocksize1 : blocksize0;
      int var3 = var2 >> 1;
      int var4 = var2 >> 2;
      int var5 = var2 >> 3;
      final float[] var6 = new float[var3];

      for (int var7 = 0; var7 < var4; ++var7) {
        var6[2 * var7] = (float) Math.cos((double) (4 * var7) * Math.PI / (double) var2);
        var6[2 * var7 + 1] = -((float) Math.sin((double) (4 * var7) * Math.PI / (double) var2));
      }

      final float[] var13 = new float[var3];

      for (int var8 = 0; var8 < var4; ++var8) {
        var13[2 * var8] = (float) Math.cos((double) (2 * var8 + 1) * Math.PI / (double) (2 * var2));
        var13[2 * var8 + 1] = (float) Math.sin((double) (2 * var8 + 1) * Math.PI / (double) (2 * var2));
      }

      final float[] var14 = new float[var4];

      for (int var9 = 0; var9 < var5; ++var9) {
        var14[2 * var9] = (float) Math.cos((double) (4 * var9 + 2) * Math.PI / (double) var2);
        var14[2 * var9 + 1] = -((float) Math.sin((double) (4 * var9 + 2) * Math.PI / (double) var2));
      }

      final int var10 = BitMath.lastSet(var5 - 1);
      final int[] var15 = IntStream.range(0, var5).map(var11 -> reverseBits(var11, var10)).toArray();

      if (i == 0) {
        _l = var6;
        _z = var13;
        _C = var14;
        _D = var15;
      } else {
        _j = var6;
        _y = var13;
        _u = var14;
        _F = var15;
      }
    }

    int numCodebooks = readBits(8) + 1;
    codebooks = new VorbisCodebook[numCodebooks];
    for (int i = 0; i < numCodebooks; ++i) {
      codebooks[i] = new VorbisCodebook();
    }

    int numVorbisTimes = readBits(6) + 1;
    for (int i = 0; i < numVorbisTimes; ++i) {
      readBits(16);
    }

    int numFloors = readBits(6) + 1;
    floors = new VorbisFloor1[numFloors];
    for (int i = 0; i < numFloors; ++i) {
      floors[i] = new VorbisFloor1();
    }

    int numResidues = readBits(6) + 1;
    residues = new VorbisResidues[numResidues];
    for (int i = 0; i < numResidues; ++i) {
      residues[i] = new VorbisResidues();
    }

    int numMappings = readBits(6) + 1;
    mappings = new VorbisMapping[numMappings];
    for (int i = 0; i < numMappings; ++i) {
      mappings[i] = new VorbisMapping();
    }

    int numModes = readBits(6) + 1;
    modeBlockFlag = new boolean[numModes];
    modeMapping = new int[numModes];

    for (int i = 0; i < numModes; ++i) {
      modeBlockFlag[i] = readBit() != 0;
      readBits(16); // window type
      readBits(16); // transform type
      modeMapping[i] = readBits(8);
    }

    setupFinished_idk = true;
  }

  @SuppressWarnings("SameParameterValue")
  public static VorbisFormat loadAudio(final ResourceLoader loader, final String group, final String item) {
    if (loadSetup(loader)) {
      final byte[] data = loader.getResource(group, item);
      if (data == null) {
        return null;
      } else {
        VorbisFormat vorbis = null;
        try {
          vorbis = new VorbisFormat(data);
        } catch (final IOException e) {
          e.printStackTrace();
        }
        return vorbis;
      }
    } else {
      loader.loadGroupDataForItem(group, item);
      return null;
    }
  }

  private static boolean loadSetup(final ResourceLoader loader) {
    if (!setupFinished_idk) {
      final byte[] data = loader.getResource(0, 0);
      if (data == null) {
        return false;
      }
      readIdentificationAndSetup(data);
    }
    return true;
  }

  private static void setCurrentPacket(final byte[] packet) {
    buffer = packet;
    curByte = 0;
    curBit = 0;
  }

  public static int readBits(int numBits) {
    int result = 0;

    int bitsUsed;
    int bitsLeft;
    for (bitsUsed = 0; 8 - curBit <= numBits; numBits -= bitsLeft) {
      bitsLeft = 8 - curBit;
      final int mask = (1 << bitsLeft) - 1;
      result += (buffer[curByte] >> curBit & mask) << bitsUsed;
      curBit = 0;
      ++curByte;
      bitsUsed += bitsLeft;
    }

    if (numBits > 0) {
      bitsLeft = (1 << numBits) - 1;
      result += (buffer[curByte] >> curBit & bitsLeft) << bitsUsed;
      curBit += numBits;
    }

    return result;
  }

  public static VorbisFormat load(final ResourceLoader loader, final int groupId, final int itemId) {
    if (loadSetup(loader)) {
      final byte[] data = loader.getResource(groupId, itemId);
      if (data == null) {
        return null;
      } else {
        VorbisFormat vorbis = null;
        try {
          vorbis = new VorbisFormat(data);
        } catch (final IOException e) {
          e.printStackTrace();
        }
        return vorbis;
      }
    } else {
      loader.loadGroupDataForItem(groupId, itemId);
      return null;
    }
  }

  private static int reverseBits(int value, int count) {
    int i = 0;
    for (; count > 0; --count) {
      i = (i << 1) | (1 & value);
      value >>>= 1;
    }
    return i;
  }

  public RawSampleS8 toRawSample() {
    if (this.sampleData == null) {
      this._M = 0;
      this.prevWindow = new float[blocksize1];
      this.sampleData = new byte[this.sampleLength];
      this.outputOffset = 0;
      this.packet = 0;
    }

    for (; this.packet < this.packets.length; ++this.packet) {
      final float[] frame = this.decodeAudioPacket(this.packet);
      if (frame != null) {
        int offset = this.outputOffset;
        int len = frame.length;
        if (len > this.sampleLength - offset) {
          len = this.sampleLength - offset;
        }
        for (int i = 0; i < len; ++i) {
          int sample = (int) (128.0F + frame[i] * 128.0F);
          if ((sample & -256) != 0) {
            sample = ~sample >> 31;
          }
          this.sampleData[offset++] = (byte) (sample - 128);
        }
        this.outputOffset = offset;
      }
    }

    this.prevWindow = null;
    final byte[] sampleData = this.sampleData;
    this.sampleData = null;
    return new RawSampleS8(this.sampleRate, sampleData, this.loopStart, this.loopEnd, this.isLooped);
  }

  private float[] decodeAudioPacket(final int packet) {
    setCurrentPacket(this.packets[packet]);
    readBit(); // packet type (assumed audio)

    final int modeNumber = readBits(BitMath.lastSet(modeMapping.length - 1));
    final boolean isLongWindow = VorbisFormat.modeBlockFlag[modeNumber];
    final int n = isLongWindow ? blocksize1 : blocksize0;

    boolean prevWindowFlag = false;
    boolean nextWindowFlag = false;
    if (isLongWindow) {
      prevWindowFlag = readBit() != 0;
      nextWindowFlag = readBit() != 0;
    }

    final int windowCenter = n >> 1;

    final int leftWindowStart;
    final int leftWindowEnd;
    final int leftN;
    if (isLongWindow && !prevWindowFlag) {
      leftWindowStart = (n >> 2) - (blocksize0 >> 2);
      leftWindowEnd = (n >> 2) + (blocksize0 >> 2);
      leftN = blocksize0 >> 1;
    } else {
      leftWindowStart = 0;
      leftWindowEnd = windowCenter;
      leftN = n >> 1;
    }

    final int rightWindowStart;
    final int rightWindowEnd;
    final int rightN;
    if (isLongWindow && !nextWindowFlag) {
      rightWindowStart = n - (n >> 2) - (blocksize0 >> 2);
      rightWindowEnd = n - (n >> 2) + (blocksize0 >> 2);
      rightN = blocksize0 >> 1;
    } else {
      rightWindowStart = windowCenter;
      rightWindowEnd = n;
      rightN = n >> 1;
    }

    final VorbisMapping mapping = mappings[modeMapping[modeNumber]];
    final boolean var15 = !floors[mapping.floor[mapping.mux]].decode();

    for (int i = 0; i < mapping.submaps; ++i) {
      final VorbisResidues residue = residues[mapping.residues[i]];
      final float[] var19 = _k;
      residue.a623(var19, n >> 1, var15);
    }

    if (!var15) {
      floors[mapping.floor[mapping.mux]].computeCurve(_k, n >> 1);
    }

    int var42;
    if (var15) {
      for (int var17 = n >> 1; var17 < n; ++var17) {
        _k[var17] = 0.0F;
      }
    } else {
      final int i = n >> 1;
      int var41 = n >> 2;
      var42 = n >> 3;
      final float[] var20 = _k;

      int var21;
      for (var21 = 0; var21 < i; ++var21) {
        var20[var21] *= 0.5F;
      }

      for (var21 = i; var21 < n; ++var21) {
        var20[var21] = -var20[n - var21 - 1];
      }

      final float[] var46 = isLongWindow ? _j : _l;
      final float[] var22 = isLongWindow ? _y : _z;
      final float[] var23 = isLongWindow ? _u : _C;
      final int[] var24 = isLongWindow ? _F : _D;

      int var25;
      float var26;
      float var27;
      float var28;
      float var29;
      for (var25 = 0; var25 < var41; ++var25) {
        var26 = var20[4 * var25] - var20[n - 4 * var25 - 1];
        var27 = var20[4 * var25 + 2] - var20[n - 4 * var25 - 3];
        var28 = var46[2 * var25];
        var29 = var46[2 * var25 + 1];
        var20[n - 4 * var25 - 1] = var26 * var28 - var27 * var29;
        var20[n - 4 * var25 - 3] = var26 * var29 + var27 * var28;
      }

      float var30;
      float var31;
      for (var25 = 0; var25 < var42; ++var25) {
        var26 = var20[i + 3 + 4 * var25];
        var27 = var20[i + 1 + 4 * var25];
        var28 = var20[4 * var25 + 3];
        var29 = var20[4 * var25 + 1];
        var20[i + 3 + 4 * var25] = var26 + var28;
        var20[i + 1 + 4 * var25] = var27 + var29;
        var30 = var46[i - 4 - 4 * var25];
        var31 = var46[i - 3 - 4 * var25];
        var20[4 * var25 + 3] = (var26 - var28) * var30 - (var27 - var29) * var31;
        var20[4 * var25 + 1] = (var27 - var29) * var30 + (var26 - var28) * var31;
      }

      var25 = BitMath.lastSet(n - 1);

      int var47;
      int var48;
      int var49;
      int var50;
      for (var47 = 0; var47 < var25 - 3; ++var47) {
        var48 = n >> var47 + 2;
        var49 = 8 << var47;

        for (var50 = 0; var50 < 2 << var47; ++var50) {
          final int var51 = n - var48 * 2 * var50;
          final int var52 = n - var48 * (2 * var50 + 1);

          for (int var32 = 0; var32 < n >> var47 + 4; ++var32) {
            final int var33 = 4 * var32;
            final float var34 = var20[var51 - 1 - var33];
            final float var35 = var20[var51 - 3 - var33];
            final float var36 = var20[var52 - 1 - var33];
            final float var37 = var20[var52 - 3 - var33];
            var20[var51 - 1 - var33] = var34 + var36;
            var20[var51 - 3 - var33] = var35 + var37;
            final float var38 = var46[var32 * var49];
            final float var39 = var46[var32 * var49 + 1];
            var20[var52 - 1 - var33] = (var34 - var36) * var38 - (var35 - var37) * var39;
            var20[var52 - 3 - var33] = (var35 - var37) * var38 + (var34 - var36) * var39;
          }
        }
      }

      for (var47 = 1; var47 < var42 - 1; ++var47) {
        var48 = var24[var47];
        if (var47 < var48) {
          var49 = 8 * var47;
          var50 = 8 * var48;
          var30 = var20[var49 + 1];
          var20[var49 + 1] = var20[var50 + 1];
          var20[var50 + 1] = var30;
          var30 = var20[var49 + 3];
          var20[var49 + 3] = var20[var50 + 3];
          var20[var50 + 3] = var30;
          var30 = var20[var49 + 5];
          var20[var49 + 5] = var20[var50 + 5];
          var20[var50 + 5] = var30;
          var30 = var20[var49 + 7];
          var20[var49 + 7] = var20[var50 + 7];
          var20[var50 + 7] = var30;
        }
      }

      for (var47 = 0; var47 < i; ++var47) {
        var20[var47] = var20[2 * var47 + 1];
      }

      for (var47 = 0; var47 < var42; ++var47) {
        var20[n - 1 - 2 * var47] = var20[4 * var47];
        var20[n - 2 - 2 * var47] = var20[4 * var47 + 1];
        var20[n - var41 - 1 - 2 * var47] = var20[4 * var47 + 2];
        var20[n - var41 - 2 - 2 * var47] = var20[4 * var47 + 3];
      }

      for (var47 = 0; var47 < var42; ++var47) {
        var27 = var23[2 * var47];
        var28 = var23[2 * var47 + 1];
        var29 = var20[i + 2 * var47];
        var30 = var20[i + 2 * var47 + 1];
        var31 = var20[n - 2 - 2 * var47];
        final float var53 = var20[n - 1 - 2 * var47];
        final float var54 = var28 * (var29 - var31) + var27 * (var30 + var53);
        var20[i + 2 * var47] = (var29 + var31 + var54) * 0.5F;
        var20[n - 2 - 2 * var47] = (var29 + var31 - var54) * 0.5F;
        final float v = var28 * (var30 + var53) - var27 * (var29 - var31);
        var20[i + 2 * var47 + 1] = (var30 - var53 + v) * 0.5F;
        var20[n - 1 - 2 * var47] = (-var30 + var53 + v) * 0.5F;
      }

      for (var47 = 0; var47 < var41; ++var47) {
        var20[var47] = var20[2 * var47 + i] * var22[2 * var47] + var20[2 * var47 + 1 + i] * var22[2 * var47 + 1];
        var20[i - 1 - var47] = var20[2 * var47 + i] * var22[2 * var47 + 1] - var20[2 * var47 + 1 + i] * var22[2 * var47];
      }

      for (var47 = 0; var47 < var41; ++var47) {
        var20[n - var41 + var47] = -var20[var47];
      }

      for (var47 = 0; var47 < var41; ++var47) {
        var20[var47] = var20[var41 + var47];
      }

      for (var47 = 0; var47 < var41; ++var47) {
        var20[var41 + var47] = -var20[var41 - var47 - 1];
      }

      for (var47 = 0; var47 < var41; ++var47) {
        var20[i + var47] = var20[n - var47 - 1];
      }

      float[] var10000;
      for (var47 = leftWindowStart; var47 < leftWindowEnd; ++var47) {
        var27 = (float) Math.sin(((double) (var47 - leftWindowStart) + 0.5D) / (double) leftN * 0.5D * Math.PI);
        var10000 = _k;
        var10000[var47] *= (float) Math.sin(1.5707963267948966D * (double) var27 * (double) var27);
      }

      for (var47 = rightWindowStart; var47 < rightWindowEnd; ++var47) {
        var27 = (float) Math.sin(((double) (var47 - rightWindowStart) + 0.5D) / (double) rightN * 0.5D * Math.PI + (Math.PI / 2));
        var10000 = _k;
        var10000[var47] *= (float) Math.sin(1.5707963267948966D * (double) var27 * (double) var27);
      }
    }

    float[] var43 = null;
    if (this._M > 0) {
      var43 = new float[this._M + n >> 2];
      int var45;
      if (!this._A) {
        for (var42 = 0; var42 < this._i; ++var42) {
          var45 = (this._M >> 1) + var42;
          var43[var42] += this.prevWindow[var45];
        }
      }

      if (!var15) {
        for (var42 = leftWindowStart; var42 < n >> 1; ++var42) {
          var45 = var43.length - (n >> 1) + var42;
          var43[var45] += _k[var42];
        }
      }
    }

    final float[] var44 = this.prevWindow;
    this.prevWindow = _k;
    _k = var44;
    this._M = n;
    this._i = rightWindowEnd - (n >> 1);
    this._A = var15;
    return var43;
  }

  private void load(final byte[] data) throws IOException {
    final Buffer buf = new Buffer(data);
    this.sampleRate = buf.readInt();
    this.sampleLength = buf.readInt();
    this.loopStart = buf.readInt();
    this.loopEnd = buf.readInt();
    if (this.loopEnd < 0) {
      this.loopEnd = ~this.loopEnd;
      this.isLooped = true;
    }

    final int count = buf.readInt();
    if (count < 0) {
      throw new IOException();
    }

    this.packets = new byte[count][];
    for (int i = 0; i < count; ++i) {
      int total = 0;
      int x;
      do {
        x = buf.readUByte();
        total += x;
      } while (x >= 255);
      final byte[] packet = new byte[total];
      buf.readBytes(packet, total);
      this.packets[i] = packet;
    }
  }

  public void b720() {
    this._M = 0;
    this.prevWindow = new float[blocksize1];
    for (int i = 0; i < this.packets.length; ++i) {
      this.decodeAudioPacket(i);
    }
  }
}
