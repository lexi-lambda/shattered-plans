package funorb.audio;

public final class AudioSampleData_idk {
  public final int _k;
  public final int _l;
  public final int sampleRate;
  public final byte[] data;
  public final boolean _i;
  public int _h;

  public AudioSampleData_idk(final byte[] data, final int var3, final int var4) {
    this(SampledAudioChannel.SAMPLES_PER_SECOND, data, var3, var4, false);
  }

  public AudioSampleData_idk(final int sampleRate, final byte[] data, final int var3, final int var4, final boolean var5) {
    this.sampleRate = sampleRate;
    this.data = data;
    this._l = var3;
    this._k = var4;
    this._i = var5;
  }
}
