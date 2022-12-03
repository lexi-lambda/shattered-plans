package funorb.audio;

import funorb.data.NodeList;
import org.jetbrains.annotations.NotNull;

import java.util.Iterator;

public final class vk_ extends MixerInterface_idk {
  private final NodeList<MixerInterface_idk> _l = new NodeList<>();

  @Override
  public @NotNull Iterator<MixerInterface_idk> iterator() {
    return this._l.iterator();
  }

  @Override
  public int a784() {
    return 0;
  }

  @Override
  public synchronized void generateAudio2_idk(final int len) {
    for (final MixerInterface_idk var2 : this._l) {
      var2.generateAudio2_idk(len);
    }
  }

  public synchronized void addFirst(final al_ var1) {
    this._l.addFirst(var1);
  }

  @Override
  public synchronized void generateAudio1_idk(final int[] dest, final int offset, final int len) {
    for (final MixerInterface_idk var4 : this._l) {
      var4.generateAudio(dest, offset, len);
    }
  }
}
