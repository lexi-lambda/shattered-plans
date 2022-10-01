package funorb.client.lobby;

import funorb.Strings;
import funorb.client.lobby.ChatMessage.Channel;
import funorb.client.lobby.ChatMessage.FilterLevel;
import funorb.shatteredplans.C2SPacket;
import funorb.shatteredplans.StringConstants;
import funorb.shatteredplans.client.JagexApplet;
import funorb.shatteredplans.client.ShatteredPlansClient;
import funorb.shatteredplans.game.ai.TutorialAI2;
import org.intellij.lang.annotations.MagicConstant;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

import static funorb.client.lobby.ContextMenu.ClickAction.*;

public final class ContextMenu {
  public static String recipientPlayerName;
  public static boolean _fpv = true;
  public static long recipientPlayerId;
  public static int roomShowingPlayers;
  public static String normalizedRecipientPlayerName;
  public static ContextMenu openInstance;
  private static @NotNull FilterLevel chatFilterLobby = FilterLevel.ALL;
  private static @NotNull FilterLevel chatFilterRoom = FilterLevel.ALL;
  private static @NotNull FilterLevel chatFilterPrivate = FilterLevel.NONE;
  public final Component<?> target;
  public final int roomId;
  public final PopupMenu view;
  public final String playerName;
  private final String playerDisplayName;
  private int[] _i;
  private @Nullable Channel selectedChannel;
  private final long playerId;

  public ContextMenu(final Component<?> target, final long playerId, final String playerName, final String playerDisplayName, final int roomId, final @Nullable Channel selectedChannel) {
    this.target = target;
    target.selected = true;
    this.view = new PopupMenu(Component._hdt);
    this._i = null;
    this.playerId = playerId;
    this.playerName = playerName;
    this.playerDisplayName = playerDisplayName;
    this.roomId = roomId;
    this.selectedChannel = selectedChannel;
  }

  private static void sendChannelMessage() {
    ShatteredPlansClient.currentChatChannel = Channel.LOBBY;
    ShatteredPlansClient.isChatboxSelected = true;
  }

  private static void sendPrivateMessage(final long playerId, final String playerName) {
    ShatteredPlansClient.currentChatChannel = Channel.PRIVATE;
    recipientPlayerId = playerId;
    recipientPlayerName = playerName;
    normalizedRecipientPlayerName = Strings.normalize(playerName);
    ShatteredPlansClient.isChatboxSelected = true;
  }

  public static void setChatFilter(final @NotNull Channel channel,
                                   final @NotNull FilterLevel value) {
    if (channel == Channel.LOBBY && chatFilterLobby != value) {
      _fpv = true;
      chatFilterLobby = value;
      C2SPacket.setChatFilters(getChatFilters());
    }
    if (channel == Channel.ROOM && chatFilterRoom != value) {
      _fpv = true;
      chatFilterRoom = value;
      C2SPacket.setChatFilters(getChatFilters());
    }
    if (channel == Channel.PRIVATE && chatFilterPrivate != value) {
      _fpv = true;
      chatFilterPrivate = value;
      C2SPacket.setChatFilters(getChatFilters());
    }
  }

  private static void sendPrivateMessageQC(final String recipientName, final long var1) {
    recipientPlayerName = recipientName;
    ShatteredPlansClient.currentChatChannel = Channel.PRIVATE;
    normalizedRecipientPlayerName = Strings.normalize(recipientName);
    recipientPlayerId = var1;
    r150bo();
  }

  public static void sendChannelMessageQC() {
    ShatteredPlansClient.currentChatChannel = Channel.LOBBY;
    r150bo();
  }

  private static void r150bo() {
    a130bp(Component._ofb);
  }

  public static boolean autoRespond(final long var0, final String var2, final int[] var4, @NotNull Channel channel) {
    if (TutorialAI2.a051(var4, var2, channel, var0)) {
      if (channel == Channel.ROOM) {
        channel = Channel.LOBBY;
      }

      ShatteredPlansClient.currentChatChannel = channel;
      recipientPlayerName = var2;
      normalizedRecipientPlayerName = Strings.normalize(var2);
      recipientPlayerId = var0;
      final vm_ var6 = a925bo(var4, Component._nld, Component._hlI, ReportAbuseDialog._Nb);
      a130bp(var6);
      return true;
    } else {
      return false;
    }
  }

  private static vm_ a925bo(final int[] var0, final Component<?> var2, final Component<?> var3, final Component<?> var4) {
    final int var5 = var0.length;
    final String[] var6 = new String[var5];
    final char[] var7 = new char[var5];
    final vm_[] var8 = new vm_[var5];
    char var9 = '1';

    try {

      for (int var10 = 0; var5 > var10; ++var10) {
        final QuickChatResponse var11 = JagexApplet._dhc.get(var0[var10]);
        var6[var10] = var11.joinStrings();
        var7[var10] = var9++;
        var8[var10] = null;
      }
    } catch (final Exception var12) {
      return null;
    }

    return new vm_(var3, var4, var2, var8, var0, var6, var7);
  }

  private static void a130bp(final vm_ var1) {
    if (var1 != null) {
      ShatteredPlansClient._dmrh = var1;
      Component._uaf.children.clear();
      Component._uaf.addChild(ShatteredPlansClient._dmrh);
      ShatteredPlansClient.isQuickChatOpen = true;
    }
  }

  public static String addFriend(final String playerName) {
    if (!PlayerListEntry.isValidPlayerName(playerName)) {
      return StringConstants.INVALID_NAME;
    } else if (PlayerListEntry.serverStatus != PlayerListEntry.ServerStatus.LOADED) {
      return StringConstants.UNABLE_TO_ADD_FRIEND;
    } else if (JagexApplet.a623jp(playerName)) {
      return StringConstants.CANNOT_ADD_YOURSELF;
    } else if (ShatteredPlansClient.a988da(playerName)) {
      return Strings.format(StringConstants.FRIEND_LIST_DUPE, playerName);
    } else if (PlayerListEntry.friendCount >= 100 && JagexApplet.membershipLevel <= 0) {
      return StringConstants.FRIEND_LIST_FULL;
    } else if (PlayerListEntry.friendCount >= 200) {
      return StringConstants.FRIEND_LIST_FULL;
    } else if (PlayerListEntry.isIgnored(playerName)) {
      return Strings.format(StringConstants.REMOVE_FROM_IGNORE_FIRST, playerName);
    } else {
      C2SPacket.friendIgnore(0, playerName);
      return null;
    }
  }

  public static String addIgnore(final String var1) {
    if (PlayerListEntry.isValidPlayerName(var1)) {
      if (JagexApplet.a623jp(var1)) {
        return StringConstants.CANNOT_ADD_YOURSELF;
      } else if (PlayerListEntry.serverStatus != PlayerListEntry.ServerStatus.LOADED) {
        return StringConstants.UNABLE_TO_ADD_IGNORE;
      } else if (PlayerListEntry.isIgnored(var1)) {
        return Strings.format(StringConstants.IGNORE_LIST_DUPE, var1);
      } else if (PlayerListEntry.ignoreCount < 100) {
        if (ShatteredPlansClient.a988da(var1)) {
          return Strings.format(StringConstants.REMOVE_FRIEND_FIRST, var1);
        } else {
          C2SPacket.friendIgnore(2, var1);
          return null;
        }
      } else {
        return StringConstants.IGNORE_LIST_FULL;
      }
    } else {
      return StringConstants.INVALID_NAME;
    }
  }

  public static void showChatMessage(final @NotNull Channel channel, final @NotNull String message, final int var2, final String var3, final String var4) {
    showChatMessage(new ChatMessage(channel, var3, var2, var4, message));
  }

  private static void showPlayersInRoom(final int roomId) {
    if (roomShowingPlayers != roomId) {
      final ClientLobbyRoom prevRoomShowingPlayers = ClientLobbyRoom.roomsMap.get(roomShowingPlayers);
      roomShowingPlayers = roomId;
      if (prevRoomShowingPlayers != null) {
        prevRoomShowingPlayers.joinedPlayerNames = null;
      }
      C2SPacket.showPlayersInGame(roomId);
    }
  }

  private static int getChatFilters() {
    return chatFilterPrivate.encode() + (chatFilterRoom.encode() << 2) + (chatFilterLobby.encode() << 4);
  }

  public static void setChatFilters(final int packed) {
    chatFilterLobby = FilterLevel.decode(Math.min((packed >> 4) & 0b11, 2));
    chatFilterRoom = FilterLevel.decode(Math.min((packed >> 2) & 0b11, 2));
    chatFilterPrivate = FilterLevel.decode(Math.min(packed & 0b11, 2));
  }

  public static @NotNull FilterLevel getChatChannelFilter(final @NotNull Channel channel) {
    return switch (channel) {
      case LOBBY   -> chatFilterLobby;
      case ROOM    -> chatFilterRoom;
      case PRIVATE -> chatFilterPrivate;
      default      -> FilterLevel.ALL;
    };
  }

  public static void showChatMessage(final ChatMessage message) {
    for (int i = 0; i < 3; ++i) {
      ShatteredPlansClient._imb[i] = 0;
    }

    for (int i = 0; i < ShatteredPlansClient.chatMessageCount; ++i) {
      if (ShatteredPlansClient.chatMessages[i].channel == message.channel) {
        ShatteredPlansClient._imb[ShatteredPlansClient.chatMessages[i].a410().ordinal()]++;
      }
    }
    ShatteredPlansClient._imb[message.a410().ordinal()]++;

    int var1 = 0;
    for (int i = 0; i < ShatteredPlansClient.chatMessageCount; ++i) {
      if (ShatteredPlansClient.chatMessages[i].channel == message.channel) {
        final Channel var3 = ShatteredPlansClient.chatMessages[i].a410();
        if (ShatteredPlansClient._kpi < ShatteredPlansClient._imb[var3.ordinal()]) {
          ShatteredPlansClient._imb[var3.ordinal()]--;
          continue;
        }
      }

      ShatteredPlansClient.chatMessages[var1++] = ShatteredPlansClient.chatMessages[i];
    }

    ShatteredPlansClient.chatMessageCount = var1;
    ShatteredPlansClient.chatMessages[ShatteredPlansClient.chatMessageCount++] = message;
  }

  public void addPlayerItems(final boolean var1) {
    if (this.playerName != null && this.playerId != ShatteredPlansClient.localPlayerId && ShatteredPlansClient.unratedLobbyRoom != null && ShatteredPlansClient.isCurrentRoomOwner()) {
      final LobbyPlayer var3 = LobbyPlayer.getOnline(this.playerId);
      final LobbyPlayer var4 = LobbyPlayer.getJoined(this.playerId);
      final String var5 = this.a738();
      if (var4 != null) {
        if (!ShatteredPlansClient.unratedLobbyRoom.hasStarted || ShatteredPlansClient.unratedLobbyRoom.finalElapsedTime >= 0) {
          this.view.addItem(Strings.format(StringConstants.MU_GAMEOPT_KICK_X_FROM_THIS_GAME, var5), KICK_FROM_GAME);
        }
      } else if (var3 != null && ShatteredPlansClient.unratedLobbyRoom.notInProgress && ShatteredPlansClient.unratedLobbyRoom.joinedPlayerCount < ShatteredPlansClient.unratedLobbyRoom.maxPlayerCount) {
        if (var3.inviteSent) {
          this.view.addItem(Strings.format(StringConstants.MU_GAMEOPT_WITHDRAW_INVITE_TO_X, var5), KICK_FROM_GAME);
        } else if (var1 && var3.joinRequestReceived) {
          this.view.addItem(Strings.format(StringConstants.MU_GAMEOPT_ACCEPT_X_INTO_GAME, var5), INVITE_TO_GAME);
          this.view.addItem(Strings.format(StringConstants.MU_GAMEOPT_REJECT_X_FROM_GAME, var5), KICK_FROM_GAME);
        } else {
          this.view.addItem(Strings.format(StringConstants.MU_GAMEOPT_INVITE_X_TO_GAME, var5), INVITE_TO_GAME);
        }
      }
    }
  }

  public boolean tick(final boolean mouseNotYetHandled) {
    @MagicConstant(valuesFromClass = ClickAction.class)
    final int which = this.view.getClickAction(mouseNotYetHandled);
    if (which == PASS) {
      return false;
    } else {
      switch (which) {
        case INVITE_TO_GAME             -> C2SPacket.inviteToGame(this.playerId);
        case KICK_FROM_GAME             -> C2SPacket.kickFromGame(this.playerId);

        case SUBMIT_JOIN_ROOM_REQUEST   -> C2SPacket.requestToJoinRoom(this.roomId);
        case WITHDRAW_JOIN_ROOM_REQUEST -> C2SPacket.requestToLeaveRoom(this.roomId);
        case SPECTATE_GAME              -> C2SPacket.spectateGame(this.roomId);
        case SHOW_PLAYERS_IN_GAME       -> showPlayersInRoom(this.roomId);
        case HIDE_PLAYERS_IN_GAME       -> showPlayersInRoom(0);

        case ADD_FRIEND                 -> this.maybeShowSocialActionError(addFriend(this.playerName));
        case REMOVE_FRIEND              -> this.maybeShowSocialActionError(PlayerListEntry.removeFriend(this.playerName));
        case ADD_IGNORE                 -> this.maybeShowSocialActionError(addIgnore(this.playerName));
        case REMOVE_IGNORE              -> this.maybeShowSocialActionError(PlayerListEntry.removeIgnore(this.playerName, this.playerDisplayName));

        case CHAT_SHOW_ALL              -> setChatFilter(Objects.requireNonNull(this.selectedChannel), FilterLevel.ALL);
        case CHAT_SHOW_FRIENDS          -> setChatFilter(Objects.requireNonNull(this.selectedChannel), FilterLevel.FRIENDS);
        case CHAT_SHOW_NONE             -> setChatFilter(Objects.requireNonNull(this.selectedChannel), FilterLevel.NONE);

        case SEND_PRIVATE_MESSAGE       -> sendPrivateMessage(this.playerId, this.playerName);
        case SEND_CHANNEL_MESSAGE       -> sendChannelMessage();
        case SEND_PRIVATE_MESSAGE_QC    -> sendPrivateMessageQC(this.playerName, this.playerId);
        case SEND_CHANNEL_MESSAGE_QC    -> sendChannelMessageQC();
        case AUTO_RESPOND               -> autoRespond(this.playerId, this.playerName, this._i, Objects.requireNonNull(this.selectedChannel));
        case REPORT_ABUSE               -> this.openReportAbuseDialog();
      }
      return true;
    }
  }

  private void maybeShowSocialActionError(final String message) {
    if (message != null) {
      showChatMessage(Channel.PRIVATE, message, 0, this.playerName, null);
    }
  }

  private void openReportAbuseDialog() {
    ReportAbuseDialog.openInstance = new ReportAbuseDialog(this.target.x2, this.target.y2, this.target.width, this.target.height, Component.TAB_ACTIVE, Component.CLOSE_BUTTON, Component.LABEL, Component.CHECKBOX, Component.UNSELECTED_LABEL, this.playerName, this.playerId);
  }

  public void a124(final int[] var1, final @NotNull Channel var2) {
    if (this.playerName != null && (this.playerId != ShatteredPlansClient.localPlayerId || var2 == Channel.PRIVATE) && PlayerListEntry.serverStatus == PlayerListEntry.ServerStatus.LOADED) {
      final String var4 = this.a738();
      final PlayerListEntry var5 = PlayerListEntry.lookupFriend(this.playerName);
      final boolean var6 = PlayerListEntry.isIgnored(this.playerName);
      if (var5 == null && !var6) {
        this.view.addItem(Strings.format(StringConstants.ADD_X_TO_FRIENDS, var4), ADD_FRIEND);
        this.view.addItem(Strings.format(StringConstants.ADD_X_TO_IGNORE, var4), ADD_IGNORE);
        if (var1 != null && var2 != Channel.PRIVATE && !JagexApplet.cannotChat) {
          this._i = var1;
          this.view.addItem(Strings.format(StringConstants.AUTO_RESPOND, var4), AUTO_RESPOND);
        }
      }

      if (var5 != null) {
        if (!ShatteredPlansClient.isComposingChatMessageTo(this.playerName) && !JagexApplet.cannotChat) {
          if (!JagexApplet.canOnlyQuickChat) {
            this.view.addItem(Strings.format(StringConstants.SEND_PM_TO_X, var4), SEND_PRIVATE_MESSAGE);
          }

          this.view.addItem(Strings.format(StringConstants.SEND_QC_TO_X, var4), SEND_PRIVATE_MESSAGE_QC);
          if (var1 != null) {
            this._i = var1;
            this.view.addItem(Strings.format(StringConstants.AUTO_RESPOND, var4), AUTO_RESPOND);
          }
        }

        this.view.addItem(Strings.format(StringConstants.RM_X_FROM_FRIENDS, var4), REMOVE_FRIEND);
      }

      if (var6) {
        this.view.addItem(Strings.format(StringConstants.RM_X_FROM_IGNORE, var4), REMOVE_IGNORE);
      }
    }
  }

  private String a738() {
    return this.playerDisplayName == null ? this.playerName : this.playerDisplayName;
  }

  public void b150() {

    if (this.playerName != null && ShatteredPlansClient.localPlayerId != this.playerId) {
      final String var2 = this.a738();
      this.view.addItem(Strings.format(StringConstants.REPORT_X_FOR_ABUSE, var2), REPORT_ABUSE);
    }

  }

  public void a430(final boolean var2) {

    this.view.b540(var2);
  }

  public void a408(final ChatMessage message) {
    if (!message._h) {
      this.selectedChannel = message.channel;
      if (message.channel == Channel.LOBBY) {
        if (chatFilterLobby == FilterLevel.ALL) {
          this.view.addItem(StringConstants.MU_CHAT_LOBBY_FRIENDS_ONLY, CHAT_SHOW_FRIENDS);
        }
        this.view.addItem(StringConstants.MU_CHAT_LOBBY_HIDE, CHAT_SHOW_NONE);
      } else if (message.channel == Channel.ROOM) {
        if (chatFilterRoom == FilterLevel.ALL) {
          this.view.addItem(StringConstants.MU_CHAT_GAME_FRIENDS_ONLY, CHAT_SHOW_FRIENDS);
        }
        this.view.addItem(StringConstants.MU_CHAT_GAME_HIDE, CHAT_SHOW_NONE);
      } else if (message.channel == Channel.PRIVATE) {
        if (chatFilterPrivate == FilterLevel.ALL) {
          this.view.addItem(StringConstants.MU_CHAT_PM_FRIENDS_ONLY, CHAT_SHOW_FRIENDS);
        }
        this.view.addItem(StringConstants.MU_CHAT_INVISIBLE_AND_SILENT_MODE, CHAT_SHOW_NONE);
      }
    }
  }

  public void a487() {
    if (ShatteredPlansClient.unratedLobbyRoom == null) {
      final ClientLobbyRoom room = ClientLobbyRoom.lookup(this.roomId);
      if (room != null) {
        if (room.youAreInvited) {
          this.view.addItem(Strings.format(StringConstants.ACCEPT_PLAYER_INVITATION, room.ownerName), SUBMIT_JOIN_ROOM_REQUEST);
          this.view.addItem(Strings.format(StringConstants.INVITE_DECLINE_XS_GAME, room.ownerName), WITHDRAW_JOIN_ROOM_REQUEST);
        } else {
          if (room.youCanJoin) {
            this.view.addItem(Strings.format(StringConstants.JOIN_XS_GAME, room.ownerName), SUBMIT_JOIN_ROOM_REQUEST);
          } else if (!room.submittedJoinRequest && room.notInProgress && !room.hasStarted) {
            this.view.addItem(Strings.format(StringConstants.JOIN_REQUEST_XS_GAME, room.ownerName), SUBMIT_JOIN_ROOM_REQUEST);
          }

          if (room.submittedJoinRequest) {
            this.view.addItem(Strings.format(StringConstants.JOIN_WITHDRAW_REQUEST_XS_GAME, room.ownerName), WITHDRAW_JOIN_ROOM_REQUEST);
          }
        }
      }
    }
  }

  @SuppressWarnings("WeakerAccess")
  public static final class ClickAction {
    public static final int NONE = -1;
    public static final int PASS = -2;
    public static final int INVITE_TO_GAME = 0;
    public static final int KICK_FROM_GAME = 1;
    public static final int SUBMIT_JOIN_ROOM_REQUEST = 2;
    public static final int WITHDRAW_JOIN_ROOM_REQUEST = 3;
    public static final int ADD_FRIEND = 4;
    public static final int REMOVE_FRIEND = 5;
    public static final int ADD_IGNORE = 6;
    public static final int REMOVE_IGNORE = 7;
    public static final int SEND_PRIVATE_MESSAGE = 8;
    public static final int SPECTATE_GAME = 10;
    public static final int CHAT_SHOW_ALL = 11;
    public static final int CHAT_SHOW_FRIENDS = 12;
    public static final int CHAT_SHOW_NONE = 13;
    public static final int SEND_CHANNEL_MESSAGE = 14;
    public static final int SHOW_PLAYERS_IN_GAME = 15;
    public static final int HIDE_PLAYERS_IN_GAME = 16;
    public static final int REPORT_ABUSE = 17;
    public static final int SEND_PRIVATE_MESSAGE_QC = 18;
    public static final int AUTO_RESPOND = 19;
    public static final int SEND_CHANNEL_MESSAGE_QC = 20;
  }
}
