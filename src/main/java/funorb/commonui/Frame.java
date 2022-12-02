package funorb.commonui;

import funorb.commonui.container.ListContainer;
import funorb.graphics.Drawing;
import funorb.graphics.Sprite;
import funorb.shatteredplans.client.ShatteredPlansClient;
import org.jetbrains.annotations.NotNull;

/**
 * A {@link Frame} represents an immediate child of a {@link RootFrame}.
 * {@link Frame}s are always centered within their enclosing {@link RootFrame},
 * and they support smooth animation of their dimensions via the
 * {@link #animateBounds(int, int, int)} method. They also automatically fade
 * in and out based on whether or not they are currently active.
 */
public abstract class Frame extends ListContainer {
  private static Sprite drawBuffer;

  private final RootFrame root;
  public boolean isActive = false;
  private int alpha = 0;

  private int animStartWidth;
  private int animStartHeight;
  private int animEndWidth;
  private int animEndHeight;
  private int animDuration = 0;
  private int animTick = 0;

  protected Frame(final RootFrame root, final int width, final int height) {
    super((ShatteredPlansClient.SCREEN_WIDTH - width) / 2, (ShatteredPlansClient.SCREEN_HEIGHT - height) / 2, width, height);
    this.root = root;
  }

  @Override
  public final @NotNull Component getFocusedChild() {
    final Component child = super.getFocusedChild();
    return child != null ? child : this;
  }

  public final boolean isReadyForRemoval() {
    return !this.isActive && this.getTargetAlpha() == 0 && this.alpha == 0;
  }

  /**
   * @return {@code true} if the view can be immediately removed, {@code false} otherwise
   */
  public final boolean expediteRemoval() {
    this.skipAnimations();
    if (this.isActive) {
      return false;
    } else {
      assert this.alpha == 0;
      return true;
    }
  }

  protected void skipAnimations() {
    this.alpha = this.getTargetAlpha();
    if (this.animDuration > 0) {
      this.setBoundsCentered(this.animEndWidth, this.animEndHeight);
      this.animDuration = 0;
      this.onAnimateBoundsComplete();
    }
  }

  protected final void setBoundsCentered(final int width, final int height) {
    this.setBounds((ShatteredPlansClient.SCREEN_WIDTH - width) / 2, (ShatteredPlansClient.SCREEN_HEIGHT - height) / 2, width, height);
  }

  protected final void animateBounds(final int duration, final int width, final int height) {
    if (duration <= 0) {
      this.setBoundsCentered(width, height);
    } else {
      this.animStartWidth = this.width;
      this.animStartHeight = this.height;
      this.animEndWidth = width;
      this.animEndHeight = height;
      this.animDuration = duration;
      this.animTick = 0;
    }
  }

  protected void onAnimateBoundsComplete() {}

  private int getTargetAlpha() {
    return !this.isActive ? 0
        : this == this.root.getActive() ? 256
        : 0;
  }

  public void tickAnimations() {
    final int targetAlpha = this.getTargetAlpha();
    final int diff = targetAlpha - this.alpha;
    if (diff > 0) { // fading in
      this.alpha += ((diff - 1) + 8) / 8;
    }
    if (diff < 0) { // fading out
      this.alpha += ((diff - 16) + 1) / 16;
    }

    if (this.animDuration > 0) {
      if (++this.animTick >= this.animDuration) {
        this.animDuration = 0;
        this.onAnimateBoundsComplete();
        this.setBoundsCentered(this.animEndWidth, this.animEndHeight);
      } else {
        final int fac = this.animTick * ((this.animDuration * 2) - this.animTick);
        final int endTickSq = this.animDuration * this.animDuration;
        final int width  = ((this.animEndWidth - this.animStartWidth) * fac / endTickSq) + this.animStartWidth;
        final int height = ((this.animEndHeight - this.animStartHeight) * fac / endTickSq) + this.animStartHeight;
        this.setBoundsCentered(width, height);
      }
    }
  }

  @Override
  public final void draw(final int x, final int y) {
    if (this.alpha != 0) {
      if (this.alpha >= 256) {
        this.drawContent(this.x + x, this.y + y);
        super.draw(x, y);
      } else {
        if (drawBuffer == null || drawBuffer.width < this.width || drawBuffer.height < this.height) {
          drawBuffer = new Sprite(this.width, this.height);
        }

        drawBuffer.withInstalledForDrawingUsingOffsets(() -> {
          Drawing.clear();
          this.drawContent(0, 0);
          super.draw(-x - this.x, -y - this.y);
        });
        drawBuffer.draw(this.x + x, this.y + y, this.alpha);
      }
    }
  }

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
}
