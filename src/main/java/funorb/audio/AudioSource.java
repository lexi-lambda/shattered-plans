package funorb.audio;

import funorb.data.NodeList;

public abstract class AudioSource extends NodeList.Node implements Iterable<AudioSource> {
  public volatile boolean enabled = true;
  public int lastP8_idk;
  public AudioSource nextSource_idk;
  public RawSampleS8 rawSample;

  protected AudioSource() {}

  public abstract void processAndWrite(int[] dataS16P8, int offset, int len);

  public abstract void processAndDiscard(int len);

  protected final void processAndWriteIfEnabled(final int[] dataS16P8, final int offset, final int len) {
    if (this.enabled) {
      this.processAndWrite(dataS16P8, offset, len);
    } else {
      this.processAndDiscard(len);
    }
  }

  public abstract int returns_0_1_or_2();

  public int someP8_idk() {
    return 255;
  }
}
