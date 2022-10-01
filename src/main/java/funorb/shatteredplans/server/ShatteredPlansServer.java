package funorb.shatteredplans.server;

import funorb.Strings;
import funorb.cache.LocalPageSource;
import funorb.io.Buffer;
import funorb.io.Packet;
import funorb.shatteredplans.client.AuthMode;
import funorb.shatteredplans.client.JagexApplet;
import funorb.client.UserIdLoginCredentials;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoop;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.ByteToMessageDecoder;
import io.netty.handler.stream.ChunkedWriteHandler;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.handler.timeout.IdleStateHandler;
import io.netty.handler.timeout.ReadTimeoutHandler;
import launcher.util.HEXUtils;

import java.io.InputStream;
import java.math.BigInteger;
import java.net.InetSocketAddress;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

public final class ShatteredPlansServer {
  public static final LocalPageSource PAGE_SOURCE = new LocalPageSource(ShatteredPlansServer::getResource);

  private final EventLoopGroup eventLoopGroup;
  private final ServerBootstrap bootstrap;

  private final LobbyState lobbyState = new LobbyState();

  public ShatteredPlansServer() {
    this.eventLoopGroup = new NioEventLoopGroup();
    final EventLoop eventLoop = this.eventLoopGroup.next();

    this.bootstrap = new ServerBootstrap();
    this.bootstrap.group(eventLoop)
        .channel(NioServerSocketChannel.class)
        .childHandler(new ChannelInitializer<SocketChannel>() {
          @Override
          protected void initChannel(final SocketChannel channel) {
            if (!JagexApplet.DEBUG_MODE) {
              channel.pipeline().addLast(new ReadTimeoutHandler(JagexApplet.SERVER_TIMEOUT_MILLIS, TimeUnit.MILLISECONDS));
            }
            channel.pipeline()
                .addLast(new IdleStateHandler(0, 10, 0))
                .addLast(new ChunkedWriteHandler())
                .addLast(new HandshakeHandler())
                .addLast(KeepAliveHandler.INSTANCE)
                .addLast(new UncaughtExceptionHandler());
          }
        })
        .option(ChannelOption.SO_BACKLOG, 128)
        .childOption(ChannelOption.SO_KEEPALIVE, true);
  }

  public static InputStream getResource(final String name) {
    return ShatteredPlansServer.class.getResourceAsStream(name);
  }

  private static InputStream getResource(final int pageId, final int part) {
    if (pageId > 0xFF || part > 0xFFFF) {
      throw new RuntimeException("resource " + pageId + " / " + part + " not found");
    }
    final String path = "res/res_" + HEXUtils.toHexString(pageId, 8) +
        "_" + HEXUtils.toHexString(part, 16) + ".dat";
    final InputStream resource = getResource(path);
    if (resource == null) {
      throw new RuntimeException("resource " + pageId + " / " + part + " not found");
    }
    return resource;
  }

  public InetSocketAddress bind(final int port) throws InterruptedException {
    final Channel channel = this.bootstrap.bind(port).sync().channel();
    return (InetSocketAddress) channel.localAddress();
  }

  public void shutdown() throws InterruptedException {
    System.out.println("Shutting down server...");
    this.eventLoopGroup.shutdownGracefully().sync();
    System.out.println("Server shutdown complete.");
  }

  @ChannelHandler.Sharable
  private static final class KeepAliveHandler extends ChannelInboundHandlerAdapter {
    private static final KeepAliveHandler INSTANCE = new KeepAliveHandler();

    @Override
    public void userEventTriggered(final ChannelHandlerContext ctx, final Object evt) throws Exception {
      if (evt instanceof IdleStateEvent) {
        ctx.channel().writeAndFlush(Packet.S2C_KEEPALIVE);
      }
      super.userEventTriggered(ctx, evt);
    }
  }

  private static final class UncaughtExceptionHandler extends ChannelDuplexHandler {
    @Override
    public void exceptionCaught(final ChannelHandlerContext ctx, final Throwable cause) {
      cause.printStackTrace();
      ctx.close();
    }
  }

  private final class HandshakeHandler extends ByteToMessageDecoder {
    private static final int FRAME_LENGTH = 8;

    @Override
    protected void decode(final ChannelHandlerContext ctx, final ByteBuf in, final List<Object> out) {
      JagexApplet.printDebug("[" + ctx.channel().remoteAddress() + "] HANDSHAKE");
      if (in.readableBytes() < FRAME_LENGTH) {
        return;
      }

      final int type = in.readUnsignedByte();
      final int proto = in.readUnsignedShort();
      final int game = in.readUnsignedShort();
      in.readUnsignedShort(); // server id
      in.readUnsignedByte(); // lang id

      if (type != 0x0c || proto != 0x11 || game != 0x19) {
        ctx.close();
        return;
      }

      ctx.pipeline().replace(this, null, new UpdateAndLoginHandler());
    }
  }

  private final class UpdateAndLoginHandler extends ByteToMessageDecoder {
    private static final int CRC_OK = 0;

    private static final int CRC = 0x02242922;

    private static final int T_LOGIN = 0x0e;
    private static final int T_UPDATE = 0x0f;

    private int packetType = -1;

    @Override
    protected void decode(final ChannelHandlerContext ctx, final ByteBuf in, final List<Object> out) {
      JagexApplet.printDebug("[" + ctx.channel().remoteAddress() + "] DECODE");
      if (this.packetType == -1) {
        if (in.readableBytes() < 1) return;
        this.packetType = in.readUnsignedByte();
      }

      if (this.packetType == T_LOGIN) {
        if (in.readableBytes() < 1) return;
        final int authMode = in.readUnsignedByte();
        // send the server part of the ISAAC cipher IV
        ctx.writeAndFlush(ctx.alloc().buffer(5)
            .writeByte(0).writeLong(new Random().nextLong()));
        ctx.pipeline().replace(this, null, new LoginHandler(authMode));
      } else if (this.packetType == T_UPDATE) {
        if (in.readableBytes() < 4) return;
        readCRC(in);
        ctx.writeAndFlush(ctx.alloc().buffer(1).writeByte(CRC_OK));
        ctx.pipeline().replace(this, null, new UpdateHandler());
      } else {
        throw new RuntimeException("unknown packet type: " + HEXUtils.toHexString(this.packetType, 8));
      }
    }

    @SuppressWarnings("WeakerAccess")
    public static void readCRC(final ByteBuf in) {
      final int crc = in.readInt();
      if (crc != CRC) {
        throw new RuntimeException("wrong CRC: " + crc);
      }
    }
  }

  private final class UpdateHandler extends ByteToMessageDecoder {
    @Override
    protected void decode(final ChannelHandlerContext ctx, final ByteBuf in, final List<Object> out) {
      if (in.readableBytes() < 1) return;
      if (in.getUnsignedByte(0) == 0x0e) {
        in.readUnsignedByte();
        ctx.pipeline().replace(this, null, new UpdateAndLoginHandler());
      }
      if (in.readableBytes() < 6) return;
      final int type = in.readUnsignedByte();
      if (type == 0x00 || type == 0x01) {
        final boolean priority = type == 1;
        final int group = in.readUnsignedByte();
        final int index = in.readInt();

        final InputStream resource = getResource(group, index);
        ctx.writeAndFlush(new ResourceStreamInput(priority, group, index, resource));
      } else if (type == 0x03 || type == 0x06) {
        // not sure what these are for
        in.skipBytes(5);
      } else {
        throw new RuntimeException("unknown packet type: " + HEXUtils.toHexString(type, 8));
      }
    }
  }

  private final class LoginHandler extends ByteToMessageDecoder {
    private final int authMode;
    private int packetType = -1;
    private int payloadLength = -1;

    @SuppressWarnings("WeakerAccess")
    public LoginHandler(final int authMode) {
      this.authMode = authMode;
    }

    @Override
    protected void decode(final ChannelHandlerContext ctx, final ByteBuf in, final List<Object> out) {
      if (this.packetType == -1) {
        if (in.readableBytes() < 1) return;
        this.packetType = in.readUnsignedByte();
      }
      if (this.payloadLength == -1) {
        if (in.readableBytes() < 2) return;
        this.payloadLength = in.readUnsignedShort();
      }
      if (in.readableBytes() < this.payloadLength) return;
      UpdateAndLoginHandler.readCRC(in);

      if (this.authMode == AuthMode.EMAIL) {
        ctx.writeAndFlush(ctx.alloc().buffer()
            .writeByte(2)
            .writeBytes(Strings.encode1252String("Please log in with a username."))
            .writeByte(0));
        ctx.pipeline().replace(this, null, new UpdateAndLoginHandler());
        return;
      }
      if (this.authMode != AuthMode.USER_ID) {
        throw new RuntimeException("unknown auth mode: " + this.authMode);
      }

      final byte[] payload = new byte[this.payloadLength - 4];
      in.readBytes(payload);
      final Buffer buffer = new Buffer(payload);

      buffer.readLong(); // instance id
      final int flags = buffer.readUByte();
      final String settings = buffer.readNullTerminatedString();

      buffer.decrypt(BigInteger.ONE, payload.length); // FIXME: encryption
      final int[] cipherIV = IntStream.range(0, 4).map(i -> buffer.readInt()).toArray();
      buffer.skipBytes(24); // random.dat (no idea what this is for)

      buffer.readUShort(); // affId
      final long userId = buffer.readLong();
      buffer.readU56(); // password hash, part 1
      buffer.readU56(); // password hash, part 2
      assert (flags & 4) == 0;
      final String username = UserIdLoginCredentials.decodeUsername(userId);

      final ByteBuf responsePayload = ctx.alloc().buffer()
          .writeLong(userId)
          .writeByte(2) // admin level
          .writeByte(7) // mod level
          .writeShort(1337)
          .writeBytes(Strings.encode1252String(settings))
          .writeByte(0)
          .writeByte(0) // flags
          .writeBytes(Strings.encode1252String(username))
          .writeByte(0)
          .writeByte(0);
      ctx.write(ctx.alloc().buffer()
          .writeByte(0)
          .writeByte(responsePayload.readableBytes()));
      ctx.writeAndFlush(responsePayload);

      final ClientConnection client = new ClientConnection(userId, username, ctx.channel(), cipherIV);
      ctx.pipeline().replace(this, null, new ClientHandler(ShatteredPlansServer.this.lobbyState, client));
    }
  }
}
