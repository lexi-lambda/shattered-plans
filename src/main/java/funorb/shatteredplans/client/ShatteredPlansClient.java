package funorb.shatteredplans.client;

import funorb.Strings;
import funorb.audio.AudioThread;
import funorb.audio.SongData;
import funorb.audio.SampledAudioChannelS16;
import funorb.audio.MusicManager;
import funorb.audio.SoundManager;
import funorb.awt.FullScreenCanvas;
import funorb.awt.KeyState;
import funorb.awt.MouseState;
import funorb.cache.CacheWorker;
import funorb.cache.ResourceLoader;
import funorb.client.AchievementRequest;
import funorb.client.EmailLoginCredentials;
import funorb.client.GetProfileRequest;
import funorb.client.JagexBaseApplet;
import funorb.client.LoginState;
import funorb.client.RankingsRequest;
import funorb.client.ReflectionRequest;
import funorb.client.SetProfileRequest;
import funorb.client.TemplateDictionary;
import funorb.client.UserIdLoginCredentials;
import funorb.client.intro.JagexLogoIntroAnimation;
import funorb.client.lobby.ActionButton;
import funorb.client.lobby.AddOrRemovePlayerPopup;
import funorb.client.lobby.ChatMessage;
import funorb.client.lobby.ChatMessage.Channel;
import funorb.client.lobby.ClientLobbyRoom;
import funorb.client.lobby.Component;
import funorb.client.lobby.ContextMenu;
import funorb.client.lobby.LobbyPlayer;
import funorb.client.lobby.PlayerListEntry;
import funorb.client.lobby.PopupMenu;
import funorb.client.lobby.QuickChatHelpPanel;
import funorb.client.lobby.ReportAbuseDialog;
import funorb.client.lobby.ScrollPane;
import funorb.client.lobby.TabbedPlayerListWrapper;
import funorb.client.lobby.vm_;
import funorb.commonui.CommonUI;
import funorb.commonui.form.DobToEnableChatForm;
import funorb.graphics.ArgbSprite;
import funorb.graphics.Drawing;
import funorb.graphics.Font;
import funorb.graphics.NineSliceSprite;
import funorb.graphics.Sprite;
import funorb.graphics.SpriteResource;
import funorb.io.HuffmanCoder;
import funorb.shatteredplans.C2SPacket;
import funorb.shatteredplans.S2CPacket;
import funorb.shatteredplans.StringConstants;
import funorb.shatteredplans.client.game.ClientGameSession;
import funorb.shatteredplans.client.game.GameView;
import funorb.shatteredplans.client.intro.IntroAnimation;
import funorb.shatteredplans.game.GameOptions;
import funorb.shatteredplans.game.GameState;
import funorb.shatteredplans.game.ai.TutorialAI2;
import funorb.shatteredplans.map.StarSystem;
import funorb.shatteredplans.map.TannhauserUnconnectedException;
import funorb.shatteredplans.map.generator.MapGenerationFailure;
import funorb.util.MathUtil;
import funorb.util.PseudoMonotonicClock;
import org.intellij.lang.annotations.MagicConstant;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.sound.sampled.LineUnavailableException;
import java.awt.Canvas;
import java.io.IOException;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Queue;
import java.util.Random;

import static funorb.shatteredplans.S2CPacket.Type;

public final class ShatteredPlansClient extends JagexApplet {
  public static final int SCREEN_WIDTH = 640;
  public static final int SCREEN_HEIGHT = 480;
  public static final int[] NUM_PLAYERS_OPTION_VALUES = {2, 3, 4, 5, 6};
  public static final int[] GAMEOPT_CHOICES_COUNTS = {5, 7, 4, 5, 2};
  public static final Random globalRandom = new Random();
  private static final int _hoc = 0x808080;
  private static final int _gnt = 0;
  private static final StringBuilder typedChatMessage = new StringBuilder(80);
  private static final int[][] _msc = new int[][]{{2, 1, 3, 4}, {2, 1, 3, 3}, {2, 1, 3, 1}, {2, 1, 3, 0}};
  public static boolean _mdQ;
  public static Sprite _fmb;
  public static int _tga;
  public static boolean isChatboxSelected = true;
  public static RenderQuality renderQuality;
  public static boolean debugModeEnabled;
  private static Sprite _jfa;
  public static int _vjC;
  public static Sprite _wab;
  private static Component<Component<?>> _few;
  private static Component<Component<?>> _abAb;
  public static Component<?> chatMessageLabel;
  public static boolean invitePlayersDialogOpen;
  private static boolean _ucB;
  public static final int STATUS_MESSAGE_TIMEOUT_DURATION = 150;
  private static Component<?> _erj;
  private static boolean areAchievementsInitialized;
  public static AchievementRequest achievementRequest;
  private static Component<Component<?>> _faX;
  private static int _pgJ;
  private static String playerWhoKickedYou;
  private static long cannotStartGameUntil;
  private static int joinRequestCount;
  private static boolean findOpponentsButtonClicked;
  private static int _tmh = 0;
  private static Component<?> HIDE_CHAT_TEMPORARILY_LABEL;
  private static Component<Component<?>> invitePlayersDialogContents;
  public static String currentServerName;
  private static int _flf = 360;
  public static Sprite LOSE_SPRITE;
  public static boolean isQuickChatOpen = false;
  private static Component<?> _mbn;
  private static int[][] _peD;
  public static String[][] GAMEOPT_TOOLTIPS;
  public static long localPlayerId;
  public static ClientLobbyRoom unratedLobbyRoom;
  private static byte[] playerSeatsFilledBitmap;
  private static boolean justRecievedRoomDetailsFromServer;
  private static int[] _cfa;
  private static int _dmgm = SCREEN_HEIGHT;
  public static final String[] STAT_NAMES = new String[16];
  public static final String[] STAT_DESCS = new String[16];
  private static final int _jlU = 4;
  public static final int SCREEN_CENTER_X = SCREEN_WIDTH / 2;
  public static final int SCREEN_CENTER_Y = SCREEN_HEIGHT / 2;
  public static int chatMessageCount;
  private static boolean inLobbyBrowser;
  public static int unratedLobbyRoomTransitionCounter;
  private static int playerRating;
  public static vm_ _dmrh;
  public static ScrollPane<?> currentContextMenuParent;
  private static Sprite BEAKER;
  public static Sprite LOGO;
  public static Sprite _dmgn;
  public static Sprite _lqo;
  private static Sprite LOBBY_ICON;
  private static boolean[] _ekF;
  public static ChatMessage[] chatMessages;
  private static boolean loadedProfile;
  private static GetProfileRequest request = null;
  private static int allowSpectateSelection;
  public static int lobbyBrowserTransitionCounter;
  private static TabbedPlayerListWrapper lobbyRoomTabbedPlayerList;
  public static Component<Component<?>> invitePlayersDialog;
  private static Component<Component<?>> lobbyRoomLeftPanel;
  public static Sprite _jcnk;
  public static Sprite _msb;
  public static long _tkz;
  public static String _cvco;
  private static int _cvcm = 0;
  public static boolean _cvcn = false;
  private static boolean leavingLobby;
  public static int ratedLobbyRoomTransitionCounter;
  public static int _vsd;
  public static boolean _lgb;
  public static boolean showYouHaveBeenKickedDialog;
  private static boolean _nsob;
  public static boolean spectatingGame;
  private static boolean _hmo = true;
  public static int previousS2cPacketType = -1;
  public static int _kpi;
  private static boolean _cjx;
  /**
   * The type of a <i>partially recieved</i> incoming packet. A value of
   * {@code -1} indicates that we have not recieved the type of the next packet.
   */
  private static int nextS2cPacketType = -1;
  public static int currentS2cPacketType = -1;
  public static int secondPreviousS2cPacketType = -1;
  public static int thirdPreviousS2cPacketType = -1;
  private static boolean _sbh;
  public static boolean _isa;
  public static SongData currentTrack;
  public static String MUSIC_OPT_NEW;
  public static String MUSIC_OPT_OLD;
  public static boolean accountCreationDisabled;
  public static boolean _tli;
  public static String networkErrorMessage;
  private static boolean returnToMainMenuClicked;
  public static int currentTick;
  public static @NotNull Channel _tlr = Channel.LOBBY;
  private static byte[] _lrc;
  public static boolean drawDebugStats;
  public static FullScreenCanvas fullScreenCanvas;
  public static TemplateDictionary templateDictionary;
  private static int _ssw;
  private static boolean _wgd;
  public static Sprite _bga;
  private static ClientLobbyRoom ratedLobbyRoom;
  private static byte[] gameoptChoicesBitmap;
  private static int maxAiPlayersSelection;
  public static Sprite STAR_FIELD;
  public static Sprite WIN_SPRITE;
  public static int[] _imb;
  public static String[] MENU_ITEM_LABELS;
  public static boolean playingGame;
  public static @NotNull Channel currentChatChannel = Channel.LOBBY;
  private static Component<?> _cbl;
  private static boolean _K = true;
  private static boolean menuInitialized;
  private static boolean isFullyLoaded;
  private static int someCounterResetEvery200Ticks = 200;
  private static int _ebb = 0;
  private static EnumJbg _jbg = EnumJbg.C0;
  @MagicConstant(valuesFromClass = Menu.Id.class)
  private static int initialMenu;
  private static int ticksSinceLastMouseEventAtStartOfTick;
  private static Component<?> _cka;
  private static int _naF = 0;
  private final Queue<AchievementNotification> achievementNotifications = new ArrayDeque<>();
  private int achievementNotificationTimer = 0;

  public static boolean a788(final long playerId, final String playerName) {
    final PlayerListEntry entry = PlayerListEntry.lookupFriend(playerName);
    if (entry == null || entry.serverName == null) {
      return unratedLobbyRoom != null && LobbyPlayer.getJoined(playerId) != null;
    } else {
      return true;
    }
  }

  public static void resetFrameClock() {
    frameClock.reset();
    Arrays.fill(recentFrameTimes, 0L);
    framesToAdvance = 0;
  }

  private static void n423() {
    if (Menu.currentMenu != Menu.Id.INTRODUCTION && ClientGameSession.playSession == null && ClientGameSession.spectateSession == null) {
      a034so((SCREEN_WIDTH - LOGO.offsetX) / 2, 10, _jfa);
      LOGO.drawAdd((SCREEN_WIDTH - LOGO.offsetX) / 2, 10, 256);
    }
  }

  private static void a111qf() {
    if (ContextMenu.openInstance != null && ContextMenu.openInstance.view.f427()) {
      dismissContextMenu();
    } else if (ReportAbuseDialog.openInstance != null && ReportAbuseDialog.openInstance.h154()) {
      ReportAbuseDialog.openInstance = null;
      dismissContextMenu();
    } else if (AddOrRemovePlayerPopup.openInstance == null || !AddOrRemovePlayerPopup.openInstance.f427()) {
      a111ph();
    }
  }

  private static void initializeUiStrings() {
    if (MENU_ITEM_LABELS == null) {
      MUSIC_OPT_NEW = StringConstants.MUSIC_COLON + StringConstants.TRACK_NAME_NEW;
      MUSIC_OPT_OLD = StringConstants.MUSIC_COLON + StringConstants.TRACK_NAME_OLD;

      MENU_ITEM_LABELS = new String[33];
      MENU_ITEM_LABELS[Menu.Item.SINGLE_PLAYER_SKIRMISH] = StringConstants.TEXT_SKIRMISH;
      MENU_ITEM_LABELS[Menu.Item.ENTER_MULTIPLAYER_LOBBY] = StringConstants.ENTER_MULTIPLAYER_LOBBY;
      MENU_ITEM_LABELS[Menu.Item.RETURN_TO_GAME] = StringConstants.RETURN_TO_GAME;
      MENU_ITEM_LABELS[Menu.Item.RANKINGS] = StringConstants.RANKINGS;
      MENU_ITEM_LABELS[Menu.Item.INSTRUCTIONS_1] = StringConstants.INSTRUCTIONS;
      MENU_ITEM_LABELS[Menu.Item.INSTRUCTIONS_2] = StringConstants.INSTRUCTIONS;
      MENU_ITEM_LABELS[Menu.Item.FULLSCREEN] = StringConstants.FULLSCREEN;
      MENU_ITEM_LABELS[Menu.Item.MENU] = StringConstants.MENU;
      MENU_ITEM_LABELS[Menu.Item.CURRENT_MENU] = null;
      MENU_ITEM_LABELS[Menu.Item.END_GAME] = StringConstants.END_GAME;
      MENU_ITEM_LABELS[Menu.Item.RESIGN] = StringConstants.RESIGN;
      MENU_ITEM_LABELS[Menu.Item.RETURN_TO_LOBBY] = StringConstants.RETURN_TO_LOBBY;
      MENU_ITEM_LABELS[Menu.Item.HS_MODE_1] = StringConstants.HS_MODE_NAMES[0];
      MENU_ITEM_LABELS[Menu.Item.HS_MODE_2] = StringConstants.HS_MODE_NAMES[1];
      MENU_ITEM_LABELS[Menu.Item.HS_MODE_3] = StringConstants.HS_MODE_NAMES[2];
      MENU_ITEM_LABELS[Menu.Item.RATING_MODE_1] = StringConstants.RATING_MODE_NAMES[0];
      MENU_ITEM_LABELS[Menu.Item.RATING_MODE_2] = StringConstants.RATING_MODE_NAMES[1];
      MENU_ITEM_LABELS[Menu.Item.QUIT] = StringConstants.QUIT;
      MENU_ITEM_LABELS[Menu.Item.SOUND_VOLUME] = StringConstants.SOUND_COLON;
      MENU_ITEM_LABELS[Menu.Item.MUSIC_VOLUME] = StringConstants.MUSIC_COLON;
      MENU_ITEM_LABELS[Menu.Item.ACHIEVEMENTS] = StringConstants.ACHIEVEMENTS;
      MENU_ITEM_LABELS[Menu.Item.LOGIN_CREATE_ACCOUNT] = StringConstants.LOGIN_CREATE_ACCOUNT;
      MENU_ITEM_LABELS[Menu.Item.DISCARD] = StringConstants.DISCARD;
      MENU_ITEM_LABELS[Menu.Item.TUTORIAL] = StringConstants.TUTORIAL;
      MENU_ITEM_LABELS[Menu.Item.WATCH_INTRODUCTION] = StringConstants.WATCH_INTRODUCTION;
      MENU_ITEM_LABELS[Menu.Item.GAME_TYPE] = StringConstants.TEXT_MAP_HEX;
      MENU_ITEM_LABELS[Menu.Item.RULE_SET] = StringConstants.TEXT_GARRISON_NO;
      MENU_ITEM_LABELS[Menu.Item.START_SKIRMISH] = StringConstants.START_SKIRMISH;
      MENU_ITEM_LABELS[Menu.Item.OPTIONS_MENU] = StringConstants.OPTIONS_MENU_NOT_PAUSED;
      MENU_ITEM_LABELS[Menu.Item.MUSIC_TRACK] = MUSIC_OPT_NEW;

      STAT_NAMES[ 0] = StringConstants.TEXT_STAT_MAX_TOTAL_FLEET_SIZE;
      STAT_NAMES[ 1] = StringConstants.TEXT_STAT_SHIPS_DESTROYED;
      STAT_NAMES[ 2] = StringConstants.TEXT_STAT_SHIPS_LOST;
      STAT_NAMES[ 3] = StringConstants.TEXT_STAT_AVG_MOVE_SIZE;
      STAT_NAMES[ 4] = StringConstants.TEXT_STAT_MAX_PRODUCTION;
      STAT_NAMES[ 5] = StringConstants.TEXT_STAT_SHIPS_CONSTRUCTED;
      STAT_NAMES[ 6] = StringConstants.TEXT_STAT_PROJECTS_USED;
      STAT_NAMES[ 7] = StringConstants.TEXT_STAT_RESEARCH_WASTED;
      STAT_NAMES[ 8] = StringConstants.TEXT_STAT_ATTACKS_SUCCESSFUL;
      STAT_NAMES[ 9] = StringConstants.TEXT_STAT_ATTACKS_FAILED;
      STAT_NAMES[10] = StringConstants.TEXT_STAT_DEFENCES_SUCCESSFUL;
      STAT_NAMES[11] = StringConstants.TEXT_STAT_DEFENCES_FAILED;
      STAT_NAMES[12] = StringConstants.TEXT_STAT_EFFICIENCY;
      STAT_NAMES[13] = StringConstants.TEXT_STAT_FLUIDITY;
      STAT_NAMES[14] = StringConstants.TEXT_STAT_AGGRESSIVENESS;
      STAT_NAMES[15] = StringConstants.TEXT_STAT_SOLIDITY;

      STAT_DESCS[ 0] = StringConstants.TEXT_STAT_DESC_MAX_TOTAL_FLEET_SIZE;
      STAT_DESCS[ 1] = StringConstants.TEXT_STAT_DESC_SHIPS_DESTROYED;
      STAT_DESCS[ 2] = StringConstants.TEXT_STAT_DESC_SHIPS_LOST;
      STAT_DESCS[ 3] = StringConstants.TEXT_STAT_DESC_AVG_MOVE_SIZE;
      STAT_DESCS[ 4] = StringConstants.TEXT_STAT_DESC_MAX_PRODUCTION;
      STAT_DESCS[ 5] = StringConstants.TEXT_STAT_DESC_SHIPS_CONSTRUCTED;
      STAT_DESCS[ 6] = StringConstants.TEXT_STAT_DESC_PROJECTS_USED;
      STAT_DESCS[ 7] = StringConstants.TEXT_STAT_DESC_RESEARCH_WASTED;
      STAT_DESCS[ 8] = StringConstants.TEXT_STAT_DESC_ATTACKS_SUCCESSFUL;
      STAT_DESCS[ 9] = StringConstants.TEXT_STAT_DESC_ATTACKS_FAILED;
      STAT_DESCS[10] = StringConstants.TEXT_STAT_DESC_DEFENCES_SUCCESSFUL;
      STAT_DESCS[11] = StringConstants.TEXT_STAT_DESC_DEFENCES_FAILED;
      STAT_DESCS[12] = StringConstants.TEXT_STAT_DESC_EFFICIENCY;
      STAT_DESCS[13] = StringConstants.TEXT_STAT_DESC_FLUIDITY;
      STAT_DESCS[14] = StringConstants.TEXT_STAT_DESC_AGGRESSIVENESS;
      STAT_DESCS[15] = StringConstants.TEXT_STAT_DESC_SOLIDITY;

      StringConstants.GAMEOPT_NAMES[0] = new String[5];
      GAMEOPT_TOOLTIPS = new String[5][];
      GAMEOPT_TOOLTIPS[0] = new String[5];
      for (int i = 0; i < 5; ++i) {
        StringConstants.GAMEOPT_NAMES[0][i] = Integer.toString(i);
        GAMEOPT_TOOLTIPS[0][i] = StringConstants.TEXT_TOTAL_PLAYERS;
      }
      GAMEOPT_TOOLTIPS[2] = StringConstants.GAME_TYPE_TOOLTIPS;
      GAMEOPT_TOOLTIPS[4] = StringConstants.RULESET_TOOLTIPS;
    }
  }

  private static String loadingMessage(final ResourceLoader loader, final String group, final String waiting, final String loading) {
    return loader.isIndexLoaded() ? loading + " - " + loader.percentLoaded(group) + "%" : waiting;
  }

  public static String loadingMessage(final ResourceLoader loader, final String waiting, final String loading) {
    return loader.isIndexLoaded() ? loading + " - " + loader.percentLoaded() + "%" : waiting;
  }

  private static String loadingMessage0(final ResourceLoader loader, final String waiting, final String loading) {
    return loader.isIndexLoaded() ? loading + " - " + loader.percentLoaded(0) + "%" : waiting;
  }

  public static void a776qk(@MagicConstant(valuesFromClass = Menu.Id.class) final int id) {
    _jbg = EnumJbg.C1;
    initialMenu = id;
  }

  private static void tickSomethingThatSeemsRelatedToAudio() {
    Sounds.musicChannel.doSomethingThatSeemsRelatedToAudio();
    Sounds.soundsChannel.doSomethingThatSeemsRelatedToAudio();

    --someCounterResetEvery200Ticks;
    if (someCounterResetEvery200Ticks == 0) {
      someCounterResetEvery200Ticks = 200;
      Sounds.playingSounds.removeIf(sound -> !sound._p.isLinked());
    }
  }

  private static void a780ck(final ResourceLoader commonSpriteLoader,
                             final ResourceLoader commonFontLoader,
                             final ResourceLoader spriteLoader,
                             final ResourceLoader fontLoader) {
    Menu.FONT = SpriteResource.loadPalettedSpriteFont(spriteLoader, fontLoader, "", "font");
    Menu.SMALL_FONT = SpriteResource.loadPalettedSpriteFont(spriteLoader, fontLoader, "", "smallfont");

    LOGO = SpriteResource.loadSprite(spriteLoader, "logo");
    final int var7 = 3 * LOGO.offsetX / 4;
    final int var8 = LOGO.offsetY * 3 / 4;
    Drawing.saveContext();
    _jfa = new Sprite(var7, var8);
    _jfa.installForDrawing();
    Drawing.fillRect(0, 0, var7, var8, Drawing.WHITE);
    LOGO.e326(0, 0, 0);
    Drawing.b669(3, 3, var7, var8);
    LOGO.e326(0, 0, 255);
    Drawing.restoreContext();

    Menu.MENU_HEX = SpriteResource.loadSprite(spriteLoader, "menu_hex");
    Menu.INSTR_MAIN_VIEW = SpriteResource.loadSprite(spriteLoader, "", "instr_mainview");
    Menu.INSTR_STAR_FRAME = SpriteResource.loadSprite(spriteLoader, "", "instr_starframe");

    final Sprite[] hudIcons = SpriteResource.loadSprites(spriteLoader, "", "hud_icons");
    GameUI.HUD_ICON_1 = hudIcons[0];
    GameUI.HUD_ICON_2 = hudIcons[1];
    GameUI.HUD_ICON_3 = hudIcons[2];
    GameUI.HUD_ICON_4 = hudIcons[3];
    GameUI.HUD_ICON_5 = hudIcons[4];
    final Sprite[] hudIconsRed = SpriteResource.loadSprites(spriteLoader, "", "hud_icons_red");
    GameUI.HUD_ICON_RED_1 = hudIconsRed[0];
    GameUI.HUD_ICON_RED_2 = hudIconsRed[1];
    GameUI.HUD_ICON_RED_3 = hudIconsRed[2];
    GameUI.HUD_ICON_RED_4 = hudIconsRed[3];
    GameUI.HUD_ICON_RED_5 = hudIconsRed[4];
    final Sprite[] hudIconsNoBase = SpriteResource.loadSprites(spriteLoader, "", "hud_icons_nobase");
    GameUI.HUD_ICON_NO_BASE_1 = hudIconsNoBase[0];
    GameUI.HUD_ICON_NO_BASE_2 = hudIconsNoBase[1];
    GameUI.HUD_ICON_NO_BASE_3 = hudIconsNoBase[2];
    GameUI.HUD_ICON_NO_BASE_4 = hudIconsNoBase[3];
    GameUI.HUD_ICON_NO_BASE_5 = hudIconsNoBase[4];

    GameUI.ICON_CIRCLES = SpriteResource.loadSprites(spriteLoader, "", "icon_circles");
    GameUI.PRODUCTION_ICONS = SpriteResource.loadSprites(spriteLoader, "", "production_icons");
    GameUI.PRODUCTION_BUTTON = SpriteResource.loadSprite(spriteLoader, "production_button");
    GameUI.PRODUCTION_BUTTON_DOWN = SpriteResource.loadSprite(spriteLoader, "production_button_down");
    GameUI.PROJECT_ICONS = SpriteResource.loadSprites(spriteLoader, "", "project_icons");

    GameUI.FACTION_ICONS = SpriteResource.loadSprites(spriteLoader, "", "faction_icons");
    GameUI.FACTION_ICONS_LARGE = SpriteResource.loadSprites(spriteLoader, "", "faction_icons_large");
    final Sprite tmp1 = GameUI.FACTION_ICONS[1];
    GameUI.FACTION_ICONS[1] = GameUI.FACTION_ICONS[5];
    GameUI.FACTION_ICONS[5] = tmp1;
    final Sprite tmp2 = GameUI.FACTION_ICONS[2];
    GameUI.FACTION_ICONS[2] = GameUI.FACTION_ICONS[4];
    GameUI.FACTION_ICONS[4] = tmp2;
    final Sprite tmp3 = GameUI.FACTION_ICONS_LARGE[1];
    GameUI.FACTION_ICONS_LARGE[1] = GameUI.FACTION_ICONS_LARGE[5];
    GameUI.FACTION_ICONS_LARGE[5] = tmp3;
    final Sprite tmp4 = GameUI.FACTION_ICONS_LARGE[2];
    GameUI.FACTION_ICONS_LARGE[2] = GameUI.FACTION_ICONS_LARGE[4];
    GameUI.FACTION_ICONS_LARGE[4] = tmp4;

    GameUI.READY_BUTTON = SpriteResource.loadSprite(spriteLoader, "ready_button");
    GameUI.READY_BUTTON_DOWN = SpriteResource.loadSprite(spriteLoader, "ready_button_down");
    Menu.SHINE_LEFT = SpriteResource.loadSprite(spriteLoader, "shine_left");
    Menu.SHINE_RIGHT = SpriteResource.loadSprite(spriteLoader, "shine_right");
    Menu.SHINE_MID = SpriteResource.loadSprite(spriteLoader, "shine_mid");

    GameView.COMBAT_HEX_WHITE = SpriteResource.loadSprite(spriteLoader, "combat_hex_white");
    Drawing.saveContext();
    GameView.COMBAT_HEX_WHITE.installForDrawing();
    GameView.COMBAT_HEX_WHITE.drawTinted2(0, 0, 2105376);
    Drawing.restoreContext();

    StarSystem.PLANET_BURNT = SpriteResource.loadSprite(spriteLoader, "planet_burnt");
    StarSystem.PLANET_SCORCHED_EARTH = SpriteResource.loadSprite(spriteLoader, "planet_scorchedearth");
    StarSystem.PLANET_EARTH_LIKE = SpriteResource.loadSprite(spriteLoader, "planet_earthlike");
    StarSystem.PLANET_EXOTIC = SpriteResource.loadSprite(spriteLoader, "planet_exotic");
    StarSystem.PLANET_GAS = SpriteResource.loadSprite(spriteLoader, "planet_gas");
    StarSystem.PLANET_RINGED = SpriteResource.loadSprite(spriteLoader, "planet_ringed");
    StarSystem.PLANET_ROCK = SpriteResource.loadSprite(spriteLoader, "planet_rock");

    StarSystem.ALIEN_MINER = SpriteResource.loadSprite(spriteLoader, "alien_miner");
    StarSystem.ALIEN_SHIP = SpriteResource.loadSprite(spriteLoader, "alien_ship");
    StarSystem.ALIEN_BASE = SpriteResource.loadSprite(spriteLoader, "alien_base");

    GameUI.SHIP = SpriteResource.loadSprite(spriteLoader, "", "ship");

    GameView.SHIELD = SpriteResource.loadSprite(spriteLoader, "", "shield");
    GameView.DEFENSE_GRID = SpriteResource.loadSprite(spriteLoader, "", "defensegrid");
    GameView.CHEVRON = SpriteResource.loadSprite(spriteLoader, "", "chevron");
    GameView.WARNING = SpriteResource.loadSprite(spriteLoader, "warning");
    GameView.HAMMER = SpriteResource.loadSprite(spriteLoader, "", "hammer");

    BEAKER = SpriteResource.loadSprite(spriteLoader, "", "beaker");

    GameUI.FACTION_RING = SpriteResource.loadSprite(spriteLoader, "factionring");
    GameUI.FACTION_RING_TAG = SpriteResource.loadSprite(spriteLoader, "factionringtag");
    GameUI.FACTION_RING_CENTER = SpriteResource.loadSprite(spriteLoader, "factionringcentre");

    GameView.ARROW_SHIP = SpriteResource.loadSprite(spriteLoader, "arrowship");
    GameView.ARROW_SHIP_DAMAGED = SpriteResource.loadSprite(spriteLoader, "arrowshipdamaged");

    GameView.FLEETS_ARROW_SHIP = SpriteResource.loadSprite(spriteLoader, "fleetsarrowship");
    GameView.FLEET_BUTTONS = SpriteResource.loadSprites(spriteLoader, "", "fleetbuttons");

    GameUI.ANIM_ICONS = SpriteResource.loadSprites(spriteLoader, "", "anim_icons");

    GameView._cos = new ArgbSprite(6, 6);
    GameView._cos.pixels[ 0] = 0;
    GameView._cos.pixels[ 1] = 0;
    GameView._cos.pixels[ 2] = 0x33ffffff;
    GameView._cos.pixels[ 3] = 0x22ffffff;
    GameView._cos.pixels[ 4] = 0;
    GameView._cos.pixels[ 5] = 0;
    GameView._cos.pixels[ 6] = 0;
    GameView._cos.pixels[ 7] = 0xffa2a2a2;
    GameView._cos.pixels[ 8] = 0xffc7c7c7;
    GameView._cos.pixels[ 9] = 0xffaeaeae;
    GameView._cos.pixels[10] = 0xff545454;
    GameView._cos.pixels[11] = 0;
    GameView._cos.pixels[12] = 0x10000000;
    GameView._cos.pixels[13] = 0xffa9a9a9;
    GameView._cos.pixels[14] = 0xffc5c5c5;
    GameView._cos.pixels[15] = 0xffb2b2b2;
    GameView._cos.pixels[16] = 0xff5d5d5d;
    GameView._cos.pixels[17] = 0x44000000;
    GameView._cos.pixels[18] = 0x20000000;
    GameView._cos.pixels[19] = 0xff707070;
    GameView._cos.pixels[20] = 0xff909090;
    GameView._cos.pixels[21] = 0xff666666;
    GameView._cos.pixels[22] = 0xff2a2a2a;
    GameView._cos.pixels[23] = 0x48000000;
    GameView._cos.pixels[24] = 0;
    GameView._cos.pixels[25] = 0xff323232;
    GameView._cos.pixels[26] = 0xff2b2b2b;
    GameView._cos.pixels[27] = 0xff1d1d1d;
    GameView._cos.pixels[28] = 0xff202020;
    GameView._cos.pixels[29] = 0;
    GameView._cos.pixels[30] = 0;
    GameView._cos.pixels[31] = 0;
    GameView._cos.pixels[32] = 503316480;
    GameView._cos.pixels[33] = 0x1bffffff;
    GameView._cos.pixels[34] = 0;
    GameView._cos.pixels[35] = 0;

    GameView.RES_SIDES = new ArgbSprite[6];
    GameView.RES_LOWS = new ArgbSprite[6];
    for (int i = 0; i < GameView.RES_SIDES.length; ++i) {
      GameView.RES_SIDES[i] = SpriteResource.loadSprite(spriteLoader, "res_side_" + i);
      GameView.RES_LOWS[i] = SpriteResource.loadSprite(spriteLoader, "res_low_" + i);
    }

    GameView.DEFNET_ANIM_SMALL = SpriteResource.loadSprites(spriteLoader, "", "defnet_anim_small");
    for (int i = 0; i < GameView.DEFNET_ANIM_SMALL[0].pixels.length; ++i) {
      if (GameView.DEFNET_ANIM_SMALL[0].pixels[i] != 0) {
        GameView.DEFNET_ANIM_SMALL[0].pixels[i] = ((GameView.DEFNET_ANIM_SMALL[0].pixels[i] >>> 24) / 4 << 24) + 0x5ba8cc;
      }
    }
    for (int i = 1; i < 13; ++i) {
      final Sprite frame = GameView.DEFNET_ANIM_SMALL[i];
      for (int j = 0; j < frame.pixels.length; ++j) {
        if ((frame.pixels[j] & 0xff000000) == 0) {
          frame.pixels[j] = 0;
        }
      }
    }

    GameView.DEFNET_ANIM_MID = SpriteResource.loadSprites(spriteLoader, "", "defnet_anim_mid");
    for (int i = 0; i < GameView.DEFNET_ANIM_MID[0].pixels.length; ++i) {
      if (GameView.DEFNET_ANIM_MID[0].pixels[i] != 0) {
        GameView.DEFNET_ANIM_MID[0].pixels[i] = 0x5ba8cc + ((GameView.DEFNET_ANIM_MID[0].pixels[i] >>> 24) / 4 << 24);
      }
    }
    for (int i = 1; i < 13; ++i) {
      final Sprite frame = GameView.DEFNET_ANIM_MID[i];
      for (int j = 0; j < frame.pixels.length; ++j) {
        if ((frame.pixels[j] & 0xff000000) == 0) {
          frame.pixels[j] = 0;
        }
      }
    }

    GameView.DEFNET_ANIM_LARGE = SpriteResource.loadSprites(spriteLoader, "", "defnet_anim_large");
    for (int i = 0; i < GameView.DEFNET_ANIM_LARGE[0].pixels.length; ++i) {
      if (GameView.DEFNET_ANIM_LARGE[0].pixels[i] != 0) {
        GameView.DEFNET_ANIM_LARGE[0].pixels[i] = ((GameView.DEFNET_ANIM_LARGE[0].pixels[i] >>> 24) / 4 << 24) + 0x5ba8cc;
      }
    }
    for (int i = 1; i < 13; ++i) {
      final Sprite frame = GameView.DEFNET_ANIM_LARGE[i];
      for (int j = 0; frame.pixels.length > j; ++j) {
        if ((frame.pixels[j] & 0xff000000) == 0) {
          frame.pixels[j] = 0;
        }
      }
    }

    final Sprite _rfk = new Sprite(16, 10);
    final Sprite _b = new Sprite(18, 12);
    Drawing.saveContext();
    _rfk.installForDrawing();
    GameUI.SHIP.d093(0, 0);
    _b.installForDrawing();
    _rfk.drawTinted2(1, 1, 8421504);
    _fmb = new Sprite(10, 12);
    _fmb.installForDrawing();
    GameView.SHIELD.drawTinted2(1, 1, 8421504);
    final Sprite _eff = new Sprite(12, 12);
    _eff.installForDrawing();
    GameView.DEFENSE_GRID.drawTinted2(1, 1, 8421504);
    final Sprite _mjKb = new Sprite(12, 12);
    _mjKb.installForDrawing();
    GameView.CHEVRON.drawTinted2(1, 1, 8421504);
    _wab = new Sprite(128, 128);
    _wab.installForDrawing();
    GameView.WARNING.draw(0, 0);

    for (int var13 = 0; var13 < _wab.pixels.length; ++var13) {
      if (_wab.pixels[var13] != 0) {
        Drawing.addPixel(var13 % _wab.width, var13 / _wab.width, Drawing.WHITE, 128);
        _wab.pixels[var13] = _wab.pixels[var13] | -16777216;
      }
    }

    final Sprite clrVb = new Sprite(18, 12);
    clrVb.installForDrawing();
    GameView.HAMMER.drawTinted2(1, 1, 0x808080);
    Drawing.restoreContext();

    _b.f150(Drawing.WHITE);
    _fmb.f150(Drawing.WHITE);
    _eff.f150(Drawing.WHITE);
    _mjKb.f150(Drawing.WHITE);
    clrVb.f150(Drawing.WHITE);
    Menu._kjf = a113hl();

    IntroAnimation.spriteLoader = spriteLoader;
    Menu.buttonSprite = new NineSliceSprite(generateButtonSlices());
    Menu.ORB_COIN = SpriteResource.loadSprite(commonSpriteLoader, "basic", "orbcoin");
    a714jc(SpriteResource.loadSprite(commonSpriteLoader, "basic", JagexApplet.membershipLevel > 0 ? "unachieved" : "locked"));
    Menu.ACHIEVEMENTS = SpriteResource.loadSprites(spriteLoader, "", "achievements");
    Menu._gvsb = new Sprite[Menu.ACHIEVEMENTS.length];
    Menu._vha = new Sprite[Menu.ACHIEVEMENTS.length];
    Menu.ACHIEVEMENT_ICONS = new Sprite[Menu.ACHIEVEMENTS.length];

    GameView.SYSTEM_ICONS = SpriteResource.loadSprites(spriteLoader, "", "system_icons");
    a174ca(commonSpriteLoader, commonFontLoader, spriteLoader);
    GameUI._R = new Sprite(15, 15);
    Drawing.saveContext();
    GameUI._R.installForDrawing();
    Drawing.fillCircle(15, 15, 15, 65793);
    Drawing.fillCircle(15, 15, 5, 0);
    final Sprite _led = new Sprite(15, 20);
    _led.installForDrawing();
    Drawing.fillRect(0, 0, 15, 20, 65793);
    Drawing.fillCircle(0, -1, 5, 0);
    Drawing.fillCircle(0, 20, 5, 0);
    final Sprite var22 = SpriteResource.loadSprite(spriteLoader, "", "menu_pip");
    final Sprite _kfle = new Sprite(2 + var22.width, 2 + var22.height);
    _kfle.installForDrawing();
    var22.e326(1, 1, 65793);
    _bga = new Sprite(21, 22);
    _bga.installForDrawing();

    for (int var16 = 0; var16 < 20; ++var16) {
      final int var17 = var16 > 10 ? 20 - var16 : 3;
      Drawing.horizontalLine(11 - var17, 1 + var16, 2 * var17, 65793);
    }

    Drawing.restoreContext();
    _kfle.f150(2458760);
    _bga.f150(2458760);
    Menu._ok = _bga.copy();
    Menu._ok.b797();

  }

  private static void a874b(final Component<?> var0, final Component<?> var2) {
    if (_cka == null) {
      isChatboxSelected = false;
      final String var5;
      if (JagexApplet.cannotChat) {
        var5 = StringConstants.DOB_CHAT_DISABLED;
      } else if (JagexApplet.canOnlyQuickChat) {
        var5 = StringConstants.PRESS_F10_TO_QUICK_CHAT;
      } else {
        var5 = StringConstants.PRESS_TAB_TO_CHAT;
      }

      a790cq(_hoc, null, var5, 0);
      _cka = chatMessageLabel;
      chatMessageLabel._ab = -(8355711 & _hoc >> 1) + _hoc + ((16711422 & Component.UNSELECTED_LABEL._ab) >> 1);
      chatMessageLabel.mouseOverTextColor = -(8355711 & _hoc >> 1) + _hoc + ((Component.UNSELECTED_LABEL.mouseOverTextColor & 16711422) >> 1);
      _erj = Component._cgC;
      chatMessageLabel._qb = -(_hoc >> 1 & 8355711) + _hoc + (Component.UNSELECTED_LABEL._qb >> 1 & 8355711);
      _faX = new Component<>(null);
      _mbn = new Component<>(var2);
      _few = new Component<>(null);
      _abAb = new Component<>(null);
      chatMessageLabel = new Component<>(var0);
      chatMessageLabel.font = Component.CHAT_FONT;
      _abAb.addChild(chatMessageLabel);
      Component._cgC = new Component<>(_erj);
      _abAb.addChild(Component._cgC);
      HIDE_CHAT_TEMPORARILY_LABEL = new Component<>(Component.UNSELECTED_LABEL, StringConstants.TAB_HIDE_CHAT_TEMPORARILY);
      _cbl = new Component<>(Component.UNSELECTED_LABEL);
      _faX.addChild(_mbn);
      _faX.addChild(_few);
      _few.addChild(_abAb);
      _few.addChild(HIDE_CHAT_TEMPORARILY_LABEL);
      _few.addChild(_cbl);
    }
  }

  private static void a430cf(final boolean mouseNotYetHandled) {
    tickLobbyListStatuses();
    final int minJoinedPlayerCount = debugModeEnabled ? 0 : 2;
    final boolean canStartUnratedGame = unratedLobbyRoom != null && unratedLobbyRoom.joinedPlayerCount >= minJoinedPlayerCount;
    tickLobbyInterface(ticksSinceLastMouseEventAtStartOfTick > 50, mouseWheelRotation, mouseNotYetHandled, playingGame || spectatingGame, canStartUnratedGame);
//    if (ClientGameSession.isAutoPlaying && _cln % 50 == 0) {
//      C2SPacket.send58();
//    }

    if (findOpponentsButtonClicked) {
      C2SPacket.send58();
      findOpponentsButtonClicked = false;
    }

    if (returnToMainMenuClicked) {
      C2SPacket.leaveLobby();
      returnToMainMenuClicked = false;
    }

    if (_ucB) {
      Menu.showLobbyDialog(StringConstants.RATED_MEMBERS_ONLY, true, 1);
      _ucB = false;
    }
  }

  private static void tickLobbyInterface(final boolean mouseStill, final int mouseWheelRotation, boolean mouseNotYetHandled, final boolean inGame, final boolean canStartUnratedGame) {
    ClientLobbyRoom.currentTooltip = null;
    tickYouHaveBeenKickedDialog();
    mouseNotYetHandled = tickPopups(mouseNotYetHandled);
    tickLobbyRightPanel(mouseNotYetHandled, mouseStill, mouseWheelRotation, inGame, canStartUnratedGame);
    tickPlayerLists(mouseStill, mouseWheelRotation);
    a778cc(mouseNotYetHandled, mouseWheelRotation);
  }

  private static void tickPlayerLists(final boolean mouseStill, final int mouseWheelRotation) {
    final PlayerListEntry clickedEntry = tickFriendList(mouseStill, mouseWheelRotation);
    if (clickedEntry != null) {
      ContextMenu.openInstance.addPlayerItems(false);
      ContextMenu.openInstance.view.addItem(StringConstants.MU_LOBBY_FRIEND_RM, ContextMenu.ClickAction.REMOVE_FRIEND);
      ContextMenu.openInstance.view.positionRelativeToTarget(mousePressX, mousePressY);
    }
    tickIgnoreList(mouseStill, mouseWheelRotation);
  }

  private static void tickYouHaveBeenKickedDialog() {
    if (playerWhoKickedYou != null) {
      final String message = Strings.format(StringConstants.YOU_HAVE_BEEN_REMOVED_FROM_XS_GAME, playerWhoKickedYou);
      showYouHaveBeenKickedDialog = true;
      Component.YOU_HAVE_BEEN_KICKED_LABEL.label = message;
      final int width = JagexBaseApplet.screenBuffer.width;
      final int height = JagexBaseApplet.screenBuffer.height;
      final int messageHeight = Component.YOU_HAVE_BEEN_KICKED_LABEL.font.measureParagraphHeight(message, 272, Component.YOU_HAVE_BEEN_KICKED_LABEL._Y);
      final int var4 = height / 2 - (messageHeight / 2) - 110 + 7;
      Component.YOU_HAVE_BEEN_KICKED_DIALOG.setBounds((width - 320) / 2, var4, 320, -(2 * var4) + (height - 120));
      Component.YOU_HAVE_BEEN_KICKED_DIALOG.nineSliceSprites = Component.createGradientOutlineSprites(Component.YOU_HAVE_BEEN_KICKED_DIALOG.height, 0xb0b0b0, 0x808080, 0x202020);
      Component.YOU_HAVE_BEEN_KICKED_LABEL.setBounds(24, 16, Component.YOU_HAVE_BEEN_KICKED_DIALOG.width - 24 - 24, Component.YOU_HAVE_BEEN_KICKED_DIALOG.height - 24 - 20);
      Component.YOU_HAVE_BEEN_KICKED_OK_BUTTON.setBounds(120, Component.YOU_HAVE_BEEN_KICKED_DIALOG.height - 20 - 24, 80, 24);
      playerWhoKickedYou = null;
    }
  }

  private static void a877u(final boolean inGame) {
    if (Menu.currentMenu == Menu.Id.INTRODUCTION) {
      IntroAnimation.render();
    } else if (ClientGameSession.playSession != null) {
      a430u(inGame);
    } else if (ClientGameSession.spectateSession == null) {
      f423fr();
    } else {
      a430u(inGame);
    }
  }

  private static void a430u(final boolean var1) {
    if (ClientGameSession.playSession != null) {
      ClientGameSession.playSession.render();
    }

    if (ClientGameSession.spectateSession != null) {
      ClientGameSession.spectateSession.render();
    }

    if (_cjx && _tli) {
      final boolean var2 = playingGame && isChatboxSelected;
      a813qr(var1 && !var2 && a154vc());
      if (playingGame && isChatboxSelected) {
        a877rad(var1 && a154vc());
      }
    }
  }

  public static void clearChatMessages() {
    for (int i = 0; i < chatMessageCount; ++i) {
      chatMessages[i] = null;
    }
    chatMessageCount = 0;
  }

  private static void a985no(final Canvas canvas) {
    if (JagexApplet.loadStage < LoadStage.REQUEST_GAME_STRINGS) {
      final boolean var3 = JagexBaseApplet._oqe;
      if (JagexBaseApplet._oqe) {
        JagexBaseApplet._oqe = false;
      }
      drawLoadingScreen(loadingScreenMessage(), loadingScreenPercent(), var3);
    } else if (!JagexLogoIntroAnimation.isFinished()) {
      Drawing.clear();
      JagexLogoIntroAnimation.draw();
      JagexBaseApplet.paint(canvas);
    } else if (connectionState == ConnectionState.NOT_CONNECTED) {
      a630gr(false);
      JagexBaseApplet.paint(canvas);
    } else {
      CommonUI.drawLoading();
      JagexBaseApplet.paint(canvas);
    }
  }

  private static String loadingScreenMessage() {
    if (JagexApplet.loadStage < 2) {
      return connectingToUpdateServerMessage;
    } else if (ResourceLoader.COMMON_STRINGS != null) {
      return ResourceLoader.COMMON_STRINGS.isIndexLoaded() ? loadingTextMessage : waitingForTextMessage;
    } else if (!ResourceLoader.COMMON_SPRITES.isIndexLoaded()) {
      return StringConstants.WAITING_FOR_GRAPHICS;
    } else if (!ResourceLoader.COMMON_SPRITES.loadGroupData("commonui")) {
      return StringConstants.LOADING_GRAPHICS + " - " + ResourceLoader.COMMON_SPRITES.percentLoaded("commonui") + "%";
    } else if (!ResourceLoader.COMMON_FONTS.isIndexLoaded()) {
      return StringConstants.WAITING_FOR_FONTS;
    } else if (!ResourceLoader.COMMON_FONTS.loadGroupData("commonui")) {
      return StringConstants.LOADING_FONTS + " - " + ResourceLoader.COMMON_FONTS.percentLoaded("commonui") + "%";
    } else if (ResourceLoader.JAGEX_LOGO_ANIMATION.isIndexLoaded()) {
      return ResourceLoader.JAGEX_LOGO_ANIMATION.loadAllGroups()
          ? StringConstants.PLEASE_WAIT
          : StringConstants.LOADING_EXTRA_DATA + " - " + ResourceLoader.JAGEX_LOGO_ANIMATION.percentLoaded() + "%";
    } else {
      return StringConstants.WAITING_FOR_EXTRA_DATA;
    }
  }

  private static int loadingScreenPercent() {
    if (JagexApplet.loadStage < 2) {
      return 0;
    } else if (JagexApplet.langId == 0) {
      if (!ResourceLoader.COMMON_SPRITES.isIndexLoaded()) {
        return 20;
      } else if (!ResourceLoader.COMMON_SPRITES.loadGroupData("commonui")) {
        return 40;
      } else if (!ResourceLoader.COMMON_FONTS.isIndexLoaded()) {
        return 50;
      } else if (!ResourceLoader.COMMON_FONTS.loadGroupData("commonui")) {
        return 60;
      } else if (!ResourceLoader.JAGEX_LOGO_ANIMATION.isIndexLoaded()) {
        return 70;
      } else if (ResourceLoader.JAGEX_LOGO_ANIMATION.loadAllGroups()) {
        return 100;
      } else {
        return 80;
      }
    } else {
      if (ResourceLoader.COMMON_STRINGS != null) {
        if (!ResourceLoader.COMMON_STRINGS.isIndexLoaded()) {
          return 14;
        } else if (!ResourceLoader.COMMON_STRINGS.hasGroup("")) {
          return 29;
        } else if (!ResourceLoader.COMMON_STRINGS.loadGroupData("")) {
          return 29;
        }
      }
      if (!ResourceLoader.COMMON_SPRITES.isIndexLoaded()) {
        return 43;
      } else if (!ResourceLoader.COMMON_SPRITES.loadGroupData("commonui")) {
        return 57;
      } else if (!ResourceLoader.COMMON_FONTS.isIndexLoaded()) {
        return 71;
      } else if (!ResourceLoader.COMMON_FONTS.loadGroupData("commonui")) {
        return 80;
      } else if (!ResourceLoader.JAGEX_LOGO_ANIMATION.isIndexLoaded()) {
        return 82;
      } else if (ResourceLoader.JAGEX_LOGO_ANIMATION.loadAllGroups()) {
        return 100;
      } else {
        return 86;
      }
    }
  }

  public static void removeFocusLossDetectingCanvas() {
    if (fullScreenCanvas != null) {
      JagexApplet.detachFromCanvas(fullScreenCanvas);
      fullScreenCanvas.repeatedlyTryToExitFullScreen();
      fullScreenCanvas = null;
      canvas.requestFocus();
    }
  }

  private static void tickLobbyRightPanel(final boolean mouseNotYetHandled, final boolean var1, final int var4, final boolean inGame, final boolean canStartUnratedGame) {
    if (!inGame
        && (Component.lastLayoutWidth != Drawing.width || Component.lastLayoutHeight != Drawing.height)
        && Drawing.height == JagexBaseApplet.screenBuffer.height
        && Drawing.width == JagexBaseApplet.screenBuffer.width) {
      if (unratedLobbyRoom != null) {
        layoutLobbyRoom(false);
      } else if (ratedLobbyRoom == null) {
        Component.layoutLobbyBrowser();
      } else {
        layoutLobbyRoom(true);
      }
    }

    if (inGame) {
      _tmh = _gnt;
    } else {
      _tmh = (Component.lastLayoutWidth - SCREEN_WIDTH) / 2;
    }

    tickLobbyTransitionCounters(inGame);
    if (lobbyBrowserTransitionCounter > 0) {
      tickLobbyBrowser(mouseNotYetHandled, inGame);
    }

    Component.WAITING_TO_START_LABEL.textAlignment = Font.HorizontalAlignment.CENTER;
    Component.WAITING_TO_START_LABEL._kb = Component.LABEL_DARK_1._kb;
    if (ratedLobbyRoomTransitionCounter > 0) {
      tickRatedLobbyRoom(mouseNotYetHandled, inGame);
    }
    if (unratedLobbyRoomTransitionCounter > 0) {
      tickUnratedLobbyRoom(mouseNotYetHandled, canStartUnratedGame, inGame);
    }

    if (invitePlayersDialogOpen && unratedLobbyRoom.maxPlayerCount <= unratedLobbyRoom.joinedPlayerCount) {
      Component.INVITE_PLAYER_LIST_SCROLL_PANE.viewport.label = StringConstants.GAME_FULL;
      Component.INVITE_PLAYER_LIST_PANEL.enabled = false;
      crunch(Component.INVITE_PLAYER_LIST_SCROLL_PANE.content);
    } else {
      Component.INVITE_PLAYER_LIST_PANEL.enabled = true;
      Component.INVITE_PLAYER_LIST_SCROLL_PANE.viewport.label = null;
      tickLobbyRoomPlayersTable(var4, Component.INVITE_PLAYER_LIST_SCROLL_PANE, var1);
    }

    ClientLobbyRoom.tick(var1, var4);
    tickLobbyRoomPlayersTable(var4, Component.JOINED_PLAYERS_TABLE, var1);
    ++_ssw;
  }

  private static String playerRemovalReasonMessage(final int which, final String who) {
    return switch (which) {
      case LobbyPlayer.Status.ENTERED_GAME -> Strings.format(StringConstants.MU_X_ENTERED_GAME, who);
      case LobbyPlayer.Status.JOINED_YOUR_GAME -> Strings.format(StringConstants.MU_X_JOINED_YOUR_GAME, who);
      case LobbyPlayer.Status.ENTERED_OTHER_GAME -> Strings.format(StringConstants.MU_X_ENTERED_OTHER_GAME, who);
      case LobbyPlayer.Status.LEFT_LOBBY -> Strings.format(StringConstants.MU_X_LEFT_LOBBY, who);
      case LobbyPlayer.Status.LOST_CON -> Strings.format(StringConstants.MU_X_LOST_CON, who);
      case LobbyPlayer.Status.CANNOT_JOIN_FULL -> Strings.format(StringConstants.MU_X_CANNOT_JOIN_FULL, who);
      case LobbyPlayer.Status.CANNOT_JOIN_IN_PROGRESS -> Strings.format(StringConstants.MU_X_CANNOT_JOIN_IN_PROGRESS, who);
      case LobbyPlayer.Status.DECLINED_INVITE -> Strings.format(StringConstants.MU_X_DECLINED_INVITE, who);
      case LobbyPlayer.Status.WITHDREW_REQUEST -> Strings.format(StringConstants.MU_X_WITHDREW_REQUEST, who);
      case LobbyPlayer.Status.KICKED -> Strings.format(StringConstants.MU_X_REMOVED, who);
      case LobbyPlayer.Status.DROPPED_OUT -> Strings.format(StringConstants.MU_X_DROPPED_OUT, who);
      default -> null;
    };
  }

  private static void tickLobbyRoomPlayersTable(final int var0, final ScrollPane<LobbyPlayer> var2, final boolean var3) {
    final int requiredRating = 0;
    final int requiredRatedGames = 0;
    final int requiredUnlockedOptions = 0;

    final boolean var31 = var2.processScrollInput(var3, var2 == currentContextMenuParent, Component.LABEL_HEIGHT * 2 + 4, 4 * (2 + Component.LABEL_HEIGHT) * var0);
    final List<LobbyPlayer> var32 = var2.content.children;
    LobbyPlayer var33 = null;

    for (final LobbyPlayer player : var32) {
      boolean var13 = false;
      if (player.children == null) {
        player._Ab = new Component<>(Component.UNSELECTED_LABEL_DARK_2);
        player.addChild(player._Ab);
        player._Ob = new Component<>(Component.UNSELECTED_LABEL_DARK_1);
        player.addChild(player._Ob);

        player._Ob.textAlignment = Font.HorizontalAlignment.RIGHT;
        player._xb = new Component<>(Component.LABEL);
        player.addChild(player._xb);
        player.recursivelySet_H();
        player._Jb = new Component<>(Component.GREEN_BUTTON);
        player.addChild(player._Jb);
        player._Mb = new Component<>(Component.RED_BUTTON);
        var13 = true;
        player.addChild(player._Mb);
      }

      player._Ab.label = null;
      final Component<?> var14 = player._Ab;
      player._Ab.height = 0;
      player._Ob.label = null;
      var14.width = 0;
      final Component<?> var15 = player._Ob;
      player._Ob.height = 0;
      var15.width = 0;
      player._Jb.label = null;
      player._Jb.height = 0;
      final Component<?> var16 = player._Jb;
      player._Mb.label = null;
      var16.width = 0;
      player._Mb.height = 0;
      final Component<?> var17 = player._Mb;
      player._xb.label = null;
      var17.width = 0;
      final Component<?> var18 = player._xb;
      player._xb.height = 0;
      var18.width = 0;
      player.width = var2.content.width;
      int var19 = 0;
      String var20 = player.playerDisplayName;
      int var21 = 72;
      if (var2 == Component.INVITE_PLAYER_LIST_SCROLL_PANE) {
        var21 += 42;
      }

      var20 = player._Ab.font.truncateWithEllipsisToFit(var20, var21);
      assert var20 != null;
      final boolean wasTruncated = !var20.equals(player.playerDisplayName);
      if (player.crown >= 4) {
        var20 = "<img=" + (Component.CROWNS_COUNT + (player.crown - 4)) + ">" + var20;
      } else if (player.crown > 0) {
        var20 = "<img=" + (player.crown - 1) + ">" + var20;
      }

      player._Ab.label = var20;
      int var24;
      if (player.isInMap()) {
        int var23 = 0xffcc66;
        var24 = Drawing.WHITE;
        if (player.rating < requiredRating || player.ratedGameCount < requiredRatedGames || (~player.unlockedOptionsBitmap & requiredUnlockedOptions) > 0) {
          var23 = 0x806633;
          var24 = 0x808080;
        }

        player._Ab.textColor = player._Ob.textColor = player._Jb.textColor = var23;
        player._Ab.mouseOverTextColor = player._Ab._qb = player._Ab._ab = var24;
        player._Ob.mouseOverTextColor = player._Ob._qb = player._Ob._ab = var24;
        player._Jb.mouseOverTextColor = player._Jb._qb = player._Jb._ab = var24;
        if (Component.JOINED_PLAYERS_TABLE == var2) {
          if (!unratedLobbyRoom.hasStarted) {
            player._Mb.label = StringConstants.KICK;
          }
        } else if (player.inviteSent) {
          player._Ab.label = Strings.format(StringConstants.INVITING_X, var20);
          player._Mb.label = StringConstants.CANCEL;
        } else if (player.joinRequestReceived) {
          player._Ab.label = Strings.format(StringConstants.X_WANTS_TO_JOIN, var20);
          player._Jb.label = StringConstants.ACCEPT;
          player._Mb.label = StringConstants.REJECT;
        } else {
          player._Jb.label = StringConstants.INVITE;
        }

        int var25 = 0;
        if (unratedLobbyRoom != null && isCurrentRoomOwner() && localPlayerId != player.playerId) {
          int var26;
          if (player._Jb.label != null) {
            var26 = player._Jb.e474() + 2 * _tga;
            player._Jb.setBounds(var25, var19, var26, Component.LABEL_HEIGHT);
            var25 += var26;
          }

          if (player._Mb.label != null) {
            if (var2 == Component.JOINED_PLAYERS_TABLE) {
              var26 = 40;
            } else {
              var26 = player._Mb.e474() + _tga * 2;
            }

            player._Mb.setBounds(var25, var19, var26, Component.LABEL_HEIGHT);
            var25 += var26;
          }
        }

        player._Ab.setBounds(var25, var19, -var25 + (player.width - (42)), Component.LABEL_HEIGHT);
        player._Ob.label = Integer.toString(player.rating);
        player._Ob.setBounds(player.width - 40, var19, 40, Component.LABEL_HEIGHT);
        if (player._Ab.isMouseOverTarget && wasTruncated) {
          ClientLobbyRoom.currentTooltip = player.playerDisplayName;
        }

        var19 += Component.LABEL_HEIGHT;
      }

      final String var34 = playerRemovalReasonMessage(player.status, var20);
      if (var34 != null) {
        var24 = player._xb.font.breakLines(var34, player.width - _tga - _tga);
        player._xb.label = var34;
        player._xb._I = 256 * player.statusTimer / STATUS_MESSAGE_TIMEOUT_DURATION;
        player._xb.setBounds(_tga, var19, -(2 * _tga) + player.width, var24 * Component.LABEL_HEIGHT);
        var19 += Component.LABEL_HEIGHT * var24;
      }

      if (!var31) {
        player._gb = var19 - player.height;
      }

      if (var13) {
        var2.content.placeAfter(var33, player);
      }

      if (player.clickButton != MouseState.Button.NONE && player.isInMap()) {
        if (player._Jb.clickButton == MouseState.Button.NONE) {
          if (player._Mb.clickButton == MouseState.Button.NONE) {
            a641tvc(player, var2, mousePressX, mousePressY);
          } else {
            C2SPacket.kickFromGame(player.playerId);
          }
        } else {
          C2SPacket.inviteToGame(player.playerId);
        }
      }

      if (player.isMouseOver && player.isInMap()) {
        String message = null;
        if (player.playerId == localPlayerId) {
          if (player.ratedGameCount < requiredRatedGames) {
            final int additionalRequiredRatedGames = requiredRatedGames - player.ratedGameCount;
            message = Strings.format(StringConstants.GAMEOPT_YOU_NEED_RATED_GAMES, null, Integer.toString(additionalRequiredRatedGames));
          } else if (player.rating < requiredRating) {
            message = Strings.format(StringConstants.GAMEOPT_YOU_NEED_RATING, null, Integer.toString(requiredRating));
          } else if ((requiredUnlockedOptions & ~player.unlockedOptionsBitmap) != 0) {
            message = StringConstants.GAMEOPT_YOU_HAVENT_UNLOCKED;
          }
        } else if (player.ratedGameCount < requiredRatedGames) {
          final int additionalRequiredRatedGames = requiredRatedGames - player.ratedGameCount;
          if (additionalRequiredRatedGames == 1) {
            message = Strings.format(StringConstants.GAMEOPT_PLAYER_NEEDS_1_RATED_GAME, var20);
          } else {
            message = Strings.format(StringConstants.GAMEOPT_PLAYER_NEEDS_RATED_GAMES, var20, Integer.toString(additionalRequiredRatedGames));
          }
        } else if (player.rating < requiredRating) {
          message = Strings.format(StringConstants.GAMEOPT_PLAYER_NEEDS_RATING, var20, Integer.toString(requiredRating));
        } else if ((requiredUnlockedOptions & ~player.unlockedOptionsBitmap) != 0) {
          message = Strings.format(StringConstants.GAMEOPT_PLAYER_HASNT_UNLOCKED, var20);
        }

        if (message != null) {
          message = "<col=A00000>" + message;

          if (Component.JOINED_PLAYERS_TABLE == var2 && isCurrentRoomOwner()) {
            message = message + "<br>" + Strings.format(StringConstants.GAMEOPT_TRY_CHANGING_1);
          } else {
            message = message + "<br>" + Strings.format(StringConstants.GAMEOPT_NEED_CHANGING_1);
          }

          if (Component.JOINED_PLAYERS_TABLE == var2 && !isCurrentRoomOwner()) {
            final String var37 = unratedLobbyRoom.ownerName;
            message = message + "<br>" + Strings.format(StringConstants.GAMEOPT_MIGHT_CHANGE, var37);
          }

          ClientLobbyRoom.currentTooltip = message;
        }
      }

      var33 = player;
    }

  }

  private static void tickRatedLobbyRoom(final boolean mouseNotYetHandled, final boolean inGame) {
    layoutLobbyRoomPanels(ratedLobbyRoomTransitionCounter);

    if (ratedLobbyRoom != null) {
      final Component<?> var4 = Component.INVITE_PLAYERS_BUTTON;
      Component.INVITE_PLAYERS_BUTTON.height = 0;
      var4.width = 0;
      final Component<?> var5 = Component.FIND_OPPONENTS_BUTTON;
      Component.FIND_OPPONENTS_BUTTON.height = 0;
      var5.width = 0;
      final Component<?> var6 = Component.WAITING_TO_START_LABEL;
      Component.WAITING_TO_START_LABEL.height = 0;
      var6.width = 0;
      Component.GAME_OWNER_HEADING.label = StringConstants.RATED_GAME.toUpperCase();
      final int var7 = (2 + Component.GAME_INFO_CONTAINER.width) / 2;
      Component.WAITING_TO_START_LABEL.setBounds(0, Component.GAME_INFO_CONTAINER.height - 40, var7 - 2, 40);
      Component.FIND_OPPONENTS_BUTTON.setBounds(var7, Component.GAME_INFO_CONTAINER.height - 40, -var7 + Component.GAME_INFO_CONTAINER.width, 40);
      boolean var8;
      int var9;
      int var10;
      var8 = false;
      var9 = 0;

      for (var10 = 0; var10 < NUM_PLAYERS_OPTION_VALUES.length; ++var10) {
        final boolean var11 = (playerSeatsFilledBitmap[var10 / 8] & 1 << (var10 & 7)) != 0;
        if (var11) {
          ++var9;
        }

        if (var11 && NUM_PLAYERS_OPTION_VALUES[var10] != 2) {
          var8 = true;
          break;
        }
      }

      if (var9 == 0) {
        var8 = true;
      }

      Component.FIND_OPPONENTS_BUTTON.enabled = true;
      String var12;
      if (justRecievedRoomDetailsFromServer) {
        Component.RETURN_TO_LOBBY_BUTTON.enabled = false;
        Component.RATED_GAME_TIPS_LABEL.label = StringConstants.RATED_GAME_TIPS;
        var12 = var8 ? StringConstants.SEARCHING_FOR_OPPONENTS : StringConstants.SEARCHING_FOR_OPPONENT;
        var10 = Component.WAITING_TO_START_LABEL.font.measureLineWidth(var12) + 3 * Component.WAITING_TO_START_LABEL.font.getAdvanceWidth('.');
        Component.WAITING_TO_START_LABEL._kb = (-var10 + Component.WAITING_TO_START_LABEL.width) / 2;
        if ((_ssw & 48) == 16) {
          var12 = var12 + ".";
        }

        Component.WAITING_TO_START_LABEL.textAlignment = Font.HorizontalAlignment.LEFT;
        if ((_ssw & 48) == 32) {
          var12 = var12 + "..";
        }

        if ((_ssw & 48) == 48) {
          var12 = var12 + "...";
        }

        Component.WAITING_TO_START_LABEL.label = var12;
        Component.FIND_OPPONENTS_BUTTON.label = StringConstants.CANCEL.toUpperCase();
      } else {
        Component.FIND_OPPONENTS_BUTTON.label = (!var8 ? StringConstants.FIND_OPPONENT_SINGULAR : StringConstants.FIND_OPPONENTS).toUpperCase();
        Component.RATED_GAME_TIPS_LABEL.label = Strings.format(!var8 ? StringConstants.RATED_GAME_TIPS_SETUP_SINGULAR : StringConstants.INFO_RATED_GAME, Component.FIND_OPPONENTS_BUTTON.label);
        Component.WAITING_TO_START_LABEL.label = StringConstants.WAITING_TO_START_HINT;
        Component.RETURN_TO_LOBBY_BUTTON.enabled = true;
        _wgd = true;
        if (_peD != null) {
          _wgd = false;
          _sbh = false;
          if (_lrc == null) {
            _lrc = new byte[GAMEOPT_CHOICES_COUNTS.length];
            _ekF = new boolean[GAMEOPT_CHOICES_COUNTS.length];
          }

          for (var9 = 0; var9 < GAMEOPT_CHOICES_COUNTS.length; ++var9) {
            _ekF[var9] = false;
          }

          a068js(-1, GAMEOPT_CHOICES_COUNTS.length, 0, true, -1, -1);
          if (adminLevel >= 2 && keysDown[12]) {
            _wgd = true;
          }
        }

        if (!_wgd) {
          Component.FIND_OPPONENTS_BUTTON.enabled = false;
          if (Component.FIND_OPPONENTS_BUTTON.isMouseOver) {
            var12 = null;
            if (_sbh) {
              var12 = "<col=A00000>" + StringConstants.MU_OPTIONS_PLAYERS + "</col>";
            }

            for (var10 = 0; GAMEOPT_CHOICES_COUNTS.length > var10; ++var10) {
              if (_ekF[var10]) {
                final String var13 = "<col=A00000>" + StringConstants.GAMEOPT_LABELS[var10] + "</col>";
                if (var12 == null) {
                  var12 = var13;
                } else {
                  var12 = var12 + ", " + var13;
                }
              }
            }

            ClientLobbyRoom.currentTooltip = "<col=A00000>" + StringConstants.GAMEOPT_NO_VALID_COMBOS + "<br>" + StringConstants.GAMEOPT_PLEASE_TRY_CHANGING + var12;
          }
        }
      }
    }

    lobbyRoomLeftPanel.rootProcessMouseEvents(mouseNotYetHandled && !inGame && !invitePlayersDialogOpen);
    Component.GAME_INFO_CONTAINER.rootProcessMouseEvents(mouseNotYetHandled && !inGame && !invitePlayersDialogOpen);
    invitePlayersDialog.rootProcessMouseEvents(mouseNotYetHandled && !inGame && invitePlayersDialogOpen);
    lobbyRoomTabbedPlayerList.view.f487();
    if (ratedLobbyRoom != null) {
      if (Component.RETURN_TO_LOBBY_BUTTON.clickButton != MouseState.Button.NONE) {
        C2SPacket.returnToLobby();
      }

      if (Component.FIND_OPPONENTS_BUTTON.clickButton != MouseState.Button.NONE) {
        if (justRecievedRoomDetailsFromServer) {
          C2SPacket.acknowledgeRatedRoomInfo();
        } else {
          C2SPacket.updateRatedGamePreferences(playerSeatsFilledBitmap, maxAiPlayersSelection, allowSpectateSelection, gameoptChoicesBitmap);
        }
      }

      a626sc(true, ratedLobbyRoom);
    }
  }

  private static boolean tickPopups(boolean mouseNotYetHandled) {
    if (isPopupOpen()) {
      tickQuickChat(mouseNotYetHandled);
      if (ContextMenu.openInstance != null && ContextMenu.openInstance.tick(mouseNotYetHandled)) {
        dismissContextMenu();
        mouseNotYetHandled = false;
      }

      AddOrRemovePlayerPopup.tick(mouseNotYetHandled);
      ReportAbuseDialog.tick(mouseNotYetHandled);
      mouseNotYetHandled = false;
    }

    return mouseNotYetHandled;
  }

  private static void b150af() {
    g150fp();
    i150ak();
  }

  private static void g150fp() {
    if (_pgJ > 0) {
      --_pgJ;
    }

    if (_dmgm != JagexBaseApplet.screenBuffer.height) {
      _flf += -_dmgm + JagexBaseApplet.screenBuffer.height;
      _dmgm = JagexBaseApplet.screenBuffer.height;
    }

    if (_pgJ > 0) {
      a150bq();
    }

  }

  public static void a150bq() {
    final int var0 = 400;
    final int var1 = var0 - _pgJ * _pgJ;
    final int var3 = _flf + var1 * (_dmgm - _flf) / var0;
    Component._tgc.setBounds(_tmh, var3, SCREEN_WIDTH, 120);
    a370qc(_dmgm - 24, _tga, ClientLobbyRoom._qob);
  }

  private static boolean processChatKeyInput() {
    if (QuickChatHelpPanel.openInstance != null && lastTypedKeyCode == KeyState.Code.ESCAPE) {
      QuickChatHelpPanel.openInstance = null;
      return true;
    } else if (isChatboxSelected) {
      if (canOnlyQuickChat) {
        return false;
      } else {
        boolean consumed = processChatKeyboardInput();
        if (lastTypedKeyCode == KeyState.Code.TAB || lastTypedKeyCode == KeyState.Code.ENTER) {
          consumed = true;
          isChatboxSelected = false;
        }

        if (lastTypedKeyCode == KeyState.Code.ESCAPE) {
          discardTypedChatMessage();
          consumed = true;
          isChatboxSelected = false;
        }

        return consumed;
      }
    } else if (isQuickChatOpen || lastTypedKeyCode == KeyState.Code.F9 || lastTypedKeyCode == KeyState.Code.F10 || lastTypedKeyCode == KeyState.Code.F11) {
      return processQuickChatKeyInput();
    } else if (lastTypedKeyCode == KeyState.Code.TAB && !canOnlyQuickChat) {
      isChatboxSelected = true;
      return true;
    } else {
      return false;
    }
  }

  private static void loadJpegImages(final ResourceLoader loader) {
    STAR_FIELD = loadJpgSprite(loader, "starfield");
    WIN_SPRITE = loadJpgSprite(loader, "win");
    LOSE_SPRITE = loadJpgSprite(loader, "lose");
  }

  private static void a813qr(final boolean var0) {
    a540ta(var0);
    a877im(var0);
    a430mj(var0);
  }

  private static void a877im(boolean var0) {
    if (isPopupOpen()) {
      var0 = false;
    }

    GameUI.a893(var0);
    b150lc();
  }

  private static void a174ca(final ResourceLoader var1, final ResourceLoader var2, final ResourceLoader spriteLoader) {
    final String[] gameoptSpritesItems = {null, "turntime_icons", "game_icons", "galaxysize_icons", "type_icons"};
    final Sprite[][] gameoptSprites = new Sprite[gameoptSpritesItems.length][];
    ClientLobbyRoom.GAMEOPT_SPRITES = gameoptSprites;

    for (int i = 0; i < gameoptSpritesItems.length; ++i) {
      if (gameoptSpritesItems[i] != null) {
        gameoptSprites[i] = SpriteResource.loadSprites(spriteLoader, "", gameoptSpritesItems[i]);
      }
    }

    StringConstants.MU_OPTIONS_PLAYERS = StringConstants.TEXT_HUMAN_PLAYERS;
    Component.a469(var1, var2, gameoptSprites);
    LOBBY_ICON = SpriteResource.loadSprite(spriteLoader, "", "lobbyicon");
    _hmo = false;
    _peD = _msc;
    _imb = new int[3];
    chatMessages = new ChatMessage[3000];
    chatMessageCount = 0;
    _kpi = 200;
  }

  private static void a778cf(final boolean var2) {
    final String var6;
    if (currentChatChannel == Channel.PRIVATE) {
      var6 = StringConstants.SEND_PM;
    } else {
      var6 = StringConstants.MESSAGE_GAME;
    }

    a500er(var6, var2, Component._rcl);
  }

  private static void a500er(final String var2, final boolean var4, final int var6) {
    if (Component._taio.clickButton != MouseState.Button.NONE && !cannotChat) {
      if (JagexApplet.canOnlyQuickChat) {
        ContextMenu.sendChannelMessageQC();
      } else {
        isChatboxSelected = true;
      }
    }

    if (isChatboxSelected) {
      a411ca(var2, var6);
      _faX.rootProcessMouseEvents(var4);
      if (var4) {
        if (HIDE_CHAT_TEMPORARILY_LABEL.clickButton != MouseState.Button.NONE) {
          isChatboxSelected = false;
        }

        if (_cbl.clickButton != MouseState.Button.NONE) {
          discardTypedChatMessage();
          isChatboxSelected = false;
        }
      }
    }

  }

  private static void a411ca(final String var3, final int var4) {
    _mbn.label = var3;
    if (currentChatChannel == Channel.PRIVATE) {
      _cbl.label = ReportAbuseDialog._Kb;
    } else {
      _cbl.label = StringConstants.ESC_CANCEL_THIS_LINE;
    }

    final short var9 = 495;
    final byte var10 = 5;
    _abAb.setBounds(5, var10, 485, Component.LABEL_HEIGHT);
    chatMessageLabel.setBounds(0, 0, -Component._cgC.width + _abAb.width, Component.LABEL_HEIGHT);
    Component._cgC.setBounds(chatMessageLabel.width, 0, Component._cgC.width, Component.LABEL_HEIGHT);
    final int var13 = var10 + Component.LABEL_HEIGHT + var4;
    HIDE_CHAT_TEMPORARILY_LABEL.setBounds(5, var13, HIDE_CHAT_TEMPORARILY_LABEL.e474(), Component.LABEL_HEIGHT);
    final int var11 = _cbl.e474();
    _cbl.setBounds(-var11 + (490), var13, var11, Component.LABEL_HEIGHT);
    int var12 = 5 + var13 + Component.LABEL_HEIGHT;
    _mbn.setBounds(0, 0, var9, 20);
    _few.setBounds(0, 20, var9, var12);
    _few.nineSliceSprites = Component.createGradientOutlineSprites(_few.height, 11579568, 8421504, 2105376);
    var12 += 20;
    _faX.setBounds(73, -(var12 / 2) + 180, var9, var12);
  }

  private static void a111ph() {
    if (cannotChat) {
      DobToEnableChatForm.instance.keyTyped(lastTypedKeyCode, lastTypedKeyChar);
    } else if (isQuickChatOpen) {
      processQuickChatKeyInput();
    } else if (!a330ms() && !JagexApplet.canOnlyQuickChat) {
      processChatKeyboardInput();
    }
  }

  private static boolean a330ms() {
    final boolean var3 = ratedLobbyRoom != null;
    if (lastTypedKeyCode == KeyState.Code.F9 && TutorialAI2.a051(JagexApplet._uof, _cvco, _D, _bqe)) {
      return (_D == Channel.PRIVATE || !var3) && ContextMenu.autoRespond(_bqe, _cvco, JagexApplet._uof, _D);
    } else if (lastTypedKeyCode != KeyState.Code.F10) {
      if (lastTypedKeyCode == KeyState.Code.F11 && _cvcn) {
        if (_tlr == Channel.PRIVATE && !a788(vm_._aco, vm_._ahS)) {
          return false;
        } else if (_tlr != Channel.PRIVATE && var3) {
          return false;
        } else {
          if (b154chmr()) {
            C2SPacket.sendQuickChatMessage(_tlr, vm_._ahS, _vsd);
          }

          return true;
        }
      } else {
        return false;
      }
    } else if (var3) {
      return false;
    } else {
      ContextMenu.sendChannelMessageQC();
      return true;
    }
  }

  private static Sprite[] generateButtonSlices() {
    final Sprite[] slices = new Sprite[]{new Sprite(10, 10), new Sprite(10, 10), new Sprite(10, 10), new Sprite(10, 10), null, new Sprite(10, 10), new Sprite(10, 10), new Sprite(10, 10), new Sprite(10, 10)};
    Drawing.saveContext();
    slices[0].installForDrawing();
    final int color = 0x3ca4a7;
    a229ch(color, 10, 30, 0, 0, color, 30);
    slices[1].installForDrawing();
    a229ch(color, 10, 30, -10, 0, color, 30);
    slices[2].installForDrawing();
    a229ch(color, 10, 30, -20, 0, color, 30);
    slices[3].installForDrawing();
    a229ch(color, 10, 30, 0, -10, color, 30);
    slices[5].installForDrawing();
    a229ch(color, 10, 30, -20, -10, color, 30);
    slices[6].installForDrawing();
    a229ch(color, 10, 30, 0, -21, color, 30);
    slices[7].installForDrawing();
    a229ch(color, 10, 30, -10, -22, color, 30);
    slices[8].installForDrawing();
    a229ch(color, 10, 30, -20, -21, color, 30);
    Drawing.restoreContext();
    return slices;
  }

  public static void a229ch(final int color1, final int var2, final int var3, final int var4, final int var5, final int color2, final int var7) {
    if (var2 * 2 <= var3) {
      final int[] bounds = new int[4];
      Drawing.saveBoundsTo(bounds);
      Drawing.horizontalLine(var2 + var4, var5, var3 - 2 * var2, color1);
      Drawing.horizontalLine(var2 + var4, var5 + var7, var3 - (2 * var2), color2);
      Drawing.expandBoundsToInclude(var4, var5, var4 + var2, var5 + var2);
      Drawing.strokeCircle(var2 + var4, var5 + var2, var2, color1);
      Drawing.restoreBoundsFrom(bounds);
      Drawing.expandBoundsToInclude(var4 + (var3 - var2), var5, var4 + var3, var5 + var2);
      Drawing.strokeCircle(var3 - var2 + (var4 - 1), var5 + var2, var2, color1);
      Drawing.restoreBoundsFrom(bounds);
      Drawing.expandBoundsToInclude(var4, var7 - var2 + var5, var4 + var2, var5 + var7);
      Drawing.strokeCircle(var4 + var2, var7 + var5 - var2 - 1, var2, color2);
      Drawing.restoreBoundsFrom(bounds);
      Drawing.expandBoundsToInclude(-var2 + var3 + var4, var5 + var7 - var2, var4 + var3, var5 + var7);
      Drawing.strokeCircle(var4 - 1 - (var2 - var3), var7 + var5 - (var2 + 1), var2, color2);
      Drawing.restoreBoundsFrom(bounds);

      for (int i = 0; i < var7 - 20; ++i) {
        final int color3 = Drawing.alphaOver(color2, color1, 256 * i / (var7 - 20));
        Drawing.setPixel(var4, var2 + var5 + i, color3);
        Drawing.setPixel(var3 + var4 - 1, i + var5 + var2, color3);
      }
    }
  }

  private static boolean recieveS2cPacket() {
    if (loginState == LoginState.LOGGED_IN) {
      if (nextS2cPacketType < 0) {
        s2cPacket.pos = 0;
        if (!JagexApplet.s2cBytesAvailable(1)) {
          return false;
        }

        nextS2cPacketType = s2cPacket.readCipheredByte();
        s2cPacket.pos = 0;
        nextS2cPacketLen = S2CPacket.LENGTH[nextS2cPacketType];
      }

      if (!JagexApplet.isS2cPacketFullyRecieved()) {
        return false;
      }

      thirdPreviousS2cPacketType = secondPreviousS2cPacketType;
      secondPreviousS2cPacketType = previousS2cPacketType;
      previousS2cPacketType = currentS2cPacketType;
      currentS2cPacketType = nextS2cPacketType;
      nextS2cPacketType = -1;
      return true;
    } else {
      return false;
    }
  }

  private static @Nullable Menu.DialogOption d474cq() {
    boolean var1 = false;
    boolean var2 = false;

    while (nextTypedKey()) {
      Menu._gsl.processKeyInputVertical();
      if (Menu._gsl.isItemActive()) {
        var1 = true;
      }

      if (lastTypedKeyCode == KeyState.Code.ESCAPE) {
        var2 = true;
      }
    }

    Menu._gsl.processMouseInput(Menu.a313gui(mouseX, mouseY), Menu.a313gui(mousePressX, mousePressY));
    if (Menu._gsl.isItemActive()) {
      var1 = true;
    }

    Menu.DialogOption var3 = null;
    if (var1 && Menu._gsl.selectedItem >= 0) {
      var3 = Menu._E[Menu._gsl.selectedItem];
      if (var3 == Menu.DialogOption.ACCEPT || var3 == Menu.DialogOption.CLOSE) {
        b423jf();
      }
    } else if (var2 && Menu.currentFullscreenDialog != Menu.FullscreenDialog.ACCEPT_COUNTDOWN) {
      b423jf();
    }

    if (var3 == null && Menu.currentFullscreenDialog == Menu.FullscreenDialog.ACCEPT_COUNTDOWN) {
      final long var4 = PseudoMonotonicClock.currentTimeMillis() - Menu._brp;
      final int var6 = (int) ((10999L - var4) / 1000L);
      if (var6 <= 0) {
        var3 = Menu.DialogOption.ACCEPT;
        Menu.showFullscreenDialog(Menu.FullscreenDialog.TIMEOUT, true);
      }
    }

    return var3;
  }

  private static boolean isProfileInitialized() {
    return (areAchievementsInitialized && loadedProfile) || isAnonymous;
  }

  private static boolean f427kh() {
    JagexApplet._vmNb = true;
    _ipb = PseudoMonotonicClock.currentTimeMillis() + 15000L;
    return connectionState == ConnectionState.RECONNECTING;
  }

  private static void a778cc(final boolean mouseNotYetHandled, final int mouseWheelRotation) {
    if (_pgJ < 20) {
      ++_pgJ;
    }

    if (_dmgm != JagexBaseApplet.screenBuffer.height) {
      _flf += JagexBaseApplet.screenBuffer.height - _dmgm;
      _dmgm = JagexBaseApplet.screenBuffer.height;
    }

    if (_pgJ > 0) {
      a150bq();
      Component._tgc.rootProcessMouseEvents(mouseNotYetHandled);
      if (DobToEnableChatForm.instance != null) {
        if (cannotChat) {
          DobToEnableChatForm.instance.tickRoot(Component._tgc.x2, Component._tgc.y2, mouseNotYetHandled);
        } else {
          DobToEnableChatForm.instance = null;
        }
      }

      for (final Channel channel : Channel.values()) {
        final int i = channel.ordinal();
        final Component<?> var5 = Component.chatFilterButtons[i];
        if (var5 != null) {
          if (var5.clickButton != MouseState.Button.NONE) {
            a939tai3(channel, var5);
          }

          final ChatMessage.FilterLevel var6 = ContextMenu.getChatChannelFilter(channel);
          Component.chatFilterIcons[i].sprite = Component.CHAT_FILTER_SPRITES[var6.ordinal()];
          Component.chatFilterLabels[i].label = Component.CHAT_FILTER_LABELS[var6.ordinal()];
        }
      }

      if (Component._a.clickButton != MouseState.Button.NONE) {
        ReportAbuseDialog.openInstance = new ReportAbuseDialog(Component._a.x2, Component._a.y2, Component._a.width, Component._a.height, Component.TAB_ACTIVE, Component.CLOSE_BUTTON, Component.LABEL, Component.CHECKBOX, Component.UNSELECTED_LABEL, null, 0L);
      }

      final ChatMessage var7 = a249chmr(mouseWheelRotation, _tga);
      if (var7 != null) {
        a681ks(var7);
      }

      final String var8 = b738bk();
      if (var8 != null) {
        ClientLobbyRoom.currentTooltip = var8;
      }
    }
  }

  private static String b738bk() {
    String var0 = null;
    String var1 = null;
    if (currentChatChannel == Channel.LOBBY && ratedLobbyRoom != null) {
      var1 = StringConstants.PUBLIC_CHAT_UNAVAILABLE_RATED_GAME;
    }

    if (currentChatChannel == Channel.PRIVATE && !a788(ContextMenu.recipientPlayerId, ContextMenu.normalizedRecipientPlayerName)) {
      if (a988da(ContextMenu.normalizedRecipientPlayerName)) {
        var1 = Strings.format(StringConstants.PRIVATE_CHAT_FRIEND_OFFLINE, ContextMenu.recipientPlayerName);
      } else {
        var1 = Strings.format(StringConstants.PRIVATE_CHAT_FRIEND_NOT_LISTED, ContextMenu.recipientPlayerName);
      }

      if (isQuickChatOpen) {
        ContextMenu.showChatMessage(Channel.PRIVATE, var1, 0, null, null);
        closeQuickChatIfOpen();
      }
    }

    if (var1 == null && !_K && _faX == null) {
      var1 = StringConstants.CHAT_VIEW_SCROLLED_UP;
    }

    if (var1 == null) {
      String var3 = playerDisplayName;
      var3 = a803(var3);
      String prefix = "";
      String var5 = "|";
      Channel channel = currentChatChannel;
      int var7 = 0;
      final String var8;
      if (channel == Channel.PRIVATE) {
        prefix = Strings.format(StringConstants.PRIVATE_QUICK_CHAT_TO_X, ContextMenu.recipientPlayerName);
        var8 = Strings.format(StringConstants.PRIVATE_QUICK_CHAT_FROM_X, var3);
        var7 = chatMessageLabel.width - 485 - (-Component._cgC.width - Component.CHAT_FONT.measureLineWidth(var8) + Component.CHAT_FONT.measureLineWidth(prefix));
        if (var7 < 0) {
          var7 = 0;
        }
      } else {
        if (channel == Channel.LOBBY) {
          if (unratedLobbyRoom == null) {
            prefix = "[" + StringConstants.MU_CHAT_LOBBY + "] ";
          }

          if (unratedLobbyRoom != null) {
            channel = Channel.ROOM;
            prefix = "[" + Strings.format(StringConstants.XS_GAME, unratedLobbyRoom.ownerName) + "] ";
          }
        }

        var8 = !a427cel() ? "<img=3>: " : ": ";
        prefix = prefix + var3 + var8;
        if (cannotChat) {
          var5 = "";
          prefix = "<col=999999>" + prefix + StringConstants.DOB_CHAT_DISABLED + "</col>";
        } else if (JagexApplet.canOnlyQuickChat) {
          var5 = "";
          prefix = "<col=999999>" + prefix + StringConstants.CLICK_TO_QUICKCHAT + "</col>";
        }

        final int var9 = Component.CHAT_FONT.measureLineWidth(prefix);
        if (!a427cel()) {
          if (chatMessageLabel.isMouseOverTarget && var9 > -chatMessageLabel.x2 + mouseX) {
            if (cannotChat) {
              var0 = "Broken!";
            } else {
              var0 = StringConstants.CLICK_TO_QUICKCHAT;
            }
          }

          if (chatMessageLabel.clickButton != MouseState.Button.NONE && chatMessageLabel.relativeClickX < var9 && !cannotChat) {
            ContextMenu.sendChannelMessageQC();
          }
        }
      }

      a790cq(Component.CHANNEL_TEXT_COLORS_1[channel.ordinal()], var5, prefix + Font.escapeTags(typedChatMessage.toString()), var7);
      if (!isChatboxSelected) {
        Component._cgC.isMouseOverTarget = false;
      }

      if (Component._cgC.isMouseOverTarget) {
        var0 = Strings.format(StringConstants.PRIVATE_CHAT_BLANK_AREA_EXPLANATION, var3, ContextMenu.recipientPlayerName);
      }
    } else {
      a790cq(_hoc, null, var1, 0);
    }

    return var0;
  }

  @SuppressWarnings("BooleanMethodIsAlwaysInverted")
  private static boolean a427cel() {
    return _cka != null;
  }

  public static boolean a988da(final String var1) {
    return PlayerListEntry.lookupFriend(var1) != null;
  }

  private static void a423ppi() {
    dismissContextMenu();
    AddOrRemovePlayerPopup.openInstance = null;
    ReportAbuseDialog.openInstance = null;
    closeQuickChat();
  }

  public static void a034so(int var1, int var2, final Sprite var3) {
    int var4 = var1 + Drawing.width * var2;
    int var5 = 0;
    int var6 = var3.height;
    int var7 = var3.width;
    int var8 = -var7 + Drawing.width;
    int var9 = 0;
    int var10;
    if (Drawing.top > var2) {
      var10 = Drawing.top - var2;
      var6 -= var10;
      var2 = Drawing.top;
      var5 = var10 * var7;
      var4 += Drawing.width * var10;
    }

    if (var1 < Drawing.left) {
      var10 = -var1 + Drawing.left;
      var5 += var10;
      var9 += var10;
      var8 += var10;
      var4 += var10;
      var7 -= var10;
      var1 = Drawing.left;
    }

    if (var6 + var2 > Drawing.bottom) {
      var6 -= var2 - (-var6 + Drawing.bottom);
    }

    if (Drawing.right < var1 + var7) {
      var10 = -Drawing.right + var1 + var7;
      var7 -= var10;
      var9 += var10;
      var8 += var10;
    }

    if (var7 > 0 && var6 > 0) {
      a699ge(var3.pixels, var5, Drawing.screenBuffer, var7, var4, var9, var6, var8);
    }
  }

  private static void a699ge(final int[] var0, int var1, final int[] var2, final int var5, int var6, final int var7, final int var8, final int var9) {
    for (int var12 = -var8; var12 < 0; ++var12) {
      for (int var13 = -var5; var13 < 0; ++var13) {
        final int var3 = var0[var1++];
        if (var3 == 0 || var3 == 255) {
          ++var6;
        } else {
          final int var11 = 255 & var3;
          final int var10 = var2[var6];
          var2[var6++] = 16711935 & (16711935 & var10) * var11 >> 8 | (16711920 & (var10 & 65280) * var11) >> 8;
        }
      }

      var1 += var7;
      var6 += var9;
    }
  }

  private static void a423kd() {
    int var0 = Menu._uqk;
    int var1 = 0;
    if (Menu.currentFullscreenDialog == Menu.FullscreenDialog.ACCEPT_COUNTDOWN) {
      final long var2 = PseudoMonotonicClock.currentTimeMillis() - Menu._brp;
      var1 = (int) ((10999L - var2) / 1000L);
      if (var1 < 0) {
        var1 = 0;
      }
    }

    for (int var9 = 0; var9 < Menu._tuef.length; ++var9) {
      final int var3 = Menu._ssa[var9];
      final int var4;
      if (var3 < 0) {
        var4 = 0x258488;
      } else if (Menu._gsl.selectedItem == var3) {
        var4 = 0x2ad0d6;
      } else {
        var4 = 0x258488;
      }

      String var5 = Menu._tuef[var9];
      int var6;
      int var7;
      if (Menu.currentFullscreenDialog == Menu.FullscreenDialog.ACCEPT_COUNTDOWN && var1 == 1) {
        var6 = Math.max(Menu._kpo.length, Menu._nld.length);
        var7 = Math.max(Menu._hmp.length, Menu._kdb.length);
        if (var9 >= 6 && var9 < var6 + 6) {
          var5 = Menu._kpo.length + var9 + (-6 - var6) >= 0 ? Menu._kpo[var9 - 6 - (-Menu._kpo.length + var6)] : "";
        }

        if (var6 + 7 <= var9 && var9 < var7 + 7 + var6) {
          var5 = -var6 + (var9 - 7) >= Menu._kdb.length ? "" : Menu._kdb[var9 - var6 - 7];
        }
      }

      if (var3 == -2) {
        var5 = Integer.toString(var1);
      }

      var6 = vm_.a827(var5);
      var7 = -(var6 >> 1) + SCREEN_CENTER_X;
      if (var3 >= 0) {
        var0 += 2;
        if (Menu.buttonSprite != null) {
          Menu.buttonSprite.draw((8 << 1) + Menu.get_idb(), -8 + var7, var0, (8 << 1) + var6);
        }

        var0 += 8;
      }

      Menu.FONT.draw(var5, var7, Menu.FONT.capHeight + var0, var4);
      if (var3 < 0) {
        var0 += Menu.get_idb();
      } else {
        var0 += Menu.get_idb() + 2 + 8;
      }
    }
  }

  private static Sprite loadJpgSprite(final ResourceLoader loader, String key) {
    if (!loader.hasResource("", key)) {
      key = key + ".jpg";
      if (!loader.hasResource("", key)) {
        return null;
      }
    }

    final byte[] imageData = loader.getResource("", key);
    return new Sprite(imageData, fullScreenCanvas != null ? fullScreenCanvas : canvas);
  }

  private static boolean processChatKeyboardInput() {
    final boolean var3 = (_K || _faX != null)
        && !(currentChatChannel == Channel.LOBBY && ratedLobbyRoom != null)
        && !(currentChatChannel == Channel.PRIVATE && !a788(ContextMenu.recipientPlayerId, ContextMenu.normalizedRecipientPlayerName));

    if (lastTypedKeyCode == KeyState.Code.ENTER) {
      if (var3) {
        if (typedChatMessage.length() > 0) {
          final String message = typedChatMessage.toString();
          if (EmailLoginCredentials.containsPassword(message)) {
            ContextMenu.showChatMessage(Channel.PRIVATE, StringConstants.DONT_TELL_PASSWORD, 0, null, null);
            ContextMenu.showChatMessage(Channel.PRIVATE, StringConstants.PLEASE_CHANGE_PASSWORD, 0, null, null);
          } else {
            Channel channel = currentChatChannel;
            if (channel == Channel.LOBBY && unratedLobbyRoom != null) {
              channel = Channel.ROOM;
            }

            if (ContextMenu.getChatChannelFilter(channel) == ChatMessage.FilterLevel.NONE) {
              ContextMenu.setChatFilter(channel, ChatMessage.FilterLevel.FRIENDS);
            }

            C2SPacket.sendChatMessage(channel, ContextMenu.recipientPlayerName, message);
          }
        }

        discardTypedChatMessage();
      } else if (currentChatChannel != Channel.LOBBY) {
        discardTypedChatMessage();
      }
      return true;
    } else if (lastTypedKeyCode == KeyState.Code.BACKSPACE) {
      if (var3 && typedChatMessage.length() > 0) {
        AddOrRemovePlayerPopup.removeLastCharacterFromStringBuilder(typedChatMessage);
      }
      return true;
    } else if (isAcceptedChatKeyChar(lastTypedKeyChar)) {
      final int var12;
      if (var3 && typedChatMessage.length() < 80) {
        typedChatMessage.append(lastTypedKeyChar);
        final short var5 = 485;
        String var6 = playerDisplayName;
        var6 = a803(var6);
        String var7;
        if (currentChatChannel == Channel.PRIVATE) {
          var7 = Strings.format(StringConstants.PRIVATE_QUICK_CHAT_TO_X, a034ih(ContextMenu.recipientPlayerName));
          final String var8 = Strings.format(StringConstants.PRIVATE_QUICK_CHAT_FROM_X, var6);
          final int var9 = Component.CHAT_FONT.measureLineWidth(var7);
          final int var10 = Component.CHAT_FONT.measureLineWidth(var8);
          if (var9 <= var10) {
            var12 = var5 - var10;
          } else {
            var12 = var5 - var9;
          }
        } else {
          var7 = "";
          if (currentChatChannel == Channel.LOBBY) {
            if (unratedLobbyRoom == null) {
              var7 = "[" + StringConstants.MU_CHAT_LOBBY + "] ";
            }

            if (unratedLobbyRoom != null) {
              var7 = "[" + Strings.format(StringConstants.XS_GAME, unratedLobbyRoom.ownerName) + "] ";
            }
          }

          var7 = var7 + var6 + ": ";
          var12 = var5 - Component.CHAT_FONT.measureLineWidth(var7);
        }

        if (var12 < Component.CHAT_FONT.measureLineWidth(typedChatMessage.toString())) {
          AddOrRemovePlayerPopup.removeLastCharacterFromStringBuilder(typedChatMessage);
        }
      }

      return true;
    } else {
      return false;
    }
  }

  private static boolean isAcceptedChatKeyChar(final char c) {
    if (c >= ' ' && c <= '~') {
      return true;
    } else if (c >= 0xa0 && c <= 0xff) {
      return true;
    } else {
      return c == 'Œ' || c == 'œ' || c == 'Ÿ' || c == Strings.EM_DASH || c == '€';
    }
  }

  private static void b150nj() {
    _faX = null;
    if (_cka != null) {
      Component._cgC = _erj;
      chatMessageLabel = _cka;
      chatMessageLabel.mouseOverTextColor = -1;
      _erj = null;
      chatMessageLabel._qb = -1;
      _cka = null;
    }

    _abAb = null;
    HIDE_CHAT_TEMPORARILY_LABEL = null;
    _mbn = null;
    _cbl = null;
    isChatboxSelected = true;
  }

  private static void a939tai3(final @NotNull Channel channel, final Component<?> target) {
    showContextMenu(null, target, 0L, null, null, -1, channel);

    if (channel == Channel.LOBBY) {
      ContextMenu.openInstance.view.addItem(Component.CHAT_FILTER_SPRITES[0], StringConstants.MU_CHAT_LOBBY_SHOW_ALL, ContextMenu.ClickAction.CHAT_SHOW_ALL);
      ContextMenu.openInstance.view.addItem(Component.CHAT_FILTER_SPRITES[1], StringConstants.MU_CHAT_LOBBY_FRIENDS, ContextMenu.ClickAction.CHAT_SHOW_FRIENDS);
      ContextMenu.openInstance.view.addItem(Component.CHAT_FILTER_SPRITES[2], StringConstants.MU_CHAT_LOBBY_HIDE, ContextMenu.ClickAction.CHAT_SHOW_NONE);
    } else if (channel == Channel.ROOM) {
      ContextMenu.openInstance.view.addItem(Component.CHAT_FILTER_SPRITES[0], StringConstants.MU_CHAT_GAME_SHOW_ALL, ContextMenu.ClickAction.CHAT_SHOW_ALL);
      ContextMenu.openInstance.view.addItem(Component.CHAT_FILTER_SPRITES[1], StringConstants.MU_GAME_CHAT_FRIENDS, ContextMenu.ClickAction.CHAT_SHOW_FRIENDS);
      ContextMenu.openInstance.view.addItem(Component.CHAT_FILTER_SPRITES[2], StringConstants.MU_CHAT_GAME_HIDE, ContextMenu.ClickAction.CHAT_SHOW_NONE);
    } else if (channel == Channel.PRIVATE) {
      ContextMenu.openInstance.view.addItem(Component.CHAT_FILTER_SPRITES[0], StringConstants.MU_CHAT_PM_SHOW_ALL, ContextMenu.ClickAction.CHAT_SHOW_ALL);
      ContextMenu.openInstance.view.addItem(Component.CHAT_FILTER_SPRITES[1], StringConstants.MU_CHAT_PM_FRIENDS, ContextMenu.ClickAction.CHAT_SHOW_FRIENDS);
      ContextMenu.openInstance.view.addItem(Component.CHAT_FILTER_SPRITES[2], StringConstants.MU_CHAT_INVISIBLE_AND_SILENT_MODE, ContextMenu.ClickAction.CHAT_SHOW_NONE);
    }

    ContextMenu.openInstance.view.positionRelativeToTarget(target.x2, target.y2, target.width, target.height);
  }

  public static void showContextMenu(final ScrollPane<?> parent,
                                     final Component<?> target,
                                     final long playerId,
                                     final String playerName,
                                     final String playerDisplayName,
                                     final int roomId,
                                     final @Nullable Channel channel) {
    currentContextMenuParent = parent;
    ContextMenu.openInstance = new ContextMenu(target, playerId, playerName, playerDisplayName, roomId, channel);
  }

  private static PlayerListEntry tickFriendList(final boolean mouseStill, final int mouseWheelRotation) {
    final boolean var3 = Component.FRIEND_LIST.scrollPane.processScrollInput(
        mouseStill, currentContextMenuParent == Component.FRIEND_LIST.scrollPane, Component.LABEL_HEIGHT + 2, mouseWheelRotation * (Component.LABEL_HEIGHT + 2) * 3
    );

    PlayerListEntry clickedEntry = null;
    if (PlayerListEntry.serverStatus == PlayerListEntry.ServerStatus.LOADED) {
      Component.FRIEND_LIST_PANEL.enabled = true;
      Component.SERVER_INFO_LABEL.label = Strings.format(StringConstants.YOU_ARE_ON_X_SERVER, currentServerName);
      Component.FRIEND_LIST.scrollPane.viewport.label = null;
      PlayerListEntry prevEntry = null;

      for (final PlayerListEntry entry : Component.FRIEND_LIST.entries.children) {
        final boolean isNew = entry.children == null;
        if (isNew) {
          entry.playerNameLabel = new Component<>(Component.UNSELECTED_LABEL_DARK_2);
          entry.addChild(entry.playerNameLabel);
          entry.serverNameLabel = new Component<>(Component.UNSELECTED_LABEL_DARK_1);
          entry.addChild(entry.serverNameLabel);
          entry.displayNameChangedIcon = new Component<>(Component.UNSELECTED_LABEL_DARK_2);
          entry.addChild(entry.displayNameChangedIcon);
          entry.serverNameLabel.textAlignment = Font.HorizontalAlignment.RIGHT;
          entry.recursivelySet_H();
        }

        entry.width = Component.FRIEND_LIST.entries.width;
        final int statusColor;
        final int mouseOverStatusColor;
        if (entry.serverName == null) {
          statusColor = 0xcc0000;
          mouseOverStatusColor = 0xff6666;
        } else if (entry.serverName.equals(currentServerName)) {
          statusColor = 0x00cc00;
          mouseOverStatusColor = 0x66ff66;
        } else {
          statusColor = 0xcccc00;
          mouseOverStatusColor = 0xffff66;
        }

        final boolean displayNameChanged = entry.previousDisplayName != null && !entry.previousDisplayName.equals("");
        if (displayNameChanged) {
          entry.displayNameChangedIcon.sprite = Component.DISPLAY_NAME_CHANGED;
          entry.displayNameChangedIcon.mouseOverTextColor = mouseOverStatusColor;
          entry.displayNameChangedIcon.setBounds(0, 0, Component.DISPLAY_NAME_CHANGED.offsetX + 3, Component.LABEL_HEIGHT);
        }

        entry.playerNameLabel.textColor = entry.serverNameLabel.textColor = statusColor;
        entry.playerNameLabel.mouseOverTextColor = entry.serverNameLabel.mouseOverTextColor = mouseOverStatusColor;
        entry.playerNameLabel._qb = entry.serverNameLabel._qb = mouseOverStatusColor;
        entry.playerNameLabel._ab = entry.serverNameLabel._ab = mouseOverStatusColor;
        int playerNameX = 0;
        final int serverNameWidth = 80;
        int playerNameWidth = entry.width - serverNameWidth - 2;
        if (displayNameChanged) {
          playerNameX = Component.DISPLAY_NAME_CHANGED.offsetX + 3;
          playerNameWidth -= playerNameX;
        }

        entry.playerNameLabel.label = playerNameWidth <= 0 ? entry.playerDisplayName : entry.playerNameLabel.font.truncateWithEllipsisToFit(entry.playerDisplayName, playerNameWidth);
        entry.playerNameLabel.setBounds(playerNameX, 0, playerNameWidth, Component.LABEL_HEIGHT);
        entry.serverNameLabel.label = entry.serverName == null ? StringConstants.OFFLINE : entry.serverName;
        entry.serverNameLabel.setBounds(entry.width - serverNameWidth, 0, serverNameWidth, Component.LABEL_HEIGHT);
        final boolean playerNameTruncated = !entry.playerNameLabel.label.equals(entry.playerDisplayName);
        if (!var3) {
          entry._gb = Component.LABEL_HEIGHT - entry.height;
        }

        if (isNew) {
          Component.FRIEND_LIST.entries.placeAfter(prevEntry, entry);
        }

        if (entry.displayNameChangedIcon != null && entry.displayNameChangedIcon.isMouseOverTarget) {
          ClientLobbyRoom.currentTooltip = entry.previousDisplayName;
        } else if (entry.clickButton != MouseState.Button.NONE) {
          final String playerName = entry.playerDisplayName;
          showContextMenu(Component.FRIEND_LIST.scrollPane, entry, 0L, playerName, null, -1, null);
          if (entry.serverName != null && !isComposingChatMessageTo(ContextMenu.openInstance.playerName) && !cannotChat) {
            if (!JagexApplet.canOnlyQuickChat) {
              ContextMenu.openInstance.view.addItem(Strings.format(StringConstants.SEND_PM_TO_X, playerName), ContextMenu.ClickAction.SEND_PRIVATE_MESSAGE);
            }
            ContextMenu.openInstance.view.addItem(Strings.format(StringConstants.SEND_QC_TO_X, playerName), ContextMenu.ClickAction.SEND_PRIVATE_MESSAGE_QC);
          }

          clickedEntry = entry;
        } else if (entry.playerNameLabel.isMouseOverTarget && playerNameTruncated) {
          ClientLobbyRoom.currentTooltip = entry.playerDisplayName;
        }

        prevEntry = entry;
      }

      if (Component.FRIEND_LIST.addButton.clickButton != MouseState.Button.NONE) {
        AddOrRemovePlayerPopup.openInstance = new AddOrRemovePlayerPopup(Component.FRIEND_LIST.addButton.x2, Component.FRIEND_LIST.addButton.y2, Component.FRIEND_LIST.addButton.width, Component.FRIEND_LIST.addButton.height, StringConstants.ENTER_FRIEND_ADD, Component.POPUP, Component.LABEL, Component.LABEL);
        AddOrRemovePlayerPopup.action = AddOrRemovePlayerPopup.Action.ADD_FRIEND;
      }
      if (Component.FRIEND_LIST.removeButton.clickButton != MouseState.Button.NONE) {
        AddOrRemovePlayerPopup.openInstance = new AddOrRemovePlayerPopup(Component.FRIEND_LIST.removeButton.x2, Component.FRIEND_LIST.removeButton.y2, Component.FRIEND_LIST.removeButton.width, Component.FRIEND_LIST.removeButton.height, StringConstants.ENTER_FRIEND_DEL, Component.POPUP, Component.LABEL, Component.LABEL);
        AddOrRemovePlayerPopup.action = AddOrRemovePlayerPopup.Action.REMOVE_FRIEND;
      }
    } else {
      Component.FRIEND_LIST_PANEL.enabled = false;
      Component.SERVER_INFO_LABEL.label = StringConstants.PLEASE_WAIT;
      if (PlayerListEntry.serverStatus == PlayerListEntry.ServerStatus.LOADING) {
        Component.FRIEND_LIST.scrollPane.viewport.label = StringConstants.CONNECTING_TO_FRIEND_SERVER_TWO_LINE;
      } else {
        Component.FRIEND_LIST.scrollPane.viewport.label = StringConstants.LOADING;
      }
      crunch(Component.FRIEND_LIST.entries);
    }

    return clickedEntry;
  }

  public static void dismissContextMenu() {
    if (ContextMenu.openInstance != null && ContextMenu.openInstance.target != null) {
      ContextMenu.openInstance.target.selected = false;
    }
    currentContextMenuParent = null;
    ContextMenu.openInstance = null;
  }

  private static void a969be() {
    localPlayerId = -1L;
    ratedLobbyRoom = null;
    unratedLobbyRoom = null;
    playerSeatsFilledBitmap = new byte[(NUM_PLAYERS_OPTION_VALUES.length + 7) / 8];
    justRecievedRoomDetailsFromServer = false;

    int totalGameoptChoices = Arrays.stream(GAMEOPT_CHOICES_COUNTS).sum();
    totalGameoptChoices = (totalGameoptChoices + 7) / 8;
    gameoptChoicesBitmap = new byte[totalGameoptChoices];

    if (Component.INVITE_PLAYER_LIST_SCROLL_PANE.content.children == null) {
      Component.INVITE_PLAYER_LIST_SCROLL_PANE.content.children = new ArrayList<>();
    }

    LobbyPlayer.online = Component.INVITE_PLAYER_LIST_SCROLL_PANE.content.children;
    LobbyPlayer.online.clear();
    LobbyPlayer.onlineMap = new HashMap<>();
    playerRating = -1;
    joinRequestCount = 0;
    ContextMenu.roomShowingPlayers = 0;
    if (Component.GAME_LIST_SCROLL_PANE.content.children == null) {
      Component.GAME_LIST_SCROLL_PANE.content.children = new ArrayList<>();
    }

    ClientLobbyRoom.rooms = Component.GAME_LIST_SCROLL_PANE.content.children;
    ClientLobbyRoom.rooms.clear();
    ClientLobbyRoom.roomsMap = new HashMap<>();
    if (Component.JOINED_PLAYERS_TABLE.content.children == null) {
      Component.JOINED_PLAYERS_TABLE.content.children = new ArrayList<>();
    }

    LobbyPlayer.joinedPlayers = Component.JOINED_PLAYERS_TABLE.content.children;
    LobbyPlayer.joinedPlayers.clear();
    LobbyPlayer.joinedPlayersMap = new HashMap<>(8);
    cannotStartGameUntil = 0L;
  }

  private static SampledAudioChannelS16 createAudioChannel(final int i, int var3) {
    if (i < 0 || i >= 2) {
      throw new IllegalArgumentException();
    }

    if (var3 < 256) {
      var3 = 256;
    }

    try {
      final SampledAudioChannelS16 channel = new SampledAudioChannelS16();
      channel.dataS16P8 = new int[512];
      channel._b = var3;
      channel.lineSize = (var3 & 0xfffffc00) + 0x400;
      if (channel.lineSize > 0x4000) {
        channel.lineSize = 0x4000;
      }

      channel.openLine(channel.lineSize);
      if (AudioThread.instance == null) {
        AudioThread.instance = new AudioThread(MessagePumpThread.instance);
        MessagePumpThread.instance.sendSpawnThreadMessage(AudioThread.instance, 10);
      }

      if (AudioThread.instance != null) {
        if (AudioThread.instance.channels[i] != null) {
          throw new IllegalArgumentException();
        }

        AudioThread.instance.channels[i] = channel;
      }

      return channel;
    } catch (final LineUnavailableException e) {
      throw new RuntimeException(e);
    }
  }

  public static void closeQuickChat() {
    isQuickChatOpen = false;
    if (_dmrh != null) {
      _dmrh.close();
    }
    if (currentChatChannel != Channel.LOBBY) {
      discardTypedChatMessage();
    }
    _cvcm = 0;
  }

  private static ChatMessage a249chmr(final int mouseWheelRotation, final int var2) {
    ChatMessage var4 = null;
    Component._jiI.content.children.clear();
    int var5 = 0;
    int var6 = 0;

    int var7;
    for (var7 = chatMessageCount - 1; var7 >= 0; --var7) {
      final ChatMessage var8 = chatMessages[var7];
      boolean var9 = false;
      if (!cannotChat && _kpi > var5) {
        final ChatMessage.FilterLevel var10 = ContextMenu.getChatChannelFilter(var8.channel);
        if (var10.lessThanOrEqual(var8.a410()) && (var8._h || !PlayerListEntry.isIgnored(var8.senderName))) {
          var9 = true;
        }
      }

      if (var9) {
        if (var8.component == null || ContextMenu._fpv) {

          final String var15 = a614hg(var8);
          final String var11 = var15 + Font.escapeTags(var8.message);
          final int var12 = a421jl(var8);
          var8.component = new Component<>(Component.UNSELECTED_LABEL, var11);
          var8.component.font = Component.CHAT_FONT;
          var8.component.mouseOverTextColor = -(var12 >> 1 & 8355711) + var12 + (8355711 & Component.UNSELECTED_LABEL.mouseOverTextColor >> 1);
          var8.component.textColor = var12;
          var6 += Component.LABEL_HEIGHT;
          var8.component._qb = -(8355711 & var12 >> 1) + var12 + (8355711 & Component.UNSELECTED_LABEL._qb >> 1);
        }

        ++var5;
      } else {
        var8.component = null;
      }
    }

    var7 = 0;

    int var13;
    for (var13 = 0; chatMessageCount > var13; ++var13) {
      final ChatMessage var14 = chatMessages[var13];
      if (var14.component != null) {
        Component._jiI.content.addChild(var14.component);
        var14.component.setBounds(var2, var7, var14.component.e474(), Component.LABEL_HEIGHT);
        if (var14.component.clickButton != MouseState.Button.NONE) {
          var4 = var14;
        }

        var7 += Component.LABEL_HEIGHT;
      }
    }

    var13 = -var7 + var6 + Component._jiI.content.height + Component._jiI.content._gb;
    Component._jiI.content.height -= var13;
    Component._jiI.content.y += var13;
    if (ContextMenu._fpv) {
      Component._jiI.content.height = var7;
    }

    Component._jiI.content._gb = -Component._jiI.content.height + var7;
    if (ContextMenu._fpv) {
      ContextMenu._fpv = false;
      Component._jiI.content._w = 0;
      _K = true;
      Component._jiI.content.y = -Component._jiI.content.height + Component._jiI.viewport.height;
    }

    if (isChatboxSelected && _faX != null) {
      _K = true;
    }

    final int var16 = Component._jiI.viewport.height - Component._jiI.content.height - Component._jiI.content._gb;
    if (_K) {
      Component._jiI.content._w = var16 - Component._jiI.content.y;
    }

    Component._jiI.a795(mouseWheelRotation * 2 * Component.LABEL_HEIGHT, Component.LABEL_HEIGHT);
    _K = var16 == Component._jiI.content._w + Component._jiI.content.y;
    return var4;
  }

  private static String a803(String var0) {
    final int var1 = a776wd(modLevel, adminLevel);
    if (var1 == 1) {
      var0 = "<img=0>" + var0;
    }

    if (var1 == 2) {
      var0 = "<img=1>" + var0;
    }

    return var0;
  }

  private static void crunch(final Component<?> var1) {
    for (final Component<?> var2 : var1.children) {
      var2.height = 0;
      var2._gb = 0;
      var2._w = 0;
      var2.y = 0;
    }
    var1.height = 0;
    var1._w = 0;
    var1.y = 0;
    var1._gb = 0;
  }

  private static void a487oq(final boolean canCreateAccount) {
    if (!isAnonymous) {
      throw new IllegalStateException();
    }
    CommonUI.switchToLogin(canCreateAccount, false);
    connectionState = ConnectionState.NOT_CONNECTED;
  }

  private static void a093oq() {
    for (final SetProfileRequest var1 : JagexApplet.profileSetRequests) {
      C2SPacket.sendProfileSet(var1);
    }
    JagexApplet.getProfileResponses.forEach(var2 -> C2SPacket.sendProfileGet());
  }

  private static boolean a154vc() {
    return (fullScreenCanvas != null || hadFocus) && (8 & currentTick) == 0;
  }

  private static void b423jf() {
    Menu.isFullscreenDialogOpen = false;
    Menu._hmp = null;
    Menu._nld = null;
    Menu._kpo = null;
    Menu._kdb = null;
  }

  private static void tickLobbyBrowser(final boolean mouseNotYetHandled, final boolean inGame) {
    Component.layoutLobbyBrowserPanels();

    if (playerRating < 0) {
      Component.YOUR_RATING_LABEL.label = null;
    } else {
      Component.YOUR_RATING_LABEL.label = Strings.format(StringConstants.YOUR_RATING_IS_X, Integer.toString(playerRating));
    }

    Component.lobbyBrowserLeftPanel.rootProcessMouseEvents(mouseNotYetHandled && !inGame && !showYouHaveBeenKickedDialog && unratedLobbyRoom == null && ratedLobbyRoom == null);
    Component.LOBBY_RIGHT_PANEL.rootProcessMouseEvents(mouseNotYetHandled && !inGame && !showYouHaveBeenKickedDialog && unratedLobbyRoom == null && ratedLobbyRoom == null);
    Component.YOU_HAVE_BEEN_KICKED_DIALOG.rootProcessMouseEvents(mouseNotYetHandled && !inGame && showYouHaveBeenKickedDialog);
    Component.lobbyBrowserTabbedPlayerList.view.f487();
    if (Component.RETURN_TO_MAIN_MENU_BUTTON.clickButton != MouseState.Button.NONE) {
      returnToMainMenuClicked = true;
    }

    if (Component.PLAY_RATED_GAME_BUTTON.clickButton != MouseState.Button.NONE) {
      if (_hmo || JagexApplet.membershipLevel > 0 || adminLevel >= 2 && keysDown[12]) {
        C2SPacket.playRatedGame();
      } else {
        _ucB = true;
      }
    }

    if (Component.CREATE_UNRATED_GAME_BUTTON.clickButton != MouseState.Button.NONE) {
      C2SPacket.createUnratedGame();
    }

    if (Component.YOU_HAVE_BEEN_KICKED_OK_BUTTON.clickButton != MouseState.Button.NONE) {
      showYouHaveBeenKickedDialog = false;
    }
  }

  public static void f423fr() {
    final int x = (int) (1600.0D * (1.0D + Math.cos((float) currentTick / 500.0F)));
    final int y = (int) (1600.0D * (1.0D - Math.sin((float) currentTick / 500.0F)));
    if (renderQuality.antialiasStarfieldBackground) {
      a034il(x, y, STAR_FIELD);
      Drawing.horizontalLine(0, 0, SCREEN_WIDTH, 0);
    } else {
      STAR_FIELD.c093(-x >> 4, -y >> 4);
    }
  }

  private static void a034il(int x, int y, final Sprite sprite) {
    if (_cfa == null || _cfa.length != Drawing.width) {
      _cfa = new int[Drawing.width];
    }

    final int var4 = y & 15;
    y >>= 4;
    final int var5 = x & 15;
    x >>= 4;
    int var12 = 0;
    int var13 = sprite.width * y + x;
    final int var14 = sprite.width - Drawing.width;

    for (int var15 = -Drawing.height; var15 < 0; var13 += var14) {
      int var16 = 0;

      for (int var17 = Drawing.width - 1; var17 >= 0; --var17) {
        final int var6 = sprite.pixels[var13];
        final int var8 = var6 & '\uff00';
        final int var7 = var6 & 16711935;
        final int var10 = 267390960 & var5 * var7;
        final int var11 = var5 * var8 & 1044480;
        final int var9 = var11 | var10;
        final int i = var16 + var9;
        final int i1 = 267390960 & i;
        var16 = (var6 << 4) - var9;
        final int i2 = i & 1044480;
        final int i3 = -16711936 & var4 * i1;
        final int i4 = 16711680 & i2 * var4;
        final int i5 = i3 | i4;
        Drawing.screenBuffer[var12] = _cfa[var17] + i5 >> 8;
        _cfa[var17] = (i << 4) - i5;
        ++var12;
        ++var13;
      }

      ++var15;
    }
  }

  private static int a421jl(final ChatMessage message) {
    int color = _hoc;
    if (message.channel == Channel.PRIVATE) {
      if (!message._h) {
        if (message._l == 0 && message._e == 0) {
          color = Component.CHANNEL_TEXT_COLORS_1[message.channel.ordinal()];
        } else {
          color = Component.CHANNEL_TEXT_COLORS_2[message.channel.ordinal()];
        }
      }
    } else if (message.channel == Channel.CHANNEL_5) {
      color = Component.CHANNEL_TEXT_COLORS_2[message.channel.ordinal()];
    } else if (message.senderId == localPlayerId) {
      color = Component.CHANNEL_TEXT_COLORS_1[message.channel.ordinal()];
    } else {
      color = Component.CHANNEL_TEXT_COLORS_2[message.channel.ordinal()];
    }

    return color;
  }

  private static void a540ta(boolean var0) {
    if (isPopupOpen()) {
      var0 = false;
    }

    a877di(var0);
    a893ml(var0);
  }

  private static void a877di(final boolean var0) {
    Drawing.withLocalContext(() -> {
      Drawing.expandBoundsToInclude(Drawing.width - SCREEN_WIDTH >> 1, 0, Drawing.width + SCREEN_WIDTH >> 1, Drawing.height);
      if (lobbyBrowserTransitionCounter > 0) {
        if (LOBBY_ICON != null) {
          LOBBY_ICON.c093(Component.lobbyBrowserLeftPanel.x, 0);
        }

        Component.lobbyBrowserLeftPanel.b540(var0 && !showYouHaveBeenKickedDialog);
        Component.LOBBY_RIGHT_PANEL.b540(var0 && !showYouHaveBeenKickedDialog);
      }

      if (ratedLobbyRoomTransitionCounter > 0 || unratedLobbyRoomTransitionCounter > 0) {
        if (LOBBY_ICON != null) {
          LOBBY_ICON.c093(lobbyRoomLeftPanel.x, 0);
        }

        lobbyRoomLeftPanel.b540(var0 && !invitePlayersDialogOpen);
        Component.GAME_INFO_CONTAINER.b540(var0 && !invitePlayersDialogOpen);
      }
    });
  }

  private static void a366fa(int chatPanelY) {
    _flf = chatPanelY;
    if (_pgJ != 20) {
      final int var1 = 400;
      final int var2 = -(_pgJ * _pgJ) + var1;
      chatPanelY += (-chatPanelY + _dmgm) * var2 / var1;
    }

    Component._tgc.setBounds(_tmh, chatPanelY, SCREEN_WIDTH, GameUI.CHAT_PANEL_HEIGHT);
    a370qc(_dmgm - 24, _tga, ClientLobbyRoom._qob);
  }

  private static void a370qc(final int var0, final int var2, final int var5) {
    Component._uaf.setBounds(5, 0, SCREEN_WIDTH, var0);
    if (_dmrh != null) {
      _dmrh.a669(var2, var0, var2 - _cvcm, var5, var0);
    }
  }

  private static void tickLoadProfile() {
    if (!areAchievementsInitialized && achievementRequest == null) {
      achievementRequest = JagexApplet.createAchievementRequest();
    }

    if (!loadedProfile) {
      if (request == null) {
        request = loadProfile();
      }

      if (request.completed) {
        if (request.data != null) {
          receiveProfile(request.data);
        }

        saveProfile();
        if (GameUI.currentSettings == -1) {
          GameUI.currentSettings = GameUI.lastSavedSettings;
        }

        loadedProfile = true;
      }
    }
  }

  private static void receiveProfile(final byte[] data) {
    if (data[0] >= 0) {
      GameUI.lastSavedSettings = data[1];
    }
  }

  private static void tickUnratedLobbyRoom(final boolean mouseNotYetHandled, final boolean canStart, final boolean inGame) {
    layoutLobbyRoomPanels(unratedLobbyRoomTransitionCounter);
    if (unratedLobbyRoom != null) {
      Component.RETURN_TO_LOBBY_BUTTON.enabled = true;
      Component.INVITE_PLAYERS_BUTTON.height = 0;
      final Component<?> var5 = Component.INVITE_PLAYERS_BUTTON;
      var5.width = 0;
      final Component<?> var6 = Component.FIND_OPPONENTS_BUTTON;
      Component.FIND_OPPONENTS_BUTTON.height = 0;
      var6.width = 0;
      Component.WAITING_TO_START_LABEL.height = 0;
      final Component<?> var7 = Component.WAITING_TO_START_LABEL;
      var7.width = 0;
      final Component<?> var8;
      int var19;
      if (isCurrentRoomOwner()) {
        Component.GAME_OWNER_HEADING.label = StringConstants.YOUR_GAME.toUpperCase();
        var19 = (Component.GAME_INFO_CONTAINER.width + 2) / 2;
        Component.INVITE_PLAYERS_BUTTON.setBounds(0, Component.GAME_INFO_CONTAINER.height - 40, var19 - 2, 40);
        if (unratedLobbyRoom.joinedPlayerCount < unratedLobbyRoom.maxPlayerCount) {
          Component.INVITE_PLAYERS_BUTTON.label = StringConstants.MU_INVITE_PLAYERS.toUpperCase();
          Component.INVITE_PLAYERS_BUTTON.enabled = true;
        } else {
          Component.INVITE_PLAYERS_BUTTON.label = StringConstants.GAME_FULL.toUpperCase();
          Component.INVITE_PLAYERS_BUTTON.enabled = false;
        }

        Component.INVITE_PLAYERS_BUTTON.nineSliceSprites = Component.BIG_BUTTON.nineSliceSprites;
        if (joinRequestCount > 0) {
          final String joinRequestsLabel;
          if (joinRequestCount == 1) {
            joinRequestsLabel = StringConstants.JOIN_REQUESTS_ONE;
          } else {
            joinRequestsLabel = Strings.format(StringConstants.JOIN_REQUESTS_MANY, Integer.toString(joinRequestCount));
          }

          if ((_ssw & 16) == 0 && !invitePlayersDialogOpen) {
            Component.INVITE_PLAYERS_BUTTON.nineSliceSprites = Component.BIG_BUTTON.mouseOverNineSliceSprites;
          }

          Component.INVITE_PLAYERS_BUTTON.label = Component.INVITE_PLAYERS_BUTTON.label + "<br>" + joinRequestsLabel;
        }

        Component.FIND_OPPONENTS_BUTTON.setBounds(var19, Component.GAME_INFO_CONTAINER.height - 40, -var19 + Component.GAME_INFO_CONTAINER.width, 40);
        Component.FIND_OPPONENTS_BUTTON.label = StringConstants.START_GAME.toUpperCase();
        Component.FIND_OPPONENTS_BUTTON.enabled = canStart && cannotStartGameUntil == 0L;
        var8 = Component.FIND_OPPONENTS_BUTTON;
        int var20 = 2;
        int var14;
        int var15;
        int var16;
        int var17;
        boolean var21;
        if (_peD != null) {
          if (_lrc == null) {
            _lrc = new byte[GAMEOPT_CHOICES_COUNTS.length];
            _ekF = new boolean[GAMEOPT_CHOICES_COUNTS.length];
          }

          for (int var11 = 0; var11 < GAMEOPT_CHOICES_COUNTS.length; ++var11) {
            _ekF[var11] = false;
          }

          for (var20 = 0; var20 < 2; ++var20) {
            var21 = false;

            label323:
            for (final int[] var13 : _peD) {
              for (var14 = 0; var14 < var13.length; var14 += 2) {
                var15 = var13[var14];
                var16 = var13[var14 + 1];
                if (var15 == -1) {
                  var17 = var20 != 0 ? unratedLobbyRoom.joinedPlayerCount : unratedLobbyRoom.maxPlayerCount;
                  if (var16 != var17) {
                    continue label323;
                  }
                } else if ((255 & unratedLobbyRoom.gameSpecificOptions[var15]) != var16) {
                  continue label323;
                }
              }

              var21 = true;
              var14 = -1;

              for (var15 = 0; var13.length > var15; var15 += 2) {
                var16 = var13[var15];
                if (var16 > var14) {
                  var14 = var16;
                }
              }

              _ekF[var14] = true;
            }

            if (var21) {
              break;
            }
          }

          if (adminLevel >= 2 && keysDown[12]) {
            var20 = 2;
          }
        }

        boolean var23;
        int var24;
        if (var20 < 2) {
          Component.FIND_OPPONENTS_BUTTON.enabled = false;
          if (Component.FIND_OPPONENTS_BUTTON.isMouseOver) {
            String var22 = null;
            var23 = false;

            String var26;
            for (var24 = 0; GAMEOPT_CHOICES_COUNTS.length > var24; ++var24) {
              if (_ekF[var24]) {
                var26 = "<col=A00000>" + StringConstants.GAMEOPT_LABELS[var24] + "</col>";
                if (var22 == null) {
                  var22 = var26;
                } else {
                  var22 = var22 + ", " + var26;
                  var23 = true;
                }
              }
            }

            final String var25;
            if (var20 == 0) {
              var25 = StringConstants.GAMEOPT_UNSELECTED_OPTIONS;
              if (var23) {
                var26 = StringConstants.GAMEOPT_PLEASE_SELECT_OPTION_2 + var22;
              } else {
                var26 = Strings.format(StringConstants.GAMEOPT_PLEASE_SELECT_OPTION_1, var22);
              }
            } else {
              var25 = StringConstants.GAMEOPT_BADNUMPLAYERS;
              if (var23) {
                var26 = StringConstants.GAMEOPT_INVITE_PLAYERS_OR_TRY_CHANGING_2 + var22;
              } else {
                var26 = Strings.format(StringConstants.GAMEOPT_INVITE_PLAYERS_OR_TRY_CHANGING_1, var22);
              }
            }

            ClientLobbyRoom.currentTooltip = "<col=A00000>" + var25 + "<br>" + var26;
          }
        }
      } else {
        final String ownerName = unratedLobbyRoom.ownerName;
        Component.GAME_OWNER_HEADING.label = Strings.format(StringConstants.XS_GAME, ownerName).toUpperCase();
        Component.WAITING_TO_START_LABEL.setBounds(0, Component.GAME_INFO_CONTAINER.height - 40, Component.GAME_INFO_CONTAINER.width, 40);
        Component.WAITING_TO_START_LABEL.label = Strings.format(StringConstants.WAITING_FOR_X_TO_START_GAME, ownerName);
        var8 = Component.WAITING_TO_START_LABEL;
      }

      if (cannotStartGameUntil != 0L) {
        var19 = (int) (cannotStartGameUntil - PseudoMonotonicClock.currentTimeMillis());
        var19 = (var19 + 999) / 1000;
        if (var19 < 1) {
          var19 = 1;
        }

        var8.label = Strings.format(StringConstants.GAME_OPTS_CHANGED, Integer.toString(var19));
      }

      Component.JOINED_PLAYER_COUNT_LABEL.label = Strings.format(StringConstants.PLAYERS_X_OF_Y, Integer.toString(unratedLobbyRoom.joinedPlayerCount), Integer.toString(unratedLobbyRoom.maxPlayerCount));
    }

    lobbyRoomLeftPanel.rootProcessMouseEvents(mouseNotYetHandled && !inGame && !invitePlayersDialogOpen);
    Component.GAME_INFO_CONTAINER.rootProcessMouseEvents(mouseNotYetHandled && !inGame && !invitePlayersDialogOpen);
    invitePlayersDialog.rootProcessMouseEvents(mouseNotYetHandled && !inGame && invitePlayersDialogOpen);
    lobbyRoomTabbedPlayerList.view.f487();

    if (unratedLobbyRoom != null) {
      if (Component.RETURN_TO_LOBBY_BUTTON.clickButton != MouseState.Button.NONE) {
        C2SPacket.requestToLeaveRoom(unratedLobbyRoom.roomId);
      }

      if (Component.FIND_OPPONENTS_BUTTON.clickButton != MouseState.Button.NONE) {
        findOpponentsButtonClicked = true;
      }

      if (Component.INVITE_PLAYERS_BUTTON.clickButton != MouseState.Button.NONE) {
        invitePlayersDialogOpen = true;
      }

      if (Component.CLOSE_BUTTON_2.clickButton != MouseState.Button.NONE) {
        invitePlayersDialogOpen = false;
      }

      a626sc(false, unratedLobbyRoom);
    }
  }

  private static void a626sc(final boolean var0, final ClientLobbyRoom room) {
    final boolean canChange = var0 ? !justRecievedRoomDetailsFromServer : (isCurrentRoomOwner() && !room.hasStarted);

    boolean didChangeOptions = false;
    if (!var0) {
      for (int i = 0; i < 5; ++i) {
        final ActionButton button = Component.GAME_OPTIONS_BUTTONS[0][i + 1];
        if (canChange && button.clickButton != MouseState.Button.NONE && room.whoCanJoin != i) {
          room.whoCanJoin = i;
          didChangeOptions = true;
        }
        button.enabled = canChange;
        button.selected = room.whoCanJoin == i;
      }

      if (Component.GAME_OPTIONS_BUTTONS[0][2].isMouseOver) {
        if (isCurrentRoomOwner()) {
          ClientLobbyRoom.currentTooltip = StringConstants.THIS_IS_RUNESCAPE_CLAN;
        } else {
          ClientLobbyRoom.currentTooltip = Strings.format(StringConstants.THIS_IS_RUNESCAPE_CLAN_NOT_OWNER, unratedLobbyRoom.ownerName);
        }
      }
    }

    for (int i = var0 ? -1 : 0; i < NUM_PLAYERS_OPTION_VALUES.length; ++i) {
      final ActionButton button = Component.GAME_OPTIONS_BUTTONS[1][i + 1];
      if (canChange && button.clickButton != MouseState.Button.NONE) {
        if (var0) {
          if (i == -1) {
            Arrays.fill(playerSeatsFilledBitmap, (byte) 0);
          } else {
            playerSeatsFilledBitmap[i >> 3] = (byte) ((int) playerSeatsFilledBitmap[i >> 3] ^ (1 << (i & 7)));
          }
        } else {
          final int playerCount = NUM_PLAYERS_OPTION_VALUES[i];
          if (room.maxPlayerCount != playerCount) {
            room.maxPlayerCount = playerCount;
            didChangeOptions = true;
          }
        }
      }

      if (!var0) {
        button.selected = room.maxPlayerCount == NUM_PLAYERS_OPTION_VALUES[i];
      } else if (i == -1) {
        Component.GAME_OPTIONS_BUTTONS[1][0].selected = true;

        for (int j = 0; j < NUM_PLAYERS_OPTION_VALUES.length; ++j) {
          Component.GAME_OPTIONS_BUTTONS[1][0].selected &= (playerSeatsFilledBitmap[j / 8] & 1 << (j & 7)) == 0;
        }
      } else {
        button.selected = (playerSeatsFilledBitmap[i / 8] & 1 << (i & 7)) != 0;
      }

      button.enabled = canChange;
    }

    for (int i = var0 ? 0 : 1; i < 3; ++i) {
      final ActionButton button = Component.GAME_OPTIONS_BUTTONS[2][i];
      if (canChange && button.clickButton != MouseState.Button.NONE) {
        if (var0) {
          if (i == 0) {
            allowSpectateSelection = 0;
          } else {
            allowSpectateSelection ^= i;
          }
        } else if (room.allowSpectate != i) {
          room.allowSpectate = i;
          didChangeOptions = true;
        }
      }

      button.enabled = canChange;
      if (var0) {
        if (i == 0) {
          button.selected = allowSpectateSelection == 0;
        } else {
          button.selected = (i & allowSpectateSelection) != 0;
        }
      } else {
        button.selected = (i & room.allowSpectate) != 0;
      }
    }

    if (var0) {
      for (int i = 0; i < 2; ++i) {
        final ActionButton button = Component.GAME_OPTIONS_BUTTONS[3][i];
        if (canChange && button.clickButton != MouseState.Button.NONE) {
          maxAiPlayersSelection = i;
        }

        button.enabled = canChange;
        button.selected = maxAiPlayersSelection == i;
      }
    }

    int var6 = 0;
    for (int i = 0; i < GAMEOPT_CHOICES_COUNTS.length; ++i) {
      final ActionButton[] buttons = Component.GAME_OPTIONS_BUTTONS[i + 4];
      for (int j = var0 ? -1 : 0; j < buttons.length - 1; ++j) {
        boolean var31 = false;
        _wgd = true;
        if (j >= 0 && _peD != null && (!var0 || !justRecievedRoomDetailsFromServer)) {
          _wgd = false;
          if (_lrc == null) {
            _lrc = new byte[GAMEOPT_CHOICES_COUNTS.length];
            _ekF = new boolean[GAMEOPT_CHOICES_COUNTS.length];
          }

          _sbh = false;

          for (int var17 = 0; var17 < i; ++var17) {
            _ekF[var17] = false;
          }

          a068js(-1, i, 0, var0, j, -1);
          if (adminLevel >= 2 && keysDown[12]) {
            _wgd = true;
          }

          if (!_wgd) {
            var31 = true;
          }
        }

        final ActionButton button = buttons[j + 1];
        int var18;
        if (canChange && button.clickButton != MouseState.Button.NONE) {
          if (!var0) {
            if (!var31 && (byte) j != room.gameSpecificOptions[i]) {
              room.gameSpecificOptions[i] = (byte) j;
              didChangeOptions = true;
            }
          } else if (j == -1) {
            for (var18 = var6; var18 < buttons.length + (var6 - 1); ++var18) {
              final int var11 = ~(1 << (var18 & 7));
              gameoptChoicesBitmap[var18 / 8] = (byte) ((int) gameoptChoicesBitmap[var18 / 8] & var11);
            }
          } else {
            gameoptChoicesBitmap[(j + var6) / 8] = (byte) ((int) gameoptChoicesBitmap[(j + var6) / 8] ^ 1 << (j + var6 & 7));
          }
        }

        if (var0 && var31) {
          final int var11 = ~(1 << (7 & var6 + j));
          gameoptChoicesBitmap[(j + var6) / 8] = (byte) ((int) gameoptChoicesBitmap[(j + var6) / 8] & var11);
        }

        if (j >= 0 && button.isMouseOver) {
          final String optionName = StringConstants.GAMEOPT_NAMES[i] != null ? StringConstants.GAMEOPT_NAMES[i][j] : null;
          final String optionTooltip = GAMEOPT_TOOLTIPS[i] != null ? GAMEOPT_TOOLTIPS[i][j] : null;
          String maybeOptionTooltip = null;
          if (optionTooltip != null && !optionTooltip.equals(optionName)) {
            maybeOptionTooltip = optionTooltip;
          }

          String var21 = null;
          String var34;
          if (canChange && !_wgd) {
            var34 = null;
            boolean var23 = false;
            if (_sbh) {
              var34 = "</col>" + StringConstants.MU_OPTIONS_PLAYERS + "<col=A00000>";
            }

            for (int var24 = 0; i > var24; ++var24) {
              if (_ekF[var24]) {
                final String var25 = "</col>" + StringConstants.GAMEOPT_LABELS[var24] + "<col=A00000>";
                if (var34 == null) {
                  var34 = var25;
                } else {
                  var34 = var34 + ", " + var25;
                  var23 = true;
                }
              }
            }

            if (var23) {
              var21 = StringConstants.GAMEOPT_CANNOT_BE_COMBINED_2 + var34;
            } else {
              var21 = Strings.format(StringConstants.GAMEOPT_CANNOT_BE_COMBINED_1, var34);
            }
          }

          if (var21 != null) {
            var21 = "<col=A00000>" + var21;
            var21 = Component.a369(var21);
            if (maybeOptionTooltip == null) {
              maybeOptionTooltip = var21;
            } else {
              maybeOptionTooltip = maybeOptionTooltip + "<br>" + var21;
            }
          }

          if (maybeOptionTooltip != null) {
            ClientLobbyRoom.currentTooltip = maybeOptionTooltip;
          }
        }

        if (var0) {
          if (j == -1) {
            button.selected = true;

            for (var18 = var6; var18 < buttons.length + var6 - 1; ++var18) {
              button.selected &= (gameoptChoicesBitmap[var18 / 8] & 1 << (var18 & 7)) == 0;
            }
          } else {
            button.selected = (gameoptChoicesBitmap[(var6 + j) / 8] & 1 << (7 & j + var6)) != 0;
          }
        } else {
          button.selected = _wgd && room.gameSpecificOptions[i] == (byte) j;
        }

        button.enabled = canChange && !var31;
      }

      var6 += GAMEOPT_CHOICES_COUNTS[i];
    }

    if (didChangeOptions && !var0) {
      C2SPacket.updateRoomOptions();
    }
  }

  private static void a068js(final int var0, final int var3, int var4, final boolean var5, final int var6, int var7) {
    if (var3 > var0) {
      final ActionButton[] var8 = Component.GAME_OPTIONS_BUTTONS[var0 != -1 ? 4 + var0 : 1];
      boolean var9 = true;
      if (var5) {
        int var10;
        if (var0 == -1) {
          for (var10 = 0; NUM_PLAYERS_OPTION_VALUES.length > var10; ++var10) {
            if ((playerSeatsFilledBitmap[var10 / 8] & 1 << (var10 & 7)) != 0) {
              var9 = false;
              break;
            }
          }
        } else {
          for (var10 = 0; var8.length - 1 > var10; ++var10) {
            if ((gameoptChoicesBitmap[(var10 + var4) / 8] & 1 << (7 & var4 + var10)) != 0) {
              var9 = false;
              break;
            }
          }

          var4 += GAMEOPT_CHOICES_COUNTS[var0];
        }
      }

      boolean var17 = false;

      int var11;
      for (var11 = 0; (var0 == -1 ? NUM_PLAYERS_OPTION_VALUES.length : var8.length - 1) > var11; ++var11) {
        if (var0 == -1) {
          var7 = var11;
        } else {
          _lrc[var0] = (byte) var11;
        }

        final boolean var12;
        final ActionButton var13 = var8[1 + var11];
        if (var5) {
          var12 = var13.selected || var9 && var13.enabled;
        } else {
          var12 = var0 != -1 ? (255 & unratedLobbyRoom.gameSpecificOptions[var0]) == var11 : NUM_PLAYERS_OPTION_VALUES[var11] == unratedLobbyRoom.maxPlayerCount;
        }

        if (var12) {
          a068js(1 + var0, var3, var4, var5, var6, var7);
          var17 = true;
        }

        if (_wgd) {
          return;
        }
      }

      if (!var17) {
        for (var11 = 0; var11 < var8.length - 1; ++var11) {
          if (var0 == -1) {
            var7 = var11;
          } else {
            _lrc[var0] = (byte) var11;
          }

          a068js(var0 + 1, var3, var4, var5, var6, var7);
          if (_wgd) {
            return;
          }
        }
      }
    } else {
      boolean var15 = true;

      label148:
      for (final int[] var18 : _peD) {
        boolean var19 = false;

        int var20;
        int var21;
        for (var20 = 0; var20 < var18.length; var20 += 2) {
          var21 = var18[var20];
          final int var14 = var18[1 + var20];
          if (var21 == -1) {
            if (NUM_PLAYERS_OPTION_VALUES[var7] != var14) {
              continue label148;
            }
          } else if (var21 == var0 && var6 == var14) {
            var19 = true;
          } else if (var0 <= var21 || (_lrc[var21] & 255) != var14) {
            continue label148;
          }
        }

        if (var19 || GAMEOPT_CHOICES_COUNTS.length == var0) {
          for (var20 = 0; var18.length > var20; var20 += 2) {
            var21 = var18[var20];
            if (var21 == -1) {
              _sbh = true;
            } else if (var21 < var0) {
              _ekF[var21] = true;
            }
          }

          var15 = false;
        }
      }

      if (var15) {
        _wgd = true;
      }
    }

  }

  private static void tickLobbyTransitionCounters(final boolean inGame) {
    if (inGame || unratedLobbyRoom == null) {
      invitePlayersDialogOpen = false;
    }

    if (inGame) {
      if (lobbyBrowserTransitionCounter > 0) {
        --lobbyBrowserTransitionCounter;
      } else if (ratedLobbyRoomTransitionCounter > 0) {
        --ratedLobbyRoomTransitionCounter;
      } else if (unratedLobbyRoomTransitionCounter > 0) {
        --unratedLobbyRoomTransitionCounter;
      }
    } else if (unratedLobbyRoom == null) {
      if (ratedLobbyRoom == null) {
        if (ratedLobbyRoomTransitionCounter > 0) {
          --ratedLobbyRoomTransitionCounter;
        } else if (unratedLobbyRoomTransitionCounter > 0) {
          --unratedLobbyRoomTransitionCounter;
        } else if (lobbyBrowserTransitionCounter < 20) {
          if (lobbyBrowserTransitionCounter == 0) {
            Component.initializeLobbyBrowserComponents();
          }

          ++lobbyBrowserTransitionCounter;
        }
      } else if (lobbyBrowserTransitionCounter > 0) {
        --lobbyBrowserTransitionCounter;
      } else if (unratedLobbyRoomTransitionCounter > 0) {
        --unratedLobbyRoomTransitionCounter;
      } else if (ratedLobbyRoomTransitionCounter < 20) {
        if (ratedLobbyRoomTransitionCounter == 0) {
          initializeLobbyRoomComponents(true);
        }

        ++ratedLobbyRoomTransitionCounter;
      }
    } else if (lobbyBrowserTransitionCounter > 0) {
      --lobbyBrowserTransitionCounter;
    } else if (ratedLobbyRoomTransitionCounter > 0) {
      --ratedLobbyRoomTransitionCounter;
    } else if (unratedLobbyRoomTransitionCounter < 20) {
      if (unratedLobbyRoomTransitionCounter == 0) {
        initializeLobbyRoomComponents(false);
      }
      ++unratedLobbyRoomTransitionCounter;
    }
  }

  private static void initializeLobbyRoomComponents(final boolean isRated) {
    lobbyRoomTabbedPlayerList = isRated
        ? new TabbedPlayerListWrapper(StringConstants.MU_CHAT_TIPS, Component.RATED_GAME_TIPS_LABEL)
        : new TabbedPlayerListWrapper(StringConstants.MU_CHAT_GAME, Component.JOINED_PLAYERS_PANEL);
    lobbyRoomLeftPanel = new Component<>(null);
    lobbyRoomLeftPanel.addChild(lobbyRoomTabbedPlayerList.view);
    lobbyRoomLeftPanel.addChild(Component.RETURN_TO_LOBBY_BUTTON);
    invitePlayersDialog = new Component<>(null);
    invitePlayersDialogContents = new Component<>(null);
    invitePlayersDialog.addChild(Component.INVITE_PLAYERS_LABEL);
    invitePlayersDialog.addChild(invitePlayersDialogContents);
    invitePlayersDialogContents.addChild(Component.INVITE_PLAYER_LIST_PANEL);
    invitePlayersDialogContents.addChild(Component.CLOSE_BUTTON_2);
    layoutLobbyRoom(isRated);
  }

  public static void switchMenus() {
    if (Menu.currentMenu != Menu.nextMenu) {
      if (Menu.currentMenu >= 0) {
        Menu.menus[Menu.currentMenu].prepareToSwitchAwayFrom();
      }

      _cjx = _tli;
      Menu._ehQ = 0;
      Menu.currentMenu = Menu.nextMenu;
      if (Menu.nextMenu == Menu.Id.INTRODUCTION) {
        IntroAnimation.reset();
      }

      if (Menu.nextMenu == Menu.Id.MAIN && !Sounds.musicTn.a419(currentTrack)) {
        a827jo(currentTrack, 0x100000, true);
      }

      if (Menu.nextMenu == Menu.Id.LOADING_LOBBY) {
        C2SPacket.enterLobby();
        _mdQ = true;
        Menu.nextMenu = Menu.currentMenu;
      }

      if (_lgb) {
        if (ClientGameSession.playSession != null && ClientGameSession.playSession.ui != null) {
          ClientGameSession.playSession.ui.destroy();
        }

        if (ClientGameSession.spectateSession != null && ClientGameSession.spectateSession.ui != null) {
          ClientGameSession.spectateSession.ui.destroy();
        }

        _lgb = false;
        ClientGameSession.playSession = null;
        ClientGameSession.spectateSession = null;
      }

      if (Menu.discarding) {
        Menu.unlockedAchievementsBitmap = 0;
        Menu.discarding = false;
      }

      if (leavingLobby) {
        a423ppi();
        e423ec();
        PlayerListEntry.reset();
        leavingLobby = false;
      }
    }
  }

  private static void a714jc(final Sprite var0) {
    Drawing.saveContext();
    _dmgn = new Sprite(128, 128);
    _dmgn.installForDrawing();
    var0.draw(0, 0);
    final Sprite _ksh = new Sprite(256, 256);
    _ksh.installForDrawing();
    _dmgn.b115(0, 0, 256, 256);
    Drawing.b669(3, 3, 256, 256);
    _lqo = new Sprite(32, 32);
    _lqo.installForDrawing();
    _dmgn.g093();
    _jcnk = new Sprite(256, 256);
    _jcnk.installForDrawing();
    _dmgn.draw(64, 64);
    Drawing.b669(12, 12, 256, 256);
    _msb = new Sprite(64, 64);
    _msb.installForDrawing();
    _lqo.draw(16, 16);
    Drawing.b669(3, 3, 64, 64);
    Drawing.restoreContext();
  }

  private static boolean b154chmr() {
    final long var1 = PseudoMonotonicClock.currentTimeMillis();
    final long var3 = var1 - _tkz;
    if (var3 <= 30000L) {
      short var5 = 3000;
      if (_vjC < 7) {
        if (_vjC < 5) {
          if (_vjC >= 3) {
            var5 = 6000;
          }
        } else {
          var5 = 9000;
        }
      } else {
        var5 = 12000;
      }

      if (var3 <= (long) var5) {
        return false;
      } else {
        ++_vjC;
        _tkz = var1;
        return true;
      }
    } else {
      _vjC = 0;
      _tkz = var1;
      return true;
    }
  }

  private static void tickQuickChat(final boolean var0) {
    QuickChatHelpPanel.tick();
    tickQuickChatSelector(var0);
  }

  private static void tickQuickChatSelector(boolean var1) {
    if (isQuickChatOpen) {
      Component._uaf.rootProcessMouseEvents(var1);
      final boolean var4 = _dmrh.f491();
      if (JagexApplet.mouseButtonJustClicked != MouseState.Button.NONE && !var4) {
        closeQuickChatIfOpen();
        var1 = false;
      }

      if (var1) {
        _dmrh.a599();
      }

      if (var4) {
        Component._uaf.rootProcessMouseEvents(var1);
      }

      final int var5 = _dmrh.g474() + _dmrh.x2;
      if (var5 <= SCREEN_WIDTH) {
        if (var5 < 635 && _cvcm > 0) {
          _cvcm -= 5;
        }
      } else {
        _cvcm += 5;
      }

    }
  }

  private static void i150ak() {
    if (lobbyBrowserTransitionCounter <= 0) {
      if (ratedLobbyRoomTransitionCounter > 0) {
        --ratedLobbyRoomTransitionCounter;
      } else if (unratedLobbyRoomTransitionCounter > 0) {
        --unratedLobbyRoomTransitionCounter;
      }
    } else {
      --lobbyBrowserTransitionCounter;
    }

    if (lobbyBrowserTransitionCounter > 0) {
      Component.layoutLobbyBrowserPanels();
    }

    if (ratedLobbyRoomTransitionCounter > 0) {
      layoutLobbyRoomPanels(ratedLobbyRoomTransitionCounter);
    }

    if (unratedLobbyRoomTransitionCounter > 0) {
      layoutLobbyRoomPanels(unratedLobbyRoomTransitionCounter);
    }

  }

  private static void layoutLobbyRoom(final boolean isRated) {
    Component.lastLayoutWidth = Drawing.width;
    Component.lastLayoutHeight = Drawing.height;
    layoutLobbyRoomPanels(isRated ? ratedLobbyRoomTransitionCounter : unratedLobbyRoomTransitionCounter);
    lobbyRoomTabbedPlayerList.updateBounds(lobbyRoomLeftPanel.width, lobbyRoomLeftPanel.height - 42);
    Component.JOINED_PLAYER_COUNT_LABEL.setBounds(0, 0, Component.JOINED_PLAYERS_PANEL.width, Component.LABEL_HEIGHT);
    Component.NAME_LABEL.setBounds(0, Component.LABEL_HEIGHT + 2, Component.JOINED_PLAYERS_PANEL.width - (4 + Component.LABEL_HEIGHT + 40), 18);
    Component.RATING_LABEL.setBounds(Component.JOINED_PLAYERS_PANEL.width - Component.LABEL_HEIGHT - 40 - 2, Component.LABEL_HEIGHT + 2, 57, 18);
    //noinspection SuspiciousNameCombination
    Component.JOINED_PLAYERS_TABLE.setBounds(0, 22 + Component.LABEL_HEIGHT, Component.JOINED_PLAYERS_PANEL.width, -Component.LABEL_HEIGHT + (Component.JOINED_PLAYERS_PANEL.height - 2) - 20, Component.LABEL_HEIGHT);
    Component.RETURN_TO_LOBBY_BUTTON.setBounds(0, lobbyRoomLeftPanel.height - 40, lobbyRoomLeftPanel.width, 40);
    Component.GAME_OWNER_HEADING.setBounds(0, 0, Component.GAME_INFO_CONTAINER.width, 30);
    Component.GAME_OPTIONS_CONTAINER.setBounds(0, 30, Component.GAME_INFO_CONTAINER.width, Component.GAME_INFO_CONTAINER.height - 30 - 42);
    int var2 = 3 + GAMEOPT_CHOICES_COUNTS.length;

    if (isRated) {
      --var2;
    }

    int var3 = (Component.GAME_OPTIONS_CONTAINER.height + 2 + (var2 + 1) / 2 - 20) / (var2 + 1) - 2;
    if (var3 > 30) {
      var3 = 30;
    }

    int var4 = -(var2 * (var3 + 2)) - 5 + (Component.GAME_OPTIONS_CONTAINER.height - 5);
    if (var4 > 40) {
      var4 = 40;
    }

    Component.GAME_OPTIONS_HEADING.setBounds(5, 5, Component.GAME_OPTIONS_CONTAINER.width - 5 - 5, var4);
    int var5 = 2 + var4 + 5;

    for (int var6 = 0; GAMEOPT_CHOICES_COUNTS.length + 4 > var6; ++var6) {
      if (var6 != 3) {
        int var8;
        ActionButton var9;
        final Component<?> var13;
        if (isRated && var6 == 0) {
          var13 = Component.GAME_OPTIONS_LABELS[0];
          Component.GAME_OPTIONS_LABELS[0].height = 0;
          var13.width = 0;

          for (var8 = 0; Component.GAME_OPTIONS_BUTTONS[var6].length > var8; ++var8) {
            if (Component.GAME_OPTIONS_BUTTONS[var6][var8] != null) {
              var9 = Component.GAME_OPTIONS_BUTTONS[var6][var8];
              Component.GAME_OPTIONS_BUTTONS[var6][var8].height = 0;
              var9.width = 0;
            }
          }
        } else {
          final int var15;
          Component.GAME_OPTIONS_LABELS[var6].setBounds(5, var5, 103, var3);
          var8 = 110;
          if (isRated) {
            Component.GAME_OPTIONS_BUTTONS[var6][0].a370(var3, var5, 2, ClientLobbyRoom._qob, 38, var8);
            var8 += 40;
          } else if (Component.GAME_OPTIONS_BUTTONS[var6][0] != null) {
            var9 = Component.GAME_OPTIONS_BUTTONS[var6][0];
            Component.GAME_OPTIONS_BUTTONS[var6][0].height = 0;
            var9.width = 0;
          }

          var15 = 2 + (Component.GAME_INFO_CONTAINER.width - var8 - 5);
          final int var10 = Component.GAME_OPTIONS_BUTTONS[var6].length - 1;

          for (int var11 = 0; var11 < var10; ++var11) {
            final int var12 = var11 * var15 / var10;
            Component.GAME_OPTIONS_BUTTONS[var6][1 + var11].a370(var3, var5, 2, ClientLobbyRoom._qob, -var12 + ((1 + var11) * var15 / var10 - 2), var12 + var8);
          }

          var5 += var3 + 2;
        }
      }
    }

    invitePlayersDialog.setBounds(Drawing.width - 360 >> 1, 10, 360, Drawing.height - 10 - 120 - 4 - 10);
    Component.INVITE_PLAYERS_LABEL.setBounds(0, 0, invitePlayersDialog.width, 24);
    invitePlayersDialogContents.setBounds(0, 24, invitePlayersDialog.width, invitePlayersDialog.height - 24);
    invitePlayersDialogContents.nineSliceSprites = Component.createGradientOutlineSprites(invitePlayersDialogContents.height, 11579568, 8421504, 1);
    Component.INVITE_PLAYER_LIST_PANEL.setBounds(5, 5, invitePlayersDialogContents.width - 10, invitePlayersDialogContents.height - 2 - 10 - 24);
    Component.CLOSE_BUTTON_2.setBounds((invitePlayersDialogContents.width - 80) / 2, invitePlayersDialogContents.height - 5 - 24, 80, 24);
    d150p();
  }

  public static void d150p() {
    Component.INVITE_PLAYER_LIST_HEADING_NAME.setBounds(0, 0, Component.INVITE_PLAYER_LIST_PANEL.width - (42 + Component.LABEL_HEIGHT + 2), 18);
    Component.INVITE_PLAYER_LIST_HEADING_RATING.setBounds(Component.INVITE_PLAYER_LIST_PANEL.width - (17) - 40, 0, 57, 18);
    //noinspection SuspiciousNameCombination
    Component.INVITE_PLAYER_LIST_SCROLL_PANE.setBounds(0, 20, Component.INVITE_PLAYER_LIST_PANEL.width, Component.INVITE_PLAYER_LIST_PANEL.height - 20, Component.LABEL_HEIGHT);
  }

  private static Sprite[] a113hl() {
    Drawing.saveContext();
    final Sprite[] var1 = new Sprite[80];
    final Sprite var2 = new Sprite(240, 120);

    for (int var3 = 0; var3 < 80; ++var3) {
      var2.installForDrawing();
      Drawing.clear();

      for (int var4 = 0; var4 < 4; ++var4) {
        final double var5 = (double) (80 * (3 + var4) + var3) * 6.283185307179586D;
        final double var7 = Math.sin(var5 / (double) (80 * 4));
        final double var9 = Math.cos(var5 / (double) (80 * 4));
        final int var11 = (int) (12288.0D / (var9 + 3.0D));
        final int var12 = 1920 + (int) (var7 * 2400.0D / (3.0D + var9));
        final int var13 = (int) (1920.0D / (3.0D + var9));
        BEAKER.b669(BEAKER.offsetX << 3, BEAKER.offsetY << 3, var12, var13, 0, var11);
      }

      var1[var3] = new Sprite(60, 30);
      var1[var3].installForDrawing();
      var2.g093();
    }

    Drawing.restoreContext();
    return var1;
  }

  private static void a641tvc(final LobbyPlayer var1, final ScrollPane<?> var3, final int mouseX, final int mouseY) {
    showContextMenu(var3, var1, var1.playerId, var1.playerName, var1.playerDisplayName, -1, null);
    ContextMenu.openInstance.addPlayerItems(true);
    ContextMenu.openInstance.a124(null, Channel.LOBBY);
    ContextMenu.openInstance.b150();
    ContextMenu.openInstance.view.positionRelativeToTarget(mouseX, mouseY);
  }

  private static void e423ec() {
    ratedLobbyRoom = null;
    unratedLobbyRoom = null;
    justRecievedRoomDetailsFromServer = false;
    if (LobbyPlayer.online != null) {
      LobbyPlayer.online.clear();
      LobbyPlayer.online = null;
    }

    LobbyPlayer.onlineMap = null;
    if (ClientLobbyRoom.rooms != null) {
      ClientLobbyRoom.rooms.clear();
      ClientLobbyRoom.rooms = null;
    }

    ClientLobbyRoom.roomsMap = null;
    if (LobbyPlayer.joinedPlayers != null) {
      LobbyPlayer.joinedPlayers.clear();
      LobbyPlayer.joinedPlayers = null;
    }

    LobbyPlayer.joinedPlayersMap = null;
    if (LobbyPlayer.onlineMap != null) {
      LobbyPlayer.onlineMap.clear();
      LobbyPlayer.onlineMap = null;
    }
  }

  private static void handleLobbyPacket() {
    @MagicConstant(valuesFromClass = S2CPacket.LobbyAction.class)
    final int action = s2cPacket.readUByte();
    if (action == S2CPacket.LobbyAction.YOU_LEFT_ROOM
        || action == S2CPacket.LobbyAction.YOU_WERE_KICKED
        || action == S2CPacket.LobbyAction.YOU_CREATED_RANKED_ROOM
        || action == S2CPacket.LobbyAction.YOU_JOINED_RANKED_ROOM
        || action == S2CPacket.LobbyAction.YOU_JOINED_ROOM) {
      LobbyPlayer.joinedPlayers.clear();
      LobbyPlayer.joinedPlayersMap.clear();

      for (final LobbyPlayer player : LobbyPlayer.onlineMap.values()) {
        if (player.inviteSent || player.joinRequestReceived) {
          player.inviteSent = false;
          if (player.joinRequestReceived) {
            --joinRequestCount;
            player.joinRequestReceived = false;
          }
          LobbyPlayer.addOnline(player);
        }
      }

      if (action == S2CPacket.LobbyAction.YOU_WERE_KICKED && unratedLobbyRoom != null) {
        playerWhoKickedYou = unratedLobbyRoom.ownerName;
      }

      if (action == S2CPacket.LobbyAction.YOU_JOINED_ROOM) {
        final int roomId = s2cPacket.readUShort();
        unratedLobbyRoom = new ClientLobbyRoom(roomId);
        unratedLobbyRoom.initializeFromServer(s2cPacket, false);
        cannotStartGameUntil = 0L;
      } else {
        unratedLobbyRoom = null;
      }

      if (action == S2CPacket.LobbyAction.YOU_CREATED_RANKED_ROOM || action == S2CPacket.LobbyAction.YOU_JOINED_RANKED_ROOM) {
        if (ratedLobbyRoom == null) {
          ratedLobbyRoom = new ClientLobbyRoom(0);
        }
      } else {
        ratedLobbyRoom = null;
      }

      if (action == S2CPacket.LobbyAction.YOU_JOINED_RANKED_ROOM) {
        justRecievedRoomDetailsFromServer = true;
        for (int i = 0; i < playerSeatsFilledBitmap.length; ++i) {
          playerSeatsFilledBitmap[i] = s2cPacket.readByte();
        }

        maxAiPlayersSelection = s2cPacket.readUByte();
        allowSpectateSelection = s2cPacket.readUByte();

        for (int i = 0; i < gameoptChoicesBitmap.length; ++i) {
          gameoptChoicesBitmap[i] = s2cPacket.readByte();
        }
      } else {
        justRecievedRoomDetailsFromServer = false;
      }
    } else if (action == S2CPacket.LobbyAction.PLAYER_ENTERED_LOBBY) {
      final long playerId = s2cPacket.readLong();
      final String playerName = s2cPacket.readNullTerminatedString();
      final String previousDisplayName = s2cPacket.readNullTerminatedString();
      final String playerDisplayName = s2cPacket.readNullTerminatedString();
      LobbyPlayer player = LobbyPlayer.getOnline(playerId);
      if (player == null) {
        player = new LobbyPlayer(playerId, playerName, playerDisplayName);
        LobbyPlayer.onlineMap.put(playerId, player);
      } else if (!previousDisplayName.isEmpty()) {
        player.setName(playerName, playerDisplayName);
      }

      player.joinedAt = PseudoMonotonicClock.currentTimeMillis() - (long) s2cPacket.readInt();
      player.rating = s2cPacket.readUShort();
      player.ratedGameCount = s2cPacket.readVariableInt() >> 1;
      player.crown = s2cPacket.readUByte();
      player.unlockedOptionsBitmap = s2cPacket.readUByte();
      LobbyPlayer.addOnline(player);
    } else if (action == S2CPacket.LobbyAction.PLAYER_LEFT_LOBBY) {
      final long playerId = s2cPacket.readLong();
      final int reason = s2cPacket.readUByte();
      final LobbyPlayer player = LobbyPlayer.getOnline(playerId);
      if (player != null) {
        if (player.joinRequestReceived) {
          --joinRequestCount;
          player.joinRequestReceived = false;
        }

        if (reason == 0) {
          LobbyPlayer.online.remove(player);
        } else {
          player.status = reason;
          player.statusTimer = STATUS_MESSAGE_TIMEOUT_DURATION;
        }

        LobbyPlayer.onlineMap.remove(playerId);
      }
    } else if (action == S2CPacket.LobbyAction.REMOVE_ALL_JOIN_REQUESTS) {
      LobbyPlayer.online.clear();
      LobbyPlayer.onlineMap.clear();
      joinRequestCount = 0;
    } else if (action == S2CPacket.LobbyAction.ADD_ROOM) {
      final int roomId = s2cPacket.readUShort();
      ClientLobbyRoom room = ClientLobbyRoom.roomsMap.get(roomId);
      if (room == null) {
        room = new ClientLobbyRoom(roomId);
        ClientLobbyRoom.roomsMap.put(roomId, room);
      }
      room.initializeFromServer(s2cPacket, true);
      ClientLobbyRoom.add(room);
    } else if (action == S2CPacket.LobbyAction.REMOVE_ROOM) {
      final int roomId = s2cPacket.readUShort();
      final int reason = s2cPacket.readUByte();
      final ClientLobbyRoom room = ClientLobbyRoom.roomsMap.get(roomId);
      if (room != null) {
        if (reason == 0) {
          ClientLobbyRoom.rooms.remove(room);
        } else {
          room.status = reason;
          room.statusTimer = STATUS_MESSAGE_TIMEOUT_DURATION;
        }
        ClientLobbyRoom.roomsMap.remove(roomId);
      }
    } else if (action == S2CPacket.LobbyAction.REMOVE_ALL_ROOMS) {
      ClientLobbyRoom.rooms.clear();
      ClientLobbyRoom.roomsMap.clear();
    } else if (action == S2CPacket.LobbyAction.YOU_ARE_INVITED || action == S2CPacket.LobbyAction.YOU_SENT_JOIN_REQUEST) {
      final int roomId = s2cPacket.readUShort();
      final ClientLobbyRoom room = ClientLobbyRoom.roomsMap.get(roomId);
      if (room != null) {
        if (action == S2CPacket.LobbyAction.YOU_ARE_INVITED) {
          room.youAreInvited = true;
        } else {
          room.submittedJoinRequest = true;
        }
        ClientLobbyRoom.add(room);
      }
    } else if (action == S2CPacket.LobbyAction.ROOM_STATUS) {
      final int roomId = s2cPacket.readUShort();
      final int status = s2cPacket.readUByte();
      final ClientLobbyRoom room = ClientLobbyRoom.roomsMap.get(roomId);
      if (room != null) {
        room.submittedJoinRequest = false;
        room.youAreInvited = false;
        if (status != 0) {
          room.status = status;
          room.statusTimer = STATUS_MESSAGE_TIMEOUT_DURATION;
        }
        ClientLobbyRoom.add(room);
      }
    } else if (action == S2CPacket.LobbyAction.ADD_PLAYER_INVITE || action == S2CPacket.LobbyAction.ADD_PLAYER_JOIN_REQUEST) {
      final long playerId = s2cPacket.readLong();
      final LobbyPlayer player = LobbyPlayer.getOnline(playerId);
      if (player != null) {
        if (action == S2CPacket.LobbyAction.ADD_PLAYER_INVITE) {
          player.inviteSent = true;
        } else if (!player.joinRequestReceived) {
          ++joinRequestCount;
          player.joinRequestReceived = true;
        }
        LobbyPlayer.addOnline(player);
      }
    } else if (action == S2CPacket.LobbyAction.REMOVE_PLAYER_INVITE || action == S2CPacket.LobbyAction.REMOVE_PLAYER_JOIN_REQUEST) {
      final long playerId = s2cPacket.readLong();
      final int reason = s2cPacket.readUByte();
      final LobbyPlayer player = LobbyPlayer.getOnline(playerId);
      if (player != null) {
        if (action == S2CPacket.LobbyAction.REMOVE_PLAYER_INVITE) {
          player.inviteSent = false;
        } else if (player.joinRequestReceived) {
          --joinRequestCount;
          player.joinRequestReceived = false;
        }
        if (reason != 0) {
          player.status = reason;
          player.statusTimer = STATUS_MESSAGE_TIMEOUT_DURATION;
        }
        LobbyPlayer.addOnline(player);
      }
    } else if (action == S2CPacket.LobbyAction.PLAYER_JOINED_ROOM) {
      final long playerId = s2cPacket.readLong();
      final String playerName = s2cPacket.readNullTerminatedString();
      final String playerDisplayName = s2cPacket.readNullTerminatedString();
      LobbyPlayer player = LobbyPlayer.getJoined(playerId);
      if (player == null) {
        player = new LobbyPlayer(playerId, playerName, playerDisplayName);
        LobbyPlayer.joinedPlayersMap.put(playerId, player);
        ++unratedLobbyRoom.joinedPlayerCount;
      }

      player.rating = s2cPacket.readUShort();
      player.ratedGameCount = s2cPacket.readVariableInt() >> 1;
      player.crown = s2cPacket.readUByte();
      player.unlockedOptionsBitmap = s2cPacket.readUByte();
      LobbyPlayer.joinedPlayers.add(player);
    } else if (action == S2CPacket.LobbyAction.PLAYER_LEFT_ROOM) {
      final long playerId = s2cPacket.readLong();
      final int reason = s2cPacket.readUByte();
      final LobbyPlayer player = LobbyPlayer.getJoined(playerId);
      if (player != null) {
        if (reason == 0) {
          LobbyPlayer.joinedPlayers.remove(player);
        } else {
          player.status = reason;
          player.statusTimer = STATUS_MESSAGE_TIMEOUT_DURATION;
        }
        LobbyPlayer.joinedPlayersMap.remove(playerId);
        --unratedLobbyRoom.joinedPlayerCount;
      }
    } else if (action == S2CPacket.LobbyAction.ROOM_INFO) {
      unratedLobbyRoom.initializeFromServer(s2cPacket, false);
    } else if (action == S2CPacket.LobbyAction.GAME_OPTIONS_CHANGED) {
      final int cannotStartTimeout = s2cPacket.readUShort();
      if (cannotStartTimeout == 0) {
        cannotStartGameUntil = 0L;
      } else {
        cannotStartGameUntil = PseudoMonotonicClock.currentTimeMillis() + (long) cannotStartTimeout;
      }
    } else if (action == S2CPacket.LobbyAction.RATING) {
      playerRating = s2cPacket.readUShort();
      s2cPacket.readVariableInt();
    } else if (action == S2CPacket.LobbyAction.PLAYER_ID) {
      localPlayerId = s2cPacket.readLong();
    } else {
      clientError(null, "L1: " + a738w());
      shutdownServerConnection();
    }
  }

  public static boolean isCurrentRoomOwner() {
    return unratedLobbyRoom.ownerId == localPlayerId;
  }

  public static boolean isPopupOpen() {
    return ContextMenu.openInstance != null
        || AddOrRemovePlayerPopup.openInstance != null
        || ReportAbuseDialog.openInstance != null
        || QuickChatHelpPanel.openInstance != null
        || isQuickChatOpen;
  }

  private static boolean processQuickChatKeyInput() {
    if (lastTypedKeyCode == KeyState.Code.ESCAPE) {
      closeQuickChatIfOpen();
      return true;
    } else if (lastTypedKeyCode == KeyState.Code.HOME) {
      _dmrh.close();
      return true;
    } else {
      return _dmrh != null && _dmrh.a777();
    }
  }

  public static void a827jo(final SongData track, final int var3, final boolean var2) {
    Sounds.musicTn.a180(track, var3, !var2);
  }

  public static int randomIntBounded(final int var0) {
    return randomIntBounded(globalRandom, var0);
  }

  public static int randomIntBounded(final Random rand, final int end) {
    if (end <= 0) {
      throw new IllegalArgumentException();
    } else if (end == (-end & end)) {
      return (int) (((long) end * ((long) rand.nextInt() & 0xffffffffL)) >> 32);
    } else {
      final int var2 = -((int) (4294967296L % (long) end)) - Integer.MIN_VALUE;

      int var3;
      do {
        var3 = rand.nextInt();
      } while (var3 >= var2);

      return a313jf(end, var3);
    }
  }

  private static GetProfileRequest loadProfile() {
    final GetProfileRequest response = new GetProfileRequest();
    getProfileResponses.add(response);
    C2SPacket.sendProfileGet();
    return response;
  }

  private static void discardTypedChatMessage() {
    typedChatMessage.setLength(0);
    currentChatChannel = Channel.LOBBY;
  }

  private static void a430mj(final boolean var1) {
    if (ClientLobbyRoom.currentTooltip != null) {
      a407dk(ClientLobbyRoom.currentTooltip);
    }

    if (ContextMenu.openInstance != null) {
      ContextMenu.openInstance.a430(var1);
    }

    a893r(var1);
    if (ReportAbuseDialog.openInstance != null) {
      ReportAbuseDialog.openInstance.b540(var1);
    }

    a540ho(var1);
  }

  private static void a430se() {
    for (final RankingsRequest request : Menu.rankingsRequests) {
      C2SPacket.a970uf(request);
    }
  }

  public static String a034ih(final CharSequence var0) {
    String var1 = a019pd(UserIdLoginCredentials.encodeUsername(var0));
    if (var1 == null) {
      var1 = "";
    }

    return var1;
  }

  public static int randomIntBounded(final int min, final int max) {
    return min + randomIntBounded(globalRandom, max - min + 1);
  }

  public static void a150wp() {
    //noinspection InlineNestedElse
    if (ClientGameSession.playSession.isUnrated) {
      if (ClientGameSession.playSession.gameState.isPlayerOfferingRematch(ClientGameSession.playSession.localPlayerIndex)) {
        MENU_ITEM_LABELS[13] = StringConstants.MP_CANCEL_REMATCH_UNRATED;
      } else if (ClientGameSession.playSession.gameState.isAnyoneOfferingRematch()) {
        MENU_ITEM_LABELS[13] = StringConstants.MP_ACCEPT_REMATCH_UNRATED;
      } else if (ClientGameSession.playSession.haveAllOtherPlayersLeft()) {
        MENU_ITEM_LABELS[13] = StringConstants.MP_REMATCH_NEW_GAME_UNRATED;
      } else {
        MENU_ITEM_LABELS[13] = StringConstants.MP_OFFER_REMATCH_UNRATED;
      }
    } else {
      if (ClientGameSession.playSession.gameState.isPlayerOfferingRematch(ClientGameSession.playSession.localPlayerIndex)) {
        MENU_ITEM_LABELS[13] = StringConstants.MP_CANCEL_REMATCH;
      } else if (ClientGameSession.playSession.gameState.isAnyoneOfferingRematch()) {
        MENU_ITEM_LABELS[13] = StringConstants.MP_ACCEPT_REMATCH;
      } else if (ClientGameSession.playSession.haveAllOtherPlayersLeft()) {
        MENU_ITEM_LABELS[13] = StringConstants.MP_REMATCH_NEW_GAME;
      } else {
        MENU_ITEM_LABELS[13] = StringConstants.MP_OFFER_REMATCH;
      }
    }
  }

  private static void layoutLobbyRoomPanels(final int transitionCounter) {
    final int var1 = (Component.lastLayoutWidth - SCREEN_WIDTH) / 2;
    final int var2 = 400;
    final int var3 = var2 - transitionCounter * transitionCounter;
    lobbyRoomLeftPanel.setBounds(var1 - 199 * var3 / var2, 90, 199, Drawing.height - 120 - 94);
    Component.GAME_INFO_CONTAINER.setBounds(438 * var3 / var2 + var1 + 202, 0, 438, Drawing.height - 124);
  }

  private static void a630gr(final boolean var0) {
    if (var0) {
      Drawing.fillRect(0, 0, Drawing.width, Drawing.height, 0, 192);
    } else {
      Drawing.clear();
    }
    CommonUI.draw();
  }

  private static void a790cq(final int textColor, final String var1, final String message, final int var3) {
    chatMessageLabel.textColor = textColor;
    chatMessageLabel.label = message;
    chatMessageLabel._u = var1;
    chatMessageLabel.width += Component._cgC.width;
    Component._cgC.x += Component._cgC.width;
    Component._cgC.width = var3;
    chatMessageLabel.width -= Component._cgC.width;
    Component._cgC.x -= Component._cgC.width;
  }

  private static void closeQuickChatIfOpen() {
    if (isQuickChatOpen) {
      closeQuickChat();
    }
  }

  private static void a893ml(final boolean var0) {
    Component._tgc.b540(var0);
    final DobToEnableChatForm var1 = DobToEnableChatForm.instance;
    if (var1 != null) {
      var1.drawRoot(Component._tgc.x2, Component._tgc.y2);
    }

  }

  private static void a877rad(final boolean var0) {
    _faX.b540(var0);
    if (ClientLobbyRoom.currentTooltip != null) {
      a407dk(ClientLobbyRoom.currentTooltip);
    }
  }

  private static void a407dk(final String var0) {
    final int var1 = mouseX;
    final int var2 = mouseY;
    final int var3 = Component.LABEL.font.breakLines(var0, 500);
    final int var5 = Component.LABEL.font.measureParagraphWidth(var0, 500) + 6;
    final int var6 = 2 + var3 * Component.LABEL_HEIGHT;
    final int var7 = PopupMenu.positionPopupX(var1, 12, var5);
    final int var8 = PopupMenu.positionPopupY(var2, 20, var6);
    Drawing.strokeRectangle(var7, var8, var5, var6, 0);
    Drawing.fillRect(1 + var7, 1 + var8, var5 - 2, var6 - 2, 16777088);
    Component.LABEL.font.drawParagraph(var0, var7 + 3, Component._si + (1 + var8 - Component.LABEL.font.ascent), 500, 1000, 0, Font.HorizontalAlignment.LEFT, Font.VerticalAlignment.TOP, Component.LABEL_HEIGHT);
  }

  private static void tickLobbyListStatuses() {
    for (final Iterator<LobbyPlayer> it = LobbyPlayer.online.iterator(); it.hasNext(); ) {
      final LobbyPlayer player = it.next();
      if (player.statusTimer > 0) {
        --player.statusTimer;
        if (player.statusTimer == 0) {
          player.status = 0;
          if (!player.isInMap()) {
            it.remove();
          }
        }
      }
    }

    for (final Iterator<ClientLobbyRoom> it = ClientLobbyRoom.rooms.iterator(); it.hasNext(); ) {
      final ClientLobbyRoom room = it.next();
      if (room.statusTimer > 0) {
        --room.statusTimer;
        if (room.statusTimer == 0) {
          room.status = 0;
          if (!room.isInMap()) {
            it.remove();
          }
        }
      }
    }

    for (final Iterator<LobbyPlayer> it = LobbyPlayer.joinedPlayers.iterator(); it.hasNext(); ) {
      final LobbyPlayer player = it.next();
      if (player.statusTimer > 0) {
        --player.statusTimer;
        if (player.statusTimer == 0) {
          player.status = 0;
          if (!player.isInMap()) {
            it.remove();
          }
        }
      }
    }
  }

  private static int a776wd(final int var0, final int var1) {
    if (var1 < 2) {
      return var0 < 5 ? 0 : 1;
    } else {
      return 2;
    }
  }

  private static void a326ts(int var0, int var2) {
    var0 += 30;
    var2 += 30;
    Menu.drawShine(-var0 + SCREEN_WIDTH >> 1, -var2 + SCREEN_HEIGHT >> 1, var0, var2);
  }

  private static void tickIgnoreList(final boolean mouseStill, final int mouseWheelRotation) {
    final boolean var3 = Component.IGNORE_LIST.scrollPane.processScrollInput(mouseStill, Component.IGNORE_LIST.scrollPane == currentContextMenuParent, 2 + Component.LABEL_HEIGHT, mouseWheelRotation * (Component.LABEL_HEIGHT + 2) * 3);
    final List<PlayerListEntry> var4 = Component.IGNORE_LIST.entries.children;
    if (PlayerListEntry.serverStatus == PlayerListEntry.ServerStatus.LOADED || PlayerListEntry.serverStatus == PlayerListEntry.ServerStatus.LOADING) {
      Component.IGNORE_LIST.scrollPane.viewport.label = null;
      Component.IGNORE_LIST.enabled = true;
      PlayerListEntry var5 = null;

      for (final PlayerListEntry var6 : var4) {
        boolean var7 = false;
        if (var6.children == null) {
          var6.playerNameLabel = new Component<>(Component.UNSELECTED_LABEL_DARK_2);
          var6.addChild(var6.playerNameLabel);
          var6.displayNameChangedIcon = new Component<>(Component.UNSELECTED_LABEL_DARK_2);
          var6.addChild(var6.displayNameChangedIcon);
          var7 = true;
          var6.recursivelySet_H();
        }

        var6.width = Component.IGNORE_LIST.entries.width;
        var6.playerNameLabel.setBounds(0, 0, var6.width, Component.LABEL_HEIGHT);
        boolean var8 = false;
        if (var6.previousDisplayName != null && !var6.previousDisplayName.equals("")) {
          var6.displayNameChangedIcon.mouseOverTextColor = 16737894;
          var6.displayNameChangedIcon.sprite = Component.DISPLAY_NAME_CHANGED;
          var8 = true;
          var6.displayNameChangedIcon.setBounds(0, 0, 3 + Component.DISPLAY_NAME_CHANGED.offsetX, Component.LABEL_HEIGHT);
        }

        int var9 = 0;
        int var10 = var6.width;
        if (var8) {
          var9 = Component.DISPLAY_NAME_CHANGED.offsetX + 3;
          var10 -= var9;
        }

        var6.playerNameLabel.label = var10 <= 0 ? var6.playerDisplayName : var6.playerNameLabel.font.truncateWithEllipsisToFit(var6.playerDisplayName, var10);
        assert var6.playerNameLabel.label != null;
        final boolean var11 = !var6.playerNameLabel.label.equals(var6.playerDisplayName);
        var6.playerNameLabel.setBounds(var9, 0, var10, Component.LABEL_HEIGHT);
        if (!var3) {
          var6._gb = Component.LABEL_HEIGHT - var6.height;
        }

        if (var7) {
          Component.IGNORE_LIST.entries.placeAfter(var5, var6);
        }

        if (var6.displayNameChangedIcon != null && var6.displayNameChangedIcon.isMouseOverTarget) {
          ClientLobbyRoom.currentTooltip = var6.previousDisplayName;
        } else if (var6.playerNameLabel.isMouseOverTarget && var11) {
          ClientLobbyRoom.currentTooltip = var6.playerDisplayName;
        }

        var5 = var6;
        if (var6.clickButton != MouseState.Button.NONE) {
          showContextMenu(Component.IGNORE_LIST.scrollPane, var6, 0L, var6.playerName, var6.playerDisplayName, -1, null);
          final ContextMenu var12 = ContextMenu.openInstance;
          final String var13 = StringConstants.MU_LOBBY_NAME_RM;
          var12.view.addItem(var13, ContextMenu.ClickAction.REMOVE_IGNORE);
          final ContextMenu m = ContextMenu.openInstance;
          final int var15 = mousePressX;
          final int var14 = mousePressY;
          m.view.positionRelativeToTarget(var15, var14);
        }
      }

      if (Component.IGNORE_LIST.addButton.clickButton != MouseState.Button.NONE) {
        AddOrRemovePlayerPopup.openInstance = new AddOrRemovePlayerPopup(Component.IGNORE_LIST.addButton.x2, Component.IGNORE_LIST.addButton.y2, Component.IGNORE_LIST.addButton.width, Component.IGNORE_LIST.addButton.height, StringConstants.ENTER_IGNORE_ADD, Component.POPUP, Component.LABEL, Component.LABEL);
        AddOrRemovePlayerPopup.action = AddOrRemovePlayerPopup.Action.ADD_IGNORE;
      }

      if (Component.IGNORE_LIST.removeButton.clickButton != MouseState.Button.NONE) {
        AddOrRemovePlayerPopup.openInstance = new AddOrRemovePlayerPopup(Component.IGNORE_LIST.removeButton.x2, Component.IGNORE_LIST.removeButton.y2, Component.IGNORE_LIST.removeButton.width, Component.IGNORE_LIST.removeButton.height, StringConstants.ENTER_IGNORE_DEL, Component.POPUP, Component.LABEL, Component.LABEL);
        AddOrRemovePlayerPopup.action = AddOrRemovePlayerPopup.Action.REMOVE_IGNORE;
      }
    } else {
      Component.IGNORE_LIST.enabled = false;
      Component.IGNORE_LIST.scrollPane.viewport.label = StringConstants.LOADING;
      crunch(Component.IGNORE_LIST.entries);
    }

  }

  @SuppressWarnings("BooleanMethodIsAlwaysInverted")
  public static boolean isComposingChatMessageTo(final String playerName) {
    if (isChatboxSelected) {
      return currentChatChannel == Channel.PRIVATE
          && ContextMenu.normalizedRecipientPlayerName != null
          && ContextMenu.normalizedRecipientPlayerName.equals(Strings.normalize(playerName));
    } else {
      return false;
    }
  }

  private static void a487er() {
    Menu._hgt = Menu.FONT.measureLineWidth(MENU_ITEM_LABELS[21]);
    final int var0 = Menu.FONT.measureLineWidth(MENU_ITEM_LABELS[22]);
    if (var0 > Menu._hgt) {
      Menu._hgt = var0;
    }

  }

  private static void b150lc() {
    if (isQuickChatOpen) {
      Drawing.h115(Drawing.left, Drawing.top, -Drawing.left + Drawing.right, -Drawing.top + Drawing.bottom);
      Component._uaf.b540(false);
    }

  }

  public static void saveProfile() {
    if (!isAnonymous) {
      if (GameUI.currentSettings != -1 && GameUI.currentSettings != GameUI.lastSavedSettings) {
        final SetProfileRequest request = new SetProfileRequest(new byte[]{0, (byte) GameUI.currentSettings});
        profileSetRequests.add(request);
        C2SPacket.sendProfileSet(request);
        GameUI.lastSavedSettings = GameUI.currentSettings;
      }
    }
  }

  private static String a614hg(final ChatMessage var1) {
    String var2 = null;
    if (var1.senderDisplayName != null) {
      var2 = var1.senderDisplayName;
      if (var1._c == 1) {
        var2 = "<img=0>" + var2;
      }

      if (var1._c == 2) {
        var2 = "<img=1>" + var2;
      }
    }

    String var3 = "";
    if (var1.channel != Channel.PRIVATE) {
      if (var1.channel == Channel.LOBBY) {
        var3 = "[" + StringConstants.MU_CHAT_LOBBY + "] ";
      }

      if (var1.channel == Channel.ROOM) {
        var3 = "[" + Strings.format(StringConstants.XS_GAME, var1._o) + "] ";
      }

      if (var1.channel == Channel.CHANNEL_4) {
        var3 = "[#" + var1._o + "] ";
      }

      if (!var1._h) {
        var3 = var3 + var2 + ": ";
      }
    } else if (!var1._h) {
      if (var1._l == 0 && var1._e == 0) {
        var3 = Strings.format(StringConstants.PRIVATE_QUICK_CHAT_TO_X, var2);
      } else {
        var3 = Strings.format(StringConstants.PRIVATE_QUICK_CHAT_FROM_X, var2);
      }
    }

    return var3;
  }

  private static void a540ho(final boolean var0) {
    if (QuickChatHelpPanel.openInstance != null) {
      Drawing.h115(Drawing.left, Drawing.top, Drawing.right - Drawing.left, -Drawing.top + Drawing.bottom);
      QuickChatHelpPanel.openInstance.b540(var0);
    }
  }

  private static int a137sa() {
    return Menu._hoa - Menu._dbf;
  }

  private static int a313jf(final int var0, final int var1) {
    final int var3 = var0 - 1 & var1 >> 31;
    return (var1 + (var1 >>> 31)) % var0 + var3;
  }

  private static void a681ks(final ChatMessage var0) {
    showContextMenu(null, var0.component, var0.senderId, var0.senderName, var0.senderDisplayName, var0.channel != Channel.ROOM ? 0 : var0._g, var0.channel);
    ContextMenu var2;
    String var3;
    if (var0.channel == Channel.LOBBY && !a427ha() && unratedLobbyRoom == null && ratedLobbyRoom == null) {
      if (!canOnlyQuickChat) {
        var2 = ContextMenu.openInstance;
        var3 = StringConstants.MESSAGE_LOBBY;
        var2.view.addItem(var3, ContextMenu.ClickAction.SEND_CHANNEL_MESSAGE);
      }

      var2 = ContextMenu.openInstance;
      var3 = StringConstants.QUICKCHAT_LOBBY;
      var2.view.addItem(var3, ContextMenu.ClickAction.SEND_CHANNEL_MESSAGE_QC);
    }

    if (var0.channel == Channel.ROOM && !a427ha() && unratedLobbyRoom != null && var0._g == unratedLobbyRoom.roomId) {
      if (!canOnlyQuickChat) {
        var2 = ContextMenu.openInstance;
        var3 = StringConstants.MESSAGE_GAME;
        var2.view.addItem(var3, ContextMenu.ClickAction.SEND_CHANNEL_MESSAGE);
      }

      var2 = ContextMenu.openInstance;
      var3 = StringConstants.QUICKCHAT_GAME;
      var2.view.addItem(var3, ContextMenu.ClickAction.SEND_CHANNEL_MESSAGE_QC);
    }

    ContextMenu.openInstance.a487();
    ContextMenu.openInstance.addPlayerItems(false);
    ContextMenu.openInstance.a124(var0._f, var0.channel);
    ContextMenu.openInstance.b150();
    ContextMenu.openInstance.a408(var0);
    var2 = ContextMenu.openInstance;
    var2.view.positionRelativeToTarget(mousePressX, mousePressY);
  }

  @SuppressWarnings("BooleanMethodIsAlwaysInverted")
  private static boolean a427ha() {
    return isChatboxSelected && currentChatChannel == Channel.LOBBY;
  }

  private static void a893r(final boolean var0) {
    if (AddOrRemovePlayerPopup.openInstance != null) {
      AddOrRemovePlayerPopup.openInstance.b540(var0);
    }
  }

  static @Nullable ChatMessage getLastChatMessage() {
    return chatMessageCount == 0 ? null
        : chatMessages[chatMessageCount - 1];
  }

  @Override
  public void shutdown() {
    if (Sounds.musicChannel != null) {
      Sounds.musicChannel.shutdown();
    }

    if (Sounds.soundsChannel != null) {
      Sounds.soundsChannel.shutdown();
    }

    removeFocusLossDetectingCanvas();
    if (canvas != null) {
      detachFromCanvas(canvas);
    }

    if (JagexApplet.pageSource != null) {
      JagexApplet.pageSource.close();
    }

    if (CacheWorker.instance != null) {
      CacheWorker.instance.close();
    }

    if (ResourceLoader.bufferedCacheDat2 != null) {
      try {
        ResourceLoader.bufferedCacheDat2.close();
      } catch (final IOException var4) {
      }
    }

    if (ResourceLoader.bufferedCacheFiles != null) {
      for (int var1 = 0; var1 < ResourceLoader.bufferedCacheFiles.length; ++var1) {
        if (ResourceLoader.bufferedCacheFiles[var1] != null) {
          try {
            ResourceLoader.bufferedCacheFiles[var1].close();
          } catch (final IOException var3) {
          }
        }
      }
    }

    if (JagexApplet.serverConnection1 != null && (loginState == LoginState.LOGIN_REQUEST_SENT || loginState == LoginState.LOGIN_REQUEST_SUCCESSFUL || loginState == LoginState.LOGGED_IN)) {
      C2SPacket.goodbye();
      JagexApplet.flushC2sPacket(0);
    }

    JagexApplet.shutdownServerConnection();
  }

  @Override
  public void initialize() {
    super.initialize();

    final MusicManager musicTn = new MusicManager();
    musicTn.midiPlayer2.initialize();
    musicTn.midiPlayer1.initialize();
    musicTn.midiPlayer2.setVolume(Sounds.MAX_VOLUME);
    musicTn.midiPlayer1.setVolume(Sounds.MAX_VOLUME);

    Sounds.musicChannel = createAudioChannel(0, SampledAudioChannelS16.SAMPLE_RATE);
    Sounds.soundsChannel = createAudioChannel(1, 1102);

    Sounds.soundsTn = new SoundManager();
    Sounds.soundsChannel.setSource(Sounds.soundsTn);
    Sounds.musicTn = musicTn;
    Sounds.musicTn.setVolume(Sounds.musicVolume);
    Sounds.musicChannel.setSource(Sounds.musicTn);

    renderQuality = RenderQuality.high();
    Menu.gameOptions = GameOptions.STREAMLINED_GAME_OPTIONS;
    GameUI.chatPanelOpenAmount = 20;
  }

  @Override
  public void tick() {
    ticksSinceLastMouseEventAtStartOfTick = MouseState.ticksSinceLastMouseEvent;
    tickSomethingThatSeemsRelatedToAudio();
    if (fullScreenCanvas != null && fullScreenCanvas.focusWasLost) {
      removeFocusLossDetectingCanvas();
      if (Menu.isFullscreenDialogOpen) {
        Menu.showFullscreenDialog(Menu.FullscreenDialog.LOST_FOCUS, false);
      }
    }

    this.a540(fullScreenCanvas != null);

    ++currentTick;
    if (!isConnectedAndLoaded()) {
      this.j423();
      if (!isConnectedAndLoaded()) {
        return;
      }
    }

    final boolean var2 = this.tick_1();
    //noinspection InlineNestedElse
    if (var2) {
      if (_ebb < 128 && (_ebb += _jlU) > 128) {
        _ebb = 128;
      }
    } else {
      if (_ebb > 0 && (_ebb -= _jlU) < 0) {
        _ebb = 0;
      }
    }

    while (recieveS2cPacket()) {
      if (currentS2cPacketType == Type.ERROR) {
        switchMenus();
        _mdQ = false;
        Menu.nextMenu = Menu.Id.ERROR;
        networkErrorMessage = JagexApplet.s2cPacket.readNullTerminatedString();
      } else if (currentS2cPacketType == Type.ENTER_MP) {
        switchMenus();
        a969be();
        PlayerListEntry.initialize(Objects.requireNonNull(Component.FRIEND_LIST.entries), Component.IGNORE_LIST.entries);
        inLobbyBrowser = true;
        _mdQ = false;
        Menu.nextMenu = Menu.Id.LOBBY;
      } else if (currentS2cPacketType == Type.LEAVE_MP) {
        if (inLobbyBrowser) {
          switchMenus();
          a423ppi();
          clearChatMessages();
          Menu.switchTo(Menu.Id.MAIN, 0, false);
          inLobbyBrowser = false;
          _lgb = true;
          _tli = false;
          spectatingGame = false;
          playingGame = false;
          leavingLobby = true;
          b150nj();
        }

        _isa = false;
        _mdQ = false;
      } else if (currentS2cPacketType == Type.LOBBY && inLobbyBrowser) {
        handleLobbyPacket();
      } else if (currentS2cPacketType == Type.ACHIEVEMENTS_UNLOCKED) {
        if (areAchievementsInitialized) {
          final int achievementsBitmap = JagexApplet.s2cPacket.readInt();
          final int newAchievementsBitmap = achievementsBitmap & ~Menu.unlockedAchievementsBitmap;
          for (int i = 0; i < StringConstants.ACHIEVEMENT_NAMES.length; ++i) {
            if ((newAchievementsBitmap & (1 << i)) != 0) {
              this.achievementNotifications.add(new AchievementNotification(i));
            }
          }
          Menu.unlockedAchievementsBitmap |= newAchievementsBitmap;
        }
      } else if (currentS2cPacketType == Type.ENTER_GAME) {
        switchMenus();
        final int turnLengthIndex = JagexApplet.s2cPacket.readUByte();
        final GameOptions options = GameOptions.read(s2cPacket);
        final GameState.GameType gameType = GameState.GameType.decode(JagexApplet.s2cPacket.readByte());
        final boolean isUnrated = JagexApplet.s2cPacket.readUByte() != 0;
        final byte localPlayerIndex = JagexApplet.s2cPacket.readByte();
        final boolean hasLocalPlayer = localPlayerIndex >= 0;
        final String[] playerNames = new String[JagexApplet.s2cPacket.readUByte()];
        Arrays.setAll(playerNames, i -> JagexApplet.s2cPacket.readNullBracketedString());

        final ClientGameSession prevSession = hasLocalPlayer ? ClientGameSession.playSession : ClientGameSession.spectateSession;
        final GameUI ui = prevSession != null ? prevSession.ui : null;
        final ClientGameSession session = new ClientGameSession(true, false, turnLengthIndex, options, gameType, playerNames, localPlayerIndex, isUnrated, ui);
        boolean var14 = true;
        if (session.localPlayer != null) {
          var14 = JagexApplet.s2cPacket.readUByte() == 1;
        }

        try {
          session.gameState.readMap(s2cPacket, session.localPlayer);
        } catch (final MapGenerationFailure var16) {
          JagexApplet.clientError(var16, "Map Generation failed when receiving initial game state from server.");
          JagexApplet.shutdownServerConnection();
          continue;
        } catch (final TannhauserUnconnectedException var17) {
          JagexApplet.clientError(var17, "Gamestate received from server is invalid.");
          JagexApplet.shutdownServerConnection();
          continue;
        }
//        catch (final Exception var18) {
//          JagexApplet.clientError(var18, "Error receiving gamestate from server.");
//          JagexApplet.shutdownServerConnection();
//          continue;
//        }

        session.initialize();
        session.readyToEndTurn = var14;
        if (hasLocalPlayer) {
          playingGame = true;
          ClientGameSession.playSession = session;
          ClientGameSession.updateDrawOfferMenuItem();
          a150wp();
          a874b(Component.LABEL, Component.TAB_ACTIVE);
        } else {
          spectatingGame = true;
          ClientGameSession.spectateSession = session;
        }

        Menu.switchTo(Menu.Id.GAME, 0, false);
        GameUI.isChatOpen = false;
        _tli = true;
        _isa = false;
      } else if (currentS2cPacketType == Type.LEAVE_GAME) {
        if (playingGame || spectatingGame) {
          switchMenus();
          if (playingGame) {
            ClientGameSession.playSession.handlePlayerLeft(false);
          }

          _lgb = true;
          playingGame = false;
          Menu.nextMenu = Menu.Id.LOBBY;
          _tli = false;
          spectatingGame = false;
          GameUI.isChatOpen = true;
          b150nj();
        }

        _isa = false;
      } else {
        ClientGameSession var4 = null;
        if (playingGame) {
          var4 = ClientGameSession.playSession;
        } else if (spectatingGame) {
          var4 = ClientGameSession.spectateSession;
        }

        if (var4 == null || !var4.handlePacket(currentS2cPacketType, s2cPacket, nextS2cPacketLen)) {
          this.mgsPacketHandler();
        }
      }
    }

    JagexApplet.flushC2sPacket(0);
    if (achievementRequest != null && achievementRequest.completed) {
      Menu.unlockedAchievementsBitmap |= achievementRequest.unlockedAchievementsBitmap;
      Menu.areAchievementsOffline = achievementRequest.areAchievementsOffline;
      achievementRequest = null;
      areAchievementsInitialized = true;
    }

    if (Menu.currentMenu == Menu.Id.LOBBY && Menu.nextMenu != Menu.Id.GAME) {
      GameUI.isChatOpen = true;
    }

    final boolean shouldForceDisconnection =
        (debugModeEnabled || (ClientGameSession.playSession != null && ClientGameSession.playSession.desynced) || (ClientGameSession.spectateSession != null && ClientGameSession.spectateSession.desynced))
            && JagexApplet.keysDown[KeyState.Code.CONTROL] && JagexApplet.keysDown[KeyState.Code.FORWARD_SLASH];
    if (shouldForceDisconnection) {
      JagexApplet.shutdownServerConnection();
    }

    if (JagexApplet.f154jc()) {
      @MagicConstant(valuesFromClass = CommonUI.LoginResult.class)
      final int loginResult = this.tickReconnecting();
      if (loginResult == CommonUI.LoginResult.SUCCESS || loginResult == CommonUI.LoginResult.R1) {
        a366vs();
        a430se();
        a093oq();
        ReflectionRequest.pending = new ArrayDeque<>();
        if (loginResult == CommonUI.LoginResult.R1) {
          _mdQ = true;
          _isa = true;
        } else {
          if (inLobbyBrowser) {
            _lgb = true;
            _tli = false;
            leavingLobby = true;
            if (playingGame) {
              networkErrorMessage = StringConstants.TEXT_REMOVED_FROM_GAME;
              Menu.switchTo(Menu.Id.ERROR, 0, false);
            } else {
              Menu.switchTo(Menu.Id.MAIN, 0, false);
            }

            switchMenus();
            Menu.menus[Menu.currentMenu].a126(false);
            spectatingGame = false;
            playingGame = false;
            inLobbyBrowser = false;
            b150nj();
          }

          _mdQ = false;
          _isa = false;
        }
      }

      if (loginResult == CommonUI.LoginResult.R2 || shouldForceDisconnection || inLobbyBrowser || _mdQ || _isa) {
        CommonUI.handleServerDisconnect();
      }
    }
  }

  private boolean tick_1() {
    if (!isFullyLoaded) {
      CommonUI.tick();
      if (this.loadNext()) {
        isFullyLoaded = true;
      }
      return false;
    }
    if (!isProfileInitialized()) {
      tickLoadProfile();
      return false;
    }
    if (!menuInitialized) {
      if (!isAnonymous) {
        Menu.updateAvailableMenuItems();
      }
      menuInitialized = true;
      return false;
    }

    if (Menu.isFullscreenDialogOpen) {
      if (_jbg == EnumJbg.C0) {
        final Menu.DialogOption var3 = d474cq();
        if (var3 == Menu.DialogOption.ACCEPT) {
          removeFocusLossDetectingCanvas();
        } else if (var3 == Menu.DialogOption.TRY_AGAIN) {
          accountCreationDisabled = false;
          Menu.a540fm(true);
        } else if (var3 == Menu.DialogOption.MEMBERS) {
          accountCreationDisabled = true;
          a776qk(Menu.Id.MAIN);
        }
        this.b877(true);
      } else {
        this.b877(false);
      }
    } else if (f427kh()) {
      final int var3 = this.tickCommonUI(true);
      if (var3 == 2) {
        if (fullScreenCanvas != null) {
          removeFocusLossDetectingCanvas();
        }
        JagexApplet.navigateToQuit(this);
      }
      this.b877(true);
    } else if (Menu.isLobbyDialogOpen) {
      if (_jbg == EnumJbg.C0) {
        final int var3 = Menu.a137ch();
        if (var3 == 1) {
          _nsob = true;
          a776qk(Menu.Id.MAIN);
        }
        this.b877(true);
      } else {
        this.b877(false);
      }
    } else {
      this.b877(false);
      return false;
    }

    return true;
  }

  private static void l150spc() {
    templateDictionary = new TemplateDictionary();
    templateDictionary.put("key", "<col=2F5FBF>");
    templateDictionary.put("highlight", "<col=FF8080>");
    templateDictionary.put("glossary", "<col=2F5FBF>");

    for (int var2 = 0; var2 < 4; ++var2) {
      templateDictionary.put("resource" + var2, Strings.format("<col=<%0>><%1></col>", Integer.toString(GameView.RESOURCE_COLORS[var2], 16), StringConstants.RESOURCE_NAMES[var2].toLowerCase()));
      templateDictionary.put("Resource" + var2, Strings.format("<col=<%0>><%1></col>", Integer.toString(GameView.RESOURCE_COLORS[var2], 16), StringConstants.RESOURCE_NAMES[var2]));
    }

    templateDictionary.put("territories", StringConstants.TAB_NAME_PRODUCTION);
    templateDictionary.put("uioptions", StringConstants.TAB_NAME_UI_OPTIONS);
    templateDictionary.put("fleetinfo", StringConstants.TAB_NAME_FLEET_INFO);
    templateDictionary.put("projects", StringConstants.TAB_NAME_PROJECTS);
    templateDictionary.put("diplomacy", StringConstants.TAB_NAME_DIPLOMACY);
    templateDictionary.put("messages", StringConstants.TAB_NAME_MESSAGES);
    templateDictionary.put("colmetal", Integer.toString(GameView.RESOURCE_COLORS[0], 16));
    templateDictionary.put("colbiomass", Integer.toString(GameView.RESOURCE_COLORS[1], 16));
    templateDictionary.put("colenergy", Integer.toString(GameView.RESOURCE_COLORS[2], 16));
    templateDictionary.put("colexotics", Integer.toString(GameView.RESOURCE_COLORS[3], 16));
    final String var3 = "<col=<%0>><%1></col>";
    templateDictionary.put("project_metal", Strings.format(var3, Integer.toString(GameView.RESOURCE_COLORS[0], 16), StringConstants.PROJECT_NAMES[0].toLowerCase()));
    templateDictionary.put("project_biomass", Strings.format(var3, Integer.toString(GameView.RESOURCE_COLORS[1], 16), StringConstants.PROJECT_NAMES[1].toLowerCase()));
    templateDictionary.put("project_energy", Strings.format(var3, Integer.toString(GameView.RESOURCE_COLORS[2], 16), StringConstants.PROJECT_NAMES[2].toLowerCase()));
    templateDictionary.put("Project_Metal", Strings.format(var3, Integer.toString(GameView.RESOURCE_COLORS[0], 16), StringConstants.PROJECT_NAMES[0]));
    templateDictionary.put("Project_Biomass", Strings.format(var3, Integer.toString(GameView.RESOURCE_COLORS[1], 16), StringConstants.PROJECT_NAMES[1]));
    templateDictionary.put("Project_Energy", Strings.format(var3, Integer.toString(GameView.RESOURCE_COLORS[2], 16), StringConstants.PROJECT_NAMES[2]));
    templateDictionary.put("Project_Exotics", Strings.format(var3, Integer.toString(GameView.RESOURCE_COLORS[3], 16), StringConstants.PROJECT_NAMES[3]));
    templateDictionary.put("PROJECT_METAL", Strings.format(var3, Integer.toString(GameView.RESOURCE_COLORS[0], 16), StringConstants.PROJECT_NAMES[0].toUpperCase()));
    templateDictionary.put("PROJECT_BIOMASS", Strings.format(var3, Integer.toString(GameView.RESOURCE_COLORS[1], 16), StringConstants.PROJECT_NAMES[1].toUpperCase()));
    templateDictionary.put("PROJECT_ENERGY", Strings.format(var3, Integer.toString(GameView.RESOURCE_COLORS[2], 16), StringConstants.PROJECT_NAMES[2].toUpperCase()));
    templateDictionary.put("PROJECT_EXOTICS", Strings.format(var3, Integer.toString(GameView.RESOURCE_COLORS[3], 16), StringConstants.PROJECT_NAMES[3].toUpperCase()));
  }

  private boolean loadNext() {
    initializeUiStrings();
    if (ResourceLoader.SHATTERED_PLANS_SFX_1 != null) {
      if (!ResourceLoader.COMMON_SPRITES.isIndexLoaded() || !ResourceLoader.COMMON_SPRITES.loadGroupData("basic")) {
        CommonUI.setLoadProgress(10.0F, loadingMessage(ResourceLoader.COMMON_SPRITES, "basic", StringConstants.WAITING_FOR_GRAPHICS, StringConstants.LOADING_GRAPHICS));
        return false;
      } else if (!ResourceLoader.COMMON_SPRITES.isIndexLoaded() || !ResourceLoader.COMMON_SPRITES.loadGroupData("lobby")) {
        CommonUI.setLoadProgress(13.0F, loadingMessage(ResourceLoader.COMMON_SPRITES, "lobby", StringConstants.WAITING_FOR_GRAPHICS, StringConstants.LOADING_GRAPHICS));
        return false;
      } else if (!ResourceLoader.COMMON_FONTS.isIndexLoaded() || !ResourceLoader.COMMON_FONTS.loadGroupData("lobby")) {
        CommonUI.setLoadProgress(17.0F, loadingMessage(ResourceLoader.COMMON_FONTS, "lobby", StringConstants.WAITING_FOR_FONTS, StringConstants.LOADING_FONTS));
        return false;
      } else if (!ResourceLoader.SHATTERED_PLANS_SFX_1.isIndexLoaded() || !ResourceLoader.SHATTERED_PLANS_SFX_1.loadAllGroups()) {
        CommonUI.setLoadProgress(20.0F, loadingMessage(ResourceLoader.SHATTERED_PLANS_SFX_1, "", StringConstants.WAITING_FOR_SOUND_EFFECTS, StringConstants.LOADING_SOUND_EFFECTS));
        return false;
      } else if (!ResourceLoader.SHATTERED_PLANS_SFX_2.isIndexLoaded() || !ResourceLoader.SHATTERED_PLANS_SFX_2.loadAllGroups()) {
        CommonUI.setLoadProgress(40.0F, loadingMessage(ResourceLoader.SHATTERED_PLANS_SFX_2, "", StringConstants.WAITING_FOR_SOUND_EFFECTS, StringConstants.LOADING_SOUND_EFFECTS));
        return false;
      } else if (!ResourceLoader.SHATTERED_PLANS_MUSIC_1.isIndexLoaded() || !ResourceLoader.SHATTERED_PLANS_MUSIC_1.loadAllGroups()) {
        CommonUI.setLoadProgress(45.0F, loadingMessage0(ResourceLoader.SHATTERED_PLANS_MUSIC_1, StringConstants.WAITING_FOR_MUSIC, StringConstants.LOADING_MUSIC));
        return false;
      } else if (!ResourceLoader.SHATTERED_PLANS_MUSIC_2.isIndexLoaded() || !ResourceLoader.SHATTERED_PLANS_MUSIC_2.loadAllGroups()) {
        CommonUI.setLoadProgress(60.0F, loadingMessage(ResourceLoader.SHATTERED_PLANS_MUSIC_2, "", StringConstants.WAITING_FOR_MUSIC, StringConstants.LOADING_MUSIC));
        return false;
      } else if (!ResourceLoader.SHATTERED_PLANS_SPRITES.isIndexLoaded() || !ResourceLoader.SHATTERED_PLANS_SPRITES.loadGroupData("")) {
        CommonUI.setLoadProgress(75.0F, loadingMessage(ResourceLoader.SHATTERED_PLANS_SPRITES, "", StringConstants.WAITING_FOR_GRAPHICS, StringConstants.LOADING_GRAPHICS));
        return false;
      } else if (!ResourceLoader.SHATTERED_PLANS_JPEGS.isIndexLoaded() || !ResourceLoader.SHATTERED_PLANS_JPEGS.loadAllGroups()) {
        CommonUI.setLoadProgress(75.0F, loadingMessage(ResourceLoader.SHATTERED_PLANS_JPEGS, "", StringConstants.WAITING_FOR_GRAPHICS, StringConstants.LOADING_GRAPHICS));
        return false;
      } else if (!ResourceLoader.SHATTERED_PLANS_FONTS.isIndexLoaded() || !ResourceLoader.SHATTERED_PLANS_FONTS.loadAllGroups()) {
        CommonUI.setLoadProgress(75.0F, loadingMessage(ResourceLoader.SHATTERED_PLANS_FONTS, "", StringConstants.WAITING_FOR_GRAPHICS, StringConstants.LOADING_GRAPHICS));
        return false;
      } else if (!ResourceLoader.SHATTERED_PLANS_STRINGS_2.isIndexLoaded() || !ResourceLoader.SHATTERED_PLANS_STRINGS_2.loadAllGroups()) {
        CommonUI.setLoadProgress(75.0F, loadingMessage(ResourceLoader.SHATTERED_PLANS_STRINGS_2, "", StringConstants.WAITING_FOR_EXTRA_DATA, StringConstants.LOADING_EXTRA_DATA));
        return false;
      } else if (!ResourceLoader.HUFFMAN_CODES.isIndexLoaded() || !ResourceLoader.HUFFMAN_CODES.loadAllGroups()) {
        CommonUI.setLoadProgress(75.0F, loadingMessage(ResourceLoader.HUFFMAN_CODES, "", StringConstants.WAITING_FOR_EXTRA_DATA, StringConstants.LOADING_EXTRA_DATA));
        return false;
      } else if (!ResourceLoader.QUICK_CHAT_DATA.isIndexLoaded() || !ResourceLoader.QUICK_CHAT_DATA.loadAllGroups()) {
        CommonUI.setLoadProgress(75.0F, loadingMessage(ResourceLoader.QUICK_CHAT_DATA, StringConstants.WAITING_FOR_EXTRA_DATA, StringConstants.LOADING_EXTRA_DATA));
        return false;
      } else {
        CommonUI.switchToLoadingScreen();
        CommonUI.setLoadProgress(65.0F, StringConstants.UNPACKING_SOUND_EFFECTS);
        this.render();
        Sounds.loadSoundEffects(ResourceLoader.SHATTERED_PLANS_SFX_1, ResourceLoader.SHATTERED_PLANS_SFX_2);
        ResourceLoader.SHATTERED_PLANS_SFX_1 = null;
        ResourceLoader.SHATTERED_PLANS_SFX_2 = null;
        resetFrameClock();
        return false;
      }
    } else if (ResourceLoader.SHATTERED_PLANS_MUSIC_2 != null) {
      CommonUI.setLoadProgress(75.0F, StringConstants.UNPACKING_MUSIC);
      this.render();
      Sounds.loadMusic(ResourceLoader.SHATTERED_PLANS_MUSIC_2, ResourceLoader.SHATTERED_PLANS_MUSIC_1);
      ResourceLoader.SHATTERED_PLANS_MUSIC_1 = null;
      ResourceLoader.SHATTERED_PLANS_MUSIC_2 = null;
      resetFrameClock();
      return false;
    } else if (ResourceLoader.SHATTERED_PLANS_FONTS != null) {
      CommonUI.setLoadProgress(90.0F, StringConstants.UNPACKING_GRAPHICS);
      this.render();
      a780ck(ResourceLoader.COMMON_SPRITES, ResourceLoader.COMMON_FONTS, ResourceLoader.SHATTERED_PLANS_SPRITES, ResourceLoader.SHATTERED_PLANS_FONTS);
      ResourceLoader.COMMON_FONTS = null;
      ResourceLoader.SHATTERED_PLANS_FONTS = null;
      ResourceLoader.QUICK_CHAT_DATA = null;
      resetFrameClock();
      return false;
    } else if (ResourceLoader.SHATTERED_PLANS_JPEGS != null) {
      loadJpegImages(ResourceLoader.SHATTERED_PLANS_JPEGS);
      ResourceLoader.SHATTERED_PLANS_JPEGS = null;
      resetFrameClock();
      return false;
    } else if (ResourceLoader.SHATTERED_PLANS_STRINGS_2 != null) {
      TutorialMessages.load(ResourceLoader.SHATTERED_PLANS_STRINGS_2);
      StarSystem.loadNames(ResourceLoader.SHATTERED_PLANS_STRINGS_2);
      ResourceLoader.SHATTERED_PLANS_STRINGS_2 = null;
      resetFrameClock();
      return false;
    } else if (ResourceLoader.HUFFMAN_CODES == null) {
      l150spc();
      a487er();
      IntroAnimation.reset();

      for (int group = 0; group < Menu.NUM_GROUPS; ++group) {
        if (group == 11) continue;
        //noinspection MagicConstant
        Menu.menus[group] = new Menu(group);
      }

      Menu.nextMenu = Menu.Id.MAIN;
      debugModeEnabled = adminLevel >= 2;
      GameUI.lastSavedSettings = -1;
      Menu.currentMenu = Menu.Id.MAIN;
      GameUI.currentSettings = -1;
      resetFrameClock();
      a827jo(currentTrack, 0x100000, true);
      return true;
    } else {
      HuffmanCoder.initialize(ResourceLoader.HUFFMAN_CODES);
      ResourceLoader.HUFFMAN_CODES = null;
      resetFrameClock();
      return false;
    }
  }

  private void b877(final boolean var2) {
    if (_jbg == EnumJbg.C0) {
      boolean var3 = false;
      boolean var4 = false;
      if (!var2) {
        if (Menu.currentMenu == Menu.nextMenu) {
          if (Menu.currentMenu == Menu.Id.GAME) {
            if (inLobbyBrowser) {
              var3 = true;
              var4 = true;
            } else {
              ClientGameSession.playSession.tick(true);

              while (nextTypedKey()) {
                ClientGameSession.playSession.handleKeyTyped();
              }
            }
          } else if (Menu.currentMenu == Menu.Id.LOBBY) {
            var3 = true;
          } else if (Menu.currentMenu == Menu.Id.INTRODUCTION) {
            IntroAnimation.tick();
          } else if (Menu.currentMenu >= 0) {
            Menu.menus[Menu.currentMenu].i150();
          }
        } else {
          if (Menu._ehQ != 0 || !(Menu.currentMenu >= 0) || Menu.menus[Menu.currentMenu].a427()) {
            ++Menu._ehQ;
          }

          if (Menu._ehQ == 32) {
            switchMenus();
          }
        }
      }

      if (inLobbyBrowser && !_mdQ && !_isa) {
        if (playingGame && isChatboxSelected) {
          var3 = false;
        }

        a430cf(var3);
        if (spectatingGame) {
          ClientGameSession.spectateSession.tick(var4);
        }
        if (playingGame) {
          a778cf(var4);
          ClientGameSession.playSession.tick(var4);
        }

        label172:
        while (true) {
          do {
            if (!nextTypedKey()) {
              break label172;
            }

            if (!playingGame) {
              a111qf();
              break;
            }
          } while (processChatKeyInput());
          if (spectatingGame) {
            ClientGameSession.spectateSession.handleKeyTyped();
          }
          if (playingGame) {
            ClientGameSession.playSession.handleKeyTyped();
          }
        }
      }

      if (!inLobbyBrowser) {
        b150af();
      }

      if (GameUI.isChatOpen) {
        if (GameUI.chatPanelOpenAmount < 20) {
          ++GameUI.chatPanelOpenAmount;
        }
      } else if (GameUI.chatPanelOpenAmount > 0) {
        --GameUI.chatPanelOpenAmount;
      }

      a366fa(GameUI.getChatPanelY());
      if (IntroAnimation._ucv > 0) {
        --IntroAnimation._ucv;
      }

      if (!this.achievementNotifications.isEmpty() && ++this.achievementNotificationTimer == 335) {
        this.achievementNotificationTimer = 0;
        this.achievementNotifications.remove();
      }
    } else if (_jbg == EnumJbg.C1) {
      ++_naF;
      if (_naF == 16) {
        a487oq(!accountCreationDisabled);
        _jbg = EnumJbg.C2;
      }
    } else if (_jbg == EnumJbg.C2) {
      if (!isAnonymous) {
        debugModeEnabled = adminLevel >= 2;
        Menu.nextMenu = initialMenu;
        a714jc(SpriteResource.loadSprite(ResourceLoader.COMMON_SPRITES, "basic", "unachieved"));
        Menu.closeLobbyDialog();
        if (accountCreationDisabled) {
          accountCreationDisabled = false;
          Menu.a540fm(false);
        }

        if (_nsob) {
          Menu.switchTo(Menu.Id.LOADING_LOBBY, Menu.nextMenu, false);
        }

        if (Menu.nextMenu >= 0) {
          switchMenus();
        }

        Menu.updateAvailableMenuItems();
      }

      _nsob = false;
      accountCreationDisabled = false;
      _jbg = EnumJbg.C3;
    } else {
      --_naF;
      if (_naF == 0) {
        _jbg = EnumJbg.C0;
      }
    }

    if (Menu.nextMenu == Menu.Id.SKIRMISH) {
      final String[] playerNames = new String[]{StringConstants.EMPIRE_NAMES[0], StringConstants.EMPIRE_NAMES[1], StringConstants.EMPIRE_NAMES[2], StringConstants.EMPIRE_NAMES[3]};
      ClientGameSession.playSession = new ClientGameSession(false, false, 4, Menu.gameOptions, Menu.skirmishGameType, playerNames, 0, false, null);
      Menu.switchTo(Menu.Id.GAME, 0, false);
      _tli = true;
    }

    if (Menu.nextMenu == Menu.Id.TUTORIAL) {
      final String[] playerNames = new String[]{StringConstants.EMPIRE_NAMES[0]};
      ClientGameSession.playSession = new ClientGameSession(false, true, 0, GameOptions.STREAMLINED_GAME_OPTIONS, GameState.GameType.CONQUEST, playerNames, 0, false, null);
      Menu.switchTo(Menu.Id.GAME, 0, false);
      _tli = true;
    }

    if (Menu.nextMenu == Menu.Id.LEAVE_GAME_TO_LOBBY) {
      C2SPacket.requestToLeaveRoom(unratedLobbyRoom.roomId);
      Menu.nextMenu = Menu.currentMenu;
      _isa = true;
    }
  }

  @Override
  protected void render() {
    final Canvas canvas = fullScreenCanvas == null ? JagexBaseApplet.canvas : fullScreenCanvas;
    if (!isConnectedAndLoaded()) {
      a985no(canvas);
    } else if (!isFullyLoaded) {
      CommonUI.drawLoading();
      JagexBaseApplet.paint(canvas);
    } else if (isProfileInitialized()) {
      if (Menu.nextMenu == Menu.currentMenu) {
        if (Menu.currentMenu == Menu.Id.LOBBY) {
          Drawing.clear();
        } else {
          a877u(Menu.currentMenu == Menu.Id.GAME);
          if (Menu.currentMenu != Menu.nextMenu || Menu.currentMenu != Menu.Id.LOBBY) {
            n423();
          }

          if (Menu.currentMenu >= 0) {
            Menu.menus[Menu.currentMenu].g150();
          }
        }
      } else {
        a877u(Menu.currentMenu == Menu.Id.GAME);
        if (Menu.currentMenu != Menu.nextMenu || Menu.currentMenu != Menu.Id.LOBBY) {
          n423();
        }

        if (Menu.currentMenu >= 0 && !(Menu.nextMenu >= 0)) {
          Menu.menus[Menu.currentMenu].b093(128 * Menu._ehQ / 32);
        } else if (!(Menu.currentMenu >= 0) && Menu.nextMenu >= 0) {
          Menu.menus[Menu.nextMenu].b093(-(Menu._ehQ * 128 / 32) + 128);
        } else if (Menu.currentMenu >= 0) {
          Menu.menus[Menu.currentMenu].c326(Menu.nextMenu, 128 * Menu._ehQ / 32);
        }
      }

      if (Menu.nextMenu != Menu.currentMenu) {
        if (Menu.nextMenu == Menu.Id.LOBBY) {
          Drawing.fillRect(0, 0, SCREEN_WIDTH, SCREEN_HEIGHT, 0, 256 * Menu._ehQ / 32);
        } else if (Menu.currentMenu == Menu.Id.LOBBY) {
          Drawing.fillRect(0, 0, SCREEN_WIDTH, SCREEN_HEIGHT, 0, -(256 * Menu._ehQ / 32) + 256);
        }
      }

      if (!_cjx || !_tli) {
        a813qr(a154vc());
      }

      if (_ebb > 0) {
        Drawing.fillRect(0, 0, SCREEN_WIDTH, SCREEN_HEIGHT, 0, _ebb);
      }

      if (Menu.isFullscreenDialogOpen) {
        a326ts(a137sa(), Menu._dmra);
        a423kd();
      } else if (f427kh()) {
        a630gr(true);
      } else if (Menu.isLobbyDialogOpen) {

        a326ts(Menu._ahR - Menu._ldj, Menu._rnb);
        Menu.a150rg();
      } else {
        Optional.ofNullable(this.achievementNotifications.poll()).ifPresent(var3 -> {
          final String name = StringConstants.ACHIEVEMENT_NAMES[var3.achievementIndex];
          final int maxWidth = Menu.FONT.measureLineWidth(name) + 50;

          final int t;
          if (this.achievementNotificationTimer > 303) {
            t = 335 - this.achievementNotificationTimer;
          } else {
            t = Math.min(this.achievementNotificationTimer, 32);
          }
          final int width = MathUtil.ease(t, 32, 0, maxWidth);
          final int height = 36;
          final int x = 10 + (maxWidth - width >> 1);
          final int y = 10;
          Menu.drawShine(x, y, width, height);
          Drawing.withBounds(x, y, x + width, y + height, () -> {
            final int var7 = 10 + (maxWidth / 2);
            Menu.achievementIcon(var3.achievementIndex).draw(var7 + maxWidth / 2 - 36, 14);
            Menu.FONT.draw(name, var7 - (maxWidth / 2) + 10, (Menu.FONT.ascent + height >> 1) - 2 + y, 0x2ad0d6);
          });
        });
      }

      if (_jbg != EnumJbg.C0) {
        final int var14 = _naF * 256 / 16;
        if (var14 > 0) {
          Drawing.fillRect(0, 0, Drawing.width, Drawing.height, 0, var14);
        }
      }

      if (drawDebugStats && debugModeEnabled) {
        Menu.FONT.draw(Integer.toString(JagexBaseApplet.lastFrameScore), 4, 4 + Menu.FONT.ascent, Drawing.WHITE);
        final long var15 = Runtime.getRuntime().totalMemory();
        final long var16 = Runtime.getRuntime().freeMemory();
        final long var17 = var15 - var16;
        Menu.FONT.draw((int) (var17 >> 10) + "kb", 4, 2 * Menu.FONT.ascent + 8, Drawing.WHITE);
      }

      JagexBaseApplet.paint(canvas);
    } else {
      CommonUI.setLoadProgress(100.0F, StringConstants.LOADING_EXTRA_DATA);
      CommonUI.drawLoading();
      JagexBaseApplet.paint(canvas);
    }
  }

  private enum EnumJbg {
    C0, C1, C2, C3
  }
}
