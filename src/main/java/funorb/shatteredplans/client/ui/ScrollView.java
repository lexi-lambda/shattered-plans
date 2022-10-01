package funorb.shatteredplans.client.ui;

import funorb.graphics.Drawing;

import java.util.ListIterator;

public final class ScrollView<T> extends UIComponent<T> implements ScrollBarListener {
  public int contentHeight = 0;
  private int scrollPosition = 0;

  public ScrollView(final int x, final int y, final int width, final int height) {
    super(x, y, width, height);
  }

  @Override
  public void removeChildren() {
    super.removeChildren();
    this.contentHeight = 0;
    this.scrollPosition = 0;
  }

  @Override
  public void setScrollPosition(final int position) {
    if (this.scrollPosition != position) {
      final int overflow = this.contentHeight - this.height;
      final int newOffset = overflow * position / ScrollBarListener.MAX_SCROLL_POSITION;
      final int oldOffset = overflow * this.scrollPosition / ScrollBarListener.MAX_SCROLL_POSITION;

      for (final UIComponent<?> child : this.children) {
        child.translate(0, oldOffset - newOffset);
      }

      this.scrollPosition = position;
    }
  }

  @Override
  public void addChild(final UIComponent<?> child) {
    super.addChild(child);
    if (child instanceof ScrollView) {
      this.contentHeight += child.height;
    }
  }

  @Override
  public UIComponent<?> findMouseTarget(final int x, final int y) {
    if (this.visible) {
      if (this.hitTest(x, y)) {
        final UIComponent<?> var4 = UIComponent.findMouseTarget(this.children, x, y);
        return var4 != null ? var4 : super.findMouseTarget(x, y);
      } else {
        return null;
      }
    } else {
      return null;
    }
  }

  @Override
  public void draw() {
    if (this.visible) {
      final int[] var3 = new int[4];
      Drawing.saveBoundsTo(var3);
      Drawing.expandBoundsToInclude(this.x, this.y, this.width + this.x, this.y + this.height);

      for (final ListIterator<UIComponent<?>> it = this.children.listIterator(this.children.size()); it.hasPrevious(); ) {
        final UIComponent<?> var4 = it.previous();
        var4.draw();
      }

      Drawing.restoreBoundsFrom(var3);
    }
  }
}
