package funorb.client.lobby;

import funorb.util.CollectionUtil;

import java.util.List;
import java.util.Map;

public final class LobbyPlayer extends Component<Component<?>> {
  public static List<LobbyPlayer> online;
  public static Map<Long, LobbyPlayer> onlineMap;
  public static List<LobbyPlayer> joinedPlayers;
  public static Map<Long, LobbyPlayer> joinedPlayersMap;

  public final long playerId;
  public int rating;
  public long joinedAt;
  public int ratedGameCount;
  public Component<?> _xb;
  public int unlockedOptionsBitmap;
  public boolean joinRequestReceived;
  public int statusTimer;
  public Component<?> _Ob;
  public Component<?> _Mb;
  public int status;
  public int crown;
  public String playerDisplayName;
  public boolean inviteSent;
  public Component<?> _Jb;
  public String playerName;
  public Component<?> _Ab;

  public LobbyPlayer(final long playerId, final String playerName, final String playerDisplayName) {
    super(null);
    this.playerName = playerName;
    this.playerDisplayName = playerDisplayName;
    this.playerId = playerId;
  }

  public static LobbyPlayer getOnline(final long playerId) {
    return onlineMap.get(playerId);
  }

  public static LobbyPlayer getJoined(final long playerId) {
    return joinedPlayersMap.get(playerId);
  }

  public static void addOnline(final LobbyPlayer player) {
    online.remove(player);
    CollectionUtil.insertSorted(online, player, LobbyPlayer::lessThan);
  }

  @SuppressWarnings("InverseElseComparator")
  private boolean lessThan(final LobbyPlayer other) {
    if (this.joinRequestReceived != other.joinRequestReceived) {
      return this.joinRequestReceived;
    } else if (this.inviteSent != other.inviteSent) {
      return this.inviteSent;
    } else {
      return this.joinedAt < other.joinedAt;
    }
  }

  public void setName(final String name, final String displayName) {
    this.playerName = name;
    this.playerDisplayName = displayName;
  }

  public boolean isInMap() {
    return onlineMap.containsKey(this.playerId) || joinedPlayersMap.containsKey(this.playerId);
  }

  public static final class Status {
    public static final int NONE = 0;
    public static final int ENTERED_GAME = 1;
    public static final int JOINED_YOUR_GAME = 2;
    public static final int ENTERED_OTHER_GAME = 3;
    public static final int LEFT_LOBBY = 4;
    public static final int LOST_CON = 5;
    public static final int CANNOT_JOIN_FULL = 6;
    public static final int CANNOT_JOIN_IN_PROGRESS = 7;
    public static final int DECLINED_INVITE = 8;
    public static final int WITHDREW_REQUEST = 11;
    public static final int KICKED = 12;
    public static final int DROPPED_OUT = 13;
  }
}
