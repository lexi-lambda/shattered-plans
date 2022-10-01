package funorb.client.lobby;

import funorb.Strings;
import funorb.commonui.form.validator.UsernameValidator;
import funorb.io.CipheredBuffer;
import funorb.shatteredplans.C2SPacket;
import funorb.shatteredplans.S2CPacket;
import funorb.shatteredplans.StringConstants;
import funorb.shatteredplans.client.JagexApplet;
import funorb.shatteredplans.client.ShatteredPlansClient;
import funorb.util.CollectionUtil;
import org.intellij.lang.annotations.MagicConstant;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.IntStream;

public final class PlayerListEntry extends Component<Component<?>> {
  @MagicConstant(valuesFromClass = ServerStatus.class)
  public static int serverStatus = ServerStatus.UNINITIALIZED;
  public static int ignoreCount;
  private static List<PlayerListEntry> ignores;
  private static Map<String, PlayerListEntry> ignoreMap;
  public static int friendCount;
  private static List<PlayerListEntry> friends;
  private static Map<String, PlayerListEntry> friendMap;

  public String playerDisplayName;
  public String playerName;
  public Component<?> serverNameLabel;
  public Component<?> displayNameChangedIcon;
  private int index;
  public String serverName;
  public String previousDisplayName;
  public Component<?> playerNameLabel;

  private PlayerListEntry() {
    super(null);
  }

  public static String removeFriend(final String playerName) {
    if (!isValidPlayerName(playerName)) {
      return StringConstants.INVALID_NAME;
    }
    if (serverStatus != ServerStatus.LOADED) {
      return StringConstants.UNABLE_TO_DELETE_FRIEND;
    }

    final PlayerListEntry entry = lookupFriend(playerName);
    if (entry == null) {
      return Strings.format(StringConstants.FRIEND_NOT_FOUND, playerName);
    }

    final int entryIndex = friends.indexOf(entry);
    friends.remove(entryIndex);
    friends.stream().skip(entryIndex).forEach(entry1 -> entry1.index--);
    friendMap.remove(Strings.normalize(playerName));
    friendCount--;
    C2SPacket.friendIgnore(1, playerName);
    return null;
  }

  static boolean isValidPlayerName(final CharSequence playerName) {
    return UsernameValidator.isValidUsername(playerName, true)
        && IntStream.range(0, playerName.length()).allMatch(i -> Strings.isNormalizable(playerName.charAt(i)));
  }

  public static String removeIgnore(final String playerName, final String playerDisplayName) {
    if (!isValidPlayerName(playerName)) {
      return StringConstants.INVALID_NAME;
    } else if (serverStatus == ServerStatus.LOADED) {
      final PlayerListEntry var4 = lookupIgnore(playerDisplayName);
      if (var4 == null) {
        return Strings.format(StringConstants.IGNORE_NOT_FOUND, playerDisplayName);
      } else {
        ignores.remove(var4);
        ignoreMap.remove(Strings.normalizeIfPossible(playerDisplayName));
        --ignoreCount;

        C2SPacket.friendIgnore(3, playerName);
        return null;
      }
    } else {
      return StringConstants.UNABLE_TO_DELETE_IGNORE;
    }
  }

  private boolean lessThan(final PlayerListEntry other) {
    int delta = other.index - this.index;

    if (Objects.equals(other.serverName, ShatteredPlansClient.currentServerName)) {
      delta -= 200;
    } else if (other.serverName == null) {
      delta += 200;
    }
    if (Objects.equals(this.serverName, ShatteredPlansClient.currentServerName)) {
      delta += 200;
    } else if (this.serverName == null) {
      delta -= 200;
    }

    return delta > 0;
  }

  public static boolean isIgnored(final String displayName) {
    return lookupIgnore(displayName) != null;
  }

  private static PlayerListEntry lookupIgnore(final String displayName) {
    return ignoreMap == null ? null : ignoreMap.get(Strings.normalizeIfPossible(displayName));
  }

  public static PlayerListEntry lookupFriend(final String displayName) {
    return (friendMap == null || displayName == null || displayName.isEmpty())
        ? null
        : friendMap.get(Strings.normalize(displayName));
  }

  public static void initialize(final @NotNull Component<PlayerListEntry> friendList, final @NotNull Component<PlayerListEntry> ignoreList) {
    if (friendList.children == null) {
      friendList.children = new ArrayList<>();
    }
    if (ignoreList.children == null) {
      ignoreList.children = new ArrayList<>();
    }
    if (friendMap == null) {
      friendMap = new HashMap<>();
    }
    if (ignoreMap == null) {
      ignoreMap = new HashMap<>();
    }

    friends = friendList.children;
    ignores = ignoreList.children;
    reset();
  }

  public static void reset() {
    ShatteredPlansClient.currentServerName = null;
    friendCount = 0;
    ignoreCount = 0;
    friends.clear();
    ignores.clear();
    friendMap.clear();
    ignoreMap.clear();
    serverStatus = ServerStatus.UNINITIALIZED;
  }

  public static void handleSocialPacket(@SuppressWarnings("SameParameterValue") final CipheredBuffer packet) {
    final int type = packet.readUByte();
    if (type == S2CPacket.SocialAction.IGNORE) {
      handleIgnorePacket(packet);
    } else if (type == S2CPacket.SocialAction.FRIEND) {
      handleFriendPacket(packet);
    } else if (type == S2CPacket.SocialAction.LOADED) {
      if (serverStatus == ServerStatus.LOADING) {
        serverStatus = ServerStatus.LOADED;
      }
    } else if (type == S2CPacket.SocialAction.LOADING) {
      if (serverStatus == ServerStatus.LOADED) {
        serverStatus = ServerStatus.LOADING;
      }
    } else if (type == S2CPacket.SocialAction.INITIALIZE) {
      serverStatus = ServerStatus.LOADING;
      ShatteredPlansClient.currentServerName = packet.readNullTerminatedString().intern();
      ContextMenu.setChatFilters(packet.readUByte());
    } else {
      JagexApplet.clientError(null, "F1: " + JagexApplet.a738w());
      JagexApplet.shutdownServerConnection();
    }
  }

  private static void handleIgnorePacket(final CipheredBuffer packet) {
    if (ignoreMap == null) {
      ignoreMap = new HashMap<>();
      ignoreCount = 0;
    }

    final boolean hasNonDisplayName = packet.readUByte() == 1;
    final String displayName = packet.readNullTerminatedString();
    final String playerName;
    if (hasNonDisplayName) {
      playerName = packet.readNullTerminatedString();
    } else {
      playerName = displayName;
    }

    PlayerListEntry entry = lookupIgnore(displayName);
    final String previousName = packet.readNullTerminatedString();
    final String normalizedDisplayName = Strings.normalizeIfPossible(displayName);

    if (entry == null) {
      entry = lookupIgnore(previousName);
      if (entry != null) {
        ignoreMap.put(normalizedDisplayName, entry);
      }
    }

    if (entry == null) {
      entry = new PlayerListEntry();
      ignoreMap.put(normalizedDisplayName, entry);
      entry.index = ignoreCount++;
      ignores.add(entry);
    }

    entry.playerName = playerName;
    entry.playerDisplayName = displayName;
    entry.previousDisplayName = previousName;
  }

  private static void handleFriendPacket(final CipheredBuffer packet) {
    if (friendMap == null) {
      friendMap = new HashMap<>();
      friendCount = 0;
    }

    String serverName = packet.readNullableNullTerminatedString();
    if (serverName != null) {
      serverName = serverName.intern();
    }

    final String displayName = packet.readNullTerminatedString();
    final String previousDisplayName = packet.readNullTerminatedString();
    PlayerListEntry entry = lookupFriend(displayName);
    if (entry == null) {
      entry = lookupFriend(previousDisplayName);
      if (entry != null) {
        friendMap.put(Strings.normalize(displayName), entry);
      }
    }

    if (entry == null) {
      entry = new PlayerListEntry();
      friendMap.put(Strings.normalize(displayName), entry);
      entry.index = friendCount++;
    }

    entry.playerDisplayName = displayName;
    entry.serverName = serverName;
    entry.previousDisplayName = previousDisplayName;

    friends.remove(entry);
    CollectionUtil.insertSorted(friends, entry, PlayerListEntry::lessThan);
  }

  @SuppressWarnings("WeakerAccess")
  public static final class ServerStatus {
    public static final int UNINITIALIZED = 0;
    public static final int LOADING = 1;
    public static final int LOADED = 2;
  }
}
