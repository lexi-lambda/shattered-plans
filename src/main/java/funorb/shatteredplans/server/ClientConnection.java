package funorb.shatteredplans.server;

import funorb.shatteredplans.S2CPacket;
import funorb.util.IsaacCipher;
import io.netty.channel.Channel;

import java.util.Arrays;
import java.util.Optional;

public final class ClientConnection {
  public final long userId;
  public final String username;
  public final Channel channel;
  public final long connectedAt;
  public final IsaacCipher c2sCipher;
  public final IsaacCipher s2cCipher;

  @SuppressWarnings("CanBeFinal")
  public int rating = 1000;
  @SuppressWarnings("CanBeFinal")
  public int ratedGameCount = 0;
  public final int crown = 0;
  public final int unlockedOptionsBitmap = 0;

  public LobbyRoom room;
  public NetworkedGameSession spectateSession;

  public ClientConnection(final long userId, final String username, final Channel channel, final int[] cipherIV) {
    this.userId = userId;
    this.username = username;
    this.channel = channel;
    this.connectedAt = System.currentTimeMillis();
    this.c2sCipher = new IsaacCipher(cipherIV);
    Arrays.setAll(cipherIV, i -> cipherIV[i] + 50);
    this.s2cCipher = new IsaacCipher(cipherIV);
  }

  public String getLogPrefix() {
    return "[" + this.channel.remoteAddress() + "] ";
  }

  public Optional<NetworkedGameSession> getPlaySession() {
    return Optional.ofNullable(this.room == null ? null : this.room.session);
  }

  public void sendPrivateMessage(final ClientConnection sender, final String message) {
    this.channel.writeAndFlush(S2CPacket.privateChatMessage(this.channel.alloc(), sender.userId, sender.username, sender.username, message));
  }
}
