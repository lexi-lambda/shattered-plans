package funorb.commonui;

import funorb.awt.KeyState;
import funorb.commonui.form.LoginForm;
import funorb.commonui.listener.ButtonListener;
import funorb.graphics.Font;
import funorb.shatteredplans.client.JagexApplet;
import funorb.client.JagexBaseApplet;
import funorb.shatteredplans.StringConstants;

public class AccountPage extends FormNavigationPage implements ButtonListener {
  private final boolean _vb;
  private final ProgressBar _yb;
  private final boolean _tb;
  private final Font _ub;
  private boolean _sb = false;
  private boolean _wb;

  public AccountPage(final NavigationRoot var1, final Font var2, final String var3, final boolean var4, final boolean var5) {
    super(var1, new cf_(null, var2, var3), 77, 10, 10);
    this._tb = var4;
    this._vb = var5;
    this._wb = false;
    this._ub = var2;
    this._yb = new ProgressBar();
    this._yb.animateStripes = true;
    this.addChild(this._yb);
  }

  private static void a150nb() {
    if (CommonUI._nlb) {
      if (CommonUI._jiG != null) {
        CommonUI._jiG.i423();
      }

      final String var1 = l738w();
      LoginForm._noe = new LoginForm(var1, true, false, false);
      CommonUI.root.pushChild(CommonUI._aef);
      CommonUI._aef.b952(LoginForm._noe);
      CommonUI._aef.n150();
    } else {
      throw new IllegalStateException();
    }
  }

  private static String l738w() {
    String var1 = "";
    if (LoginForm._noe != null) {
      var1 = LoginForm._noe.d791();
    }

    if (var1.length() == 0) {
      var1 = a738id();
    }

    if (var1.length() == 0) {
      var1 = StringConstants.DEFAULT_PLAYER_NAME;
    }

    return var1;
  }

  private static String a738id() {
    return JagexApplet.playerDisplayName == null ? "" : JagexApplet.playerDisplayName;
  }

  public final void p150() {
    this._yb.setColor(ProgressBar.SUCCESS_COLOR);
    final cf_ var2 = new cf_(this, this._ub, StringConstants.CONNECTION_RESTORED);
    var2.a966(StringConstants.RETURN_TO_GAME, CommonUI.TickResult.RETURN_TO_GAME);
    this.b952(var2);
  }

  public final void i423() {
    if (this.isAlive) {
      this.isAlive = false;
      if (this._tb) {
        a150nb();
      } else if (this._vb) {
        LoginForm.a667ce(JagexApplet._aeg);
      }
    }
  }

  @Override
  public void handleButtonClicked(final Button button) {
    if (this._sb) {
      CommonUI._crb = CommonUI.TickResult.LOGGING_IN;
      this.i423();
    } else {
      JagexApplet.a808bp("tochangedisplayname.ws", JagexBaseApplet.getInstance());
    }
  }

  @Override
  public final boolean a686(final int keyCode, final char keyChar, final Component var4) {
    if (keyCode == KeyState.Code.ESCAPE) {
      this.i423();
      return true;
    } else {
      return super.a686(keyCode, keyChar, var4);
    }
  }

  public final void a503(final String var1, final int var2) {
    if (!this._wb) {
      this._sb = var2 == 256;
      this._wb = true;
      this._yb.setColor(ProgressBar.FAILURE_COLOR);
      final cf_ var4 = new cf_(this, this._ub, var1);
      if (var2 == 5) {
        var4.a966(StringConstants.RELOAD_GAME, CommonUI.TickResult.RELOAD);
        var4.a966(StringConstants.QUIT_TO_WEBSITE, CommonUI.TickResult.QUIT_TO_WEBSITE);
      } else if (var2 == 256) {
        var4.a700(this, StringConstants.RETRY);
      } else {
        var4.a966(this._tb ? StringConstants.RETRY : StringConstants.BACK, null);
      }

      if (var2 == 3) {
        var4.a966(StringConstants.TO_SERVER_LIST, CommonUI.TickResult.TO_SERVER_LIST);
      } else if (var2 == 4) {
        var4.a966(StringConstants.PLAY_FREE_VERSION, CommonUI.TickResult.PLAY_FREE_VERSION);
      } else if (var2 == 6) {
        var4.a966(StringConstants.TO_CUSTOMER_SUPPORT, CommonUI.TickResult.TO_CUSTOMER_SUPPORT);
      } else if (var2 == 9) {
        var4.a700(this, StringConstants.CHANGE_DISPLAY_NAME);
      }

      this.b952(var4);
    }
  }
}
