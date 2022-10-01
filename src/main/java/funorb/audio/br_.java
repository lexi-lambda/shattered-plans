package funorb.audio;

import funorb.io.Buffer;

public final class br_ {
  public final kk_[] _h = new kk_[128];
  public final kc_[] _j = new kc_[128];
  public final byte[] _t = new byte[128];
  public final int _q;
  public final short[] _k = new short[128];
  public final byte[] _r = new byte[128];
  public final byte[] _s = new byte[128];
  private int[] _n = new int[128];

  public br_(final byte[] var1) {
    final Buffer var2 = new Buffer(var1);

    int var3 = 0;
    while (var2.data[var3 + var2.pos] != 0) {
      ++var3;
    }

    final byte[] var4 = new byte[var3];

    int var5;
    for (var5 = 0; var5 < var3; ++var5) {
      var4[var5] = var2.readByte();
    }

    ++var3;
    ++var2.pos;
    var5 = var2.pos;
    var2.pos += var3;

    int var6 = 0;
    while (var2.data[var6 + var2.pos] != 0) {
      ++var6;
    }

    final byte[] var7 = new byte[var6];

    int var8;
    for (var8 = 0; var8 < var6; ++var8) {
      var7[var8] = var2.readByte();
    }

    ++var2.pos;
    ++var6;
    var8 = var2.pos;
    var2.pos += var6;

    int var9 = 0;
    while (var2.data[var9 + var2.pos] != 0) {
      ++var9;
    }

    final byte[] var10 = new byte[var9];

    for (int var11 = 0; var11 < var9; ++var11) {
      var10[var11] = var2.readByte();
    }

    ++var9;
    ++var2.pos;
    final byte[] var36 = new byte[var9];
    int var12;
    int var14;
    if (var9 <= 1) {
      var12 = var9;
    } else {
      var36[1] = 1;
      var12 = 2;
      int var13 = 1;

      for (var14 = 2; var9 > var14; ++var14) {
        int var15 = var2.readUByte();
        if (var15 == 0) {
          var13 = var12++;
        } else {
          if (var13 >= var15) {
            --var15;
          }

          var13 = var15;
        }

        var36[var14] = (byte) var13;
      }
    }

    final kc_[] var37 = new kc_[var12];

    kc_ var38;
    for (var14 = 0; var14 < var37.length; ++var14) {
      var38 = var37[var14] = new kc_();
      final int var16 = var2.readUByte();
      if (var16 > 0) {
        var38._n = new byte[var16 * 2];
      }

      final int j137 = var2.readUByte();
      if (j137 > 0) {
        var38._e = new byte[2 * j137 + 2];
        var38._e[1] = 64;
      }
    }

    var14 = var2.readUByte();
    final byte[] var39 = var14 > 0 ? new byte[2 * var14] : null;
    var14 = var2.readUByte();
    final byte[] var40 = var14 <= 0 ? null : new byte[var14 * 2];

    int var17 = 0;
    while (var2.data[var2.pos + var17] != 0) {
      ++var17;
    }

    final byte[] var18 = new byte[var17];

    int var19;
    for (var19 = 0; var19 < var17; ++var19) {
      var18[var19] = var2.readByte();
    }

    ++var17;
    ++var2.pos;
    var19 = 0;

    int var20;
    for (var20 = 0; var20 < 128; ++var20) {
      var19 += var2.readUByte();
      this._k[var20] = (short) var19;
    }

    var19 = 0;

    for (var20 = 0; var20 < 128; ++var20) {
      var19 += var2.readUByte();
      this._k[var20] = (short) (this._k[var20] + (var19 << 8));
    }

    var20 = 0;
    int var21 = 0;
    int var22 = 0;

    int var23;
    for (var23 = 0; var23 < 128; ++var23) {
      if (var20 == 0) {
        if (var18.length <= var21) {
          var20 = -1;
        } else {
          var20 = var18[var21++];
        }

        var22 = var2.readVariableInt();
      }

      this._k[var23] = (short) (this._k[var23] + ((var22 - 1 & 2) << 14));
      --var20;
      this._n[var23] = var22;
    }

    int i = 0;
    var20 = 0;
    var23 = 0;

    int var24;
    for (var24 = 0; var24 < 128; ++var24) {
      if (this._n[var24] != 0) {
        if (var20 == 0) {
          if (i < var4.length) {
            var20 = var4[i++];
          } else {
            var20 = -1;
          }

          var23 = var2.data[var5++] - 1;
        }

        --var20;
        this._r[var24] = (byte) var23;
      }
    }

    int i1 = 0;
    var20 = 0;
    var24 = 0;

    for (int var25 = 0; var25 < 128; ++var25) {
      if (this._n[var25] != 0) {
        if (var20 == 0) {
          if (i1 >= var7.length) {
            var20 = -1;
          } else {
            var20 = var7[i1++];
          }

          var24 = 16 + var2.data[var8++] << 2;
        }

        --var20;
        this._t[var25] = (byte) var24;
      }
    }

    int i2 = 0;
    var20 = 0;
    kc_ var42 = null;

    int var26;
    for (var26 = 0; var26 < 128; ++var26) {
      if (this._n[var26] != 0) {
        if (var20 == 0) {
          var42 = var37[var36[i2]];
          if (var10.length <= i2) {
            var20 = -1;
          } else {
            var20 = var10[i2++];
          }
        }

        --var20;
        this._j[var26] = var42;
      }
    }

    int i3 = 0;
    var20 = 0;
    var26 = 0;

    int var27;
    for (var27 = 0; var27 < 128; ++var27) {
      if (var20 == 0) {
        if (i3 < var18.length) {
          var20 = var18[i3++];
        } else {
          var20 = -1;
        }

        if (this._n[var27] > 0) {
          var26 = var2.readUByte() + 1;
        }
      }

      --var20;
      this._s[var27] = (byte) var26;
    }

    this._q = var2.readUByte() + 1;

    kc_ var28;
    int var29;
    for (var27 = 0; var27 < var12; ++var27) {
      var28 = var37[var27];
      if (var28._n != null) {
        for (var29 = 1; var29 < var28._n.length; var29 += 2) {
          var28._n[var29] = var2.readByte();
        }
      }

      if (var28._e != null) {
        for (var29 = 3; var28._e.length - 2 > var29; var29 += 2) {
          var28._e[var29] = var2.readByte();
        }
      }
    }

    if (var39 != null) {
      for (var27 = 1; var39.length > var27; var27 += 2) {
        var39[var27] = var2.readByte();
      }
    }

    if (var40 != null) {
      for (var27 = 1; var27 < var40.length; var27 += 2) {
        var40[var27] = var2.readByte();
      }
    }

    for (var27 = 0; var27 < var12; ++var27) {
      var28 = var37[var27];
      if (var28._e != null) {
        var19 = 0;

        for (var29 = 2; var29 < var28._e.length; var29 += 2) {
          var19 = var2.readUByte() + var19 + 1;
          var28._e[var29] = (byte) var19;
        }
      }
    }

    for (var27 = 0; var12 > var27; ++var27) {
      var28 = var37[var27];
      if (var28._n != null) {
        var19 = 0;

        for (var29 = 2; var29 < var28._n.length; var29 += 2) {
          var19 = var2.readUByte() + var19 + 1;
          var28._n[var29] = (byte) var19;
        }
      }
    }

    byte var30;
    int var32;
    int var33;
    int var34;
    int var45;
    byte var47;
    if (var39 != null) {
      var19 = var2.readUByte();
      var39[0] = (byte) var19;

      for (var27 = 2; var27 < var39.length; var27 += 2) {
        var19 = var2.readUByte() + var19 + 1;
        var39[var27] = (byte) var19;
      }

      var47 = var39[0];
      byte var43 = var39[1];

      for (var29 = 0; var29 < var47; ++var29) {
        this._s[var29] = (byte) (32 + this._s[var29] * var43 >> 6);
      }

      for (var29 = 2; var29 < var39.length; var47 = var30) {
        var30 = var39[var29];
        final byte var31 = var39[var29 + 1];
        var32 = (-var47 + var30) / 2 + var43 * (var30 - var47);

        for (var33 = var47; var30 > var33; ++var33) {
          var34 = a666ql(var32, var30 - var47);
          this._s[var33] = (byte) (32 + this._s[var33] * var34 >> 6);
          var32 += -var43 + var31;
        }

        var43 = var31;
        var29 += 2;
      }

      for (var45 = var47; var45 < 128; ++var45) {
        this._s[var45] = (byte) (32 + this._s[var45] * var43 >> 6);
      }
    }

    if (var40 != null) {
      var19 = var2.readUByte();
      var40[0] = (byte) var19;

      for (var27 = 2; var40.length > var27; var27 += 2) {
        var19 = 1 + var19 + var2.readUByte();
        var40[var27] = (byte) var19;
      }

      var47 = var40[0];
      int var44 = var40[1] << 1;

      for (var29 = 0; var29 < var47; ++var29) {
        var45 = (this._t[var29] & 255) + var44;
        if (var45 < 0) {
          var45 = 0;
        }

        if (var45 > 128) {
          var45 = 128;
        }

        this._t[var29] = (byte) var45;
      }

      int var46;
      for (var29 = 2; var40.length > var29; var44 = var46) {
        var30 = var40[var29];
        var46 = var40[1 + var29] << 1;
        var32 = (-var47 + var30) / 2 + (var30 - var47) * var44;

        for (var33 = var47; var33 < var30; ++var33) {
          var34 = a666ql(var32, -var47 + var30);
          int var35 = var34 + (255 & this._t[var33]);
          if (var35 < 0) {
            var35 = 0;
          }

          if (var35 > 128) {
            var35 = 128;
          }

          this._t[var33] = (byte) var35;
          var32 += -var44 + var46;
        }

        var47 = var30;
        var29 += 2;
      }

      for (var45 = var47; var45 < 128; ++var45) {
        var46 = var44 + (this._t[var45] & 255);
        if (var46 < 0) {
          var46 = 0;
        }

        if (var46 > 128) {
          var46 = 128;
        }

        this._t[var45] = (byte) var46;
      }
    }

    for (var27 = 0; var12 > var27; ++var27) {
      var37[var27]._h = var2.readUByte();
    }

    for (var27 = 0; var12 > var27; ++var27) {
      var28 = var37[var27];
      if (var28._n != null) {
        var28._k = var2.readUByte();
      }

      if (var28._e != null) {
        var28._c = var2.readUByte();
      }

      if (var28._h > 0) {
        var28._a = var2.readUByte();
      }
    }

    for (var27 = 0; var27 < var12; ++var27) {
      var37[var27]._o = var2.readUByte();
    }

    for (var27 = 0; var12 > var27; ++var27) {
      var28 = var37[var27];
      if (var28._o > 0) {
        var28._f = var2.readUByte();
      }
    }

    for (var27 = 0; var12 > var27; ++var27) {
      var28 = var37[var27];
      if (var28._f > 0) {
        var28._j = var2.readUByte();
      }
    }

  }

  private static int a666ql(final int var0, final int var1) {
    final int var2 = var0 >>> 31;
    return -var2 + (var0 + var2) / var1;
  }

  public boolean a972(final SoundLoader var1, final byte[] var2) {
    boolean var5 = true;
    int var6 = 0;
    kk_ var7 = null;

    for (int var8 = 0; var8 < 128; ++var8) {
      if (var2 == null || var2[var8] != 0) {
        int var9 = this._n[var8];
        if (var9 != 0) {
          if (var9 != var6) {
            var6 = var9--;
            if ((1 & var9) == 0) {
              var7 = var1.loadSingleton1(var9 >> 2);
            } else {
              var7 = var1.loadSingleton2(var9 >> 2);
            }

            if (var7 == null) {
              var5 = false;
            }
          }

          if (var7 != null) {
            this._h[var8] = var7;
            this._n[var8] = 0;
          }
        }
      }
    }

    return var5;
  }

  public void e150() {
    this._n = null;
  }
}
