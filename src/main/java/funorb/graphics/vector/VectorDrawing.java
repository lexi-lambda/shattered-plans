package funorb.graphics.vector;

import funorb.graphics.Drawing;
import funorb.graphics.Point;
import funorb.util.MathUtil;

import java.util.Arrays;

public final class VectorDrawing {
  private static RasterSpan[] spans;
  private static RasterSpan nextSpan = null;

  private static int firstScanline;
  private static int lastScanline;
  private static int currentScanline;

  private VectorDrawing() {}

  public static void fillPolygon(final Point[] points, final int color) {
    initializeFillPolygon();
    a450bk(points);

    nextSpan = null;
    currentScanline = firstScanline;

    final int rb = color & 0xff00ff;
    final int g = color & 0x00ff00;
    while (true) {
      RasterSpan span;
      int n;
      do {
        if ((span = nextSpan()) == null) {
          return;
        }
        n = (Drawing.width * currentScanline) + span.x;
      } while (span.x >= Drawing.width);

      int i = span.width;
      int var9 = span.alpha2;
      if (span.x >= 0) {
        int alpha = span.alpha1 & 511;
        if (alpha > 256) {
          alpha = 512 - alpha;
        }
        final int color2 = Drawing.screenBuffer[n];
        Drawing.screenBuffer[n++] = Drawing.alphaOver(rb, g, color2, alpha);
      } else {
        i += span.x + 1;
        n -= span.x;
        var9 += (span.x + 1) * span.alphaStep;
        span.x = 0;
      }

      i = Math.min(i, Math.min(1000, Drawing.width - span.x));
      for (--i; i > 0; --i) {
        int alpha = var9 & 511;
        if (alpha > 256) {
          alpha = 512 - alpha;
        }
        final int color2 = Drawing.screenBuffer[n];
        Drawing.screenBuffer[n++] = Drawing.alphaOver(rb, g, color2, alpha);
        var9 += span.alphaStep;
      }
    }
  }

  private static void initializeFillPolygon() {
    firstScanline = Drawing.height - 1;
    lastScanline = 0;
    if (spans != null && Drawing.height <= spans.length) {
      Arrays.fill(spans, 0, Drawing.height, null);
    } else {
      spans = new RasterSpan[Drawing.height];
    }
  }

  private static void a450bk(final Point[] points) {
    for (final Point point : points) {
      int y = point.y >> 4;
      firstScanline = MathUtil.clamp(firstScanline, 0, y);

      ++y;
      if (lastScanline < y) {
        lastScanline = Drawing.height > y ? y : Drawing.height - 1;
      }
    }

    Point prev = points[points.length - 1];
    for (final Point point : points) {
      if (point.y > prev.y) {
        a170tc(prev, point, true);
      } else if (prev.y > point.y) {
        a170tc(point, prev, false);
      }
      prev = point;
    }
  }

  private static RasterSpan nextSpan() {
    while (nextSpan == null && currentScanline < lastScanline) {
      nextSpan = spans[currentScanline++];
    }
    if (nextSpan == null) {
      return null;
    }

    RasterSpan var1 = nextSpan.next;
    if (var1 != null) {
      while (nextSpan.x == var1.x) {
        final int var2 = nextSpan.width - var1.width;
        if (var2 > 0) {
          splitAfter(nextSpan, var1.width);
        } else if (var2 < 0) {
          throw new RuntimeException();
        }

        nextSpan.alpha2 += var1.alpha2;
        nextSpan.alpha1 += var1.alpha1;
        nextSpan.alphaStep += var1.alphaStep;
        final RasterSpan var3 = var1;
        nextSpan.next = var1 = var1.next;
        var3.release();
        if (nextSpan.alpha2 == 0 && nextSpan.alphaStep == 0) {
          if (var1 != null && nextSpan.x == var1.x) {
            var1.alpha1 += nextSpan.alpha1;
            nextSpan.release();
            nextSpan = var1;
            var1 = var1.next;
          } else {
            nextSpan.width = 1;
          }
        }

        if (var1 == null) {
          var1 = nextSpan;
          nextSpan = null;
          var1.release();
          return var1;
        }
      }

      if (var1.x - nextSpan.x < nextSpan.width) {
        splitAfter(nextSpan, var1.x - nextSpan.x);
      }
    }

    final RasterSpan g = nextSpan;
    nextSpan = g.next;
    g.release();
    return g;
  }

  private static void splitAfter(RasterSpan orig, final int width) {
    if (width <= 0 || width >= orig.width) {
      throw new RuntimeException();
    }

    final RasterSpan fresh = RasterSpan.create(
        null,
        orig.x + width,
        orig.width == Integer.MAX_VALUE ? orig.width : orig.width - width,
        orig.alpha2 + orig.alphaStep * (width - 1),
        orig.alpha2 + orig.alphaStep * width,
        orig.alphaStep);

    orig.width = width;
    while (orig.next != null && (orig.next.x < fresh.x || orig.next.x == fresh.x && orig.next.width > fresh.width)) {
      orig = orig.next;
    }

    fresh.next = orig.next;
    orig.next = fresh;
  }

  private static void a370sq(final int x, final int y, int width, final int alpha1, final int alpha2, final int alphaStep) {
    if (y >= 0 && y < Drawing.height && width != 0) {
      if (alpha2 == 0 && alphaStep == 0) {
        if (alpha1 == 0) {
          return;
        }
        width = 1;
      }

      RasterSpan var6 = spans[y];
      if (var6 != null && var6.x <= x && (x != var6.x || var6.width >= width)) {
        while (var6.next != null && (x > var6.next.x || x == var6.next.x && width < var6.next.width)) {
          var6 = var6.next;
        }
        var6.next = RasterSpan.create(var6.next, x, width, alpha1, alpha2, alphaStep);
      } else {
        spans[y] = RasterSpan.create(var6, x, width, alpha1, alpha2, alphaStep);
      }
    }
  }

  private static void a170tc(final Point p1, final Point p2, final boolean reverse) {
    final int dx = p2.x - p1.x;
    final int dy = p2.y - p1.y;
    final int y1 = p1.y >> 4;
    final int y1a = p1.y & 15;
    final int y2 = p2.y >> 4;
    final int y2a = p2.y & 15;
    final int x1 = p1.x >> 4;
    final int x1a = p1.x & 15;
    final int x2 = p2.x >> 4;
    final int x2a = p2.x & 15;
    if (y1 == y2) {
      if (x1 == x2) {
        int var19 = (y2a - y1a) * (x2a + x1a) >> 1;
        int var20 = (y2a - y1a) << 4;
        if (reverse) {
          var19 = -var19;
          var20 = -var20;
        }

        a370sq(x1, y1, Integer.MAX_VALUE, var19, var20, 0);
      } else if (dx < 0) {
        int var19 = (16 - x2a) * (16 - x2a) * -dy / (dx * 2);
        int var20 = (24 - x2a) * -dy / dx;
        int var21 = 16 * -dy / dx;
        int var22 = 16 * (y2a - y1a) - x1a * x1a * -dy / (dx * 2);
        int var23 = (y2a - y1a) * 16;
        if (reverse) {
          var19 = -var19;
          var20 = -var20;
          var22 = -var22;
          var21 = -var21;
          var23 = -var23;
        }

        a370sq(x2, y1, -x2 + x1, var19, var20, var21);
        a370sq(x1, y1, Integer.MAX_VALUE, var22, var23, 0);
      } else {
        int var19 = (-x1a + 16) * (-x1a + 16) * dy / (dx * 2);
        int var20 = (24 - x1a) * dy / dx;
        int var21 = 16 * dy / dx;
        int var22 = -(x2a * x2a * dy / (2 * dx)) + (y2a - y1a) * 16;
        int var23 = (-y1a + y2a) * 16;
        if (reverse) {
          var21 = -var21;
          var20 = -var20;
          var22 = -var22;
          var23 = -var23;
          var19 = -var19;
        }

        a370sq(x1, y1, x2 - x1, var19, var20, var21);
        a370sq(x2, y1, Integer.MAX_VALUE, var22, var23, 0);
      }
    } else {
      final int var19 = dx * (-y1a + 16) / dy + p1.x;
      int var20 = var19 >> 4;
      int var21 = 15 & var19;
      if (var20 == x1) {
        int var22 = (-y1a + 16) * (32 - x1a - var21) / 2;
        int var23 = -(16 * y1a) + 256;
        if (reverse) {
          var23 = -var23;
          var22 = -var22;
        }

        a370sq(x1, y1, Integer.MAX_VALUE, var22, var23, 0);
      } else if (dx < 0) {
        int var22 = -dy * (-var21 + 16) * (-var21 + 16) / (dx * 2);
        int var23 = (-var21 + 24) * 16 * -dy / dx;
        int var24 = -dy * 256 / dx;
        int var25 = 16 * (-y1a + 16) + x1a * x1a * dy / (dx * 2);
        int var26 = -(y1a * 16) + 256;
        if (reverse) {
          var26 = -var26;
          var22 = -var22;
          var24 = -var24;
          var23 = -var23;
          var25 = -var25;
        }

        a370sq(var20, y1, -var20 + x1, var22, var23, var24);
        a370sq(x1, y1, Integer.MAX_VALUE, var25, var26, 0);
      } else {
        int var22 = dy * (-x1a + 16) * (16 - x1a) / (2 * dx);
        int var23 = 16 * (-x1a + 24) * dy / dx;
        int var24 = (-(16 * dy) + 256) / dx;
        int var25 = 16 * (16 - y1a) - var21 * var21 * dy / (2 * dx);
        int var26 = 256 - y1a * 16;
        if (reverse) {
          var22 = -var22;
          var25 = -var25;
          var24 = -var24;
          var26 = -var26;
          var23 = -var23;
        }

        a370sq(x1, y1, var20 - x1, var22, var23, var24);
        a370sq(var20, y1, Integer.MAX_VALUE, var25, var26, 0);
      }

      for (int var22 = 1 + y1; y2 > var22; ++var22) {
        final int var23 = p1.x + dx * (16 * (-y1 + var22) - (y1a - 16)) / dy;
        final int var24 = var23 >> 4;
        final int var25 = var23 & 15;
        if (var24 == var20) {
          int var26 = 8 * (-var21 + 32 - var25);
          int var27 = 256;
          if (reverse) {
            var27 = -var27;
            var26 = -var26;
          }

          a370sq(var20, var22, Integer.MAX_VALUE, var26, var27, 0);
        } else if (dx >= 0) {
          int var26 = dy * (16 - var21) * (-var21 + 16) / (dx * 2);
          int var27 = (-var21 + 24) * 16 * dy / dx;
          int var28 = 256 * dy / dx;
          int var29 = -(var25 * var25 * dy / (dx * 2)) + 256;
          int var30 = 256;
          if (reverse) {
            var28 = -var28;
            var30 = -var30;
            var27 = -var27;
            var26 = -var26;
            var29 = -var29;
          }

          a370sq(var20, var22, var24 - var20, var26, var27, var28);
          a370sq(var24, var22, Integer.MAX_VALUE, var29, var30, 0);
        } else {
          int var26 = (-var25 + 16) * (-var25 + 16) * -dy / (dx * 2);
          int var27 = -dy * (-(var25 * 16) + 384) / dx;
          int var28 = 256 * -dy / dx;
          int var29 = 256 - var21 * var21 * -dy / (dx * 2);
          int var30 = 256;
          if (reverse) {
            var29 = -var29;
            var27 = -var27;
            var30 = -var30;
            var26 = -var26;
            var28 = -var28;
          }

          a370sq(var24, var22, var20 - var24, var26, var27, var28);
          a370sq(var20, var22, Integer.MAX_VALUE, var29, var30, 0);
        }

        var21 = var25;
        var20 = var24;
      }

      final int var19a = p2.x - dx * y2a / dy;
      final int var21a = 15 & var19a;
      final int var20a = var19a >> 4;
      if (x2 == var20a) {
        int var22 = (32 - (x2a + var21a)) * y2a / 2;
        int var23 = 16 * y2a;
        if (reverse) {
          var22 = -var22;
          var23 = -var23;
        }

        a370sq(x2, y2, Integer.MAX_VALUE, var22, var23, 0);
      } else if (dx >= 0) {
        int var22 = (-var21a + 16) * (16 - var21a) * dy / (dx * 2);
        int var23 = dy * 16 * (-var21a + 24) / dx;
        int var24 = dy * 256 / dx;
        int var25 = 16 * y2a - dy * x2a * x2a / (2 * dx);
        int var26 = y2a * 16;
        if (reverse) {
          var24 = -var24;
          var23 = -var23;
          var25 = -var25;
          var22 = -var22;
          var26 = -var26;
        }

        a370sq(var20a, y2, x2 - var20a, var22, var23, var24);
        a370sq(x2, y2, Integer.MAX_VALUE, var25, var26, 0);
      } else {
        int var22 = (-x2a + 16) * (-x2a + 16) * -dy / (dx * 2);
        int var23 = 16 * (24 - x2a) * -dy / dx;
        int var24 = -dy * 256 / dx;
        int var25 = -(-dy * var21a * var21a / (dx * 2)) + 16 * y2a;
        int var26 = y2a * 16;
        if (reverse) {
          var24 = -var24;
          var25 = -var25;
          var26 = -var26;
          var22 = -var22;
          var23 = -var23;
        }

        a370sq(x2, y2, -x2 + var20a, var22, var23, var24);
        a370sq(var20a, y2, Integer.MAX_VALUE, var25, var26, 0);
      }
    }
  }
}
