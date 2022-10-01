package funorb.graphics;

import java.awt.Component;
import java.awt.Image;
import java.awt.MediaTracker;
import java.awt.Toolkit;
import java.awt.image.PixelGrabber;

public class Sprite {
  public int x;
  public int y;
  public int width;
  public int height;
  public int offsetX;
  public int offsetY;
  public int[] pixels;

  public Sprite(final int offsetX, final int offsetY, final int x, final int y, final int width, final int height, final int[] pixels) {
    this.offsetX = offsetX;
    this.offsetY = offsetY;
    this.x = x;
    this.y = y;
    this.width = width;
    this.height = height;
    this.pixels = pixels;
  }

  public Sprite(final int var1, final int var2) {
    this.pixels = new int[var1 * var2];
    this.width = this.offsetX = var1;
    this.height = this.offsetY = var2;
    this.y = 0;
    this.x = 0;
  }

  public Sprite(final byte[] imageData, final Component canvas) {
    try {
      final Image image = Toolkit.getDefaultToolkit().createImage(imageData);
      final MediaTracker tracker = new MediaTracker(canvas);
      tracker.addImage(image, 0);
      tracker.waitForAll();
      this.width = image.getWidth(canvas);
      this.height = image.getHeight(canvas);
      this.offsetX = this.width;
      this.offsetY = this.height;
      this.x = 0;
      this.y = 0;
      this.pixels = new int[this.width * this.height];
      new PixelGrabber(image, 0, 0, this.width, this.height, this.pixels, 0, this.width).grabPixels();
    } catch (final InterruptedException var6) {
    }
  }

  private static void compositeOverTinted(final int[] src, final int srcStride, int srcPos, final int[] dest, final int destStride, int destPos, final int cols, final int rows, final int color) {
    final int r1 = (color >> 16) & 255;
    final int g1 = (color >> 8) & 255;
    final int b1 = color & 255;
    final int var14 = (-(cols >> 2) << 2) - (cols & 3);

    for (int i = 0; i < rows; ++i) {
      for (int j = var14; j < 0; ++j) {
        final int srcColor = src[srcPos++];
        if (srcColor != 0) {
          final int r2 = (srcColor >> 16) & 255;
          final int g2 = (srcColor >> 8) & 255;
          final int b2 = srcColor & 255;
          if (r2 == g2 && g2 == b2) {
            final int r3;
            final int g3;
            final int b3;
            if (r2 <= 128) {
              r3 = ((r2 * r1) >> 7) << 16;
              g3 = ((g2 * g1) >> 7) << 8;
              b3 = (b2 * b1) >> 7;
            } else {
              r3 = (((r1 * (256 - r2)) + (255 * (r2 - 128))) >> 7) << 16;
              g3 = (((g1 * (256 - g2)) + (255 * (g2 - 128))) >> 7) << 8;
              b3 = ((b1 * (256 - b2)) + (255 * (b2 - 128))) >> 7;
            }
            dest[destPos] = r3 + g3 + b3;
          } else {
            dest[destPos] = srcColor;
          }
        }
        destPos++;
      }

      destPos += destStride;
      srcPos += srcStride;
    }
  }

  private static void a543(int var3, final int[] var4, final int[] var5, int var8, int var9, int var10, final int var11, final int var12, final int var13, final int var14, final int var15) {
    for (final int var16 = var3; var8 < 0; ++var8) {
      final int var7 = (var9 >> 16) * var15;

      int var6;
      for (var6 = -var12; var6 < 0; ++var6) {
        int var0 = var4[(var3 >> 16) + var7];
        if (var0 == 0) {
          ++var10;
        } else {
          final int var1 = var5[var10];
          final int var2 = var0 + var1;
          var0 = (var0 & 16711935) + (var1 & 16711935);
          final int i = (var0 & 16777472) + (var2 - var0 & 65536);
          var5[var10++] = var2 - i | i - (i >>> 8);
        }

        var3 += var13;
      }

      var9 += var14;
      var3 = var16;
      var10 += var11;
    }

  }

  protected void compositeOver(final int[] src, final int srcStride, int srcPos, final int[] dest, final int destStride, int destPos, final int cols, final int rows) {
    for (int i = 0; i < rows; ++i) {
      for (int j = 0; j < cols; ++j) {
        final int color = src[srcPos++];
        if (color == 0) {
          ++destPos;
        } else {
          dest[destPos++] = color;
        }
      }

      srcPos += srcStride;
      destPos += destStride;
    }
  }

  private static void compositeOver(final int[] src, final int srcStride, int srcPos, final int[] dest, final int destStride, int destPos, final int cols, final int rows, final int alpha) {
    final int alpha2 = 256 - alpha;

    for (int i = 0; i < rows; ++i) {
      for (int j = 0; j < cols; ++j) {
        final int srcColor = src[srcPos++];
        if (srcColor == 0) {
          ++destPos;
        } else {
          final int destColor = dest[destPos];
          final int rb = (((srcColor & 0xff00ff) * alpha) + ((destColor & 0xff00ff) * alpha2)) & 0xff00ff00;
          final int g = (((srcColor & 0x00ff00) * alpha) + ((destColor & 0x00ff00) * alpha2)) & 0xff0000;
          dest[destPos++] = (rb + g) >> 8;
        }
      }

      destPos += destStride;
      srcPos += srcStride;
    }
  }

  private static void compositeAdd(final int[] src, final int srcStride, int srcPos, final int[] dest, final int destStride, int destPos, final int cols, final int rows, final int alpha) {
    for (int i = 0; i < rows; ++i) {
      for (int j = 0; j < cols; ++j) {
        final int srcColor = src[srcPos++];
        if (srcColor != 0) {
          dest[destPos] = Drawing.saturatingAdd(srcColor, dest[destPos], alpha);
        }
        destPos++;
      }

      destPos += destStride;
      srcPos += srcStride;
    }
  }

  private static void compositeAdd(final int[] src, final int srcStride, int srcPos, final int[] dest, final int destStride, int destPos, final int cols, final int rows) {
    for (int i = 0; i < rows; ++i) {
      for (int j = 0; j < cols; ++j) {
        final int srcColor = src[srcPos++];
        if (srcColor != 0) {
          dest[destPos] = Drawing.saturatingAdd(srcColor, dest[destPos]);
        }
        destPos++;
      }

      destPos += destStride;
      srcPos += srcStride;
    }
  }

  private static void a871(final int[] var1, final int[] var2, int var4, int var5, final int var6, final int var7, final int var8, final int var9) {
    for (int var10 = -var7; var10 < 0; ++var10) {
      for (int var11 = -var6; var11 < 0; ++var11) {
        final int var3 = var2[var4++];
        if (var3 == 0) {
          ++var5;
        } else {
          final int var0 = var1[var5];
          if (var0 == 0) {
            ++var5;
          } else {
            final int var12 = ((var3 & 16711680) >>> 16) * ((var0 & 16711680) >>> 16) >>> 8;
            final int var13 = (var3 & '\uff00') * (var0 & '\uff00') >>> 24;
            final int var14 = (var3 & 255) * (var0 & 255) >>> 8;
            var1[var5++] = (var12 << 16) + (var13 << 8) + var14;
          }
        }
      }

      var5 += var8;
      var4 += var9;
    }

  }

  private static void a600(final int[] var0, int var1, int var2, final int var3, final int var4, final int var5, final int var6, final int var7) {
    for (int var8 = 0; var8 < var7; var2 += var4) {
      for (int var9 = 0; var9 < var6; var1 += 2) {
        final int var11 = Drawing.screenBuffer[var2] & 16711935;
        final int var12 = Drawing.screenBuffer[var2] & '\uff00';
        final byte var13 = 0;
        final byte var14 = 0;
        int var10;
        int var15;
        int var16;
        if ((var10 = var0[var1]) == 0) {
          var15 = var13 + var11;
          var16 = var14 + var12;
        } else {
          var15 = var13 + (var10 & 16711935);
          var16 = var14 + (var10 & '\uff00');
        }

        if ((var10 = var0[var1 + 1]) == 0) {
          var15 += var11;
          var16 += var12;
        } else {
          var15 += var10 & 16711935;
          var16 += var10 & '\uff00';
        }

        if ((var10 = var0[var1 + var5]) == 0) {
          var15 += var11;
          var16 += var12;
        } else {
          var15 += var10 & 16711935;
          var16 += var10 & '\uff00';
        }

        if ((var10 = var0[var1 + var5 + 1]) == 0) {
          var15 += var11;
          var16 += var12;
        } else {
          var15 += var10 & 16711935;
          var16 += var10 & '\uff00';
        }

        Drawing.screenBuffer[var2++] = (var15 & 66847740 | var16 & 261120) >> 2;
        ++var9;
      }

      ++var8;
      var1 += var3;
    }

  }

  private static void compositeOverTinted2(final int[] src, final int srcStride, int srcPos, final int[] dest, final int destStride, int destPos, final int cols, final int rows, final int color) {
    final int rb1 = color & 0xff00ff;
    final int g1 = (color >> 8) & 255;

    for (int i = 0; i < rows; ++i) {
      for (int j = 0; j < cols; ++j) {
        final int color2 = src[srcPos++];
        if (color2 != 0) {
          if ((color2 >> 8) == (color2 & 0x00ffff)) {
            final int g2 = color2 & 255;
            dest[destPos] = (((g2 * rb1) >> 8) & 0xff00fe) + ((g2 * g1) & 0x00ff00) + 1;
          } else {
            dest[destPos] = color2;
          }
        }
        destPos++;
      }

      destPos += destStride;
      srcPos += srcStride;
    }
  }

  private static void a590(final int[] var0, final int[] var1, int var3, int var4, int var5, final int var6, final int var7, final int var8, final int var9, final int var10, final int var11, final int var12) {
    final int var13 = 256 - var12;
    final int var14 = var3;

    for (int var15 = -var8; var15 < 0; ++var15) {
      final int var16 = (var4 >> 16) * var11;

      for (int var17 = -var7; var17 < 0; ++var17) {
        final int var2 = var1[(var3 >> 16) + var16];
        if (var2 == 0) {
          ++var5;
        } else {
          final int var18 = var0[var5];
          var0[var5++] = ((var2 & 16711935) * var12 + (var18 & 16711935) * var13 & -16711936) + ((var2 & '\uff00') * var12 + (var18 & '\uff00') * var13 & 16711680) >> 8;
        }

        var3 += var9;
      }

      var4 += var10;
      var3 = var14;
      var5 += var6;
    }

  }

  private static void b983(final int[] var0, final int[] var1, int var3, int var4, int var5, final int var6, final int var7, final int var8, final int var9, final int var10, final int var11) {
    final int var12 = var3;

    for (int var13 = -var8; var13 < 0; ++var13) {
      final int var14 = (var4 >> 16) * var11;

      for (int var15 = -var7; var15 < 0; ++var15) {
        final int var2 = var1[(var3 >> 16) + var14];
        if (var2 == 0) {
          ++var5;
        } else {
          var0[var5++] = var2;
        }

        var3 += var9;
      }

      var4 += var10;
      var3 = var12;
      var5 += var6;
    }

  }

  private static void a134(int var3, final int[] var4, final int[] var5, int var8, int var9, int var10, final int var11, final int var12, final int var13, final int var14, final int var15, final int var16) {
    for (final int var17 = var3; var8 < 0; ++var8) {
      final int var7 = (var9 >> 16) * var15;

      int var6;
      for (var6 = -var12; var6 < 0; ++var6) {
        int var0 = var4[(var3 >> 16) + var7];
        if (var0 == 0) {
          ++var10;
        } else {
          final int var1 = (var0 & 16711935) * var16;
          var0 = (var1 & -16711936) + (var0 * var16 - var1 & 16711680) >>> 8;
          final int i = var5[var10];
          final int var2 = var0 + i;
          var0 = (var0 & 16711935) + (i & 16711935);
          final int i1 = (var0 & 16777472) + (var2 - var0 & 65536);
          var5[var10++] = var2 - i1 | i1 - (i1 >>> 8);
        }

        var3 += var13;
      }

      var9 += var14;
      var3 = var17;
      var10 += var11;
    }

  }

  private static void a415(final int[] dest, final int[] src, int srcPos, int destPos, final int var4, final int var5, final int var6, final int var7) {
    for (int i = 0; i < var5; ++i) {
      final int var9 = destPos + var4;
      while (destPos < var9) {
        dest[destPos++] = src[srcPos++];
      }
      destPos += var6;
      srcPos += var7;
    }
  }

  private static void b650(final int[] var0, final int[] var1, final int var2, int var3, int var4, int var5, final int var6, final int var7, final int var8) {
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

  public final void d797() {
    int var1 = 0;
    int var2 = this.width * this.height;

    int var10001;
    final int[] var3;
    for (var3 = new int[var2]; var2 > 0; var3[var10001] = this.pixels[var2]) {
      var10001 = var1++;
      --var2;
    }

    this.pixels = var3;
    this.x = this.offsetX - this.width - this.x;
    this.y = this.offsetY - this.height - this.y;
  }

  public void draw(int x, int y, final int alpha) {
    x += this.x;
    y += this.y;
    int rows = this.height;
    int cols = this.width;
    int srcPos = 0;
    int srcStride = 0;
    int destPos = Drawing.pixelIndex(x, y);
    int destStride = Drawing.width - cols;
    if (y < Drawing.top) {
      final int clippedRows = Drawing.top - y;
      y = Drawing.top;
      rows -= clippedRows;
      srcPos += clippedRows * this.width;
      destPos += clippedRows * Drawing.width;
    }
    if (rows > Drawing.bottom - y) {
      rows = Drawing.bottom - y;
    }

    if (x < Drawing.left) {
      final int clippedCols = Drawing.left - x;
      x = Drawing.left;
      cols -= clippedCols;
      srcPos += clippedCols;
      destPos += clippedCols;
      srcStride += clippedCols;
      destStride += clippedCols;
    }
    if (cols > Drawing.right - x) {
      final int clippedCols = cols - (Drawing.right - x);
      cols -= clippedCols;
      srcStride += clippedCols;
      destStride += clippedCols;
    }

    if (cols > 0 && rows > 0) {
      compositeOver(this.pixels, srcStride, srcPos, Drawing.screenBuffer, destStride, destPos, cols, rows, alpha);
    }
  }

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
          if (var27 == 0) {
            if (var26 == 0) {
              for (var33 = var22; var33 < 0; var23 += Drawing.width) {
                var34 = var23;
                var35 = var30;
                var36 = var31;
                var37 = var20;
                if (var30 >= 0 && var31 >= 0 && var30 - (this.width << 12) < 0 && var31 - (this.height << 12) < 0) {
                  for (; var37 < 0; ++var37) {
                    var38 = this.pixels[(var36 >> 12) * this.width + (var35 >> 12)];
                    if (var38 == 0) {
                      ++var34;
                    } else {
                      Drawing.screenBuffer[var34++] = var38;
                    }
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
                    if (var38 == 0) {
                      ++var34;
                    } else {
                      Drawing.screenBuffer[var34++] = var38;
                    }

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
                    if (var38 == 0) {
                      ++var34;
                    } else {
                      Drawing.screenBuffer[var34++] = var38;
                    }

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
                    if (var38 == 0) {
                      ++var34;
                    } else {
                      Drawing.screenBuffer[var34++] = var38;
                    }

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
                  if (var38 == 0) {
                    ++var34;
                  } else {
                    Drawing.screenBuffer[var34++] = var38;
                  }

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
                  if (var38 == 0) {
                    ++var34;
                  } else {
                    Drawing.screenBuffer[var34++] = var38;
                  }

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
                  if (var38 == 0) {
                    ++var34;
                  } else {
                    Drawing.screenBuffer[var34++] = var38;
                  }

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
                if (var38 == 0) {
                  ++var34;
                } else {
                  Drawing.screenBuffer[var34++] = var38;
                }

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
                if (var38 == 0) {
                  ++var34;
                } else {
                  Drawing.screenBuffer[var34++] = var38;
                }

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

  public final void e093() {
    int var1 = 0;
    int var2 = 0;
    var1 += this.x;
    var2 += this.y;
    int var3 = Drawing.pixelIndex(var1, var2);
    int var4 = 0;
    int var5 = this.height;
    int var6 = this.width;
    int var7 = Drawing.width - var6;
    int var8 = 0;
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
      var4 += var9;
      var3 += var9;
      var8 += var9;
      var7 += var9;
    }

    if (var1 + var6 > Drawing.right) {
      var9 = var1 + var6 - Drawing.right;
      var6 -= var9;
      var8 += var9;
      var7 += var9;
    }

    if (var6 > 0 && var5 > 0) {
      a871(Drawing.screenBuffer, this.pixels, var4, var3, var6, var5, var7, var8);
    }
  }

  public void drawTinted(int x, int y, final int color) {
    x += this.x;
    y += this.y;
    int rows = this.height;
    int cols = this.width;
    int srcPos = 0;
    int srcStride = 0;
    int destPos = Drawing.pixelIndex(x, y);
    int destStride = Drawing.width - cols;
    if (y < Drawing.top) {
      final int clippedRows = Drawing.top - y;
      y = Drawing.top;
      rows -= clippedRows;
      srcPos += clippedRows * this.width;
      destPos += clippedRows * Drawing.width;
    }
    if (rows > Drawing.bottom - y) {
      rows = Drawing.bottom - y;
    }

    if (x < Drawing.left) {
      final int clippedCols = Drawing.left - x;
      x = Drawing.left;
      cols -= clippedCols;
      srcPos += clippedCols;
      destPos += clippedCols;
      srcStride += clippedCols;
      destStride += clippedCols;
    }
    if (cols > Drawing.right - x) {
      final int clippedCols = cols - (Drawing.right - x);
      cols -= clippedCols;
      srcStride += clippedCols;
      destStride += clippedCols;
    }

    if (cols > 0 && rows > 0) {
      compositeOverTinted(this.pixels, srcStride, srcPos, Drawing.screenBuffer, destStride, destPos, cols, rows, color);
    }
  }

  public final void b797() {
    final int[] var1 = this.pixels;

    for (int var2 = (this.height >> 1) - 1; var2 >= 0; --var2) {
      int var3 = var2 * this.width;
      int var4 = (this.height - var2 - 1) * this.width;

      for (int var5 = -this.width; var5 < 0; ++var5) {
        final int var6 = var1[var3];
        var1[var3] = var1[var4];
        var1[var4] = var6;
        ++var3;
        ++var4;
      }
    }

    this.y = this.offsetY - this.height - this.y;
  }

  private void d050(final int var1, final int var2, final int var3, final int var4a, final int var5a) {
    final int var6 = var3 * this.width + var2;
    final int var4 = var4a & 0x0fff;
    final int var5 = var5a & 0x0fff;
    final int var7;
    final int var8;
    int var11;
    int var12;
    if (var3 >= 0) {
      if (var2 >= 0) {
        var7 = this.pixels[var6];
        var11 = var7 != 0 ? (4096 - var4) * (4096 - var5) : 0;
      } else {
        var11 = 0;
        var7 = 0;
      }

      if (var2 < this.width - 1) {
        var8 = this.pixels[var6 + 1];
        var12 = var8 != 0 ? var4 * (4096 - var5) : 0;
      } else {
        var12 = 0;
        var8 = 0;
      }
    } else {
      var12 = 0;
      var11 = 0;
      var8 = 0;
      var7 = 0;
    }

    final int var9;
    final int var10;
    int var13;
    int var14;
    if (var3 < this.height - 1) {
      if (var2 >= 0) {
        var9 = this.pixels[var6 + this.width];
        var13 = var9 != 0 ? (4096 - var4) * var5 : 0;
      } else {
        var13 = 0;
        var9 = 0;
      }

      if (var2 < this.width - 1) {
        var10 = this.pixels[var6 + this.width + 1];
        var14 = var10 != 0 ? var4 * var5 : 0;
      } else {
        var14 = 0;
        var10 = 0;
      }
    } else {
      var14 = 0;
      var13 = 0;
      var10 = 0;
      var9 = 0;
    }

    var11 >>= 16;
    var12 >>= 16;
    var13 >>= 16;
    var14 >>= 16;
    final int var15 = var11 + var12 + var13 + var14;
    final int i1 = (var7 & 0xff00ff) * var11 + (var8 & 0xff00ff) * var12 + ((var9 & 0xff00ff) * var13 + (var10 & 0xff00ff) * var14);
    final int i2 = (var7 & 0x00ff00) * var11 + (var8 & 0x00ff00) * var12 + ((var9 & 0x00ff00) * var13 + (var10 & 0x00ff00) * var14);
    if (var15 >= 256) {
      int var18 = (i1 >>> 8 & 0xff00ff) + (i2 >>> 8 & 0x00ff00);
      if (var18 == 0) {
        var18 = 1;
      }

      Drawing.screenBuffer[var1] = var18;
    } else if (var15 >= 128) {
      int var18 = ((i1 >>> 16) / var15 << 16) + (i2 / var15 & 0x00ff00) + (i1 & 0x00ffff) / var15;
      if (var18 == 0) {
        var18 = 1;
      }

      Drawing.screenBuffer[var1] = var18;
    }
  }

  public final void f797() {
    final int[] pixels = new int[this.width * this.height];
    int i = 0;
    for (int x = 0; x < this.width; ++x) {
      for (int y = this.height - 1; y >= 0; --y) {
        pixels[i++] = this.pixels[x + y * this.width];
      }
    }
    this.pixels = pixels;

    final int tmp1 = this.y;
    //noinspection SuspiciousNameCombination
    this.y = this.x;
    this.x = this.offsetY - this.height - tmp1;

    final int tmp2 = this.height;
    //noinspection SuspiciousNameCombination
    this.height = this.width;
    this.width = tmp2;

    final int tmp3 = this.offsetY;
    //noinspection SuspiciousNameCombination
    this.offsetY = this.offsetX;
    this.offsetX = tmp3;
  }

  public void drawAdd(int x, int y, final int alpha) {
    x += this.x;
    y += this.y;
    int rows = this.height;
    int cols = this.width;
    int srcPos = 0;
    int srcStride = 0;
    int destPos = Drawing.pixelIndex(x, y);
    int destStride = Drawing.width - cols;
    if (y < Drawing.top) {
      final int clippedRows = Drawing.top - y;
      y = Drawing.top;
      rows -= clippedRows;
      srcPos += clippedRows * this.width;
      destPos += clippedRows * Drawing.width;
    }
    if (rows > Drawing.bottom - y) {
      rows = Drawing.bottom - y;
    }

    if (x < Drawing.left) {
      final int clippedCols = Drawing.left - x;
      x = Drawing.left;
      cols -= clippedCols;
      srcPos += clippedCols;
      destPos += clippedCols;
      srcStride += clippedCols;
      destStride += clippedCols;
    }
    if (cols > Drawing.right - x) {
      final int clippedCols = cols - (Drawing.right - x);
      cols -= clippedCols;
      srcStride += clippedCols;
      destStride += clippedCols;
    }

    if (cols > 0 && rows > 0) {
      if (alpha == 256) {
        compositeAdd(this.pixels, srcStride, srcPos, Drawing.screenBuffer, destStride, destPos, cols, rows);
      } else {
        compositeAdd(this.pixels, srcStride, srcPos, Drawing.screenBuffer, destStride, destPos, cols, rows, alpha);
      }
    }
  }

  public final void draw(int x, int y) {
    x += this.x;
    y += this.y;
    int rows = this.height;
    int cols = this.width;
    int srcPos = 0;
    int srcStride = 0;
    int destPos = Drawing.pixelIndex(x, y);
    int destStride = Drawing.width - cols;

    if (y < Drawing.top) {
      final int clippedRows = Drawing.top - y;
      y = Drawing.top;
      rows -= clippedRows;
      srcPos += clippedRows * this.width;
      destPos += clippedRows * Drawing.width;
    }

    if (y + rows > Drawing.bottom) {
      rows -= y + rows - Drawing.bottom;
    }

    if (x < Drawing.left) {
      final int clippedCols = Drawing.left - x;
      x = Drawing.left;
      cols -= clippedCols;
      srcPos += clippedCols;
      destPos += clippedCols;
      srcStride += clippedCols;
      destStride += clippedCols;
    }

    if (x + cols > Drawing.right) {
      final int clippedCols = x + cols - Drawing.right;
      cols -= clippedCols;
      srcStride += clippedCols;
      destStride += clippedCols;
    }

    if (cols > 0 && rows > 0) {
      this.compositeOver(this.pixels, srcStride, srcPos, Drawing.screenBuffer, destStride, destPos, cols, rows);
    }
  }

  public final Sprite horizontallyFlipped() {
    final Sprite sprite = new Sprite(this.width, this.height);
    sprite.offsetX = this.offsetX;
    sprite.offsetY = this.offsetY;
    sprite.x = this.offsetX - this.width - this.x;
    sprite.y = this.y;

    for (int y = 0; y < this.height; ++y) {
      for (int x = 0; x < this.width; ++x) {
        sprite.pixels[(y * this.width) + x] = this.pixels[(y * this.width) + this.width - x - 1];
      }
    }

    return sprite;
  }

  public final void e326(int var1, int var2, final int var3) {
    var1 += this.x;
    var2 += this.y;
    int var4 = Drawing.pixelIndex(var1, var2);
    int var5 = 0;
    int var6 = this.height;
    int var7 = this.width;
    int var8 = Drawing.width - var7;
    int var9 = 0;
    int var10;
    if (var2 < Drawing.top) {
      var10 = Drawing.top - var2;
      var6 -= var10;
      var2 = Drawing.top;
      var5 += var10 * var7;
      var4 += var10 * Drawing.width;
    }

    if (var2 + var6 > Drawing.bottom) {
      var6 -= var2 + var6 - Drawing.bottom;
    }

    if (var1 < Drawing.left) {
      var10 = Drawing.left - var1;
      var7 -= var10;
      var1 = Drawing.left;
      var5 += var10;
      var4 += var10;
      var9 += var10;
      var8 += var10;
    }

    if (var1 + var7 > Drawing.right) {
      var10 = var1 + var7 - Drawing.right;
      var7 -= var10;
      var9 += var10;
      var8 += var10;
    }

    if (var7 > 0 && var6 > 0) {
      b650(Drawing.screenBuffer, this.pixels, var3, var5, var4, var7, var6, var8, var9);
    }
  }

  public void d093(int var1, int var2) {
    var1 += this.x >> 1;
    var2 += this.y >> 1;
    final int var3 = var1 < Drawing.left ? Drawing.left - var1 << 1 : 0;
    final int var4 = var1 + (this.width >> 1) > Drawing.right ? Drawing.right - var1 << 1 : this.width;
    final int var5 = var2 < Drawing.top ? Drawing.top - var2 << 1 : 0;
    final int var6 = var2 + (this.height >> 1) > Drawing.bottom ? Drawing.bottom - var2 << 1 : this.height;
    a600(this.pixels, var5 * this.width + var3, (var2 + (var5 >> 1)) * Drawing.width + var1 + (var3 >> 1), (this.width << 1) - (var4 - var3) + (this.width & 1), Drawing.width - (var4 - var3 >> 1), this.width, var4 - var3 >> 1, var6 - var5 >> 1);
  }

  public final void a115(final int var3) {
    int var1 = 40;
    int var2 = 40;
    final int var5 = this.offsetX << 3;
    final int var6 = this.offsetY << 3;
    var1 = (var1 << 4) + (var5 & 15);
    var2 = (var2 << 4) + (var6 & 15);
    this.b669(var5, var6, var1, var2, var3, 16384);
  }

  public void b115(int x, int y, int width, int height) {
    if (width > 0 && height > 0) {
      final int var5 = this.width;
      final int var6 = this.height;
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

      if (var5 < var9) {
        width = ((var5 << 16) - var7 + var11 - 1) / var11;
      }

      if (var6 < var10) {
        height = ((var6 << 16) - var8 + var12 - 1) / var12;
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

      b983(Drawing.screenBuffer, this.pixels, var7, var8, var13, var14, width, height, var11, var12, var5);
    }
  }

  public final void installForDrawing() {
    Drawing.initialize(this.pixels, this.width, this.height);
  }

  public final void withInstalledForDrawing(final Runnable action) {
    Drawing.withLocalContext(() -> {
      this.installForDrawing();
      action.run();
    });
  }

  public final void withInstalledForDrawingUsingOffsets(final Runnable action) {
    Drawing.withLocalContext(() -> {
      Drawing.initialize(this.pixels, this.offsetX, this.offsetY);
      action.run();
    });
  }

  public final void c115(final int var1, final int var2, final int var3, final int var4) {
    if (var3 <= this.offsetX && var4 <= this.offsetY) {
      int var5 = var1 + this.x * var3 / this.offsetX;
      int var6 = var1 + ((this.x + this.width) * var3 + this.offsetX - 1) / this.offsetX;
      int var7 = var2 + this.y * var4 / this.offsetY;
      int var8 = var2 + ((this.y + this.height) * var4 + this.offsetY - 1) / this.offsetY;
      if (var5 < Drawing.left) {
        var5 = Drawing.left;
      }

      if (var6 > Drawing.right) {
        var6 = Drawing.right;
      }

      if (var7 < Drawing.top) {
        var7 = Drawing.top;
      }

      if (var8 > Drawing.bottom) {
        var8 = Drawing.bottom;
      }

      if (var5 < var6 && var7 < var8) {
        int var9 = var7 * Drawing.width + var5;
        final int var10 = Drawing.width - (var6 - var5);

        for (int var11 = var7; var11 < var8; ++var11) {
          for (int var12 = var5; var12 < var6; ++var12) {
            final int var13 = var12 - var1 << 4;
            final int var14 = var11 - var2 << 4;
            int var15 = var13 * this.offsetX / var3 - (this.x << 4);
            int var16 = (var13 + 16) * this.offsetX / var3 - (this.x << 4);
            int var17 = var14 * this.offsetY / var4 - (this.y << 4);
            int var18 = (var14 + 16) * this.offsetY / var4 - (this.y << 4);
            final int var19 = (var16 - var15) * (var18 - var17);
            if (var19 != 0) {
              if (var15 < 0) {
                var15 = 0;
              }

              if (var16 > this.width << 4) {
                var16 = this.width << 4;
              }

              if (var17 < 0) {
                var17 = 0;
              }

              if (var18 > this.height << 4) {
                var18 = this.height << 4;
              }

              --var16;
              --var18;
              final int var20 = 16 - (var15 & 15);
              final int var21 = (var16 & 15) + 1;
              final int var22 = 16 - (var17 & 15);
              final int var23 = (var18 & 15) + 1;
              var15 >>= 4;
              var16 >>= 4;
              var17 >>= 4;
              var18 >>= 4;
              int var24 = 0;
              int var25 = 0;
              int var26 = 0;
              int var27 = 0;
              final int var28 = Drawing.screenBuffer[var9];

              int var29;
              for (var29 = var17; var29 <= var18; ++var29) {
                int var30 = 16;
                if (var29 == var17) {
                  var30 = var22;
                }

                if (var29 == var18) {
                  var30 = var23;
                }

                for (int var31 = var15; var31 <= var16; ++var31) {
                  int var32 = this.pixels[var29 * this.width + var31];
                  if (var32 == 0) {
                    var32 = var28;
                  }

                  final int var33;
                  if (var31 == var15) {
                    var33 = var30 * var20;
                  } else if (var31 == var16) {
                    var33 = var30 * var21;
                  } else {
                    var33 = var30 << 4;
                  }

                  var27 += var33;
                  var24 += (var32 >> 16 & 255) * var33;
                  var25 += (var32 >> 8 & 255) * var33;
                  var26 += (var32 & 255) * var33;
                }
              }

              if (var27 < var19) {
                var29 = var19 - var27;
                var24 += (var28 >> 16 & 255) * var29;
                var25 += (var28 >> 8 & 255) * var29;
                var26 += (var28 & 255) * var29;
              }

              var29 = (var24 / var19 << 16) + (var25 / var19 << 8) + var26 / var19;
              if (var29 == 0) {
                var29 = 1;
              }

              Drawing.screenBuffer[var9] = var29;
              ++var9;
            }
          }

          var9 += var10;
        }

      }
    } else {
      throw new IllegalArgumentException();
    }
  }

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

      a590(Drawing.screenBuffer, this.pixels, var8, var9, var14, var15, var3, var4, var12, var13, var6, var5);
    }
  }

  public final void b669(int var1, int var2, final int var3, final int var4, final int var5, final int var6) {
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
          final int var24 = Drawing.width + var20;
          final double var25 = 1.6777216E7D / (double) var6;
          final int var27 = (int) Math.floor(Math.sin(var7) * var25 + 0.5D);
          final int var28 = (int) Math.floor(Math.cos(var7) * var25 + 0.5D);
          final int var29 = (var19 << 4) + 8 - var3;
          final int var30 = (var21 << 4) + 8 - var4;
          int var31 = (var1 << 8) - 2048 - (var30 * var27 >> 4);
          int var32 = (var2 << 8) - 2048 + (var30 * var28 >> 4);
          int var33;
          int var34;
          int var35;
          int var36;
          int var37;
          int var38;
          int var39;
          if (var28 < 0) {
            if (var27 < 0) {
              for (var36 = var22; var36 < 0; var23 += var24) {
                {
                  var37 = var31 + (var29 * var28 >> 4);
                  var38 = var32 + (var29 * var27 >> 4);
                  var39 = var20;
                  if ((var35 = var37 - (this.width << 12)) >= 0) {

                    var35 = (var28 - var35) / var28;
                    var39 = var20 + var35;
                    var37 += var28 * var35;
                    var38 += var27 * var35;
                    var23 += var35;
                  }

                  if ((var35 = var38 - (this.height << 12)) >= 0) {

                    var35 = (var27 - var35) / var27;
                    var39 += var35;
                    var37 += var28 * var35;
                    var38 += var27 * var35;
                    var23 += var35;
                  }

                  while (var39 < 0 && var37 >= -4096 && var38 >= -4096) {
                    var33 = var37 >> 12;
                    var34 = var38 >> 12;
                    this.d050(var23, var33, var34, var37, var38);
                    ++var39;
                    var37 += var28;
                    var38 += var27;
                    ++var23;
                  }

                  var23 -= var39;
                }

                ++var36;
                var31 -= var27;
                var32 += var28;
              }
            } else {
              for (var36 = var22; var36 < 0; var23 += var24) {
                label252:
                {
                  var37 = var31 + (var29 * var28 >> 4);
                  var38 = var32 + (var29 * var27 >> 4);
                  var39 = var20;
                  if ((var35 = var37 - (this.width << 12)) >= 0) {

                    var35 = (var28 - var35) / var28;
                    var39 = var20 + var35;
                    var37 += var28 * var35;
                    var38 += var27 * var35;
                    var23 += var35;
                  }

                  if ((var35 = var38 + 4096) < 0) {
                    if (var27 == 0) {
                      var23 -= var39;
                      break label252;
                    }

                    var35 = (var27 - 1 - var35) / var27;
                    var39 += var35;
                    var37 += var28 * var35;
                    var38 += var27 * var35;
                    var23 += var35;
                  }

                  while (var39 < 0 && var37 >= -4096 && (var34 = var38 >> 12) < this.height) {
                    var33 = var37 >> 12;
                    this.d050(var23, var33, var34, var37, var38);
                    ++var39;
                    var37 += var28;
                    var38 += var27;
                    ++var23;
                  }

                  var23 -= var39;
                }

                ++var36;
                var31 -= var27;
                var32 += var28;
              }
            }
          } else if (var27 < 0) {
            for (var36 = var22; var36 < 0; var23 += var24) {
              label254:
              {
                var37 = var31 + (var29 * var28 >> 4);
                var38 = var32 + (var29 * var27 >> 4);
                var39 = var20;
                if ((var35 = var37 + 4096) < 0) {
                  if (var28 == 0) {
                    var23 -= var20;
                    break label254;
                  }

                  var35 = (var28 - 1 - var35) / var28;
                  var39 = var20 + var35;
                  var37 += var28 * var35;
                  var38 += var27 * var35;
                  var23 += var35;
                }

                if ((var35 = var38 - (this.height << 12)) >= 0) {

                  var35 = (var27 - var35) / var27;
                  var39 += var35;
                  var37 += var28 * var35;
                  var38 += var27 * var35;
                  var23 += var35;
                }

                while (var39 < 0 && var38 >= -4096 && (var33 = var37 >> 12) < this.width) {
                  var34 = var38 >> 12;
                  this.d050(var23, var33, var34, var37, var38);
                  ++var39;
                  var37 += var28;
                  var38 += var27;
                  ++var23;
                }

                var23 -= var39;
              }

              ++var36;
              var31 -= var27;
              var32 += var28;
            }
          } else {
            for (var36 = var22; var36 < 0; var23 += var24) {
              label256:
              {
                var37 = var31 + (var29 * var28 >> 4);
                var38 = var32 + (var29 * var27 >> 4);
                var39 = var20;
                if ((var35 = var37 + 4096) < 0) {
                  if (var28 == 0) {
                    var23 -= var20;
                    break label256;
                  }

                  var35 = (var28 - 1 - var35) / var28;
                  var39 = var20 + var35;
                  var37 += var28 * var35;
                  var38 += var27 * var35;
                  var23 += var35;
                }

                if ((var35 = var38 + 4096) < 0) {
                  if (var27 == 0) {
                    var23 -= var39;
                    break label256;
                  }

                  var35 = (var27 - 1 - var35) / var27;
                  var39 += var35;
                  var37 += var28 * var35;
                  var38 += var27 * var35;
                  var23 += var35;
                }

                while (var39 < 0 && (var33 = var37 >> 12) < this.width && (var34 = var38 >> 12) < this.height) {
                  this.d050(var23, var33, var34, var37, var38);
                  ++var39;
                  var37 += var28;
                  var38 += var27;
                  ++var23;
                }

                var23 -= var39;
              }

              ++var36;
              var31 -= var27;
              var32 += var28;
            }
          }

        }
      }
    }
  }

  public final void c050(int var1, int var2, int var3, int var4, final int var5) {
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

      if (var5 == 256) {
        a543(var8, this.pixels, Drawing.screenBuffer, -var4, var9, var14, var15, var3, var12, var13, var6);
      } else {
        a134(var8, this.pixels, Drawing.screenBuffer, -var4, var9, var14, var15, var3, var12, var13, var6, var5);
      }

    }
  }

  public void drawTinted2(int x, int y, final int color) {
    x += this.x;
    y += this.y;
    int rows = this.height;
    int cols = this.width;
    int srcPos = 0;
    int srcStride = 0;
    int destPos = Drawing.pixelIndex(x, y);
    int destStride = Drawing.width - cols;
    if (y < Drawing.top) {
      final int clippedRows = Drawing.top - y;
      y = Drawing.top;
      rows -= clippedRows;
      srcPos += clippedRows * this.width;
      destPos += clippedRows * Drawing.width;
    }
    if (rows > Drawing.bottom - y) {
      rows = Drawing.bottom - y;
    }

    if (x < Drawing.left) {
      final int clippedCols = Drawing.left - x;
      x = Drawing.left;
      cols -= clippedCols;
      srcPos += clippedCols;
      destPos += clippedCols;
      srcStride += clippedCols;
      destStride += clippedCols;
    }
    if (cols > Drawing.right - x) {
      final int clippedCols = cols - (Drawing.right - x);
      cols -= clippedCols;
      srcStride += clippedCols;
      destStride += clippedCols;
    }

    if (cols > 0 && rows > 0) {
      compositeOverTinted2(this.pixels, srcStride, srcPos, Drawing.screenBuffer, destStride, destPos, cols, rows, color);
    }
  }

  public Sprite copy() {
    final Sprite sprite = new Sprite(this.width, this.height);
    sprite.offsetX = this.offsetX;
    sprite.offsetY = this.offsetY;
    sprite.x = this.x;
    sprite.y = this.y;
    System.arraycopy(this.pixels, 0, sprite.pixels, 0, this.pixels.length);
    return sprite;
  }

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

    for (int var9 = var7; var9 <= var8; var9 += 4) {
      int var10 = var9 * this.width + var5;
      int var11 = (var2 + (var9 >> 2)) * Drawing.width + var1 + (var5 >> 2);

      for (int var12 = var5; var12 <= var6; ++var11) {
        int var14 = 0;
        int var15 = 0;

        for (int var16 = 0; var16 < 4; ++var16) {
          for (int var17 = 0; var17 < 4; ++var17) {
            int var18 = this.pixels[var10 + var16 * this.width + var17];
            if (var18 == 0) {
              var18 = Drawing.screenBuffer[var11];
            }

            var14 += var18 & 16711935;
            var15 += var18 & '\uff00';
          }
        }

        Drawing.screenBuffer[var11] = (var14 & 267390960 | var15 & 1044480) >> 4;
        var12 += 4;
        var10 += 4;
      }
    }
  }

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
      a415(Drawing.screenBuffer, this.pixels, var4, var3, var6, var5, var7, var8);
    }
  }

  public final void flipHorizontal() {
    for (int i = this.height - 1; i >= 0; --i) {
      int k = (i + 1) * this.width;
      for (int j = i * this.width; j < k; ++j) {
        --k;
        final int tmp = this.pixels[j];
        this.pixels[j] = this.pixels[k];
        this.pixels[k] = tmp;
      }
    }
    this.x = this.offsetX - this.width - this.x;
  }

  public final void f150(final int var1) {
    final int[] var2 = new int[this.width * this.height];
    int var3 = 0;

    for (int var4 = 0; var4 < this.height; ++var4) {
      for (int var5 = 0; var5 < this.width; ++var5) {
        int var6 = this.pixels[var3];
        if (var6 == 0) {
          if (var5 > 0 && this.pixels[var3 - 1] != 0) {
            var6 = var1;
          } else if (var4 > 0 && this.pixels[var3 - this.width] != 0) {
            var6 = var1;
          } else if (var5 < this.width - 1 && this.pixels[var3 + 1] != 0) {
            var6 = var1;
          } else if (var4 < this.height - 1 && this.pixels[var3 + this.width] != 0) {
            var6 = var1;
          }
        }

        var2[var3++] = var6;
      }
    }

    this.pixels = var2;
  }

  public final void a050(int var2, final int var3, int var4, final int var5) {
    final int var6 = this.offsetX << 3;
    final int var7 = this.offsetY << 3;
    var4 = (var4 << 4) + (var6 & 15);
    var2 = (var2 << 4) + (15 & var7);
    this.a669(var6, var7, var4, var2, var5, var3);
  }
}
