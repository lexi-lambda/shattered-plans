package funorb.client.lobby;

import funorb.Strings;
import funorb.awt.MouseState;
import funorb.graphics.Drawing;
import funorb.graphics.Font;
import funorb.graphics.Sprite;
import funorb.io.Buffer;
import funorb.shatteredplans.C2SPacket;
import funorb.shatteredplans.StringConstants;
import funorb.shatteredplans.client.JagexApplet;
import funorb.shatteredplans.client.ShatteredPlansClient;
import funorb.util.CollectionUtil;
import funorb.util.PseudoMonotonicClock;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public final class ClientLobbyRoom extends Component<Component<?>> {
  private static final int _mlj = 4;

  public static List<ClientLobbyRoom> rooms;
  public static Map<Integer, ClientLobbyRoom> roomsMap;

  public final int roomId;
  public static Sprite[][] GAMEOPT_SPRITES;
  public static String currentTooltip;
  public static int _qob;
  public static Sprite[] ALLOW_SPECTATORS_SPRITES;
  public static Sprite[] RATED_GAME_SPRITES;
  public static Sprite[] OPEN_TO_ME_SPRITES;
  public static Sprite[] WHO_CAN_JOIN_OPTION_SPRITES;
  public static String[] WHO_CAN_JOIN_OPTION_TOOLTIPS;
  public final byte[] gameSpecificOptions;
  private Component<?> askingToJoinLabel;
  public boolean notInProgress;
  private Component<?> _Wb;
  private Component<?> _Ib;
  private Component<?> _Db;
  private int averageRating;
  private Component<?> _Ub;
  private boolean youAreAllowedToJoin;
  public String[] joinedPlayerNames;
  private Component<?> isRatedIcon;
  private Component<Component<?>> _Mb;
  private long startedAt;
  public int status;
  private Component<?>[] gameOptionIcons;
  private Component<?> _Rb;
  public boolean youCanJoin;
  public int allowSpectate;
  private Component<?> canJoinIcon;
  public int finalElapsedTime;
  public boolean hasStarted;
  private Component<?> _Pb;
  public long ownerId;
  public int whoCanJoin;
  private Component<?> youAreInvitedLabel;
  public int maxPlayerCount;
  private Component<?> cannotJoinReasonLabel;
  private Component<?> playersInGameLabel;
  private boolean isRated;
  public boolean submittedJoinRequest;
  private Component<?> whoCanJoinIcon;
  public int statusTimer;
  public String ownerName;
  public boolean youAreInvited;
  public int joinedPlayerCount;
  private Component<?> ageLabel;
  private Component<?> allowSpectateIcon;

  public ClientLobbyRoom(final int roomId) {
    super(null);
    this.roomId = roomId;
    this.gameSpecificOptions = new byte[ShatteredPlansClient.GAMEOPT_CHOICES_COUNTS.length];
  }

  private static String removalReasonMessage(final int status, final String ownerName) {
    if (status == Status.ENTERED_OTHER_GAME) {
      return StringConstants.MU_ENTERED_OTHER_GAME;
    } else if (status == Status.ROOM_FULL) {
      return StringConstants.MU_GAME_IS_FULL;
    } else if (status == Status.GAME_HAS_STARTED) {
      return StringConstants.MU_GAME_HAS_STARTED;
    } else if (status == Status.YOU_DECLINED_INVITE) {
      return StringConstants.MU_YOU_DECLINED_INVITE;
    } else if (status == Status.INVITE_WITHDRAWN) {
      return StringConstants.MU_INVITE_WITHDRAWN;
    } else if (status == Status.REQUEST_DECLINED) {
      return StringConstants.MU_REQUEST_DECLINED;
    } else if (status == Status.REQUEST_WITHDRAWN) {
      return StringConstants.MU_REQUEST_WITHDRAWN;
    } else if (status == Status.ALL_PLAYERS_HAVE_LEFT) {
      return Strings.format(StringConstants.MU_ALL_PLAYERS_HAVE_LEFT, ownerName);
    } else {
      return null;
    }
  }

  public static void tick(final boolean var0, final int var4) {
    final boolean var6 = Component.GAME_LIST_SCROLL_PANE.processScrollInput(var0, ShatteredPlansClient.currentContextMenuParent == Component.GAME_LIST_SCROLL_PANE, (2 + Component.LABEL_HEIGHT) * 2, (Component.LABEL_HEIGHT * 4 + 8) * var4);
    final List<ClientLobbyRoom> rooms = Component.GAME_LIST_SCROLL_PANE.content.children;
    final long now = PseudoMonotonicClock.currentTimeMillis();

    for (final ClientLobbyRoom room : rooms) {
      boolean var12 = false;
      if (room.children == null) {
        room._Ub = new Component<>(Component.UNSELECTED_LABEL_DARK_1);
        room.addChild(room._Ub);
        room._Ub.textAlignment = Font.HorizontalAlignment.CENTER;
        room._Pb = new Component<>(Component.UNSELECTED_LABEL_DARK_2);
        room.addChild(room._Pb);
        room._Rb = new Component<>(Component.UNSELECTED_LABEL_DARK_1);
        room.addChild(room._Rb);
        room._Rb._kb = 0;
        room._Wb = new Component<>(Component.UNSELECTED_LABEL_DARK_1);
        room.addChild(room._Wb);
        room._Wb._kb = 0;
        room._Ib = new Component<>(Component.UNSELECTED_LABEL_DARK_2);
        room.addChild(room._Ib);
        room._Ib.textAlignment = Font.HorizontalAlignment.RIGHT;
        room._Mb = new Component<>(Component.UNSELECTED_LABEL_DARK_1);
        room.addChild(room._Mb);
        room.gameOptionIcons = new Component[ShatteredPlansClient.GAMEOPT_CHOICES_COUNTS.length];
        room.whoCanJoinIcon = new Component<>(null);
        room._Mb.addChild(room.whoCanJoinIcon);

        room.canJoinIcon = new Component<>(null);
        room._Mb.addChild(room.canJoinIcon);

        room.allowSpectateIcon = new Component<>(null);
        room._Mb.addChild(room.allowSpectateIcon);

        room.isRatedIcon = new Component<>(null);
        room._Mb.addChild(room.isRatedIcon);

        final Component<?> var14 = room.whoCanJoinIcon;
        final Component<?> var15 = room.isRatedIcon;
        room.allowSpectateIcon.verticalAlignment = Font.VerticalAlignment.MIDDLE;
        final Component<?> var16 = room.canJoinIcon;
        var16.verticalAlignment = Font.VerticalAlignment.MIDDLE;
        var15.verticalAlignment = Font.VerticalAlignment.MIDDLE;
        var14.verticalAlignment = Font.VerticalAlignment.MIDDLE;

        for (int i = 0; i < ShatteredPlansClient.GAMEOPT_CHOICES_COUNTS.length; ++i) {
          room.gameOptionIcons[i] = new Component<>(null);
          if (((1 << (_mlj + i)) & Drawing.WHITE) != 0) {
            room._Mb.addChild(room.gameOptionIcons[i]);
          }
          room.gameOptionIcons[i].verticalAlignment = Font.VerticalAlignment.MIDDLE;
        }

        room.ageLabel = new Component<>(Component.UNSELECTED_LABEL_DARK_2);
        room.addChild(room.ageLabel);
        room.ageLabel.textAlignment = Font.HorizontalAlignment.RIGHT;
        room.playersInGameLabel = new Component<>(Component.UNSELECTED_LABEL_DARK_2);
        room.addChild(room.playersInGameLabel);
        room.youAreInvitedLabel = new Component<>(Component.UNSELECTED_LABEL);
        room.addChild(room.youAreInvitedLabel);
        room.askingToJoinLabel = new Component<>(Component.UNSELECTED_LABEL);
        room.addChild(room.askingToJoinLabel);
        room.cannotJoinReasonLabel = new Component<>(Component.LABEL);
        room.addChild(room.cannotJoinReasonLabel);
        room.recursivelySet_H();
        room._Db = new Component<>(Component.GREEN_BUTTON);
        var12 = true;
        room.addChild(room._Db);
      }

      room._Ub.label = null;
      room._Ub.height = 0;
      final Component<?> var14 = room._Ub;
      var14.width = 0;
      room._Db.label = null;
      room._Db.height = 0;
      final Component<?> var15 = room._Db;
      var15.width = 0;
      room._Pb.label = null;
      final Component<?> var16 = room._Pb;
      room._Pb.height = 0;
      room._Rb.label = null;
      var16.width = 0;
      room._Rb.height = 0;
      final Component<?> var40 = room._Rb;
      var40.width = 0;
      room._Wb.label = null;
      room._Wb.height = 0;
      final Component<?> var18 = room._Wb;
      var18.width = 0;
      room._Ib.label = null;
      final Component<?> var19 = room._Ib;
      room._Ib.height = 0;
      var19.width = 0;
      room._Mb.label = null;
      final Component<?> var20 = room._Mb;
      room._Mb.height = 0;
      var20.width = 0;

      for (int i = 0; i < ShatteredPlansClient.GAMEOPT_CHOICES_COUNTS.length; ++i) {
        room.gameOptionIcons[i].sprite = null;
        room.gameOptionIcons[i].height = 0;
        room.gameOptionIcons[i].width = 0;
      }

      room.whoCanJoinIcon.sprite = null;
      room.whoCanJoinIcon.height = 0;
      final Component<?> var41 = room.whoCanJoinIcon;
      room.isRatedIcon.sprite = null;
      var41.width = 0;
      room.isRatedIcon.height = 0;
      final Component<?> var22 = room.isRatedIcon;
      room.canJoinIcon.sprite = null;
      var22.width = 0;
      final Component<?> var23 = room.canJoinIcon;
      room.canJoinIcon.height = 0;
      room.allowSpectateIcon.sprite = null;
      var23.width = 0;
      final Component<?> var24 = room.allowSpectateIcon;
      room.allowSpectateIcon.height = 0;
      var24.width = 0;
      room.ageLabel.label = null;
      final Component<?> var25 = room.ageLabel;
      room.ageLabel.height = 0;
      room.playersInGameLabel.label = null;
      var25.width = 0;
      room.playersInGameLabel.height = 0;
      final Component<?> var26 = room.playersInGameLabel;
      room.youAreInvitedLabel.label = null;
      var26.width = 0;
      room.youAreInvitedLabel.height = 0;
      final Component<?> var27 = room.youAreInvitedLabel;
      room.askingToJoinLabel.label = null;
      var27.width = 0;
      room.askingToJoinLabel.height = 0;
      final Component<?> var28 = room.askingToJoinLabel;
      var28.width = 0;
      room.cannotJoinReasonLabel.label = null;
      room.cannotJoinReasonLabel.height = 0;
      final Component<?> var29 = room.cannotJoinReasonLabel;
      var29.width = 0;
      room.width = Component.GAME_LIST_SCROLL_PANE.content.width;

      int var30 = 0;
      if (room.isInMap()) {
        if (room.hasStarted) {
          if (room.finalElapsedTime >= 0) {
            room._Ub.label = StringConstants.STATUS_CONCLUDED;
          } else if (room.notInProgress && (room.youCanJoin || room.youAreInvited)) {
            room._Db.label = StringConstants.STATUS_JOIN;
          } else if (room.allowSpectate == 2) {
            room._Db.label = StringConstants.STATUS_SPECTATE;
          } else {
            room._Ub.label = StringConstants.STATUS_PLAYING;
          }
        } else if (room.youCanJoin || room.youAreInvited) {
          room._Db.label = StringConstants.STATUS_JOIN;
        } else if (room.youAreAllowedToJoin) {
          room._Ub.label = StringConstants.STATUS_FULL;
        } else {
          room._Ub.label = StringConstants.STATUS_PRIVATE;
        }

        if (room._Db.label == null) {
          room._Ub.setBounds(0, var30, 68, Component.LABEL_HEIGHT);
        } else {
          room._Db.setBounds(0, var30, 68, Component.LABEL_HEIGHT);
        }

        room._Pb.label = room._Pb.font.truncateWithEllipsisToFit(room.ownerName, 78);
        room._Pb.setBounds(70, var30, 78, Component.LABEL_HEIGHT);
        if (room._Pb.isMouseOverTarget && !room._Pb.label.equals(room.ownerName)) {
          currentTooltip = room.ownerName;
        }

        room._Rb.label = Integer.toString(room.joinedPlayerCount);
        int var32;
        if (room.notInProgress) {
          room._Wb.label = "/" + room.maxPlayerCount;
          room._Rb.textAlignment = Font.HorizontalAlignment.RIGHT;
          var32 = (348 - room._Wb.font.measureLineWidth("/")) / 2;
          room._Rb.setBounds(150, var30, var32 - 150, Component.LABEL_HEIGHT);
          room._Wb.setBounds(var32, var30, -var32 + 198, Component.LABEL_HEIGHT);
        } else {
          room._Rb.textAlignment = Font.HorizontalAlignment.CENTER;
          room._Rb.setBounds(150, var30, 48, Component.LABEL_HEIGHT);
        }

        room._Ib.label = Integer.toString(room.averageRating);
        room._Ib.setBounds(200, var30, 48, Component.LABEL_HEIGHT);
        var32 = 250;
        room._Mb.setBounds(var32, var30, 365 - var32 - 2, Component.LABEL_HEIGHT);
        int var33 = _qob;
        Sprite var34;
        var34 = WHO_CAN_JOIN_OPTION_SPRITES[room.whoCanJoin];
        room.whoCanJoinIcon.sprite = var34;
        room.whoCanJoinIcon.setBounds(var33, 0, var34.offsetX, room._Mb.height);
        var33 += var34.offsetX + _qob;

        final boolean var35 = room.youCanJoin || room.youAreInvited;
        var34 = OPEN_TO_ME_SPRITES[var35 ? 1 : 0];
        room.canJoinIcon.sprite = var34;
        room.canJoinIcon.setBounds(var33, 0, var34.offsetX, room._Mb.height);
        var33 += _qob + var34.offsetX;

        var34 = ALLOW_SPECTATORS_SPRITES[room.allowSpectate - 1];
        room.allowSpectateIcon.sprite = var34;
        room.allowSpectateIcon.setBounds(var33, 0, var34.offsetX, room._Mb.height);
        var33 += _qob + var34.offsetX;

        var34 = RATED_GAME_SPRITES[!room.isRated ? 0 : 1];
        room.isRatedIcon.sprite = var34;
        room.isRatedIcon.setBounds(var33, 0, var34.offsetX, room._Mb.height);
        var33 += _qob + var34.offsetX;

        if (GAMEOPT_SPRITES != null) {
          for (int i = 0; i < ShatteredPlansClient.GAMEOPT_CHOICES_COUNTS.length; ++i) {
            if (GAMEOPT_SPRITES[i] != null && (((1 << (i + _mlj)) & Drawing.WHITE) != 0)) {
              var34 = GAMEOPT_SPRITES[i][room.gameSpecificOptions[i] & 255];
              room.gameOptionIcons[i].sprite = var34;
              room.gameOptionIcons[i].setBounds(var33, 0, var34.offsetX, room._Mb.height);
              var33 += _qob + var34.offsetX;
            }
          }
        }

        final int var43 = (room._Mb.width - var33) / 2;
        if (var43 > 0) {
          room.whoCanJoinIcon.x += var43;
          final Component<?> oc = room.canJoinIcon;
          oc.x += var43;
          final Component<?> ac = room.allowSpectateIcon;
          ac.x += var43;
          Component<?> kb = room.isRatedIcon;
          kb.x += var43;

          for (int var36 = 0; ShatteredPlansClient.GAMEOPT_CHOICES_COUNTS.length > var36; ++var36) {
            kb = room.gameOptionIcons[var36];
            kb.x += var43;
          }
        }

        if (room.hasStarted) {
          int millis = room.finalElapsedTime;
          if (millis < 0) {
            millis = (int) (now - room.startedAt);
          }

          int seconds = millis / 1000;
          int minutes = seconds / 60;
          seconds %= 60;
          if (minutes >= 60) {
            final int hours = minutes / 60;
            minutes %= 60;
            room.ageLabel.label = hours + ":" + minutes / 10 + minutes % 10 + ":" + seconds / 10 + seconds % 10;
          } else {
            room.ageLabel.label = minutes + ":" + seconds / 10 + seconds % 10;
          }
        }

        room.ageLabel.setBounds(365, var30, room.width - 365, Component.LABEL_HEIGHT);
        var30 += Component.LABEL_HEIGHT;
        if (room.joinedPlayerNames != null) {
          var30 += 2;

          final String playersInGame = Arrays.stream(room.joinedPlayerNames, 0, room.joinedPlayerCount)
              .collect(Collectors.joining(", ", StringConstants.PLAYERS_IN_GAME, ""));
          room.playersInGameLabel.label = playersInGame;
          final int lineCount = room.playersInGameLabel.font.breakLines(playersInGame, room.width - 2 * room.playersInGameLabel._kb);
          room.playersInGameLabel.setBounds(0, var30, room.width, Component.LABEL_HEIGHT * lineCount);
          var30 += lineCount * Component.LABEL_HEIGHT;
        }

        if (room.youAreInvited) {
          room.youAreInvitedLabel.label = Strings.format(StringConstants.YOU_ARE_INVITED_TO_XS_GAME, room.ownerName);
          room.youAreInvitedLabel.setBounds(ShatteredPlansClient._tga, var30, room.width - ShatteredPlansClient._tga * 2, Component.LABEL_HEIGHT);
          var30 += Component.LABEL_HEIGHT;
        }

        if (room.submittedJoinRequest) {
          room.askingToJoinLabel.label = Strings.format(StringConstants.ASKING_TO_JOIN_XS_GAME, room.ownerName);
          room.askingToJoinLabel.setBounds(ShatteredPlansClient._tga, var30, -(2 * ShatteredPlansClient._tga) + room.width, Component.LABEL_HEIGHT);
          var30 += Component.LABEL_HEIGHT;
        }
      }

      final String message = removalReasonMessage(room.status, room.ownerName);
      if (message != null) {
        final int var33 = room.cannotJoinReasonLabel.font.breakLines(message, -ShatteredPlansClient._tga - ShatteredPlansClient._tga + room.width);
        room.cannotJoinReasonLabel.label = message;
        room.cannotJoinReasonLabel._I = 256 * room.statusTimer / ShatteredPlansClient.STATUS_MESSAGE_TIMEOUT_DURATION;
        room.cannotJoinReasonLabel.setBounds(ShatteredPlansClient._tga, var30, room.width - ShatteredPlansClient._tga * 2, Component.LABEL_HEIGHT * var33);
        var30 += var33 * Component.LABEL_HEIGHT;
      }

      if (!var6) {
        room._gb = var30 - room.height;
      }

      if (var12) {
        Component.GAME_LIST_SCROLL_PANE.content.placeAfter(null, room);
      }

      for (int i = 0; i < ShatteredPlansClient.GAMEOPT_CHOICES_COUNTS.length; ++i) {
        if (room.gameOptionIcons[i].isMouseOverTarget) {
          final String tooltip = ShatteredPlansClient.GAMEOPT_TOOLTIPS == null ? null
              : ShatteredPlansClient.GAMEOPT_TOOLTIPS[i] == null ? null
              : ShatteredPlansClient.GAMEOPT_TOOLTIPS[i][room.gameSpecificOptions[i] & 255];
          if (tooltip == null) {
            currentTooltip = StringConstants.GAMEOPT_LABELS[i];
          } else {
            currentTooltip = StringConstants.GAMEOPT_LABELS[i] + " - " + tooltip;
          }
        }
      }

      if (room.whoCanJoinIcon.isMouseOverTarget) {
        final String tooltip;
        if (room.whoCanJoin == 1) {
          tooltip = StringConstants.RUNESCAPE_CLAN;
        } else {
          tooltip = WHO_CAN_JOIN_OPTION_TOOLTIPS[room.whoCanJoin];
        }

        currentTooltip = StringConstants.WHO_CAN_JOIN + " - " + tooltip;
      }

      if (room.isRatedIcon.isMouseOverTarget) {
        currentTooltip = room.isRated ? StringConstants.RATED_GAME : StringConstants.UNRATED_GAME;
      }

      if (room.canJoinIcon.isMouseOverTarget) {
        currentTooltip = room.youCanJoin || room.youAreInvited ? StringConstants.YOU_CAN_JOIN
            : room.notInProgress ? StringConstants.YOU_CAN_ASK_TO_JOIN
            : StringConstants.YOU_CANNOT_JOIN_IN_PROGRESS;
      }

      if (room.allowSpectateIcon.isMouseOverTarget) {
        currentTooltip = room.allowSpectate != 2 ? StringConstants.YOU_CAN_NOT_SPECTATE : StringConstants.YOU_CAN_SPECTATE;
      }

      if (room.clickButton != MouseState.Button.NONE && room.isInMap()) {
        if (room._Db.clickButton == MouseState.Button.NONE) {
          a458aj(room, room.ownerName);
        } else if (!room.hasStarted || (room.notInProgress && (room.youCanJoin || room.youAreInvited))) {
          C2SPacket.requestToJoinRoom(room.roomId);
        } else {
          C2SPacket.spectateGame(room.roomId);
        }
      }
    }
  }

  private static void a458aj(final ClientLobbyRoom var1, final String var2) {
    ShatteredPlansClient.showContextMenu(Component.GAME_LIST_SCROLL_PANE, var1, 0L, null, null, var1.roomId, null);
    if (var1.hasStarted && (var1.allowSpectate == 2 || JagexApplet.adminLevel >= 2)) {
      ContextMenu.openInstance.view.addItem(Strings.format(StringConstants.SPECTATE_XS_GAME, var2), ContextMenu.ClickAction.SPECTATE_GAME);
    }

    ContextMenu.openInstance.a487();
    if (ContextMenu.openInstance.roomId == ContextMenu.roomShowingPlayers) {
      ContextMenu.openInstance.view.addItem(Strings.format(StringConstants.HIDE_PLAYERS_IN_XS_GAME, var2), ContextMenu.ClickAction.HIDE_PLAYERS_IN_GAME);
    } else {
      ContextMenu.openInstance.view.addItem(Strings.format(StringConstants.SHOW_PLAYERS_IN_XS_GAME, var2), ContextMenu.ClickAction.SHOW_PLAYERS_IN_GAME);
    }

    ContextMenu.openInstance.view.positionRelativeToTarget(JagexApplet.mousePressX, JagexApplet.mousePressY);
  }

  public static ClientLobbyRoom lookup(final int roomId) {
    return roomsMap == null ? null : roomsMap.get(roomId);
  }

  public static void add(final ClientLobbyRoom room) {
    rooms.remove(room);
    CollectionUtil.insertSorted(rooms, room, ClientLobbyRoom::lessThan);
  }

  public boolean isInMap() {
    return roomsMap.containsKey(this.roomId);
  }

  public void initializeFromServer(@SuppressWarnings("SameParameterValue") final Buffer packet, final boolean includesJoinedPlayerCount) {
    if (includesJoinedPlayerCount) {
      this.joinedPlayerCount = packet.readUByte();
    }
    this.maxPlayerCount = packet.readUByte();
    this.whoCanJoin = packet.readUByte();
    final int var4 = packet.readUByte();
    final boolean includesJoinedPlayerNames = (var4 & 2) != 0;
    final boolean concluded = (var4 & 4) != 0;
    this.notInProgress = (var4 & 8) != 0;
    this.allowSpectate = (var4 & 16) == 0 ? 1 : 2;
    this.isRated = (var4 & 32) != 0;
    this.hasStarted = (var4 & 64) != 0;
    this.youAreAllowedToJoin = (var4 & 128) != 0;
    this.youCanJoin = this.youAreAllowedToJoin && this.joinedPlayerCount < this.maxPlayerCount;
    packet.readBytes(this.gameSpecificOptions, this.gameSpecificOptions.length);
    this.averageRating = packet.readUShort();
    this.startedAt = PseudoMonotonicClock.currentTimeMillis() - (long) packet.readInt();
    if (concluded) {
      this.finalElapsedTime = packet.readInt();
    } else {
      this.finalElapsedTime = -1;
    }

    this.ownerId = packet.readLong();
    final int var7 = packet.pos;
    this.ownerName = packet.readNullTerminatedString();
    if (includesJoinedPlayerNames) {
      packet.pos = var7;
      this.joinedPlayerNames = new String[this.joinedPlayerCount];
      for (int i = 0; i < this.joinedPlayerCount; ++i) {
        this.joinedPlayerNames[i] = packet.readNullTerminatedString();
      }
    } else {
      this.joinedPlayerNames = null;
    }
  }

  private boolean lessThan(final ClientLobbyRoom var1) {
    final boolean var3 = this.youAreInvited || this.status == Status.ROOM_FULL;
    final boolean var4 = var1.youAreInvited || var1.status == Status.ROOM_FULL;
    if (var3 == var4) {
      if (!var3) {
        if (this.youCanJoin != var1.youCanJoin) {
          return this.youCanJoin;
        }

        if (this.youCanJoin && this.whoCanJoin != var1.whoCanJoin) {
          return this.whoCanJoin < var1.whoCanJoin;
        }
      }

      if (this.hasStarted == var1.hasStarted) {
        if (this.hasStarted) {
          final boolean var5 = this.notInProgress || this.allowSpectate == 2;
          final boolean var6 = var1.notInProgress || var1.allowSpectate == 2;
          if (var5 == var6) {
            return this.startedAt > var1.startedAt;
          } else {
            return var5;
          }
        } else {
          return this.startedAt < var1.startedAt;
        }
      } else {
        return !this.hasStarted;
      }
    } else {
      return var3;
    }
  }

  @SuppressWarnings("WeakerAccess")
  public static final class Status {
    public static final int NONE = 0;
    public static final int ENTERED_OTHER_GAME = 3;
    public static final int ROOM_FULL = 6;
    public static final int GAME_HAS_STARTED = 7;
    public static final int YOU_DECLINED_INVITE = 8;
    public static final int INVITE_WITHDRAWN = 9;
    public static final int REQUEST_DECLINED = 10;
    public static final int REQUEST_WITHDRAWN = 11;
    public static final int ALL_PLAYERS_HAVE_LEFT = 14;
  }
}
