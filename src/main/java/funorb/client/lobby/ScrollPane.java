package funorb.client.lobby;

import funorb.commonui.kj_;

public final class ScrollPane<T extends Component<?>> extends Component<Component<?>> {
  public final Component<T> content;
  public final Component<Component<?>> viewport;
  public final ScrollBar scrollBar;

  public ScrollPane(final Component<T> content, final Component<?> viewportAttrsSrc, final ScrollBar scrollBar) {
    super(null);
    this.viewport = new Component<>(viewportAttrsSrc);
    this.scrollBar = new ScrollBar(scrollBar);
    this.addChild(this.viewport);
    this.addChild(this.scrollBar);
    this.content = content;
    this.viewport.addChild(content);
  }

  public static kj_ a705(final boolean var0) {
    final kj_ var1 = new kj_(true);
    var1._b = var0;
    return var1;
  }

  private void processScrollBar(final int mouseWheelRotation, final int buttonScrollAmount, final int slideRegionScrollAmount, final boolean var2) {
    if (this.scrollBar.isUpButtonClicked()) {
      this.content._w += buttonScrollAmount;
    }
    if (this.scrollBar.isDownButtonClicked()) {
      this.content._w -= buttonScrollAmount;
    }
    if (this.scrollBar.isUpperSlideRegionClicked()) {
      this.content._w += slideRegionScrollAmount;
    }
    if (this.scrollBar.isLowerSlideRegionClicked()) {
      this.content._w -= slideRegionScrollAmount;
    }
    if (this.isMouseOverTarget) {
      this.content._w -= mouseWheelRotation;
    }

    if (var2) {
      if (this.content._w + this.content.y > 0) {
        this.content._w = -this.content.y;
      }

      if (-this.viewport.height + this.content._gb + this.content.height < -(this.content.y + this.content._w)) {
        this.content._w = -(this.content.height + this.content._gb - this.viewport.height) - this.content.y;
      }
    } else {
      if (-(this.content._w + this.content.y) > this.content.height + this.content._gb - this.viewport.height) {
        this.content._w = -this.content.y - (-this.viewport.height + this.content._gb + this.content.height);
      }

      if (this.content.y + this.content._w > 0) {
        this.content._w = -this.content.y;
      }
    }

    if (this.scrollBar.isDragBarBeingDragged()) {
      this.content.y = -this.scrollBar.a791(this.content.height, this.viewport.height, var2);
      this.content._w = 0;
    }

    this.scrollBar.updateScrollBounds(this.viewport.height, this.content.height, -this.content.y);
  }

  public void a795(final int mouseWheelRotationAmount, final int buttonScrollAmount) {
    this.processScrollBar(mouseWheelRotationAmount, buttonScrollAmount, this.viewport.height, true);
  }

  public void setBounds(final int x, final int y, final int width, final int height, final int scrollBarWidth) {
    this.x = x;
    this.width = width;
    this.y = y;
    this.height = height;
    this.updateChildBounds(scrollBarWidth);
  }

  public boolean processScrollInput(final boolean mouseStill, final boolean isContextMenuParent, final int buttonScrollAmount, final int mouseWheelRotationAmount) {
    final boolean var8 = isContextMenuParent || (this.isMouseOverTarget && !mouseStill);
    this.content.a811(this.viewport.height, var8);
    this.processScrollBar(mouseWheelRotationAmount, buttonScrollAmount, this.viewport.height, false);
    return var8;
  }

  private void updateChildBounds(final int scrollBarWidth) {
    this.viewport.height = this.height;
    this.viewport.width = (this.width - scrollBarWidth) - 2;
    this.content.x = 0;
    this.content.width = this.width - scrollBarWidth - 2;
    this.scrollBar.setBounds(this.width - scrollBarWidth, 0, scrollBarWidth, this.height, this.viewport.height, this.content.height, -this.content.y);
  }
}
