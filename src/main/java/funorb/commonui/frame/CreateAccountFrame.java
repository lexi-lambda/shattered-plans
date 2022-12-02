package funorb.commonui.frame;

import funorb.commonui.Button;
import funorb.commonui.CommonUI;
import funorb.commonui.Enum1;
import funorb.commonui.Resources;
import funorb.commonui.form.CreateAccountForm;
import funorb.commonui.form.DialogForm;
import funorb.commonui.form.Under13DialogForm;
import funorb.commonui.kj_;
import funorb.shatteredplans.StringConstants;
import funorb.shatteredplans.client.JagexApplet;

public final class CreateAccountFrame extends AccountFrame {
  private final CreateAccountForm form;
  private boolean _Eb;
  private boolean _Hb;

  public CreateAccountFrame(final RootFrame root, final CreateAccountForm form) {
    super(root, Resources.AREZZO_14_BOLD, StringConstants.CREATING_YOUR_ACCOUNT, false, false);
    this.form = form;
  }

  public static void b150rm() {
    CommonUI.a584ai(CreateAccountForm.emailFieldText, CreateAccountForm.passwordFieldText, true);
    CommonUI.loggingInFromCreateAccount = true;
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

  private void a122(final boolean var2, final kj_ response) {
    this._Hb = true;

    String message;
    if (response.success) {
      message = StringConstants.ACCOUNT_CREATED_SUCCESSFULLY;
    } else if (response.suggestedUsernames == null) {
      message = response.errorMessage;
      if (response.code == kj_.Code.INELIGIBLE) {
        if (!var2) {
          CreateAccountForm.accountCreationFailed();
        }

        message = StringConstants.CREATE_INELIGIBLE;
        this._Eb = true;
      }
    } else {
      message = StringConstants.CREATE_USERNAME_UNAVAILABLE;
      if (this.form != null) {
        this.form.a150();
      }
    }

    final DialogForm dialog = new DialogForm(this, Resources.AREZZO_14_BOLD, message);
    if (response.success) {
      if (response.under13) {
        this.setNextContent(new Under13DialogForm(this));
        return;
      }

      dialog.addButton(StringConstants.CONT, this);
    } else {
      if (this._Eb) {
        dialog.addButton(StringConstants.CONT, this);
      } else if (response.code == kj_.Code.C5) {
        dialog.addActionButton(StringConstants.RELOAD_GAME, CommonUI.TickResult.RELOAD);
        dialog.addActionButton(StringConstants.QUIT_TO_WEBSITE, CommonUI.TickResult.QUIT_TO_WEBSITE);
      } else {
        dialog.addActionButton(StringConstants.BACK, null);
      }

      if (response.code == kj_.Code.C3) {
        dialog.addActionButton(StringConstants.TO_SERVER_LIST, CommonUI.TickResult.TO_SERVER_LIST);
      } else if (response.code == kj_.Code.C6) {
        dialog.addActionButton(StringConstants.TO_CUSTOMER_SUPPORT, CommonUI.TickResult.TO_CUSTOMER_SUPPORT);
      }
    }

    this.setNextContent(dialog);
  }

  public void f487() {
    this.a122(true, kj_.a431ck(kj_.Code.INELIGIBLE, StringConstants.CREATE_INELIGIBLE));
  }
}
