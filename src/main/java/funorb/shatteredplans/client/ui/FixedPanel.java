package funorb.shatteredplans.client.ui;

import funorb.graphics.Drawing;

import java.util.ListIterator;

public final class FixedPanel extends UIComponent<Object> {
  public PanelState state;

  public FixedPanel(final int x, final int y, final int width, final int height) {
    super(x, y, width, height);
  }

  private static void a050er(final int var0, final int var1, final int var2, final int var3) {
    FloatingPanel.a669am(var2, 0x3ca4a7, var1, var0, var3);
  }

  @Override
  public void draw() {
    if (this.visible) {
      final int[] var2 = new int[4];
      Drawing.saveBoundsTo(var2);
      Drawing.fillRoundedRect(this.x, this.y, this.width, this.height, 10, 0, 200);
      a050er(this.x, this.height, this.width - 10, this.y);
      Drawing.horizontalLine(10 + this.x, this.y, this.width - 20, 2052949);
      Drawing.horizontalLine(this.x + 10, this.y + this.height, this.width - 20, 0);
      Drawing.expandBoundsToInclude(this.x, this.y, this.x + 10, this.y + 10);
      Drawing.strokeCircle(10 + this.x, 10 + this.y, 10, 2052949);
      Drawing.restoreBoundsFrom(var2);
      Drawing.expandBoundsToInclude(this.width + (this.x - 10), this.y, this.width + this.x, this.y + 10);
      Drawing.strokeCircle(this.width + (this.x - 10 - 1), this.y + 10, 10, 2052949);
      Drawing.restoreBoundsFrom(var2);
      Drawing.expandBoundsToInclude(this.x, this.height + this.y - 10, this.x + 10, this.y + this.height);
      Drawing.strokeCircle(this.x + 10, this.y - (-this.height + 10 + 1), 10, 0);
      Drawing.restoreBoundsFrom(var2);
      Drawing.expandBoundsToInclude(this.width + (this.x - 10), this.height + this.y - 10, this.x + this.width, this.y + this.height);
      Drawing.strokeCircle(this.x - 1 - (-this.width + 10), this.y - (-this.height + 10) - 1, 10, 0);
      Drawing.restoreBoundsFrom(var2);

      for (int var3 = 0; var3 < 3 * (this.height - 10) / 4; ++var3) {
        final int var5 = Drawing.alphaOver(0, 0x1f5355, 256 * var3 / (3 * (this.height - 10) / 4));
        Drawing.setPixel(this.x, var3 + 10 + this.y, var5);
        Drawing.setPixel(this.width + this.x - 1, var3 + this.y + 10, var5);
      }

      for (final ListIterator<UIComponent<?>> it = this.children.listIterator(this.children.size()); it.hasPrevious(); ) {
        final UIComponent<?> var4 = it.previous();
        var4.draw();
      }
    }
  }

  @Override
  public UIComponent<?> findMouseTarget(final int x, final int y) {
    if (this.visible) {
      final UIComponent<?> var4 = UIComponent.findMouseTarget(this.children, x, y);
      return var4 != null ? var4 : super.findMouseTarget(x, y);
    } else {
      return null;
    }
  }
}
