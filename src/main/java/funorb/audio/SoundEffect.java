package funorb.audio;

public final class SoundEffect {
  public final int volume;
  public final RawSampleS8 sample;

  private SoundEffect(final RawSampleS8 sample, final int volume) {
    this.volume = volume;
    this.sample = sample;
  }

  @SuppressWarnings("SameParameterValue")
  public static SoundEffect load1(final String item, final int volume) {
    return new SoundEffect(SoundLoader.globalLoader.load1(item), volume);
  }

  @SuppressWarnings("SameParameterValue")
  public static SoundEffect load2(final String item, final int volume) {
    return new SoundEffect(SoundLoader.globalLoader.load2(item), volume);
  }
}
