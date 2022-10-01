package funorb.io;

import funorb.util.IsaacCipher;
import org.jetbrains.annotations.Range;

public final class CipheredBuffer extends Buffer {
  private static final int[] _rqj = new int[]{0, 1, 3, 7, 15, 31, 63, 127, 255, 511, 1023, 2047, 4095, 8191, 16383, 32767, 65535, 131071, 262143, 524287, 1048575, 2097151, 4194303, 8388607, 16777215, 33554431, 67108863, 134217727, 268435455, 536870911, 1073741823, Integer.MAX_VALUE, -1};
  private IsaacCipher isaac;
  private int _n;

  public CipheredBuffer(final byte[] data) {
    super(data);
  }

  public CipheredBuffer() {
    super(5000);
  }

  public void setCipher(final IsaacCipher isaac) {
    this.isaac = isaac;
  }

  public int b543(int var2) {
    int var3 = this._n >> 3;

    int var4 = -(7 & this._n) + 8;
    int var5 = 0;

    for (this._n += var2; var4 < var2; var4 = 8) {
      var5 += (this.data[var3++] & _rqj[var4]) << -var4 + var2;
      var2 -= var4;
    }

    if (var4 == var2) {
      var5 += _rqj[var4] & this.data[var3];
    } else {
      var5 += this.data[var3] >> var4 - var2 & _rqj[var2];
    }

    return var5;
  }

  public void writeCipheredByte(final int val) {
    this.data[this.pos++] = (byte) (val + this.isaac.nextInt());
  }

  public void readCipheredBytes(final byte[] buffer, final int len) {
    for (int i = 0; i < len; ++i) {
      buffer[i] = (byte) (this.data[this.pos++] - this.isaac.nextInt());
    }
  }

  public @Range(from=0, to=0xff) int readCipheredByte() {
    return (this.data[this.pos++] - this.isaac.nextInt()) & 0xff;
  }

  public void m150() {
    this._n = this.pos * 8;
  }

  public void i423() {
    this.pos = (7 + this._n) / 8;
  }
}
