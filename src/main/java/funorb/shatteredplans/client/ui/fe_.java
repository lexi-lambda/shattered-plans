package funorb.shatteredplans.client.ui;

import funorb.graphics.Drawing;
import funorb.graphics.Sprite;
import funorb.shatteredplans.client.Menu;

public final class fe_<T> extends UIComponent<T> {
  private final Sprite _D;
  private final int _K;
  private String message;
  private int color;

  public fe_(final int var1, final int var2, final int var3, final int var4, final int var5, final Sprite var6, final String var7, final int var8) {
    super(var1, var2, var3, var4);
    this.color = var8;
    this._K = var5;
    this.message = var7;
    this._D = var6;
  }

  @Override
  public void draw() {
    if (this.visible) {
      if (this._K != -1) {
        Drawing.fillRect(this.x, this.y, this.width, this.height, this._K);
      }

      if (this._D != null) {
        this._D.draw((this.width - this._D.width) / 2 + this.x, (-this._D.height + this.height) / 2 + this.y);
      }

      if (this.message != null) {
        Menu.SMALL_FONT.drawCentered(this.message, this.width / 2 + this.x + 1, (Menu.SMALL_FONT.ascent + Menu.SMALL_FONT.descent) / 4 + this.y + this.height / 2, this.color);
      }

    }
  }

  public void a290(final int color, final String message) {
    this.color = color;
    this.message = message;
  }
}
