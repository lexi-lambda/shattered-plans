package funorb.audio;

public final class VorbisResidue {
  // vorbis spec uses a [resClassifications][8] array of ints, but resBook
  // is a flattened array containing [resClassifications*8] entries
  private final int[] books;
  private final int type;
  private final int begin;
  private final int end;
  private final int partitionSize;
  private final int classifications;
  private final int classbook;

  public VorbisResidue() {
    this.type = VorbisFormat.readBits(16);
    this.begin = VorbisFormat.readBits(24);
    this.end = VorbisFormat.readBits(24);
    this.partitionSize = VorbisFormat.readBits(24) + 1;
    this.classifications = VorbisFormat.readBits(6) + 1;
    this.classbook = VorbisFormat.readBits(8);

    final int[] resCascade = new int[this.classifications];
    for (int i = 0; i < this.classifications; ++i) {
      int highBits = 0;
      final int lowBits = VorbisFormat.readBits(3);
      final boolean bitflag = VorbisFormat.readBit() != 0;
      if (bitflag) {
        highBits = VorbisFormat.readBits(5);
      }
      resCascade[i] = highBits << 3 | lowBits;
    }

    this.books = new int[this.classifications * 8];
    for (int i = 0; i < this.classifications * 8; ++i) {
      this.books[i] = (resCascade[i >> 3] & 1 << (i & 7)) != 0
        ? VorbisFormat.readBits(8)
        : -1;
    }
  }

  public void computeResidue(final float[] window, final int len, final boolean thisWindowZero) {
    for (int i = 0; i < len; ++i) {
      window[i] = 0.0F;
    }
    if (thisWindowZero) {
      return;
    }

    final int cbDim = VorbisFormat.codebooks[this.classbook].cbDim;
    final int range = this.end - this.begin;
    final int partitionCount = range / this.partitionSize;
    final int[] classes = new int[partitionCount];

    for (int pass = 0; pass < 8; ++pass) {
      for (int part = 0; part < partitionCount; ++part) {
        if (pass == 0) {
          int value = VorbisFormat.codebooks[this.classbook].decodeScalar();
          for (int j = cbDim - 1; j >= 0; --j) {
            if (part + j < partitionCount) {
              classes[part + j] = value % this.classifications;
            }
            value /= this.classifications;
          }
        }

        for (int dim = 0; dim < cbDim; ++dim) {
          int cls = classes[part];
          final int bookIndex = this.books[cls * 8 + pass];
          if (bookIndex < 0) {
            continue;
          }

          final int offset = this.begin + part * this.partitionSize;
          final VorbisCodebook book = VorbisFormat.codebooks[bookIndex];

          if (this.type == 0) {
            int step = this.partitionSize / book.cbDim;
            for (int i = 0; i < step; ++i) {
              final float[] vector = book.decodeVector();
              for (int j = 0; j < book.cbDim; ++j) {
                window[offset + i + j * step] += vector[j];
              }
            }
          } else {
            int i = 0;
            while (i < this.partitionSize) {
              final float[] vector = book.decodeVector();
              for (int j = 0; j < book.cbDim; ++j) {
                window[offset + i] += vector[j];
                ++i;
              }
            }
          }
        }
      }
    }

  }
}
