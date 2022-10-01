package funorb.shatteredplans;

import funorb.client.lobby.ChatMessage;
import funorb.io.HuffmanCoder;
import funorb.io.Packet;
import funorb.io.PacketLengthType;
import io.netty.buffer.ByteBufAllocator;
import org.jetbrains.annotations.NotNull;

import static funorb.io.PacketLengthType.VARIABLE_BYTE_I;
import static funorb.io.PacketLengthType.VARIABLE_SHORT_I;
import static funorb.shatteredplans.S2CPacket.Type.*;

public final class S2CPacket {
  public static final int[] LENGTH = new int[256];
  static {
    LENGTH[0x01]                      = 16;
    LENGTH[0x02]                      = VARIABLE_SHORT_I;
    LENGTH[ACHIEVEMENTS]              = VARIABLE_BYTE_I;
    LENGTH[0x04]                      = VARIABLE_BYTE_I;
    LENGTH[PROFILE]                   = VARIABLE_BYTE_I;
    LENGTH[RATINGS]                   = VARIABLE_SHORT_I;
    LENGTH[SESSION_ID]                = VARIABLE_BYTE_I;
    LENGTH[REFLECT]                   = VARIABLE_SHORT_I;
    LENGTH[ERROR]                     = VARIABLE_BYTE_I;
    LENGTH[LOBBY]                     = VARIABLE_BYTE_I;
    LENGTH[CHAT]                      = VARIABLE_BYTE_I;
    LENGTH[QUICK_CHAT]                = VARIABLE_BYTE_I;
    LENGTH[SOCIAL]                    = VARIABLE_BYTE_I;
    LENGTH[DISPLAY_NAME]              = VARIABLE_BYTE_I;
    LENGTH[SHOW_DOCUMENT]             = VARIABLE_BYTE_I;
    LENGTH[DISABLE_CHAT_RESTRICTIONS] = 1;
    LENGTH[ENTER_GAME]                = VARIABLE_SHORT_I;
    LENGTH[VICTORY]                   = 1;
    LENGTH[DRAW_OFFERS]               = 1;
    LENGTH[RESIGNATIONS]              = 1;
    LENGTH[REMATCH_OFFERS]            = 1;
    LENGTH[PLAYERS_LEFT]              = 1;
    LENGTH[ADVANCE_TURN]              = 15;
    LENGTH[TURN_ORDERS]               = VARIABLE_SHORT_I;
    LENGTH[DIPLOMATIC_PACTS]          = VARIABLE_BYTE_I;
    LENGTH[PLAYERS_WAITING_ON]        = 1;
    LENGTH[TURN_ORDERS_AND_UPDATE]    = VARIABLE_SHORT_I;
    LENGTH[0x47]                      = 4;
    LENGTH[0x48]                      = 0;
    LENGTH[AI_CHAT]                   = 3;
    LENGTH[0x4a]                      = VARIABLE_SHORT_I;
  }

  public static final boolean[] MGS_ENABLED = new boolean[64];
  static {
    MGS_ENABLED[KEEPALIVE]                 = true;
    MGS_ENABLED[ACHIEVEMENTS]              = true;
    MGS_ENABLED[PROFILE]                   = true;
    MGS_ENABLED[RATINGS]                   = true;
    MGS_ENABLED[SESSION_ID]                = true;
    MGS_ENABLED[REFLECT]                   = true;
    MGS_ENABLED[CHAT]                      = true;
    MGS_ENABLED[QUICK_CHAT]                = true;
    MGS_ENABLED[SOCIAL]                    = true;
    MGS_ENABLED[DISPLAY_NAME]              = true;
    MGS_ENABLED[SHOW_DOCUMENT]             = true;
    MGS_ENABLED[DISABLE_CHAT_RESTRICTIONS] = true;
  }

  public static Packet lobbyChatMessage(final @NotNull ByteBufAllocator alloc,
                                        final long senderId,
                                        final @NotNull String senderName,
                                        final @NotNull String senderDisplayName,
                                        final @NotNull String message) {
    final Packet packet = new Packet(CHAT, PacketLengthType.VARIABLE_BYTE, alloc.buffer());
    try {
      packet.writeByte(ChatMessage.Channel.LOBBY.encode());
      packet.writeByte(0); // some sort of image prefix?
      packet.writeLong(senderId);

      final boolean separateDisplayName = !senderName.equals(senderDisplayName);
      packet.writeByte(separateDisplayName ? 1 : 0);
      packet.writeNullTerminatedString(senderDisplayName);
      if (separateDisplayName) {
        packet.writeNullTerminatedString(senderName);
      }

      HuffmanCoder.instance.writeEncoded(packet, message);
      return packet.retain();
    } finally {
      packet.release();
    }
  }

  public static Packet roomChatMessage(final @NotNull ByteBufAllocator alloc,
                                       final int roomId,
                                       final @NotNull String gameOwnerDisplayName,
                                       final long senderId,
                                       final @NotNull String senderName,
                                       final @NotNull String senderDisplayName,
                                       final @NotNull String message) {
    final Packet packet = new Packet(CHAT, PacketLengthType.VARIABLE_BYTE, alloc.buffer());
    try {
      packet.writeByte(ChatMessage.Channel.ROOM.encode());
      packet.writeByte(0); // some sort of image prefix?
      packet.writeLong(senderId);

      final boolean separateDisplayName = !senderName.equals(senderDisplayName);
      packet.writeByte(separateDisplayName ? 1 : 0);
      packet.writeNullTerminatedString(senderDisplayName);
      if (separateDisplayName) {
        packet.writeNullTerminatedString(senderName);
      }

      packet.writeShort(roomId);
      packet.writeNullTerminatedString(gameOwnerDisplayName);

      HuffmanCoder.instance.writeEncoded(packet, message);
      return packet.retain();
    } finally {
      packet.release();
    }
  }

  public static Packet privateChatMessage(final @NotNull ByteBufAllocator alloc,
                                          final long senderId,
                                          final @NotNull String senderName,
                                          final @NotNull String senderDisplayName,
                                          final @NotNull String message) {
    final Packet packet = new Packet(CHAT, PacketLengthType.VARIABLE_BYTE, alloc.buffer());
    try {
      packet.writeByte(ChatMessage.Channel.PRIVATE.encode());
      packet.writeByte(0); // some sort of image prefix?
      packet.writeLong(senderId);

      packet.writeShort(0); // not sure
      packet.writeU24(0); // not sure

      final boolean separateDisplayName = !senderName.equals(senderDisplayName);
      packet.writeByte(separateDisplayName ? 1 : 0);
      packet.writeNullTerminatedString(senderDisplayName);
      if (separateDisplayName) {
        packet.writeNullTerminatedString(senderName);
      }

      HuffmanCoder.instance.writeEncoded(packet, message);
      return packet.retain();
    } finally {
      packet.release();
    }
  }

  public static final class Type {
    public static final int KEEPALIVE = 0x00;
    public static final int XTEA = 0x01;
    public static final int HISCORE = 0x02;
    public static final int ACHIEVEMENTS = 0x03;
    public static final int LEVEL_PROGRESS = 0x04;
    public static final int PROFILE = 0x05;
    public static final int RATINGS = 0x06;
    public static final int SESSION_ID = 0x07;
    public static final int REFLECT = 0x08;
    public static final int ERROR = 0x09;
    public static final int LOBBY = 0x0a;
    public static final int CHAT = 0x0b;
    public static final int QUICK_CHAT = 0x0c;
    public static final int SOCIAL = 0x0d;
    public static final int ENTER_MP = 0x0e;
    public static final int LEAVE_MP = 0x0f;
    public static final int DISPLAY_NAME = 0x10;
    public static final int SHOW_DOCUMENT = 0x11;
    public static final int DISABLE_CHAT_RESTRICTIONS = 0x12;
    public static final int ENTER_GAME = 0x3a;
    public static final int LEAVE_GAME = 0x3c;
    public static final int VICTORY = 0x3d;
    public static final int DRAW_OFFERS = 0x3e;
    public static final int RESIGNATIONS = 0x3f;
    public static final int REMATCH_OFFERS = 0x40;
    public static final int PLAYERS_LEFT = 0x41;
    public static final int ADVANCE_TURN = 0x42;
    public static final int TURN_ORDERS = 0x43;
    public static final int DIPLOMATIC_PACTS = 0x44;
    public static final int PLAYERS_WAITING_ON = 0x45;
    public static final int TURN_ORDERS_AND_UPDATE = 0x46;
    public static final int ACHIEVEMENTS_UNLOCKED = 0x47;
    public static final int RESEND_ALL_TURN_ORDERS = 0x48;
    public static final int AI_CHAT = 0x49;
  }

  public static final class SocialAction {
    public static final int IGNORE = 0;
    public static final int FRIEND = 1;
    public static final int LOADED = 2;
    public static final int LOADING = 3;
    public static final int INITIALIZE = 4;
  }

  public static final class LobbyAction {
    public static final int YOU_LEFT_ROOM = 0;
    public static final int YOU_WERE_KICKED = 1;
    public static final int YOU_CREATED_RANKED_ROOM = 2;
    public static final int YOU_JOINED_RANKED_ROOM = 3;
    public static final int YOU_JOINED_ROOM = 4;
    public static final int PLAYER_ENTERED_LOBBY = 5;
    public static final int PLAYER_LEFT_LOBBY = 6;
    public static final int REMOVE_ALL_JOIN_REQUESTS = 7;
    public static final int ADD_ROOM = 8;
    public static final int REMOVE_ROOM = 9;
    public static final int REMOVE_ALL_ROOMS = 10;
    public static final int YOU_ARE_INVITED = 11;
    public static final int YOU_SENT_JOIN_REQUEST = 12;
    public static final int ROOM_STATUS = 13;
    public static final int ADD_PLAYER_INVITE = 14;
    public static final int REMOVE_PLAYER_INVITE = 15;
    public static final int ADD_PLAYER_JOIN_REQUEST = 16;
    public static final int REMOVE_PLAYER_JOIN_REQUEST = 17;
    public static final int PLAYER_JOINED_ROOM = 18;
    public static final int PLAYER_LEFT_ROOM = 19;
    public static final int ROOM_INFO = 20;
    public static final int GAME_OPTIONS_CHANGED = 21;
    public static final int RATING = 22;
    public static final int PLAYER_ID = 23;
  }
}
