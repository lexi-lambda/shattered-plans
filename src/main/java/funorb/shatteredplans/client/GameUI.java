package funorb.shatteredplans.client;

import funorb.Strings;
import funorb.awt.KeyState;
import funorb.awt.MouseState;
import funorb.client.JagexBaseApplet;
import funorb.client.lobby.Component;
import funorb.client.lobby.ScrollPane;
import funorb.graphics.ArgbSprite;
import funorb.graphics.Drawing;
import funorb.graphics.Font;
import funorb.graphics.Rect;
import funorb.graphics.Sprite;
import funorb.shatteredplans.StringConstants;
import funorb.shatteredplans.client.game.AbstractGameView;
import funorb.shatteredplans.client.game.AbstractGameView.SystemHighlight;
import funorb.shatteredplans.client.game.ClientGameSession;
import funorb.shatteredplans.client.game.GameView;
import funorb.shatteredplans.client.game.PlayerStats;
import funorb.shatteredplans.client.game.TutorialState;
import funorb.shatteredplans.client.ui.Button;
import funorb.shatteredplans.client.ui.ChatMessage;
import funorb.shatteredplans.client.ui.DiplomacyPanelState;
import funorb.shatteredplans.client.ui.FixedPanel;
import funorb.shatteredplans.client.ui.FloatingPanel;
import funorb.shatteredplans.client.ui.Icon;
import funorb.shatteredplans.client.ui.Label;
import funorb.shatteredplans.client.ui.MultilineLabel;
import funorb.shatteredplans.client.ui.PanelState;
import funorb.shatteredplans.client.ui.ProductionPanelState;
import funorb.shatteredplans.client.ui.ProjectsPanelState;
import funorb.shatteredplans.client.ui.RoundedRect;
import funorb.shatteredplans.client.ui.ScrollBar;
import funorb.shatteredplans.client.ui.ScrollView;
import funorb.shatteredplans.client.ui.StatusPanelState;
import funorb.shatteredplans.client.ui.UIComponent;
import funorb.shatteredplans.client.ui.fe_;
import funorb.shatteredplans.client.ui.kb_;
import funorb.shatteredplans.client.ui.uc_;
import funorb.shatteredplans.game.ContiguousForce;
import funorb.shatteredplans.game.Force;
import funorb.shatteredplans.game.GameSession;
import funorb.shatteredplans.game.GameState;
import funorb.shatteredplans.game.Player;
import funorb.shatteredplans.map.CaptureAndHoldVictoryChecker;
import funorb.shatteredplans.map.ConquestVictoryChecker;
import funorb.shatteredplans.map.DerelictsVictoryChecker;
import funorb.shatteredplans.map.PointsVictoryChecker;
import funorb.shatteredplans.map.StarSystem;
import funorb.shatteredplans.map.TutorialVictoryChecker;
import funorb.shatteredplans.map.VictoryChecker;
import funorb.util.MathUtil;
import org.intellij.lang.annotations.MagicConstant;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.ListIterator;
import java.util.Queue;
import java.util.stream.IntStream;

public final class GameUI {
  public static final int INFO_PANEL_CONTENT_WIDTH = 182;
  public static final int VICTORY_PANEL_ROW_HEIGHT = 56;
  private static final int STATS_SCREEN_HEIGHT = 360;
  private static final int STATS_SCREEN_Y = 80;
  public static final int[] SYSTEM_SCORE_ORB_GRADIENT = new int[]{-14671840, -12566464, -10461088, -8355712, -6250336, -4144960, -2039584, -1};
  public static final int[] PLAYER_COLORS_1 = new int[]{0x950e00, 0x00954d, 0x002a95, 0x820095, 0xa99700, 0x007b95, 0x7f3f3f, 0x3f7f3f, 0x3f3f7f};
  public static final int[] PLAYER_COLORS_2 = new int[]{0xff4444, 0x44ff44, 0x7777ff, 0x7f007f, 0xffff00, 0x00ffff, 0xffff00, 0x7fff7f, 0x7f7fff};
  public static final int[] PLAYER_COLORS_DARK = new int[]{0x1f0000, 0x001f00, 0x00001f, 0x1f001f, 0x1f1f00, 0x001f1f, 0x1f0f0f, 0x0f1f0f, 0x0f0f1f};
  private static final int STATS_SCREEN_OPEN_AMOUNT_MAX = 32;
  private static final int STATS_GRAPH_TURN_ADVANCE_ANIMATION_COUNTER_MAX = 32;
  private static final int STATS_GRAPH_MARGIN = 20;
  private static final int STATS_GRAPH_WIDTH = 320;
  private static final int STATS_GRAPH_HEIGHT = 220;
  private static final int _rga = 56;
  private static final int ADD_CHAT_MESSAGE_TIMER_MAX = 18;
  private static final int CHAT_PANEL_OPEN_AMOUNT_MAX = 20;
  private static final int CHAT_PANEL_OPEN_AMOUNT_MAX_SQ = CHAT_PANEL_OPEN_AMOUNT_MAX * CHAT_PANEL_OPEN_AMOUNT_MAX;
  public static final int CHAT_PANEL_HEIGHT = 120;
  private static final int TOOLTIP_HEIGHT = 13;
  private static final int PANEL_MARGIN = 5;
  private static final int STATUS_PANEL_HEIGHT = 45;
  private static final int DIPLOMACY_PANEL_WIDTH = 200;
  private static final int PROJECTS_PANEL_HEIGHT = 186;
  private static final int PROJECTS_PANEL_WIDTH = 150;
  public static int chatPanelOpenAmount;

  private static ArgbSprite _kbw;
  public static Font _ssb;
  private static Sprite _gsF;
  public static Sprite READY_BUTTON;
  public static int currentSettings;
  public static Sprite HUD_ICON_3;
  public static Sprite HUD_ICON_RED_4;
  public static Sprite HUD_ICON_RED_5;
  public static Sprite[] ANIM_ICONS;
  private static String[] BROKEN_LINES;
  public static int lastSavedSettings;
  private static Queue<ChatMessage> newChatMessages;
  public static Sprite FACTION_RING;
  public static Sprite FACTION_RING_CENTER;
  private static ChatMessage[] recentChatMessages;
  private static int addChatMessageTimer;
  public static boolean isChatOpen = true;
  public static Sprite[] PRODUCTION_ICONS;
  private static funorb.client.lobby.ChatMessage lastChatMessage;
  public static Sprite PRODUCTION_BUTTON_DOWN;
  public static Sprite HUD_ICON_RED_3;
  public static Sprite HUD_ICON_1;
  public static Sprite HUD_ICON_2;
  public static Sprite HUD_ICON_4;
  public static Sprite HUD_ICON_RED_1;
  public static Sprite HUD_ICON_RED_2;
  public static Sprite HUD_ICON_NO_BASE_1;
  public static Sprite HUD_ICON_NO_BASE_3;
  public static Sprite HUD_ICON_NO_BASE_4;
  public static Sprite HUD_ICON_NO_BASE_5;
  public static Sprite[] ICON_CIRCLES;
  public static Sprite PRODUCTION_BUTTON;
  public static Sprite[] PROJECT_ICONS;
  public static Sprite[] FACTION_ICONS;
  public static Sprite[] FACTION_ICONS_LARGE;
  public static Sprite READY_BUTTON_DOWN;
  public static Sprite SHIP;
  public static Sprite FACTION_RING_TAG;
  private static Sprite _fjr;
  public static Sprite HUD_ICON_5;
  public static Sprite HUD_ICON_NO_BASE_2;
  private static Sprite _ncd;
  public static Sprite _R;

  private final Button<StatsScreenTab> systemsTabButton;
  private final FixedPanel fleetsTabControl;
  private final FixedPanel overviewTabControl;
  private final FixedPanel showChatButtonPanel;
  private final Button<StatsScreenTab> overviewTabButton;
  private final FixedPanel systemsTabControl;
  private final FixedPanel productionTabControl;
  private final int[] _p = new int[]{32, 0, 0, 0};
  private final List<UIComponent<?>> statsScreenTabs;
  private final int[] _ob = new int[]{82, 52, 52, 52};
  public final FixedPanel animationControlsPanel;
  private final Button<?> showChatButton;
  private final Button<StatsScreenTab> productionTabButton;
  private final List<UIComponent<?>> components = new ArrayList<>();
  private final Button<StatsScreenTab> fleetsTabButton;
  private final ArgbSprite warningSprite;
  public Button<?> animationPlayingButton;
  public Button<?> animationSpeedDoubledButton;

  private int mouseDownX;
  private int mouseDownY;
  private int dragOriginX;
  private int dragOriginY;
  private int mouseDownTicks;
  private UIComponent<?> hoveredComponent;
  private UIComponent<?> mouseDownComponent;
  private UIComponent<?> draggedComponent;
  private UIComponent<?> clickedComponent;

  private FloatingPanel<?> victoryPanel;
  private Button<?> projectsButton;
  private int statsScreenOpenAmount;
  private Button<?> victoryButton;
  private Button<?> endTurnButton;
  private FloatingPanel<ProjectsPanelState> projectsPanel;
  private String turnName;
  private int _x;
  public Button<?> animationAutoPlayButton;
  private FixedPanel statusPanel;
  private String[] playerDiplomacyStatusMessage;
  private StatsScreenTab currentStatsScreenTab;
  private ScrollPane<Component<?>> chatScrollPaneSinglePlayer;
  private int[][] _G;
  private int newTurnPanelOpenAmount;
  private int[][] _q;
  private boolean isStatsScreenOpen;
  private boolean _I = true;
  private FloatingPanel<DiplomacyPanelState> diplomacyPanel;
  private Component<Component<?>> chatPanelSinglePlayer;
  private int _w;
  private FloatingPanel<?> fleetInfoPanel;
  private Sprite _T;
  private boolean _v = true;
  private FloatingPanel<ProductionPanelState> productionPanel;
  private String[][] _l;
  /**
   * The value we’re current animating {@link #statsGraphData} to.
   */
  private int[][] targetStatsGraphData;
  private String currentStatDescriptionTooltip;
  private int _U = -1;
  private int statsGraphTurnCount;
  private int _jb;
  private int[][] statsGraphData;
  private boolean _ib;
  private Button<?> productionButton;
  private Sprite _eb;
  private boolean[] _z;
  private int statsGraphStartTurn;
  private ClientGameSession gameSession;
  private int statsGraphTurnAdvanceAnimationCounter;
  private Button<?> diplomacyButton;
  private Button<?> fleetInfoButton;
  private int statsGraphAlpha = 0;

  public GameUI(final ClientGameSession session) {
    _fjr = new Sprite(20 + PRODUCTION_BUTTON.offsetX, PRODUCTION_BUTTON.offsetY + 20);
    _kbw = new ArgbSprite(READY_BUTTON.offsetX + 20, READY_BUTTON.offsetY + 20);
    this.gameSession = session;

    final int readyButtonX = getReadyButtonX();
    if (this.gameSession.localPlayer != null) {
      this.endTurnButton = new Button<>(readyButtonX, 20, READY_BUTTON.width, READY_BUTTON.height, READY_BUTTON, null, 0, READY_BUTTON_DOWN, null, 0);
      this.endTurnButton.tooltip = StringConstants.TOOLTIP_END_TURN;
      this.addComponent(this.endTurnButton);
    }

    final int hudIconWidth = HUD_ICON_1.width;
    final int victoryButtonX = readyButtonX - 6 - hudIconWidth;
    if (this.victoryPanel != null) {
      this.victoryButton = new Button<>(victoryButtonX, 25, HUD_ICON_5.width, HUD_ICON_5.height, HUD_ICON_5, null, 0, HUD_ICON_RED_5, null, 0);
      this.victoryButton.tooltip = StringConstants.TOOLTIP_VICTORY_BUTTON_SHOW;
      this.addComponent(this.victoryButton);
    }
    this.productionButton = new Button<>(readyButtonX - 6 - (hudIconWidth / 2) - hudIconWidth, 5, HUD_ICON_2.width, HUD_ICON_2.height, HUD_ICON_2, null, 0, HUD_ICON_RED_2, null, 0);
    this.productionButton.tooltip = StringConstants.TOOLTIP_PRODUCTION_BUTTON_SHOW;
    this.addComponent(this.productionButton);
    this.fleetInfoButton = new Button<>(victoryButtonX - 2 - hudIconWidth, 25, HUD_ICON_4.width, HUD_ICON_4.height, HUD_ICON_4, null, 0, HUD_ICON_RED_4, null, 0);
    this.fleetInfoButton.tooltip = StringConstants.TOOLTIP_FLEET_INFO_BUTTON_SHOW;
    this.addComponent(this.fleetInfoButton);
    this.diplomacyButton = new Button<>(this.productionButton.x - 2 - hudIconWidth, 5, HUD_ICON_1.width, HUD_ICON_1.height, HUD_ICON_1, null, 0, HUD_ICON_RED_1, null, 0);
    this.diplomacyButton.tooltip = StringConstants.TOOLTIP_DIPLOMACY_BUTTON_SHOW;
    this.addComponent(this.diplomacyButton);
    final int projectsButtonX = this.fleetInfoButton.x - 2 - hudIconWidth;
    if (this.gameSession.localPlayer != null) {
      this.projectsButton = new Button<>(projectsButtonX, 25, HUD_ICON_3.width, HUD_ICON_3.height, HUD_ICON_3, null, 0, HUD_ICON_RED_3, null, 0);
      this.projectsButton.tooltip = StringConstants.TOOLTIP_PROJECTS_BUTTON_SHOW;
      this.addComponent(this.projectsButton);
    }

    this.statusPanel = createStatusPanel(projectsButtonX);
    this.addComponent(this.statusPanel);

    this.productionPanel = createProductionPanel();
    this.addComponent(this.productionPanel);
    if (this.gameSession.localPlayer != null) {
      this.projectsPanel = createProjectsPanel();
      this.addComponent(this.projectsPanel);
    }
    this.diplomacyPanel = createDiplomacyPanel(this.gameSession.gameState.playerCount);
    this.initialize();
    this.addComponent(this.diplomacyPanel);
    this.fleetInfoPanel = createFleetInfoPanel(this.gameSession.gameState.playerCount);
    this.addComponent(this.fleetInfoPanel);
    final int victoryPanelHeight = this.gameSession.gameState.victoryChecker.victoryPanelHeight();
    if (victoryPanelHeight > 0) {
      this.victoryPanel = createVictoryPanel(victoryPanelHeight);
      this.addComponent(this.victoryPanel);
    }

    this.animationControlsPanel = new FixedPanel(ShatteredPlansClient.SCREEN_WIDTH - 50, ShatteredPlansClient.SCREEN_HEIGHT - Menu.SMALL_FONT.ascent, 60, 2 * Menu.SMALL_FONT.ascent);
    this.addComponent(this.animationControlsPanel, 0);
    this.animationAutoPlayButton = new Button<>(this.animationControlsPanel.x + 5, ShatteredPlansClient.SCREEN_HEIGHT - ANIM_ICONS[3].height, ANIM_ICONS[3].width, ANIM_ICONS[3].height, ANIM_ICONS[3], null, 0, ANIM_ICONS[0], null, 0);
    if ((currentSettings & 0b100000) == 0) {
      this.animationAutoPlayButton.tooltip = StringConstants.TOOLTIP_ANIM_AUTO_PLAY_IS_OFF;
    } else {
      this.animationAutoPlayButton.toggle();
      this.animationAutoPlayButton.tooltip = StringConstants.TOOLTIP_ANIM_AUTO_PLAY_IS_ON;
    }

    this.animationPlayingButton = new Button<>(this.animationControlsPanel.x + 20, -ANIM_ICONS[1].height + ShatteredPlansClient.SCREEN_HEIGHT, ANIM_ICONS[1].width, ANIM_ICONS[1].height, ANIM_ICONS[1], null, 0, ANIM_ICONS[4], null, 0);
    this.animationPlayingButton.tooltip = StringConstants.TOOLTIP_ANIM_CLICK_TO_PLAY;
    this.animationSpeedDoubledButton = new Button<>(this.animationControlsPanel.x + 35, ShatteredPlansClient.SCREEN_HEIGHT - ANIM_ICONS[5].height, ANIM_ICONS[5].width, ANIM_ICONS[5].height, ANIM_ICONS[5], null, 0, ANIM_ICONS[2], null, 0);
    if ((currentSettings & 0b1000000) == 0) {
      this.animationSpeedDoubledButton.tooltip = StringConstants.TOOLTIP_ANIM_SPEED_IS_NORMAL;
    } else {
      this.animationSpeedDoubledButton.toggle();
      this.animationSpeedDoubledButton.tooltip = StringConstants.TOOLTIP_ANIM_SPEED_IS_DOUBLE;
    }

    this.animationControlsPanel.addChild(this.animationAutoPlayButton);
    this.animationControlsPanel.addChild(this.animationPlayingButton);
    this.animationControlsPanel.addChild(this.animationSpeedDoubledButton);
    this.showChatButtonPanel = new FixedPanel(3, ShatteredPlansClient.SCREEN_HEIGHT, 10 + Menu.SMALL_FONT.measureLineWidth(StringConstants.TEXT_SHOW_CHAT), 2 * Menu.SMALL_FONT.ascent);
    this.showChatButton = new Button<>(3, ShatteredPlansClient.SCREEN_HEIGHT, this.showChatButtonPanel.width, Menu.SMALL_FONT.ascent - 4, null, StringConstants.TEXT_SHOW_CHAT, Drawing.WHITE, null, StringConstants.TEXT_HIDE_CHAT, Drawing.WHITE);
    this.showChatButtonPanel.addChild(this.showChatButton);
    this.addComponent(this.showChatButtonPanel, 0);

    this.warningSprite = new ArgbSprite(GameView.WARNING.offsetX, GameView.WARNING.offsetY);
    this.warningSprite.withInstalledForDrawing(() -> {
      GameView.WARNING.drawTinted(0, 0, Drawing.RED);
      for (int i = 0; i < this.warningSprite.pixels.length; ++i) {
        if (this.warningSprite.pixels[i] != 0) {
          Drawing.addPixel(i % this.warningSprite.width, i / this.warningSprite.width, Drawing.RED, 128);
          this.warningSprite.pixels[i] |= 0xff000000;
        }
      }
    });

    if (!this.gameSession.isMultiplayer && !this.gameSession.isTutorial) {
      ShatteredPlansClient.clearChatMessages();
      final Component<Component<?>> var6 = new Component<>(null);
      var6.children = new ArrayList<>();
      this.chatScrollPaneSinglePlayer = new ScrollPane<>(var6, Component.LABEL_DARK_2, Component.SCROLL_BAR);
      final Component<Component<?>> var4 = new Component<>(Component.CHAT_MESSAGES_PANEL);
      this.chatPanelSinglePlayer = new Component<>(Component.CHAT_PANEL);
      this.chatPanelSinglePlayer.setBounds(0, ShatteredPlansClient.SCREEN_HEIGHT, ShatteredPlansClient.SCREEN_WIDTH, CHAT_PANEL_HEIGHT);
      var4.setBounds(3, 3, ShatteredPlansClient.SCREEN_WIDTH - 6, this.chatPanelSinglePlayer.height - 6);
      this.chatPanelSinglePlayer.addChild(var4);
      var4.addChild(this.chatScrollPaneSinglePlayer);
      final int var5 = var4.height - 5;
      this.chatScrollPaneSinglePlayer.setBounds(5, 5, ShatteredPlansClient.SCREEN_WIDTH - 16, var5 - 5, 15);
      isChatOpen = false;
    }

    a613cq(Menu.SMALL_FONT);
    this.playerDiplomacyStatusMessage = new String[this.gameSession.gameState.playerCount + 1];
    this._z = new boolean[this.gameSession.gameState.playerCount + 1];

    this.statsScreenTabs = new ArrayList<>();
    this.fleetsTabControl = new FixedPanel(0, 410, 128, 21);
    this.productionTabControl = new FixedPanel(0, 410, 128, 21);
    this.systemsTabControl = new FixedPanel(0, 410, 128, 21);
    this.overviewTabControl = new FixedPanel(0, 410, 128, 21);
    this.statsScreenTabs.add(this.fleetsTabControl);
    this.statsScreenTabs.add(this.productionTabControl);
    this.statsScreenTabs.add(this.systemsTabControl);
    this.statsScreenTabs.add(this.overviewTabControl);
    this.fleetsTabButton = new Button<>(0, 410, 128, 21, null, StringConstants.TEXT_SHIPS.toUpperCase(), Drawing.WHITE, null, StringConstants.TEXT_SHIPS.toUpperCase(), 0x2ad0d6);
    this.fleetsTabControl.addChild(this.fleetsTabButton);
    this.productionTabButton = new Button<>(0, 410, 128, 21, null, StringConstants.TEXT_PRODUCTION.toUpperCase(), Drawing.WHITE, null, StringConstants.TEXT_PRODUCTION.toUpperCase(), 0x2ad0d6);
    this.productionTabControl.addChild(this.productionTabButton);
    this.systemsTabButton = new Button<>(0, 410, 128, 21, null, StringConstants.TEXT_SYSTEMS.toUpperCase(), Drawing.WHITE, null, StringConstants.TEXT_SYSTEMS.toUpperCase(), 0x2ad0d6);
    this.systemsTabControl.addChild(this.systemsTabButton);
    this.overviewTabButton = new Button<>(0, 410, 128, 21, null, StringConstants.TEXT_OVERVIEW.toUpperCase(), Drawing.WHITE, null, StringConstants.TEXT_OVERVIEW.toUpperCase(), 0x2ad0d6);
    this.overviewTabControl.addChild(this.overviewTabButton);
    this.fleetsTabButton.data = StatsScreenTab.FLEETS;
    this.productionTabButton.data = StatsScreenTab.PRODUCTION;
    this.systemsTabButton.data = StatsScreenTab.SYSTEMS;
    this.overviewTabButton.data = StatsScreenTab.OVERVIEW;
    this.overviewTabButton.toggle();

    if (this.gameSession.isTutorial) {
      this.productionButton.visible = false;
      this.diplomacyButton.visible = false;
      this.fleetInfoButton.visible = false;
      this.projectsButton.visible = false;
      this.victoryButton.visible = false;
      this.endTurnButton.visible = false;
      this.animationControlsPanel.visible = false;
      this.showChatButtonPanel.visible = false;
      TutorialState.a018jr("continue", StringConstants.TUTORIAL_CONTINUE);
      TutorialState.a018jr("continue2", StringConstants.TUTORIAL_CONTINUE_2);
      TutorialState.a018jr("continue3", StringConstants.TUTORIAL_CONTINUE_3);
      TutorialState.a018jr("continuekey", StringConstants.TUTORIAL_CONTINUE_KEY);
      TutorialState.a018jr("captureAll", StringConstants.TUTORIAL_CAPTURE_ALL);
      TutorialState.a018jr("exit", StringConstants.TUTORIAL_EXIT);
      TutorialState.localPlayer = this.gameSession.localPlayer;

      for (int i = 0; i < this.gameSession.gameState.playerCount; ++i) {
        final Player var7 = this.gameSession.gameState.players[i];
        TutorialState.a018jr("playercol" + i, Strings.format("<col=<%0>>", Integer.toString(var7.color2, 16)));
        TutorialState.a018jr("player" + i, Strings.format("<col=<%0>><%1></col>", Integer.toString(var7.color2, 16), var7.name));
        TutorialState.a018jr("playerhome" + i, Strings.format("<col=<%0>><%1></col>", Integer.toString(var7.color2, 16), var7.combinedForce.getCapital().name));
      }

      TutorialState.a529lp(TutorialMessages.get("start"));
    }
  }

  private static int getReadyButtonX() {
    return ShatteredPlansClient.SCREEN_WIDTH - 12 - READY_BUTTON.width;
  }

  private void addComponent(final UIComponent<?> component) {
    assert !this.components.contains(component);
    this.components.add(component);
  }

  private void addComponent(final UIComponent<?> component, final int index) {
    assert !this.components.contains(component);
    this.components.add(index, component);
  }

  private static <T> ScrollView<T> createVictoryPanelRow(final GameState state,
                                                         final Player[] leaders,
                                                         final int targetPoints,
                                                         final int turnsUntilVictory,
                                                         final Player player,
                                                         final int points,
                                                         final int pointsPerTurn) {
    final ScrollView<T> row = new ScrollView<>(0, 0, INFO_PANEL_CONTENT_WIDTH, VICTORY_PANEL_ROW_HEIGHT);

    final boolean isLeader = Arrays.stream(leaders).anyMatch(leader -> player == leader);
    final Sprite icon = FACTION_ICONS[player.index];
    row.addChild(new RoundedRect(
        icon.width / 2,
        ((row.height / 2) - 4) - (Menu.SMALL_FONT.ascent / 2),
        row.width - (icon.width / 2),
        Menu.SMALL_FONT.ascent + 8,
        isLeader ? 0x808080 : 0x202020));

    if (state.isPlayerDefeated(player.index)) {
      row.addChild(new Label(
          icon.width,
          (row.height / 2) - (Menu.SMALL_FONT.ascent / 2),
          row.width - icon.width,
          Menu.SMALL_FONT.ascent,
          StringConstants.TEXT_DEFEATED.toUpperCase()));
    } else {
      row.addChild(new Icon(
          icon.width / 2,
          (row.height / 2) - (Menu.SMALL_FONT.ascent / 2) - 1,
          a774er(player, points, pointsPerTurn, targetPoints)));
    }

    if (isLeader && turnsUntilVictory > 0 && turnsUntilVictory <= 3) {
      row.addChild(new Label(icon.width, -(Menu.SMALL_FONT.ascent / 2) + row.height / 2, -icon.width + row.width, Menu.SMALL_FONT.ascent, Strings.format(StringConstants.VICTORY_IN_X, Integer.toString(turnsUntilVictory))));
    }

    row.addChild(new Icon(0, (row.height - icon.height) / 2, icon));
    if (!state.isPlayerDefeated(player.index)) {
      String tooltip;
      if (points == 1) {
        if (pointsPerTurn == 1) {
          tooltip = StringConstants.ONE_POINT_1_PER_TURN;
        } else {
          tooltip = Strings.format(StringConstants.TOOLTIP_VICTORY_PANE_ONE_MANY, Integer.toString(pointsPerTurn));
        }
      } else if (pointsPerTurn == 1) {
        tooltip = Strings.format(StringConstants.N_POINTS_1_PER_TURN, Integer.toString(points));
      } else {
        tooltip = Strings.format(StringConstants.TOOLTIP_VICTORY_PANE_MANY_MANY, Integer.toString(points), Integer.toString(pointsPerTurn));
      }

      if (isLeader && !state.hasEnded) {
        tooltip = tooltip + " " + StringConstants.TOOLTIP_PROJECTED_WINNER;
      }

      row.tooltip = tooltip;

      for (final UIComponent<?> var14 : row.children) {
        var14.tooltip = tooltip;
      }
    }

    return row;
  }

  private static Sprite a774er(final Player player, int points, int pointsPerTurn, final int targetPoints) {
    if (pointsPerTurn + points == 0) {
      return new Sprite(1, 1);
    }

    if (points > targetPoints) {
      points = targetPoints;
      pointsPerTurn = 0;
    }
    if (pointsPerTurn > targetPoints - points) {
      pointsPerTurn = targetPoints - points;
    }

    final int iconWidth = FACTION_ICONS[player.index].width;
    int var5 = (INFO_PANEL_CONTENT_WIDTH - iconWidth) * points / targetPoints;
    int var6 = (INFO_PANEL_CONTENT_WIDTH - iconWidth) * pointsPerTurn / targetPoints;
    final Sprite var7 = new Sprite(var5 + var6 + iconWidth / 2, 2 + Menu.SMALL_FONT.ascent);
    Drawing.saveContext();
    var7.installForDrawing();
    if (points > 0) {
      var5 += iconWidth / 2;

      for (int i = 0; i < var5; ++i) {
        Drawing.verticalLine(i, 0, var7.height, Drawing.alphaOver(player.color1, 0, 128 + i * 128 / var5));
      }

      if (pointsPerTurn > 0) {
        final int var8 = Drawing.alphaOver(player.color1, 0x202020, 128);

        for (int i = 0; i < var6; ++i) {
          Drawing.verticalLine(var5 + i, 0, var7.height, var8);
        }
      }
    } else {
      var6 += iconWidth / 2;
      final int var8 = Drawing.alphaOver(player.color1, 0x202020, 128);

      for (int i = 0; var6 > i; ++i) {
        Drawing.verticalLine(i, 0, var7.height, var8);
      }
    }

    for (int i = 0; i < var7.width - 1; ++i) {
      var7.pixels[i] = Drawing.alphaOver(var7.pixels[i], 0, 128);
      var7.pixels[(var7.height - 1) * var7.width + i] = Drawing.alphaOver(var7.pixels[i + (var7.height - 1) * var7.width], 0, 64);
    }

    for (int i = 0; i < var7.height; ++i) {
      var7.pixels[var7.width - 1 + i * var7.width] = Drawing.alphaOver(var7.pixels[i * var7.width - 1 + var7.width], 0, 128);
    }

    var7.pixels[var7.height * var7.width - 1] = Drawing.alphaOver(var7.pixels[var7.height * var7.width - 1], 0, 190);
    Drawing.restoreContext();
    return var7;
  }

  private void processMouseInput(final Collection<UIComponent<?>> components) {
    this.hoveredComponent = UIComponent.findMouseTarget(components, JagexApplet.mouseX, JagexApplet.mouseY);
    if (JagexApplet.mouseButtonJustClicked == MouseState.Button.LEFT) {
      this.mouseDownTicks = 0;
      this.mouseDownX = JagexApplet.mousePressX;
      this.mouseDownY = JagexApplet.mousePressY;
      this.mouseDownComponent = UIComponent.findMouseTarget(components, this.mouseDownX, this.mouseDownY);
    } else if (JagexApplet.mouseButtonDown == MouseState.Button.LEFT) {
      ++this.mouseDownTicks;
      if (this.draggedComponent != null) {
        this.draggedComponent.handleDrag(JagexApplet.mouseX, JagexApplet.mouseY, this.dragOriginX, this.dragOriginY);
        return;
      }

      final int dx = this.mouseDownX - JagexApplet.mouseX;
      final int dy = this.mouseDownY - JagexApplet.mouseY;
      if (this.mouseDownTicks > 5 && MathUtil.isEuclideanDistanceGreaterThan(dx, dy, 5) && this.mouseDownComponent != null) {
        this.draggedComponent = this.mouseDownComponent;
        this.dragOriginX = this.mouseDownComponent.x - this.mouseDownX;
        this.dragOriginY = this.mouseDownComponent.y - this.mouseDownY;
      }
    } else {
      if ((this.draggedComponent == null || this.mouseDownTicks < 5) && a865rr(this.mouseDownComponent, components)) {
        this.clickedComponent = this.mouseDownComponent;
        if (this.clickedComponent != null) {
          this.clickedComponent.handleClick(JagexApplet.mouseX - this.clickedComponent.x, JagexApplet.mouseY - this.clickedComponent.y);
        }
      } else {
        this.clickedComponent = null;
      }

      this.draggedComponent = null;
      this.mouseDownComponent = null;
    }
  }

  private static FloatingPanel<ProductionPanelState> createProductionPanel() {
    final FloatingPanel<ProductionPanelState> panel = new FloatingPanel<>(PANEL_MARGIN, STATUS_PANEL_HEIGHT + Menu.SMALL_FONT.ascent + 2 + PANEL_MARGIN * 3, 238, 300, StringConstants.TAB_NAME_PRODUCTION.toUpperCase());
    final fe_<FloatingPanel<ProductionPanelState>> var0 = new fe_<>(panel.x - 16 + panel.width, 2 + panel.y, 11, 11, -1, null, "X", Drawing.RED);
    var0.data = panel;
    final ScrollBar scrollBar = new ScrollBar(panel.width - 20, panel.y + 20, 11, panel.height - 28);
    panel.addChild(var0);
    panel.addChild(scrollBar);
    final ScrollView<?> var3 = new ScrollView<>(panel.x + 9, 20 + panel.y, 209, panel.height - 28);
    panel.addChild(var3);
    panel.content = var3;
    scrollBar.listener = var3;
    final ProductionPanelState var4 = new ProductionPanelState();
    panel.state = var4;
    var4.scrollBar = scrollBar;
    return panel;
  }

  private static FloatingPanel<ProjectsPanelState> createProjectsPanel() {
    final FloatingPanel<ProjectsPanelState> panel = new FloatingPanel<>(
        ShatteredPlansClient.SCREEN_WIDTH - PROJECTS_PANEL_WIDTH - PANEL_MARGIN,
        ShatteredPlansClient.SCREEN_HEIGHT - PROJECTS_PANEL_HEIGHT - 30,
        PROJECTS_PANEL_WIDTH,
        PROJECTS_PANEL_HEIGHT,
        StringConstants.TAB_NAME_PROJECTS.toUpperCase());
    final fe_<FloatingPanel<ProjectsPanelState>> var0 = new fe_<>(panel.x + panel.width - 16, panel.y + 2, 11, 11, -1, null, "X", Drawing.RED);
    var0.data = panel;
    panel.addChild(var0);
    final ScrollView<?> var1 = new ScrollView<>(9 + panel.x, 20 + panel.y, 132, 158);
    panel.addChild(var1);
    panel.content = var1;
    panel.state = new ProjectsPanelState();
    return panel;
  }

  private static FloatingPanel<DiplomacyPanelState> createDiplomacyPanel(final int playerCount) {
    final FloatingPanel<DiplomacyPanelState> panel = new FloatingPanel<>(
        ShatteredPlansClient.SCREEN_WIDTH - DIPLOMACY_PANEL_WIDTH - PANEL_MARGIN,
        STATUS_PANEL_HEIGHT + PANEL_MARGIN * 2,
        DIPLOMACY_PANEL_WIDTH,
        28 + _rga * playerCount,
        StringConstants.TAB_NAME_DIPLOMACY.toUpperCase());
    final fe_<FloatingPanel<DiplomacyPanelState>> var2 = new fe_<>(panel.x + 200 - 16, 2 + panel.y, 11, 11, -1, null, "X", Drawing.RED);
    var2.data = panel;
    panel.addChild(var2);
    final ScrollView<?> var3 = new ScrollView<>(panel.x + 9, panel.y + 20, INFO_PANEL_CONTENT_WIDTH, playerCount * _rga);
    panel.addChild(var3);
    panel.content = var3;
    panel.state = new DiplomacyPanelState(playerCount);
    return panel;
  }

  private static FloatingPanel<PanelState> createFleetInfoPanel(final int playerCount) {
    final int height = (SHIP.offsetX + 4) * playerCount + 28;
    final FloatingPanel<PanelState> panel = new FloatingPanel<>(PANEL_MARGIN, ShatteredPlansClient.SCREEN_HEIGHT - height - 30, 200, height, StringConstants.TAB_NAME_FLEET_INFO.toUpperCase());
    final fe_<FloatingPanel<PanelState>> var2 = new fe_<>(184 + panel.x, 2 + panel.y, 11, 11, -1, null, "X", Drawing.RED);
    var2.data = panel;
    panel.addChild(var2);
    final ScrollView<?> var3 = new ScrollView<>(9 + panel.x, 20 + panel.y, INFO_PANEL_CONTENT_WIDTH, (4 + SHIP.offsetX) * playerCount);
    panel.addChild(var3);
    panel.content = var3;
    return panel;
  }

  private static FloatingPanel<PanelState> createVictoryPanel(final int height) {
    final FloatingPanel<PanelState> panel = new FloatingPanel<>(320, 451 - height, 200, height + 28, StringConstants.TAB_NAME_VICTORY.toUpperCase());
    final fe_<FloatingPanel<PanelState>> var1 = new fe_<>(panel.x + 184, panel.y + 2, 11, 11, -1, null, "X", Drawing.RED);
    var1.data = panel;
    panel.addChild(var1);
    final ScrollView<?> var2 = new ScrollView<>(panel.x + 9, panel.y + 20, INFO_PANEL_CONTENT_WIDTH, height);
    panel.addChild(var2);
    panel.content = var2;
    return panel;
  }

  private static FixedPanel createStatusPanel(final int width) {
    final FixedPanel _goE = new FixedPanel(PANEL_MARGIN, PANEL_MARGIN, width - (PANEL_MARGIN * 2), STATUS_PANEL_HEIGHT);
    final StatusPanelState var1 = new StatusPanelState();
    _goE.state = var1;
    final Icon var2 = new Icon(15, 10, 36, 36, null);
    _goE.addChild(var2);
    var1.icon = var2;
    final MultilineLabel var3 = new MultilineLabel(15, 5 - (-(_goE.height / 2) + Menu.SMALL_FONT.ascent / 2), 450, Menu.SMALL_FONT.ascent, Font.HorizontalAlignment.LEFT);
    final MultilineLabel var4 = new MultilineLabel(15, _goE.height - Menu.SMALL_FONT.ascent, 450, Menu.SMALL_FONT.ascent, Font.HorizontalAlignment.RIGHT);
    var1.label = var3;
    _goE.addChild(var3);
    _goE.addChild(var4);
    return _goE;
  }

  public static void a893(final boolean var1) {
    if (ShatteredPlansClient.lobbyBrowserTransitionCounter > 0 && ShatteredPlansClient.showYouHaveBeenKickedDialog) {
      Drawing.h115(0, 0, Drawing.width, Component.lobbyChatPanel.y2);
      Component.YOU_HAVE_BEEN_KICKED_DIALOG.draw(var1);
    }

    if ((ShatteredPlansClient.ratedLobbyRoomTransitionCounter > 0 || ShatteredPlansClient.unratedLobbyRoomTransitionCounter > 0) && ShatteredPlansClient.invitePlayersDialogOpen) {
      Drawing.h115(0, 0, Drawing.width, Component.lobbyChatPanel.y2);
      ShatteredPlansClient.invitePlayersDialog.draw(var1);
    }
  }

  private static Sprite a800cle(final int var0) {
    final Sprite var1 = new Sprite(_ncd.width, _ncd.height);
    Drawing.saveContext();
    var1.installForDrawing();
    _ncd.drawTinted2(0, 0, var0 >= 0 ? var0 : 2105376);
    Drawing.restoreContext();
    return var1;
  }

  private static ScrollView<Player> a318lr(final DiplomacyPanelState var0, final Player var1, final Player var3, final Player[] var4) {
    final ScrollView<Player> var5 = new ScrollView<>(0, 0, INFO_PANEL_CONTENT_WIDTH, _rga);
    var5.data = var3;
    var0._h[var1.index >= var3.index ? var3.index + 1 : var3.index] = var5;
    final kb_ var6 = new kb_(FACTION_ICONS[var3.index].width * 3 / 4, var5.height / 2 - Menu.SMALL_FONT.ascent - 1, INFO_PANEL_CONTENT_WIDTH - FACTION_ICONS[var3.index].width * 3 / 4, var3.color1, var3.name, true);
    var5.addChild(var6);
    final fe_<?> var7 = new fe_<>(3 * FACTION_ICONS[var3.index].width / 4, var5.height / 2 + 1, INFO_PANEL_CONTENT_WIDTH - 3 * FACTION_ICONS[var3.index].width / 4, Menu.SMALL_FONT.ascent, 8421504, i432md(), StringConstants.PACT_OFFER, 3375155);
    var5.addChild(var7);
    var0._i[var3.index < var1.index ? var3.index : var3.index - 1] = var7;
    var5.tooltip = var6.tooltip = var7.tooltip = Strings.format(StringConstants.TOOLTIP_OFFER_TREATY, var3.name);
    a093bg(var4.length);
    final Icon var8 = new Icon(3 * FACTION_ICONS[var3.index].width / 4, 3 + var5.height / 2 + Menu.SMALL_FONT.ascent, a800cle(-1));
    var5.addChild(var8);
    var8.tooltip = Strings.format(StringConstants.TOOLTIP_OFFER_TREATY, var3.name);
    var0._f[var1.index >= var3.index ? 1 + var3.index : var3.index][0] = var8;

    for (int var9 = 1; var4.length - 1 > var9; ++var9) {
      final Icon var10 = new Icon(FACTION_ICONS[var3.index].width + (2 + _gsF.width) * var9, var5.height / 2 + Menu.SMALL_FONT.ascent + 3, a367wm(-1));
      var5.addChild(var10);
      var10.tooltip = Strings.format(StringConstants.TOOLTIP_OFFER_TREATY, var3.name);
      var0._f[var3.index > var1.index ? var3.index : 1 + var3.index][var9] = var10;
    }

    final Icon var11 = new Icon(0, (var5.height - FACTION_ICONS[var3.index].height) / 2, FACTION_ICONS[var3.index]);
    var5.addChild(var11);
    var11.tooltip = Strings.format(StringConstants.TOOLTIP_OFFER_TREATY, var3.name);
    return var5;
  }

  private static ScrollView<Player> a583nl(final DiplomacyPanelState var0, final Player[] var1, final Player var3) {
    final ScrollView<Player> var4 = new ScrollView<>(0, 0, INFO_PANEL_CONTENT_WIDTH, _rga);
    var4.data = var3;

    int var10;
    for (var10 = 0; var0._h.length > var10; ++var10) {
      if (var0._h[var10] == null) {
        var0._h[var10] = var4;
        break;
      }
    }

    final kb_ var6 = new kb_(3 * FACTION_ICONS[var3.index].width / 4, -(Menu.SMALL_FONT.ascent / 2) + var4.height / 2, -(3 * FACTION_ICONS[var3.index].width / 4) + INFO_PANEL_CONTENT_WIDTH, var3.color1, var3.name, true);
    var4.addChild(var6);
    a093bg(var1.length);
    final Icon var7 = new Icon(3 * FACTION_ICONS[var3.index].width / 4, var4.height / 2 + Menu.SMALL_FONT.ascent / 2 + 3, a800cle(-1));
    var4.addChild(var7);
    var0._f[var10][0] = var7;

    for (int var8 = 1; var1.length - 1 > var8; ++var8) {
      final Icon var9 = new Icon(FACTION_ICONS[var3.index].width + var8 * (2 + _gsF.width), 3 + Menu.SMALL_FONT.ascent / 2 + var4.height / 2, a367wm(-1));
      var4.addChild(var9);
      var0._f[var10][var8] = var9;
    }

    final Icon var11 = new Icon(0, (-FACTION_ICONS[var3.index].height + var4.height) / 2, FACTION_ICONS[var3.index]);
    var4.addChild(var11);
    return var4;
  }

  private static void a093bg(final int var1) {
    final int var2 = (INFO_PANEL_CONTENT_WIDTH - FACTION_ICONS[0].width) / (var1 - 1);
    _gsF = new Sprite(var2, 2);
    _ncd = new Sprite(var2 + FACTION_ICONS[0].width / 4, 2);
    Drawing.saveContext();
    _gsF.installForDrawing();
    Drawing.horizontalLine(0, 0, _gsF.width, 10790052);
    Drawing.horizontalLine(0, 1, _gsF.width, Drawing.WHITE);
    _gsF.pixels[0] = Drawing.alphaOver(_gsF.pixels[0], 0, 185);
    _gsF.pixels[1] = Drawing.alphaOver(_gsF.pixels[1], 0, 220);
    _gsF.pixels[var2] = Drawing.alphaOver(_gsF.pixels[var2], 0, 185);
    _gsF.pixels[1 + var2] = Drawing.alphaOver(_gsF.pixels[var2 + 1], 0, 220);
    _ncd.installForDrawing();
    Drawing.horizontalLine(0, 0, _ncd.width, 10790052);
    Drawing.horizontalLine(0, 1, _ncd.width, Drawing.WHITE);
    Drawing.restoreContext();

  }

  private static void tickChat() {
    final funorb.client.lobby.ChatMessage newLastMessage = ShatteredPlansClient.getLastChatMessage();
    if (newLastMessage != null && newLastMessage != lastChatMessage) {
      addNewChatMessages(lastChatMessage);
      lastChatMessage = newLastMessage;
    }

    if (addChatMessageTimer < ADD_CHAT_MESSAGE_TIMER_MAX) {
      ++addChatMessageTimer;
    }

    if (addChatMessageTimer >= ADD_CHAT_MESSAGE_TIMER_MAX) {
      addChatMessageTimer = ADD_CHAT_MESSAGE_TIMER_MAX;
      final ChatMessage message = newChatMessages.poll();
      if (message != null) {
        addRecentChatMessage(message);
      }
    }

    for (final ChatMessage message : recentChatMessages) {
      if (message != null) {
        message.tick();
      }
    }
  }

  private static void addRecentChatMessage(final ChatMessage message) {
    for (int var1 = recentChatMessages.length - 1; var1 >= 1; --var1) {
      recentChatMessages[var1] = recentChatMessages[var1 - 1];
    }
    addChatMessageTimer = 0;
    recentChatMessages[0] = message;
  }

  private static ScrollView<Force> addForceToProductionPanel(final Force var1, final ProductionPanelState var2, final boolean includePlayerName) {
    final ScrollView<Force> var4 = new ScrollView<>(0, 0, 209, 70);
    var4.data = var1;
    final Icon var5 = new Icon(40 - (-((5 + PRODUCTION_ICONS[0].width) * 4) - HUD_ICON_4.width) - 5, Menu.SMALL_FONT.ascent - 7, _fjr);
    var4.addChild(var5);
    String var6 = var1.getCapital().name;
    if (includePlayerName) {
      var6 = var6 + " (" + var1.player.name + ")";
    }

    final kb_ var7 = new kb_(0, 0, 209, var1.player.color1, var6, false);
    var4.addChild(var7);
    final byte var8 = 7;
    final Icon var9 = new Icon(0, var8 + var7.height, 35, 35, var1.getCapital().getSprite());
    var4.addChild(var9);
    final RoundedRect var10 = new RoundedRect(40, var8 + var7.height + PRODUCTION_ICONS[0].height, 4 * PRODUCTION_ICONS[0].width + 15, 35 - PRODUCTION_ICONS[0].height, 2105376);
    var4.addChild(var10);

    for (int i = 0; i < GameState.NUM_RESOURCES; ++i) {
      final RoundedRect var12 = new RoundedRect(i * (5 + PRODUCTION_ICONS[i].width) + 40, var7.height + var8 + PRODUCTION_ICONS[i].height / 2, PRODUCTION_ICONS[i].width, 35 - PRODUCTION_ICONS[i].height / 2, 2105376);
      var4.addChild(var12);
      int var13 = var1.fleetProduction + var1.surplusResources[i];
      if (var13 < 0) {
        var13 = 0;
      }

      final Label var14 = new Label((PRODUCTION_ICONS[i].width + 5) * i + 40, PRODUCTION_ICONS[i].height + var7.height + var8, PRODUCTION_ICONS[i].width, Menu.SMALL_FONT.ascent, Integer.toString(var13));
      var4.addChild(var14);
      final Icon var15 = new Icon(40 + i * (5 + PRODUCTION_ICONS[i].width), var7.height + var8, PRODUCTION_ICONS[i]);
      var4.addChild(var15);
      String var16 = StringConstants.TOOLTIP_INCOME[i];
      if (var1.surplusResources[i] == 0) {
        var16 = var16 + " " + StringConstants.TOOLTIP_INCOME_LIMITING;
      }

      var12.tooltip = var14.tooltip = var15.tooltip = var16;
    }

    final RoundedRect var19 = new RoundedRect(PRODUCTION_ICONS[0].width * 4 + 60, var8 + var7.height + PRODUCTION_ICONS[0].height / 2, HUD_ICON_4.width, -(PRODUCTION_ICONS[0].height / 2) + 35, 534312);
    var4.addChild(var19);
    final Label var21 = new Label(20 + 4 * PRODUCTION_ICONS[0].width + 40, PRODUCTION_ICONS[0].height + var7.height + var8, HUD_ICON_4.width, Menu.SMALL_FONT.ascent, Integer.toString(Math.max(var1.fleetProduction, 0)));
    var4.addChild(var21);
    final Icon var22 = new Icon(20 - (-(4 * PRODUCTION_ICONS[0].width) - 40), var8 + (var7.height - 1), HUD_ICON_4);
    var4.addChild(var22);
    var19.tooltip = var21.tooltip = var22.tooltip = StringConstants.TOOLTIP_TOTAL_FLEET_PRODUCTION;
    final Label var23 = new Label(54 + PRODUCTION_ICONS[0].width * 4, var7.height + var8 + PRODUCTION_ICONS[0].height, "=");
    var4.addChild(var23);
    final Button<Force> var24 = new Button<>(4 * PRODUCTION_ICONS[0].width + 20 + 40 - (-5 - HUD_ICON_4.width), var7.height + 3, PRODUCTION_BUTTON.width, PRODUCTION_BUTTON.height, PRODUCTION_BUTTON, null, -1, PRODUCTION_BUTTON_DOWN, null, -1);
    var24.data = var1;
    var4.addChild(var24);
    final Label var17 = new Label(var24.x, 29 + var24.y, var24.width, Menu.SMALL_FONT.ascent, Integer.toString(var1.fleetsAvailableToBuild));
    var17.data = var1;
    var4.addChild(var17);
    var24.tooltip = var17.tooltip = StringConstants.TOOLTIP_PLACE_FLEETS + " " + (var1.fleetsAvailableToBuild != 1 ? Strings.format(StringConstants.TOOLTIP_FLEETS_REMAINING, Integer.toString(var1.fleetsAvailableToBuild)) : StringConstants.TOOLTIP_ONE_FLEET_REMAINING);

    var2.buildFleetsButtons.add(var24);
    var2._f.add(var4);
    var2.buildFleetsLabels.add(var17);

    return var4;
  }

  private static Sprite a367wm(final int var0) {
    final Sprite var2 = new Sprite(_gsF.width, _gsF.height);
    Drawing.saveContext();
    var2.installForDrawing();
    _gsF.drawTinted2(0, 0, var0 < 0 ? 2105376 : var0);
    Drawing.restoreContext();
    return var2;
  }

  private static <T> ScrollView<T> a755bp(final int var0, final Player var1, final int var2) {
    final ScrollView<T> var3 = new ScrollView<>(0, 0, INFO_PANEL_CONTENT_WIDTH, SHIP.offsetX + 4);
    final uc_ var4 = new uc_(var3.height / 2 - 3, var2, var1.color1);
    var3.addChild(var4);
    final Icon var5 = new Icon(-(var1._v.offsetX / 2) + var2, -(var1._v.offsetY / 2) + var3.height / 2, var1._v);
    var3.addChild(var5);
    final Label var6 = new Label(-(var1._v.offsetX / 2) + var2, var3.height / 2 - Menu.SMALL_FONT.ascent / 2, var1._v.offsetX, Menu.SMALL_FONT.ascent, Integer.toString(var0));
    var3.addChild(var6);
    return var3;
  }

  private static void a898am(final int var0, int var1, final Sprite var2, int var4, final int var5) {
    int var3 = 0;
    var1 -= var2.y;
    var3 -= var2.x;
    int var6 = var1 * var2.width + var3;
    int var8 = Drawing.width * var5 + var0;

    while (true) {
      --var4;
      if (var4 < 0) {
        return;
      }

      Drawing.screenBuffer[var8++] = var2.pixels[var6++];
    }
  }

  private static ScrollView<Force> a278an(final Force var2, final boolean includePlayerName) {
    final ScrollView<Force> var3 = new ScrollView<>(0, 0, 209, 50);
    var3.data = var2;
    String var4 = var2.getCapital().name;
    if (includePlayerName) {
      var4 = var4 + " (" + var2.player.name + ")";
    }

    final kb_ var5 = new kb_(0, 0, 209, var2.player.color1, var4, false);
    var3.addChild(var5);

    for (int var6 = 0; var6 < 4; ++var6) {
      final Icon var7 = new Icon(40 + var6 * (5 + ICON_CIRCLES[var6].width), var5.height + 7, ICON_CIRCLES[var6]);
      var3.addChild(var7);
      int var8 = var2.fleetProduction + var2.surplusResources[var6];
      if (var8 < 0) {
        var8 = 0;
      }

      final Label var9 = new Label(40 + var6 * (ICON_CIRCLES[var6].width + 5), 7 + var5.height + ICON_CIRCLES[var6].height / 8, ICON_CIRCLES[var6].width, ICON_CIRCLES[var6].height, Integer.toString(var8));
      var3.addChild(var9);
      var7.tooltip = var9.tooltip = StringConstants.TOOLTIP_INCOME[var6];
    }

    final RoundedRect var10 = new RoundedRect(HUD_ICON_4.width / 2 + (ICON_CIRCLES[0].width + 5) * 4 + 40, 6 + var5.height, HUD_ICON_4.width * 2, HUD_ICON_4.height - 2, 534312);
    var3.addChild(var10);
    int var11 = var2.fleetProduction;
    if (var11 < 0) {
      var11 = 0;
    }

    final Label var12 = new Label(HUD_ICON_4.width + 4 * (5 + ICON_CIRCLES[0].width) + 40, var5.height + 7 + ICON_CIRCLES[0].height / 8, 3 * HUD_ICON_4.width / 2, HUD_ICON_4.height, Integer.toString(var11));
    var3.addChild(var12);
    final Icon var13 = new Icon(40 + 4 * (ICON_CIRCLES[0].width + 5), 5 + var5.height, HUD_ICON_4);
    var3.addChild(var13);
    var10.tooltip = var12.tooltip = var13.tooltip = StringConstants.TOOLTIP_TOTAL_FLEET_PRODUCTION;
    return var3;
  }

  private static <T> ScrollView<T> a301e(final int var0, final int var1, final ProjectsPanelState var2) {
    final ScrollView<T> var3 = new ScrollView<>(0, 0, 132, 40);
    var2._c[var1] = var3;
    final Label var4 = new Label(132 - Menu.SMALL_FONT.measureLineWidth(StringConstants.PROJECT_NAMES[var1]), 0, StringConstants.PROJECT_NAMES[var1]);
    var3.addChild(var4);
    final RoundedRect var5 = new RoundedRect(PROJECT_ICONS[var1].width / 2, Menu.SMALL_FONT.ascent + 2, 132 - PROJECT_ICONS[var1].width / 2, PROJECT_ICONS[var1].height, 2105376);
    var3.addChild(var5);
    final Icon var6 = new Icon(3 * PROJECT_ICONS[var1].width / 4, Menu.SMALL_FONT.ascent + 5, a919ec(var1, var0));
    var3.addChild(var6);
    final Icon var7 = new Icon(0, 2 + Menu.SMALL_FONT.ascent, PROJECT_ICONS[var1]);
    var3.addChild(var7);
    if (var0 == 5) {
      final Label var8 = new Label(PROJECT_ICONS[var1].width, 1 + Menu.SMALL_FONT.ascent + 2 + PROJECT_ICONS[var1].height / 8, 130 - PROJECT_ICONS[var1].width, PROJECT_ICONS[var1].height, StringConstants.TEXT_READY);
      var3.addChild(var8);
      var2.statusLabels[var1] = var8;
      var3.tooltip = var4.tooltip = var5.tooltip = var6.tooltip = var7.tooltip = var8.tooltip = StringConstants.TOOLTIP_PROJECT_COMPLETE;
    } else {
      var3.tooltip = var4.tooltip = var5.tooltip = var6.tooltip = var7.tooltip = Strings.format(StringConstants.TOOLTIP_PARTIALLY_COMPLETE, Integer.toString(var0));
    }

    return var3;
  }

  private static Sprite a919ec(final int var0, final int var1) {
    int var2 = (-(3 * PROJECT_ICONS[var0].width / 4) + 130) * var1 / 5;
    if (var2 <= 0) {
      var2 = 1;
    }

    final Sprite var3 = new Sprite(var2, PROJECT_ICONS[var0].height - 6);
    Drawing.saveContext();
    var3.installForDrawing();

    int var4;
    for (var4 = 0; var3.width > var4; ++var4) {
      Drawing.verticalLine(var4, 0, var3.height, Drawing.alphaOver(GameView.RESOURCE_COLORS[var0], 0, 128 + 128 * var4 / var3.width));
    }

    for (var4 = 0; var3.width - 1 > var4; ++var4) {
      var3.pixels[var4] = Drawing.alphaOver(var3.pixels[var4], 0, 128);
      var3.pixels[var3.width * (var3.height - 1) + var4] = Drawing.alphaOver(var3.pixels[var3.width * (var3.height - 1) + var4], 0, 64);
    }

    for (var4 = 0; var4 < var3.height; ++var4) {
      var3.pixels[var3.width - 1 + var4 * var3.width] = Drawing.alphaOver(var3.pixels[var4 * var3.width + (var3.width - 1)], 0, 128);
    }

    var3.pixels[var3.width * var3.height - 1] = Drawing.alphaOver(var3.pixels[var3.height * var3.width - 1], 0, 190);
    Drawing.restoreContext();
    return var3;
  }

  private static void a613cq(final Font var0) {
    _ssb = var0;
    lastChatMessage = ShatteredPlansClient.getLastChatMessage();
    newChatMessages = new ArrayDeque<>();
    recentChatMessages = new ChatMessage[7];
  }

  private static <T> ScrollView<T> a179hc(final int var0, final int var1) {
    final ScrollView<T> var2 = new ScrollView<>(var0, var1, 209, Menu.SMALL_FONT.ascent);
    final int var3 = (-Menu.SMALL_FONT.measureLineWidth(StringConstants.TEXT_ENEMY_PRODUCTION) + 209 - 20) / 2;
    final Sprite _vcb = new Sprite(var3, 2);
    Drawing.saveContext();
    _vcb.installForDrawing();
    Drawing.fillRect(0, 0, var3, 2, Drawing.WHITE);
    Drawing.restoreContext();
    final Icon var5 = new Icon(5 + var0, var2.height / 2 + var1, _vcb);
    final Icon var6 = new Icon(-_vcb.width + var2.width + (var0 - 5), var2.height / 2 + var1, _vcb);
    final Label var7 = new Label(var0, var1, var2.width, var2.height, StringConstants.TEXT_ENEMY_PRODUCTION);
    var2.addChild(var5);
    var2.addChild(var6);
    var2.addChild(var7);
    return var2;
  }

  @SuppressWarnings("SameParameterValue")
  private static void a910ld(final int var0a, final int x2, final int var2a, final int y2, final int var4a, final int alpha, final Sprite sprite) {
    final int x1 = var4a - sprite.x;
    final int y1 = var0a - sprite.y;
    final int alpha2 = 256 - alpha;

    int j = x1 + sprite.width * y1;
    int k = x2 + Drawing.width * y2;
    for (int i = var2a - 1; i >= 0; --i) {
      final int color1 = sprite.pixels[j++];
      final int color2 = Drawing.screenBuffer[k];
      final int rb1 = color1 & 0xff00ff;
      final int g1  = color1 & 0x00ff00;
      final int rb2 = color2 & 0xff00ff;
      final int g2  = color2 & 0x00ff00;
      final int rb3 = ((rb1 * alpha)  & 0xff00ff00) + ((rb2 * alpha2) & 0xff00ff00);
      final int g3  = ((g2  * alpha2) & Drawing.RED) + ((g1  * alpha)  & Drawing.RED);
      final int color3 = (g3 | rb3) >>> 8;
      Drawing.screenBuffer[k++] = color3;
    }
  }

  private static boolean a865rr(final UIComponent<?> var0, final Collection<UIComponent<?>> var1) {
    return var1.stream().anyMatch(var2 -> var2.hasChild(var0));
  }

  private static void a771ml(final int var0, int var1, int var2, final int var3, final Sprite var4) {
    final int var5 = -var4.x;
    var2 -= var4.y;
    int var6 = var5 + var4.width * var2;
    int var7 = Drawing.pixelIndex(var3, var0);

    while (true) {
      --var1;
      if (var1 < 0) {
        return;
      }

      int var8 = var4.pixels[var6++];
      final int var9 = Drawing.screenBuffer[var7];
      final int var10 = var8 + var9;
      var8 = (var8 & 16711935) + (var9 & 16711935);
      final int i = (var8 & 16777472) + (-var8 + var10 & 65536);
      final int var01 = -(i >>> 8) + i;
      Drawing.screenBuffer[var7++] = var01 | var10 - i;
    }
  }

  private static <T> ScrollView<T> createScrollView(final Label[] labels) {
    final ScrollView<T> view = new ScrollView<>(0, 0, INFO_PANEL_CONTENT_WIDTH, labels.length * Menu.SMALL_FONT.ascent);

    for (int i = 0; i < labels.length; ++i) {
      labels[i].setPosition(0, i * Menu.SMALL_FONT.ascent);
      view.addChild(labels[i]);
    }

    return view;
  }

  private static Sprite i432md() {
    final Sprite _ssF = new Sprite(INFO_PANEL_CONTENT_WIDTH - FACTION_ICONS[0].width * 3 / 4, Menu.SMALL_FONT.ascent);
    Drawing.saveContext();
    _ssF.installForDrawing();

    int var1;
    for (var1 = 1; _ssF.height - 1 > var1; ++var1) {
      final int var2 = (-var1 + _ssF.height / 2) * (_ssF.height / 2 - var1);
      Drawing.horizontalLine(0, var1, _ssF.width, 328965 * (_ssF.height / 2 * (_ssF.height / 2) - var2));
    }

    Drawing.horizontalLine(0, 0, _ssF.width, 1052688);
    Drawing.horizontalLine(0, _ssF.height - 1, _ssF.width, 1052688);

    for (var1 = 0; var1 < _ssF.height; ++var1) {
      Drawing.setPixel(_ssF.width - 1, var1, Drawing.alphaOver(_ssF.pixels[(var1 + 1) * _ssF.width - 1], 1052688, 128));
    }

    Drawing.setPixel(_ssF.width - 2, 1, Drawing.alphaOver(_ssF.pixels[_ssF.width * 2 - 2], 1052688, 128));
    Drawing.setPixel(_ssF.width - 3, 1, Drawing.alphaOver(_ssF.pixels[_ssF.width * 2 - 3], 1052688, 192));
    Drawing.setPixel(_ssF.width - 2, 2, Drawing.alphaOver(_ssF.pixels[_ssF.width * 3 - 2], 1052688, 192));
    Drawing.setPixel(_ssF.width - 2, _ssF.height - 2, Drawing.alphaOver(_ssF.pixels[_ssF.width * (_ssF.height - 1) - 2], 1052688, 128));
    Drawing.setPixel(_ssF.width - 3, _ssF.height - 2, Drawing.alphaOver(_ssF.pixels[(_ssF.height - 1) * _ssF.width - 3], 1052688, 192));
    Drawing.setPixel(_ssF.width - 2, _ssF.height - 3, Drawing.alphaOver(_ssF.pixels[_ssF.width * (_ssF.height - 2) - 2], 1052688, 192));
    Drawing.restoreContext();
    return _ssF;
  }

  public static void fadeRect(int x, int y, int width, int height, final int alpha) {
    if (alpha != 256) {
      if (alpha == 0) {
        Drawing.fillRect(x, y, width, height, 0);
      } else if (alpha == 128) {
        Drawing.h115(x, y, width, height);
      } else {
        if (x < 0) {
          width += x;
          x = 0;
        }

        if (y < 0) {
          height += y;
          y = 0;
        }

        if (x + width > Drawing.width) {
          width = Drawing.width - x;
        }

        if (height + y > Drawing.height) {
          height = Drawing.height - y;
        }

        --x;
        final int var6 = y + height;

        for (int i = y; i < var6; ++i) {
          int n = Drawing.pixelIndex(x, i);
          for (int j = width; j > 0; --j) {
            ++n;
            final int px = Drawing.screenBuffer[n];
            final int rb = ((px & 0xff00ff) * alpha) & 0xff00ff00;
            final int g = ((px & 0x00ff00) * alpha) & Drawing.RED;
            Drawing.screenBuffer[n] = (g | rb) >> 8;
          }
        }
      }
    }
  }

  public static String[] breakLinesWithColorTags(final Font font, final String text, final int[] lineWidths) {
    if (BROKEN_LINES == null) {
      BROKEN_LINES = new String[16];
    }

    int lineCount;
    while (true) {
      try {
        lineCount = font.breakLines(text, lineWidths, BROKEN_LINES);
        break;
      } catch (final ArrayIndexOutOfBoundsException var6) {
        if (BROKEN_LINES.length >= 1024) {
          return null;
        }
        BROKEN_LINES = new String[BROKEN_LINES.length << 1];
      }
    }

    final String[] lines = new String[lineCount];
    System.arraycopy(BROKEN_LINES, 0, lines, 0, lineCount);
    fixMultilineColorTags(lines);
    return lines;
  }

  private static void fixMultilineColorTags(final String[] lines) {
    int currentColor = -1;
    for (int i = 0; i < lines.length; ++i) {
      final String line = lines[i];
      if (currentColor != -1) {
        lines[i] = Strings.format("<col=<%0>>", Integer.toString(currentColor, 16)) + line;
      }

      final int open = Strings.lastIndexOf(line, "<col=");
      final int close = Strings.lastIndexOf(line, "</col>");
      if (open > close) {
        final int openEnd = line.indexOf('>', open);
        if (openEnd != -1) {
          final String colorStr = line.substring(5 + open, openEnd);
          currentColor = Strings.parseHexInteger(colorStr);
        }
      } else if (close != -1) {
        currentColor = -1;
      }
    }
  }

  private static void addNewChatMessages(final funorb.client.lobby.ChatMessage lastOldMessage) {
    int newIndex = 0;
    for (int i = ShatteredPlansClient.chatMessageCount - 1; i >= 0; --i) {
      if (lastOldMessage == ShatteredPlansClient.chatMessages[i]) {
        newIndex = i + 1;
        break;
      }
    }
    for (int i = newIndex; i < ShatteredPlansClient.chatMessageCount; ++i) {
      if (ShatteredPlansClient.chatMessages[i].component != null) {
        addChatMessage(ShatteredPlansClient.chatMessages[i].component.label);
      }
    }
  }

  private static void addChatMessage(final String text) {
    final ChatMessage message = new ChatMessage(text);
    if (addChatMessageTimer < ADD_CHAT_MESSAGE_TIMER_MAX) {
      newChatMessages.add(message);
    } else {
      addRecentChatMessage(message);
    }
  }

  public static int breakLines(final String text, final Font font, final int[] lineWidths) {
    if (BROKEN_LINES == null) {
      BROKEN_LINES = new String[32];
    }

    while (true) {
      try {
        return font.breakLines(text, lineWidths, BROKEN_LINES);
      } catch (final ArrayIndexOutOfBoundsException var6) {
        if (BROKEN_LINES.length >= 1024) {
          return -1;
        }
        BROKEN_LINES = new String[BROKEN_LINES.length << 1];
      }
    }
  }

  private static void d150vn() {
    Drawing.withLocalContext(() -> {
      Drawing.expandBoundsToInclude(0, 372, ShatteredPlansClient.SCREEN_WIDTH, ShatteredPlansClient.SCREEN_HEIGHT);
      int var0 = -addChatMessageTimer + ShatteredPlansClient.SCREEN_HEIGHT;

      for (int var1 = 0; recentChatMessages.length > var1 && recentChatMessages[var1] != null; ++var1) {
        recentChatMessages[var1].draw(var0);
        var0 -= ADD_CHAT_MESSAGE_TIMER_MAX;
      }
    });
  }

  public void setPlacementMode(final PlacementMode mode) {
    if (this.gameSession.placementMode == PlacementMode.BUILD_FLEET && mode != PlacementMode.BUILD_FLEET) {
      this.productionPanel.state.deactivateFleetPlacement();
    }

    this.gameSession.placementMode = mode;
    Arrays.fill(this.gameSession.gameView.highlightedSystems, SystemHighlight.NONE);

    ((StatusPanelState) this.statusPanel.state).icon.setSprite(null);
    if (this.gameSession.gameView.animationPhase == AbstractGameView.AnimationPhase.NOT_PLAYING && !this.gameSession.readyToEndTurn) {
      this.setActionHint(switch (mode) {
        case MOVE_FLEET_SRC  -> StringConstants.HINT_SELECT_SRC;
        case MOVE_FLEET_DEST -> StringConstants.HINT_SELECT_DEST;
        case DEFENSIVE_NET   -> StringConstants.HINT_DEFENSEGRID;
        case TERRAFORM       -> StringConstants.HINT_TERRAFORM;
        case STELLAR_BOMB    -> StringConstants.HINT_FLARE;
        case GATE_SRC        -> StringConstants.HINT_SELECT_GATE_SRC;
        case GATE_DEST       -> StringConstants.HINT_SELECT_GATE_DEST;
        case BUILD_FLEET     -> Strings.format(
            StringConstants.HINT_PLACEMENT,
            this.gameSession.selectedForce.getCapital().name,
            Integer.toString(this.gameSession.selectedForce.fleetsAvailableToBuild));
      });
    }
  }

  private void activateProject(@MagicConstant(valuesFromClass = GameState.ResourceType.class) final int which) {
    this.gameSession.cancelProjectOrder(which);

    if (which == GameState.ResourceType.METAL) {
      this.setPlacementMode(PlacementMode.DEFENSIVE_NET);
      for (final StarSystem starSystem : this.gameSession.gameState.map.systems) {
        if (starSystem.owner == this.gameSession.localPlayer && !starSystem.hasDefensiveNet) {
          this.gameSession.gameView.highlightedSystems[starSystem.index] = SystemHighlight.TARGET;
        }
      }
    } else if (which == GameState.ResourceType.BIOMASS) {
      this.setPlacementMode(PlacementMode.TERRAFORM);
      for (final StarSystem starSystem : this.gameSession.gameState.map.systems) {
        if (this.gameSession.localPlayer == starSystem.owner && starSystem.score == StarSystem.Score.NORMAL) {
          this.gameSession.gameView.highlightedSystems[starSystem.index] = SystemHighlight.TARGET;
        }
      }
    } else if (which == GameState.ResourceType.ENERGY) {
      this.setPlacementMode(PlacementMode.STELLAR_BOMB);
      for (final StarSystem starSystem : this.gameSession.gameState.map.systems) {
        if (starSystem.owner != this.gameSession.localPlayer && (starSystem.owner == null || !this.gameSession.localPlayer.allies[starSystem.owner.index])) {
          if (Arrays.stream(starSystem.neighbors).anyMatch(system -> this.gameSession.localPlayer == system.owner)) {
            this.gameSession.gameView.highlightedSystems[starSystem.index] = SystemHighlight.TARGET;
          }
        }
      }
    } else if (which == GameState.ResourceType.EXOTICS) {
      this.setPlacementMode(PlacementMode.GATE_SRC);
      for (final StarSystem var5 : this.gameSession.gameState.map.systems) {
        if (this.gameSession.localPlayer == var5.owner) {
          this.gameSession.gameView.highlightedSystems[var5.index] = SystemHighlight.TARGET;
        }
      }
    }
  }

  private void updateDiplomacyState(final Player player) {
    if (!this.gameSession.isTutorial || TutorialState.stage >= 6) {
      final DiplomacyPanelState var3 = this.diplomacyPanel.state;
      if (this.gameSession.localPlayer == null || player == this.gameSession.localPlayer) {
        for (int var4 = 0; var3._f[0].length > var4; ++var4) {
          final Player var5 = player.index > var4 ? this.gameSession.gameState.players[var4] : this.gameSession.gameState.players[var4 + 1];
          final int var6 = player.allies[var5.index] && !this.gameSession.gameState.isPlayerDefeated(var5.index) ? var5.color1 : -1;
          if (var4 == 0) {
            var3._f[0][0].setSprite(a800cle(var6));
          } else {
            var3._f[0][var4].setSprite(a367wm(var6));
          }
        }
      } else {
        final boolean var11 = player.index < this.gameSession.localPlayer.index;
        final fe_<?> var12 = var3._i[var11 ? player.index : player.index - 1];
        final Icon[] var13 = var3._f[var11 ? 1 + player.index : player.index];
        final ScrollView<?> var7 = var3._h[!var11 ? player.index : 1 + player.index];
        if (this.gameSession.gameState.isPlayerDefeated(player.index)) {
          final String var14 = (this.gameSession.gameState.didPlayerResign(player.index)) ? StringConstants.TEXT_RESIGNED : StringConstants.TEXT_DEFEATED;
          var12.a290(0, var14);
          var13[0].setSprite(a800cle(-1));

          for (int var15 = 1; var15 < var13.length; ++var15) {
            var13[var15].setSprite(a367wm(-1));
          }

          var7.tooltip = null;

          for (final UIComponent<?> var9 : var7.children) {
            var9.tooltip = null;
          }
        } else {
          int var8 = -1;
          if (player.allies[this.gameSession.localPlayer.index]) {
            var8 = this.gameSession.localPlayer.color1;
            var12.a290(9386040, Strings.format(StringConstants.PACT_EXPIRES, Integer.toString(player.pactTurnsRemaining[this.gameSession.localPlayer.index])));
            var7.tooltip = null;

            for (final UIComponent<?> var9 : var7.children) {
              var9.tooltip = null;
            }
          } else if (player.hasPactOfferFrom(this.gameSession.localPlayer)) {
            var12.a290(0x338033, StringConstants.PACT_AWAITING);
            var7.tooltip = Strings.format(StringConstants.TOOLTIP_WAIT_TREATY, player.name);

            for (final UIComponent<?> var9 : var7.children) {
              var9.tooltip = Strings.format(StringConstants.TOOLTIP_WAIT_TREATY, player.name);
            }
          } else if (this.gameSession.localPlayer.hasPactOfferFrom(player)) {
            var12.a290(0x338033, StringConstants.PACT_ACCEPT);
            var7.tooltip = Strings.format(StringConstants.TOOLTIP_ACCEPT_TREATY, player.name);

            for (final UIComponent<?> var9 : var7.children) {
              var9.tooltip = Strings.format(StringConstants.TOOLTIP_ACCEPT_TREATY, player.name);
            }
          } else {
            var12.a290(0x338033, StringConstants.PACT_OFFER);
            var7.tooltip = Strings.format(StringConstants.TOOLTIP_OFFER_TREATY, player.name);

            for (final UIComponent<?> var9 : var7.children) {
              var9.tooltip = Strings.format(StringConstants.TOOLTIP_OFFER_TREATY, player.name);
            }
          }

          var13[0].setSprite(a800cle(var8));

          for (int var15 = 1; var13.length > var15; ++var15) {
            int var10 = var15 - 1;
            if (this.gameSession.localPlayer.index <= var10) {
              ++var10;
            }

            if (var10 >= player.index) {
              ++var10;
              if (var10 == this.gameSession.localPlayer.index) {
                ++var10;
              }
            }

            final int var16 = player.allies[var10] ? this.gameSession.gameState.players[var10].color1 : -1;
            var13[var15].setSprite(a367wm(var16));
          }
        }
      }
    }
  }

  public void a950(final String var1, final boolean var2) {
    if (var1.startsWith("production")) {
      this.productionButton.visible = var2;
    }

    if (var1.startsWith("projects")) {
      this.projectsButton.visible = var2;
    }

    if (var1.startsWith("fleets")) {
      this.fleetInfoButton.visible = var2;
    }

    if (var1.startsWith("diplomacy")) {
      this.diplomacyButton.visible = var2;
    }

    if (var1.startsWith("victory") && this.victoryButton != null) {
      this.victoryButton.visible = var2;
    }

    if (var1.startsWith("turn")) {
      this.endTurnButton.visible = var2;
    }
  }

  private void drawStatsScreen() {
    final int openAmount = Math.min(this.statsScreenOpenAmount, STATS_SCREEN_OPEN_AMOUNT_MAX);
    final int width = MathUtil.ease(openAmount, STATS_SCREEN_OPEN_AMOUNT_MAX, 20, 550);
    fadeRect(0, 0, ShatteredPlansClient.SCREEN_WIDTH, ShatteredPlansClient.SCREEN_HEIGHT, 192 + (STATS_SCREEN_OPEN_AMOUNT_MAX - openAmount << 6) / STATS_SCREEN_OPEN_AMOUNT_MAX);
    final int statsX = (ShatteredPlansClient.SCREEN_WIDTH - width) / 2;
    if (ShatteredPlansClient.renderQuality.statsScreenFadeQuality == RenderQuality.Level.HIGH) {
      fadeRect(statsX, STATS_SCREEN_Y, width, STATS_SCREEN_HEIGHT, (STATS_SCREEN_OPEN_AMOUNT_MAX - openAmount << 7) / STATS_SCREEN_OPEN_AMOUNT_MAX + 128);
      final Sprite var8 = Menu.captureScreenRect(statsX, STATS_SCREEN_Y, width, STATS_SCREEN_HEIGHT);
      assert var8 != null;
      Drawing.saveContext();
      var8.installForDrawing();
      Drawing.b669(3, 3, width, STATS_SCREEN_HEIGHT);
      Drawing.restoreContext();
      var8.draw(statsX, STATS_SCREEN_Y, (openAmount << 7) / STATS_SCREEN_OPEN_AMOUNT_MAX);
    } else if (ShatteredPlansClient.renderQuality.statsScreenFadeQuality == RenderQuality.Level.MEDIUM) {
      fadeRect(statsX, STATS_SCREEN_Y, width, STATS_SCREEN_HEIGHT, 128 + (STATS_SCREEN_OPEN_AMOUNT_MAX - openAmount << 7) / STATS_SCREEN_OPEN_AMOUNT_MAX);
    } else if (ShatteredPlansClient.renderQuality.statsScreenFadeQuality == RenderQuality.Level.LOW) {
      if (openAmount == STATS_SCREEN_OPEN_AMOUNT_MAX) {
        Drawing.fillRect(statsX, STATS_SCREEN_Y, width, STATS_SCREEN_HEIGHT, 0x0d0d14);
      } else {
        Drawing.fillRect(statsX, STATS_SCREEN_Y, width, STATS_SCREEN_HEIGHT, 0x0d0d14, (openAmount << 8) / STATS_SCREEN_OPEN_AMOUNT_MAX);
      }
    }

    Menu.drawPanel(statsX, STATS_SCREEN_Y, width, STATS_SCREEN_HEIGHT);
    final int[] bounds = new int[4];
    Drawing.saveBoundsTo(bounds);
    Drawing.setBounds(statsX, STATS_SCREEN_Y, statsX + width, STATS_SCREEN_Y + STATS_SCREEN_HEIGHT);
    boolean isVictor = false;
    boolean isDefeated = false;
    final boolean hasEnded = this.gameSession.gameState.hasEnded;
    final boolean isDraw = hasEnded && this.gameSession.gameState.winnerIndex < 0;
    if (this.gameSession.localPlayer != null) {
      isVictor = this.gameSession.localPlayer.index == this.gameSession.gameState.winnerIndex;
      isDefeated = this.gameSession.gameState.isPlayerDefeated(this.gameSession.localPlayer.index) || this.gameSession.gameState.victoryChecker.isLoser(this.gameSession.localPlayer);
    }

    final Sprite endSprite = !isDefeated ? ShatteredPlansClient.WIN_SPRITE : ShatteredPlansClient.LOSE_SPRITE;
    final int playerCount = this.gameSession.gameState.playerCount;
    final int[][] graphData = this.getAnimatedStatsGraphData();
    if (this.statsGraphAlpha == 0 || graphData == null) {
      for (int i = 0; i < 240; ++i) {
        final int var19 = STATS_SCREEN_Y + STATS_GRAPH_MARGIN + i;
        final int var20 = STATS_GRAPH_MARGIN + statsX;
        final int var21 = Math.min(width - 40, 320);
        if (var21 >= 0) {
          a898am(var20, i, endSprite, var21, var19);
          Drawing.setPixel(var20 - 1, var19, Drawing.WHITE);
          Drawing.setPixel(var20 + var21, var19, Drawing.WHITE);
        }
      }
    } else {
      for (int i = 0; i < 240; ++i) {
        final int var19 = i + STATS_GRAPH_MARGIN + STATS_SCREEN_Y;
        final int var20 = STATS_GRAPH_MARGIN + statsX;
        final int var21 = Math.min(width - (STATS_GRAPH_MARGIN * 2), 320);
        if (var21 >= 0) {
          a771ml(var19, var21, i, var20, endSprite);
          Drawing.horizontalLine(var20, var19, var21, 0, 192);
          Drawing.setPixel(var20 - 1, var19, Drawing.WHITE);
          Drawing.setPixel(var20 + var21, var19, Drawing.WHITE);
        }
      }
    }

    if (this.statsGraphAlpha != 0 && this.statsGraphAlpha != 25 && graphData != null) {
      for (int i = 0; i < 240; ++i) {
        final int var19 = i + STATS_SCREEN_Y + STATS_GRAPH_MARGIN;
        final int var20 = statsX + STATS_GRAPH_MARGIN;
        final int var21 = Math.min(width - 40, STATS_GRAPH_WIDTH);
        if (var21 >= 0) {
          a910ld(i, var20, var21, var19, 0, 256 - this.statsGraphAlpha, endSprite);
        }
      }
    }

    Drawing.horizontalLine(statsX + STATS_GRAPH_MARGIN - 1, 99, STATS_GRAPH_WIDTH + 2, Drawing.WHITE);
    Drawing.horizontalLine(statsX + STATS_GRAPH_MARGIN - 1, 340, STATS_GRAPH_WIDTH + 2, Drawing.WHITE);
    final int var18 = statsX + STATS_GRAPH_WIDTH + 125;
    String endMessage = isVictor ? StringConstants.TEXT_VICTORY
        : isDefeated ? StringConstants.TEXT_DEFEAT
        : isDraw ? StringConstants.TEXT_PEACE_SHORT
        : hasEnded ? StringConstants.TEXT_VICTORY
        : null;
    if (this.gameSession.localPlayer == null && !isDraw) {
      endMessage = " ";
    }

    Menu.FONT.drawCentered(endMessage, var18, 107, Drawing.WHITE);
    final int var21 = 40 + statsX - 10 + 320;
    int var43 = 120;
    int var23 = this.currentStatsScreenTab.ordinal() * 4;

    for (int var24 = 0; var24 < 4; ++var24) {
      this.b050(var24, var43, var21, var23);
      ++var23;
      var43 += 8 + this._ob[var24];
    }

    final int var17 = Math.min(width - 40, 320);
    Drawing.setBounds(20 + statsX, 100, var17 + statsX + 20, 340);
    if (this.statsGraphAlpha != 0 && graphData != null) {
      final int turnCount = graphData[0].length;
      int graphMax = 1;
      for (int i = 0; i < playerCount; ++i) {
        for (int j = 0; j < turnCount; ++j) {
          if (graphMax < graphData[i][j]) {
            graphMax = graphData[i][j];
          }
        }
      }

      final int graphHeight = STATS_GRAPH_HEIGHT + 2;
      final int graphWidth = STATS_GRAPH_WIDTH + 2;
      final int graphX = statsX + STATS_GRAPH_MARGIN;
      final int graphSteps;
      if (this.statsGraphTurnAdvanceAnimationCounter == 0) {
        graphSteps = this.statsGraphTurnCount - 1 << 10;
      } else {
        graphSteps = MathUtil.ease(this.statsGraphTurnAdvanceAnimationCounter, STATS_GRAPH_TURN_ADVANCE_ANIMATION_COUNTER_MAX, this.statsGraphStartTurn << 10, this.statsGraphTurnCount << 10) - 1024;
      }

      if (this.statsGraphAlpha == 256) {
        for (int i = 0; i < playerCount; ++i) {
          final int color = this.gameSession.gameState.players[i].color2;
          int x1 = graphX;
          int y1 = graphWidth - (graphHeight * graphData[i][0] / graphMax);
          int y1a = y1;

          for (int j = 1; j < turnCount; ++j) {
            final int x2 = graphX + (((j * STATS_GRAPH_WIDTH) << 10) / graphSteps);
            final int y2 = graphWidth - (graphHeight * graphData[i][j] / graphMax);

            for (int k = 0; k < x2 - x1; ++k) {
              final int y2a = MathUtil.ease(k, x2 - x1, y1, y2);
              Drawing.line(x1 + k - 1, y1a, x1 + k, y2a, color);
              y1a = y2a;
            }

            x1 = x2;
            y1 = y2;
          }
        }
      } else {
        for (int i = 0; i < playerCount; ++i) {
          final int color = this.gameSession.gameState.players[i].color2;
          int x1 = graphX;
          int y1 = graphWidth - (graphHeight * graphData[i][0] / graphMax);
          int y1a = y1;

          for (int j = 1; j < turnCount; ++j) {
            final int x2 = graphX + (((j * STATS_GRAPH_WIDTH) << 10) / graphSteps);
            final int y2 = graphWidth - (graphHeight * graphData[i][j] / graphMax);

            for (int k = 0; k < x2 - x1; ++k) {
              final int y2a = MathUtil.ease(k, x2 - x1, y1, y2);
              Drawing.line(x1 + k - 1, y1a, x1 + k, y2a, color, this.statsGraphAlpha);
              y1a = y2a;
            }

            x1 = x2;
            y1 = y2;
          }
        }
      }

      int var30 = (turnCount + 7) / 8;
      if (var30 == 0) {
        var30 = 1;
      }

      for (int i = 0; i < turnCount; ++i) {
        final int x1 = graphX + STATS_GRAPH_WIDTH * i / (turnCount - 1);
        if (i % var30 == 0) {
          final String var47 = Integer.toString(i);
          final int var34 = Menu.SMALL_FONT.measureLineWidth(var47);
          int var35 = -(var34 >> 1) + x1;
          if (i == 0) {
            var35 = graphX + var34 / 2;
          } else if (turnCount - 1 == i) {
            var35 -= var34;
          }

          Menu.SMALL_FONT.draw(Integer.toString(i), var35, 20 + STATS_SCREEN_Y + graphHeight + Menu.SMALL_FONT.ascent, Drawing.WHITE);
        }

        Drawing.verticalLineBlended(x1, 100, 222, 0x0c0c0c);
      }
    }

    Drawing.setBounds(statsX, STATS_SCREEN_Y, statsX + width, 440);
    if (this.gameSession.gameState.hasEnded || this.gameSession.localPlayer != null && this.gameSession.gameState.isPlayerDefeated(this.gameSession.localPlayer.index)) {
      Menu.SMALL_FONT.drawCentered(StringConstants.TEXT_ESC_TO_EXIT, statsX + 20 + 160, 366, Drawing.WHITE);
    }

    this.statsScreenTabs.forEach(UIComponent::draw);

    Drawing.restoreBoundsFrom(bounds);
    if (this.currentStatDescriptionTooltip != null && this._jb > 60) {
      this.drawTooltip(this.currentStatDescriptionTooltip);
    }
  }

  private void b050(final int var1, final int var2, final int var3, final int var5) {
    final int var6 = this.gameSession.gameState.playerCount;
    final int var7 = this._G[var5][var6 - 1];
    int var8 = this._G[var5][var6 - 2];
    if (this.gameSession.localPlayer != null && this.gameSession.localPlayer.index != var7) {
      var8 = this.gameSession.localPlayer.index;
    }

    final int var9 = this._ob[var1];

    Menu.drawPanel(var3, var2, 190, var9);
    Menu.SMALL_FONT.draw(ShatteredPlansClient.STAT_NAMES[var5], 10 + var3, var2 + Menu.SMALL_FONT.ascent, Drawing.WHITE);
    final short var10 = 210;
    final int var11 = var10 / (6 + var6);
    final int var12 = 2 * var11 + 12;
    final int var13 = -(var11 * 8) + var10;
    final int var14 = var9 - 27;
    if (this._p[var1] == 0) {
      this.a050(var7, var5, var3 + 10, Menu.SMALL_FONT.ascent + var2 + (var14 >> 1));
      this.a050(var8, var5, var3 + 10, var2 - (-Menu.SMALL_FONT.ascent - var14));
    } else {
      final int[] var15 = JagexBaseApplet.a612(this._G[var5]);

      for (int var16 = 0; var6 > var16; ++var16) {
        int var17 = (var6 * var14 - (var15[var16] * var14 >> 1)) / var6;
        final int var18 = (var6 - var15[var16]) * var14 / var6;
        int var19 = (-var12 + this._ob[var1] << 8) / var13;
        if (var16 == var7) {
          var17 = var14 >> 1;
          var19 = 256;
        }

        if (var16 == var8) {
          var17 = var14;
          var19 = 256;
        }

        final int var20 = Menu.SMALL_FONT.ascent + var2 + MathUtil.ease(this._p[var1], 32, var17, var18);
        this.a132(var5, var20, var16, var19, 10 + var3);
      }
    }

  }

  public void handleVictory() {
    if (this.endTurnButton != null) {
      this.endTurnButton.enabled = false;
    }

    this.productionPanel.visible = false;
    if (this.projectsPanel != null) {
      this.projectsPanel.visible = false;
    }

    this.diplomacyPanel.visible = false;
    this.fleetInfoPanel.visible = false;

    if (this.victoryPanel != null) {
      this.victoryPanel.visible = false;
    }
  }

  private void a132(final int var1, final int var2, final int var3, final int var4, final int var6) {
    final Player var7 = this.gameSession.gameState.players[var3];
    final int var8 = var7.color2;
    Menu.SMALL_FONT.drawRightAligned(this._l[var1][var3], var6 + 27, var2, var8, var4);
    Menu.SMALL_FONT.draw(StringConstants.TEXT_ORDINALS[this._q[var1][var3]], 37 + var6, var2, var8, var4);
    Menu.SMALL_FONT.draw(var7.name, var6 + 67, var2, var8, var4);
  }

  private void processInputStatsScreen() {
    final int var2 = this.statsScreenOpenAmount;
    final int var3 = Math.min(32, var2);

    final int var4 = MathUtil.ease(var3, 32, 20, 550);
    final int var5 = (-var4 + ShatteredPlansClient.SCREEN_WIDTH) / 2;
    final int var7 = 320 + var5 + 40 - 10;
    int var8 = 131;

    int var9;
    int var10;
    for (var9 = 0; var9 < 4; ++var9) {
      var10 = this._ob[var9];
      if (var7 <= JagexApplet.mouseX && var8 <= JagexApplet.mouseY && 190 + var7 > JagexApplet.mouseX && JagexApplet.mouseY <= var8 + var10) {
        break;
      }

      var8 += var10 + 8;
    }

    this.processMouseInput(this.statsScreenTabs);
    if (this.clickedComponent != null) {
      this.selectStatsScreenTab((StatsScreenTab) this.clickedComponent.data);
      if (this.fleetsTabButton.isActive()) {
        this.fleetsTabButton.toggle();
      }
      if (this.productionTabButton.isActive()) {
        this.productionTabButton.toggle();
      }
      if (this.systemsTabButton.isActive()) {
        this.systemsTabButton.toggle();
      }
      if (this.overviewTabButton.isActive()) {
        this.overviewTabButton.toggle();
      }
      ((Button<?>) this.clickedComponent).toggle();
    }

    if (var9 == 4) {
      this.currentStatDescriptionTooltip = null;
      this._jb = 0;
    } else {
      this._w = var9;
      var10 = this._w + this.currentStatsScreenTab.ordinal() * 4;
      final String var14 = ShatteredPlansClient.STAT_DESCS[var10];
      //noinspection StringEquality
      if (var14 == this.currentStatDescriptionTooltip) {
        ++this._jb;
      } else {
        this.currentStatDescriptionTooltip = ShatteredPlansClient.STAT_DESCS[var10];
        this._jb = 0;
      }
    }

  }

  private void a050(final int var1, final int var3, final int var4, final int var5) {
    final Player var6 = this.gameSession.gameState.players[var1];
    final int var7 = var6.color2;
    Menu.SMALL_FONT.drawRightAligned(this._l[var3][var1], var4 + 27, var5, var7);
    Menu.SMALL_FONT.draw(StringConstants.TEXT_ORDINALS[this._q[var3][var1]], var4 + 37, var5, var7);
    Menu.SMALL_FONT.draw(var6.name, 67 + var4, var5, var7);
  }

  public void a223(final String var1, final boolean var2) {
    if (var1.startsWith("production")) {
      this.productionPanel.visible = var2;
    }

    if (var1.startsWith("projects")) {
      this.projectsPanel.visible = var2;
    }

    if (var1.startsWith("fleets")) {
      this.fleetInfoPanel.visible = var2;
    }

    if (var1.startsWith("diplomacy")) {
      this.diplomacyPanel.visible = var2;
    }

    if (var1.startsWith("victory") && this.victoryPanel != null) {
      this.victoryPanel.visible = var2;
    }

  }

  private void updateProjectsPanel() {
    this.projectsPanel.content.removeChildren();
    this.projectsPanel.flashing = false;
    this.projectsButton.deactivate();
    final ProjectsPanelState var2 = this.projectsPanel.state;
    for (int i = 0; i < GameState.NUM_RESOURCES; ++i) {
      final ScrollView<?> var4 = a301e(this.gameSession.localPlayer.researchPoints[i], i, var2);
      var4.setPosition(this.projectsPanel.content.x, this.projectsPanel.content.contentHeight + this.projectsPanel.content.y);
      this.projectsPanel.content.addChild(var4);
      if (this.gameSession.localPlayer.researchPoints[i] >= 5) {
        this.projectsPanel.flashing = true;
        this.projectsButton.activate();
      }
    }
  }

  private void b115(final int var1, final int var2, final int var4) {
    final String var5 = StringConstants.TURN_OBJECTIVE.toUpperCase() + ": " + StringConstants.GAME_TYPE_TOOLTIPS[2];
    Menu.SMALL_FONT.drawParagraph(var5, var4 + 5, var2 + 5, var1 - 10, ShatteredPlansClient.SCREEN_HEIGHT, Drawing.WHITE, Font.HorizontalAlignment.LEFT, Font.VerticalAlignment.TOP, Menu.SMALL_FONT.ascent);
    final int var6 = FACTION_RING.width * 2 + Menu.SMALL_FONT.measureLineWidth("= " + StringConstants.TURN_ONE_POINT);
    Drawing.fillRoundedRect((var1 - var6) / 2 + (var4 - 10), 15 + var2 + Menu.SMALL_FONT.ascent, var6 + 20, 10 + FACTION_RING.height * 3, 5, 0, 128);
    int var7 = 29 + Menu.SMALL_FONT.ascent + var2;
    final int var8 = FACTION_RING.width + Menu.SMALL_FONT.measureLineWidth("= " + Strings.format(StringConstants.TURN_POINTS, Integer.toString(3)));
    FACTION_RING_CENTER.draw((-FACTION_RING_CENTER.width + FACTION_RING.width) / 2 + (var1 - var8) / 2 + var4, var7);
    FACTION_RING.draw((-var8 + var1) / 2 + var4, var7 - 9);
    Drawing.drawCircleGradientAdd(GameView.systemScoreOrbXOffsets[0] + (var4 + (FACTION_RING.width + (var1 - var8)) / 2 << 4), GameView.systemScoreOrbYOffsets[0] + (var7 + FACTION_RING_CENTER.height / 2 << 4), 50, 7, SYSTEM_SCORE_ORB_GRADIENT);
    Drawing.drawCircleGradientAdd((var4 + (FACTION_RING.width + var1 - var8) / 2 << 4) + GameView.systemScoreOrbXOffsets[2], (FACTION_RING_CENTER.height / 2 + var7 << 4) + GameView.systemScoreOrbYOffsets[2], 50, 7, SYSTEM_SCORE_ORB_GRADIENT);
    Drawing.drawCircleGradientAdd(GameView.systemScoreOrbXOffsets[3] + ((FACTION_RING.width - var8 + var1) / 2 + var4 << 4), (FACTION_RING_CENTER.height / 2 + var7 << 4) + GameView.systemScoreOrbYOffsets[3], 50, 7, SYSTEM_SCORE_ORB_GRADIENT);
    Menu.SMALL_FONT.draw("= " + Strings.format(StringConstants.TURN_POINTS, Integer.toString(3)), FACTION_RING.width + var4 + (-var8 + var1) / 2 + 5, FACTION_RING.height / 2 + var7 - 4, Drawing.WHITE);
    var7 += FACTION_RING.height;
    FACTION_RING_CENTER.draw(var4 + (-var8 + var1) / 2 + (-FACTION_RING_CENTER.width + FACTION_RING.width) / 2, var7);
    FACTION_RING.draw((var1 - var8) / 2 + var4, var7 - 9);
    Drawing.drawCircleGradientAdd((var4 + (FACTION_RING.width - var8 + var1) / 2 << 4) + GameView.systemScoreOrbXOffsets[0], GameView.systemScoreOrbYOffsets[0] + (FACTION_RING_CENTER.height / 2 + var7 << 4), 50, 7, SYSTEM_SCORE_ORB_GRADIENT);
    Drawing.drawCircleGradientAdd(((FACTION_RING.width - var8 + var1) / 2 + var4 << 4) + GameView.systemScoreOrbXOffsets[1], GameView.systemScoreOrbYOffsets[1] + (FACTION_RING_CENTER.height / 2 + var7 << 4), 50, 7, SYSTEM_SCORE_ORB_GRADIENT);
    Menu.SMALL_FONT.draw("= " + Strings.format(StringConstants.TURN_POINTS, Integer.toString(2)), 5 + FACTION_RING.width + (var1 - var8) / 2 + var4, var7 + FACTION_RING.height / 2 - 4, Drawing.WHITE);
    var7 += FACTION_RING.height;
    FACTION_RING_CENTER.draw(var4 + (-var6 + var1) / 2 + (-FACTION_RING_CENTER.width + FACTION_RING.width) / 2 - 5, var7);
    FACTION_RING.draw((-var6 + var1) / 2 + var4 - 5, var7 - 9);
    Drawing.drawCircleGradientAdd(GameView.systemScoreOrbXOffsets[0] + ((FACTION_RING.width + (var1 - var6)) / 2 + var4 - 5 << 4), GameView.systemScoreOrbYOffsets[0] + (var7 + FACTION_RING_CENTER.height / 2 << 4), 50, 7, SYSTEM_SCORE_ORB_GRADIENT);
    FACTION_RING_CENTER.draw(FACTION_RING.width + (-var6 + var1) / 2 + var4 - 5 + (-FACTION_RING_CENTER.width + FACTION_RING.width) / 2, var7);
    FACTION_RING.draw(var4 + (-var6 + var1) / 2 + (FACTION_RING.width - 5), var7 - 9);
    Menu.SMALL_FONT.draw("= " + StringConstants.TURN_ONE_POINT, 2 * FACTION_RING.width + (-var6 + var1) / 2 + var4, FACTION_RING.height / 2 + (var7 - 4), Drawing.WHITE);
  }

  private void c115(final int var1, final int var3, final int var4) {
    final String var5 = StringConstants.TURN_OBJECTIVE.toUpperCase() + ": " + StringConstants.GAME_TYPE_TOOLTIPS[1];
    Menu.SMALL_FONT.drawParagraph(var5, 5 + var1, var3 + 5, var4 - 10, ShatteredPlansClient.SCREEN_HEIGHT, Drawing.WHITE, Font.HorizontalAlignment.LEFT, Font.VerticalAlignment.TOP, Menu.SMALL_FONT.ascent);
    final int var6 = StarSystem.PLANET_SCORCHED_EARTH.width + Menu.SMALL_FONT.measureLineWidth("= " + StringConstants.TURN_ONE_POINT);
    Drawing.fillRoundedRect((var4 - var6) / 2 - 5 + var1, 20 + Menu.SMALL_FONT.ascent + var3, var6 + 15, StarSystem.PLANET_SCORCHED_EARTH.height + 10, 5, 0, 128);
    StarSystem.PLANET_SCORCHED_EARTH.draw(var1 + (var4 - var6) / 2, Menu.SMALL_FONT.ascent + var3 + 20);
    Menu.SMALL_FONT.draw("= " + StringConstants.TURN_ONE_POINT, (var4 - var6) / 2 + var1 + StarSystem.PLANET_SCORCHED_EARTH.width, 25 + Menu.SMALL_FONT.ascent + StarSystem.PLANET_SCORCHED_EARTH.height / 2 + var3, Drawing.WHITE);
  }

  private void selectStatsScreenTab(final StatsScreenTab tab) {
    final GameState state = this.gameSession.gameState;
    final int playerCount = state.playerCount;
    if (tab == this.currentStatsScreenTab) {
      this.statsGraphData = this.targetStatsGraphData;
    } else {
      if (this.statsGraphTurnAdvanceAnimationCounter == 0) {
        this.statsGraphData = this.targetStatsGraphData;
      } else {
        this.statsGraphData = this.getAnimatedStatsGraphData();
      }
      this.statsGraphTurnAdvanceAnimationCounter = 1;
      this.currentStatsScreenTab = tab;
    }

    final int turnCount = Math.min(state.turnNumber - this.gameSession.turnNumberWhenJoined + 1, 100);
    if (this.statsGraphData == null || turnCount == this.statsGraphData[0].length) {
      this.statsGraphStartTurn = turnCount;
    } else {
      final int startTurn = this.statsGraphData[0].length;
      final int len = Math.min(turnCount, startTurn);
      final int offset = Math.max(startTurn - turnCount, 0);

      final int[][] statsGraphData = new int[playerCount][turnCount];
      for (int playerIndex = 0; playerIndex < playerCount; ++playerIndex) {
        System.arraycopy(this.statsGraphData[playerIndex], offset, statsGraphData[playerIndex], 0, len);
        for (int turnIndex = startTurn; turnIndex < turnCount; ++turnIndex) {
          statsGraphData[playerIndex][turnIndex] = this.statsGraphData[playerIndex][startTurn - 1];
        }
      }

      this.statsGraphTurnAdvanceAnimationCounter = 1;
      this.statsGraphData = statsGraphData;
      this.statsGraphStartTurn = startTurn;
    }

    this.statsGraphTurnCount = turnCount;
    if (turnCount > 1) {
      this.targetStatsGraphData = new int[playerCount][];
      for (final Player player : state.players) {
        final PlayerStats var17 = player.stats;
        final int[] var18 = this.currentStatsScreenTab == StatsScreenTab.PRODUCTION ? var17.production
            : this.currentStatsScreenTab == StatsScreenTab.SYSTEMS ? var17.systems
            : var17.fleets;
        final int[] var19 = new int[turnCount];
        for (int turnIndex = 0; turnIndex < turnCount; ++turnIndex) {
          final int var14 = (1 + turnIndex + state.turnNumber + (100 - turnCount)) % 100;
          var19[turnIndex] = var18[var14] << 10;
        }

        this.targetStatsGraphData[player.index] = var19;
      }
    } else {
      this.targetStatsGraphData = null;
    }
  }

  public void markProjectPending(@MagicConstant(valuesFromClass = GameState.ResourceType.class) final int type) {
    if (this.projectsPanel.state.statusLabels[type] != null) {
      this.projectsPanel.state.statusLabels[type].text = StringConstants.PENDING;
    }

    final ScrollView<?> var4 = this.projectsPanel.state._c[type];
    var4.tooltip = StringConstants.TOOLTIP_PROJECT_PENDING;

    for (final UIComponent<?> var5 : var4.children) {
      if (var5.tooltip != null) {
        var5.tooltip = StringConstants.TOOLTIP_PROJECT_PENDING;
      }
    }
  }

  private void e150() {
    this.fleetInfoPanel.content.removeChildren();
    final int[] var2 = new int[this.gameSession.gameState.playerCount];
    final StarSystem[] var3 = this.gameSession.gameState.map.systems;

    int var4;
    int var10001;
    for (var4 = 0; var4 < var3.length; ++var4) {
      final StarSystem var5 = var3[var4];
      if (var5.owner != null) {
        var10001 = var5.owner.index;
        var2[var10001] += var5.garrison;
      }
    }

    final Player[] var10 = this.gameSession.gameState.players;

    for (var4 = 0; var10.length > var4; ++var4) {
      final Player var13 = var10[var4];
      if (!this.gameSession.gameState.gameOptions.unifiedTerritories) {
        for (final ContiguousForce var6 : var13.contiguousForces) {
          if (var6.fleetProduction > 0) {
            var10001 = var13.index;
            var2[var10001] += var6.fleetProduction;
          }
        }
      } else if (var13.combinedForce.fleetProduction > 0) {
        var10001 = var13.index;
        var2[var10001] += var13.combinedForce.fleetProduction;
      }
    }

    int var11 = 0;

    int var14;
    for (var14 = 0; var2.length > var14; ++var14) {
      final int var16 = var2[var14];
      if (var16 > var11) {
        var11 = var16;
      }
    }

    final Player[] var15 = this.gameSession.gameState.players;

    for (var14 = 0; var15.length > var14; ++var14) {
      final Player var17 = var15[var14];
      final int var7 = var2[var17.index];
      final int var8 = var17._v.offsetX / 2 + (-var17._v.offsetX + INFO_PANEL_CONTENT_WIDTH) * var7 / var11;
      final ScrollView<?> var9 = a755bp(var7, var17, var8);
      var9.setPosition(this.fleetInfoPanel.content.x, this.fleetInfoPanel.content.contentHeight + this.fleetInfoPanel.content.y);
      this.fleetInfoPanel.content.addChild(var9);
    }

  }

  public void handleProjectOrderCanceled(@MagicConstant(valuesFromClass = GameState.ResourceType.class) final int type) {
    final ProjectsPanelState panelState = this.projectsPanel.state;
    if (panelState.statusLabels[type] != null) {
      panelState.statusLabels[type].text = StringConstants.TEXT_READY;
    }

    final ScrollView<?> var4 = panelState._c[type];
    var4.tooltip = StringConstants.TOOLTIP_PROJECT_COMPLETE;

    for (final UIComponent<?> child : var4.children) {
      if (child.tooltip != null) {
        child.tooltip = StringConstants.TOOLTIP_PROJECT_COMPLETE;
      }
    }
  }

  public void a735(final Player[] var1) {
    final String[] var3 = new String[1 + var1.length];
    final boolean[] var4 = new boolean[var1.length + 1];

    for (int var5 = 0; var5 < this.playerDiplomacyStatusMessage.length - 1; ++var5) {
      var3[var5] = this.playerDiplomacyStatusMessage[var5];
      var4[var5] = this._z[var5];
    }

    var3[var3.length - 1] = this.playerDiplomacyStatusMessage[this.playerDiplomacyStatusMessage.length - 1];
    var4[var4.length - 1] = this._z[this._z.length - 1];
    this.playerDiplomacyStatusMessage = var3;
    this._z = var4;
    this.e150();
    final int var5 = this.fleetInfoPanel.height;
    this.fleetInfoPanel.a183(this.fleetInfoPanel.content.contentHeight + 28, this.fleetInfoPanel.width);
    this.fleetInfoPanel.content.a183(this.fleetInfoPanel.content.contentHeight + 10, this.fleetInfoPanel.content.width);
    this.fleetInfoPanel.translate(0, -this.fleetInfoPanel.height + var5);
    this.f150();
    this.initialize();
    final Player[] var6 = this.gameSession.gameState.players;

    for (final Player var8 : var6) {
      this.updateDiplomacyState(var8);
    }

    this.diplomacyPanel.a183(this.diplomacyPanel.content.contentHeight + 28, this.diplomacyPanel.width);
    this.diplomacyPanel.content.a183(10 + this.diplomacyPanel.content.contentHeight, this.diplomacyPanel.content.width);
    final int[][] var9 = new int[var1.length][];
    final int[][] var10 = new int[var1.length][];
    if (this.targetStatsGraphData != null) {
      System.arraycopy(this.targetStatsGraphData, 0, var9, 0, this.targetStatsGraphData.length);
      var9[var1.length - 1] = new int[var9[0].length];
      this.targetStatsGraphData = var9;
    }

    if (this.statsGraphData != null) {
      System.arraycopy(this.statsGraphData, 0, var10, 0, this.statsGraphData.length);
      var10[var1.length - 1] = new int[var10[0].length];
      this.statsGraphData = var10;
    }

    if (TutorialState.stage == 7) {
      this.gameSession.gameState.victoryChecker = new TutorialVictoryChecker(var1);
      this.gameSession.gameState.victoryChecker.updateVictoryPanel(this.gameSession.gameState, this);
    }
  }

  public void draw() {
    if (this.gameSession.isTutorial) {
      TutorialState.draw();
    }

    if (this.gameSession.isMultiplayer && !this.gameSession.gameState.hasEnded) {
      final int readyButtonX = getReadyButtonX();
      Drawing.fillRoundedRect(readyButtonX, 4, READY_BUTTON.width, Menu.SMALL_FONT.ascent - 2, 2, 0, 128);
      final int var2 = (this.gameSession.turnTicksLeft + 49) / 50;
      final int turnTimerX = readyButtonX + (READY_BUTTON.width / 2);
      if (this.gameSession.turnTicksLeft < 0) {
        Menu.SMALL_FONT.drawCentered(this.a436(Math.abs(var2)), turnTimerX, Menu.SMALL_FONT.ascent * 3 / 4 + 3, Drawing.RED);
      }

      if (this.gameSession.turnTicksLeft * 3 >= GameSession.TURN_DURATIONS[this.gameSession.gameState.turnLengthIndex]) {
        Menu.SMALL_FONT.drawCentered(this.a436(Math.abs(var2)), turnTimerX, Menu.SMALL_FONT.ascent * 3 / 4 + 3, Drawing.WHITE);
      } else {
        final int var3 = this.gameSession.turnTicksLeft % 50;
        if (var3 >= 30 || var3 < 20 && var3 >= 10) {
          Menu.SMALL_FONT.drawCentered(this.a436(Math.abs(var2)), turnTimerX, 3 * Menu.SMALL_FONT.ascent / 4 + 3, 0x3ca4a7);
        }
      }
    }

    if (this.gameSession.isTutorial && TutorialState._phg) {
      _kbw.draw(this.endTurnButton.x - 10, this.endTurnButton.y - 10);
    }

    for (final ListIterator<UIComponent<?>> it = this.components.listIterator(this.components.size()); it.hasPrevious(); ) {
      final UIComponent<?> var12 = it.previous();
      if (!(var12 instanceof FloatingPanel)) {
        var12.draw();
      }
    }

    d150vn();
    if (this.chatPanelSinglePlayer != null) {
      this.chatPanelSinglePlayer.draw(false);
    }

    if (this._U != -1) {
      final int var4 = this._x;
      final int var5 = this.statusPanel.y + this.statusPanel.height + 5;
      Drawing.fillRoundedRect(this.statusPanel.x + 1, var5, this._T.width + 10, Menu.SMALL_FONT.ascent, 5, 0);
      Drawing.f669(this.statusPanel.x, var5 - 1, 12 + this._T.width, Menu.SMALL_FONT.ascent + 2, 6, 2052949);
      TutorialState.a833sa(var4 * 5, this.statusPanel.x + 6, 32, this._T, var5 - 4);
      if (this._ib) {
        final int var6 = this._x - 125;
        if (var6 > 0) {
          int var7 = (var6 << 12) / 250;
          var7 &= 511;
          if (var7 > 256) {
            var7 = 512 - var7;
          }

          this._eb.drawAdd(6 + this.statusPanel.x, var5 - 4, var7);
        }
      }
    }

    int var4 = ShatteredPlansClient.currentTick % 64;
    if (var4 > 24 && var4 <= 40) {
      var4 = 24;
    }
    if (var4 > 40) {
      var4 = 64 - var4;
    }

    var4 = var4 * 10;
    final int var5 = Drawing.alphaOver(Drawing.RED, Drawing.WHITE, var4);
    if (this.productionPanel.flashing && this.productionButton.visible) {
      HUD_ICON_NO_BASE_2.drawTinted2(this.productionButton.x, this.productionButton.y, var5);
    }

    if (this.projectsPanel != null && this.projectsPanel.flashing && this.projectsButton.visible) {
      HUD_ICON_NO_BASE_3.drawTinted2(this.projectsButton.x, this.projectsButton.y, var5);
    }

    if (this.fleetInfoPanel.flashing && this.fleetInfoButton.visible) {
      HUD_ICON_NO_BASE_4.drawTinted2(this.fleetInfoButton.x, this.fleetInfoButton.y, var5);
    }

    if (this.diplomacyPanel.flashing && this.diplomacyButton.visible) {
      HUD_ICON_NO_BASE_1.drawTinted2(this.diplomacyButton.x, this.diplomacyButton.y, var5);
    }

    if (this.victoryPanel != null && this.victoryPanel.flashing && this.victoryButton.visible) {
      HUD_ICON_NO_BASE_5.drawTinted2(this.victoryButton.x, this.victoryButton.y, var5);
    }

    for (final ListIterator<UIComponent<?>> it = this.components.listIterator(this.components.size()); it.hasPrevious(); ) {
      final UIComponent<?> var12 = it.previous();
      if (var12 instanceof FloatingPanel) {
        var12.draw();
      }
    }

    final boolean var13;
    boolean var14 = false;
    String var8 = null;
    boolean var9 = false;
    final int var10 = Drawing.WHITE;
    if (this.gameSession.gameState.hasEnded) {
      var13 = this.gameSession.gameState.winnerIndex < 0;
      if (this.gameSession.localPlayer != null && (this.gameSession.gameState.isPlayerDefeated(this.gameSession.localPlayer.index) || this.gameSession.gameState.victoryChecker.isLoser(this.gameSession.localPlayer))) {
        var14 = true;
      }

      var8 = !var14 ? (!var13 ? StringConstants.TEXT_VICTORY : StringConstants.TEXT_PEACE) : StringConstants.TEXT_DEFEAT;
      if (this.gameSession.localPlayer == null && !var13) {
        var8 = Strings.format(StringConstants.TEXT_PLAYER_HAS_WON, this.gameSession.gameState.players[this.gameSession.gameState.winnerIndex].name);
      }
    }

    if (this.gameSession.localPlayer != null && this.gameSession.gameState.isPlayerDefeated(this.gameSession.localPlayer.index) && this._I) {
      var9 = true;
      var8 = StringConstants.TEXT_DEFEAT;
    }

    if (var8 != null) {
      Menu.FONT.drawCentered(var8, ShatteredPlansClient.SCREEN_CENTER_X, ShatteredPlansClient.SCREEN_CENTER_Y, var10, GameView.uiPulseCounter);
      int var11 = Menu.SMALL_FONT.ascent + ShatteredPlansClient.SCREEN_CENTER_Y + 5;
      //noinspection StringEquality
      if (var8 == StringConstants.TEXT_PEACE) {
        final String s = this.gameSession.gameState.anyPlayersDefeated() ? StringConstants.TEXT_PEACE_3 : StringConstants.TEXT_PEACE_2;
        Menu.SMALL_FONT.drawCentered(s, ShatteredPlansClient.SCREEN_CENTER_X, var11, var10, GameView.uiPulseCounter);
        var11 += 3 + Menu.SMALL_FONT.ascent;
      }

      Menu.SMALL_FONT.drawCentered(StringConstants.TEXT_TOGGLE_STATS, ShatteredPlansClient.SCREEN_CENTER_X, var11, var10, GameView.uiPulseCounter);
      if (var9) {
        var11 += Menu.SMALL_FONT.ascent + 3;
        Menu.SMALL_FONT.drawCentered(StringConstants.TEXT_TOGGLE_STATS_2, ShatteredPlansClient.SCREEN_CENTER_X, var11, var10, GameView.uiPulseCounter);
      }
    }

    if (this.newTurnPanelOpenAmount != 0) {
      this.drawNewTurnPanel();
    }

    if (this.statsScreenOpenAmount != 0) {
      this.drawStatsScreen();
    }

    if (this.hoveredComponent != null && this.hoveredComponent.tooltip != null) {
      this.drawTooltip(this.hoveredComponent.tooltip);
    }
  }

  private void a652(final int var1, final int var2, final int var4) {
    final String var5 = StringConstants.TURN_OBJECTIVE.toUpperCase() + ": " + StringConstants.GAME_TYPE_TOOLTIPS[0];
    Menu.SMALL_FONT.drawParagraph(var5, 5 + var1, var4 + 5, var2 - 10, ShatteredPlansClient.SCREEN_HEIGHT, Drawing.WHITE, Font.HorizontalAlignment.LEFT, Font.VerticalAlignment.TOP, Menu.SMALL_FONT.ascent);
  }

  public boolean isProductionWindowOpen() {
    return this.productionPanel.visible;
  }

  public void updateAvailableFleetCounters() {
    final ProductionPanelState var2 = this.productionPanel.state;
    for (int i = 0; i < var2.buildFleetsLabels.size(); ++i) {
      final Label button = var2.buildFleetsLabels.get(i);
      final Force force = button.data;
      button.text = Integer.toString(force.fleetsAvailableToBuild);
      button.tooltip = var2.buildFleetsButtons.get(i).tooltip = ProductionPanelState.buildingFleetsTooltip(force.fleetsAvailableToBuild);
    }
  }

  public void initialize(final ClientGameSession session) {
    this.gameSession = session;
    this.initialize();
  }

  public void setActionHint(final String message) {
    final StatusPanelState var4 = (StatusPanelState) this.statusPanel.state;

    if (Menu.SMALL_FONT.measureLineWidth(message) < 400) {
      var4.label.setTextAndLeftAlign(message);
      var4.label.setPosition(15, -(3 * Menu.SMALL_FONT.ascent / 4) + (this.statusPanel.y + this.statusPanel.height / 2 - 1));
    } else {
      final int var5 = message.indexOf(" ", message.length() / 2);
      final String var6 = message.substring(0, var5) + "<br>" + message.substring(var5 + 1);
      var4.label.setTextAndLeftAlign(var6);
      var4.label.setPosition(15, -(Menu.SMALL_FONT.ascent * 5 / 4) + (this.statusPanel.y + this.statusPanel.height / 2 - 1));
    }
  }

  private void a423() {
    this.newTurnPanelOpenAmount = 0;
    if (this.animationAutoPlayButton.isActive() && this.gameSession.turnEventLog != null) {
      this.gameSession.gameView.advanceAnimationPhase(AbstractGameView.AnimationPhase.BUILD, this.gameSession.turnEventLog.events);
      this.animationPlayingButton.activate();
      this.animationPlayingButton.tooltip = StringConstants.TOOLTIP_ANIM_CLICK_TO_STOP;
    } else {
      this.gameSession.gameView.advanceAnimationPhase(AbstractGameView.AnimationPhase.NOT_PLAYING, Collections.emptyList());
      this.gameSession.recalculateSystemState();
      this.animationPlayingButton.deactivate();
      this.animationPlayingButton.tooltip = StringConstants.TOOLTIP_ANIM_CLICK_TO_PLAY;
    }
  }

  public void h150() {
    if (this.gameSession.isTutorial) {
      if (JagexApplet.lastTypedKeyCode == KeyState.Code.ENTER) {
        TutorialState.a984fl("enter");
      }
    }

    if (JagexApplet.lastTypedKeyCode == KeyState.Code.ENTER) {
      this.gameSession.gameView.stopCombatAnimations();
      this.gameSession.recalculateSystemState();
    }

    if (JagexApplet.lastTypedKeyCode == KeyState.Code.SPACE) {
      if (this.gameSession.gameState.playerCount == 1) {
        return;
      }

      this.isStatsScreenOpen = !this.isStatsScreenOpen;
      if (this.isStatsScreenOpen && this.gameSession.localPlayer != null && this.gameSession.gameState.isPlayerDefeated(this.gameSession.localPlayer.index)) {
        this._I = false;
      }

      final int var2 = this.gameSession.gameState.playerCount;
      this._l = new String[16][var2];
      this._G = new int[16][];
      final int[][] var3 = new int[16][var2];
      final Player[] var4 = this.gameSession.gameState.players;

      int var5;
      for (var5 = 0; var5 < var4.length; ++var5) {
        final Player var6 = var4[var5];
        final int[] var7 = var6.stats.b341();
        final String[] var8 = var6.stats.a061();

        for (int var9 = 0; var9 < 16; ++var9) {
          var3[var9][var6.index] = var7[var9];
          this._l[var9][var6.index] = var8[var9];
        }
      }

      this._q = new int[16][var2];

      for (int var10 = 0; var10 < 16; ++var10) {
        this._G[var10] = GameState.calculateRanksAscending(var3[var10]);

        for (var5 = 0; var2 > var5; ++var5) {
          int var11 = 0;

          for (int var12 = 0; var12 < var2; ++var12) {
            if (var3[var10][var12] <= var3[var10][var5]) {
              ++var11;
            }
          }

          this._q[var10][var5] = var2 - var11;
        }
      }
    }

    if (JagexApplet.lastTypedKeyCode == KeyState.Code.LETTER_C) {
      isChatOpen = !isChatOpen;
      this.showChatButton.toggle();
    }
  }

  public int getHeight() {
    if (this.gameSession.isTutorial) {
      return ShatteredPlansClient.SCREEN_HEIGHT;
    } else {
      return this.gameSession.isMultiplayer ? Component.lobbyChatPanel.y : this.chatPanelSinglePlayer.y;
    }
  }

  private void m150() {
    currentSettings = 0;
    if (this.animationAutoPlayButton.isActive()) {
      currentSettings |= 32;
    }

    if (this.animationSpeedDoubledButton.isActive()) {
      currentSettings |= 64;
    }

  }

  private void drawNewTurnPanel() {
    int var2 = 32;
    if (this.newTurnPanelOpenAmount >= 32) {
      if (this.newTurnPanelOpenAmount > 59968) {
        var2 = '\uea60' - this.newTurnPanelOpenAmount;
      }
    } else {
      var2 = this.newTurnPanelOpenAmount;
    }

    final int var3 = MathUtil.ease(var2, 32, 21, 450);
    fadeRect(0, 0, ShatteredPlansClient.SCREEN_WIDTH, ShatteredPlansClient.SCREEN_HEIGHT, (-var2 + 32 << 6) / 32 + 192);
    final int var4 = (ShatteredPlansClient.SCREEN_WIDTH - var3) / 2;
    final byte var5 = 80;
    final short var6 = 250;
    Menu.drawPanel(var4, var5, var3, var6, 3974311, true);
    final int[] var7 = new int[4];
    Drawing.saveBoundsTo(var7);
    Drawing.setBounds(var4 + Menu.SMALL_FONT.ascent / 2, var5, -(Menu.SMALL_FONT.ascent / 2) + var4 + var3, 330);
    final VictoryChecker var9 = this.gameSession.gameState.victoryChecker;
    String var8;
    if (var9 instanceof CaptureAndHoldVictoryChecker) {
      var8 = StringConstants.TEXT_MAP_SOL;
    } else if (var9 instanceof PointsVictoryChecker) {
      var8 = StringConstants.TEXT_MAP_POINTS;
    } else if (var9 instanceof DerelictsVictoryChecker) {
      var8 = StringConstants.TEXT_MAP_DERELICTS;
    } else {
      var8 = StringConstants.TEXT_MAP_HEX;
    }

    var8 = var8 + "   ";
    if (this.gameSession.gameState.gameOptions.simpleGarrisoning) {
      var8 = var8 + StringConstants.TEXT_GARRISON_NO;
    } else {
      var8 = var8 + StringConstants.TEXT_GARRISON_YES;
    }

    Menu.SMALL_FONT.draw(var8.toUpperCase(), 10 + var4, Menu.SMALL_FONT.ascent + var5, Drawing.WHITE);
    Menu.drawPanel(var4 + Menu.SMALL_FONT.ascent / 2, Menu.SMALL_FONT.ascent + var5 + 5, -Menu.SMALL_FONT.ascent + 450 + 1, 10 + FACTION_ICONS_LARGE[0].height, 0);
    int var10 = 0;
    if (this.gameSession.localPlayer != null) {
      var10 = this.gameSession.localPlayer.index;
    }

    final Sprite var11 = new Sprite(FACTION_ICONS_LARGE[0].height, -Menu.SMALL_FONT.ascent + 450 - (10 + FACTION_ICONS_LARGE[0].width / 2));
    Drawing.saveContext();
    var11.installForDrawing();
    Drawing.fillRectangleVerticalGradient(0, 0, var11.width, var11.height, this.gameSession.gameState.players[var10].color1, 0);
    Drawing.restoreContext();
    var11.f797();
    var11.draw(5 + Menu.SMALL_FONT.ascent / 2 + var4, Menu.SMALL_FONT.ascent + var5 + 10, 64);
    final ArgbSprite var12 = new ArgbSprite(FACTION_ICONS_LARGE[0].width, FACTION_ICONS_LARGE[0].height);

    for (int var13 = 0; FACTION_ICONS_LARGE[var10].pixels.length > var13; ++var13) {
      if ((-16777216 & FACTION_ICONS_LARGE[var10].pixels[var13]) == 0) {
        var12.pixels[var13] = 0;
      } else {
        var12.pixels[var13] = -16777216;
      }
    }

    var12.draw(5 + var4 + Menu.SMALL_FONT.ascent / 2 + (var11.width - FACTION_ICONS_LARGE[0].width / 2), 10 + var5 + Menu.SMALL_FONT.ascent);
    FACTION_ICONS_LARGE[var10].draw(-(FACTION_ICONS_LARGE[0].width / 2) + var11.width + Menu.SMALL_FONT.ascent / 2 + var4 + 5, Menu.SMALL_FONT.ascent + var5 + 10, 64);
    final String var16 = Strings.format(StringConstants.TURN_ORDINAL, Integer.toString(1 + this.gameSession.gameState.turnNumber));
    final int var14 = 10 + FACTION_ICONS_LARGE[0].height + var5 + 20 + Menu.FONT.ascent;
    Menu.FONT.drawCentered(var16, 320, var14, Drawing.WHITE);
    final int var15 = this.newTurnPanelOpenAmount - 32;
    if (var15 > 0) {
      if (var15 >= 32) {
        Menu.SMALL_FONT.drawCentered(this.turnName, 320, var14 + Menu.SMALL_FONT.ascent + 3, 2458760);
      } else {
        Menu.SMALL_FONT.drawCentered(this.turnName, 320, 3 + var14 + Menu.SMALL_FONT.ascent, 2458760, var15 * 8);
      }
    }

    if (var9 instanceof ConquestVictoryChecker) {
      this.a652(var4 + Menu.SMALL_FONT.ascent / 2 + 5, 439 - Menu.SMALL_FONT.ascent, var5 + Menu.SMALL_FONT.ascent);
    }

    if (var9 instanceof CaptureAndHoldVictoryChecker) {
      this.c115(Menu.SMALL_FONT.ascent / 2 + var4 + 5, Menu.SMALL_FONT.ascent + var5, 450 - Menu.SMALL_FONT.ascent - 11);
    }

    if (var9 instanceof PointsVictoryChecker) {
      this.b115(-Menu.SMALL_FONT.ascent + 450 - 11, Menu.SMALL_FONT.ascent + var5, Menu.SMALL_FONT.ascent / 2 + var4 + 5);
    }

    if (var9 instanceof DerelictsVictoryChecker) {
      this.a115(5 + Menu.SMALL_FONT.ascent / 2 + var4, -Menu.SMALL_FONT.ascent + 439, Menu.SMALL_FONT.ascent + var5);
    }

    Drawing.restoreBoundsFrom(var7);
  }

  private int[][] getAnimatedStatsGraphData() {
    if (this.targetStatsGraphData == null) {
      return null;
    }
    if (this.statsGraphData == null || this.statsGraphData[0] == null) {
      this.statsGraphTurnAdvanceAnimationCounter = 0;
      this.statsGraphData = this.targetStatsGraphData;
      this.statsGraphStartTurn = this.statsGraphTurnCount;
    }

    if (this.targetStatsGraphData[0].length != this.statsGraphData[0].length) {
      return null;
    } else if (this.statsGraphTurnAdvanceAnimationCounter == 0) {
      return this.statsGraphData;
    } else {
      final int playerCount = this.gameSession.gameState.playerCount;
      final int turnCount = this.targetStatsGraphData[0].length;
      final int[][] data = new int[playerCount][];

      for (int i = 0; i < playerCount; ++i) {
        final int[] prev = this.statsGraphData[i];
        final int[] next = this.targetStatsGraphData[i];
        data[i] = IntStream.range(0, turnCount)
            .map(j -> MathUtil.ease(this.statsGraphTurnAdvanceAnimationCounter, STATS_GRAPH_TURN_ADVANCE_ANIMATION_COUNTER_MAX, prev[j], next[j]))
            .toArray();
      }

      return data;
    }
  }

  public void destroy() {
    this.components.forEach(UIComponent::destroy);
    this.projectsPanel = null;
    this.animationAutoPlayButton = null;
    this.productionPanel = null;
    this.diplomacyPanel = null;
    this.victoryButton = null;
    this.fleetInfoPanel = null;
    this.diplomacyButton = null;
    this.animationSpeedDoubledButton = null;
    this.projectsButton = null;
    this.victoryPanel = null;
    this.fleetInfoButton = null;
    this.statusPanel = null;
    this.endTurnButton = null;
    this.animationPlayingButton = null;
    this.productionButton = null;
  }

  public void updateForTurnStart() {
    this.f150();
    if (this.projectsPanel != null) {
      this.updateProjectsPanel();
    }
    for (final Player player : this.gameSession.gameState.players) {
      this.updateDiplomacyState(player);
    }

    this.diplomacyPanel.flashing = false;
    this.diplomacyButton.deactivate();
    this.e150();
    if (this.fleetsTabButton.isActive()) {
      this.selectStatsScreenTab(StatsScreenTab.FLEETS);
    } else if (this.productionTabButton.isActive()) {
      this.selectStatsScreenTab(StatsScreenTab.PRODUCTION);
    } else if (this.systemsTabButton.isActive()) {
      this.selectStatsScreenTab(StatsScreenTab.SYSTEMS);
    } else if (this.overviewTabButton.isActive()) {
      this.selectStatsScreenTab(StatsScreenTab.OVERVIEW);
    }

    if (this.gameSession.isTutorial) {
      TutorialState.k150pe();
      this.a423();
    } else if (ClientGameSession.isAutoPlaying) {
      this.a423();
    } else {
      this.turnName = this.gameSession.gameState.turnName();
      this.newTurnPanelOpenAmount = 1;
      Sounds.play(Sounds.SFX_NEXT_OPEN);
    }
  }

  public boolean processInput(final boolean var1) {
    if (this.statsScreenOpenAmount != 0) {
      this.processInputStatsScreen();
      return true;
    } else if (this.newTurnPanelOpenAmount == 0) {
      return this.processInputHud(var1);
    } else {
      if (JagexApplet.mouseButtonJustClicked != MouseState.Button.NONE) {
        if (this.newTurnPanelOpenAmount < 32) {
          this.newTurnPanelOpenAmount = -this.newTurnPanelOpenAmount + 60000;
          Sounds.play(Sounds.SFX_NEXT_CLOSE);
        } else if (this.newTurnPanelOpenAmount < 59968) {
          this.newTurnPanelOpenAmount = 59968;
          Sounds.play(Sounds.SFX_NEXT_CLOSE);
        }
      }
      return true;
    }
  }

  private boolean processInputHud(final boolean var1) {
    final boolean var3 = this.hoveredComponent != null || Component.lobbyChatPanel.y <= JagexApplet.mouseY || this.chatPanelSinglePlayer != null && this.chatPanelSinglePlayer.y <= JagexApplet.mouseY;
    this.processMouseInput(this.components);
    if (this.mouseDownComponent != null) {
      for (final UIComponent<?> var4 : this.components) {
        if (var4 instanceof FloatingPanel<?> var5) {
          if (var5.visible && var5.hasChild(this.mouseDownComponent)) {
            this.a690(var5);
            var5.flashing = false;
            if (var5 == this.productionPanel) {
              this.productionButton.deactivate();
            }

            if (var5 == this.projectsPanel) {
              this.projectsButton.deactivate();
            }

            if (var5 == this.diplomacyPanel) {
              this.diplomacyButton.deactivate();
            }

            if (var5 == this.fleetInfoPanel) {
              this.fleetInfoButton.deactivate();
            }

            if (var5 == this.victoryPanel) {
              this.victoryButton.deactivate();
            }
            break;
          }
        }
      }
    }

    if (this.clickedComponent != null) {
      if (this.clickedComponent.data instanceof FloatingPanel<?> clickedTarget2) {
        clickedTarget2.visible = false;
        if (clickedTarget2 == this.productionPanel) {
          this.productionButton.tooltip = StringConstants.TOOLTIP_PRODUCTION_BUTTON_SHOW;
          this.productionButton.deactivate();
        }

        if (clickedTarget2 == this.projectsPanel) {
          this.projectsButton.tooltip = StringConstants.TOOLTIP_PROJECTS_BUTTON_SHOW;
          this.projectsButton.deactivate();
        }

        if (clickedTarget2 == this.diplomacyPanel) {
          this.diplomacyButton.tooltip = StringConstants.TOOLTIP_DIPLOMACY_BUTTON_SHOW;
          this.diplomacyButton.deactivate();
        }

        if (clickedTarget2 == this.fleetInfoPanel) {
          this.fleetInfoButton.tooltip = StringConstants.TOOLTIP_FLEET_INFO_BUTTON_SHOW;
          this.fleetInfoButton.deactivate();
        }

        if (clickedTarget2 == this.victoryPanel) {
          this.victoryButton.tooltip = StringConstants.TOOLTIP_VICTORY_BUTTON_SHOW;
          this.victoryButton.deactivate();
        }

        clickedTarget2.flashing = false;
        return var3;
      }

      if (this.clickedComponent == this.productionButton) {
        if (this.productionPanel.visible) {
          this.productionPanel.visible = false;
          this.productionButton.tooltip = StringConstants.TOOLTIP_PRODUCTION_BUTTON_SHOW;
        } else {
          this.a690(this.productionPanel);
          this.productionButton.tooltip = StringConstants.TOOLTIP_PRODUCTION_BUTTON_HIDE;
          this.productionPanel.visible = true;
        }

        this.productionPanel.flashing = false;
        this.productionButton.deactivate();
        return var3;
      }

      if (this.clickedComponent == this.projectsButton) {
        if (this.projectsPanel.visible) {
          this.projectsButton.tooltip = StringConstants.TOOLTIP_PROJECTS_BUTTON_SHOW;
          this.projectsPanel.visible = false;
        } else {
          this.a690(this.projectsPanel);
          this.projectsButton.tooltip = StringConstants.TOOLTIP_PROJECTS_BUTTON_HIDE;
          this.projectsPanel.visible = true;
        }

        this.projectsPanel.flashing = false;
        this.projectsButton.deactivate();
        return var3;
      }

      if (this.clickedComponent == this.diplomacyButton) {
        if (this.diplomacyPanel.visible) {
          this.diplomacyButton.tooltip = StringConstants.TOOLTIP_DIPLOMACY_BUTTON_SHOW;
          this.diplomacyPanel.visible = false;
        } else {
          this.a690(this.diplomacyPanel);
          this.diplomacyButton.tooltip = StringConstants.TOOLTIP_DIPLOMACY_BUTTON_HIDE;
          this.diplomacyPanel.visible = true;
        }

        this.diplomacyPanel.flashing = false;
        this.diplomacyButton.deactivate();
        return var3;
      }

      if (this.clickedComponent == this.fleetInfoButton) {
        if (this.fleetInfoPanel.visible) {
          this.fleetInfoPanel.visible = false;
          this.fleetInfoButton.tooltip = StringConstants.TOOLTIP_FLEET_INFO_BUTTON_SHOW;
        } else {
          this.a690(this.fleetInfoPanel);
          this.fleetInfoButton.tooltip = StringConstants.TOOLTIP_FLEET_INFO_BUTTON_HIDE;
          this.fleetInfoPanel.visible = true;
        }

        this.fleetInfoPanel.flashing = false;
        this.fleetInfoButton.deactivate();
        return var3;
      }

      if (this.clickedComponent == this.victoryButton) {
        if (this.victoryPanel.visible) {
          this.victoryPanel.visible = false;
        } else {
          this.a690(this.victoryPanel);
          this.victoryPanel.visible = true;
        }
        this.victoryButton.tooltip = StringConstants.TOOLTIP_VICTORY_BUTTON_SHOW;

        this.victoryPanel.flashing = false;
        this.victoryButton.deactivate();
        return var3;
      }

      if (this.clickedComponent == this.endTurnButton) {
        this.endTurnButtonClicked();
        return var3;
      }

      if (this.clickedComponent == this.showChatButton) {
        isChatOpen = !isChatOpen;
        this.showChatButton.toggle();
        return var3;
      }

      if (this.clickedComponent == this.animationAutoPlayButton) {
        this.animationAutoPlayButton.toggle();
        if (this.animationAutoPlayButton.isActive()) {
          this.animationAutoPlayButton.tooltip = StringConstants.TOOLTIP_ANIM_AUTO_PLAY_IS_ON;
          if (this.gameSession.turnEventLog != null) {
            this.gameSession.gameView.advanceAnimationPhase(AbstractGameView.AnimationPhase.BUILD, this.gameSession.turnEventLog.events);
            if (!this.animationPlayingButton.isActive()) {
              this.animationPlayingButton.toggle();
              this.animationPlayingButton.tooltip = StringConstants.TOOLTIP_ANIM_CLICK_TO_STOP;
            }
          }
        } else {
          this.animationAutoPlayButton.tooltip = StringConstants.TOOLTIP_ANIM_AUTO_PLAY_IS_OFF;
          if (this.animationPlayingButton.isActive()) {
            this.animationPlayingButton.toggle();
            this.animationPlayingButton.tooltip = StringConstants.TOOLTIP_ANIM_CLICK_TO_PLAY;
          }

          this.gameSession.gameView.advanceAnimationPhase(AbstractGameView.AnimationPhase.NOT_PLAYING, this.gameSession.turnEventLog == null ? null : this.gameSession.turnEventLog.events);
          this.gameSession.recalculateSystemState();
        }

        this.m150();
        return var3;
      }

      if (this.clickedComponent == this.animationPlayingButton) {
        if (this.gameSession.turnEventLog != null) {
          this.animationPlayingButton.toggle();
        }

        if (this.animationPlayingButton.isActive()) {
          this.animationPlayingButton.tooltip = StringConstants.TOOLTIP_ANIM_CLICK_TO_STOP;
          this.gameSession.gameView.advanceAnimationPhase(AbstractGameView.AnimationPhase.BUILD, this.gameSession.turnEventLog == null ? null : this.gameSession.turnEventLog.events);
        } else {
          this.animationPlayingButton.tooltip = StringConstants.TOOLTIP_ANIM_CLICK_TO_PLAY;
          this.gameSession.gameView.advanceAnimationPhase(AbstractGameView.AnimationPhase.NOT_PLAYING, this.gameSession.turnEventLog == null ? null : this.gameSession.turnEventLog.events);
          this.gameSession.recalculateSystemState();
        }

        return var3;
      }

      if (this.clickedComponent == this.animationSpeedDoubledButton) {
        this.animationSpeedDoubledButton.toggle();
        if (this.animationSpeedDoubledButton.isActive()) {
          this.animationSpeedDoubledButton.tooltip = StringConstants.TOOLTIP_ANIM_SPEED_IS_DOUBLE;
        } else {
          this.animationSpeedDoubledButton.tooltip = StringConstants.TOOLTIP_ANIM_SPEED_IS_NORMAL;
        }

        this.m150();
        return var3;
      }

      if (this.gameSession.localPlayer != null && this.productionPanel.visible) {
        final ProductionPanelState panelState = this.productionPanel.state;
        for (int i = 0; i < panelState._f.size(); ++i) {
          final ScrollView<Force> var6 = panelState._f.get(i);
          final Force force = var6.data;
          if (var6.hasChild(this.clickedComponent)) {
            if (this.gameSession.placementMode == PlacementMode.BUILD_FLEET && this.gameSession.selectedForce == force) {
              this.setPlacementMode(PlacementMode.NONE);
            } else if (force.fleetsAvailableToBuild > 0) {
              this.activateFleetPlacement(force, false);
            }
          }
        }
      }

      if (this.gameSession.localPlayer != null && this.projectsPanel.visible) {
        final ProjectsPanelState var11 = this.projectsPanel.state;

        for (final int type : GameState.RESOURCE_TYPES) {
          if (var11._c[type].hasChild(this.clickedComponent) && this.gameSession.localPlayer.researchPoints[type] == GameState.MAX_RESEARCH_POINTS) {
            this.activateProject(type);
          }
        }
      }

      if (this.gameSession.localPlayer != null && this.diplomacyPanel.visible) {
        final DiplomacyPanelState var12 = this.diplomacyPanel.state;
        for (final ScrollView<Player> var17 : var12._h) {
          final Player player = var17.data;
          if (player != this.gameSession.localPlayer && var17.hasChild(this.clickedComponent) && !this.gameSession.localPlayer.allies[player.index]) {
            this.gameSession.requestOrAcceptPact(player);
          }
        }
      }
    }

    if (!var3 && this.gameSession.isTutorial) {
      if (TutorialState.a881ks(var1)) {
        return true;
      }
    }

    return var3;
  }

  private void drawTooltip(final String message) {
    int x = JagexApplet.mouseX + 10;
    int y = JagexApplet.mouseY + 20;
    final int width = Menu.SMALL_FONT.measureLineWidth(message);
    if (y + TOOLTIP_HEIGHT > ShatteredPlansClient.SCREEN_HEIGHT) {
      x = JagexApplet.mouseX;
      y = JagexApplet.mouseY - 15;
    }

    if (x + width > ShatteredPlansClient.SCREEN_WIDTH - 11) {
      x = ShatteredPlansClient.SCREEN_WIDTH - 11 - width;
    }

    Drawing.fillRoundedRect(x, y, width + 10, TOOLTIP_HEIGHT, 5, 0, 192);
    Drawing.f669(x - 1, y - 1, width + 12, 15, 6, 0x3ca4a7);
    Menu.SMALL_FONT.draw(message, 6 + x, 3 + y + Menu.SMALL_FONT.ascent / 2, Drawing.WHITE);
  }

  private void initialize() {
    if (!this.gameSession.isTutorial || TutorialState.stage >= 6) {
      this.diplomacyPanel.content.removeChildren();
      final DiplomacyPanelState var2 = new DiplomacyPanelState(this.gameSession.gameState.playerCount);
      this.diplomacyPanel.state = var2;
      if (this.gameSession.localPlayer == null) {
        for (int var3 = 0; var3 < this.gameSession.gameState.playerCount; ++var3) {
          final ScrollView<?> var4 = a583nl(var2, this.gameSession.gameState.players, this.gameSession.gameState.players[var3]);
          var4.setPosition(this.diplomacyPanel.content.x, this.diplomacyPanel.content.y + this.diplomacyPanel.content.contentHeight);
          this.diplomacyPanel.content.addChild(var4);
        }
      } else {
        final ScrollView<?> var6 = a583nl(var2, this.gameSession.gameState.players, this.gameSession.localPlayer);
        var6.setPosition(this.diplomacyPanel.content.x, this.diplomacyPanel.content.y + this.diplomacyPanel.content.contentHeight);
        this.diplomacyPanel.content.addChild(var6);

        for (int var7 = 0; this.gameSession.gameState.playerCount > var7; ++var7) {
          if (this.gameSession.gameState.players[var7] != this.gameSession.localPlayer) {
            final ScrollView<?> var5 = a318lr(var2, this.gameSession.localPlayer, this.gameSession.gameState.players[var7], this.gameSession.gameState.players);
            var5.setPosition(this.diplomacyPanel.content.x, this.diplomacyPanel.content.y + this.diplomacyPanel.content.contentHeight);
            this.diplomacyPanel.content.addChild(var5);
          }
        }
      }
    }
  }

  public void tick() {
    if (this.gameSession.readyToEndTurn || this.endTurnButton == null) {
      final StatusPanelState var2 = (StatusPanelState) this.statusPanel.state;
      var2.icon.setSprite(null);
      if (this.gameSession.playersWaitingOn == 1) {
        this.setActionHint(StringConstants.TEXT_WAITING_FOR_PLAYER);
      } else {
        this.setActionHint(Strings.format(StringConstants.WAITING_FOR_N_PLAYERS, Integer.toString(this.gameSession.playersWaitingOn)));
      }
    } else {
      this.endTurnButton.deactivate();
    }

    for (final Player players : this.gameSession.gameState.players) {
      this.updateDiplomacyState(players);
    }

    if (!this.gameSession.isMultiplayer && !this.gameSession.isTutorial) {
      this.tickChatSinglePlayer();
    }

    tickChat();
    this.showChatButtonPanel.setPosition(this.showChatButtonPanel.x, this.getHeight() - this.showChatButtonPanel.height / 2);
    this.animationControlsPanel.setPosition(this.animationControlsPanel.x, this.showChatButtonPanel.y);
    boolean var9 = false;

    for (int i = 0; i < this.gameSession.gameState.playerCount; ++i) {
      final String message;
      if (i == this.gameSession.localPlayerIndex) {
        if (this.gameSession.gameState.isPlayerOfferingRematch(i)) {
          if (this.gameSession.isUnrated) {
            message = StringConstants.MP_YOU_OFFER_REMATCH_UNRATED;
          } else {
            message = StringConstants.MP_YOU_OFFER_REMATCH;
          }
        } else if (this.gameSession.gameState.didPlayerResign(i)) {
          message = StringConstants.MP_YOU_RESIGNED;
        } else if (this.gameSession.gameState.isPlayerDefeated(i)) {
          message = StringConstants.TEXT_YOU_HAVE_BEEN_DEFEATED;
        } else if (!this.gameSession.gameState.hasEnded && this.gameSession.gameState.isPlayerOfferingDraw(i)) {
          message = StringConstants.MP_YOU_OFFER_DRAW;
        } else {
          message = null;
        }
      } else {
        final String playerName = this.gameSession.gameState.playerNames[i];
        if (this.gameSession.didPlayerLeave(i)) {
          if (this.gameSession.gameState.didPlayerResign(i)) {
            message = Strings.format(StringConstants.MP_X_HAS_RESIGNED_AND_LEFT, playerName);
          } else {
            message = Strings.format(StringConstants.MP_X_HAS_LEFT, playerName);
          }
        } else if (this.gameSession.gameState.isPlayerOfferingRematch(i)) {
          if (this.gameSession.isUnrated) {
            message = Strings.format(StringConstants.MP_X_OFFERS_REMATCH_UNRATED, playerName);
          } else {
            message = Strings.format(StringConstants.MP_X_OFFERS_REMATCH, playerName);
          }
        } else if (this.gameSession.gameState.didPlayerResign(i)) {
          message = Strings.format(StringConstants.MP_X_HAS_RESIGNED, playerName);
        } else if (this.gameSession.gameState.isPlayerDefeated(i)) {
          message = null;
        } else if (!this.gameSession.gameState.hasEnded && this.gameSession.gameState.isPlayerOfferingDraw(i)) {
          message = Strings.format(StringConstants.MP_X_WANTS_TO_DRAW, playerName);
        } else {
          message = null;
        }
      }

      if (message != null && !message.equals(this.playerDiplomacyStatusMessage[i])) {
        this._z[i] = true;
      }

      this.playerDiplomacyStatusMessage[i] = message;
      var9 |= message != null;
    }

    if (this.gameSession.localPlayer == null || this.gameSession.localPlayer.incomingPactOffersBitmap == 0) {
      this.playerDiplomacyStatusMessage[this.gameSession.gameState.playerCount] = null;
    } else {
      var9 = true;
      //noinspection StringEquality
      if (this.playerDiplomacyStatusMessage[this.gameSession.gameState.playerCount] != StringConstants.MESSAGE_INCOMING_OFFERS) {
        this.playerDiplomacyStatusMessage[this.gameSession.gameState.playerCount] = StringConstants.MESSAGE_INCOMING_OFFERS;
        this._z[this.gameSession.gameState.playerCount] = true;
        this.diplomacyPanel.flashing = true;
        this.diplomacyButton.activate();
      }
    }

    int var10;
    if (var9) {
      int var5 = 250;
      if (this._ib) {
        var5 = 5 * var5 >> 2;
      }

      if (var5 <= ++this._x) {
        do {
          if (++this._U == this.playerDiplomacyStatusMessage.length) {
            this._U = 0;
          }
        } while (this.playerDiplomacyStatusMessage[this._U] == null);

        this._x = 0;
        final String _o = this.playerDiplomacyStatusMessage[this._U];
        this._ib = this._z[this._U];
        this._z[this._U] = false;
        var10 = Menu.SMALL_FONT.measureLineWidth(_o);
        this._T = new Sprite(var10, Menu.SMALL_FONT.descent + Menu.SMALL_FONT.ascent);
        Drawing.saveContext();
        this._T.installForDrawing();
        Drawing.clear();
        Menu.SMALL_FONT.drawRightAligned(_o, this._T.width, Menu.SMALL_FONT.ascent, Drawing.WHITE);
        this._eb = this._T.copy();
        this._eb.installForDrawing();
        Drawing.clear();
        Menu.SMALL_FONT.drawRightAligned(_o, this._T.width, Menu.SMALL_FONT.ascent, Drawing.WHITE);
        Drawing.b669(1, 1, this._eb.width, this._eb.height);
        Drawing.restoreContext();
      }
    } else {
      this._x = 250;
      this._U = -1;
    }

    if (this.newTurnPanelOpenAmount != 0) {
      if (++this.newTurnPanelOpenAmount == 60000) {
        this.a423();
      }

      if (this.newTurnPanelOpenAmount == 59968) {
        Sounds.play(Sounds.SFX_NEXT_CLOSE);
      }
    }

    if (this.isStatsScreenOpen) {
      if (this.statsScreenOpenAmount < 32) {
        ++this.statsScreenOpenAmount;
      }
    } else if (this.statsScreenOpenAmount > 0) {
      --this.statsScreenOpenAmount;
    }

    if (this.statsScreenOpenAmount != 0) {
      this.tickStatsScreen();
    }

    if (this.gameSession.isTutorial) {
      TutorialState.tick();
      Drawing.saveContext();
      _fjr.installForDrawing();
      Drawing.fillRect(0, 0, _fjr.width, _fjr.height, 0);
      if (TutorialState._sek) {
        PRODUCTION_BUTTON.draw(10, 10);
        final int var5 = Drawing.alphaOver(Drawing.WHITE, Drawing.BLACK, GameView.uiPulseCounter);
        _fjr.f150(var5);
        _fjr.f150(var5);
        _fjr.installForDrawing();
        Drawing.b669(5, 5, _fjr.width, _fjr.height);
      }

      _kbw.installForDrawing();
      Drawing.fillRect(0, 0, _kbw.width, _kbw.height, 0);
      if (TutorialState._phg) {
        READY_BUTTON.draw(10, 10);
        final int var5 = Drawing.alphaOver(Drawing.WHITE, Drawing.BLACK, GameView.uiPulseCounter);
        _kbw.f150(var5);
        _kbw.f150(var5);
        _kbw.installForDrawing();
        Drawing.b669(2, 2, _kbw.width, _kbw.height);

        for (var10 = 0; var10 < _kbw.pixels.length; ++var10) {
          _kbw.pixels[var10] = Drawing.WHITE + (_kbw.pixels[var10] << 8 & 0xff000000);
        }
      }

      Drawing.restoreContext();
    }
  }

  private void tickStatsScreen() {
    if (this.statsGraphTurnAdvanceAnimationCounter != 0 && ++this.statsGraphTurnAdvanceAnimationCounter >= STATS_GRAPH_TURN_ADVANCE_ANIMATION_COUNTER_MAX) {
      this.statsGraphData = this.targetStatsGraphData;
      this.statsGraphStartTurn = this.statsGraphTurnCount;
      this.statsGraphTurnAdvanceAnimationCounter = 0;
    }

    if (this.currentStatsScreenTab != StatsScreenTab.OVERVIEW && this.targetStatsGraphData != null) {
      if ((this.statsGraphAlpha += 8) > 256) {
        this.statsGraphAlpha = 256;
      }
    } else if ((this.statsGraphAlpha -= 8) < 0) {
      this.statsGraphAlpha = 0;
    }

    if (++this._p[this._w] > 32) {
      this._p[this._w] = 32;
    }

    final int playerCount = this.gameSession.gameState.playerCount;
    int var4 = 210;
    int var5;
    int var6;
    if (playerCount == 2) {
      for (var5 = 0; var5 < 4; ++var5) {
        var6 = var4 / (4 - var5);
        this._ob[var5] = 12 + var6;
        var4 -= var6;
      }
    } else {
      var5 = var4 / (6 + playerCount);
      var6 = 12 + 2 * var5;
      int var7 = -(8 * var5) + var4;
      this._ob[this._w] = var6 + var7;
      final int var8 = MathUtil.ease(this._p[this._w], 32, 0, var7);
      var7 -= var8;
      final int[] var9 = new int[4];
      int var10 = 0;

      for (int var11 = 0; var11 < 4; ++var11) {
        if (this._w != var11) {
          final int var12 = MathUtil.ease(this._p[var11], 32, 0, 1024);
          var9[var11] = var12;
          var10 += var12;
        }
      }

      if (var10 == 0) {
        for (int var11 = 0; var11 < 4; ++var11) {
          if (var11 != this._w) {
            this._ob[var11] = var6 + var7 / 3;
            this._ob[this._w] -= var7 / 3;
          }
        }
      } else {
        for (int var11 = 0; var11 < 4; ++var11) {
          if (var11 != this._w) {
            this._ob[var11] = var7 * var9[var11] / var10 + var6;
            this._ob[this._w] -= var7 * var9[var11] / var10;
          }
        }
      }
    }

    var5 = MathUtil.ease(this.statsScreenOpenAmount, 32, 20, 550);
    this.fleetsTabControl.setPosition(20 + (ShatteredPlansClient.SCREEN_WIDTH - var5) / 2, this.fleetsTabButton.y);
    this.productionTabControl.setPosition(5 + this.fleetsTabButton.width + (ShatteredPlansClient.SCREEN_WIDTH - var5) / 2 + 20, this.productionTabButton.y);
    this.systemsTabControl.setPosition(2 * (this.fleetsTabButton.width + 5) + (ShatteredPlansClient.SCREEN_WIDTH - var5) / 2 + 20, this.systemsTabButton.y);
    this.overviewTabControl.setPosition((ShatteredPlansClient.SCREEN_WIDTH - var5) / 2 + 20 + (this.fleetsTabButton.width + 5) * 3, this.overviewTabButton.y);
  }

  private void tickChatSinglePlayer() {
    final int chatPanelY = getChatPanelY();
    this.chatPanelSinglePlayer.setBounds(0, chatPanelY, ShatteredPlansClient.SCREEN_WIDTH, CHAT_PANEL_HEIGHT);
    this.chatScrollPaneSinglePlayer.content.children.clear();
    int var5 = 0;
    int var6 = 0;

    for (int var7 = ShatteredPlansClient.chatMessageCount - 1; var7 >= 0; --var7) {
      final funorb.client.lobby.ChatMessage var8 = ShatteredPlansClient.chatMessages[var7];
      final boolean var9 = ShatteredPlansClient._kpi > var5;
      if (var9) {
        if (var8.component == null) {
          final int var10 = var8._g;
          var8.component = new Component<>(Component.UNSELECTED_LABEL, var8.message);
          var8.component.mouseOverTextColor = ((16711422 & Component.UNSELECTED_LABEL.mouseOverTextColor) >> 1) + var10 - (var10 >> 1 & 8355711);
          var6 += 15;
          var8.component.font = Component.CHAT_FONT;
          var8.component.textColor = var10;
          var8.component._qb = (8355711 & Component.UNSELECTED_LABEL._qb >> 1) - ((var10 & 16711422) >> 1) + var10;
        }

        ++var5;
      } else {
        var8.component = null;
      }
    }

    int var7 = 0;

    for (int var11 = 0; var11 < ShatteredPlansClient.chatMessageCount; ++var11) {
      final funorb.client.lobby.ChatMessage var12 = ShatteredPlansClient.chatMessages[var11];
      if (var12.component != null) {
        this.chatScrollPaneSinglePlayer.content.addChild(var12.component);
        var12.component.setBounds(ShatteredPlansClient._tga, var7, var12.component.e474(), 15);
        var7 += 15;
      }
    }

    final int var11 = -var7 + var6 + this.chatScrollPaneSinglePlayer.content._gb + this.chatScrollPaneSinglePlayer.content.height;
    this.chatScrollPaneSinglePlayer.content.y += var11;
    this.chatScrollPaneSinglePlayer.content.height = var7;
    this.chatScrollPaneSinglePlayer.content._gb = 0;
    final int var13 = this.chatScrollPaneSinglePlayer.viewport.height - this.chatScrollPaneSinglePlayer.content.height - this.chatScrollPaneSinglePlayer.content._gb;
    if (this._v) {
      this.chatScrollPaneSinglePlayer.content._w = var13 - this.chatScrollPaneSinglePlayer.content.y;
    }

    this.chatScrollPaneSinglePlayer.a795(30 * JagexApplet.mouseWheelRotation, 15);
    this._v = var13 == this.chatScrollPaneSinglePlayer.content.y + this.chatScrollPaneSinglePlayer.content._w;
    this.chatPanelSinglePlayer.rootProcessMouseEvents(true);
  }

  public static int getChatPanelY() {
    final int chatPanelOpenFac = CHAT_PANEL_OPEN_AMOUNT_MAX_SQ - (chatPanelOpenAmount * chatPanelOpenAmount);
    return (ShatteredPlansClient.SCREEN_HEIGHT - CHAT_PANEL_HEIGHT) + (CHAT_PANEL_HEIGHT * chatPanelOpenFac / CHAT_PANEL_OPEN_AMOUNT_MAX_SQ);
  }

  private void a690(final FloatingPanel<?> var2) {
    assert this.components.indexOf(this.showChatButtonPanel) == 0 && this.components.indexOf(this.animationControlsPanel) == 1;
    this.components.remove(var2);
    this.addComponent(var2, 2);
  }

  private String a436(int var2) {
    final int var3 = var2 / 60;
    var2 %= 60;

    return String.valueOf((char) (48 + var3 / 10)) +
        (char) (48 + var3 % 10) +
        ':' +
        (char) (var2 / 10 + 48) +
        (char) (var2 % 10 + 48);
  }

  public Rect b520() {
    if (this.gameSession.isTutorial) {
      return TutorialState.b520b();
    } else {
      return new Rect(0, 50, ShatteredPlansClient.SCREEN_WIDTH, 430);
    }
  }

  private void f150() {
    this.productionPanel.content.removeChildren();
    final ProductionPanelState info = this.productionPanel.state;
    if (this.gameSession.localPlayer != null && !this.gameSession.gameState.isPlayerDefeated(this.gameSession.localPlayer.index)) {
      if (this.gameSession.gameState.gameOptions.unifiedTerritories) {
        info.buildFleetsButtons = new ArrayList<>(1);
        info._f = new ArrayList<>(1);
        info.buildFleetsLabels = new ArrayList<>(1);
        final ScrollView<?> var3 = addForceToProductionPanel(this.gameSession.localPlayer.combinedForce, info, true);
        var3.setPosition(this.productionPanel.content.x, this.productionPanel.content.contentHeight + this.productionPanel.content.y);
        this.productionPanel.content.addChild(var3);
        if (this.gameSession.localPlayer.combinedForce.fleetsAvailableToBuild <= 0) {
          this.productionPanel.flashing = false;
          this.productionButton.deactivate();
        } else {
          this.productionPanel.flashing = true;
          this.productionButton.activate();
        }
      } else {
        final int forcesCount = this.gameSession.localPlayer.contiguousForces.size();
        info.buildFleetsButtons = new ArrayList<>(forcesCount);
        info._f = new ArrayList<>(forcesCount);
        info.buildFleetsLabels = new ArrayList<>(forcesCount);
        this.productionPanel.flashing = false;
        this.productionButton.deactivate();

        boolean isFirst = true;
        for (final ContiguousForce force : this.gameSession.localPlayer.contiguousForces) {
          final ScrollView<?> var5 = addForceToProductionPanel(force, info, isFirst);
          var5.setPosition(this.productionPanel.content.x, this.productionPanel.content.y + this.productionPanel.content.contentHeight);
          this.productionPanel.content.addChild(var5);
          if (force.fleetsAvailableToBuild > 0) {
            this.productionPanel.flashing = true;
            this.productionButton.activate();
          }
          isFirst = false;
        }
      }
    }

    if (this.gameSession.localPlayer != null && (!this.gameSession.isTutorial || TutorialState.stage >= 5)) {
      this.productionPanel.content.addChild(a179hc(this.productionPanel.content.x, this.productionPanel.content.contentHeight + this.productionPanel.content.y));
    }

    final Player[] var10 = this.gameSession.gameState.players;

    for (final Player var13 : var10) {
      if (var13 != this.gameSession.localPlayer && !this.gameSession.gameState.isPlayerDefeated(var13.index)) {
        if (this.gameSession.gameState.gameOptions.unifiedTerritories) {
          final ScrollView<?> var14 = a278an(var13.combinedForce, true);
          var14.setPosition(this.productionPanel.content.x, this.productionPanel.content.contentHeight + this.productionPanel.content.y);
          this.productionPanel.content.addChild(var14);
        } else {
          boolean isFirst = true;
          for (final ContiguousForce force : var13.contiguousForces) {
            final ScrollView<?> var7 = a278an(force, isFirst);
            var7.setPosition(this.productionPanel.content.x, this.productionPanel.content.contentHeight + this.productionPanel.content.y);
            this.productionPanel.content.addChild(var7);
            isFirst = false;
          }
        }
      }
    }

    int var12 = 3 + this.productionPanel.content.contentHeight + 20;
    if (var12 > 363) {
      var12 = 363;
    }

    this.productionPanel.a183(var12, this.productionPanel.width);
    this.productionPanel.content.a183(var12 - 10 - 18, this.productionPanel.content.width);
    info.scrollBar.a183(var12 - 3 - 15 - 10, info.scrollBar.width);
    info.scrollBar.enabled = var12 == 363;

  }

  private void endTurnButtonClicked() {
    this.gameSession.gameView.stopCombatAnimations();
    this.gameSession.recalculateSystemState();
    if (this.gameSession.localPlayer != null) {
      final StatusPanelState var2 = (StatusPanelState) this.statusPanel.state;
      if (var2.icon.isEmpty() && this.gameSession.placementMode != PlacementMode.BUILD_FLEET) {
        if (this.gameSession.gameState.gameOptions.unifiedTerritories) {
          if (this.gameSession.localPlayer.combinedForce.fleetsAvailableToBuild > 0) {
            this.activateFleetPlacement(this.gameSession.localPlayer.combinedForce, true);
            return;
          }
        } else {
          for (final ContiguousForce var3 : this.gameSession.localPlayer.contiguousForces) {
            if (var3.fleetsAvailableToBuild > 0) {
              this.activateFleetPlacement(var3, true);
              return;
            }
          }
        }
      }

      this.gameSession.endTurn();
      this.endTurnButton.activate();
      var2.icon.setSprite(null);
      if (this.gameSession.playersWaitingOn == 1) {
        this.setActionHint(StringConstants.TEXT_WAITING_FOR_PLAYER);
      } else {
        this.setActionHint(Strings.format(StringConstants.WAITING_FOR_N_PLAYERS, Integer.toString(this.gameSession.playersWaitingOn)));
      }
    }
  }

  private void a115(final int var1, final int var2, final int var4) {
    final String var5 = StringConstants.TURN_OBJECTIVE.toUpperCase() + ": " + StringConstants.GAME_TYPE_TOOLTIPS[3];
    Menu.SMALL_FONT.drawParagraph(var5, 5 + var1, var4 + 5, var2 - 10, ShatteredPlansClient.SCREEN_HEIGHT, Drawing.WHITE, Font.HorizontalAlignment.LEFT, Font.VerticalAlignment.TOP, Menu.SMALL_FONT.ascent);
    int var6 = 20 + var4 + Menu.SMALL_FONT.ascent + 9;
    final int var7 = 64 + Menu.SMALL_FONT.measureLineWidth("= " + Strings.format(StringConstants.TURN_FEWER_SHIPS, Integer.toString(2)));
    StarSystem.ALIEN_MINER.b115((-var7 + var2) / 2 + var1, var6 - 24, 64, 64);
    Menu.SMALL_FONT.draw(Strings.format(StringConstants.TURN_POINTS, Integer.toString(2)), var1 - (-((var2 - var7) / 2) - 75), var6 + 7, Drawing.WHITE);
    Menu.SMALL_FONT.draw("=", var1 + (var2 - var7) / 2 + 64, 13 + var6, Drawing.WHITE);
    Menu.SMALL_FONT.draw(Strings.format(StringConstants.TURN_FEWER_SHIPS, Integer.toString(2)), 75 + (var2 - var7) / 2 + var1, 19 + var6, Drawing.WHITE);
    var6 += 44;
    StarSystem.ALIEN_SHIP.b115((-var7 + var2) / 2 + var1, var6 - 24, 64, 64);
    Menu.SMALL_FONT.draw(Strings.format(StringConstants.TURN_POINTS, Integer.toString(2)), 75 + (-var7 + var2) / 2 + var1, 7 + var6, Drawing.WHITE);
    Menu.SMALL_FONT.draw("=", (-var7 + var2) / 2 + var1 + 64, 13 + var6, Drawing.WHITE);
    Menu.SMALL_FONT.draw(Strings.format(StringConstants.TURN_FEWER_SHIPS, Integer.toString(2)), 75 + (var2 - var7) / 2 + var1, var6 + 19, Drawing.WHITE);
    var6 += 52;
    StarSystem.ALIEN_BASE.b115(var1 + (var2 - var7) / 2, var6 - 24, 64, 64);
    Menu.SMALL_FONT.draw(Strings.format(StringConstants.TURN_POINTS, Integer.toString(5)), 75 + (-var7 + var2) / 2 + var1, var6 + 7, Drawing.WHITE);
    Menu.SMALL_FONT.draw("=", 64 + var1 + (-var7 + var2) / 2, 13 + var6, Drawing.WHITE);
    Menu.SMALL_FONT.draw(Strings.format(StringConstants.TURN_FEWER_SHIPS, Integer.toString(5)), var1 - (-((-var7 + var2) / 2) - 75), 19 + var6, Drawing.WHITE);
  }

  public void updateVictoryPanel(final int var1, final Player[] players, final int[] var3, final int[] var4, final Player[] leaders, final Label[] labels, final int turnsUntilVictory) {
    final ScrollView<?> content = this.victoryPanel.content;
    content.removeChildren();
    final ScrollView<?> labelView = createScrollView(labels);
    labelView.setPosition(content.x, content.y + content.contentHeight);
    content.addChild(labelView);

    for (int i = 0; i < players.length; ++i) {
      final ScrollView<?> var12 = createVictoryPanelRow(this.gameSession.gameState, leaders, var1, turnsUntilVictory, players[i], var4[i], var3[i]);
      var12.setPosition(content.x, content.y + content.contentHeight);
      content.addChild(var12);
    }

    if (turnsUntilVictory > 0 && turnsUntilVictory <= 3) {
      this.victoryPanel.flashing = true;
      this.victoryButton.activate();
    } else {
      this.victoryPanel.flashing = false;
      this.victoryButton.deactivate();
    }
  }

  public void activateFleetPlacement(final Force force, final boolean showUnplacedWarning) {
    this.gameSession.selectedForce = force;
    this.setPlacementMode(PlacementMode.BUILD_FLEET);
    this.productionPanel.state.activateFleetPlacement(force);

    if (showUnplacedWarning) {
      final StatusPanelState statusState = (StatusPanelState) this.statusPanel.state;
      statusState.icon.setSprite(this.warningSprite);
      statusState.label.setPosition(55, statusState.label.y);
      if (this.gameSession.isTutorial) {
        TutorialState.a984fl("unplaced");
      }
    }

    for (final StarSystem system : force) {
      this.gameSession.gameView.highlightedSystems[system.index] = SystemHighlight.TARGET;
    }
  }

  public enum PlacementMode {
    MOVE_FLEET_SRC,
    MOVE_FLEET_DEST,
    BUILD_FLEET,
    DEFENSIVE_NET,
    TERRAFORM,
    STELLAR_BOMB,
    GATE_SRC,
    GATE_DEST;

    /**
     * When no other placement mode is active, the placement mode is implicitly
     * {@link PlacementMode#MOVE_FLEET_SRC}.
     */
    public static final PlacementMode NONE = MOVE_FLEET_SRC;
  }

  private enum StatsScreenTab {
    FLEETS, // = 0;
    PRODUCTION, // = 1;
    SYSTEMS, // = 2;
    OVERVIEW, // = 3;
  }
}
