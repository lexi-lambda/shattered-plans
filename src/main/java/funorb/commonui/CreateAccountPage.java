package funorb.commonui;

import funorb.commonui.form.CreateAccountForm;
import funorb.shatteredplans.client.JagexApplet;
import funorb.shatteredplans.StringConstants;

import java.util.Objects;

public final class CreateAccountPage extends AccountPage {
  private final CreateAccountForm _Db;
  private boolean _Eb;
  private boolean _Hb;

  public CreateAccountPage(final RootFrame var1, final CreateAccountForm var2) {
    super(var1, Resources.AREZZO_14_BOLD, StringConstants.CREATING_YOUR_ACCOUNT, false, false);
    this._Db = var2;
  }

  public static kj_ a431ck(final String var0, final int var1) {
    final kj_ var3 = new kj_(false);
    var3._c = var1;
    var3._g = var0;
    return var3;
  }

  public static void b150rm() {
    a584ai(CreateAccountForm.emailFieldText, CreateAccountForm.passwordFieldText, true);
    CommonUI._nsbD = true;
  }

  private static kj_ a752br() {
    if (CommonUI._eel == Enum1.C1) {
      throw new IllegalStateException();
    } else if (CommonUI._eel == Enum1.C2) {
      CommonUI._eel = Enum1.C1;
      return JagexApplet._tplc;
    } else {
      return null;
    }
  }

  public static void a584ai(final String username, final String password, final boolean var0) {
    CommonUI.enteredUsername = username;
    CommonUI.enteredPassword = password;
    CommonUI.setStateLoggingIn(StringConstants.LOGGING_IN, var0);
  }

  @Override
  public void handleButtonClicked(final Button button) {
    if (this._Eb) {
      CommonUI.switchToLogin(false, true);
    } else {
      b150rm();
      this.i423();
    }
  }

  @Override
  public void tickAnimations() {
    if (this.isActive && !this._Hb) {
      final kj_ var2 = a752br();
      if (var2 != null) {
        this.a122(false, var2);
      }
    }

    super.tickAnimations();
  }

  private void a122(final boolean var2, final kj_ var3) {
    this._Hb = true;
    String var4;
    if (var3._h) {
      var4 = StringConstants.ACCOUNT_CREATED_SUCCESSFULLY;
    } else if (var3._d == null) {
      var4 = var3._g;
      if (var3._c == 248) {
        if (!var2) {
          CreateAccountForm.accountCreationFailed();
        }

        var4 = StringConstants.CREATE_INELIGIBLE;
        this._Eb = true;
      }
    } else {
      var4 = StringConstants.CREATE_USERNAME_UNAVAILABLE;
      if (this._Db != null) {
        this._Db.a150();
      }
    }

    final cf_ var5 = new cf_(this, Resources.AREZZO_14_BOLD, var4);
    if (var3._h) {
      if (var3._b) {
        this.setNextContent(new pe_(this));
        return;
      }

      var5.addButton(StringConstants.CONT, this);
    } else {
      if (this._Eb) {
        var5.addButton(StringConstants.CONT, this);
      } else if (var3._c == 5) {
        var5.addActionButton(StringConstants.RELOAD_GAME, CommonUI.TickResult.RELOAD);
        var5.addActionButton(StringConstants.QUIT_TO_WEBSITE, CommonUI.TickResult.QUIT_TO_WEBSITE);
      } else {
        var5.addActionButton(StringConstants.BACK, null);
      }

      if (var3._c == 3) {
        var5.addActionButton(StringConstants.TO_SERVER_LIST, CommonUI.TickResult.TO_SERVER_LIST);
      } else if (var3._c == 6) {
        var5.addActionButton(StringConstants.TO_CUSTOMER_SUPPORT, CommonUI.TickResult.TO_CUSTOMER_SUPPORT);
      }
    }

    this.setNextContent(var5);
  }

  public void f487() {
    this.a122(true, Objects.requireNonNull(a431ck(StringConstants.CREATE_INELIGIBLE, 248)));
  }
}
