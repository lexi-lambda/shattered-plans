package funorb.graphics;

public final class SpriteFont extends Font {
  private final byte[][] _H;

  public SpriteFont(final byte[] var1, final int[] xs, final int[] ys, final int[] widths, final int[] heights, final byte[][] pixels) {
    super(var1, xs, ys, widths, heights);
    this._H = pixels;
  }

  public static void a183(final int[] var0, final byte[] var1, int var2, int var3, int var4, final int var5, final int var6, final int var7, final int var8, final int var9, int var10) {
    var2 = ((var2 & 16711935) * var10 & -16711936) + ((var2 & '\uff00') * var10 & 16711680) >> 8;
    var10 = 256 - var10;

    for (int var11 = -var7; var11 < 0; ++var11) {
      for (int var12 = -var6; var12 < 0; ++var12) {
        if (var1[var3] == 0) {
          ++var4;
        } else {
          final int var13 = var0[var4];
          var0[var4++] = (((var13 & 16711935) * var10 & -16711936) + ((var13 & '\uff00') * var10 & 16711680) >> 8) + var2;
        }

        var3 += var5;
      }

      var4 += var8;
      var3 += var9;
    }

  }

  public static void a038(final int[] var0, final byte[] var1, final int var2, int var3, int var4, final int var5, int var6, final int var7, final int var8, final int var9) {
    final int var10 = -(var6 >> 2);
    var6 = -(var6 & 3);

    for (int var11 = -var7; var11 < 0; ++var11) {
      int var12;
      for (var12 = var10; var12 < 0; ++var12) {
        if (var1[var3] == 0) {
          ++var4;
        } else {
          var0[var4++] = var2;
        }

        var3 += var5;
        if (var1[var3] == 0) {
          ++var4;
        } else {
          var0[var4++] = var2;
        }

        var3 += var5;
        if (var1[var3] == 0) {
          ++var4;
        } else {
          var0[var4++] = var2;
        }

        var3 += var5;
        if (var1[var3] == 0) {
          ++var4;
        } else {
          var0[var4++] = var2;
        }

        var3 += var5;
      }

      for (var12 = var6; var12 < 0; ++var12) {
        if (var1[var3] == 0) {
          ++var4;
        } else {
          var0[var4++] = var2;
        }

        var3 += var5;
      }

      var4 += var8;
      var3 += var9;
    }

  }

  public static void b038(final int[] var0, final byte[] var1, int var2, int var3, int var4, final int var5, final int var6, final int var7, final int var8, int var9) {
    var2 = ((var2 & 16711935) * var9 & -16711936) + ((var2 & '\uff00') * var9 & 16711680) >> 8;
    var9 = 256 - var9;

    for (int var10 = -var6; var10 < 0; ++var10) {
      for (int var11 = -var5; var11 < 0; ++var11) {
        if (var1[var3++] == 0) {
          ++var4;
        } else {
          final int var12 = var0[var4];
          var0[var4++] = (((var12 & 16711935) * var9 & -16711936) + ((var12 & '\uff00') * var9 & 16711680) >> 8) + var2;
        }
      }

      var4 += var7;
      var3 += var8;
    }

  }

  public static void a111(final int[] var0, final byte[] var1, final int var2, int var3, int var4, int var5, final int var6, final int var7, final int var8) {
    final int var9 = -(var5 >> 2);
    var5 = -(var5 & 3);

    for (int var10 = -var6; var10 < 0; ++var10) {
      int var11;
      for (var11 = var9; var11 < 0; ++var11) {
        if (var1[var3++] == 0) {
          ++var4;
        } else {
          var0[var4++] = var2;
        }

        if (var1[var3++] == 0) {
          ++var4;
        } else {
          var0[var4++] = var2;
        }

        if (var1[var3++] == 0) {
          ++var4;
        } else {
          var0[var4++] = var2;
        }

        if (var1[var3++] == 0) {
          ++var4;
        } else {
          var0[var4++] = var2;
        }
      }

      for (var11 = var5; var11 < 0; ++var11) {
        if (var1[var3++] == 0) {
          ++var4;
        } else {
          var0[var4++] = var2;
        }
      }

      var4 += var7;
      var3 += var8;
    }

  }

  @Override
  public void drawGlyph(final int index, int x, int y, int var4, int var5, final int color, final boolean var7) {
    int var8 = Drawing.pixelIndex(x, y);
    int var9 = Drawing.width - var4;
    int var10 = 0;
    int var11 = 0;
    int var12;
    if (y < Drawing.top) {
      var12 = Drawing.top - y;
      var5 -= var12;
      y = Drawing.top;
      var11 += var12 * var4;
      var8 += var12 * Drawing.width;
    }

    if (y + var5 > Drawing.bottom) {
      var5 -= y + var5 - Drawing.bottom;
    }

    if (x < Drawing.left) {
      var12 = Drawing.left - x;
      var4 -= var12;
      x = Drawing.left;
      var11 += var12;
      var8 += var12;
      var10 += var12;
      var9 += var12;
    }

    if (x + var4 > Drawing.right) {
      var12 = x + var4 - Drawing.right;
      var4 -= var12;
      var10 += var12;
      var9 += var12;
    }

    if (var4 > 0 && var5 > 0) {
      a111(Drawing.screenBuffer, this._H[index], color, var11, var8, var4, var5, var9, var10);
    }
  }

  @Override
  public void drawGlyph(final int index, int x, int y, int var4, int var5, final int color, final int alpha, final boolean var8) {
    int var9 = Drawing.pixelIndex(x, y);
    int var10 = Drawing.width - var4;
    int var11 = 0;
    int var12 = 0;
    int var13;
    if (y < Drawing.top) {
      var13 = Drawing.top - y;
      var5 -= var13;
      y = Drawing.top;
      var12 += var13 * var4;
      var9 += var13 * Drawing.width;
    }

    if (y + var5 > Drawing.bottom) {
      var5 -= y + var5 - Drawing.bottom;
    }

    if (x < Drawing.left) {
      var13 = Drawing.left - x;
      var4 -= var13;
      x = Drawing.left;
      var12 += var13;
      var9 += var13;
      var11 += var13;
      var10 += var13;
    }

    if (x + var4 > Drawing.right) {
      var13 = x + var4 - Drawing.right;
      var4 -= var13;
      var11 += var13;
      var10 += var13;
    }

    if (var4 > 0 && var5 > 0) {
      b038(Drawing.screenBuffer, this._H[index], color, var12, var9, var4, var5, var10, var11, alpha);
    }
  }

  @Override
  public void drawVerticalGlyph(final int index, int x, int y, int height, int width, final int color, final boolean var7) {
    int var8 = Drawing.pixelIndex(x, y);
    int var9 = Drawing.width - height;
    final int var10 = width;
    final byte var11 = -1;
    int var12 = width - 1;
    int var13;
    if (y < Drawing.top) {
      var13 = Drawing.top - y;
      width -= var13;
      y = Drawing.top;
      var12 -= var13;
      var8 += var13 * Drawing.width;
    }

    if (y + width > Drawing.bottom) {
      width -= y + width - Drawing.bottom;
    }

    if (x < Drawing.left) {
      var13 = Drawing.left - x;
      height -= var13;
      x = Drawing.left;
      var12 += var13 * var10;
      var8 += var13;
      var9 += var13;
    }

    if (x + height > Drawing.right) {
      var13 = x + height - Drawing.right;
      height -= var13;
      var9 += var13;
    }

    if (height > 0 && width > 0) {
      final int var14 = var11 - var10 * height;
      a038(Drawing.screenBuffer, this._H[index], color, var12, var8, var10, height, width, var9, var14);
    }
  }

  @Override
  public void drawVerticalGlyph(final int index, int x, int y, int height, int width, final int color, final int alpha, final boolean var8) {
    int var9 = Drawing.pixelIndex(x, y);
    int var10 = Drawing.width - height;
    final int var11 = width;
    final byte var12 = -1;
    int var13 = width - 1;
    int var14;
    if (y < Drawing.top) {
      var14 = Drawing.top - y;
      width -= var14;
      y = Drawing.top;
      var13 -= var14;
      var9 += var14 * Drawing.width;
    }

    if (y + width > Drawing.bottom) {
      width -= y + width - Drawing.bottom;
    }

    if (x < Drawing.left) {
      var14 = Drawing.left - x;
      height -= var14;
      x = Drawing.left;
      var13 += var14 * var11;
      var9 += var14;
      var10 += var14;
    }

    if (x + height > Drawing.right) {
      var14 = x + height - Drawing.right;
      height -= var14;
      var10 += var14;
    }

    if (height > 0 && width > 0) {
      final int var15 = var12 - var11 * height;
      a183(Drawing.screenBuffer, this._H[index], color, var13, var9, var11, height, width, var10, var15, alpha);
    }
  }
}
