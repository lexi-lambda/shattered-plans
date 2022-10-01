package funorb.commonui.form;

import funorb.Strings;
import funorb.awt.KeyState;
import funorb.client.JagexBaseApplet;
import funorb.commonui.Button;
import funorb.commonui.Checkbox;
import funorb.commonui.CommonUI;
import funorb.commonui.Component;
import funorb.commonui.Enum1;
import funorb.commonui.container.ListContainer;
import funorb.commonui.Resources;
import funorb.commonui.ah_;
import funorb.commonui.form.field.InputField;
import funorb.commonui.form.field.TextField;
import funorb.commonui.form.validator.AgeValidator;
import funorb.commonui.form.validator.ConfirmEmailValidator;
import funorb.commonui.form.validator.ConfirmPasswordValidator;
import funorb.commonui.form.validator.EmailValidator;
import funorb.commonui.form.validator.InputValidator;
import funorb.commonui.form.validator.PasswordValidator;
import funorb.commonui.form.validator.UsernameValidator;
import funorb.commonui.form.validator.ValidationState;
import funorb.commonui.hl_;
import funorb.commonui.listener.ButtonListener;
import funorb.commonui.CreateAccountPage;
import funorb.commonui.op_;
import funorb.commonui.pg_;
import funorb.commonui.renderer.LinkRenderer;
import funorb.commonui.renderer.PasswordFieldRenderer;
import funorb.commonui.renderer.TextRenderer;
import funorb.commonui.ts_;
import funorb.graphics.Drawing;
import funorb.graphics.Font;
import funorb.shatteredplans.StringConstants;
import funorb.shatteredplans.client.JagexApplet;

import java.applet.Applet;

public final class CreateAccountForm extends ListContainer implements op_, ButtonListener, CreateForm {
  public static String _G;
  public static String _cgF = null;
  public static CreateAccountForm _anb;
  private static boolean accountCreationFailed;

  public final CreateFormListener _D;
  private final Button backButton = new Button(StringConstants.GO_BACK, new LinkRenderer(), null);
  private final Button createButton = new Button(StringConstants.CREATE, null);
  private final TextField usernameField = new TextField("", null, 12);
  private final TextField emailField = new TextField("", null, 100);
  private final TextField confirmEmailField = new TextField("", null, 100);
  private final TextField passwordField = new TextField("", new PasswordFieldRenderer(), null, 20);
  private final TextField confirmPasswordField = new TextField("", new PasswordFieldRenderer(), null, 20);
  private final TextField ageField = new TextField("", null, 3);
  private final Checkbox optInCheckbox = new Checkbox(true);

  private CreateAccountForm() {
    super(0, 0, 496, 0);
    this.usernameField.tooltip = StringConstants.CREATE_DISPLAYNAME_TOOLTIP;
    this.emailField.tooltip = StringConstants.CREATE_EMAIL_TOOLTIP;
    this.confirmEmailField.tooltip = StringConstants.CREATE_EMAIL_CONFIRM_TOOLTIP;
    this.passwordField.tooltip = StringConstants.CREATE_PASSWORD_TOOLTIP;
    this.confirmPasswordField.tooltip = StringConstants.CREATE_PASSWORD_CONFIRM_TOOLTIP;
    this.ageField.tooltip = StringConstants.CREATE_AGE_TOOLTIP;
    this.optInCheckbox.tooltip = StringConstants.CREATE_OPT_IN_NEWS_TOOLTIP;
    this.usernameField.setValidator(new UsernameValidator(this.usernameField));
    this.emailField.setValidator(new EmailValidator(this.emailField));
    this.confirmEmailField.setValidator(new ConfirmEmailValidator(this.confirmEmailField, this.emailField));
    this.passwordField.setValidator(new PasswordValidator(this.passwordField, this.usernameField, this.emailField));
    this.confirmPasswordField.setValidator(new ConfirmPasswordValidator(this.confirmPasswordField, this.passwordField));
    this.ageField.setValidator(new AgeValidator(this.ageField));
    this.createButton.enabled = false;

    final String var2 = Strings.format(StringConstants.CREATE_AGREE_TERMS, "<u=2164A2><col=2164A2>", "</col></u>");
    final byte var3 = 20;
    int var8 = var3 + this.a187(StringConstants.CREATE_EMAIL, this.emailField, var3);
    var8 += 5 + this.a986(var8, this.confirmEmailField, "", StringConstants.CREATE_EMAIL_CONFIRM, 20);
    var8 += this.a187(StringConstants.CREATE_PASSWORD, this.passwordField, var8);
    var8 += 5 + this.a244(StringConstants.CREATE_PASSWORD_HINT, var8, (byte) -127, this.confirmPasswordField, StringConstants.CREATE_PASSWORD_CONFIRM);
    var8 += this.a244(StringConstants.CREATE_DIPLAY_NAME_HINT, var8, (byte) -128, this.usernameField, StringConstants.CREATE_DISPLAY_NAME) + 5;
    var8 += this.a840(StringConstants.CREATE_AGE, this.ageField, var8);
    final pg_ var4 = new pg_(46, var8, this.width - 90, this.optInCheckbox, true, this.width - 120, 5, Resources.AREZZO_12, 11579568, StringConstants.CREATE_OPT_IN_NEWS);
    this.addChild(var4);
    var8 += var4.height;
    final TextRenderer var5 = new TextRenderer(Resources.AREZZO_14, 0, 0, 0, 0, Drawing.WHITE, Font.HorizontalAlignment.LEFT, Font.VerticalAlignment.TOP, Resources.AREZZO_14.ascent, true);
    final ts_ _I = new ts_(var2, var5);
    _I.tooltip = "";
    _I.a096(0, StringConstants.OPEN_IN_POPUP_WINDOW);
    _I.a096(1, StringConstants.OPEN_IN_POPUP_WINDOW);
    _I.listener = this;
    _I.a652(46, var8, this.width - 90);
    _I.pack();
    var8 += 15 + _I.height;
    this.addChild(_I);
    final byte var6 = 4;
    final short var7 = 200;
    this.createButton.setBounds(148, var8, var7, 40);
    this.backButton.setBounds(7, 15 + var8, 60, 40);
    this.backButton.listener = this;
    this.createButton.listener = this;
    this.addChild(this.createButton);
    this.addChild(this.backButton);
    this._D = new CreateFormListener(this);
    this._D.setBounds(60 + this.usernameField.x + this.usernameField.width, this.usernameField.y + 20, -this.usernameField.x + this.width - 60 - this.usernameField.width, 150);
    this.addChild(this._D);
    this.setBounds(0, 0, 496, var8 + 55 + var6);
  }

  public static void a984gm(final String var1) {
    _cgF = var1;
    CommonUI._crb = CommonUI.TickResult.R12;
  }

  private static boolean a591js(final Applet var0) {
    if (accountCreationFailed) {
      return true;
    } else {
      return var0.getParameter("tuhstatbut") != null;
    }
  }

  public static void accountCreationFailed() {
    accountCreationFailed = true;
  }

  public static void a423tl() {
    _anb = new CreateAccountForm();
    CommonUI._aef.b952(_anb);
  }

  @Override
  public void a746(final int var2) {
    if (var2 == 0) {
      a984gm("terms.ws");
    } else if (var2 == 1) {
      a984gm("privacy.ws");
    } else if (var2 == 2) {
      a984gm("conduct.ws");
    }
  }

  private int a986(final int var3, final Component var4, final String var5, final String var6, final int var7) {
    final pg_ var8 = new pg_(20, var3, 290, var4, false, 120, 3, Resources.AREZZO_14, Drawing.WHITE, var6);
    this.addChild(var8);
    final ah_ var9 = new ah_(((InputField) var4).getValidator(), var5, 126, var3 + var8.height, 220, var7);
    var9.listener = this;
    this.addChild(var9);
    return var9.height + var8.height;
  }

  private boolean a668(final InputField var2) {
    final InputValidator var3 = var2.getValidator();
    if (var3 == null) {
      return true;
    } else {
      final ValidationState var4 = var3.validate();

      if (var4 == ValidationState.INVALID) {
        return false;
      } else if (var4 == ValidationState.CHECKING_2) {
        return false;
      } else {
        return var4 != ValidationState.C5;
      }
    }
  }

  private int a187(final String var1, final Component var3, final int var4) {
    final pg_ var6 = new pg_(20, var4, 290, var3, false, 120, 3, Resources.AREZZO_14, Drawing.WHITE, var1);

    this.addChild(var6);
    return var6.height;
  }

  @Override
  public void a984(final String var2) {
    this.usernameField.a676(var2, false);

  }

  private int a244(final String var2, final int var3, final byte var4, final Component var5, final String var6) {
    return var4 >= -126 ? -57 : this.a986(var3, var5, var2, var6, 35);
  }

  @Override
  public void a150() {
    ((UsernameValidator) this.usernameField.getValidator()).d150();
  }

  @Override
  public void tick(final int x, final int y, final Component root) {
    super.tick(x, y, root);
    this.createButton.enabled = this.m154();
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

  private int a840(final String var1, final Component var2, final int var3) {
    final pg_ var6 = new pg_(20, var3, 290, var2, false, 120, 3, Resources.AREZZO_14, Drawing.WHITE, var1);
    this.addChild(var6);
    final hl_ var7 = new hl_(((InputField) var2).getValidator());
    this.addChild(var7);
    var7.setBounds(var6.width + var6.x + 3, var6.y + (var6.height - 15 >> 1), 15, 15);
    return var6.height;
  }

  private boolean m154() {
    return this.a668(this.usernameField) && this.a668(this.emailField) && this.a668(this.confirmEmailField) && this.a668(this.passwordField) && this.a668(this.confirmPasswordField) && this.a668(this.ageField);
  }

  @Override
  public void handleButtonClicked(final Button button) {
    if (this.backButton == button) {
      CommonUI.b423ol();
    } else if (button == this.createButton) {
      if (this.m154()) {
        int var2 = -1;

        try {
          var2 = Integer.parseInt(this.ageField.text);
        } catch (final NumberFormatException var4) {
        }

        if (CommonUI._eel == Enum1.C1) {
          final CreateAccountPage var6 = new CreateAccountPage(CommonUI.root, this);
          CommonUI.root.pushChild(var6);
          if (a591js(JagexBaseApplet.getInstance())) {
            var6.f487();
          } else {
            CommonUI._eel = Enum1.C3;
            JagexApplet._npm = this.optInCheckbox.active;
            _G = this.passwordField.text;
            JagexApplet._umj = this.usernameField.text;
            JagexApplet._tplc = null;
            JagexApplet._aeg = this.emailField.text;
            JagexApplet._jmt = var2;
          }
        }
      }
    }
  }
}
