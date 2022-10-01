package funorb.shatteredplans.server;

import funorb.client.lobby.ChatMessage;
import funorb.client.lobby.LobbyPlayer;
import funorb.io.HuffmanCoder;
import funorb.io.Packet;
import funorb.io.PacketLengthType;
import funorb.shatteredplans.C2SPacket;
import funorb.shatteredplans.S2CPacket;
import funorb.shatteredplans.client.JagexApplet;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPromise;
import io.netty.handler.timeout.ReadTimeoutException;
import io.netty.util.ReferenceCountUtil;
import launcher.util.HEXUtils;

public final class ClientHandler extends ChannelDuplexHandler {
  private final LobbyState lobbyState;
  private final ClientConnection client;
  private C2SPacket.Type packetType = null;
  private int packetLen = 0;
  private ByteBuf packetPayload;

  public ClientHandler(final LobbyState lobbyState, final ClientConnection client) {
    this.lobbyState = lobbyState;
    this.client = client;
  }

  @Override
  public void handlerRemoved(final ChannelHandlerContext ctx) throws Exception {
    if (this.packetPayload != null) {
      this.packetPayload.release();
      this.packetPayload = null;
    }
    this.lobbyState.removeClient(this.client, LobbyPlayer.Status.LOST_CON);
    super.handlerRemoved(ctx);
  }

  @Override
  public void channelRead(final ChannelHandlerContext ctx, final Object msg) throws Exception {
    boolean release = true;
    try {
      if (msg instanceof ByteBuf buf) {
        this.channelRead0(ctx, buf);
      } else {
        release = false;
        super.channelRead(ctx, msg);
      }
    } finally {
      if (release) {
        ReferenceCountUtil.release(msg);
      }
    }
  }

  @Override
  public void write(final ChannelHandlerContext ctx, final Object msg, final ChannelPromise promise) throws Exception {
    if (msg instanceof Packet packet) {
      final ByteBuf header = ctx.alloc().buffer(1 + packet.lengthType.length);
      if (packet.typeIsCiphered) {
        header.writeByte(packet.type + this.client.s2cCipher.nextInt());
      } else {
        header.writeByte(packet.type);
      }
      switch (packet.lengthType) {
        case FIXED -> {}
        case VARIABLE_BYTE -> header.writeByte(packet.payload.readableBytes());
        case VARIABLE_SHORT -> header.writeShort(packet.payload.readableBytes());
      }
      ctx.write(header);
      ctx.write(packet.payload);
    } else {
      super.write(ctx, msg, promise);
    }
  }

  @Override
  public void exceptionCaught(final ChannelHandlerContext ctx, final Throwable cause) throws Exception {
    if (cause instanceof ReadTimeoutException) {
      JagexApplet.printDebug(this.client.getLogPrefix() + "TIMEOUT");
      ctx.close();
    } else {
      super.exceptionCaught(ctx, cause);
    }
  }

  private void channelRead0(final ChannelHandlerContext ctx, final ByteBuf msg) {
    while (msg.readableBytes() > 0) {
      if (this.packetType == null) {
        final int type = (msg.readUnsignedByte() - this.client.c2sCipher.nextInt()) & 0xff;
        this.packetType = C2SPacket.Type.lookup(type)
            .orElseThrow(() -> new IllegalStateException("unknown packet type: " + HEXUtils.toHexString(type, 8)));
        this.packetLen = this.packetType.length;
        this.packetPayload = ctx.alloc().buffer();
      }

      if (this.packetType == C2SPacket.Type.DISCONNECT) {
        JagexApplet.printDebug(this.client.getLogPrefix() + "RECV DISCONNECT");
        ctx.close();
        return;
      }

      final int neededBytes = switch (this.packetLen) {
        case PacketLengthType.VARIABLE_BYTE_I -> 1;
        case PacketLengthType.VARIABLE_SHORT_I -> 2;
        default -> this.packetLen;
      };
      final int newByteCount = Math.min(msg.readableBytes(), neededBytes - this.packetPayload.readableBytes());
      if (newByteCount > 0) {
        msg.readBytes(this.packetPayload, newByteCount);
      }

      if (this.packetPayload.readableBytes() == neededBytes) {
        switch (this.packetLen) {
          case PacketLengthType.VARIABLE_BYTE_I -> {
            this.packetLen = this.packetPayload.readUnsignedByte();
            this.packetPayload.clear();
          }
          case PacketLengthType.VARIABLE_SHORT_I -> {
            this.packetLen = this.packetPayload.readUnsignedShort();
            this.packetPayload.clear();
          }
          default -> {
            this.handlePacket(ctx, new Packet(this.packetType.id, this.packetType.lengthType, this.packetPayload));
            this.packetType = null;
            this.packetPayload.release();
            this.packetPayload = null;
          }
        }
      }
    }
  }

  private void handlePacket(final ChannelHandlerContext ctx, final Packet request) {
    if (this.packetType == C2SPacket.Type.KEEPALIVE) {
      ctx.channel().writeAndFlush(Packet.S2C_KEEPALIVE);
      return;
    }

    JagexApplet.printDebug(this.client.getLogPrefix() + "RECV " + this.packetType +
        (this.packetLen > 0 ? ": " + HEXUtils.toHexString(request.payload, 1) : ""));
    switch (this.packetType) {
      case ACHIEVEMENTS -> {
        final int type = request.readUByte();
        if (type == 2) {
          ctx.channel().writeAndFlush(new Packet(S2CPacket.Type.ACHIEVEMENTS, PacketLengthType.VARIABLE_BYTE,
              ctx.alloc().buffer(1, 1).writeByte(2)));
        } else {
          System.out.println(this.client.getLogPrefix() + "MISS ACHIVEMENTS " + HEXUtils.toHexString(type, 8));
        }
      }
      case PROFILE -> {
        final int type = request.readUByte();
        if (type == 0) {
          final Packet packet = new Packet(S2CPacket.Type.PROFILE, PacketLengthType.VARIABLE_BYTE, ctx.alloc().buffer());
          packet.writeByte(0);
          packet.writeByte(0);
          packet.writeCRC(2);
          ctx.channel().writeAndFlush(packet);
        } else {
          System.out.println(this.client.getLogPrefix() + "MISS PROFILE " + HEXUtils.toHexString(type, 8));
        }
      }
      case ENTER_MP -> {
        ctx.channel().write(Packet.S2C_ENTER_MP);
        ctx.channel().write(new Packet(S2CPacket.Type.LOBBY, PacketLengthType.VARIABLE_BYTE,
            ctx.alloc().buffer(9, 9)
                .writeByte(S2CPacket.LobbyAction.PLAYER_ID)
                .writeLong(this.client.userId)));

        final Packet socialPacket1 = new Packet(S2CPacket.Type.SOCIAL, PacketLengthType.VARIABLE_BYTE, ctx.alloc().buffer());
        socialPacket1.writeByte(S2CPacket.SocialAction.INITIALIZE);
        socialPacket1.writeNullTerminatedString("Shattered Plans");
        socialPacket1.writeByte(0);
        ctx.channel().write(socialPacket1);

        final Packet socialPacket3 = new Packet(S2CPacket.Type.SOCIAL, PacketLengthType.VARIABLE_BYTE, ctx.alloc().buffer());
        socialPacket3.writeByte(S2CPacket.SocialAction.LOADED);
        ctx.channel().write(socialPacket3);

        ctx.channel().flush();
        this.lobbyState.addClient(this.client);
      }
      case LEAVE_MP -> {
        ctx.channel().writeAndFlush(Packet.S2C_LEAVE_MP);
        this.lobbyState.removeClient(this.client, LobbyPlayer.Status.LEFT_LOBBY);
      }
      case LOBBY -> {
        final int type = request.readUByte();
        switch (type) {
          case C2SPacket.LobbyAction.CREATE_UNRATED_GAME -> {
            if (this.client.room == null) {
              this.lobbyState.createRoom(this.client);
            }
          }
          case C2SPacket.LobbyAction.SET_ROOM_OPTIONS -> {
            if (this.client.room != null && this.client.room.owner == this.client) {
              this.client.room.receiveOptions(request);
            }
          }
          case C2SPacket.LobbyAction.INVITE_PLAYER_TO_GAME -> {
            final long userId = request.readLong();
            if (this.client.room != null && this.client.room.owner == this.client) {
              this.lobbyState.getClientById(userId).ifPresent(this.client.room::handleInviteRequest);
            }
          }
          case C2SPacket.LobbyAction.KICK_PLAYER_FROM_GAME -> {
            final long userId = request.readLong();
            if (this.client.room != null && this.client.room.owner == this.client) {
              this.lobbyState.getClientById(userId).ifPresent(this.client.room::handleKickRequest);
            }
          }
          case C2SPacket.LobbyAction.JOIN_ROOM -> {
            final int roomId = request.readUShort();
            if (this.client.room == null) {
              this.lobbyState.getRoom(roomId).ifPresent(room -> room.handleJoinRequest(this.client));
            }
          }
          case C2SPacket.LobbyAction.LEAVE_ROOM -> {
            final int roomId = request.readUShort();
            this.lobbyState.getRoom(roomId).ifPresent(room -> room.handleLeaveRequest(this.client));
          }
          case C2SPacket.LobbyAction.SPECTATE_GAME -> {
            final int roomId = request.readUShort();
            if (roomId == LobbyState.NULL_ROOM_ID) {
              if (this.client.spectateSession != null) {
                this.client.spectateSession.removeClient(this.client);
                this.client.spectateSession = null;
              }
            } else {
              this.lobbyState.getRoom(roomId).ifPresent(room -> {
                if (room.isSpectatingAllowed() && room.session != null) {
                  this.client.spectateSession = room.session;
                  room.session.addSpectator(this.client);
                }
              });
            }
          }
          default -> System.out.println(this.client.getLogPrefix() + "MISS LOBBY " + HEXUtils.toHexString(type, 8));
        }
      }
      case CHAT -> {
        final ChatMessage.Channel channel = ChatMessage.Channel.decode(request.readUByte());
        final String recipientName;
        if (channel == ChatMessage.Channel.PRIVATE) {
          recipientName = request.readNullTerminatedString();
        } else {
          recipientName = null;
        }
        final String message = HuffmanCoder.instance.readEncoded(request, request.readableBytes());
        switch (channel) {
          case LOBBY -> this.lobbyState.broadcastChatMessage(this.client, message);
          case ROOM -> {
            if (this.client.room != null) {
              this.client.room.broadcastChatMessage(this.client, message);
            }
          }
          case PRIVATE ->
              this.lobbyState.getClientByName(recipientName).ifPresent(recipient ->
                  recipient.sendPrivateMessage(this.client, message));
          default -> System.out.println(this.client.getLogPrefix() + "MISS CHAT " + HEXUtils.toHexString(channel.encode(), 8));
        }
      }
      case START_GAME -> {
        if (this.client.room != null
            && this.client.room.owner == this.client
            && this.client.room.session == null) {
          this.client.room.startGameSession();
        }
      }
      case ORDERS -> this.client.getPlaySession().ifPresent(session -> session.receiveOrders(this.client, request, this.packetLen));
      case END_TURN -> this.client.getPlaySession().ifPresent(session -> session.receiveEndTurn(this.client, request));
      case CANCEL_END_TURN -> this.client.getPlaySession().ifPresent(session -> session.receiveCancelEndTurn(this.client, request));
      case RESIGN -> this.client.getPlaySession().ifPresent(session -> session.handlePlayerResignation(this.client));
      default -> System.out.println(this.client.getLogPrefix() + "MISS " + this.packetType);
    }
  }
}
