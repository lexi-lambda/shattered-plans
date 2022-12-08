package funorb.audio;

public final class VorbisResidues {
  // vorbis spec uses a [resClassifications][8] array of ints, but resBook
  // is a flattened array containing [resClassifications*8] entries
  private final int[] resBooks;
  private final int _c;
  private final int resBegin;
  private final int resEnd;
  private final int resPartitionSize;
  private final int resClassifications;
  private final int resClassbook;

  public VorbisResidues() {
    this._c = VorbisFormat.readBits(16);
    this.resBegin = VorbisFormat.readBits(24);
    this.resEnd = VorbisFormat.readBits(24);
    this.resPartitionSize = VorbisFormat.readBits(24) + 1;
    this.resClassifications = VorbisFormat.readBits(6) + 1;
    this.resClassbook = VorbisFormat.readBits(8);

    final int[] resCascade = new int[this.resClassifications];
    for (int i = 0; i < this.resClassifications; ++i) {
      int highBits = 0;
      final int lowBits = VorbisFormat.readBits(3);
      final boolean bitflag = VorbisFormat.readBit() != 0;
      if (bitflag) {
        highBits = VorbisFormat.readBits(5);
      }
      resCascade[i] = highBits << 3 | lowBits;
    }

    this.resBooks = new int[this.resClassifications * 8];
    for (int i = 0; i < this.resClassifications * 8; ++i) {
      this.resBooks[i] = (resCascade[i >> 3] & 1 << (i & 7)) != 0
        ? VorbisFormat.readBits(8)
        : -1;
    }
  }

  public void a623(final float[] var1, final int var2, final boolean var3) {
    int var4;
    for (var4 = 0; var4 < var2; ++var4) {
      var1[var4] = 0.0F;
    }

    if (!var3) {
      var4 = VorbisFormat.codebooks[this.resClassbook].cbDim;
      final int var5 = this.resEnd - this.resBegin;
      final int var6 = var5 / this.resPartitionSize;
      final int[] var7 = new int[var6];

      for (int var8 = 0; var8 < 8; ++var8) {
        int var9 = 0;

        while (var9 < var6) {
          int var10;
          int var11;
          if (var8 == 0) {
            var10 = VorbisFormat.codebooks[this.resClassbook].a784();

            for (var11 = var4 - 1; var11 >= 0; --var11) {
              if (var9 + var11 < var6) {
                var7[var9 + var11] = var10 % this.resClassifications;
              }

              var10 /= this.resClassifications;
            }
          }

          for (var10 = 0; var10 < var4; ++var10) {
            var11 = var7[var9];
            final int var12 = this.resBooks[var11 * 8 + var8];
            if (var12 >= 0) {
              final int var13 = this.resBegin + var9 * this.resPartitionSize;
              final VorbisCodebook var14 = VorbisFormat.codebooks[var12];
              int var15;
              if (this._c == 0) {
                var15 = this.resPartitionSize / var14.cbDim;

                for (int var19 = 0; var19 < var15; ++var19) {
                  final float[] var20 = var14.c932();

                  for (int var18 = 0; var18 < var14.cbDim; ++var18) {
                    var1[var13 + var19 + var18 * var15] += var20[var18];
                  }
                }
              } else {
                var15 = 0;

                while (var15 < this.resPartitionSize) {
                  final float[] var16 = var14.c932();

                  for (int var17 = 0; var17 < var14.cbDim; ++var17) {
                    var1[var13 + var15] += var16[var17];
                    ++var15;
                  }
                }
              }
            }

            ++var9;
            if (var9 >= var6) {
              break;
            }
          }
        }
      }

    }
  }
}
