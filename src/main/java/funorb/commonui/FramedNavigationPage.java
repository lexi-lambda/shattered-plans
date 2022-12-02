package funorb.commonui;

import funorb.graphics.Drawing;
import funorb.graphics.Sprite;

public abstract class FramedNavigationPage extends NavigationPage {
  private int startWidth;
  private int endHeight;
  private int endWidth;
  private int startHeight;
  private int endTick = 0;
  private int currentTick = 0;

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

  protected final void animateBounds(final int ticks, final int width, final int height) {
    if (ticks <= 0) {
      this.setBoundsCentered(width, height);
    } else {
      this.startWidth = this.width;
      this.startHeight = this.height;
      this.endWidth = width;
      this.endHeight = height;
      this.currentTick = 0;
      this.endTick = ticks;
    }
  }

  @Override
  protected void drawContent(final int x, final int y) {
    Drawing.fillRectangleVerticalGradient(x + 6, y + 35, this.width - 12, this.height - 40, 0x202020, 0);
    final byte var4 = 35;
    final short var5 = 211;
    int var7 = 0;

    for (int var8 = y; var7 < var4; ++var8) {
      if (Drawing.top <= var8 && var8 < Drawing.bottom) {
        int var9 = var5 + var7 * (-17) / var4;
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

    for (int var8 = y + 35; i2 < b; ++var8) {
      int var9 = i1 + (-25) * i2 / b;
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

    for (int var8 = y + 57; i4 < var16; ++var8) {
      int var9 = i3 + i4 * (-42) / var16;
      var9 |= var9 << 16 | var9 << 8;
      Drawing.horizontalLine(x, var8, 6, var9);
      Drawing.horizontalLine(x + this.width - 6, var8, 6, var9);
      ++i4;
    }
  }

  protected void animateBoundsFinished() {
  }

  @Override
  public boolean canBeRemoved() {
    this.skipAnimations();
    return super.canBeRemoved();
  }

  protected void skipAnimations() {
    if (this.endTick > 0) {
      this.setBoundsCentered(this.endWidth, this.endHeight);
      this.endTick = 0;
      this.animateBoundsFinished();
    }
  }

  @Override
  public void tick2() {
    if (this.endTick > 0) {
      if (++this.currentTick >= this.endTick) {
        this.endTick = 0;
        this.animateBoundsFinished();
        this.setBoundsCentered(this.endWidth, this.endHeight);
      } else {
        final int fac = this.currentTick * ((this.endTick * 2) - this.currentTick);
        final int endTickSq = this.endTick * this.endTick;
        final int width  = ((this.endWidth - this.startWidth) * fac / endTickSq) + this.startWidth;
        final int height = ((this.endHeight - this.startHeight) * fac / endTickSq) + this.startHeight;
        this.setBoundsCentered(width, height);
      }
    }

    super.tick2();
  }
}
