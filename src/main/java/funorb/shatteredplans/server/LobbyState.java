package funorb.shatteredplans.server;

import funorb.io.Packet;
import funorb.io.PacketLengthType;
import funorb.client.lobby.ClientLobbyRoom;
import funorb.client.lobby.LobbyPlayer;
import funorb.shatteredplans.S2CPacket;
import funorb.client.UserIdLoginCredentials;
import org.intellij.lang.annotations.MagicConstant;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public final class LobbyState {
  private final Map<Long, ClientConnection> clients = new HashMap<>();

  public static final int NULL_ROOM_ID = 0;
  private int nextRoomId = 1;
  private final Map<Integer, LobbyRoom> rooms = new HashMap<>();

  private void broadcast(final @NotNull Packet packet) {
    this.clients.values().forEach(client1 -> client1.channel.writeAndFlush(packet.retainedSlice()));
  }

  private void broadcastAndRelease(final @NotNull Packet packet) {
    try {
      this.broadcast(packet);
    } finally {
      packet.release();
    }
  }

  public Optional<ClientConnection> getClientById(final long userId) {
    return Optional.ofNullable(this.clients.get(userId));
  }

  public Optional<ClientConnection> getClientByName(final @NotNull String username) {
    return Optional.ofNullable(this.clients.get(UserIdLoginCredentials.encodeUsername(username)));
  }

  public void addClient(final @NotNull ClientConnection client) {
    this.clients.values().forEach(client1 -> client.channel.write(createJoinPacket(client1)));
    this.clients.put(client.userId, client);
    this.broadcastAndRelease(createJoinPacket(client));
    this.rooms.values().forEach(room -> client.channel.writeAndFlush(room.createUpdatePacket(client)));
  }

  private static Packet createJoinPacket(final @NotNull ClientConnection client) {
    final Packet packet = new Packet(S2CPacket.Type.LOBBY, PacketLengthType.VARIABLE_BYTE, client.channel.alloc().buffer());
    packet.writeByte(S2CPacket.LobbyAction.PLAYER_ENTERED_LOBBY);
    packet.writeLong(client.userId);
    packet.writeNullTerminatedString(client.username);
    packet.writeNullTerminatedString(""); // previous display name
    packet.writeNullTerminatedString(client.username);
    packet.writeInt((int) (System.currentTimeMillis() - client.connectedAt));
    packet.writeShort(client.rating);
    packet.writeVariableInt(client.ratedGameCount << 1);
    packet.writeByte(client.crown);
    packet.writeByte(client.unlockedOptionsBitmap);
    return packet;
  }

  public void removeClient(final @NotNull ClientConnection client,
                           @MagicConstant(valuesFromClass = LobbyPlayer.Status.class) final int reason) {
    if (client.room != null) {
      client.room.removeClient(client, reason);
    }
    if (this.clients.remove(client.userId) != null) {
      final Packet packet = new Packet(S2CPacket.Type.LOBBY, PacketLengthType.VARIABLE_BYTE, client.channel.alloc().buffer());
      try {
        packet.writeByte(S2CPacket.LobbyAction.PLAYER_LEFT_LOBBY);
        packet.writeLong(client.userId);
        packet.writeByte(reason);
        this.broadcast(packet);
      } finally {
        packet.release();
      }
    }
  }

  public void broadcastChatMessage(final @NotNull ClientConnection sender, final @NotNull String message) {
    this.broadcastAndRelease(S2CPacket.lobbyChatMessage(sender.channel.alloc(), sender.userId, sender.username, sender.username, message));
  }

  @SuppressWarnings("UnusedReturnValue")
  public LobbyRoom createRoom(final @NotNull ClientConnection owner) {
    final LobbyRoom room = new LobbyRoom(this, this.nextRoomId++, owner);
    this.rooms.put(room.id, room);
    this.broadcastRoomUpdate(room);
    return room;
  }

  public void broadcastRoomUpdate(final @NotNull LobbyRoom room) {
    this.clients.values().forEach(client -> client.channel.writeAndFlush(room.createUpdatePacket(client)));
  }

  public Optional<LobbyRoom> getRoom(final int roomId) {
    return Optional.ofNullable(this.rooms.get(roomId));
  }

  @SuppressWarnings("SameParameterValue")
  public void removeRoom(final @NotNull LobbyRoom room,
                         @MagicConstant(valuesFromClass = ClientLobbyRoom.Status.class) final int reason) {
    this.rooms.remove(room.id);
    room.destroy();

    final Packet packet = new Packet(S2CPacket.Type.LOBBY, PacketLengthType.VARIABLE_BYTE, room.owner.channel.alloc().buffer());
    try {
      packet.writeByte(S2CPacket.LobbyAction.REMOVE_ROOM);
      packet.writeShort(room.id);
      packet.writeByte(reason);
      this.broadcast(packet);
    } finally {
      packet.release();
    }
  }
}
