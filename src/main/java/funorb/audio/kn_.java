package funorb.audio;

import funorb.util.BitMath;

public final class kn_ {
  private static final float[] _j = new float[]{1.0649863E-7F, 1.1341951E-7F, 1.2079015E-7F, 1.2863978E-7F, 1.369995E-7F, 1.459025E-7F, 1.5538409E-7F, 1.6548181E-7F, 1.7623574E-7F, 1.8768856E-7F, 1.998856E-7F, 2.128753E-7F, 2.2670913E-7F, 2.4144197E-7F, 2.5713223E-7F, 2.7384212E-7F, 2.9163792E-7F, 3.1059022E-7F, 3.307741E-7F, 3.5226967E-7F, 3.7516213E-7F, 3.995423E-7F, 4.255068E-7F, 4.5315863E-7F, 4.8260745E-7F, 5.1397E-7F, 5.4737063E-7F, 5.829419E-7F, 6.208247E-7F, 6.611694E-7F, 7.041359E-7F, 7.4989464E-7F, 7.98627E-7F, 8.505263E-7F, 9.057983E-7F, 9.646621E-7F, 1.0273513E-6F, 1.0941144E-6F, 1.1652161E-6F, 1.2409384E-6F, 1.3215816E-6F, 1.4074654E-6F, 1.4989305E-6F, 1.5963394E-6F, 1.7000785E-6F, 1.8105592E-6F, 1.9282195E-6F, 2.053526E-6F, 2.1869757E-6F, 2.3290977E-6F, 2.4804558E-6F, 2.6416496E-6F, 2.813319E-6F, 2.9961443E-6F, 3.1908505E-6F, 3.39821E-6F, 3.619045E-6F, 3.8542307E-6F, 4.1047006E-6F, 4.371447E-6F, 4.6555283E-6F, 4.958071E-6F, 5.280274E-6F, 5.623416E-6F, 5.988857E-6F, 6.3780467E-6F, 6.7925284E-6F, 7.2339453E-6F, 7.704048E-6F, 8.2047E-6F, 8.737888E-6F, 9.305725E-6F, 9.910464E-6F, 1.0554501E-5F, 1.1240392E-5F, 1.1970856E-5F, 1.2748789E-5F, 1.3577278E-5F, 1.4459606E-5F, 1.5399271E-5F, 1.6400005E-5F, 1.7465769E-5F, 1.8600793E-5F, 1.9809577E-5F, 2.1096914E-5F, 2.2467912E-5F, 2.3928002E-5F, 2.5482977E-5F, 2.7139005E-5F, 2.890265E-5F, 3.078091E-5F, 3.2781227E-5F, 3.4911533E-5F, 3.718028E-5F, 3.9596467E-5F, 4.2169668E-5F, 4.491009E-5F, 4.7828602E-5F, 5.0936775E-5F, 5.424693E-5F, 5.7772202E-5F, 6.152657E-5F, 6.552491E-5F, 6.9783084E-5F, 7.4317984E-5F, 7.914758E-5F, 8.429104E-5F, 8.976875E-5F, 9.560242E-5F, 1.0181521E-4F, 1.0843174E-4F, 1.1547824E-4F, 1.2298267E-4F, 1.3097477E-4F, 1.3948625E-4F, 1.4855085E-4F, 1.5820454E-4F, 1.6848555E-4F, 1.7943469E-4F, 1.9109536E-4F, 2.0351382E-4F, 2.167393E-4F, 2.3082423E-4F, 2.4582449E-4F, 2.6179955E-4F, 2.7881275E-4F, 2.9693157E-4F, 3.1622787E-4F, 3.3677815E-4F, 3.5866388E-4F, 3.8197188E-4F, 4.0679457E-4F, 4.3323037E-4F, 4.613841E-4F, 4.913675E-4F, 5.2329927E-4F, 5.573062E-4F, 5.935231E-4F, 6.320936E-4F, 6.731706E-4F, 7.16917E-4F, 7.635063E-4F, 8.1312325E-4F, 8.6596457E-4F, 9.2223985E-4F, 9.821722E-4F, 0.0010459992F, 0.0011139743F, 0.0011863665F, 0.0012634633F, 0.0013455702F, 0.0014330129F, 0.0015261382F, 0.0016253153F, 0.0017309374F, 0.0018434235F, 0.0019632196F, 0.0020908006F, 0.0022266726F, 0.0023713743F, 0.0025254795F, 0.0026895993F, 0.0028643848F, 0.0030505287F, 0.003248769F, 0.0034598925F, 0.0036847359F, 0.0039241905F, 0.0041792067F, 0.004450795F, 0.004740033F, 0.005048067F, 0.0053761187F, 0.005725489F, 0.0060975635F, 0.0064938175F, 0.0069158226F, 0.0073652514F, 0.007843887F, 0.008353627F, 0.008896492F, 0.009474637F, 0.010090352F, 0.01074608F, 0.011444421F, 0.012188144F, 0.012980198F, 0.013823725F, 0.014722068F, 0.015678791F, 0.016697686F, 0.017782796F, 0.018938422F, 0.020169148F, 0.021479854F, 0.022875736F, 0.02436233F, 0.025945531F, 0.027631618F, 0.029427277F, 0.031339627F, 0.03337625F, 0.035545226F, 0.037855156F, 0.0403152F, 0.042935107F, 0.045725275F, 0.048696756F, 0.05186135F, 0.05523159F, 0.05882085F, 0.062643364F, 0.06671428F, 0.07104975F, 0.075666964F, 0.08058423F, 0.08582105F, 0.09139818F, 0.097337745F, 0.1036633F, 0.11039993F, 0.11757434F, 0.12521498F, 0.13335215F, 0.14201812F, 0.15124726F, 0.16107617F, 0.1715438F, 0.18269168F, 0.19456401F, 0.20720787F, 0.22067343F, 0.23501402F, 0.25028655F, 0.26655158F, 0.28387362F, 0.3023213F, 0.32196787F, 0.34289113F, 0.36517414F, 0.3889052F, 0.41417846F, 0.44109413F, 0.4697589F, 0.50028646F, 0.53279793F, 0.5674221F, 0.6042964F, 0.64356697F, 0.6853896F, 0.72993004F, 0.777365F, 0.8278826F, 0.88168305F, 0.9389798F, 1.0F};
  private static final int[] _a = new int[]{256, 128, 86, 64};
  private static boolean[] _g;
  private static int[] _i;
  private static int[] _d;
  private final int[] _f;
  private final int[] _c;
  private final int[][] _k;
  private final int[] _l;
  private final int[] _h;
  private final int _b;
  private final int[] _e;

  public kn_() {
    final int var1 = fd_.a137(16);
    if (var1 == 1) {
      final int var2 = fd_.a137(5);
      int var3 = 0;
      this._l = new int[var2];

      int var4;
      int var5;
      for (var4 = 0; var4 < var2; ++var4) {
        var5 = fd_.a137(4);
        this._l[var4] = var5;
        if (var5 >= var3) {
          var3 = var5 + 1;
        }
      }

      this._e = new int[var3];
      this._c = new int[var3];
      this._h = new int[var3];
      this._k = new int[var3][];

      int var7;
      for (var4 = 0; var4 < var3; ++var4) {
        this._e[var4] = fd_.a137(3) + 1;
        var5 = this._c[var4] = fd_.a137(2);
        if (var5 != 0) {
          this._h[var4] = fd_.a137(8);
        }

        var5 = 1 << var5;
        final int[] var6 = new int[var5];
        this._k[var4] = var6;

        for (var7 = 0; var7 < var5; ++var7) {
          var6[var7] = fd_.a137(8) - 1;
        }
      }

      this._b = fd_.a137(2) + 1;
      var4 = fd_.a137(4);
      var5 = 2;

      int var9;
      for (var9 = 0; var9 < var2; ++var9) {
        var5 += this._e[this._l[var9]];
      }

      this._f = new int[var5];
      this._f[0] = 0;
      this._f[1] = 1 << var4;
      var5 = 2;

      for (var9 = 0; var9 < var2; ++var9) {
        var7 = this._l[var9];

        for (int var8 = 0; var8 < this._e[var7]; ++var8) {
          this._f[var5++] = fd_.a137(var4);
        }
      }

      if (_d == null || _d.length < var5) {
        _d = new int[var5];
        _i = new int[var5];
        _g = new boolean[var5];
      }

    } else {
      throw new RuntimeException();
    }
  }

  private static int b691(final int[] var0, final int var1) {
    final int var2 = var0[var1];
    int var3 = -1;
    int var4 = Integer.MAX_VALUE;

    for (int var5 = 0; var5 < var1; ++var5) {
      final int var6 = var0[var5];
      if (var6 > var2 && var6 < var4) {
        var3 = var5;
        var4 = var6;
      }
    }

    return var3;
  }

  private static int a691(final int[] var0, final int var1) {
    final int var2 = var0[var1];
    int var3 = -1;
    int var4 = Integer.MIN_VALUE;

    for (int var5 = 0; var5 < var1; ++var5) {
      final int var6 = var0[var5];
      if (var6 < var2 && var6 > var4) {
        var3 = var5;
        var4 = var6;
      }
    }

    return var3;
  }

  public boolean b801() {
    final boolean var1 = fd_.c784() != 0;
    if (var1) {
      final int var2 = this._f.length;

      int var3;
      for (var3 = 0; var3 < var2; ++var3) {
        _d[var3] = this._f[var3];
      }

      var3 = _a[this._b - 1];
      final int var4 = BitMath.lastSet(var3 - 1);
      _i[0] = fd_.a137(var4);
      _i[1] = fd_.a137(var4);
      int var5 = 2;

      for (final int var7 : this._l) {
        final int var8 = this._e[var7];
        final int var9 = this._c[var7];
        final int var10 = (1 << var9) - 1;
        int var11 = 0;
        if (var9 > 0) {
          var11 = fd_._L[this._h[var7]].a784();
        }

        for (int var12 = 0; var12 < var8; ++var12) {
          final int var13 = this._k[var7][var11 & var10];
          var11 >>>= var9;
          _i[var5++] = var13 >= 0 ? fd_._L[var13].a784() : 0;
        }
      }

      return true;
    } else {
      return false;
    }
  }

  private void a093(final int var1, final int var2) {
    if (var1 < var2) {
      int var3 = var1;
      final int var4 = _d[var1];
      final int var5 = _i[var1];
      final boolean var6 = _g[var1];

      for (int var7 = var1 + 1; var7 <= var2; ++var7) {
        final int var8 = _d[var7];
        if (var8 < var4) {
          _d[var3] = var8;
          _i[var3] = _i[var7];
          _g[var3] = _g[var7];
          ++var3;
          _d[var7] = _d[var3];
          _i[var7] = _i[var3];
          _g[var7] = _g[var3];
        }
      }

      _d[var3] = var4;
      _i[var3] = var5;
      _g[var3] = var6;
      this.a093(var1, var3 - 1);
      this.a093(var3 + 1, var2);
    }
  }

  private int a063(final int var1, final int var2, final int var3, final int var4, final int var5) {
    final int var6 = var4 - var2;
    final int var7 = var3 - var1;
    final int var8 = var6 < 0 ? -var6 : var6;
    final int var9 = var8 * (var5 - var1);
    final int var10 = var9 / var7;
    return var6 < 0 ? var2 - var10 : var2 + var10;
  }

  private void a365(final int var1, final int var2, int var3, final int var4, final float[] var5, final int var6) {
    final int var7 = var4 - var2;
    final int var8 = var3 - var1;
    int var9 = var7 < 0 ? -var7 : var7;
    final int var10 = var7 / var8;
    int var11 = var2;
    int var12 = 0;
    final int var13 = var7 < 0 ? var10 - 1 : var10 + 1;
    var9 -= (var10 < 0 ? -var10 : var10) * var8;
    var5[var1] *= _j[var2];
    if (var3 > var6) {
      var3 = var6;
    }

    for (int var14 = var1 + 1; var14 < var3; ++var14) {
      var12 += var9;
      if (var12 >= var8) {
        var12 -= var8;
        var11 += var13;
      } else {
        var11 += var10;
      }

      var5[var14] *= _j[var11];
    }

  }

  public void a331(final float[] var1, final int var2) {
    final int var3 = this._f.length;
    final int var4 = _a[this._b - 1];
    final boolean[] var5 = _g;
    _g[1] = true;
    var5[0] = true;

    int var6;
    int var7;
    int var8;
    int var9;
    int var10;
    for (var6 = 2; var6 < var3; ++var6) {
      var7 = a691(_d, var6);
      var8 = b691(_d, var6);
      var9 = this.a063(_d[var7], _i[var7], _d[var8], _i[var8], _d[var6]);
      var10 = _i[var6];
      final int var11 = var4 - var9;
      final int var13 = (Math.min(var11, var9)) << 1;
      if (var10 == 0) {
        _g[var6] = false;
        _i[var6] = var9;
      } else {
        final boolean[] var14 = _g;
        _g[var8] = true;
        var14[var7] = true;
        _g[var6] = true;
        if (var10 >= var13) {
          _i[var6] = var11 > var9 ? var10 - var9 + var9 : var9 - var10 + var11 - 1;
        } else {
          _i[var6] = (var10 & 1) != 0 ? var9 - (var10 + 1) / 2 : var9 + var10 / 2;
        }
      }
    }

    this.a093(0, var3 - 1);
    var6 = 0;
    var7 = _i[0] * this._b;

    for (var8 = 1; var8 < var3; ++var8) {
      if (_g[var8]) {
        var9 = _d[var8];
        var10 = _i[var8] * this._b;
        this.a365(var6, var7, var9, var10, var1, var2);
        if (var9 >= var2) {
          return;
        }

        var6 = var9;
        var7 = var10;
      }
    }

    final float var16 = _j[var7];

    for (var9 = var6; var9 < var2; ++var9) {
      var1[var9] *= var16;
    }

  }
}
