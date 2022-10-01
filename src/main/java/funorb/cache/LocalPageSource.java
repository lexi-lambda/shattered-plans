package funorb.cache;

import funorb.io.Buffer;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.InputStream;
import java.util.function.BiFunction;

public final class LocalPageSource extends PageSource {
  private final BiFunction<Integer, Integer, InputStream> open;

  public LocalPageSource(final BiFunction<Integer, Integer, InputStream> open) {
    this.open = open;
  }

  @Override
  protected void enqueue(final @NotNull WorkItem item) {
    final int pageId = (int) (item.partId >> 32);
    final int part = (int) item.partId;
    try (final InputStream in = this.open.apply(pageId, part)) {
      final byte[] data = in.readAllBytes();
      item.buffer = new Buffer(data.length + item.extraBytes);
      item.buffer.writeBytes(data);
    } catch (final IOException e) {
      throw new RuntimeException(e);
    }
    item.setLoaded();
  }

  @Override
  public void closeDueToError() {}
}
