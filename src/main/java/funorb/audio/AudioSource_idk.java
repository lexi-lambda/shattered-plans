package funorb.audio;

import funorb.data.NodeList;

public abstract class AudioSource_idk extends NodeList.Node implements Iterable<AudioSource_idk> {
  public volatile boolean enabled = true;
  public int _i;
  public AudioSource_idk _h;
  public AudioSampleData_idk sampleData;

  protected AudioSource_idk() {}

  public abstract void processAndWrite(int[] dest, int offset, int len);

  public abstract void processAndDiscard(int len);

  protected final void processAndWriteIfEnabled(final int[] dest, final int offset, final int len) {
    if (this.enabled) {
      this.processAndWrite(dest, offset, len);
    } else {
      this.processAndDiscard(len);
    }
  }

  public abstract int a784();

  public int c784() {
    return 255;
  }
}
