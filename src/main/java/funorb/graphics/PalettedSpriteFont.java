package funorb.graphics;

public final class PalettedSpriteFont extends Font {
  private final byte[][] _H;

  public PalettedSpriteFont(final byte[] var1, final int[] xs, final int[] ys, final int[] widths, final int[] heights, final int[] palette, final byte[][] pixels) {
    super(var1, xs, ys, widths, heights);
    this._H = a675(palette, pixels);
  }

  private static byte[][] a675(final int[] var0, final byte[][] var1) {
    int var3;
    for (int var2 = 0; var2 < var0.length; ++var2) {
      var3 = var0[var2];
      final int var4 = (var3 >> 15 & 510) + (var3 & 255);
      var0[var2] = var4 / 3 + (var3 >> 8 & 255) >> 1;
    }

    for (var3 = 0; var3 < var1.length; ++var3) {
      final byte[] var8 = var1[var3];

      for (int var5 = 0; var5 < var8.length; ++var5) {
        final byte var6 = var8[var5];
        if (var6 != 0) {
          var8[var5] = (byte) var0[var6];
        }
      }
    }

    return var1;
  }

  private static void b038(final int[] var0, final byte[] var1, final int var2, int var3, int var4, final int var5, final int var6, final int var7, final int var8, final int var9) {
    for (int var10 = -var6; var10 < 0; ++var10) {
      for (int var11 = -var5; var11 < 0; ++var11) {
        int var12 = (255 & var1[var3++]) * var9 >> 8;
        if (var12 == 0) {
          ++var4;
        } else {
          final int var13 = ((var2 & 16711935) * var12 & -16711936) + ((var2 & '\uff00') * var12 & 16711680) >> 8;
          var12 = 256 - var12;
          final int var14 = var0[var4];
          var0[var4++] = (((var14 & 16711935) * var12 & -16711936) + ((var14 & '\uff00') * var12 & 16711680) >> 8) + var13;
        }
      }

      var4 += var7;
      var3 += var8;
    }

  }

  private static void a038(final int[] var0, final byte[] var1, final int var2, int var3, int var4, final int var5, final int var6, final int var7, final int var8, final int var9) {
    for (int var10 = -var7; var10 < 0; ++var10) {
      for (int var11 = -var6; var11 < 0; ++var11) {
        int var12 = 255 & var1[var3];
        if (var12 == 0) {
          ++var4;
        } else {
          final int var13 = ((var2 & 16711935) * var12 & -16711936) + ((var2 & '\uff00') * var12 & 16711680) >> 8;
          var12 = 256 - var12;
          final int var14 = var0[var4];
          var0[var4++] = (((var14 & 16711935) * var12 & -16711936) + ((var14 & '\uff00') * var12 & 16711680) >> 8) + var13;
        }

        var3 += var5;
      }

      var4 += var8;
      var3 += var9;
    }

  }

  private static void a111(final int[] var0, final byte[] var1, final int var2, int var3, int var4, final int var5, final int var6, final int var7, final int var8) {
    for (int var9 = -var6; var9 < 0; ++var9) {
      for (int var10 = -var5; var10 < 0; ++var10) {
        int var11 = 255 & var1[var3++];
        if (var11 == 0) {
          ++var4;
        } else {
          final int var12 = ((var2 & 16711935) * var11 & -16711936) + ((var2 & '\uff00') * var11 & 16711680) >> 8;
          var11 = 256 - var11;
          final int var13 = var0[var4];
          var0[var4++] = (((var13 & 16711935) * var11 & -16711936) + ((var13 & '\uff00') * var11 & 16711680) >> 8) + var12;
        }
      }

      var4 += var7;
      var3 += var8;
    }
  }

  private static void a183(final int[] var0, final byte[] var1, final int var2, int var3, int var4, final int var5, final int var6, final int var7, final int var8, final int var9, final int var10) {
    for (int var11 = -var7; var11 < 0; ++var11) {
      for (int var12 = -var6; var12 < 0; ++var12) {
        int var13 = (255 & var1[var3]) * var10 >> 8;
        if (var13 == 0) {
          ++var4;
        } else {
          final int var14 = ((var2 & 16711935) * var13 & -16711936) + ((var2 & '\uff00') * var13 & 16711680) >> 8;
          var13 = 256 - var13;
          final int var15 = var0[var4];
          var0[var4++] = (((var15 & 16711935) * var13 & -16711936) + ((var15 & '\uff00') * var13 & 16711680) >> 8) + var14;
        }

        var3 += var5;
      }

      var4 += var8;
      var3 += var9;
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
      if (var7) {
        SpriteFont.a111(Drawing.screenBuffer, this._H[index], color, var11, var8, var4, var5, var9, var10);
      } else {
        a111(Drawing.screenBuffer, this._H[index], color, var11, var8, var4, var5, var9, var10);
      }
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
      if (var8) {
        SpriteFont.b038(Drawing.screenBuffer, this._H[index], color, var12, var9, var4, var5, var10, var11, alpha);
      } else {
        b038(Drawing.screenBuffer, this._H[index], color, var12, var9, var4, var5, var10, var11, alpha);
      }

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
      if (var8) {
        SpriteFont.a183(Drawing.screenBuffer, this._H[index], color, var13, var9, var11, height, width, var10, var15, alpha);
      } else {
        a183(Drawing.screenBuffer, this._H[index], color, var13, var9, var11, height, width, var10, var15, alpha);
      }

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
      if (var7) {
        SpriteFont.a038(Drawing.screenBuffer, this._H[index], color, var12, var8, var10, height, width, var9, var14);
      } else {
        a038(Drawing.screenBuffer, this._H[index], color, var12, var8, var10, height, width, var9, var14);
      }

    }
  }
}
