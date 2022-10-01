package funorb.cache;

import funorb.io.Buffer;
import funorb.io.Bzip2;
import funorb.io.Inflater;
import funorb.util.ArrayUtil;
import funorb.util.Whirlpool;

public final class PageIndex {
  public ResourceDirectory groupDirectory;
  public ResourceDirectory[] itemDirectories;
  public final int pageVersion;
  public final int groupCount;
  public final int[] groupIndexes;
  public final int[] groupSizes;
  public final int[] itemCounts;
  public final int[][] itemIdMaps;
  public byte[][] groupWhirlpoolHashes;
  public final int[] _v;
  public final int[] groupCrcs;

  public PageIndex(final byte[] data, final int expectedCrc, final byte[] expectedWhirlpool) {
    final int crc = Buffer.computeCrc(data, data.length);
    if (crc != expectedCrc) {
      throw new RuntimeException();
    }
    if (expectedWhirlpool != null) {
      if (expectedWhirlpool.length != 64) {
        throw new RuntimeException();
      }
      Whirlpool.checkHash(data, 0, data.length, expectedWhirlpool);
    }

    final Buffer buffer = new Buffer(decompress(data));
    final int var4 = buffer.readUByte();
    if (var4 < 5 || var4 > 7) {
      throw new RuntimeException();
    }

    if (var4 >= 6) {
      this.pageVersion = buffer.readInt();
    } else {
      this.pageVersion = 0;
    }

    final int flags = buffer.readUByte();
    final boolean var6 = (flags & 1) != 0;
    final boolean hasWhirlpool = (flags & 2) != 0;

    final int len = var4 == 7 ? buffer.readVariable16_32() : buffer.readUShort();
    int var8 = 0;
    this.groupIndexes = new int[len];
    int highestEntryIndex = -1;
    if (var4 == 7) {
      for (int i = 0; i < len; ++i) {
        this.groupIndexes[i] = var8 += buffer.readVariable16_32();
        if (highestEntryIndex < this.groupIndexes[i]) {
          highestEntryIndex = this.groupIndexes[i];
        }
      }
    } else {
      for (int i = 0; i < len; ++i) {
        this.groupIndexes[i] = var8 += buffer.readUShort();
        if (highestEntryIndex < this.groupIndexes[i]) {
          highestEntryIndex = this.groupIndexes[i];
        }
      }
    }

    this.groupCount = highestEntryIndex + 1;
    this.itemIdMaps = new int[this.groupCount][];
    this.itemCounts = new int[this.groupCount];
    this.groupSizes = new int[this.groupCount];
    this.groupCrcs = new int[this.groupCount];
    if (hasWhirlpool) {
      this.groupWhirlpoolHashes = new byte[this.groupCount][];
    }

    this._v = new int[this.groupCount];
    if (var6) {
      final int[] _s = ArrayUtil.create(this.groupCount, -1);
      for (int i = 0; i < len; ++i) {
        _s[this.groupIndexes[i]] = buffer.readInt();
      }
      this.groupDirectory = new ResourceDirectory(_s);
    }

    for (int i = 0; i < len; ++i) {
      this.groupCrcs[this.groupIndexes[i]] = buffer.readInt();
    }

    if (hasWhirlpool) {
      for (int i = 0; len > i; ++i) {
        final byte[] var11 = new byte[64];
        buffer.readBytes(var11, 64);
        this.groupWhirlpoolHashes[this.groupIndexes[i]] = var11;
      }
    }

    for (int i = 0; i < len; ++i) {
      this._v[this.groupIndexes[i]] = buffer.readInt();
    }

    if (var4 == 7) {
      for (int i = 0; len > i; ++i) {
        this.itemCounts[this.groupIndexes[i]] = buffer.readVariable16_32();
      }

      for (int i = 0; i < len; ++i) {
        final int var16 = this.groupIndexes[i];
        final int var12 = this.itemCounts[var16];
        var8 = 0;
        this.itemIdMaps[var16] = new int[var12];
        int var13 = -1;

        for (int j = 0; var12 > j; ++j) {
          final int var15 = this.itemIdMaps[var16][j] = var8 += buffer.readVariable16_32();
          if (var15 > var13) {
            var13 = var15;
          }
        }

        this.groupSizes[var16] = var13 + 1;
        if (var12 == var13 + 1) {
          this.itemIdMaps[var16] = null;
        }
      }
    } else {
      for (int i = 0; i < len; ++i) {
        this.itemCounts[this.groupIndexes[i]] = buffer.readUShort();
      }

      for (int i = 0; i < len; ++i) {
        final int var16 = this.groupIndexes[i];
        var8 = 0;
        final int var12 = this.itemCounts[var16];
        int var13 = -1;
        this.itemIdMaps[var16] = new int[var12];

        for (int var14 = 0; var12 > var14; ++var14) {
          final int var15 = this.itemIdMaps[var16][var14] = var8 += buffer.readUShort();
          if (var13 < var15) {
            var13 = var15;
          }
        }

        this.groupSizes[var16] = 1 + var13;
        if (var13 + 1 == var12) {
          this.itemIdMaps[var16] = null;
        }
      }
    }

    if (var6) {
      final int[][] _t = new int[1 + highestEntryIndex][];
      this.itemDirectories = new ResourceDirectory[highestEntryIndex + 1];

      for (int i = 0; len > i; ++i) {
        final int var16 = this.groupIndexes[i];
        final int var12 = this.itemCounts[var16];
        _t[var16] = new int[this.groupSizes[var16]];

        for (int var13 = 0; this.groupSizes[var16] > var13; ++var13) {
          _t[var16][var13] = -1;
        }

        for (int var13 = 0; var13 < var12; ++var13) {
          final int var14;
          if (this.itemIdMaps[var16] == null) {
            var14 = var13;
          } else {
            var14 = this.itemIdMaps[var16][var13];
          }

          _t[var16][var14] = buffer.readInt();
        }

        this.itemDirectories[var16] = new ResourceDirectory(_t[var16]);
      }
    }
  }

  public static byte[] decompress(final byte[] data) {
    final Buffer buffer = new Buffer(data);
    final int compressionFormat = buffer.readUByte();
    final int dataLen = buffer.readInt();
    if (dataLen < 0) {
      throw new RuntimeException("negative data length");
    }

    if (compressionFormat == 0) {
      final byte[] result = new byte[dataLen];
      buffer.readBytes(result);
      return result;
    }

    final int compressedLen = buffer.readInt();
    if (compressedLen < 0) {
      throw new RuntimeException("negative compressed data length");
    }
    final byte[] result = new byte[compressedLen];
    if (compressionFormat == 1) {
      Bzip2.decompress(data, result);
    } else {
      Inflater.inflate(buffer, result);
    }
    return result;
  }
}
