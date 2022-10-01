package funorb.shatteredplans.client.game;

import funorb.Strings;
import funorb.graphics.ArgbSprite;
import funorb.graphics.Drawing;
import funorb.graphics.Point;
import funorb.graphics.Rect;
import funorb.graphics.Sprite;
import funorb.graphics.vector.VectorDrawing;
import funorb.shatteredplans.StringConstants;
import funorb.shatteredplans.client.CombatEngagementAnimationState;
import funorb.shatteredplans.client.GameUI;
import funorb.shatteredplans.client.Menu;
import funorb.shatteredplans.client.ShatteredPlansClient;
import funorb.shatteredplans.client.Sounds;
import funorb.shatteredplans.game.GameState;
import funorb.shatteredplans.game.GameState.ResourceType;
import funorb.shatteredplans.game.MoveFleetsOrder;
import funorb.shatteredplans.game.Player;
import funorb.shatteredplans.game.ProjectOrder;
import funorb.shatteredplans.map.Map;
import funorb.shatteredplans.map.StarSystem;
import funorb.shatteredplans.map.TannhauserLink;
import funorb.util.ArrayUtil;
import funorb.util.MathUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

public final class GameView extends AbstractGameView {
  public static final int[] RESOURCE_COLORS = new int[]{0x7fbfff, 0x7fdf2f, 0xff7f00, 0xff00ff};
  public static final double COS_THIRTY_DEGREES = Math.cos(0.5235987755982988D); // sqrt(3)/2
  private static final int[] _rj = new int[]{-65536, -65536, -65536, 0, -65536};
  private static final int TICKS_PER_ANIMATION_PHASE = 200;
  public static Sprite[] DEFNET_ANIM_MID;
  public static Sprite[] DEFNET_ANIM_LARGE;
  private static int _ch;
  private static int _ce;
  private static int _ca;
  public static int uiPulseCounter;
  public static Sprite ARROW_SHIP;
  public static Sprite ARROW_SHIP_DAMAGED;
  private static int[] _enb;
  public static Sprite COMBAT_HEX_WHITE;
  public static Sprite SHIELD;
  public static Sprite DEFENSE_GRID;
  public static Sprite CHEVRON;
  public static Sprite WARNING;
  public static Sprite HAMMER;
  public static Sprite FLEETS_ARROW_SHIP;
  public static Sprite[] FLEET_BUTTONS;
  public static Sprite[] RES_SIDES;
  public static Sprite[] RES_LOWS;
  public static ArgbSprite _cos;
  public static Sprite[] DEFNET_ANIM_SMALL;
  public static Sprite[] SYSTEM_ICONS;
  private static int[] _aib;
  private static int _cd;
  private static int _cc;
  private static int _cb2;
  private static int _cg;
  private static int[] _cf;
  public final int[] _nb = new int[]{-1, -1};
  private final boolean isTutorial;
  private final int[] _tb = new int[]{-1, -1};
  private final ArgbSprite[][] resourceLightSprites;
  private final int[] _ub = new int[]{-1, -1};
  public boolean _Bb;
  public boolean _Gb;
  public boolean _Ab;
  public MoveFleetsOrder _rb;
  private Sprite[] _Mb;
  private ArgbSprite _K;
  private double _wb;
  private ArgbSprite _ob;
  private Sprite[] _Kb;
  private int _yb = 0;
  private ArgbSprite _Cb;
  private double _Nb = -1.0;
  private int _Hb;
  private double zoomFactor;
  private MoveFleetsOrder _Db;
  private int[][] _Qb;
  private ArgbSprite _Ib;
  private Sprite[] _xb;
  private ArgbSprite _Ob;

  public GameView(final Map var1, final Player[] var2, final Player localPlayer, final boolean isTutorial) {
    super(localPlayer);
    this._ib = var2;
    this.map = var1;
    this.isTutorial = isTutorial;
    this._cb = new int[this._ib.length][];
    this.j150();
    this.mapScrollPosnX = (float) (this.map.drawingWidth / 2);
    this.mapScrollPosnY = (float) (this.map.drawingHeight / 2);
    this.unitScalingFactor = 300.0F;
    this.maxUnitScalingFactor = (float) (this.map.drawingWidth * 300 / 450);

    for (int i = 0; i < GameState.NUM_RESOURCES; ++i) {
      this.projectHighlightColors[i] = new int[256];
    }
    for (int i = 0; i < 0x100; ++i) {
      this.projectHighlightColors[ResourceType.METAL  ][i] = ((i & 0xfe) << 7) | i;
      this.projectHighlightColors[ResourceType.BIOMASS][i] = i << 8;
      this.projectHighlightColors[ResourceType.ENERGY ][i] = i << 8 | i << 16;
      this.projectHighlightColors[ResourceType.EXOTICS][i] = i << 16 | i;
    }

    this.highlightedSystems = new SystemHighlight[this.map.systems.length];
    Arrays.fill(this.highlightedSystems, SystemHighlight.NONE);
    this.fleetMovements = new ArrayList<>();
    this.combatEngagements = new ArrayList<>();
    this.buildEvents = new ArrayList<>();
    this.combatRetreats = new ArrayList<>();
    this.collapseRetreats = new ArrayList<>();
    this.combatExplosions = new ArrayList<>();
    this.retreatTargets = new boolean[this.map.systems.length];
    this.resourceLightSprites = new ArgbSprite[4][6];

    for (int i = 0; i < 6; ++i) {
      final ArgbSprite side = (ArgbSprite) RES_SIDES[i];
      final ArgbSprite low = (ArgbSprite) RES_LOWS[i];
      this.resourceLightSprites[0][i] = side.copy();
      this.resourceLightSprites[1][i] = low.copy();
      this.resourceLightSprites[2][i] = low.copy();
      this.resourceLightSprites[2][i].flipHorizontal();
      this.resourceLightSprites[3][i] = side.copy();
      this.resourceLightSprites[3][i].flipHorizontal();
    }

    for (int i = 0; i < GameState.NUM_RESOURCES; ++i) {
      final int var16 = RESOURCE_COLORS[i];
      final int r = (var16 >> 16) & 0xff;
      final int g = (var16 >> 8) & 0xff;
      final int b = var16 & 0xff;

      for (int j = 0; j < 6; ++j) {
        for (int k = 0; k < this.resourceLightSprites[i][j].pixels.length; ++k) {
          final int var13 = (this.resourceLightSprites[i][j].pixels[k] & 0xff0000) >> 16;
          final int var14 = (this.resourceLightSprites[i][j].pixels[k] & Drawing.GREEN) >> 8;
          final int var15 = 255 & this.resourceLightSprites[i][j].pixels[k];
          if (var14 == var13 && var13 == var15) {
            if (var13 <= 128) {
              this.resourceLightSprites[i][j].pixels[k] = (0xff000000 & this.resourceLightSprites[i][j].pixels[k]) + (b * var15 >> 7) + (g * var14 >> 7 << 8) + (r * var13 >> 7 << 16);
            } else {
              this.resourceLightSprites[i][j].pixels[k] = (0xff000000 & this.resourceLightSprites[i][j].pixels[k]) + (b * (256 - var15) - (32640 - 255 * var15) >> 7) + ((-var14 + 256) * g - 32640 + var14 * 255 >> 7 << 8) + (255 * (var13 - 128) + r * (-var13 + 256) >> 7 << 16);
            }
          }
        }
      }
    }
  }

  private static void drawLine(int x1, int y1, int x2, int y2, final int color) {
    y1 >>= 4;
    x1 >>= 4;
    x2 >>= 4;
    y2 >>= 4;
    int tmp;
    if (x1 > x2) {
      tmp = x1;
      x1 = x2;
      x2 = tmp;

      tmp = y1;
      y1 = y2;
      y2 = tmp;
    }

    if (x2 >= Drawing.left && x1 <= Drawing.right) {
      if (y1 > y2) {
        tmp = x1;
        x1 = x2;
        x2 = tmp;

        tmp = y1;
        y1 = y2;
        y2 = tmp;
      }

      if (y2 >= Drawing.top && y1 <= Drawing.bottom) {
        Drawing.line(x1, y1, x2, y2, color);
      }
    }
  }

  public static void a070eo(final boolean var0, final int var1, final int var2, final int var3, final int var4, final int var5) {
    final double var6 = Math.sqrt(MathUtil.euclideanDistanceSquared(var4 - var5, var2 - var1));
    final double var8 = (double) (var4 - var5) / var6;
    final double var10 = (double) (-var1 + var2) / var6;
    int var12;
    int var13;
    if (var4 <= var5) {
      var13 = var5;
      var12 = var4;
    } else {
      var12 = var5;
      var13 = var4;
    }

    int var14;
    int var15;
    if (var2 > var1) {
      var15 = var2;
      var14 = var1;
    } else {
      var15 = var1;
      var14 = var2;
    }

    if (var8 > 0.0D) {
      var14 -= 4;
      var15 = (int) ((double) var15 + 4.0D + var8 * var6 / 10.0D);
    } else {
      var15 += 4;
      var14 = (int) ((double) var14 + (var6 * var8 / 10.0D - 5.0D));
    }

    if (var10 > 0.0D) {
      var13 += 4;
      var12 = (int) ((double) var12 - (5.0D + var6 * var10 / 10.0D));
    } else {
      var13 = (int) ((double) var13 - (-5.0 + var6 * var10 / 10.0D));
      var12 -= 4;
    }

    if (var12 < 0) {
      var12 = 0;
    }

    if (var14 < 0) {
      var14 = 0;
    }

    if (var13 > 639) {
      var13 = 639;
    }

    if (var15 > 479) {
      var15 = 479;
    }

    final double var36 = 1.0D / var6;
    final double var38 = var6 / 2.0D;
    final double var40 = 1.0D / var38;
    final double var42 = var10 != 0.0D ? 1.0D / var10 : 0.0D;
    final double var44 = var6 / 10.0D;
    double var46 = var1 - var2;
    double var48 = -var4 + var5;
    final double var50 = Math.sqrt(var46 * var46 + var48 * var48);
    var48 /= var50;
    var46 /= var50;
    double var52 = (var44 + 4.0D) / Math.cos(Math.atan(var8 / var10));
    if (var52 < 0.0D) {
      var52 = -var52;
    }

    for (int var55 = 0; var55 < var15 + (1 - var14); ++var55) {
      final int var33 = var14 + (var55 - var1);
      final double var25 = (double) var33 * var42;
      final double var27 = (double) var5 + var8 * var25;
      int var34;
      int var35;
      if (var10 >= 0.0D) {
        if (var10 == 0.0D) {
          var35 = 1 + var13 - var12;
          var34 = 0;
        } else {
          var35 = (int) ((double) (-var12) + 8.0D + var27);
          var34 = -var12 + (int) (var27 - var52);
          if (var34 < 0) {
            var34 = 0;
          }
        }
      } else {
        var35 = (int) (var27 + var52) + 1 - var12;
        var34 = (int) ((double) (-var12) + (var27 - 8.0D));
        if (-var12 + var13 + 1 < var35) {
          var35 = 1 + (var13 - var12);
        }
      }

      for (int var56 = var34; var35 > var56; ++var56) {
        final double var31 = -var27 + (double) (var56 + var12);
        final double var19 = -(var31 * var48) + var25;
        if (var19 >= 0.0D && var19 <= var6 * (double) var3 / 200.0D) {
          final double var17;
          if (var6 / 4.0D <= var19) {
            var17 = 4.0D;
          } else {
            var17 = 1.0D + var36 * 4.0D * var19 * 3.0D;
          }

          final double var23;
          if (var10 == 0.0D) {
            var23 = var8 * (double) var33;
          } else {
            var23 = var31 * var46;
          }

          final double var21 = var44 - (-var38 + var19) * (var19 - var38) * var40 * 0.2D;
          double var29 = -var21 + var23;
          if (var29 < 0.0D) {
            var29 = -var29;
          }

          if (var29 <= var17) {
            if (var19 > var6) {
              var29 = Math.sqrt((var12 + var56 - var4) * (-var4 + var12 + var56) + (var14 - (-var55 + var2)) * (-var2 + var55 + var14));
              if (var29 > var17) {
                continue;
              }
            }

            final int var16 = (int) (160.0D * (var17 - var29) / var17);
            if (var0) {
              Drawing.setPixel(var56, var55, var16 << 24 | Drawing.WHITE);
            } else {
              Drawing.addPixel(var12 + var56, var55 + var14, Drawing.WHITE, var16);
            }
          }
        }
      }
    }

  }

  private static void a669tue(int var0, int var1, int var2, final int var3, int var4) {
    int var5 = 0;
    int var6 = 0;
    final int var7 = Drawing.left << 4;
    final int var8 = Drawing.right << 4;
    if (var2 > var1) {
      final int var9 = var2;
      var2 = var1;
      var1 = var9;
      final int i = var0;
      var0 = var4;
      var4 = i;
    }

    if (var1 >= var7 && var2 <= var8) {
      final long var14 = -var2 + var1;
      if (var1 > var8) {
        var6 = (int) ((long) (var1 - var8) * 65536L / var14);
      }

      final long var11 = var4 - var0;
      if (var2 < var7) {
        var5 = (int) ((long) (-var2 + var7) * 65536L / var14);
      }

      final int i = Drawing.bottom << 4;
      var4 -= (int) ((long) var6 * var11 >> 16);
      var1 -= (int) (var14 * (long) var6 >> 16);
      var0 += (int) (var11 * (long) var5 >> 16);
      final int i1 = Drawing.top << 4;
      var2 += (int) (var14 * (long) var5 >> 16);
      if (var4 < var0) {
        final int var13 = var2;
        var2 = var1;
        var1 = var13;
        final int var01 = var0;
        var0 = var4;
        var4 = var01;
      }

      if (var4 >= i1 && var0 <= i) {
        int i2 = 0;
        int i3 = 0;
        final long l = var4 - var0;
        if (i < var4) {
          i2 = (int) ((long) (-i + var4) * 65536L / (long) (var4 - var0));
        }

        if (var0 < i1) {
          i3 = (int) (65536L * (long) (-var0 + i1) / (long) (var4 - var0));
        }

        final long i4 = -var2 + var1;
        var4 -= (int) (l * (long) i2 >> 16);
        var1 -= (int) (i4 * (long) i2 >> 16);
        var2 += (int) ((long) i3 * i4 >> 16);
        var0 += (int) (l * (long) i3 >> 16);
        a022ar(var1, var2, var4, var0, var3);
      }
    }
  }

  private static void a022ar(final int var0, final int var1, final int var2, final int var3, final int var4) {
    if ((Math.abs(var1) | Math.abs(var3) | Math.abs(var0) | Math.abs(var2)) >>> 19 == 0) {
      if (Math.abs(var0 - var1) < Math.abs(var2 - var3)) {
        a669in(var4, var3, var2, var1, var0);
      } else {
        a802tue(var2, var1, var0, var4, var3);
      }
    }
  }

  private static void a802tue(int var0, int var1, int var2, final int var3, int var4) {
    int var5;
    if (var2 < var1) {
      var5 = var2;
      var2 = var1;
      var1 = var5;
      var5 = var0;
      var0 = var4;
      var4 = var5;
    }

    var5 = -var1 + var2;
    final int var6 = -var4 + var0;
    int var7;
    int var8;
    int var9;
    int var10;
    int var11;
    int var12;
    int var13;
    if (var5 == 0) {
      if (var6 < 16) {
        var7 = var1 >> 4;
        var8 = var4 >> 4;
        var10 = (16 - (15 & var1)) * var6;
        if (Drawing.left <= var7 && Drawing.right > var7 && var8 >= Drawing.top && var8 < Drawing.bottom) {
          var10 = (int) (256.0D * Math.pow((double) var10 / 256.0D, 0.55D));
          var11 = var3 & '\uff00';
          var9 = var3 & 16711935;
          var11 *= var10;
          var11 &= 16711680;
          var9 *= var10;
          var9 &= -16711936;
          var12 = var7 + var8 * ShatteredPlansClient.SCREEN_WIDTH;
          var13 = Drawing.screenBuffer[var12];
          var9 += -16711936 & (var13 & 16711935) * (256 - var10);
          var11 += 16711680 & (-var10 + 256) * ('\uff00' & var13);
          Drawing.screenBuffer[var12] = (var11 | var9) >>> 8;
        }

        var7 = 1 + (var1 >> 4);
        var8 = var4 >> 4;
        var10 = (15 & var1) * var6;
        if (Drawing.left <= var7 && Drawing.right > var7 && Drawing.top <= var8 && Drawing.bottom > var8) {
          var10 = (int) (Math.pow((double) var10 / 256.0D, 0.55D) * 256.0D);
          var11 = '\uff00' & var3;
          var9 = var3 & 16711935;
          var11 *= var10;
          var11 &= 16711680;
          var9 *= var10;
          var9 &= -16711936;
          var12 = var8 * ShatteredPlansClient.SCREEN_WIDTH + var7;
          var13 = Drawing.screenBuffer[var12];
          var11 += 16711680 & (-var10 + 256) * (var13 & '\uff00');
          var9 += -16711936 & (16711935 & var13) * (-var10 + 256);
          Drawing.screenBuffer[var12] = (var11 | var9) >>> 8;
        }
      } else {
        var7 = var1 >> 4;
        var8 = var4 >> 4;
        var10 = (-(var1 & 15) + 16) * (16 - (var4 & 15));
        if (Drawing.left <= var7 && var7 < Drawing.right && var8 >= Drawing.top && var8 < Drawing.bottom) {
          var10 = (int) (256.0D * Math.pow((double) var10 / 256.0D, 0.55D));
          var11 = '\uff00' & var3;
          var9 = var3 & 16711935;
          var11 *= var10;
          var9 *= var10;
          var11 &= 16711680;
          var9 &= -16711936;
          var12 = var8 * ShatteredPlansClient.SCREEN_WIDTH + var7;
          var13 = Drawing.screenBuffer[var12];
          var11 += (-var10 + 256) * ('\uff00' & var13) & 16711680;
          var9 += -16711936 & (var13 & 16711935) * (256 - var10);
          Drawing.screenBuffer[var12] = (var9 | var11) >>> 8;
        }

        var7 = var1 >> 4;
        var8 = var0 >> 4;
        var10 = (15 & var0) * (16 - (15 & var1));
        if (Drawing.left <= var7 && Drawing.right > var7 && Drawing.top <= var8 && Drawing.bottom > var8) {
          var10 = (int) (Math.pow((double) var10 / 256.0D, 0.55D) * 256.0D);
          var11 = var3 & '\uff00';
          var11 *= var10;
          var9 = var3 & 16711935;
          var9 *= var10;
          var11 &= 16711680;
          var9 &= -16711936;
          var12 = var8 * ShatteredPlansClient.SCREEN_WIDTH + var7;
          var13 = Drawing.screenBuffer[var12];
          var11 += (-var10 + 256) * (var13 & '\uff00') & 16711680;
          var9 += -16711936 & (-var10 + 256) * (var13 & 16711935);
          Drawing.screenBuffer[var12] = (var9 | var11) >>> 8;
        }

        Drawing.verticalLine(var1 >> 4, 1 + (var4 >> 4), (var6 >> 4) - 2, var3, 256 - 16 * (15 & var1));
        var7 = (var1 >> 4) + 1;
        var8 = var4 >> 4;
        var10 = (15 & var1) * (16 - (var4 & 15));
        if (var7 >= Drawing.left && Drawing.right > var7 && var8 >= Drawing.top && var8 < Drawing.bottom) {
          var10 = (int) (Math.pow((double) var10 / 256.0D, 0.55D) * 256.0D);
          var11 = var3 & '\uff00';
          var9 = var3 & 16711935;
          var11 *= var10;
          var9 *= var10;
          var11 &= 16711680;
          var9 &= -16711936;
          var12 = var8 * ShatteredPlansClient.SCREEN_WIDTH + var7;
          var13 = Drawing.screenBuffer[var12];
          var11 += ('\uff00' & var13) * (256 - var10) & 16711680;
          var9 += (16711935 & var13) * (-var10 + 256) & -16711936;
          Drawing.screenBuffer[var12] = (var9 | var11) >>> 8;
        }

        var7 = (var1 >> 4) + 1;
        var8 = var0 >> 4;
        var10 = (15 & var1) * (15 & var0);
        if (Drawing.left <= var7 && var7 < Drawing.right && Drawing.top <= var8 && Drawing.bottom > var8) {
          var10 = (int) (Math.pow((double) var10 / 256.0D, 0.55D) * 256.0D);
          var11 = var3 & '\uff00';
          var11 *= var10;
          var9 = var3 & 16711935;
          var11 &= 16711680;
          var9 *= var10;
          var9 &= -16711936;
          var12 = var8 * ShatteredPlansClient.SCREEN_WIDTH + var7;
          var13 = Drawing.screenBuffer[var12];
          var9 += (var13 & 16711935) * (-var10 + 256) & -16711936;
          var11 += 16711680 & (var13 & '\uff00') * (256 - var10);
          Drawing.screenBuffer[var12] = (var11 | var9) >>> 8;
        }

        Drawing.verticalLine(1 + (var1 >> 4), 1 + (var4 >> 4), (var6 >> 4) - 2, var3, (15 & var1) * 16);
      }

    } else {
      var7 = (var6 << 12) / var5;
      var8 = var1 + 7 >> 4;
      var9 = var7 * (-var1 + (var8 << 4)) + (var4 << 12);
      var10 = 16 - (15 & var1 + 7);
      var11 = var8;
      var12 = var9 >> 16;
      int var17 = (16 - ((var9 & '\uf326') >> 12)) * var10;
      int var16;
      int var18;
      int var19;
      int var20;
      if (Drawing.left <= var8 && var8 < Drawing.right && Drawing.top <= var12 && var12 < Drawing.bottom) {
        var17 = (int) (Math.pow((double) var17 / 256.0D, 0.55D) * 256.0D);
        var18 = var3 & '\uff00';
        var16 = var3 & 16711935;
        var18 *= var17;
        var16 *= var17;
        var18 &= 16711680;
        var16 &= -16711936;
        var19 = var8 + ShatteredPlansClient.SCREEN_WIDTH * var12;
        var20 = Drawing.screenBuffer[var19];
        var18 += (var20 & '\uff00') * (256 - var17) & 16711680;
        var16 += -16711936 & (var20 & 16711935) * (-var17 + 256);
        Drawing.screenBuffer[var19] = (var18 | var16) >>> 8;
      }

      var13 = var7 + var9;
      final int var15 = 1 + var12;
      int i = (15 & var9 >> 12) * var10;
      if (Drawing.left <= var8 && Drawing.right > var8 && Drawing.top <= var15 && Drawing.bottom > var15) {
        i = (int) (Math.pow((double) i / 256.0D, 0.55D) * 256.0D);
        var18 = var3 & '\uff00';
        var18 *= i;
        var16 = var3 & 16711935;
        var16 *= i;
        var18 &= 16711680;
        var16 &= -16711936;
        var19 = var8 + var15 * ShatteredPlansClient.SCREEN_WIDTH;
        var20 = Drawing.screenBuffer[var19];
        var18 += (var20 & '\uff00') * (-i + 256) & 16711680;
        var16 += (-i + 256) * (16711935 & var20) & -16711936;
        Drawing.screenBuffer[var19] = (var18 | var16) >>> 8;
      }

      var8 = var2 + 7 >> 4;
      var9 = var7 * (-var2 + (var8 << 4)) + (var0 << 12);
      var10 = 16 - (var2 + 7 & 15);
      final int var14 = var8;
      final int i1 = var9 >> 16;
      var19 = (16 - (var9 >> 12 & 15)) * var10;
      int var21;
      int var22;
      if (Drawing.left <= var8 && Drawing.right > var8 && i1 >= Drawing.top && Drawing.bottom > i1) {
        var19 = (int) (Math.pow((double) var19 / 256.0D, 0.55D) * 256.0D);
        var20 = '\uff00' & var3;
        var20 *= var19;
        var18 = var3 & 16711935;
        var18 *= var19;
        var20 &= 16711680;
        var18 &= -16711936;
        var21 = i1 * ShatteredPlansClient.SCREEN_WIDTH + var8;
        var22 = Drawing.screenBuffer[var21];
        var20 += ('\uff00' & var22) * (-var19 + 256) & 16711680;
        var18 += (256 - var19) * (var22 & 16711935) & -16711936;
        Drawing.screenBuffer[var21] = (var18 | var20) >>> 8;
      }

      int i2 = 1 + i1;
      var19 = var10 * (var9 >> 12 & 15);
      if (var8 >= Drawing.left && Drawing.right > var8 && Drawing.top <= i2 && i2 < Drawing.bottom) {
        var19 = (int) (256.0D * Math.pow((double) var19 / 256.0D, 0.55D));
        var20 = '\uff00' & var3;
        var20 *= var19;
        var18 = var3 & 16711935;
        var18 *= var19;
        var20 &= 16711680;
        var18 &= -16711936;
        var21 = ShatteredPlansClient.SCREEN_WIDTH * i2 + var8;
        var22 = Drawing.screenBuffer[var21];
        var18 += (var22 & 16711935) * (-var19 + 256) & -16711936;
        var20 += ('\uff00' & var22) * (256 - var19) & 16711680;
        Drawing.screenBuffer[var21] = (var20 | var18) >>> 8;
      }

      var7 <<= 4;
      ++var11;

      while (var14 > var11) {
        i2 = var13 >> 16;
        var19 = 256 - (var13 >> 8 & 255);
        if (Drawing.left <= var11 && var11 < Drawing.right && i2 >= Drawing.top && Drawing.bottom > i2) {
          var19 = (int) (Math.pow((double) var19 / 256.0D, 0.55D) * 256.0D);
          var20 = var3 & '\uff00';
          var18 = var3 & 16711935;
          var20 *= var19;
          var20 &= 16711680;
          var18 *= var19;
          var18 &= -16711936;
          var21 = var11 + ShatteredPlansClient.SCREEN_WIDTH * i2;
          var22 = Drawing.screenBuffer[var21];
          var18 += -16711936 & (-var19 + 256) * (16711935 & var22);
          var20 += 16711680 & (256 - var19) * (var22 & '\uff00');
          Drawing.screenBuffer[var21] = (var20 | var18) >>> 8;
        }

        i2 = (var13 >> 16) + 1;
        var19 = (var13 & '\uffd1') >> 8;
        if (var11 >= Drawing.left && Drawing.right > var11 && i2 >= Drawing.top && Drawing.bottom > i2) {
          var19 = (int) (256.0D * Math.pow((double) var19 / 256.0D, 0.55D));
          var20 = '\uff00' & var3;
          var20 *= var19;
          var18 = var3 & 16711935;
          var20 &= 16711680;
          var18 *= var19;
          var18 &= -16711936;
          var21 = i2 * ShatteredPlansClient.SCREEN_WIDTH + var11;
          var22 = Drawing.screenBuffer[var21];
          var20 += (256 - var19) * ('\uff00' & var22) & 16711680;
          var18 += -16711936 & (16711935 & var22) * (256 - var19);
          Drawing.screenBuffer[var21] = (var18 | var20) >>> 8;
        }

        var13 += var7;
        ++var11;
      }

    }
  }

  private static int[] a385qp(final int[] var1) {
    final int var2 = var1.length;
    if (_aib == null || _aib.length < var2 * 2) {
      _aib = new int[2 * var2];
      _enb = new int[var2 * 2];
    }

    int _bpr = 0;
    int var5 = var1[var2 - 2];
    int var6 = var1[var2 - 1];

    int var3;
    int var4;
    for (int var7 = 0; var7 < var2; var7 += 2) {
      var3 = var5;
      var4 = var6;
      var5 = var1[var7];
      var6 = var1[var7 + 1];
      if (Drawing.left > var5) {
        if (Drawing.left <= var3) {
          _aib[_bpr++] = Drawing.left;
          _aib[_bpr++] = var4 + (var6 - var4) * (Drawing.left - var3) / (-var3 + var5);
        }
      } else {
        if (Drawing.left > var3) {
          _aib[_bpr++] = Drawing.left;
          _aib[_bpr++] = var6 + (-var5 + Drawing.left) * (-var6 + var4) / (var3 - var5);
        }

        _aib[_bpr++] = var5;
        _aib[_bpr++] = var6;
      }
    }

    if (_bpr == 0) {
      return null;
    } else {
      final int[] var13 = _enb;
      _enb = _aib;
      _aib = var13;
      int var100 = 0;
      int i = _enb[_bpr - 2];
      int i1 = _enb[_bpr - 1];

      int var8;
      for (var8 = 0; _bpr > var8; var8 += 2) {
        var3 = i;
        var4 = i1;
        i1 = _enb[1 + var8];
        i = _enb[var8];
        if (Drawing.right <= i) {
          if (var3 < Drawing.right) {
            _aib[var100++] = Drawing.right;
            _aib[var100++] = var4 + (-var4 + i1) * (Drawing.right - var3) / (-var3 + i);
          }
        } else {
          if (var3 >= Drawing.right) {
            _aib[var100++] = Drawing.right;
            _aib[var100++] = (var4 - i1) * (-i + Drawing.right) / (var3 - i) + i1;
          }

          _aib[var100++] = i;
          _aib[var100++] = i1;
        }
      }

      if (var100 == 0) {
        return null;
      } else {
        final int[] b = _enb;
        _enb = _aib;
        _aib = b;
        int var101 = 0;
        int i2 = _enb[var100 - 2];
        int i3 = _enb[var100 - 1];

        for (var8 = 0; var100 > var8; var8 += 2) {
          var4 = i3;
          var3 = i2;
          i3 = _enb[var8 + 1];
          i2 = _enb[var8];
          if (i3 < Drawing.top) {
            if (var4 >= Drawing.top) {
              _aib[var101++] = var3 + (Drawing.top - var4) * (-var3 + i2) / (-var4 + i3);
              _aib[var101++] = Drawing.top;
            }
          } else {
            if (var4 < Drawing.top) {
              _aib[var101++] = i2 + (-i2 + var3) * (-i3 + Drawing.top) / (var4 - i3);
              _aib[var101++] = Drawing.top;
            }

            _aib[var101++] = i2;
            _aib[var101++] = i3;
          }
        }

        if (var101 == 0) {
          return null;
        } else {
          final int[] ints = _enb;
          _enb = _aib;
          _aib = ints;
          int var102 = 0;
          int i4 = _enb[var101 - 1];
          int i5 = _enb[var101 - 2];

          for (var8 = 0; var8 < var101; var8 += 2) {
            var3 = i5;
            var4 = i4;
            i5 = _enb[var8];
            i4 = _enb[1 + var8];
            if (i4 < Drawing.bottom) {
              if (Drawing.bottom <= var4) {
                _aib[var102++] = (-i5 + var3) * (Drawing.bottom - i4) / (var4 - i4) + i5;
                _aib[var102++] = Drawing.bottom;
              }

              _aib[var102++] = i5;
              _aib[var102++] = i4;
            } else if (var4 < Drawing.bottom) {
              _aib[var102++] = (Drawing.bottom - var4) * (i5 - var3) / (i4 - var4) + var3;
              _aib[var102++] = Drawing.bottom;
            }
          }

          if (var102 == 0) {
            return null;
          } else {
            final int[] var14 = new int[var102];
            System.arraycopy(_aib, 0, var14, 0, var102);
            return var14;
          }
        }
      }
    }
  }

  private static void drawSystemHex(final int[] points, final int color, final int alpha) {
    c797c();
    a397c(points, points.length);
    d797c();
    while (a801c()) {
      Drawing.horizontalLine(_ce, _ch, -_ce + _ca, color, alpha);
    }
  }

  private static void a669in(final int var0, int var1, int var3, int var4, int var5) {
    int var6;
    if (var1 > var3) {
      var6 = var3;
      var3 = var1;
      var1 = var6;
      var6 = var5;
      var5 = var4;
      var4 = var6;
    }

    var6 = -var4 + var5;
    final int var7 = -var1 + var3;
    int var8;
    int var9;
    int var10;
    int var11;
    int var12;
    int var13;
    int var14;
    if (var7 == 0) {
      if (var6 < 16) {
        var8 = var4 >> 4;
        var9 = var1 >> 4;
        var11 = (16 - (var1 & 15)) * var6;
        if (var8 >= Drawing.left && var8 < Drawing.right && Drawing.top <= var9 && Drawing.bottom > var9) {
          var11 = (int) (Math.pow((double) var11 / 256.0D, 0.55D) * 256.0D);
          var12 = '\uff00' & var0;
          var10 = var0 & 16711935;
          var12 *= var11;
          var10 *= var11;
          var12 &= 16711680;
          var10 &= -16711936;
          var13 = ShatteredPlansClient.SCREEN_WIDTH * var9 + var8;
          var14 = Drawing.screenBuffer[var13];
          var12 += 16711680 & ('\uff00' & var14) * (256 - var11);
          var10 += (16711935 & var14) * (-var11 + 256) & -16711936;
          Drawing.screenBuffer[var13] = (var12 | var10) >>> 8;
        }

        var8 = var4 >> 4;
        var9 = (var1 >> 4) + 1;
        var11 = var6 * (var1 & 15);
        if (var8 >= Drawing.left && var8 < Drawing.right && var9 >= Drawing.top && var9 < Drawing.bottom) {
          var11 = (int) (Math.pow((double) var11 / 256.0D, 0.55D) * 256.0D);
          var12 = '\uff00' & var0;
          var10 = var0 & 16711935;
          var12 *= var11;
          var12 &= 16711680;
          var10 *= var11;
          var10 &= -16711936;
          var13 = var8 + ShatteredPlansClient.SCREEN_WIDTH * var9;
          var14 = Drawing.screenBuffer[var13];
          var12 += (var14 & '\uff00') * (-var11 + 256) & 16711680;
          var10 += (16711935 & var14) * (-var11 + 256) & -16711936;
          Drawing.screenBuffer[var13] = (var10 | var12) >>> 8;
        }
      } else {
        var8 = var4 >> 4;
        var9 = var1 >> 4;
        var11 = (16 - (15 & var4)) * (16 - (var1 & 15));
        if (var8 >= Drawing.left && var8 < Drawing.right && var9 >= Drawing.top && var9 < Drawing.bottom) {
          var11 = (int) (256.0D * Math.pow((double) var11 / 256.0D, 0.55D));
          var12 = var0 & '\uff00';
          var12 *= var11;
          var10 = var0 & 16711935;
          var12 &= 16711680;
          var10 *= var11;
          var10 &= -16711936;
          var13 = ShatteredPlansClient.SCREEN_WIDTH * var9 + var8;
          var14 = Drawing.screenBuffer[var13];
          var10 += -16711936 & (16711935 & var14) * (-var11 + 256);
          var12 += 16711680 & (var14 & '\uff00') * (256 - var11);
          Drawing.screenBuffer[var13] = (var12 | var10) >>> 8;
        }

        var8 = var5 >> 4;
        var9 = var1 >> 4;
        var11 = (var5 & 15) * (16 - (15 & var1));
        if (Drawing.left <= var8 && var8 < Drawing.right && var9 >= Drawing.top && Drawing.bottom > var9) {
          var11 = (int) (Math.pow((double) var11 / 256.0D, 0.55D) * 256.0D);
          var12 = '\uff00' & var0;
          var12 *= var11;
          var10 = var0 & 16711935;
          var10 *= var11;
          var12 &= 16711680;
          var10 &= -16711936;
          var13 = var8 + ShatteredPlansClient.SCREEN_WIDTH * var9;
          var14 = Drawing.screenBuffer[var13];
          var10 += -16711936 & (16711935 & var14) * (256 - var11);
          var12 += 16711680 & (var14 & '\uff00') * (-var11 + 256);
          Drawing.screenBuffer[var13] = (var10 | var12) >>> 8;
        }

        Drawing.horizontalLine((var4 >> 4) + 1, var1 >> 4, (var6 >> 4) - 2, var0, (16 - (15 & var1)) * 16);
        var8 = var4 >> 4;
        var9 = (var1 >> 4) + 1;
        var11 = (var4 & 15) * (16 - (15 & var1));
        if (Drawing.left <= var8 && Drawing.right > var8 && Drawing.top <= var9 && Drawing.bottom > var9) {
          var11 = (int) (256.0D * Math.pow((double) var11 / 256.0D, 0.55D));
          var12 = '\uff00' & var0;
          var10 = var0 & 16711935;
          var12 *= var11;
          var12 &= 16711680;
          var10 *= var11;
          var10 &= -16711936;
          var13 = var9 * ShatteredPlansClient.SCREEN_WIDTH + var8;
          var14 = Drawing.screenBuffer[var13];
          var10 += (var14 & 16711935) * (-var11 + 256) & -16711936;
          var12 += 16711680 & (-var11 + 256) * ('\uff00' & var14);
          Drawing.screenBuffer[var13] = (var10 | var12) >>> 8;
        }

        var8 = var5 >> 4;
        var9 = 1 + (var1 >> 4);
        var11 = (15 & var4) * (15 & var3);
        if (var8 >= Drawing.left && Drawing.right > var8 && Drawing.top <= var9 && Drawing.bottom > var9) {
          var11 = (int) (256.0D * Math.pow((double) var11 / 256.0D, 0.55D));
          var12 = '\uff00' & var0;
          var10 = var0 & 16711935;
          var12 *= var11;
          var10 *= var11;
          var12 &= 16711680;
          var10 &= -16711936;
          var13 = var9 * ShatteredPlansClient.SCREEN_WIDTH + var8;
          var14 = Drawing.screenBuffer[var13];
          var10 += (256 - var11) * (var14 & 16711935) & -16711936;
          var12 += (-var11 + 256) * (var14 & '\uff00') & 16711680;
          Drawing.screenBuffer[var13] = (var12 | var10) >>> 8;
        }

        Drawing.horizontalLine((var4 >> 4) + 1, (var1 >> 4) + 1, (var6 >> 4) - 2, var0, 16 * (15 & var1));
      }

    } else {
      var8 = (var6 << 12) / var7;
      var9 = var1 + 7 >> 4;
      var10 = ((var9 << 4) - var1) * var8 + (var4 << 12);
      var11 = -(7 + var1 & 15) + 16;
      var12 = var9;
      var13 = var10 >> 16;
      int var17 = (-((var10 & 'ﷷ') >> 12) + 16) * var11;
      int var16;
      int var18;
      int var19;
      int var20;
      if (var13 >= Drawing.left && Drawing.right > var13 && Drawing.top <= var9 && var9 < Drawing.bottom) {
        var17 = (int) (256.0D * Math.pow((double) var17 / 256.0D, 0.55D));
        var18 = var0 & '\uff00';
        var16 = var0 & 16711935;
        var18 *= var17;
        var16 *= var17;
        var18 &= 16711680;
        var16 &= -16711936;
        var19 = var9 * ShatteredPlansClient.SCREEN_WIDTH + var13;
        var20 = Drawing.screenBuffer[var19];
        var16 += (16711935 & var20) * (-var17 + 256) & -16711936;
        var18 += (-var17 + 256) * ('\uff00' & var20) & 16711680;
        Drawing.screenBuffer[var19] = (var16 | var18) >>> 8;
      }

      var14 = 1 + var13;
      var18 = var11 * (15 & var10 >> 12);
      int var21;
      if (var14 >= Drawing.left && var14 < Drawing.right && Drawing.top <= var9 && Drawing.bottom > var9) {
        var18 = (int) (Math.pow((double) var18 / 256.0D, 0.55D) * 256.0D);
        var19 = var0 & '\uff00';
        var19 *= var18;
        int i = var0 & 16711935;
        i *= var18;
        var19 &= 16711680;
        i &= -16711936;
        var20 = var9 * ShatteredPlansClient.SCREEN_WIDTH + var14;
        var21 = Drawing.screenBuffer[var20];
        i += -16711936 & (var21 & 16711935) * (256 - var18);
        var19 += (-var18 + 256) * ('\uff00' & var21) & 16711680;
        Drawing.screenBuffer[var20] = (var19 | i) >>> 8;
      }

      var9 = var3 + 7 >> 4;
      var14 = var10 + var8;
      var10 = (var5 << 12) + (-var3 + (var9 << 4)) * var8;
      var11 = -(var3 + 7 & 15) + 16;
      final int var15 = var10 >> 16;
      var16 = var9;
      var20 = (16 - (('\uf64c' & var10) >> 12)) * var11;
      int var22;
      int var23;
      if (var15 >= Drawing.left && Drawing.right > var15 && var9 >= Drawing.top && Drawing.bottom > var9) {
        var20 = (int) (256.0D * Math.pow((double) var20 / 256.0D, 0.55D));
        var21 = var0 & '\uff00';
        var21 *= var20;
        var19 = var0 & 16711935;
        var21 &= 16711680;
        var19 *= var20;
        var19 &= -16711936;
        var22 = ShatteredPlansClient.SCREEN_WIDTH * var9 + var15;
        var23 = Drawing.screenBuffer[var22];
        var19 += (var23 & 16711935) * (256 - var20) & -16711936;
        var21 += (-var20 + 256) * (var23 & '\uff00') & 16711680;
        Drawing.screenBuffer[var22] = (var19 | var21) >>> 8;
      }

      int i = 1 + var15;
      var20 = var11 * (15 & var10 >> 12);
      if (i >= Drawing.left && Drawing.right > i && Drawing.top <= var9 && Drawing.bottom > var9) {
        var20 = (int) (Math.pow((double) var20 / 256.0D, 0.55D) * 256.0D);
        var21 = var0 & '\uff00';
        var21 *= var20;
        var19 = var0 & 16711935;
        var19 *= var20;
        var21 &= 16711680;
        var19 &= -16711936;
        var22 = i + ShatteredPlansClient.SCREEN_WIDTH * var9;
        var23 = Drawing.screenBuffer[var22];
        var19 += (-var20 + 256) * (var23 & 16711935) & -16711936;
        var21 += (var23 & '\uff00') * (256 - var20) & 16711680;
        Drawing.screenBuffer[var22] = (var21 | var19) >>> 8;
      }

      var8 <<= 4;
      ++var12;

      while (var16 > var12) {
        i = var14 >> 16;
        var20 = 256 - (var14 >> 8 & 255);
        if (i >= Drawing.left && Drawing.right > i && var12 >= Drawing.top && Drawing.bottom > var12) {
          var20 = (int) (256.0D * Math.pow((double) var20 / 256.0D, 0.55D));
          var21 = '\uff00' & var0;
          var19 = var0 & 16711935;
          var21 *= var20;
          var19 *= var20;
          var21 &= 16711680;
          var19 &= -16711936;
          var22 = i + ShatteredPlansClient.SCREEN_WIDTH * var12;
          var23 = Drawing.screenBuffer[var22];
          var19 += -16711936 & (16711935 & var23) * (256 - var20);
          var21 += ('\uff00' & var23) * (256 - var20) & 16711680;
          Drawing.screenBuffer[var22] = (var19 | var21) >>> 8;
        }

        i = (var14 >> 16) + 1;
        var20 = 255 & var14 >> 8;
        if (Drawing.left <= i && i < Drawing.right && Drawing.top <= var12 && var12 < Drawing.bottom) {
          var20 = (int) (256.0D * Math.pow((double) var20 / 256.0D, 0.55D));
          var21 = '\uff00' & var0;
          var21 *= var20;
          var19 = var0 & 16711935;
          var19 *= var20;
          var21 &= 16711680;
          var19 &= -16711936;
          var22 = var12 * ShatteredPlansClient.SCREEN_WIDTH + i;
          var23 = Drawing.screenBuffer[var22];
          var21 += 16711680 & (-var20 + 256) * (var23 & '\uff00');
          var19 += (var23 & 16711935) * (-var20 + 256) & -16711936;
          Drawing.screenBuffer[var22] = (var21 | var19) >>> 8;
        }

        var14 += var8;
        ++var12;
      }

    }
  }

  private static void a306we(int var0, int var1, int var2, final int var3, int var5, final int var6) {
    int var4 = 0;
    final byte var8 = 0;
    int var9 = 0;
    int var10 = 0;
    final int var11 = Drawing.left << 4;
    final int var12 = Drawing.right << 4;
    if (var2 > var0) {
      final int var13 = var2;
      var2 = var0;
      var0 = var13;
      final int i = var1;
      var1 = var5;
      var5 = i;
      var4 = -var4 + 80;
    }

    if (var2 < var12 && var12 < var0) {
      var10 = (int) (65536L * (long) (var0 - var12) / (long) (-var2 + var0));
    }

    if (var11 > var2 && var0 > var11) {
      var9 = (int) (65536L * (long) (-var2 + var11) / (long) (-var2 + var0));
    }

    final long var23 = var0 - var2;
    final long var15 = var5 - var1;
    var2 += (int) ((long) var9 * var23 >> 16);
    var1 += (int) ((long) var9 * var15 >> 16);
    var0 -= (int) ((long) var10 * var23 >> 16);
    var5 -= (int) ((long) var10 * var15 >> 16);
    final int i = Drawing.bottom << 4;
    final int i1 = Drawing.top << 4;
    int i2 = 0;
    int i3 = 0;
    int var17;
    if (var1 > var5) {
      var17 = var2;
      var2 = var0;
      var0 = var17;
      var17 = var1;
      var1 = var5;
      var5 = var17;
      var4 = 80 - var4;
    }

    final long l = -var2 + var0;
    final long i4 = var5 - var1;
    if (var1 < i1 && var5 > i1) {
      i3 = (int) ((long) (i1 - var1) * 65536L / (long) (var5 - var1));
    }

    if (var1 < i && i < var5) {
      i2 = (int) ((long) (var5 - i) * 65536L / (long) (-var1 + var5));
    }

    var5 -= (int) (i4 * (long) i2 >> 16);
    var1 += (int) ((long) i3 * i4 >> 16);
    var2 += (int) (l * (long) i3 >> 16);
    int var22 = var8 + var4 / 80;
    var0 -= (int) (l * (long) i2 >> 16);
    var4 %= 80;

    var17 = 1 + (int) Math.sqrt((var5 - var1) * (var5 - var1) + (-var2 + var0) * (var0 - var2));
    int var18 = var2;

    int var19;
    for (var19 = var1; var4 < var17; var4 += 80) {
      final int var20 = var4 * (-var2 + var0) / var17 + var2;
      final int var21 = var1 + var4 * (-var1 + var5) / var17;
      a022ar(var20, var18, var21, var19, (1 & var22) == 0 ? var3 : var6);
      var18 = var20;
      var19 = var21;
      ++var22;
    }

    a022ar(var0, var18, var5, var19, (1 & var22) != 0 ? var6 : var3);
  }

  private static void a681be(int var1, final int var2, int var3, final int var4, int var5, int var6) {
    int var0 = 0;
    final int var7 = 5;
    int var9;
    if (var1 < var5) {
      var9 = var5;
      var5 = var1;
      var1 = var9;
      var9 = var3;
      var3 = var6;
      var6 = var9;
      var0 = var7 - var0;
    }

    final byte var8 = 0;
    if (Drawing.left <= var1 && var5 <= Drawing.right) {
      if (var3 > var6) {
        var9 = var5;
        var5 = var1;
        var1 = var9;
        var9 = var3;
        var3 = var6;
        var6 = var9;
        var0 = var7 - var0;
      }

      if (var6 >= Drawing.top && var3 <= Drawing.bottom) {
        int var14 = var8 + var0 / var7;
        var0 %= var7;

        var9 = (int) Math.sqrt((-var5 + var1) * (var1 - var5) + (var6 - var3) * (var6 - var3)) + 1;
        int var10 = var5;

        int var11;
        for (var11 = var3; var0 < var9; var0 += var7) {
          final int var12 = (var1 - var5) * var0 / var9 + var5;
          final int var13 = var3 + (-var3 + var6) * var0 / var9;
          Drawing.line(var10, var11, var12, var13, (var14 & 1) == 0 ? var2 : var4);
          var11 = var13;
          var10 = var12;
          ++var14;
        }

        Drawing.line(var10, var11, var1, var6, (var14 & 1) != 0 ? var4 : var2);
      }
    }
  }

  private static void drawSystemHatching(final int[] points, final int color, final int alpha) {
    final int var3 = 256 - alpha;
    final int var4 = color & 0xff00ff;
    c797c();
    final int var5 = color & Drawing.GREEN;
    a397c(points, points.length);
    d797c();

    while (a801c()) {
      int var6 = _ce;
      if (Drawing.left > var6) {
        var6 = Drawing.left;
      }

      int var7 = _ca;
      if (Drawing.right < var7) {
        var7 = Drawing.right;
      }

      int var8 = (var6 & -8) - (7 & _ch);
      var8 += 8 + (-8 & -var8 + var6);

      for (int var9 = Drawing.pixelIndex(var8, _ch); var7 > var8; var8 += 8) {
        Drawing.screenBuffer[var9] = (16711935 & (16711935 & Drawing.screenBuffer[var9]) * var3 + var4 * alpha >>> 8) + (((Drawing.GREEN & Drawing.screenBuffer[var9]) * var3 + var5 * alpha & 16711680) >>> 8);
        var9 += 8;
      }
    }
  }

  private static void b093c(final int var0, int var1) {
    while (true) {
      if (var1 >= var0 + 8) {
        boolean var2 = true;

        for (int var3 = var0 + 4; var3 < var1; var3 += 4) {
          final int var4 = _cf[var3 - 4];
          final int var5 = _cf[var3];
          if (var4 > var5) {
            var2 = false;
            _cf[var3 - 4] = var5;
            _cf[var3] = var4;
            final int i = _cf[var3 - 2];
            _cf[var3 - 2] = _cf[var3 + 2];
            _cf[var3 + 2] = i;
            final int i1 = _cf[var3 - 1];
            _cf[var3 - 1] = _cf[var3 + 3];
            _cf[var3 + 3] = i1;
          }
        }

        if (!var2) {
          var1 -= 4;
          continue;
        }
      }

      return;
    }
  }

  private static boolean a801c() {
    int var0 = _cd;
    int var1 = _cb2;

    int var3;
    for (int var2 = _ch; var1 >= var0; var1 = var3) {
      ++var2;
      _ch = var2;
      if (var2 >= Drawing.bottom) {
        return false;
      }

      int var4;
      int var5;
      for (var3 = _cc; var0 < _cg; var0 += 4) {
        var4 = _cf[var0 + 1];
        if (var2 < var4) {
          break;
        }

        var5 = _cf[var0];
        final int var6 = _cf[var0 + 2];
        final int var7 = _cf[var0 + 3];
        final int var8 = (var6 - var5 << 16) / (var7 - var4);
        final int var9 = (var5 << 16) + '耀';
        _cf[var0] = var9;
        _cf[var0 + 2] = var8;
      }

      for (var4 = var3; var4 < var0; var4 += 4) {
        var5 = _cf[var4 + 3];
        if (var2 >= var5) {
          _cf[var4] = _cf[var3];
          _cf[var4 + 1] = _cf[var3 + 1];
          _cf[var4 + 2] = _cf[var3 + 2];
          _cf[var4 + 3] = _cf[var3 + 3];
          var3 += 4;
        }
      }

      if (var3 == _cg) {
        _cg = 0;
        return false;
      }

      b093c(var3, var0);
      _cc = var3;
      _cd = var0;
    }

    _ce = _cf[var1] >> 16;
    _ca = _cf[var1 + 4] >> 16;
    _cf[var1] += _cf[var1 + 2];
    _cf[var1 + 4] += _cf[var1 + 6];
    var1 += 8;
    _cb2 = var1;
    return true;
  }

  private static void a093c(final int var0, final int var1) {
    if (var1 > var0 + 4) {
      int var2 = var0;
      final int var3 = _cf[var0];
      final int var4 = _cf[var0 + 1];
      final int var5 = _cf[var0 + 2];
      final int var6 = _cf[var0 + 3];

      for (int var7 = var0 + 4; var7 < var1; var7 += 4) {
        final int var8 = _cf[var7 + 1];
        if (var8 < var4) {
          _cf[var2] = _cf[var7];
          _cf[var2 + 1] = var8;
          _cf[var2 + 2] = _cf[var7 + 2];
          _cf[var2 + 3] = _cf[var7 + 3];
          var2 += 4;
          _cf[var7] = _cf[var2];
          _cf[var7 + 1] = _cf[var2 + 1];
          _cf[var7 + 2] = _cf[var2 + 2];
          _cf[var7 + 3] = _cf[var2 + 3];
        }
      }

      _cf[var2] = var3;
      _cf[var2 + 1] = var4;
      _cf[var2 + 2] = var5;
      _cf[var2 + 3] = var6;
      a093c(var0, var2);
      a093c(var2 + 4, var1);
    }
  }

  private static void d797c() {
    if (_cg < 0) {
      _cb2 = 0;
      _cd = 0;
      _cc = 0;
      _ch = 2147483646;
    } else {
      a093c(0, _cg);
      int var0 = _cf[1];
      if (var0 < Drawing.top) {
        var0 = Drawing.top;
      }

      final byte var1 = 0;

      int var2;
      for (var2 = 0; var2 < _cg; var2 += 4) {
        final int var3 = _cf[var2 + 1];
        if (var0 < var3) {
          break;
        }

        final int var4 = _cf[var2];
        final int var5 = _cf[var2 + 2];
        final int var6 = _cf[var2 + 3];
        final int var7 = (var5 - var4 << 16) / (var6 - var3);
        final int var8 = (var4 << 16) + '耀';
        _cf[var2] = var8 + (var0 - var3) * var7;
        _cf[var2 + 2] = var7;
      }

      _cc = var1;
      _cd = var2;
      _cb2 = var2;
      _ch = var0 - 1;
    }
  }

  private static void c797c() {
    _cg = 0;
  }

  private static void a397c(final int[] var0, final int var2) {
    final int var3 = _cg + (var2 << 1);
    int var5;
    if (_cf == null || _cf.length < var3) {
      final int[] var4 = new int[var3];

      for (var5 = 0; var5 < _cg; ++var5) {
        assert _cf != null;
        var4[var5] = _cf[var5];
      }

      _cf = var4;
    }

    int var8 = var2 - 2;

    for (var5 = 0; var5 < var2; var5 += 2) {
      final int var6 = var0[var8 + 1];
      final int var7 = var0[var5 + 1];
      if (var6 < var7) {
        _cf[_cg++] = var0[var8];
        _cf[_cg++] = var6;
        _cf[_cg++] = var0[var5];
        _cf[_cg++] = var7;
      } else if (var7 < var6) {
        _cf[_cg++] = var0[var5];
        _cf[_cg++] = var7;
        _cf[_cg++] = var0[var8];
        _cf[_cg++] = var6;
      }

      var8 = var5;
    }

  }

  public static void a835ie(final Sprite var0, int var1, int var2) {
    var1 += var0.x;
    var2 += var0.y;
    int var3 = Drawing.pixelIndex(var1, var2);
    int var4 = 0;
    int var5 = var0.height;
    int var6 = var0.width;
    int var7 = Drawing.width - var6;
    int var8 = 0;
    int var9;
    if (var2 < Drawing.top) {
      var9 = Drawing.top - var2;
      var5 -= var9;
      var2 = Drawing.top;
      var4 += var9 * var6;
      var3 += var9 * Drawing.width;
    }

    if (var2 + var5 > Drawing.bottom) {
      var5 -= var2 + var5 - Drawing.bottom;
    }

    if (var1 < Drawing.left) {
      var9 = Drawing.left - var1;
      var6 -= var9;
      var1 = Drawing.left;
      var4 += var9;
      var3 += var9;
      var8 += var9;
      var7 += var9;
    }

    if (var1 + var6 > Drawing.right) {
      var9 = var1 + var6 - Drawing.right;
      var6 -= var9;
      var8 += var9;
      var7 += var9;
    }

    if (var6 > 0 && var5 > 0) {
      a650ie(Drawing.screenBuffer, var0.pixels, var4, var3, var6, var5, var7, var8);
    }
  }

  private static void a650ie(final int[] var0, final int[] var1, int var3, int var4, final int var5, final int var6, final int var7, final int var8) {
    for (int var11 = -var6; var11 < 0; ++var11) {
      for (int var12 = -var5; var12 < 0; ++var12) {
        final int var2 = var1[var3++];
        if (var2 == 0) {
          ++var4;
        } else {
          final int var9 = var0[var4];
          final int var10 = 256 - (var2 & 255);
          var0[var4++] = ((var9 & 16711935) * var10 & -16711936 | (var9 & '\uff00') * var10 & 16711680) >> 8;
        }
      }

      var4 += var7;
      var3 += var8;
    }

  }

  public static void a194ie(final ArgbSprite var0, int var1, int var2, final int var3, final int var4, final int var5, final int var6) {
    if (var6 != 0) {
      var1 -= var0.x << 4;
      var2 -= var0.y << 4;
      final double var7 = (double) (var5 & '\uffff') * 9.587379924285257E-5D;
      final int var9 = (int) Math.floor(Math.sin(var7) * (double) var6 + 0.5D);
      final int var10 = (int) Math.floor(Math.cos(var7) * (double) var6 + 0.5D);
      final int var11 = -var1 * var10 + -var2 * var9;
      final int var12 = var1 * var9 + -var2 * var10;
      final int var13 = ((var0.width << 4) - var1) * var10 + -var2 * var9;
      final int var14 = -((var0.width << 4) - var1) * var9 + -var2 * var10;
      final int var15 = -var1 * var10 + ((var0.height << 4) - var2) * var9;
      final int var16 = var1 * var9 + ((var0.height << 4) - var2) * var10;
      final int var17 = ((var0.width << 4) - var1) * var10 + ((var0.height << 4) - var2) * var9;
      final int var18 = -((var0.width << 4) - var1) * var9 + ((var0.height << 4) - var2) * var10;
      int var19;
      int var20;
      if (var11 < var13) {
        var19 = var11;
        var20 = var13;
      } else {
        var19 = var13;
        var20 = var11;
      }

      if (var15 < var19) {
        var19 = var15;
      }

      if (var17 < var19) {
        var19 = var17;
      }

      if (var15 > var20) {
        var20 = var15;
      }

      if (var17 > var20) {
        var20 = var17;
      }

      int var21;
      int var22;
      if (var12 < var14) {
        var21 = var12;
        var22 = var14;
      } else {
        var21 = var14;
        var22 = var12;
      }

      if (var16 < var21) {
        var21 = var16;
      }

      if (var18 < var21) {
        var21 = var18;
      }

      if (var16 > var22) {
        var22 = var16;
      }

      if (var18 > var22) {
        var22 = var18;
      }

      var19 >>= 12;
      var20 = var20 + 4095 >> 12;
      var21 >>= 12;
      var22 = var22 + 4095 >> 12;
      var19 += var3;
      var20 += var3;
      var21 += var4;
      var22 += var4;
      var19 >>= 4;
      var20 = var20 + 15 >> 4;
      var21 >>= 4;
      var22 = var22 + 15 >> 4;
      if (var19 < Drawing.left) {
        var19 = Drawing.left;
      }

      if (var20 > Drawing.right) {
        var20 = Drawing.right;
      }

      if (var21 < Drawing.top) {
        var21 = Drawing.top;
      }

      if (var22 > Drawing.bottom) {
        var22 = Drawing.bottom;
      }

      var20 = var19 - var20;
      if (var20 < 0) {
        var22 = var21 - var22;
        if (var22 < 0) {
          int var23 = var21 * Drawing.width + var19;
          final int var24 = Drawing.width + var20;
          final double var25 = 1.6777216E7D / (double) var6;
          final int var27 = (int) Math.floor(Math.sin(var7) * var25 + 0.5D);
          final int var28 = (int) Math.floor(Math.cos(var7) * var25 + 0.5D);
          final int var29 = (var19 << 4) + 8 - var3;
          final int var30 = (var21 << 4) + 8 - var4;
          int var31 = (var1 << 8) - 2048 - (var30 * var27 >> 4);
          int var32 = (var2 << 8) - 2048 + (var30 * var28 >> 4);
          int var33;
          int var34;
          int var35;
          int var36;
          int var37;
          int var38;
          int var39;
          if (var28 < 0) {
            if (var27 < 0) {
              for (var36 = var22; var36 < 0; var23 += var24) {
                {
                  var37 = var31 + (var29 * var28 >> 4);
                  var38 = var32 + (var29 * var27 >> 4);
                  var39 = var20;
                  if ((var35 = var37 - (var0.width << 12)) >= 0) {

                    var35 = (var28 - var35) / var28;
                    var39 = var20 + var35;
                    var37 += var28 * var35;
                    var38 += var27 * var35;
                    var23 += var35;
                  }

                  if ((var35 = var38 - (var0.height << 12)) >= 0) {

                    var35 = (var27 - var35) / var27;
                    var39 += var35;
                    var37 += var28 * var35;
                    var38 += var27 * var35;
                    var23 += var35;
                  }

                  while (var39 < 0 && var37 >= -4096 && var38 >= -4096) {
                    var33 = var37 >> 12;
                    var34 = var38 >> 12;
                    a600ie(var0.pixels, var0.width, var0.height, var23, var33, var34, var37, var38);
                    ++var39;
                    var37 += var28;
                    var38 += var27;
                    ++var23;
                  }

                  var23 -= var39;
                }

                ++var36;
                var31 -= var27;
                var32 += var28;
              }
            } else {
              for (var36 = var22; var36 < 0; var23 += var24) {
                label252:
                {
                  var37 = var31 + (var29 * var28 >> 4);
                  var38 = var32 + (var29 * var27 >> 4);
                  var39 = var20;
                  if ((var35 = var37 - (var0.width << 12)) >= 0) {

                    var35 = (var28 - var35) / var28;
                    var39 = var20 + var35;
                    var37 += var28 * var35;
                    var38 += var27 * var35;
                    var23 += var35;
                  }

                  if ((var35 = var38 + 4096) < 0) {
                    if (var27 == 0) {
                      var23 -= var39;
                      break label252;
                    }

                    var35 = (var27 - 1 - var35) / var27;
                    var39 += var35;
                    var37 += var28 * var35;
                    var38 += var27 * var35;
                    var23 += var35;
                  }

                  while (var39 < 0 && var37 >= -4096 && (var34 = var38 >> 12) < var0.height) {
                    var33 = var37 >> 12;
                    a600ie(var0.pixels, var0.width, var0.height, var23, var33, var34, var37, var38);
                    ++var39;
                    var37 += var28;
                    var38 += var27;
                    ++var23;
                  }

                  var23 -= var39;
                }

                ++var36;
                var31 -= var27;
                var32 += var28;
              }
            }
          } else if (var27 < 0) {
            for (var36 = var22; var36 < 0; var23 += var24) {
              label254:
              {
                var37 = var31 + (var29 * var28 >> 4);
                var38 = var32 + (var29 * var27 >> 4);
                var39 = var20;
                if ((var35 = var37 + 4096) < 0) {
                  if (var28 == 0) {
                    var23 -= var20;
                    break label254;
                  }

                  var35 = (var28 - 1 - var35) / var28;
                  var39 = var20 + var35;
                  var37 += var28 * var35;
                  var38 += var27 * var35;
                  var23 += var35;
                }

                if ((var35 = var38 - (var0.height << 12)) >= 0) {

                  var35 = (var27 - var35) / var27;
                  var39 += var35;
                  var37 += var28 * var35;
                  var38 += var27 * var35;
                  var23 += var35;
                }

                while (var39 < 0 && var38 >= -4096 && (var33 = var37 >> 12) < var0.width) {
                  var34 = var38 >> 12;
                  a600ie(var0.pixels, var0.width, var0.height, var23, var33, var34, var37, var38);
                  ++var39;
                  var37 += var28;
                  var38 += var27;
                  ++var23;
                }

                var23 -= var39;
              }

              ++var36;
              var31 -= var27;
              var32 += var28;
            }
          } else {
            for (var36 = var22; var36 < 0; var23 += var24) {
              label256:
              {
                var37 = var31 + (var29 * var28 >> 4);
                var38 = var32 + (var29 * var27 >> 4);
                var39 = var20;
                if ((var35 = var37 + 4096) < 0) {
                  if (var28 == 0) {
                    var23 -= var20;
                    break label256;
                  }

                  var35 = (var28 - 1 - var35) / var28;
                  var39 = var20 + var35;
                  var37 += var28 * var35;
                  var38 += var27 * var35;
                  var23 += var35;
                }

                if ((var35 = var38 + 4096) < 0) {
                  if (var27 == 0) {
                    var23 -= var39;
                    break label256;
                  }

                  var35 = (var27 - 1 - var35) / var27;
                  var39 += var35;
                  var37 += var28 * var35;
                  var38 += var27 * var35;
                  var23 += var35;
                }

                while (var39 < 0 && (var33 = var37 >> 12) < var0.width && (var34 = var38 >> 12) < var0.height) {
                  a600ie(var0.pixels, var0.width, var0.height, var23, var33, var34, var37, var38);
                  ++var39;
                  var37 += var28;
                  var38 += var27;
                  ++var23;
                }

                var23 -= var39;
              }

              ++var36;
              var31 -= var27;
              var32 += var28;
            }
          }

        }
      }
    }
  }

  private static void a600ie(final int[] var0, final int var1, final int var2, final int var3, final int var4, final int var5, int var6, int var7) {
    final int var8 = var5 * var1 + var4;
    var6 &= 4095;
    var7 &= 4095;
    final int var9;
    final int var10;
    int var13;
    int var14;
    if (var5 >= 0) {
      if (var4 >= 0) {
        var9 = var0[var8];
        var13 = (var9 & -16777216) != 0 ? (4096 - var6) * (4096 - var7) : 0;
      } else {
        var13 = 0;
        var9 = 0;
      }

      if (var4 < var1 - 1) {
        var10 = var0[var8 + 1];
        var14 = (var10 & -16777216) != 0 ? var6 * (4096 - var7) : 0;
      } else {
        var14 = 0;
        var10 = 0;
      }
    } else {
      var14 = 0;
      var13 = 0;
      var10 = 0;
      var9 = 0;
    }

    final int var11;
    final int var12;
    int var15;
    int var16;
    if (var5 < var2 - 1) {
      if (var4 >= 0) {
        var11 = var0[var8 + var1];
        var15 = (var11 & -16777216) != 0 ? (4096 - var6) * var7 : 0;
      } else {
        var15 = 0;
        var11 = 0;
      }

      if (var4 < var1 - 1) {
        var12 = var0[var8 + var1 + 1];
        var16 = (var12 & -16777216) != 0 ? var6 * var7 : 0;
      } else {
        var16 = 0;
        var12 = 0;
      }
    } else {
      var16 = 0;
      var15 = 0;
      var12 = 0;
      var11 = 0;
    }

    var13 >>= 16;
    var14 >>= 16;
    var15 >>= 16;
    var16 >>= 16;
    final int var17 = var13 + var14 + var15 + var16;
    if (var17 >= 128) {
      int var18 = (var9 & 16711935) * var13 + (var10 & 16711935) * var14;
      var18 += (var11 & 16711935) * var15 + (var12 & 16711935) * var16;
      int var19 = ((var9 & -16711936) >>> 8) * var13 + ((var10 & -16711936) >>> 8) * var14;
      var19 += ((var11 & -16711936) >>> 8) * var15 + ((var12 & -16711936) >>> 8) * var16;
      final int var20 = var19 >>> 24;
      var18 = var18 >>> 8 & 16711935;
      var19 &= Drawing.GREEN;
      if (var20 != 0) {
        final int var21 = 256 - var20;
        final int var22 = Drawing.screenBuffer[var3];
        Drawing.screenBuffer[var3] = (var18 * var20 + (var22 & 16711935) * var21 & -16711936) + (var19 * var20 + (var22 & '\uff00') * var21 & 16711680) >>> 8;
      }
    }

  }

  private void c815(final StarSystem var1) {
    this.drawSystemResources(var1);
    this.drawSystemProjectHighlights(var1);
    this.b815(var1);
    if (this.unitScalingFactor < 1024.0F) {
      this.d815(var1);
    }
  }

  private void drawSystems() {
    if (this.unitScalingFactor > 1024.0F) {
      final int maxRemainingGarrison = Arrays.stream(this.map.systems)
          .mapToInt(var5 -> var5.remainingGarrison)
          .filter(var5 -> var5 >= 0)
          .max().orElse(0);
      for (final StarSystem system : this.map.systems) {
        if (isOnScreen(this.systemBounds[system.index])) {
          this.drawSystem(system, maxRemainingGarrison);
          if (system.hasDefensiveNet) {
            this.drawDefensiveNet(system);
          }
        }
      }

      this.drawWormholeConnections();

      for (final StarSystem system : this.map.systems) {
        if (isOnScreen(this.systemBounds[system.index])) {
          Menu.SMALL_FONT.drawCentered(
              Integer.toString(this.clonedRemainingGarrisons[system.index]),
              this.systemDrawX[system.index] - 1,
              this.systemDrawY[system.index] + 3,
              Drawing.WHITE);
        }
      }

      for (final StarSystem system : this.map.systems) {
        if (isOnScreen(this.systemBounds[system.index])) {
          this.a527(system, false);
        }
      }

      this.i150();
    } else {
      for (final StarSystem system : this.map.systems) {
        if (isOnScreen(this.systemBounds[system.index])) {
          this.b549(system);
          this.drawSystemIcon(system);
          if (this.unitScalingFactor < 330.0F && system.hasDefensiveNet) {
            this.drawDefensiveNet(system);
          }
        }
      }

      if (this.unitScalingFactor < 1024.0F) {
        for (final StarSystem var10 : this.map.systems) {
          this.a527(var10, false);
        }
      }

      for (final StarSystem system : this.map.systems) {
        if (isOnScreen(this.systemBounds[system.index])) {
          this.c815(system);
          if (this.unitScalingFactor > 330.0F && system.hasDefensiveNet) {
            this.drawDefensiveNet(system);
          }
        }
      }

      if (this.unitScalingFactor < 1024.0F) {
        for (final StarSystem system : this.map.systems) {
          this.a527(system, true);
        }
      }

      this.drawWormholeConnections();
      this.i150();
    }
  }

  private static boolean isOnScreen(final Rect bounds) {
    return bounds.x1 <= ShatteredPlansClient.SCREEN_WIDTH && bounds.y1 <= ShatteredPlansClient.SCREEN_HEIGHT && bounds.x2 >= 0 && bounds.y2 >= 0;
  }

  private MoveFleetsOrder findSelectedFleetOrder(final StarSystem system, final int x, final int y) {
    if (this.unitScalingFactor > 1024.0F) {
      for (final MoveFleetsOrder order : system.incomingOrders) {
        if (this.localPlayer == order.player) {
          final int midX = (this.systemDrawX[order.target.index] + this.systemDrawX[order.source.index]) / 2;
          final int midY = (this.systemDrawY[order.source.index] + this.systemDrawY[order.target.index]) / 2;
          if (MathUtil.isEuclideanDistanceLessThan(x - midX, y - midY, 9)) {
            this._nb[0] = midX;
            this._nb[1] = midY;
            return order;
          }
        }
      }
    } else {
      final int var5 = (int) system.incomingOrders.stream().filter(var6 -> var6.player == this.localPlayer).count();
      if (var5 != 0) {
        final int var7;
        if (this.unitScalingFactor < 330.0F) {
          var7 = 12 * ((var5 - 1) / 11 + 1);
        } else {
          var7 = 12 * ((var5 - 1) / 12 + 1);
        }

        final boolean[] var8 = new boolean[var7];
        final int[] var9 = new int[var7 * 2];

        for (int var10 = 0; var10 < var7; ++var10) {
          final double var11 = (double) (var10 * 2) * Math.PI / (double) var7;
          if (this.unitScalingFactor < 330.0F && var11 >= 4.4505895925855405D && var11 <= 4.97418836818384D) {
            var8[var10] = true;
            var9[2 * var10] = -1;
            var9[1 + 2 * var10] = -1;
          }

          final double var13 = Math.sin(var11);
          final double var15 = Math.cos(var11);
          var9[var10 * 2] = (int) (var15 * 150.0D * this._wb) + this.systemDrawX[system.index];
          var9[var10 * 2 + 1] = (int) (150.0D * this._wb * var13) + this.systemDrawY[system.index];
        }

        for (final MoveFleetsOrder var6 : system.incomingOrders) {
          if (var6.player == this.localPlayer) {
            final StarSystem var33 = var6.source;
            final StarSystem var34 = var6.target;
            final int var12 = var33.index;
            final int var35 = var34.index;
            final int var14 = this.systemDrawX[var12];
            final int var36 = this.systemDrawY[var12];
            final int var16 = this.systemDrawX[var35];
            final int var17 = this.systemDrawY[var35];
            final double var20 = Math.sqrt((-var36 + var17) * (-var36 + var17) + (-var14 + var16) * (var16 - var14));
            final double var22 = (double) (var16 - var14) / var20;
            final double var24 = (double) (var17 - var36) / var20;
            final int var18 = var16 - (int) (var22 * this.zoomFactor * 150.0D);
            final int var19 = -((int) (this.zoomFactor * 150.0D * var24)) + var17;
            final int var26 = this.a357(var18, var9, var8, var36, var24, var19, var14, var22);
            final int i = var9[2 * var26 + 1];
            final int i1 = var9[var26 * 2];
            if (this.unitScalingFactor < 700.0F && var7 <= 12) {
              if (ARROW_SHIP.height * ARROW_SHIP.height / 4 >= MathUtil.euclideanDistanceSquared(-i1 + x, -i + y)) {
                this._nb[0] = i1;
                this._nb[1] = i;
                return var6;
              }
            } else {
              final int var27 = var14 + (int) ((double) this._Hb * var22);
              final int var28 = var36 + (int) ((double) this._Hb * var24);
              int var29 = (i1 + var27) / 2;
              int var30 = (var28 + i) / 2;
              final double sqrt = Math.sqrt((var28 - i) * (-i + var28) + (var27 - i1) * (-i1 + var27));
              var29 = (int) ((double) var29 + -var24 * sqrt / 10.0D);
              var30 = (int) ((double) var30 + var22 * sqrt / 10.0D);
              if ((-var30 + y) * (y - var30) + (-var29 + x) * (-var29 + x) < 81) {
                this._nb[0] = var29;
                this._nb[1] = var30;
                return var6;
              }
            }
          }
        }
      }
    }
    return null;
  }

  private void i150() {
    if (this._rb != null) {
      if (this.unitScalingFactor <= 1024.0F) {
        final int var2 = (int) this._rb.target.incomingOrders.stream()
            .filter(var3 -> this.localPlayer == var3.player).count();
        if (var2 == 0) {
          return;
        }

        final int var4;
        if (this.unitScalingFactor >= 330.0F) {
          var4 = 12 + (var2 - 1) / 12 * 12;
        } else {
          var4 = 12 * ((var2 - 1) / 11) + 12;
        }

        int var5 = 0;
        int var6 = 0;
        int var15;
        int var16;
        if (this.unitScalingFactor < 700.0F && var4 <= 12) {
          var5 = this._ub[0];
          var6 = this._ub[1];
        } else {
          final boolean[] var7 = new boolean[var4];
          final int[] var8 = new int[2 * var4];

          for (int var9 = 0; var4 > var9; ++var9) {
            final double var10 = Math.PI * (double) (var9 * 2) / (double) var4;
            if (this.unitScalingFactor < 330.0F && var10 >= 4.4505895925855405D && var10 <= 4.97418836818384D) {
              var7[var9] = true;
              var8[var9 * 2] = -1;
              var8[var9 * 2 + 1] = -1;
            }

            final double var12 = Math.sin(var10);
            final double var14 = Math.cos(var10);
            var8[2 * var9] = (int) (150.0D * this._wb * var14) + this.systemDrawX[this._rb.target.index];
            var8[1 + var9 * 2] = (int) (150.0D * this._wb * var12) + this.systemDrawY[this._rb.target.index];
          }

          for (final MoveFleetsOrder var3 : this._rb.target.incomingOrders) {
            if (var3.player == this.localPlayer) {
              final StarSystem var29 = var3.source;
              final StarSystem var31 = var3.target;
              final int var11 = var29.index;
              final int var33 = var31.index;
              final int var13 = this.systemDrawX[var11];
              final int var35 = this.systemDrawY[var11];
              var15 = this.systemDrawX[var33];
              var16 = this.systemDrawY[var33];
              final double var17 = Math.sqrt((var16 - var35) * (var16 - var35) + (-var13 + var15) * (var15 - var13));
              final double var19 = (double) (var15 - var13) / var17;
              var5 = var15 - (int) (var19 * 150.0D * this.zoomFactor);
              final double var21 = (double) (var16 - var35) / var17;
              var6 = var16 - (int) (this.zoomFactor * 150.0D * var21);
              final int var23 = this.a357(var5, var8, var7, var35, var21, var6, var13, var19);
              var5 = var8[var23 * 2];
              var6 = var8[var23 * 2 + 1];
              if (var3 == this._rb) {
                break;
              }
            }
          }
        }

        final int var25 = this.systemDrawX[this._rb.source.index];
        final int var27 = this.systemDrawY[this._rb.source.index];
        final double var30 = Math.sqrt((-var27 + var6) * (var6 - var27) + (-var25 + var5) * (-var25 + var5));
        final double var32 = (double) (-var25 + var5) / var30;
        final double var34 = (double) (-var27 + var6) / var30;
        var15 = (int) (var32 * (double) this._Hb) + var25;
        var16 = var27 + (int) ((double) this._Hb * var34);
        int var37;
        if (var32 != 0.0D) {
          if (var34 != 0.0D) {
            double var18 = -var27 + var6;
            if (var18 < 0.0D) {
              var18 = -var18;
            }

            var37 = (int) (32768.0D * Math.asin(var18 / var30) / Math.PI);
            if (var32 >= 0.0D) {
              if (var34 > 0.0D) {
                var37 = 65536 - var37;
              }
            } else if (var34 < 0.0D) {
              var37 = '耀' - var37;
            } else {
              var37 += 32768;
            }
          } else if (var32 <= 0.0D) {
            var37 = 32768;
          } else {
            var37 = 0;
          }
        } else if (var34 > 0.0D) {
          var37 = 49152;
        } else {
          var37 = 16384;
        }

        if (var30 <= 1.0D + (this.zoomFactor * 300.0D + 0.5D - this._wb * 150.0D)) {
          if (var37 == 0) {
            this._Cb.draw(4 + var5 - this._Cb.width, var16 - 4);
          } else if (var37 == 32768) {
            this._Cb.i093(1 + (var5 - 4), 5 + var16 - this._Cb.height);
          } else if (var37 >= 16384) {
            if (var37 > 16384 && var37 < 32768) {
              this._Ob.draw(var5 - 4, var6 - 4);
            } else if (var37 > 32768 && var37 < 49152) {
              this._K.i093(var5 + 1 - 4, 5 - this._K.height + var6);
            } else if (var37 > 49152) {
              this._Ob.i093(5 - this._Ob.width + var5, 1 + (4 - this._Ob.height) + var6);
            }
          } else {
            this._K.draw(-this._K.width + var5 + 4, var6 - 4);
          }
        } else {
          a070eo(false, var16, var6, TICKS_PER_ANIMATION_PHASE, var5, var15);
        }

        if (this.unitScalingFactor < 700.0F && var4 <= 12) {
          Drawing.strokeCircle(var5, var6, 30, Drawing.WHITE);
          Drawing.fillCircle(var5, var6, 30, Drawing.WHITE, 92);
          a194ie((ArgbSprite) this.localPlayer._n, ARROW_SHIP.width << 3, ARROW_SHIP.height << 3, var5 * 16, var6 * 16, 3800 + (var37 - 16384), 4096);
          Drawing.fillCircle(var5, var6, 9, 0, 92);
          Menu.SMALL_FONT.drawCentered(Integer.toString(this._rb.quantity), var5, var6 + 4, Drawing.WHITE);
          FLEET_BUTTONS[0].draw(30 + (var5 - FLEET_BUTTONS[0].width / 2), -(FLEET_BUTTONS[0].height / 2) + var6);
          FLEET_BUTTONS[1].draw(var5 - FLEET_BUTTONS[0].width / 2 - 30, var6 - FLEET_BUTTONS[0].height / 2);
          FLEET_BUTTONS[2].draw(-(FLEET_BUTTONS[0].width / 2) + var5, var6 - (FLEET_BUTTONS[0].height / 2 - 30));
        } else {
          final int var38 = this._ub[0];
          int var39 = this._ub[1];
          --var39;
          Drawing.strokeCircle(var38, var39, 30, Drawing.WHITE);
          Drawing.fillCircle(var38, var39, 30, Drawing.WHITE, 92);
          a194ie((ArgbSprite) this.localPlayer._n, ARROW_SHIP.width << 3, ARROW_SHIP.height << 3, 16 * var38, 16 * var39, var37 - 16384, 4096);
          Drawing.fillCircle(var38, var39, 9, 0, 92);
          Menu.SMALL_FONT.drawCentered(Integer.toString(this._rb.quantity), var38, var39 + 4, Drawing.WHITE);
          FLEET_BUTTONS[0].draw(30 + var38 - FLEET_BUTTONS[0].width / 2, -(FLEET_BUTTONS[0].height / 2) + var39);
          FLEET_BUTTONS[1].draw(var38 - 30 - FLEET_BUTTONS[0].width / 2, var39 - FLEET_BUTTONS[0].height / 2);
          FLEET_BUTTONS[2].draw(-(FLEET_BUTTONS[0].width / 2) + var38, 30 + (var39 - FLEET_BUTTONS[0].height / 2));
        }
      } else {
        final int var2 = this._ub[0];
        int var24 = this._ub[1];
        --var24;
        final int var4 = this.systemDrawX[this._rb.target.index] - this.systemDrawX[this._rb.source.index];
        final int var5 = this.systemDrawY[this._rb.target.index] - this.systemDrawY[this._rb.source.index];
        int var6;
        if (var4 != 0) {
          if (var5 == 0) {
            if (var4 <= 0) {
              var6 = 32768;
            } else {
              var6 = 0;
            }
          } else {
            int var25 = var5;
            final double var28 = Math.sqrt(MathUtil.euclideanDistanceSquared(var4, var5));
            if (var5 < 0) {
              var25 = -var5;
            }

            var6 = (int) (Math.asin((double) var25 / var28) * 32768.0D / Math.PI);
            if (var4 < 0) {
              if (var5 < 0) {
                var6 = -var6 + '耀';
              } else {
                var6 += 32768;
              }
            } else if (var5 > 0) {
              var6 = 65536 - var6;
            }
          }
        } else if (var5 <= 0) {
          var6 = 16384;
        } else {
          var6 = 49152;
        }

        Drawing.strokeCircle(var2, var24, 30, Drawing.WHITE);
        Drawing.fillCircle(var2, var24, 30, Drawing.WHITE, 92);
        a194ie((ArgbSprite) this.localPlayer._n, ARROW_SHIP.width << 3, ARROW_SHIP.height << 3, var2 * 16, 16 * var24, var6 - 16384, 4096);
        Drawing.fillCircle(var2, var24, 9, 0, 92);
        Menu.SMALL_FONT.drawCentered(Integer.toString(this._rb.quantity), var2, 4 + var24, Drawing.WHITE);
        FLEET_BUTTONS[0].draw(var2 + (30 - FLEET_BUTTONS[0].width / 2), -(FLEET_BUTTONS[0].height / 2) + var24);
        FLEET_BUTTONS[1].draw(-(FLEET_BUTTONS[0].width / 2) + (var2 - 30), var24 - FLEET_BUTTONS[0].height / 2);
        FLEET_BUTTONS[2].draw(var2 - FLEET_BUTTONS[0].width / 2, -(FLEET_BUTTONS[0].height / 2) + 30 + var24);
      }

    }
  }

  public void tick(final Collection<TurnEventLog.Event> turnEvents) {
    if (this.isAnimatingViewport) {
      final float var3 = this.targetScrollPosnX - this.mapScrollPosnX;
      final float var4 = this.targetScrollPosnY - this.mapScrollPosnY;
      final float var5 = this.targetZoomFactor - this.unitScalingFactor;
      this.isAnimatingViewport = Math.abs(var3) > 1.0F || Math.abs(var4) > 1.0F || Math.abs(var5) > 1.0F;
      this.mapScrollPosnX += 0.1F * var3;
      this.mapScrollPosnY += 0.1F * var4;
      this.unitScalingFactor += 0.1F * var5;
    }

    this.a487();

    for (final CombatExplosion explosion : this.combatExplosions) {
      explosion.ticksAlive++;
      final int index = explosion.system.index;
      final int dx = this.systemDrawX[index] - ShatteredPlansClient.SCREEN_CENTER_X;
      final int dy = this.systemDrawY[index] - ShatteredPlansClient.SCREEN_CENTER_Y;
      final float distance = MathUtil.euclideanDistance(dx, dy, this.unitScalingFactor) / 300.0F;
      explosion.sound.setVolume((int) ((float) (Sounds.soundVolume * Sounds.SFX_EXPLOSION.volume << 6) / (distance * 96.0F)));
    }
    this.combatExplosions.removeIf(explosion -> explosion.ticksAlive == CombatExplosion.LIFETIME);

    this.tickAnimations(turnEvents);
    this.gameUI.tick();

    uiPulseCounter = (ShatteredPlansClient.currentTick % 64) * 8;
    if (uiPulseCounter > 256) {
      uiPulseCounter = 512 - uiPulseCounter;
    }
  }

  private void a679() {
    for (final CombatEngagementAnimationState var4 : this.combatEngagements) {
      final StarSystem var6 = var4.system;
      final int var7 = var6.index;
      final byte var8 = 20;
      if (this.unitScalingFactor >= 1024.0F) {
        final int var10 = -10 + this.systemDrawY[var7];
        final int var26 = this.animationTick / 20 % var4.players.length;
        final int var9 = this.systemDrawX[var7] - (10);
        final Player var27 = var4.players[var26];
        final Sprite var11 = var27 == null ? ShatteredPlansClient._wab : var27._e;
        var11.c115(var9 - 1, var10 - 1, var8, var8);
        final Sprite bi_ = var4.ownerAtCombatStart == null ? ShatteredPlansClient._fmb : var4.ownerAtCombatStart._o;
        bi_.draw(-1 - (bi_.width >> 1) + var8 + var9, -(bi_.height >> 1) + (var8 + var10 - 1));
      } else {
        drawSystemHex(this.systemHexes[var6.index], this.clonedSystemOwners[var6.index] != null ? this.clonedSystemOwners[var6.index].darkColor : 4210752, 160);
        final Player[] var12 = new Player[var4.players.length];
        final int[] var13 = new int[var4.players.length];

        for (int var14 = 0; var14 < var12.length; ++var14) {
          var13[var14] = var4.players[var14] == null ? var4.fleetsAtCombatStart[var4.fleetsAtCombatStart.length - 1] : var4.fleetsAtCombatStart[var14];
          var12[var14] = var4.players[var14];
        }

        ArrayUtil.sortScored(var12, var13);
        final Player[] var28 = new Player[var12.length];
        final int var15 = (var28.length - 1) / 2;
        var28[var15] = var12[0];
        boolean var16 = true;
        int var17 = 1;

        int var18;
        for (var18 = 1; var12.length > var18; ++var18) {
          var28[(var16 ? var17 : -var17) + var15] = var12[var18];
          if (!var16) {
            ++var17;
          }

          var16 = !var16;
        }

        var18 = (int) (this._wb * 200.0D);
        int var19 = (int) (345.0D * this._wb);
        if (var19 > 30 * var12.length) {
          var19 = 30 * var12.length;
        }

        final int var20 = -(var19 / 2) + this.systemDrawX[var6.index];
        final byte var21 = 5;
        final int var22 = this.unitScalingFactor >= 700.0F ? 0 : 10;
        Drawing.fillRoundedRect(var20 - 2, -(var18 / 2) + this.systemDrawY[var6.index] - 5, 4 + var19, 10 + var18 + var22, var21, 0, 128);
        var19 /= var12.length;
        final int var23 = (int) ((double) this.systemDrawY[var6.index] + 0.5D * (double) var18);

        int var24;
        for (var24 = 0; var28.length > var24; ++var24) {
          this.a323(var28[var24], var23, var19, var18, var20 + var24 * var19, var4);
        }

        if (this.unitScalingFactor < 700.0F) {
          for (var24 = 0; var24 < var28.length; ++var24) {
            int var25 = var28[var24] != null ? var4.fleets[var28[var24].index] : var4.fleets[var4.fleets.length - 1];
            if (var28[var24] == var4.victor) {
              var25 += var4.fleetsAtCombatEnd;
            }

            if (var28[var24] == var4.selectedPlayer && var25 < var4.fleetsAtCombatStart[var28[var24] == null ? var4.fleets.length - 1 : var28[var24].index] && var4.fleets[var4.victor == null ? var4.fleets.length - 1 : var4.victor.index] > 0) {
              ++var25;
            }

            Menu.SMALL_FONT.drawCentered(Integer.toString(var25), var19 / 2 + var20 + var19 * var24, Menu.SMALL_FONT.ascent / 2 + 5 + var23, Drawing.WHITE);
          }
        }
      }
    }

  }

  private void drawDefensiveNet(final StarSystem system) {
    byte var3 = 0;
    byte var4 = 0;
    if (this.unitScalingFactor > 1024.0F) {
      var4 = 1;
      var3 = 1;
    }

    final int var5 = (ShatteredPlansClient.currentTick + 5 * system.index) % 104;
    if (system.type == StarSystem.Type.ALIEN_MINER || system.type == StarSystem.Type.ALIEN_SHIP || system.type == StarSystem.Type.ALIEN_BASE) {
      final int var6 = this._xb[0].offsetX;
      DEFNET_ANIM_LARGE[0].b115(-var3 + (this.systemDrawX[system.index] - var6 / 2), -var4 + this.systemDrawY[system.index] - var6 / 2, var6, var6);
      if (var5 > 4 && var5 <= 48) {
        this._xb[var5 / 4].drawAdd(-var3 - var6 / 2 + this.systemDrawX[system.index], -(var6 / 2) + this.systemDrawY[system.index] - var4, 128);
      }
    } else if (system.type == StarSystem.Type.PLANET_RINGED) {
      final int var6 = this._Kb[0].offsetX;
      DEFNET_ANIM_MID[0].b115(this.systemDrawX[system.index] - (var6 / 2 + var3), -(var6 / 2) + this.systemDrawY[system.index] - var4, var6, var6);
      if (var5 > 4 && var5 <= 48) {
        this._Kb[var5 / 4].drawAdd(-var3 - var6 / 2 + this.systemDrawX[system.index], -var4 - (var6 / 2) + this.systemDrawY[system.index], 128);
      }
    } else {
      final int var6 = this._Mb[0].offsetX;
      DEFNET_ANIM_SMALL[0].b115(-var3 - var6 / 2 + this.systemDrawX[system.index], -var4 - var6 / 2 + this.systemDrawY[system.index], var6, var6);
      if (var5 > 4 && var5 <= 48) {
        this._Mb[var5 / 4].drawAdd(-var3 - var6 / 2 + this.systemDrawX[system.index], this.systemDrawY[system.index] - var6 / 2 - var4, 128);
      }
    }
  }

  private void a527(final StarSystem system, final boolean var1) {
    int var10;
    int var12;
    int var13;
    int var14;
    int var15;
    int var16;
    int var17;
    int var18;
    if (this.unitScalingFactor > 1024.0F) {
      final int[] var5 = new int[]{-14671840, -12566464, -10461088, -8355712};

      for (final MoveFleetsOrder var6 : system.incomingOrders) {
        final StarSystem var7 = var6.source;
        final int var8 = var7.index;
        final int var9 = system.index;
        var10 = this.systemDrawX[var8];
        final int var11 = this.systemDrawY[var8];
        var12 = this.systemDrawX[var9];
        var13 = this.systemDrawY[var9];
        var14 = (int) Math.sqrt(MathUtil.euclideanDistanceSquared(var12 - var10, -var11 + var13)) + 1;

        for (var15 = ShatteredPlansClient.currentTick / 2 % 10; var14 > var15; var15 += 10) {
          var16 = (1 + 2 * var15 * (var12 - var10)) / (var14 * 2) + var10;
          var17 = var11 + (var15 * 2 * (var13 - var11) + 1) / (var14 * 2);
          Drawing.drawCircleGradientAdd(var16 << 4, var17 << 4, 30, 3, var5);
        }

        var15 = (var10 + var12) / 2;
        var16 = (var11 + var13) / 2;
        Drawing.fillCircle(var15, var16 - 1, 9, Drawing.WHITE, 192);
        Menu.SMALL_FONT.drawCentered(Integer.toString(var6.quantity), var15, 3 + var16, 0);
        if (var6 == this._rb) {
          var17 = (var10 + var12) / 2;
          var18 = (var13 + var11) / 2;
          this._ub[0] = var17;
          this._ub[1] = var18;
          if (this._rb == this._Db) {
            this._tb[0] = var17;
            this._tb[1] = var18;
          }
        }
      }

    } else {
      final int var32 = (int) system.incomingOrders.stream().filter(var6 -> this.localPlayer == var6.player).count();
      if (var32 != 0) {
        final int var33;
        if (this.unitScalingFactor < 330.0F) {
          var33 = 12 * (1 + (var32 - 1) / 11);
        } else {
          var33 = (1 + (var32 - 1) / 12) * 12;
        }

        final boolean[] var34 = new boolean[var33];
        final int[] var35 = new int[2 * var33];

        for (var10 = 0; var33 > var10; ++var10) {
          final double var37 = Math.PI * (double) (var10 * 2) / (double) var33;
          if (this.unitScalingFactor < 330.0F && var37 >= 4.4505895925855405D && var37 <= 4.97418836818384D) {
            var34[var10] = true;
            var35[2 * var10] = -1;
            var35[1 + var10 * 2] = -1;
          }

          final double var39 = Math.sin(var37);
          final double var40 = Math.cos(var37);
          var35[2 * var10] = this.systemDrawX[system.index] + (int) (var40 * 150.0D * this._wb);
          var35[1 + 2 * var10] = this.systemDrawY[system.index] + (int) (150.0D * this._wb * var39);
        }

        for (final MoveFleetsOrder var6 : system.incomingOrders) {
          if (this.localPlayer == var6.player) {
            final StarSystem var36 = var6.source;
            final StarSystem var38 = var6.target;
            var12 = var36.index;
            var13 = var38.index;
            var14 = this.systemDrawX[var12];
            var15 = this.systemDrawY[var12];
            var16 = this.systemDrawX[var13];
            var17 = this.systemDrawY[var13];
            final double var22 = Math.sqrt((var16 - var14) * (-var14 + var16) + (-var15 + var17) * (var17 - var15));
            final double var24 = (double) (-var14 + var16) / var22;
            final int var20 = -((int) (150.0D * this.zoomFactor * var24)) + var16;
            final double var26 = (double) (-var15 + var17) / var22;
            final int var21 = var17 - (int) (var26 * 150.0D * this.zoomFactor);
            final int var28 = this.a357(var20, var35, var34, var15, var26, var21, var14, var24);
            final int i = var35[2 * var28];
            final int i1 = var35[2 * var28 + 1];
            double sqrt = Math.sqrt((i - var14) * (-var14 + i) + (i1 - var15) * (-var15 + i1));
            final double v = (double) (i1 - var15) / sqrt;
            final double v1 = (double) (-var14 + i) / sqrt;
            final int var19 = var15 + (int) ((double) this._Hb * v);
            var18 = var14 + (int) ((double) this._Hb * v1);
            int var41;
            int var42;
            if (var6 == this._rb) {
              if (this.unitScalingFactor < 700.0F && var33 <= 12) {
                this._ub[1] = i1;
                this._ub[0] = i;
                if (this._Db == this._rb) {
                  this._tb[0] = i;
                  this._tb[1] = i1;
                }
              } else {
                var41 = (i + var18) / 2;
                sqrt = Math.sqrt(MathUtil.euclideanDistanceSquared(-i + var18, var19 - i1));
                var42 = (i1 + var19) / 2;
                var41 = (int) ((double) var41 + sqrt * -v / 10.0D);
                var42 = (int) ((double) var42 + sqrt * v1 / 10.0D);
                this._ub[0] = var41;
                this._ub[1] = var42;
                if (this._rb == this._Db) {
                  this._tb[0] = var41;
                  this._tb[1] = var42;
                }
              }
              continue;
            }

            if (v1 == 0.0D) {
              if (v <= 0.0D) {
                var41 = 16384;
              } else {
                var41 = 49152;
              }
            } else if (v == 0.0D) {
              if (v1 <= 0.0D) {
                var41 = 32768;
              } else {
                var41 = 0;
              }
            } else {
              double var30 = -var15 + i1;
              if (var30 < 0.0D) {
                var30 = -var30;
              }

              var41 = (int) (Math.asin(var30 / sqrt) * 32768.0D / Math.PI);
              if (v1 >= 0.0D) {
                if (v > 0.0D) {
                  var41 = -var41 + 65536;
                }
              } else if (v >= 0.0D) {
                var41 += 32768;
              } else {
                var41 = -var41 + '耀';
              }
            }

            if (var1) {
              if (this.unitScalingFactor < 700.0F && var33 <= 12) {
                a194ie((ArgbSprite) this.localPlayer._n, ARROW_SHIP.width << 3, ARROW_SHIP.height << 3, 16 * i, i1 * 16, var41 + 3800 - 16384, 4096);
                Drawing.fillCircle(i, i1, 9, 0, 92);
                Menu.SMALL_FONT.drawCentered(Integer.toString(var6.quantity), i, 4 + i1, Drawing.WHITE);
              } else {
                Drawing.drawCircleGradientAdd(16 * i, i1 * 16, 60, _rj.length - 1, new int[]{-65536, -65536, 0, -65536, -65536});
              }

              if (this.unitScalingFactor > 700.0F) {
                var42 = (i + var18) / 2;
                int var31 = (i1 + var19) / 2;
                var42 -= (int) (v * (sqrt / 10.0D));
                var31 += (int) (v1 * (sqrt / 10.0D));
                Drawing.fillCircle(var42, var31 - 1, 9, Drawing.WHITE, 192);
                Menu.SMALL_FONT.drawCentered(Integer.toString(var6.quantity), var42, 3 + var31, 0);
              }
            } else if (sqrt > 1.0D + (0.5D + this.zoomFactor * 300.0D - 150.0D * this._wb)) {
              a070eo(false, var19, i1, 200, i, var18);
            } else if (var41 == 0) {
              this._Cb.draw(-this._Cb.width + i + 4, var19 - 4);
            } else if (var41 == 32768) {
              this._Cb.i093(i - 3, 4 + var19 - this._Cb.height + 1);
            } else if (var41 >= 16384) {
              if (var41 > 16384 && var41 < 32768) {
                this._Ob.draw(i - 4, i1 - 4);
              } else if (var41 > 32768 && var41 < 49152) {
                this._K.i093(1 + i - 4, -this._K.height + i1 + 5);
              } else if (var41 > 49152) {
                this._Ob.i093(4 - this._Ob.width + i + 1, 4 + i1 - (this._Ob.height - 1));
              }
            } else {
              this._K.draw(4 + (i - this._K.width), i1 - 4);
            }
          }
        }
      }
    }
  }

  private void drawSystem(final StarSystem system, final int maxGarrison) {
    final Player owner = this.clonedSystemOwners[system.index];

    final int baseColor = owner == null ? 0x808080 : owner.color2;
    final int color = Drawing.alphaOver(0, baseColor, 160 - (this.clonedRemainingGarrisons[system.index] * 160 / maxGarrison));
    drawSystemHex(this.systemHexes[system.index], color, 160);

    if (this.animationPhase == AnimationPhase.NOT_PLAYING) {
      this.drawSystemHexWithHatching(system);
    }
    this.drawSystemProjectHighlights(system);

    if (system.type >= StarSystem.Type.ALIEN_MINER) {
      this.drawSystemIcon(system);
      final int radius = Math.max(Menu.SMALL_FONT.measureLineWidth(Integer.toString(this.clonedRemainingGarrisons[system.index])) + 1, 10);
      Drawing.fillCircle(
          this.systemDrawX[system.index] - 1,
          this.systemDrawY[system.index] - 1,
          radius,
          Drawing.BLACK,
          128);
    }

    if (system.name.equalsIgnoreCase("sol")) {
      this.drawSystemIcon(system);
    }
  }

  public void a073(final Player[] var2) {
    this._ib = var2;
    this._cb = new int[this._ib.length][];
    this.j150();
  }

  private void a662(final List<MoveFleetsAnimationState> var1, final boolean var2, final boolean var3) {
    if (this.animationTick <= TICKS_PER_ANIMATION_PHASE) {

      for (final MoveFleetsAnimationState var5 : var1) {
        final int var6 = this.systemDrawX[var5.target.index];
        final int var7 = this.systemDrawY[var5.target.index];
        final int var8 = this.systemDrawX[var5.source.index];
        final int var9 = this.systemDrawY[var5.source.index];
        final double var10 = Math.sqrt((var6 - var8) * (-var8 + var6) + (var7 - var9) * (var7 - var9));
        final double var12 = (double) (var6 - var8) / var10;
        final double var14 = (double) (-var9 + var7) / var10;
        final int var16 = (int) (var12 * (double) this._Hb) + var8;
        final int var17 = (int) (var14 * (double) this._Hb) + var9;
        final int var18 = -((int) (var12 * (double) this._Hb)) + var6;
        final int var19 = var7 - (int) (var14 * (double) this._Hb);
        if (var2) {
          final int var20 = this.animationTick * (-var16 + var18) * 16 / TICKS_PER_ANIMATION_PHASE + var16 * 16;
          final int var21 = (-var17 + var19) * 16 * this.animationTick / TICKS_PER_ANIMATION_PHASE + 16 * var17;
          final double var22 = Math.sqrt((var21 - 16 * var17) * (var21 - var17 * 16) + (var20 - 16 * var16) * (-(16 * var16) + var20));
          final double sqrt = Math.sqrt((-var17 + var19) * 256 * (-var17 + var19) + (-var16 + var18) * (var18 - var16) * 256);
          final double var24 = sqrt / 2.0D;
          final double var26 = 1.0D / var24;
          final double var28 = sqrt / 10.0D - (var22 - var24) * (-var24 + var22) * var26 * 0.2D;
          double var30 = (-var19 + var17) * 16;
          double var32 = (var16 - var18) * 16;
          var30 /= sqrt;
          var32 /= sqrt;
          final int var34 = var20 + (int) (var28 * var30);
          final int var35 = -((int) (var32 * var28)) + var21;
          int var39;
          if (var12 != 0.0D) {
            if (var14 != 0.0D) {
              double var37 = (var19 - var17) * 16;
              if (var37 < 0.0D) {
                var37 = -var37;
              }

              var39 = (int) (32768.0D * Math.asin(var37 / sqrt) / Math.PI);
              if (var12 < 0.0D) {
                if (var14 >= 0.0D) {
                  var39 += 32768;
                } else {
                  var39 = -var39 + '耀';
                }
              } else if (var14 > 0.0D) {
                var39 = -var39 + 65536;
              }
            } else if (var12 <= 0.0D) {
              var39 = 32768;
            } else {
              var39 = 0;
            }
          } else if (var14 <= 0.0D) {
            var39 = 16384;
          } else {
            var39 = 49152;
          }

          var39 -= 16384;
          var39 = var39 + 7600 * this.animationTick / TICKS_PER_ANIMATION_PHASE - 3800;
          final ArgbSprite var40 = (ArgbSprite) (var3 ? var5.player._b : var5.player._n);
          a194ie(var40, ARROW_SHIP.width << 3, ARROW_SHIP.height << 3, var34, var35, var39, 4096);
          Drawing.fillCircle(var34 >> 4, var35 >> 4, 9, 0, 92);
          Menu.SMALL_FONT.drawCentered(Integer.toString(var5.quantity), var34 >> 4, (var35 >> 4) + 4, Drawing.WHITE);
        } else {
          a070eo(false, var17, var19, this.animationTick, var18, var16);
        }
      }

    }
  }

  private void j150() {
    Drawing.saveContext();

    for (int var2 = 0; this._ib.length > var2; ++var2) {
      final int var3 = this._ib[var2].color1;
      this._ib[var2]._v = new ArgbSprite(FLEETS_ARROW_SHIP.offsetX, FLEETS_ARROW_SHIP.offsetY);
      this._ib[var2]._v.installForDrawing();
      FLEETS_ARROW_SHIP.drawTinted2(0, 0, var3);

      int var4;
      for (var4 = 0; var4 < FLEETS_ARROW_SHIP.pixels.length; ++var4) {
        this._ib[var2]._v.pixels[var4] = (-16777216 & FLEETS_ARROW_SHIP.pixels[var4]) + (Drawing.WHITE & this._ib[var2]._v.pixels[var4]);
      }

      this._ib[var2]._n = new ArgbSprite(ARROW_SHIP.offsetX, ARROW_SHIP.offsetY);
      this._ib[var2]._n.installForDrawing();
      ARROW_SHIP.drawTinted(0, 0, var3);

      for (var4 = 0; var4 < ARROW_SHIP.pixels.length; ++var4) {
        this._ib[var2]._n.pixels[var4] = (-16777216 & ARROW_SHIP.pixels[var4]) + (this._ib[var2]._n.pixels[var4] & Drawing.WHITE);
      }

      this._ib[var2]._b = new ArgbSprite(ARROW_SHIP_DAMAGED.offsetX, ARROW_SHIP_DAMAGED.offsetY);
      this._ib[var2]._b.installForDrawing();
      ARROW_SHIP_DAMAGED.drawTinted(0, 0, var3);

      for (var4 = 0; ARROW_SHIP_DAMAGED.pixels.length > var4; ++var4) {
        this._ib[var2]._b.pixels[var4] = (Drawing.WHITE & this._ib[var2]._b.pixels[var4]) + (ARROW_SHIP_DAMAGED.pixels[var4] & -16777216);
      }

      this._ib[var2]._o = new Sprite(10, 12);
      this._ib[var2]._o.installForDrawing();
      SHIELD.drawTinted2(1, 1, var3);
      this._ib[var2]._p = new Sprite(12, 12);
      this._ib[var2]._p.installForDrawing();
      DEFENSE_GRID.drawTinted2(1, 1, var3);
      this._ib[var2]._r = new Sprite(12, 12);
      this._ib[var2]._r.installForDrawing();
      CHEVRON.drawTinted2(1, 1, var3);
      this._ib[var2]._e = new Sprite(128, 128);
      this._ib[var2]._e.installForDrawing();
      WARNING.drawTinted(0, 0, var3);

      for (var4 = 0; var4 < this._ib[var2]._e.pixels.length; ++var4) {
        if (this._ib[var2]._e.pixels[var4] != 0) {
          Drawing.addPixel(var4 % this._ib[var2]._e.width, var4 / this._ib[var2]._e.width, this._ib[var2].color1, 128);
          this._ib[var2]._e.pixels[var4] = this._ib[var2]._e.pixels[var4] | -16777216;
        }
      }

      this._ib[var2]._c = new Sprite(18, 12);
      this._ib[var2]._c.installForDrawing();
      HAMMER.drawTinted2(1, 1, var3);
      this._ib[var2]._d = new Sprite(16, 10);
      this._ib[var2]._d.installForDrawing();
      HAMMER.drawTinted2(0, 0, var3);
      this._ib[var2]._o.f150(Drawing.WHITE);
      this._ib[var2]._p.f150(Drawing.WHITE);
      this._ib[var2]._r.f150(Drawing.WHITE);
      this._ib[var2]._c.f150(Drawing.WHITE);
      this._cb[var2] = new int[256];

      for (var4 = 0; var4 < 256; ++var4) {
        int var5 = var3 & 16711935;
        int var6 = '\uff00' & var3;
        var5 *= var4;
        var6 *= var4;
        var5 &= -16711936;
        var6 &= 16711680;
        this._cb[var2][var4] = (var6 | var5) >>> 8;
      }
    }

    Drawing.restoreContext();
  }

  private void a140(final StarSystem system) {
    final int index = system.index;
    final Player owner = this.clonedSystemOwners[index];
    if (owner == null) {
      if (system == this.targetedSystem) {
        this._Ib.draw((int) (-(this._wb * 345.0D / 2.0D) + (double) (1 + this.systemDrawX[index])), (int) (-(400.0D * this._wb / 2.0D) + (double) (1 + this.systemDrawY[index])));
      } else {
        this._ob.draw((int) (-(this._wb * 345.0D / 2.0D) + (double) (1 + this.systemDrawX[index])), (int) (-(this._wb * 400.0D / 2.0D) + (double) (1 + this.systemDrawY[index])));
      }
    } else if (system == this.targetedSystem) {
      this._Ib.drawTinted2((int) (-(345.0D * this._wb / 2.0D) + (double) (this.systemDrawX[index] + 1)), (int) ((double) (this.systemDrawY[index] + 1) - this._wb * 400.0D / 2.0D), owner.color2);
    } else {
      this._ob.drawTinted2((int) ((double) (this.systemDrawX[index] + 1) - 345.0D * this._wb / 2.0D), (int) (-(this._wb * 400.0D / 2.0D) + (double) (this.systemDrawY[index] + 1)), owner.color2);
      if (this.isTutorial && TutorialState._jau) {
        this._Ib.draw((int) ((double) (this.systemDrawX[index] + 1) - 345.0D * this._wb / 2.0D), (int) ((double) (this.systemDrawY[index] + 1) - 400.0D * this._wb / 2.0D), uiPulseCounter);
      }
    }
  }

  private void b549(final StarSystem system) {
    if (this.animationPhase == AnimationPhase.NOT_PLAYING) {
      this.drawSystemHexWithHatching(system);
    }
    this.a140(system);
  }

  private int a357(final int var1, final int[] var2, final boolean[] var3, final int var4, final double var5, final int var8, final int var9, final double var10) {
    int var12 = -1;
    int var13 = Integer.MAX_VALUE;

    int var14;
    int var15;
    double var16;
    for (var14 = 0; var3.length > var14; ++var14) {
      if (!var3[var14]) {
        var15 = (var8 - var2[var14 * 2 + 1]) * (-var2[2 * var14 + 1] + var8) + (var1 - var2[2 * var14]) * (-var2[var14 * 2] + var1);
        var16 = (-((double) var4 * var10) + (double) var9 * var5 + (double) var2[1 + var14 * 2] * var10 - var5 * (double) var2[var14 * 2]) / (var5 * var5 + var10 * var10);
        if (var16 >= -2.0 && (var15 < var13 || (double) var15 < (double) var13 && var16 < (-(var10 * (double) var4) + var5 * (double) var9 + var10 * (double) var2[2 * var12 + 1] - (double) var2[2 * var12] * var5) / (var5 * var5 + var10 * var10))) {
          var12 = var14;
          var13 = var15;
        }
      }
    }

    if (var12 == -1) {
      for (var14 = 0; var3.length > var14; ++var14) {
        if (!var3[var14]) {
          var15 = (-var2[2 * var14] + var1) * (-var2[var14 * 2] + var1) + (var8 - var2[var14 * 2 + 1]) * (-var2[var14 * 2 + 1] + var8);
          if ((double) var13 * 0.35D > (double) var15) {
            var12 = var14;
            var13 = var15;
          }
        }
      }
    }

    if (var12 == -1) {
      throw new RuntimeException("Unable to anchor incoming fleet.");
    } else {
      var3[var12] = true;
      return var12;
    }
  }

  public void c326() {
    if (this.selectedFleetOrder != null && this._Db == this.selectedFleetOrder) {
      if (this._yb < 15 && this.unitScalingFactor < 700.0F && ++this._yb == 15) {
        this._ub[0] = this._tb[0];
        this._ub[1] = this._tb[1];
        this._rb = this._Db;
      }
    } else if (this._yb > 0) {
      if (--this._yb == 0) {
        this._rb = null;
        this._ub[1] = -1;
        this._Db = this.selectedFleetOrder;
        this._ub[0] = -1;
        this._tb[0] = this._nb[0];
        this._tb[1] = this._nb[1];
      }
    } else {
      this._Db = this.selectedFleetOrder;
      this._tb[0] = this._nb[0];
      this._tb[1] = this._nb[1];
    }
  }

  private void drawSystemResources(final StarSystem system) {
    if (system.type != StarSystem.Type.ALIEN_MINER && system.type != StarSystem.Type.ALIEN_SHIP && system.type != StarSystem.Type.ALIEN_BASE) {
      final int index = system.index;

      final byte var4 = 24;
      if (this.unitScalingFactor < 330.0F) {
        final double var5 = TWO_HUNDRED_F * (float) this._Hb / ((float) this._n * this.unitScalingFactor);
        this.resourceLightSprites[0][system.resources[0]].b115(
            this.systemDrawX[index] - ((int) (var5 * 35.0D)) - this._Hb,
            this.systemDrawY[index] - ((int) (var5 * (double) this.resourceLightSprites[0][0].height) / 2),
            (int) ((double) this.resourceLightSprites[0][0].width * var5),
            (int) ((double) this.resourceLightSprites[0][0].height * var5));
        this.resourceLightSprites[1][system.resources[1]].b115(
            this.systemDrawX[index] - ((int) (COS_THIRTY_DEGREES * var5 * 32.0D)) - this._Hb,
            this.systemDrawY[index] + (int) (var5 * 33.0D),
            (int) ((double) this.resourceLightSprites[1][0].width * var5),
            (int) ((double) this.resourceLightSprites[1][0].height * var5));
        this.resourceLightSprites[2][system.resources[2]].b115(
            this.systemDrawX[index] - this._Hb + (int) (COS_THIRTY_DEGREES * 62.0D * var5) - 2,
            (int) (33.0D * var5) + this.systemDrawY[index],
            (int) ((double) this.resourceLightSprites[2][0].width * var5),
            (int) ((double) this.resourceLightSprites[2][0].height * var5));
        this.resourceLightSprites[3][system.resources[3]].b115(
            this.systemDrawX[index] + this._Hb + (int) (var5 * 4.0D),
            this.systemDrawY[index] - ((int) (var5 * (double) this.resourceLightSprites[3][0].height) / 2),
            (int) ((double) this.resourceLightSprites[3][0].width * var5),
            (int) ((double) this.resourceLightSprites[3][0].height * var5));
        if (this.isTutorial && TutorialState._tdL) {
          RES_SIDES[system.resources[0]].b050(-((int) (var5 * 35.0D)) - this._Hb + this.systemDrawX[index], -((int) (var5 * (double) this.resourceLightSprites[0][0].height) / 2) + this.systemDrawY[index], (int) ((double) this.resourceLightSprites[0][0].width * var5), (int) (var5 * (double) this.resourceLightSprites[0][0].height), uiPulseCounter);
          RES_LOWS[system.resources[1]].b050(this.systemDrawX[index] + (-this._Hb - (int) (COS_THIRTY_DEGREES * 32.0D * var5)), this.systemDrawY[index] + (int) (33.0D * var5), (int) (var5 * (double) this.resourceLightSprites[1][0].width), (int) ((double) this.resourceLightSprites[1][0].height * var5), uiPulseCounter);
          ArgbSprite var7 = (ArgbSprite) RES_LOWS[system.resources[2]];
          var7 = var7.copy();
          var7.flipHorizontal();
          var7.b050((int) (COS_THIRTY_DEGREES * var5 * 62.0D) + (this.systemDrawX[index] - this._Hb - 2), this.systemDrawY[index] + (int) (var5 * 33.0D), (int) ((double) this.resourceLightSprites[2][0].width * var5), (int) (var5 * (double) this.resourceLightSprites[2][0].height), uiPulseCounter);
          ArgbSprite var8 = (ArgbSprite) RES_SIDES[system.resources[3]];
          var8 = var8.copy();
          var8.flipHorizontal();
          var8.b050((int) (var5 * 4.0D) + this.systemDrawX[index] + this._Hb, -((int) (var5 * (double) this.resourceLightSprites[3][0].height) / 2) + this.systemDrawY[index], (int) (var5 * (double) this.resourceLightSprites[3][0].width), (int) ((double) this.resourceLightSprites[3][0].height * var5), uiPulseCounter);
        }
      } else if (this.unitScalingFactor < 1024.0F) {
        Drawing.fillCircle(this.systemDrawX[index], this.systemDrawY[index], 25, Drawing.BLACK, 128);

        if (system.resources[0] != 0) {
          final Point[] points = new Point[2 + 2 * system.resources[0]];
          final int x = this.systemDrawX[index] << 4;
          final int y = this.systemDrawY[index] << 4;
          points[0] = new Point(x, y);
          final int x1 = (this.systemDrawX[index] - ((int) ((double) var4 * COS_THIRTY_DEGREES))) << 4;
          final int y1 = (this.systemDrawY[index] + 12) << 4;
          points[1] = new Point(x1, y1);

          for (int i = 0; i < system.resources[0]; ++i) {
            final int x2 = this.systemDrawX[index] - (int) ((double) var4 * Math.cos((double) (24 - (i * 12)) * Math.PI / 180.0D)) << 4;
            final int y2 = this.systemDrawY[index] + (int) ((double) var4 * Math.sin((double) (24 - (i * 12)) * Math.PI / 180.0D)) << 4;
            points[(i * 2) + 2] = new Point(x2, y2);
            final int x3 = this.systemDrawX[index] - (int) ((double) var4 * Math.cos((double) (18 - (i * 12)) * Math.PI / 180.0D)) << 4;
            final int y3 = this.systemDrawY[index] + (int) ((double) var4 * Math.sin((double) (18 - (i * 12)) * Math.PI / 180.0D)) << 4;
            points[(i * 2) + 3] = new Point(x3, y3);
          }
          VectorDrawing.fillPolygon(points, RESOURCE_COLORS[0]);
        }

        if (system.resources[1] != 0) {
          final Point[] var9 = new Point[2 + 2 * system.resources[1]];
          final int x = this.systemDrawX[index] << 4;
          final int y = this.systemDrawY[index] << 4;
          var9[0] = new Point(x, y);
          final int x1 = this.systemDrawX[index] << 4;
          final int y1 = var4 + this.systemDrawY[index] << 4;
          var9[1] = new Point(x1, y1);

          for (int var6 = 0; var6 < system.resources[1]; ++var6) {
            final int x2 = this.systemDrawX[index] - (int) ((double) var4 * Math.sin((double) (6 + var6 * 12) * Math.PI / 180.0D)) << 4;
            final int y2 = this.systemDrawY[index] + (int) (Math.cos((double) (6 + 12 * var6) * Math.PI / 180.0D) * (double) var4) << 4;
            var9[2 + 2 * var6] = new Point(x2, y2);
            final int x3 = this.systemDrawX[index] - (int) (Math.sin(Math.PI * (double) (12 + 12 * var6) / 180.0D) * (double) var4) << 4;
            final int y3 = this.systemDrawY[index] + (int) (Math.cos(Math.PI * (double) (var6 * 12 + 12) / 180.0D) * (double) var4) << 4;
            var9[2 + 2 * var6 + 1] = new Point(x3, y3);
          }

          VectorDrawing.fillPolygon(var9, RESOURCE_COLORS[1]);
        }

        if (system.resources[2] != 0) {
          final Point[] var9 = new Point[2 + system.resources[2] * 2];
          final int x = this.systemDrawX[index] << 4;
          final int y = this.systemDrawY[index] << 4;
          var9[0] = new Point(x, y);
          final int x1 = this.systemDrawX[index] << 4;
          final int y1 = this.systemDrawY[index] + var4 << 4;
          var9[1] = new Point(x1, y1);

          for (int var6 = 0; var6 < system.resources[2]; ++var6) {
            final int x2 = this.systemDrawX[index] + (int) (Math.sin(Math.PI * (double) (6 + var6 * 12) / 180.0D) * (double) var4) << 4;
            final int y2 = this.systemDrawY[index] + (int) (Math.cos((double) (6 + 12 * var6) * Math.PI / 180.0D) * (double) var4) << 4;
            var9[var6 * 2 + 2] = new Point(x2, y2);
            final int x3 = this.systemDrawX[index] + (int) ((double) var4 * Math.sin((double) (12 + 12 * var6) * Math.PI / 180.0D)) << 4;
            final int y3 = this.systemDrawY[index] + (int) ((double) var4 * Math.cos((double) (12 + 12 * var6) * Math.PI / 180.0D)) << 4;
            var9[2 + var6 * 2 + 1] = new Point(x3, y3);
          }

          VectorDrawing.fillPolygon(var9, RESOURCE_COLORS[2]);
        }

        if (system.resources[3] != 0) {
          final Point[] var9 = new Point[2 * system.resources[3] + 2];
          final int x = this.systemDrawX[index] << 4;
          final int y = this.systemDrawY[index] << 4;
          var9[0] = new Point(x, y);
          final int x1 = (int) (COS_THIRTY_DEGREES * (double) var4) + this.systemDrawX[index] << 4;
          final int y1 = 12 + this.systemDrawY[index] << 4;
          var9[1] = new Point(x1, y1);

          for (int var6 = 0; var6 < system.resources[3]; ++var6) {
            final int x2 = this.systemDrawX[index] + (int) (Math.cos(Math.PI * (double) (24 - 12 * var6) / 180.0D) * (double) var4) << 4;
            final int y2 = this.systemDrawY[index] + (int) ((double) var4 * Math.sin(Math.PI * (double) (-(12 * var6) + 24) / 180.0D)) << 4;
            var9[2 + 2 * var6] = new Point(x2, y2);
            final int x3 = this.systemDrawX[index] + (int) ((double) var4 * Math.cos(Math.PI * (double) (18 - var6 * 12) / 180.0D)) << 4;
            final int y3 = this.systemDrawY[index] + (int) (Math.sin((double) (18 - 12 * var6) * Math.PI / 180.0D) * (double) var4) << 4;
            var9[2 + var6 * 2 + 1] = new Point(x3, y3);
          }

          VectorDrawing.fillPolygon(var9, RESOURCE_COLORS[3]);
        }
      }
    }
  }

  private void d815(final StarSystem var1) {
    for (final ProjectOrder order : this.projectOrders) {
      if (order.target == var1 || order.type == ResourceType.EXOTICS && order.source == var1) {
        int var4 = this.systemDrawX[var1.index];
        int var5 = this.systemDrawY[var1.index];
        if (this.unitScalingFactor < 330.0F) {
          var5 -= 7 * this._n / 10 + 44;
        }

        if (order.type == ResourceType.METAL) {
          var4 -= 13;
          var5 -= 21;
        } else if (order.type == ResourceType.BIOMASS) {
          var4 -= 5;
          var5 -= 24;
        } else if (order.type == ResourceType.ENERGY) {
          var5 -= 24;
          var4 += 3;
        } else {
          var4 += 10;
          var5 -= 20;
        }

        _cos.drawTinted(var4, var5, RESOURCE_COLORS[order.type]);
      }
    }
  }

  private void a599() {
    final StarSystem[] var4 = this.map.systems;
    for (final StarSystem var12 : var4) {
      final int var8 = var12.index;
      if (this.retreatTargets[var8]) {
        if (this.localPlayer == null) {
          if (var12.lastOwner == null) {
            continue;
          }
        } else if (var12.lastOwner != this.localPlayer) {
          continue;
        }

        int var9 = this.animationTick;
        if (var9 > 125) {
          var9 = -this.animationTick + 150;
        }

        if (var9 > 0) {
          int var10 = 0;
          int var11 = this._n;
          if (var11 < 16) {
            var11 = 16;
          }

          if (var9 < 25) {
            if (this.animationTick <= 125) {
              var10 = MathUtil.ease(this.animationTick, 25, var11, 0);
            } else {
              var10 = MathUtil.ease(this.animationTick - 125, 25, 0, -var11);
            }

            var11 = MathUtil.ease(var9, 25, var11 / 2, var11);
          }

          SYSTEM_ICONS[4].b115(this.systemDrawX[var8] - var11, -var11 + this.systemDrawY[var8] + var10, 2 * var11, var11 * 2);
        }
      }
    }

    this.a662(this.collapseRetreats, false, true);
    this.a662(this.collapseRetreats, true, true);
  }

  private void drawSystemHexWithHatching(final StarSystem system) {
    final Player owner = this.clonedSystemOwners[system.index];
    if (owner == null) {
      final boolean isAttacked = system.incomingOrders.stream()
          .anyMatch(order -> order.player == this.localPlayer);
      if (isAttacked && this.localPlayer != null) {
        if (!this.canOwnSystem[system.index]) {
          drawSystemHatching(this.systemHexes[system.index], Drawing.RED, 96);
        } else if (this.willOwnSystem[system.index]) {
          drawSystemHatching(this.systemHexes[system.index], Drawing.GREEN, 64);
        } else {
          drawSystemHatching(this.systemHexes[system.index], Drawing.YELLOW, 64);
        }
      }

      if (this.highlightedSystems[system.index] == SystemHighlight.NONE) {
        return;
      }
    }

    final int var8 = owner == null ? 0x303030 : owner.darkColor;
    final int[] var6 = a385qp(this._Qb[system.index]);
    if (var6 != null) {
      if (this.highlightedSystems[system.index] == SystemHighlight.SOURCE) {
        drawSystemHex(var6, ((var8 & 0xfefefe) >> 1) + 0x004000 + ((var8 & 0xfcfcfc) >> 2), 192);
      } else if (this.highlightedSystems[system.index] == SystemHighlight.TARGET) {
        drawSystemHex(var6, ((var8 & 0xfefefe) >> 1) + 0x404040 + ((var8 & 0xfcfcfc) >> 2), 192);
      } else {
        drawSystemHex(var6, (var8 & 0x8f8f8f) << 1, 128);
      }

      if (owner == this.localPlayer && this.localPlayer != null) {
        if (!this.canOwnSystem[system.index]) {
          drawSystemHatching(var6, Drawing.RED, this.systemCollapseStages[system.index] == 0 ? 192 : 96);
        } else if (!this.willOwnSystem[system.index]) {
          drawSystemHatching(var6, Drawing.YELLOW, this.possibleSystemCollapseStages[system.index] == 0 ? 128 : 64);
        }
      }
    }
  }

  private void b815(final StarSystem var1) {
    final int var3 = var1.index;
    final Player var4 = this.clonedSystemOwners[var3];
    final int var5 = var4 == null ? 8421504 : var4.color1;

    if (this.unitScalingFactor < 330.0F) {
      GameUI.FACTION_RING_CENTER.drawTinted(-(GameUI.FACTION_RING_CENTER.width / 2) + this.systemDrawX[var1.index], -(7 * this._n / 10) + this.systemDrawY[var1.index] - 63, var5);
      if (this.isTutorial && TutorialState._vcj) {
        GameUI.FACTION_RING_CENTER.draw(-(GameUI.FACTION_RING_CENTER.width / 2) + this.systemDrawX[var1.index], -(7 * this._n / 10) + this.systemDrawY[var1.index] - 63, uiPulseCounter);
      }

      GameUI.FACTION_RING_TAG.draw(-(GameUI.FACTION_RING_TAG.width / 2) + this.systemDrawX[var1.index], -(this._n * 7 / 10) + this.systemDrawY[var1.index] - 72);
      Menu.SMALL_FONT.drawCentered(Integer.toString(this.clonedRemainingGarrisons[var3]), this.systemDrawX[var3], this.systemDrawY[var3] - 41 - 7 * this._n / 10, Drawing.WHITE);
      Menu.SMALL_FONT.drawCentered(var1.name, this.systemDrawX[var3], this.systemDrawY[var3] - 7 * this._n / 10 - 11, Drawing.WHITE);
      if (var1.score > 0 && var1.type < 6) {
        Drawing.drawCircleGradientAdd(Menu._emc[0] + (this.systemDrawX[var3] << 4), (this.systemDrawY[var3] - (7 * this._n / 10 + 45) << 4) + Menu._pmDb[0], 50, 7, GameUI._hs);
        if (var1.score == StarSystem.Score.NEUTRAL_HOMEWORLD) {
          Drawing.drawCircleGradientAdd(Menu._emc[1] + (this.systemDrawX[var3] << 4), Menu._pmDb[1] + (this.systemDrawY[var3] - 45 - this._n * 7 / 10 << 4), 50, 7, GameUI._hs);
        }

        if (var1.score == StarSystem.Score.PLAYER_HOMEWORLD) {
          Drawing.drawCircleGradientAdd(Menu._emc[2] + (this.systemDrawX[var3] << 4), Menu._pmDb[2] + (this.systemDrawY[var3] + (-(this._n * 7 / 10) - 45) << 4), 50, 7, GameUI._hs);
          Drawing.drawCircleGradientAdd((this.systemDrawX[var3] << 4) + Menu._emc[3], Menu._pmDb[3] + (this.systemDrawY[var3] - 7 * this._n / 10 - 45 << 4), 50, 7, GameUI._hs);
        }
      }
    } else if (this.unitScalingFactor < 1024.0F) {
      GameUI.FACTION_RING_CENTER.drawTinted(-(GameUI.FACTION_RING_CENTER.width / 2) + this.systemDrawX[var1.index], this.systemDrawY[var1.index] - GameUI.FACTION_RING_CENTER.height / 2, var5);
      GameUI.FACTION_RING.draw(this.systemDrawX[var1.index] - GameUI.FACTION_RING.width / 2, this.systemDrawY[var1.index] - 3 - GameUI.FACTION_RING.height / 2);
      Menu.SMALL_FONT.drawCentered(Integer.toString(this.clonedRemainingGarrisons[var3]), this.systemDrawX[var3], this.systemDrawY[var3] + 4, Drawing.WHITE);
      if (var1.score > 0 && var1.type < 6) {
        Drawing.drawCircleGradientAdd((this.systemDrawX[var3] << 4) + Menu._emc[0], (this.systemDrawY[var3] << 4) + Menu._pmDb[0], 50, 7, GameUI._hs);
        if (var1.score == StarSystem.Score.NEUTRAL_HOMEWORLD) {
          Drawing.drawCircleGradientAdd((this.systemDrawX[var3] << 4) + Menu._emc[1], Menu._pmDb[1] + (this.systemDrawY[var3] << 4), 50, 7, GameUI._hs);
        }

        if (var1.score == StarSystem.Score.PLAYER_HOMEWORLD) {
          Drawing.drawCircleGradientAdd(Menu._emc[2] + (this.systemDrawX[var3] << 4), Menu._pmDb[2] + (this.systemDrawY[var3] << 4), 50, 7, GameUI._hs);
          Drawing.drawCircleGradientAdd((this.systemDrawX[var3] << 4) + Menu._emc[3], (this.systemDrawY[var3] << 4) + Menu._pmDb[3], 50, 7, GameUI._hs);
        }
      }
    }

  }

  private void tickAnimations(final Collection<TurnEventLog.Event> turnEvents) {
    final int ticksToAdvance = this.gameUI.animationSpeedDoubledButton.isActive() ? 2 : 1;
    if (this.animationPhase == AnimationPhase.BUILD) {
      final int buildPhaseDuration = Math.max(this.buildPhases != 0 ? (this.buildPhases * 125) + 25 : 0, TICKS_PER_ANIMATION_PHASE);
      this.animationTick += ticksToAdvance;
      if (this.animationTick >= buildPhaseDuration) {
        this.advanceAnimationPhase(AnimationPhase.COMBAT, turnEvents);
      }
    } else if (this.animationPhase == AnimationPhase.COMBAT) {
      for (final CombatEngagementAnimationState engagement : this.combatEngagements) {
        while (ShatteredPlansClient.randomIntBounded(TICKS_PER_ANIMATION_PHASE + engagement.totalKills) >= TICKS_PER_ANIMATION_PHASE) {
          this.combatExplosions.add(new CombatExplosion(engagement.system));
        }

        if (engagement.totalFleets >= 0) {
          if (engagement.totalFleets == 0) {
            if (ShatteredPlansClient.randomIntBounded(10) == 0) {
              Arrays.fill(engagement.fleets, 0, engagement.players.length, 0);
              engagement.totalFleets = -1;
            }
          } else {
            while (ShatteredPlansClient.randomIntBounded(this.random, engagement.totalFleets + 40) >= 40) {
              int roll = ShatteredPlansClient.randomIntBounded(this.random, engagement.totalFleets);
              for (final Player player : engagement.players) {
                final int playerIndex = player == null ? engagement.fleets.length - 1 : player.index;
                final int playerFleets = engagement.fleets[playerIndex];
                roll -= playerFleets;
                if (roll < 0) {
                  engagement.fleets[playerIndex]--;
                  engagement.totalFleets--;
                  break;
                }
              }
            }
          }
        }
      }

      this.animationTick += ticksToAdvance;
      if (this.animationTick >= TICKS_PER_ANIMATION_PHASE) {
        this.advanceAnimationPhase(AnimationPhase.POST_COMBAT, turnEvents);
      }
    } else if (this.animationPhase == AnimationPhase.POST_COMBAT) {
      this.animationTick += ticksToAdvance;
      if (this.animationTick >= 150) {
        this.advanceAnimationPhase(AnimationPhase.RETREAT, turnEvents);
      }
    } else if (this.animationPhase == AnimationPhase.RETREAT) {
      this.animationTick += ticksToAdvance;
      if (this.animationTick >= TICKS_PER_ANIMATION_PHASE) {
        this.advanceAnimationPhase(AnimationPhase.NOT_PLAYING, turnEvents);
        this.gameUI.animationPlayingButton.deactivate();
        this.gameUI.animationPlayingButton.tooltip = StringConstants.TOOLTIP_ANIM_CLICK_TO_PLAY;
      }
    }
  }

  private void a323(final Player var1, final int var2, final int var3, final int var4, final int var6, final CombatEngagementAnimationState var7) {
    final int var8 = var1 == null ? var7.fleets.length - 1 : var1.index;
    int var9 = var7.fleets[var8];
    if (var1 == var7.victor) {
      var9 += var7.fleetsAtCombatEnd;
    }

    if (var1 == var7.selectedPlayer && var9 < var7.fleetsAtCombatStart[var8] && var7.fleets[var7.victor != null ? var7.victor.index : this._ib.length] > 0) {
      ++var9;
    }

    int var10 = var3 - 2;
    if (var10 > 20) {
      var10 = 20;
    }

    int var11 = var10 / 2;
    if (var11 > 5) {
      var11 = 5;
    }

    final int var12 = var4 - var11 * 2;
    int var13 = var12 * var7.fleetsAtCombatStart[var8] / var7.highestFleetsAtCombatStart;
    if (var13 == 0) {
      var13 = 1;
    }

    var13 += var11 * 2;
    Drawing.fillRoundedRect(-2 - var10 / 2 + var3 / 2 + var6, var2 - var13 - 2, var10 + 4, 4 + var13, 2 + var11, 0);
    final int var14 = var1 != null ? var1.color1 : 8421504;
    if (var1 == var7.ownerAtCombatStart && var7.system.hasDefensiveNet) {
      Drawing.f669(-(var10 / 2) + var3 / 2 + var6 - 2, var2 - var13 - 2, 4 + var10, var13 + 4, 2 + var11, RESOURCE_COLORS[0]);
    } else {
      ShatteredPlansClient.a229ch(var14, 2 + var11, 4 + var10, var3 / 2 + var6 - var10 / 2 - 2, var2 - var13 - 2, 0, 4 + var13);
    }

    int var15 = var12 * var9 / var7.highestFleetsAtCombatStart;
    if (var15 != 0) {
      var15 += 2 * var11;
      Drawing.fillRoundedRect(-(var10 / 2) + var6 + var3 / 2, var2 - var15, var10, var15, var11, var14);
      int var16 = var15 / 3;
      if (var11 * 2 + 1 > var16) {
        var16 = 1 + var11 * 2;
      }

      Drawing.b370(-(var10 / 2) + var3 / 2 + var6, -var15 + var2, var10, var16, var11, var14);
      final int[] var17 = new int[4];
      Drawing.saveBoundsTo(var17);
      Drawing.setBounds(var6 - (-(var3 / 2) + var10 / 2), var2 - var13, var3 / 2 + var6 + var10 / 2, var2);
      a835ie(COMBAT_HEX_WHITE, var6 + (var3 / 2 - var10 / 2), -var13 + var2);
      Drawing.restoreBoundsFrom(var17);
    }
  }

  public void render(final Collection<TannhauserLink> tannhauserLinks, final Collection<ProjectOrder> projectOrders, final boolean showDesyncMessage) {
    this.projectOrders = projectOrders;
    this.tannhauserLinks = tannhauserLinks;
    Menu.a487ai();

    if (this._Nb != this._wb) {
      this._Nb = this._wb;
      Drawing.saveContext();
      final ArgbSprite var7 = new ArgbSprite(10, (int) (this._wb * 200.0D));
      var7.installForDrawing();

      for (int var9 = 0; var9 < 10; ++var9) {
        final int var10 = Drawing.WHITE - 218103808 * var9 - 2113929216;
        Drawing.verticalLine(var9, 0, 200, var10);
      }

      final ArgbSprite var8 = new ArgbSprite(15, (int) (200.0D * this._wb));
      var8.installForDrawing();

      for (int var9 = 0; var9 < 15; ++var9) {
        final int var10 = Drawing.WHITE + 285212672 * (-var9 + 15);
        Drawing.verticalLine(var9, 0, 200, var10);
      }

      final int var9 = (int) (0.5D + 345.0D * this._wb);
      final int var10 = (int) (400.0D * this._wb + 0.5D);
      this._ob = new ArgbSprite(var9, var10);
      this._ob.installForDrawing();
      var7.draw(0, (int) (this._wb * 100.0D + 0.5D));
      var7.h093(var9 - 10, (int) (100.0D * this._wb + 0.5D));
      var7.a050(var10 - 5 - (int) (100.0D * this._wb * 0.5D + 0.5D), 4096, (int) (this._wb * 100.0D * COS_THIRTY_DEGREES) + 3, 10923);
      var7.a050(-5 - (int) (0.5D + 0.5D * this._wb * 100.0D) + var10, 4096, var9 - 3 - (int) (100.0D * this._wb * COS_THIRTY_DEGREES), 21845);
      var7.a050(4 + (int) (0.5D * this._wb * 100.0D), 4096, var9 - (int) (COS_THIRTY_DEGREES * 100.0D * this._wb) - 3, 43691);
      var7.a050(4 + (int) (100.0D * this._wb * 0.5D), 4096, (int) (this._wb * 100.0D * COS_THIRTY_DEGREES) + 3, 54613);
      this._Ib = new ArgbSprite(var9, var10);
      this._Ib.installForDrawing();
      var8.draw(0, (int) (0.5D + this._wb * 100.0D));
      var8.h093(var9 - 15, (int) (0.5D + this._wb * 100.0D));
      var8.a050(-7 - (int) (0.5D * 100.0D * this._wb + 0.5D) + var10, 4096, (int) (COS_THIRTY_DEGREES * this._wb * 100.0D) + 3, 10923);
      var8.a050(var10 - ((int) (0.5D + 0.5D * this._wb * 100.0D) + 7), 4096, -5 - (int) (COS_THIRTY_DEGREES * 100.0D * this._wb) + var9, 21845);
      var8.a050((int) (0.5D * this._wb * 100.0D) + 6, 4096, var9 - 5 - (int) (COS_THIRTY_DEGREES * this._wb * 100.0D), 43691);
      var8.a050((int) (0.5D * this._wb * 100.0D) + 6, 4096, (int) (COS_THIRTY_DEGREES * this._wb * 100.0D) + 3, 54613);
      Drawing.restoreContext();

      for (int var11 = 0; var10 * var9 > var11; ++var11) {
        this._ob.pixels[var11] = Drawing.WHITE | this._ob.pixels[var11] << 8;
        this._Ib.pixels[var11] = Drawing.WHITE | this._Ib.pixels[var11] << 8;
      }

      final int var11 = -this._Hb - (int) (150.0D * this._wb) + (int) (300.0D * this.zoomFactor + 0.5D);
      this._Cb = new ArgbSprite(var11 + 4, var11 / 10 + 8);
      this._K = new ArgbSprite((int) (4.0D + (double) var11 * 0.5D), (int) (4.0D + COS_THIRTY_DEGREES * (double) var11));
      this._Ob = new ArgbSprite((int) (4.0D + (double) var11 * 0.5D), (int) (4.0D + COS_THIRTY_DEGREES * (double) var11));
      Drawing.saveContext();
      this._Cb.installForDrawing();
      a070eo(true, 4, 4, 200, var11, 0);
      this._K.installForDrawing();
      a070eo(true, this._K.height - 1, 4, 200, var11 / 2, 0);
      this._Ob.installForDrawing();
      a070eo(true, this._K.height - 1, 4, 200, 4, this._Ob.width - 1);
      Drawing.restoreContext();
      if (this._xb == null) {
        this._xb = new Sprite[13];
        this._Mb = new Sprite[13];
        this._Kb = new Sprite[13];
      }

      Drawing.saveContext();

      for (int var12 = 0; var12 < this._xb.length; ++var12) {
        int var13 = (int) (this.zoomFactor * (double) DEFNET_ANIM_LARGE[var12].offsetX);
        if (var13 < 20 + GameUI.FACTION_RING.height) {
          var13 = GameUI.FACTION_RING.height + 20;
        }

        if (this.unitScalingFactor > 1024.0F && (double) var13 > 345.0D * this._wb * 0.9D) {
          var13 = (int) (this._wb * 345.0D * 0.9D);
        }

        this._xb[var12] = new Sprite(var13, var13);
        this._xb[var12].installForDrawing();
        DEFNET_ANIM_LARGE[var12].b115(0, 0, var13, var13);
        int i = (int) (0.8D * this.zoomFactor * (double) DEFNET_ANIM_MID[var12].offsetX);
        if (i < 20 + GameUI.FACTION_RING.height) {
          i = GameUI.FACTION_RING.height + 20;
        }

        if (this.unitScalingFactor > 1024.0F && 0.9D * this._wb * 345.0D < (double) i) {
          i = (int) (0.9D * 345.0D * this._wb);
        }

        this._Kb[var12] = new Sprite(i, i);
        this._Kb[var12].installForDrawing();
        DEFNET_ANIM_MID[var12].b115(0, 0, i, i);
        int i1 = (int) (this.zoomFactor * (double) DEFNET_ANIM_SMALL[var12].offsetX);
        if (i1 < 20 + GameUI.FACTION_RING.height) {
          i1 = GameUI.FACTION_RING.height + 20;
        }

        if (this.unitScalingFactor > 1024.0F && this._wb * 345.0D * 0.9D < (double) i1) {
          i1 = (int) (this._wb * 345.0D * 0.9D);
        }

        this._Mb[var12] = new Sprite(i1, i1);
        this._Mb[var12].installForDrawing();
        DEFNET_ANIM_SMALL[var12].b115(0, 0, i1, i1);
      }

      Drawing.restoreContext();
    }

    this.drawBackground();
    this.drawSystems();
    this.drawCombatAnimations();

    if (showDesyncMessage) {
      int alpha = (ShatteredPlansClient.currentTick << 3) & 510;
      if (alpha >= 256) {
        alpha = 511 - alpha;
      }
      Menu.FONT.drawCentered("Checksum", 320, 240, Drawing.RED, alpha);
      Menu.FONT.drawCentered("Failure", 320, Menu.FONT.ascent + 240, Drawing.RED, alpha);
    }

    this.gameUI.render();
  }

  @Override
  public void a487() {
    super.a487();
    this._Hb = this._n;
    if (GameUI.FACTION_RING.width / 2 > this._Hb) {
      this._Hb = GameUI.FACTION_RING.width / 2;
    }

    this.zoomFactor = TWO_HUNDRED_F / this.unitScalingFactor;
    if (this.unitScalingFactor > 700.0F) {
      this._Hb = this._n;
    }

    this._wb = this.zoomFactor * 0.8D;
    if (this._Qb == null || this._Qb.length < this.map.systems.length) {
      this._Qb = this.systemHexes.clone();
    }

    float var2 = 10.0F;
    var2 *= 300.0F / this.unitScalingFactor;
    final float var3 = (float) (COS_THIRTY_DEGREES * (double) var2);
    final float var4 = (float) (0.5D * (double) var2);

    for (final int[] ints : this._Qb) {
      ints[1] = (int) ((float) ints[1] + var2);
      ints[2] = (int) ((float) ints[2] - var3);
      ints[3] = (int) ((float) ints[3] + var4);
      ints[4] = (int) ((float) ints[4] - var3);
      ints[5] = (int) ((float) ints[5] - var4);
      ints[7] = (int) ((float) ints[7] - var2);
      ints[8] = (int) ((float) ints[8] + var3);
      ints[9] = (int) ((float) ints[9] - var4);
      ints[10] = (int) ((float) ints[10] + var3);
      ints[11] = (int) ((float) ints[11] + var4);
    }
  }

  private MoveFleetsOrder findSelectedFleetOrder(final int x, final int y) {
    this._Ab = false;
    this._Gb = false;
    this._Bb = false;
    if (this._rb != null) {
      if ((-this._ub[0] - (-30 - x)) * (x - (this._ub[0] - 30)) + (y - this._ub[1]) * (-this._ub[1] + y) < FLEET_BUTTONS[0].width * FLEET_BUTTONS[0].width / 4) {
        this._Gb = true;
        return this._rb;
      }
      if ((x - 30 - this._ub[0]) * (-30 - this._ub[0] + x) + (y - this._ub[1]) * (-this._ub[1] + y) < FLEET_BUTTONS[0].width * FLEET_BUTTONS[0].width / 4) {
        this._Ab = true;
        return this._rb;
      }
      if (MathUtil.euclideanDistanceSquared(y - this._ub[1] - 30, x - this._ub[0]) < FLEET_BUTTONS[0].width * FLEET_BUTTONS[0].width / 4) {
        this._Bb = true;
        return this._rb;
      }
      if (MathUtil.isEuclideanDistanceLessThan(x - this._ub[0], y - this._ub[1], 30)) {
        return this._rb;
      }
    }

    if (this.targetedSystem != null) {
      final MoveFleetsOrder order = this.findSelectedFleetOrder(this.targetedSystem, x, y);
      if (order != null) {
        return order;
      }
    }

    return Arrays.stream(this.map.systems)
        .map(system -> this.findSelectedFleetOrder(system, x, y))
        .filter(Objects::nonNull)
        .findFirst().orElse(null);
  }

  private void b599() {
    for (final BuildEvent var4 : this.buildEvents) {
      final int var5 = this.animationTick - (var4.phase * 125);
      if (var5 > 0 && var5 < 150) {
        drawSystemHex(this.systemHexes[var4.system.index], 0, 64);
      }
    }


    for (final BuildEvent var4 : this.buildEvents) {
      final int var5 = this.animationTick - 125 * var4.phase;
      if (var5 > 0 && var5 < 150) {
        int var6 = var5;
        if (var5 > 125) {
          var6 = -var5 + 150;
        }

        final int var7 = var4.system.index;
        int var8 = this._n;
        if (var4.projectType == -1) {
          var8 = var8 * (this.largestFleetBuildQuantity + var4.fleetsBuilt * 3) / this.largestFleetBuildQuantity;
          if (var8 < 12) {
            var8 = 12;
          }
        } else if (var8 < 32) {
          var8 = 32;
        }

        int var9 = 0;
        if (var6 < 25) {
          if (var5 > 125) {
            var9 = MathUtil.ease(var5 - 125, 25, 0, -var8);
          } else {
            var9 = MathUtil.ease(var5, 25, var8, 0);
          }

          var8 = MathUtil.ease(var6, 25, var8 / 2, var8);
        }

        if (var4.projectType == -1) {
          if (var4.player != null) {
            var4.player._d.a050(var9 + this.systemDrawY[var7], 4096 * var8 / HAMMER.width, this.systemDrawX[var7], this.animationTick % 25 * TICKS_PER_ANIMATION_PHASE);
          }
        } else {
          SYSTEM_ICONS[var4.projectType].b115(-var8 + this.systemDrawX[var7], this.systemDrawY[var7] + var9 - var8, var8 * 2, var8 * 2);
        }
      }
    }

  }

  private void a326() {
    this.a662(this.combatRetreats, false, true);
    this.a662(this.combatRetreats, true, true);
  }

  private void drawCombatAnimations() {
    for (final CombatExplosion var5 : this.combatExplosions) {
      final int i = var5.system.index;
      final int x = ((var5.x * this._n) >> 10) + (this.systemDrawX[i] << 4);
      final int y = ((var5.y * this._n) >> 10) + (this.systemDrawY[i] << 4);
      final int alpha = var5.ticksAlive < 6
          ? var5.ticksAlive * 40
          : 260 - (var5.ticksAlive * 2);
      Drawing.drawCircleGradientAdd(x, y, (this._n + 4) * 2, alpha, Drawing.SHADES_OF_GRAY);
    }

    if (this.animationPhase == AnimationPhase.BUILD) {
      this.a662(this.fleetMovements, false, false);
      this.a662(this.fleetMovements, true, false);
      this.b599();
    } else if (this.animationPhase == AnimationPhase.COMBAT) {
      this.a326();
      this.a679();
    } else if (this.animationPhase == AnimationPhase.POST_COMBAT) {
      this.b326();
    } else if (this.animationPhase == AnimationPhase.RETREAT) {
      this.a599();
    }
  }

  public void b423() {
    this._tb[0] = this._nb[0];
    this._tb[1] = this._nb[1];
    this._rb = this.selectedFleetOrder;
    this._Db = this.selectedFleetOrder;
    this._yb = 15;
    this._ub[1] = this._nb[1];
    this._ub[0] = this._nb[0];
  }

  private void drawWormholeConnections() {
    for (StarSystem system : this.map.systems) {
      if (system.neighbors != null) {
        for (final StarSystem neighbor : system.neighbors) {
          if (neighbor.index >= system.index) {
            if (MathUtil.isEuclideanDistanceGreaterThan(system.posnX - neighbor.posnX, system.posnY - neighbor.posnY, 210)) {
              final int drawX1 = this.systemDrawX[system.index];
              final int drawY1 = this.systemDrawY[system.index];
              final int drawX2 = this.systemDrawX[neighbor.index];
              final int drawY2 = this.systemDrawY[neighbor.index];
              float dx = (float) (drawX2 - drawX1);
              float dy = (float) (drawY2 - drawY1);
              final float distance = MathUtil.euclideanDistance(dx, dy);
              dx /= distance;
              dy /= distance;
              int var22 = 128;
              if ((this.isTutorial && TutorialState._erg) || this.targetedSystem == system || this.targetedSystem == neighbor) {
                var22 = 127 + (int) ((1.0D + Math.cos(Math.PI * (double) (ShatteredPlansClient.currentTick % 50) / 25.0D)) * 64.0D);
              }

              TannhauserLink link = null;
              for (final TannhauserLink link1 : this.tannhauserLinks) {
                if (link1.system1 == system && link1.system2 == neighbor) {
                  link = link1;
                  break;
                }
                if (link1.system1 == neighbor && link1.system2 == system) {
                  link = link1;
                  system = neighbor;
                  break;
                }
              }

              final int threshold = (int) (170.0D * this.zoomFactor);
              if (distance > (float) (threshold * 2)) {
                final int var24 = link == null ? var22 * 0x10101 : var22 * 0x10001 + (var22 >> 1) * 256;
                final int var25 = drawX1 + (int) (dx * (float) threshold);
                final int var26 = drawY1 + (int) (dy * (float) threshold);
                final int var27 = drawX2 - (int) (dx * (float) threshold);
                final int var28 = drawY2 - (int) (dy * (float) threshold);
                if (link != null && link.turnsLeft != 3) {
                  var22 = (link.turnsLeft - 1) * var22 / 2;
                  final int var29 = 0x10001 * var22 + 256 * (var22 >> 1);
                  if (ShatteredPlansClient.renderQuality.antialiasWormholeConnections) {
                    a306we(var27 << 4, var26 << 4, var25 << 4, var24, var28 << 4, var29);
                  } else {
                    a681be(var27 << 4, var24, var26 << 4, var29, var25 << 4, var28 << 4);
                  }
                } else if (ShatteredPlansClient.renderQuality.antialiasWormholeConnections) {
                  a669tue(var26 << 4, var27 << 4, var25 << 4, var24, var28 << 4);
                } else {
                  drawLine(var25 << 4, var26 << 4, var27 << 4, var28 << 4, var24);
                }
              }
            }
          }
        }
      }
    }
  }

  public void a115(final GameUI.PlacementMode mode, final int x, final int y) {
    if (mode == GameUI.PlacementMode.MOVE_FLEET_DEST || mode == GameUI.PlacementMode.GATE_DEST) {
      this._nb[1] = -1;
      this.selectedFleetOrder = null;
      this._nb[0] = -1;
      this.targetedSystem = this.systemHexAtPoint(x, y);
    } else {
      this.selectedFleetOrder = this.findSelectedFleetOrder(x, y);
      if (this.selectedFleetOrder == null) {
        this.targetedSystem = this.systemHexAtPoint(x, y);
        if (mode == GameUI.PlacementMode.MOVE_FLEET_SRC) {
          ProjectOrder projectOrder = null;
          if (this.targetedSystem != null) {
            projectOrder = this.projectOrders.stream()
                .filter(order -> order.player == this.localPlayer && (order.target == this.targetedSystem || order.source == this.targetedSystem))
                .findFirst().orElse(null);
          }

          if (this.animationPhase == AnimationPhase.NOT_PLAYING) {
            if (projectOrder == null) {
              this.gameUI.setActionHint(StringConstants.HINT_SELECT_SRC);
            } else {
              this.gameUI.setActionHint(Strings.format(StringConstants.HINT_CANCEL_PROJECT, StringConstants.PROJECT_NAMES[projectOrder.type], this.targetedSystem.name));
            }
          }
        }
      } else {
        this.targetedSystem = null;
      }
    }
  }

  private void b326() {
    final StarSystem[] var4 = this.map.systems;
    for (final StarSystem var6 : var4) {
      final int var7 = var6.index;
      if (var7 >= this.systemOwners.length) {
        break;
      }

      final Player var8 = this.systemOwners[var7];
      final Player var9 = this.clonedSystemOwners[var7];
      if (var8 != var9) {
        final Sprite var14;
        if (this.localPlayer == null) {
          if (var9 == null) {
            continue;
          }

          var14 = SYSTEM_ICONS[5];
        } else if (var9 == this.localPlayer) {
          var14 = SYSTEM_ICONS[5];
        } else {
          if (var8 != this.localPlayer || this.localPlayer == var6.lastOwner) {
            continue;
          }

          var14 = SYSTEM_ICONS[4];
        }

        int var11 = this.animationTick;
        if (var11 > 125) {
          var11 = 150 - this.animationTick;
        }

        int var12 = 0;
        int var13 = this._n;
        if (var13 < 16) {
          var13 = 16;
        }

        if (var11 < 25) {
          if (this.animationTick <= 125) {
            var12 = MathUtil.ease(this.animationTick, 25, var13, 0);
          } else {
            var12 = MathUtil.ease(this.animationTick - 125, 25, 0, -var13);
          }

          var13 = MathUtil.ease(var11, 25, var13 / 2, var13);
        }

        var14.b115(-var13 + this.systemDrawX[var7], this.systemDrawY[var7] + var12 - var13, 2 * var13, 2 * var13);
      }
    }
  }

  private void drawSystemProjectHighlights(final StarSystem system) {
    for (final ProjectOrder order : this.projectOrders) {
      if (system == order.target || (order.type == ResourceType.EXOTICS && system == order.source)) {
        if (this.unitScalingFactor < 330.0F) {
          Drawing.drawCircleGradientAdd(
              this.systemDrawX[system.index] << 4,
              (this.systemDrawY[system.index] - ((this._n * 7) / 10) - 44) << 4,
              550,
              255,
              this.projectHighlightColors[order.type]);
        } else {
          final int radius = MathUtil.clamp(this.unitScalingFactor < 1024.0F ? 450 : 150, (int) (this.zoomFactor * 1500.0D), 700);
          final boolean var11 = this.unitScalingFactor > 1024.0F;
          Drawing.drawCircleGradientAdd(
              (this.systemDrawX[system.index] - (var11 ? 2 : 0)) << 4,
              (this.systemDrawY[system.index] - (var11 ? 1 : 0)) << 4,
              radius,
              255,
              this.projectHighlightColors[order.type]);
        }
      }
    }
  }
}
