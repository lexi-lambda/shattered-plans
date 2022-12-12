package funorb.shatteredplans.client.game;

import funorb.Strings;
import funorb.awt.KeyState;
import funorb.awt.MouseState;
import funorb.client.lobby.ChatMessage;
import funorb.client.lobby.Component;
import funorb.client.lobby.ContextMenu;
import funorb.io.Buffer;
import funorb.io.CipheredBuffer;
import funorb.shatteredplans.C2SPacket;
import funorb.shatteredplans.S2CPacket;
import funorb.shatteredplans.StringConstants;
import funorb.shatteredplans.client.GameUI;
import funorb.shatteredplans.client.GameUI.PlacementMode;
import funorb.shatteredplans.client.JagexApplet;
import funorb.shatteredplans.client.Menu;
import funorb.shatteredplans.client.ShatteredPlansClient;
import funorb.shatteredplans.client.Sounds;
import funorb.shatteredplans.client.game.AbstractGameView.SystemHighlight;
import funorb.shatteredplans.game.BuildFleetsOrder;
import funorb.shatteredplans.game.ContiguousForce;
import funorb.shatteredplans.game.Force;
import funorb.shatteredplans.game.GameOptions;
import funorb.shatteredplans.game.GameSession;
import funorb.shatteredplans.game.GameState;
import funorb.shatteredplans.game.MoveFleetsOrder;
import funorb.shatteredplans.game.Player;
import funorb.shatteredplans.game.ProjectOrder;
import funorb.shatteredplans.game.TurnOrders;
import funorb.shatteredplans.game.ai.AI;
import funorb.shatteredplans.game.ai.TaskAI;
import funorb.shatteredplans.game.ai.TutorialAI1;
import funorb.shatteredplans.game.ai.TutorialAI3;
import funorb.shatteredplans.map.Map;
import funorb.shatteredplans.map.StarSystem;
import funorb.util.Functions;
import funorb.util.MathUtil;
import org.intellij.lang.annotations.MagicConstant;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public final class ClientGameSession extends GameSession {
  private static final int MOUSE_HOLD_DELAY_MAX = 25;

  public static ClientGameSession playSession;
  public static ClientGameSession spectateSession;
  public static boolean isAutoPlaying = false;

  public final boolean isMultiplayer;
  public final boolean isTutorial;
  public final boolean isUnrated;
  public final GameState gameState;
  public int localPlayerIndex;
  public Player localPlayer;
  public AI[] ais;
  public GameUI ui;
  public GameView gameView;
  private boolean localPlayerIsAlive;
  private Player[] systemOwners;
  private ContiguousForce[] systemForces;
  private int[] remainingGarrisons;
  private TacticalAnalysis tacticalAnalysis;

  public boolean desynced;
  public int turnNumberWhenJoined;
  public TurnEventLog turnEventLog;
  public int turnTicksLeft;
  public int playersWaitingOn;
  public boolean readyToEndTurn = false;
  private int leftPlayersBitmap;
  private List<ProjectOrder> unsentProjectOrders;
  private List<BuildFleetsOrder> unsentBuildOrders;
  private List<MoveFleetsOrder> unsentMoveOrders;
  private int orderUpdateBusyTimer;
  private int orderUpdateIdleTimer;

  public Force selectedForce; // the selected force when building new fleets
  private StarSystem selectedSystem; // the system that is selected as the source for an order, like a fleet movement or gate placement
  public PlacementMode placementMode;
  private boolean rightMouseDown = false;
  private boolean rightMouseDragging = false;
  private int rightClickX;
  private int rightClickY;
  private int mouseHoldDelayCounter;
  private int mouseHoldDelayAmount = MOUSE_HOLD_DELAY_MAX;
  private boolean isDragPanningMap = false;

  private TickTimer recentlyPlayedBuildSfxQueue;
  private int recentlyPlayedBuildSfxCounter;

  public ClientGameSession(final boolean isMultiplayer,
                           final boolean isTutorial,
                           final int turnLengthIndex,
                           final @NotNull GameOptions options,
                           final @NotNull GameState.GameType gameType,
                           final @NotNull String @NotNull [] playerNames,
                           final int localPlayerIndex,
                           final boolean isUnrated,
                           final @Nullable GameUI ui) {
    this.isMultiplayer = isMultiplayer;
    this.localPlayerIndex = localPlayerIndex;
    this.isTutorial = isTutorial;
    this.isUnrated = isUnrated;
    if (this.isMultiplayer) {
      this.gameState = new GameState(turnLengthIndex, options, gameType, playerNames);
    } else if (this.isTutorial) {
      this.gameState = TutorialState.createTutorialGameState(turnLengthIndex, options, playerNames);
      TutorialState.initialize(this);
    } else {
      this.gameState = GameState.generate(turnLengthIndex, playerNames, options, gameType);
    }

    if (this.isMultiplayer) {
      for (int i = 0; i < this.gameState.playerCount; ++i) {
        if (playerNames[i].equals("bot_sp")) {
          this.gameState.players[i].name = StringConstants.EMPIRE_NAMES[i];
        }
      }
    } else {
      this.gameState.players[0].name = JagexApplet.isAnonymous ? StringConstants.EMPIRE_NAMES[0] : JagexApplet.playerDisplayName;
      for (int i = 1; i < this.gameState.playerCount; ++i) {
        this.gameState.players[i].name = StringConstants.EMPIRE_NAMES[i];
      }
    }

    if (this.localPlayerIndex < 0) {
      this.localPlayer = null;
    } else {
      this.localPlayer = this.gameState.players[this.localPlayerIndex];
    }

    this.localPlayerIsAlive = this.localPlayer != null;
    this.ais = new TaskAI[this.gameState.players.length];
    for (final Player player : this.gameState.players) {
      player.stats = new PlayerStats(20);
    }

    if (this.isTutorial) {
      for (int i = 1; i < this.gameState.playerCount; ++i) {
        this.ais[i] = new TutorialAI3(this.gameState, this.gameState.players[i], this);
        this.ais[i].initialize(false);
      }
    } else if (!this.isMultiplayer) {
      for (int i = 0; i < this.gameState.playerCount; ++i) {
        if (i != this.localPlayerIndex) {
          this.ais[i] = new TaskAI(this.gameState.players[i], this.gameState, this);
          this.ais[i].initialize(true);
        }
      }
    }

    if (ui != null) {
      ui.initialize(this);
      this.ui = ui;
    }

    if (this.isMultiplayer) {
      this.unsentProjectOrders = new ArrayList<>();
      this.unsentBuildOrders = new ArrayList<>();
      this.unsentMoveOrders = new ArrayList<>();
      this.orderUpdateIdleTimer = -1;
      this.orderUpdateBusyTimer = -1;
      this.gameState.setTurnNumber(-1);
    } else {
      this.initialize();
      this.gameView.targetedSystem = null;
      this.gameState.setTurnNumber(0);
      this.turnTicksLeft = this.gameState.getTurnDurationTicks();
    }
  }

  public void initialize() {
    this.gameView = new GameView(this.gameState.map, this.gameState.players, this.localPlayer, this.isTutorial);
    if (this.ui == null) {
      this.ui = new GameUI(this);
    }
    this.gameView.setGameUI(this.ui);

    if (!this.isMultiplayer && !this.isTutorial) {
      for (int i = 0; i < this.gameState.playerCount; ++i) {
        if (this.ais[i] != null) {
          this.ais[i].initialize();
          if (ShatteredPlansClient.debugModeEnabled) {
            final String message = "My personality is " + StringConstants.AI_TYPES[this.ais[i].getType()];
            this.showChatMessage(this.gameState.players[i], message);
          }
        }
      }
    }

    this.gameState.recalculatePlayerFleetProduction();

    this.tacticalAnalysis = new TacticalAnalysis(this.gameState.map.systems.length);
    this.gameView.setTacticalAnalysis(this.tacticalAnalysis);

    if (this.localPlayerIndex < 0) {
      isAutoPlaying = false;
    }
    if (isAutoPlaying) {
      this.ais[this.localPlayerIndex] = new TaskAI(this.localPlayer, this.gameState, this);
    }

    for (final Player player : this.gameState.players) {
      if (player.contiguousForces.isEmpty()) {
        this.gameState.markPlayerDefeated(player.index);
      }
    }

    this.gameView.resetSystemState();
    this.updateViewStateForTurnStart();

    for (int i = 0; i < this.gameState.playerCount; ++i) {
      if (this.ais[i] != null) {
        this.ais[i].makeDesiredPactOffers();
      }
    }

    this.recalculateSystemState();
    if (isAutoPlaying || this.localPlayer == null || this.localPlayer.contiguousForces.isEmpty()) {
      this.gameView.unitScalingFactor = this.gameView.maxUnitScalingFactor;
    } else {
      this.gameView.a968(this.localPlayer.contiguousForces.get(0));
    }

    this.gameView.a487();
  }

  public static void updateDrawOfferMenuItem() {
    if (playSession.gameState.isPlayerOfferingDraw(playSession.localPlayerIndex)) {
      ShatteredPlansClient.MENU_ITEM_LABELS[11] = StringConstants.CANCEL_DRAW;
    } else if (playSession.gameState.isAnyoneOfferingDraw()) {
      ShatteredPlansClient.MENU_ITEM_LABELS[11] = StringConstants.ACCEPT_DRAW;
    } else {
      ShatteredPlansClient.MENU_ITEM_LABELS[11] = StringConstants.OFFER_DRAW;
    }
  }

  private void showChatMessage(final Player player, final String message) {
    if (!this.isTutorial) {
      final String var4 = player.name + ": ";
      int var5 = Component.lobbyChatMessagesScrollPane.width;
      if (this.isMultiplayer) {
        var5 -= Component.CHAT_FONT.measureLineWidth("[" + Strings.format(StringConstants.XS_GAME, this.gameState.players[0].name) + "] ");
      }

      final int var6 = !this.isMultiplayer ? player.color1 : 0;
      final int var7 = var5 - Component.CHAT_FONT.measureLineWidth(var4) - 20;
      if (var7 < Component.CHAT_FONT.measureLineWidth(message)) {
        final String[] obj = GameUI.breakLinesWithColorTags(Component.CHAT_FONT, message, new int[]{var7});
        assert obj != null;
        for (final String var11 : obj) {
          ContextMenu.showChatMessage(ChatMessage.Channel.ROOM, var4 + var11, var6, JagexApplet.playerDisplayName, this.gameState.players[0].name);
        }
      } else {
        ContextMenu.showChatMessage(ChatMessage.Channel.ROOM, var4 + message, var6, JagexApplet.playerDisplayName, this.gameState.players[0].name);
      }
    }
  }

  private void destroyPlayerForces(final Player player) {
    for (final ContiguousForce force : player.contiguousForces) {
      for (final StarSystem system : force) {
        system.contiguousForce = null;
        system.owner = null;
      }
    }
    player.contiguousForces.clear();
  }

  private void readTurnOrders(final CipheredBuffer packet, final int len) {
    this.gameState.readTurnOrders(packet, len);
  }

  public void handlePlayerLeft(final boolean dueToMouseEvent) {
    if (!this.isMultiplayer) {
      ShatteredPlansClient.clearChatMessages();
      Menu.switchTo(Menu.Id.MAIN, 0, dueToMouseEvent);
    }
    ShatteredPlansClient.saveProfile();
  }

  private void recalculateTacticalAnalysis() {
    this.tacticalAnalysis.analyze(this.gameState, this.localPlayer);
  }

  public boolean isSystemOwnershipGuaranteed(final StarSystem system) {
    return this.tacticalAnalysis.isOwnershipGuaranteed(system);
  }

  private void readTurnOrdersAndUpdate(final CipheredBuffer packet, final int len) {
    this.readTurnOrders(packet, len);

    for (final BuildFleetsOrder order : this.gameState.buildOrders) {
      if (this.gameState.gameOptions.unifiedTerritories) {
        order.system.owner.combinedForce.fleetsAvailableToBuild -= order.quantity;
      } else {
        order.system.contiguousForce.fleetsAvailableToBuild -= order.quantity;
      }
      order.system.remainingGarrison += order.quantity;
    }

    for (final MoveFleetsOrder order : this.gameState.moveOrders) {
      order.source.remainingGarrison -= order.quantity;
    }

    this.recalculateTacticalAnalysis();
  }

  // only used by the tutorial
  public void setMap(final Map map) {
    this.gameState.setMap(map);

    this.tacticalAnalysis = new TacticalAnalysis(map.systems.length);
    this.recalculateTacticalAnalysis();

    this.gameView.setMap(map);
    this.gameView.setTacticalAnalysis(this.tacticalAnalysis);
  }

  public void draw() {
    this.gameView.draw(this.gameState.tannhauserLinks, this.gameState.projectOrders, ShatteredPlansClient.debugModeEnabled && this.desynced);
  }

  private void handleRightClickOrderCanceling(final int mouseX, final int mouseY) {
    if (JagexApplet.mouseButtonJustClicked == MouseState.Button.RIGHT && !this.rightMouseDown) {
      this.rightMouseDown = true;
      this.rightClickX = mouseX;
      this.rightClickY = mouseY;
      this.rightMouseDragging = false;
    } else {
      if (this.rightMouseDown && JagexApplet.mouseButtonDown == MouseState.Button.RIGHT
          && MathUtil.isEuclideanDistanceGreaterThan(this.rightClickX - mouseX, this.rightClickY - mouseY, 5)) {
        this.rightMouseDragging = true;
      }

      if (this.rightMouseDown && JagexApplet.mouseButtonDown != MouseState.Button.RIGHT) {
        this.rightMouseDown = false;
        if (!this.rightMouseDragging) {
          if (this.placementMode != PlacementMode.NONE) {
            Sounds.play(Sounds.SFX_SHIP_SELECTION);
            this.ui.setPlacementMode(PlacementMode.NONE);
          } else if (this.gameView.targetedSystem != null) {
            this.gameState.projectOrders.stream()
                .filter(order -> order.player == this.localPlayer && (order.target == this.gameView.targetedSystem || order.source == this.gameView.targetedSystem))
                .toList().forEach(this::cancelProjectOrder);
          }
        }
      }
    }
  }

  @Override
  public void showAIChatMessage(final @NotNull Player sender,
                                final @NotNull Player recipient,
                                @MagicConstant(valuesFromClass = StringConstants.AIMessage.class) final int which,
                                final int systemIndex) {
    final Player self = this.isMultiplayer ? this.localPlayer : this.gameState.players[0];
    if (self == recipient) {
      final String[] messages = StringConstants.AI_CHAT[which];
      if (messages.length != 0) {
        final Player largestPlayer = this.gameState.players[this.gameState.playerFleetProductionRanks[this.gameState.playerCount - 1]];
        final String systemName = systemIndex < this.gameState.map.systems.length ? this.gameState.map.systems[systemIndex].name : "";
        String message = messages[ShatteredPlansClient.randomIntBounded(messages.length)];
        message = Strings.formatNamed(message, "largestplayer", largestPlayer.name);
        message = Strings.formatNamed(message, "you", this.localPlayer.name);
        message = Strings.formatNamed(message, "me", sender.name);
        message = Strings.formatNamed(message, "system", systemName);
        this.showChatMessage(sender, message);
      }
    }
  }

  @SuppressWarnings("SameParameterValue")
  public boolean handlePacket(final int type, final CipheredBuffer packet, final int len) {
    switch (type) {
      case S2CPacket.Type.VICTORY -> {
        final byte winnerId = packet.readByte();
        this.gameState.setWinner(winnerId);
        this.handleVictory();
        return true;
      }
      case S2CPacket.Type.DRAW_OFFERS -> {
        this.gameState.receivePlayersOfferingDrawBitmap(packet.readUByte());
        if (playSession == this) {
          updateDrawOfferMenuItem();
        }
        return true;
      }
      case S2CPacket.Type.RESIGNATIONS -> {
        this.gameState.receiveResignedPlayersBitmap(packet.readUByte());
        return true;
      }
      case S2CPacket.Type.REMATCH_OFFERS -> {
        this.gameState.receivePlayersOfferingRematchBitmap(packet.readUByte());
        if (this == playSession) {
          ShatteredPlansClient.a150wp();
        }
        return true;
      }
      case S2CPacket.Type.PLAYERS_LEFT -> {
        this.leftPlayersBitmap = packet.readUByte();
        return true;
      }
      case S2CPacket.Type.ADVANCE_TURN -> {
        final int turnNumber = packet.readUByte();
        final int alliances = packet.readUShort();
        final int turnSeed = packet.readInt();
        final int checksum = packet.readInt();
        final int turnNameIndex2 = packet.readUByte();
        final int turnNameIndex1 = packet.readUByte();
        final int turnTicksLeft = packet.readUShort();

        this.gameState.setTurnNameIndexes(turnNameIndex1, turnNameIndex2);
        if (this.gameState.turnNumber == -1) {
          this.gameState.setTurnNumber(turnNumber);
          this.tallyPlayerStats(null);
          this.turnNumberWhenJoined = this.gameState.turnNumber;
        } else if (this.gameState.turnNumber < turnNumber) {
          this.tutorialHandleTurnAdvance();
          this.advanceTurnMultiplayer(alliances, turnSeed);
          if (checksum == this.gameState.checksum()) {
            this.desynced = false;
          } else {
            C2SPacket.Type.DESYNC.write(C2SPacket.buffer);
            C2SPacket.buffer.withLengthShort(() -> {
              if (this.localPlayerIndex >= 0) {
                this.gameState.writeDesyncReport(C2SPacket.buffer, this.localPlayer);
              }
            });
            if (!ShatteredPlansClient.debugModeEnabled) {
              JagexApplet.flushC2sPacket(0);
              JagexApplet.shutdownServerConnection();
            }
            this.desynced = true;
            isAutoPlaying = false;
          }

          this.readyToEndTurn = false;
          this.turnTicksLeft = turnTicksLeft;
          this.updateViewStateForTurnStart();
        }

        this.turnTicksLeft = turnTicksLeft;
        return true;
      }
      case S2CPacket.Type.TURN_ORDERS -> {
        this.readTurnOrders(packet, len);
        return true;
      }
      case S2CPacket.Type.DIPLOMATIC_PACTS -> {
        this.readPactUpdates(packet, len);
        return true;
      }
      case S2CPacket.Type.PLAYERS_WAITING_ON -> {
        this.playersWaitingOn = packet.readUByte();
        return true;
      }
      case S2CPacket.Type.TURN_ORDERS_AND_UPDATE -> {
        this.readTurnOrdersAndUpdate(packet, len);
        return true;
      }
      case S2CPacket.Type.RESEND_ALL_TURN_ORDERS -> {
        this.resendAllTurnOrders();
        return true;
      }
      case S2CPacket.Type.AI_CHAT -> {
        final int senderIndex = packet.readUByte();
        @MagicConstant(valuesFromClass = StringConstants.AIMessage.class) final int which = packet.readUByte();
        final int systemIndex = packet.readUByte();
        this.showAIChatMessage(this.gameState.players[senderIndex], this.localPlayer, which, systemIndex);
        return true;
      }
      case 74 -> {
        if (ShatteredPlansClient.debugModeEnabled) {
          final int var2 = len / 4;
          for (int i = 0; i < var2; ++i) {
            final int var4 = packet.readUByte();
            final int var5 = packet.readUByte();
            final int var6 = packet.readUShort();
            final String var12 = "My personality type is " + StringConstants.AI_TYPES[var5] + " and my rating is " + var6;
            this.showChatMessage(this.gameState.players[var4], var12);
          }
          return true;
        } else {
          return false;
        }
      }
      default -> {
        return false;
      }
    }
  }

  private void handleVictory() {
    Menu.switchTo(Menu.Id.GAME, 0, false);
    this.localPlayerIsAlive = false;
    this.ui.handleVictory();
    if ((this.localPlayer != null && this.gameState.isPlayerDefeated(this.localPlayerIndex)) || this.gameState.victoryChecker.isLoser(this.localPlayer)) {
      ShatteredPlansClient.a827jo(Sounds.MUSIC_LOSE, 10, false);
    } else if (this.gameState.hasEnded) {
      ShatteredPlansClient.a827jo(Sounds.MUSIC_WIN, 10, false);
    }
//    if (isAutoPlaying && !this.gameState.isPlayerOfferingRematch(this.localPlayerIndex)) {
//      this.offerRematch();
//    }
    isAutoPlaying = false;
  }

  private void simulateTurn(final int seed) {
    this.turnEventLog = new TurnEventLog();
    this.gameView.resetSystemState();
    final int[] playerFleetProduction = this.tallyPlayerFleetProduction();
    this.gameState.simulateTurn(this.turnEventLog, seed);

    final Optional<String> maybeError = this.gameView.setTurnEvents(this.turnEventLog);
    this.gameState.resetTurnState();
    this.tallyPlayerStats(playerFleetProduction);
    maybeError.ifPresent(errorMessage -> {
      JagexApplet.clientError(null, errorMessage);
      JagexApplet.shutdownServerConnection();
    });

    if (this.isTutorial) {
      if (this.gameState.isPlayerDefeated(this.localPlayerIndex)) {
        TutorialState.a984fl("losegame");
      } else if (this.gameState.victoryChecker.isLoser(this.localPlayer)) {
        TutorialState.a984fl("wingame");
      }
    }
  }

  public void sendResign() {
    if (this.isMultiplayer) {
      C2SPacket.Type.RESIGN.write(C2SPacket.buffer);
    } else {
      throw new IllegalStateException();
    }
  }

  private void processInput() {
    boolean mouseHandled = false;
    if (this.ui.processInput(true)) {
      this.rightMouseDragging = true;
      mouseHandled = true;
      this.rightMouseDown = false;
      this.gameView._nb[1] = -1;
      this.gameView._nb[0] = -1;
      this.gameView.targetedSystem = null;
      this.gameView.selectedFleetOrder = null;
    } else {
      if (JagexApplet.mouseButtonJustClicked == MouseState.Button.LEFT) {
        this.handleLeftClick(JagexApplet.mousePressX, JagexApplet.mousePressY);
      }

      this.handleRightClickOrderCanceling(JagexApplet.mouseX, JagexApplet.mouseY);
      this.gameView.a115(this.placementMode, JagexApplet.mouseX, JagexApplet.mouseY);
    }

    float var3 = this.gameView.mapScrollPosnX;
    float var4 = this.gameView.mapScrollPosnY;
    if (JagexApplet.keysDown[96]) {
      var3 -= 5.0F * (this.gameView.unitScalingFactor + 50.0F) / 300.0F;
      if (var3 < 0.0F) {
        var3 = 0.0F;
      }
    }

    float var5 = this.gameView.unitScalingFactor;
    if (JagexApplet.keysDown[97]) {
      var3 += 5.0F * (this.gameView.unitScalingFactor + 50.0F) / 300.0F;
      if ((float) (this.gameState.map.drawingWidth) < var3) {
        var3 = (float) (this.gameState.map.drawingWidth);
      }
    }

    if (JagexApplet.keysDown[98]) {
      var4 -= 5.0F * (this.gameView.unitScalingFactor + 50.0F) / 300.0F;
      if (var4 < 0.0F) {
        var4 = 0.0F;
      }
    }

    if (JagexApplet.keysDown[99]) {
      var4 += 5.0F * (this.gameView.unitScalingFactor + 50.0F) / 300.0F;
      if ((float) (this.gameState.map.drawingHeight) < var4) {
        var4 = (float) (this.gameState.map.drawingHeight);
      }
    }

    if (JagexApplet.keysDown[27] || JagexApplet.keysDown[87]) {
      var5 /= 1.1F;
      if (var5 < AbstractGameView.TWO_HUNDRED_F) {
        var5 = AbstractGameView.TWO_HUNDRED_F;
      }
    }

    if (JagexApplet.keysDown[26] || JagexApplet.keysDown[88]) {
      var5 *= 1.1F;
      if (var5 > this.gameView.maxUnitScalingFactor) {
        var5 = this.gameView.maxUnitScalingFactor;
      }
    }

    if (mouseHandled || JagexApplet.mouseButtonDown != MouseState.Button.RIGHT) {
      this.isDragPanningMap = false;
    } else {
      if (this.isDragPanningMap) {
        final int var6 = JagexApplet.mouseX - this.gameView.lastTickMouseX;
        final int var7 = JagexApplet.mouseY - this.gameView.lastTickMouseY;
        var3 -= this.gameView.unitScalingFactor * (float) var6 / 300.0F;
        if (var3 < 0.0F) {
          var3 = 0.0F;
        }

        var4 -= (float) var7 * this.gameView.unitScalingFactor / 300.0F;
        if (var3 > (float) (this.gameState.map.drawingWidth)) {
          var3 = (float) (this.gameState.map.drawingWidth);
        }

        if (var4 < 0.0F) {
          var4 = 0.0F;
        }

        if (var4 > (float) (this.gameState.map.drawingHeight)) {
          var4 = (float) (this.gameState.map.drawingHeight);
        }
      }

      this.gameView.lastTickMouseX = JagexApplet.mouseX;
      this.gameView.lastTickMouseY = JagexApplet.mouseY;
      this.isDragPanningMap = true;
    }

    if (!mouseHandled && JagexApplet.mouseWheelRotation != 0) {
      final int var7 = this.ui.getHeight() / 2;
      final float var8 = var3 + (float) (JagexApplet.mouseX - ShatteredPlansClient.SCREEN_CENTER_X) * this.gameView.unitScalingFactor / 300.0F;
      final float var9 = (float) (JagexApplet.mouseY - var7) * this.gameView.unitScalingFactor / 300.0F + var4;
      int var10;
      if (JagexApplet.mouseWheelRotation <= 0) {
        for (var10 = 2 * JagexApplet.mouseWheelRotation; var10 < 0; ++var10) {
          var5 /= 1.1F;
        }

        if (!JagexApplet.DEBUG_MODE && var5 < AbstractGameView.TWO_HUNDRED_F) {
          var5 = AbstractGameView.TWO_HUNDRED_F;
        }
      } else {
        for (var10 = 0; JagexApplet.mouseWheelRotation * 2 > var10; ++var10) {
          var5 *= 1.1F;
        }

        if (var5 > this.gameView.maxUnitScalingFactor) {
          var5 = this.gameView.maxUnitScalingFactor;
        }
      }

      var3 = var8 - var5 * (float) (JagexApplet.mouseX - ShatteredPlansClient.SCREEN_CENTER_X) / 300.0F;
      var4 = var9 - var5 * (float) (JagexApplet.mouseY - var7) / 300.0F;
      if (var3 < 0.0F) {
        var3 = 0.0F;
      }

      if (var4 < 0.0F) {
        var4 = 0.0F;
      }

      if ((float) (this.gameState.map.drawingWidth) < var3) {
        var3 = (float) (this.gameState.map.drawingWidth);
      }

      if (var4 > (float) (this.gameState.map.drawingHeight)) {
        var4 = (float) (this.gameState.map.drawingHeight);
      }
    }

    if (this.gameView.mapScrollPosnX != var3 || this.gameView.mapScrollPosnY != var4 || this.gameView.unitScalingFactor != var5) {
      this.gameView.unitScalingFactor = var5;
      this.gameView.isAnimatingViewport = false;
      this.gameView._fb = null;
      this.gameView.mapScrollPosnY = var4;
      this.gameView.mapScrollPosnX = var3;
      this.gameView.a487();
    }

    if (JagexApplet.mouseButtonDown == MouseState.Button.NONE) {
      this.mouseHoldDelayAmount = MOUSE_HOLD_DELAY_MAX;
      this.mouseHoldDelayCounter = 0;
    } else if (this.gameView.selectedFleetOrder != null) {
      this.processInputFleetOrder(this.gameView.selectedFleetOrder);
    }

    this.gameView.c326();
  }

  private void sendOutstandingOrders(@SuppressWarnings("SameParameterValue") final CipheredBuffer packet) {
    C2SPacket.Type.ORDERS.write(packet);
    packet.withLengthShort(() -> {
      packet.writeByte(this.gameState.turnNumber);
      TurnOrders.write(packet,
          this.unsentProjectOrders,
          this.unsentBuildOrders,
          this.unsentMoveOrders);
    });
    this.unsentProjectOrders.clear();
    this.unsentBuildOrders.clear();
    this.unsentMoveOrders.clear();
    this.orderUpdateBusyTimer = -1;
    this.orderUpdateIdleTimer = -1;
    if (isAutoPlaying) {
      this.endTurn();
    }
  }

  @MagicConstant(intValues = {0, KeyState.Code.SHIFT, KeyState.Code.ALT, KeyState.Code.CONTROL})
  private int getModifierKey() {
    int modifierCode = 0;

    if (JagexApplet.keysDown[KeyState.Code.SHIFT]) {
      modifierCode = KeyState.Code.SHIFT;
    }

    if (JagexApplet.keysDown[KeyState.Code.ALT]) {
      if (modifierCode == 0) {
        modifierCode = KeyState.Code.ALT;
      } else {
        return 0;
      }
    }

    if (JagexApplet.keysDown[KeyState.Code.CONTROL]) {
      if (modifierCode == 0) {
        modifierCode = KeyState.Code.CONTROL;
      } else {
        return 0;
      }
    }

    return modifierCode;
  }

  public void offerRematch() {
    if (this.isMultiplayer) {
      this.gameState.setPlayerOfferingRematch(this.localPlayerIndex);
      C2SPacket.Type.OFFER_REMATCH.write(C2SPacket.buffer);
    } else {
      throw new IllegalStateException();
    }
  }

  private void processInputFleetOrder(final MoveFleetsOrder order) {
    if (order.player != this.localPlayer) return;
    if (this.mouseHoldDelayCounter > 0) {
      --this.mouseHoldDelayCounter;
      return;
    }

    if (JagexApplet.mouseButtonDown == MouseState.Button.LEFT && order != this.gameView._rb) {
      this.gameView.b423();
    }

    if (JagexApplet.mouseButtonDown == MouseState.Button.LEFT && this.gameView._Ab) {
      int var3 = 1;
      final int modifier = this.getModifierKey();
      final StarSystem var5 = this.gameView.selectedFleetOrder.source;
      if (modifier == KeyState.Code.CONTROL) {
        var3 = var5.minimumGarrison >= var5.remainingGarrison ? var5.remainingGarrison : var5.remainingGarrison - var5.minimumGarrison;
      } else if (modifier == KeyState.Code.ALT) {
        var3 = 5;
      }

      final int var6 = this.gameState.gameOptions.garrisonsCanBeRemoved ? 0 : var5.minimumGarrison;
      if (var5.remainingGarrison - var6 < var3) {
        var3 = var5.remainingGarrison - var6;
      }

      if (var3 <= 0) {
        Sounds.play(Sounds.SFX_SHIP_ATTACK_ORDER);
      } else {
        this.addToMoveOrder(this.gameView.selectedFleetOrder, var3);
      }
    } else if (JagexApplet.mouseButtonDown == MouseState.Button.LEFT && this.gameView._Gb) {
      int var3 = -1;
      final int modifier = this.getModifierKey();
      if (modifier == KeyState.Code.CONTROL) {
        var3 = -this.gameView.selectedFleetOrder.quantity;
      } else if (modifier == KeyState.Code.ALT) {
        var3 = -(Math.min(5, this.gameView.selectedFleetOrder.quantity));
      }

      this.addToMoveOrder(this.gameView.selectedFleetOrder, var3);
      if (this.gameView.selectedFleetOrder.quantity == 0) {
        this.gameView._nb[1] = -1;
        this.gameView._nb[0] = -1;
        this.gameView.selectedFleetOrder = null;
        this.gameView.b423();
      }
    } else if (JagexApplet.mouseButtonDown == MouseState.Button.LEFT && this.gameView._Bb) {
      final int var3 = -order.quantity;
      this.addToMoveOrder(this.gameView.selectedFleetOrder, var3);
      this.gameView._nb[1] = -1;
      this.gameView.selectedFleetOrder = null;
      this.gameView._nb[0] = -1;
      this.gameView.b423();
    }

    this.mouseHoldDelayAmount -= 5;
    if (this.mouseHoldDelayAmount < 0) {
      this.mouseHoldDelayAmount = 0;
    }

    this.mouseHoldDelayCounter = this.mouseHoldDelayAmount;
    this.recalculateSystemState();
  }

  public void recalculateSystemState() {
    if (this.systemOwners.length < this.gameState.map.systems.length) {
      this.systemOwners = new Player[this.gameState.map.systems.length];
      this.remainingGarrisons = new int[this.gameState.map.systems.length];
      this.systemForces = new ContiguousForce[this.gameState.map.systems.length];
    }

    for (final StarSystem system : this.gameState.map.systems) {
      final int i = system.index;
      this.systemOwners[i] = system.owner;
      this.remainingGarrisons[i] = system.owner == this.localPlayer ? system.remainingGarrison : system.garrison;
      this.systemForces[i] = system.contiguousForce;
    }

    this.gameView.assignSystemState(this.remainingGarrisons, this.systemForces, this.systemOwners, true);
  }

  public void endTurn() {
    if (this.isMultiplayer) {
      if (this.orderUpdateIdleTimer != -1) {
        this.sendOutstandingOrders(C2SPacket.buffer);
      }

      this.sendEndTurn();
      this.readyToEndTurn = true;
      if (this.playersWaitingOn > 1) {
        --this.playersWaitingOn;
      }
    } else {
      this.advanceTurnSinglePlayer();
    }
  }

  private void sendEndTurn() {
    C2SPacket.Type.END_TURN.write(C2SPacket.buffer);
    C2SPacket.buffer.writeByte(this.gameState.turnNumber);
    C2SPacket.buffer.writeInt(this.gameState.ordersChecksum());
  }

  private void cancelEndTurn() {
    C2SPacket.Type.CANCEL_END_TURN.write(C2SPacket.buffer);
    C2SPacket.buffer.writeByte(this.gameState.turnNumber);
  }

  private void handlePactOffer(final Player offerer, final Player offeree) {
    this.getAI(offeree).ifPresent(ai -> ai.handlePactOffer(offerer));
  }

  private void playerIssuedOrder() {
    if (this.readyToEndTurn) {
      this.cancelEndTurn();
      this.readyToEndTurn = false;
    }

    if (this.orderUpdateBusyTimer == -1) {
      this.orderUpdateBusyTimer = 250;
    }

    this.orderUpdateIdleTimer = 25;
  }

  private void advanceTurnMultiplayer(final int alliances, final int seed) {
    this.gameState.recalculateFleetsRemaining();
    this.gameState.setAlliancesBitmap(alliances);
    this.simulateTurn(seed);
    this.gameView.turnSeed = seed;
  }

  private void readPactUpdates(final Buffer packet, int len) {
    while (len > 0) {
      final int type = packet.readUByte();
      --len;
      if (type == 0) {
        final int offers = packet.readUByte();
        --len;
        final int newOffers = ~this.localPlayer.incomingPactOffersBitmap & offers;
        this.localPlayer.incomingPactOffersBitmap = offers;
        if (newOffers == 0) {
          continue;
        }

        for (int i = 0; i < this.gameState.playerCount; ++i) {
          if ((newOffers & (1 << i)) != 0) {
            this.handlePactOffer(this.gameState.players[i], this.localPlayer);
          }
        }
      } else if (type == 1) {
        final int offererIndex = packet.readUByte();
        final int offereeIndex = packet.readUByte();
        len -= 2;
        final Player offerer = this.gameState.players[offererIndex];
        final Player offeree = this.gameState.players[offereeIndex];
        JagexApplet.printDebug("RECV PACT " + offerer + " <-> " + offeree);
        Player.establishPact(offerer, offeree);
        this.handlePactAccepted(offerer, offeree);
      }
    }
  }

  private void tallyPlayerStats(final int[] playerFleetProduction) {
    final int[] var3 = new int[this.gameState.playerCount];
    final int[] var4 = new int[this.gameState.playerCount];
    final int[] var5 = new int[this.gameState.playerCount];
    final int[] var6 = new int[this.gameState.playerCount];
    final int[] var7 = new int[this.gameState.playerCount];
    final int[] var8 = new int[this.gameState.playerCount];
    final int[] var9 = new int[this.gameState.playerCount];
    final int[] var10 = new int[this.gameState.playerCount];

    for (final StarSystem system : this.gameState.map.systems) {
      if (system.owner != null) {
        var3[system.owner.index] += system.garrison;
        var6[system.owner.index]++;
        if (system.resources[0] >= 0) {
          for (int i = 0; i < GameState.NUM_RESOURCES; ++i) {
            var5[system.owner.index] += system.resources[i];
          }
        }
      }
    }

    for (final Player value : this.gameState.players) {
      if (!this.gameState.gameOptions.unifiedTerritories) {
        for (final ContiguousForce var28 : value.contiguousForces) {
          var4[value.index] += var28.fleetProduction;
        }
      } else if (value.combinedForce != null) {
        var4[value.index] = value.combinedForce.fleetProduction;
      }
    }

    if (this.turnEventLog != null) {
      for (final TurnEventLog.Event event : this.turnEventLog.events) {
        if (event instanceof BuildFleetsEvent buildEvent) {
          if (buildEvent.player != null) {
            var7[buildEvent.player.index] += buildEvent.quantity;
          }
        }

        if (event instanceof CombatEngagementLog combatLog) {
          for (int i = 0; i < this.gameState.playerCount; ++i) {
            var8[i] += combatLog.playerKills[i];
          }

          if (combatLog.victor == combatLog.ownerAtCombatStart) {
            if (combatLog.victor != null) {
              ++combatLog.victor.stats._y;
            }

            for (final Player player : combatLog.players) {
              if (player != null && player != combatLog.ownerAtCombatStart) {
                ++player.stats._q;
              }
            }
          } else {
            if (combatLog.ownerAtCombatStart != null) {
              ++combatLog.ownerAtCombatStart.stats._a;
            }

            if (combatLog.victor != null) {
              ++combatLog.victor.stats._m;
            }

            for (final Player player : combatLog.players) {
              if (player != null && combatLog.ownerAtCombatStart != player && combatLog.victor != player) {
                ++player.stats._q;
              }
            }
          }

          for (final CombatLogEvent combatEvent : combatLog.events) {
            if (combatLog.ownerAtCombatStart != combatEvent.player) {
              var10[combatEvent.player.index] += combatEvent.fleetsAtStart;
            }
          }
        }

        if (event instanceof StellarBombEvent bombEvent) {
          var8[bombEvent.player.index] += bombEvent.kill;
          ++bombEvent.player.stats._s;
        }

        if (event instanceof ProjectOrder projectOrder) {
          ++projectOrder.player.stats._s;
        }

        if (event instanceof MoveFleetsOrder moveOrder) {
          moveOrder.player.stats._t++;
          moveOrder.player.stats._i += moveOrder.quantity;
          var9[moveOrder.player.index] += moveOrder.quantity;
        }
      }
    }

    final int turnIndex = this.gameState.turnNumber != -1 ? this.gameState.turnNumber % 100 : 0;
    for (int i = 0; i < this.gameState.playerCount; ++i) {
      final Player player = this.gameState.players[i];
      player.stats.fleets[turnIndex] = var3[i];
      player.stats.production[turnIndex] = var4[i];
      player.stats.systems[turnIndex] = var6[i];
      if (var3[i] > player.stats._A) {
        player.stats._A = var3[i];
      }

      player.stats._v += var8[i];
      if (var4[i] > player.stats._x) {
        player.stats._x = var4[i];
      }

      player.stats._o += var7[i];
      player.stats._r = player.stats._e + (player.stats._o - var3[i]);
      if (!player.contiguousForces.isEmpty()) {
        if (playerFleetProduction != null) {
          if (var5[i] > 0) {
            player.stats._u += (var4[i] * 800 + var5[i]) / (var5[i] * 2);
          }
          player.stats._z += (playerFleetProduction[i] + 200 * var10[i]) / (2 * playerFleetProduction[i]);
          player.stats._w += (playerFleetProduction[i] + 200 * var9[i]) / (2 * playerFleetProduction[i]);
        }
        player.stats._l = this.gameState.turnNumber - this.turnNumberWhenJoined;
      }
    }
  }

  @Override
  public void handleAIPactOffer(final @NotNull Player offerer, final @NotNull Player offeree) {
    if (this.isMultiplayer) {
      if (isAutoPlaying) {
        this.requestOrAcceptPact(offeree);
      } else {
        throw new RuntimeException();
      }
    }
    super.handleAIPactOffer(offerer, offeree);
  }

  public void sendOfferDraw() {
    if (this.isMultiplayer) {
      C2SPacket.Type.OFFER_DRAW.write(C2SPacket.buffer);
    } else {
      throw new IllegalStateException();
    }
  }

  public void addTutorialAIPlayer(final Player player) {
    this.gameState.addPlayer(player);
    final TutorialAI1 var3 = new TutorialAI1(this.gameState, player, this);
    final TutorialAI1[] ais = new TutorialAI1[this.ais.length + 1];

    for (int i = 0; i < this.ais.length; ++i) {
      if (this.ais[i] != null) {
        ais[i] = new TutorialAI1(this.gameState, this.gameState.players[i], this);
        final TutorialAI1 var6 = (TutorialAI1) this.ais[i];
        System.arraycopy(var6._i, 0, ais[i]._i, 0, var6._i.length);
      }
    }
    ais[player.index] = new TutorialAI1(this.gameState, player, this);
    ais[this.ais.length] = var3;
    this.ais = ais;
  }

  public void handleKeyTyped() {
    if (this.isMultiplayer) {
      if (this.localPlayerIndex >= 0 ? ShatteredPlansClient.isChatboxSelected : (JagexApplet.lastTypedKeyCode != KeyState.Code.ESCAPE && GameUI.isChatOpen)) {
        return;
      }
    }

    if (JagexApplet.lastTypedKeyCode == KeyState.Code.ESCAPE) {
      @MagicConstant(valuesFromClass = Menu.Id.class)
      int var8 = Menu.Id.PAUSE_SINGLEPLAYER;
      if (this.isMultiplayer) {
        if (this.localPlayerIndex < 0) {
          var8 = Menu.Id.PAUSE_MULTIPLAYER_3;
        } else if (this.localPlayerIsAlive) {
          var8 = Menu.Id.PAUSE_MULTIPLAYER_1;
        } else {
          var8 = Menu.Id.PAUSE_MULTIPLAYER_2;
        }
      }
      Menu.switchTo(var8, 0, false);
    } else {
      this.ui.h150();
      if (ShatteredPlansClient.debugModeEnabled && !this.isMultiplayer) {
        if (JagexApplet.keysDown[KeyState.Code.ALT] && JagexApplet.keysDown[KeyState.Code.LETTER_R]) {
          this.destroyPlayerForces(this.localPlayer);
          this.endTurn();
        } else if (JagexApplet.keysDown[KeyState.Code.ALT] && JagexApplet.keysDown[KeyState.Code.LETTER_V]) {
          for (final Player player : this.gameState.players) {
            if (player != this.localPlayer) {
              this.destroyPlayerForces(player);
            }
          }
          this.endTurn();
        }
      }

      if (ShatteredPlansClient.debugModeEnabled && this.isMultiplayer && this.localPlayer != null) {
        if (JagexApplet.keysDown[KeyState.Code.CONTROL] && JagexApplet.keysDown[KeyState.Code.SHIFT] && JagexApplet.lastTypedKeyCode == KeyState.Code.NUMBER_1) {
          this.autoPlanTurn();
        } else if (JagexApplet.keysDown[KeyState.Code.CONTROL] && JagexApplet.keysDown[KeyState.Code.SHIFT] && JagexApplet.lastTypedKeyCode == KeyState.Code.NUMBER_2) {
          this.autoPlanTurn();
          isAutoPlaying = false;
          this.ais[this.localPlayerIndex] = null;
        }
      }
    }
  }

  private void autoPlanTurn() {
    if (this.localPlayer == null) {
      throw new IllegalStateException("cannot plan turn without a local player");
    }

    isAutoPlaying = true;

    while (!this.gameState.moveOrders.isEmpty()) {
      this.cancelMoveOrder(this.gameState.moveOrders.get(0));
    }
    while (!this.gameState.buildOrders.isEmpty()) {
      this.cancelBuildOrder(this.gameState.buildOrders.get(0));
    }
    while (!this.gameState.projectOrders.isEmpty()) {
      this.cancelProjectOrder(this.gameState.projectOrders.get(0));
    }
    this.playerIssuedOrder();

    final TaskAI ai = new TaskAI(this.localPlayer, this.gameState, this);
    this.ais[this.localPlayerIndex] = ai;

    ai.makeDesiredPactOffers();
    for (final Player player : this.gameState.players) {
      if (player != this.localPlayer && this.localPlayer.hasPactOfferFrom(player)) {
        ai.handlePactOffer(player);
      }
    }
    final TurnOrders orders = ai.planAndGetTurnOrders();
    for (final StarSystem system : this.gameState.map.systems) {
      system.remainingGarrison = system.garrison;
    }
    this.addOrders(orders);

    this.recalculateSystemState();

    this.unsentMoveOrders.addAll(this.gameState.moveOrders);
    this.unsentBuildOrders.addAll(this.gameState.buildOrders);
    this.unsentProjectOrders.addAll(this.gameState.projectOrders);
    this.playerIssuedOrder();
  }

  private void handleLeftClick(final int x, final int y) {
    this.gameView.a115(this.placementMode, x, y);
    if (this.localPlayerIsAlive) {
      if (this.gameView.targetedSystem != null) {
        if (this.placementMode == PlacementMode.MOVE_FLEET_SRC) {
          if (this.localPlayer != null
              && this.gameView.targetedSystem.owner == this.localPlayer
              && this.gameView.targetedSystem.remainingGarrison > 0) {
            this.selectedSystem = this.gameView.targetedSystem;
            Sounds.play(Sounds.SFX_SHIP_SELECTION);
            this.ui.setPlacementMode(PlacementMode.MOVE_FLEET_DEST);

            this.gameView.highlightedSystems[this.selectedSystem.index] = SystemHighlight.GREEN;
            for (final StarSystem neighbor : this.selectedSystem.neighbors) {
              if (neighbor.owner == null || !this.localPlayer.allies[neighbor.owner.index]) {
                this.gameView.highlightedSystems[neighbor.index] = SystemHighlight.GRAY;
              }
            }
            for (final StarSystem system : this.selectedSystem.contiguousForce) {
              if (system != this.selectedSystem && this.gameState.movementInRange(this.selectedSystem, system)) {
                this.gameView.highlightedSystems[system.index] = SystemHighlight.GRAY;
              }
            }
          }
        } else if (this.placementMode == PlacementMode.MOVE_FLEET_DEST) {
          final StarSystem source = this.selectedSystem;
          final StarSystem target = this.gameView.targetedSystem;

          if ((source.hasNeighbor(target) && (target.owner == null || !this.localPlayer.allies[target.owner.index]))
              || ((source != target)
                  && (source.contiguousForce == target.contiguousForce)
                  && this.gameState.movementInRange(source, target))) {
            this.gameView.stopCombatAnimations();
            final int spareFleets = source.remainingGarrison - source.minimumGarrison;
            int fleetsToMove = spareFleets <= 0 ? 1 : (1 + spareFleets) / 2;
            final int modifier = this.getModifierKey();
            if (modifier == KeyState.Code.SHIFT) {
              fleetsToMove = 1;
            } else if (modifier == KeyState.Code.ALT) {
              fleetsToMove = 5;
            } else if (modifier == KeyState.Code.CONTROL) {
              if (spareFleets <= 0) {
                fleetsToMove = source.remainingGarrison;
              } else {
                fleetsToMove = spareFleets;
              }
            }

            final int immovableFleets = this.gameState.gameOptions.garrisonsCanBeRemoved ? 0 : source.minimumGarrison;
            if (fleetsToMove > source.remainingGarrison - immovableFleets) {
              fleetsToMove = source.remainingGarrison - immovableFleets;
            }

            if (fleetsToMove > 0) {
              this.handleMoveFleets(source, target, fleetsToMove);
              if (target.owner == this.localPlayer
                  || target.owner == null && target.garrison == 0) {
                Sounds.play(Sounds.SFX_SHIP_MOVE_ORDER);
              } else {
                Sounds.play(Sounds.SFX_SHIP_ATTACK_ORDER);
              }
            }

            this.recalculateSystemState();
          }

          this.ui.setPlacementMode(PlacementMode.NONE);
        } else if (this.placementMode == PlacementMode.BUILD_FLEET) {
          if (this.gameView.targetedSystem.contiguousForce == this.selectedForce
              || this.gameState.gameOptions.unifiedTerritories
              && this.gameView.targetedSystem.owner != null
              && this.gameView.targetedSystem.owner.combinedForce == this.selectedForce) {
            this.gameView.stopCombatAnimations();
            this.handleBuildFleets(this.gameView.targetedSystem);
            if (this.recentlyPlayedBuildSfxCounter > 3) {
              final int var5 = this.recentlyPlayedBuildSfxCounter - 3;
              int volume = 0x60 >> (var5 >> 1);
              if ((var5 & 1) != 0) {
                volume = (0xb505 * volume) >> 16;
              }
              if (volume < 24) {
                volume = 24;
              }
              Sounds.play(Sounds.SFX_FACTORY_NOISE, volume);
            } else {
              Sounds.play(Sounds.SFX_FACTORY_NOISE);
            }

            if (this.recentlyPlayedBuildSfxQueue == null) {
              this.recentlyPlayedBuildSfxQueue = new TickTimer(ShatteredPlansClient.currentTick);
            } else {
              this.recentlyPlayedBuildSfxQueue.addNext(ShatteredPlansClient.currentTick);
            }

            ++this.recentlyPlayedBuildSfxCounter;
            this.recalculateSystemState();
          }
        } else if (this.placementMode == PlacementMode.DEFENSIVE_NET) {
          if (this.gameView.targetedSystem.owner == this.localPlayer && !this.gameView.targetedSystem.hasDefensiveNet) {
            this.gameView.stopCombatAnimations();
            this.recalculateSystemState();
            this.handleBuildProject(GameState.ResourceType.METAL, this.gameView.targetedSystem);
            this.ui.setPlacementMode(PlacementMode.NONE);
            this.ui.markProjectPending(GameState.ResourceType.METAL);
          }
        } else if (this.placementMode == PlacementMode.TERRAFORM) {
          if (this.gameView.targetedSystem.owner == this.localPlayer && this.gameView.targetedSystem.score == StarSystem.Score.NORMAL) {
            this.gameView.stopCombatAnimations();
            this.recalculateSystemState();
            this.handleBuildProject(GameState.ResourceType.BIOMASS, this.gameView.targetedSystem);
            this.ui.setPlacementMode(PlacementMode.NONE);
            this.ui.markProjectPending(GameState.ResourceType.BIOMASS);
          }
        } else if (this.placementMode == PlacementMode.STELLAR_BOMB) {
          final StarSystem target = this.gameView.targetedSystem;
          final boolean ownsNeighboring = Arrays.stream(target.neighbors)
              .anyMatch(neighbor -> neighbor.owner == this.localPlayer);
          final boolean isNotAllied = target.owner == null || !this.localPlayer.allies[target.owner.index];
          if (ownsNeighboring && target.owner != this.localPlayer && isNotAllied) {
            this.gameView.stopCombatAnimations();
            this.recalculateSystemState();
            this.handleBuildProject(GameState.ResourceType.ENERGY, target);
            this.recalculateTacticalAnalysis();
            this.ui.setPlacementMode(PlacementMode.NONE);
            this.ui.markProjectPending(GameState.ResourceType.ENERGY);
          }
        } else if (this.placementMode == PlacementMode.GATE_SRC) {
          if (this.gameView.targetedSystem.owner == this.localPlayer) {
            this.gameView.stopCombatAnimations();
            this.recalculateSystemState();
            this.selectedSystem = this.gameView.targetedSystem;
            this.ui.setPlacementMode(PlacementMode.GATE_DEST);

            for (final StarSystem system : this.gameState.map.systems) {
              if (system != this.gameView.targetedSystem && !this.gameView.targetedSystem.hasNeighbor(system)) {
                this.gameView.highlightedSystems[system.index] = SystemHighlight.GRAY;
              }
            }
          }
        } else if (this.placementMode == PlacementMode.GATE_DEST)
          if (!this.selectedSystem.hasNeighbor(this.gameView.targetedSystem) && this.selectedSystem != this.gameView.targetedSystem) {
            this.gameView.stopCombatAnimations();
            this.recalculateSystemState();
            this.handleBuildProject(this.gameView.targetedSystem, this.selectedSystem);
            this.ui.setPlacementMode(PlacementMode.NONE);
            this.ui.markProjectPending(GameState.ResourceType.EXOTICS);
          }
      }
    }
  }

  private void advanceTurnSinglePlayer() {
    this.tutorialHandleTurnAdvance();

    if (++this.localPlayerIndex == this.gameState.players.length) {
      this.localPlayer = this.gameState.players[0];
      this.localPlayerIndex = 0;
      this.gameState.recalculateFleetsRemaining();
      this.simulateTurn(ShatteredPlansClient.globalRandom.nextInt());
      this.gameView.turnSeed = ShatteredPlansClient.globalRandom.nextInt();
      this.gameState.generateNewTurnName();

      for (int i = 0; i < this.gameState.playerCount; ++i) {
        if (this.ais[i] != null) {
          this.ais[i].makeDesiredPactOffers();
        }
      }
    }

    this.localPlayer = this.gameState.players[this.localPlayerIndex];
    this.turnTicksLeft = this.gameState.getTurnDurationTicks();
    this.updateViewStateForTurnStart();
  }

  public void cancelProjectOrder(@MagicConstant(valuesFromClass = GameState.ResourceType.class) final int type) {
    this.gameState.projectOrders.stream()
        .filter(order -> order.type == type)
        .findFirst()
        .ifPresent(this::cancelProjectOrder);
  }

  private void cancelProjectOrder(final ProjectOrder order) {
    order.source = null;
    order.target = null;
    if (this.isMultiplayer) {
      this.unsentProjectOrders.add(order);
      this.playerIssuedOrder();
    }
    this.gameState.projectOrders.remove(order);
    this.ui.handleProjectOrderCanceled(order.type);
    if (order.type == GameState.ResourceType.ENERGY) {
      this.recalculateTacticalAnalysis();
    }
  }

  public void tick(final boolean var1) {
    if (var1 && !ShatteredPlansClient.isPopupOpen()) {
      this.processInput();
    } else {
      this.ui.processInput(false);
    }

    this.ui.tick();
    this.gameView.tick(this.turnEventLog != null ? this.turnEventLog.events : null);

    while (this.recentlyPlayedBuildSfxQueue != null && ShatteredPlansClient.currentTick - this.recentlyPlayedBuildSfxQueue.createdTick >= 50) {
      --this.recentlyPlayedBuildSfxCounter;
      this.recentlyPlayedBuildSfxQueue = this.recentlyPlayedBuildSfxQueue.next;
    }

    if (this.isMultiplayer) {
      if (this.turnTicksLeft > 1 && --this.turnTicksLeft == 1 && !this.readyToEndTurn && this.localPlayerIsAlive) {
        this.endTurn();
      }

      if (this.turnTicksLeft % 200 == 0 && this.readyToEndTurn) {
        this.sendEndTurn();
      }
    } else {
      --this.turnTicksLeft;
    }

    if (this.isMultiplayer && this.localPlayerIsAlive && this.orderUpdateIdleTimer != -1 && (--this.orderUpdateIdleTimer == -1 || --this.orderUpdateBusyTimer < 0)) {
      this.sendOutstandingOrders(C2SPacket.buffer);
    }
  }

  @Override
  protected @NotNull Optional<AI> getAI(final @NotNull Player player) {
    return Optional.ofNullable(this.ais[player.index]);
  }

  @Override
  public void handlePactAccepted(final @NotNull Player offerer, final @NotNull Player offeree) {
    if (this.isTutorial) {
      TutorialState.a984fl("signtreaty");
    }

    if (offerer == this.localPlayer) {
      this.cancelOrdersToAttackPlayer(offeree);
    } else if (offeree == this.localPlayer) {
      this.cancelOrdersToAttackPlayer(offerer);
    }

    this.recalculateTacticalAnalysis();
    super.handlePactAccepted(offerer, offeree);
  }

  private void cancelOrdersToAttackPlayer(final Player player) {
    this.gameState.moveOrders.stream()
        .filter(order -> order.quantity > 0 && order.target.owner == player)
        .toList().forEach(this::cancelMoveOrder);
    this.gameState.projectOrders.stream()
        .filter(order -> order.type == GameState.ResourceType.ENERGY && order.target.owner == player)
        .toList().forEach(this::cancelProjectOrder);
    if (this.placementMode == PlacementMode.MOVE_FLEET_DEST || this.placementMode == PlacementMode.STELLAR_BOMB) {
      for (final StarSystem system : this.gameState.map.systems) {
        if (system.owner == player) {
          this.gameView.highlightedSystems[system.index] = SystemHighlight.NONE;
        }
      }
    }
  }

  private void tutorialHandleTurnAdvance() {
    if (this.isTutorial) {
      for (final ProjectOrder order : this.gameState.projectOrders) {
        if (order.type == GameState.ResourceType.METAL) {
          TutorialState.a984fl("defensivenet");
        } else if (order.type == GameState.ResourceType.BIOMASS) {
          TutorialState.a984fl("terraforming");
        } else if (order.type == GameState.ResourceType.ENERGY) {
          TutorialState.a984fl("stellarbomb");
        } else if (order.type == GameState.ResourceType.EXOTICS) {
          TutorialState.a984fl("tannhauser");
        }
      }

      if (this.gameState.turnNumber == 0) {
        if (this.gameState.gameOptions.unifiedTerritories) {
          for (final Player var4 : this.gameState.players) {
            var4.combinedForce.fleetsAvailableToBuild = 0;
          }
        } else {
          for (final Player var4 : this.gameState.players) {
            for (final ContiguousForce var5 : var4.contiguousForces) {
              var5.fleetsAvailableToBuild = 0;
            }
          }
        }
      }
    }
  }

  private void updateViewStateForTurnStart() {
    System.gc();
    this.gameState.victoryChecker.updateVictoryPanel(this.gameState, this.ui);

    for (final StarSystem system : this.gameState.map.systems) {
      system.remainingGarrison = system.garrison;
    }

    if (this.systemOwners == null || this.systemOwners.length < this.gameState.map.systems.length) {
      final int systemCount = this.gameState.map.systems.length;
      this.remainingGarrisons = new int[systemCount];
      this.systemOwners = new Player[systemCount];
      this.systemForces = new ContiguousForce[systemCount];
    }

    for (final StarSystem system : this.gameState.map.systems) {
      this.systemOwners[system.index] = system.owner;
      this.remainingGarrisons[system.index] = system.garrison;
      this.systemForces[system.index] = system.contiguousForce;
    }

    this.gameView.assignSystemState(this.remainingGarrisons, this.systemForces, this.systemOwners, false);
    this.recalculateTacticalAnalysis();
    if (this.isMultiplayer || this.ais[this.localPlayerIndex] == null) {
      if (isAutoPlaying && !this.desynced && this.localPlayerIsAlive) {
        this.ais[this.localPlayerIndex].makeDesiredPactOffers();
        this.ais[this.localPlayerIndex].planTurnOrders();
        this.recalculateTacticalAnalysis();

        this.unsentMoveOrders.addAll(this.gameState.moveOrders);
        this.unsentBuildOrders.addAll(this.gameState.buildOrders);
        this.unsentProjectOrders.addAll(this.gameState.projectOrders);
        this.playerIssuedOrder();
      }

      this.gameView.a487();
      this.ui.updateForTurnStart();
      this.ui.setPlacementMode(PlacementMode.NONE);
      this.playersWaitingOn = 0;

      for (int i = 0; i < this.gameState.playerCount; ++i) {
        if (!this.gameState.isPlayerDefeated(i)) {
          ++this.playersWaitingOn;
        }
      }

      if (this.gameState.hasEnded || this.localPlayer != null && this.gameState.isPlayerDefeated(this.localPlayerIndex)) {
        this.handleVictory();
      }
    } else {
      try {
        this.ais[this.localPlayerIndex].planTurnOrders();
      } catch (final Exception var6) {
        JagexApplet.clientError(var6, "AI has errored in single player game");
      }

      this.recalculateTacticalAnalysis();
      this.advanceTurnSinglePlayer();
    }
  }

  private void resendAllTurnOrders() {
    C2SPacket.Type.ALL_TURN_ORDERS.write(C2SPacket.buffer);
    C2SPacket.buffer.withLengthShort(() -> {
      C2SPacket.buffer.writeByte(this.gameState.turnNumber);
      this.gameState.writeTurnOrders(C2SPacket.buffer);
    });
    this.unsentProjectOrders.clear();
    this.unsentBuildOrders.clear();
    this.unsentMoveOrders.clear();

    this.orderUpdateIdleTimer = -1;
    this.orderUpdateBusyTimer = -1;
    if (this.readyToEndTurn) {
      this.sendEndTurn();
    }
  }

  private void addOrders(final @NotNull TurnOrders orders) {
    orders.projectOrders.forEach(this::addOrder);
    orders.buildOrders.forEach(this::addOrder);
    orders.moveOrders.forEach(this::addOrder);
    this.recalculateTacticalAnalysis();
    this.ui.updateAvailableFleetCounters();
  }

  private void addOrder(final @NotNull BuildFleetsOrder order) {
    final Force force = order.getForce(this.gameState.gameOptions);
    assert this.localPlayer != null;
    assert force.player == this.localPlayer;
    assert force.fleetsAvailableToBuild >= order.quantity;

    final BuildFleetsOrder keptOrder = this.gameState.buildOrders.stream()
        .filter(order::replaces).findFirst()
        .map(Functions.tee(order::appendTo))
        .orElseGet(() -> {
          this.gameState.buildOrders.add(order);
          return order;
        });

    if (this.isMultiplayer) {
      this.unsentBuildOrders.remove(keptOrder);
      this.unsentBuildOrders.add(keptOrder);
      this.playerIssuedOrder();
    }

    order.system.remainingGarrison += order.quantity;
    this.remainingGarrisons[order.system.index] += order.quantity;
    force.fleetsAvailableToBuild -= order.quantity;
  }

  private void addOrder(final @NotNull MoveFleetsOrder order) {
    assert this.localPlayer != null;
    assert order.source.owner == this.localPlayer;
    assert order.source.remainingGarrison >= order.quantity;

    final MoveFleetsOrder keptOrder = order.source.outgoingOrders.stream()
        .filter(order::replaces).findFirst()
        .map(Functions.tee(order::appendTo))
        .orElseGet(() -> {
          order.source.outgoingOrders.add(order);
          order.target.incomingOrders.add(order);
          this.gameState.moveOrders.add(order);
          return order;
        });

    if (this.isMultiplayer) {
      this.unsentMoveOrders.remove(keptOrder);
      this.unsentMoveOrders.add(keptOrder);
      this.playerIssuedOrder();
    }

    order.source.remainingGarrison -= order.quantity;
    this.remainingGarrisons[order.source.index] -= order.quantity;
  }

  private void addOrder(final @NotNull ProjectOrder order) {
    this.gameState.projectOrders.stream()
        .filter(order::replaces).findFirst()
        .ifPresent(this.gameState.projectOrders::remove);
    this.gameState.projectOrders.add(order);
    if (this.isMultiplayer) {
      this.unsentProjectOrders.add(order);
      this.playerIssuedOrder();
    }
  }

  private void addToMoveOrder(final MoveFleetsOrder order, final int quantity) {
    order.source.remainingGarrison -= quantity;
    this.remainingGarrisons[order.source.index] -= quantity;
    order.quantity += quantity;

    if (order.quantity < 0) {
      throw new RuntimeException();
    } else {
      if (order.quantity == 0) {
        order.source.outgoingOrders.remove(order);
        order.target.incomingOrders.remove(order);
        this.gameState.moveOrders.remove(order);
      }

      this.recalculateTacticalAnalysis();
      if (this.isMultiplayer) {
        this.unsentMoveOrders.remove(order);
        this.unsentMoveOrders.add(order);
        this.playerIssuedOrder();
      }
    }
  }

  private void cancelBuildOrder(final BuildFleetsOrder order) {
    order.system.remainingGarrison -= order.quantity;
    this.remainingGarrisons[order.system.index] -= order.quantity;
    order.getForce(this.gameState.gameOptions).fleetsAvailableToBuild += order.quantity;
    order.quantity = 0;
    this.unsentBuildOrders.add(order);
    this.gameState.buildOrders.remove(order);
  }

  private void cancelMoveOrder(final MoveFleetsOrder order) {
    this.addToMoveOrder(order, -order.quantity);
  }

  private void handleBuildFleets(final StarSystem targetSystem) {
    final int quantity;
    final int modifier = this.getModifierKey();
    if (modifier == KeyState.Code.CONTROL) {
      quantity = this.selectedForce.fleetsAvailableToBuild;
    } else if (modifier == KeyState.Code.ALT) {
      quantity = Math.min(5, this.selectedForce.fleetsAvailableToBuild);
    } else {
      quantity = 1;
    }

    final BuildFleetsOrder order = new BuildFleetsOrder(targetSystem, quantity);
    assert order.getForce(this.gameState.gameOptions) == this.selectedForce;
    this.addOrder(order);

    this.ui.updateAvailableFleetCounters();
    if (this.selectedForce.fleetsAvailableToBuild <= 0) {
      if (this.localPlayer == null || this.gameState.gameOptions.unifiedTerritories) {
        this.ui.setPlacementMode(PlacementMode.NONE);
      } else {
        this.localPlayer.contiguousForces.stream()
            .filter(force -> force.fleetsAvailableToBuild > 0).findFirst()
            .ifPresentOrElse(nextForce -> this.ui.activateFleetPlacement(nextForce, false), () -> {
              if (this.isTutorial) {
                TutorialState.a984fl("buildships");
              }
              this.ui.setPlacementMode(PlacementMode.NONE);
            });
      }
    } else {
      this.ui.setPlacementMode(PlacementMode.BUILD_FLEET);
      for (final StarSystem system : this.selectedForce) {
        this.gameView.highlightedSystems[system.index] = SystemHighlight.GRAY;
      }
    }
    this.recalculateTacticalAnalysis();
  }

  private void handleMoveFleets(final StarSystem source, final StarSystem target, final int quantity) {
    this.addOrder(new MoveFleetsOrder(source, target, quantity));
    this.recalculateTacticalAnalysis();
  }

  private void handleBuildProject(@MagicConstant(valuesFromClass = GameState.ResourceType.class) final int type, final StarSystem target) {
    this.addOrder(new ProjectOrder(type, this.localPlayer, target));
  }

  private void handleBuildProject(final StarSystem source, final StarSystem target) {
    this.addOrder(new ProjectOrder(this.localPlayer, source, target));
  }

  private int[] tallyPlayerFleetProduction() {
    final int[] production = new int[this.gameState.playerCount];

    for (final StarSystem system : this.gameState.map.systems) {
      if (system.owner != null) {
        production[system.owner.index] += system.garrison;
      }
    }

    for (final Player player : this.gameState.players) {
      if (this.gameState.gameOptions.unifiedTerritories) {
        if (player.combinedForce != null && player.combinedForce.fleetProduction > 0) {
          production[player.index] += player.combinedForce.fleetProduction;
        }
      } else {
        for (final ContiguousForce force : player.contiguousForces) {
          if (force.fleetProduction > 0) {
            production[player.index] += force.fleetProduction;
          }
        }
      }
    }

    return production;
  }

  public void requestOrAcceptPact(final Player offeree) {
    final Player offerer = this.localPlayer; // the offerer is implicitly the local player
    if (!this.gameState.isPlayerDefeated(offerer.index) && !this.gameState.isPlayerDefeated(offeree.index)) {
      if (offerer != offeree) {
        if (!offerer.isOfferingPactTo(offeree)) {
          Player.offerPact(offerer, offeree);
          if (this.isMultiplayer) {
            this.sendPactOrder(offeree, offerer);
            if (!offerer.hasPactOfferFrom(offeree)) {
              this.handlePactOffer(offerer, offeree);
            }
          } else if (offerer.hasPactOfferFrom(offeree)) {
            Player.establishPact(offerer, offeree);
            this.handlePactAccepted(offeree, offerer);
            if (this.isTutorial) {
              TutorialState.a984fl("signtreaty");
            }
          } else {
            this.handlePactOffer(offerer, offeree);
          }
        }
      }
    }
  }

  private void sendPactOrder(final Player offeree, final Player offerer) {
    C2SPacket.Type.ORDERS.write(C2SPacket.buffer);
    C2SPacket.buffer.withLengthShort(() -> {
      C2SPacket.buffer.writeByte(this.gameState.turnNumber);
      C2SPacket.buffer.writeByte(C2SPacket.OrderType.PACT);
      C2SPacket.buffer.writeByte(offerer.index);
      C2SPacket.buffer.writeByte(offeree.index);
    });
  }

  public boolean didPlayerLeave(final int index) {
    return (this.leftPlayersBitmap & (1 << index)) != 0;
  }
  public boolean haveAllOtherPlayersLeft() {
    return (1 << this.gameState.playerCount) - 1 == (this.leftPlayersBitmap | (1 << this.localPlayerIndex));
  }
}
