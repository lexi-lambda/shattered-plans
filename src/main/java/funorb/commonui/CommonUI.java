package funorb.commonui;

import funorb.Strings;
import funorb.cache.ResourceLoader;
import funorb.commonui.form.CreateAccountForm;
import funorb.commonui.form.CreateDisplayNameForm;
import funorb.commonui.form.JustPlayForm;
import funorb.commonui.form.LoginForm;
import funorb.graphics.Drawing;
import funorb.shatteredplans.StringConstants;
import funorb.shatteredplans.client.JagexApplet;
import org.intellij.lang.annotations.MagicConstant;
import org.jetbrains.annotations.Nullable;

public final class CommonUI {
  public static RootFrame root;
  public static AccountPage _jiG;
  public static FormFrame _aef;
  public static ks_ _wha;
  private static LoadingBar loadingBar;

  public static @Nullable TickResult nextTickResult = null;

  private static float loadingPercent;
  private static String loadingMessage;
  private static String loadingNotificationMessage = null;

  private static boolean loggingIn = false;
  public static boolean wasConnected;
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
    _wha = new ks_("");
    _wha.a540(false);
    Resources.load(spritesLoader, fontsLoader, dataLoader);
    TooltipManager.initialize(Resources.AREZZO_14);
    root = new RootFrame();
    switchToLogin(true, true);
    _eel = Enum1.C1;
    _fjs = Enum1.C1;
  }

  public static TickResult tick() {
    root.tickRoot(0, 0, true);
    root.tickAnimations();

    while (JagexApplet.nextTypedKey()) {
      root.keyTyped(JagexApplet.lastTypedKeyCode, JagexApplet.lastTypedKeyChar);
    }

    if (nextTickResult != null) {
      final TickResult tmp = nextTickResult;
      nextTickResult = null;
      return tmp;
    } else if (loggingIn) {
      return TickResult.LOGGING_IN;
    } else if (_fjs == Enum1.C3) {
      return TickResult.R1;
    } else if (_wha.b154()) {
      return _eel == Enum1.C3 ? TickResult.R2 : null;
    } else {
      return TickResult.R1;
    }
  }

  public static String getPassword() {
    return _eel != Enum1.C3 ? enteredPassword : CreateAccountForm.passwordFieldText;
  }

  public static void switchToLoadingScreen() {
    if (root != null) {
      root.expediteRemoval();
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
    root.popAll();
    LoginForm.instance = new LoginForm(enteredUsername, wasConnected, canCreateAccount, isInitialLogin);
    _aef = new FormFrame(root, LoginForm.instance, 33, 20, 30);
    root.pushActive(_aef);
  }

  public static void setStateLoggingIn(final String message, final boolean var2) {
    _nsbD = var2;
    loggingIn = true;
    _jiG = new AccountPage(root, Resources.AREZZO_14_BOLD, message, wasConnected, _nsbD);
    root.pushActive(_jiG);
  }

  public static void handleServerDisconnect() {
    if (JagexApplet.connectionState == JagexApplet.ConnectionState.CONNECTED) {
      f150fe();
      wasConnected = true;
      root.popAll();
      setStateLoggingIn(StringConstants.CONNECTION_LOST_RECONNECTING, false);
      JagexApplet.connectionState = JagexApplet.ConnectionState.RECONNECTING;
    }
    loadingFailed = true;
  }

  private static void f150fe() {
    loggingIn = false;
    wasConnected = false;
    nextTickResult = null;
    _eel = Enum1.C1;
    _fjs = Enum1.C1;
  }

  public static void handleLoginSucceeded() {
    loggingIn = false;
    loadingNotificationMessage = null;
    if (wasConnected) {
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
    if (_jiG != null && _jiG.isActive) {
      if (var0 == LoginResult.R8) {
        var0 = LoginResult.R2;
        if (wasConnected) {
          var1 = StringConstants.INVALID_PASS;
        } else {
          var1 = StringConstants.INVALID_USER_OR_PASS;
        }
        LoginForm.instance.a984(enteredUsername);
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

        _jiG.handleFailure(var0, var1);
      }
      if (var0 != LoginResult.PROTOCOL_ERROR && var0 != LoginResult.R10 && !wasConnected) {
        LoginForm.instance.l150();
      }
    }
  }

  private static void f150mm() {
    if (_jiG != null) {
      _jiG.i423();
    }
    _sjb = new CreateDisplayNameForm();
    _aef.setNextContent(_sjb);
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
    nextTickResult = TickResult.R4;
  }

  private static void switchToLoading() {
    root.popAll();
    if (loadingBar == null) {
      loadingBar = new LoadingBar(root, loadingNotificationMessage);
    }
    root.pushActive(loadingBar);
  }

  public static void draw() {
    root.drawRoot(0, 0);
  }

  public static void drawLoading() {
    if (loadingBar == null) {
      loadingBar = new LoadingBar(root, loadingNotificationMessage);
      root.pushActive(loadingBar);
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
    _aef.setNextContent(new JustPlayForm());
  }

  public static ks_ a661os(final String var1) {
    if (_wha.b154() && !var1.equals(_wha.getLabel())) {
      _wha = new ks_(var1);
    }
    return _wha;
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
    public static final int CONNECTION_LOST = 3;
    public static final int PLAY_FREE_VERSION = 4;
    public static final int GAME_UPDATED = 5;
    public static final int SEE_CUSTOMER_SUPPORT = 6;
    public static final int R8 = 8;
    public static final int CHANGE_DISPLAY_NAME = 9;
    public static final int R10 = 10;
    public static final int PROTOCOL_ERROR = 256;
  }
}
