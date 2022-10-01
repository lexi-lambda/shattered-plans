package funorb.shatteredplans.client.intro;

import funorb.awt.KeyState;
import funorb.awt.MouseState;
import funorb.cache.ResourceLoader;
import funorb.graphics.Drawing;
import funorb.graphics.Font;
import funorb.graphics.Sprite;
import funorb.graphics.SpriteResource;
import funorb.shatteredplans.StringConstants;
import funorb.shatteredplans.client.JagexApplet;
import funorb.shatteredplans.client.Menu;
import funorb.shatteredplans.client.ShatteredPlansClient;
import funorb.shatteredplans.client.Sounds;
import funorb.util.MathUtil;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public final class IntroAnimation {
  private static final int[] _fex = new int[256];
  private static final int[] _feu = new int[256];
  private static final nd_[] _fey;
  private static final nd_[] _feH;
  private static final nd_[] _feC;
  private static List<sb_> _rpK;
  private static boolean resourcesLoaded;
  public static int _ucv = 0;
  private static boolean _cio;
  private static Sprite[] TEXT_INTRO_0;
  private static Sprite _heh;
  private static Sprite INTRO_3_CITY_FG;
  private static Sprite INTRO_3_CRATER;
  private static Sprite _qqn;
  private static float _cfe;
  private static Sprite _wef;
  private static Sprite INTRO_3_CITY_BG;
  private static Sprite _ksj;
  private static Sprite _tfb;
  private static List<RenderedTextLine> _cbh;
  private static int _dpr;
  private static int[] _pA;
  private static int _cfp;
  public static ResourceLoader spriteLoader;
  private static Sprite INTRO_1_3;
  private static Sprite INTRO_1_GLOW;
  private static Sprite INTRO_1_GLOW_FG;
  private static Sprite _mcu;
  private static Sprite _dsb;
  private static Sprite _mar;
  private static Sprite INTRO_2_CLOUDS;
  private static boolean _pn;
  private static int[] _npf;
  private static int _dim;
  private static boolean resourcesInitialized;
  private static Font currentFont;
  private static Sprite INTRO_1_2;
  private static Sprite INTRO_1_1;
  private static Sprite INTRO_1_FLASH;
  private static Sprite INTRO_3_STARS;
  private static Sprite INTRO_2_EARTH;
  private static int _ua;
  private static int currentStage;
  private static int nextStage;
  private static int timeTillNextStage;

  static {
    final int[] var2 = new int[]{327, 239, 128, 326, 639, 717, 529, 507, 132, 195, 772, 777, 211, 319, 84, 202, 717, 303, 235, 193};
    final int[] var3 = new int[]{395, 478, 553, 617, 475, 615, 611, 384, 552, 628, 492, 403, 401, 348, 627, 393, 615, 509, 500, 545};
    _feC = new nd_[var2.length];
    for (int var4 = 0; var4 < var2.length; ++var4) {
      _feC[var4] = new nd_(var2[var4], var3[var4], 240);
    }

    for (int var0 = 0; var0 < 256; ++var0) {
      _feu[var0] = 65793 * var0;
      _fex[var0] = var0 >> 1 << 8 | var0 | var0 >> 2 << 16;
    }

    final int[] var5 = new int[]{256, 251, 245, 239, 219, 215, 161, 157, 154, 150, 146, 144, 316, 306, 612, 604, 599, 774, 777};
    final int[] var1 = new int[]{199, 209, 223, 235, 214, 227, 289, 302, 313, 327, 338, 351, 247, 267, 180, 209, 240, 235, 258};
    _fey = new nd_[12];

    int var4;
    for (var4 = 0; var4 < 12; ++var4) {
      _fey[var4] = new nd_(var5[var4], var1[var4], 80);
    }

    _feH = new nd_[7];

    for (var4 = 0; var4 < 7; ++var4) {
      _feH[var4] = new nd_(var5[var4 + 12], var1[var4 + 12], 144);
    }
  }

  public static void reset() {
    _ua = ShatteredPlansClient.randomIntBounded(50);
    currentFont = Menu.SMALL_FONT;
    _rpK = new ArrayList<>();
    currentStage = 0;
    nextStage = 0;
    _dim = 0;
    _pn = false;
    timeTillNextStage = 0;
    ShatteredPlansClient.a827jo(null, 50, true);
    if (_cbh != null) {
      _cbh.clear();
    }
  }

  public static void tick() {
    if (resourcesLoaded) {
      if (!resourcesInitialized) {
        initializeResources();
      }
    } else if (spriteLoader.loadGroupData("intro")) {
      resourcesLoaded = true;
    }

    while (JagexApplet.nextTypedKey()) {
      if (JagexApplet.lastTypedKeyCode == KeyState.Code.ESCAPE) {
        a366jc(-1);
      } else if (JagexApplet.lastTypedKeyCode == KeyState.Code.SPACE && !_pn && resourcesInitialized && timeTillNextStage > 0) {
        a150ph();
        a366jc(currentStage < 5 ? currentStage + 1 : -1);
      }
    }

    if (_ua + 50 < ShatteredPlansClient.currentTick) {
      _ua = ShatteredPlansClient.currentTick + ShatteredPlansClient.randomIntBounded(50);
    }

    final int var1 = ShatteredPlansClient.currentTick - _ua;
    if (var1 < 0) {
      _dpr = 0;
    } else if (var1 < 10) {
      _dpr = var1 * 25;
    } else if (var1 < 15) {
      _dpr = 500 - 25 * var1;
    } else if (var1 < 20) {
      _dpr = 25 * var1 - 150;
    } else if (var1 < 25) {
      _dpr = 750 - var1 * 25;
    } else if (var1 < 30) {
      _dpr = 25 * var1 - 400;
    } else if (var1 >= 40) {
      _dpr = 0;
    } else {
      _dpr = 1000 - var1 * 25;
    }

    if (_pn) {
      if (--timeTillNextStage <= 0) {
        _pn = false;
        currentStage = nextStage;
        timeTillNextStage = 0;
        if (currentStage == -1) {
          _ucv = 50;
          Menu.switchTo(Menu.Id.MAIN, 0, false);
          ShatteredPlansClient.switchMenus();
          freeResources();
        }

        if (currentStage == 2) {
          b150gq();
        }

        if (currentStage == 4) {
          _pA = new int[600];
          _cfp = 0;

          _npf = new int[600];
        }

        _dim = 0;
      }
    } else if (timeTillNextStage < 50) {
      ++timeTillNextStage;
    }

    b150ej();
    switch (currentStage) {
      case 0 -> tickStage0();
      case 1 -> tickStage1();
      case 2 -> tickStage2();
      case 3 -> tickStage3();
      case 4 -> tickStage4();
      case 5 -> tickStage5();
    }

    _cio = JagexApplet.mouseX >= 540 && JagexApplet.mouseX < 620 && JagexApplet.mouseY >= 440 && JagexApplet.mouseY < 460;

    if (JagexApplet.mouseButtonJustClicked != MouseState.Button.NONE) {
      if (JagexApplet.mousePressX >= 540 && JagexApplet.mousePressX < 620 && JagexApplet.mousePressY >= 440 && JagexApplet.mousePressY < 460) {
        a366jc(-1);
      } else if (!_pn && resourcesInitialized && timeTillNextStage > 0) {
        a150ph();
        a366jc(currentStage >= 5 ? -1 : 1 + currentStage);
      }
    }
  }

  public static void render() {
    switch (currentStage) {
      case 0 -> renderStage0();
      case 1 -> renderStage1();
      case 2 -> renderStage2();
      case 3 -> renderStage3();
      case 4 -> renderStage4();
      case 5 -> renderStage5();
    }

    if (timeTillNextStage < 50) {
      if (currentStage == (_pn ? 2 : 3) || _pn && nextStage == -1) {
        a669(255 - (5 * timeTillNextStage));
      } else {
        a455cle(Drawing.width, 256, 5 * timeTillNextStage, Drawing.height);
      }
    }

    a423qn();
    int var1 = currentFont.measureLineWidth(StringConstants.TEXT_SKIP_INTRO) + 20;
    if (var1 < 80) {
      var1 = 80;
    }

    a544pvc(var1, currentFont, -20 - var1 + ShatteredPlansClient.SCREEN_WIDTH, StringConstants.TEXT_SKIP_INTRO, _cio);
  }

  private static void a455cle(int var1, final int var3, final int var4, int var5) {
    final int var0 = 0;
    final int var2 = 0;

    if (var1 + var2 > Drawing.width) {
      var1 = Drawing.width - var2;
    }

    if (Drawing.height < var5 + var0) {
      var5 = Drawing.height;
    }

    final int var6 = var3 * var4 >> 8;
    final int var7 = -var6 + var4;
    final int var8 = var5 + var0;

    for (int var9 = var0; var9 < var8; ++var9) {
      int var10 = Drawing.pixelIndex(var2, var9);

      for (int var11 = var1; var11 > 0; --var11) {
        final int var12 = Drawing.screenBuffer[var10];
        final int var13 = (16711680 & var12) >> 16;
        final int var14 = 255 & var12 >> 8;
        final int var15 = 255 & var12;
        final int var16 = 5 * var13 + var14 * 6 + 5 * var15;
        final int var17 = (var13 * var6 >> 8) + (var16 * var7 >> 12);
        final int var18 = (var14 * var6 >> 8) + (var7 * var16 >> 12);
        final int var19 = (var15 * var6 >> 8) + (var16 * var7 >> 12);
        final int var20 = var19 | var18 << 8 | var17 << 16;
        Drawing.screenBuffer[var10] = var20;
        ++var10;
      }
    }
  }

  private static void renderStage0() {
    if (TEXT_INTRO_0 == null) {
      TEXT_INTRO_0 = new Sprite[]{renderStage0Text(StringConstants.TEXT_INTRO_0[0]), renderStage0Text(StringConstants.TEXT_INTRO_0[1])};
    }

    Drawing.clear();
    draw(TEXT_INTRO_0[0], (ShatteredPlansClient.SCREEN_WIDTH - TEXT_INTRO_0[0].width) >> 1, (short) 240, 256, _dim << 1);
    draw(TEXT_INTRO_0[1], (ShatteredPlansClient.SCREEN_WIDTH - TEXT_INTRO_0[1].width) >> 1, (short) 240 + TEXT_INTRO_0[1].height, 256, (_dim << 1) - 128);
    if (!resourcesLoaded) {
      renderLoading(spriteLoader.percentLoaded("intro"));
    }
  }

  private static void renderStage1() {
    Drawing.clear();
    final int var2 = 300 - _dim;
    final int var1 = var2 / 8 - 25;
    INTRO_1_3.drawTinted2(var1, 0, 33023);
    final int i = var2 / 6 - 100;
    INTRO_1_2.drawTinted2(i, 0, 4227327);
    INTRO_1_GLOW.draw(i, 0, _dpr / 8);

    for (final nd_ var5 : _fey) {
      if (var5._a != 0) {
        Drawing.drawCircleGradientAdd(i + var5._e << 4, var5._j << 4, var5._f, var5._a, _feu);
      }
    }

    final int i1 = var2 / 4 - 250;
    INTRO_1_1.drawTinted2(i1, 120, 8421631);
    INTRO_1_GLOW_FG.draw(i1, 120, _dpr / 4);
    INTRO_1_FLASH.draw(i1, 120, _dpr);

    for (final nd_ var5 : _feH) {
      if (var5._a != 0) {
        Drawing.drawCircleGradientAdd(var5._e + i1 << 4, var5._j + 120 << 4, var5._f, var5._a, _feu);
      }
    }
  }

  private static void renderStage2() {
    final float var2 = (float) _dim / 1000.0F + 0.15F;
    int var0 = -((int) (200.0D * Math.sin(var2)));
    final int var1 = (int) (Math.cos(var2) * 200.0D) - 200;
    INTRO_3_STARS.draw(var0, var1);
    int var3 = -(_dim * 120 / 1000) + 176;
    final int var4 = 540 - var3;
    Drawing.saveContext();
    if (var4 < 256) {
      var3 = 284;
    }

    if (_mcu == null) {
      _mcu = _dsb.copy();
      _mcu.installForDrawing();
    } else {
      _mcu.installForDrawing();
      _dsb.c093(0, 0);
    }

    final byte var9 = -5;
    int i = 100 * _dim / 1000 - 80;
    ShatteredPlansClient.a034so(0, i - var3 + INTRO_2_EARTH.y, _mar);
    Drawing.restoreContext();
    int var5 = 256;
    if (_dim < 512) {
      var5 = _dim >> 1;
      Drawing.fillCircle(320, var3 + 528, 400, Drawing.WHITE);
    }

    INTRO_2_EARTH.draw(var9, i);
    a885tg(i, INTRO_2_CLOUDS);
    _mcu.drawAdd(0, var3, var5 * (_dpr < 0 ? 0 : (Math.min(_dpr, 256))) >> 8);
    _mcu.drawAdd(0, var3, var5);

    for (final nd_ var8 : _feC) {
      if (var8._a != 0) {
        Drawing.drawCircleGradientAdd(var8._e + var9 << 4, var8._j + i << 4, var8._f, var8._a, _feu);
      }
    }

    for (final sb_ var11 : _rpK) {
      i = (int) var11._n;
      var0 = (int) var11._l;

      for (int var7 = 1; var7 < 50; ++var7) {
        int var12 = -var7 + var11._q;
        Drawing.drawCircleGradientAdd(var0 << 4, i << 4, -var7 + 50, -(var7 * 2) + 100, _feu);
        if (var12 < 0) {
          var12 += 50;
        }

        var0 = (int) var11._o[var12];
        i = (int) var11._i[var12];
      }

      Drawing.drawCircleGradientAdd((int) var11._l << 4, (int) var11._n << 4, 128, 100, _feu);
    }

  }

  private static void renderStage3() {
    final float var2 = (float) _dim / 1000.0F + 0.15F;
    final int var0 = -((int) (Math.sin(var2) * 200.0D));
    final int var1 = (int) (Math.cos(var2) * 200.0D) - 200;
    INTRO_3_STARS.draw(var0, var1);
    final int i = -(_dim * 40 / 800) + 200;
    final byte var3 = -62;
    _heh.drawAdd(var3, i - 60, 256);
    final int i1 = 300 - 150 * _dim / 800;
    final byte b = -120;
    _wef.drawAdd(b, i1 - 60, 256);
    final byte i2 = -62;
    final int i3 = -(_dim * 40 / 800) + 200;
    INTRO_3_CITY_BG.draw(i2, i3);
    final byte i4 = -120;
    final int i5 = -(_dim * 150 / 800) + 300;
    INTRO_3_CITY_FG.draw(i4, i5);
    final int i6 = _dim * 240 / 800 + 20;
    final byte var4 = 0;
    INTRO_3_CRATER.draw(var4, i6);
    Drawing.fillRect(0, INTRO_3_CRATER.offsetY + i6, ShatteredPlansClient.SCREEN_WIDTH, ShatteredPlansClient.SCREEN_HEIGHT, 0);
  }

  private static void renderStage4() {
    final float var3 = 0.15F + (float) _dim / 1000.0F;
    final int var1 = -((int) (200.0D * Math.sin(var3)));
    final int var2 = (int) (200.0D * Math.cos(var3)) - 200;
    _ksj.draw(var1, var2);
    Drawing.h115(0, 0, Drawing.width, Drawing.height);
    final byte var4 = 125;
    final short var5 = 400;
    final int var6 = 550;
    final int var7 = 650;
    final int var9 = 450;
    final byte var10 = -80;
    final short var11 = 140;
    final short var34 = -260;
    final int var12 = _dim >= var5 ? var11 : (_dim >= var4 ? MathUtil.ease(-125 + _dim, 275, var10, var11) : var10);
    _tfb.drawAdd(var34, var10 + (var12 - 120), 128);
    _qqn.draw(var34, var12 + var10);
    final int var13 = _dim < var5 ? (_dim >= var4 ? MathUtil.ease(_dim - var4, 275, 0, 2000) : 0) : 2000;
    final int var14 = 3104;
    final int var15 = -var13 + (3424);
    int var16 = 32 + ShatteredPlansClient.randomIntBounded(32);
    if (_dim < var6) {
      Drawing.drawCircleGradientAdd(var14, var15, var16, 128, _feu);
    }

    int var17 = 197;
    int var18 = var17 << 4;
    int var19 = (var13 >> 4) - 36;

    int var20 = var19 << 4;
    int var21 = 144;
    int var22;
    int var23;
    int var24;
    if (_dim >= var7) {
      var22 = -650 + _dim;
      var23 = 350;
      var24 = var22 << 2;
      var24 += var22 * 16 * var22 / var23;
      var20 -= var24 >> 4;
      var21 = var21 * (-var22 + var23) / var23;
      var17 += var24 >> 4;
      var19 -= var24 >> 8;
      var18 += var24;
    }

    var22 = 3152;
    var23 = 1424;
    var24 = 1200;
    var24 += 4114;
    var22 += var24;
    var23 -= var24 >> 4;
    Drawing.setBounds(0, 0, var22 + 320 >> 4, ShatteredPlansClient.SCREEN_HEIGHT);
    Drawing.fillRect(var17, var19, 20, 4, 0);
    int var25;
    int var26;
    if (_dim >= var9) {
      var25 = -450 + _dim;
      var26 = 100;
      final int var27 = 440 * var25 / var26;
      int var28 = var27 + ShatteredPlansClient.randomIntBounded(-64, 64);
      if (var28 < 0) {
        var28 = 0;
      }

      if (var28 > 255) {
        var28 = 255;
      }

      Drawing.setPixel(16 + var17, var19 + 2, (var28 >> 2) * 65793);
      int i = var27 + ShatteredPlansClient.randomIntBounded(-64, 64) - 40;
      if (i < 0) {
        i = 0;
      }

      if (i > 255) {
        i = 255;
      }

      Drawing.setPixel(3 + var17, var19 + 3, 65793 * (i >> 2));
      int i1 = ShatteredPlansClient.randomIntBounded(-64, 64) + (var27 - 80);
      if (i1 < 0) {
        i1 = 0;
      }

      if (i1 > 255) {
        i1 = 255;
      }

      Drawing.setPixel(var17 + 12, var19, 65793 * (i1 >> 2));
      int i2 = var27 - 120 + ShatteredPlansClient.randomIntBounded(-64, 64);
      if (i2 < 0) {
        i2 = 0;
      }

      if (i2 > 255) {
        i2 = 255;
      }

      Drawing.setPixel(var17 + 7, 1 + var19, (i2 >> 2) * 65793);
    }

    Drawing.a797();
    if (_dim >= var9) {
      var25 = -450 + _dim;
      var26 = 250;
      final double var35 = 3.141592653589793E-4D * (double) var25 * (double) var25;
      final int var29 = var25 <= var26 ? var25 * 128 / var26 : 128;

      int var30;
      int var31;
      int var32;
      for (var30 = 0; var30 < 3; ++var30) {
        var31 = (int) (Math.cos(Math.PI * (double) (2 * var30) / 3.0D + var35) * (double) (var21 >> 1));
        var31 += var18;
        var32 = (int) ((double) var21 * Math.sin(var35 + Math.PI * (double) (var30 * 2) / 3.0D));
        var32 += var20;
        if (var22 + 320 >= var31) {
          Drawing.line(var17, var19, var31 >> 4, var32 >> 4, 0);
          Drawing.drawCircleGradientAdd(var31, var32, 64, var29, _fex);
          _pA[_cfp] = var31;
          _npf[_cfp] = var32;
          if (++_cfp == 600) {
            _cfp = 0;
          }
        }
      }

      var30 = Math.min(600, var25);
      var31 = _cfp - 1;

      for (var32 = 0; var30 > var32; ++var32) {
        if (var31 < 0) {
          var31 += 600;
        }

        final int var33 = 256 * (var30 - var32) / 600;
        Drawing.drawCircleGradientAdd(_pA[var31], _npf[var31], var33, 12, _fex);
        Drawing.drawCircleGradientAdd(_pA[var31], _npf[var31], var33 >> 2, 128, _fex);
        --var31;
      }
    }

    if (_dim > 900) {
      if (_dim <= 950) {
        var16 = MathUtil.ease(_dim - 950, 50, 256, 0);
      } else {
        var16 = MathUtil.ease(_dim - 900, 50, 0, 256);
      }

      Drawing.a907(var22, var23, var16);
      Drawing.a907(var22 + 128, var23 - 16, var16 >> 1);
      Drawing.a907(var22 + 256, var23 - 32, var16 >> 2);
    }

    var25 = -var22 + var18;
    if (var25 > 0) {
      int i = var25;
      if (var25 > 320) {
        var20 -= var25 - 320 >> 2;
        var18 += var25 - 320 << 1;
        i = -(var25 >> 2) + 320;
      }

      Drawing.drawCircleGradientAdd(var18, var20, i, 256, _feu);
    }

  }

  private static void renderStage5() {
    ShatteredPlansClient.f423fr();
    final int var1 = ShatteredPlansClient.LOGO.offsetX;
    final int var2 = ShatteredPlansClient.LOGO.offsetY;
    ShatteredPlansClient.LOGO.drawAdd((-var1 + ShatteredPlansClient.SCREEN_WIDTH) / 2, (ShatteredPlansClient.SCREEN_HEIGHT - var2) / 2, 256);
  }

  private static void a669(final int var3) {
    final int var10 = var3 * 0x010101;
    final int width = Math.min(Drawing.width, ShatteredPlansClient.SCREEN_WIDTH);
    final int height = Math.min(Drawing.height, ShatteredPlansClient.SCREEN_HEIGHT);

    for (int i = 0; i < height; ++i) {
      int n = i * Drawing.width;
      for (int j = width; j > 0; --j) {
        final int px = Drawing.screenBuffer[n];
        final int var2 = px + var10;
        final int rb = (var10 & 0xff00ff) + (px & 0xff00ff);
        final int var100 = ((var2 - rb) & 0x10000) + (rb & 0x1000100);
        final int var01 = var100 - (var100 >>> 8);
        Drawing.screenBuffer[n++] = var01 | (var2 - var100);
      }
    }
  }

  private static void renderLoading(int introPercentLoaded) {
    if (introPercentLoaded > 100) {
      introPercentLoaded = 100;
    }

    Drawing.horizontalLine(18, 455, 101, Drawing.WHITE);
    Drawing.setPixel(17, 456, Drawing.WHITE);
    Drawing.horizontalLine(18, 456, 99, 0);
    Drawing.setPixel(118, 456, Drawing.WHITE);

    {
      final int var11 = 0x202020;
      final int var12 = 0x101010;
      final int var13 = 457;
      final int var14 = 16;
      final int var15 = introPercentLoaded + 18;
      int var16 = var15 + 5;
      final int var17 = 118;
      if (var16 > var17) {
        var16 = var17;
      }

      Drawing.setPixel(var14, var13, Drawing.WHITE);
      Drawing.setPixel(1 + var14, var13, 0);
      Drawing.horizontalLine(2 + var14, var13, var15 - var14 - 2, 0xff0000);
      Drawing.horizontalLine(var15, var13, var16 - var15, var12);
      Drawing.horizontalLine(var16, var13, var17 - var16, var11);
      Drawing.setPixel(var17, var13, Drawing.WHITE);
      Drawing.setPixel(117, var13, 0);
    }

    for (int var8 = 3; var8 < 7; ++var8) {
      final int var9 = (var8 - 2) * 256 / (6);
      final int var10 = (0xff00ff00 & (var9 >> 1) * 0xff00ff + (-var9 + 256) * 0xff0000 | 0xff00 * (var9 >> 1) & 16711680) >>> 8;
      final int var11 = (0xff0000 & (-var9 + 256) * 8192 + 0xC000 * (var9 >> 1) | 0xff00ff00 & 0x200020 * (256 - var9) + 0xc000c0 * (var9 >> 1)) >>> 8;
      final int var12 = (0xfefefe & var11) >> 1;
      final int var13 = var8 + 455;
      final int var14 = introPercentLoaded + 18;
      int var15 = var14 + 5;
      final int var16 = 118;
      if (var15 > var16) {
        var15 = var16;
      }

      Drawing.setPixel(15, var13, Drawing.WHITE);
      Drawing.setPixel(16, var13, 0);
      Drawing.horizontalLine(17, var13, var14 - 15 - 2, var10);
      Drawing.horizontalLine(var14, var13, var15 - var14, var12);
      Drawing.horizontalLine(var15, var13, -var15 + var16, var11);
      Drawing.setPixel(118, var13, Drawing.WHITE);
      Drawing.setPixel(117, var13, 0);
    }

    {
      final int var10 = 0x946969;
      final int var11 = 0x747474;
      final int var12 = 0x3a3a3a;
      final int var13 = 462;
      final int var14 = introPercentLoaded + 17;
      int var15 = var14 + 5;
      final int var16 = 117;
      Drawing.setPixel(15, var13, Drawing.WHITE);
      if (var15 > var16) {
        var15 = var16;
      }

      Drawing.setPixel(16, var13, 0);
      Drawing.horizontalLine(17, var13, var14 - 17, var10);
      Drawing.horizontalLine(var14, var13, -var14 + var15, var12);
      Drawing.horizontalLine(var15, var13, -var15 + var16, var11);
      Drawing.setPixel(var16, var13, Drawing.WHITE);
      Drawing.setPixel(var16 - 1, var13, 0);
    }

    Drawing.horizontalLine(15, 464, 101, Drawing.WHITE);
    Drawing.setPixel(15, 463, Drawing.WHITE);
    Drawing.horizontalLine(16, 463, 99, 0);
    Drawing.setPixel(116, 463, Drawing.WHITE);
  }

  private static void a423qn() {
    if (_cbh != null) {
      final byte var2 = 20;
      int var1 = (int) _cfe + 20;

      for (final RenderedTextLine var0 : _cbh) {
        if (var0._h >= 0) {
          if (var0._h >= 230) {
            if (var0._h < 250) {
              draw(var0._o, 17, var1 - 3, 12 * var0._h - 2760, var0._h << 4);
              draw(var0._i, var2, var1, 256, var0._h << 4);
            } else {
              final int var4 = -(5 * var0._h) + 1500;
              draw(var0._o, 17, var1 - 3, var4, var0._h << 4);
              draw(var0._i, var2, var1, var4, var0._h << 4);
            }
          } else {
            draw(var0._i, var2, var1, 256, var0._h << 4);
          }
        }

        var1 += var0._i.height;
      }
    }
  }

  private static void a544pvc(final int var0, final Font var1, final int var5, final String var6, final boolean var7) {
    a306cl(var5, var0);
    var1.drawCentered(var6, var5 + (var0 >> 1), 440 + (var1.ascent + 20 >> 1), Drawing.WHITE);
    if (var7) {
      final Sprite var12 = new Sprite(var0, 20);
      Drawing.saveContext();
      var12.installForDrawing();
      var1.drawCentered(var6, (var0 >> 1) + 1, 20 + var1.ascent >> 1, Drawing.WHITE);
      var1.drawCentered(var6, (var0 >> 1) - 1, var1.ascent + 20 >> 1, Drawing.WHITE);
      var1.drawCentered(var6, var0 >> 1, (20 + var1.ascent >> 1) + 1, Drawing.WHITE);
      var1.drawCentered(var6, var0 >> 1, -1 + (var1.ascent + 20 >> 1), Drawing.WHITE);
      Drawing.restoreContext();
      var12.drawAdd(var5, 440, 64);
    }
  }

  private static Sprite renderStage0Text(final String text) {
    final int var3 = Menu.FONT.measureLineWidth(text);
    final int var4 = Menu.FONT.ascent + Menu.FONT.descent;
    final Sprite var5 = new Sprite(var3, var4);
    Drawing.saveContext();
    var5.installForDrawing();
    Menu.FONT.draw(text, 0, Menu.FONT.ascent, Drawing.WHITE);
    Drawing.restoreContext();
    return var5;
  }

  private static void a306cl(final int var0, final int var3) {
    for (int var9 = 0; var9 < 20; ++var9) {
      final int var10 = var9 + 440;
      int var11 = 5 - var9;
      if (var11 < 0) {
        var11 = 0;
      }

      int var12 = -var9 + var3 + 20 - 5;
      if (var3 < var12) {
        var12 = var3;
      }

      final int var13 = var12 - var11;
      var11 += var0;
      Drawing.horizontalLine(5 + var11, 5 + var10, var13, 0, 128);
    }

    Drawing.horizontalLine(5 + var0, 440, -5 + var3, Drawing.WHITE);
    Drawing.horizontalLine(var0, 20 + (440 - 1), 1 - 5 + var3, Drawing.WHITE);

    for (int var9 = 1; var9 < 20 - 1; ++var9) {
      final int var10 = 440 + var9;
      final int var11 = (var9 - 1) * 256 / (20 - 2);
      final int var12 = var9 < 2 ? 10526880 : Drawing.alphaOver(4210752, 8421504, var11);
      int var13 = 5 - var9;
      if (var13 < 0) {
        var13 = 0;
      }

      int var14 = -5 + (20 + var3 - var9);
      if (var14 > var3) {
        var14 = var3;
      }

      final int var15 = var14 - var13;
      var13 += var0;
      var14 += var0;
      Drawing.setPixel(var13, var10, Drawing.WHITE);
      Drawing.horizontalLine(1 + var13, var10, var15 - 4, var12);
      Drawing.horizontalLine(var14 - 4, var10, 3, 10526880);
      Drawing.setPixel(var14 - 1, var10, Drawing.WHITE);
    }
  }

  private static void initializeResources() {
    Drawing.saveContext();
    INTRO_1_1 = SpriteResource.loadSprite(spriteLoader, "intro", "intro1_1");
    INTRO_1_2 = SpriteResource.loadSprite(spriteLoader, "intro", "intro1_2");
    INTRO_1_3 = SpriteResource.loadSprite(spriteLoader, "intro", "intro1_3");
    INTRO_1_FLASH = SpriteResource.loadSprite(spriteLoader, "intro", "intro1_flash");
    INTRO_1_GLOW = SpriteResource.loadSprite(spriteLoader, "intro", "intro1_glow");
    INTRO_1_GLOW_FG = SpriteResource.loadSprite(spriteLoader, "intro", "intro1_glow_fg");
    INTRO_1_3.installForDrawing();
    a455cle(Drawing.width, 0, 64, Drawing.height);
    INTRO_1_2.installForDrawing();
    a455cle(Drawing.width, 0, 128, Drawing.height);
    INTRO_1_1.installForDrawing();
    a669mws(Drawing.height, Drawing.width);
    INTRO_2_EARTH = SpriteResource.loadSprite(spriteLoader, "intro", "intro2_earth");
    INTRO_2_CLOUDS = SpriteResource.loadSprite(spriteLoader, "intro", "intro2_clouds");
    a046ja(INTRO_2_EARTH.width / 2, INTRO_2_EARTH, INTRO_2_EARTH.height / 4);
    a046ja(INTRO_2_CLOUDS.width / 2, INTRO_2_CLOUDS, INTRO_2_CLOUDS.height / 4);
    _dsb = new Sprite(ShatteredPlansClient.SCREEN_WIDTH, 430);
    _dsb.installForDrawing();

    for (int var1 = 1; var1 < 64; ++var1) {
      final int var2 = (int) (20.0D * Math.log(63.0F / (float) var1) / Math.log(2.0D));
      Drawing.fillCircle(320, 527, var2 + 400, (var1 << 2) * 65793);
    }

    Drawing.b669(4, 4, Drawing.width, Drawing.height);
    _mar = new Sprite(ShatteredPlansClient.SCREEN_WIDTH, 624 - INTRO_2_EARTH.y);
    _mar.installForDrawing();

    for (int var1 = 254; var1 >= 1; --var1) {
      final int var2 = (int) (Math.log(254.0F / (float) var1) * 40.0D / Math.log(2.0D));
      INTRO_2_EARTH.e326(var2 - 5, var2 - INTRO_2_EARTH.y, var1 * 65793);
    }

    INTRO_3_STARS = SpriteResource.loadSprite(spriteLoader, "intro", "intro3_stars");
    INTRO_3_CITY_BG = SpriteResource.loadSprite(spriteLoader, "intro", "intro3_citybg");
    INTRO_3_CITY_FG = SpriteResource.loadSprite(spriteLoader, "intro", "intro3_cityfg");
    INTRO_3_CRATER = SpriteResource.loadSprite(spriteLoader, "intro", "intro3_crater");
    _heh = new Sprite(INTRO_3_CITY_BG.width, INTRO_3_CITY_BG.height + 60);

    if (INTRO_3_CITY_BG.height * INTRO_3_CITY_BG.width >= 0) {
      System.arraycopy(INTRO_3_CITY_BG.pixels, 0, _heh.pixels, INTRO_3_CITY_BG.width * 60, INTRO_3_CITY_BG.height * INTRO_3_CITY_BG.width);
    }

    _heh.offsetX = INTRO_3_CITY_BG.offsetX;
    _heh.x = INTRO_3_CITY_BG.x;
    _heh.y = INTRO_3_CITY_BG.y - 60;
    _heh.offsetY = 60 + INTRO_3_CITY_BG.offsetY;
    _wef = new Sprite(INTRO_3_CITY_FG.width, 60 + INTRO_3_CITY_FG.height);

    if (INTRO_3_CITY_FG.height * INTRO_3_CITY_FG.width >= 0) {
      System.arraycopy(INTRO_3_CITY_FG.pixels, 0, _wef.pixels, INTRO_3_CITY_FG.width * 60, INTRO_3_CITY_FG.height * INTRO_3_CITY_FG.width);
    }

    _wef.x = INTRO_3_CITY_FG.x;
    _wef.offsetY = INTRO_3_CITY_FG.offsetY + 60;
    _wef.y = INTRO_3_CITY_FG.y - 60;
    _wef.offsetX = INTRO_3_CITY_FG.offsetX;
    _heh.installForDrawing();
    Drawing.b669(60, 60, _heh.width, _heh.height);
    _wef.installForDrawing();
    Drawing.b669(60, 60, _wef.width, _wef.height);
    _ksj = INTRO_3_STARS.copy();
    _ksj.d797();

    for (int var3 = 0; var3 < _ksj.pixels.length; ++var3) {
      final int var4 = _ksj.pixels[var3];
      final int var5 = 8323072 & var4 >> 1;
      int var6 = (var5 >> 9 & 32512) + (var4 & '\uff00');
      var6 = var6 * 3 >> 1;
      if (var6 <= 65280) {
        var6 &= 65280;
      } else {
        var6 = 65280;
      }

      final int var7 = 255 & var4;
      _ksj.pixels[var3] = var6 | var5 | var7;
    }

    _qqn = INTRO_2_EARTH.horizontallyFlipped();
    a332uo(_qqn);
    final int var102 = _qqn.width;
    final int var103 = _qqn.width + 120;
    _tfb = new Sprite(var102, var103);
    _tfb.installForDrawing();
    _qqn.e326(0, 120, 4259648);
    Drawing.b669(120, 120, var102, var103);
    Drawing.restoreContext();
    resourcesInitialized = true;
  }

  private static void a150ph() {
    if (_cbh != null) {
      _cbh.removeIf(var1 -> var1._h < 0);
    }
  }

  private static void freeResources() {
    resourcesInitialized = false;
    INTRO_3_CRATER = null;
    INTRO_1_GLOW = null;
    _wef = null;
    INTRO_1_3 = null;
    _qqn = null;
    INTRO_1_2 = null;
    INTRO_3_STARS = null;
    _ksj = null;
    _tfb = null;
    INTRO_2_EARTH = null;
    INTRO_1_FLASH = null;
    INTRO_1_GLOW_FG = null;
    _mar = null;
    INTRO_3_CITY_BG = null;
    INTRO_2_CLOUDS = null;
    _dsb = null;
    INTRO_3_CITY_FG = null;
    _heh = null;
    INTRO_1_1 = null;
  }

  private static void b150gq() {
    for (final nd_ var3 : _feC) {
      var3._h = 0;
      var3._a = 0;
    }

    _rpK.clear();
  }

  private static void b150ej() {
    if (_cbh != null) {
      final Iterator<RenderedTextLine> it = _cbh.iterator();

      if (it.hasNext()) {
        final RenderedTextLine var0 = it.next();
        if (++var0._h == 300) {
          _cfe += (float) var0._i.height;
          it.remove();
        }
      }

      while (it.hasNext()) {
        final RenderedTextLine var0 = it.next();
        if (++var0._h > 75) {
          var0._h = 75;
        }
      }

      if (_cfe > 0.0F) {
        _cfe *= 0.9F;
      }
    }
  }

  private static void tickStage2() {
    final int var0 = _dim * 100 / 1000 - 80;
    final short var1 = 465;
    final int var2 = 324 + var0 + 675;

    for (final nd_ var6 : _feC) {
      if (var6._h != 0) {
        var6._a += var6._h;
        if (var6._a >= 200) {
          var6._h = -var6._h;
          float var7 = (float) (var6._e - var1) / 500.0F;
          float var8 = (float) (var6._j - var2) / 500.0F;
          var7 = (float) ((double) var7 + (Math.random() - 0.5D) / 5.0D);
          var8 = (float) ((double) var8 + (Math.random() - 0.5D) / 5.0D);
          _rpK.add(new sb_((float) (var6._e - 5), (float) (var0 + var6._j), var7 / 200.0F, var8 / 200.0F));
        }

        if (var6._a <= 0) {
          var6._a = 0;
          var6._h = 0;
        }
      }

      if (var6._a == 0 && ShatteredPlansClient.randomIntBounded(250) == 0 && _dim > 75) {
        var6._h = 10;
      }
    }

    for (final sb_ var11 : _rpK) {
      var11._o[var11._q] = var11._l;
      var11._i[var11._q] = var11._n;
      if (++var11._q == 50) {
        var11._q = 0;
      }

      var11._p += var11._r;
      var11._m += var11._s;
      var11._j += var11._m;
      var11._k += var11._p;
      var11._n += var11._j;
      var11._l += var11._k;
    }

    if (_dim == 0) {
      a740sg(false, 100, StringConstants.TEXT_INTRO_2_START, currentFont);
      a740sg(true, 800, StringConstants.TEXT_INTRO_2_END, currentFont);
    }

    if (++_dim == 950) {
      a366jc(3);
    }

  }

  private static void tickStage3() {
    if (_dim == 0) {
      a740sg(false, 50, StringConstants.TEXT_INTRO_3_START, currentFont);
    }

    if (++_dim == 750) {
      a366jc(4);
    }
  }

  private static void tickStage4() {
    if (_dim == 0) {
      a740sg(false, 0, StringConstants.TEXT_INTRO_4_START, currentFont);
      a740sg(false, 700, StringConstants.TEXT_INTRO_4_END, currentFont);
    }

    if (++_dim == 950) {
      a366jc(5);
    }

  }

  private static void tickStage5() {
    if (++_dim == 350) {
      a366jc(-1);
    }

  }

  private static void tickStage1() {
    for (final nd_ var2 : _fey) {
      if (var2._h != 0) {
        var2._a += var2._h;
        if (var2._a >= 100) {
          var2._h = -var2._h;
        }

        if (var2._a <= 0) {
          var2._a = 0;
          var2._h = 0;
        }
      }

      if (var2._a == 0 && ShatteredPlansClient.randomIntBounded(100) == 0) {
        var2._h = 10;
      }
    }

    for (final nd_ var2 : _feH) {
      if (var2._h != 0) {
        var2._a += var2._h;
        if (var2._a >= 100) {
          var2._h = -var2._h;
        }

        if (var2._a <= 0) {
          var2._a = 0;
          var2._h = 0;
        }
      }

      if (var2._a == 0 && ShatteredPlansClient.randomIntBounded(100) == 0) {
        var2._h = 10;
      }
    }

    if (_dim == 0) {
      a740sg(false, 0, StringConstants.TEXT_INTRO_1_START, currentFont);
      a740sg(true, 400, StringConstants.TEXT_INTRO_1_END, currentFont);
    }

    if (++_dim == 500) {
      a366jc(2);
    }
  }

  private static void tickStage0() {
    if (++_dim >= 200 && resourcesInitialized && !_pn) {
      a366jc(1);
    }
  }

  private static void a669mws(int var2, int var4) {
    final int var0 = 0;
    final int var3 = 0;

    if (var0 + var2 > Drawing.height) {
      var2 = Drawing.height - var0;
    }

    if (var3 + var4 > Drawing.width) {
      var4 = Drawing.width;
    }

    final int var6 = 256;
    final int var7 = var0 + var2;

    for (int var8 = var0; var8 < var7; ++var8) {
      int var9 = Drawing.pixelIndex(var3, var8);

      for (int var10 = var4; var10 > 0; --var10) {
        final int var11 = Drawing.screenBuffer[var9];
        final int r = (var11 & 0xff0000) >> 16;
        final int g = (var11 & 0x00ff00) >> 8;
        final int b = (var11 & 0x0000ff);
        final int var15 = 5 * r - (-(g * 6) - 5 * b);
        final int var16 = (var15 * var6 >> 12);
        final int var17 = (var6 * var15 >> 12);
        final int var18 = (var15 * var6 >> 12);
        final int var19 = var16 << 16 | var17 << 8 | var18;
        Drawing.screenBuffer[var9] = var19;
        ++var9;
      }
    }
  }

  private static void a366jc(final int var0) {
    nextStage = var0;
    _pn = true;
    if (var0 == 1) {
      ShatteredPlansClient.a827jo(Sounds.MUSIC_INTRO, 476, true);
    }
  }

  private static void a885tg(int var0, final Sprite var1) {
    int var2 = -5 + var1.x;
    var0 += var1.y;
    int var3 = 0;
    int var4 = 0;
    int var5 = var1.width;
    if (Drawing.left > var2) {
      var5 += var2 - Drawing.left;
      var3 = -var2 + Drawing.left;
      var2 = Drawing.left;
    }

    int var6 = var1.height;
    if (var0 < Drawing.top) {
      var4 = -var0 + Drawing.top;
      var6 += -Drawing.top + var0;
      var0 = Drawing.top;
    }

    if (var5 + var2 > Drawing.right) {
      var5 = Drawing.right - var2;
    }

    if (var6 + var0 > Drawing.bottom) {
      var6 = -var0 + Drawing.bottom;
    }

    int var8 = var3 + var4 * var1.width;
    final int var9 = -var5 + var1.width;
    int var10 = Drawing.width * var0 + var2;
    final int var11 = -var5 + Drawing.width;

    for (var0 = -var6; var0 < 0; ++var0) {
      for (var2 = -var5; var2 < 0; ++var2) {
        int var12 = 255 & var1.pixels[var8++];
        if (var12 == 0) {
          ++var10;
        } else {
          int var13 = Drawing.screenBuffer[var10];
          if (var12 == 255) {
            var13 = 8355711 & var13 >>> 1 | 8421504;
          } else {
            var12 >>= 2;
            int var14 = var13 & '\uff00';
            var13 &= 16711935;
            var13 *= 256 - var12;
            var13 &= -16711936;
            var14 *= 256 - var12;
            var14 &= 16711680;
            var13 = (var13 | var14) >>> 8;
            var13 += var12 * 65793;
          }

          Drawing.screenBuffer[var10++] = var13;
        }
      }

      var8 += var9;
      var10 += var11;
    }

  }

  private static void a740sg(final boolean var0, int var1, final String var2, final Font font) {
    if (_cbh == null) {
      _cbh = new ArrayList<>();
    }

    final String[] strs = new String[32];
    final int var5 = font.breakLines(var2, new int[]{600}, strs);
    if (var0) {
      var1 -= var5 * 50;
    }

    for (int i = 0; i < var5; ++i) {
      _cbh.add(new RenderedTextLine(strs[i], font, i * 50 + var1));
    }
  }

  private static void a046ja(final int var1, final Sprite var2, final int var3) {
    int var4 = -1;
    final int var5 = var2.width;
    final int var6 = Math.max(-var1 + var5, var1);

    final int var7 = var2.height;
    int var8 = var3;
    if (var3 < -var3 + var7) {
      var8 = var7 - var3;
    }

    for (int var9 = 0; var9 < var7; ++var9) {
      for (int var10 = 0; var5 > var10; ++var10) {
        ++var4;
        final int var11 = var2.pixels[var4];
        if (var11 != 0) {
          float var12 = (float) ((-var1 + var10) * (var10 - var1) + (-var3 + var9) * (-var3 + var9));
          var12 /= (float) (MathUtil.euclideanDistanceSquared(var8, var6));
          final int var13 = (int) (256.0F * (-((float) Math.sqrt(var12)) + 1.0F));
          if (var13 > 0) {
            if (var13 <= 255) {
              int var14 = var11 & 16711935;
              var14 *= var13;
              int var15 = '\uff00' & var11;
              var15 *= var13;
              final int i = (16711680 & var15 | -16711936 & var14) >>> 8;
              var2.pixels[var4] = i != 0 ? i : 1;
            }
          } else {
            var2.pixels[var4] = 1;
          }
        }
      }
    }
  }

  private static void draw(final Sprite sprite, final int x, final int y, final int alpha, final int var0) {
    if (var0 >= 128) {
      Drawing.setBounds(x, 0, x + var0 - 128, y + ShatteredPlansClient.SCREEN_HEIGHT);
      sprite.drawAdd(x, y, alpha);
    }

    final int var5 = var0 < 128 ? 0 : var0 - 128;

    for (int var7 = var5; var0 > var7; ++var7) {
      Drawing.setBounds(x + var7, 0, x + var7 + 1, ShatteredPlansClient.SCREEN_HEIGHT + y);
      final int var8 = alpha * (-var7 + var0) >> 7;
      sprite.drawAdd(x, y, var8);
    }

    Drawing.a797();
  }

  private static void a332uo(final Sprite var0) {
    final int var1 = 32768;
    int var3 = -1;

    for (int var4 = -var0.height; var4 < 0; ++var4) {
      for (int var5 = -var0.width; var5 < 0; ++var5) {
        ++var3;
        final int var6 = var0.pixels[var3];
        if (var6 != 0) {
          final int var7 = ((var6 & 16711680) >> 16) + (var6 & 255) + (('\uff00' & var6) >> 7) >> 2;
          int i = (var1 * var7 & 16711680) >>> 8;
          if (i == 0) {
            i = 1;
          }

          var0.pixels[var3] = i;
        }
      }
    }
  }
}
