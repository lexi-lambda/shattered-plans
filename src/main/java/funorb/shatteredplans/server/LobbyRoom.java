package funorb.shatteredplans.server;

import funorb.io.Packet;
import funorb.io.PacketLengthType;
import funorb.io.WritableBuffer;
import funorb.client.lobby.ClientLobbyRoom;
import funorb.shatteredplans.game.GameOptions;
import funorb.shatteredplans.game.GameSession;
import funorb.shatteredplans.game.GameState;
import funorb.shatteredplans.game.GameState.GalaxySize;
import funorb.shatteredplans.game.GameState.GameType;
import funorb.client.lobby.LobbyPlayer;
import funorb.shatteredplans.S2CPacket;
import org.intellij.lang.annotations.MagicConstant;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public final class LobbyRoom {
  private final @NotNull LobbyState lobby;

  public final int id;
  public @NotNull ClientConnection owner;
  private final List<ClientConnection> clients = new ArrayList<>();
  private final Set<ClientConnection> invitations = new HashSet<>();
  private final Set<ClientConnection> joinRequests = new HashSet<>();
  public NetworkedGameSession session;

  @SuppressWarnings({"FieldCanBeLocal"})
  private final boolean isRated = false;
  private int maxHumanPlayerCount = 4;
  private int maxAiPlayerCount = 4;
  private int turnLengthIndex = 0;
  private @NotNull WhoCanJoin whoCanJoin = WhoCanJoin.INVITE_ONLY;
  private @NotNull GalaxySize galaxySize = GalaxySize.MEDIUM;
  private boolean spectatingAllowed = true;
  private @NotNull GameState.GameType gameType = GameType.CONQUEST;
  private boolean classicRuleset = true;

  private long startedAt;
  private int finalElapsedTime = -1;

  public LobbyRoom(final @NotNull LobbyState lobby,
                   final int id,
                   final @NotNull ClientConnection owner) {
    this.lobby = lobby;
    this.id = id;
    this.owner = owner;
    this.addClient(owner);
  }

  public boolean isSpectatingAllowed() {
    return this.spectatingAllowed;
  }

  public void destroy() {
    assert this.clients.isEmpty();
    if (this.session != null) {
      this.session.shutdown();
      this.session = null;
    }
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

  public void handleJoinRequest(final @NotNull ClientConnection client) {
    if (this.clients.size() >= this.maxHumanPlayerCount) {
      this.sendRoomStatus(client, ClientLobbyRoom.Status.ROOM_FULL);
    } else if (this.invitations.contains(client)) {
      this.removeInvitation(client, LobbyPlayer.Status.JOINED_YOUR_GAME);
      this.addClient(client);
    } else if (this.isAllowedToJoin(client)) {
      this.addClient(client);
    } else if (!this.joinRequests.contains(client)) {
      this.addJoinRequest(client);
    }
  }

  public void handleLeaveRequest(final @NotNull ClientConnection client) {
    if (this.clients.contains(client)) {
      this.removeClient(client, LobbyPlayer.Status.DROPPED_OUT);
    } else if (this.invitations.contains(client)) {
      this.removeInvitation(client, LobbyPlayer.Status.DECLINED_INVITE);
      this.sendRoomStatus(client, ClientLobbyRoom.Status.YOU_DECLINED_INVITE);
    } else if (this.joinRequests.contains(client)) {
      this.removeJoinRequest(client, LobbyPlayer.Status.WITHDREW_REQUEST);
      this.sendRoomStatus(client, ClientLobbyRoom.Status.REQUEST_WITHDRAWN);
    }
  }

  public void handleInviteRequest(final @NotNull ClientConnection client) {
    if (!this.clients.contains(client)) {
      if (this.joinRequests.contains(client)) {
        this.removeJoinRequest(client, LobbyPlayer.Status.JOINED_YOUR_GAME);
        this.addClient(client);
      } else if (!this.invitations.contains(client)) {
        this.addInvitation(client);
      }
    }
  }

  public void handleKickRequest(final @NotNull ClientConnection client) {
    if (client != this.owner) {
      if (this.clients.contains(client)) {
        this.removeClient(client, LobbyPlayer.Status.KICKED);
      } else if (this.invitations.contains(client)) {
        this.removeInvitation(client, LobbyPlayer.Status.NONE);
        this.sendRoomStatus(client, ClientLobbyRoom.Status.INVITE_WITHDRAWN);
      } else if (this.joinRequests.contains(client)) {
        this.removeJoinRequest(client, LobbyPlayer.Status.NONE);
        this.sendRoomStatus(client, ClientLobbyRoom.Status.REQUEST_DECLINED);
      }
    }
  }

  private void addClient(final @NotNull ClientConnection client) {
    this.clients.add(client);
    client.room = this;

    this.sendRoomStatus(client, ClientLobbyRoom.Status.NONE);
    final Packet packet = new Packet(S2CPacket.Type.LOBBY, PacketLengthType.VARIABLE_BYTE, client.channel.alloc().buffer());
    packet.writeByte(S2CPacket.LobbyAction.YOU_JOINED_ROOM);
    packet.writeShort(this.id);
    this.writeState(packet, client, false, false);
    client.channel.write(packet);

    this.clients.stream().filter(client1 -> client1 != client)
        .forEach(client1 -> client.channel.write(createPlayerJoinedPacket(client1)));
    this.broadcastAndRelease(createPlayerJoinedPacket(client));
  }

  public void removeClient(final @NotNull ClientConnection client,
                           final @MagicConstant(valuesFromClass = LobbyPlayer.Status.class) int reason) {
    if (client.room == this) {
      if (this.session != null) {
        this.session.removeClient(client);
      }

      client.room = null;
      this.clients.remove(client);

      final Packet packet1 = new Packet(S2CPacket.Type.LOBBY, PacketLengthType.VARIABLE_BYTE, client.channel.alloc().buffer());
      packet1.writeByte(reason == LobbyPlayer.Status.KICKED ? S2CPacket.LobbyAction.YOU_WERE_KICKED : S2CPacket.LobbyAction.YOU_LEFT_ROOM);
      client.channel.writeAndFlush(packet1);

      if (this.clients.isEmpty()) {
        this.lobby.removeRoom(this, ClientLobbyRoom.Status.ALL_PLAYERS_HAVE_LEFT);
      } else if (client == this.owner) {
        this.owner = this.clients.get(0);
        this.lobby.broadcastRoomUpdate(this);
        this.broadcastRoomUpdate();
      }

      final Packet packet3 = new Packet(S2CPacket.Type.LOBBY, PacketLengthType.VARIABLE_BYTE, client.channel.alloc().buffer());
      try {
        packet3.writeByte(S2CPacket.LobbyAction.PLAYER_LEFT_ROOM);
        packet3.writeLong(client.userId);
        packet3.writeByte(reason);
        this.broadcast(packet3);
      } finally {
        packet3.release();
      }
    }
  }

  private void addInvitation(final @NotNull ClientConnection client) {
    this.invitations.add(client);
    client.channel.writeAndFlush(new Packet(S2CPacket.Type.LOBBY, PacketLengthType.VARIABLE_BYTE,
        client.channel.alloc().buffer()
            .writeByte(S2CPacket.LobbyAction.YOU_ARE_INVITED)
            .writeShort(this.id)));
    this.owner.channel.writeAndFlush(new Packet(S2CPacket.Type.LOBBY, PacketLengthType.VARIABLE_BYTE,
        this.owner.channel.alloc().buffer()
            .writeByte(S2CPacket.LobbyAction.ADD_PLAYER_INVITE)
            .writeLong(client.userId)));
  }

  private void removeInvitation(final @NotNull ClientConnection client,
                                @MagicConstant(valuesFromClass = LobbyPlayer.Status.class) final int status) {
    this.invitations.remove(client);
    this.owner.channel.writeAndFlush(new Packet(S2CPacket.Type.LOBBY, PacketLengthType.VARIABLE_BYTE,
        this.owner.channel.alloc().buffer()
            .writeByte(S2CPacket.LobbyAction.REMOVE_PLAYER_INVITE)
            .writeLong(client.userId)
            .writeByte(status)));
  }

  private void addJoinRequest(final @NotNull ClientConnection client) {
    this.joinRequests.add(client);
    client.channel.writeAndFlush(new Packet(S2CPacket.Type.LOBBY, PacketLengthType.VARIABLE_BYTE,
        client.channel.alloc().buffer()
            .writeByte(S2CPacket.LobbyAction.YOU_SENT_JOIN_REQUEST)
            .writeShort(this.id)));
    this.owner.channel.writeAndFlush(new Packet(S2CPacket.Type.LOBBY, PacketLengthType.VARIABLE_BYTE,
        this.owner.channel.alloc().buffer()
            .writeByte(S2CPacket.LobbyAction.ADD_PLAYER_JOIN_REQUEST)
            .writeLong(client.userId)));
  }

  private void removeJoinRequest(final @NotNull ClientConnection client,
                                 @MagicConstant(valuesFromClass = LobbyPlayer.Status.class) final int status) {
    this.joinRequests.remove(client);
    this.owner.channel.writeAndFlush(new Packet(S2CPacket.Type.LOBBY, PacketLengthType.VARIABLE_BYTE,
        this.owner.channel.alloc().buffer()
            .writeByte(S2CPacket.LobbyAction.REMOVE_PLAYER_JOIN_REQUEST)
            .writeLong(client.userId)
            .writeByte(status)));
  }

  private boolean isAllowedToJoin(final @NotNull ClientConnection client) {
    return switch (this.whoCanJoin) {
      case INVITE_ONLY, CLAN, FRIENDS -> false;
      case OPEN -> true;
      case SIMILAR_RATING -> Math.abs(this.owner.rating - client.rating) < 200; // not sure how jagex calculated this
    };
  }

  private static @NotNull Packet createPlayerJoinedPacket(final @NotNull ClientConnection client) {
    final Packet packet2 = new Packet(S2CPacket.Type.LOBBY, PacketLengthType.VARIABLE_BYTE, client.channel.alloc().buffer());
    packet2.writeByte(S2CPacket.LobbyAction.PLAYER_JOINED_ROOM);
    packet2.writeLong(client.userId);
    packet2.writeNullTerminatedString(client.username);
    packet2.writeNullTerminatedString(client.username);
    packet2.writeShort(client.rating);
    packet2.writeVariableInt(client.ratedGameCount << 1);
    packet2.writeByte(client.crown);
    packet2.writeByte(client.unlockedOptionsBitmap);
    return packet2;
  }

  private void broadcastRoomUpdate() {
    this.clients.forEach(client -> {
      final Packet packet = new Packet(S2CPacket.Type.LOBBY, PacketLengthType.VARIABLE_BYTE, client.channel.alloc().buffer());
      packet.writeByte(S2CPacket.LobbyAction.ROOM_INFO);
      this.writeState(packet, client, false, false);
      client.channel.writeAndFlush(packet);
    });
  }

  @SuppressWarnings("SameParameterValue")
  private void writeState(final @NotNull WritableBuffer buffer,
                          final @NotNull ClientConnection client,
                          final boolean includeJoinedPlayerCount,
                          final boolean includeJoinedPlayerNames) {
    if (includeJoinedPlayerCount) {
      buffer.writeByte(this.clients.size());
    }
    buffer.writeByte(this.maxHumanPlayerCount);
    buffer.writeByte(this.whoCanJoin.ordinal());
    final boolean concluded = this.finalElapsedTime >= 0;
    buffer.writeByte(
        (includeJoinedPlayerNames ? 2 : 0)
            | (concluded ? 4 : 0)
            | ((this.session == null || concluded) ? 8 : 0)
            | (this.spectatingAllowed ? 16 : 0)
            | (this.isRated ? 32 : 0)
            | (this.session != null ? 64 : 0)
            | (this.isAllowedToJoin(client) ? 128 : 0)
    );
    buffer.writeByte(this.maxAiPlayerCount);
    buffer.writeByte(this.turnLengthIndex);
    buffer.writeByte(this.gameType.encode());
    buffer.writeByte(this.galaxySize.ordinal());
    buffer.writeByte(this.classicRuleset ? 1 : 0);
    buffer.writeShort(this.getAverageRating());
    buffer.writeInt((int) (System.currentTimeMillis() - this.startedAt));
    if (concluded) {
      buffer.writeInt(this.finalElapsedTime);
    }
    buffer.writeLong(this.owner.userId);
    buffer.writeNullTerminatedString(this.owner.username);
    if (includeJoinedPlayerNames) {
      for (final ClientConnection client1 : this.clients) {
        if (client1 == this.owner) continue;
        buffer.writeNullTerminatedString(client1.username);
      }
    }
  }

  private void sendRoomStatus(final @NotNull ClientConnection client,
                              @MagicConstant(valuesFromClass = ClientLobbyRoom.Status.class) final int status) {
    client.channel.writeAndFlush(new Packet(S2CPacket.Type.LOBBY, PacketLengthType.VARIABLE_BYTE,
        client.channel.alloc().buffer()
            .writeByte(S2CPacket.LobbyAction.ROOM_STATUS)
            .writeShort(this.id)
            .writeByte(status)));
  }

  private int getAverageRating() {
    return this.clients.stream().mapToInt(client -> client.rating).sum() / this.clients.size();
  }

  public Packet createUpdatePacket(final @NotNull ClientConnection client) {
    final Packet packet = new Packet(S2CPacket.Type.LOBBY, PacketLengthType.VARIABLE_BYTE, this.owner.channel.alloc().buffer());
    packet.writeByte(S2CPacket.LobbyAction.ADD_ROOM);
    packet.writeShort(this.id);
    this.writeState(packet, client, true, false);
    return packet;
  }

  public void receiveOptions(final @NotNull Packet packet) {
    this.maxHumanPlayerCount = Math.max(2, Math.min(packet.readUByte(), 6));
    final int b = packet.readUByte();
    this.spectatingAllowed = (b >>> 6) == 2;
    this.whoCanJoin = WhoCanJoin.values()[Math.min(b & 0x3f, WhoCanJoin.values().length - 1)];
    this.maxAiPlayerCount = Math.min(packet.readUByte(), 6);
    this.turnLengthIndex = Math.min(packet.readUByte(), GameSession.TURN_DURATIONS.length - 1);
    this.gameType = GameState.GameType.decode(packet.readUByte());
    this.galaxySize = GalaxySize.values()[Math.min(packet.readUByte(), GalaxySize.values().length - 1)];
    this.classicRuleset = packet.readUByte() != 0;

    this.lobby.broadcastRoomUpdate(this);
    this.broadcastRoomUpdate();
  }

  public void broadcastChatMessage(final @NotNull ClientConnection sender, final @NotNull String message) {
    final Packet packet = S2CPacket.roomChatMessage(sender.channel.alloc(), this.id, this.owner.username, sender.userId, sender.username, sender.username, message);
    try {
      this.broadcast(packet);
    } finally {
      packet.release();
    }
  }

  public void startGameSession() {
    assert this.session == null;
    this.session = new NetworkedGameSession(
        this.owner.channel.alloc(),
        this.owner.channel.eventLoop(),
        this,
        this.gameType,
        this.galaxySize,
        this.classicRuleset ? GameOptions.CLASSIC_GAME_OPTIONS : GameOptions.STREAMLINED_GAME_OPTIONS,
        this.turnLengthIndex,
        this.clients,
        Math.max(this.clients.size() == 1 ? 1 : 0, this.maxAiPlayerCount - this.clients.size()));
    this.startedAt = System.currentTimeMillis();
    this.clients.forEach(this.session::sendInitialState);
    this.lobby.broadcastRoomUpdate(this);
    this.session.processTurnStart();
  }

  public void handleGameConcluded() {
    assert this.finalElapsedTime == -1;
    this.finalElapsedTime = (int) (System.currentTimeMillis() - this.startedAt);
    this.lobby.broadcastRoomUpdate(this);
  }

  private enum WhoCanJoin {
    INVITE_ONLY,
    CLAN,
    FRIENDS,
    SIMILAR_RATING,
    OPEN
  }
}
