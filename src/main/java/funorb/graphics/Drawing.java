package funorb.graphics;

import funorb.util.ArrayUtil;
import funorb.util.MathUtil;

import java.util.Arrays;

public final class Drawing {
  public static final int[] SHADES_OF_GRAY = ArrayUtil.build(256, i -> i * 0x010101);
  public static final int BLACK = 0x000000;
  public static final int WHITE = 0xffffff;
  public static final int MAX_ALPHA = 256;
  public static final int RED = 0xff0000;
  public static final int GREEN = 0x00ff00;
  public static final int YELLOW = 0xffff00;
  public static int[] screenBuffer;
  public static int width;
  public static int height;
  public static int left = 0;
  public static int top = 0;
  public static int right = 0;
  public static int bottom = 0;

  private static final int SAVE_STACK_DEPTH = 4;
  private static int saveIndex = 0;
  private static final int[][] savedScreenBuffer = new int[SAVE_STACK_DEPTH][];
  private static final int[] savedWidth = new int[SAVE_STACK_DEPTH];
  private static final int[] savedHeight = new int[SAVE_STACK_DEPTH];
  private static final int[] savedLeft = new int[SAVE_STACK_DEPTH];
  private static final int[] savedTop = new int[SAVE_STACK_DEPTH];
  private static final int[] savedRight = new int[SAVE_STACK_DEPTH];
  private static final int[] savedBottom = new int[SAVE_STACK_DEPTH];

  private static final int[] _fev = new int[7];
  static {
    for (int var0 = 0; var0 < 3; ++var0) {
      _fev[var0] = 0x404040 * (1 + var0);
      _fev[6 - var0] = 0x404040 + var0 * 0x404040;
    }
    _fev[3] = WHITE;
  }

  private static int[] _e;
  private static int[] _g;
  private static int[] _j;

  public static void initialize(final int[] screenBuffer, final int width, final int height) {
    Drawing.screenBuffer = screenBuffer;
    Drawing.width = width;
    Drawing.height = height;
    setBounds(0, 0, width, height);
  }

  public static void setBounds(final int left, final int top, final int right, final int bottom) {
    Drawing.left = Math.max(left, 0);
    Drawing.top = Math.max(top, 0);
    Drawing.right = Math.min(right, width);
    Drawing.bottom = Math.min(bottom, height);
  }

  public static void expandBoundsToInclude(final int left, final int top, final int right, final int bottom) {
    if (Drawing.left < left) {
      Drawing.left = left;
    }
    if (Drawing.top < top) {
      Drawing.top = top;
    }
    if (Drawing.right > right) {
      Drawing.right = right;
    }
    if (Drawing.bottom > bottom) {
      Drawing.bottom = bottom;
    }
  }

  public static void saveContext() {
    savedScreenBuffer[saveIndex] = screenBuffer;
    savedLeft[saveIndex] = left;
    savedRight[saveIndex] = right;
    savedTop[saveIndex] = top;
    savedBottom[saveIndex] = bottom;
    savedWidth[saveIndex] = width;
    savedHeight[saveIndex] = height;
    ++saveIndex;
  }

  public static void restoreContext() {
    --saveIndex;
    initialize(savedScreenBuffer[saveIndex], savedWidth[saveIndex], savedHeight[saveIndex]);
    left = savedLeft[saveIndex];
    right = savedRight[saveIndex];
    top = savedTop[saveIndex];
    bottom = savedBottom[saveIndex];
  }

  public static void withLocalContext(final Runnable action) {
    final int[] screenBuffer = Drawing.screenBuffer;
    final int width = Drawing.width;
    final int height = Drawing.height;
    final int left = Drawing.left;
    final int top = Drawing.top;
    final int right = Drawing.right;
    final int bottom = Drawing.bottom;
    try {
      action.run();
    } finally {
      initialize(screenBuffer, width, height);
      setBounds(left, top, right, bottom);
    }
  }

  public static void e115(int var2, int var3) {
    int var0 = 0;
    int var1 = 0;
    if (var0 < left) {
      var2 -= left - var0;
      var0 = left;
    }

    if (var0 + var2 > right) {
      var2 = right - var0;
    }

    if (var1 < top) {
      var3 -= top - var1;
      var1 = top;
    }

    if (var1 + var3 > bottom) {
      var3 = bottom - var1;
    }

    int var4 = pixelIndex(var0, var1);
    if (var2 > 0 && var3 > 0) {
      for (int var5 = 0; var5 < var3; ++var5) {
        for (int var6 = 0; var6 < var2; ++var6) {
          final int var7 = screenBuffer[var4];
          final int var8 = (var7 >> 15) & 510;
          final int var9 = (var7 >> 8) & 0xff;
          final int var10 = var7 & 0xff;
          final int var11 = (((var10 + var8) / 3) + var9) >> 1;
          screenBuffer[var4++] = (var11 << 16) + (var11 << 8) + var11;
        }

        var4 += width - var2;
      }
    }
  }

  public static int pixelIndex(final int x, final int y) {
    return x + y * width;
  }

  public static void b669(final int var0, final int var1, final int var4, final int var5) {
    a600(screenBuffer, var0, var4, width - var4, var5);
    a621(screenBuffer, var1, var5, width - var4, var4);
  }

  @SuppressWarnings("SameParameterValue")
  public static void verticalLineBlended(final int x, int y, int height, final int color) {
    if (x >= left && x < right) {
      if (y < top) {
        height -= top - y;
        y = top;
      }

      if (y + height > bottom) {
        height = bottom - y;
      }

      int n = pixelIndex(x, y);
      for (int i = 0; i < height; ++i) {
        final int color1 = screenBuffer[n];
        final int sum = color + color1;
        final int rb = (color & 0xff00ff) + (color1 & 0xff00ff);
        final int overflow = (rb & 0x01000100) + ((sum - rb) & 0x010000);
        screenBuffer[n] = (sum - overflow) | (overflow - (overflow >>> 8));
        n += width;
      }
    }
  }

  public static void f669(final int var0, final int var1, final int var2, final int var3, int var4, final int var5) {
    if (var4 == 0) {
      strokeRectangle(var0, var1, var2, var3, var5);
    } else {
      if (var4 < 0) {
        var4 = -var4;
      }

      final int var6 = var0 + var4;
      final int var7 = var1 + var4;
      final int var8 = var0 + var2 - var4 - 1;
      final int var9 = var1 + var3 - var4 - 1;
      if (right > left && bottom > top) {
        if (var0 + var2 > left && var0 < right && var1 + var3 >= top && var1 < bottom) {
          int var10 = pixelIndex(var6, (var7 - var4));
          int var11 = pixelIndex(var8, (var7 - var4));
          int var12 = pixelIndex(var6, var7);
          int var13 = pixelIndex(var8, var7);
          int var14 = pixelIndex(var6, var9);
          int var15 = pixelIndex(var8, var9);
          int var16 = pixelIndex(var6, (var9 + var4));
          int var17 = pixelIndex(var8, (var9 + var4));
          int var18 = var4;
          int var19 = 0;
          final int var20 = var4 * var4;
          int var21 = var20 - var4;
          if (var0 >= left && var0 + var2 < right && var1 >= top && var1 + var3 < bottom) {
            int var22;
            for (var22 = var12; var22 <= var14; var22 += width) {
              screenBuffer[var22 - var18] = var5;
            }

            for (var22 = var13; var22 <= var15; var22 += width) {
              screenBuffer[var22 + var18] = var5;
            }

            for (var22 = var10; var22 <= var11; ++var22) {
              screenBuffer[var22] = var5;
            }

            for (var22 = var16; var22 <= var17; ++var22) {
              screenBuffer[var22] = var5;
            }

            while (true) {
              var21 += var19++ + var19;
              var12 -= width;
              var13 -= width;
              var14 += width;
              var15 += width;
              if (var21 > var20) {
                --var18;
                var21 -= var18 + var18;
                var10 += width;
                var11 += width;
                var16 -= width;
                var17 -= width;
              }

              if (var18 < var19) {
                break;
              }

              screenBuffer[var10 - var19] = var5;
              screenBuffer[var11 + var19] = var5;
              screenBuffer[var12 - var18] = var5;
              screenBuffer[var13 + var18] = var5;
              screenBuffer[var14 - var18] = var5;
              screenBuffer[var15 + var18] = var5;
              screenBuffer[var16 - var19] = var5;
              screenBuffer[var17 + var19] = var5;
            }
          } else {
            verticalLine(var0, var1 + var4, var3 - var4 - var4, var5);
            verticalLine(var0 + var2 - 1, var1 + var4, var3 - var4 - var4, var5);
            horizontalLine(var0 + var4, var1, var2 - var4 - var4, var5);
            horizontalLine(var0 + var4, var1 + var3 - 1, var2 - var4 - var4, var5);

            while (true) {
              var21 += var19++ + var19;
              var12 -= width;
              var13 -= width;
              var14 += width;
              var15 += width;
              if (var21 > var20) {
                --var18;
                var21 -= var18 + var18;
                var10 += width;
                var11 += width;
                var16 -= width;
                var17 -= width;
              }

              if (var18 < var19) {
                break;
              }

              if (var7 - var18 >= top && var7 - var18 < bottom) {
                if (var6 - var19 >= left && var6 - var19 < right) {
                  screenBuffer[var10 - var19] = var5;
                }

                if (var8 + var19 >= left && var8 + var19 < right) {
                  screenBuffer[var11 + var19] = var5;
                }
              }

              if (var7 - var19 >= top && var7 - var19 < bottom) {
                if (var6 - var18 >= left && var6 - var18 < right) {
                  screenBuffer[var12 - var18] = var5;
                }

                if (var8 + var18 >= left && var8 + var18 < right) {
                  screenBuffer[var13 + var18] = var5;
                }
              }

              if (var9 + var19 >= top && var9 + var19 < bottom) {
                if (var6 - var18 >= left && var6 - var18 < right) {
                  screenBuffer[var14 - var18] = var5;
                }

                if (var8 + var18 >= left && var8 + var18 < right) {
                  screenBuffer[var15 + var18] = var5;
                }
              }

              if (var9 + var18 >= top && var9 + var18 < bottom) {
                if (var6 - var19 >= left && var6 - var19 < right) {
                  screenBuffer[var16 - var19] = var5;
                }

                if (var8 + var19 >= left && var8 + var19 < right) {
                  screenBuffer[var17 + var19] = var5;
                }
              }
            }
          }

        }
      }
    }
  }

  public static void fillRoundedRect(final int x, final int y, final int width, final int height, final int radius, final int color) {
    if (radius == 0) {
      fillRect(x, y, width, height, color);
      return;
    }

    final int var4a = Math.abs(radius);

    final int var6 = x + var4a;
    int var7 = y + var4a;
    final int yStart = Math.max(y, top);
    final int yEnd = Math.min(y + height, bottom);

    final int var10 = width - var4a - var4a - 1;
    final int var12 = var4a * var4a;
    int var13 = 0;
    int var14 = var7 - yStart;
    int var15 = var14 * var14;
    int var16 = var15 - var14;
    if (var7 > yEnd) {
      var7 = yEnd;
    }

    int y1 = yStart;
    while (y1 < var7) {
      while (var16 <= var12 || var15 <= var12) {
        var15 += var13 + var13;
        var16 += var13++ + var13;
      }

      final int var17 = Math.max(var6 - var13 + 1, left);
      final int var18 = Math.min(var6 + var10 + var13, right);

      int var19 = pixelIndex(var17, y1);
      for (int i = var17; i < var18; ++i) {
        screenBuffer[var19++] = color;
      }

      ++y1;
      var15 -= var14-- + var14;
      var16 -= var14 + var14;
    }

    final int var17 = Math.max(x, left);
    final int var18 = Math.min(x + width, right);

    int var19 = pixelIndex(var17, y1);
    final int var20 = Drawing.width + var17 - var18;
    final int var21 = Math.min(y + height - var4a - 1, bottom);

    while (y1 < var21) {
      for (int var22 = var17; var22 < var18; ++var22) {
        screenBuffer[var19++] = color;
      }

      ++y1;
      var19 += var20;
    }

    int var41 = var4a;
    int var121 = var12;
    int i1 = var121 - var4a;

    int i = 0;
    for (; y1 < yEnd; y1++) {
      while (var121 > var12 && i1 > var12) {
        var121 -= var41-- + var41;
        i1 -= var41 + var41;
      }

      final int var17a = Math.max(var6 - var41, left);
      final int var18a = Math.min(var6 + var10 + var41, right - 1);
      int var19a = pixelIndex(var17a, y1);
      for (int var20a = var17a; var20a <= var18a; ++var20a) {
        screenBuffer[var19a++] = color;
      }

      var121 += i + i;
      i1 += i + i;
      i++;
    }
  }

  public static void drawCircleGradientAdd(final int x, final int y, final int radius, final int alpha, final int[] colors) {
    final int x1 = Math.max((x - radius) >> 4, left);
    final int x2 = Math.min((x + radius + 15) >> 4, right);
    final int y1 = Math.max((y - radius) >> 4, top);
    final int y2 = Math.min((y + radius + 15) >> 4, bottom);

    final int var10 = MathUtil.square((x1 << 4) - x);
    final int var11 = MathUtil.square((x1 + 1 << 4) - x);
    final int var12 = MathUtil.square((x1 + 2 << 4) - x);
    final int var13 = var11 - var10;
    final int var15 = (var12 - var11) - var13;
    final int var16 = MathUtil.square((y1 << 4) - y);
    final int var17 = MathUtil.square(((y1 + 1) << 4) - y);
    final int var18 = MathUtil.square(((y1 + 2) << 4) - y);
    final int var21 = (var18 - var17) - (var17 - var16);
    final int var23 = width + x1 - x2;

    int n = pixelIndex(x1, y1);
    int var141 = var16 + var10;
    int var151 = var17 - var16;
    final int radius2 = MathUtil.square(radius);
    int i = y1 - y2;
    while (i < 0) {
      int var31 = var141;
      int var41 = var13;

      for (int j = x1 - x2; j < 0; ++j) {
        if (var31 < radius2) {
          final int color1 = colors[(radius2 - var31) * alpha / radius2];
          final int color2 = screenBuffer[n];
          final int sum = color1 + color2;
          final int rb = (color1 & 0xff00ff) + (color2 & 0xff00ff);
          final int var25 = (rb & 0x01000100) + ((sum - rb) & 0x010000);
          screenBuffer[n] = (sum - var25) | (var25 - (var25 >>> 8));
        }

        ++n;
        var31 += var41;
        var41 += var15;
      }

      n += var23;
      var141 += var151;
      var151 += var21;
      ++i;
    }
  }

  public static void fillCircle(final int x, int y, int radius, final int color, final int alpha) {
    if (alpha == 0) return;
    if (alpha == 256) {
      fillCircle(x, y, radius, color);
      return;
    }

    if (radius < 0) {
      radius = -radius;
    }

    final int alpha2 = 256 - alpha;
    final int var6 = (color >> 16 & 255) * alpha;
    final int var7 = (color >> 8 & 255) * alpha;
    final int var8 = (color & 255) * alpha;
    int var12 = y - radius;
    if (var12 < top) {
      var12 = top;
    }

    int var13 = y + radius + 1;
    if (var13 > bottom) {
      var13 = bottom;
    }

    int var14 = var12;
    final int var15 = radius * radius;
    int var16 = 0;
    int var17 = y - var12;
    int var18 = var17 * var17;
    int var19 = var18 - var17;
    if (y > var13) {
      y = var13;
    }

    int var9;
    int var10;
    int var11;
    int var20;
    int var21;
    int var22;
    int var23;
    int var24;
    while (var14 < y) {
      while (var19 <= var15 || var18 <= var15) {
        var18 += var16 + var16;
        var19 += var16++ + var16;
      }

      var20 = x - var16 + 1;
      if (var20 < left) {
        var20 = left;
      }

      var21 = x + var16;
      if (var21 > right) {
        var21 = right;
      }

      var22 = pixelIndex(var20, var14);

      for (var23 = var20; var23 < var21; ++var23) {
        var9 = (screenBuffer[var22] >> 16 & 255) * alpha2;
        var10 = (screenBuffer[var22] >> 8 & 255) * alpha2;
        var11 = (screenBuffer[var22] & 255) * alpha2;
        var24 = (var6 + var9 >> 8 << 16) + (var7 + var10 >> 8 << 8) + (var8 + var11 >> 8);
        screenBuffer[var22++] = var24;
      }

      ++var14;
      var18 -= var17-- + var17;
      var19 -= var17 + var17;
    }

    int i = radius;
    var17 = -var17;
    int i1 = var17 * var17 + var15;
    int i2 = i1 - radius;

    for (i1 -= var17; var14 < var13; i2 += var17++ + var17) {
      while (i1 > var15 && i2 > var15) {
        i1 -= i-- + i;
        i2 -= i + i;
      }

      var20 = x - i;
      if (var20 < left) {
        var20 = left;
      }

      var21 = x + i;
      if (var21 > right - 1) {
        var21 = right - 1;
      }

      var22 = pixelIndex(var20, var14);

      for (var23 = var20; var23 <= var21; ++var23) {
        var9 = (screenBuffer[var22] >> 16 & 255) * alpha2;
        var10 = (screenBuffer[var22] >> 8 & 255) * alpha2;
        var11 = (screenBuffer[var22] & 255) * alpha2;
        var24 = (var6 + var9 >> 8 << 16) + (var7 + var10 >> 8 << 8) + (var8 + var11 >> 8);
        screenBuffer[var22++] = var24;
      }

      ++var14;
      i1 += var17 + var17;
    }

  }

  public static void fillRect(int x, int y, int width, int height, final int color) {
    if (x < left) {
      width -= left - x;
      x = left;
    }

    if (y < top) {
      height -= top - y;
      y = top;
    }

    if (x + width > right) {
      width = right - x;
    }

    if (y + height > bottom) {
      height = bottom - y;
    }

    final int stride = Drawing.width - width;
    int n = pixelIndex(x, y);
    for (int i = 0; i < height; ++i) {
      for (int j = 0; j < width; ++j) {
        screenBuffer[n++] = color;
      }
      n += stride;
    }
  }

  public static void h115(int x, int y, int width, int height) {
    if (x < left) {
      width -= left - x;
      x = left;
    }
    if (y < top) {
      height -= top - y;
      y = top;
    }
    if (x + width > right) {
      width = right - x;
    }
    if (y + height > bottom) {
      height = bottom - y;
    }

    final int stride = Drawing.width - width;
    int n = pixelIndex(x, y);
    for (int i = 0; i < height; ++i) {
      for (int j = 0; j < width; ++j) {
        screenBuffer[n] = (screenBuffer[n] & 0xfefefe) >> 1;
        ++n;
      }
      n += stride;
    }
  }

  private static void e669(int var0, int var1, int var2, int var3, final int var5) {
    int var6 = 0;
    final int var7 = 65536 / var3;
    if (var0 < left) {
      var2 -= left - var0;
      var0 = left;
    }

    if (var1 < top) {
      var6 += (top - var1) * var7;
      var3 -= top - var1;
      var1 = top;
    }

    if (var0 + var2 > right) {
      var2 = right - var0;
    }

    if (var1 + var3 > bottom) {
      var3 = bottom - var1;
    }

    final int var8 = width - var2;
    int var9 = pixelIndex(var0, var1);

    for (int var10 = -var3; var10 < 0; ++var10) {
      final int var12 = var6 >> 8;
      final int var13 = ((var5 & 16711935) * var12 & -16711936) + ((var5 & '\uff00') * var12 & 16711680) >>> 8;

      for (int var14 = -var2; var14 < 0; ++var14) {
        final int var15 = screenBuffer[var9];
        final int var16 = var13 + var15;
        final int var17 = (var13 & 16711935) + (var15 & 16711935);
        final int i = (var17 & 16777472) + (var16 - var17 & 65536);
        screenBuffer[var9++] = var16 - i | i - (i >>> 8);
      }

      var9 += var8;
      var6 += var7;
    }

  }

  public static void h669(int var0, int var1, int var2, int var3, final int var4, final int var5) {
    var2 -= var0;
    var3 -= var1;
    if (var3 == 0) {
      if (var2 >= 0) {
        horizontalLine(var0, var1, var2 + 1, var4, var5);
      } else {
        horizontalLine(var0 + var2, var1, -var2 + 1, var4, var5);
      }

    } else if (var2 == 0) {
      if (var3 >= 0) {
        verticalLine(var0, var1, var3 + 1, var4, var5);
      } else {
        verticalLine(var0, var1 + var3, -var3 + 1, var4, var5);
      }

    } else {
      if (var2 + var3 < 0) {
        var0 += var2;
        var2 = -var2;
        var1 += var3;
        var3 = -var3;
      }

      final int var6 = 256 - var5;
      final int var7 = (var4 >> 16 & 255) * var5;
      final int var8 = (var4 >> 8 & 255) * var5;
      final int var9 = (var4 & 255) * var5;
      final int var10;
      int var11;
      int var12;
      int var13;
      int var14;
      int var15;
      int var16;
      if (var2 > var3) {
        var1 <<= 16;
        var1 += 32768;
        var3 <<= 16;
        var10 = (int) Math.floor((double) var3 / (double) var2 + 0.5D);
        var2 += var0;
        if (var0 < left) {
          var1 += var10 * (left - var0);
          var0 = left;
        }

        if (var2 >= right) {
          var2 = right - 1;
        }

        while (var0 <= var2) {
          var11 = var1 >> 16;
          if (var11 >= top && var11 < bottom) {
            var12 = pixelIndex(var0, var11);
            var13 = (screenBuffer[var12] >> 16 & 255) * var6;
            var14 = (screenBuffer[var12] >> 8 & 255) * var6;
            var15 = (screenBuffer[var12] & 255) * var6;
            var16 = (var7 + var13 >> 8 << 16) + (var8 + var14 >> 8 << 8) + (var9 + var15 >> 8);
            screenBuffer[var12] = var16;
          }

          var1 += var10;
          ++var0;
        }
      } else {
        var0 <<= 16;
        var0 += 32768;
        var2 <<= 16;
        var10 = (int) Math.floor((double) var2 / (double) var3 + 0.5D);
        var3 += var1;
        if (var1 < top) {
          var0 += var10 * (top - var1);
          var1 = top;
        }

        if (var3 >= bottom) {
          var3 = bottom - 1;
        }

        while (var1 <= var3) {
          var11 = var0 >> 16;
          if (var11 >= left && var11 < right) {
            var12 = pixelIndex(var11, var1);
            var13 = (screenBuffer[var12] >> 16 & 255) * var6;
            var14 = (screenBuffer[var12] >> 8 & 255) * var6;
            var15 = (screenBuffer[var12] & 255) * var6;
            var16 = (var7 + var13 >> 8 << 16) + (var8 + var14 >> 8 << 8) + (var9 + var15 >> 8);
            screenBuffer[var12] = var16;
          }

          var0 += var10;
          ++var1;
        }
      }

    }
  }

  private static void a621(final int[] var0, final int var3, final int var5, final int var6, final int var8) {
    int var2 = 0;
    if (_e == null || _e.length < var8) {
      _e = new int[var8];
      _j = new int[var8];
      _g = new int[var8];
    }

    final int[] var9 = _e;
    final int[] var10 = _j;
    final int[] var11 = _g;
    Arrays.fill(var9, 0, var8, 0);
    Arrays.fill(var10, 0, var8, 0);
    Arrays.fill(var11, 0, var8, 0);
    final int var12 = 16384 / (2 * var3 + 1);
    int var13 = -var3;
    if (var13 < 0) {
      var13 = 0;
    }

    int var14 = var13 * width;
    int var15 = var3;
    int var16 = 0;
    if (var15 >= height) {
      var16 = var15 - height + 1;
      var15 = height - 1;
    }

    int var17;
    int var18;
    int var1;
    for (var17 = var15 - var13 + 1; var13 <= var15; ++var13) {
      for (var18 = 0; var18 < var8; ++var18) {
        var1 = var0[var14++];
        var9[var18] += var1 >> 16 & 255;
        var10[var18] += var1 >> 8 & 255;
        var11[var18] += var1 & 255;
      }

      var14 += var6;
    }

    var14 += var16 * width;

    for (var18 = 0; var18 < var8; ++var18) {
      var0[var2++] = (var9[var18] / var17 << 16) + (var10[var18] / var17 << 8) + var11[var18] / var17;
    }

    var2 += var6;
    int i = 1 - var5;
    var18 = 1 + var3 - var5;
    if (var18 > 0) {
      var18 = 0;
    }

    int var19 = (-var3) * width;
    if (i < var18) {
      var19 += (var18 - i) * width;
    }

    int var20;
    int var21;
    int var22;
    int var23;
    while (i < var18) {
      if (i + var5 + var3 >= bottom) {
        var14 += width;
      } else {
        for (var20 = 0; var20 < var8; ++var20) {
          var1 = var0[var14++];
          var9[var20] += var1 >> 16 & 255;
          var10[var20] += var1 >> 8 & 255;
          var11[var20] += var1 & 255;
        }

        var14 += var6;
        ++var17;
      }

      for (var20 = 0; var20 < var8; ++var20) {
        var21 = var9[var20] / var17;
        var22 = var10[var20] / var17;
        var23 = var11[var20] / var17;
        var0[var2++] = (var21 << 16) + (var22 << 8) + var23;
      }

      var2 += var6;
      ++i;
    }

    var18 = height - var5 - var3;
    if (var18 > 0) {
      var18 = 0;
    }

    while (i < var18) {
      for (var20 = 0; var20 < var8; ++var20) {
        var1 = var0[var19++];
        var21 = var9[var20] - (var1 >> 16 & 255);
        var9[var20] = Math.max(var21, 0);
        var21 = var10[var20] - (var1 >> 8 & 255);
        var10[var20] = Math.max(var21, 0);
        var21 = var11[var20] - (var1 & 255);
        var11[var20] = Math.max(var21, 0);
      }

      var19 += var6;

      for (var20 = 0; var20 < var8; ++var20) {
        var1 = var0[var14++];
        var9[var20] += var1 >> 16 & 255;
        var10[var20] += var1 >> 8 & 255;
        var11[var20] += var1 & 255;
      }

      var14 += var6;

      for (var20 = 0; var20 < var8; ++var20) {
        var21 = var9[var20] * var12 >> 14;
        var22 = var10[var20] * var12 >> 14;
        var23 = var11[var20] * var12 >> 14;
        if (var21 > 255) {
          var21 = 255;
        }

        if (var22 > 255) {
          var22 = 255;
        }

        if (var23 > 255) {
          var23 = 255;
        }

        var0[var2++] = (var21 << 16) + (var22 << 8) + var23;
      }

      var2 += var6;
      ++i;
    }

    while (i < 0) {
      for (var20 = 0; var20 < var8; ++var20) {
        var1 = var0[var19++];
        var9[var20] -= var1 >> 16 & 255;
        var10[var20] -= var1 >> 8 & 255;
        var11[var20] -= var1 & 255;
      }

      var19 += var6;
      --var17;

      for (var20 = 0; var20 < var8; ++var20) {
        var21 = var9[var20] / var17;
        var22 = var10[var20] / var17;
        var23 = var11[var20] / var17;
        if (var21 < 0) {
          var21 = 0;
        } else if (var21 > 255) {
          var21 = 255;
        }

        if (var22 < 0) {
          var22 = 0;
        } else if (var22 > 255) {
          var22 = 255;
        }

        if (var23 < 0) {
          var23 = 0;
        } else if (var23 > 255) {
          var23 = 255;
        }

        var0[var2++] = (var21 << 16) + (var22 << 8) + var23;
      }

      var2 += var6;
      ++i;
    }

  }

  public static void strokeCircle(final int x, final int y, int radius, final int color) {
    if (radius == 0) {
      setPixel(x, y, color);
    } else {
      if (radius < 0) {
        radius = -radius;
      }

      if (right > left && bottom > top) {
        if (x + radius >= left && x - radius < right && y + radius >= top && y - radius < bottom) {
          int var4 = pixelIndex(x, y);
          int var5 = var4;
          int var6 = var4 - radius * width;
          int var7 = pixelIndex(var4, radius);
          int var8 = radius;
          int var9 = 0;
          radius *= radius;
          int var10 = radius - var8;
          if (x - var8 >= left && x + var8 < right && y - var8 >= top && y + var8 < bottom) {
            screenBuffer[var4 - var8] = color;
            screenBuffer[var4 + var8] = color;
            screenBuffer[var6] = color;
            screenBuffer[var7] = color;

            while (true) {
              var10 += var9++ + var9;
              var4 -= width;
              var5 += width;
              if (var10 > radius) {
                --var8;
                var10 -= var8 + var8;
                var6 += width;
                var7 -= width;
              }

              if (var8 < var9) {
                break;
              }

              screenBuffer[var6 - var9] = color;
              screenBuffer[var6 + var9] = color;
              screenBuffer[var4 - var8] = color;
              screenBuffer[var4 + var8] = color;
              screenBuffer[var5 - var8] = color;
              screenBuffer[var5 + var8] = color;
              screenBuffer[var7 - var9] = color;
              screenBuffer[var7 + var9] = color;
            }
          } else {
            if (x - var8 >= left && y >= top && y < bottom) {
              screenBuffer[var4 - var8] = color;
            }

            if (x + var8 < right && y >= top && y < bottom) {
              screenBuffer[var4 + var8] = color;
            }

            if (y - var8 >= top && x >= left && x < right) {
              screenBuffer[var6] = color;
            }

            if (y + var8 < bottom && x >= left && x < right) {
              screenBuffer[var7] = color;
            }

            while (true) {
              var10 += var9++ + var9;
              var4 -= width;
              var5 += width;
              if (var10 > radius) {
                --var8;
                var10 -= var8 + var8;
                var6 += width;
                var7 -= width;
              }

              if (var8 < var9) {
                break;
              }

              if (y - var8 >= top && y - var8 < bottom) {
                if (x - var9 >= left && x - var9 < right) {
                  screenBuffer[var6 - var9] = color;
                }

                if (x + var9 >= left && x + var9 < right) {
                  screenBuffer[var6 + var9] = color;
                }
              }

              if (y - var9 >= top && y - var9 < bottom) {
                if (x - var8 >= left && x - var8 < right) {
                  screenBuffer[var4 - var8] = color;
                }

                if (x + var8 >= left && x + var8 < right) {
                  screenBuffer[var4 + var8] = color;
                }
              }

              if (y + var9 >= top && y + var9 < bottom) {
                if (x - var8 >= left && x - var8 < right) {
                  screenBuffer[var5 - var8] = color;
                }

                if (x + var8 >= left && x + var8 < right) {
                  screenBuffer[var5 + var8] = color;
                }
              }

              if (y + var8 >= top && y + var8 < bottom) {
                if (x - var9 >= left && x - var9 < right) {
                  screenBuffer[var7 - var9] = color;
                }

                if (x + var9 >= left && x + var9 < right) {
                  screenBuffer[var7 + var9] = color;
                }
              }
            }
          }
        }
      }
    }
  }

  public static void verticalLine(final int x, int y, int height, final int color, final int alpha) {
    if (x >= left && x < right) {
      if (y < top) {
        height -= top - y;
        y = top;
      }

      if (y + height > bottom) {
        height = bottom - y;
      }

      final int var5 = 256 - alpha;
      final int var6 = (color >> 16 & 255) * alpha;
      final int var7 = (color >> 8 & 255) * alpha;
      final int var8 = (color & 255) * alpha;
      int var12 = pixelIndex(x, y);

      for (int var13 = 0; var13 < height; ++var13) {
        final int var9 = (screenBuffer[var12] >> 16 & 255) * var5;
        final int var10 = (screenBuffer[var12] >> 8 & 255) * var5;
        final int var11 = (screenBuffer[var12] & 255) * var5;
        final int var14 = (var6 + var9 >> 8 << 16) + (var7 + var10 >> 8 << 8) + (var8 + var11 >> 8);
        screenBuffer[var12] = var14;
        var12 += width;
      }

    }
  }

  public static void strokeRectangle(final int x, final int y, final int width, final int height, final int color) {
    horizontalLine(x, y, width, color);
    horizontalLine(x, y + height - 1, width, color);
    verticalLine(x, y, height, color);
    verticalLine(x + width - 1, y, height, color);
  }

  public static void verticalLine(final int x, int y, int height, final int color) {
    if (x >= left && x < right) {
      if (y < top) {
        height -= top - y;
        y = top;
      }

      if (y + height > bottom) {
        height = bottom - y;
      }

      int var4 = pixelIndex(x, y);

      for (int var5 = 0; var5 < height; var4 += width) {
        screenBuffer[var4] = color;
        ++var5;
      }

    }
  }

  public static void b370(final int var0, int var1, final int var2, int var3, int var4, final int var6) {
    if (var4 == 0) {
      e669(var0, var1, var2, var3, var6);
    } else {
      if (var4 < 0) {
        var4 = -var4;
      }

      int var7 = 0;
      final int var8 = 65536 / var3;
      final int var9 = var0 + var4;
      int var10 = var1 + var4;
      int var11 = var1;
      if (var1 < top) {
        var7 += (top - var1) * var8;
        var3 -= top - var1;
        var1 = var11 = top;
      }

      int var12 = var1 + var3;
      if (var12 > bottom) {
        var12 = bottom;
      }

      final int var13 = var2 - var4 - var4 - 1;
      int var14 = var11;
      final int var15 = var4 * var4;
      int var16 = 0;
      int var17 = var10 - var11;
      int var18 = var17 * var17;
      int var19 = var18 - var17;
      if (var10 > var12) {
        var10 = var12;
      }

      int var20;
      int var21;
      int var22;
      final int var23;
      int var24;
      int var25;
      int var26;
      while (var14 < var10) {
        while (var19 <= var15 || var18 <= var15) {
          var18 += var16 + var16;
          var19 += var16++ + var16;
        }

        var20 = var9 - var16 + 1;
        if (var20 < left) {
          var20 = left;
        }

        var21 = var9 + var13 + var16;
        if (var21 > right) {
          var21 = right;
        }

        var22 = pixelIndex(var20, var14);
        var24 = var7 >> 8;
        var25 = ((var6 & 16711935) * var24 & -16711936) + ((var6 & '\uff00') * var24 & 16711680) >>> 8;

        for (var26 = var20; var26 < var21; ++var26) {
          screenBuffer[var22++] = var25;
        }

        ++var14;
        var18 -= var17-- + var17;
        var19 -= var17 + var17;
        var7 += var8;
      }

      var20 = Math.max(var0, left);

      var21 = var0 + var2;
      if (var21 > right) {
        var21 = right;
      }

      var22 = pixelIndex(var20, var14);
      var23 = width + var20 - var21;
      var24 = var1 + var3 - var4 - 1;
      if (var24 > bottom) {
        var24 = bottom;
      }

      while (var14 < var24) {
        var26 = var7 >> 8;
        final int var27 = ((var6 & 16711935) * var26 & -16711936) + ((var6 & '\uff00') * var26 & 16711680) >>> 8;

        for (int var28 = var20; var28 < var21; ++var28) {
          screenBuffer[var22++] = var27;
        }

        ++var14;
        var22 += var23;
        var7 += var8;
      }

      int i = 0;
      int var41 = var4;
      int var151 = var15;
      int i1 = var151 - var4;

      for (var151 -= i; var14 < var12; var7 += var8) {
        while (var151 > var15 && i1 > var15) {
          var151 -= var41-- + var41;
          i1 -= var41 + var41;
        }

        var20 = var9 - var41;
        if (var20 < left) {
          var20 = left;
        }

        var21 = var9 + var13 + var41;
        if (var21 > right - 1) {
          var21 = right - 1;
        }

        var22 = pixelIndex(var20, var14);
        var24 = var7 >> 8;
        var25 = ((var6 & 16711935) * var24 & -16711936) + ((var6 & '\uff00') * var24 & 16711680) >>> 8;

        for (var26 = var20; var26 <= var21; ++var26) {
          screenBuffer[var22++] = var25;
        }

        ++var14;
        var151 += i + i;
        i1 += i++ + i;
      }

    }
  }

  public static void a797() {
    left = 0;
    top = 0;
    right = width;
    bottom = height;
  }

  public static void horizontalLine(int x, final int y, int width, final int color, final int alpha) {
    if (y >= top && y < bottom) {
      if (x < left) {
        width -= left - x;
        x = left;
      }
      if (x + width > right) {
        width = right - x;
      }

      final int alpha2 = 256 - alpha;
      final int r1 = ((color >> 16) & 0xff) * alpha;
      final int g1 = ((color >> 8) & 0xff) * alpha;
      final int b1 = (color & 0xff) * alpha;

      int n = pixelIndex(x, y);
      for (int i = 0; i < width; ++i) {
        final int r2 = ((screenBuffer[n] >> 16) & 255) * alpha2;
        final int g2 = ((screenBuffer[n] >> 8) & 255) * alpha2;
        final int b2 = (screenBuffer[n] & 255) * alpha2;
        screenBuffer[n++] = (((r1 + r2) >> 8) << 16)
            + (((g1 + g2) >> 8) << 8)
            + ((b1 + b2) >> 8);
      }
    }
  }

  public static void fillRect(int x, int y, int width, int height, int color, final int alpha) {
    if (x < left) {
      width -= left - x;
      x = left;
    }
    if (y < top) {
      height -= top - y;
      y = top;
    }
    if (x + width > right) {
      width = right - x;
    }
    if (y + height > bottom) {
      height = bottom - y;
    }

    color = ((((color & 0xff00ff) * alpha) >> 8) & 0xff00ff) + ((((color & 0x00ff00) * alpha) >> 8) & 0x00ff00);
    final int occlusion = 256 - alpha;
    final int stride = Drawing.width - width;

    int n = pixelIndex(x, y);
    for (int i = 0; i < height; ++i) {
      for (int j = -width; j < 0; ++j) {
        final int px = screenBuffer[n];
        final int px2 = ((px & 0xff00ff) * occlusion >> 8 & 0xff00ff) + ((px & 0x00ff00) * occlusion >> 8 & 0x00ff00);
        screenBuffer[n++] = color + px2;
      }
      n += stride;
    }
  }

  public static void restoreBoundsFrom(final int[] bounds) {
    left = bounds[0];
    top = bounds[1];
    right = bounds[2];
    bottom = bounds[3];
  }

  public static void saveBoundsTo(final int[] bounds) {
    bounds[0] = left;
    bounds[1] = top;
    bounds[2] = right;
    bounds[3] = bottom;
  }

  public static void withSavedBounds(final Runnable action) {
    final int left = Drawing.left;
    final int top = Drawing.top;
    final int right = Drawing.right;
    final int bottom = Drawing.bottom;
    try {
      action.run();
    } finally {
      setBounds(left, top, right, bottom);
    }
  }

  @SuppressWarnings("SameParameterValue")
  public static void withBounds(final int left, final int top, final int right, final int bottom, final Runnable action) {
    withSavedBounds(() -> {
      setBounds(left, top, right, bottom);
      action.run();
    });
  }

  public static void fillRoundedRect(final int x, final int y, final int width, final int height, int radius, int color, final int alpha) {
    if (alpha == 256) {
      fillRoundedRect(x, y, width, height, radius, color);
    } else if (radius == 0) {
      fillRect(x, y, width, height, color, alpha);
    } else {
      final int var7 = 256 - alpha;
      color = ((color & 16711935) * alpha >> 8 & 16711935) + ((color & '\uff00') * alpha >> 8 & '\uff00');
      if (radius < 0) {
        radius = -radius;
      }

      final int var8 = x + radius;
      int var9 = y + radius;
      final int var10 = Math.max(y, top);

      int var11 = y + height;
      if (var11 > bottom) {
        var11 = bottom;
      }

      final int var12 = width - radius - radius - 1;
      int var13 = var10;
      final int var14 = radius * radius;
      int var15 = 0;
      int var16 = var9 - var10;
      int var17 = var16 * var16;
      int var18 = var17 - var16;
      if (var9 > var11) {
        var9 = var11;
      }

      int var19;
      int var20;
      int var21;
      int var22;
      int var23;
      while (var13 < var9) {
        while (var18 <= var14 || var17 <= var14) {
          var17 += var15 + var15;
          var18 += var15++ + var15;
        }

        var19 = var8 - var15 + 1;
        if (var19 < left) {
          var19 = left;
        }

        var20 = var8 + var12 + var15;
        if (var20 > right) {
          var20 = right;
        }

        var21 = pixelIndex(var19, var13);

        for (var22 = var19; var22 < var20; ++var22) {
          var23 = screenBuffer[var21];
          var23 = ((var23 & 16711935) * var7 >> 8 & 16711935) + ((var23 & '\uff00') * var7 >> 8 & '\uff00');
          screenBuffer[var21++] = color + var23;
        }

        ++var13;
        var17 -= var16-- + var16;
        var18 -= var16 + var16;
      }

      var19 = Math.max(x, left);

      var20 = x + width;
      if (var20 > right) {
        var20 = right;
      }

      var21 = pixelIndex(var19, var13);
      var22 = Drawing.width + var19 - var20;
      var23 = y + height - radius - 1;
      if (var23 > bottom) {
        var23 = bottom;
      }

      while (var13 < var23) {
        for (int var24 = var19; var24 < var20; ++var24) {
          int var25 = screenBuffer[var21];
          var25 = ((var25 & 16711935) * var7 >> 8 & 16711935) + ((var25 & '\uff00') * var7 >> 8 & '\uff00');
          screenBuffer[var21++] = color + var25;
        }

        ++var13;
        var21 += var22;
      }

      int i = 0;
      int var41 = radius;
      int var141 = var14;
      int i1 = var141 - radius;

      for (var141 -= i; var13 < var11; i1 += i++ + i) {
        while (var141 > var14 && i1 > var14) {
          var141 -= var41-- + var41;
          i1 -= var41 + var41;
        }

        var19 = var8 - var41;
        if (var19 < left) {
          var19 = left;
        }

        var20 = var8 + var12 + var41;
        if (var20 > right - 1) {
          var20 = right - 1;
        }

        var21 = pixelIndex(var19, var13);

        for (var22 = var19; var22 <= var20; ++var22) {
          var23 = screenBuffer[var21];
          var23 = ((var23 & 16711935) * var7 >> 8 & 16711935) + ((var23 & '\uff00') * var7 >> 8 & '\uff00');
          screenBuffer[var21++] = color + var23;
        }

        ++var13;
        var141 += i + i;
      }

    }
  }

  private static boolean a398(final int var5, final int var6, int var7, int var8, final int[] var9, final int var12, final int var13, final int var14) {
    int var3 = var14;

    int var4;
    for (var4 = var13; var8 < 0; ++var8) {
      final int var1 = (var6 - var3) * 12 / var6;
      if (var1 >= _fev.length) {
        return true;
      }

      int var0 = _fev[var1];
      final int i = var9[var7];
      final int var2 = var0 + i;
      var0 = (var0 & 16711935) + (i & 16711935);
      final int i1 = (var0 & 16777472) + (var2 - var0 & 65536);
      var9[var7] = var2 - i1 | i1 - (i1 >>> 8);
      var7 += var12;
      var3 += var4;
      var4 += var5;
    }

    return false;
  }

  public static void d669(final int var0, final int var1, final int var2, final int var3, final int var4, final int var5) {
    horizontalLine(var0, var1, var2, var4, var5);
    horizontalLine(var0, var1 + var3 - 1, var2, var4, var5);
    if (var3 >= 3) {
      verticalLine(var0, var1 + 1, var3 - 2, var4, var5);
      verticalLine(var0 + var2 - 1, var1 + 1, var3 - 2, var4, var5);
    }

  }

  public static void setPixel(final int x, final int y, final int color) {
    if (x >= left && x < right && y >= top && y < bottom) {
      screenBuffer[pixelIndex(x, y)] = color;
    }
  }

  public static void line(int x1, int y1, final int x2, final int y2, final int color, final int alpha) {
    int width = x2 - x1;
    int height = y2 - y1;
    if (height == 0) {
      if (width >= 0) {
        horizontalLine(x1, y1, width, color, alpha);
      } else {
        horizontalLine(x1 + width + 1, y1, -width, color, alpha);
      }
    } else if (width == 0) {
      if (height >= 0) {
        verticalLine(x1, y1, height, color, alpha);
      } else {
        verticalLine(x1, y1 + height + 1, -height, color, alpha);
      }
    } else {
      boolean var6 = false;
      if (width + height < 0) {
        x1 += width;
        width = -width;
        y1 += height;
        height = -height;
        var6 = true;
      }

      final int var7 = 256 - alpha;
      final int var8 = (color >> 16 & 255) * alpha;
      final int var9 = (color >> 8 & 255) * alpha;
      final int var10 = (color & 255) * alpha;
      final int var11;
      int var12;
      int var13;
      int var14;
      int var15;
      int var16;
      int var17;
      if (width > height) {
        y1 <<= 16;
        y1 += 32768;
        height <<= 16;
        var11 = (int) Math.floor((double) height / (double) width + 0.5D);
        width += x1;
        if (var6) {
          y1 += var11;
          ++x1;
        }

        if (x1 < left) {
          y1 += var11 * (left - x1);
          x1 = left;
        }

        if (width >= right) {
          width = right - 1;
        }

        if (!var6) {
          --width;
        }

        while (x1 <= width) {
          var12 = y1 >> 16;
          if (var12 >= top && var12 < bottom) {
            var13 = pixelIndex(x1, var12);
            var14 = (screenBuffer[var13] >> 16 & 255) * var7;
            var15 = (screenBuffer[var13] >> 8 & 255) * var7;
            var16 = (screenBuffer[var13] & 255) * var7;
            var17 = (var8 + var14 >> 8 << 16) + (var9 + var15 >> 8 << 8) + (var10 + var16 >> 8);
            screenBuffer[var13] = var17;
          }

          y1 += var11;
          ++x1;
        }
      } else {
        x1 <<= 16;
        x1 += 32768;
        width <<= 16;
        var11 = (int) Math.floor((double) width / (double) height + 0.5D);
        height += y1;
        if (var6) {
          x1 += var11;
          ++y1;
        }

        if (y1 < top) {
          x1 += var11 * (top - y1);
          y1 = top;
        }

        if (height >= bottom) {
          height = bottom - 1;
        }

        if (!var6) {
          --height;
        }

        while (y1 <= height) {
          var12 = x1 >> 16;
          if (var12 >= left && var12 < right) {
            var13 = pixelIndex(var12, y1);
            var14 = (screenBuffer[var13] >> 16 & 255) * var7;
            var15 = (screenBuffer[var13] >> 8 & 255) * var7;
            var16 = (screenBuffer[var13] & 255) * var7;
            var17 = (var8 + var14 >> 8 << 16) + (var9 + var15 >> 8 << 8) + (var10 + var16 >> 8);
            screenBuffer[var13] = var17;
          }

          x1 += var11;
          ++y1;
        }
      }

    }
  }

  public static void fillCircle(final int x, int y, final int radius, final int color) {
    if (radius == 0) {
      setPixel(x, y, color);
      return;
    }

    final int radiusPos = Math.abs(radius);
    final int radiusSq = MathUtil.square(radiusPos);

    final int y1 = Math.max(y - radiusPos, top);
    final int y2 = Math.min(y + radiusPos + 1, bottom);

    int var9 = y - y1;
    int var10 = var9 * var9;
    int var11 = var10 - var9;
    if (y > y2) {
      y = y2;
    }

    int yi = y1;
    for (int ri = 0; yi < y; ++yi) {
      while (var11 <= radiusSq || var10 <= radiusSq) {
        var10 += ri + ri;
        var11 += ri + ri;
        ri++;
      }

      final int x1 = Math.max(x - ri + 1, left);
      final int x2 = Math.min(x + ri, right);
      int n = pixelIndex(x1, yi);
      for (int i = x1; i < x2; ++i) {
        screenBuffer[n++] = color;
      }

      var10 -= var9 + var9;
      var9--;
      var11 -= var9 + var9;
    }

    int i0 = radiusPos;
    int i1 = yi - y;
    int i2 = MathUtil.square(i1) + radiusSq;
    int i3 = i2 - radiusPos;
    i2 -= i1;

    for (; yi < y2; ++yi) {
      while (i2 > radiusSq && i3 > radiusSq) {
        i2 -= i0 + i0;
        i0--;
        i3 -= i0 + i0;
      }

      final int x1 = Math.max(x - i0, left);
      final int x2 = Math.min(x + i0, right - 1);
      int n = pixelIndex(x1, yi);
      for (int i = x1; i <= x2; ++i) {
        screenBuffer[n++] = color;
      }

      i2 += i1 + i1;
      i3 += i1 + i1;
      i1++;
    }
  }

  private static void a600(final int[] var0, final int var3, final int var5, final int var6, final int var7) {
    int var2 = 0;
    final int var8 = 16384 / (2 * var3 + 1);
    int var9 = 1 + var3 - var5;
    if (var9 > 0) {
      var9 = 0;
    }

    int var10 = width - var5 - var3;
    if (var10 > 0) {
      var10 = 0;
    }

    int var11 = 0;
    int var12 = var3 + 1;
    if (width < var12) {
      var11 = var12 - width;
      var12 = width;
    }

    for (int var13 = -var7; var13 < 0; ++var13) {
      int var14 = 0;
      int var15 = 0;
      int var16 = 0;
      int var17 = var2 - var3;
      int var18 = var17 - (var3 << 1) - 1;
      int var19 = -var3;
      if (var19 < 0) {
        var17 -= var19;
        var18 -= var19;
        var19 = 0;
      }

      int var20;
      int var1;
      for (var20 = var12 - var19; var19 < var12; ++var19) {
        var1 = var0[var17];
        var14 += var1 >> 16 & 255;
        var15 += var1 >> 8 & 255;
        var16 += var1 & 255;
        ++var17;
        ++var18;
      }

      var18 += var11;
      var0[var2++] = (var14 / var20 << 16) + (var15 / var20 << 8) + var16 / var20;

      int var21;
      int var22;
      int var23;
      for (var19 = 1 - var5; var19 < var9; ++var19) {
        ++var18;
        if (var5 + var19 + var3 < right) {
          var1 = var0[var17];
          ++var17;
          var14 += var1 >> 16 & 255;
          var15 += var1 >> 8 & 255;
          var16 += var1 & 255;
          ++var20;
        }

        var21 = var14 / var20;
        var22 = var15 / var20;
        var23 = var16 / var20;
        var0[var2++] = (var21 << 16) + (var22 << 8) + var23;
      }

      while (var19 < var10) {
        var1 = var0[var18++];
        var14 -= var1 >> 16 & 255;
        if (var14 < 0) {
          var14 = 0;
        }

        var15 -= var1 >> 8 & 255;
        if (var15 < 0) {
          var15 = 0;
        }

        var16 -= var1 & 255;
        if (var16 < 0) {
          var16 = 0;
        }

        var1 = var0[var17];
        ++var17;
        var14 += var1 >> 16 & 255;
        var15 += var1 >> 8 & 255;
        var16 += var1 & 255;
        var21 = var14 * var8 >> 14;
        var22 = var15 * var8 >> 14;
        var23 = var16 * var8 >> 14;
        if (var21 > 255) {
          var21 = 255;
        }

        if (var22 > 255) {
          var22 = 255;
        }

        if (var23 > 255) {
          var23 = 255;
        }

        var0[var2++] = (var21 << 16) + (var22 << 8) + var23;
        ++var19;
      }

      while (var19 < 0) {
        var1 = var0[var18++];
        var14 -= var1 >> 16 & 255;
        var15 -= var1 >> 8 & 255;
        var16 -= var1 & 255;
        --var20;
        var21 = var14 / var20;
        var22 = var15 / var20;
        var23 = var16 / var20;
        if (var21 < 0) {
          var21 = 0;
        } else if (var21 > 255) {
          var21 = 255;
        }

        if (var22 < 0) {
          var22 = 0;
        } else if (var22 > 255) {
          var22 = 255;
        }

        if (var23 < 0) {
          var23 = 0;
        } else if (var23 > 255) {
          var23 = 255;
        }

        var0[var2++] = (var21 << 16) + (var22 << 8) + var23;
        ++var19;
      }

      var2 += var6;
    }

  }

  public static void addPixel(final int x, final int y, final int color, final int alpha) {
    if (x >= left && y >= top && x < right && y < bottom) {
      final int i = pixelIndex(x, y);
      screenBuffer[i] = saturatingAdd(color, screenBuffer[i], alpha);
    }
  }

  public static void fillRectangleVerticalGradient(int x, int y, int width, int height, final int color1, final int color2) {
    if (x < left) {
      width -= left - x;
      x = left;
    }

    int var6 = 0;
    final int var7 = 0x10000 / height;
    if (y < top) {
      var6 += (top - y) * var7;
      height -= top - y;
      y = top;
    }

    if (x + width > right) {
      width = right - x;
    }

    if (y + height > bottom) {
      height = bottom - y;
    }

    final int var8 = Drawing.width - width;
    int pos = pixelIndex(x, y);

    for (int i = 0; i < height; ++i) {
      final int var11 = (0x10000 - var6) >> 8;
      final int var12 = var6 >> 8;
      final int var13 = ((color1 & 0xff00ff) * var11 + (color2 & 0xff00ff) * var12 & 0xff00ff00) + ((color1 & 0x00ff00) * var11 + (color2 & 0x00ff00) * var12 & 0xff0000) >>> 8;

      for (int j = 0; j < width; ++j) {
        screenBuffer[pos++] = var13;
      }

      pos += var8;
      var6 += var7;
    }
  }

  public static void clear() {
    Arrays.fill(screenBuffer, 0, width * height, 0);
  }

  public static void line(int x1, int y1, final int x2, final int y2, final int color) {
    int dx = x2 - x1;
    int dy = y2 - y1;
    if (dy == 0) {
      if (dx >= 0) {
        horizontalLine(x1, y1, dx + 1, color);
      } else {
        horizontalLine(x1 + dx, y1, -dx + 1, color);
      }

    } else if (dx == 0) {
      if (dy >= 0) {
        verticalLine(x1, y1, dy + 1, color);
      } else {
        verticalLine(x1, y1 + dy, -dy + 1, color);
      }

    } else {
      if (dx + dy < 0) {
        x1 += dx;
        dx = -dx;
        y1 += dy;
        dy = -dy;
      }

      final int var5;
      int var6;
      if (dx > dy) {
        y1 <<= 16;
        y1 += 32768;
        dy <<= 16;
        var5 = (int) Math.floor((double) dy / (double) dx + 0.5D);
        dx += x1;
        if (x1 < left) {
          y1 += var5 * (left - x1);
          x1 = left;
        }

        if (dx >= right) {
          dx = right - 1;
        }

        while (x1 <= dx) {
          var6 = y1 >> 16;
          if (var6 >= top && var6 < bottom) {
            screenBuffer[pixelIndex(x1, var6)] = color;
          }

          y1 += var5;
          ++x1;
        }
      } else {
        x1 <<= 16;
        x1 += 32768;
        dx <<= 16;
        var5 = (int) Math.floor((double) dx / (double) dy + 0.5D);
        dy += y1;
        if (y1 < top) {
          x1 += var5 * (top - y1);
          y1 = top;
        }

        if (dy >= bottom) {
          dy = bottom - 1;
        }

        while (y1 <= dy) {
          var6 = x1 >> 16;
          if (var6 >= left && var6 < right) {
            screenBuffer[pixelIndex(var6, y1)] = color;
          }

          x1 += var5;
          ++y1;
        }
      }

    }
  }

  public static void a907(final int var0, final int var1, final int var2) {
    final int var5 = var2 * var2;
    int var6 = var1 - var2 >> 4;
    int var7 = var1 + var2 + 15 >> 4;
    if (var6 < top) {
      var6 = top;
    }

    if (var7 > bottom) {
      var7 = bottom;
    }

    int var8 = (var6 << 4) - var1;
    var8 *= var8;
    int var9 = (var6 + 1 << 4) - var1;
    var9 *= var9;
    int var10 = (var6 + 2 << 4) - var1;
    var10 *= var10;
    int var11 = var9 - var8;
    final int var12 = var10 - var9;
    final int var13 = var12 - var11;
    int var14 = var6 * width;
    final int var15 = width;

    for (int var16 = var6 - var7; var16 < 0; ++var16) {
      int var17 = var0 - var2 >> 4;
      int var18 = var0 + var2 + 15 >> 4;
      if (var17 < left) {
        var17 = left;
      }

      if (var18 > right) {
        var18 = right;
      }

      int var19 = var0 + 15 >> 4;
      int var20 = var19;

      int var21;
      int var22;
      while (var17 < var19) {
        var21 = var17 + var19 >> 1;
        var22 = (var21 << 4) - var0;
        var22 *= var22;
        if (var8 + var22 < var5) {
          var19 = var21;
        } else {
          var17 = var21 + 1;
        }
      }

      while (var18 > var20) {
        var21 = var20 + var18 >> 1;
        var22 = (var21 << 4) - var0;
        var22 *= var22;
        if (var8 + var22 < var5) {
          var20 = var21 + 1;
        } else {
          var18 = var21;
        }
      }

      var21 = (var17 << 4) - var0;
      var21 *= var21;
      var22 = (var17 + 1 << 4) - var0;
      var22 *= var22;
      int var23 = (var17 + 2 << 4) - var0;
      var23 *= var23;
      final int var24 = var22 - var21;
      final int var25 = var23 - var22;
      final int var26 = var25 - var24;
      int var27 = (var18 - 1 << 4) - var0;
      var27 *= var27;
      int var28 = (var18 - 2 << 4) - var0;
      var28 *= var28;
      final int var29 = var28 - var27;
      final boolean var30 = a398(var26, var5, var14 + var17, var17 - var18, screenBuffer, 1, var24, var8 + var21);
      if (var30) {
        a398(var26, var5, var14 + var18 - 1, var17 - var18, screenBuffer, -1, var29, var8 + var27);
      }

      var14 += var15;
      var8 += var11;
      var11 += var13;
    }
  }

  public static void horizontalLine(int x, final int y, int width, final int color) {
    if (y >= top && y < bottom) {
      if (x < left) {
        width -= left - x;
        x = left;
      }

      if (x + width > right) {
        width = right - x;
      }

      final int var4 = pixelIndex(x, y);
      for (int i = 0; i < width; ++i) {
        screenBuffer[var4 + i] = color;
      }
    }
  }

  public static int alphaOver(final int color1, final int color2, final int alpha) {
    final int rb1 = color1 & 0xff00ff;
    final int  g1 = color1 & 0x00ff00;
    return alphaOver(rb1, g1, color2, alpha);
  }

  public static int alphaOver(final int rb1, final int g1, final int color2, final int alpha) {
    final int alpha2 = 256 - alpha;
    final int rb2 = color2 & 0xff00ff;
    final int  g2 = color2 & 0x00ff00;
    final int rb3 = ((rb1 * alpha + rb2 * alpha2) >>> 8) & 0xeaff00ff;
    final int  g3 = (( g1 * alpha +  g2 * alpha2) >>> 8) & 0x9600ff00;
    return rb3 | g3;
  }

  public static int saturatingAdd(final int color1, final int color2) {
    final int    sum = color1 + color2;
    final int rb_sum = (color1 & 0xff00ff) + (color2 & 0xff00ff);
    final int  g_sum = sum - rb_sum;
    final int overflow = (rb_sum & 0x01_00_01_00) + (g_sum & 0x01_00_00);
    return (sum - overflow) | (overflow - (overflow >>> 8));
  }

  public static int saturatingAdd(final int srcColor, final int destColor, final int srcAlpha) {
    final int    rb1 = (srcColor & 0xff00ff) * srcAlpha;
    final int     g1 = (srcColor * srcAlpha) - rb1;
    final int color1 = ((rb1 & 0xff00ff00) + (g1 & 0xff0000)) >>> 8;
    final int    sum = color1 + destColor;
    final int rb_sum = (color1 & 0xff00ff) + (destColor & 0xff00ff);
    final int  g_sum = sum - rb_sum;
    final int overflow = (rb_sum & 0x01_00_01_00) + (g_sum & 0x01_00_00);
    return (sum - overflow) | (overflow - (overflow >>> 8));
  }

  public static int gray(final int value) {
    return (value << 16) | (value << 8) | value;
  }
}
