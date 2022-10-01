package funorb.util;

public final class IsaacCipher {
  private final int[] state = new int[256];
  private final int[] data = new int[256];
  private int _i;
  private int _g;
  private int _c;
  private int _f;

  public IsaacCipher(final int[] iv) {
    System.arraycopy(iv, 0, this.data, 0, iv.length);
    this.initialize();
  }

  public int nextInt() {
    if (this._c == 0) {
      this.c150();
      this._c = 256;
    }

    return this.data[--this._c];
  }

  private void initialize() {
    int var3 = -1640531527;
    int var10 = -1640531527;
    int var8 = -1640531527;
    int var4 = -1640531527;
    int var6 = -1640531527;
    int var5 = -1640531527;
    int var9 = -1640531527;
    int var7 = -1640531527;

    int var2;
    for (var2 = 0; var2 < 4; ++var2) {
      var3 ^= var4 << 11;
      var4 += var5;
      var6 += var3;
      var4 ^= var5 >>> 2;
      var5 += var6;
      var7 += var4;
      var5 ^= var6 << 8;
      var8 += var5;
      var6 += var7;
      var6 ^= var7 >>> 16;
      var9 += var6;
      var7 += var8;
      var7 ^= var8 << 10;
      var10 += var7;
      var8 += var9;
      var8 ^= var9 >>> 4;
      var3 += var8;
      var9 += var10;
      var9 ^= var10 << 8;
      var4 += var9;
      var10 += var3;
      var10 ^= var3 >>> 9;
      var5 += var10;
      var3 += var4;
    }

    for (var2 = 0; var2 < 256; var2 += 8) {
      var7 += this.data[var2 + 4];
      var8 += this.data[5 + var2];
      var3 += this.data[var2];
      var9 += this.data[var2 + 6];
      var6 += this.data[3 + var2];
      var5 += this.data[2 + var2];
      var4 += this.data[var2 + 1];
      var10 += this.data[var2 + 7];
      var3 ^= var4 << 11;
      var6 += var3;
      var4 += var5;
      var4 ^= var5 >>> 2;
      var5 += var6;
      var7 += var4;
      var5 ^= var6 << 8;
      var8 += var5;
      var6 += var7;
      var6 ^= var7 >>> 16;
      var7 += var8;
      var9 += var6;
      var7 ^= var8 << 10;
      var10 += var7;
      var8 += var9;
      var8 ^= var9 >>> 4;
      var9 += var10;
      var3 += var8;
      var9 ^= var10 << 8;
      var4 += var9;
      var10 += var3;
      var10 ^= var3 >>> 9;
      var5 += var10;
      var3 += var4;
      this.state[var2] = var3;
      this.state[var2 + 1] = var4;
      this.state[var2 + 2] = var5;
      this.state[var2 + 3] = var6;
      this.state[4 + var2] = var7;
      this.state[5 + var2] = var8;
      this.state[6 + var2] = var9;
      this.state[var2 + 7] = var10;
    }

    for (var2 = 0; var2 < 256; var2 += 8) {
      var10 += this.state[var2 + 7];
      var5 += this.state[var2 + 2];
      var4 += this.state[1 + var2];
      var6 += this.state[3 + var2];
      var3 += this.state[var2];
      var9 += this.state[6 + var2];
      var7 += this.state[4 + var2];
      var8 += this.state[5 + var2];
      var3 ^= var4 << 11;
      var6 += var3;
      var4 += var5;
      var4 ^= var5 >>> 2;
      var5 += var6;
      var7 += var4;
      var5 ^= var6 << 8;
      var6 += var7;
      var8 += var5;
      var6 ^= var7 >>> 16;
      var7 += var8;
      var9 += var6;
      var7 ^= var8 << 10;
      var8 += var9;
      var10 += var7;
      var8 ^= var9 >>> 4;
      var9 += var10;
      var3 += var8;
      var9 ^= var10 << 8;
      var4 += var9;
      var10 += var3;
      var10 ^= var3 >>> 9;
      var3 += var4;
      var5 += var10;
      this.state[var2] = var3;
      this.state[var2 + 1] = var4;
      this.state[var2 + 2] = var5;
      this.state[3 + var2] = var6;
      this.state[4 + var2] = var7;
      this.state[var2 + 5] = var8;
      this.state[var2 + 6] = var9;
      this.state[var2 + 7] = var10;
    }

    this.c150();
    this._c = 256;
  }

  private void c150() {
    this._i += ++this._f;

    for (int var2 = 0; var2 < 256; ++var2) {
      final int var3 = this.state[var2];
      if ((2 & var2) != 0) {
        if ((1 & var2) == 0) {
          this._g ^= this._g << 2;
        } else {
          this._g ^= this._g >>> 16;
        }
      } else if ((1 & var2) == 0) {
        this._g ^= this._g << 13;
      } else {
        this._g ^= this._g >>> 6;
      }

      this._g += this.state[255 & var2 + 128];
      final int var4;
      this.state[var2] = var4 = this._g + this.state[var3 >> 2 & 255] + this._i;
      this.data[var2] = this._i = var3 + this.state[255 & var4 >> 8 >> 2];
    }
  }
}
