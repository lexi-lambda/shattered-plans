package funorb.audio;

import funorb.data.NodeList;
import org.jetbrains.annotations.NotNull;

import java.util.Iterator;

public final class SoundManager extends AudioSource {
  private final NodeList<AudioSource> inner = new NodeList<>();

  @Override
  public @NotNull Iterator<AudioSource> iterator() {
    return this.inner.iterator();
  }

  @Override
  public int returns_0_1_or_2() {
    return 0;
  }

  @Override
  public synchronized void processAndDiscard(final int len) {
    for (final AudioSource gen : this.inner) {
      gen.processAndDiscard(len);
    }
  }

  public synchronized void addFirst(final RawSamplePlayer var1) {
    this.inner.addFirst(var1);
  }

  @Override
  public synchronized void processAndWrite(final int[] data_s16p8, final int offset, final int len) {
    for (final AudioSource gen : this.inner) {
      gen.processAndWriteIfEnabled(data_s16p8, offset, len);
    }
  }
}
