package funorb.audio;

import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.Iterator;

public final class al_ extends tn_ {
  private final int _u;
  private final boolean _o;
  private final int _r;
  private int _y;
  private int _z;
  private int _s;
  private int _n;
  private int _x;
  private int _t;
  private int volume;
  private int _v;
  private int _q;
  private int _l;
  private int _p;
  private int _m;

  private al_(final kk_ var1, final int var2, final int volume, final int var4) {
    this._k = var1;
    this._u = var1._l;
    this._r = var1._k;
    this._o = var1._i;
    this._l = var2;
    this.volume = volume;
    this._z = var4;
    this._t = 0;
    this.k797();
  }

  private static int c984(final byte[] var2, final int[] var3, int var4, int var5, final int var6, final int var7, final int var9, final int var10, final al_ var11, final int var12, final int var13) {
    int var8;
    if (var12 == 0 || (var8 = var5 + (var10 + 256 - var4 + var12) / var12) > var9) {
      var8 = var9;
    }

    var5 <<= 1;

    int var10001;
    int var0;
    int var1;
    for (var8 <<= 1; var5 < var8; var4 += var12) {
      var1 = var4 >> 8;
      final byte var14 = var2[var1 - 1];
      var0 = (var14 << 8) + (var2[var1] - var14) * (var4 & 255);
      var10001 = var5++;
      var3[var10001] += var0 * var6 >> 6;
      var10001 = var5++;
      var3[var10001] += var0 * var7 >> 6;
    }

    if (var12 == 0 || (var8 = (var5 >> 1) + (var10 - var4 + var12) / var12) > var9) {
      var8 = var9;
    }

    var8 <<= 1;

    for (var1 = var13; var5 < var8; var4 += var12) {
      var0 = (var1 << 8) + (var2[var4 >> 8] - var1) * (var4 & 255);
      var10001 = var5++;
      var3[var10001] += var0 * var6 >> 6;
      var10001 = var5++;
      var3[var10001] += var0 * var7 >> 6;
    }

    var11._t = var4;
    return var5 >> 1;
  }

  public static al_ a771(final kk_ var0, final int var1, final int var2, final int var3) {
    return (var0.data == null || var0.data.length == 0) ? null : new al_(var0, var1, var2, var3);
  }

  private static int b775(final byte[] var1, final int[] var2, int var3, int var4, int var5, int var6, final int var8, int var9, final al_ var10) {
    var3 >>= 8;
    var9 >>= 8;
    var5 <<= 2;
    var6 <<= 2;
    int var7;
    if ((var7 = var4 + var9 - var3) > var8) {
      var7 = var8;
    }

    var4 <<= 1;
    var7 <<= 1;

    int var10001;
    byte var11;
    for (var7 -= 6; var4 < var7; var2[var10001] += var11 * var6) {
      var11 = var1[var3++];
      var10001 = var4++;
      var2[var10001] += var11 * var5;
      var10001 = var4++;
      var2[var10001] += var11 * var6;
      var11 = var1[var3++];
      var10001 = var4++;
      var2[var10001] += var11 * var5;
      var10001 = var4++;
      var2[var10001] += var11 * var6;
      var11 = var1[var3++];
      var10001 = var4++;
      var2[var10001] += var11 * var5;
      var10001 = var4++;
      var2[var10001] += var11 * var6;
      var11 = var1[var3++];
      var10001 = var4++;
      var2[var10001] += var11 * var5;
      var10001 = var4++;
    }

    for (var7 += 6; var4 < var7; var2[var10001] += var11 * var6) {
      var11 = var1[var3++];
      var10001 = var4++;
      var2[var10001] += var11 * var5;
      var10001 = var4++;
    }

    var10._t = var3 << 8;
    return var4 >> 1;
  }

  private static int b961(final byte[] var1, final int[] var2, int var3, int var4, int var5, int var6, int var7, int var8, final int var10, int var11, final al_ var12) {
    var3 >>= 8;
    var11 >>= 8;
    var5 <<= 2;
    var6 <<= 2;
    var7 <<= 2;
    var8 <<= 2;
    int var9;
    if ((var9 = var4 + var3 - (var11 - 1)) > var10) {
      var9 = var10;
    }

    var12._s += var12._p * (var9 - var4);
    var4 <<= 1;
    var9 <<= 1;

    byte var13;
    int var10001;
    for (var9 -= 6; var4 < var9; var6 += var8) {
      var13 = var1[var3--];
      var10001 = var4++;
      var2[var10001] += var13 * var5;
      var5 += var7;
      var10001 = var4++;
      var2[var10001] += var13 * var6;
      var6 += var8;
      var13 = var1[var3--];
      var10001 = var4++;
      var2[var10001] += var13 * var5;
      var5 += var7;
      var10001 = var4++;
      var2[var10001] += var13 * var6;
      var6 += var8;
      var13 = var1[var3--];
      var10001 = var4++;
      var2[var10001] += var13 * var5;
      var5 += var7;
      var10001 = var4++;
      var2[var10001] += var13 * var6;
      var6 += var8;
      var13 = var1[var3--];
      var10001 = var4++;
      var2[var10001] += var13 * var5;
      var5 += var7;
      var10001 = var4++;
      var2[var10001] += var13 * var6;
    }

    for (var9 += 6; var4 < var9; var6 += var8) {
      var13 = var1[var3--];
      var10001 = var4++;
      var2[var10001] += var13 * var5;
      var5 += var7;
      var10001 = var4++;
      var2[var10001] += var13 * var6;
    }

    var12._y = var5 >> 2;
    var12._m = var6 >> 2;
    var12._t = var3 << 8;
    return var4 >> 1;
  }

  private static int a961(final byte[] var1, final int[] var2, int var3, int var4, int var5, int var6, int var7, int var8, final int var10, int var11, final al_ var12) {
    var3 >>= 8;
    var11 >>= 8;
    var5 <<= 2;
    var6 <<= 2;
    var7 <<= 2;
    var8 <<= 2;
    int var9;
    if ((var9 = var4 + var11 - var3) > var10) {
      var9 = var10;
    }

    var12._s += var12._p * (var9 - var4);
    var4 <<= 1;
    var9 <<= 1;

    byte var13;
    int var10001;
    for (var9 -= 6; var4 < var9; var6 += var8) {
      var13 = var1[var3++];
      var10001 = var4++;
      var2[var10001] += var13 * var5;
      var5 += var7;
      var10001 = var4++;
      var2[var10001] += var13 * var6;
      var6 += var8;
      var13 = var1[var3++];
      var10001 = var4++;
      var2[var10001] += var13 * var5;
      var5 += var7;
      var10001 = var4++;
      var2[var10001] += var13 * var6;
      var6 += var8;
      var13 = var1[var3++];
      var10001 = var4++;
      var2[var10001] += var13 * var5;
      var5 += var7;
      var10001 = var4++;
      var2[var10001] += var13 * var6;
      var6 += var8;
      var13 = var1[var3++];
      var10001 = var4++;
      var2[var10001] += var13 * var5;
      var5 += var7;
      var10001 = var4++;
      var2[var10001] += var13 * var6;
    }

    for (var9 += 6; var4 < var9; var6 += var8) {
      var13 = var1[var3++];
      var10001 = var4++;
      var2[var10001] += var13 * var5;
      var5 += var7;
      var10001 = var4++;
      var2[var10001] += var13 * var6;
    }

    var12._y = var5 >> 2;
    var12._m = var6 >> 2;
    var12._t = var3 << 8;
    return var4 >> 1;
  }

  private static int d984(final byte[] var2, final int[] var3, int var4, int var5, final int var6, final int var7, final int var9, final int var10, final al_ var11, final int var12, final int var13) {
    int var8;
    if (var12 == 0 || (var8 = var5 + (var10 - var4 + var12 - 257) / var12) > var9) {
      var8 = var9;
    }

    var5 <<= 1;

    byte var14;
    int var10001;
    int var0;
    int var1;
    for (var8 <<= 1; var5 < var8; var4 += var12) {
      var1 = var4 >> 8;
      var14 = var2[var1];
      var0 = (var14 << 8) + (var2[var1 + 1] - var14) * (var4 & 255);
      var10001 = var5++;
      var3[var10001] += var0 * var6 >> 6;
      var10001 = var5++;
      var3[var10001] += var0 * var7 >> 6;
    }

    if (var12 == 0 || (var8 = (var5 >> 1) + (var10 - var4 + var12 - 1) / var12) > var9) {
      var8 = var9;
    }

    var8 <<= 1;

    for (var1 = var13; var5 < var8; var4 += var12) {
      var14 = var2[var4 >> 8];
      var0 = (var14 << 8) + (var1 - var14) * (var4 & 255);
      var10001 = var5++;
      var3[var10001] += var0 * var6 >> 6;
      var10001 = var5++;
      var3[var10001] += var0 * var7 >> 6;
    }

    var11._t = var4;
    return var5 >> 1;
  }

  public static al_ a638(final kk_ var0, final int volume) {
    if (var0.data == null || var0.data.length == 0) {
      return null;
    } else {
      return new al_(var0, (int) ((long) var0._j * 256L * (long) 100 / (100L * SampledAudioChannel.SAMPLES_PER_SECOND)), volume << 6, 0x2000);
    }
  }

  private static int a240(final byte[] var2, final int[] var3, int var4, int var5, int var6, int var7, final int var8, final int var9, final int var11, final int var12, final al_ var13, final int var14, final int var15) {
    var13._s -= var13._p * var5;
    int var10;
    if (var14 == 0 || (var10 = var5 + (var12 - var4 + var14 - 257) / var14) > var11) {
      var10 = var11;
    }

    var5 <<= 1;

    byte var16;
    int var10001;
    int var0;
    int var1;
    for (var10 <<= 1; var5 < var10; var4 += var14) {
      var1 = var4 >> 8;
      var16 = var2[var1];
      var0 = (var16 << 8) + (var2[var1 + 1] - var16) * (var4 & 255);
      var10001 = var5++;
      var3[var10001] += var0 * var6 >> 6;
      var6 += var8;
      var10001 = var5++;
      var3[var10001] += var0 * var7 >> 6;
      var7 += var9;
    }

    if (var14 == 0 || (var10 = (var5 >> 1) + (var12 - var4 + var14 - 1) / var14) > var11) {
      var10 = var11;
    }

    var10 <<= 1;

    for (var1 = var15; var5 < var10; var4 += var14) {
      var16 = var2[var4 >> 8];
      var0 = (var16 << 8) + (var1 - var16) * (var4 & 255);
      var10001 = var5++;
      var3[var10001] += var0 * var6 >> 6;
      var6 += var8;
      var10001 = var5++;
      var3[var10001] += var0 * var7 >> 6;
      var7 += var9;
    }

    var5 >>= 1;
    var13._s += var13._p * var5;
    var13._y = var6;
    var13._m = var7;
    var13._t = var4;
    return var5;
  }

  private static int a775(final byte[] var1, final int[] var2, int var3, int var4, int var5, int var6, final int var8, int var9, final al_ var10) {
    var3 >>= 8;
    var9 >>= 8;
    var5 <<= 2;
    var6 <<= 2;
    int var7;
    if ((var7 = var4 + var3 - (var9 - 1)) > var8) {
      var7 = var8;
    }

    var4 <<= 1;
    var7 <<= 1;

    int var10001;
    byte var11;
    for (var7 -= 6; var4 < var7; var2[var10001] += var11 * var6) {
      var11 = var1[var3--];
      var10001 = var4++;
      var2[var10001] += var11 * var5;
      var10001 = var4++;
      var2[var10001] += var11 * var6;
      var11 = var1[var3--];
      var10001 = var4++;
      var2[var10001] += var11 * var5;
      var10001 = var4++;
      var2[var10001] += var11 * var6;
      var11 = var1[var3--];
      var10001 = var4++;
      var2[var10001] += var11 * var5;
      var10001 = var4++;
      var2[var10001] += var11 * var6;
      var11 = var1[var3--];
      var10001 = var4++;
      var2[var10001] += var11 * var5;
      var10001 = var4++;
    }

    for (var7 += 6; var4 < var7; var2[var10001] += var11 * var6) {
      var11 = var1[var3--];
      var10001 = var4++;
      var2[var10001] += var11 * var5;
      var10001 = var4++;
    }

    var10._t = var3 << 8;
    return var4 >> 1;
  }

  private static int d080(final int var0, final int var1) {
    return var1 < 0 ? -var0 : (int) ((double) var0 * Math.sqrt((double) var1 * 1.220703125E-4D) + 0.5D);
  }

  private static int b240(final byte[] var2, final int[] var3, int var4, int var5, int var6, int var7, final int var8, final int var9, final int var11, final int var12, final al_ var13, final int var14, final int var15) {
    var13._s -= var13._p * var5;
    int var10;
    if (var14 == 0 || (var10 = var5 + (var12 + 256 - var4 + var14) / var14) > var11) {
      var10 = var11;
    }

    var5 <<= 1;

    int var10001;
    int var0;
    int var1;
    for (var10 <<= 1; var5 < var10; var4 += var14) {
      var1 = var4 >> 8;
      final byte var16 = var2[var1 - 1];
      var0 = (var16 << 8) + (var2[var1] - var16) * (var4 & 255);
      var10001 = var5++;
      var3[var10001] += var0 * var6 >> 6;
      var6 += var8;
      var10001 = var5++;
      var3[var10001] += var0 * var7 >> 6;
      var7 += var9;
    }

    if (var14 == 0 || (var10 = (var5 >> 1) + (var12 - var4 + var14) / var14) > var11) {
      var10 = var11;
    }

    var10 <<= 1;

    for (var1 = var15; var5 < var10; var4 += var14) {
      var0 = (var1 << 8) + (var2[var4 >> 8] - var1) * (var4 & 255);
      var10001 = var5++;
      var3[var10001] += var0 * var6 >> 6;
      var6 += var8;
      var10001 = var5++;
      var3[var10001] += var0 * var7 >> 6;
      var7 += var9;
    }

    var5 >>= 1;
    var13._s += var13._p * var5;
    var13._y = var6;
    var13._m = var7;
    var13._t = var4;
    return var5;
  }

  private static int e080(final int var0, final int var1) {
    return var1 < 0 ? var0 : (int) ((double) var0 * Math.sqrt((double) (16384 - var1) * 1.220703125E-4D) + 0.5D);
  }

  private synchronized void c093(final int volume, final int var2) {
    this.volume = volume;
    this._z = var2;
    this._v = 0;
    this.k797();
  }

  private int b682(final int[] var1, int var2, final int var3, final int var4, final int var5) {
    while (true) {
      if (this._v > 0) {
        int var6 = var2 + this._v;
        if (var6 > var4) {
          var6 = var4;
        }

        this._v += var2;
        if (this._l == 256 && (this._t & 255) == 0) {
          var2 = a961(this._k.data, var1, this._t, var2, this._y, this._m, this._x, this._n, var6, var3, this);
        } else {
          var2 = a240(this._k.data, var1, this._t, var2, this._y, this._m, this._x, this._n, var6, var3, this, this._l, var5);
        }

        this._v -= var2;
        if (this._v != 0) {
          return var2;
        }

        if (!this.h801()) {
          continue;
        }

        return var4;
      }

      if (this._l == 256 && (this._t & 255) == 0) {
        return b775(this._k.data, var1, this._t, var2, this._y, this._m, var4, var3, this);

      }

      return d984(this._k.data, var1, this._t, var2, this._y, this._m, var4, var3, this, this._l, var5);

    }
  }

  public synchronized int getVolume() {
    return this.volume == Integer.MIN_VALUE ? 0 : this.volume;
  }

  public synchronized void a093(final int var1, final int var2) {
    this.a326(var1, var2, this.l784());
  }

  public synchronized void h150(int var1) {
    final int var2 = this._k.data.length << 8;
    if (var1 < -1) {
      var1 = -1;
    }

    if (var1 > var2) {
      var1 = var2;
    }

    this._t = var1;
  }

  public synchronized void g150(int var1) {
    if (var1 == 0) {
      this.setVolume(0);
      this.unlink();
    } else if (this._y == 0 && this._m == 0) {
      this._v = 0;
      this.volume = 0;
      this._s = 0;
      this.unlink();
    } else {
      int var2 = -this._s;
      if (this._s > var2) {
        var2 = this._s;
      }

      if (-this._y > var2) {
        var2 = -this._y;
      }

      if (this._y > var2) {
        var2 = this._y;
      }

      if (-this._m > var2) {
        var2 = -this._m;
      }

      if (this._m > var2) {
        var2 = this._m;
      }

      if (var1 > var2) {
        var1 = var2;
      }

      this._v = var1;
      this.volume = Integer.MIN_VALUE;
      this._p = -this._s / var1;
      this._x = -this._y / var1;
      this._n = -this._m / var1;
    }
  }

  private void k797() {
    this._s = this.volume;
    this._y = e080(this.volume, this._z);
    this._m = d080(this.volume, this._z);
  }

  public synchronized boolean e801() {
    return this._v != 0;
  }

  public synchronized void c487() {
    this._l = (this._l ^ this._l >> 31) + (this._l >>> 31);
    this._l = -this._l;

  }

  @Override
  public synchronized void a150(int len) {
    if (this._v > 0) {
      if (len >= this._v) {
        if (this.volume == Integer.MIN_VALUE) {
          this.volume = 0;
          this._m = 0;
          this._y = 0;
          this._s = 0;
          this.unlink();
          len = this._v;
        }

        this._v = 0;
        this.k797();
      } else {
        this._s += this._p * len;
        this._y += this._x * len;
        this._m += this._n * len;
        this._v -= len;
      }
    }

    final kk_ var2 = this._k;
    final int var3 = this._u << 8;
    final int var4 = this._r << 8;
    final int var5 = var2.data.length << 8;
    final int var6 = var4 - var3;
    if (var6 <= 0) {
      this._q = 0;
    }

    if (this._t < 0) {
      if (this._l <= 0) {
        this.j797();
        this.unlink();
        return;
      }

      this._t = 0;
    }

    if (this._t >= var5) {
      if (this._l >= 0) {
        this.j797();
        this.unlink();
        return;
      }

      this._t = var5 - 1;
    }

    this._t += this._l * len;
    if (this._q < 0) {
      if (this._o) {
        if (this._l < 0) {
          if (this._t >= var3) {
            return;
          }

          this._t = var3 + var3 - 1 - this._t;
          this._l = -this._l;
        }

        while (this._t >= var4) {
          this._t = var4 + var4 - 1 - this._t;
          this._l = -this._l;
          if (this._t >= var3) {
            return;
          }

          this._t = var3 + var3 - 1 - this._t;
          this._l = -this._l;
        }

      } else if (this._l < 0) {
        if (this._t >= var3) {
          return;
        }

        this._t = var4 - 1 - (var4 - 1 - this._t) % var6;
      } else {
        if (this._t < var4) {
          return;
        }

        this._t = var3 + (this._t - var3) % var6;
      }
    } else {
      if (this._q > 0) {
        label121:
        {
          if (this._l < 0) {
            if (this._t >= var3) {
              return;
            }

            this._t = var3 + var3 - 1 - this._t;
            this._l = -this._l;
            if (--this._q == 0) {
              break label121;
            }
          }

          do {
            if (this._t < var4) {
              return;
            }

            this._t = var4 + var4 - 1 - this._t;
            this._l = -this._l;
            if (--this._q == 0) {
              break;
            }

            if (this._t >= var3) {
              return;
            }

            this._t = var3 + var3 - 1 - this._t;
            this._l = -this._l;
          } while (--this._q != 0);
        }
      }

      if (this._l < 0) {
        if (this._t < 0) {
          this._t = -1;
          this.j797();
          this.unlink();
        }
      } else if (this._t >= var5) {
        this._t = var5;
        this.j797();
        this.unlink();
      }
    }
  }

  public synchronized boolean g801() {
    return this._t < 0 || this._t >= this._k.data.length << 8;
  }

  public synchronized void a326(int var1, final int var2, final int var3) {
    if (var1 == 0) {
      this.c093(var2, var3);
    } else {
      final int var4 = e080(var2, var3);
      final int var5 = d080(var2, var3);
      if (this._y == var4 && this._m == var5) {
        this._v = 0;
      } else {
        int var6 = var2 - this._s;
        if (this._s - var2 > var6) {
          var6 = this._s - var2;
        }

        if (var4 - this._y > var6) {
          var6 = var4 - this._y;
        }

        if (this._y - var4 > var6) {
          var6 = this._y - var4;
        }

        if (var5 - this._m > var6) {
          var6 = var5 - this._m;
        }

        if (this._m - var5 > var6) {
          var6 = this._m - var5;
        }

        if (var1 > var6) {
          var1 = var6;
        }

        this._v = var1;
        this.volume = var2;
        this._z = var3;
        this._p = (var2 - this._s) / var1;
        this._x = (var4 - this._y) / var1;
        this._n = (var5 - this._m) / var1;
      }
    }
  }

  @Override
  public @NotNull Iterator<tn_> iterator() {
    return Collections.emptyIterator();
  }

  @Override
  public int a784() {
    return this.volume == 0 && this._v == 0 ? 0 : 1;
  }

  public synchronized void d150(final int var1) {
    if (this._l < 0) {
      this._l = -var1;
    } else {
      this._l = var1;
    }

  }

  @Override
  public synchronized void b397(final int[] dest, final int offset, final int len) {
    if (this.volume == 0 && this._v == 0) {
      this.a150(len);
      return;
    }

    final int var5 = this._u << 8;
    final int var6 = this._r << 8;
    final int var7 = this._k.data.length << 8;
    final int var8 = var6 - var5;
    if (var8 <= 0) {
      this._q = 0;
    }

    int pos = offset;
    final int end = len + offset;
    if (this._t < 0) {
      if (this._l <= 0) {
        this.j797();
        this.unlink();
        return;
      }

      this._t = 0;
    }

    if (this._t >= var7) {
      if (this._l >= 0) {
        this.j797();
        this.unlink();
        return;
      }

      this._t = var7 - 1;
    }

    if (this._q < 0) {
      if (this._o) {
        if (this._l < 0) {
          pos = this.a682(dest, offset, var5, end, this._k.data[this._u]);
          if (this._t >= var5) {
            return;
          }

          this._t = var5 + var5 - 1 - this._t;
          this._l = -this._l;
        }

        while (true) {
          pos = this.b682(dest, pos, var6, end, this._k.data[this._r - 1]);
          if (this._t < var6) {
            return;
          }

          this._t = var6 + var6 - 1 - this._t;
          this._l = -this._l;
          pos = this.a682(dest, pos, var5, end, this._k.data[this._u]);
          if (this._t >= var5) {
            return;
          }

          this._t = var5 + var5 - 1 - this._t;
          this._l = -this._l;
        }
      } else if (this._l < 0) {
        while (true) {
          pos = this.a682(dest, pos, var5, end, this._k.data[this._r - 1]);
          if (this._t >= var5) {
            return;
          }

          this._t = var6 - 1 - (var6 - 1 - this._t) % var8;
        }
      } else {
        while (true) {
          pos = this.b682(dest, pos, var6, end, this._k.data[this._u]);
          if (this._t < var6) {
            return;
          }

          this._t = var5 + (this._t - var5) % var8;
        }
      }
    } else {
      if (this._q > 0) {
        if (this._o) {
          label131:
          {
            if (this._l < 0) {
              pos = this.a682(dest, offset, var5, end, this._k.data[this._u]);
              if (this._t >= var5) {
                return;
              }

              this._t = var5 + var5 - 1 - this._t;
              this._l = -this._l;
              if (--this._q == 0) {
                break label131;
              }
            }

            do {
              pos = this.b682(dest, pos, var6, end, this._k.data[this._r - 1]);
              if (this._t < var6) {
                return;
              }

              this._t = var6 + var6 - 1 - this._t;
              this._l = -this._l;
              if (--this._q == 0) {
                break;
              }

              pos = this.a682(dest, pos, var5, end, this._k.data[this._u]);
              if (this._t >= var5) {
                return;
              }

              this._t = var5 + var5 - 1 - this._t;
              this._l = -this._l;
            } while (--this._q != 0);
          }
        } else {
          int var10;
          if (this._l < 0) {
            while (true) {
              pos = this.a682(dest, pos, var5, end, this._k.data[this._r - 1]);
              if (this._t >= var5) {
                return;
              }

              var10 = (var6 - 1 - this._t) / var8;
              if (var10 >= this._q) {
                this._t += var8 * this._q;
                this._q = 0;
                break;
              }

              this._t += var8 * var10;
              this._q -= var10;
            }
          } else {
            while (true) {
              pos = this.b682(dest, pos, var6, end, this._k.data[this._u]);
              if (this._t < var6) {
                return;
              }

              var10 = (this._t - var5) / var8;
              if (var10 >= this._q) {
                this._t -= var8 * this._q;
                this._q = 0;
                break;
              }

              this._t -= var8 * var10;
              this._q -= var10;
            }
          }
        }
      }

      if (this._l < 0) {
        this.a682(dest, pos, 0, end, 0);
        if (this._t < 0) {
          this._t = -1;
          this.j797();
          this.unlink();
        }
      } else {
        this.b682(dest, pos, var7, end, 0);
        if (this._t >= var7) {
          this._t = var7;
          this.j797();
          this.unlink();
        }
      }
    }
  }

  @Override
  public int c784() {
    final int var1 = (this._s * 3) >> 6;
    int var2 = (var1 ^ (var1 >> 31)) + (var1 >>> 31);
    if (this._q == 0) {
      var2 -= var2 * this._t / (this._k.data.length << 8);
    } else if (this._q >= 0) {
      var2 -= var2 * this._u / this._k.data.length;
    }
    return Math.min(var2, 255);
  }

  private void j797() {
    if (this._v != 0) {
      if (this.volume == Integer.MIN_VALUE) {
        this.volume = 0;
      }

      this._v = 0;
      this.k797();
    }

  }

  public synchronized int f784() {
    return this._l < 0 ? -this._l : this._l;
  }

  private int a682(final int[] dest, int var2, final int var3, final int var4, final int var5) {
    while (true) {
      if (this._v > 0) {
        int var6 = var2 + this._v;
        if (var6 > var4) {
          var6 = var4;
        }

        this._v += var2;
        if (this._l == -256 && (this._t & 255) == 0) {
          var2 = b961(this._k.data, dest, this._t, var2, this._y, this._m, this._x, this._n, var6, var3, this);
        } else {
          var2 = b240(this._k.data, dest, this._t, var2, this._y, this._m, this._x, this._n, var6, var3, this, this._l, var5);
        }

        this._v -= var2;
        if (this._v != 0) {
          return var2;
        }

        if (!this.h801()) {
          continue;
        }

        return var4;
      }

      if (this._l == -256 && (this._t & 255) == 0) {
        return a775(this._k.data, dest, this._t, var2, this._y, this._m, var4, var3, this);

      }

      return c984(this._k.data, dest, this._t, var2, this._y, this._m, var4, var3, this, this._l, var5);

    }
  }

  @SuppressWarnings("BooleanMethodIsAlwaysInverted")
  private boolean h801() {
    int var1 = this.volume;
    final int var2;
    final int var3;
    if (var1 == Integer.MIN_VALUE) {
      var3 = 0;
      var2 = 0;
      var1 = 0;
    } else {
      var2 = e080(var1, this._z);
      var3 = d080(var1, this._z);
    }

    if (this._s == var1 && this._y == var2 && this._m == var3) {
      if (this.volume == Integer.MIN_VALUE) {
        this.volume = 0;
        this.unlink();
        return true;
      } else {
        this.k797();
        return false;
      }
    } else {
      if (this._s < var1) {
        this._p = 1;
        this._v = var1 - this._s;
      } else if (this._s > var1) {
        this._p = -1;
        this._v = this._s - var1;
      } else {
        this._p = 0;
      }

      if (this._y < var2) {
        this._x = 1;
        if (this._v == 0 || this._v > var2 - this._y) {
          this._v = var2 - this._y;
        }
      } else if (this._y > var2) {
        this._x = -1;
        if (this._v == 0 || this._v > this._y - var2) {
          this._v = this._y - var2;
        }
      } else {
        this._x = 0;
      }

      if (this._m < var3) {
        this._n = 1;
        if (this._v == 0 || this._v > var3 - this._m) {
          this._v = var3 - this._m;
        }
      } else if (this._m > var3) {
        this._n = -1;
        if (this._v == 0 || this._v > this._m - var3) {
          this._v = this._m - var3;
        }
      } else {
        this._n = 0;
      }

      return false;
    }
  }

  public synchronized int l784() {
    return this._z < 0 ? -1 : this._z;
  }

  public synchronized void setVolume(final int volume) {
    this.c093(volume, this.l784());
  }

  public synchronized void f150() {
    this._q = -1;
  }
}
