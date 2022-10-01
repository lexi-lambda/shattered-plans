package funorb.graphics;

public final class PalettedSymbol extends Symbol {
  public byte[] pixels;
  public int[] palette;

  public PalettedSymbol(final int offsetX, final int offsetY, final int x, final int y, final int width, final int height, final byte[] pixels, final int[] palette) {
    this.advanceX = offsetX;
    this.advanceY = offsetY;
    this.x = x;
    this.y = y;
    this.width = width;
    this.height = height;
    this.pixels = pixels;
    this.palette = palette;
  }

  public PalettedSymbol() {
    this.advanceX = this.width = 0;
    this.advanceY = this.height = 0;
    this.y = 0;
    this.x = 0;
    this.pixels = new byte[0];
    this.palette = new int[0];
  }

  private static void b723(final int[] var0, final byte[] var1, final int[] var2, int var4, int var5, int var6, final int var7, final int var8, final int var9) {
    final int var10 = -(var6 >> 2);
    var6 = -(var6 & 3);

    for (int var11 = -var7; var11 < 0; ++var11) {
      int var12;
      byte var13;
      for (var12 = var10; var12 < 0; ++var12) {
        var13 = var1[var4++];
        if (var13 == 0) {
          ++var5;
        } else {
          var0[var5++] = var2[var13 & 255];
        }

        var13 = var1[var4++];
        if (var13 == 0) {
          ++var5;
        } else {
          var0[var5++] = var2[var13 & 255];
        }

        var13 = var1[var4++];
        if (var13 == 0) {
          ++var5;
        } else {
          var0[var5++] = var2[var13 & 255];
        }

        var13 = var1[var4++];
        if (var13 == 0) {
          ++var5;
        } else {
          var0[var5++] = var2[var13 & 255];
        }
      }

      for (var12 = var6; var12 < 0; ++var12) {
        var13 = var1[var4++];
        if (var13 == 0) {
          ++var5;
        } else {
          var0[var5++] = var2[var13 & 255];
        }
      }

      var5 += var8;
      var4 += var9;
    }

  }

  private static void a723(final int[] buffer, final byte[] pixels, final int[] palette, int var3, int var4, final int var5, final int var6, final int var7, final int var8, final int alpha) {
    final int var10 = 256 - alpha;

    for (int var11 = -var6; var11 < 0; ++var11) {
      for (int var12 = -var5; var12 < 0; ++var12) {
        final byte var13 = pixels[var3++];
        if (var13 == 0) {
          ++var4;
        } else {
          final int var15 = palette[var13 & 255];
          final int var14 = buffer[var4];
          buffer[var4++] = ((var15 & 0xff00ff) * alpha + (var14 & 0xff00ff) * var10 & 0xff00ff00) + ((var15 & 0xff00) * alpha + (var14 & 0xff00) * var10 & 0xff0000) >> 8;
        }
      }

      var4 += var7;
      var3 += var8;
    }

  }

  @Override
  public void draw(int x, int y) {
    x += this.x;
    y += this.y;
    int var3 = Drawing.pixelIndex(x, y);
    int var4 = 0;
    int var5 = this.height;
    int var6 = this.width;
    int var7 = Drawing.width - var6;
    int var8 = 0;
    int var9;
    if (y < Drawing.top) {
      var9 = Drawing.top - y;
      var5 -= var9;
      y = Drawing.top;
      var4 += var9 * var6;
      var3 += var9 * Drawing.width;
    }

    if (y + var5 > Drawing.bottom) {
      var5 -= y + var5 - Drawing.bottom;
    }

    if (x < Drawing.left) {
      var9 = Drawing.left - x;
      var6 -= var9;
      x = Drawing.left;
      var4 += var9;
      var3 += var9;
      var8 += var9;
      var7 += var9;
    }

    if (x + var6 > Drawing.right) {
      var9 = x + var6 - Drawing.right;
      var6 -= var9;
      var8 += var9;
      var7 += var9;
    }

    if (var6 > 0 && var5 > 0) {
      b723(Drawing.screenBuffer, this.pixels, this.palette, var4, var3, var6, var5, var7, var8);
    }
  }

  @Override
  public void draw(int x, int y, final int alpha) {
    x += this.x;
    y += this.y;
    int var4 = Drawing.pixelIndex(x, y);
    int var5 = 0;
    int var6 = this.height;
    int var7 = this.width;
    int var8 = Drawing.width - var7;
    int var9 = 0;
    int var10;
    if (y < Drawing.top) {
      var10 = Drawing.top - y;
      var6 -= var10;
      y = Drawing.top;
      var5 += var10 * var7;
      var4 += var10 * Drawing.width;
    }

    if (y + var6 > Drawing.bottom) {
      var6 -= y + var6 - Drawing.bottom;
    }

    if (x < Drawing.left) {
      var10 = Drawing.left - x;
      var7 -= var10;
      x = Drawing.left;
      var5 += var10;
      var4 += var10;
      var9 += var10;
      var8 += var10;
    }

    if (x + var7 > Drawing.right) {
      var10 = x + var7 - Drawing.right;
      var7 -= var10;
      var9 += var10;
      var8 += var10;
    }

    if (var7 > 0 && var6 > 0) {
      a723(Drawing.screenBuffer, this.pixels, this.palette, var5, var4, var7, var6, var8, var9, alpha);
    }
  }
}
