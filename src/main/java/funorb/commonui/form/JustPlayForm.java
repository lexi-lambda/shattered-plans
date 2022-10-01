package funorb.commonui.form;

import funorb.awt.KeyState;
import funorb.commonui.Button;
import funorb.commonui.CommonUI;
import funorb.commonui.Component;
import funorb.commonui.Resources;
import funorb.commonui.container.ListContainer;
import funorb.commonui.listener.ButtonListener;
import funorb.commonui.renderer.ButtonRenderer;
import funorb.graphics.Drawing;
import funorb.graphics.Font;
import funorb.shatteredplans.StringConstants;

public final class JustPlayForm extends ListContainer implements ButtonListener {
  private final Button justPlayButton;
  private final Button backButton;
  private final Button createAccountButton;

  public JustPlayForm() {
    super(0, 0, 476, 225);
    final ButtonRenderer var1 = new ButtonRenderer();
    this.createAccountButton = new Button(StringConstants.CREATE_CREATE_AN_ACCOUNT, var1, null);
    this.backButton = new Button(StringConstants.GO_BACK, var1, null);
    this.justPlayButton = new Button(StringConstants.JUST_PLAY, var1, null);
    final int var2 = 4;
    final int var3 = 326;
    final int var4 = 161;
    this.backButton.setBounds(-326 + this.width >> 1, this.height - 48 - var2, var4, 30);
    this.justPlayButton.setBounds(var2 + var4 + (this.width - var3 >> 1), -52 + this.height, var4, 30);
    this.createAccountButton.setBounds(this.width - var3 >> 1, this.height - 86, var3, 30);
    this.backButton.listener = this;
    this.createAccountButton.listener = this;
    this.justPlayButton.listener = this;
    this.createAccountButton.tooltip = StringConstants.LOGIN_CREATE_TOOLTIP;
    this.justPlayButton.tooltip = StringConstants.LOGIN_JUST_PLAY_TOOLTIP;
    this.addChild(this.backButton);
    this.addChild(this.createAccountButton);
    this.addChild(this.justPlayButton);
  }

  @Override
  public void draw(final int x, final int y) {
    final int var5 = x + this.x;
    final int var6 = y + this.y;
    Resources.AREZZO_14.drawParagraph(StringConstants.CREATE_WELCOME, var5 + 20, var6 + 20, this.width - 40, this.height - 50, Drawing.WHITE, Font.HorizontalAlignment.CENTER, Font.VerticalAlignment.TOP, Resources.AREZZO_14.ascent);
    super.draw(x, y);
  }

  @Override
  public boolean a686(final int keyCode, final char keyChar, final Component var4) {
    if (super.a686(keyCode, keyChar, var4)) {
      return true;
    } else if (keyCode == KeyState.Code.UP) {
      return this.a611(var4);
    } else {
      return keyCode == KeyState.Code.DOWN && this.a948(var4);
    }
  }

  @Override
  public void handleButtonClicked(final Button button) {
    if (button == this.backButton) {
      LoginForm.a487la();
    } else if (button == this.createAccountButton) {
      CreateAccountForm.a423tl();
    } else if (button == this.justPlayButton) {
      CommonUI.a423oo();
    }
  }
}
