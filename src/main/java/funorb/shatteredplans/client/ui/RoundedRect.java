package funorb.shatteredplans.client.ui;

import funorb.graphics.Drawing;

public final class RoundedRect extends UIComponent<Object> {
  private static final int CORNER_RADIUS = 2;

  private final int color;

  public RoundedRect(final int x, final int y, final int width, final int height, final int color) {
    super(x, y, width, height);
    this.color = color;
  }

  @Override
  public void draw() {
    if (this.visible) {
      if (this.color <= Drawing.WHITE) {
        Drawing.fillRoundedRect(this.x, this.y, this.width, this.height, CORNER_RADIUS, this.color);
      } else {
        Drawing.fillRoundedRect(this.x, this.y, this.width, this.height, CORNER_RADIUS, this.color & Drawing.WHITE, this.color >> 24);
      }
    }
  }
}
