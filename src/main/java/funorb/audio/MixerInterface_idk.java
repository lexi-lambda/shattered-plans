package funorb.audio;

import funorb.data.NodeList;

public abstract class MixerInterface_idk extends NodeList.Node implements Iterable<MixerInterface_idk> {
  public volatile boolean useGenerateAudio1_idk = true;
  public int _i;
  public MixerInterface_idk _h;
  public kk_ _k;

  protected MixerInterface_idk() {}

  public abstract void generateAudio1_idk(int[] dest, int offset, int len);

  public abstract void generateAudio2_idk(int len);

  protected final void generateAudio(final int[] dest, final int offset, final int len) {
    if (this.useGenerateAudio1_idk) {
      this.generateAudio1_idk(dest, offset, len);
    } else {
      this.generateAudio2_idk(len);
    }
  }

  public abstract int a784();

  public int c784() {
    return 255;
  }
}
