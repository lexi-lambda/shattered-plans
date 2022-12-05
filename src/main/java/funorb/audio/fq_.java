package funorb.audio;

public final class fq_ {
  public final int[] _b;
  public final int[] _a;
  public final int _c;
  public int _d;

  public fq_() {
    FmtVorbis.readBits(16);
    this._c = FmtVorbis.readBit() != 0 ? FmtVorbis.readBits(4) + 1 : 1;
    if (FmtVorbis.readBit() != 0) {
      FmtVorbis.readBits(8);
    }

    FmtVorbis.readBits(2);
    if (this._c > 1) {
      this._d = FmtVorbis.readBits(4);
    }

    this._b = new int[this._c];
    this._a = new int[this._c];

    for (int var1 = 0; var1 < this._c; ++var1) {
      FmtVorbis.readBits(8);
      this._b[var1] = FmtVorbis.readBits(8);
      this._a[var1] = FmtVorbis.readBits(8);
    }

  }
}
