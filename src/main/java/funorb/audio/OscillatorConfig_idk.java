package funorb.audio;

import funorb.io.Buffer;

public final class OscillatorConfig_idk {
  public int _d;
  public int _i;
  public int waveform;
  private int count = 2;
  private int[] values1 = new int[2];
  private int[] values2 = new int[2];
  private int _g;
  private int _e;
  private int _b;
  private int _j;
  private int pos;

  public OscillatorConfig_idk() {
    this.values1[1] = 65535;
    this.values2[1] = 65535;
  }

  public void initialize(final Buffer buffer) {
    this.waveform = buffer.readUByte();
    this._d = buffer.readInt();
    this._i = buffer.readInt();
    this.read(buffer);
  }

  public void read(final Buffer buffer) {
    this.count = buffer.readUByte();
    this.values1 = new int[this.count];
    this.values2 = new int[this.count];
    for (int i = 0; i < this.count; ++i) {
      this.values1[i] = buffer.readUShort();
      this.values2[i] = buffer.readUShort();
    }
  }

  public void reset() {
    this._e = 0;
    this.pos = 0;
    this._b = 0;
    this._j = 0;
    this._g = 0;
  }

  public int next(final int volume) {
    if (this._g >= this._e) {
      this._j = this.values2[this.pos++] << 15;
      if (this.pos >= this.count) {
        this.pos = this.count - 1;
      }

      this._e = (int) (((double) this.values1[this.pos] / 65536.0D) * (double) volume);
      if (this._e > this._g) {
        this._b = ((this.values2[this.pos] << 15) - this._j) / (this._e - this._g);
      }
    }

    this._j += this._b;
    ++this._g;
    return this._j - this._b >> 15;
  }
}
