package funorb.audio;

public final class RawSampleS8 {
  public final int loopEnd_idfk;
  public final int loopStart_idfk;
  public final int sampleRate;
  public final byte[] data;
  public final boolean isLooped_idk;
  public int _h;

  public RawSampleS8(final byte[] data, final int loopStart, final int loopEnd) {
    this(SampledAudioChannel.SAMPLES_PER_SECOND, data, loopStart, loopEnd, false);
  }

  public RawSampleS8(
    final int sampleRate,
    final byte[] data,
    final int loopStart,
    final int loopEnd,
    final boolean isLooped
  ) {
    this.sampleRate = sampleRate;
    this.data = data;
    this.loopStart_idfk = loopStart;
    this.loopEnd_idfk = loopEnd;
    this.isLooped_idk = isLooped;
  }
}