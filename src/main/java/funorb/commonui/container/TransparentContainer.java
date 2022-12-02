package funorb.commonui.container;

import funorb.commonui.Component;
import funorb.graphics.Sprite;

public final class TransparentContainer extends WrapperContainer {
  public int alpha;

  public TransparentContainer() {
    super(0, 0, 0, 0);
    this.alpha = 256;
  }

  public TransparentContainer(final Component child) {
    super(child.x, child.y, child.width, child.height);
    child.setBounds(0, 0, this.width, this.height);
    this.child = child;
    this.alpha = 256;
  }

  @Override
  public void draw(final int x, final int y) {
    if (this.child != null && this.alpha != 0) {
      if (this.alpha == 256) {
        this.child.draw(x + this.x, y + this.y);
      } else {
        final Sprite sprite = new Sprite(this.child.width, this.child.height);
        sprite.withInstalledForDrawingUsingOffsets(() -> this.child.draw(0, 0));
        sprite.draw(x + this.x, y + this.y, this.alpha);
      }
    }
  }
}
