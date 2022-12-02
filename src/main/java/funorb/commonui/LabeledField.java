package funorb.commonui;

import funorb.commonui.container.WrapperContainer;
import funorb.graphics.Font;

public final class LabeledField extends WrapperContainer {
  private static final int HEIGHT = 25;

  private final int _K;
  private final int _H;
  private final boolean _I;
  private final Font font;
  private final String label;
  private final int _D;

  public LabeledField(final int x, final int y, final int width, final Component child, final boolean var6, final int var7, final int var8, final Font font, final int var10, final String label) {
    super(x, y, width, HEIGHT);
    this.child = child;
    this.label = label;
    this._D = var10;
    this._H = var7;
    this.font = font;
    this._K = var8;
    this._I = var6;
    final int var12 = -this._K + this._H;
    int var13 = this.font.measureParagraphHeight(label, var12, this.font.ascent) + this._K * 2;
    if (var13 <= HEIGHT) {
      var13 = HEIGHT;
    } else {
      this.setBounds(x, y, width, var13);
    }

    final int var14 = this._I ? 0 : 2 * this._K + this._H;
    this.child.setBounds(var14, (-HEIGHT + var13 >> 1) + this._K, -this._H + width - 3 * this._K, HEIGHT - 2 * this._K);
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
    final int var7 = this._I ? this.width - (2 * this._K) - this._H : 0;
    this.font.drawParagraph(
        this.label,
        var5 + var7 + this._K,
        var6 + this._K,
        this._H - this._K,
        this.height - this._K * 2,
        this._D,
        this._I ? Font.HorizontalAlignment.LEFT : Font.HorizontalAlignment.RIGHT,
        Font.VerticalAlignment.MIDDLE,
        this.font.ascent);
  }
}
