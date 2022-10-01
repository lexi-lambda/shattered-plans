package funorb.commonui;

import funorb.Strings;
import funorb.cache.ResourceLoader;
import funorb.commonui.form.CreateAccountForm;
import funorb.commonui.form.CreateDisplayNameForm;
import funorb.commonui.form.JustPlayForm;
import funorb.commonui.form.LoginForm;
import funorb.commonui.form.validator.ConfirmEmailValidator;
import funorb.graphics.Drawing;
import funorb.shatteredplans.StringConstants;
import funorb.shatteredplans.client.JagexApplet;
import org.intellij.lang.annotations.MagicConstant;
import org.jetbrains.annotations.Nullable;

public final class CommonUI {
  public static NavigationRoot root;
  public static AccountPage _jiG;
  public static FormNavigationPage _aef;
  private static LoadingBar loadingBar;

  public static @Nullable TickResult _crb = null;

  private static float loadingPercent;
  private static String loadingMessage;
  private static String loadingNotificationMessage = null;

  private static boolean loggingIn = false;
  public static boolean _nlb;
  public static boolean _nsbD = false;
  public static Enum1 _eel;
  public static Enum1 _fjs;

  public static String enteredUsername;
  public static String enteredPassword;
  public static boolean loadingFailed;
  public static CreateDisplayNameForm _sjb;
  public static int _tfn;

  private CommonUI() {}


  public static void load(final ResourceLoader spritesLoader, final ResourceLoader fontsLoader, final ResourceLoader dataLoader) {
    ConfirmEmailValidator._wha = new ks_("");
    ConfirmEmailValidator._wha.a540(false);
    Resources.load(spritesLoader, fontsLoader, dataLoader);
    TooltipManager.initialize(Resources.AREZZO_14);
    root = new NavigationRoot();
    switchToLogin(true, true);
    _eel = Enum1.C1;
    _fjs = Enum1.C1;
  }

  public static TickResult tick() {
    root.tickRoot(0, 0, true);
    root.tick2();

    while (JagexApplet.nextTypedKey()) {
      root.keyTyped(JagexApplet.lastTypedKeyCode, JagexApplet.lastTypedKeyChar);
    }

    if (_crb != null) {
      final TickResult tmp = _crb;
      _crb = null;
      return tmp;
    } else if (loggingIn) {
      return TickResult.LOGGING_IN;
    } else if (_fjs == Enum1.C3) {
      return TickResult.R1;
    } else if (ConfirmEmailValidator._wha.b154()) {
      return _eel == Enum1.C3 ? TickResult.R2 : null;
    } else {
      return TickResult.R1;
    }
  }

  public static String getPassword() {
    return _eel != Enum1.C3 ? enteredPassword : CreateAccountForm._G;
  }

  public static void switchToLoadingScreen() {
    if (root != null) {
      root.switchToLoadingScreen();
    }
    if (loadingBar != null) {
      loadingBar.disableAnimation();
    }
    if (TooltipManager.INSTANCE != null) {
      TooltipManager.INSTANCE.reset();
    }
  }

  public static void switchToLogin(final boolean canCreateAccount, final boolean isInitialLogin) {
    f150fe();
    root.startTransitioningOut();
    LoginForm._noe = new LoginForm(enteredUsername, _nlb, canCreateAccount, isInitialLogin);
    _aef = new FormNavigationPage(root, LoginForm._noe, 33, 20, 30);
    root.pushChild(_aef);
  }

  public static void setStateLoggingIn(final String message, final boolean var2) {
    _nsbD = var2;
    loggingIn = true;
    _jiG = new AccountPage(root, Resources.AREZZO_14_BOLD, message, _nlb, _nsbD);
    root.pushChild(_jiG);
  }

  public static void handleServerDisconnect() {
    if (JagexApplet.connectionState == JagexApplet.ConnectionState.CONNECTED) {
      f150fe();
      _nlb = true;
      root.startTransitioningOut();
      setStateLoggingIn(StringConstants.CONNECTION_LOST_RECONNECTING, false);
      JagexApplet.connectionState = JagexApplet.ConnectionState.RECONNECTING;
    }
    loadingFailed = true;
  }

  private static void f150fe() {
    loggingIn = false;
    _nlb = false;
    _crb = null;
    _eel = Enum1.C1;
    _fjs = Enum1.C1;
  }

  public static void handleLoginSucceeded() {
    loggingIn = false;
    loadingNotificationMessage = null;
    if (_nlb) {
      _jiG.p150();
    } else {
      final int var1 = _tfn;
      if (var1 > 0) {
        if (var1 == 1) {
          loadingNotificationMessage = StringConstants.TICKETING_ONE_UNREAD;
        } else {
          loadingNotificationMessage = Strings.format(StringConstants.TICKETING_X_UNREAD, Integer.toString(var1));
        }

        loadingNotificationMessage = a547lr(new CharSequence[]{loadingNotificationMessage, "<br>", StringConstants.VISIT_ACCOUNT_MANAGEMENT});
      }

      _jiG.i423();
      switchToLoading();
    }
  }

  public static void handleLoginFailed(@MagicConstant(valuesFromClass = LoginResult.class) int var0, String var1) {
    loggingIn = false;
    if (_jiG != null && _jiG.isAlive) {
      if (var0 == LoginResult.R8) {
        var0 = LoginResult.R2;
        if (_nlb) {
          var1 = StringConstants.INVALID_PASS;
        } else {
          var1 = StringConstants.INVALID_USER_OR_PASS;
        }
        LoginForm._noe.a984(enteredUsername);
      }

      boolean var2 = true;
      if (var0 == LoginResult.R10) {
        var2 = false;
        f150mm();
      }
      if (var2) {
        if (_nsbD) {
          var1 = StringConstants.PLEASE_TRY_AGAIN;
        }

        _jiG.a503(var1, var0);
      }
      if (var0 != LoginResult.R256 && var0 != LoginResult.R10 && !_nlb) {
        LoginForm._noe.l150();
      }
    }
  }

  private static void f150mm() {
    if (_jiG != null) {
      _jiG.i423();
    }
    _sjb = new CreateDisplayNameForm();
    _aef.b952(_sjb);
  }

  private static String a547lr(final CharSequence[] var0) {
    return a176hm(var0, var0.length);
  }

  private static String a176hm(final CharSequence[] var0, final int var2) {
    if (var2 == 0) {
      return "";
    } else if (var2 == 1) {
      final CharSequence var3 = var0[0];
      return var3 != null ? var3.toString() : "null";
    } else {
      int var4 = 0;

      for (int var5 = 0; var5 < var2; ++var5) {
        final CharSequence var6 = var0[var5];
        if (var6 == null) {
          var4 += 4;
        } else {
          var4 += var6.length();
        }
      }

      final StringBuilder var9 = new StringBuilder(var4);

      for (int var10 = 0; var10 < var2; ++var10) {
        final CharSequence var7 = var0[var10];
        if (var7 == null) {
          var9.append("null");
        } else {
          var9.append(var7);
        }
      }

      return var9.toString();
    }
  }

  public static void a423oo() {
    switchToLoading();
    _crb = TickResult.R4;
  }

  private static void switchToLoading() {
    root.startTransitioningOut();
    if (loadingBar == null) {
      loadingBar = new LoadingBar(root, loadingNotificationMessage);
    }
    root.pushChild(loadingBar);
  }

  public static void draw() {
    root.drawRoot(0, 0);
  }

  public static void drawLoading() {
    if (loadingBar == null) {
      loadingBar = new LoadingBar(root, loadingNotificationMessage);
      root.pushChild(loadingBar);
    }
    loadingBar.update(loadingMessage, loadingPercent, loadingFailed);
    Drawing.clear();
    draw();
  }

  public static void setLoadProgress(final float percent, final String message) {
    loadingPercent = percent;
    loadingMessage = message;
  }

  public static void b423ol() {
    _aef.b952(new JustPlayForm());
  }

  public enum TickResult {
    R1,
    R2,
    LOGGING_IN,
    R4,
    TO_SERVER_LIST,
    PLAY_FREE_VERSION,
    TO_CUSTOMER_SUPPORT,
    VIEW_MESSAGES,
    RELOAD,
    R12,
    TO_LANGUAGE_SELECT,
    RETURN_TO_GAME,
    QUIT_TO_WEBSITE,
  }

  @SuppressWarnings("WeakerAccess")
  public static final class LoginResult {
    public static final int NONE = -1;
    public static final int SUCCESS = 0;
    public static final int R1 = 1;
    public static final int R2 = 2;
    public static final int R3 = 3;
    public static final int R5 = 5;
    public static final int R8 = 8;
    public static final int R10 = 10;
    public static final int R256 = 256;
  }
}
