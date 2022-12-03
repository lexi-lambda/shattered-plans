package funorb.audio;

public final class SoundEffect {
  public final int volume;
  public final AudioSampleData_idk sample;

  private SoundEffect(final AudioSampleData_idk sample, final int volume) {
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
