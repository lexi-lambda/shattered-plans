package funorb.client.intro;

import funorb.audio.VorbisFormat;
import funorb.cache.ResourceLoader;
import funorb.client.JagexBaseApplet;
import funorb.graphics.Drawing;
import funorb.graphics.Sprite;
import funorb.graphics.mq_;
import funorb.io.CipheredBuffer;
import funorb.shatteredplans.client.JagexApplet;
import funorb.util.BitMath;

import java.util.stream.IntStream;

public final class JagexLogoIntroAnimation {
  private static final Sprite _rfh = new Sprite(270, 70);
  private static final Sprite _ini = new Sprite(540, 140);
  private static sr_[] _vcd;
  private static int[] _ucw;
  private static hc_[] _wca;
  private static Sprite _qaq;
  private static Sprite _akz;
  private static int[][] _faZ;

  private static int jagexIntroAnimationFrame;
  private static int width;
  private static int height;
  private static int halfWidth;
  private static int halfHeight;

  private static final int[] _uoh = new int[128];
  private static final int[] _gdb = new int[1024];
  private static final int[] _noc = new int[8192];
  private static final int[] _hko = new int[8192];
  private static final int[] _fmc = new int[8192];
  private static final int[] _cff = new int[8192];
  private static final int[] _hei = new int[8192];
  private static final int[] _pmC = new int[8192];
  private static final int[] _jlM = new int[16384];

  private static int[] _h = new int[1024];
  private static int _qje = 0;
  private static int[] _mwsa;

  //<editor-fold desc="Initialization">
  public static void load(final ResourceLoader loader) {
    load1(loader);
    load2(loader);
    load3(loader);
    load4();
    load5();

    jagexIntroAnimationFrame = -55;
  }

  private static void load1(final ResourceLoader loader) {
    VorbisFormat.readIdentificationAndSetup(loader.getResource("headers.packvorbis", ""));
    final VorbisFormat var2 = VorbisFormat.loadAudio(loader, "jagex logo2.packvorbis", "");
    assert var2 != null;
    var2.b720();
  }

  private static void load2(final ResourceLoader loader) {
    final CipheredBuffer var2 = new CipheredBuffer(loader.getResource("logo.fo3d", ""));
    final int var3 = var2.readUByte();
    var2.m150();
    _wca = a477ip(var2);
    _vcd = new sr_[var3];
    _faZ = new int[var3][];
    for (int var4 = 0; var3 > var4; ++var4) {
      _vcd[var4] = a523ec(var2);
    }

    var2.i423();

    for (int var4 = 0; var3 > var4; ++var4) {
      final sr_ var5 = _vcd[var4];
      var5.a050();
      var5.a487();
      final int[] var6 = new int[]{var5._O + var5._k >> 1, var5._j + var5._t >> 1, var5._E + var5._r >> 1};
      _faZ[var4] = var6;
      var5.a115(-var6[2], -var6[0], -var6[1]);
    }
  }

  private static void load3(final ResourceLoader loader) {
    final Sprite var1 = new Sprite(loader.getResource("final_frame.jpg", ""), JagexBaseApplet.canvas);
    final int var2 = var1.width;
    final int var3 = var1.height;
    Drawing.withLocalContext(() -> {
      _qaq = new Sprite(var2, 3 * var3 / 4);
      _qaq.installForDrawing();
      var1.c093(0, 0);
      _akz = new Sprite(var2, -_qaq.height + var3);
      _akz.installForDrawing();
      var1.c093(0, -_qaq.height);
      _akz.y = _qaq.height;
    });
  }

  private static void load4() {
    c797wn();
    _ucw = new int[260];

    int var0;
    for (var0 = 0; var0 < 256; ++var0) {
      final double var1 = 15.0D;
      _ucw[var0] = (int) (255.0D * Math.pow((float) var0 / 256.0F, var1));
    }

    for (var0 = 256; var0 < _ucw.length; ++var0) {
      _ucw[var0] = 255;
    }
  }

  private static void load5() {
    final Sprite var0 = new Sprite(540, 140);
    var0.withInstalledForDrawingUsingOffsets(() -> {
      c797wn();
      Drawing.clear();
      jagexIntroAnimationFrame = 0;
      e150gq();
      final Sprite var1 = var0.copy();

      for (int var2 = 0; var2 < 15; ++var2) {
        var1.e326(-2, -2, Drawing.WHITE);
        Drawing.b669(4, 4, 540, 140);
      }

      _rfh.installForDrawing();
      var0.d093(0, 0);
    });
  }
  //</editor-fold>

  @SuppressWarnings("BooleanMethodIsAlwaysInverted")
  public static boolean isFinished() {
    return JagexApplet.DEBUG_MODE || jagexIntroAnimationFrame > 250;
  }

  public static void tick() {
    ++jagexIntroAnimationFrame;
  }

  public static void draw() {
    if (jagexIntroAnimationFrame >= 0) {
      final int var2 = 185;
      final int var3 = 205;
      int var4 = 256;
      if (jagexIntroAnimationFrame < 75) {
        var4 = (jagexIntroAnimationFrame << 8) / 75;
      }

      if (jagexIntroAnimationFrame > 200) {
        var4 = (-jagexIntroAnimationFrame + 250 << 8) / 50;
      }

      final int finalVar4 = var4;
      _ini.withInstalledForDrawingUsingOffsets(() -> {
        c797wn();
        Drawing.clear();
        e150gq();
        if (finalVar4 < 256) {
          Drawing.fillRect(0, 0, Drawing.width, Drawing.height, 0, 256 - finalVar4);
        }
      });

      if (jagexIntroAnimationFrame < 150) {
        _ini.d093(var2, var3);
      } else {
        _qaq.draw(200, 215, var4);
      }

      final int var5 = jagexIntroAnimationFrame - 125;
      int var6;
      if (var5 > 0 && var5 < 50) {
        if (var5 < 20) {
          var6 = var5 * 256 / 20;
          _rfh.drawAdd(var2, var3, var6);
        } else if (var5 >= 30) {
          var6 = 256 * (50 - var5) / 20;
          _rfh.drawAdd(var2, var3, var6);
        } else {
          _rfh.drawAdd(var2, var3, 256);
        }
      }

      final int i = jagexIntroAnimationFrame - 140;
      if (i > 0) {
        var6 = 256;
        if (i < 20) {
          var6 = i * 256 / 20;
        }

        _akz.draw(200, 215, var6 * var4 >> 8);
      }
    }
  }

  private static void c797wn() {
    width = Drawing.right - Drawing.left;
    height = Drawing.bottom - Drawing.top;
    halfWidth = width / 2;
    halfHeight = height / 2;
    if (_h.length < height) {
      _h = new int[BitMath.nextLowestPowerOf2(height)];
    }

    int var4 = Drawing.top * Drawing.width + Drawing.left;

    for (int var5 = 0; var5 < height; ++var5) {
      _h[var5] = var4;
      var4 += Drawing.width;
    }
  }

  private static void a806pe(final int[] var2, final sr_ var4) {
    final int var7 = (var2[0] << 16) >> 5;
    final int var8 = (var2[1] * 0xffff0000) >> 5;
    final int var9 = ((var2[2] - 0xffffe030) << 16) >> 16;
    final int var10 = (var2[3] << 14) >> 14;
    final int var11 = var2[4] * 0xffffc000 >> 14;
    final int var12 = (var2[5] << 14) >> 14;
    final int var13 = (var2[6] << 14) >> 14;
    final int var14 = var2[7] * 0xffffc000 >> 14;
    final int var15 = (var2[8] << 14) >> 14;
    final int var16 = (var2[9] << 14) >> 14;
    final int var17 = var2[10] * 0xffffc000 >> 14;
    final int var18 = (var2[11] << 14) >> 14;

    int var5 = Integer.MAX_VALUE;
    int var6 = Integer.MIN_VALUE;
    for (int i = 0; i < var4._x; ++i) {
      final short var31 = var4._w[i];
      final short var32 = var4._z[i];
      final short var33 = var4._f[i];
      final int var25 = (var10 * var31 + var32 * var13 + var33 * var16 >> 5) + var7;
      final int var26 = var8 + (var32 * var14 + var31 * var11 + var33 * var17 >> 5);
      final int var27 = var9 + (var32 * var15 + var31 * var12 + var33 * var18 >> 16);
      if (var27 < 50) {
        _noc[i] = Integer.MIN_VALUE;
      } else {
        _pmC[i] = var25 / var27 + halfWidth;
        _hei[i] = var26 / var27 + halfHeight;
        if (var5 > var27) {
          var5 = var27;
        }

        if (var27 > var6) {
          var6 = var27;
        }

        _noc[i] = var27;
      }
    }

    final int i4 = var2[3];
    final int i5 = var2[4];
    final int i6 = var2[5];
    final int i7 = var2[6];
    final int i8 = var2[7];
    final int i9 = var2[8];
    final int i10 = var2[9];
    final int i11 = var2[10];
    final int i12 = var2[11];
    for (int i = 0; i < var4._e && i < _fmc.length; ++i) {
      final short var28 = var4._I[i];
      final short var29 = var4._y[i];
      final short var30 = var4._v[i];
      _fmc[i] = var30 * i10 + var29 * i7 + i4 * var28 >> 16;
      _cff[i] = var30 * i11 + var28 * i5 + i8 * var29 >> 16;
      _hko[i] = i6 * var28 - (-(i9 * var29) - var30 * i12) >> 16;
    }

    a787ej(var4, var5, var6);
  }

  private static void a087er(final int var0, final int var1, final int var3, final int var4, final sr_ var5, final int var6, final int var7) {
    if (var5._p != null && var5._o > 1) {
      final byte[] var9 = var5._p;
      a181eb(var9);
    } else {
      a423kc();
    }

    final int[] var8 = new int[var5._e];
    final int[] var40 = new int[var5._e];
    final int[] var10 = _fmc;
    final int[] var11 = _cff;
    final int[] var12 = _hko;

    int var13;
    int var14;
    for (var13 = 0; var5._e > var13; ++var13) {
      var14 = var10[var13] * var0 + var11[var13] * var3 + var12[var13] * var7 >> 8;
      if (var14 < 0) {
        var14 = -var14;
      }

      var14 = var14 >= 128 ? 256 : var14 + 128;
      int var15 = var10[var13] * var6 - (-(var1 * var11[var13]) - var12[var13] * var4) >> 8;
      var15 = _ucw[var15 >= 0 ? var15 : -var15];
      var14 = (-var15 + 256) * var14 >>> 8;
      var8[var13] = var14;
      var40[var13] = var15;
    }

    for (var13 = 0; var13 < _qje; ++var13) {
      var14 = _jlM[var13];
      final short var41 = var5._s[var14];
      final short var16 = var5._i[var14];
      final short var17 = var5._B[var14];
      final short var18 = var5._M[var14] < _fmc.length ? var5._M[var14] : -1;
      final short var19 = var5._P[var14] < _fmc.length ? var5._P[var14] : -1;
      final short var20 = var5._n[var14] >= _fmc.length ? -1 : var5._n[var14];
      final hc_ var21 = _wca != null && var5._b != null && var14 < var5._b.length && var5._b[var14] != -1 && _wca.length > var5._b[var14] ? _wca[var5._b[var14]] : null;
      final int var22 = _pmC[var41];
      final int var23 = _hei[var41];
      final int var24 = _pmC[var16];
      final int var25 = _hei[var16];
      final int var26 = _pmC[var17];
      final int var27 = _hei[var17];
      final int var28;
      final int var29;
      final int var30;
      final int var31;
      final int var32;
      int var33;
      if (var18 == var19 && var20 == var19) {
        var28 = var8[var18];
        var29 = var40[var18];
        var30 = var21 != null ? var21._a : 8355711;
        var31 = 16711935 & var30;
        var32 = '\uff00' & var30;
        var33 = var28 * var31 >>> 8 & 268370175 | var28 * var32 >>> 8 & 989921024;
        var33 += 65793 * var29;
        a676oo(var23, var26, var27, var25, var33 >> 1 & 8355711, var24, var22);
      } else {
        var28 = var8[var18];
        var29 = var8[var19];
        var30 = var8[var20];
        var31 = var40[var18];
        var32 = var40[var19];
        var33 = var40[var20];
        final int var34 = var21 != null ? var21._a : 8355711;
        final int var35 = 16711935 & var34;
        final int var36 = var34 & '\uff00';
        int var37 = (-16711905 & var35 * var28) >>> 8 | var28 * var36 >>> 8 & -1845428480;
        int var38 = (16711777 & var36 * var29) >>> 8 | var29 * var35 >>> 8 & 1459552511;
        var37 += 65793 * var31;
        int var39 = var30 * var36 >>> 8 & -570360064 | (-16711872 & var30 * var35) >>> 8;
        var38 += 65793 * var32;
        var39 += var33 * 65793;
        a101bf((var38 & 'ｕ') >> 8, var37 & 255, var39 >> 16, var37 >> 8 & 255, var37 >> 16, var26, var22, var39 & 255, var25, 255 & var38, var27, (var39 & '\ufff3') >> 8, var24, var38 >> 16, var23);
      }
    }

  }

  private static void a495lr(final int var0, final int var1, final int var3, int var4, final int[] var5, final int var6, final int var7, final int var8, final int var9, final int var10, final int var11, final int var12, final int var13, final int var14, final int var15, final int var16) {
    if (var8 >= 0 && var4 < height) {
      if (var3 >= 0 || var0 >= 0 || var14 >= 0) {
        if (width > var3 || width > var0 || var14 < width) {
          final int var34 = -var4 + var8;
          int var17;
          int var18;
          int var19;
          int var20;
          int var21;
          int var22;
          int var23;
          int var24;
          int var25;
          int var26;
          int var27;
          int var28;
          int var29;
          int var30;
          int var31;
          int var32;
          final boolean var33;
          int var35;
          int var37;
          int var38;
          int var39;
          int var40;
          if (var15 == var4) {
            if (var8 == var4) {
              var27 = 0;
              var32 = 0;
              var18 = var0 << 16;
              var29 = var10;
              var23 = 0;
              var17 = var3 << 16;
              var24 = 0;
              var19 = 0;
              var21 = var9;
              var25 = var16;
              var28 = 0;
              var26 = var11;
              var31 = 0;
              var30 = var1;
              var20 = 0;
              var22 = var7;
            } else {
              var35 = -var15 + var8;
              if (var0 <= var3) {
                var28 = (var12 - var16 << 16) / var34;
                var30 = var10 << 16;
                var18 = var3 << 16;
                var29 = var1 << 16;
                var31 = (var13 - var1 << 16) / var35;
                var17 = var0 << 16;
                var20 = (-var3 + var14 << 16) / var34;
                var32 = (-var10 + var13 << 16) / var34;
                var25 = var11 << 16;
                var27 = (-var11 + var12 << 16) / var35;
                var21 = var7 << 16;
                var24 = (var6 - var9 << 16) / var34;
                var19 = (var14 - var0 << 16) / var35;
                var22 = var9 << 16;
                var26 = var16 << 16;
                var23 = (var6 - var7 << 16) / var35;
              } else {
                var23 = (-var9 + var6 << 16) / var34;
                var32 = (-var1 + var13 << 16) / var35;
                var27 = (var12 - var16 << 16) / var34;
                var29 = var10 << 16;
                var30 = var1 << 16;
                var22 = var7 << 16;
                var31 = (var13 - var10 << 16) / var34;
                var24 = (var6 - var7 << 16) / var35;
                var28 = (var12 - var11 << 16) / var35;
                var26 = var11 << 16;
                var25 = var16 << 16;
                var18 = var0 << 16;
                var17 = var3 << 16;
                var21 = var9 << 16;
                var19 = (var14 - var3 << 16) / var34;
                var20 = (var14 - var0 << 16) / var35;
              }
            }

            if (var4 < 0) {
              var4 = Math.min(-var4, 0);
              var22 += var24 * var4;
              var17 += var19 * var4;
              var25 += var27 * var4;
              var18 += var4 * var20;
              var29 += var31 * var4;
              var30 += var4 * var32;
              var26 += var4 * var28;
              var21 += var23 * var4;
              var4 = 0;
            }

          } else {
            var25 = var26 = var16 << 16;
            var17 = var18 = var3 << 16;
            var21 = var22 = var9 << 16;
            var29 = var30 = var10 << 16;
            var35 = var15 - var4;
            var20 = (var14 - var3 << 16) / var34;
            var19 = (-var3 + var0 << 16) / var35;
            if (var20 <= var19) {
              final int var36 = var19;
              var19 = var20;
              var20 = var36;
              var27 = (var12 - var16 << 16) / var34;
              var32 = (var1 - var10 << 16) / var35;
              var24 = (-var9 + var7 << 16) / var35;
              var31 = (var13 - var10 << 16) / var34;
              var23 = (-var9 + var6 << 16) / var34;
              var33 = true;
              var28 = (var11 - var16 << 16) / var35;
            } else {
              var27 = (var11 - var16 << 16) / var35;
              var32 = (var13 - var10 << 16) / var34;
              var28 = (-var16 + var12 << 16) / var34;
              var31 = (-var10 + var1 << 16) / var35;
              var33 = false;
              var23 = (var7 - var9 << 16) / var35;
              var24 = (-var9 + var6 << 16) / var34;
            }

            label146:
            {
              if (var4 < 0) {
                if (var15 < 0) {
                  var4 = var15 - var4;
                  var17 += var19 * var4;
                  var30 += var4 * var32;
                  var29 += var4 * var31;
                  var22 += var4 * var24;
                  var26 += var28 * var4;
                  var18 += var4 * var20;
                  var21 += var23 * var4;
                  var25 += var27 * var4;
                  var4 = var15;
                  break label146;
                }

                var4 = -var4;
                var29 += var4 * var31;
                var26 += var28 * var4;
                var21 += var4 * var23;
                var25 += var4 * var27;
                var22 += var4 * var24;
                var18 += var4 * var20;
                var30 += var32 * var4;
                var17 += var19 * var4;
                var4 = 0;
              }

              for (int var36 = _h[var4]; var4 < var15; var30 += var32) {
                var37 = var17 >> 16;
                if (var37 < width) {
                  var38 = (var18 >> 16) - (var17 >> 16);
                  if (var38 != 0) {
                    var39 = (var22 - var21) / var38;
                    var40 = (var26 - var25) / var38;
                    final int var41 = (var30 - var29) / var38;
                    if (width <= var38 + var37) {
                      var38 = width - 1 - var37;
                    }

                    if (var37 >= 0) {
                      a575jg(var38, var29, var37 + var36, var40, var25, var21, var41, var5, var39);
                    } else {
                      a575jg(var38 + var37, var29 - var37 * var41, var36, var40, -(var40 * var37) + var25, -(var37 * var39) + var21, var41, var5, var39);
                    }
                  } else if (var37 >= 0) {
                    a575jg(var38, var29, var37 + var36, 0, var25, var21, 0, var5, 0);
                  }
                }

                ++var4;
                if (height <= var4) {
                  return;
                }

                var21 += var23;
                var29 += var31;
                var17 += var19;
                var18 += var20;
                var22 += var24;
                var25 += var27;
                var36 += Drawing.width;
                var26 += var28;
              }
            }

            final int var36 = -var15 + var8;
            if (var36 == 0) {
              var19 = 0;
              var28 = 0;
              var31 = 0;
              var23 = 0;
              var27 = 0;
              var24 = 0;
              var20 = 0;
              var32 = 0;
            } else {
              var37 = var14 << 16;
              var38 = var6 << 16;
              var39 = var12 << 16;
              if (var33) {
                var30 = var1 << 16;
                var22 = var7 << 16;
                var26 = var11 << 16;
                var18 = var0 << 16;
              } else {
                var21 = var7 << 16;
                var29 = var1 << 16;
                var17 = var0 << 16;
                var25 = var11 << 16;
              }

              var40 = var13 << 16;
              var19 = (var37 - var17) / var36;
              var27 = (-var25 + var39) / var36;
              var23 = (-var21 + var38) / var36;
              var28 = (-var26 + var39) / var36;
              var31 = (var40 - var29) / var36;
              var32 = (-var30 + var40) / var36;
              var24 = (-var22 + var38) / var36;
              var20 = (-var18 + var37) / var36;
            }
          }

          if (var4 < 0) {
            var4 = -var4;
            var26 += var4 * var28;
            var30 += var4 * var32;
            var29 += var4 * var31;
            var18 += var20 * var4;
            var25 += var4 * var27;
            var22 += var24 * var4;
            var17 += var19 * var4;
            var21 += var4 * var23;
            var4 = 0;
          }

          for (var35 = _h[var4]; var4 < var8; var30 += var32) {
            final int var36 = var17 >> 16;
            if (var36 < width) {
              var37 = -(var17 >> 16) + (var18 >> 16);
              if (var37 != 0) {
                var38 = (-var21 + var22) / var37;
                var39 = (-var25 + var26) / var37;
                var40 = (-var29 + var30) / var37;
                if (var37 + var36 >= width) {
                  var37 = -var36 + (width - 1);
                }

                if (var36 < 0) {
                  a575jg(var37 + var36, -(var36 * var40) + var29, var35, var39, var25 - var36 * var39, var21 - var36 * var38, var40, var5, var38);
                } else {
                  a575jg(var37, var29, var36 + var35, var39, var25, var21, var40, var5, var38);
                }
              } else if (var36 >= 0) {
                a575jg(var37, var29, var36 + var35, 0, var25, var21, 0, var5, 0);
              }
            }

            ++var4;
            if (var4 >= height) {
              return;
            }

            var25 += var27;
            var21 += var23;
            var26 += var28;
            var35 += Drawing.width;
            var22 += var24;
            var17 += var19;
            var18 += var20;
            var29 += var31;
          }
        }
      }
    }
  }

  private static void a575jg(int var0, int var1, int var2, final int var3, int var4, int var5, final int var6, final int[] var7, final int var8) {
    while (true) {
      --var0;
      if (var0 < 0) {
        return;
      }

      final int var14 = (var7[var2] & 16711422) >> 1;
      var7[var2] = var14 + ((33471547 & var1) >> 17) - (-(65280 & var4 >> 9) - ((33423360 & var5) >> 1));
      ++var2;
      var1 += var6;
      var5 += var8;
      var4 += var3;
    }
  }

  private static void a451oo(final int var0, final int var1, final int var3, final int var4, final int var5, final int[] var6, int var7, final int var8) {
    if (var8 >= 0 && var7 < height) {
      if (var3 >= 0 || var1 >= 0 || var0 >= 0) {
        if (var3 < width || var1 < width || width > var0) {
          final int var14 = -var7 + var8;
          int var9;
          int var10;
          int var11;
          int var12;
          final boolean var13;
          int var15;
          int var16;
          int var17;
          if (var5 == var7) {
            if (var7 == var8) {
              var9 = var3 << 16;
              var11 = 0;
              var12 = 0;
              var10 = var1 << 16;
            } else {
              var15 = var8 - var5;
              if (var3 < var1) {
                var10 = var1 << 16;
                var12 = (var0 - var1 << 16) / var15;
                var9 = var3 << 16;
                var11 = (var0 - var3 << 16) / var14;
              } else {
                var10 = var3 << 16;
                var12 = (var0 - var3 << 16) / var14;
                var11 = (-var1 + var0 << 16) / var15;
                var9 = var1 << 16;
              }
            }

            if (var7 < 0) {
              var7 = Math.min(-var7, -var7 + var5);
              var9 += var7 * var11;
              var10 += var12 * var7;
              var7 = 0;
            }

          } else {
            var9 = var10 = var3 << 16;
            var15 = var5 - var7;
            var11 = (var1 - var3 << 16) / var15;
            var12 = (-var3 + var0 << 16) / var14;
            if (var11 >= var12) {
              var13 = true;
              var16 = var11;
              var11 = var12;
              var12 = var16;
            } else {
              var13 = false;
            }

            label143:
            {
              if (var7 < 0) {
                if (var5 < 0) {
                  var7 = -var7 + var5;
                  var10 += var12 * var7;
                  var9 += var7 * var11;
                  var7 = var5;
                  break label143;
                }

                var7 = -var7;
                var9 += var7 * var11;
                var10 += var12 * var7;
                var7 = 0;
              }

              for (var16 = _h[var7]; var7 < var5; var9 += var11) {
                var17 = var9 >> 16;
                if (width > var17) {
                  int var18 = -(var9 >> 16) + (var10 >> 16);
                  if (var18 == 0) {
                    if (var17 >= 0) {
                      a743dp(var17 + var16, var6, var4, 0);
                    }
                  } else {
                    if (width <= var17 + var18) {
                      var18 = width - 1 - var17;
                    }

                    if (var17 < 0) {
                      a743dp(var16, var6, var4, var17 + var18);
                    } else {
                      a743dp(var17 + var16, var6, var4, var18);
                    }
                  }
                }

                ++var7;
                if (height <= var7) {
                  return;
                }

                var10 += var12;
                var16 += Drawing.width;
              }
            }

            var16 = var8 - var5;
            if (var16 == 0) {
              var11 = 0;
              var12 = 0;
            } else {
              if (var13) {
                var10 = var1 << 16;
              } else {
                var9 = var1 << 16;
              }

              var17 = var0 << 16;
              var11 = (var17 - var9) / var16;
              var12 = (var17 - var10) / var16;
            }
          }

          if (var7 < 0) {
            var7 = -var7;
            var9 += var7 * var11;
            var10 += var12 * var7;
            var7 = 0;
          }

          for (var15 = _h[var7]; var8 > var7; var9 += var11) {
            var16 = var9 >> 16;
            if (var16 < width) {
              var17 = (var10 >> 16) - (var9 >> 16);
              if (var17 != 0) {
                if (var16 + var17 >= width) {
                  var17 = width - 1 - var16;
                }

                if (var16 >= 0) {
                  a743dp(var16 + var15, var6, var4, var17);
                } else {
                  a743dp(var15, var6, var4, var17 + var16);
                }
              } else if (var16 >= 0) {
                a743dp(var15 + var16, var6, var4, var17);
              }
            }

            ++var7;
            if (var7 >= height) {
              return;
            }

            var15 += Drawing.width;
            var10 += var12;
          }

        }
      }
    }
  }

  private static void a743dp(int var0, final int[] var1, final int var2, int var3) {
    while (true) {
      --var3;
      if (var3 < 0) {
        return;
      }

      var1[var0] = var2 + ((var1[var0] & 16711422) >> 1);
      ++var0;
    }
  }

  private static void a787ej(final sr_ var2, final int var3, final int var4) {
    final int var5 = BitMath.lastSet((var4 - var3) * 3);
    final int var6 = 3 * var3;
    g423ah();
    final int var7 = var5 - 10;
    if (var2._o > 0 && var2._p != null) {
      e150mf();
    }

    _qje = 0;

    int var8;
    label72:
    for (var8 = 0; var8 < var2._u; ++var8) {
      final short var9 = var2._s[var8];
      final short var10 = var2._i[var8];
      final short var11 = var2._B[var8];
      final int var12;
      final int var13;
      final int var14;
      final int var15;
      int var16;
      int var17;

      var12 = _noc[var9];
      if (var12 != Integer.MIN_VALUE) {
        var13 = _noc[var10];
        if (var13 != Integer.MIN_VALUE) {
          var14 = _noc[var11];
          if (var14 != Integer.MIN_VALUE) {
            var15 = var13 + var12 - (-var14 + var6);
            var16 = -(var7 < 0 ? var15 << -var7 : var15 >> var7) - 1 + _gdb.length;

            for (var17 = _gdb[var16]; var17 >> 4 != 0; var17 = _gdb[var16]) {
              --var16;
              if (var16 < 0) {
                System.err.println("Out of range!");
                continue label72;
              }
            }

            final int var18 = (var16 << 4) + var17;
            _jlM[var18] = var8;
            _gdb[var16] = var17 + 1;
            if (var2._o > 0 && var2._p != null) {
              _uoh[var2._p[var8]]++;
            }

            ++_qje;
          }
        }
      }
    }

    if (var2._o > 0 && var2._p != null) {
      var8 = 0;

      for (int var19 = 0; _uoh.length > var19; ++var19) {
        final int var20 = _uoh[var19];
        _uoh[var19] = var8;
        var8 += var20;
      }
    }

  }

  private static void g423ah() {
    final int[] var0 = _gdb;
    int var1 = 0;

    for (final int var3 = var0.length; var3 > var1; var0[var1++] = 0) {
      var0[var1++] = 0;
      var0[var1++] = 0;
      var0[var1++] = 0;
      var0[var1++] = 0;
      var0[var1++] = 0;
      var0[var1++] = 0;
      var0[var1++] = 0;
    }
  }

  private static void e150mf() {
    final int[] var1 = _uoh;

    int var2 = 0;

    for (final int var3 = var1.length; var3 > var2; var1[var2++] = 0) {
      var1[var2++] = 0;
      var1[var2++] = 0;
      var1[var2++] = 0;
      var1[var2++] = 0;
      var1[var2++] = 0;
      var1[var2++] = 0;
      var1[var2++] = 0;
    }

  }

  private static void a101bf(final int var0, final int var1, final int var2, final int var3, final int var4, final int var5, final int var6, final int var7, final int var8, final int var9, final int var10, final int var11, final int var12, final int var13, final int var14) {
    if (var14 >= var8) {
      if (var10 <= var14) {
        if (var8 >= var10) {
          a495lr(var12, var9, var5, var10, Drawing.screenBuffer, var4, var13, var14, var2, var7, var0, var3, var1, var6, var8, var11);
        } else {
          a495lr(var5, var7, var12, var8, Drawing.screenBuffer, var4, var2, var14, var13, var9, var11, var3, var1, var6, var10, var0);
        }
      } else {
        a495lr(var6, var1, var12, var8, Drawing.screenBuffer, var2, var4, var10, var13, var9, var3, var11, var7, var5, var14, var0);
      }
    } else if (var8 < var10) {
      a495lr(var12, var9, var6, var14, Drawing.screenBuffer, var2, var13, var10, var4, var1, var0, var11, var7, var5, var8, var3);
    } else if (var14 >= var10) {
      a495lr(var6, var1, var5, var10, Drawing.screenBuffer, var13, var4, var8, var2, var7, var3, var0, var9, var12, var14, var11);
    } else {
      a495lr(var5, var7, var6, var14, Drawing.screenBuffer, var13, var2, var8, var4, var1, var11, var0, var9, var12, var10, var3);
    }
  }

  private static void a181eb(final byte[] var2) {
    for (int var4 = 0; var4 < _gdb.length; ++var4) {
      int var1 = _gdb[var4];
      int var10004;
      int var0;
      for (int var5 = var4 << 4; var1-- != 0; _jlM[var10004] = var0) {
        var0 = _jlM[var5++];
        final byte var10002 = var2[var0];
        var10004 = _uoh[var2[var0]];
        _uoh[var10002] = _uoh[var2[var0]] + 1;
      }
    }
  }

  private static void a423kc() {
    int var1 = _gdb[0];
    for (int var2 = 1; var2 < _gdb.length; ++var2) {
      final int var3 = _gdb[var2];
      System.arraycopy(_jlM, var2 << 4, _jlM, var1, var3);
      var1 += var3;
    }
  }

  private static void a676oo(final int var0, final int var1, final int var2, final int var3, final int var4, final int var5, final int var7) {
    if (var0 < var3) {
      if (var3 < var2) {
        a451oo(var1, var5, var7, var4, var3, Drawing.screenBuffer, var0, var2);
      } else if (var0 < var2) {
        a451oo(var5, var1, var7, var4, var2, Drawing.screenBuffer, var0, var3);
      } else {
        a451oo(var5, var7, var1, var4, var0, Drawing.screenBuffer, var2, var3);
      }
    } else if (var0 < var2) {
      a451oo(var1, var7, var5, var4, var0, Drawing.screenBuffer, var3, var2);
    } else if (var3 >= var2) {
      a451oo(var7, var5, var1, var4, var3, Drawing.screenBuffer, var2, var0);
    } else {
      a451oo(var7, var1, var5, var4, var2, Drawing.screenBuffer, var3, var0);
    }

  }

  private static void e150gq() {
    final int var1 = _vcd.length;
    final int[] var2 = new int[var1];

    for (int var3 = 0; var3 < var1; ++var3) {
      final sr_ var4 = _vcd[var3];
      var4.a487();
      a093ec(var3);
      final int var5 = var4._O + var4._k >> 1;
      final int var6 = var4._j + var4._t >> 1;
      final int var7 = var4._r + var4._E >> 1;
      final int var11 = (_mwsa[5] << 14) >> 14;
      final int var12 = (_mwsa[8] << 14) >> 14;
      final int var13 = (_mwsa[11] << 14) >> 14;
      var2[var3] = var13 * var7 + var11 * var5 + var6 * var12 >> 16;
    }

    final int var19 = 0;
    final int var6 = jagexIntroAnimationFrame << 4;
    int var7 = 0;
    int var8 = mq_.a353je(var6) >> 8;
    int var9 = mq_.b080mq(var6) >> 8;
    if (JagexApplet.mouseX != -1 && JagexApplet.mouseY != -1) {
      var7 = JagexApplet.mouseX - 320;
      var9 = -128;
      var8 = 240 - JagexApplet.mouseY;
    }

    final double var20 = 256.0D / Math.sqrt(var9 * var9 + var7 * var7 + var8 * var8);
    var7 = (int) ((double) var7 * var20);
    var8 = (int) ((double) var8 * var20);
    var9 = (int) ((double) var9 * var20);
    int var12 = var7;
    int var13 = var8 - var19;
    int var14 = var9 - 0x100;
    final double v = 256.0D / Math.sqrt(var14 * var14 + var13 * var13 + var12 * var12);
    var12 = (int) ((double) var12 * v);
    var14 = (int) ((double) var14 * v);
    var13 = (int) ((double) var13 * v);

    int var16;
    for (int var15 = 0; _vcd.length > var15; ++var15) {
      var16 = 0;

      for (int var17 = 1; var17 < _vcd.length; ++var17) {
        if (var2[var17] > var2[var16]) {
          var16 = var17;
        }
      }

      var2[var16] = Integer.MIN_VALUE;
      final sr_ var21 = _vcd[var16];
      a093ec(var16);

      for (int var18 = 0; var18 < 3; ++var18) {
        final int[] var10000 = _mwsa;
        var10000[var18] += _faZ[var15][var18];
      }

      a806pe(_mwsa, var21);
      a087er(var7, var13, var8, var14, var21, var12, var9);
    }
  }

  private static void a093ec(final int var0) {
    int var1 = 0;
    int var2 = jagexIntroAnimationFrame;
    if (var2 >= 5) {
      if (var2 < 105) {
        var1 = ((var2 << 14) - 0xa000) / 220;
      } else if (var2 < 120) {
        var2 = 120 - var2;
        var1 = 8192 - var2 * var2 * 8192 / 3300;
      }
    } else {
      var1 = 8192 * var2 * var2 / 1100;
    }

    byte var3 = 1;
    byte var4 = 0;
    if (var0 == 1) {
      var4 = 1;
    }

    if (var0 == 3) {
      var3 = -1;
    }

    if (var0 == 4) {
      var4 = 1;
    }

    if (var0 == 5) {
      var4 = 1;
      var3 = -1;
    }

    if (var0 == 6) {
      var4 = -1;
    }

    if (var0 == 7 || var0 == 8) {
      var4 = -1;
      var3 = -1;
    }

    if (var0 == 11) {
      var3 = -1;
    }

    if (var0 == 12) {
      var4 = -1;
      var3 = -1;
    }

    if (var0 == 13) {
      var4 = -1;
    }

    if (var0 == 14) {
      var3 = -1;
      var4 = 1;
    }

    if (var0 == 15) {
      var4 = 1;
    }

    _mwsa = mq_.a977mq(var3 * var1, var4 * var1);
  }

  private static hc_[] a477ip(final CipheredBuffer var0) {
    final int var1 = var0.b543(8);
    if (var1 <= 0) {
      final int var2 = var0.b543(12);
      final hc_[] var3 = new hc_[var2];

      for (int var4 = 0; var2 > var4; ++var4) {
        if (a523ng(var0)) {
          final hc_ var5 = new hc_();
          var0.b543(24);
          var0.b543(24);
          var5._a = var0.b543(24);
          var0.b543(9);
          var0.b543(12);
          var0.b543(12);
          var0.b543(12);
          var3[var4] = var5;
        } else {
          final int var6 = var0.b543(a080a(var4 - 1));
          var3[var4] = var3[var6];
        }
      }

      return var3;
    } else {
      return null;
    }
  }

  private static byte[] a382ec(final CipheredBuffer var1, byte[] var2) {
    final int var3 = var1.b543(16);
    if (var3 == 0) {
      return null;
    } else {
      if (var2 == null || var2.length != var3) {
        var2 = new byte[var3];
      }

      final int var4 = var1.b543(3);
      final byte var5 = (byte) var1.b543(8);
      int var6;
      if (var4 <= 0) {
        for (var6 = 0; var6 < var3; ++var6) {
          var2[var6] = var5;
        }
      } else {
        for (var6 = 0; var6 < var3; ++var6) {
          var2[var6] = (byte) (var5 + var1.b543(var4));
        }
      }

      return var2;
    }
  }

  private static sr_ a523ec(final CipheredBuffer var0) {
    final int var1 = var0.b543(8);
    if (var1 <= 0) {
      final boolean var2 = a523ng(var0);
      final boolean var3 = a523ng(var0);
      final sr_ var4 = new sr_();
      var4._x = (short) var0.b543(16);
      var4._w = a704tm(var0, var4._w);
      var4._z = a704tm(var0, var4._z);
      var4._f = a704tm(var0, var4._f);
      var4._u = (short) var0.b543(16);
      var4._s = a704tm(var0, var4._s);
      var4._i = a704tm(var0, var4._i);
      var4._B = a704tm(var0, var4._B);
      if (var2) {
        var4._e = (short) var0.b543(16);
        var4._I = a704tm(var0, var4._I);
        var4._y = a704tm(var0, var4._y);
        var4._v = a704tm(var0, var4._v);
        var4._M = a704tm(var0, var4._M);
        var4._P = a704tm(var0, var4._P);
        var4._n = a704tm(var0, var4._n);
      }

      if (var3) {
        var0.b543(16);
        var4._C = a704tm(var0, var4._C);
        var4._g = a704tm(var0, var4._g);
        var4._L = a704tm(var0, var4._L);
        var4._J = a704tm(var0, var4._J);
        var4._G = a704tm(var0, var4._G);
      }

      if (a523ng(var0)) {
        var4._b = a704tm(var0, var4._b);
      }

      if (a523ng(var0)) {
        var4._p = a382ec(var0, var4._p);
        assert var4._p != null;
        final int var6 = IntStream.range(0, var4._p.length).map(var7 -> (var4._p[var7] & 255)).max().orElse(0);

        if (var6 == 0) {
          var4._p = null;
        } else {
          var4._o = (byte) (1 + var6);
        }
      }

      return var4;
    } else {
      throw new IllegalStateException("" + var1);
    }
  }

  private static boolean a523ng(final CipheredBuffer var0) {
    return var0.b543(1) == 1;
  }

  private static short[] a704tm(final CipheredBuffer var1, short[] var3) {
    final int var4 = var1.b543(16);
    if (var4 == 0) {
      return null;
    } else {
      if (var3 == null || var3.length != var4) {
        var3 = new short[var4];
      }

      final int var5 = var1.b543(4);
      final short var6 = (short) var1.b543(16);
      int var7;
      if (var5 <= 0) {
        for (var7 = 0; var7 < var4; ++var7) {
          var3[var7] = var6;
        }
      } else {
        for (var7 = 0; var7 < var4; ++var7) {
          var3[var7] = (short) (var1.b543(var5) + var6);
        }
      }

      return var3;
    }
  }

  private static int a080a(int var0) {
    if (var0 == 0) {
      return 0;
    } else {
      int var1;
      if (var0 <= 0) {
        var1 = 2;
        if (var0 < -65536) {
          var1 += 16;
          var0 >>= 16;
        }

        if (var0 < -256) {
          var1 += 8;
          var0 >>= 8;
        }

        if (var0 < -16) {
          var1 += 4;
          var0 >>= 4;
        }

        if (var0 < -4) {
          var1 += 2;
          var0 >>= 2;
        }

        if (var0 < -2) {
          ++var1;
        }

      } else {
        var1 = 1;
        if (var0 > 65535) {
          var1 += 16;
          var0 >>= 16;
        }

        if (var0 > 255) {
          var0 >>= 8;
          var1 += 8;
        }

        if (var0 > 15) {
          var0 >>= 4;
          var1 += 4;
        }

        if (var0 > 3) {
          var1 += 2;
          var0 >>= 2;
        }

        if (var0 > 1) {
          ++var1;
        }

      }
      return var1;
    }
  }
}
