package funorb.audio;

import funorb.io.Buffer;

import java.util.Arrays;

public final class pi_ {
  private static final byte[] _g = new byte[]{2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 0, 1, 2, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
  private final Buffer _i = new Buffer(null);
  public int _e;
  public int[] _b;
  private int[] _h;
  private int[] _c;
  private int[] _d;
  private long _f;
  private int _a;

  public pi_() {
  }

  public pi_(final byte[] var1) {
    this.a604(var1);
  }

  public boolean a801() {
    return Arrays.stream(this._h).noneMatch(i -> i >= 0);
  }

  public int a137(final int var1) {
    return this.f137(var1);
  }

  public void a111(final long var1) {
    this._f = var1;
    final int var3 = this._h.length;

    for (int var4 = 0; var4 < var3; ++var4) {
      this._b[var4] = 0;
      this._d[var4] = 0;
      this._i.pos = this._c[var4];
      this.d150(var4);
      this._h[var4] = this._i.pos;
    }

  }

  public long c138(final int var1) {
    return this._f + (long) var1 * (long) this._a;
  }

  public void b150(final int var1) {
    this._i.pos = this._h[var1];
  }

  private int a080(final int var1, final int var2) {
    int var4;
    if (var2 == 255) {
      final int var7 = this._i.readUByte();
      var4 = this._i.readVariableInt();
      final Buffer var10000;
      if (var7 == 47) {
        var10000 = this._i;
        var10000.pos += var4;
        return 1;
      } else if (var7 == 81) {
        final int var5 = this._i.readU24();
        var4 -= 3;
        final int var6 = this._b[var1];
        this._f += (long) var6 * (long) (this._a - var5);
        this._a = var5;
        var10000 = this._i;
        var10000.pos += var4;
        return 2;
      } else {
        var10000 = this._i;
        var10000.pos += var4;
        return 3;
      }
    } else {
      final byte var3 = _g[var2 - 128];
      var4 = var2;
      if (var3 >= 1) {
        var4 = var2 | this._i.readUByte() << 8;
      }

      if (var3 >= 2) {
        var4 |= this._i.readUByte() << 16;
      }

      return var4;
    }
  }

  public int g784() {
    final int var1 = this._h.length;
    int var2 = -1;
    int var3 = Integer.MAX_VALUE;

    for (int var4 = 0; var4 < var1; ++var4) {
      if (this._h[var4] >= 0 && this._b[var4] < var3) {
        var2 = var4;
        var3 = this._b[var4];
      }
    }

    return var2;
  }

  public void e797() {
    this._i.pos = -1;
  }

  public void e150(final int var1) {
    this._h[var1] = this._i.pos;
  }

  public int c784() {
    return this._h.length;
  }

  private int f137(final int var1) {
    final byte var2 = this._i.data[this._i.pos];
    final int var5;
    if (var2 < 0) {
      var5 = var2 & 255;
      this._d[var1] = var5;
      ++this._i.pos;
    } else {
      var5 = this._d[var1];
    }

    if (var5 == 240 || var5 == 247) {
      final int var3 = this._i.readVariableInt();
      if (var5 == 247 && var3 > 0) {
        final int var4 = this._i.data[this._i.pos] & 255;
        if (var4 >= 241 && var4 <= 243 || var4 == 246 || var4 == 248 || var4 >= 250 && var4 <= 252 || var4 == 254) {
          ++this._i.pos;
          this._d[var1] = var4;
          return this.a080(var1, var4);
        }
      }

      this._i.pos += var3;
      return 0;
    } else {
      return this.a080(var1, var5);
    }
  }

  public void d797() {
    this._i.data = null;
    this._c = null;
    this._h = null;
    this._b = null;
    this._d = null;
  }

  public boolean f801() {
    return this._i.data != null;
  }

  public void d150(final int var1) {
    final int var2 = this._i.readVariableInt();
    final int[] var10000 = this._b;
    var10000[var1] += var2;
  }

  public void a604(final byte[] var1) {
    this._i.data = var1;
    this._i.pos = 10;
    final int var2 = this._i.readUShort();
    this._e = this._i.readUShort();
    this._a = 500000;
    this._c = new int[var2];

    Buffer var10000;
    int var3;
    int var5;
    for (var3 = 0; var3 < var2; var10000.pos += var5) {
      final int var4 = this._i.readInt();
      var5 = this._i.readInt();
      if (var4 == 1297379947) {
        this._c[var3] = this._i.pos;
        ++var3;
      }

      var10000 = this._i;
    }

    this._f = 0L;
    this._h = new int[var2];

    for (var3 = 0; var3 < var2; ++var3) {
      this._h[var3] = this._c[var3];
    }

    this._b = new int[var2];
    this._d = new int[var2];
  }
}
