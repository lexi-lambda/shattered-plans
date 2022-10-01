package funorb.client.lobby;

import funorb.awt.KeyState;
import funorb.awt.MouseState;
import funorb.graphics.Font;
import funorb.graphics.Sprite;
import funorb.shatteredplans.C2SPacket;
import funorb.shatteredplans.StringConstants;
import funorb.shatteredplans.client.JagexApplet;
import funorb.shatteredplans.client.Menu;
import funorb.shatteredplans.client.ShatteredPlansClient;
import funorb.util.PseudoMonotonicClock;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;

public final class vm_ extends Component<Component<?>> {
  public static String _ahS;
  public static long _aco;
  public static Sprite _kflc;
  private final int _xb;
  private final int _Qb;
  private final ActionButton[] _Kb;
  private final int _Pb;
  private final char[] _zb;
  private final int[] _Hb;
  private final vm_[] _yb;
  private int _Eb;
  private vm_ _Ob;
  private int _Mb;
  private int _Ib;
  private int _Db;
  private int _Gb = -1;

  public vm_(final Component<?> var3, final Component<?> var4, final Component<?> var5, final vm_[] var6, final int[] var7, final String[] var8, final char[] var9) {
    super(var3);
    this._Hb = var7;
    this._zb = var9;
    this._yb = var6;
    this._Pb = this._Hb.length;
    final Font var10 = var5.font;
    this._Qb = var10.descent + 2 + var10.ascent;
    this._xb = this._Qb * this._Pb;
    this._Kb = new ActionButton[this._Pb];
    this._Ib = 0;
    final String var11 = "<col=999999>";
    final String var12 = "</col>";

    for (int var13 = 0; this._Pb > var13; ++var13) {
      if (this._zb[var13] > 0) {
        var8[var13] = var11 + String.valueOf(this._zb[var13]).toUpperCase() + ": " + var12 + var8[var13];
      }

      Sprite var14 = null;
      if (this._yb[var13] != null || this._Hb[var13] == -1) {
        var14 = _kflc;
      }

      this._Kb[var13] = new ActionButton(var4, var5, var14, var8[var13]);
      this.addChild(this._Kb[var13]);
      final int var15 = var10.measureLineWidth(var8[var13]);
      if (this._Ib < var15) {
        this._Ib = var15;
      }
    }

    this._Ib += _kflc.offsetX + 10;
    this.c540(12);
  }

  public static int a827(final String var0) {
    return Menu.FONT.measureLineWidth(var0);
  }

  private static void a345un(final String var0, final long var1, final @NotNull ChatMessage.Channel var3, final int var4) {
    ShatteredPlansClient._cvcn = true;
    ShatteredPlansClient._vsd = var4;
    _aco = var1;
    _ahS = var0;
    ShatteredPlansClient._tlr = var3;
  }

  private static void a150gs() {
    ShatteredPlansClient._tkz = PseudoMonotonicClock.currentTimeMillis();
    ShatteredPlansClient._vjC = 0;
  }

  public void close() {
    for (final ActionButton button : this._Kb) {
      button.clickButton = MouseState.Button.NONE;
      button.selected = false;
    }

    if (this._Ob != null) {
      this._Ob.close();
      _uaf.children.remove(this._Ob);
    }

    this._Ob = null;
    this._Gb = -1;
    this.c540(12);
  }

  public void a669(final int var2, final int var3, final int var4, final int var5, final int var6) {
    this._Eb = this._Ib + var2 * 2;
    this.setBounds(var4, var6 - this._xb, this._Eb, this._xb);
    if (this._Mb != var3) {
      this._Mb = var3;
      this.c540(this._Db);
    }

    int var7;
    for (var7 = 0; this._Pb > var7; ++var7) {
      this._Kb[var7].a370(this._Qb, this._Kb[var7].y, var2, var5, this._Eb, 0);
    }

    if (this._Gb != -1 && this._yb[this._Gb] != null) {
      var7 = this._yb[this._Gb]._Pb;

      int var8 = this._Qb * (var7 + this._Gb) + this.y;
      while (var6 < var8) {
        var8 -= this._Qb;
      }

      this._yb[this._Gb].a669(var2, this._Kb[this._Gb].y2, var4 + this._Eb, var5, var8);
    }

  }

  public int g474() {
    return this._Eb + (this._Ob != null ? this._Ob.g474() : 0);
  }

  public void a599() {
    for (int var4 = 0; this._Kb.length > var4; ++var4) {
      final ActionButton var5 = this._Kb[var4];
      if (var5.clickButton == MouseState.Button.LEFT) {
        this.a115(var4);
        var5.selected = this._Gb == var4;
      }
    }

    if (this._Gb != -1) {
      final vm_ var6 = this._yb[this._Gb];
      if (var6 != null) {
        var6.a599();
      }
    }

    if (this._Db > 0) {
      this.c540(this._Db - 1);
    }

  }

  private void a115(final int var3) {
    if (this._Gb == var3) {
      this.close();
      this.c540(0);
    } else if (this._yb[var3] != null) {
      this.close();
      this.c540(0);
      this._Gb = var3;
      this._Ob = this._yb[this._Gb];
      _uaf.addChild(this._Ob);
      this._Ob.c540(12);
    } else if (this._Hb[var3] == -1) {
      QuickChatHelpPanel.openInstance = new QuickChatHelpPanel(Component.TAB_ACTIVE, CLOSE_BUTTON, Component.LABEL);
      ShatteredPlansClient.closeQuickChat();
    } else {
      final int var5 = this._Hb[var3] | 0x8000;
      ChatMessage.Channel var6 = ShatteredPlansClient.currentChatChannel;
      if (var6 == ChatMessage.Channel.LOBBY && ShatteredPlansClient.unratedLobbyRoom != null) {
        var6 = ChatMessage.Channel.ROOM;
      }

      if (ContextMenu.getChatChannelFilter(var6) == ChatMessage.FilterLevel.NONE) {
        ContextMenu.setChatFilter(var6, ChatMessage.FilterLevel.FRIENDS);
      }

      C2SPacket.sendQuickChatMessage(ShatteredPlansClient.currentChatChannel, ContextMenu.recipientPlayerName, var5);
      a345un(ContextMenu.recipientPlayerName, ContextMenu.recipientPlayerId, ShatteredPlansClient.currentChatChannel, var5);
      ShatteredPlansClient.closeQuickChat();
      a150gs();
    }

  }

  private void c540(final int var2) {
    this._Db = var2;
    for (int var5 = 0; this._Pb > var5; ++var5) {
      final int var3 = this._Qb * var5;
      final int var4 = this._Db * this._Db;
      this._Kb[var5].y = ((-this.y2 + this._Mb) * var4 + var3 * (144 - var4)) / 144;
    }
  }

  public boolean f491() {
    boolean var2 = Arrays.stream(this._Kb).anyMatch(var5 -> var5.clickButton != MouseState.Button.NONE);

    if (!var2 && this._Gb != -1 && this._yb[this._Gb] != null) {
      var2 = this._yb[this._Gb].f491();
    }

    return var2;
  }

  public boolean a777() {
    final boolean var4 = JagexApplet.lastTypedKeyCode == KeyState.Code.BACKSPACE;
    if (this._Ob != null) {
      if (var4 && this._Ob._Gb == -1) {
        this.close();
        this.c540(0);
        return true;
      } else {
        return this._Ob.a777();
      }
    } else if (this == ShatteredPlansClient._dmrh && var4) {
      ShatteredPlansClient.closeQuickChat();
      return true;
    } else {
      char var5 = JagexApplet.lastTypedKeyChar;
      if (var5 > 0) {
        if (var5 == StringConstants.KEYCHAR_THE_CHARACTER_UNDER_QUESTION_MARK) {
          var5 = '?';
        }

        for (int var6 = 0; var6 < this._zb.length; ++var6) {
          if (var5 == this._zb[var6]) {
            this.a115(var6);
            return true;
          }
        }
      }

      return false;
    }
  }
}
