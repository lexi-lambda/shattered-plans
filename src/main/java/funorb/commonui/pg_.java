package funorb.commonui;

import funorb.commonui.container.WrapperContainer;
import funorb.graphics.Font;

public final class pg_ extends WrapperContainer {
  private final int _K;
  private final int _H;
  private final boolean _I;
  private final Font _G;
  private final String _L;
  private final int _D;

  public pg_(final int var1, final int var2, final int var3, final Component var5, final boolean var6, final int var7, final int var8, final Font var9, final int var10, final String var11) {
    super(var1, var2, var3, 25);
    this.child = var5;
    this._L = var11;
    this._D = var10;
    this._H = var7;
    this._G = var9;
    this._K = var8;
    this._I = var6;
    final int var12 = -this._K + this._H;
    int var13 = this._G.measureParagraphHeight(var11, var12, this._G.ascent) + this._K * 2;
    if (var13 <= 25) {
      var13 = 25;
    } else {
      this.setBounds(var1, var2, var3, var13);
    }

    final int var14 = this._I ? 0 : 2 * this._K + this._H;
    this.child.setBounds(var14, (-25 + var13 >> 1) + this._K, -this._H + var3 - 3 * this._K, 25 - 2 * this._K);
  }

  @Override
  public String getCurrentTooltip() {
    final boolean var2 = this.child.isMouseOver;
    this.child.isMouseOver = this.isMouseOver;
    final String var3 = this.child.getCurrentTooltip();
    this.child.isMouseOver = var2;
    return var3;
  }

  @Override
  public void draw(final int x, final int y) {
    final int var5 = this.x + x;
    final int var6 = this.y + y;
    super.draw(x, y);
    final int var7 = !this._I ? 0 : -(2 * this._K) - this._H + this.width;
    this._G.drawParagraph(
        this._L,
        var5 + var7 + this._K,
        var6 + this._K,
        this._H - this._K,
        this.height - this._K * 2,
        this._D,
        this._I ? Font.HorizontalAlignment.LEFT : Font.HorizontalAlignment.RIGHT,
        Font.VerticalAlignment.MIDDLE,
        this._G.ascent);
  }
}
