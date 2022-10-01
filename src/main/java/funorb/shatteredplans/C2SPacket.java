package funorb.shatteredplans;

import funorb.client.RankingsRequest;
import funorb.client.SetProfileRequest;
import funorb.client.lobby.ChatMessage;
import funorb.io.CipheredBuffer;
import funorb.io.HuffmanCoder;
import funorb.io.PacketLengthType;
import funorb.shatteredplans.client.JagexApplet;
import funorb.shatteredplans.client.ShatteredPlansClient;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Range;

import java.util.Optional;

public final class C2SPacket {
  public static final CipheredBuffer buffer = new CipheredBuffer();

  public static void goodbye() {
    Type.DISCONNECT.write(buffer);
  }

  public static void a093bo() {
    Type.ACHIEVEMENTS.write(buffer);
    buffer.writeByte(1);
    buffer.writeByte(2);
  }

  public static void sendProfileGet() {
    Type.PROFILE.write(buffer);
    buffer.writeBytes(1, ProfileAction.GET);
  }

  public static void sendProfileSet(final SetProfileRequest var1) {
    Type.PROFILE.write(buffer);
    buffer.withLengthByte(() -> {
      final int startPos = buffer.pos;
      buffer.writeByte(ProfileAction.SET);
      if (var1.data == null) {
        buffer.writeByte(0);
      } else {
        buffer.writeByte(var1.data.length);
        buffer.writeBytes(var1.data);
      }

      buffer.writeDigest(startPos);
      buffer.pos -= 4;
      var1.digest = buffer.readInt();
    });
  }

  public static void a970uf(final RankingsRequest var0) {
    Type.RANKING.write(buffer);
    buffer.writeByte(var0._k);
    buffer.writeByte(var0._j);
  }

  public static void enterLobby() {
    Type.ENTER_MP.write(buffer);
  }

  public static void leaveLobby() {
    Type.LEAVE_MP.write(buffer);
  }

  private static void sendLobbyAction(final int type, final Runnable writePayload) {
    Type.LOBBY.write(buffer);
    buffer.withLengthByte(() -> {
      buffer.writeByte(type);
      writePayload.run();
    });
  }

  public static void playRatedGame() {
    sendLobbyAction(LobbyAction.PLAY_RATED_GAME, () -> {});
  }

  public static void returnToLobby() {
    sendLobbyAction(LobbyAction.RETURN_TO_LOBBY, () -> {});
  }

  public static void updateRatedGamePreferences(final byte[] a, final int b, final int c, final byte[] d) {
    sendLobbyAction(LobbyAction.SET_RATED_OPTIONS, () -> {
      buffer.writeBytes(a);
      buffer.writeByte(b);
      buffer.writeByte(c);
      buffer.writeBytes(d);
    });
  }

  public static void acknowledgeRatedRoomInfo() {
    sendLobbyAction(LobbyAction.ACK_RATED_ROOM_INFO, () -> {});
  }

  public static void createUnratedGame() {
    sendLobbyAction(LobbyAction.CREATE_UNRATED_GAME, () -> buffer.writeBytes(6, 128, 4, 2, 0, 2, 0));
  }

  public static void updateRoomOptions() {
    sendLobbyAction(LobbyAction.SET_ROOM_OPTIONS, () -> {
      buffer.writeByte(ShatteredPlansClient.unratedLobbyRoom.maxPlayerCount);
      buffer.writeByte((ShatteredPlansClient.unratedLobbyRoom.allowSpectate << 6) + ShatteredPlansClient.unratedLobbyRoom.whoCanJoin);
      buffer.writeBytes(ShatteredPlansClient.unratedLobbyRoom.gameSpecificOptions);
    });
  }

  public static void inviteToGame(final long player) {
    sendLobbyAction(LobbyAction.INVITE_PLAYER_TO_GAME, () -> buffer.writeLong(player));
  }

  public static void kickFromGame(final long player) {
    sendLobbyAction(LobbyAction.KICK_PLAYER_FROM_GAME, () -> buffer.writeLong(player));
  }

  public static void requestToJoinRoom(final int game) {
    sendLobbyAction(LobbyAction.JOIN_ROOM, () -> buffer.writeShort(game));
  }

  public static void requestToLeaveRoom(final int game) {
    sendLobbyAction(LobbyAction.LEAVE_ROOM, () -> buffer.writeShort(game));
  }

  public static void spectateGame(final int game) {
    sendLobbyAction(LobbyAction.SPECTATE_GAME, () -> buffer.writeShort(game));
  }

  public static void showPlayersInGame(final int game) {
    sendLobbyAction(LobbyAction.SHOW_PLAYERS_IN_GAME, () -> buffer.writeShort(game));
  }

  public static void friendIgnore(final int what, final String who) {
    Type.SOCIAL.write(buffer);
    buffer.withLengthByte(() -> {
      buffer.writeByte(what);
      buffer.writeNullTerminatedString(who);
    });
  }

  public static void sendChatMessage(final @NotNull ChatMessage.Channel channel, final String recipientName, final @NotNull String message) {
    Type.CHAT.write(buffer);
    buffer.withLengthByte(() -> {
      buffer.writeByte(channel.encode());
      if (channel == ChatMessage.Channel.PRIVATE) {
        buffer.writeNullTerminatedString(recipientName);
      }
      HuffmanCoder.instance.writeEncoded(buffer, message);
    });
  }

  public static void sendQuickChatMessage(final @NotNull ChatMessage.Channel channel, final String recipientName, final int messageId) {
    Type.QUICK_CHAT.write(buffer);
    buffer.withLengthByte(() -> {
      buffer.writeByte(channel.encode());
      if (channel == ChatMessage.Channel.PRIVATE) {
        buffer.writeNullTerminatedString(recipientName);
      }
      buffer.writeShort(messageId);
    });
  }

  public static void setChatFilters(final int filters) {
    Type.SOCIAL.write(buffer);
    buffer.withLengthByte(() -> {
      buffer.writeByte(4);
      buffer.writeByte(filters);
    });
  }

  public static void reportAbuse(final long var0, final boolean var3, final String var4, final int var5) {
    Type.REPORT.write(buffer);
    buffer.withLengthByte(() -> {
      buffer.writeLong(var0);
      buffer.writeNullTerminatedString(var4);
      buffer.writeByte(var5);
      buffer.writeByte(var3 ? 1 : 0);
    });
  }

  public static void sendViewMessages() {
    Type.VIEW_MESSAGES.write(buffer);
  }

  public static void a115vf2() {
    buffer.writeCipheredByte(18);
    buffer.withLengthByte(() -> buffer.writeBytes(JagexApplet.loginPacket.data, JagexApplet.loginPacket.pos));
  }

  public static void send58() {
    Type.START_GAME.write(buffer);
  }

  public enum Type {
    KEEPALIVE       (0x00, 0),
    DISCONNECT      (0x01, 0),
    LEVEL_KEY       (0x02, 3),
    HIGHSCORE       (0x03, PacketLengthType.VARIABLE_BYTE_I),
    ACHIEVEMENTS    (0x04, PacketLengthType.VARIABLE_BYTE_I),
    PROGRESS        (0x05, PacketLengthType.VARIABLE_BYTE_I),
    PROFILE         (0x06, PacketLengthType.VARIABLE_BYTE_I),
    RANKING         (0x07, 2),
    REFLECT         (0x08, PacketLengthType.VARIABLE_BYTE_I),
    ENTER_MP        (0x09, 0),
    LEAVE_MP        (0x0a, 0),
    LOBBY           (0x0b, PacketLengthType.VARIABLE_BYTE_I),
    CHAT            (0x0c, PacketLengthType.VARIABLE_BYTE_I),
    SOCIAL          (0x0d, PacketLengthType.VARIABLE_BYTE_I),
    REPORT          (0x0e, PacketLengthType.VARIABLE_BYTE_I),
    QUICK_CHAT      (0x0f, PacketLengthType.VARIABLE_BYTE_I),
    COMPLETE        (0x10, PacketLengthType.VARIABLE_BYTE_I),
    VIEW_MESSAGES   (0x11, 0),
    BIRTH           (0x12, PacketLengthType.VARIABLE_BYTE_I),
    START_GAME      (0x3a, 0),
    ORDERS          (0x3b, PacketLengthType.VARIABLE_SHORT_I),
    ALL_TURN_ORDERS (0x3c, PacketLengthType.VARIABLE_SHORT_I),
    END_TURN        (0x3d, 5),
    CANCEL_END_TURN (0x3e, 1),
    OFFER_DRAW      (0x3f, 0),
    RESIGN          (0x40, 0),
    OFFER_REMATCH   (0x41, 0),
    DESYNC          (0x42, PacketLengthType.VARIABLE_SHORT_I);

    private static final Type[] BY_ID = new Type[256];
    static {
      for (final Type type : values()) {
        assert BY_ID[type.id] == null;
        BY_ID[type.id] = type;
      }
    }

    public static Optional<Type> lookup(final @Range(from=0, to=0xff) int id) {
      return Optional.ofNullable(BY_ID[id]);
    }

    public final byte id;
    public final int length;
    public final PacketLengthType lengthType;

    Type(final int id, final int length) {
      this.id = (byte) id;
      this.length = length;
      this.lengthType = switch (length) {
        case PacketLengthType.VARIABLE_BYTE_I -> PacketLengthType.VARIABLE_BYTE;
        case PacketLengthType.VARIABLE_SHORT_I -> PacketLengthType.VARIABLE_SHORT;
        default                                       -> PacketLengthType.FIXED;
      };
    }

    public void write(final CipheredBuffer buffer) {
      buffer.writeCipheredByte((int) this.id & 0xff);
    }
  }

  @SuppressWarnings("WeakerAccess")
  public static final class ProfileAction {
    public static final int GET = 0;
    public static final int SET = 1;
  }

  @SuppressWarnings("WeakerAccess")
  public static final class LobbyAction {
    public static final int PLAY_RATED_GAME = 0;
    public static final int RETURN_TO_LOBBY = 1;
    public static final int SET_RATED_OPTIONS = 2;
    public static final int ACK_RATED_ROOM_INFO = 3;
    public static final int CREATE_UNRATED_GAME = 4;
    public static final int SET_ROOM_OPTIONS = 5;
    public static final int INVITE_PLAYER_TO_GAME = 6;
    public static final int KICK_PLAYER_FROM_GAME = 7;
    public static final int JOIN_ROOM = 8;
    public static final int LEAVE_ROOM = 9;
    public static final int SPECTATE_GAME = 10;
    public static final int SHOW_PLAYERS_IN_GAME = 11;
  }

  public static final class OrderType {
    public static final int BUILD = 192;
    public static final int PROJECT = 251;
    public static final int PACT = 255;
  }
}
