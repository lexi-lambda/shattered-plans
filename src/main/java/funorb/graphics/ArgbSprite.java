package funorb.graphics;

public final class ArgbSprite extends Sprite {
  public ArgbSprite(final int var1, final int var2) {
    super(var1, var2);
  }

  public ArgbSprite(final int var1, final int var2, final int var3, final int var4, final int var5, final int var6, final int[] var7) {
    super(var1, var2, var3, var4, var5, var6, var7);
  }

  private static void b057(final int[] var3, final int[] var4, int var5, int var7, final int var9, final int var10, final int var11, final int var12, final int var13) {
    int var8;
    for (var8 = -var10; var8 < 0; ++var8) {
      int var6;
      for (var6 = -var9; var6 < 0; ++var6) {
        int var0 = var4[var5++];
        if (var0 == 0) {
          ++var7;
        } else {
          final int var14 = var13 * (var0 >>> 24) >> 8 & 255;
          final int var1 = (var0 & 16711935) * var14;
          var0 = (var1 & -16711936) + (var0 * var14 - var1 & 16711680) >>> 8;
          final int i = var3[var7];
          final int var2 = var0 + i;
          var0 = (var0 & 16711935) + (i & 16711935);
          final int i1 = (var0 & 16777472) + (var2 - var0 & 65536);
          var3[var7++] = var2 - i1 | i1 - (i1 >>> 8);
        }
      }

      var7 += var11;
      var5 += var12;
    }

  }

  private static void b590(final int[] var0, final int[] var1, int var2, int var3, int var4, final int var5, final int var6, final int var7, final int var8, final int var9, final int var10, final int var11) {
    final int var12 = var2;

    for (int var13 = -var7; var13 < 0; ++var13) {
      final int var14 = (var3 >> 16) * var10;

      for (int var15 = -var6; var15 < 0; ++var15) {
        final int var16 = var1[(var2 >> 16) + var14];
        final int var17 = var0[var4];
        final int var18 = (var16 >>> 24) * var11 >> 8;
        final int var19 = 256 - var18;
        var0[var4++] = ((var16 & 16711935) * var18 + (var17 & 16711935) * var19 & -16711936) + ((var16 & '\uff00') * var18 + (var17 & '\uff00') * var19 & 16711680) >>> 8;
        var2 += var8;
      }

      var3 += var9;
      var2 = var12;
      var4 += var5;
    }

  }

  private static void d663(final int[] var0, final int[] var1, int var2, int var3, final int var4, final int var5, final int var6, final int var7, final int var8) {
    for (int var9 = -var5; var9 < 0; ++var9) {
      for (int var10 = -var4; var10 < 0; ++var10) {
        final int var11 = (var1[var2] >>> 24) * var8 >> 8;
        final int var12 = 256 - var11;
        final int var13 = var1[var2++];
        final int var14 = var0[var3];
        var0[var3++] = ((var13 & 16711935) * var11 + (var14 & 16711935) * var12 & -16711936) + ((var13 & '\uff00') * var11 + (var14 & '\uff00') * var12 & 16711680) >>> 8;
      }

      var3 += var6;
      var2 += var7;
    }

  }

  private static void d983(final int[] var0, final int[] var1, int var3, int var4, final int var7, final int var8, final int var9, final int var10, final int var11) {
    final int var12 = var11 & 16711935;
    final int var13 = var11 >> 8 & 255;

    int var6;
    for (var6 = -var8; var6 < 0; ++var6) {
      int var5;
      for (var5 = -var7; var5 < 0; ++var5) {
        int var2 = var1[var3++];
        final int var14 = var2 >>> 24;
        var2 &= Drawing.WHITE;
        if (var14 == 0) {
          ++var4;
        } else {
          final int var18;
          if (var2 >> 8 == (var2 & '\uffff')) {
            var2 &= 255;
            var18 = (var2 * var12 >> 8 & 16711934) + (var2 * var13 & '\uff00') + 1;
          } else {
            var18 = var2;
          }

          final int var16 = 256 - var14;
          final int var17 = var0[var4];
          var0[var4++] = ((var18 & 16711935) * var14 + (var17 & 16711935) * var16 & -16711936) + ((var18 & '\uff00') * var14 + (var17 & '\uff00') * var16 & 16711680) >>> 8;
        }
      }

      var4 += var9;
      var3 += var10;
    }

  }

  @Override
  protected void compositeOver(final int[] src, final int srcStride, int srcPos, final int[] dest, final int destStride, int destPos, final int cols, final int rows) {
    for (int i = 0; i < rows; ++i) {
      for (int j = 0; j < cols; ++j) {
        final int srcColor = src[srcPos++];
        final int alpha = srcColor >>> 24;
        if (alpha == 0) {
          ++destPos;
        } else {
          final int occlusion = 0x100 - alpha;
          final int destColor = dest[destPos];
          final int rb = ((srcColor & 0xff00ff) * alpha + (destColor & 0xff00ff) * occlusion) & 0xff00ff00;
          final int g  = ((srcColor & 0x00ff00) * alpha + (destColor & 0x00ff00) * occlusion) & 0x00ff0000;
          dest[destPos++] = (rb + g) >>> 8;
        }
      }

      destPos += destStride;
      srcPos += srcStride;
    }
  }

  private static void d650(final int[] var0, final int[] var1, int var3, int var4, final int var5, final int var6, final int var7, final int var8) {
    final int var9 = -var5;

    for (int var10 = -var6; var10 < 0; ++var10) {
      for (int var11 = var9; var11 < 0; ++var11) {
        final int var2 = var1[var3--];
        final int var12 = var2 >>> 24;
        if (var12 == 0) {
          ++var4;
        } else {
          final int var13 = 256 - var12;
          final int var14 = var0[var4];
          var0[var4++] = ((var2 & 16711935) * var12 + (var14 & 16711935) * var13 & -16711936) + ((var2 & '\uff00') * var12 + (var14 & '\uff00') * var13 & 16711680) >>> 8;
        }
      }

      var4 += var7;
      var3 += var8;
    }

  }

  private static void c983(final int[] var0, final int[] var1, int var3, int var4, int var5, final int var6, final int var7, final int var8, final int var9, final int var10, final int var11) {
    final int var12 = var3;

    for (int var13 = -var8; var13 < 0; ++var13) {
      final int var14 = (var4 >> 16) * var11;

      for (int var15 = -var7; var15 < 0; ++var15) {
        final int var2 = var1[(var3 >> 16) + var14];
        final int var16 = var2 >>> 24;
        if (var16 == 0) {
          ++var5;
        } else {
          final int var17 = 256 - var16;
          final int var18 = var0[var5];
          var0[var5++] = ((var2 & 16711935) * var16 + (var18 & 16711935) * var17 & -16711936) + ((var2 & '\uff00') * var16 + (var18 & '\uff00') * var17 & 16711680) >>> 8;
        }

        var3 += var9;
      }

      var4 += var10;
      var3 = var12;
      var5 += var6;
    }

  }

  private static void c663(final int[] var0, final int[] var1, int var3, int var4, int var5, final int var6, final int var7, final int var8, final int var9) {
    final int var10 = var9 >> 16 & 255;
    final int var11 = var9 >> 8 & 255;
    final int var12 = var9 & 255;
    final int var13 = -(var5 >> 2);
    var5 = -(var5 & 3);
    final int var14 = var13 + var13 + var13 + var13 + var5;

    for (int var15 = -var6; var15 < 0; ++var15) {
      for (int var16 = var14; var16 < 0; ++var16) {
        final int var2 = var1[var3++];
        final int var17 = var2 >>> 24;
        if (var17 == 0) {
          ++var4;
        } else {
          final int var19 = var2 >> 16 & 255;
          final int var20 = var2 >> 8 & 255;
          final int var21 = var2 & 255;
          final int var18;
          if (var19 == var20 && var20 == var21) {
            if (var19 <= 128) {
              var18 = (var19 * var10 >> 7 << 16) + (var20 * var11 >> 7 << 8) + (var21 * var12 >> 7);
            } else {
              var18 = (var10 * (256 - var19) + 255 * (var19 - 128) >> 7 << 16) + (var11 * (256 - var20) + 255 * (var20 - 128) >> 7 << 8) + (var12 * (256 - var21) + 255 * (var21 - 128) >> 7);
            }
          } else {
            var18 = var2;
          }

          final int var22 = 256 - var17;
          final int var23 = var0[var4];
          var0[var4++] = ((var18 & 16711935) * var17 + (var23 & 16711935) * var22 & -16711936) + ((var18 & '\uff00') * var17 + (var23 & '\uff00') * var22 & 16711680) >>> 8;
        }
      }

      var4 += var7;
      var3 += var8;
    }

  }

  @Override
  public void drawAdd(int x, int y, final int alpha) {
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
      b057(Drawing.screenBuffer, this.pixels, var5, var4, var7, var6, var8, var9, alpha);
    }
  }

  @Override
  public void drawTinted2(int x, int y, final int color) {
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
      d983(Drawing.screenBuffer, this.pixels, var5, var4, var7, var6, var8, var9, color);
    }
  }

  @Override
  public void b115(int x, int y, int width, int height) {
    if (width > 0 && height > 0) {
      int var7 = 0;
      int var8 = 0;
      final int var9 = this.offsetX;
      final int var10 = this.offsetY;
      final int var11 = (var9 << 16) / width;
      final int var12 = (var10 << 16) / height;
      int var13;
      if (this.x > 0) {
        var13 = ((this.x << 16) + var11 - 1) / var11;
        x += var13;
        var7 += var13 * var11 - (this.x << 16);
      }

      if (this.y > 0) {
        var13 = ((this.y << 16) + var12 - 1) / var12;
        y += var13;
        var8 += var13 * var12 - (this.y << 16);
      }

      if (this.width < var9) {
        width = ((this.width << 16) - var7 + var11 - 1) / var11;
      }

      if (this.height < var10) {
        height = ((this.height << 16) - var8 + var12 - 1) / var12;
      }

      var13 = Drawing.pixelIndex(x, y);
      int var14 = Drawing.width - width;
      if (y + height > Drawing.bottom) {
        height -= y + height - Drawing.bottom;
      }

      int var15;
      if (y < Drawing.top) {
        var15 = Drawing.top - y;
        height -= var15;
        var13 += var15 * Drawing.width;
        var8 += var12 * var15;
      }

      if (x + width > Drawing.right) {
        var15 = x + width - Drawing.right;
        width -= var15;
        var14 += var15;
      }

      if (x < Drawing.left) {
        var15 = Drawing.left - x;
        width -= var15;
        var13 += var15;
        var7 += var11 * var15;
        var14 += var15;
      }

      c983(Drawing.screenBuffer, this.pixels, var7, var8, var13, var14, width, height, var11, var12, this.width);
    }
  }

  @Override
  public void drawTinted(int x, int y, final int color) {
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
      c663(Drawing.screenBuffer, this.pixels, var5, var4, var7, var6, var8, var9, color);
    }
  }

  @Override
  public void b050(int var1, int var2, int var3, int var4, final int var5) {
    if (var3 > 0 && var4 > 0) {
      final int var6 = this.width;
      final int var7 = this.height;
      int var8 = 0;
      int var9 = 0;
      final int var10 = this.offsetX;
      final int var11 = this.offsetY;
      final int var12 = (var10 << 16) / var3;
      final int var13 = (var11 << 16) / var4;
      int var14;
      if (this.x > 0) {
        var14 = ((this.x << 16) + var12 - 1) / var12;
        var1 += var14;
        var8 += var14 * var12 - (this.x << 16);
      }

      if (this.y > 0) {
        var14 = ((this.y << 16) + var13 - 1) / var13;
        var2 += var14;
        var9 += var14 * var13 - (this.y << 16);
      }

      if (var6 < var10) {
        var3 = ((var6 << 16) - var8 + var12 - 1) / var12;
      }

      if (var7 < var11) {
        var4 = ((var7 << 16) - var9 + var13 - 1) / var13;
      }

      var14 = Drawing.pixelIndex(var1, var2);
      int var15 = Drawing.width - var3;
      if (var2 + var4 > Drawing.bottom) {
        var4 -= var2 + var4 - Drawing.bottom;
      }

      int var16;
      if (var2 < Drawing.top) {
        var16 = Drawing.top - var2;
        var4 -= var16;
        var14 += var16 * Drawing.width;
        var9 += var13 * var16;
      }

      if (var1 + var3 > Drawing.right) {
        var16 = var1 + var3 - Drawing.right;
        var3 -= var16;
        var15 += var16;
      }

      if (var1 < Drawing.left) {
        var16 = Drawing.left - var1;
        var3 -= var16;
        var14 += var16;
        var8 += var12 * var16;
        var15 += var16;
      }

      b590(Drawing.screenBuffer, this.pixels, var8, var9, var14, var15, var3, var4, var12, var13, var6, var5);
    }
  }

  public void i093(int var1, int var2) {
    var1 += this.x;
    var2 += this.offsetY - this.height - this.y;
    int var3 = Drawing.pixelIndex(var1, var2 + this.height - 1);
    int var4 = this.width - 1;
    int var5 = this.height;
    int var6 = this.width;
    int var7 = -Drawing.width - var6;
    int var8 = var6 + var6;
    if (var2 < Drawing.top) {
      var5 -= Drawing.top - var2;
      var2 = Drawing.top;
    }

    int var9;
    if (var2 + var5 > Drawing.bottom) {
      var9 = var2 + var5 - Drawing.bottom;
      var5 -= var9;
      var3 -= var9 * Drawing.width;
      var4 += var9 * var6;
    }

    if (var1 < Drawing.left) {
      var9 = Drawing.left - var1;
      var6 -= var9;
      var1 = Drawing.left;
      var4 -= var9;
      var3 += var9;
      var8 -= var9;
      var7 += var9;
    }

    if (var1 + var6 > Drawing.right) {
      var9 = var1 + var6 - Drawing.right;
      var6 -= var9;
      var8 -= var9;
      var7 += var9;
    }

    if (var6 > 0 && var5 > 0) {
      d650(Drawing.screenBuffer, this.pixels, var4, var3, var6, var5, var7, var8);
    }
  }

  @Override
  public void g093() {
    int var1 = 0;
    int var2 = 0;
    final int var3 = this.width >> 2;
    final int var4 = this.height >> 2;
    var1 += this.x / 4;
    var2 += this.y / 4;
    final int var5 = var1 < Drawing.left ? Drawing.left - var1 << 2 : 0;
    final int var6 = var1 + var3 > Drawing.right ? (Drawing.right - var1 << 2) - 4 : this.width - 4;
    final int var7 = var2 < Drawing.top ? Drawing.top - var2 << 2 : 0;
    final int var8 = var2 + var4 > Drawing.bottom ? (Drawing.bottom - var2 << 2) - 4 : this.height - 4;
    final int[] var9 = new int[16];

    for (int var10 = var7; var10 <= var8; var10 += 4) {
      for (int var11 = var5; var11 <= var6; var11 += 4) {
        final int var12 = var10 * this.width + var11;
        final int var13 = (var2 + (var10 >> 2)) * Drawing.width + var1 + (var11 >> 2);

        int var14;
        int var15;
        for (var14 = 0; var14 < 4; ++var14) {
          for (var15 = 0; var15 < 4; ++var15) {
            var9[(var14 << 2) + var15] = this.pixels[var12 + var14 * this.width + var15];
          }
        }

        var15 = 0;
        int var16 = 0;
        int var17 = 0;
        int var18 = 0;

        int var19;
        for (var19 = 0; var19 < 16; ++var19) {
          var14 = var9[var19] >>> 24;
          var15 += var14;
          var16 += var14 * (var9[var19] >> 16 & 255);
          var17 += var14 * (var9[var19] >> 8 & 255);
          var18 += var14 * (var9[var19] & 255);
        }

        if (var15 != 0) {
          var16 = (var16 / var15 << 16) + var18 / var15;
          var17 = var17 / var15 << 8;
          var19 = var15 >> 4;
          final int var20 = 256 - var19;
          final int var21 = Drawing.screenBuffer[var13];
          Drawing.screenBuffer[var13] = (var19 * var16 + var20 * (var21 & 16711935) & -16711936) + (var19 * var17 + var20 * (var21 & '\uff00') & 16711680) >>> 8;
        }
      }
    }

  }

  @Override
  public void d093(int var1, int var2) {
    final int var3 = this.width >> 1;
    final int var4 = this.height >> 1;
    var1 += this.x / 2;
    var2 += this.y / 2;
    final int var5 = var1 < Drawing.left ? Drawing.left - var1 << 1 : 0;
    final int var6 = var1 + var3 > Drawing.right ? (Drawing.right - var1 << 1) - 2 : this.width - 2;
    final int var7 = var2 < Drawing.top ? Drawing.top - var2 << 1 : 0;
    final int var8 = var2 + var4 > Drawing.bottom ? (Drawing.bottom - var2 << 1) - 2 : this.height - 2;

    for (int var9 = var7; var9 <= var8; var9 += 2) {
      int var10 = var9 * this.width + var5;
      int var11 = (var2 + (var9 >> 1)) * Drawing.width + var1 + (var5 >> 1);

      for (int var12 = var5; var12 <= var6; var10 += 2) {
        int var15 = 0;
        int var16 = 0;
        int var17 = 0;
        int var18 = 0;

        int var19;
        for (var19 = 0; var19 < 4; ++var19) {
          final int var22 = this.pixels[var10 + (var19 & 1) + ((var19 & 2) == 0 ? this.width : 0)];
          final int var23 = var22 >>> 24;
          var18 += var23;
          var15 += var23 * (var22 >> 16 & 255);
          var16 += var23 * (var22 >> 8 & 255);
          var17 += var23 * (var22 & 255);
        }

        if (var18 != 0) {
          var15 = (var15 / var18 << 16) + var17 / var18;
          var16 = var16 / var18 << 8;
          var19 = var18 >> 2;
          final int var20 = 256 - var19;
          final int var21 = Drawing.screenBuffer[var11];
          Drawing.screenBuffer[var11] = (var19 * var15 + var20 * (var21 & 16711935) & -16711936) + (var19 * var16 + var20 * (var21 & '\uff00') & 16711680) >>> 8;
        }

        var12 += 2;
        ++var11;
      }
    }

  }

  @Override
  public void a669(int var1, int var2, final int var3, final int var4, final int var5, final int var6) {
    if (var6 != 0) {
      var1 -= this.x << 4;
      var2 -= this.y << 4;
      final double var7 = (double) (var5 & '\uffff') * 9.587379924285257E-5D;
      final int var9 = (int) Math.floor(Math.sin(var7) * (double) var6 + 0.5D);
      final int var10 = (int) Math.floor(Math.cos(var7) * (double) var6 + 0.5D);
      final int var11 = -var1 * var10 + -var2 * var9;
      final int var12 = -(-var1) * var9 + -var2 * var10;
      final int var13 = ((this.width << 4) - var1) * var10 + -var2 * var9;
      final int var14 = -((this.width << 4) - var1) * var9 + -var2 * var10;
      final int var15 = -var1 * var10 + ((this.height << 4) - var2) * var9;
      final int var16 = -(-var1) * var9 + ((this.height << 4) - var2) * var10;
      final int var17 = ((this.width << 4) - var1) * var10 + ((this.height << 4) - var2) * var9;
      final int var18 = -((this.width << 4) - var1) * var9 + ((this.height << 4) - var2) * var10;
      int var19;
      int var20;
      if (var11 < var13) {
        var19 = var11;
        var20 = var13;
      } else {
        var19 = var13;
        var20 = var11;
      }

      if (var15 < var19) {
        var19 = var15;
      }

      if (var17 < var19) {
        var19 = var17;
      }

      if (var15 > var20) {
        var20 = var15;
      }

      if (var17 > var20) {
        var20 = var17;
      }

      int var21;
      int var22;
      if (var12 < var14) {
        var21 = var12;
        var22 = var14;
      } else {
        var21 = var14;
        var22 = var12;
      }

      if (var16 < var21) {
        var21 = var16;
      }

      if (var18 < var21) {
        var21 = var18;
      }

      if (var16 > var22) {
        var22 = var16;
      }

      if (var18 > var22) {
        var22 = var18;
      }

      var19 >>= 12;
      var20 = var20 + 4095 >> 12;
      var21 >>= 12;
      var22 = var22 + 4095 >> 12;
      var19 += var3;
      var20 += var3;
      var21 += var4;
      var22 += var4;
      var19 >>= 4;
      var20 = var20 + 15 >> 4;
      var21 >>= 4;
      var22 = var22 + 15 >> 4;
      if (var19 < Drawing.left) {
        var19 = Drawing.left;
      }

      if (var20 > Drawing.right) {
        var20 = Drawing.right;
      }

      if (var21 < Drawing.top) {
        var21 = Drawing.top;
      }

      if (var22 > Drawing.bottom) {
        var22 = Drawing.bottom;
      }

      var20 = var19 - var20;
      if (var20 < 0) {
        var22 = var21 - var22;
        if (var22 < 0) {
          int var23 = var21 * Drawing.width + var19;
          final double var24 = 1.6777216E7D / (double) var6;
          final int var26 = (int) Math.floor(Math.sin(var7) * var24 + 0.5D);
          final int var27 = (int) Math.floor(Math.cos(var7) * var24 + 0.5D);
          final int var28 = (var19 << 4) + 8 - var3;
          final int var29 = (var21 << 4) + 8 - var4;
          int var30 = (var1 << 8) - (var29 * var26 >> 4);
          int var31 = (var2 << 8) + (var29 * var27 >> 4);
          int var32;
          int var33;
          int var34;
          int var35;
          int var36;
          int var37;
          int var38;
          int var39;
          int var40;
          int var41;
          if (var27 == 0) {
            if (var26 == 0) {
              for (var33 = var22; var33 < 0; var23 += Drawing.width) {
                var34 = var23;
                var35 = var30;
                var36 = var31;
                var37 = var20;
                if (var30 >= 0 && var31 >= 0 && var30 - (this.width << 12) < 0 && var31 - (this.height << 12) < 0) {
                  while (var37 < 0) {
                    var38 = this.pixels[(var36 >> 12) * this.width + (var35 >> 12)];
                    var39 = Drawing.screenBuffer[var34];
                    var40 = var38 >>> 24;
                    var41 = 256 - var40;
                    Drawing.screenBuffer[var34++] = ((var38 & 16711935) * var40 + (var39 & 16711935) * var41 & -16711936) + ((var38 & '\uff00') * var40 + (var39 & '\uff00') * var41 & 16711680) >>> 8;
                    ++var37;
                  }
                }

                ++var33;
              }
            } else if (var26 < 0) {
              for (var33 = var22; var33 < 0; var23 += Drawing.width) {
                var34 = var23;
                var35 = var30;
                var36 = var31 + (var28 * var26 >> 4);
                var37 = var20;
                if (var30 >= 0 && var30 - (this.width << 12) < 0) {
                  if ((var32 = var36 - (this.height << 12)) >= 0) {
                    var32 = (var26 - var32) / var26;
                    var37 = var20 + var32;
                    var36 += var26 * var32;
                    var34 = var23 + var32;
                  }

                  if ((var32 = (var36 - var26) / var26) > var37) {
                    var37 = var32;
                  }

                  while (var37 < 0) {
                    var38 = this.pixels[(var36 >> 12) * this.width + (var35 >> 12)];
                    var39 = Drawing.screenBuffer[var34];
                    var40 = var38 >>> 24;
                    var41 = 256 - var40;
                    Drawing.screenBuffer[var34++] = ((var38 & 16711935) * var40 + (var39 & 16711935) * var41 & -16711936) + ((var38 & '\uff00') * var40 + (var39 & '\uff00') * var41 & 16711680) >>> 8;
                    var36 += var26;
                    ++var37;
                  }
                }

                ++var33;
                var30 -= var26;
              }
            } else {
              for (var33 = var22; var33 < 0; var23 += Drawing.width) {
                var34 = var23;
                var35 = var30;
                var36 = var31 + (var28 * var26 >> 4);
                var37 = var20;
                if (var30 >= 0 && var30 - (this.width << 12) < 0) {
                  if (var36 < 0) {
                    var32 = (var26 - 1 - var36) / var26;
                    var37 = var20 + var32;
                    var36 += var26 * var32;
                    var34 = var23 + var32;
                  }

                  if ((var32 = (1 + var36 - (this.height << 12) - var26) / var26) > var37) {
                    var37 = var32;
                  }

                  while (var37 < 0) {
                    var38 = this.pixels[(var36 >> 12) * this.width + (var35 >> 12)];
                    var39 = Drawing.screenBuffer[var34];
                    var40 = var38 >>> 24;
                    var41 = 256 - var40;
                    Drawing.screenBuffer[var34++] = ((var38 & 16711935) * var40 + (var39 & 16711935) * var41 & -16711936) + ((var38 & '\uff00') * var40 + (var39 & '\uff00') * var41 & 16711680) >>> 8;
                    var36 += var26;
                    ++var37;
                  }
                }

                ++var33;
                var30 -= var26;
              }
            }
          } else if (var27 < 0) {
            if (var26 == 0) {
              for (var33 = var22; var33 < 0; var23 += Drawing.width) {
                var34 = var23;
                var35 = var30 + (var28 * var27 >> 4);
                var36 = var31;
                var37 = var20;
                if (var31 >= 0 && var31 - (this.height << 12) < 0) {
                  if ((var32 = var35 - (this.width << 12)) >= 0) {
                    var32 = (var27 - var32) / var27;
                    var37 = var20 + var32;
                    var35 += var27 * var32;
                    var34 = var23 + var32;
                  }

                  if ((var32 = (var35 - var27) / var27) > var37) {
                    var37 = var32;
                  }

                  while (var37 < 0) {
                    var38 = this.pixels[(var36 >> 12) * this.width + (var35 >> 12)];
                    var39 = Drawing.screenBuffer[var34];
                    var40 = var38 >>> 24;
                    var41 = 256 - var40;
                    Drawing.screenBuffer[var34++] = ((var38 & 16711935) * var40 + (var39 & 16711935) * var41 & -16711936) + ((var38 & '\uff00') * var40 + (var39 & '\uff00') * var41 & 16711680) >>> 8;
                    var35 += var27;
                    ++var37;
                  }
                }

                ++var33;
                var31 += var27;
              }
            } else if (var26 < 0) {
              for (var33 = var22; var33 < 0; var23 += Drawing.width) {
                var34 = var23;
                var35 = var30 + (var28 * var27 >> 4);
                var36 = var31 + (var28 * var26 >> 4);
                var37 = var20;
                if ((var32 = var35 - (this.width << 12)) >= 0) {
                  var32 = (var27 - var32) / var27;
                  var37 = var20 + var32;
                  var35 += var27 * var32;
                  var36 += var26 * var32;
                  var34 = var23 + var32;
                }

                if ((var32 = (var35 - var27) / var27) > var37) {
                  var37 = var32;
                }

                if ((var32 = var36 - (this.height << 12)) >= 0) {
                  var32 = (var26 - var32) / var26;
                  var37 += var32;
                  var35 += var27 * var32;
                  var36 += var26 * var32;
                  var34 += var32;
                }

                if ((var32 = (var36 - var26) / var26) > var37) {
                  var37 = var32;
                }

                while (var37 < 0) {
                  var38 = this.pixels[(var36 >> 12) * this.width + (var35 >> 12)];
                  var39 = Drawing.screenBuffer[var34];
                  var40 = var38 >>> 24;
                  var41 = 256 - var40;
                  Drawing.screenBuffer[var34++] = ((var38 & 16711935) * var40 + (var39 & 16711935) * var41 & -16711936) + ((var38 & '\uff00') * var40 + (var39 & '\uff00') * var41 & 16711680) >>> 8;
                  var35 += var27;
                  var36 += var26;
                  ++var37;
                }

                ++var33;
                var30 -= var26;
                var31 += var27;
              }
            } else {
              for (var33 = var22; var33 < 0; var23 += Drawing.width) {
                var34 = var23;
                var35 = var30 + (var28 * var27 >> 4);
                var36 = var31 + (var28 * var26 >> 4);
                var37 = var20;
                if ((var32 = var35 - (this.width << 12)) >= 0) {
                  var32 = (var27 - var32) / var27;
                  var37 = var20 + var32;
                  var35 += var27 * var32;
                  var36 += var26 * var32;
                  var34 = var23 + var32;
                }

                if ((var32 = (var35 - var27) / var27) > var37) {
                  var37 = var32;
                }

                if (var36 < 0) {
                  var32 = (var26 - 1 - var36) / var26;
                  var37 += var32;
                  var35 += var27 * var32;
                  var36 += var26 * var32;
                  var34 += var32;
                }

                if ((var32 = (1 + var36 - (this.height << 12) - var26) / var26) > var37) {
                  var37 = var32;
                }

                while (var37 < 0) {
                  var38 = this.pixels[(var36 >> 12) * this.width + (var35 >> 12)];
                  var39 = Drawing.screenBuffer[var34];
                  var40 = var38 >>> 24;
                  var41 = 256 - var40;
                  Drawing.screenBuffer[var34++] = ((var38 & 16711935) * var40 + (var39 & 16711935) * var41 & -16711936) + ((var38 & '\uff00') * var40 + (var39 & '\uff00') * var41 & 16711680) >>> 8;
                  var35 += var27;
                  var36 += var26;
                  ++var37;
                }

                ++var33;
                var30 -= var26;
                var31 += var27;
              }
            }
          } else if (var26 == 0) {
            for (var33 = var22; var33 < 0; var23 += Drawing.width) {
              var34 = var23;
              var35 = var30 + (var28 * var27 >> 4);
              var36 = var31;
              var37 = var20;
              if (var31 >= 0 && var31 - (this.height << 12) < 0) {
                if (var35 < 0) {
                  var32 = (var27 - 1 - var35) / var27;
                  var37 = var20 + var32;
                  var35 += var27 * var32;
                  var34 = var23 + var32;
                }

                if ((var32 = (1 + var35 - (this.width << 12) - var27) / var27) > var37) {
                  var37 = var32;
                }

                while (var37 < 0) {
                  var38 = this.pixels[(var36 >> 12) * this.width + (var35 >> 12)];
                  var39 = Drawing.screenBuffer[var34];
                  var40 = var38 >>> 24;
                  var41 = 256 - var40;
                  Drawing.screenBuffer[var34++] = ((var38 & 16711935) * var40 + (var39 & 16711935) * var41 & -16711936) + ((var38 & '\uff00') * var40 + (var39 & '\uff00') * var41 & 16711680) >>> 8;
                  var35 += var27;
                  ++var37;
                }
              }

              ++var33;
              var31 += var27;
            }
          } else if (var26 < 0) {
            for (var33 = var22; var33 < 0; var23 += Drawing.width) {
              var34 = var23;
              var35 = var30 + (var28 * var27 >> 4);
              var36 = var31 + (var28 * var26 >> 4);
              var37 = var20;
              if (var35 < 0) {
                var32 = (var27 - 1 - var35) / var27;
                var37 = var20 + var32;
                var35 += var27 * var32;
                var36 += var26 * var32;
                var34 = var23 + var32;
              }

              if ((var32 = (1 + var35 - (this.width << 12) - var27) / var27) > var37) {
                var37 = var32;
              }

              if ((var32 = var36 - (this.height << 12)) >= 0) {
                var32 = (var26 - var32) / var26;
                var37 += var32;
                var35 += var27 * var32;
                var36 += var26 * var32;
                var34 += var32;
              }

              if ((var32 = (var36 - var26) / var26) > var37) {
                var37 = var32;
              }

              while (var37 < 0) {
                var38 = this.pixels[(var36 >> 12) * this.width + (var35 >> 12)];
                var39 = Drawing.screenBuffer[var34];
                var40 = var38 >>> 24;
                var41 = 256 - var40;
                Drawing.screenBuffer[var34++] = ((var38 & 16711935) * var40 + (var39 & 16711935) * var41 & -16711936) + ((var38 & '\uff00') * var40 + (var39 & '\uff00') * var41 & 16711680) >>> 8;
                var35 += var27;
                var36 += var26;
                ++var37;
              }

              ++var33;
              var30 -= var26;
              var31 += var27;
            }
          } else {
            for (var33 = var22; var33 < 0; var23 += Drawing.width) {
              var34 = var23;
              var35 = var30 + (var28 * var27 >> 4);
              var36 = var31 + (var28 * var26 >> 4);
              var37 = var20;
              if (var35 < 0) {
                var32 = (var27 - 1 - var35) / var27;
                var37 = var20 + var32;
                var35 += var27 * var32;
                var36 += var26 * var32;
                var34 = var23 + var32;
              }

              if ((var32 = (1 + var35 - (this.width << 12) - var27) / var27) > var37) {
                var37 = var32;
              }

              if (var36 < 0) {
                var32 = (var26 - 1 - var36) / var26;
                var37 += var32;
                var35 += var27 * var32;
                var36 += var26 * var32;
                var34 += var32;
              }

              if ((var32 = (1 + var36 - (this.height << 12) - var26) / var26) > var37) {
                var37 = var32;
              }

              while (var37 < 0) {
                var38 = this.pixels[(var36 >> 12) * this.width + (var35 >> 12)];
                var39 = Drawing.screenBuffer[var34];
                var40 = var38 >>> 24;
                var41 = 256 - var40;
                Drawing.screenBuffer[var34++] = ((var38 & 16711935) * var40 + (var39 & 16711935) * var41 & -16711936) + ((var38 & '\uff00') * var40 + (var39 & '\uff00') * var41 & 16711680) >>> 8;
                var35 += var27;
                var36 += var26;
                ++var37;
              }

              ++var33;
              var30 -= var26;
              var31 += var27;
            }
          }

        }
      }
    }
  }

  @Override
  public void c093(int x, int y) {
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
      this.compositeOver(this.pixels, var8, var4, Drawing.screenBuffer, var7, var3, var6, var5);
    }
  }

  @Override
  public ArgbSprite copy() {
    final ArgbSprite sprite = new ArgbSprite(this.width, this.height);
    sprite.x = this.x;
    sprite.y = this.y;
    sprite.offsetX = this.offsetX;
    sprite.offsetY = this.offsetY;
    System.arraycopy(this.pixels, 0, sprite.pixels, 0, this.pixels.length);
    return sprite;
  }

  public void h093(int var1, int var2) {
    var1 += this.offsetX - this.width - this.x;
    var2 += this.y;
    int var3 = Drawing.pixelIndex(var1, var2);
    int var4 = this.width - 1;
    int var5 = this.height;
    int var6 = this.width;
    int var7 = Drawing.width - var6;
    int var8 = var6 + var6;
    int var9;
    if (var2 < Drawing.top) {
      var9 = Drawing.top - var2;
      var5 -= var9;
      var2 = Drawing.top;
      var4 += var9 * var6;
      var3 += var9 * Drawing.width;
    }

    if (var2 + var5 > Drawing.bottom) {
      var5 -= var2 + var5 - Drawing.bottom;
    }

    if (var1 < Drawing.left) {
      var9 = Drawing.left - var1;
      var6 -= var9;
      var1 = Drawing.left;
      var4 -= var9;
      var3 += var9;
      var8 -= var9;
      var7 += var9;
    }

    if (var1 + var6 > Drawing.right) {
      var9 = var1 + var6 - Drawing.right;
      var6 -= var9;
      var8 -= var9;
      var7 += var9;
    }

    if (var6 > 0 && var5 > 0) {
      d650(Drawing.screenBuffer, this.pixels, var4, var3, var6, var5, var7, var8);
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
      d663(Drawing.screenBuffer, this.pixels, var5, var4, var7, var6, var8, var9, alpha);
    }
  }
}
