package funorb.client.lobby;

import funorb.awt.KeyState;
import funorb.awt.MouseState;
import funorb.graphics.Font;
import funorb.shatteredplans.C2SPacket;
import funorb.shatteredplans.StringConstants;
import funorb.shatteredplans.client.JagexApplet;
import funorb.shatteredplans.client.ShatteredPlansClient;

public final class ReportAbuseDialog extends Component<Component<?>> {
  public static Component<?> _Nb;
  public static String _Kb = "ESC - cancel private message";
  public static ReportAbuseDialog openInstance;
  public static String[] RULE_STRINGS;
  private final long _Gb;
  private final Component<?> _Ob;
  private Component<?> _Bb;
  private Component<?>[] _Hb;
  private int _yb = -2;
  private Checkbox _zb;
  private StringBuilder _xb;

  public ReportAbuseDialog(final int var1, final int var2, final int var3, final int var4, final Component<?> var7, final Component<?> var8, final Component<?> var9, final Checkbox var10, final Component<?> var11, final String var12, final long var13) {
    super(null);
    this._Gb = var13;
    final Component<Component<?>> _Jb = new Component<>(var7, StringConstants.RA_TITLE.toUpperCase());
    _Jb.textAlignment = Font.HorizontalAlignment.CENTER;
    this.addChild(_Jb);
    this._Ob = new Component<>(var8);
    _Jb.addChild(this._Ob);
    final Component<Component<?>> _Cb = new Component<>(null);
    this.addChild(_Cb);
    int var16;
    int var17;
    int var18;
    int var19;
    int var20;
    int var31;
    final Component<?> _Ab;
    if (var12 == null) {
      _Ab = new Component<>(var9, StringConstants.RA_INTRO_NO_NAME);
      _Ab.textAlignment = Font.HorizontalAlignment.CENTER;
      _Ab.textColor = 11184810;
      _Cb.addChild(_Ab);
      final short var27 = 226;
      final byte var30 = 10;
      var17 = _Ab.font.breakLines(_Ab.label, var27);
      _Ab.setBounds(13, var30, var27, Component.LABEL_HEIGHT * var17);
      var16 = var30 + Component.LABEL_HEIGHT * var17;
      _Cb.setBounds(0, 24, 252, var16 + 10);
      _Cb.nineSliceSprites = createGradientOutlineSprites(_Cb.height, 11579568, 8421504, 2105376);
      var18 = 252;
      var19 = 34 + var16;
      var20 = PopupMenu.positionPopupX(var1, var3, var18);
      var31 = PopupMenu.positionPopupY(var2, var4, var19);
      this.setBounds(var20, var31, var18, var19);
    } else {
      _Ab = new Component<>(var9, StringConstants.RA_INTRO);
      _Ab.textAlignment = Font.HorizontalAlignment.CENTER;
      _Ab.textColor = 11184810;
      _Cb.addChild(_Ab);
      final Component<?> _Fb = new Component<>(var9, StringConstants.RA_EXPLANATION);
      _Fb.textAlignment = Font.HorizontalAlignment.CENTER;
      _Fb.textColor = 11184810;
      _Cb.addChild(_Fb);
      this._Bb = new Component<>(var9);
      this._Bb.textColor = 16764006;
      _Cb.addChild(this._Bb);
      this._Bb._u = "|";
      if (JagexApplet.modLevel >= 5 || JagexApplet.adminLevel >= 2) {
        this._zb = new Checkbox(var10, JagexApplet.modLevel < 7 && JagexApplet.adminLevel < 2 ? StringConstants.RA_SUGGEST_MUTE : StringConstants.RA_MUTE_THIS_PLAYER);
        _Cb.addChild(this._zb);
      }

      final Component<Component<?>> qr1 = new Component<>(null);
      final Component<Component<?>> qr2 = new Component<>(null);
      final Component<Component<?>> qr3 = new Component<>(null);
      _Cb.addChild(qr1);
      _Cb.addChild(qr2);
      _Cb.addChild(qr3);
      this._Hb = new Component[RULE_STRINGS.length];

      for (var16 = 0; var16 < RULE_STRINGS.length; ++var16) {
        if (RULE_STRINGS[var16] != null) {
          this._Hb[var16] = new Component<>(var11, RULE_STRINGS[var16]);
          this._Hb[var16].textAlignment = Font.HorizontalAlignment.LEFT;
          this._Hb[var16].enabled = true;
          _Cb.addChild(this._Hb[var16]);
        }
      }

      this._xb = new StringBuilder(12);
      this._xb.append(var12);

      var16 = 0;
      var17 = var7.font.measureLineWidth(StringConstants.RULE_PILLAR_0);
      if (var17 > var16) {
        var16 = var17;
      }

      var17 = var7.font.measureLineWidth(StringConstants.RULE_PILLAR_1);
      if (var17 > var16) {
        var16 = var17;
      }

      var17 = var7.font.measureLineWidth(StringConstants.RULE_PILLAR_2);
      if (var17 > var16) {
        var16 = var17;
      }

      for (var17 = 0; var17 < RULE_STRINGS.length; ++var17) {
        if (this._Hb[var17] != null) {
          var18 = this._Hb[var17].e474();
          if (var16 < var18) {
            var16 = var18;
          }
        }
      }

      if (var16 > 140) {
        var16 = 140;
      }

      var17 = 0;
      this.a315(var7, qr1, StringConstants.RULE_PILLAR_0, var16);
      var18 = this.a082(qr1, this._Hb[6], 32, var16);
      var18 = this.a082(qr1, this._Hb[9], var18, var16);
      var18 = this.a082(qr1, this._Hb[5], var18, var16);
      var18 = this.a082(qr1, this._Hb[7], var18, var16);
      var18 = this.a082(qr1, this._Hb[15], var18, var16);
      var18 = this.a082(qr1, this._Hb[4], var18, var16);
      if (var17 < var18) {
        var17 = var18;
      }

      this.a315(var7, qr2, StringConstants.RULE_PILLAR_1, var16);
      var18 = this.a082(qr2, this._Hb[16], 32, var16);
      var18 = this.a082(qr2, this._Hb[17], var18, var16);
      var18 = this.a082(qr2, this._Hb[18], var18, var16);
      var18 = this.a082(qr2, this._Hb[19], var18, var16);
      var18 = this.a082(qr2, this._Hb[20], var18, var16);
      if (var18 > var17) {
        var17 = var18;
      }

      this.a315(var7, qr3, StringConstants.RULE_PILLAR_2, var16);
      var18 = this.a082(qr3, this._Hb[13], 32, var16);
      var18 = this.a082(qr3, this._Hb[21], var18, var16);
      var18 = this.a082(qr3, this._Hb[11], var18, var16);
      if (var18 > var17) {
        var17 = var18;
      }

      var19 = 3 * var16 + 26;
      var20 = _Jb.e474();
      if (var20 > var19) {
        var19 = var20;
      }

      if (this._zb != null) {
        var20 = this._zb.c080();
        if (var19 < var20) {
          var19 = var20;
        }
      }

      _Jb.setBounds(0, 0, var19 + 26, 24);
      this._Ob.setBounds(_Jb.width - 20, 5, 15, 15);
      final byte var21 = 10;
      _Ab.setBounds(13, var21, var19, Component.LABEL_HEIGHT * 2);
      var31 = var21 + Component.LABEL_HEIGHT * 2;
      _Fb.setBounds(13, var31, var19, Component.LABEL_HEIGHT * 2);
      var31 += Component.LABEL_HEIGHT * 2 + 10;
      this._Bb.setBounds(0, var31, 0, Component.LABEL_HEIGHT);
      var31 += Component.LABEL_HEIGHT + 10;
      if (this._zb != null) {
        var20 = this._zb.c080();
        this._zb.a669(var20, 13 + (var19 - var20) / 2, var31);
        var31 += Component.LABEL_HEIGHT + 10;
      }

      qr1.setBounds(13, var31, var16, var17);
      qr2.setBounds(13 + var16 + 13, var31, var16, var17);
      qr3.setBounds(39 + var16 * 2, var31, var16, var17);
      _Cb.setBounds(0, 24, 13 + var19 + 13, var17 + var31 + 10);
      _Cb.nineSliceSprites = createGradientOutlineSprites(_Cb.height, 11579568, 8421504, 2105376);
      final int var23 = 13 + 13 + var19;
      final int var24 = var17 + 24 + var31 + 10;
      final int var25 = PopupMenu.positionPopupX(var1, var3, var23);
      final int var26 = PopupMenu.positionPopupY(var2, var4, var24);
      this.setBounds(var25, var26, var23, var24);
    }

  }

  public static void tick(final boolean mouseNotYetHandled) {
    final int var3;
    if (openInstance != null) {
      var3 = openInstance.getAction(mouseNotYetHandled);
      if (var3 != -2) {
        if (var3 != -1) {
          final boolean var4 = openInstance.i154();
          C2SPacket.reportAbuse(openInstance._Gb, var4, openInstance.g983(), var3);
        }

        openInstance = null;
        ShatteredPlansClient.dismissContextMenu();
      }
    }
  }

  private void a315(final Component<?> var1, final Component<Component<?>> var2, final String var3, final int var4) {
    var2.addChild(new Component<>(var1, var4, 24, var3));
  }

  private int a082(final Component<Component<?>> var2, final Component<?> var3, int var4, final int var5) {
    var4 += 8;
    final int var6 = var3.font.measureParagraphHeight(var3.label, var5 - 2 * var3._kb, var3._Y);
    var3.setBounds(0, var4, var5, var6);
    var4 += var6;
    var2.addChild(var3);
    return var4;
  }

  public boolean h154() {
    if (this._yb == -2) {
      if (JagexApplet.lastTypedKeyCode == KeyState.Code.ESCAPE) {
        this._yb = -1;
      }

      return true;
    } else {
      return false;
    }
  }

  private String g983() {
    return this._xb.toString();
  }

  private int getAction(final boolean var1) {
    this.rootProcessMouseEvents(var1);
    if (this._Bb != null) {
      this._Bb.label = this._xb.toString();
      this._Bb.x = (this.width - this._Bb.font.measureLineWidth(this._Bb.label)) / 2;
      if (this._zb != null && this._zb.clickButton != MouseState.Button.NONE) {
        this._zb.selected = !this._zb.selected;
      }

      this._Bb.width = this.width - this._Bb.x;

      for (int var3 = 0; var3 < RULE_STRINGS.length; ++var3) {
        if (this._Hb[var3] != null) {
          this._Hb[var3].enabled = this._xb.length() > 0;
          if (this._Hb[var3].enabled && this._Hb[var3].clickButton != MouseState.Button.NONE) {
            return var3;
          }
        }
      }
    }

    if (this._Ob.clickButton == MouseState.Button.NONE) {
      return var1 && JagexApplet.mouseButtonJustClicked != MouseState.Button.NONE && this.clickButton == MouseState.Button.NONE ? -1 : this._yb;
    } else {
      return -1;
    }
  }

  private boolean i154() {
    return this._zb != null && this._zb.selected;
  }
}
