package funorb.audio;

public final class RawSampleS8 {
  public final int loopEnd;
  public final int loopStart;
  public final int sampleRate;
  public final byte[] data_s8;
  public final boolean isPingPongLoop;
  public int someCounter_idk;

  public RawSampleS8(final byte[] data, final int loopStart, final int loopEnd) {
    this(SampledAudioChannelS16.SAMPLE_RATE, data, loopStart, loopEnd, false);
  }

  public RawSampleS8(
    final int sampleRate,
    final byte[] data,
    final int loopStart,
    final int loopEnd,
    final boolean isPingPongLoop
  ) {
    this.sampleRate = sampleRate;
    this.data_s8 = data;
    this.loopStart = loopStart;
    this.loopEnd = loopEnd;
    this.isPingPongLoop = isPingPongLoop;
  }
}
