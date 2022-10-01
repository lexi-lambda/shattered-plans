package funorb.commonui.form;

import funorb.awt.KeyState;
import funorb.commonui.AbstractTextField;
import funorb.commonui.Button;
import funorb.commonui.CommonUI;
import funorb.commonui.Component;
import funorb.commonui.container.ListContainer;
import funorb.commonui.Resources;
import funorb.commonui.form.field.TextField;
import funorb.commonui.listener.ButtonListener;
import funorb.commonui.listener.TextFieldListener;
import funorb.commonui.CreateAccountPage;
import funorb.commonui.pg_;
import funorb.commonui.renderer.ButtonRenderer;
import funorb.commonui.renderer.LinkRenderer;
import funorb.commonui.renderer.PasswordFieldRenderer;
import funorb.graphics.Drawing;
import funorb.graphics.Font;
import funorb.shatteredplans.StringConstants;

public final class LoginForm extends ListContainer implements TextFieldListener, ButtonListener {
  public static LoginForm _noe;

  private final boolean _L;
  private final String _J;
  private final Button cancelButton;
  private final AbstractTextField usernameField;
  private final AbstractTextField passwordField;
  private final boolean isInitialLogin;
  private final Button logInButton;
  private Button _Q;

  public LoginForm(final String var1, final boolean var3, final boolean canCreateAccount, final boolean isInitialLogin) {
    super(0, 0, 310, 190);
    this._J = null;
    this.isInitialLogin = isInitialLogin;
    this._L = var3;
    final ButtonRenderer var6 = new ButtonRenderer();
    if (!this._L || (!canCreateAccount && !this.isInitialLogin)) {
      this.usernameField = new TextField(var1, this, 100);
      this.passwordField = new TextField("", new PasswordFieldRenderer(), this, 20);
      if (this._L) {
        this.logInButton = new Button(StringConstants.RETRY, var6, null);
        this.cancelButton = new Button(StringConstants.QUIT_TO_WEBSITE, var6, null);
        this.usernameField.enabled = false;
      } else {
        this.logInButton = new Button(StringConstants.LOG_IN, var6, null);
        final String cancelText = this.isInitialLogin ? StringConstants.JUST_PLAY : StringConstants.BACK;
        this.cancelButton = new Button(cancelText, var6, null);
        if (canCreateAccount) {
          this._Q = new Button(StringConstants.CREATE_CREATE_AN_ACCOUNT, var6, this);
        }
      }

      this.usernameField.tooltip = StringConstants.LOGIN_USERNAME_TOOLTIP;
      if (this._Q != null) {
        this._Q.tooltip = StringConstants.LOGIN_CREATE_TOOLTIP;
      }

      if (this._L) {
        this.cancelButton.tooltip = StringConstants.WARNING_IF_YOU_QUIT;
      } else {
        if (this.isInitialLogin) {
          this.cancelButton.tooltip = StringConstants.LOGIN_JUST_PLAY_TOOLTIP;
        }
        this.cancelButton.renderer = new LinkRenderer();
      }

      this.y = 15;
      final Font var7 = Resources.AREZZO_14;

      final String var8 = StringConstants.LOGIN_USERNAME_EMAIL;

      pg_ var10;
      this.addChild(var10 = new pg_(10, this.y, this.width - 20, this.usernameField, false, 80, 3, var7, Drawing.WHITE, var8));
      this.y += var10.height + 5;
      this.addChild(var10 = new pg_(10, this.y, this.width - 20, this.passwordField, false, 80, 3, var7, Drawing.WHITE, StringConstants.CREATE_PASSWORD));
      this.y += var10.height + 5;
      this.logInButton.listener = this;
      if (this._Q != null) {
        this._Q.listener = this;
      }

      this.cancelButton.listener = this;

      if (this._Q == null) {
        this.logInButton.setBounds(8, this.y, this.width - 10 - 6, 30);
        this.y += 35;
      } else {
        this.logInButton.setBounds(85, this.y, this.width - 95, 30);
        this.y += 60;
      }

      if (this._Q != null) {
        this._Q.setBounds(8, this.y, this.width - 16, 30);
        this.y += 35;
      }

      if (this._L || this.isInitialLogin) {
        this.cancelButton.setBounds(8, this.y, this.width - 10 - 6, 30);
        this.y += 35;
      } else {
        this.cancelButton.setBounds(8, this.y, 40, 20);
        this.y += 25;
      }

      this.setBounds(0, 0, this.width, this.y + 3);
      this.addChild(this.logInButton);
      if (this._Q != null) {
        this.addChild(this._Q);
      }

      this.addChild(this.cancelButton);

    } else {
      throw new IllegalStateException();
    }
  }

  public static void a667ce(final String var0) {
    if (CommonUI._jiG != null) {
      CommonUI._jiG.i423();
    }
    _noe = new LoginForm(var0, false, true, true);
    CommonUI._aef.b952(_noe);
  }

  public static void a487la() {
    a667ce("");
  }

  public void a984(final String var2) {
    this.usernameField.a676(var2, false);
    this.passwordField.e487();
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
  public void handleTextFieldChanged() {
  }

  private void submit() {
    if (CommonUI._nlb || (this.usernameField.text.length() > 0 && this.passwordField.text.length() > 0)) {
      CreateAccountPage.a584ai(this.usernameField.text, this.passwordField.text, false);
    }
  }

  @Override
  public void draw(final int x, final int y) {
    if (this._J != null) {
      Resources.AREZZO_14.drawParagraph(this._J, 20 + x + this.x, 15 + this.y + y, this.width - 40, this.height, Drawing.WHITE, Font.HorizontalAlignment.CENTER, Font.VerticalAlignment.TOP, Resources.AREZZO_14.ascent);
    }

    if (this._Q != null) {
      Drawing.horizontalLine(x + 10, 134 + y, this.width - 20, 4210752);
    }

    super.draw(x, y);
  }

  @Override
  public void handleTextFieldEnterPressed(final AbstractTextField textField) {
    if (textField == this.usernameField) {
      this.passwordField.focus(this);
    }
    if (textField == this.passwordField) {
      this.submit();
    }
  }

  @Override
  public void handleButtonClicked(final Button button) {
    if (this.logInButton == button) {
      this.submit();
    } else if (this._Q == button) {
      CreateAccountForm.a423tl();
    } else if (this.cancelButton == button) {
      if (this._L) {
        CommonUI._crb = CommonUI.TickResult.QUIT_TO_WEBSITE;
      } else if (this.isInitialLogin) {
        CommonUI.b423ol();
      } else {
        CommonUI._crb = CommonUI.TickResult.R4;
      }
    }
  }

  public String d791() {
    return this.usernameField.text == null ? "" : this.usernameField.text;
  }

  public void l150() {
    this.usernameField.e487();
    this.passwordField.e487();
  }
}
