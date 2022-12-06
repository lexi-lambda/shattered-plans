package funorb.audio;

import funorb.io.Buffer;

public final class SynthEnvelope {
  public int min;
  public int max;
  public int waveform;
  private int numSegs;
  private int[] envPtsX;
  private int[] envPtsY;
  private int curSamples;
  private int envSegEnd;
  private int curSlope;
  private int curY;
  private int curSegIdx;

  public SynthEnvelope() {
    this.numSegs = 2;
    this.envPtsX = new int[]{0, 65535};
    this.envPtsY = new int[]{0, 65535};
  }

  public void load(final Buffer buffer) {
    this.waveform = buffer.readUByte();
    this.min = buffer.readInt();
    this.max = buffer.readInt();
    this.loadPoints(buffer);
  }

  public void loadPoints(final Buffer buffer) {
    this.numSegs = buffer.readUByte();
    this.envPtsX = new int[this.numSegs];
    this.envPtsY = new int[this.numSegs];
    for (int i = 0; i < this.numSegs; ++i) {
      this.envPtsX[i] = buffer.readUShort();
      this.envPtsY[i] = buffer.readUShort();
    }
  }

  public void reset() {
    this.envSegEnd = 0;
    this.curSegIdx = 0;
    this.curSlope = 0;
    this.curY = 0;
    this.curSamples = 0;
  }

  public int next(final int len) {
    if (this.curSamples >= this.envSegEnd) {
      this.curY = this.envPtsY[this.curSegIdx++] << 15;
      if (this.curSegIdx >= this.numSegs) {
        this.curSegIdx = this.numSegs - 1;
      }

      this.envSegEnd = (int) (((double) this.envPtsX[this.curSegIdx] / 65536.0D) * (double) len);
      if (this.envSegEnd > this.curSamples) {
        this.curSlope = ((this.envPtsY[this.curSegIdx] << 15) - this.curY) / (this.envSegEnd - this.curSamples);
      }
    }

    this.curY += this.curSlope;
    ++this.curSamples;
    return this.curY - this.curSlope >> 15;
  }
}
