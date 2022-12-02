package funorb.commonui;

import funorb.commonui.container.ListContainer;
import funorb.graphics.Drawing;
import funorb.graphics.Sprite;
import funorb.shatteredplans.client.ShatteredPlansClient;
import org.jetbrains.annotations.NotNull;

public abstract class NavigationPage extends ListContainer {
  private static Sprite drawBuffer;

  private final NavigationRoot root;
  public boolean isAlive = false;
  private int alpha;

  protected NavigationPage(final NavigationRoot root, final int width, final int height) {
    super((ShatteredPlansClient.SCREEN_WIDTH - width) / 2, (ShatteredPlansClient.SCREEN_HEIGHT - height) / 2, width, height);
    this.root = root;
    this.alpha = 0;
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

  public boolean canBeRemoved() {
    this.alpha = this.getTargetAlpha();
    if (this.isAlive) {
      return false;
    } else {
      assert this.alpha == 0;
      return true;
    }
  }

  @Override
  public final @NotNull Component getFocusedChild() {
    final Component child = super.getFocusedChild();
    return child != null ? child : this;
  }

  public void tick2() {
    final int targetAlpha = this.getTargetAlpha();
    final int diff = targetAlpha - this.alpha;
    if (diff > 0) { // fading in
      this.alpha += ((diff - 1) + 8) / 8;
    }
    if (diff < 0) { // fading out
      this.alpha += ((diff - 16) + 1) / 16;
    }
  }

  public final boolean isReadyToBeRemoved() {
    return this.alpha == 0 && this.getTargetAlpha() == 0 && !this.isAlive;
  }

  protected final void setBoundsCentered(final int width, final int height) {
    this.setBounds((ShatteredPlansClient.SCREEN_WIDTH - width) / 2, (ShatteredPlansClient.SCREEN_HEIGHT - height) / 2, width, height);
  }

  private int getTargetAlpha() {
    return !this.isAlive ? 0
        : this == this.root.getActive() ? 256
        : 0;
  }

  protected abstract void drawContent(int x, int y);
}
