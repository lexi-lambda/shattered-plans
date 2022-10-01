package funorb.cache;

import funorb.shatteredplans.client.MailboxMessage;
import funorb.shatteredplans.client.MessagePumpThread;

import java.io.Closeable;
import java.util.ArrayDeque;
import java.util.Queue;
import java.util.function.Function;

/**
 * A {@link CacheWorker} mediates reads and writes to the local resource cache.
 * It maintains a queue of {@link WorkItem}s and processes them in the
 * background, ensuring all reads and writes to the cache are serialized.
 */
public final class CacheWorker implements Runnable, Closeable {
  public static CacheWorker instance;

  private final Queue<WorkItem> pending = new ArrayDeque<>();
  public int pendingCount = 0;
  private Thread thread;
  private boolean shutdownRequested = false;

  public CacheWorker(final Function<Runnable, Thread> createThread) {
    this.thread = createThread.apply(this);
  }

  public static CacheWorker create(final MessagePumpThread messagePumpThread) {
    return new CacheWorker(self -> {
      final MailboxMessage message = messagePumpThread.sendSpawnThreadMessage(self, 5);
      if (message.busyAwait() == MailboxMessage.Status.FAILURE) {
        throw new RuntimeException();
      } else {
        return (Thread) message.response;
      }
    });
  }

  @Override
  public void run() {
    while (!this.shutdownRequested) {
      final WorkItem req;
      synchronized (this.pending) {
        req = this.pending.poll();
        if (req == null) {
          try {
            this.pending.wait();
          } catch (final InterruptedException var5) {}
          continue;
        }

        --this.pendingCount;
      }

      if (req.type == WorkItem.Type.WRITE) {
        req.cacheFile.write(req.groupId, req.data, req.data.length);
      } else if (req.type == WorkItem.Type.READ) {
        req.data = req.cacheFile.read(req.groupId);
      } else {
        throw new IllegalStateException();
      }

      req.setLoaded();
    }
  }

  private void enqueue(final WorkItem req) {
    synchronized (this.pending) {
      this.pending.add(req);
      ++this.pendingCount;
      this.pending.notifyAll();
    }
  }

  public void enqueueWrite(final BufferedPageCache cacheFile, final int groupId, final byte[] data) {
    final WorkItem req = new WorkItem(WorkItem.Type.WRITE, cacheFile, false);
    req.groupId = groupId;
    req.data = data;
    this.enqueue(req);
  }

  public WorkItem enqueueRead(final BufferedPageCache cacheFile, final int groupId) {
    final WorkItem req = new WorkItem(WorkItem.Type.READ, cacheFile, false);
    req.groupId = groupId;
    this.enqueue(req);
    return req;
  }

  /**
   * Performs an <i>immediate</i> read, blocking until the read has been
   * performed. If there are any pending writes to the cache for the given
   * group, the pending written data will be returned. Otherwise, the data is
   * fetched via a call to {@link BufferedPageCache#read(int)}. <b>Note that the
   * returned data may be {@code null} in the case of a cache miss.</b>
   */
  public WorkItem tryRead(final BufferedPageCache cacheFile, final int groupId) {
    synchronized (this.pending) {
      for (final WorkItem next : this.pending) {
        if (next.groupId == groupId && next.cacheFile == cacheFile && next.type == WorkItem.Type.WRITE) {
          assert next.data != null;
          final WorkItem req = new WorkItem(WorkItem.Type.TRY_READ, null, false);
          req.data = next.data;
          req.setLoaded();
          return req;
        }
      }
    }

    final WorkItem req = new WorkItem(WorkItem.Type.TRY_READ, null, true);
    req.data = cacheFile.read(groupId);
    req.setLoaded();
    return req;
  }

  @Override
  public void close() {
    this.shutdownRequested = true;
    synchronized (this.pending) {
      this.pending.notifyAll();
    }

    try {
      this.thread.join();
    } catch (final InterruptedException var4) {}

    this.thread = null;
  }

  public static final class WorkItem extends PageLoader.WorkItem {
    private enum Type {
      TRY_READ, WRITE, READ
    }

    private final Type type;
    private final BufferedPageCache cacheFile;
    private byte[] data;
    private int groupId;
    public final boolean isTryWithoutWrite;

    private WorkItem(final Type type, final BufferedPageCache cacheFile, final boolean isTryWithoutWrite) {
      this.type = type;
      this.cacheFile = cacheFile;
      this.isTryWithoutWrite = isTryWithoutWrite;
    }

    @Override
    public int percentLoaded() {
      return this.isLoaded() ? 100 : 0;
    }

    @Override
    public byte[] getData() {
      if (!this.isLoaded()) {
        throw new IllegalStateException("not yet loaded");
      }
      return this.data;
    }
  }
}
