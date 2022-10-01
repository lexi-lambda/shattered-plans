package funorb.shatteredplans.server;

import funorb.io.Packet;
import funorb.io.PacketLengthType;
import funorb.io.WritableBuffer;
import funorb.shatteredplans.game.ai.AI;
import funorb.shatteredplans.C2SPacket;
import funorb.shatteredplans.game.GameOptions;
import funorb.shatteredplans.game.GameSession;
import funorb.shatteredplans.game.GameState;
import funorb.shatteredplans.client.JagexApplet;
import funorb.shatteredplans.game.Player;
import funorb.shatteredplans.S2CPacket;
import funorb.shatteredplans.client.ShatteredPlansClient;
import funorb.shatteredplans.StringConstants;
import funorb.shatteredplans.game.ai.TaskAI;
import funorb.shatteredplans.client.game.TurnEventLog;
import funorb.shatteredplans.map.StarSystem;
import io.netty.buffer.ByteBufAllocator;
import io.netty.channel.EventLoop;
import io.netty.util.concurrent.ScheduledFuture;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

public final class NetworkedGameSession extends GameSession {
  private final @NotNull ByteBufAllocator alloc;
  private final @NotNull EventLoop eventLoop;

  private final @NotNull LobbyRoom room;
  private final @NotNull GameState state;
  private final @NotNull List<ClientConnection> clients;
  private final @NotNull Map<ClientConnection, ClientPlayer> clientPlayers;
  private final @NotNull List<AI> aiPlayers;
  private int playersLeftBitmap = 0;

  private int turnSeed = -1;
  private long turnEndTimestamp;
  private @Nullable ScheduledFuture<?> endTurnFuture;
  private int alliancesBitmapAtTurnStart = 0;

  public NetworkedGameSession(final @NotNull ByteBufAllocator alloc,
                              final @NotNull EventLoop eventLoop,
                              final @NotNull LobbyRoom room,
                              final @NotNull GameState.GameType gameType,
                              final @NotNull GameState.GalaxySize galaxySize,
                              final @NotNull GameOptions options,
                              final int turnLengthIndex,
                              final @NotNull List<ClientConnection> clients,
                              final int aiPlayerCount) {
    this.alloc = alloc;
    this.eventLoop = eventLoop;
    this.room = room;

    final int playerCount = clients.size() + aiPlayerCount;
    assert playerCount <= StringConstants.EMPIRE_NAMES.length;
    final String[] playerNames = new String[playerCount];
    for (int i = 0; i < clients.size(); i++) {
      playerNames[i] = clients.get(i).username;
    }
    System.arraycopy(StringConstants.EMPIRE_NAMES, clients.size(), playerNames, clients.size(), aiPlayerCount);

    this.state = GameState.generate(turnLengthIndex, playerNames, options, gameType, galaxySize);
    this.clients = new ArrayList<>(clients);
    this.clientPlayers = new HashMap<>();
    for (int i = 0; i < clients.size(); i++) {
      this.clientPlayers.put(clients.get(i), new ClientPlayer(clients.get(i), this.state.players[i]));
    }

    this.aiPlayers = new ArrayList<>();
    for (int i = clients.size(); i < playerCount; i++) {
      final TaskAI ai = new TaskAI(this.state.players[i], this.state, this);
      this.aiPlayers.add(ai);
      ai.initialize(true);
    }

    this.scheduleTurnEnd();
  }

  public void shutdown() {
    if (this.endTurnFuture != null) {
      this.endTurnFuture.cancel(false);
      this.endTurnFuture = null;
    }
  }

  @Override
  protected @NotNull Optional<AI> getAI(final @NotNull Player player) {
    return this.aiPlayers.stream().filter(ai -> ai.getPlayer() == player).findFirst();
  }

  private Optional<ClientPlayer> getClientPlayer(final @NotNull Player player) {
    return this.clientPlayers.values().stream().filter(clientPlayer -> clientPlayer.player == player).findFirst();
  }

  private void broadcast(final @NotNull Packet packet) {
    this.clients.forEach(client -> client.channel.writeAndFlush(packet.retainedSlice()));
  }

  private void broadcastAndRelease(final @NotNull Packet packet) {
    try {
      this.broadcast(packet);
    } finally {
      packet.release();
    }
  }

  public void sendInitialState(final @NotNull ClientConnection client) {
    final ClientPlayer clientPlayer = this.clientPlayers.get(client);
    final Player localPlayer = clientPlayer != null ? clientPlayer.player : null;
    final Packet enterPacket = new Packet(S2CPacket.Type.ENTER_GAME, PacketLengthType.VARIABLE_SHORT, this.alloc.buffer());

    enterPacket.writeByte(this.state.turnLengthIndex);
    this.state.gameOptions.write(enterPacket);
    enterPacket.writeByte(this.state.gameType.encode());
    enterPacket.writeByte(1); // is unrated
    enterPacket.writeByte(localPlayer == null ? -1 : localPlayer.index);

    enterPacket.writeByte(this.state.playerNames.length);
    Arrays.stream(this.state.playerNames).forEach(enterPacket::writeNullBracketedString);

    if (clientPlayer != null) {
      enterPacket.writeByte(clientPlayer.endedTurn ? 1 : 0);
    }
    this.state.map.write(enterPacket, this.state.players, localPlayer, this.state.tannhauserLinks);
    this.state.victoryChecker.write(enterPacket);
    this.state.writeTurnNameIndexes(enterPacket);
    client.channel.write(enterPacket);

    client.channel.write(this.createAdvanceTurnPacket());
    client.channel.flush();
  }

  public void receiveOrders(final @NotNull ClientConnection client, final @NotNull Packet buffer, final int len) {
    final int startPos = buffer.readerIndex();
    final int turnNumber = buffer.readUByte();
    if (turnNumber != this.state.turnNumber) {
      return;
    }

    final ClientPlayer clientPlayer = this.clientPlayers.get(client);
    while (buffer.readerIndex() - startPos < len) {
      final int type = buffer.readUByte();
      if (type == C2SPacket.OrderType.PACT) {
        final Player offerer = this.state.readPlayer(buffer);
        final Player offeree = this.state.readPlayer(buffer);
        assert offerer == clientPlayer.player;
        assert offeree != clientPlayer.player;
        if (offerer.hasPactOfferFrom(offeree)) {
          Player.establishPact(offerer, offeree);
          this.handlePactAccepted(offerer, offeree);
        } else if (!offeree.hasPactOfferFrom(offerer)) {
          Player.offerPact(offerer, offeree);
          this.handlePactOffered(offerer, offeree);
        }
      } else if (type >= C2SPacket.OrderType.PROJECT) {
        clientPlayer.addOrder(this.state.readProjectOrder(buffer, type));
      } else if (type >= C2SPacket.OrderType.BUILD) {
        for (int i = 0; i < type - 191; i++) {
          clientPlayer.addOrder(this.state.readBuildOrder(buffer));
        }
      } else {
        for (int i = 0; i < type + 1; i++) {
          clientPlayer.addOrder(this.state.readMoveOrder(buffer));
        }
      }
    }
  }

  public void receiveEndTurn(final @NotNull ClientConnection client, final @NotNull Packet buffer) {
    final int turnNumber = buffer.readUByte();
    final int clientChecksum = buffer.readInt();
    final ClientPlayer clientPlayer = this.clientPlayers.get(client);
    final int serverChecksum = clientPlayer.ordersChecksum();
    if (turnNumber == this.state.turnNumber) {
      if (clientChecksum == serverChecksum) {
        clientPlayer.endedTurn = true;
        this.maybeEndTurn();
      } else {
        System.out.println(client.getLogPrefix() + " END TURN CHECKSUM MISMATCH (" + clientChecksum + ", " + serverChecksum + ")");
      }
    }
  }

  public void receiveCancelEndTurn(final @NotNull ClientConnection client, final @NotNull Packet buffer) {
    final int turnNumber = buffer.readUByte();
    if (turnNumber == this.state.turnNumber) {
      this.clientPlayers.get(client).endedTurn = false;
    }
  }

  public void handlePlayerResignation(final @NotNull ClientConnection client) {
    this.handlePlayerResignation(this.clientPlayers.get(client));
  }

  private void handlePlayerResignation(final @NotNull ClientPlayer clientPlayer) {
    if (!this.state.hasEnded) {
      this.state.receiveResignation(clientPlayer.player.index);
      this.broadcastAndRelease(new Packet(S2CPacket.Type.RESIGNATIONS, PacketLengthType.FIXED,
          this.alloc.buffer().writeByte(this.state.getResignedPlayersBitmap())));

      this.state.checkVictory();
      if (this.state.hasEnded) {
        assert this.state.winnerIndex >= 0;
        this.broadcastAndRelease(new Packet(S2CPacket.Type.VICTORY, PacketLengthType.FIXED,
            this.alloc.buffer().writeByte(this.state.winnerIndex)));
        this.shutdown();
      } else {
        this.maybeEndTurn();
      }
    }
  }

  public void addSpectator(final @NotNull ClientConnection client) {
    this.clients.add(client);
    this.sendInitialState(client);
  }

  public void removeClient(final @NotNull ClientConnection client) {
    this.clients.remove(client);
    final ClientPlayer player = this.clientPlayers.remove(client);
    client.channel.writeAndFlush(Packet.S2C_LEAVE_GAME);

    if (player != null) {
      this.playersLeftBitmap |= 1 << player.player.index;
      this.broadcastAndRelease(new Packet(S2CPacket.Type.PLAYERS_LEFT, PacketLengthType.FIXED,
          this.alloc.buffer().writeByte(this.playersLeftBitmap)));
      if (!this.state.hasEnded && !this.state.isPlayerDefeated(player.player.index)) {
        this.handlePlayerResignation(player);
      }
    }
  }

  public void processTurnStart() {
    // Note: we can’t call this in the constructor, because we haven’t sent the
    // initial turn state to clients yet, and sending them pact offers at that
    // point will cause protocol errors.
    this.clientPlayers.values().forEach(ClientPlayer::resetForTurnStart);
    if (this.state.hasEnded) {
      this.room.handleGameConcluded();
    } else {
      this.aiPlayers.forEach(AI::makeDesiredPactOffers);
    }
  }

  private void scheduleTurnEnd() {
    if (!this.state.hasEnded) {
      this.turnEndTimestamp = System.currentTimeMillis() + this.state.getTurnDurationMillis();
      // Even though the game is not yet over, it’s possible that all human
      // players are defeated, in which case we want to schedule a turn update
      // for the very near future to just play out AI moves.
      final int delay = this.areAllHumanPlayersDefeated() ? 5000 : this.state.getTurnDurationMillis();
      this.endTurnFuture = this.eventLoop.schedule(this::endTurn, delay, TimeUnit.MILLISECONDS);
    }
  }

  private int getTurnTicksLeft() {
    return (int) ((this.turnEndTimestamp - System.currentTimeMillis()) / GameState.MILLIS_PER_TICK);
  }

  private boolean haveAllPlayersEndedTurn() {
    return this.clientPlayers.values().stream()
        .allMatch(clientPlayer -> clientPlayer.endedTurn || this.state.isPlayerDefeated(clientPlayer.player.index));
  }

  private boolean areAllHumanPlayersDefeated() {
    return this.clientPlayers.values().stream()
        .allMatch(clientPlayer -> this.state.isPlayerDefeated(clientPlayer.player.index));
  }

  private void maybeEndTurn() {
    if (!this.state.hasEnded && this.haveAllPlayersEndedTurn()) {
      assert this.endTurnFuture != null;
      this.endTurnFuture.cancel(false);
      this.endTurn();
    }
  }

  private void endTurn() {
    this.alliancesBitmapAtTurnStart = this.state.getAlliancesBitmap();
    this.broadcastAndRelease(this.createPactUpdatesPacket());
    this.advanceTurn();
    this.broadcastAndRelease(this.createTurnOrdersPacket());
    this.broadcastAndRelease(this.createAdvanceTurnPacket());
    this.processTurnStart();
  }

  private void advanceTurn() {
    this.state.resetTurnState();
    this.clientPlayers.values().forEach(clientPlayer -> clientPlayer.submitOrders(this.state));
    this.aiPlayers.forEach(ai -> {
      for (final StarSystem system : this.state.map.systems) {
        system.remainingGarrison = system.garrison;
      }
      ai.planTurnOrders();
    });

    this.turnSeed = ShatteredPlansClient.globalRandom.nextInt();
    this.state.recalculateFleetsRemaining();
    this.state.simulateTurn(new TurnEventLog(), this.turnSeed);
    this.state.generateNewTurnName();
    this.scheduleTurnEnd();
  }

  private Packet createTurnOrdersPacket() {
    final Packet packet = new Packet(S2CPacket.Type.TURN_ORDERS, PacketLengthType.VARIABLE_SHORT, this.alloc.buffer());
    this.state.writeTurnOrders(packet);
    return packet;
  }

  private Packet createAdvanceTurnPacket() {
    final Packet turnPacket = new Packet(S2CPacket.Type.ADVANCE_TURN, PacketLengthType.FIXED, this.alloc.buffer());
    turnPacket.writeByte(this.state.turnNumber);
    turnPacket.writeShort(this.alliancesBitmapAtTurnStart);
    turnPacket.writeInt(this.turnSeed);
    turnPacket.writeInt(this.state.checksum());
    this.state.writeTurnNameIndexes(turnPacket);
    turnPacket.writeShort(this.getTurnTicksLeft());
    return turnPacket;
  }

  private void sendIncomingPactOffers(final ClientPlayer offeree) {
    final Packet packet = new Packet(S2CPacket.Type.DIPLOMATIC_PACTS, PacketLengthType.VARIABLE_BYTE, this.alloc.buffer());
    packet.writeByte(0);
    packet.writeByte(offeree.player.incomingPactOffersBitmap);
    offeree.client.channel.writeAndFlush(packet);
  }

  private Packet createPactUpdatesPacket() {
    final Packet packet = new Packet(S2CPacket.Type.DIPLOMATIC_PACTS, PacketLengthType.VARIABLE_BYTE, this.alloc.buffer());
    for (int i = 0; i < this.state.playerCount; i++) {
      final Player player = this.state.players[i];
      for (int j = i + 1; j < this.state.playerCount; j++) {
        if (player.pactTurnsRemaining[j] == 3) {
          JagexApplet.printDebug("SEND PACT " + player + " <-> " + this.state.players[j]);
          ((WritableBuffer) packet).writeByte(1);
          ((WritableBuffer) packet).writeByte(i);
          ((WritableBuffer) packet).writeByte(j);
        }
      }
    }
    return packet;
  }

  private void handlePactOffered(final Player offerer, final Player offeree) {
    this.getAI(offeree).ifPresent(ai -> ai.handlePactOffer(offerer));
    this.getClientPlayer(offeree).ifPresent(this::sendIncomingPactOffers);
  }

  @Override
  public void handlePactAccepted(final @NotNull Player offerer, final @NotNull Player offeree) {
    super.handlePactAccepted(offerer, offeree);

    final Packet packet = new Packet(S2CPacket.Type.DIPLOMATIC_PACTS, PacketLengthType.VARIABLE_BYTE, this.alloc.buffer());
    try {
      packet.writeByte(1);
      offerer.write(packet);
      offeree.write(packet);
      this.getClientPlayer(offerer).ifPresent(clientPlayer -> {
        JagexApplet.printDebug("SEND PACT " + offerer + " <- " + offeree);
        clientPlayer.client.channel.writeAndFlush(packet.retainedSlice());
      });
      this.getClientPlayer(offeree).ifPresent(clientPlayer -> {
        JagexApplet.printDebug("SEND PACT " + offerer + " -> " + offeree);
        clientPlayer.client.channel.writeAndFlush(packet.retainedSlice());
      });
    } finally {
      packet.release();
    }
  }

  @Override
  public void showAIChatMessage(final @NotNull Player sender, final @NotNull Player recipient, final int which, final int systemIndex) {
    this.getClientPlayer(recipient).ifPresent(clientPlayer -> clientPlayer.client.channel.writeAndFlush(new Packet(
        S2CPacket.Type.AI_CHAT,
        PacketLengthType.FIXED,
        this.alloc.buffer(3, 3).writeByte(sender.index).writeByte(which).writeByte(systemIndex))));
  }

  @Override
  public void handleAIPactOffer(final @NotNull Player offerer, final @NotNull Player offeree) {
    super.handleAIPactOffer(offerer, offeree);
    this.getClientPlayer(offeree).ifPresent(this::sendIncomingPactOffers);
  }
}
