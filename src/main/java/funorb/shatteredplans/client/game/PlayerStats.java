package funorb.shatteredplans.client.game;

import funorb.Strings;
import funorb.shatteredplans.StringConstants;

public final class PlayerStats {
  public final int[] production;
  public final int _e;
  public final int[] systems;
  public final int[] fleets;
  public int _l;
  public int _x;
  public int wastedResearch;
  public int _m;
  public int _t;
  public int _a;
  public int _v;
  public int _A;
  public int _y;
  public int _u;
  public int _q;
  public int _w;
  public int _i;
  public int _o;
  public int _r;
  public int _z;
  public int _s;

  public PlayerStats(final int var1) {
    this._e = var1;
    this.systems = new int[100];
    this.fleets = new int[100];
    this.production = new int[100];
  }

  public int[] b341() {
    final int[] var2 = new int[16];
    var2[3] = this._t == 0 ? 0 : 200 * this._i / this._t;
    var2[2] = -this._r;
    var2[9] = -this._q;
    var2[0] = this._A;
    var2[7] = -this.wastedResearch;
    var2[6] = this._s;
    var2[11] = -this._a;
    var2[4] = this._x;
    var2[10] = this._y;
    if (this._l == 0) {
      var2[14] = -1;
      var2[12] = -1;
      var2[13] = -1;
    } else {
      var2[12] = (this._l + 2 * this._u) / (2 * this._l);
      var2[13] = (2 * this._w + this._l) / (this._l * 2);
      var2[14] = (this._l + 2 * this._z) / (2 * this._l);
    }

    var2[15] = this._a != 0 ? (this._a + 201 * this._y) / ((this._y + this._a) * 2) : 100;
    var2[5] = this._o;
    var2[8] = this._m;
    var2[1] = this._v;
    return var2;
  }

  public String[] a061() {
    final String[] var2 = new String[16];
    var2[0] = Integer.toString(this._A);
    var2[1] = Integer.toString(this._v);
    var2[2] = Integer.toString(this._r);
    if (this._t == 0) {
      var2[3] = StringConstants.TEXT_NOT_AVAILABLE;
    } else {
      final int var3 = (this._i * 20 + this._t) / (this._t * 2);
      var2[3] = var3 / 10 + StringConstants.TEXT_DECIMAL + var3 % 10;
    }

    var2[4] = Integer.toString(this._x);
    var2[5] = Integer.toString(this._o);
    var2[6] = Integer.toString(this._s);
    var2[7] = Integer.toString(this.wastedResearch);
    var2[8] = Integer.toString(this._m);
    var2[9] = Integer.toString(this._q);
    var2[10] = Integer.toString(this._y);
    var2[11] = Integer.toString(this._a);
    if (this._l == 0) {
      var2[14] = StringConstants.TEXT_NOT_AVAILABLE;
      var2[12] = StringConstants.TEXT_NOT_AVAILABLE;
      var2[13] = StringConstants.TEXT_NOT_AVAILABLE;
    } else {
      var2[12] = Strings.format(StringConstants.TEXT_PERCENTAGE, Integer.toString((this._l + this._u * 2) / (this._l * 2)));
      var2[13] = Strings.format(StringConstants.TEXT_PERCENTAGE, Integer.toString((2 * this._w + this._l) / (2 * this._l)));
      var2[14] = Strings.format(StringConstants.TEXT_PERCENTAGE, Integer.toString((this._l + 2 * this._z) / (this._l * 2)));
    }

    var2[15] = Strings.format(StringConstants.TEXT_PERCENTAGE, Integer.toString(this._a == 0 ? 100 : (this._y * 201 + this._a) / (2 * (this._a + this._y))));
    return var2;
  }
}
