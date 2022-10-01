package funorb.shatteredplans.client.ui;

import funorb.graphics.Drawing;

public final class uc_ extends UIComponent<Object> {
  private final int _x;
  private final int _u;

  public uc_(final int var2, final int var3, final int var6) {
    super(0, var2, var3, 6);
    this._u = var6;
    this._x = 0;
  }

  @Override
  public void draw() {
    if (this.visible) {
      for (int var3 = 0; this.width > var3; ++var3) {
        final int var2 = Drawing.alphaOver(this._u, this._x, 256 * var3 / this.width);
        Drawing.verticalLine(var3 + this.x, this.y, this.height, var2);
      }
    }
  }
}
