package funorb.client.lobby;

import funorb.awt.MouseState;
import funorb.cache.ResourceLoader;
import funorb.commonui.form.DobToEnableChatForm;
import funorb.graphics.Drawing;
import funorb.graphics.Font;
import funorb.graphics.NineSliceSprite;
import funorb.graphics.PalettedSymbol;
import funorb.graphics.Sprite;
import funorb.graphics.SpriteFont;
import funorb.graphics.SpriteResource;
import funorb.shatteredplans.StringConstants;
import funorb.shatteredplans.client.JagexApplet;
import funorb.shatteredplans.client.ShatteredPlansClient;
import org.intellij.lang.annotations.MagicConstant;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

public class Component<T extends Component<?>> {
  public static final int[] CHANNEL_TEXT_COLORS_2 = new int[5];
  public static final int LABEL_HEIGHT = 15;
  public static final String[] CHAT_FILTER_LABELS = new String[3];
  public static final int[] CHANNEL_TEXT_COLORS_1 = new int[5];
  private static final int UNSELECTED_TEXT_COLOR = 0xffcc66;
  public static Component<?> LABEL_DARK_1;
  public static Component<?> _cna;
  public static Component<?> CLOSE_BUTTON;
  public static Component<?> LABEL_DARK_2;
  public static int _si;
  public static Component<?> NAME_LABEL;
  public static vm_ _ofb;
  public static Component<?>[] chatFilterIcons;
  public static Component<Component<?>>[] chatFilterButtons;
  public static Sprite DISPLAY_NAME_CHANGED;
  public static Component<Component<?>> lobbyBrowserLeftPanel;
  public static Component<?> INVITE_PLAYER_LIST_HEADING_RATING;
  public static Component<?> RETURN_TO_MAIN_MENU_BUTTON;
  private static QuickChatCategories _guiA;
  private static Component<?> ALL_GAMES_HEADING;
  private static Component<?> COLUMN_HEADING_MEDIUM_CENTER;
  private static Component<?> COLUMN_HEADING_MEDIUM_RIGHT;
  private static Component<?> GAME_LIST_HEADING_OWNER;
  private static Component<?> GAME_LIST_HEADING_AVG_RATING;
  public static Component<?> YOU_HAVE_BEEN_KICKED_OK_BUTTON;
  public static Component<?> _mpa;
  public static ScrollPane<LobbyPlayer> JOINED_PLAYERS_TABLE;
  public static int lastLayoutHeight;
  public static int lastLayoutWidth;
  private static Component<Component<?>> lobbyBrowserPlayerList;
  public static Component<?> UNSELECTED_LABEL_DARK_2;
  private static int chatFilterButtonCount;
  public static Component<Component<?>> YOU_HAVE_BEEN_KICKED_DIALOG;
  public static Component<Component<?>> JOINED_PLAYERS_PANEL;
  public static PopupMenu _hdt;
  public static Component<?> PLAY_RATED_GAME_BUTTON;
  public static Component<?> CREATE_UNRATED_GAME_BUTTON;
  public static ActionButton[][] GAME_OPTIONS_BUTTONS;
  public static Component<?> GAME_OPTIONS_HEADING;
  public static Component<?> JOINED_PLAYER_COUNT_LABEL;
  public static Component<Component<?>> INVITE_PLAYER_LIST_PANEL;
  public static Component<?> INVITE_PLAYERS_BUTTON;
  public static Component<?> GREEN_BUTTON;
  public static Component<?> FIND_OPPONENTS_BUTTON;
  public static PlayerList FRIEND_LIST;
  public static Component<?> _tmt;
  public static Component<Component<?>> _tgc;
  public static int _rcl;
  public static int CROWNS_COUNT;
  public static Component<Component<?>> _taio;
  public static Component<?> RETURN_TO_LOBBY_BUTTON;
  public static ScrollPane<Component<?>> _jiI;
  private static Component<?> _rsEb;
  public static Component<Component<?>> WAITING_TO_START_LABEL;
  public static Component<?> NAME_LABEL_2;
  private static Component<?> GAME_LIST_HEADING_STATUS;
  public static Component<Component<?>> FRIEND_LIST_PANEL;
  public static Component<?> _cgC;
  private static Component<?> BACK_BUTTON;
  public static SpriteFont CHAT_FONT;
  public static ScrollBar SCROLL_BAR;
  public static Component<Component<?>> LOBBY_RIGHT_PANEL;
  private static PlayerList PLAYER_LIST;
  public static Checkbox CHECKBOX;
  private static Component<?> MEDIUM_BUTTON;
  public static Component<?> TAB_ACTIVE;
  public static Component<?> INVITE_PLAYERS_LABEL;
  public static Component<?> _hlI;
  public static Component<?> CLOSE_BUTTON_2;
  public static Component<?> BIG_BUTTON;
  private static Component<?> GAME_LIST_HEADING_OPTIONS;
  private static Component<?> GAMEOPT_BUTTON;
  public static Component<?> UNSELECTED_LABEL;
  private static Component<?> GAME_LIST_HEADING_ELAPSED_TIME;
  public static Component<?> GAME_OWNER_HEADING;
  public static Component<?> UNSELECTED_LABEL_DARK_1;
  public static Component<vm_> _uaf;
  public static Component<?> LABEL;
  public static ScrollPane<ClientLobbyRoom> GAME_LIST_SCROLL_PANE;
  public static ScrollPane<LobbyPlayer> INVITE_PLAYER_LIST_SCROLL_PANE;
  public static TabbedPlayerListWrapper lobbyBrowserTabbedPlayerList;
  private static Component<?> GAME_LIST_HEADING_PLAYERS;
  private static Component<?> CHAT_FILTER_BUTTON;
  public static Component<?>[] GAME_OPTIONS_LABELS;
  public static Component<?> TAB_INACTIVE;
  private static boolean mouseOverConsumed;
  private static boolean clickConsumed;
  private static SpriteFont GENERAL_FONT;
  private static Component<?> CHAT_BUTTON;
  public static Component<?> RED_BUTTON;
  private static Component<?> HEADING;
  public static Component<?> POPUP;
  private static Component<?> COLUMN_HEADING_MEDIUM_LEFT;
  public static Component<?> RATED_GAME_TIPS_LABEL;
  public static Component<Component<?>> GAME_INFO_CONTAINER;
  private static Component<Component<?>> GAME_LIST_PANEL;
  public static Component<?> YOU_HAVE_BEEN_KICKED_LABEL;
  public static Component<?> INVITE_PLAYER_LIST_HEADING_NAME;
  public static Sprite[] CHAT_FILTER_SPRITES;
  public static Component<?> _a;
  private static Component<Component<?>> _adc;
  public static Component<?>[] chatFilterLabels;
  private static Component<?>[] chatFilterChannelLabels;
  public static Component<?> RATING_LABEL;
  public static Component<?> YOUR_RATING_LABEL;
  public static Component<Component<?>> GAME_OPTIONS_CONTAINER;
  public static Component<?> SERVER_INFO_LABEL;
  public static Component<?> LOCATION_LABEL;
  private static Component<?> COLUMN_HEADING_LARGE;
  public static Component<?> _nld;
  public static PlayerList IGNORE_LIST;

  public int _I = 256;
  public boolean enabled = true;
  public int y2;
  protected Sprite _Z;
  public int textColor;
  private int _V = Integer.MIN_VALUE;
  private Sprite disabledSprite;
  public int _w;
  public int _kb;
  private int _t = Integer.MIN_VALUE;
  public int x = 0;
  public @NotNull Font.HorizontalAlignment textAlignment = Font.HorizontalAlignment.LEFT;
  public Sprite sprite;
  @MagicConstant(valuesFromClass = MouseState.Button.class)
  protected int dragButton;
  public int _Y;
  protected boolean _r;
  protected @NotNull Font.VerticalAlignment verticalAlignment = Font.VerticalAlignment.TOP;
  private Sprite mouseHeldSprite;
  public Font font;
  private int _T = Integer.MIN_VALUE;
  public Sprite[] mouseOverNineSliceSprites;
  private Sprite mouseOverSprite;
  public List<T> children;
  private Sprite[] mouseHeldNineSliceSprites;
  private int _J = Integer.MIN_VALUE;
  private int _W = -1;
  public String _u;
  public int mouseOverTextColor = -1;
  public int _gb;
  @MagicConstant(valuesFromClass = MouseState.Button.class)
  public int clickButton;
  private Sprite[] disabledNineSliceSprites;
  public int _qb = -1;
  public int width;
  public Sprite[] nineSliceSprites;
  protected boolean isDraggable;
  private Sprite[] activeNineSliceSprites;
  public int x2;
  public int relativeClickX;
  public boolean selected;
  public boolean isMouseOver;
  public String label;
  public int y = 0;
  public int height;
  public int _ab = -1;
  public boolean isMouseOverTarget;
  private int relativeClickY;
  private int _tb;
  private boolean _H;
  private int _cb;
  private int _S;

  public Component(final Component<?> attrsSrc) {
    this(attrsSrc, 0, 0, null);
  }

  public Component(final Component<?> attrsSrc, final String label) {
    this(attrsSrc, 0, 0, label);
  }

  protected Component() {
    this(null, 0, 0, null);
  }

  protected Component(final Component<?> attrsSrc, final int var6, final int var7, final String label) {
    this.width = var6;
    this.height = var7;
    this.copyAttributesFrom(attrsSrc);
    this.label = label;
  }

  public static String a369(final String var0) {
    final int var3 = var0.length();
    final int var4 = "<br>".length();
    final int var5 = "<br><col=A00000>".length();
    int var6 = var3;
    final int var7 = -var4 + var5;
    int var8 = 0;

    while (true) {
      var8 = var0.indexOf("<br>", var8);
      if (var8 < 0) {
        break;
      }

      var6 += var7;
      var8 += var4;
    }

    final StringBuilder var11 = new StringBuilder(var6);
    int var9 = 0;

    while (true) {
      final int var10 = var0.indexOf("<br>", var9);
      if (var10 < 0) {
        var11.append(var0.substring(var9));
        return var11.toString();
      }

      var11.append(var0, var9, var10);
      var11.append("<br><col=A00000>");
      var9 = var10 + var4;
    }
  }

  public static void a469(final ResourceLoader spriteLoader, final ResourceLoader fontLoader, final @NotNull Sprite[][] gameoptSprites) {
    a674m(spriteLoader, fontLoader);
    a460bq(spriteLoader);
    a958tb(spriteLoader, gameoptSprites);
    initializeChatResources(spriteLoader);
    initializeRuleStrings();
    initializeLobbyBrowserComponents();
    a487te();
  }

  private static void a674m(final ResourceLoader spriteLoader, final ResourceLoader fontLoader) {
    final PalettedSymbol[] crowns = SpriteResource.loadPalettedSprites(spriteLoader, "lobby", "crowns");
    CROWNS_COUNT = crowns.length;
    final int[] crowsAscents = new int[crowns.length];
    for (int i = 0; i < CROWNS_COUNT; ++i) {
      crowsAscents[i] = 10;
    }

    _rcl = 2;
    _si = 11;
    ShatteredPlansClient._tga = 4;
    ClientLobbyRoom._qob = 2;
    final SpriteFont LARGE_FONT = SpriteResource.loadSpriteFont(spriteLoader, fontLoader, "largefont");
    GENERAL_FONT = SpriteResource.loadSpriteFont(spriteLoader, fontLoader, "generalfont");
    CHAT_FONT = SpriteResource.loadSpriteFont(spriteLoader, fontLoader, "chatfont");
    LARGE_FONT.setSymbols(crowns, crowsAscents);
    GENERAL_FONT.setSymbols(crowns, crowsAscents);
    CHAT_FONT.setSymbols(crowns, crowsAscents);
    final Component<?> var7 = new Component<>(null);
    var7.font = LARGE_FONT;
    var7.textColor = Drawing.WHITE;
    var7._Y = LABEL_HEIGHT;
    var7.verticalAlignment = Font.VerticalAlignment.MIDDLE;
    var7.textAlignment = Font.HorizontalAlignment.CENTER;
    final Component<?> var9 = new Component<>(null);
    var9.textColor = Drawing.WHITE;
    var9._Y = LABEL_HEIGHT;
    var9.font = GENERAL_FONT;
    var9.verticalAlignment = Font.VerticalAlignment.MIDDLE;
    var9.textAlignment = Font.HorizontalAlignment.CENTER;
    HEADING = new Component<>(var7);
    HEADING.nineSliceSprites = adjustForNineSlice(SpriteResource.loadSprites(spriteLoader, "lobby", "heading"));
    _cna = new Component<>(null);
    _cna.nineSliceSprites = createColumnHeadingSprites(120, 0x404040, 0x808080, false, false);
    _mpa = new Component<>(null);
    _mpa.nineSliceSprites = createGradientOutlineSprites(114, 0x606060, 0x606060, 1);
    POPUP = new Component<>(null);
    POPUP.nineSliceSprites = adjustForNineSlice(SpriteResource.loadSprites(spriteLoader, "lobby", "popup"));
    final Sprite[] POPUP_MOUSE_OVER_SPRITES = adjustForNineSlice(SpriteResource.loadSprites(spriteLoader, "lobby", "popup_mouseover"));
    final Sprite[] TAB_ACTIVE_SPRITES = adjustForNineSlice(SpriteResource.loadSprites(spriteLoader, "lobby", "tab_active"));
    TAB_ACTIVE = new Component<>(var7);
    TAB_ACTIVE.nineSliceSprites = TAB_ACTIVE_SPRITES;
    CLOSE_BUTTON = new Component<>(null);
    CLOSE_BUTTON.sprite = SpriteResource.loadSprite(spriteLoader, "lobby", "closebutton");
    CLOSE_BUTTON.mouseOverSprite = SpriteResource.loadSprite(spriteLoader, "lobby", "closebutton_mouseover");
    COLUMN_HEADING_LARGE = new Component<>(var7);
    COLUMN_HEADING_LARGE.nineSliceSprites = createColumnHeadingSprites(40, 0x1f1f1f, 0x3a3a3a, true, true);
    COLUMN_HEADING_MEDIUM_LEFT = new Component<>(var9);
    COLUMN_HEADING_MEDIUM_LEFT._kb = 2;
    COLUMN_HEADING_MEDIUM_LEFT.nineSliceSprites = createColumnHeadingSprites(30, 0x1f1f1f, 0x3a3a3a, true, false);
    COLUMN_HEADING_MEDIUM_CENTER = new Component<>(var9);
    COLUMN_HEADING_MEDIUM_CENTER._kb = 2;
    COLUMN_HEADING_MEDIUM_CENTER.nineSliceSprites = createColumnHeadingSprites(30, 0x1f1f1f, 0x3a3a3a, false, false);
    COLUMN_HEADING_MEDIUM_RIGHT = new Component<>(var9);
    COLUMN_HEADING_MEDIUM_RIGHT._kb = 2;
    COLUMN_HEADING_MEDIUM_RIGHT.nineSliceSprites = createColumnHeadingSprites(30, 0x1f1f1f, 0x3a3a3a, false, true);
    LABEL = new Component<>(null);
    LABEL.textColor = 0xcccccc;
    LABEL._Y = LABEL_HEIGHT;
    LABEL.verticalAlignment = Font.VerticalAlignment.MIDDLE;
    LABEL.font = GENERAL_FONT;
    UNSELECTED_LABEL = new Component<>(LABEL);
    UNSELECTED_LABEL._ab = Drawing.WHITE;
    UNSELECTED_LABEL._W = 0x808080;
    UNSELECTED_LABEL.mouseOverTextColor = Drawing.WHITE;
    UNSELECTED_LABEL.textColor = UNSELECTED_TEXT_COLOR;
    UNSELECTED_LABEL._qb = Drawing.WHITE;
    final Component<?> LARGE_LABEL = new Component<>(UNSELECTED_LABEL);
    LARGE_LABEL.textColor = Drawing.WHITE;
    LARGE_LABEL._Y = LABEL_HEIGHT;
    LARGE_LABEL.font = LARGE_FONT;
    LABEL_DARK_1 = new Component<>(LABEL);
    LABEL_DARK_1.nineSliceSprites = createColumnHeadingSprites(16, 0x222222, 0x222222, false, false);
    LABEL_DARK_1._kb = 2;
    LABEL_DARK_2 = new Component<>(LABEL);
    LABEL_DARK_2.nineSliceSprites = createColumnHeadingSprites(16, 0x171717, 0x171717, false, false);
    LABEL_DARK_2._kb = 2;
    UNSELECTED_LABEL_DARK_1 = new Component<>(LABEL_DARK_1);
    UNSELECTED_LABEL_DARK_1.copyAttributesFrom(UNSELECTED_LABEL);
    UNSELECTED_LABEL_DARK_2 = new Component<>(LABEL_DARK_2);
    UNSELECTED_LABEL_DARK_2.copyAttributesFrom(UNSELECTED_LABEL);
    _hdt = new PopupMenu(POPUP, POPUP_MOUSE_OVER_SPRITES, LABEL, UNSELECTED_LABEL, 3, 2, ClientLobbyRoom._qob, 3, LABEL_HEIGHT);
    TAB_INACTIVE = new Component<>(UNSELECTED_LABEL);
    TAB_INACTIVE.textAlignment = Font.HorizontalAlignment.CENTER;
    TAB_INACTIVE.nineSliceSprites = adjustForNineSlice(SpriteResource.loadSprites(spriteLoader, "lobby", "tab_inactive"));
    TAB_INACTIVE.mouseOverNineSliceSprites = adjustForNineSlice(SpriteResource.loadSprites(spriteLoader, "lobby", "tab_mouseover"));
    TAB_INACTIVE.activeNineSliceSprites = TAB_ACTIVE_SPRITES;
    _tmt = new Component<>(null);
    _tmt.nineSliceSprites = createGradientOutlineSprites(206, 1856141, 1127256, -1);
    _rsEb = new Component<>(null);
    _rsEb.nineSliceSprites = createGradientOutlineSprites(290, 11579568, 6052956, -1);
    BIG_BUTTON = new Component<>(LARGE_LABEL);
    BIG_BUTTON._T = 1;
    BIG_BUTTON.textAlignment = Font.HorizontalAlignment.CENTER;
    BIG_BUTTON._J = 1;
    BIG_BUTTON._t = 1;
    BIG_BUTTON._V = 1;
    MEDIUM_BUTTON = new Component<>(BIG_BUTTON);
    final Component<?> smallButton = new Component<>(UNSELECTED_LABEL);
    smallButton._V = 1;
    smallButton._t = 1;
    smallButton.textAlignment = Font.HorizontalAlignment.CENTER;
    smallButton._T = 1;
    smallButton._J = 1;
    GREEN_BUTTON = new Component<>(smallButton);
    RED_BUTTON = new Component<>(smallButton);
    BACK_BUTTON = new Component<>(BIG_BUTTON);
    CHAT_BUTTON = new Component<>(smallButton);
    CHAT_FILTER_BUTTON = new Component<>(smallButton);
    GAMEOPT_BUTTON = new Component<>(smallButton);
    smallButton.nineSliceSprites = adjustForNineSlice(SpriteResource.loadSprites(spriteLoader, "lobby", "smallbutton"));
    smallButton.mouseOverNineSliceSprites = adjustForNineSlice(SpriteResource.loadSprites(spriteLoader, "lobby", "smallbutton_mouseover"));
    smallButton.mouseHeldNineSliceSprites = smallButton.activeNineSliceSprites = adjustForNineSlice(SpriteResource.loadSprites(spriteLoader, "lobby", "smallbutton_active"));
    smallButton.disabledNineSliceSprites = adjustForNineSlice(SpriteResource.loadSprites(spriteLoader, "lobby", "smallbutton_disabled"));
    MEDIUM_BUTTON.nineSliceSprites = adjustForNineSlice(SpriteResource.loadSprites(spriteLoader, "lobby", "mediumbutton"));
    MEDIUM_BUTTON.mouseOverNineSliceSprites = adjustForNineSlice(SpriteResource.loadSprites(spriteLoader, "lobby", "mediumbutton_mouseover"));
    MEDIUM_BUTTON.mouseHeldNineSliceSprites = adjustForNineSlice(SpriteResource.loadSprites(spriteLoader, "lobby", "mediumbutton_mouseheld"));
    BIG_BUTTON.nineSliceSprites = adjustForNineSlice(SpriteResource.loadSprites(spriteLoader, "lobby", "bigbutton"));
    BIG_BUTTON.mouseOverNineSliceSprites = adjustForNineSlice(SpriteResource.loadSprites(spriteLoader, "lobby", "bigbutton_mouseover"));
    BIG_BUTTON.mouseHeldNineSliceSprites = adjustForNineSlice(SpriteResource.loadSprites(spriteLoader, "lobby", "bigbutton_mouseheld"));
    BIG_BUTTON.disabledNineSliceSprites = adjustForNineSlice(SpriteResource.loadSprites(spriteLoader, "lobby", "bigbutton_disabled"));
    GREEN_BUTTON.nineSliceSprites = adjustForNineSlice(SpriteResource.loadSprites(spriteLoader, "lobby", "greenbutton"));
    GREEN_BUTTON.mouseOverNineSliceSprites = adjustForNineSlice(SpriteResource.loadSprites(spriteLoader, "lobby", "greenbutton_mouseover"));
    GREEN_BUTTON.mouseHeldNineSliceSprites = adjustForNineSlice(SpriteResource.loadSprites(spriteLoader, "lobby", "greenbutton_mouseheld"));
    RED_BUTTON.nineSliceSprites = adjustForNineSlice(SpriteResource.loadSprites(spriteLoader, "lobby", "redbutton"));
    RED_BUTTON.mouseOverNineSliceSprites = adjustForNineSlice(SpriteResource.loadSprites(spriteLoader, "lobby", "redbutton_mouseover"));
    RED_BUTTON.mouseHeldNineSliceSprites = adjustForNineSlice(SpriteResource.loadSprites(spriteLoader, "lobby", "redbutton_mouseheld"));
    BACK_BUTTON.nineSliceSprites = adjustForNineSlice(SpriteResource.loadSprites(spriteLoader, "lobby", "backbutton"));
    BACK_BUTTON.mouseOverNineSliceSprites = adjustForNineSlice(SpriteResource.loadSprites(spriteLoader, "lobby", "backbutton_mouseover"));
    BACK_BUTTON.mouseHeldNineSliceSprites = adjustForNineSlice(SpriteResource.loadSprites(spriteLoader, "lobby", "backbutton_mouseheld"));
    BACK_BUTTON.disabledNineSliceSprites = adjustForNineSlice(SpriteResource.loadSprites(spriteLoader, "lobby", "backbutton_disabled"));
    GAMEOPT_BUTTON.nineSliceSprites = adjustForNineSlice(SpriteResource.loadSprites(spriteLoader, "lobby", "gameoptionbutton"));
    GAMEOPT_BUTTON.mouseOverNineSliceSprites = adjustForNineSlice(SpriteResource.loadSprites(spriteLoader, "lobby", "gameoptionbutton_mouseover"));
    GAMEOPT_BUTTON.mouseHeldNineSliceSprites = GAMEOPT_BUTTON.activeNineSliceSprites = adjustForNineSlice(SpriteResource.loadSprites(spriteLoader, "lobby", "gameoptionbutton_active"));
    GAMEOPT_BUTTON.disabledNineSliceSprites = adjustForNineSlice(SpriteResource.loadSprites(spriteLoader, "lobby", "gameoptionbutton_disabled"));
    CHAT_BUTTON.nineSliceSprites = adjustForNineSlice(SpriteResource.loadSprites(spriteLoader, "lobby", "chatbutton"));
    CHAT_BUTTON.mouseOverNineSliceSprites = adjustForNineSlice(SpriteResource.loadSprites(spriteLoader, "lobby", "chatbutton_mouseover"));
    CHAT_BUTTON.mouseHeldNineSliceSprites = CHAT_BUTTON.activeNineSliceSprites = adjustForNineSlice(SpriteResource.loadSprites(spriteLoader, "lobby", "chatbutton_active"));
    CHAT_FILTER_BUTTON.nineSliceSprites = adjustForNineSlice(SpriteResource.loadSprites(spriteLoader, "lobby", "chatfilterbutton"));
    CHAT_FILTER_BUTTON.mouseOverNineSliceSprites = adjustForNineSlice(SpriteResource.loadSprites(spriteLoader, "lobby", "chatfilterbutton_mouseover"));
    CHAT_FILTER_BUTTON.mouseHeldNineSliceSprites = CHAT_FILTER_BUTTON.activeNineSliceSprites = adjustForNineSlice(SpriteResource.loadSprites(spriteLoader, "lobby", "chatfilterbutton_active"));

    final Sprite[] checkboxSprites = SpriteResource.loadSprites(spriteLoader, "lobby", "checkbox");
    CHECKBOX = new Checkbox(checkboxSprites[1], checkboxSprites[0], UNSELECTED_LABEL);

    final Component<?> slideRegion = new Component<>(null);
    slideRegion.nineSliceSprites = centerOnly(SpriteResource.loadSprite(spriteLoader, "lobby", "slideregion"));
    slideRegion.mouseOverNineSliceSprites = centerOnly(SpriteResource.loadSprite(spriteLoader, "lobby", "slideregion_mouseover"));
    slideRegion.mouseHeldNineSliceSprites = centerOnly(SpriteResource.loadSprite(spriteLoader, "lobby", "slideregion_mouseheld"));
    slideRegion.disabledNineSliceSprites = centerOnly(SpriteResource.loadSprite(spriteLoader, "lobby", "slideregion_disabled"));
    final Component<?> dragBar = new Component<>(null);
    dragBar.nineSliceSprites = adjustForNineSlice(SpriteResource.loadSprites(spriteLoader, "lobby", "dragbar"));
    dragBar.mouseOverNineSliceSprites = adjustForNineSlice(SpriteResource.loadSprites(spriteLoader, "lobby", "dragbar_mouseover"));
    dragBar.mouseHeldNineSliceSprites = adjustForNineSlice(SpriteResource.loadSprites(spriteLoader, "lobby", "dragbar_mouseheld"));
    dragBar.disabledNineSliceSprites = adjustForNineSlice(SpriteResource.loadSprites(spriteLoader, "lobby", "dragbar_disabled"));
    final Component<?> upButton = new Component<>(null);
    upButton.sprite = SpriteResource.loadSprite(spriteLoader, "lobby", "upbutton");
    upButton.mouseOverSprite = SpriteResource.loadSprite(spriteLoader, "lobby", "upbutton_mouseover");
    upButton.mouseHeldSprite = SpriteResource.loadSprite(spriteLoader, "lobby", "upbutton_mouseheld");
    upButton.disabledSprite = SpriteResource.loadSprite(spriteLoader, "lobby", "upbutton_disabled");
    final Component<?> downButton = new Component<>(null);
    downButton.sprite = SpriteResource.loadSprite(spriteLoader, "lobby", "downbutton");
    downButton.mouseOverSprite = SpriteResource.loadSprite(spriteLoader, "lobby", "downbutton_mouseover");
    downButton.mouseHeldSprite = SpriteResource.loadSprite(spriteLoader, "lobby", "downbutton_mouseheld");
    downButton.disabledSprite = SpriteResource.loadSprite(spriteLoader, "lobby", "downbutton_disabled");
    SCROLL_BAR = new ScrollBar(upButton, downButton, slideRegion, dragBar);
    PLAYER_LIST = new PlayerList(null, null, SCROLL_BAR, smallButton, null, null);
  }

  private static void a958tb(final ResourceLoader var4, final @NotNull Sprite[][] gameoptSprites) {
    ClientLobbyRoom.WHO_CAN_JOIN_OPTION_SPRITES = SpriteResource.loadSprites(var4, "lobby", "gameprivacy");
    ClientLobbyRoom.RATED_GAME_SPRITES = SpriteResource.loadSprites(var4, "lobby", "ratedgame");
    ClientLobbyRoom.OPEN_TO_ME_SPRITES = SpriteResource.loadSprites(var4, "lobby", "opentome");
    ClientLobbyRoom.ALLOW_SPECTATORS_SPRITES = SpriteResource.loadSprites(var4, "lobby", "allowspectators");

    ClientLobbyRoom.WHO_CAN_JOIN_OPTION_TOOLTIPS = new String[5];
    ClientLobbyRoom.WHO_CAN_JOIN_OPTION_TOOLTIPS[0] = StringConstants.MULTICONST_INVITE_ONLY;
    ClientLobbyRoom.WHO_CAN_JOIN_OPTION_TOOLTIPS[1] = StringConstants.MULTICONST_CLAN;
    ClientLobbyRoom.WHO_CAN_JOIN_OPTION_TOOLTIPS[2] = StringConstants.MULTICONST_FRIENDS;
    ClientLobbyRoom.WHO_CAN_JOIN_OPTION_TOOLTIPS[3] = StringConstants.MULTICONST_SIMILAR_RATING;
    ClientLobbyRoom.WHO_CAN_JOIN_OPTION_TOOLTIPS[4] = StringConstants.MULTICONST_OPEN;

    INVITE_PLAYER_LIST_PANEL = new Component<>(null);
    INVITE_PLAYER_LIST_HEADING_NAME = new Component<>(COLUMN_HEADING_MEDIUM_LEFT, StringConstants.MU_LOBBY_NAME);
    INVITE_PLAYER_LIST_HEADING_RATING = new Component<>(COLUMN_HEADING_MEDIUM_RIGHT, StringConstants.MU_LOBBY_RATING);
    INVITE_PLAYER_LIST_SCROLL_PANE = new ScrollPane<>(new Component<>(null), null, SCROLL_BAR);
    INVITE_PLAYER_LIST_PANEL.addChild(INVITE_PLAYER_LIST_HEADING_NAME);
    INVITE_PLAYER_LIST_PANEL.addChild(INVITE_PLAYER_LIST_HEADING_RATING);
    INVITE_PLAYER_LIST_PANEL.addChild(INVITE_PLAYER_LIST_SCROLL_PANE);
    INVITE_PLAYER_LIST_SCROLL_PANE.viewport.copyAttributesFrom(LABEL);
    INVITE_PLAYER_LIST_SCROLL_PANE.viewport.verticalAlignment = Font.VerticalAlignment.MIDDLE;
    INVITE_PLAYER_LIST_SCROLL_PANE.viewport.textAlignment = Font.HorizontalAlignment.CENTER;

    YOUR_RATING_LABEL = new Component<>(LABEL);
    YOUR_RATING_LABEL.textAlignment = Font.HorizontalAlignment.CENTER;
    RETURN_TO_MAIN_MENU_BUTTON = new Component<>(BACK_BUTTON, StringConstants.RETURN_TO_MAIN_MENU.toUpperCase());
    ALL_GAMES_HEADING = new Component<>(HEADING, StringConstants.MU_GAMELIST_ALL_GAMES.toUpperCase());
    GAME_LIST_PANEL = new Component<>(_rsEb);
    GAME_LIST_HEADING_STATUS = new Component<>(COLUMN_HEADING_MEDIUM_LEFT, StringConstants.STATUS);
    GAME_LIST_HEADING_OWNER = new Component<>(COLUMN_HEADING_MEDIUM_CENTER, StringConstants.MU_GAMELIST_OWNER);
    GAME_LIST_HEADING_PLAYERS = new Component<>(COLUMN_HEADING_MEDIUM_CENTER, StringConstants.MU_GAMELIST_PLAYERS);
    GAME_LIST_HEADING_AVG_RATING = new Component<>(COLUMN_HEADING_MEDIUM_CENTER, StringConstants.MU_GAMELIST_AVG_RATING);
    GAME_LIST_HEADING_OPTIONS = new Component<>(COLUMN_HEADING_MEDIUM_CENTER, StringConstants.MU_GAMELIST_OPTIONS);
    GAME_LIST_HEADING_ELAPSED_TIME = new Component<>(COLUMN_HEADING_MEDIUM_RIGHT, StringConstants.MU_GAMELIST_ELAPSED_TIME);
    GAME_LIST_SCROLL_PANE = new ScrollPane<>(new Component<>(null), null, SCROLL_BAR);
    PLAY_RATED_GAME_BUTTON = new Component<>(BIG_BUTTON, StringConstants.MU_PLAY_RATED.toUpperCase());
    PLAY_RATED_GAME_BUTTON.enabled = true;
    CREATE_UNRATED_GAME_BUTTON = new Component<>(BIG_BUTTON, (StringConstants.MU_CREATE_UNRATED).toUpperCase());
    LOBBY_RIGHT_PANEL = new Component<>(null);
    LOBBY_RIGHT_PANEL.addChild(ALL_GAMES_HEADING);
    LOBBY_RIGHT_PANEL.addChild(GAME_LIST_PANEL);
    GAME_LIST_PANEL.addChild(GAME_LIST_HEADING_STATUS);
    GAME_LIST_PANEL.addChild(GAME_LIST_HEADING_OWNER);
    GAME_LIST_PANEL.addChild(GAME_LIST_HEADING_PLAYERS);
    GAME_LIST_PANEL.addChild(GAME_LIST_HEADING_AVG_RATING);
    GAME_LIST_PANEL.addChild(GAME_LIST_HEADING_OPTIONS);
    GAME_LIST_PANEL.addChild(GAME_LIST_HEADING_ELAPSED_TIME);
    GAME_LIST_PANEL.addChild(GAME_LIST_SCROLL_PANE);
    LOBBY_RIGHT_PANEL.addChild(PLAY_RATED_GAME_BUTTON);
    LOBBY_RIGHT_PANEL.addChild(CREATE_UNRATED_GAME_BUTTON);
    YOU_HAVE_BEEN_KICKED_LABEL = new Component<>(LABEL);
    YOU_HAVE_BEEN_KICKED_LABEL.verticalAlignment = Font.VerticalAlignment.TOP;
    YOU_HAVE_BEEN_KICKED_LABEL.textAlignment = Font.HorizontalAlignment.CENTER;
    YOU_HAVE_BEEN_KICKED_OK_BUTTON = new Component<>(MEDIUM_BUTTON, StringConstants.OK.toUpperCase());
    YOU_HAVE_BEEN_KICKED_DIALOG = new Component<>(null);
    YOU_HAVE_BEEN_KICKED_DIALOG.addChild(YOU_HAVE_BEEN_KICKED_LABEL);
    YOU_HAVE_BEEN_KICKED_DIALOG.addChild(YOU_HAVE_BEEN_KICKED_OK_BUTTON);
    RATED_GAME_TIPS_LABEL = new Component<>(null);
    RATED_GAME_TIPS_LABEL.copyAttributesFrom(LABEL);
    RATED_GAME_TIPS_LABEL.verticalAlignment = Font.VerticalAlignment.MIDDLE;
    RATED_GAME_TIPS_LABEL.textAlignment = Font.HorizontalAlignment.CENTER;
    JOINED_PLAYERS_PANEL = new Component<>(null);
    JOINED_PLAYER_COUNT_LABEL = new Component<>(LABEL);
    JOINED_PLAYER_COUNT_LABEL.textAlignment = Font.HorizontalAlignment.CENTER;
    NAME_LABEL = new Component<>(COLUMN_HEADING_MEDIUM_LEFT, StringConstants.MU_LOBBY_NAME);
    RATING_LABEL = new Component<>(COLUMN_HEADING_MEDIUM_RIGHT, StringConstants.MU_LOBBY_RATING);
    JOINED_PLAYERS_TABLE = new ScrollPane<>(new Component<>(null), null, SCROLL_BAR);
    JOINED_PLAYERS_PANEL.addChild(JOINED_PLAYER_COUNT_LABEL);
    JOINED_PLAYERS_PANEL.addChild(NAME_LABEL);
    JOINED_PLAYERS_PANEL.addChild(RATING_LABEL);
    JOINED_PLAYERS_PANEL.addChild(JOINED_PLAYERS_TABLE);
    RETURN_TO_LOBBY_BUTTON = new Component<>(BACK_BUTTON, StringConstants.RETURN_TO_LOBBY.toUpperCase());
    GAME_OWNER_HEADING = new Component<>(HEADING);
    GAME_OPTIONS_CONTAINER = new Component<>(_rsEb);

    GAME_OPTIONS_HEADING = new Component<>(COLUMN_HEADING_LARGE, StringConstants.MU_OPTIONS.toUpperCase());
    GAME_OPTIONS_LABELS = new Component[9];
    GAME_OPTIONS_BUTTONS = new ActionButton[9][];

    GAME_OPTIONS_LABELS[0] = new Component<>(LABEL_DARK_2, StringConstants.MU_OPTIONS_WHO_CAN_JOIN);
    GAME_OPTIONS_BUTTONS[0] = new ActionButton[6];
    for (int i = 0; i < 5; ++i) {
      GAME_OPTIONS_BUTTONS[0][i + 1] = new ActionButton(GAMEOPT_BUTTON, UNSELECTED_LABEL, ClientLobbyRoom.WHO_CAN_JOIN_OPTION_SPRITES[i], ClientLobbyRoom.WHO_CAN_JOIN_OPTION_TOOLTIPS[i]);
    }

    GAME_OPTIONS_LABELS[1] = new Component<>(LABEL_DARK_2, StringConstants.MU_OPTIONS_PLAYERS);
    GAME_OPTIONS_BUTTONS[1] = new ActionButton[1 + ShatteredPlansClient.NUM_PLAYERS_OPTION_VALUES.length];
    GAME_OPTIONS_BUTTONS[1][0] = new ActionButton(GAMEOPT_BUTTON, UNSELECTED_LABEL, null, StringConstants.MU_OPTIONS_DONT_MIND);
    for (int i = 0; i < ShatteredPlansClient.NUM_PLAYERS_OPTION_VALUES.length; ++i) {
      GAME_OPTIONS_BUTTONS[1][i + 1] = new ActionButton(GAMEOPT_BUTTON, UNSELECTED_LABEL, null, Integer.toString(ShatteredPlansClient.NUM_PLAYERS_OPTION_VALUES[i]));
    }

    GAME_OPTIONS_LABELS[2] = new Component<>(LABEL_DARK_2, StringConstants.MU_OPTIONS_ALLOW_SPECTATE);
    GAME_OPTIONS_BUTTONS[2] = new ActionButton[]{
        new ActionButton(GAMEOPT_BUTTON, UNSELECTED_LABEL, null, StringConstants.MU_OPTIONS_DONT_MIND),
        new ActionButton(GAMEOPT_BUTTON, UNSELECTED_LABEL, ClientLobbyRoom.ALLOW_SPECTATORS_SPRITES[0], StringConstants.NO),
        new ActionButton(GAMEOPT_BUTTON, UNSELECTED_LABEL, ClientLobbyRoom.ALLOW_SPECTATORS_SPRITES[1], StringConstants.YES)
    };

    for (int i = 0; i < 5; ++i) {
      GAME_OPTIONS_LABELS[i + 4] = new Component<>(LABEL_DARK_2, StringConstants.GAMEOPT_LABELS[i]);
      GAME_OPTIONS_BUTTONS[i + 4] = new ActionButton[ShatteredPlansClient.GAMEOPT_CHOICES_COUNTS[i] + 1];
      GAME_OPTIONS_BUTTONS[i + 4][0] = new ActionButton(GAMEOPT_BUTTON, UNSELECTED_LABEL, null, StringConstants.MU_OPTIONS_DONT_MIND);

      for (int j = 0; j < ShatteredPlansClient.GAMEOPT_CHOICES_COUNTS[i]; ++j) {
        final Sprite sprites = gameoptSprites[i] != null ? gameoptSprites[i][j] : null;
        final String tooltip = StringConstants.GAMEOPT_NAMES[i] != null ? StringConstants.GAMEOPT_NAMES[i][j] : null;
        GAME_OPTIONS_BUTTONS[i + 4][j + 1] = new ActionButton(GAMEOPT_BUTTON, UNSELECTED_LABEL, sprites, tooltip);
      }
    }

    for (int i = 0; i < 9; ++i) {
      if (GAME_OPTIONS_LABELS[i] != null) {
        GAME_OPTIONS_LABELS[i]._Y = 11;
      }

      if (GAME_OPTIONS_BUTTONS[i] != null) {
        for (int j = 0; j < GAME_OPTIONS_BUTTONS[i].length; ++j) {
          if (GAME_OPTIONS_BUTTONS[i][j] != null && GAME_OPTIONS_BUTTONS[i][j]._Bb != null) {
            GAME_OPTIONS_BUTTONS[i][j]._Bb._Y = 11;
          }
        }
      }
    }

    INVITE_PLAYERS_BUTTON = new Component<>(BIG_BUTTON);
    FIND_OPPONENTS_BUTTON = new Component<>(BIG_BUTTON);
    WAITING_TO_START_LABEL = new Component<>(LABEL_DARK_1);
    WAITING_TO_START_LABEL.copyAttributesFrom(LABEL);
    WAITING_TO_START_LABEL.verticalAlignment = Font.VerticalAlignment.MIDDLE;
    WAITING_TO_START_LABEL.textAlignment = Font.HorizontalAlignment.CENTER;
    GAME_INFO_CONTAINER = new Component<>(null);
    GAME_INFO_CONTAINER.addChild(GAME_OWNER_HEADING);
    GAME_INFO_CONTAINER.addChild(GAME_OPTIONS_CONTAINER);
    GAME_OPTIONS_CONTAINER.addChild(GAME_OPTIONS_HEADING);

    for (int i = 0; i < 4 + 5; ++i) {
      if (i != 3) {
        GAME_OPTIONS_CONTAINER.addChild(GAME_OPTIONS_LABELS[i]);
        for (int j = 0; j < GAME_OPTIONS_BUTTONS[i].length; ++j) {
          if (GAME_OPTIONS_BUTTONS[i][j] != null) {
            GAME_OPTIONS_CONTAINER.addChild(GAME_OPTIONS_BUTTONS[i][j]);
          }
        }
      }
    }

    GAME_INFO_CONTAINER.addChild(INVITE_PLAYERS_BUTTON);
    GAME_INFO_CONTAINER.addChild(FIND_OPPONENTS_BUTTON);
    GAME_INFO_CONTAINER.addChild(WAITING_TO_START_LABEL);
    INVITE_PLAYERS_LABEL = new Component<>(TAB_ACTIVE, StringConstants.MU_INVITE_PLAYERS.toUpperCase());
    CLOSE_BUTTON_2 = new Component<>(MEDIUM_BUTTON, StringConstants.CLOSE.toUpperCase());
  }

  private static void initializeChatResources(final ResourceLoader var2) {
    initializeChatResources1();
    CHAT_FILTER_SPRITES = SpriteResource.loadSprites(var2, "lobby", "chatfilter");
    CHAT_FILTER_LABELS[0] = StringConstants.MU_CHAT_ON;
    CHAT_FILTER_LABELS[1] = StringConstants.MU_CHAT_FRIENDS;
    CHAT_FILTER_LABELS[2] = StringConstants.MU_CHAT_OFF;
    initializeQuickChatResources();
  }

  private static void initializeQuickChatResources() {
    _uaf = new Component<>(null);
    if (ResourceLoader.QUICK_CHAT_DATA == null) {
      JagexApplet.clientError(null, "QC1");
      return;
    }

    ResourceLoader.QUICK_CHAT_DATA.releaseItemsOnGet = false;
    ResourceLoader.QUICK_CHAT_DATA.releaseGroupsOnGet = false;
    _guiA = new QuickChatCategories(ResourceLoader.QUICK_CHAT_DATA);
    JagexApplet._dhc = new QuickChatResponses(ResourceLoader.QUICK_CHAT_DATA);
    final QuickChatCategory var4 = a528hc();
    if (var4 == null) {
      JagexApplet.clientError(null, "QC2");
      return;
    }

    a857ke(var4);
    ReportAbuseDialog._Nb = a658rd(65793, 5138823, 65793, 65793, 4020342, 1127256, 1513239, null, 0, 2245737, 1513239, 8947848);
    _nld = a658rd(0, 0, 0, 0, 0, 0, 0, GENERAL_FONT, 16764006, 0, 0, 0);
    _hlI = a658rd(0, 0, 0, 0, 0, 0, 0, GENERAL_FONT, Drawing.WHITE, 0, 0, 0);

    final int var5 = Drawing.width;
    final int var6 = Drawing.height;
    final int[] var7 = Drawing.screenBuffer;
    vm_._kflc = new Sprite(10, 14);
    vm_._kflc.installForDrawing();

    for (int var8 = 2; var8 < 7; ++var8) {
      Drawing.verticalLine(var8, var8 + 1, -(var8 << 1) + 14, Drawing.WHITE);
    }

    Drawing.initialize(var7, var5, var6);
    _ofb = a427gn(var4, _hlI, _nld, ReportAbuseDialog._Nb);
    _uaf.children = new ArrayList<>();
  }

  private static void initializeChatResources1() {
    CHANNEL_TEXT_COLORS_1[0] = 0xffcc60;
    CHANNEL_TEXT_COLORS_1[1] = 0x60ff60;
    CHANNEL_TEXT_COLORS_1[2] = 0x9090ff;
    CHANNEL_TEXT_COLORS_1[3] = 0xff60ff;
    CHANNEL_TEXT_COLORS_1[4] = 0xff00ff;

    CHANNEL_TEXT_COLORS_2[0] = 0xffcc60;
    CHANNEL_TEXT_COLORS_2[1] = 0x60ff60;
    CHANNEL_TEXT_COLORS_2[2] = 0xff6060;
    CHANNEL_TEXT_COLORS_2[3] = 0xff60ff;

    final Sprite hatching = new Sprite(4, 4);
    hatching.pixels[2] = 0x707070;
    hatching.pixels[5] = 0x707070;
    hatching.pixels[8] = 0x707070;
    hatching.pixels[15] = 0x707070;

    _tgc = new Component<>(_cna);
    _adc = new Component<>(_mpa);
    _tgc.addChild(_adc);
    final Component<Component<?>> var16 = new Component<>(null);
    var16.children = new ArrayList<>();
    _jiI = new ScrollPane<>(var16, LABEL_DARK_2, SCROLL_BAR);
    _adc.addChild(_jiI);
    _taio = new Component<>(LABEL_DARK_1);
    _adc.addChild(_taio);
    ShatteredPlansClient.chatMessageLabel = new Component<>(LABEL);
    ShatteredPlansClient.chatMessageLabel.font = CHAT_FONT;
    _taio.addChild(ShatteredPlansClient.chatMessageLabel);
    _taio.recursivelySet_H();
    _cgC = new Component<>(null);
    _cgC.nineSliceSprites = new Sprite[9];
    _cgC.nineSliceSprites[NineSliceSprite.CENTER] = hatching;
    _taio.addChild(_cgC);
    chatFilterIcons = new Component[5];
    chatFilterChannelLabels = new Component[5];
    chatFilterLabels = new Component[5];
    //noinspection unchecked
    chatFilterButtons = new Component[5];

    for (final ChatMessage.Channel channel : ChatMessage.Channel.values()) {
      final String channelName;
      if (channel == ChatMessage.Channel.LOBBY) {
        channelName = StringConstants.MU_CHAT_LOBBY;
      } else if (channel == ChatMessage.Channel.ROOM) {
        channelName = StringConstants.MU_CHAT_GAME;
      } else if (channel == ChatMessage.Channel.PRIVATE) {
        channelName = StringConstants.MU_CHAT_PRIVATE;
      } else {
        continue;
      }

      final int i = channel.ordinal();
      chatFilterButtons[i] = new Component<>(CHAT_FILTER_BUTTON);
      chatFilterChannelLabels[i] = new Component<>(UNSELECTED_LABEL, channelName);
      chatFilterIcons[i] = new Component<>(null);
      chatFilterIcons[i].verticalAlignment = Font.VerticalAlignment.MIDDLE;
      chatFilterLabels[i] = new Component<>(UNSELECTED_LABEL);
      chatFilterLabels[i].verticalAlignment = Font.VerticalAlignment.MIDDLE;
      chatFilterButtons[i].addChild(chatFilterChannelLabels[i]);
      chatFilterButtons[i].addChild(chatFilterIcons[i]);
      chatFilterButtons[i].addChild(chatFilterLabels[i]);
      chatFilterButtons[i].recursivelySet_H();
      _tgc.addChild(chatFilterButtons[i]);
      ++chatFilterButtonCount;
    }

    _a = new Component<>(CHAT_BUTTON);
    _a.label = StringConstants.REPORT_ABUSE;
    _tgc.addChild(_a);
    if (!JagexApplet.connectedAndLoggedIn() || JagexApplet.cannotChat) {
      DobToEnableChatForm.instance = new DobToEnableChatForm();
    }
  }

  private static void initializeRuleStrings() {
    ReportAbuseDialog.RULE_STRINGS = new String[22];
    ReportAbuseDialog.RULE_STRINGS[ 4] = StringConstants.RULE_0_5;
    ReportAbuseDialog.RULE_STRINGS[ 5] = StringConstants.RULE_0_2;
    ReportAbuseDialog.RULE_STRINGS[ 6] = StringConstants.RULE_0_0;
    ReportAbuseDialog.RULE_STRINGS[ 7] = StringConstants.RULE_0_3;
    ReportAbuseDialog.RULE_STRINGS[ 9] = StringConstants.RULE_0_1;
    ReportAbuseDialog.RULE_STRINGS[11] = StringConstants.RULE_2_2;
    ReportAbuseDialog.RULE_STRINGS[13] = StringConstants.RULE_2_0;
    ReportAbuseDialog.RULE_STRINGS[15] = StringConstants.RULE_0_4;
    ReportAbuseDialog.RULE_STRINGS[16] = StringConstants.RULE_1_0;
    ReportAbuseDialog.RULE_STRINGS[17] = StringConstants.RULE_1_1;
    ReportAbuseDialog.RULE_STRINGS[18] = StringConstants.RULE_1_2;
    ReportAbuseDialog.RULE_STRINGS[19] = StringConstants.RULE_1_3;
    ReportAbuseDialog.RULE_STRINGS[20] = StringConstants.RULE_1_4;
    ReportAbuseDialog.RULE_STRINGS[21] = StringConstants.RULE_2_1;
  }

  private static void a460bq(final ResourceLoader loader) {
    DISPLAY_NAME_CHANGED = SpriteResource.loadSprite(loader, "basic", "display_name_changed");
    FRIEND_LIST = new PlayerList(PLAYER_LIST, StringConstants.MU_LOBBY_FRIEND_ADD, StringConstants.MU_LOBBY_FRIEND_RM);
    IGNORE_LIST = new PlayerList(PLAYER_LIST, StringConstants.MU_LOBBY_NAME_ADD, StringConstants.MU_LOBBY_NAME_RM);
    FRIEND_LIST_PANEL = new Component<>(null);
    SERVER_INFO_LABEL = new Component<>(LABEL);
    SERVER_INFO_LABEL.textAlignment = Font.HorizontalAlignment.CENTER;
    NAME_LABEL_2 = new Component<>(COLUMN_HEADING_MEDIUM_LEFT, StringConstants.MU_LOBBY_NAME);
    LOCATION_LABEL = new Component<>(COLUMN_HEADING_MEDIUM_RIGHT, StringConstants.MU_LOBBY_LOCATION);
    FRIEND_LIST_PANEL.addChild(SERVER_INFO_LABEL);
    FRIEND_LIST_PANEL.addChild(NAME_LABEL_2);
    FRIEND_LIST_PANEL.addChild(LOCATION_LABEL);
    FRIEND_LIST_PANEL.addChild(FRIEND_LIST);
    FRIEND_LIST_PANEL.addChild(new Component<>(UNSELECTED_LABEL));

    FRIEND_LIST.scrollPane.viewport.copyAttributesFrom(LABEL);
    FRIEND_LIST.scrollPane.viewport.verticalAlignment = Font.VerticalAlignment.MIDDLE;
    FRIEND_LIST.scrollPane.viewport.textAlignment = Font.HorizontalAlignment.CENTER;
    IGNORE_LIST.scrollPane.viewport.copyAttributesFrom(LABEL);
    IGNORE_LIST.scrollPane.viewport.verticalAlignment = Font.VerticalAlignment.MIDDLE;
    IGNORE_LIST.scrollPane.viewport.textAlignment = Font.HorizontalAlignment.CENTER;
  }

  private static Component<?> a658rd(final int var0, final int var1, final int var2, final int var3, final int var4, final int var6, final int var7, final Font var8, final int var9, final int var10, final int var11, final int var12) {
    final Component<?> var13 = new Component<>(null);
    var13.nineSliceSprites = a130cr(var7, var3);
    var13.mouseOverNineSliceSprites = a130cr(var12, var11);
    var13.mouseHeldNineSliceSprites = a130cr(var10, var6);
    var13.activeNineSliceSprites = a130cr(var1, var4);
    var13.disabledNineSliceSprites = a130cr(var0, var2);
    var13.font = var8;
    var13.textColor = var9;
    return var13;
  }

  private static Sprite[] a130cr(final int var0, final int var3) {
    final Sprite[] var4 = new Sprite[9];
    var4[0] = funorb.commonui.Component.createSolidSquareSprite(1, var3);

    for (int var5 = 1; var5 < 9; ++var5) {
      var4[var5] = var4[0];
    }

    var4[4] = funorb.commonui.Component.createSolidSquareSprite(64, var0);
    return var4;
  }

  private static void a487te() {
    ShatteredPlansClient.a150bq();
    a669hr(CHAT_FILTER_SPRITES[0].offsetX, ShatteredPlansClient._tga, _rcl);
  }

  private static void a857ke(final QuickChatCategory var1) {
    var1._s = new char[]{'?'};
    var1._x = new int[]{-1};
  }

  private static Sprite[] createColumnHeadingSprites(final int height, final int color2, final int color1, final boolean roundTopLeft, final boolean roundTopRight) {
    final int[] oldBuffer = Drawing.screenBuffer;
    final int oldWidth = Drawing.width;
    final int oldHeight = Drawing.height;
    final Sprite center = new Sprite(16, height);
    center.installForDrawing();
    Drawing.fillRectangleVerticalGradient(0, 0, 16, height, color1, color2);
    final Sprite left;
    if (roundTopLeft) {
      left = center.copy();
      left.installForDrawing();
      Drawing.horizontalLine(0, 0, 5, 0);
      Drawing.horizontalLine(0, 1, 3, 0);
      Drawing.horizontalLine(0, 2, 2, 0);
      Drawing.horizontalLine(0, 3, 1, 0);
      Drawing.horizontalLine(0, 4, 1, 0);
    } else {
      left = null;
    }

    final Sprite right;
    if (roundTopRight) {
      right = center.copy();
      right.installForDrawing();
      Drawing.horizontalLine(11, 0, 5, 0);
      Drawing.horizontalLine(13, 1, 3, 0);
      Drawing.horizontalLine(14, 2, 2, 0);
      Drawing.horizontalLine(15, 3, 1, 0);
      Drawing.horizontalLine(15, 4, 1, 0);
    } else {
      right = null;
    }

    Drawing.initialize(oldBuffer, oldWidth, oldHeight);
    return new Sprite[]{null, null, null, left, center, right, null, null, null};
  }

  public static void initializeLobbyBrowserComponents() {
    lobbyBrowserPlayerList = new Component<>(null);
    lobbyBrowserPlayerList.addChild(YOUR_RATING_LABEL);
    lobbyBrowserPlayerList.addChild(INVITE_PLAYER_LIST_PANEL);
    lobbyBrowserTabbedPlayerList = new TabbedPlayerListWrapper(StringConstants.MU_CHAT_LOBBY, lobbyBrowserPlayerList);
    lobbyBrowserLeftPanel = new Component<>(null);
    lobbyBrowserLeftPanel.addChild(lobbyBrowserTabbedPlayerList.view);
    lobbyBrowserLeftPanel.addChild(RETURN_TO_MAIN_MENU_BUTTON);
    layoutLobbyBrowser();
  }

  public static void layoutLobbyBrowser() {
    lastLayoutHeight = Drawing.height;
    lastLayoutWidth = Drawing.width;
    layoutLobbyBrowserPanels();
    lobbyBrowserTabbedPlayerList.updateBounds(lobbyBrowserLeftPanel.width, lobbyBrowserLeftPanel.height - 42);
    YOUR_RATING_LABEL.setBounds(0, 0, lobbyBrowserPlayerList.width, LABEL_HEIGHT);
    final int var1 = LABEL_HEIGHT + 2;
    INVITE_PLAYER_LIST_PANEL.setBounds(0, var1, lobbyBrowserPlayerList.width, lobbyBrowserPlayerList.height - (var1));
    ShatteredPlansClient.d150p();
    RETURN_TO_MAIN_MENU_BUTTON.setBounds(0, lobbyBrowserLeftPanel.height - 40, lobbyBrowserLeftPanel.width, 40);
    ALL_GAMES_HEADING.setBounds(0, 0, LOBBY_RIGHT_PANEL.width, 30);
    GAME_LIST_PANEL.setBounds(0, 30, LOBBY_RIGHT_PANEL.width, LOBBY_RIGHT_PANEL.height - 40 - 32);
    GAME_LIST_HEADING_STATUS.setBounds(5, 5, 68, 30);
    GAME_LIST_HEADING_OWNER.setBounds(75, 5, 78, 30);
    GAME_LIST_HEADING_PLAYERS.setBounds(155, 5, 48, 30);
    GAME_LIST_HEADING_AVG_RATING.setBounds(205, 5, 48, 30);
    GAME_LIST_HEADING_OPTIONS.setBounds(255, 5, 113, 30);
    GAME_LIST_HEADING_ELAPSED_TIME.setBounds(370, 5, LOBBY_RIGHT_PANEL.width - 5 - 370, 30);
    //noinspection SuspiciousNameCombination
    GAME_LIST_SCROLL_PANE.setBounds(5, 37, GAME_LIST_PANEL.width - 5 - 5, GAME_LIST_PANEL.height - 32 - 5 - 5, LABEL_HEIGHT);

    final int var3 = (LOBBY_RIGHT_PANEL.width + 2) / 2;
    PLAY_RATED_GAME_BUTTON.setBounds(0, LOBBY_RIGHT_PANEL.height - 40, var3 - 2, 40);
    CREATE_UNRATED_GAME_BUTTON.setBounds(var3, LOBBY_RIGHT_PANEL.height - 40, LOBBY_RIGHT_PANEL.width - var3, 40);
  }

  public static void layoutLobbyBrowserPanels() {
    final int var1 = (lastLayoutWidth - ShatteredPlansClient.SCREEN_WIDTH) / 2;
    final int var2 = 400;
    final int var3 = var2 - ShatteredPlansClient.lobbyBrowserTransitionCounter * ShatteredPlansClient.lobbyBrowserTransitionCounter;
    lobbyBrowserLeftPanel.setBounds(var1 - (199 * var3 / var2), 90, 199, Drawing.height - 90 - 124);
    LOBBY_RIGHT_PANEL.setBounds(var1 + 202 + var3 * 438 / var2, 0, 438, Drawing.height - 124);
  }

  private static Sprite[] adjustForNineSlice(final Sprite[] sprites) {
    for (final Sprite sprite : sprites) {
      sprite.x = 0;
      sprite.y = 0;
      sprite.offsetX = sprite.width;
      sprite.offsetY = sprite.height;
    }

    return sprites;
  }

  private static QuickChatCategory a528hc() {
    try {
      int var1 = 0;
      while (true) {
        final QuickChatCategory var2 = _guiA.get(var1);
        if (var2._y) {
          return var2;
        }

        ++var1;
      }
    } catch (final IllegalArgumentException var3) {
      return null;
    }
  }

  private static Sprite[] centerOnly(final Sprite sprite) {
    final Sprite[] sprites = new Sprite[9];
    sprites[NineSliceSprite.CENTER] = sprite;
    return sprites;
  }

  private static vm_ a427gn(final QuickChatCategory var1, final Component<?> var2, final Component<?> var3, final Component<?> var4) {
    if (var1 == null) {
      return null;
    } else {
      final int var5 = var1._r == null ? 0 : var1._r.length;
      final int var6 = var1._x != null ? var1._x.length : 0;
      final int var7 = var5 + var6;
      final String[] var8 = new String[var7];
      final char[] var9 = new char[var7];
      final int[] var10 = new int[var7];
      final vm_[] var11 = new vm_[var7];
      int var12;
      if (var1._r != null) {
        for (var12 = 0; var12 < var1._r.length; ++var12) {
          final QuickChatCategory var13 = _guiA.get(var1._r[var12]);
          var8[var12] = var13.name;
          var9[var12] = var1._p[var12];
          var11[var12] = a427gn(var13, var2, var3, var4);
        }
      }

      if (var1._x != null) {
        var12 = var5;
        char var17 = '1';

        for (int var14 = 0; var14 < var1._x.length; ++var14) {
          final int var15 = var1._x[var14];
          if (var15 == -1) {
            var8[var12 + var14] = StringConstants.QUICK_CHAT_HELP;
            var9[var12 + var14] = var1._s[var14];
          } else {
            final QuickChatResponse var16 = JagexApplet._dhc.get(var15);
            var8[var14 + var12] = var16.joinStrings();
            var9[var12 + var14] = var1._s[var14];
            if (var9[var12 + var14] == 0) {
              var9[var14 + var12] = var17++;
            }

          }
          var10[var12 + var14] = var1._x[var14];
        }
      }

      return new vm_(var2, var4, var3, var11, var10, var8, var9);
    }
  }

  private static void a669hr(final int var0, final int var2, final int var4) {
    final int var5 = LABEL_HEIGHT + 2 + 485 + 8 + var2 + 8;
    _adc.setBounds(3, 3, var5 - 6, _tgc.height - 6);
    int var6 = _adc.height - 5;
    _taio.setBounds(5, -LABEL_HEIGHT + var6, 487 + var2 + LABEL_HEIGHT, LABEL_HEIGHT);
    ShatteredPlansClient.chatMessageLabel.setBounds(var2, 0, -_cgC.width - var2 + _taio.width, LABEL_HEIGHT);
    var6 -= LABEL_HEIGHT + 2;
    _cgC.setBounds(var2 + ShatteredPlansClient.chatMessageLabel.width, 0, _cgC.width, LABEL_HEIGHT);
    //noinspection SuspiciousNameCombination
    _jiI.setBounds(5, 5, 485 + var2 - (-17), var6 - 5, LABEL_HEIGHT);
    if (DobToEnableChatForm.instance != null) {
      DobToEnableChatForm.instance.setBounds(_jiI.x, _jiI.y, _jiI.width, _jiI.height);
    }

    final int var7 = -var5 - var2 + _tgc.width;
    final int var8 = var7 / 2;
    final int var9 = var2 + var0 + var8;
    int var10 = 0;

    for (int var11 = 0; var11 < 6; ++var11) {
      if (var11 == 5 || chatFilterButtons[var11] != null) {
        final int var12 = (_tgc.height - 4) * var10 / (chatFilterButtonCount + 1) + 3;
        ++var10;
        var6 = -var12 + var10 * (_tgc.height - 4) / (1 + chatFilterButtonCount) + 3 - 2;
        if (var11 < 5) {
          chatFilterButtons[var11].setBounds(var5, var12, var7, var6);
          chatFilterChannelLabels[var11].setBounds(var2, 0, var8 - var2, var6);
          chatFilterIcons[var11].setBounds(var8, var4, var0, -var4 + var6 - var4);
          chatFilterLabels[var11].setBounds(var9, var4, var7 - var2 - var9, var6 - var4 - var4);
        } else {
          _a.setBounds(var5, var12, var7, var6);
        }
      }
    }

  }

  public static Sprite[] createGradientOutlineSprites(final int height, final int topColor, final int bottomColor, final int centerColor) {
    final int[] oldBuffer = Drawing.screenBuffer;
    final int oldWidth = Drawing.width;
    final int oldHeight = Drawing.height;

    final Sprite sides = new Sprite(3, height - 6);
    sides.installForDrawing();
    Drawing.fillRectangleVerticalGradient(0, 0, 3, height - 6, topColor, bottomColor);

    final Sprite topCorner = new Sprite(3, 3);
    topCorner.installForDrawing();
    Drawing.fillRect(0, 0, 3, 3, topColor);

    final Sprite top = new Sprite(16, 3);
    top.installForDrawing();
    Drawing.fillRect(0, 0, 16, 3, topColor);

    final Sprite bottomCorner = new Sprite(3, 3);
    bottomCorner.installForDrawing();
    Drawing.fillRect(0, 0, 3, 3, bottomColor);

    final Sprite bottom = new Sprite(16, 3);
    bottom.installForDrawing();
    Drawing.fillRect(0, 0, 16, 3, bottomColor);
    Sprite center = null;

    if (centerColor > 0) {
      center = new Sprite(16, 16);
      center.installForDrawing();
      Drawing.fillRect(0, 0, 16, 16, centerColor);
    }

    Drawing.initialize(oldBuffer, oldWidth, oldHeight);
    return new Sprite[]{topCorner, top, topCorner, sides, center, sides, bottomCorner, bottom, bottomCorner};
  }

  protected final int a353(final int var1) {
    int var3 = 0;
    int var4;
    if (this.label != null && this.font != null) {
      var4 = this.font.measureParagraphWidth(this.label, var1);
      if (var4 > var3) {
        var3 = var4;
      }
    }

    if (this.sprite != null) {
      var4 = this.sprite.offsetX;
      if (var4 > var3) {
        var3 = var4;
      }
    }

    if (this.children != null) {
      for (final Component<?> var6 : this.children) {
        final int var5 = var6.x + var6.width;
        if (var3 < var5) {
          var3 = var5;
        }
      }
    }

    return var3;
  }

  public final void addChild(final T child) {
    if (this.children == null) {
      this.children = new ArrayList<>();
    }
    assert !this.children.contains(child);
    this.children.add(child);
  }

  public final void placeAfter(final Component<?> prev, final Component<?> next) {
    if (prev == null) {
      next._w = 0;
      next.y = 0;
    } else {
      next.y = prev.height + prev.y + 2;
      next._w = prev._w + prev._gb;
    }
  }

  public final void b540(final boolean var1) {
    this.a360(true, false, 0, 0, false, Drawing.height, false, var1, Drawing.width);
  }

  protected final void a811(final int var1, final boolean var3) {
    int var5 = -this.y;
    int var6 = var5 - this._w;
    final int var7 = this.height;
    int var8 = var7 + this._gb;
    int var9;
    if (!var3) {
      var9 = 0;

      for (final Component<?> var10 : this.children) {
        var10._w = -var10.y + var9;
        var9 += var10.height + var10._gb + 2;
      }

      var8 = -2 + var9;
    }

    var9 = var8 - var1;
    if (var6 > var9) {
      var6 = var9;
    }

    if (var6 < 0) {
      var6 = 0;
    }

    final int var11 = -var1 + var7;
    if (var5 > var11) {
      var5 = var11;
    }

    if (var5 < 0) {
      var5 = 0;
    }

    this._w = var5 - var6;
    this.y = -var5;
    this._gb = -var7 + var8;
  }

  public final void recursivelySet_H() {
    if (this.children != null) {
      for (final Component<?> child : this.children) {
        child._H = true;
        child.recursivelySet_H();
      }
    }
  }

  private void processMouseEvents(final int var1, final int var2, final int var3, final int var6) {
    final int var7;
    if (this._cb > 0) {
      var7 = -(-this._cb >> 2);
    } else {
      var7 = this._cb >> 2;
    }
    this._cb -= var7;
    this.x += var7;

    final int var100;
    if (this._w <= 0) {
      var100 = this._w >> 2;
    } else {
      var100 = -(-this._w >> 2);
    }
    this.y += var100;
    this._w -= var100;

    final int var101;
    if (this._S <= 0) {
      var101 = this._S >> 2;
    } else {
      var101 = -(-this._S >> 2);
    }
    this.width += var101;
    this._S -= var101;

    final int var102;
    if (this._gb <= 0) {
      var102 = this._gb >> 2;
    } else {
      var102 = -(-this._gb >> 2);
    }
    this.height += var102;
    this._gb -= var102;

    this.x2 = this.x + var1;
    this.y2 = this.y + var2;
    final int var9 = Drawing.left;
    final int var10 = Drawing.top;
    final int var11 = Drawing.right;
    final int var12 = Drawing.bottom;
    Drawing.expandBoundsToInclude(this.x2, this.y2, this.x2 + this.width, this.y2 + this.height);
    boolean clicked = false;
    if (!clickConsumed && this.enabled
        && JagexApplet.mouseButtonJustClicked != MouseState.Button.NONE
        && Drawing.left <= JagexApplet.mousePressX && Drawing.right > JagexApplet.mousePressX
        && JagexApplet.mousePressY >= Drawing.top && JagexApplet.mousePressY < Drawing.bottom) {
      clicked = true;
      this.relativeClickX = JagexApplet.mousePressX - this.x2;
      this.relativeClickY = JagexApplet.mousePressY - this.y2;
      this.clickButton = JagexApplet.mouseButtonJustClicked;
      this.dragButton = JagexApplet.mouseButtonJustClicked;
    } else {
      if (JagexApplet.mouseButtonJustClicked != MouseState.Button.NONE) {
        this.dragButton = MouseState.Button.NONE;
      }
      this.clickButton = MouseState.Button.NONE;
    }

    if (JagexApplet.mouseButtonDown == MouseState.Button.NONE) {
      this.dragButton = MouseState.Button.NONE;
    }

    this.isMouseOver = !mouseOverConsumed && Drawing.left <= JagexApplet.mouseX && Drawing.right > JagexApplet.mouseX && Drawing.top <= JagexApplet.mouseY && Drawing.bottom > JagexApplet.mouseY;
    this.isMouseOverTarget = this.isMouseOver && this.dragButton == JagexApplet.mouseButtonDown && this.enabled;
    if (this.children != null) {
      for (final ListIterator<T> it = this.children.listIterator(this.children.size()); it.hasPrevious(); ) {
        final Component<?> child = it.previous();
        child.processMouseEvents(this.x2, this.y2, this.width, this.height);
      }
    }

    if (this.isMouseOver) {
      mouseOverConsumed = true;
    }
    if (clicked) {
      clickConsumed = true;
    }

    Drawing.setBounds(var9, var10, var11, var12);
    if (this.isDraggable && this.dragButton != MouseState.Button.NONE) {
      this.y = JagexApplet.mouseY - this.relativeClickY - var2;
      this.x = JagexApplet.mouseX - this.relativeClickX - var1;
      if (this.x < 0) {
        this.x = 0;
      }

      if (this.x > var3 - this.width) {
        this.x = var3 - this.width;
      }

      if (this.y < 0) {
        this.y = 0;
      }

      this._cb = 0;
      if (this.y > var6 - this.height) {
        this.y = var6 - this.height;
      }

      this.x2 = this.x + var1;
      this._w = 0;
      this.y2 = this.y + var2;
    }
  }

  private void copyAttributesFrom(final Component<?> var1) {
    if (var1 != null) {
      if (var1.label != null) {
        this.label = var1.label;
      }

      if (var1._r) {
        this._r = true;
      }

      if (var1._Z != null) {
        this._Z = var1._Z;
      }

      if (!var1.enabled) {
        this.enabled = false;
      }

      if (var1.selected) {
        this.selected = true;
      }

      if (var1._kb != 0) {
        this._kb = var1._kb;
      }

      if (var1.mouseOverNineSliceSprites != null) {
        this.mouseOverNineSliceSprites = var1.mouseOverNineSliceSprites;
      }

      if (var1.disabledNineSliceSprites != null) {
        this.disabledNineSliceSprites = var1.disabledNineSliceSprites;
      }

      if (var1._H) {
        this._H = true;
      }

      if (var1._I != 256) {
        this._I = var1._I;
      }

      if (var1._qb >= 0) {
        this._qb = var1._qb;
      }

      if (var1.mouseOverTextColor >= 0) {
        this.mouseOverTextColor = var1.mouseOverTextColor;
      }

      if (var1._u != null) {
        this._u = var1._u;
      }

      if (var1.disabledSprite != null) {
        this.disabledSprite = var1.disabledSprite;
      }

      if (var1._W >= 0) {
        this._W = var1._W;
      }

      if (var1._ab >= 0) {
        this._ab = var1._ab;
      }

      if (var1.mouseHeldNineSliceSprites != null) {
        this.mouseHeldNineSliceSprites = var1.mouseHeldNineSliceSprites;
      }

      if (var1.nineSliceSprites != null) {
        this.nineSliceSprites = var1.nineSliceSprites;
      }

      if (var1.sprite != null) {
        this.sprite = var1.sprite;
      }

      if (var1.mouseOverSprite != null) {
        this.mouseOverSprite = var1.mouseOverSprite;
      }

      if (var1._Y != 0) {
        this._Y = var1._Y;
      }

      if (var1.mouseHeldSprite != null) {
        this.mouseHeldSprite = var1.mouseHeldSprite;
      }

      if (var1.textColor != 0) {
        this.textColor = var1.textColor;
      }

      if (var1.font != null) {
        this.font = var1.font;
      }

      if (var1.activeNineSliceSprites != null) {
        this.activeNineSliceSprites = var1.activeNineSliceSprites;
      }

      if (var1._V != Integer.MIN_VALUE) {
        this._V = var1._V;
      }

      if (var1._T != Integer.MIN_VALUE) {
        this._T = var1._T;
      }

      if (var1.textAlignment != Font.HorizontalAlignment.LEFT) {
        this.textAlignment = var1.textAlignment;
      }

      if (var1._t != Integer.MIN_VALUE) {
        this._t = var1._t;
      }

      if (var1._tb != 0) {
        this._tb = var1._tb;
      }

      if (var1.isDraggable) {
        this.isDraggable = true;
      }

      if (var1.verticalAlignment != Font.VerticalAlignment.TOP) {
        this.verticalAlignment = var1.verticalAlignment;
      }

      if (var1._J != Integer.MIN_VALUE) {
        this._J = var1._J;
      }
    }
  }

  public final void setBounds(final int x, final int y, final int width, final int height) {
    this.x = x;
    this.y = y;
    this.width = width;
    this.height = height;
    this._gb = 0;
    this._cb = 0;
    this._w = 0;
    this._S = 0;
  }

  public final int e474() {
    return this.a353(Integer.MAX_VALUE);
  }

  public final void rootProcessMouseEvents(final boolean mouseNotYetHandled) {
    clickConsumed = !mouseNotYetHandled;
    mouseOverConsumed = !mouseNotYetHandled;
    this.processMouseEvents(0, 0, Drawing.width, Drawing.height);
  }

  private void a360(boolean var2, boolean dragging, final int var4, final int var5, boolean selected, final int var8, boolean isMouseOver, final boolean var10, final int var11) {
    this.x2 = this.x + var4;
    this.y2 = this.y + var5;
    final int var12 = Drawing.left;
    final int var13 = Drawing.top;
    final int var14 = Drawing.right;
    final int var15 = Drawing.bottom;
    Drawing.expandBoundsToInclude(this.x2, this.y2, this.x2 + this.width, this.height + this.y2);
    var2 &= this.enabled;

    if (!this._H) {
      selected = this.selected;
      isMouseOver = this.isMouseOverTarget;
      dragging = this.dragButton != MouseState.Button.NONE;
    }

    Sprite[] var16 = this.nineSliceSprites;
    Sprite var17 = this.sprite;
    int color = this.textColor;
    int var19 = 0;
    if (!var2) {
      if (this.disabledSprite != null) {
        var17 = this.disabledSprite;
      }

      if (this._W >= 0) {
        color = this._W;
      }

      if (this.disabledNineSliceSprites != null) {
        var16 = this.disabledNineSliceSprites;
      }
    }

    int var20 = 0;
    if (isMouseOver) {
      if (this.mouseOverSprite != null) {
        var17 = this.mouseOverSprite;
      }
      if (this.mouseOverNineSliceSprites != null) {
        var16 = this.mouseOverNineSliceSprites;
      }
      if (this.mouseOverTextColor >= 0) {
        color = this.mouseOverTextColor;
      }
    }

    if (dragging) {
      if (this._qb >= 0) {
        color = this._qb;
      }

      if (this.mouseHeldNineSliceSprites != null) {
        var16 = this.mouseHeldNineSliceSprites;
      }

      if (this.mouseHeldSprite != null) {
        var17 = this.mouseHeldSprite;
      }

      if (this._t != Integer.MIN_VALUE) {
        var19 = this._t;
      }

      if (this._J != Integer.MIN_VALUE) {
        var20 = this._J;
      }
    }

    if (selected) {
      if (this._V != Integer.MIN_VALUE) {
        var19 = this._V;
      }

      if (this._T != Integer.MIN_VALUE) {
        var20 = this._T;
      }

      if (this.activeNineSliceSprites != null) {
        var16 = this.activeNineSliceSprites;
      }

      if (this._Z != null) {
        var17 = this._Z;
      }

      if (this._ab >= 0) {
        color = this._ab;
      }
    }

    final int var21 = var19;
    int var22 = this._tb + var20;
    if (this._r) {
      NineSliceSprite.draw(var16, var4, var5, var11, var8);
    } else {
      NineSliceSprite.draw(var16, this.x2, this.y2, this.width, this.height);
    }

    if (var17 != null) {
      int var23 = this.x2 + var21;
      if (this.textAlignment == Font.HorizontalAlignment.CENTER) {
        var23 += (this.width - var17.offsetX) / 2;
      }

      int var24 = var22 + this.y2;
      if (this.verticalAlignment == Font.VerticalAlignment.MIDDLE) {
        var24 += (this.height - var17.offsetY) / 2;
      }

      if (this.textAlignment == Font.HorizontalAlignment.RIGHT) {
        var23 += this.width - var17.offsetX;
      }

      if (this.verticalAlignment == Font.VerticalAlignment.BOTTOM) {
        var24 += this.height - var17.offsetY;
      }

      var17.draw(var23, var24);
    }

    if (this.label != null && this.font != null) {
      String var25 = this.label;
      if (var10 && this._u != null) {
        var25 = var25 + this._u;
      }

      if (this.font.measureLineWidth(var25) <= -(2 * this._kb) + this.width && !var25.contains("<br>")) {
        if (this.verticalAlignment == Font.VerticalAlignment.MIDDLE) {
          var22 += (-this.font.descent + this.height - this.font.ascent) / 2;
        } else if (this.verticalAlignment == Font.VerticalAlignment.BOTTOM) {
          var22 += -this.font.ascent - this.font.descent + this.height;
        }

        if (this.textAlignment == Font.HorizontalAlignment.LEFT) {
          this.font.draw(var25, var21 + this.x2 + this._kb, this.font.ascent + var22 + this.y2, color, this._I);
        } else if (this.textAlignment == Font.HorizontalAlignment.CENTER) {
          this.font.drawCentered(var25, var21 + this.x2 + this._kb + (this.width - this._kb * 2) / 2, this.y2 + var22 + this.font.ascent, color, this._I);
        } else if (this.textAlignment == Font.HorizontalAlignment.RIGHT) {
          this.font.drawRightAligned(var25, this.width - (this._kb * 2) + var21 + this.x2 + this._kb, var22 + this.y2 + this.font.ascent, color, this._I);
        } else {
          this.font.drawParagraph(var25, this._kb + this.x2 + var21, var22 + this.y2, this.width - 2 * this._kb, this.height, color, this._I, this.textAlignment, this.verticalAlignment, this._Y);
        }
      } else {
        this.font.drawParagraph(var25, var21 + this._kb + this.x2, this.y2 + var22, this.width - 2 * this._kb, this.height, color, this._I, this.textAlignment, this.verticalAlignment, this._Y);
      }
    }

    if (this.children != null) {
      for (final Component<?> var26 : this.children) {
        var26.a360(var2, dragging, this.x2 + var19, var20 + this.y2, selected, this.height, isMouseOver, var10, this.width);
      }
    }

    Drawing.setBounds(var12, var13, var14, var15);
  }

}
