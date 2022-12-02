package funorb.commonui.form;

import funorb.awt.KeyState;
import funorb.commonui.Button;
import funorb.commonui.CommonUI;
import funorb.commonui.Component;
import funorb.commonui.Resources;
import funorb.commonui.ValidationLabel;
import funorb.commonui.container.ListContainer;
import funorb.commonui.form.field.InputField;
import funorb.commonui.form.field.TextField;
import funorb.commonui.form.validator.InputValidator;
import funorb.commonui.form.validator.UsernameValidator;
import funorb.commonui.form.validator.ValidationState;
import funorb.commonui.listener.ButtonListener;
import funorb.commonui.pg_;
import funorb.commonui.renderer.LinkRenderer;
import funorb.commonui.renderer.TextRenderer;
import funorb.graphics.Drawing;
import funorb.graphics.Font;
import funorb.shatteredplans.StringConstants;

public final class CreateDisplayNameForm extends ListContainer implements CreateForm, ButtonListener {
  public static String _frH;
  public final CreateFormListener _F;
  private final TextField _H = new TextField("", null, 12);
  private final Button _G;
  private final Button _I;

  public CreateDisplayNameForm() {
    super(0, 0, 496, 0);
    final TextRenderer var1 = new TextRenderer(Resources.AREZZO_12, 0, 0, 0, 0, Drawing.WHITE, Font.HorizontalAlignment.JUSTIFY, Font.VerticalAlignment.TOP, Resources.AREZZO_14.ascent, true);
    final Component var2 = new Component(StringConstants.LOGIN_NO_DISPLAY_NAME, var1, null);
    this._I = new Button(StringConstants.OK, null);
    this._G = new Button(StringConstants.CANCEL, new LinkRenderer(), null);
    this._H.tooltip = StringConstants.CREATE_DISPLAYNAME_TOOLTIP;
    this._H.setValidator(new UsernameValidator(this._H));
    this._I.enabled = false;
    final byte var3 = 20;
    final byte var4 = 4;
    final short var5 = 200;
    var2.setBounds(20, var3, 270, 50);
    this.addChild(var2);
    int var6 = 70;
    var6 += this.a516(var6, this._H, StringConstants.CREATE_DIPLAY_NAME_HINT, StringConstants.CREATE_DISPLAY_NAME) + 5;
    this._I.setBounds(148, var6, var5, 40);
    this._G.setBounds(7, var6 + 15, 60, 40);
    this._G.listener = this;
    this._I.listener = this;
    this.addChild(this._I);
    this.addChild(this._G);
    this._F = new CreateFormListener(this);
    this._F.setBounds(this._H.x - (-this._H.width - 60), 20, this.width - this._H.x + (-this._H.width - 60), 150);
    this.addChild(this._F);
    this.setBounds(0, 0, 496, 55 + var6 + var4);
  }

  private int a649(final int var2, final Component var4, final String var5, final String var6) {

    final pg_ var8 = new pg_(20, var2, 290, var4, false, 120, 3, Resources.AREZZO_14, Drawing.WHITE, var6);
    this.addChild(var8);
    final ValidationLabel var9 = new ValidationLabel(((InputField) var4).getValidator(), var5, 126, var8.height + var2, 195, 35);
    var9.listener = this;
    this.addChild(var9);
    return var9.height + var8.height;
  }

  @Override
  public void tick(final int x, final int y, final Component root) {
    super.tick(x, y, root);
    this._I.enabled = this.k154();
  }

  @Override
  public void a150() {
    ((UsernameValidator) this._H.getValidator()).d150();
  }

  @Override
  public void a984(final String var2) {

    this._H.a676(var2, false);
  }

  private void a423() {
    if (this.k154()) {
      _frH = this._H.text;
      CommonUI.setStateLoggingIn(StringConstants.LOGGING_IN, false);
    }
  }

  @Override
  public void handleButtonClicked(final Button button) {
    if (this._G == button) {
      LoginForm.a487la();
    } else if (this._I == button) {
      this.a423();
    }

  }

  private int a516(final int var3, final Component var4, final String var5, final String var6) {
    return this.a649(var3, var4, var5, var6);
  }

  private boolean k154() {

    return this.a294(this._H);
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

  private boolean a294(final InputField var1) {
    final InputValidator var3 = var1.getValidator();
    if (var3 == null) {
      return true;
    } else {
      final ValidationState var4 = var3.validate();
      return var4 == ValidationState.C2;
    }
  }
}
