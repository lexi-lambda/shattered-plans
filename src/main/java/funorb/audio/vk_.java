package funorb.audio;

import funorb.data.NodeList;
import org.jetbrains.annotations.NotNull;

import java.util.Iterator;

public final class vk_ extends tn_ {
  private final NodeList<tn_> _l = new NodeList<>();

  @Override
  public @NotNull Iterator<tn_> iterator() {
    return this._l.iterator();
  }

  @Override
  public int a784() {
    return 0;
  }

  @Override
  public synchronized void a150(final int len) {
    for (final tn_ var2 : this._l) {
      var2.a150(len);
    }
  }

  public synchronized void addFirst(final al_ var1) {
    this._l.addFirst(var1);
  }

  @Override
  public synchronized void b397(final int[] dest, final int offset, final int len) {
    for (final tn_ var4 : this._l) {
      var4.a397(dest, offset, len);
    }
  }
}
