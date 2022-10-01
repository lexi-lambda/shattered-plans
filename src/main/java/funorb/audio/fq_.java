package funorb.audio;

public final class fq_ {
  public final int[] _b;
  public final int[] _a;
  public final int _c;
  public int _d;

  public fq_() {
    fd_.a137(16);
    this._c = fd_.c784() != 0 ? fd_.a137(4) + 1 : 1;
    if (fd_.c784() != 0) {
      fd_.a137(8);
    }

    fd_.a137(2);
    if (this._c > 1) {
      this._d = fd_.a137(4);
    }

    this._b = new int[this._c];
    this._a = new int[this._c];

    for (int var1 = 0; var1 < this._c; ++var1) {
      fd_.a137(8);
      this._b[var1] = fd_.a137(8);
      this._a[var1] = fd_.a137(8);
    }

  }
}
