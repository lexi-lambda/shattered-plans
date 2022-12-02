package funorb.commonui;

import funorb.awt.KeyState;
import funorb.commonui.form.CreateAccountForm;
import funorb.commonui.form.LoginForm;
import funorb.commonui.listener.ButtonListener;
import funorb.graphics.Font;
import funorb.shatteredplans.client.JagexApplet;
import funorb.client.JagexBaseApplet;
import funorb.shatteredplans.StringConstants;
import org.intellij.lang.annotations.MagicConstant;

public class AccountPage extends FormNavigationPage implements ButtonListener {
  private final boolean isLoginPage;
  private final ProgressBar progressBar;
  private final boolean canRetryOnFailure;
  private final Font _ub;
  private boolean wasProtocolError = false;
  private boolean hasFailed = false;

  public AccountPage(final NavigationRoot var1, final Font var2, final String var3, final boolean var4, final boolean var5) {
    super(var1, new cf_(null, var2, var3), 77, 10, 10);
    this.canRetryOnFailure = var4;
    this.isLoginPage = var5;
    this._ub = var2;
    this.progressBar = new ProgressBar();
    this.progressBar.animateStripes = true;
    this.addChild(this.progressBar);
  }

  private static void a150nb() {
    if (!CommonUI.wasConnected) {
      throw new IllegalStateException();
    }

    if (CommonUI._jiG != null) {
      CommonUI._jiG.i423();
    }

    final String var1 = l738w();
    LoginForm.instance = new LoginForm(var1, true, false, false);
    CommonUI.root.pushChild(CommonUI._aef);
    CommonUI._aef.setNextContent(LoginForm.instance);
    CommonUI._aef.skipAnimations();
  }

  private static String l738w() {
    String var1 = "";
    if (LoginForm.instance != null) {
      var1 = LoginForm.instance.d791();
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
    this.progressBar.setColor(ProgressBar.SUCCESS_COLOR);
    final cf_ var2 = new cf_(this, this._ub, StringConstants.CONNECTION_RESTORED);
    var2.addActionButton(StringConstants.RETURN_TO_GAME, CommonUI.TickResult.RETURN_TO_GAME);
    this.setNextContent(var2);
  }

  public final void i423() {
    if (this.isAlive) {
      this.isAlive = false;
      if (this.canRetryOnFailure) {
        a150nb();
      } else if (this.isLoginPage) {
        LoginForm.a667ce(CreateAccountForm.emailFieldText);
      }
    }
  }

  @Override
  public void handleButtonClicked(final Button button) {
    if (this.wasProtocolError) {
      CommonUI.nextTickResult = CommonUI.TickResult.LOGGING_IN;
      this.i423();
    } else {
      JagexApplet.a808bp("tochangedisplayname.ws", JagexBaseApplet.getInstance());
    }
  }

  @Override
  public final boolean keyTyped(final int keyCode, final char keyChar, final Component focusRoot) {
    if (keyCode == KeyState.Code.ESCAPE) {
      this.i423();
      return true;
    } else {
      return super.keyTyped(keyCode, keyChar, focusRoot);
    }
  }

  public final void handleFailure(@MagicConstant(valuesFromClass = CommonUI.LoginResult.class) final int result, final String reasonMessage) {
    if (this.hasFailed) return;
    this.hasFailed = true;
    this.wasProtocolError = result == CommonUI.LoginResult.PROTOCOL_ERROR;
    this.progressBar.setColor(ProgressBar.FAILURE_COLOR);

    final cf_ var4 = new cf_(this, this._ub, reasonMessage);
    if (result == CommonUI.LoginResult.GAME_UPDATED) {
      var4.addActionButton(StringConstants.RELOAD_GAME, CommonUI.TickResult.RELOAD);
      var4.addActionButton(StringConstants.QUIT_TO_WEBSITE, CommonUI.TickResult.QUIT_TO_WEBSITE);
    } else if (result == CommonUI.LoginResult.PROTOCOL_ERROR) {
      var4.addButton(StringConstants.RETRY, this);
    } else {
      var4.addActionButton(this.canRetryOnFailure ? StringConstants.RETRY : StringConstants.BACK, null);
    }

    if (result == CommonUI.LoginResult.CONNECTION_LOST) {
      var4.addActionButton(StringConstants.TO_SERVER_LIST, CommonUI.TickResult.TO_SERVER_LIST);
    } else if (result == CommonUI.LoginResult.PLAY_FREE_VERSION) {
      var4.addActionButton(StringConstants.PLAY_FREE_VERSION, CommonUI.TickResult.PLAY_FREE_VERSION);
    } else if (result == CommonUI.LoginResult.SEE_CUSTOMER_SUPPORT) {
      var4.addActionButton(StringConstants.TO_CUSTOMER_SUPPORT, CommonUI.TickResult.TO_CUSTOMER_SUPPORT);
    } else if (result == CommonUI.LoginResult.CHANGE_DISPLAY_NAME) {
      var4.addButton(StringConstants.CHANGE_DISPLAY_NAME, this);
    }

    this.setNextContent(var4);
  }
}
