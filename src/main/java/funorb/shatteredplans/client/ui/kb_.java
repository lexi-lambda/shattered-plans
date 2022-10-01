package funorb.shatteredplans.client.ui;

import funorb.graphics.Drawing;
import funorb.graphics.Sprite;
import funorb.shatteredplans.client.Menu;

public final class kb_ extends UIComponent<Object> {
  private final boolean _B;
  private final int _u;
  private final Sprite _C;
  private final String _z;

  public kb_(final int var1, final int var2, final int var3, final int var4, final String var5, final boolean var7) {
    super(var1, var2, var3, Menu.SMALL_FONT.ascent);
    this._B = var7;
    this._u = Drawing.WHITE;
    this._z = var5;
    this._C = new Sprite(this.width, this.height);
    Drawing.saveContext();
    this._C.installForDrawing();

    int var8;
    for (var8 = 0; this._C.height > var8; ++var8) {
      final int var9 = (this._C.height / 2 - var8) * (this._C.height / 2 - var8);
      Drawing.horizontalLine(0, var8, this._C.width, Drawing.alphaOver(var4, 0, -(256 * var9 / (this._C.height / 2 * (this._C.height / 2))) + 256));
    }

    for (var8 = 0; var8 < this._C.height; ++var8) {
      Drawing.setPixel(0, var8, Drawing.alphaOver(this._C.pixels[var8 * this._C.width], 0, 128));
      Drawing.setPixel(this._C.width - 1, var8, Drawing.alphaOver(this._C.pixels[this._C.width * (1 + var8) - 1], 0, 128));
    }

    Drawing.setPixel(1, 1, Drawing.alphaOver(this._C.pixels[1 + this._C.width], 0, 128));
    Drawing.setPixel(2, 1, Drawing.alphaOver(this._C.pixels[this._C.width + 2], 0, 192));
    Drawing.setPixel(1, 2, Drawing.alphaOver(this._C.pixels[1 + this._C.width * 2], 0, 192));
    Drawing.setPixel(1, this._C.height - 2, Drawing.alphaOver(this._C.pixels[1 + this._C.width * (this._C.height - 2)], 0, 128));
    Drawing.setPixel(2, this._C.height - 2, Drawing.alphaOver(this._C.pixels[2 + (this._C.height - 2) * this._C.width], 0, 192));
    Drawing.setPixel(1, this._C.height - 3, Drawing.alphaOver(this._C.pixels[1 + this._C.width * (this._C.height - 3)], 0, 192));
    Drawing.setPixel(this._C.width - 2, 1, Drawing.alphaOver(this._C.pixels[2 * this._C.width - 2], 0, 128));
    Drawing.setPixel(this._C.width - 3, 1, Drawing.alphaOver(this._C.pixels[this._C.width * 2 - 3], 0, 192));
    Drawing.setPixel(this._C.width - 2, 2, Drawing.alphaOver(this._C.pixels[3 * this._C.width - 2], 0, 192));
    Drawing.setPixel(this._C.width - 2, this._C.height - 2, Drawing.alphaOver(this._C.pixels[this._C.width * (this._C.height - 1) - 2], 0, 128));
    Drawing.setPixel(this._C.width - 3, this._C.height - 2, Drawing.alphaOver(this._C.pixels[(this._C.height - 1) * this._C.width - 3], 0, 192));
    Drawing.setPixel(this._C.width - 2, this._C.height - 3, Drawing.alphaOver(this._C.pixels[(this._C.height - 2) * this._C.width - 2], 0, 192));
    Drawing.restoreContext();
  }

  @Override
  public void draw() {
    if (this.visible) {
      final int[] var2 = new int[4];
      Drawing.saveBoundsTo(var2);
      Drawing.expandBoundsToInclude(this.x, this.y, this.width + this.x, this.height + this.y);
      final int var3 = Menu.SMALL_FONT.ascent;
      this._C.draw(this.x, this.y);
      if (this._z != null) {
        if (this._B) {
          Menu.SMALL_FONT.drawCentered(this._z, 1 + this.x + this.width / 2, (Menu.SMALL_FONT.ascent + Menu.SMALL_FONT.descent) / 2 + 3 + this.y, this._u);
        } else {
          Menu.SMALL_FONT.draw(this._z, this.x + var3 / 2, (Menu.SMALL_FONT.descent + Menu.SMALL_FONT.ascent) / 2 + this.y + 3, this._u);
        }
      }

      Drawing.restoreBoundsFrom(var2);
    }
  }
}
