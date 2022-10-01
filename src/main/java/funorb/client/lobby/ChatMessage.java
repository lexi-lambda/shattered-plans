package funorb.client.lobby;

import funorb.shatteredplans.client.ShatteredPlansClient;
import org.jetbrains.annotations.NotNull;

public final class ChatMessage {
  public final long senderId;
  public final boolean _h;
  public final int _g;
  public final String _o;
  public final @NotNull String message;
  public final int _c;
  public final String senderDisplayName;
  public final int _l;
  public final int[] _f;
  public final String senderName;
  public final @NotNull Channel channel;
  public final int _e;
  public Component<?> component;

  public ChatMessage(final boolean isQuick,
                     final int c,
                     final long senderId,
                     final int l,
                     final @NotNull Channel channel,
                     final int e,
                     final String senderDisplayName,
                     final int[] f,
                     final String senderName,
                     final @NotNull String message,
                     final boolean h,
                     final String o,
                     final int g) {
    this._e = e;
    this._f = isQuick ? f : null;
    this.senderName = senderName;
    this.message = message;
    this._c = c;
    this._h = h;
    this._o = o;
    this.senderId = senderId;
    this._l = l;
    this._g = g;
    this.channel = channel;
    this.senderDisplayName = senderDisplayName;
  }

  public ChatMessage(final @NotNull Channel channel,
                     final String var2,
                     final int var3,
                     final String var4,
                     final @NotNull String message) {
    this._h = true;
    this.message = message;
    this._g = var3;
    this._o = var4;
    this.senderDisplayName = var2;
    this._c = 0;
    this.channel = channel;
    this.senderId = 0L;
    this._e = 0;
    this._f = null;
    this.senderName = var2;
    this._l = 0;
  }

  public static String a651(final String var0, String var1, final String var2) {
    for (int var3 = var1.indexOf(var0); var3 != -1; var3 = var1.indexOf(var0, var3 + var2.length())) {
      var1 = var1.substring(0, var3) + var2 + var1.substring(var3 + var0.length());
    }

    return var1;
  }

  public Channel a410() {
    if (this._h || (this.channel == Channel.PRIVATE && this._c > 0)) {
      return Channel.PRIVATE;
    } else if (this.senderId == ShatteredPlansClient.localPlayerId || (PlayerListEntry.serverStatus == PlayerListEntry.ServerStatus.LOADED && ShatteredPlansClient.a988da(this.senderName))) {
      return Channel.ROOM;
    } else {
      return Channel.LOBBY;
    }
  }

  public enum Channel {
    LOBBY,
    ROOM,
    PRIVATE,
    // not sure what these are for!
    CHANNEL_4,
    CHANNEL_5;

    public int encode() {
      return this.ordinal();
    }

    public static Channel decode(final int val) {
      return values()[val];
    }
  }

  public enum FilterLevel {
    ALL,
    FRIENDS,
    NONE;

    public boolean lessThanOrEqual(final Channel other) {
      return this.ordinal() <= other.ordinal();
    }

    public int encode() {
      return this.ordinal();
    }

    public static FilterLevel decode(final int val) {
      return values()[val];
    }
  }
}
