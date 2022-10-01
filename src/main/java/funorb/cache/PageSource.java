package funorb.cache;

import funorb.io.Buffer;
import org.jetbrains.annotations.NotNull;

/**
 * A {@link PageSource} manages loading resource pages that aren’t yet in the cache.
 */
public abstract class PageSource {
  public volatile int failureCount = 0;
  public volatile ErrorCode errorCode = null;

  protected abstract void enqueue(@NotNull WorkItem item);
  public abstract void closeDueToError();

  public final WorkItem enqueueLoad(final int pageId, final int part, final byte extraBytes) {
    final WorkItem item = new WorkItem(((long) pageId << 32) + (long) part);
    item.extraBytes = extraBytes;

    this.enqueue(item);
    return item;
  }

  public static final class WorkItem extends PageLoader.WorkItem {
    final long partId;
    byte extraBytes;
    int bytesReadSoFar;
    Buffer buffer;

    private WorkItem(final long partId) {
      this.partId = partId;
    }

    @Override
    public int percentLoaded() {
      if (this.buffer == null) {
        return 0;
      } else {
        return 100 * this.buffer.pos / (this.buffer.data.length - this.extraBytes);
      }
    }
  
    @Override
    public byte[] getData() {
      if (!this.isLoaded() || this.buffer.pos < this.buffer.data.length - this.extraBytes) {
        throw new IllegalStateException("not yet loaded");
      }
      return this.buffer.data;
    }
  }

  public enum ErrorCode {
    PROTOCOL_ERROR, INTEGRITY_CHECK_FAILURE, UNKNOWN_CONNECTION_FAILURE, SERVER_CODE_51, SERVER_CODE_50
  }
}
