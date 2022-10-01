package funorb.client.lobby;

import funorb.awt.MouseState;
import funorb.shatteredplans.client.JagexApplet;

public final class ScrollBar extends Component<Component<?>> {
  private final Component<Component<?>> track;
  private final Component<?> upButton;
  private final Component<?> upperSlideRegion;
  private final Component<?> lowerSlideRegion;
  private final Component<?> dragBar;
  private final Component<?> downButton;
  private int repeatDelayTimer;

  public ScrollBar(final Component<?> upButton, final Component<?> downButton, final Component<?> slideRegion, final Component<?> dragBar) {
    super(null);
    this.upButton = new Component<>(upButton);
    this.downButton = new Component<>(downButton);
    this.addChild(this.upButton);
    this.addChild(this.downButton);
    this.track = new Component<>(null);
    this.addChild(this.track);
    this.upperSlideRegion = new Component<>(slideRegion);
    this.lowerSlideRegion = new Component<>(slideRegion);
    this.lowerSlideRegion._r = true;
    this.upperSlideRegion._r = true;
    this.track.addChild(this.upperSlideRegion);
    this.track.addChild(this.lowerSlideRegion);
    this.dragBar = new Component<>(dragBar);
    this.dragBar.isDraggable = true;
    this.track.addChild(this.dragBar);
  }

  @SuppressWarnings("CopyConstructorMissesField")
  public ScrollBar(final ScrollBar other) {
    this(other.upButton, other.downButton, other.upperSlideRegion, other.dragBar);
  }

  public boolean isUpperSlideRegionClicked() {
    if (this.upperSlideRegion.clickButton == MouseState.Button.NONE) {
      if (this.upperSlideRegion.dragButton != MouseState.Button.NONE) {
        if (this.repeatDelayTimer > 0) {
          --this.repeatDelayTimer;
        }

        if (this.repeatDelayTimer == 0 && this.dragBar.y2 + this.dragBar._w > JagexApplet.mouseY) {
          this.repeatDelayTimer = 3;
          return true;
        }
      }

      return false;
    } else {
      this.repeatDelayTimer = 20;
      return true;
    }
  }

  public int a791(final int var2, final int var3, final boolean var4) {
    int var5 = 0;
    final int var6 = this.track.height - this.dragBar.height;
    if (var6 > 0) {
      final int var7 = this.dragBar.y;
      final int var8 = -var3 + var2;
      var5 = (var6 / 2 + var8 * var7) / var6;
    }

    if (var4) {
      if (var5 < 0) {
        var5 = 0;
      }

      if (var5 > var2 - var3) {
        var5 = var2 - var3;
      }
    } else {
      if (-var3 + var2 < var5) {
        var5 = -var3 + var2;
      }

      if (var5 < 0) {
        var5 = 0;
      }
    }

    return var5;
  }

  public boolean isUpButtonClicked() {
    if (this.upButton.clickButton == MouseState.Button.NONE) {
      if (this.upButton.dragButton != MouseState.Button.NONE) {
        if (this.repeatDelayTimer > 0) {
          --this.repeatDelayTimer;
        }

        if (this.repeatDelayTimer == 0) {
          this.repeatDelayTimer = 3;
          return true;
        }
      }
      return false;
    } else {
      this.repeatDelayTimer = 20;
      return true;
    }
  }

  public boolean isLowerSlideRegionClicked() {
    if (this.lowerSlideRegion.clickButton == MouseState.Button.NONE) {
      if (this.lowerSlideRegion.dragButton != MouseState.Button.NONE) {
        if (this.repeatDelayTimer > 0) {
          --this.repeatDelayTimer;
        }

        if (this.repeatDelayTimer == 0 && JagexApplet.mouseY >= this.dragBar.y2 + this.dragBar._w + this.dragBar.height + this.dragBar._gb) {
          this.repeatDelayTimer = 3;
          return true;
        }
      }

      return false;
    } else {
      this.repeatDelayTimer = 20;
      return true;
    }
  }

  @SuppressWarnings("SameParameterValue")
  public void setBounds(final int x, final int y, final int width, final int height, final int viewportHeight, final int contentHeight, final int scrollPosition) {
    this.x = x;
    this.height = height;
    this.y = y;
    this.width = width;
    this.updateScrollBounds(viewportHeight, contentHeight, scrollPosition);
  }

  public boolean isDragBarBeingDragged() {
    return this.dragBar.dragButton != MouseState.Button.NONE;
  }

  public boolean isDownButtonClicked() {
    if (this.downButton.clickButton == MouseState.Button.NONE) {
      if (this.downButton.dragButton != MouseState.Button.NONE) {
        if (this.repeatDelayTimer > 0) {
          --this.repeatDelayTimer;
        }

        if (this.repeatDelayTimer == 0) {
          this.repeatDelayTimer = 3;
          return true;
        }
      }

      return false;
    } else {
      this.repeatDelayTimer = 20;
      return true;
    }
  }

  public void updateScrollBounds(final int viewportHeight, final int contentHeight, final int scrollPosition) {
    final int trackStart;
    final int trackEnd;
    if (this.width * 2 <= this.height) {
      trackStart = this.width;
      trackEnd = this.height - this.width;
    } else {
      trackStart = trackEnd = this.height / 2;
    }

    final int trackHeight = trackEnd - trackStart;
    int dragBarHeight = trackHeight;
    if (contentHeight > 0) {
      dragBarHeight = Math.min(Math.max(this.width, trackHeight * viewportHeight / contentHeight), trackHeight);
    }

    final int overflowHeight = contentHeight - viewportHeight;
    final int visibleTrackHeight = trackHeight - dragBarHeight;
    int dragBarStart = 0;
    if (overflowHeight > 0) {
      dragBarStart = (scrollPosition * visibleTrackHeight + overflowHeight / 2) / overflowHeight;
    }

    final int upperRegionHeight = dragBarStart + dragBarHeight / 2;
    this.upButton.height = trackStart;
    this.upButton.x = 0;
    this.upButton.y = 0;
    this.upButton.width = this.width;
    this.downButton.x = 0;
    this.downButton.y = trackEnd;
    this.downButton.width = this.width;
    this.downButton.height = this.height - trackEnd;
    this.track.x = 0;
    this.track.y = trackStart;
    this.track.width = this.width;
    this.track.height = trackHeight;
    this.upperSlideRegion.height = upperRegionHeight;
    this.upperSlideRegion.width = this.width;
    this.upperSlideRegion.x = 0;
    this.upperSlideRegion.y = 0;
    this.lowerSlideRegion.height = trackHeight - upperRegionHeight;
    this.lowerSlideRegion.y = upperRegionHeight;
    this.lowerSlideRegion.x = 0;
    this.lowerSlideRegion.width = this.width;
    this.upButton.enabled = this.downButton.enabled = this.track.enabled = contentHeight > viewportHeight;
    this.dragBar.x = 0;
    this.dragBar.y = dragBarStart;
    this.dragBar.width = this.width;
    this.dragBar.height = dragBarHeight;
  }
}
