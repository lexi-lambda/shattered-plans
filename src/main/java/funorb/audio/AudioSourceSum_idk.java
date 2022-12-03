package funorb.audio;

import funorb.data.NodeList;
import org.jetbrains.annotations.NotNull;

import java.util.Iterator;

public final class AudioSourceSum_idk extends AudioSource_idk {
  private final NodeList<AudioSource_idk> inner = new NodeList<>();

  @Override
  public @NotNull Iterator<AudioSource_idk> iterator() {
    return this.inner.iterator();
  }

  @Override
  public int a784() {
    return 0;
  }

  @Override
  public synchronized void processAndDiscard(final int len) {
    for (final AudioSource_idk gen : this.inner) {
      gen.processAndDiscard(len);
    }
  }

  public synchronized void addFirst(final AudioSamplePlayback_idk var1) {
    this.inner.addFirst(var1);
  }

  @Override
  public synchronized void processAndWrite(final int[] dest, final int offset, final int len) {
    for (final AudioSource_idk gen : this.inner) {
      gen.processAndWriteIfEnabled(dest, offset, len);
    }
  }
}
