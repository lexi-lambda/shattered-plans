package funorb.audio;

public final class kk_ {
  public final int _k;
  public final int _l;
  public final int _j;
  public final byte[] data;
  public final boolean _i;
  public int _h;

  public kk_(final byte[] data, final int var3, final int var4) {
    this(SampledAudioChannel.SAMPLES_PER_SECOND, data, var3, var4, false);
  }

  public kk_(final int var1, final byte[] data, final int var3, final int var4, final boolean var5) {
    this._j = var1;
    this.data = data;
    this._l = var3;
    this._k = var4;
    this._i = var5;
  }
}