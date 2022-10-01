package funorb.audio;

public final class to_ {
  private final int[] _e;
  private final int _c = fd_.a137(16);
  private final int _g = fd_.a137(24);
  private final int _d = fd_.a137(24);
  private final int _b = fd_.a137(24) + 1;
  private final int _a = fd_.a137(6) + 1;
  private final int _f = fd_.a137(8);

  public to_() {
    final int[] var1 = new int[this._a];

    int var2;
    for (var2 = 0; var2 < this._a; ++var2) {
      int var3 = 0;
      final int var4 = fd_.a137(3);
      final boolean var5 = fd_.c784() != 0;
      if (var5) {
        var3 = fd_.a137(5);
      }

      var1[var2] = var3 << 3 | var4;
    }

    this._e = new int[this._a * 8];

    for (var2 = 0; var2 < this._a * 8; ++var2) {
      this._e[var2] = (var1[var2 >> 3] & 1 << (var2 & 7)) != 0 ? fd_.a137(8) : -1;
    }

  }

  public void a623(final float[] var1, final int var2, final boolean var3) {
    int var4;
    for (var4 = 0; var4 < var2; ++var4) {
      var1[var4] = 0.0F;
    }

    if (!var3) {
      var4 = fd_._L[this._f]._a;
      final int var5 = this._d - this._g;
      final int var6 = var5 / this._b;
      final int[] var7 = new int[var6];

      for (int var8 = 0; var8 < 8; ++var8) {
        int var9 = 0;

        while (var9 < var6) {
          int var10;
          int var11;
          if (var8 == 0) {
            var10 = fd_._L[this._f].a784();

            for (var11 = var4 - 1; var11 >= 0; --var11) {
              if (var9 + var11 < var6) {
                var7[var9 + var11] = var10 % this._a;
              }

              var10 /= this._a;
            }
          }

          for (var10 = 0; var10 < var4; ++var10) {
            var11 = var7[var9];
            final int var12 = this._e[var11 * 8 + var8];
            if (var12 >= 0) {
              final int var13 = this._g + var9 * this._b;
              final vb_ var14 = fd_._L[var12];
              int var15;
              if (this._c == 0) {
                var15 = this._b / var14._a;

                for (int var19 = 0; var19 < var15; ++var19) {
                  final float[] var20 = var14.c932();

                  for (int var18 = 0; var18 < var14._a; ++var18) {
                    var1[var13 + var19 + var18 * var15] += var20[var18];
                  }
                }
              } else {
                var15 = 0;

                while (var15 < this._b) {
                  final float[] var16 = var14.c932();

                  for (int var17 = 0; var17 < var14._a; ++var17) {
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
