package funorb.io;

import funorb.shatteredplans.client.JagexApplet;
import funorb.shatteredplans.client.MailboxMessage;
import funorb.shatteredplans.client.MessagePumpThread;
import org.jetbrains.annotations.Range;

import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public final class DuplexStream implements Runnable {
  private final MessagePumpThread messagePumpThread;
  private final Socket socket;
  private final int bufferSize;
  private final OutputStream outputStream;
  private final InputStream inputStream;
  private int readPos;
  private MailboxMessage threadMessage;
  private boolean ioExceptionOccurred;
  private int writePos;
  private byte[] buffer;
  private boolean shutdownRequested;

  public DuplexStream(final Socket socket, final MessagePumpThread messagePumpThread) throws IOException {
    this.readPos = 0;
    this.writePos = 0;
    this.ioExceptionOccurred = false;
    this.shutdownRequested = false;
    this.messagePumpThread = messagePumpThread;
    this.socket = socket;
    this.socket.setSoTimeout(JagexApplet.DEBUG_MODE ? 0 : 30000);
    this.socket.setTcpNoDelay(true);
    this.inputStream = this.socket.getInputStream();
    this.outputStream = this.socket.getOutputStream();
    this.bufferSize = 5000;
  }

  public void write(final byte[] data, final int len) throws IOException {
    if (!this.shutdownRequested) {
      if (this.ioExceptionOccurred) {
        this.ioExceptionOccurred = false;
        throw new IOException();
      } else {
        if (this.buffer == null) {
          this.buffer = new byte[this.bufferSize];
        }

        synchronized (this) {
          for (int i = 0; i < len; ++i) {
            this.buffer[this.writePos] = data[i];
            this.writePos = (this.writePos + 1) % this.bufferSize;
            if (this.writePos == (this.bufferSize - 100 + this.readPos) % this.bufferSize) {
              throw new IOException();
            }
          }

          if (this.threadMessage == null) {
            this.threadMessage = this.messagePumpThread.sendSpawnThreadMessage(this, 3);
          }

          this.notifyAll();
        }
      }
    }
  }

  public void close() {
    if (!this.shutdownRequested) {
      synchronized (this) {
        this.shutdownRequested = true;
        this.notifyAll();
      }

      if (this.threadMessage != null) {
        if (this.threadMessage.busyAwait() == MailboxMessage.Status.SUCCESS) {
          try {
            ((Thread) this.threadMessage.response).join();
          } catch (final InterruptedException e) {}
        }
      }

      this.threadMessage = null;
    }
  }

  public int readByte() throws IOException {
    if (this.shutdownRequested) {
      return 0;
    } else {
      return this.inputStream.read();
    }
  }

  public void pollForException() throws IOException {
    if (!this.shutdownRequested) {
      if (this.ioExceptionOccurred) {
        this.ioExceptionOccurred = false;
        throw new IOException();
      }
    }
  }

  @Override
  protected void finalize() {
    this.close();
  }

  @Range(from = 0, to = Integer.MAX_VALUE)
  public int inputAvailable() throws IOException {
    if (this.shutdownRequested) {
      return 0;
    } else {
      return this.inputStream.available();
    }
  }

  @Override
  public void run() {
    while (true) {
      final int len;
      final int pos;
      synchronized (this) {
        if (this.writePos == this.readPos) {
          if (this.shutdownRequested) {
            break;
          }

          try {
            this.wait();
          } catch (final InterruptedException var8) {
          }
        }

        if (this.readPos <= this.writePos) {
          len = this.writePos - this.readPos;
        } else {
          len = this.bufferSize - this.readPos;
        }

        pos = this.readPos;
      }

      if (len > 0) {
        try {
          this.outputStream.write(this.buffer, pos, len);
        } catch (final IOException var7) {
          this.ioExceptionOccurred = true;
        }

        this.readPos = (len + this.readPos) % this.bufferSize;

        try {
          if (this.writePos == this.readPos) {
            this.outputStream.flush();
          }
        } catch (final IOException var6) {
          this.ioExceptionOccurred = true;
        }
      }
    }

    try {
      if (this.inputStream != null) {
        this.inputStream.close();
      }

      if (this.outputStream != null) {
        this.outputStream.close();
      }

      if (this.socket != null) {
        this.socket.close();
      }
    } catch (final IOException var9) {
    }

    this.buffer = null;
  }

  public void read(final byte[] out, int offset, int len) throws IOException {
    if (!this.shutdownRequested) {
      while (len > 0) {
        final int bytesRead = this.inputStream.read(out, offset, len);
        if (bytesRead <= 0) {
          throw new EOFException();
        }

        offset += bytesRead;
        len -= bytesRead;
      }
    }
  }
}
