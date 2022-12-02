package funorb.commonui;

import funorb.Strings;
import funorb.awt.KeyState;
import funorb.commonui.container.ListContainer;
import funorb.commonui.form.CreateAccountForm;
import funorb.commonui.listener.ButtonListener;
import funorb.commonui.renderer.TextRenderer;
import funorb.graphics.Drawing;
import funorb.graphics.Font;
import funorb.shatteredplans.StringConstants;

public final class pe_ extends ListContainer implements op_, ButtonListener {
  private final Button _H;
  private final CreateAccountPage _I;

  public pe_(final CreateAccountPage var1) {
    super(0, 0, 288, 0);
    this._I = var1;
    this._H = new Button(StringConstants.CONT, null);
    final String var2 = Strings.format(StringConstants.CREATE_U13_TERMS, "<u=2164A2><col=2164A2>", "</col></u>");
    final byte var3 = 20;
    final TextRenderer var4 = new TextRenderer(Resources.AREZZO_14, 0, 0, 0, 0, Drawing.WHITE, Font.HorizontalAlignment.JUSTIFY, Font.VerticalAlignment.TOP, Resources.AREZZO_14.ascent, true);
    final ts_ _G = new ts_(var2, var4);
    _G.tooltip = "";
    _G.a096(0, StringConstants.OPEN_IN_POPUP_WINDOW);
    _G.a096(1, StringConstants.OPEN_IN_POPUP_WINDOW);
    _G.listener = this;
    _G.width = this.width - 40;
    _G.a652(26, var3, this.width - 40);
    final int var7 = var3 + _G.height + 15;
    this.addChild(_G);
    final byte var5 = 4;
    final short var6 = 200;
    this._H.setBounds(50, var7, var6, 40);
    this._H.listener = this;
    this.addChild(this._H);
    this.setBounds(0, 0, 300, var7 + 55 + var5);
  }

  @Override
  public void a746(final int var2) {
    if (var2 == 0) {
      CreateAccountForm.a984gm("terms.ws");
    } else if (var2 == 1) {
      CreateAccountForm.a984gm("privacy.ws");
    } else if (var2 == 2) {
      CreateAccountForm.a984gm("conduct.ws");
    }
  }

  @Override
  public boolean keyTyped(final int keyCode, final char keyChar, final Component focusRoot) {
    if (super.keyTyped(keyCode, keyChar, focusRoot)) {
      return true;
    } else if (keyCode == KeyState.Code.UP) {
      return this.a611(focusRoot);
    } else {
      return keyCode == KeyState.Code.DOWN && this.a948(focusRoot);
    }
  }

  @Override
  public void handleButtonClicked(final Button button) {
    if (button == this._H) {
      CreateAccountPage.b150rm();
      this._I.i423();
    }

  }
}
