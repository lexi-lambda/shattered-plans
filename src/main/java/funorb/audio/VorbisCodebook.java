package funorb.audio;

import funorb.util.BitMath;

public final class VorbisCodebook {
  public final int cbDim;
  private final int cbSize;
  private final int[] cwLengths;
  private float[][] vectors;
  private int[] _f;

  public VorbisCodebook() {
    VorbisFormat.readBits(24);
    this.cbDim = VorbisFormat.readBits(16);
    this.cbSize = VorbisFormat.readBits(24);
    this.cwLengths = new int[this.cbSize];

    final boolean ordered = VorbisFormat.readBit() != 0;
    if (ordered) {
      int curEntry = 0;
      int curLength = VorbisFormat.readBits(5) + 1;
      while (curEntry < this.cbSize) {
        final int number = VorbisFormat.readBits(BitMath.lastSet(this.cbSize - curEntry));
        for (int i = 0; i < number; ++i) {
          this.cwLengths[curEntry++] = curLength;
        }
        curLength++;
      }
    } else {
      final boolean sparse = VorbisFormat.readBit() != 0;
      for (int i = 0; i < this.cbSize; ++i) {
        if (sparse && VorbisFormat.readBit() == 0) {
          this.cwLengths[i] = 0;
        } else {
          this.cwLengths[i] = VorbisFormat.readBits(5) + 1;
        }
      }
    }

    this.b797();

    int lookupTableType = VorbisFormat.readBits(4);
    if (lookupTableType > 0) {
      final float cbMinValue = VorbisFormat.float32Unpack(VorbisFormat.readBits(32));
      final float cbDeltaValue = VorbisFormat.float32Unpack(VorbisFormat.readBits(32));
      int cbValueBits = VorbisFormat.readBits(4) + 1;
      final boolean cbSequenceP = VorbisFormat.readBit() != 0;

      final int lookupValues;
      if (lookupTableType == 1) {
        lookupValues = lookup1Values(this.cbSize, this.cbDim);
      } else {
        lookupValues = this.cbSize * this.cbDim;
      }

      final int[] cbMultiplicands = new int[lookupValues];
      for (int i = 0; i < lookupValues; ++i) {
        cbMultiplicands[i] = VorbisFormat.readBits(cbValueBits);
      }

      this.vectors = new float[this.cbSize][this.cbDim];
      float last;
      int indexDiv;

      if (lookupTableType == 1) {
        for (int i = 0; i < this.cbSize; ++i) {
          last = 0.0F;
          indexDiv = 1;
          for (int j = 0; j < this.cbDim; ++j) {
            final int offset = i / indexDiv % lookupValues;
            final float value = (float) cbMultiplicands[offset] * cbDeltaValue + cbMinValue + last;
            this.vectors[i][j] = value;
            if (cbSequenceP) {
              last = value;
            }
            indexDiv *= lookupValues;
          }
        }
      } else {
        for (int i = 0; i < this.cbSize; ++i) {
          last = 0.0F;
          indexDiv = i * this.cbDim;
          for (int j = 0; j < this.cbDim; ++j) {
            final float value = (float) cbMultiplicands[indexDiv] * cbDeltaValue + cbMinValue + last;
            this.vectors[i][j] = value;
            if (cbSequenceP) {
              last = value;
            }
            ++indexDiv;
          }
        }
      }
    }

  }

  private static int lookup1Values(final int cbSize, final int cbDim) {
    int res = (int) Math.pow(cbSize, 1.0D / (double) cbDim) + 1;
    while (pow(res, cbDim) > cbSize) {
      --res;
    }
    return res;
  }

  private static int pow(int base, int exp) {
    int res;

    for (res = 1; exp > 1; base *= base) {
      if ((exp & 1) != 0) {
        res *= base;
      }
      exp >>= 1;
    }

    if (exp == 1) {
      return res * base;
    } else {
      return res;
    }
  }

  public float[] c932() {
    return this.vectors[this.a784()];
  }

  public int a784() {
    int var1 = 0;
    while (this._f[var1] >= 0) {
      var1 = VorbisFormat.readBit() != 0 ? this._f[var1] : var1 + 1;
    }
    return ~this._f[var1];
  }

  private void b797() {
    final int[] var1 = new int[this.cbSize];
    final int[] var2 = new int[33];

    int var4;
    int var5;
    int var6;
    int var7;
    int var10;

    for (int i = 0; i < this.cbSize; ++i) {
      var4 = this.cwLengths[i];
      if (var4 != 0) {
        var5 = 1 << 32 - var4;
        var6 = var2[var4];
        var1[i] = var6;
        int var9;
        if ((var6 & var5) == 0) {
          var7 = var6 | var5;

          for (int j = var4 - 1; j >= 1; --j) {
            var9 = var2[j];
            if (var9 != var6) {
              break;
            }

            var10 = 1 << 32 - j;
            if ((var9 & var10) != 0) {
              var2[j] = var2[j - 1];
              break;
            }

            var2[j] = var9 | var10;
          }
        } else {
          var7 = var2[var4 - 1];
        }

        var2[var4] = var7;

        for (int j = var4 + 1; j <= 32; ++j) {
          var9 = var2[j];
          if (var9 == var6) {
            var2[j] = var7;
          }
        }
      }
    }

    this._f = new int[8];
    int var11 = 0;

    for (int i = 0; i < this.cbSize; ++i) {
      var4 = this.cwLengths[i];
      if (var4 != 0) {
        var5 = var1[i];
        var6 = 0;

        for (var7 = 0; var7 < var4; ++var7) {
          int var8 = Integer.MIN_VALUE >>> var7;
          if ((var5 & var8) == 0) {
            ++var6;
          } else {
            if (this._f[var6] == 0) {
              this._f[var6] = var11;
            }

            var6 = this._f[var6];
          }

          if (var6 >= this._f.length) {
            final int[] var12 = new int[this._f.length * 2];

            for (var10 = 0; var10 < this._f.length; ++var10) {
              var12[var10] = this._f[var10];
            }

            this._f = var12;
          }

        }

        this._f[var6] = ~i;
        if (var6 >= var11) {
          var11 = var6 + 1;
        }
      }
    }

  }
}
