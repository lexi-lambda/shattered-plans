package funorb.commonui;

import funorb.commonui.container.ListContainer;
import funorb.commonui.listener.ButtonListener;
import funorb.commonui.listener.ComponentListener;
import funorb.graphics.Drawing;
import funorb.graphics.Font;

public final class cf_ extends ListContainer implements ButtonListener {
  private final AccountPage _K;
  private final Font _J;
  private final String _L;
  private CommonUI.TickResult[] _N;
  private int _G = 0;
  private Button[] _E;

  public cf_(final AccountPage var1, final Font var2, final String var3) {
    super(0, 0, 288, 0);
    this._J = var2;
    this._K = var1;
    this._L = var3;
    final int var4 = this._L != null ? this._J.measureParagraphHeight(this._L, 260, this._J.ascent) : 0;
    this.setBounds(0, 0, 288, 22 + var4);
  }

  public Button a700(final ComponentListener var1, final String var3) {
    final Button var4 = new Button(var3, var1);
    final int var5 = this.height - 2;
    this.setBounds(0, 0, this.width, this.height + 34);
    var4.setBounds(7, var5, this.width - 14, 30);
    this.addChild(var4);
    return var4;
  }

  public void a966(final String var1, final CommonUI.TickResult var3) {
    final int var4 = this._G;
    this.b430(var4 + 1);
    this._E[var4] = this.a700(this, var1);
    this._N[var4] = var3;
  }

  @Override
  public void draw(final int x, final int y) {
    super.draw(x, y);
    this._J.drawParagraph(this._L, x + this.x + 14, 10 + this.y + y, this.width - 28, this.height, Drawing.WHITE, Font.HorizontalAlignment.LEFT, Font.VerticalAlignment.TOP, this._J.ascent);
  }

  @Override
  public void handleButtonClicked(final Button button) {
    for (int var6 = 0; var6 < this._G; ++var6) {
      if (button == this._E[var6]) {
        final CommonUI.TickResult var7 = this._N[var6];
        if (var7 == null) {
          this._K.i423();
        } else {
          CommonUI._crb = this._N[var6];
        }
        break;
      }
    }

  }

  private void b430(final int var1) {
    if (var1 > this._G) {
      final Button[] var3 = new Button[var1];
      final CommonUI.TickResult[] var4 = new CommonUI.TickResult[var1];

      for (int var5 = 0; var5 < this._G; ++var5) {
        var3[var5] = this._E[var5];
        var4[var5] = this._N[var5];
      }

      this._N = var4;
      this._G = var1;
      this._E = var3;
    }
  }
}
