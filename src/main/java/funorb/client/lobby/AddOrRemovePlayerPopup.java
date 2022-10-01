package funorb.client.lobby;

import funorb.Strings;
import funorb.awt.KeyState;
import funorb.awt.MouseState;
import funorb.shatteredplans.client.JagexApplet;
import org.intellij.lang.annotations.MagicConstant;

public final class AddOrRemovePlayerPopup extends Component<Component<?>> {
  public static AddOrRemovePlayerPopup openInstance;
  @MagicConstant(valuesFromClass = Action.class)
  public static int action = Action.NONE;
  private final Component<?> _zb;
  private final StringBuilder _Db;
  private int _Ab = 0;

  public AddOrRemovePlayerPopup(final int x, final int y, final int var3, final int var4, final String labelText, final Component<?> attrsSrc, final Component<?> labelAttrsSrc, final Component<?> var8) {
    super(attrsSrc);
    final Component<?> label = new Component<>(labelAttrsSrc, labelText);
    this.addChild(label);
    this._zb = new Component<>(var8);
    this._zb.textColor = 16764006;
    this.addChild(this._zb);
    this._zb._u = "|";
    this._Db = new StringBuilder(12);
    final int var9 = label.e474();
    label.setBounds(5, 3, var9, Component.LABEL_HEIGHT);
    this._zb.setBounds(5, Component.LABEL_HEIGHT + 3, var9, Component.LABEL_HEIGHT);
    final int var10 = 5 + var9 + 5;
    final int var11 = 3 + Component.LABEL_HEIGHT * 2 + 3;
    final int var12 = PopupMenu.positionPopupX(x, var3, var10);
    final int var13 = PopupMenu.positionPopupY(y, var4, var11);
    this.setBounds(var12, var13, var10, var11);
  }

  private static void setStringBuilderLengthAndFillWithSpaces(final StringBuilder sb, final int newLen) {
    final int oldLen = sb.length();
    sb.setLength(newLen);

    for (int i = oldLen; i < newLen; ++i) {
      sb.setCharAt(i, ' ');
    }
  }

  public static void removeLastCharacterFromStringBuilder(final StringBuilder sb) {
    setStringBuilderLengthAndFillWithSpaces(sb, sb.length() - 1);
  }

  private static boolean a412tmg(final char var0, final CharSequence var1) {
    if (Strings.isNormalizable(var0)) {

      if (var1 == null) {
        return false;
      } else {
        final int var3 = var1.length();
        if (var3 >= 12) {
          return false;
        } else {
          return !Strings.isSpaceLikeUsernameChar(var0) || var3 != 0;
        }
      }
    } else {
      return false;
    }
  }

  public static void tick(final boolean mouseNotYetHandled) {
    if (openInstance != null) {
      final int var3 = openInstance.getAction(mouseNotYetHandled);
      if (var3 != 0) {
        if (var3 == 2 && openInstance.label != null && !openInstance.label.equals("")) {
          final String var4;
          if (openInstance.label.charAt(0) == '[') {
            var4 = openInstance.label;
          } else {
            var4 = Strings.normalize(openInstance.label);
          }

          String var5 = null;
          if (action == Action.ADD_FRIEND) {
            var5 = ContextMenu.addFriend(var4);
          }

          if (action == Action.REMOVE_FRIEND) {
            var5 = PlayerListEntry.removeFriend(var4);
          }

          if (action == Action.ADD_IGNORE) {
            var5 = ContextMenu.addIgnore(var4);
          }

          if (action == Action.REMOVE_IGNORE) {
            var5 = PlayerListEntry.removeIgnore(var4, var4);
          }

          if (var5 != null) {
            ContextMenu.showChatMessage(ChatMessage.Channel.PRIVATE, var5, 0, var4, null);
          }
        }

        action = Action.NONE;
        openInstance = null;
      }
    }
  }

  public boolean f427() {
    if (this._Ab == 0) {
      if (JagexApplet.lastTypedKeyCode == KeyState.Code.BACKSPACE && this._Db.length() > 0) {
        removeLastCharacterFromStringBuilder(this._Db);
      }

      if (this._Db.length() < 12) {
        char var2 = Character.toLowerCase(JagexApplet.lastTypedKeyChar);
        if (var2 == ' ') {
          var2 = '_';
        }

        if (var2 == '_' && this._Db.length() > 0) {
          this._Db.append('_');
        }

        if (Strings.isAlpha(var2) || Strings.isDigit(var2)) {
          this._Db.append(var2);
        }
      }

      if (JagexApplet.lastTypedKeyCode == KeyState.Code.ENTER) {
        if (this._Db.length() <= 0) {
          this._Ab = 1;
        } else {
          this.label = this._Db.toString();
          this._Ab = 2;
        }
      }

      if (JagexApplet.lastTypedKeyCode == KeyState.Code.ESCAPE) {
        this._Ab = 1;
      }

      return true;
    } else {
      return false;
    }
  }

  private int getAction(final boolean mouseNotYetHandled) {
    this.rootProcessMouseEvents(mouseNotYetHandled);
    if (mouseNotYetHandled) {
      while (JagexApplet.nextTypedKey()) {
        if (JagexApplet.lastTypedKeyCode == KeyState.Code.BACKSPACE && this._Db.length() > 0) {
          removeLastCharacterFromStringBuilder(this._Db);
        }

        if (a412tmg(JagexApplet.lastTypedKeyChar, this._Db) || this._Db.length() == 0 && JagexApplet.lastTypedKeyChar == '[' || this._Db.length() == 1 && JagexApplet.lastTypedKeyChar == '#' || JagexApplet.lastTypedKeyChar == ']') {
          this._Db.append(JagexApplet.lastTypedKeyChar);
        }

        if (JagexApplet.lastTypedKeyCode == KeyState.Code.ENTER) {
          if (this._Db.length() <= 0) {
            return 1;
          }

          this.label = this._Db.toString();
          return 2;
        }

        if (JagexApplet.lastTypedKeyCode == KeyState.Code.ESCAPE) {
          return 1;
        }
      }
    }

    this._zb.label = this._Db.toString();
    if (mouseNotYetHandled && JagexApplet.mouseButtonJustClicked != MouseState.Button.NONE && this.clickButton == MouseState.Button.NONE) {
      this._Ab = 1;
    }

    return this._Ab;
  }

  @SuppressWarnings("WeakerAccess")
  public static final class Action {
    public static final int NONE = -1;
    public static final int ADD_FRIEND = 0;
    public static final int REMOVE_FRIEND = 1;
    public static final int ADD_IGNORE = 2;
    public static final int REMOVE_IGNORE = 3;
  }
}
