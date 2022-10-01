package funorb.audio;

import funorb.data.NodeList;

public abstract class tn_ extends NodeList.Node implements Iterable<tn_> {
  public volatile boolean _j = true;
  public int _i;
  public tn_ _h;
  public kk_ _k;

  protected tn_() {}

  public abstract void b397(int[] dest, int offset, int len);

  protected final void a397(final int[] dest, final int offset, final int len) {
    if (this._j) {
      this.b397(dest, offset, len);
    } else {
      this.a150(len);
    }
  }

  public abstract void a150(int len);

  public abstract int a784();

  public int c784() {
    return 255;
  }
}
