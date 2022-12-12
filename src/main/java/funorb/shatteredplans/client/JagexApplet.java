package funorb.shatteredplans.client;

import funorb.Strings;
import funorb.awt.KeyState;
import funorb.awt.MouseState;
import funorb.awt.MouseWheelState;
import funorb.cache.BufferedCacheFile;
import funorb.cache.CacheWorker;
import funorb.cache.MasterIndexLoader;
import funorb.cache.PageSource;
import funorb.cache.RemotePageSource;
import funorb.cache.ResourceLoader;
import funorb.client.AchievementRequest;
import funorb.client.EmailLoginCredentials;
import funorb.client.GetProfileRequest;
import funorb.client.JagexBaseApplet;
import funorb.client.LoginCredentials;
import funorb.client.LoginState;
import funorb.client.ReflectionRequest;
import funorb.client.SetProfileRequest;
import funorb.client.UserIdLoginCredentials;
import funorb.client.e_;
import funorb.client.DisplayMode;
import funorb.client.intro.JagexLogoIntroAnimation;
import funorb.client.lobby.ChatMessage;
import funorb.client.lobby.ContextMenu;
import funorb.client.lobby.PlayerListEntry;
import funorb.client.lobby.QuickChatResponse;
import funorb.client.lobby.QuickChatResponses;
import funorb.client.r_;
import funorb.commonui.CommonUI;
import funorb.commonui.Enum1;
import funorb.commonui.form.CreateAccountForm;
import funorb.commonui.form.CreateDisplayNameForm;
import funorb.commonui.form.DobToEnableChatForm;
import funorb.commonui.form.validator.UsernameValidator;
import funorb.commonui.AccountResponse;
import funorb.io.Buffer;
import funorb.io.CipheredBuffer;
import funorb.io.DuplexStream;
import funorb.io.HuffmanCoder;
import funorb.io.PacketLengthType;
import funorb.shatteredplans.C2SPacket;
import funorb.shatteredplans.S2CPacket;
import funorb.shatteredplans.StringConstants;
import funorb.util.IsaacCipher;
import funorb.util.PseudoMonotonicClock;
import org.intellij.lang.annotations.MagicConstant;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.applet.Applet;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InvalidClassException;
import java.io.ObjectInputStream;
import java.io.OptionalDataException;
import java.io.PrintWriter;
import java.io.StreamCorruptedException;
import java.io.StringReader;
import java.io.StringWriter;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.Socket;
import java.net.URL;
import java.security.SecureRandom;
import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Optional;
import java.util.Queue;
import java.util.Random;
import java.util.stream.LongStream;

public abstract class JagexApplet extends JagexBaseApplet {
  private static final Queue<AchievementRequest> achievementRequests = new ArrayDeque<>();
  protected static final Queue<SetProfileRequest> profileSetRequests = new ArrayDeque<>();
  private static final int UPDATE = 15;
  private static final Color LOADING_SCREEN_PURPLE = new Color(0x9933ff);
  public static boolean DEBUG_MODE = false;
  public static final int SERVER_TIMEOUT_MILLIS = 30_000;
  public static final Random cipherIVGen = new Random();
  public static final Buffer loginPacket = new Buffer(256);
  protected static final Queue<GetProfileRequest> getProfileResponses = new ArrayDeque<>();
  public static final SecureRandom secureRandom = new SecureRandom();
  public static AccountResponse _tplc;
  public static boolean isAnonymous = true;
  protected static boolean _vmNb;
  @MagicConstant(valuesFromClass = KeyState.Code.class)
  public static int lastTypedKeyCode;
  public static char lastTypedKeyChar;
  private static int _ffu;
  private static Image _rma;
  private static int _igd;
  private static int _npj;
  protected static int[] _uof;
  protected static RemotePageSource pageSource;
  private static int[] _gsc;
  private static long _coo;
  public static String lastLoginPassword;
  public static int membershipLevel;
  private static Applet _eic;
  public static boolean canOnlyQuickChat;
  private static int[] _clrzb;
  private static r_ _vag;
  public static int adminLevel;
  private static MasterIndexLoader masterIndexLoader;
  public static int mouseY = 0;
  @MagicConstant(valuesFromClass = MouseState.Button.class)
  public static int mouseButtonJustClicked = 0;
  public static boolean mouseEventReceived = false;
  protected static int nextS2cPacketLen;
  public static QuickChatResponses _dhc;
  private static int[] _red;
  private static boolean[] _aqp;
  private static MailboxMessage socketMessage2;
  private static long _amCb;
  private static DuplexStream serverConnection2;
  private static long lastC2sMessageTimestamp;
  private static Boolean _sad;
  private static long serverCipherIV;
  private static int[] _fy;
  private static int _efa;
  private static final int IDLE_TICKS = 60_000;
  public static final boolean[] keysDown = new boolean[112];
  private static final int[] cipherIV = new int[4];
  private static final char[] _pdkf = new char[]{'_', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9'};
  private static final String[] CONNECTING_TO_UPDATE_SERVER = new String[]{"Connecting to update server", "Verbinde mit Aktualisierungsserver", "Connexion au serveur de mise à jour", "Conectando ao servidor de atualização", "Met updateserver verbinden", "Connecting to update server (untranslated)"};
  private static final String[] LOADING_TEXT = new String[]{"Loading text", "Lade Text", "Chargement du texte", "Carregando textos", "Tekst laden", "Cargando texto"};
  private static final String[] WAITING_FOR_TEXT = new String[]{"Waiting for text", "Warte auf Text", "En attente du texte", "Aguardando textos", "Op tekst wachten", "Esperando a texto"};
  private static final String[] _nla = new String[255];
  protected static String connectingToUpdateServerMessage;
  protected static String loadingTextMessage;
  protected static String waitingForTextMessage;
  public static String playerDisplayName;
  protected static final CipheredBuffer s2cPacket = new CipheredBuffer();
  private static String normalizedPlayerName;
  protected static @NotNull ChatMessage.Channel _D = ChatMessage.Channel.LOBBY;
  @MagicConstant(valuesFromClass = MouseState.Button.class)
  public static int mouseButtonDown = 0;
  public static int mousePressX = 0;
  public static int mousePressY = 0;
  public static int modLevel;
  private static int _feJ;
  private static int _se;
  private static int _wmc;
  private static int _usb;
  private static LoginCredentials loginCredentials = null;
  private static BufferedCacheFile _vca;
  private static boolean _kej;
  private static long lastRecievedDataFromServer;
  private static int[] _lgd;
  public static int langId;
  private static String sessionId;
  private static String _cju;
  private static String[] _aee;
  private static int _gpc;
  public static int mouseX = 0;
  protected static LoginState loginState;
  @MagicConstant(valuesFromClass = ConnectionState.class)
  public static int connectionState;
  private static int[] _ajd;
  public static int mouseWheelRotation;
  private static java.awt.Font loadingScreenFont;
  private int gamePort1Primary;
  private int gamePort1Secondary;
  private static MailboxMessage socketMessage1;
  protected static DuplexStream serverConnection1;
  private int gamePort2Primary;
  private int gamePort2Secondary;
  private static String accountErrorMessage;
  public static boolean cannotChat = true;
  protected static long _bqe;
  private static boolean _mdB;
  protected static long _ipb;
  private static @NotNull JagexApplet.LoadingStatus _qjg = LoadingStatus.OK_COMPLETE;
  @MagicConstant(valuesFromClass = LoadStage.class)
  protected static int loadStage;
  private static long userId;
  private static int reinitializationCount = 0;
  private static boolean isSimpleModeEnabled;
  private static int _naL;
  private static int _gbi;
  private boolean isMember;
  private int gamePort1;
  private int gamePort2;
  private String codeHost;
  private long instanceId;
  private int affId;
  private boolean isJagexHost;
  private int serverId;

  public static void printDebug(final String message) {
    if (DEBUG_MODE) {
      System.out.println(message);
    }
  }

  protected JagexApplet() {
  }

  private static void initializeLoadingMessages(final int langId) {
    connectingToUpdateServerMessage = CONNECTING_TO_UPDATE_SERVER[langId];
    waitingForTextMessage = WAITING_FOR_TEXT[langId];
    loadingTextMessage = LOADING_TEXT[langId];
  }

  private static void a681nc(final ChatMessage message) {
    if (message._l != 0 || message._e != 0) {
      if (Arrays.stream(ShatteredPlansClient.chatMessages, 0, ShatteredPlansClient.chatMessageCount)
          .anyMatch(existing -> existing.channel == ChatMessage.Channel.PRIVATE
              && message._l == existing._l
              && message._e == existing._e)) {
        return;
      }
    }

    if (message._f != null) {
      ShatteredPlansClient._cvco = message.senderName;
      _bqe = message.senderId;
      _uof = message._f;
      _D = message.channel;
    }

    ContextMenu.showChatMessage(message);
  }

  private static void setPlayerDisplayName(final String newName) {
    playerDisplayName = newName;
    normalizedPlayerName = Strings.normalize(playerDisplayName);
  }

  protected static Frame createFullScreenFrame() {
    int bitDepth = 0;
    final DisplayMode[] modes = listDisplayModes();

    DisplayMode foundMode = null;
    for (final DisplayMode mode : modes) {
      if (mode.width == ShatteredPlansClient.SCREEN_WIDTH
          && mode.height == ShatteredPlansClient.SCREEN_HEIGHT
          && (foundMode == null || foundMode.bitDepth < mode.bitDepth)) {
        foundMode = mode;
      }
    }

    if (foundMode == null) {
      return null;
    }

    final MailboxMessage message = MessagePumpThread.instance.sendEnterFullScreenMessage(
      foundMode.width,
      foundMode.height,
      foundMode.bitDepth
    );
    message.busyAwait();

    final Frame frame = (Frame) message.response;
    if (frame == null) {
      return null;
    } else if (message.status == MailboxMessage.Status.FAILURE) {
      repeatedlyTryToExitFullScreen(frame);
      return null;
    } else {
      return frame;
    }
  }

  private static void processMouseState() {
    synchronized (MouseState.instance) {
      ++MouseState.ticksSinceLastMouseEvent;

      mouseEventReceived = MouseState.mouseEventReceived;
      MouseState.mouseEventReceived = false;

      mouseButtonDown = MouseState.mouseButtonDown;
      mouseX = MouseState.mouseX;
      mouseY = MouseState.mouseY;

      mouseButtonJustClicked = MouseState.mouseButtonJustClicked;
      mousePressX = MouseState.pressX;
      mousePressY = MouseState.pressY;
      MouseState.mouseButtonJustClicked = MouseState.Button.NONE;
    }
  }

  private static String getUsernameOrEmail() {
    if (CommonUI._eel == Enum1.C3) {
      return CreateAccountForm.usernameFieldText;
    } else if (CommonUI._fjs == Enum1.C3) {
      return UsernameValidator._gpb;
    } else {
      return CommonUI._wha.b154() ? CommonUI.enteredUsername : UsernameValidator._gpb;
    }
  }

  private static void processKeyState() {
    synchronized (KeyState.instance) {
      ++KeyState.ticksSinceLastKeyEvent;
      KeyState.keyTypeQueueFront = _feJ;
      if (KeyState.keyPressQueueBack < 0) {
        // we couldn't keep up! reset all the state
        Arrays.fill(keysDown, false);
        KeyState.keyPressQueueBack = KeyState.keyPressQueueFront;
      } else {
        while (KeyState.keyPressQueueFront != KeyState.keyPressQueueBack) {
          final int var1 = KeyState.keyPressQueue[KeyState.keyPressQueueFront];
          KeyState.keyPressQueueFront = (KeyState.keyPressQueueFront + 1) & 127;
          if (var1 >= 0) {
            keysDown[var1] = true;
          } else {
            keysDown[~var1] = false;
          }
        }
      }

      _feJ = KeyState.keyTypeQueueBack;
    }
  }

  public static void clientError(final Throwable var1, final String var2) {
    try {
      String var3 = "";
      if (var1 != null) {
        var3 = a900es(var1);
      }

      if (var2 != null) {
        if (var1 != null) {
          var3 = var3 + " | ";
        }

        var3 = var3 + var2;
      }

      System.out.println("Error: " + ChatMessage.a651("%0a", var3, "\n"));
      var3 = ChatMessage.a651(":", var3, "%3a");
      var3 = ChatMessage.a651("@", var3, "%40");
      var3 = ChatMessage.a651("&", var3, "%26");
      var3 = ChatMessage.a651("#", var3, "%23");
      if (_eic == null) {
        return;
      }

      final MailboxMessage var4 = MessagePumpThread.instance.sendOpenUrlStreamMessage(new URL(_eic.getCodeBase(), "clienterror.ws?c=" + getInstance().gameCrc + "&u=" + "" + _coo + "&v1=" + MessagePumpThread.JAVA_VENDOR + "&v2=" + MessagePumpThread.JAVA_VERSION + "&e=" + var3));

      while (var4.status == MailboxMessage.Status.PENDING) {
        JagexBaseApplet.maybeSleep(1L);
      }

      if (var4.status == MailboxMessage.Status.SUCCESS) {
        final DataInputStream var5 = (DataInputStream) var4.response;
        //noinspection ResultOfMethodCallIgnored
        var5.read();
        var5.close();
      }
    } catch (final Exception var6) {
    }
  }

  @SuppressWarnings("BooleanMethodIsAlwaysInverted")
  protected static boolean isConnectedAndLoaded() {
    return connectionState >= ConnectionState.CONNECTED && loadStage >= LoadStage.LOADED;
  }

  @SuppressWarnings("SameParameterValue")
  private static void handleProfilePacket(final CipheredBuffer packet) {
    final int var2 = packet.readUByte();
    if (var2 == 0) {
      Optional.ofNullable(getProfileResponses.poll()).ifPresentOrElse(var3 -> {
        final int var4 = packet.readUByte();
        final byte[] var5;
        if (var4 == 0) {
          var5 = null;
        } else {
          var5 = new byte[var4];
          packet.readBytes(var5, var4);
        }

        packet.pos += 4;
        if (!packet.f427()) {
          shutdownServerConnection();
          return;
        }

        var3.completed = true;
        var3.data = var5;
      }, JagexApplet::shutdownServerConnection);
    } else if (var2 == 1) {
      final int digest = packet.readInt();
      profileSetRequests.stream().filter(request -> request.digest == digest).findFirst()
          .ifPresentOrElse(profileSetRequests::remove, JagexApplet::shutdownServerConnection);
    } else {
      clientError(null, "A1: " + a738w());
      shutdownServerConnection();
    }
  }

  @Contract(pure = true)
  protected static String a019pd(long var0) {
    if (var0 > 0L && var0 < 6582952005840035281L) {
      if (var0 % 37L == 0L) {
        return null;
      } else {
        final int var2 = (int) LongStream.iterate(var0, var3 -> var3 != 0L, var3 -> var3 / 37L).count();

        final StringBuilder var5;
        char var9;
        for (var5 = new StringBuilder(var2); var0 != 0L; var5.append(var9)) {
          final long var7 = var0;
          var0 /= 37L;
          var9 = _pdkf[(int) (var7 - var0 * 37L)];
          if (var9 == '_') {
            final int var10 = var5.length() - 1;
            var5.setCharAt(var10, Character.toUpperCase(var5.charAt(var10)));
            var9 = 160;
          }
        }

        var5.reverse();
        var5.setCharAt(0, Character.toUpperCase(var5.charAt(0)));
        return var5.toString();
      }
    } else {
      return null;
    }
  }

  public static void repeatedlyTryToExitFullScreen(final Frame frame) {
    while (true) {
      final MailboxMessage message = MessagePumpThread.instance.sendExitFullScreenMessage(frame);
      if (message.busyAwait() == MailboxMessage.Status.SUCCESS) {
        frame.setVisible(false);
        frame.dispose();
        return;
      }
      maybeSleep(100L);
    }
  }

  private static String a124ck(final Applet var0) {
    return _cju != null ? _cju : var0.getParameter("settings");
  }

  private static LoginCredentials createLoginCredentials(final String username, final String password) {
    long userId = 0L;
    String email = null;
    if (username.indexOf('@') == -1) {
      userId = UserIdLoginCredentials.encodeUsername(username);
    } else {
      email = username;
    }

    return createLoginCredentials(email, userId, password);
  }

  private static void a423r() {
    final CipheredBuffer packet = s2cPacket;
    final int var1 = packet.readUByte();
    Menu.rankingsRequests.stream()
        .filter(var3 -> var3._k == var1)
        .findFirst().ifPresentOrElse(var2 -> {
          final int var4 = packet.readUByte();
          if (var4 != 0) {
            final int var5 = var2._j;
            _nla[0] = playerDisplayName;

            int var6;
            for (var6 = 1; var4 > var6; ++var6) {
              _nla[var6] = packet.readNullTerminatedString();
            }

            a599uic(var5, var4);

            for (var6 = 0; var4 > var6; ++var6) {
              a277tvc(packet);
              if (var6 == 0) {
                var2._i = _npj;
                var2._l = _naL;
                var2._h = _igd;
                var2._r = _gpc;
                a669pm(_naL, _npj, _gpc, 0, _igd);
              } else {
                a669pm(_naL, _npj, _gpc, var6, _igd);
              }
            }

            a366bh(var5);
            final String[][] var12 = var2._n = new String[2][var5];
            final int[][] var7 = var2._m = new int[2][4 * var5];
            final int var8 = _wmc;
            int var9 = 0;

            int var10;
            int var11;
            for (var10 = 0; var8 > var9; ++var9) {
              var11 = _lgd[var9];
              var12[0][var10] = _nla[var11];
              var7[0][4 * var10] = _f[var11];
              var7[0][var10 * 4 + 1] = _clrzb[var11];
              var7[0][2 + var10 * 4] = _ajd[var11];
              var7[0][var10 * 4 + 3] = _fy[var11];
              if (a623jp(_nla[var11]) && _fy[var11] + _ajd[var11] + _clrzb[var11] == 0) {
                var12[0][var10] = null;
                --var10;
              }

              ++var10;
            }

            int i = 0;

            for (var10 = 0; var8 > i; ++var10) {
              var11 = _lgd[i + var5];
              var12[1][var10] = _nla[var11];
              var7[1][4 * var10] = _f[var11];
              var7[1][4 * var10 + 1] = _clrzb[var11];
              var7[1][var10 * 4 + 2] = _ajd[var11];
              var7[1][var10 * 4 + 3] = _fy[var11];
              if (a623jp(_nla[var11]) && _clrzb[var11] + _ajd[var11] + _fy[var11] == 0) {
                var12[1][var10] = null;
                --var10;
              }

              ++i;
            }
          }

          var2._q = true;
          Menu.rankingsRequests.remove(var2);
        }, JagexApplet::shutdownServerConnection);
  }

  private static void a277tvc(final Buffer var0) {
    _npj = var0.readUShort() << 5;
    final int var1 = var0.readUByte();
    _npj += var1 >> 3;
    _gpc = 1835008 & var1 << 18;
    _gpc += var0.readUShort() << 2;
    final int i = var0.readUByte();
    _gpc += i >> 6;
    _igd = 2064384 & i << 15;
    _igd += var0.readUByte() << 7;
    final int j137 = var0.readUByte();
    _naL = (1 & j137) << 16;
    _igd += j137 >> 1;
    _naL += var0.readUShort();
  }

  private static void a366bh(final int var0) {
    a394ai(var0, true, 0, _wmc, _efa, _usb);

    int var1 = 0;
    while (_wmc > var1) {
      _lgd[var1 + var0] = var1++;
    }

    a394ai(var0 + var0, false, var0, _wmc + var0, _ffu, _se);
    if (_wmc > var0) {
      _wmc = var0;
    }

  }

  private static void a599uic(final int var0, final int var1) {
    if (_red == null || var1 > _red.length) {
      _red = new int[var1 * 2];
    }

    if (_f == null || var1 > _f.length) {
      _f = new int[var1 * 2];
    }

    if (_clrzb == null || var1 > _clrzb.length) {
      _clrzb = new int[var1 * 2];
    }

    if (_ajd == null || var1 > _ajd.length) {
      _ajd = new int[2 * var1];
    }

    if (_fy == null || var1 > _fy.length) {
      _fy = new int[2 * var1];
    }

    if (_gsc == null || _gsc.length < var1) {
      _gsc = new int[var1 * 2];
    }

    if (_lgd == null || var1 + var0 > _lgd.length) {
      _lgd = new int[2 * (var0 + var1)];
    }

    if (_aqp == null || _aqp.length < var1) {
      _aqp = new boolean[2 * var1];
    }

    _wmc = 0;
    _ffu = Integer.MAX_VALUE;
    _se = Integer.MIN_VALUE;
    _usb = Integer.MIN_VALUE;
    _efa = Integer.MAX_VALUE;
  }

  private static void handlePendingReflectionRequests() {
    while (anyPendingReflectionRequests()) {
      C2SPacket.buffer.writeCipheredByte(8);
      final int lenPos = ++C2SPacket.buffer.pos;
      respondToReflectionRequest();
      C2SPacket.buffer.insertLengthByte(C2SPacket.buffer.pos - lenPos);
    }
  }

  protected static void attachToCanvas(final Canvas canvas) {
    KeyState.instance.attach(canvas);
    MouseState.instance.attach(canvas);
    MouseWheelState.instance.attach(canvas);
  }

  protected static void detachFromCanvas(final Canvas canvas) {
    KeyState.instance.detach(canvas);
    MouseState.instance.detach(canvas);
    MouseWheelState.instance.detach(canvas);
  }

  public static void flushC2sPacket(final int n) {
    if (serverConnection1 != null && (n < 0 || loginState == LoginState.LOGGED_IN)) {
      if (C2SPacket.buffer.pos == 0 && lastC2sMessageTimestamp + 10000L < PseudoMonotonicClock.currentTimeMillis()) {
        C2SPacket.buffer.writeCipheredByte(n);
      }

      if (C2SPacket.buffer.pos > 0) {
        try {
          serverConnection1.write(C2SPacket.buffer.data, C2SPacket.buffer.pos);
          lastC2sMessageTimestamp = PseudoMonotonicClock.currentTimeMillis();
        } catch (final IOException var3) {
          shutdownServerConnection();
        }

        C2SPacket.buffer.pos = 0;
      }
    } else {
      C2SPacket.buffer.pos = 0;
    }
  }

  public static boolean connectedAndLoggedIn() {
    return serverConnection1 != null && loginState == LoginState.LOGGED_IN;
  }

  public static void shutdownServerConnection() {
    if (serverConnection1 != null) {
      serverConnection1.close();
      serverConnection1 = null;
    }
  }

  protected static boolean f154jc() {
    return connectionState >= ConnectionState.CONNECTED && !isAnonymous && !connectedAndLoggedIn();
  }

  @SuppressWarnings("SameParameterValue")
  private static void handleAchievementsPacket(final CipheredBuffer packet) {
    final int var1 = packet.readUByte();
    if (var1 == 0) {
      final int[] var2 = new int[8];
      final int len = packet.readUByte();
      for (int i = 0; i < len; ++i) {
        var2[i] = packet.readInt();
      }

      Optional.ofNullable(achievementRequests.poll()).ifPresentOrElse((req) -> {
        req.areAchievementsOffline = false;
        req.unlockedAchievementsBitmap = var2[0];
        req.completed = true;
      }, JagexApplet::shutdownServerConnection);
    } else if (var1 == 1) {
      shutdownServerConnection();
    } else if (var1 == 2) {
      Optional.ofNullable(achievementRequests.poll()).ifPresentOrElse(req -> {
        req.unlockedAchievementsBitmap = 0;
        req.completed = true;
        req.areAchievementsOffline = true;
      }, JagexApplet::shutdownServerConnection);
    } else {
      clientError(null, "A1: " + a738w());
      shutdownServerConnection();
    }
  }

  public static void navigateToQuit(final Applet var1) {
    navigateTo("quit.ws", var1);
  }

  private static boolean anyPendingReflectionRequests() {
    return Optional.ofNullable(ReflectionRequest.pending.peek()).map(req -> {
      for (int i = 0; i < req.messageCount; ++i) {
        if (req.fieldMessages[i] != null && req.fieldMessages[i].status == MailboxMessage.Status.PENDING) {
          return false;
        }
        if (req.methodMessages[i] != null && req.methodMessages[i].status == MailboxMessage.Status.PENDING) {
          return false;
        }
      }
      return true;
    }).orElse(false);
  }

  private static void a813bs(final boolean var0) {
    CommonUI._wha.a540(var0);
  }

  private static void a077wo(@MagicConstant(valuesFromClass = AccountResponse.Code.class) final int var0, final String[] var2, final String var3) {
    CommonUI._fjs = Enum1.C2;
    if (var0 == AccountResponse.Code.SUCCESS) {
      UsernameValidator._ija = AccountResponse.createSuccess(CreateAccountForm.ageFieldNum < 13);
      a786jp(null);
    } else if (var0 >= AccountResponse.Code.C100 && var0 <= AccountResponse.Code.C105) {
      a786jp(var2);
      UsernameValidator._ija = AccountResponse.a612tc(var2);
    } else {
      UsernameValidator._ija = AccountResponse.a431ck(var0, var3);
    }
  }

  private static void a453va(final String[] var0, @MagicConstant(valuesFromClass = AccountResponse.Code.class) final int code, final String errorMessage) {
    CommonUI._eel = Enum1.C2;
    if (code == AccountResponse.Code.SUCCESS) {
      _tplc = AccountResponse.createSuccess(CreateAccountForm.ageFieldNum < 13);
    } else if (code >= AccountResponse.Code.C100 && code <= AccountResponse.Code.C105) {
      _tplc = AccountResponse.a612tc(var0);
    } else {
      _tplc = AccountResponse.a431ck(code, errorMessage);
    }
  }

  private static e_ j083bp() {
    return new e_(a983of(), !CommonUI._wha.b154());
  }

  private static String a983of() {
    if (CommonUI._eel == Enum1.C3) {
      return CreateAccountForm.emailFieldText;
    } else if (CommonUI._wha.b154()) {
      return CommonUI._fjs == Enum1.C3 ? CommonUI._wha.getLabel() : CommonUI.enteredUsername;
    } else {
      return CommonUI._wha.getLabel();
    }
  }

  private static void navigateToReload(final Applet applet) {
    try {
      final String basePath = applet.getDocumentBase().getFile();
      final int queryIndex = basePath.indexOf('?');
      String reloadPage = "reload.ws";
      if (queryIndex >= 0) {
        reloadPage = reloadPage + basePath.substring(queryIndex);
      }

      final URL reloadUrl = new URL(applet.getCodeBase(), reloadPage);
      applet.getAppletContext().showDocument(a236jg(applet, reloadUrl), "_self");
    } catch (final Exception var6) {
      var6.printStackTrace();
    }
  }

  private static URL a236jg(final Applet applet, final URL url) {
    String var2 = null;
    if (_cju != null && !_cju.equals(applet.getParameter("settings"))) {
      var2 = _cju;
    }
    String var3 = null;
    if (sessionId != null && !sessionId.equals(applet.getParameter("session"))) {
      var3 = sessionId;
    }
    return a408np(url, var2, var3);
  }

  private static URL a408np(final URL var3, final String var0, final String var2) {
    String var4 = var3.getFile();
    int var5 = 0;

    while (true) {
      int var6;
      while (true) {
        while (true) {
          while (var4.regionMatches(var5, "/l=", 0, 3)) {
            var6 = var4.indexOf('/', var5 + 1);
            if (var6 < 0) {
              break;
            }

            var5 = var6;
          }

          if (!var4.regionMatches(var5, "/a=", 0, 3)) {
            break;
          }

          var6 = var4.indexOf('/', var5 + 1);
          if (var6 < 0) {
            break;
          }

          var5 = var6;
        }

        if (!var4.regionMatches(var5, "/p=", 0, 3)) {
          break;
        }

        var6 = var4.indexOf('/', var5 + 1);
        if (var6 < 0) {
          break;
        }

        if (var0 == null) {
          var5 = var6;
        } else {
          var4 = var4.substring(0, var5) + var4.substring(var6);
        }
      }

      if (!var4.regionMatches(var5, "/s=", 0, 3) && !var4.regionMatches(var5, "/c=", 0, 3)) {
        break;
      }

      var6 = var4.indexOf('/', 1 + var5);
      if (var6 < 0) {
        break;
      }

      if (var2 == null) {
        var5 = var6;
      } else {
        var4 = var4.substring(0, var5) + var4.substring(var6);
      }
    }

    final StringBuilder var9 = new StringBuilder(var5);
    var9.append(var4, 0, var5);

    if (var0 != null && var0.length() > 0) {
      var9.append("/p=");
      var9.append(var0);
    }

    if (var2 != null && var2.length() > 0) {
      var9.append("/s=");
      var9.append(var2);
    }

    if (var4.length() <= var5) {
      var9.append('/');
    } else {
      var9.append(var4.substring(var5));
    }

    try {
      return new URL(var3, var9.toString());
    } catch (final Exception var8) {
      var8.printStackTrace();
      return var3;
    }
  }

  @SuppressWarnings("SameParameterValue")
  private static ChatMessage receiveChatMessage(final boolean isQuick, final CipheredBuffer packet) {
    final int b0 = packet.readUByte();
    final boolean _tueb = (b0 & 128) != 0;
    final ChatMessage.Channel channel = ChatMessage.Channel.decode(b0 & 127);
    final int c = packet.readUByte();
    final long j = packet.readLong();
    final int l;
    final int e;
    if (channel == ChatMessage.Channel.PRIVATE) {
      l = packet.readUShort();
      e = packet.readU24();
    } else {
      l = 0;
      e = 0;
    }

    final boolean var4 = packet.readUByte() == 1;
    final String senderDisplayName = packet.readNullTerminatedString();
    final String senderName;
    if (var4) {
      senderName = packet.readNullTerminatedString();
    } else {
      senderName = senderDisplayName;
    }

    final int _fmf;
    final String _mcq;
    if (channel == ChatMessage.Channel.ROOM || channel == ChatMessage.Channel.CHANNEL_5) {
      _fmf = packet.readUShort();
      _mcq = packet.readNullTerminatedString();
    } else {
      _fmf = 0;
      _mcq = null;
    }

    String message;
    final int[] _mbp;
    if (isQuick) {
      final int var5 = packet.readUShort();
      final QuickChatResponse var6 = _dhc.get(var5);
      message = var6.joinStrings();
      _mbp = senderName.equals(playerDisplayName) ? null : var6.ids;
    } else {
      try {
        message = HuffmanCoder.instance.readEncoded(packet, 80);
      } catch (final Exception ex) {
        message = "Cabbage";
      }
      _mbp = null;
    }

    return new ChatMessage(isQuick, c, j, l, channel, e, senderDisplayName, _mbp, senderName, message, _tueb, _mcq, _fmf);
  }

  private static void a394ai(final int var0, final boolean var1, final int var2, final int var3, final int var5, final int var6) {
    if (var0 > var2) {
      if (var3 > var2 + 1) {
        int var7;
        int var8;
        int var9;
        int var10;
        if (5 + var2 < var3 && var5 != var6) {
          var7 = (var6 >> 1) + (var5 >> 1) + (1 & var5 & var6);
          var8 = var2;
          var9 = var6;
          var10 = var5;

          for (int var11 = var2; var3 > var11; ++var11) {
            final int var12 = _lgd[var11];
            final int var13 = var1 ? _f[var12] : _gsc[var12];
            if (var7 < var13) {
              _lgd[var11] = _lgd[var8];
              if (var13 < var9) {
                var9 = var13;
              }

              _lgd[var8++] = var12;
            } else if (var10 < var13) {
              var10 = var13;
            }
          }

          a394ai(var0, var1, var2, var8, var9, var6);
          a394ai(var0, var1, var8, var3, var5, var10);
        } else {
          for (var7 = var3 - 1; var2 < var7; --var7) {
            for (var8 = var2; var7 > var8; ++var8) {
              var9 = _lgd[var8];
              var10 = _lgd[var8 + 1];
              if (a917aq(var10, var9, var1)) {
                _lgd[var8] = var10;
                _lgd[var8 + 1] = var9;
              }
            }
          }
        }
      }
    }
  }

  private static String a755ql(final CharSequence var0) {
    int var2 = var0.length();
    if (var2 > 20) {
      var2 = 20;
    }

    final char[] var3 = new char[var2];

    for (int var4 = 0; var4 < var2; ++var4) {
      final char var5 = var0.charAt(var4);
      if (var5 >= 'A' && var5 <= 'Z') {
        var3[var4] = (char) (var5 - 65 + 97);
      } else if ((var5 < 'a' || var5 > 'z') && (var5 < '0' || var5 > '9')) {
        var3[var4] = '_';
      } else {
        var3[var4] = var5;
      }
    }

    return new String(var3);
  }

  protected static boolean s2cBytesAvailable(final int len) {
    if (s2cPacket.pos >= len) {
      return true;
    }

    if (serverConnection1 == null) {
      return false;
    }
    if (hasConnectionTimedOut()) {
      shutdownServerConnection();
      return false;
    }

    try {
      final int available = serverConnection1.inputAvailable();
      if (available > 0) {
        final int bytesToRead = Math.min(available, len - s2cPacket.pos);
        serverConnection1.read(s2cPacket.data, s2cPacket.pos, bytesToRead);
        lastRecievedDataFromServer = PseudoMonotonicClock.currentTimeMillis();
        s2cPacket.pos += bytesToRead;
        if (s2cPacket.pos < len) {
          return false;
        }

        s2cPacket.pos = 0;
        return true;
      }
    } catch (final IOException var4) {
      shutdownServerConnection();
    }

    return false;
  }

  private static boolean hasConnectionTimedOut() {
    // don't timeout the connection in debug mode
    return !DEBUG_MODE && PseudoMonotonicClock.currentTimeMillis() - lastRecievedDataFromServer > SERVER_TIMEOUT_MILLIS;
  }

  public static boolean a623jp(final String var0) {
    return normalizedPlayerName.equals(Strings.normalize(var0));
  }

  private static void a786jp(final String[] var0) {
    if (CreateAccountForm._anb != null) {
      CreateAccountForm._anb._D.a449(var0);
    }

    if (CommonUI._sjb != null) {
      CommonUI._sjb._F.a449(var0);
    }
  }

  public static void a808bp(final String var0, final Applet var1) {
    try {
      URL var2 = new URL(var1.getCodeBase(), var0);
      var2 = a236jg(var1, var2);
      showDocument(var1, var2.toString());
    } catch (final Exception var3) {
      var3.printStackTrace();
    }
  }

  public static String a738w() {
    String var1 = "(" + ShatteredPlansClient.thirdPreviousS2cPacketType + " " + ShatteredPlansClient.secondPreviousS2cPacketType + " " + ShatteredPlansClient.previousS2cPacketType + ") " + ShatteredPlansClient.currentS2cPacketType;
    if (nextS2cPacketLen > 0) {
      var1 = var1 + ":";

      for (int var2 = 0; nextS2cPacketLen > var2; ++var2) {
        var1 = var1 + ' ';
        int var3 = 255 & s2cPacket.data[var2];
        int var4 = var3 >> 4;
        var3 &= 15;
        if (var4 >= 10) {
          var4 += 55;
        } else {
          var4 += 48;
        }

        if (var3 < 10) {
          var3 += 48;
        } else {
          var3 += 55;
        }

        var1 = var1 + (char) var4;
        var1 = var1 + (char) var3;
      }
    }

    return var1;
  }

  private static void respondToReflectionRequest() {
    final ReflectionRequest req = ReflectionRequest.pending.poll();
    if (req != null) {
      boolean anyStillPending = false;

      for (int i = 0; i < req.messageCount; ++i) {
        if (req.fieldMessages[i] != null) {
          if (req.fieldMessages[i].status == MailboxMessage.Status.FAILURE) {
            req.statusCodes[i] = -5;
          }

          if (req.fieldMessages[i].status == MailboxMessage.Status.PENDING) {
            anyStillPending = true;
          }
        }

        if (req.methodMessages[i] != null) {
          if (req.methodMessages[i].status == MailboxMessage.Status.FAILURE) {
            req.statusCodes[i] = -6;
          }

          if (req.methodMessages[i].status == MailboxMessage.Status.PENDING) {
            anyStillPending = true;
          }
        }
      }

      if (!anyStillPending) {
        final int startPos = C2SPacket.buffer.pos;
        C2SPacket.buffer.writeInt(req.id);

        for (int i = 0; i < req.messageCount; ++i) {
          if (req.statusCodes[i] == ReflectionRequest.Status.OK) {
            try {
              final int type = req.types[i];
              if (type == ReflectionRequest.Type.STATIC_FIELD_GET) {
                final Field field = (Field) req.fieldMessages[i].response;
                C2SPacket.buffer.writeByte(0);
                C2SPacket.buffer.writeInt(field.getInt(null));
              } else if (type == ReflectionRequest.Type.STATIC_FIELD_SET) {
                final Field field = (Field) req.fieldMessages[i].response;
                field.setInt(null, req.fieldValues[i]);
                C2SPacket.buffer.writeByte(0);
              } else if (type == ReflectionRequest.Type.FIELD_GET_MODIFIERS) {
                final Field field = (Field) req.fieldMessages[i].response;
                C2SPacket.buffer.writeByte(0);
                C2SPacket.buffer.writeInt(field.getModifiers());
              }

              if (type == ReflectionRequest.Type.METHOD_INVOKE) {
                final Method method = (Method) req.methodMessages[i].response;

                final byte[][] serializedParams = req.serializedParams[i];
                final Object[] params = new Object[serializedParams.length];
                for (int j = 0; j < serializedParams.length; ++j) {
                  params[j] = new ObjectInputStream(new ByteArrayInputStream(serializedParams[j])).readObject();
                }

                final Object result = method.invoke(null, params);
                if (result == null) {
                  C2SPacket.buffer.writeByte(0);
                } else if (result instanceof Number) {
                  C2SPacket.buffer.writeByte(1);
                  C2SPacket.buffer.writeLong(((Number) result).longValue());
                } else if (result instanceof String) {
                  C2SPacket.buffer.writeByte(2);
                  C2SPacket.buffer.writeNullTerminatedString((String) result);
                } else {
                  C2SPacket.buffer.writeByte(4);
                }
              } else if (type == ReflectionRequest.Type.METHOD_GET_MODIFIERS) {
                final Method method = (Method) req.methodMessages[i].response;
                C2SPacket.buffer.writeByte(0);
                C2SPacket.buffer.writeInt(method.getModifiers());
              }
            } catch (final ClassNotFoundException var12) {
              C2SPacket.buffer.writeByte(-10);
            } catch (final InvalidClassException var13) {
              C2SPacket.buffer.writeByte(-11);
            } catch (final StreamCorruptedException var14) {
              C2SPacket.buffer.writeByte(-12);
            } catch (final OptionalDataException var15) {
              C2SPacket.buffer.writeByte(-13);
            } catch (final IllegalAccessException var16) {
              C2SPacket.buffer.writeByte(-14);
            } catch (final IllegalArgumentException var17) {
              C2SPacket.buffer.writeByte(-15);
            } catch (final InvocationTargetException var18) {
              C2SPacket.buffer.writeByte(-16);
            } catch (final SecurityException var19) {
              C2SPacket.buffer.writeByte(-17);
            } catch (final IOException var20) {
              C2SPacket.buffer.writeByte(-18);
            } catch (final NullPointerException var21) {
              C2SPacket.buffer.writeByte(-19);
            } catch (final Exception var22) {
              C2SPacket.buffer.writeByte(-20);
            } catch (final Throwable var23) {
              C2SPacket.buffer.writeByte(-21);
            }
          } else {
            C2SPacket.buffer.writeByte(req.statusCodes[i]);
          }
        }

        C2SPacket.buffer.writeDigest(startPos);
      }
    }
  }

  private static void writeHandshake(final Buffer packet, final int serverId, final int langId) {
    packet.writeByte(12);
    packet.writeShort(17);
    packet.writeShort(25);
    packet.writeShort(serverId);
    packet.writeByte(langId);
  }

  private static void handleDisableChatRestrictionsPacket() {
    cannotChat = false;
    canOnlyQuickChat = s2cPacket.readUByte() == 0;
  }

  private static void handleLevelPacket() {
    final CipheredBuffer var1 = s2cPacket;
    final int var2 = var1.readUByte();
    var1.readUByte();
    if (var2 == 0) {
      shutdownServerConnection();
    } else if (var2 == 1) {
      var1.d410();
      shutdownServerConnection();
    } else {
      clientError(null, "LR1: " + a738w());
      shutdownServerConnection();
    }
  }

  protected static boolean isS2cPacketFullyRecieved() {
    if (nextS2cPacketLen == PacketLengthType.VARIABLE_BYTE_I) {
      if (!s2cBytesAvailable(1)) {
        return false;
      }

      nextS2cPacketLen = s2cPacket.readUByte();
      s2cPacket.pos = 0;
    }

    if (nextS2cPacketLen == PacketLengthType.VARIABLE_SHORT_I) {
      if (!s2cBytesAvailable(2)) {
        return false;
      }

      nextS2cPacketLen = s2cPacket.readUShort();
      s2cPacket.pos = 0;
    }

    return s2cBytesAvailable(nextS2cPacketLen);
  }

  private static void writeRandomDat(@SuppressWarnings("SameParameterValue") final Buffer buffer) {
    final byte[] var2 = new byte[24];
    if (_vca != null) {
      try {
        _vca.seek(0L);
        _vca.read(var2);

        int var3 = 0;
        while (var3 < 24 && var2[var3] == 0) {
          ++var3;
        }

        if (var3 >= 24) {
          throw new IOException();
        }
      } catch (final Exception var5) {
        for (int var4 = 0; var4 < 24; ++var4) {
          var2[var4] = -1;
        }
      }
    }

    buffer.writeBytes(var2, 24);
  }

  private static void showDocument(final Applet var0, final String var2) {
    try {
      var0.getAppletContext().showDocument(new URL(var2), "_blank");
    } catch (final MalformedURLException var4) {
      clientError(null, "MGR1: " + var2);
    }
  }

  private static void a150ea() {
    pageSource.failureCount = 0;
    pageSource.errorCode = null;
  }

  private static boolean a917aq(final int var0, final int var1, final boolean var2) {
    if (var2) {
      if (_f[var0] > _f[var1]) {
        return true;
      }

      if (_f[var1] > _f[var0]) {
        return false;
      }

      if (_gsc[var1] < _gsc[var0]) {
        return true;
      }

      if (_gsc[var1] > _gsc[var0]) {
        return false;
      }
    } else {
      if (_gsc[var0] > _gsc[var1]) {
        return true;
      }

      if (_gsc[var0] < _gsc[var1]) {
        return false;
      }

      if (_f[var0] > _f[var1]) {
        return true;
      }

      if (_f[var1] > _f[var0]) {
        return false;
      }
    }

    final int var3 = _ajd[var1] + _clrzb[var1] + _fy[var1];
    final int var4 = _fy[var0] + _clrzb[var0] + _ajd[var0];
    if (var3 >= var4) {
      if (var4 >= var3) {
        return var0 > var1;
      } else {
        return false;
      }
    } else {
      return true;
    }
  }

  private static void a150ij() {
    if (_vca != null) {
      try {
        _vca.seek(0L);
        _vca.write(s2cPacket.data, s2cPacket.pos, 24);
      } catch (final Exception var2) {
      }
    }

    s2cPacket.pos += 24;
  }

  @SuppressWarnings("SameParameterValue")
  private static void handleHiscorePacket(final CipheredBuffer packet) {
    final int var1 = packet.readUByte();
    if (var1 == 0) {
      packet.readUShort();
    } else if (var1 == 1) {
      packet.readUShort();
      packet.readLong();
    } else {
      clientError(null, "HS1: " + a738w());
    }
    shutdownServerConnection();
  }

  private static DisplayMode[] listDisplayModes() {
    final MailboxMessage message = MessagePumpThread.instance.sendListDisplayModesMessage();
    if (message.busyAwait() == MailboxMessage.Status.FAILURE) {
      return new DisplayMode[0];
    }

    final int[] values = (int[]) message.response;
    final DisplayMode[] modes = new DisplayMode[values.length >> 2];

    for (int i = 0; i < modes.length; ++i) {
      final DisplayMode mode = new DisplayMode();
      modes[i] = mode;
      mode.width = values[i << 2];
      mode.height = values[(i << 2) + 1];
      mode.bitDepth = values[(i << 2) + 2];
    }

    return modes;
  }

  private static LoginCredentials createLoginCredentials(final String email, final long userId, final String password) {
    if (userId == 0L && email != null) {
      return new EmailLoginCredentials(email, password);
    } else {
      return new UserIdLoginCredentials(userId, password);
    }
  }

  private static boolean isCommonUiLoaded(final ResourceLoader spriteLoader, final ResourceLoader fontLoader, final ResourceLoader dataLoader) {
    return spriteLoader.isIndexLoaded() && spriteLoader.loadGroupData("commonui")
        && fontLoader.isIndexLoaded() && fontLoader.loadGroupData("commonui")
        && dataLoader.isIndexLoaded() && dataLoader.loadGroupData("button.gif");
  }

  private static void initializeGameResourceLoaders() {
    if (ResourceLoader.SHATTERED_PLANS_STRINGS_1 != null) {
      StringConstants.loadShatteredPlans(ResourceLoader.SHATTERED_PLANS_STRINGS_1);
      ResourceLoader.SHATTERED_PLANS_STRINGS_1 = null;
      ShatteredPlansClient.resetFrameClock();
    }

    ResourceLoader.HUFFMAN_CODES = createResourceLoader(ResourceLoader.PageId.HUFFMAN_CODES);
    ResourceLoader.SHATTERED_PLANS_SPRITES = ResourceLoader.create(MessagePumpThread.instance.cacheFiles, masterIndexLoader, ResourceLoader.PageId.SHATTERED_PLANS_SPRITES, false);
    ResourceLoader.SHATTERED_PLANS_JPEGS = createResourceLoader(ResourceLoader.PageId.SHATTERED_PLANS_JPEGS);
    ResourceLoader.SHATTERED_PLANS_FONTS = createResourceLoader(ResourceLoader.PageId.SHATTERED_PLANS_FONTS);
    ResourceLoader.SHATTERED_PLANS_SFX_1 = createResourceLoader(ResourceLoader.PageId.SHATTERED_PLANS_SFX_1);
    ResourceLoader.SHATTERED_PLANS_SFX_2 = createResourceLoader(ResourceLoader.PageId.SHATTERED_PLANS_SFX_2);
    ResourceLoader.SHATTERED_PLANS_MUSIC_1 = createResourceLoader(ResourceLoader.PageId.SHATTERED_PLANS_MUSIC_1);
    ResourceLoader.SHATTERED_PLANS_MUSIC_2 = createResourceLoader(ResourceLoader.PageId.SHATTERED_PLANS_MUSIC_2);
    ResourceLoader.SHATTERED_PLANS_STRINGS_2 = createResourceLoader(ResourceLoader.PageId.SHATTERED_PLANS_STRINGS_2);
    ResourceLoader.QUICK_CHAT_DATA = createResourceLoader(ResourceLoader.PageId.QUICK_CHAT_DATA);
  }

  private static ResourceLoader createResourceLoader(final int extraData2) {
    return ResourceLoader.create(masterIndexLoader, MessagePumpThread.instance.cacheFiles, extraData2);
  }

  private static void a669pm(final int var1, final int var2, final int var3, final int var4, final int var5) {
    _red[_wmc] = var4;
    _lgd[_wmc] = _wmc;
    _f[_wmc] = var1;
    if (_usb < var1) {
      _se = var1;
    }

    if (_efa > var1) {
      _ffu = var1;
    }

    _clrzb[_wmc] = var2;
    _ajd[_wmc] = var3;
    _fy[_wmc] = var5;
    final int var6 = var5 + var3 + var2;
    final int var7 = var6 != 0 ? 1000 * var2 / var6 : 0;
    _gsc[_wmc] = var7;
    if (var7 > _se) {
      _se = var7;
    }

    ++_wmc;
    if (_ffu > var7) {
      _ffu = var7;
    }

  }

  private static String a900es(final Throwable var0) throws IOException {
    String var1 = "";

    final StringWriter var13 = new StringWriter();
    final PrintWriter var3 = new PrintWriter(var13);
    var0.printStackTrace(var3);
    var3.close();
    final String var4 = var13.toString();
    final BufferedReader var5 = new BufferedReader(new StringReader(var4));
    final String var6 = var5.readLine();

    while (true) {
      final String var8 = var5.readLine();
      if (var8 == null) {
        var1 = var1 + "| " + var6;
        return var1;
      }

      final int var9 = var8.indexOf(40);
      final int var10 = var8.indexOf(41, var9 + 1);
      String var11;
      if (var9 == -1) {
        var11 = var8;
      } else {
        var11 = var8.substring(0, var9);
      }

      var11 = var11.trim();
      var11 = var11.substring(var11.lastIndexOf(32) + 1);
      var11 = var11.substring(var11.lastIndexOf(9) + 1);
      var1 = var1 + var11;
      if (var9 != -1 && var10 != -1) {
        final int var12 = var8.indexOf(".java:", var9);
        if (var12 >= 0) {
          var1 = var1 + var8.substring(var12 + 5, var10);
        }
      }

      var1 = var1 + ' ';
    }
  }

  private static Boolean j653fr() {
    final Boolean var0 = _sad;
    _sad = null;
    return var0;
  }

  private static boolean a351ro() {
    final int var1 = s2cPacket.readUByte();
    return var1 == 1;
  }

  private static void a599we(final Applet var0) {
    final String var1 = var0.getParameter("username");
    if (var1 != null) {
      UserIdLoginCredentials.encodeUsername(var1);
    }
  }

  public static AchievementRequest createAchievementRequest() {
    final AchievementRequest var1 = new AchievementRequest();
    achievementRequests.add(var1);
    C2SPacket.a093bo();
    return var1;
  }

  static void a366vs() {
    achievementRequests.forEach(var1 -> C2SPacket.a093bo());
  }

  static void drawLoadingScreen(final int percent, final String message, final boolean clear) {
    try {
      final Graphics2D g = (Graphics2D) canvas.getGraphics();
      g.scale(
        canvas.getWidth() / (double) ShatteredPlansClient.SCREEN_WIDTH,
        canvas.getHeight() / (double) ShatteredPlansClient.SCREEN_HEIGHT
      );

      if (loadingScreenFont == null) {
        loadingScreenFont = new java.awt.Font("Helvetica", java.awt.Font.BOLD, 13);
      }

      if (clear) {
        g.setColor(Color.black);
        g.fillRect(0, 0, ShatteredPlansClient.SCREEN_WIDTH, ShatteredPlansClient.SCREEN_HEIGHT);
      }

      g.translate(
        (ShatteredPlansClient.SCREEN_WIDTH - ShatteredPlansClient.ORIGINAL_SCREEN_WIDTH) / 2,
        (ShatteredPlansClient.SCREEN_HEIGHT - ShatteredPlansClient.ORIGINAL_SCREEN_HEIGHT) / 2
      );

      try {
        if (_rma == null) {
          _rma = canvas.createImage(304, 34);
        }

        final Graphics var6 = _rma.getGraphics();
        var6.setColor(LOADING_SCREEN_PURPLE);
        var6.drawRect(0, 0, 303, 33);
        var6.fillRect(2, 2, 3 * percent, 30);
        var6.setColor(Color.black);
        var6.drawRect(1, 1, 301, 31);
        var6.fillRect(3 * percent + 2, 2, 300 - percent * 3, 30);
        var6.setFont(loadingScreenFont);
        var6.setColor(Color.white);
        var6.drawString(message, (-(6 * message.length()) + 304) / 2, 22);
        g.drawImage(_rma, 320 - 152, 240 - 18, null);
      } catch (final Exception var9) {
        final int var7 = 320 - 152;
        final int var8 = 240 - 18;
        g.setColor(LOADING_SCREEN_PURPLE);
        g.drawRect(var7, var8, 303, 33);
        g.fillRect(2 + var7, 2 + var8, 3 * percent, 30);
        g.setColor(Color.black);
        g.drawRect(1 + var7, var8 + 1, 301, 31);
        g.fillRect(percent * 3 + var7 + 2, 2 + var8, 300 - 3 * percent, 30);
        g.setFont(loadingScreenFont);
        g.setColor(Color.white);
        g.drawString(message, (304 - 6 * message.length()) / 2 + var7, 22 + var8);
      }
    } catch (final Exception var10) {
      canvas.repaint();
    }
  }

  private static void f150com() {
    loadingScreenFont = null;
    _rma = null;
  }

  public static boolean nextTypedKey() {
    synchronized (KeyState.instance) {
      if (KeyState.keyTypeQueueFront == _feJ) {
        return false;
      } else {
        //noinspection MagicConstant
        lastTypedKeyCode = KeyState.keyTypeCodeQueue[KeyState.keyTypeQueueFront];
        lastTypedKeyChar = KeyState.keyTypeCharQueue[KeyState.keyTypeQueueFront];
        KeyState.keyTypeQueueFront = 1 + KeyState.keyTypeQueueFront & 127;
        return true;
      }
    }
  }

  private @NotNull JagexApplet.LoadingStatus serverConnectionFailure(final int errorCode) {
    _gbi = 0;
    socketMessage2 = null;
    serverConnection2 = null;

    final int var2 = this.gamePort2Primary;
    this.gamePort2Primary = this.gamePort2Secondary;
    this.gamePort2Secondary = var2;

    switch (errorCode) {
      case 50 -> pageSource.errorCode = PageSource.ErrorCode.SERVER_CODE_50;
      case 51 -> pageSource.errorCode = PageSource.ErrorCode.SERVER_CODE_51;
      default -> pageSource.errorCode = PageSource.ErrorCode.UNKNOWN_CONNECTION_FAILURE;
    }

    //noinspection NonAtomicOperationOnVolatileField
    ++pageSource.failureCount;
    if (pageSource.failureCount >= 2 && errorCode == 51) {
      return LoadingStatus.ERROR_SERVER_CODE_51;
    } else if (pageSource.failureCount >= 2 && errorCode == 50) {
      return LoadingStatus.ERROR_SERVER_CODE_50;
    } else if (pageSource.failureCount >= 4) {
      return LoadingStatus.ERROR_PAGE_SOURCE_MISC;
    } else {
      return LoadingStatus.OK_PENDING;
    }
  }

  private @NotNull JagexApplet.LoadingStatus b410u() {
    if (pageSource.failureCount < 4) {
      try {
        if (_gbi == 0) {
          socketMessage2 = MessagePumpThread.instance.sendOpenSocketMessage(this.codeHost, this.gamePort2Primary);
          ++_gbi;
        }

        if (_gbi == 1) {
          if (socketMessage2.status == MailboxMessage.Status.FAILURE) {
            return this.serverConnectionFailure(-1);
          }
          if (socketMessage2.status == MailboxMessage.Status.SUCCESS) {
            ++_gbi;
          }
        }

        if (_gbi == 2) {
          serverConnection2 = new DuplexStream((Socket) socketMessage2.response, MessagePumpThread.instance);
          final Buffer packet = new Buffer(13);
          writeHandshake(packet, this.serverId, langId);
          packet.writeByte(UPDATE);
          packet.writeInt(this.gameCrc);
          serverConnection2.write(packet.data, 13);
          ++_gbi;
          _amCb = 30000L + PseudoMonotonicClock.currentTimeMillis();
        }

        if (_gbi == 3) {
          if (serverConnection2.inputAvailable() > 0) {
            final int var3 = serverConnection2.readByte();
            if (var3 != 0) {
              return this.serverConnectionFailure(var3);
            }

            ++_gbi;
          } else if (_amCb < PseudoMonotonicClock.currentTimeMillis()) {
            return this.serverConnectionFailure(-2);
          }
        }

        if (_gbi == 4) {
          pageSource.initializeServerConnection(serverConnection2);
          serverConnection2 = null;
          _gbi = 0;
          socketMessage2 = null;
          return LoadingStatus.OK_COMPLETE;
        } else {
          return LoadingStatus.OK_PENDING;
        }
      } catch (final IOException var2) {
        return this.serverConnectionFailure(-3);
      }
    } else {
      return switch (pageSource.errorCode) {
        case INTEGRITY_CHECK_FAILURE -> LoadingStatus.ERROR_RESOURCE_INTEGRITY_CHECK;
        case PROTOCOL_ERROR          -> LoadingStatus.ERROR_PAGE_SOURCE_PROTOCOL;
        default                      -> LoadingStatus.ERROR_PAGE_SOURCE_MISC;
      };
    }
  }

  private @NotNull JagexApplet.LoadingStatus tickIndexLoaders() {
    masterIndexLoader.tick();
    if (pageSource.processWork()) {
      return LoadingStatus.OK_COMPLETE;
    } else {
      return this.b410u();
    }
  }

  @MagicConstant(valuesFromClass = AccountResponse.Code.class)
  private int a425si(final int var0, final int var1, final e_ var2, final e_ var3, final String var4, final boolean var5) {
    final String var6 = var3.a983();
    final String var7 = var2.a983();
    if (serverConnection1 == null) {
      final boolean var8 = this.initializeServerConnection(false);
      if (!var8) {
        return AccountResponse.Code.NONE;
      }
    }

    if (loginState == LoginState.SERVER_HANDSHAKE_SUCCESS) {
      C2SPacket.buffer.pos = 0;
      _sad = null;
      if (var4 == null) {
        loginPacket.pos = 0;
        loginPacket.writeInt(cipherIVGen.nextInt());
        loginPacket.writeInt(cipherIVGen.nextInt());
        loginPacket.writeNullBracketedString(var3.a154() ? var6 : "");
        loginPacket.writeNullBracketedString(var2.a154() ? var7 : "");
        C2SPacket.buffer.writeByte(16);
        C2SPacket.buffer.withLengthByte(() -> C2SPacket.buffer.writeEncrypted(loginPacket.data, loginPacket.pos));
      } else {
        int var12 = 0;
        loginPacket.pos = 0;
        if (var5) {
          var12 |= 1;
        }

        loginPacket.writeInt(cipherIVGen.nextInt());
        loginPacket.writeInt(cipherIVGen.nextInt());
        loginPacket.writeNullBracketedString(var6);
        loginPacket.writeNullBracketedString(var7);
        loginPacket.writeNullBracketedString(a755ql(var4));
        loginPacket.writeShort(var0);
        loginPacket.writeByte(var1);
        loginPacket.writeByte(var12);
        C2SPacket.buffer.writeByte(18);
        C2SPacket.buffer.withLengthShort(() -> {
          String var10 = a124ck(getInstance());
          if (var10 == null) {
            var10 = "";
          }

          C2SPacket.buffer.writeNullTerminatedString(var10);
          C2SPacket.buffer.writeEncrypted(loginPacket.data, loginPacket.pos);
        });
      }

      flushC2sPacket(-1);
      loginState = LoginState.C6;
    }

    if (loginState == LoginState.C6 && s2cBytesAvailable(1)) {
      final int var12 = s2cPacket.readUByte();
      s2cPacket.pos = 0;
      if (var12 >= 100 && var12 <= 105) {
        loginState = LoginState.C3;
        _aee = new String[var12 - 100];
      } else {
        if (var12 == 248) {
          CreateAccountForm.accountCreationFailed();
          accountErrorMessage = StringConstants.CREATE_UNABLE;
          shutdownServerConnection();
          _kej = false;
          return AccountResponse.Code.INELIGIBLE;
        }

        if (var12 == 99) {
          s2cBytesAvailable(1);
          _sad = a351ro();
          s2cPacket.pos = 0;
        } else {
          loginState = LoginState.C4;
          ShatteredPlansClient.currentS2cPacketType = var12;
          nextS2cPacketLen = -1;
        }
      }
    }

    if (loginState == LoginState.C3) {
      final byte var13 = 2;
      if (s2cBytesAvailable(var13)) {
        final int var9 = s2cPacket.readUShort();
        s2cPacket.pos = 0;
        if (s2cBytesAvailable(var9)) {
          final int var15 = _aee.length;

          for (int i = 0; i < var15; ++i) {
            _aee[i] = s2cPacket.readNullBracketedString();
          }

          shutdownServerConnection();
          _kej = false;
          //noinspection MagicConstant
          return AccountResponse.Code.C100 + var15;
        }
      }
    }

    if (loginState == LoginState.C4 && isS2cPacketFullyRecieved()) {
      if (ShatteredPlansClient.currentS2cPacketType == 255) {
        final String var14 = s2cPacket.readNullableNullTerminatedString();
        if (var14 != null) {
          _cju = var14;
        }
      } else {
        accountErrorMessage = s2cPacket.readNullTerminatedString();
      }

      shutdownServerConnection();
      _kej = false;
      //noinspection MagicConstant
      return ShatteredPlansClient.currentS2cPacketType;
    } else {
      if (serverConnection1 == null) {
        if (_kej) {
          if (hasConnectionTimedOut()) {
            accountErrorMessage = StringConstants.LOGIN_M3;
          } else {
            accountErrorMessage = StringConstants.LOGIN_M2;
          }

          _kej = false;
          return AccountResponse.Code.CONNECTION_FAILED;
        }

        final int var12 = this.gamePort1Primary;
        this.gamePort1Primary = this.gamePort1Secondary;
        this.gamePort1Secondary = var12;
        _kej = true;
      }

      return AccountResponse.Code.NONE;
    }
  }

  @MagicConstant(valuesFromClass = AccountResponse.Code.class)
  private int a968sr(final String usernameOrEmail, final int affId, final int var3, final String password, final String var5, final boolean var6) {
    final e_ var7 = new e_(usernameOrEmail);
    final e_ var8 = new e_(var5);
    return this.a425si(affId, var3, var8, var7, password, var6);
  }

  @MagicConstant(valuesFromClass = AccountResponse.Code.class)
  private int a031wi(final e_ var0, final e_ var1) {
    return this.a425si(0, 0, var0, var1, null, false);
  }

  private boolean initializeServerConnection(final boolean var0) {
    if (socketMessage1 == null) {
      socketMessage1 = MessagePumpThread.instance.sendOpenSocketMessage(this.codeHost, this.gamePort1Primary);
    }

    if (socketMessage1.status == MailboxMessage.Status.PENDING) {
      return false;
    } else {
      lastRecievedDataFromServer = lastC2sMessageTimestamp = PseudoMonotonicClock.currentTimeMillis();
      if (socketMessage1.status == MailboxMessage.Status.SUCCESS) {
        try {
          serverConnection1 = new DuplexStream((Socket) socketMessage1.response, MessagePumpThread.instance);
          C2SPacket.buffer.pos = 0;
          ShatteredPlansClient.previousS2cPacketType = ShatteredPlansClient.secondPreviousS2cPacketType = ShatteredPlansClient.thirdPreviousS2cPacketType = var0 ? -2 : -1;
          loginState = LoginState.SERVER_HANDSHAKE_SUCCESS;
          s2cPacket.pos = 0;
          writeHandshake(C2SPacket.buffer, this.serverId, langId);
          flushC2sPacket(-1);
        } catch (final IOException var3) {
          loginState = LoginState.SERVER_HANDSHAKE_FAILED;
        }
      } else {
        loginState = LoginState.SERVER_HANDSHAKE_FAILED;
      }

      socketMessage1 = null;
      return true;
    }
  }

  private void submitLoginRequest(final LoginCredentials credentials, final boolean isReconnecting) {
    cipherIV[0] = cipherIVGen.nextInt();
    cipherIV[1] = cipherIVGen.nextInt();
    cipherIV[2] = (int) (serverCipherIV >> 32);
    cipherIV[3] = (int) serverCipherIV;
    loginPacket.pos = 0;
    loginPacket.writeInt(cipherIV[0]);
    loginPacket.writeInt(cipherIV[1]);
    loginPacket.writeInt(cipherIV[2]);
    loginPacket.writeInt(cipherIV[3]);
    writeRandomDat(loginPacket);
    loginPacket.writeShort(this.affId);
    credentials.writeToPacket(loginPacket);

    C2SPacket.buffer.pos = 0;
    C2SPacket.buffer.writeByte(isReconnecting ? 18 : 16);
    C2SPacket.buffer.withLengthShort(() -> {
      C2SPacket.buffer.writeInt(this.gameCrc);
      C2SPacket.buffer.writeLong(this.instanceId);
      int flags = 0;
      if (this.isMember) {
        flags |= 1;
      }
      if (this.isJagexHost) {
        flags |= 8;
      }
      if (CreateDisplayNameForm._frH != null) {
        flags |= 16;
      }

      C2SPacket.buffer.writeByte(flags);
      String var6 = a124ck(getInstance());
      if (var6 == null) {
        var6 = "";
      }

      C2SPacket.buffer.writeNullTerminatedString(var6);
      if (CreateDisplayNameForm._frH != null) {
        C2SPacket.buffer.writeNullBracketedString(CreateDisplayNameForm._frH);
      }

      C2SPacket.buffer.writeEncrypted(loginPacket.data, loginPacket.pos);
    });
    flushC2sPacket(-1);
  }

  @MagicConstant(valuesFromClass = CommonUI.LoginResult.class)
  private int tickLoggingIn(final boolean isReconnecting) {
    final String password = CommonUI.getPassword();
    if (serverConnection1 == null) {
      if (!this.initializeServerConnection(isReconnecting)) {
        return CommonUI.LoginResult.NONE;
      }
    }

    if (loginState == LoginState.SERVER_HANDSHAKE_SUCCESS) {
      if (isReconnecting) {
        loginCredentials = createLoginCredentials(null, userId, password);
      } else {
        loginCredentials = createLoginCredentials(getUsernameOrEmail(), password);
      }

      C2SPacket.buffer.pos = 0;
      C2SPacket.buffer.writeByte(14);
      C2SPacket.buffer.writeByte(loginCredentials.getAuthMode().value);
      flushC2sPacket(-1);
      loginState = LoginState.C10;
    }

    if (loginState == LoginState.C10 && s2cBytesAvailable(1)) {
      final int var10 = s2cPacket.readUByte();
      s2cPacket.pos = 0;
      if (var10 == 0) {
        loginState = LoginState.READY_TO_LOG_IN;
      } else {
        ShatteredPlansClient.currentS2cPacketType = var10;
        loginState = LoginState.C1;
        nextS2cPacketLen = -1;
      }
    }

    if (loginState == LoginState.READY_TO_LOG_IN && s2cBytesAvailable(8)) {
      serverCipherIV = s2cPacket.readLong();
      s2cPacket.pos = 0;
      this.submitLoginRequest(loginCredentials, isReconnecting);
      loginState = LoginState.LOGIN_REQUEST_SENT;
    }

    if (loginState == LoginState.LOGIN_REQUEST_SENT && s2cBytesAvailable(1)) {
      final int loginResponseCode = s2cPacket.readUByte();
      CreateDisplayNameForm._frH = null;
      s2cPacket.pos = 0;
      ShatteredPlansClient.currentS2cPacketType = loginResponseCode;
      if (loginResponseCode == 0 || loginResponseCode == 1) {
        loginState = LoginState.LOGIN_REQUEST_SUCCESSFUL;
        nextS2cPacketLen = PacketLengthType.VARIABLE_BYTE_I;
      } else {
        if (loginResponseCode == 8) {
          shutdownServerConnection();
          _kej = false;
          return CommonUI.LoginResult.R8;
        }

        nextS2cPacketLen = -1;
        loginState = LoginState.C1;
      }
    }

    if (loginState == LoginState.LOGIN_REQUEST_SUCCESSFUL && isS2cPacketFullyRecieved()) {
      userId = s2cPacket.readLong();
      lastLoginPassword = password;
      adminLevel = s2cPacket.readUByte();
      modLevel = s2cPacket.readUByte();
      membershipLevel = s2cPacket.readUShort();
      final String settings = s2cPacket.readNullableNullTerminatedString();
      final int flags = s2cPacket.readUByte();
      if ((flags & 1) != 0) {
        a150ij();
      }

      DobToEnableChatForm._vaj = (flags & 16) != 0;
      if (!isReconnecting) {
        cannotChat = (flags & 8) != 0;
        canOnlyQuickChat = (flags & 2) != 0;
        if (cannotChat) {
          canOnlyQuickChat = true;
        }
      }

      playerDisplayName = s2cPacket.readNullTerminatedString();
      normalizedPlayerName = Strings.normalize(playerDisplayName);
      CommonUI._tfn = s2cPacket.readUByte();
      loginState = LoginState.LOGGED_IN;

      _kej = false;
      if (settings != null) {
        _cju = settings;
      }

      C2SPacket.buffer.setCipher(new IsaacCipher(cipherIV));
      Arrays.setAll(cipherIV, i -> cipherIV[i] + 50);
      s2cPacket.setCipher(new IsaacCipher(cipherIV));
      //noinspection MagicConstant
      return ShatteredPlansClient.currentS2cPacketType;
    } else if (loginState == LoginState.C1 && isS2cPacketFullyRecieved()) {
      shutdownServerConnection();
      if (ShatteredPlansClient.currentS2cPacketType == 7 && !_kej) {
        _kej = true;
        return CommonUI.LoginResult.NONE;
      } else {
        if (ShatteredPlansClient.currentS2cPacketType == 7) {
          ShatteredPlansClient.currentS2cPacketType = 3;
        }

        accountErrorMessage = s2cPacket.readNullTerminatedString();
        _kej = false;
        //noinspection MagicConstant
        return ShatteredPlansClient.currentS2cPacketType;
      }
    } else {
      if (serverConnection1 == null) {
        if (_kej) {
          if (hasConnectionTimedOut()) {
            accountErrorMessage = StringConstants.LOGIN_M3;
          } else {
            accountErrorMessage = StringConstants.LOGIN_M2;
          }

          _kej = false;
          return CommonUI.LoginResult.CONNECTION_LOST;
        }

        final int tmp = this.gamePort1Primary;
        this.gamePort1Primary = this.gamePort1Secondary;
        this.gamePort1Secondary = tmp;
        _kej = true;
      }

      return CommonUI.LoginResult.NONE;
    }
  }

  @Override
  public final void init() {
    if (instance != null) {
      ++reinitializationCount;
      if (reinitializationCount >= 3) {
        this.redirectToErrorPage("alreadyloaded");
        return;
      }

      this.getAppletContext().showDocument(this.getDocumentBase(), "_self");
      return;
    }

    try {
      this.codeHost = this.getCodeBase().getHost();
      final String codeHostLowerCase = this.codeHost.toLowerCase();
      this.isJagexHost = codeHostLowerCase.equals("jagex.com") || codeHostLowerCase.endsWith(".jagex.com");
      this.gamePort1 = Integer.parseInt(this.getParameter("gameport1"));
      this.gamePort2 = Integer.parseInt(this.getParameter("gameport2"));
      final String serverIdStr = this.getParameter("servernum");
      if (serverIdStr != null) {
        this.serverId = Integer.parseInt(serverIdStr);
      }

      this.gameCrc = Integer.parseInt(this.getParameter("gamecrc"));
      this.instanceId = Long.parseLong(this.getParameter("instanceid"));

      this.isMember = this.getParameter("member").equals("yes");
      final String langStr = this.getParameter("lang");
      if (langStr != null) {
        final int langId = Integer.parseInt(langStr);
        JagexApplet.langId = langId >= 5 ? 0 : langId;
      }

      final String affIdStr = this.getParameter("affid");
      if (affIdStr != null) {
        this.affId = Integer.parseInt(affIdStr);
      }

      isSimpleModeEnabled = Boolean.parseBoolean(this.getParameter("simplemode"));

      instance = this;

      _eic = JagexBaseApplet.getInstance();

      MessagePumpThread.instance = new MessagePumpThread();
      final MailboxMessage msg = MessagePumpThread.instance.sendSpawnThreadMessage(this, 1);
      assert msg.busyAwait() == MailboxMessage.Status.SUCCESS;
    } catch (final Exception e) {
      clientError(e, null);
      this.redirectToErrorPage("crash");
    }
  }

  protected final int tickCommonUI(final boolean alreadyLoggedIn) {
    final CommonUI.TickResult action = CommonUI.tick();
    if (action == CommonUI.TickResult.R1) {
      @MagicConstant(valuesFromClass = AccountResponse.Code.class)
      final int var5 = this.a031wi(j083bp(), new e_(getUsernameOrEmail(), CommonUI._fjs == Enum1.C3));
      if (var5 != AccountResponse.Code.NONE) {
        a077wo(var5, _aee, accountErrorMessage);
        _aee = null;
        accountErrorMessage = null;
      }

      final Boolean var6 = j653fr();
      if (var6 != null) {
        a813bs(var6);
      }
    }

    if (action == CommonUI.TickResult.R2) {
      @MagicConstant(valuesFromClass = AccountResponse.Code.class)
      final int code = this.a968sr(getUsernameOrEmail(), this.affId, CreateAccountForm.ageFieldNum, CommonUI.getPassword(), a983of(), CreateAccountForm.optInCheckboxActive);
      if (code != AccountResponse.Code.NONE) {
        a453va(_aee, code, accountErrorMessage);
        _aee = null;
        accountErrorMessage = null;
      }
    }

    if (action == CommonUI.TickResult.LOGGING_IN) {
      if (!_qjg.isOk()) {
        _qjg = LoadingStatus.OK_PENDING;
        a150ea();
      }

      if (alreadyLoggedIn) {
        _mdB = false;
      } else {
        @MagicConstant(valuesFromClass = CommonUI.LoginResult.class)
        final int loginResult = this.tickLoggingIn(false);
        if (loginResult != CommonUI.LoginResult.NONE) {
          if (loginResult == CommonUI.LoginResult.SUCCESS) {
            _coo = userId;
            CommonUI.handleLoginSucceeded();
            isAnonymous = false;
            connectionState = ConnectionState.CONNECTED;
          } else {
            CommonUI.handleLoginFailed(loginResult, accountErrorMessage);
            accountErrorMessage = null;
          }
        }
      }
    }

    if (action == CommonUI.TickResult.R4) {
      if (this.isMember) {
        sessionId = "";
        navigateToReload(JagexBaseApplet.getInstance());
      } else {
        connectionState = ConnectionState.CONNECTED;
        isAnonymous = true;
      }
    }

    if (action == CommonUI.TickResult.TO_SERVER_LIST) {
      navigateTo("toserverlist.ws", JagexBaseApplet.getInstance());
    }

    if (action == CommonUI.TickResult.PLAY_FREE_VERSION) {
      sessionId = "";
      navigateToReload(JagexBaseApplet.getInstance());
    }

    if (action == CommonUI.TickResult.TO_CUSTOMER_SUPPORT) {
      navigateTo("tosupport.ws", JagexBaseApplet.getInstance());
    }

    if (action == CommonUI.TickResult.VIEW_MESSAGES) {
      C2SPacket.sendViewMessages();
    }

    if (action == CommonUI.TickResult.RELOAD) {
      navigateToReload(JagexBaseApplet.getInstance());
    }

    if (action == CommonUI.TickResult.R12) {
      a808bp(CreateAccountForm._cgF, JagexBaseApplet.getInstance());
    }

    if (action == CommonUI.TickResult.TO_LANGUAGE_SELECT) {
      try {
        if (_vag == null) {
          _vag = new r_(MessagePumpThread.instance, new URL(this.getCodeBase(), "countrylist.ws"));
        }

        final boolean var9 = _vag.b154();
        if (var9) {

          _vag = null;
        }
      } catch (final Exception var8) {
        clientError(var8, "S1");

        _vag = null;
      }
    }

    if (action == CommonUI.TickResult.RETURN_TO_GAME) {
      connectionState = ConnectionState.CONNECTED;
    }

    if (action == CommonUI.TickResult.QUIT_TO_WEBSITE) {
      return 2;
    } else {
      return 0;
    }
  }

  private static void navigateTo(final String path, final Applet applet) {
    try {
      final URL url = new URL(applet.getCodeBase(), path);
      applet.getAppletContext().showDocument(a236jg(applet, url), "_top");
    } catch (final Exception var3) {
      var3.printStackTrace();
    }
  }

  @Override
  protected void initialize() {
    final Frame frame = new Frame("Jagex");
    frame.pack();
    frame.dispose();

    this.setBackground(Color.black);
    initializeLoadingMessages(langId);

    this.gamePort1Primary = this.gamePort1;
    this.gamePort1Secondary = this.gamePort2;
    this.gamePort2Primary = this.gamePort1;
    this.gamePort2Secondary = this.gamePort2;

    if (MessagePumpThread.instance.cacheFiles.random != null) {
      try {
        _vca = new BufferedCacheFile(MessagePumpThread.instance.cacheFiles.random, 64);
      } catch (final IOException e) {
        throw new RuntimeException(e);
      }
    }

    pageSource = new RemotePageSource();
    CacheWorker.instance = CacheWorker.create(MessagePumpThread.instance);
    masterIndexLoader = new MasterIndexLoader(pageSource, CacheWorker.instance);

    KeyState.initializeAdditionalCodeMappings();
    attachToCanvas(JagexBaseApplet.canvas);
  }

  protected final void mgsPacketHandler() {
    final int packetId = ShatteredPlansClient.currentS2cPacketType;

    if (packetId < 64 && S2CPacket.MGS_ENABLED[packetId]) {
      switch (packetId) {
        case S2CPacket.Type.KEEPALIVE -> {}
        case S2CPacket.Type.XTEA -> shutdownServerConnection();
        case S2CPacket.Type.HISCORE -> handleHiscorePacket(s2cPacket);
        case S2CPacket.Type.ACHIEVEMENTS -> handleAchievementsPacket(s2cPacket);
        case S2CPacket.Type.LEVEL_PROGRESS -> handleLevelPacket();
        case S2CPacket.Type.PROFILE -> handleProfilePacket(s2cPacket);
        case S2CPacket.Type.RATINGS -> a423r();
        case S2CPacket.Type.SESSION_ID -> sessionId = s2cPacket.readNullTerminatedString();
        case S2CPacket.Type.REFLECT -> ReflectionRequest.recieve(MessagePumpThread.instance, s2cPacket);
        case S2CPacket.Type.CHAT, S2CPacket.Type.QUICK_CHAT ->
            a681nc(receiveChatMessage(packetId == S2CPacket.Type.QUICK_CHAT, s2cPacket));
        case S2CPacket.Type.SOCIAL -> PlayerListEntry.handleSocialPacket(s2cPacket);
        case S2CPacket.Type.DISPLAY_NAME -> setPlayerDisplayName(s2cPacket.readNullTerminatedString());
        case S2CPacket.Type.SHOW_DOCUMENT -> this.handleShowDocumentPacket();
        case S2CPacket.Type.DISABLE_CHAT_RESTRICTIONS -> handleDisableChatRestrictionsPacket();
        default -> {
          clientError(null, "MGS1: " + a738w());
          shutdownServerConnection();
        }
      }
    } else {
      clientError(null, "MGS2: " + a738w());
      shutdownServerConnection();
    }
  }

  protected final void a540(final boolean var1) {
    processKeyState();
    processMouseState();
    mouseWheelRotation = MouseWheelState.instance.poll();
    assert !isConnectedAndLoaded() || connectionState == ConnectionState.RECONNECTING || ShatteredPlansClient.fullScreenCanvas == null || !ShatteredPlansClient.fullScreenCanvas.focusWasLost;

    if (connectedAndLoggedIn()) {
      if (KeyState.ticksSinceLastKeyEvent > IDLE_TICKS && MouseState.ticksSinceLastMouseEvent > IDLE_TICKS) {
        shutdownServerConnection();
        CommonUI.handleServerDisconnect();
        CommonUI.handleLoginFailed(CommonUI.LoginResult.R2, StringConstants.IDLE_MESSAGE_20_MIN);
        _mdB = true;
        _ipb = 15000L + PseudoMonotonicClock.currentTimeMillis();
      }
    }


    if (_qjg == LoadingStatus.OK_PENDING || _qjg == LoadingStatus.OK_COMPLETE) {
      final boolean var6 = _qjg == LoadingStatus.OK_PENDING;
      _qjg = this.tickIndexLoaders();
      if (var6 && _qjg == LoadingStatus.OK_COMPLETE && connectionState == ConnectionState.RECONNECTING && !f154jc()) {
        CommonUI.handleLoginSucceeded();
      }

      if (!_qjg.isOk()) {
        _ipb = PseudoMonotonicClock.currentTimeMillis() + 15000L;
      }
    }

    if (!_qjg.isOk()) {
      if (loadStage < LoadStage.REQUEST_GAME_STRINGS) {
        switch (_qjg) {
          case ERROR_SERVER_CODE_51 -> this.redirectToErrorPage("js5connect_full");
          case ERROR_RESOURCE_INTEGRITY_CHECK -> this.redirectToErrorPage("js5crc");
          case ERROR_PAGE_SOURCE_PROTOCOL -> this.redirectToErrorPage("js5io");
          case ERROR_SERVER_CODE_50 -> this.redirectToErrorPage("outofdate");
          default -> this.redirectToErrorPage("js5connect");
        }
      } else if (connectionState >= ConnectionState.CONNECTED) {
        CommonUI.handleServerDisconnect();
        switch (_qjg) {
          case ERROR_SERVER_CODE_51 -> CommonUI.handleLoginFailed(CommonUI.LoginResult.PROTOCOL_ERROR, StringConstants.ERROR_JS5CONNECT_FULL);
          case ERROR_RESOURCE_INTEGRITY_CHECK -> CommonUI.handleLoginFailed(CommonUI.LoginResult.PROTOCOL_ERROR, StringConstants.ERROR_JS5CRC);
          case ERROR_PAGE_SOURCE_PROTOCOL -> CommonUI.handleLoginFailed(CommonUI.LoginResult.PROTOCOL_ERROR, StringConstants.COMM_IO_ERROR);
          case ERROR_SERVER_CODE_50 -> CommonUI.handleLoginFailed(CommonUI.LoginResult.GAME_UPDATED, StringConstants.LOGIN_GAME_UPDATED);
          default -> CommonUI.handleLoginFailed(CommonUI.LoginResult.PROTOCOL_ERROR, StringConstants.ERROR_JS5_CONNECT);
        }

        _mdB = true;
      }
    }

    if ((!_qjg.isOk() || f154jc()) && _ipb <= PseudoMonotonicClock.currentTimeMillis()) {
      _mdB = false;
      if (!_qjg.isOk()) {
        _qjg = LoadingStatus.OK_PENDING;
        a150ea();
      }
    }

    if (_qjg == LoadingStatus.OK_COMPLETE && !f154jc()) {
      CommonUI.loadingFailed = false;
    }

    if (loadStage == LoadStage.LOAD_MASTER_INDEX && masterIndexLoader.loadIndex()) {
      loadStage = LoadStage.REQUEST_COMMON_DATA;
    }

    if (loadStage == LoadStage.REQUEST_COMMON_DATA) {
      if (langId != 0) {
        ResourceLoader.COMMON_STRINGS = createResourceLoader(ResourceLoader.PageId.COMMON_STRINGS);
      }

      ResourceLoader.COMMON_SPRITES = createResourceLoader(ResourceLoader.PageId.COMMON_SPRITES);
      ResourceLoader.COMMON_FONTS = createResourceLoader(ResourceLoader.PageId.COMMON_FONTS);
      ResourceLoader.JAGEX_LOGO_ANIMATION = createResourceLoader(ResourceLoader.PageId.JAGEX_LOGO_ANIMATION);
      loadStage = LoadStage.LOAD_COMMON_STRINGS;
    }

    if (loadStage == LoadStage.LOAD_COMMON_STRINGS) {
      if (ResourceLoader.COMMON_STRINGS != null && ResourceLoader.COMMON_STRINGS.isIndexLoaded()) {
        if (!ResourceLoader.COMMON_STRINGS.hasGroup("")) {
          ResourceLoader.COMMON_STRINGS = null;
        } else if (ResourceLoader.COMMON_STRINGS.loadGroupData("")) {
          StringConstants.loadCommon(ResourceLoader.COMMON_STRINGS);
          ResourceLoader.COMMON_STRINGS = null;
          ShatteredPlansClient.resetFrameClock();
        }
      }

      if (ResourceLoader.COMMON_STRINGS == null) {
        loadStage = LoadStage.LOAD_COMMON_DATA;
      }
    }

    if (loadStage == LoadStage.LOAD_COMMON_DATA && isCommonUiLoaded(ResourceLoader.COMMON_SPRITES, ResourceLoader.COMMON_FONTS, ResourceLoader.JAGEX_LOGO_ANIMATION) && ResourceLoader.JAGEX_LOGO_ANIMATION.loadAllGroups()) {
      f150com();
      CommonUI.setLoadProgress(0, StringConstants.LOADING);
      CommonUI.load(ResourceLoader.COMMON_SPRITES, ResourceLoader.COMMON_FONTS, ResourceLoader.JAGEX_LOGO_ANIMATION);

      if (isSimpleModeEnabled) {
        CommonUI.a423oo();
      }

      JagexLogoIntroAnimation.load(ResourceLoader.JAGEX_LOGO_ANIMATION);
      ResourceLoader.JAGEX_LOGO_ANIMATION = null;
      a599we(this);
      ShatteredPlansClient.resetFrameClock();
      loadStage = LoadStage.REQUEST_GAME_STRINGS;
    }

    if (loadStage == LoadStage.REQUEST_GAME_STRINGS) {
      if (langId != 0) {
        ResourceLoader.SHATTERED_PLANS_STRINGS_1 = createResourceLoader(ResourceLoader.PageId.SHATTERED_PLANS_STRINGS_1);
      }
      loadStage = LoadStage.LOAD_GAME_STRINGS;
    }

    if (loadStage == LoadStage.LOAD_GAME_STRINGS) {
      if (ResourceLoader.SHATTERED_PLANS_STRINGS_1 == null || ResourceLoader.SHATTERED_PLANS_STRINGS_1.isIndexLoaded() && ResourceLoader.SHATTERED_PLANS_STRINGS_1.loadAllGroups()) {
        loadStage = LoadStage.REQUEST_GAME_DATA;
      } else {
        CommonUI.setLoadProgress(0.0F, ShatteredPlansClient.loadingMessage(ResourceLoader.SHATTERED_PLANS_STRINGS_1, waitingForTextMessage, loadingTextMessage));
      }
    }

    if (loadStage == LoadStage.REQUEST_GAME_DATA) {
      initializeGameResourceLoaders();
      loadStage = LoadStage.LOADED;
    }

    if (!var1 && JagexBaseApplet._ncc) {
      detachFromCanvas(JagexBaseApplet.canvas);
      this.initializeCanvas();
      attachToCanvas(JagexBaseApplet.canvas);
    }

    if (S2CPacket.MGS_ENABLED[8]) {
      handlePendingReflectionRequests();
    }
  }

  protected final void j423() {
    if (loadStage >= LoadStage.REQUEST_GAME_STRINGS) {
      if (!JagexLogoIntroAnimation.isFinished()) {
        JagexLogoIntroAnimation.tick();
      } else if (connectionState == ConnectionState.NOT_CONNECTED) {
        this.tickCommonUI(false);
      } else {
        CommonUI.tick();
      }
    }
  }

  @MagicConstant(valuesFromClass = CommonUI.LoginResult.class)
  protected final int tickReconnecting() {
    if (this.didHandleError || !f154jc() || _mdB) {
      return CommonUI.LoginResult.NONE;
    }
    @MagicConstant(valuesFromClass = CommonUI.LoginResult.class)
    final int var2 = this.tickLoggingIn(true);
    if (var2 == CommonUI.LoginResult.NONE) {
      return CommonUI.LoginResult.NONE;
    }
    if (var2 == CommonUI.LoginResult.SUCCESS || var2 == CommonUI.LoginResult.R1) {
      if (connectionState == ConnectionState.RECONNECTING && _qjg == LoadingStatus.OK_COMPLETE) {
        CommonUI.handleLoginSucceeded();
      }
    } else {
      if (!_vmNb) {
        this.redirectToErrorPage("reconnect");
      }
      CommonUI.handleServerDisconnect();
      CommonUI.handleLoginFailed(var2, accountErrorMessage);
      _mdB = true;
      _ipb = PseudoMonotonicClock.currentTimeMillis() + 15000L;
    }
    return var2;
  }

  private void handleShowDocumentPacket() {
    final int len = nextS2cPacketLen - 1;
    final byte[] data = new byte[len];
    s2cPacket.readCipheredBytes(data, len);
    showDocument(JagexBaseApplet.getInstance(), Strings.decode1252String(data));
  }

  @SuppressWarnings("WeakerAccess")
  protected static final class LoadStage {
    public static final int LOAD_MASTER_INDEX = 0;
    public static final int REQUEST_COMMON_DATA = 1;
    public static final int LOAD_COMMON_STRINGS = 2;
    public static final int LOAD_COMMON_DATA = 3;
    public static final int REQUEST_GAME_STRINGS = 10;
    public static final int LOAD_GAME_STRINGS = 11;
    public static final int REQUEST_GAME_DATA = 12;
    public static final int LOADED = 20;
  }

  @SuppressWarnings("WeakerAccess")
  public static final class ConnectionState {
    public static final int NOT_CONNECTED = 0;
    public static final int CONNECTED = 10;
    public static final int RECONNECTING = 11;
  }

  private enum LoadingStatus {
    OK_PENDING(true),
    OK_COMPLETE(true),
    ERROR_PAGE_SOURCE_MISC(false),
    ERROR_PAGE_SOURCE_PROTOCOL(false),
    ERROR_RESOURCE_INTEGRITY_CHECK(false),
    ERROR_SERVER_CODE_51(false),
    ERROR_SERVER_CODE_50(false);

    private final boolean isOk;

    LoadingStatus(final boolean isOk) {
      this.isOk = isOk;
    }

    @SuppressWarnings({"WeakerAccess", "BooleanMethodIsAlwaysInverted"})
    public boolean isOk() {
      return this.isOk;
    }
  }
}
