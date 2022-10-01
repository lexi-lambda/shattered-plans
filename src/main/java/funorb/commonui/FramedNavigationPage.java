package funorb.commonui;

import funorb.graphics.Drawing;
import funorb.graphics.Sprite;

public abstract class FramedNavigationPage extends NavigationPage {
  private int _L;
  private int _K;
  private int _T;
  private int _R;
  private int _P = 0;
  private int _Q = 0;

  protected FramedNavigationPage(final NavigationRoot root, final int width, final int height) {
    super(root, width, height);
  }

  private static void drawFrameTopBottom(final Sprite[] sprite, final int x, final int y, final int width) {
    if (sprite != null && width > 0) {
      final int leftOffset = sprite[0].offsetX;
      final int middleOffset = sprite[1].offsetX;
      final int rightOffset = sprite[2].offsetX;
      sprite[0].draw(x, y);
      sprite[2].draw(x + width - rightOffset, y);
      Drawing.withSavedBounds(() -> {
        Drawing.expandBoundsToInclude(leftOffset + x, y, x + width - rightOffset, sprite[1].offsetY + y);
        final int right = x + width - rightOffset;
        for (int i = x + leftOffset; i < right; i += middleOffset) {
          sprite[1].draw(i, y);
        }
      });
    }
  }

  protected final void b115(final int var2, final int var3, final int var4) {
    if (var2 <= 0) {
      this.b599(var4, var3);
    } else {
      this._L = this.width;
      this._R = this.height;
      this._T = var3;
      this._Q = 0;
      this._K = var4;
      this._P = var2;
    }
  }

  @Override
  protected void drawContent(final int x, final int y) {
    Drawing.fillRectangleVerticalGradient(x + 6, y + 35, this.width - 12, this.height - 40, 0x202020, 0);
    final byte var4 = 35;
    final short var5 = 211;
    int var7 = 0;

    int var8;
    int var9;
    for (var8 = y; var7 < var4; ++var8) {
      if (Drawing.top <= var8 && var8 < Drawing.bottom) {
        var9 = var5 + var7 * (-17) / var4;
        int var10 = 0;
        int var11 = this.width;
        int var12;
        int var13;
        if (var7 <= 20) {
          for (; var10 <= 20; ++var10) {
            var12 = (-var7 + 20) * (-var7 + 20) + (20 - var10) * (-var10 + 20);
            if (var12 <= 462) {
              if (var12 < 420) {
                break;
              }

              var13 = (-var12 + 462) * var9 / 42;
              var13 |= var13 << 16 | var13 << 8;
              Drawing.screenBuffer[var10 + var8 * Drawing.width + x] = var13;
            }
          }
        }

        if (var7 <= 20) {
          var12 = var11;
          var11 -= 21;

          for (var13 = 0; var13 <= 20; ++var11) {
            final int var14 = var13 * var13 + (20 - var7) * (-var7 + 20);
            if (var14 > 462) {
              break;
            }

            if (var14 >= 420) {
              int var15 = var9 * (462 - var14) / 42;
              var15 |= var15 << 8 | var15 << 16;
              Drawing.screenBuffer[var11 + x + Drawing.width * var8] = var15;
            } else {
              var12 = var11 + 1;
            }

            ++var13;
          }

          var11 = var12;
        }

        var9 |= var9 << 8 | var9 << 16;
        Drawing.horizontalLine(var10 + x, var8, var11 - var10, var9);
      }

      ++var7;
    }

    final byte b = 22;
    final short i1 = 194;
    int i2 = 0;

    for (var8 = y + 35; i2 < b; ++var8) {
      var9 = i1 + (-25) * i2 / b;
      var9 |= var9 << 8 | var9 << 16;
      Drawing.horizontalLine(x, var8, 6, var9);
      Drawing.horizontalLine(this.width + x - 6, var8, 6, var9);
      ++i2;
    }

    Resources.JAGEX_LOGO_GREY.draw(this.width + x - 90, 10 + y);
    drawFrameTopBottom(Resources.FRAME_TOP, 5 + x, y + 35, this.width - 10);
    drawFrameTopBottom(Resources.FRAME_BOTTOM, x, this.height + y - 22, this.width);
    final short i3 = 169;
    final int var16 = this.height - 79;
    int i4 = 0;

    for (var8 = y + 57; i4 < var16; ++var8) {
      var9 = i3 + i4 * (-42) / var16;
      var9 |= var9 << 16 | var9 << 8;
      Drawing.horizontalLine(x, var8, 6, var9);
      Drawing.horizontalLine(x + this.width - 6, var8, 6, var9);
      ++i4;
    }

  }

  protected void g423() {
  }

  @Override
  public boolean k154() {
    this.n150();
    return super.k154();
  }

  protected void n150() {
    if (this._P > 0) {
      this.b599(this._K, this._T);
      this._P = 0;
      this.g423();
    }
  }

  @Override
  public void tick2() {
    int var2;
    if (this._P > 0) {
      var2 = this._T;
      int var3 = this._K;
      if (++this._Q >= this._P) {
        this._P = 0;
        this.g423();
      } else {
        final int var4 = (-this._Q + 2 * this._P) * this._Q;
        final int var5 = this._P * this._P;
        var3 = var4 * (-this._R + this._K) / var5 + this._R;
        var2 = (this._T - this._L) * var4 / var5 + this._L;
      }

      this.b599(var3, var2);
    }

    super.tick2();
  }
}
