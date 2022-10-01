package funorb.audio;

public final class SoundEffect {
  public final int volume;
  public final kk_ _f;

  private SoundEffect(final kk_ var1, final int var2) {
    this.volume = var2;
    this._f = var1;
  }

  @SuppressWarnings("SameParameterValue")
  public static SoundEffect load1(final String item, final int var1) {
    return new SoundEffect(SoundLoader.globalLoader.load1(item), var1);
  }

  @SuppressWarnings("SameParameterValue")
  public static SoundEffect load2(final String item, final int var1) {
    return new SoundEffect(SoundLoader.globalLoader.load2(item), var1);
  }
}
