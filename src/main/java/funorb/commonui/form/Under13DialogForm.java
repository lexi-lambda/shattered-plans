package funorb.commonui.form;

import funorb.Strings;
import funorb.awt.KeyState;
import funorb.commonui.Button;
import funorb.commonui.Component;
import funorb.commonui.Label;
import funorb.commonui.Resources;
import funorb.commonui.container.ListContainer;
import funorb.commonui.frame.CreateAccountFrame;
import funorb.commonui.listener.ButtonListener;
import funorb.commonui.listener.LinkedTextListener;
import funorb.commonui.renderer.TextRenderer;
import funorb.graphics.Drawing;
import funorb.graphics.Font;
import funorb.shatteredplans.StringConstants;

public final class Under13DialogForm extends ListContainer implements LinkedTextListener, ButtonListener {
  private final CreateAccountFrame createAccountFrame;
  private final Button continueButton;

  public Under13DialogForm(final CreateAccountFrame createAccountFrame) {
    super(0, 0, 288, 0);
    this.createAccountFrame = createAccountFrame;
    this.continueButton = new Button(StringConstants.CONT, null);
    final String var2 = Strings.format(StringConstants.CREATE_U13_TERMS, "<u=2164A2><col=2164A2>", "</col></u>");
    final byte var3 = 20;
    final TextRenderer var4 = new TextRenderer(Resources.AREZZO_14, 0, 0, 0, 0, Drawing.WHITE, Font.HorizontalAlignment.JUSTIFY, Font.VerticalAlignment.TOP, Resources.AREZZO_14.ascent, true);
    final Label label = new Label(var2, var4);
    label.tooltip = "";
    label.setHotspotTooltip(0, StringConstants.OPEN_IN_POPUP_WINDOW);
    label.setHotspotTooltip(1, StringConstants.OPEN_IN_POPUP_WINDOW);
    label.listener = this;
    label.width = this.width - 40;
    label.setBounds(26, var3, this.width - 40);
    final int var7 = var3 + label.height + 15;
    this.addChild(label);
    this.continueButton.setBounds(50, var7, 200, 40);
    this.continueButton.listener = this;
    this.addChild(this.continueButton);
    this.setBounds(0, 0, 300, var7 + 55 + 4);
  }

  @Override
  public void handleLinkClicked(final int hotspotIndex) {
    if (hotspotIndex == 0) {
      CreateAccountForm.a984gm("terms.ws");
    } else if (hotspotIndex == 1) {
      CreateAccountForm.a984gm("privacy.ws");
    } else if (hotspotIndex == 2) {
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
    if (button == this.continueButton) {
      CreateAccountFrame.b150rm();
      this.createAccountFrame.i423();
    }
  }
}
