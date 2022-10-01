package funorb.cache;

import funorb.io.Buffer;
import funorb.io.DuplexStream;
import funorb.shatteredplans.client.JagexApplet;
import funorb.util.PseudoMonotonicClock;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.ArrayDeque;
import java.util.Queue;

public final class RemotePageSource extends PageSource {
  private static final int QUEUE_CAPACITY = 20;

  private final Queue<WorkItem> unsentItems = new ArrayDeque<>(QUEUE_CAPACITY);
  private final Queue<WorkItem> pendingItems = new ArrayDeque<>(QUEUE_CAPACITY);
  private WorkItem current;

  private final Buffer c2sPacket = new Buffer(6);
  private final Buffer s2cPacket = new Buffer(10);

  private DuplexStream serverConnection;
  private int millisSinceLastReceivedData;
  private long lastLoadTime;

  @Override
  protected void enqueue(final @NotNull WorkItem item) {
    if (this.isQueueFull()) {
      throw new IllegalStateException("queue overflow");
    }
    this.unsentItems.add(item);
  }

  private boolean isQueueFull() {
    return this.queueSize() >= QUEUE_CAPACITY;
  }

  private int queueSize() {
    return this.unsentItems.size() + this.pendingItems.size();
  }

  public void initializeServerConnection(final DuplexStream serverConnection) {
    if (this.serverConnection != null) {
      try {
        this.serverConnection.close();
      } catch (final Exception e) {}
    }

    this.serverConnection = serverConnection;
    this.handshake1();
    this.handshake2();
    this.current = null;
    this.s2cPacket.pos = 0;

    WorkItem item;
    while ((item = this.pendingItems.poll()) != null) {
      this.unsentItems.add(item);
    }

    this.millisSinceLastReceivedData = 0;
    this.lastLoadTime = PseudoMonotonicClock.currentTimeMillis();
  }

  public void close() {
    if (this.serverConnection != null) {
      this.serverConnection.close();
    }
  }

  @Override
  public void closeDueToError() {
    try {
      this.serverConnection.close();
    } catch (final Exception e) {}

    this.serverConnection = null;
    this.errorCode = ErrorCode.INTEGRITY_CHECK_FAILURE;
    //noinspection NonAtomicOperationOnVolatileField
    ++this.failureCount;
  }

  public boolean processWork() {
    if (this.serverConnection != null) {
      final long now = PseudoMonotonicClock.currentTimeMillis();
      int millisSinceLast = (int) (now - this.lastLoadTime);
      this.lastLoadTime = now;
      if (millisSinceLast > 200) {
        millisSinceLast = 200;
      }

      this.millisSinceLastReceivedData += millisSinceLast;
      if (this.millisSinceLastReceivedData > JagexApplet.SERVER_TIMEOUT_MILLIS) {
        this.serverConnection.close();
        this.serverConnection = null;
      }
    }
    if (this.serverConnection == null) {
      return this.queueSize() == 0;
    }

    try {
      this.serverConnection.pollForException();

      WorkItem item;
      while ((item = this.unsentItems.poll()) != null) {
        this.c2sPacket.pos = 0;
        this.c2sPacket.writeByte(0);
        this.c2sPacket.writeI40(item.partId);
        this.serverConnection.write(this.c2sPacket.data, this.c2sPacket.data.length);
        this.pendingItems.add(item);
      }

      for (int i = 0; i < 100; ++i) {
        final int available = this.serverConnection.inputAvailable();
        if (available == 0) {
          break;
        }

        this.millisSinceLastReceivedData = 0;
        final byte bytesNeeded;
        if (this.current == null) {
          bytesNeeded = 10;
        } else if (this.current.bytesReadSoFar == 0) {
          bytesNeeded = 1;
        } else {
          bytesNeeded = 0;
        }

        if (bytesNeeded > 0) {
          int bytesToRead = bytesNeeded - this.s2cPacket.pos;
          if (available < bytesToRead) {
            bytesToRead = available;
          }

          this.serverConnection.read(this.s2cPacket.data, this.s2cPacket.pos, bytesToRead);
          this.s2cPacket.pos += bytesToRead;

          if (this.s2cPacket.pos >= bytesNeeded) {
            if (this.current == null) {
              this.s2cPacket.pos = 0;
              final int group = this.s2cPacket.readUByte();
              final int index = this.s2cPacket.readInt();
              final int var8 = this.s2cPacket.readUByte();
              final int var9 = this.s2cPacket.readInt();
              final int isHigh = var8 & 127;
              final boolean priority = (var8 & 128) != 0;
              final long groupAndIndex = ((long) group << 32) + (long) index;
              if (!priority) {
                throw new IOException();
              }

              final WorkItem var14 = this.pendingItems.stream()
                  .filter(entry -> entry.partId == groupAndIndex).findFirst()
                  .orElseThrow(IOException::new);

              final int headerSize = isHigh != 0 ? 9 : 5;
              this.current = var14;
              this.current.buffer = new Buffer(this.current.extraBytes + var9 + headerSize);
              this.current.buffer.writeByte(isHigh);
              this.current.buffer.writeInt(var9);
              this.s2cPacket.pos = 0;
              this.current.bytesReadSoFar = 10;
            } else {
              if (this.current.bytesReadSoFar != 0) {
                throw new IOException();
              }

              if (this.s2cPacket.data[0] == -1) {
                this.s2cPacket.pos = 0;
                this.current.bytesReadSoFar = 1;
              } else {
                this.current = null;
              }
            }
          }
        } else {
          final int bytesWanted = this.current.buffer.data.length - this.current.extraBytes;
          int bytesToRead = 512 - this.current.bytesReadSoFar;
          if (bytesToRead > bytesWanted - this.current.buffer.pos) {
            bytesToRead = bytesWanted - this.current.buffer.pos;
          }

          if (bytesToRead > available) {
            bytesToRead = available;
          }

          this.serverConnection.read(this.current.buffer.data, this.current.buffer.pos, bytesToRead);

          this.current.bytesReadSoFar += bytesToRead;
          this.current.buffer.pos += bytesToRead;
          if (this.current.buffer.pos == bytesWanted) {
            this.pendingItems.remove(this.current);
            this.current.setLoaded();
            this.current = null;
          } else if (this.current.bytesReadSoFar == 512) {
            this.current.bytesReadSoFar = 0;
          }
        }
      }
      return true;
    } catch (final IOException e) {
      this.serverConnection.close();
      this.serverConnection = null;
      //noinspection NonAtomicOperationOnVolatileField
      ++this.failureCount;
      this.errorCode = ErrorCode.PROTOCOL_ERROR;
      return this.queueSize() == 0;
    }
  }

  private void handshake1() {
    if (this.serverConnection != null) {
      try {
        this.c2sPacket.pos = 0;
        this.c2sPacket.writeByte(6);
        this.c2sPacket.writeI24(3);
        this.c2sPacket.writeShort(0);
        this.serverConnection.write(this.c2sPacket.data, this.c2sPacket.data.length);
      } catch (final IOException var5) {
        try {
          this.serverConnection.close();
        } catch (final Exception e) {}

        //noinspection NonAtomicOperationOnVolatileField
        ++this.failureCount;
        this.serverConnection = null;
        this.errorCode = ErrorCode.PROTOCOL_ERROR;
      }
    }
  }

  private void handshake2() {
    if (this.serverConnection != null) {
      try {
        this.c2sPacket.pos = 0;
        this.c2sPacket.writeByte(3);
        this.c2sPacket.writeI40(0L);
        this.serverConnection.write(this.c2sPacket.data, this.c2sPacket.data.length);
      } catch (final IOException var6) {
        try {
          this.serverConnection.close();
        } catch (final Exception e) {}

        //noinspection NonAtomicOperationOnVolatileField
        ++this.failureCount;
        this.errorCode = ErrorCode.PROTOCOL_ERROR;
        this.serverConnection = null;
      }
    }
  }
}
