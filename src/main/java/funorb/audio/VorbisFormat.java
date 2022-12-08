package funorb.audio;

import funorb.cache.ResourceLoader;
import funorb.io.Buffer;
import funorb.util.BitMath;

import java.io.IOException;
import java.util.stream.IntStream;

public final class VorbisFormat {
  public static VorbisCodebook[] codebooks;
  private static float[] window;
  private static VorbisResidue[] residues;
  private static int[] modeMapping;
  private static int[] blocksize0Invbit;
  private static VorbisMapping[] mappings;
  private static float[] blocksize1Tbl3;
  private static float[] blocksize0Tbl3;
  private static boolean setupFinished_idk = false;
  private static byte[] buffer;
  private static float[] blocksize0Tbl2;
  private static boolean[] modeBlockFlag;
  private static float[] blocksize1Tbl2;
  private static float[] blocksize1Tbl1;
  private static int blocksize1;
  private static float[] blocksize0Tbl1;
  private static int curBit;
  private static int[] blocksize1Invbit;
  private static int blocksize0;
  private static VorbisFloor1[] floors;
  private static int curByte;
  private int sampleLength;
  private int loopEnd;
  private boolean isLooped;
  private int loopStart;
  private int lastWindowStart;
  private int outputOffset;
  private boolean lastWindowZero;
  private byte[][] packets;
  private int lastWindowN;
  private int sampleRate;
  private byte[] sampleData;
  private float[] lastWindow;
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
    window = new float[blocksize1];

    for (int i = 0; i < 2; ++i) {
      int n = i == 0 ? blocksize0 : blocksize1;
      int n2 = n >> 1;
      int n4 = n >> 2;
      int n8 = n >> 3;

      final float[] tbl1 = new float[n2];
      for (int j = 0; j < n4; ++j) {
        tbl1[2 * j] = (float) Math.cos((double) (4 * j) * Math.PI / (double) n);
        tbl1[2 * j + 1] = -((float) Math.sin((double) (4 * j) * Math.PI / (double) n));
      }

      final float[] tbl2 = new float[n2];
      for (int j = 0; j < n4; ++j) {
        tbl2[2 * j] = (float) Math.cos((double) (2 * j + 1) * Math.PI / (double) (2 * n));
        tbl2[2 * j + 1] = (float) Math.sin((double) (2 * j + 1) * Math.PI / (double) (2 * n));
      }

      final float[] tbl3 = new float[n4];
      for (int j = 0; j < n8; ++j) {
        tbl3[2 * j] = (float) Math.cos((double) (4 * j + 2) * Math.PI / (double) n);
        tbl3[2 * j + 1] = -((float) Math.sin((double) (4 * j + 2) * Math.PI / (double) n));
      }

      final int bits = BitMath.lastSet(n8 - 1);
      final int[] invbit = IntStream.range(0, n8).map(j -> reverseBits(j, bits)).toArray();

      if (i == 0) {
        blocksize0Tbl1 = tbl1;
        blocksize0Tbl2 = tbl2;
        blocksize0Tbl3 = tbl3;
        blocksize0Invbit = invbit;
      } else {
        blocksize1Tbl1 = tbl1;
        blocksize1Tbl2 = tbl2;
        blocksize1Tbl3 = tbl3;
        blocksize1Invbit = invbit;
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
    residues = new VorbisResidue[numResidues];
    for (int i = 0; i < numResidues; ++i) {
      residues[i] = new VorbisResidue();
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
      this.lastWindowN = 0;
      this.lastWindow = new float[blocksize1];
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

    this.lastWindow = null;
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

    // compute spectrum
    final VorbisMapping mapping = mappings[modeMapping[modeNumber]];
    final boolean thisWindowZero = !floors[mapping.floor[mapping.mux]].decode();
    for (int i = 0; i < mapping.submaps; ++i) {
      residues[mapping.residues[i]].computeResidue(window, n >> 1, thisWindowZero);
    }
    if (!thisWindowZero) {
      floors[mapping.floor[mapping.mux]].computeCurve(window, n >> 1);
    }

    // mdct and windowing
    if (thisWindowZero) {
      for (int i = n >> 1; i < n; ++i) {
        window[i] = 0.0F;
      }
    } else {
      final int n2 = n >> 1;
      final int n4 = n >> 2;
      final int n8 = n >> 3;
      final float[] v = window;

      for (int i = 0; i < n2; ++i) {
        v[i] *= 0.5F;
      }
      for (int i = n2; i < n; ++i) {
        v[i] = -v[n - i - 1];
      }

      final float[] tbl1 = isLongWindow ? blocksize1Tbl1 : blocksize0Tbl1;
      final float[] tbl2 = isLongWindow ? blocksize1Tbl2 : blocksize0Tbl2;
      final float[] tbl3 = isLongWindow ? blocksize1Tbl3 : blocksize0Tbl3;
      final int[] invbit = isLongWindow ? blocksize1Invbit : blocksize0Invbit;

      for (int i = 0; i < n4; ++i) {
        final float a = v[4 * i] - v[n - 4 * i - 1];
        final float b = v[4 * i + 2] - v[n - 4 * i - 3];
        final float c = tbl1[2 * i];
        final float d = tbl1[2 * i + 1];
        v[n - 4 * i - 1] = a * c - b * d;
        v[n - 4 * i - 3] = a * d + b * c;
      }

      for (int i = 0; i < n8; ++i) {
        final float a = v[n2 + 3 + 4 * i];
        final float b = v[n2 + 1 + 4 * i];
        final float c = v[4 * i + 3];
        final float d = v[4 * i + 1];
        v[n2 + 3 + 4 * i] = a + c;
        v[n2 + 1 + 4 * i] = b + d;
        final float e = tbl1[n2 - 4 - 4 * i];
        final float f = tbl1[n2 - 3 - 4 * i];
        v[4 * i + 3] = (a - c) * e - (b - d) * f;
        v[4 * i + 1] = (b - d) * e + (a - c) * f;
      }

      int bits = BitMath.lastSet(n - 1);

      for (int i = 0; i < bits - 3; ++i) {
        final int nI = n >> i + 2;
        final int coeff = 8 << i;

        for (int j = 0; j < 2 << i; ++j) {
          final int i0 = n - nI * 2 * j;
          final int i1 = n - nI * (2 * j + 1);

          for (int k = 0; k < n >> i + 4; ++k) {
            final int k4 = 4 * k;
            final float a = v[i0 - 1 - k4];
            final float b = v[i0 - 3 - k4];
            final float c = v[i1 - 1 - k4];
            final float d = v[i1 - 3 - k4];
            v[i0 - 1 - k4] = a + c;
            v[i0 - 3 - k4] = b + d;
            final float e = tbl1[k * coeff];
            final float f = tbl1[k * coeff + 1];
            v[i1 - 1 - k4] = (a - c) * e - (b - d) * f;
            v[i1 - 3 - k4] = (b - d) * e + (a - c) * f;
          }
        }
      }

      for (int i = 1; i < n8 - 1; ++i) {
        final int j = invbit[i];
        if (i < j) {
          final int i8 = 8 * i;
          final int j8 = 8 * j;
          float a;
          a = v[i8 + 1];
          v[i8 + 1] = v[j8 + 1];
          v[j8 + 1] = a;
          a = v[i8 + 3];
          v[i8 + 3] = v[j8 + 3];
          v[j8 + 3] = a;
          a = v[i8 + 5];
          v[i8 + 5] = v[j8 + 5];
          v[j8 + 5] = a;
          a = v[i8 + 7];
          v[i8 + 7] = v[j8 + 7];
          v[j8 + 7] = a;
        }
      }

      for (int i = 0; i < n2; ++i) {
        v[i] = v[2 * i + 1];
      }

      for (int i = 0; i < n8; ++i) {
        v[n - 1 - 2 * i] = v[4 * i];
        v[n - 2 - 2 * i] = v[4 * i + 1];
        v[n - n4 - 1 - 2 * i] = v[4 * i + 2];
        v[n - n4 - 2 - 2 * i] = v[4 * i + 3];
      }

      for (int i = 0; i < n8; ++i) {
        final float a = tbl3[2 * i];
        final float b = tbl3[2 * i + 1];
        final float c = v[n2 + 2 * i];
        final float d = v[n2 + 2 * i + 1];
        final float e = v[n - 2 - 2 * i];
        final float f = v[n - 1 - 2 * i];
        final float g = b * (c - e) + a * (d + f);
        v[n2 + 2 * i] = (c + e + g) * 0.5F;
        v[n - 2 - 2 * i] = (c + e - g) * 0.5F;
        final float h = b * (d + f) - a * (c - e);
        v[n2 + 2 * i + 1] = (d - f + h) * 0.5F;
        v[n - 1 - 2 * i] = (-d + f + h) * 0.5F;
      }

      for (int i = 0; i < n4; ++i) {
        v[i] = v[2 * i + n2] * tbl2[2 * i] + v[2 * i + 1 + n2] * tbl2[2 * i + 1];
        v[n2 - 1 - i] = v[2 * i + n2] * tbl2[2 * i + 1] - v[2 * i + 1 + n2] * tbl2[2 * i];
      }
      for (int i = 0; i < n4; ++i) {
        v[n - n4 + i] = -v[i];
      }
      for (int i = 0; i < n4; ++i) {
        v[i] = v[n4 + i];
      }
      for (int i = 0; i < n4; ++i) {
        v[n4 + i] = -v[n4 - i - 1];
      }
      for (int i = 0; i < n4; ++i) {
        v[n2 + i] = v[n - i - 1];
      }

      for (int i = leftWindowStart; i < leftWindowEnd; ++i) {
        final float a = (float) Math.sin(((double) (i - leftWindowStart) + 0.5D) / (double) leftN * 0.5D * Math.PI);
        window[i] *= (float) Math.sin(1.5707963267948966D * (double) a * (double) a);
      }

      for (int i = rightWindowStart; i < rightWindowEnd; ++i) {
        final float a = (float) Math.sin(((double) (i - rightWindowStart) + 0.5D) / (double) rightN * 0.5D * Math.PI + (Math.PI / 2));
        window[i] *= (float) Math.sin(1.5707963267948966D * (double) a * (double) a);
      }
    }

    // lapping
    float[] output = null;
    if (this.lastWindowN > 0) {
      output = new float[this.lastWindowN + n >> 2];
      if (!this.lastWindowZero) {
        for (int i = 0; i < this.lastWindowStart; ++i) {
          output[i] += this.lastWindow[(this.lastWindowN >> 1) + i];
        }
      }
      if (!thisWindowZero) {
        for (int i = leftWindowStart; i < n >> 1; ++i) {
          output[output.length - (n >> 1) + i] += window[i];
        }
      }
    }

    this.lastWindow = window;
    this.lastWindowN = n;
    this.lastWindowStart = rightWindowEnd - (n >> 1);
    this.lastWindowZero = thisWindowZero;

    return output;
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
    this.lastWindowN = 0;
    this.lastWindow = new float[blocksize1];
    for (int i = 0; i < this.packets.length; ++i) {
      this.decodeAudioPacket(i);
    }
  }
}
