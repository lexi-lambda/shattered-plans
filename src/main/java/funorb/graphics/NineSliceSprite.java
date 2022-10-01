package funorb.graphics;

@SuppressWarnings("WeakerAccess")
public final class NineSliceSprite {
  public static final int TOP_LEFT = 0;
  public static final int TOP = 1;
  public static final int TOP_RIGHT = 2;
  public static final int LEFT = 3;
  public static final int CENTER = 4;
  public static final int RIGHT = 5;
  public static final int BOTTOM_LEFT = 6;
  public static final int BOTTOM = 7;
  public static final int BOTTOM_RIGHT = 8;

  private final Sprite[] patches;

  public NineSliceSprite(final Sprite[] var1) {
    this.patches = var1;
  }

  public static void draw(final Sprite[] patches, final int x, final int y, final int width, final int height) {
    if (patches != null) {
      if (width > 0 && height > 0) {
        final int topOffset    = patches[TOP]    != null ? patches[TOP].offsetY    : 0;
        final int leftOffset   = patches[LEFT]   != null ? patches[LEFT].offsetX   : 0;
        final int rightOffset  = patches[RIGHT]  != null ? patches[RIGHT].offsetX  : 0;
        final int bottomOffset = patches[BOTTOM] != null ? patches[BOTTOM].offsetY : 0;

        final int right = x + width;
        final int bottom = y + height;

        final int leftInsideX   = x + leftOffset;
        final int rightInsideX  = right - rightOffset;
        final int topInsideY    = y + topOffset;
        final int bottomInsideY = bottom - bottomOffset;

        int var16 = leftInsideX;
        int var17 = rightInsideX;
        if (leftInsideX > rightInsideX) {
          var16 = var17 = x + ((width * leftOffset) / (rightOffset + leftOffset));
        }

        int var18 = topInsideY;
        int var19 = bottomInsideY;
        if (topInsideY > bottomInsideY) {
          var18 = var19 = y + ((topOffset * height) / (topOffset + bottomOffset));
        }

        final int[] savedBounds = new int[4];
        Drawing.saveBoundsTo(savedBounds);
        if (patches[TOP_LEFT] != null) {
          Drawing.expandBoundsToInclude(x, y, var16, var18);
          patches[TOP_LEFT].draw(x, y);
          Drawing.restoreBoundsFrom(savedBounds);
        }

        if (patches[TOP_RIGHT] != null) {
          Drawing.expandBoundsToInclude(var17, y, right, var18);
          patches[TOP_RIGHT].draw(rightInsideX, y);
          Drawing.restoreBoundsFrom(savedBounds);
        }

        if (patches[BOTTOM_LEFT] != null) {
          Drawing.expandBoundsToInclude(x, var19, var16, bottom);
          patches[BOTTOM_LEFT].draw(x, bottomInsideY);
          Drawing.restoreBoundsFrom(savedBounds);
        }

        if (patches[BOTTOM_RIGHT] != null) {
          Drawing.expandBoundsToInclude(var17, var19, right, bottom);
          patches[BOTTOM_RIGHT].draw(rightInsideX, bottomInsideY);
          Drawing.restoreBoundsFrom(savedBounds);
        }

        if (patches[TOP] != null && patches[TOP].offsetX != 0) {
          Drawing.expandBoundsToInclude(var16, y, var17, var18);
          for (int i = leftInsideX; i < rightInsideX; i += patches[TOP].offsetX) {
            patches[TOP].draw(i, y);
          }
          Drawing.restoreBoundsFrom(savedBounds);
        }

        if (patches[BOTTOM] != null && patches[BOTTOM].offsetX != 0) {
          Drawing.expandBoundsToInclude(var16, var19, var17, bottom);
          for (int i = leftInsideX; i < rightInsideX; i += patches[BOTTOM].offsetX) {
            patches[BOTTOM].draw(i, bottomInsideY);
          }
          Drawing.restoreBoundsFrom(savedBounds);
        }

        if (patches[LEFT] != null && patches[LEFT].offsetY != 0) {
          Drawing.expandBoundsToInclude(x, var18, var16, var19);
          for (int i = topInsideY; bottomInsideY > i; i += patches[LEFT].offsetY) {
            patches[LEFT].draw(x, i);
          }
          Drawing.restoreBoundsFrom(savedBounds);
        }

        if (patches[RIGHT] != null && patches[RIGHT].offsetY != 0) {
          Drawing.expandBoundsToInclude(var17, var18, right, var19);
          for (int i = topInsideY; i < bottomInsideY; i += patches[RIGHT].offsetY) {
            patches[RIGHT].draw(rightInsideX, i);
          }
          Drawing.restoreBoundsFrom(savedBounds);
        }

        if (patches[CENTER] != null && patches[CENTER].offsetX != 0 && patches[CENTER].offsetY != 0) {
          Drawing.expandBoundsToInclude(var16, var18, var17, var19);
          for (int i = topInsideY; i < bottomInsideY; i += patches[CENTER].offsetY) {
            for (int j = leftInsideX; j < rightInsideX; j += patches[CENTER].offsetX) {
              patches[CENTER].draw(j, i);
            }
          }
          Drawing.restoreBoundsFrom(savedBounds);
        }
      }
    }
  }

  public void draw(final int var1, final int var2, final int var3, final int var4) {
    draw(this.patches, var2, var3, var4, var1);
  }
}
