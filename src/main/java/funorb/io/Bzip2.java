package funorb.io;

import java.util.Arrays;

public final class Bzip2 {
  private static final int ALL_DONE = 23;
  private static final int SOME_LENGTH = 23;
  private static final int SOME_CONSTANT = 50;

  private static final int[] _kpk = new int[100_000];
  private static final byte[][] huffmanCodes = new byte[6][258];
  private static final boolean[] huffmanCodesUsedMap = new boolean[256];
  private static final byte[] huffmanSelectors = new byte[18002];
  private static final int[][] _y = new int[6][258];
  private static final int[] _q = new int[256];
  private static final int[] _i = new int[6];
  private static final int[][] _o = new int[6][258];
  private static final boolean[] huffmanBlocksUsedMap = new boolean[16];
  private static final byte[] _D = new byte[4096];
  private static final byte[] huffmanCodeIndexes = new byte[256];
  private static final int[][] _H = new int[6][258];
  private static final byte[] huffmanGroupIndexes = new byte[18002];
  private static final int[] _l = new int[257];
  private static final int[] _K = new int[16];
  private static int _x;
  private static int len;
  private static byte _v;
  private static int nextValueBits;
  private static int _f;
  private static int _G;
  private static int _w;
  private static int srcPos = 0;
  private static byte[] src;
  private static int _k;
  private static byte[] dest;
  private static int nextValue;
  private static int _d = 0;

  private Bzip2() {}

  public static synchronized void decompress(final byte[] src, final byte[] dest) {
    Bzip2.src = src;
    Bzip2.dest = dest;
    len = dest.length;
    srcPos = 9;
    _d = 0;
    nextValueBits = 0;
    nextValue = 0;
    go();
    Bzip2.src = null;
    Bzip2.dest = null;
  }

  private static void a701(final byte[] huffmanCodes, final int codeCount, final int codeMin, final int codeMax, final int[] arr1, final int[] arr2, final int[] arr3) {
    int var7 = 0;
    for (int i = codeMin; i <= codeMax; ++i) {
      for (int j = 0; j < codeCount; ++j) {
        if (huffmanCodes[j] == i) {
          arr3[var7] = j;
          ++var7;
        }
      }
    }

    Arrays.fill(arr1, 0, SOME_LENGTH, 0);
    for (int i = 0; i < codeCount; ++i) {
      ++arr1[huffmanCodes[i] + 1];
    }
    for (int i = 1; i < SOME_LENGTH; ++i) {
      arr1[i] += arr1[i - 1];
    }


    Arrays.fill(arr2, 0, SOME_LENGTH, 0);
    int var10 = 0;
    for (int i = codeMin; i <= codeMax; ++i) {
      var10 += arr1[i + 1] - arr1[i];
      arr2[i] = var10 - 1;
      var10 <<= 1;
    }
    for (int i = codeMin + 1; i <= codeMax; ++i) {
      arr1[i] = ((arr2[i - 1] + 1) << 1) - arr1[i];
    }
  }


  private static void go() {
    do {
      if (readByte() == ALL_DONE) {
        return;
      }

      skipBytes(9);
      readBit();

      final int origOffset = readBits(24);
      for (int i = 0; i < 16; ++i) {
        huffmanBlocksUsedMap[i] = readBool();
      }

      Arrays.fill(huffmanCodesUsedMap, false);
      for (int i = 0; i < 16; ++i) {
        if (huffmanBlocksUsedMap[i]) {
          for (int j = 0; j < 16; ++j) {
            if (readBool()) {
              huffmanCodesUsedMap[i * 16 + j] = true;
            }
          }
        }
      }

      int huffmanCodeIndexesIndex = 0;
      for (int i = 0; i < 256; ++i) {
        if (huffmanCodesUsedMap[i]) {
          huffmanCodeIndexes[huffmanCodeIndexesIndex] = (byte) i;
          ++huffmanCodeIndexesIndex;
        }
      }
      final int huffmanCodeCount = huffmanCodeIndexesIndex + 2;

      final int huffmanGroupCount = readBits(3);
      final int huffmanSelectorCount = readBits(15);
      for (int i = 0; i < huffmanSelectorCount; ++i) {
        huffmanSelectors[i] = (byte) readUnary();
      }

      final byte[] huffmanGroupStack = new byte[6];
      for (byte i = 0; i < huffmanGroupCount; i++) {
        huffmanGroupStack[i] = i;
      }
      for (int i = 0; i < huffmanSelectorCount; ++i) {
        final byte selector = huffmanSelectors[i];
        final byte groupIndex = huffmanGroupStack[selector];
        for (byte j = selector; j > 0; --j) {
          huffmanGroupStack[j] = huffmanGroupStack[j - 1];
        }
        huffmanGroupStack[0] = groupIndex;
        huffmanGroupIndexes[i] = groupIndex;
      }

      for (int i = 0; i < huffmanGroupCount; ++i) {
        int code = readBits(5);
        for (int j = 0; j < huffmanCodeCount; ++j) {
          while (readBool()) {
            if (readBool()) {
              --code;
            } else {
              ++code;
            }
          }
          huffmanCodes[i][j] = (byte) code;
        }
      }

      for (int i = 0; i < huffmanGroupCount; ++i) {
        byte codeMin = 32;
        byte codeMax = 0;

        for (int j = 0; j < huffmanCodeCount; ++j) {
          if (codeMax < huffmanCodes[i][j]) {
            codeMax = huffmanCodes[i][j];
          }
          if (codeMin > huffmanCodes[i][j]) {
            codeMin = huffmanCodes[i][j];
          }
        }

        a701(huffmanCodes[i], huffmanCodeCount, codeMin, codeMax, _y[i], _o[i], _H[i]);
        _i[i] = codeMin;
      }

      Arrays.fill(_q, 0);

      {
        int var56 = _D.length - 1;
        for (int i = 15; i >= 0; --i) {
          for (int j = 15; j >= 0; --j) {
            _D[var56] = (byte) (i * 16 + j);
            --var56;
          }
          _K[i] = var56 + 1;
        }
      }

      int var46 = 0;
      int var42 = 0;
      byte var53 = huffmanGroupIndexes[0];
      int var22 = _i[var53];
      int[] var23 = _o[var53];
      int[] var25 = _H[var53];
      int[] var24 = _y[var53];

      int var45 = common1(var22, var23, var25, var24);
      int var44 = SOME_CONSTANT - 1;
      while (var45 != huffmanCodeCount - 1) {
        if (var45 == 0 || var45 == 1) {
          int var47 = -1;
          int var48 = 1;

          do {
            if (var45 == 0) {
              var47 += var48;
            } else {
              var47 += 2 * var48;
            }

            var48 *= 2;
            if (var44 == 0) {
              ++var42;
              var44 = SOME_CONSTANT;
              var53 = huffmanGroupIndexes[var42];
              var22 = _i[var53];
              var23 = _o[var53];
              var25 = _H[var53];
              var24 = _y[var53];
            }

            --var44;
            var45 = common1(var22, var23, var25, var24);
          } while (var45 == 0 || var45 == 1);
          ++var47;
          final byte b1 = huffmanCodeIndexes[_D[_K[0]] & 255];
          for (_q[b1 & 255] += var47; var47 > 0; --var47) {
            _kpk[var46] = b1 & 255;
            ++var46;
          }
        } else {
          int var33 = var45 - 1;
          final byte b1;
          if (var33 < 16) {
            final int var30 = _K[0];
            b1 = _D[var30 + var33];
            for (; var33 > 3; var33 -= 4) {
              final int var34 = var30 + var33;
              _D[var34] = _D[var34 - 1];
              _D[var34 - 1] = _D[var34 - 2];
              _D[var34 - 2] = _D[var34 - 3];
              _D[var34 - 3] = _D[var34 - 4];
            }
            while (var33 > 0) {
              _D[var30 + var33] = _D[var30 + var33 - 1];
              --var33;
            }
            _D[var30] = b1;
          } else {
            int var31 = var33 / 16;
            final int var32 = var33 % 16;
            int var30 = _K[var31] + var32;
            b1 = _D[var30];
            for (; var30 > _K[var31]; --var30) {
              _D[var30] = _D[var30 - 1];
            }

            _K[var31]++;
            for (; var31 > 0; --var31) {
              _K[var31]--;
              _D[_K[var31]] = _D[_K[var31 - 1] + 16 - 1];
            }

            _K[0]--;
            _D[_K[0]] = b1;
            if (_K[0] == 0) {
              int var56 = _D.length - 1;
              for (int i = 15; i >= 0; --i) {
                for (int j = 15; j >= 0; --j) {
                  _D[var56] = _D[_K[i] + j];
                  --var56;
                }
                _K[i] = var56 + 1;
              }
            }
          }

          _q[huffmanCodeIndexes[b1 & 255] & 255]++;
          _kpk[var46] = huffmanCodeIndexes[b1 & 255] & 255;
          ++var46;
          if (var44 == 0) {
            ++var42;
            var44 = 50;
            var53 = huffmanGroupIndexes[var42];
            var22 = _i[var53];
            var23 = _o[var53];
            var25 = _H[var53];
            var24 = _y[var53];
          }

          --var44;
          var45 = common1(var22, var23, var25, var24);
        }
      }

      _k = 0;
      _v = 0;
      _l[0] = 0;

      System.arraycopy(_q, 0, _l, 1, _q.length);

      for (int i = 1; i <= 256; ++i) {
        _l[i] += _l[i - 1];
      }
      for (int i = 0; i < var46; ++i) {
        final int b1 = _kpk[i] & 255;
        _kpk[_l[b1 & 255]] |= i << 8;
        _l[b1 & 255]++;
      }

      _w = _kpk[origOffset] >> 8;
      _G = 0;
      _w = _kpk[_w];
      _x = (byte) (_w & 255);
      _w >>= 8;
      ++_G;
      _f = var46;
      b855();
    } while (_G == _f + 1 && _k == 0);
  }

  private static int common1(final int bits, final int[] var23, final int[] var25, final int[] var24) {
    int var50 = bits;
    int var51 = readBits(bits);
    while (var51 > var23[var50]) {
      ++var50;
      var51 = (var51 << 1) | readBit();
    }
    return var25[var51 - var24[var50]];
  }

  private static void b855() {
    final int var12 = _f + 1;

    outer:
    while (true) {
      if (_k > 0) {
        while (true) {
          if (len == 0) {
            return;
          }

          if (_k == 1) {
            dest[_d] = _v;
            ++_d;
            --len;
            break;
          }

          dest[_d] = _v;
          --_k;
          ++_d;
          --len;
        }
      }

      while (_G != var12) {
        _v = (byte) _x;
        _w = _kpk[_w];
        final byte var1 = (byte) _w;
        _w >>= 8;
        ++_G;
        if (var1 == _x) {
          if (_G != var12) {
            _k = 2;
            _w = _kpk[_w];
            final byte b = (byte) _w;
            _w >>= 8;
            ++_G;
            if (_G != var12) {
              if (b == _x) {
                _k = 3;
                _w = _kpk[_w];
                final byte var71 = (byte) _w;
                _w >>= 8;
                ++_G;
                if (_G != var12) {
                  if (var71 == _x) {
                    _w = _kpk[_w];
                    final byte var72 = (byte) _w;
                    _w >>= 8;
                    ++_G;
                    _k = (var72 & 255) + 4;
                    _w = _kpk[_w];
                    _x = (byte) _w;
                    _w >>= 8;
                    ++_G;
                  } else {
                    _x = var71;
                  }
                }
              } else {
                _x = b;
              }
            }
            continue outer;
          }

        } else {
          _x = var1;
        }
        if (len == 0) {
          _k = 1;
          return;
        }
        dest[_d] = _v;
        ++_d;
        --len;
      }

      _k = 0;
      break;
    }
  }

  private static int readBits(final int bits) {
    while (nextValueBits < bits) {
      nextValue = (nextValue << 8) | (src[srcPos] & 255);
      nextValueBits += 8;
      ++srcPos;
    }

    final int leftoverBits = nextValueBits - bits;
    nextValueBits = leftoverBits;
    return (nextValue >> leftoverBits) & ((1 << bits) - 1);
  }

  private static byte readBit() {
    return (byte) readBits(1);
  }

  private static boolean readBool() {
    return readBit() == 1;
  }

  private static byte readByte() {
    return (byte) readBits(8);
  }

  @SuppressWarnings("SameParameterValue")
  private static void skipBytes(final int count) {
    for (int i = 0; i < count; i++) {
      readByte();
    }
  }

  private static int readUnary() {
    int val = 0;
    while (readBool()) val++;
    return val;
  }
}
