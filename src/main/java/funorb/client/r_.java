package funorb.client;

import funorb.Strings;
import funorb.io.Buffer;
import funorb.shatteredplans.client.MailboxMessage;
import funorb.shatteredplans.client.MessagePumpThread;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.net.URL;

public final class r_ implements Runnable {
  private final Buffer _k;
  private final MessagePumpThread _l;
  private final URL _e;
  private MailboxMessage _b;
  private DataInputStream _g;
  private int _h;
  private MailboxMessage _a;
  private MailboxMessage _i;

  public r_(final MessagePumpThread var1, final URL var2) {
    this._e = var2;
    this._l = var1;
    this._k = new Buffer(5000);
  }

  @Override
  protected void finalize() {
    if (this._b != null) {
      if (this._b.response != null) {
        try {
          ((DataInputStream) this._b.response).close();
        } catch (final Exception var4) {
        }
      }

      this._b = null;
    }

    if (this._i != null) {
      if (this._i.response != null) {
        try {
          ((Socket) this._i.response).close();
        } catch (final Exception var3) {
        }
      }

      this._i = null;
    }

    if (this._g != null) {
      try {
        this._g.close();
      } catch (final Exception var2) {
      }

      this._g = null;
    }

    this._a = null;
  }

  @Override
  public void run() {
    while (true) {
      try {
        if (this._k.data.length > this._k.pos) {
          final int var1 = this._g.read(this._k.data, this._k.pos, this._k.data.length - this._k.pos);
          if (var1 >= 0) {
            this._k.pos += var1;
            continue;
          }
        }

        if (this._k.pos == this._k.data.length) {
          throw new Exception("HG1: " + this._k.data.length + " " + this._e);
        }

        synchronized (this) {
          //noinspection FinalizeCalledExplicitly
          this.finalize();
          this._h = 3;
        }
      } catch (final Exception var6) {
        synchronized (this) {
          //noinspection FinalizeCalledExplicitly
          this.finalize();
          ++this._h;
        }
      }

      return;
    }
  }

  public synchronized boolean b154() {
    if (this._h < 2) {
      if (this._h == 0) {
        if (this._b == null) {
          this._b = this._l.sendOpenUrlStreamMessage(this._e);
        }

        if (this._b.status == MailboxMessage.Status.PENDING) {
          return false;
        }

        if (this._b.status != MailboxMessage.Status.SUCCESS) {
          ++this._h;
          this._b = null;
          return false;
        }
      }

      if (this._h == 1) {
        if (this._i == null) {
          this._i = this._l.sendOpenSocketMessage(this._e.getHost(), 443);
        }

        if (this._i.status == MailboxMessage.Status.PENDING) {
          return false;
        }

        if (this._i.status != MailboxMessage.Status.SUCCESS) {
          ++this._h;
          this._i = null;
          return false;
        }
      }

      if (this._g == null) {
        try {
          if (this._h == 0) {
            this._g = (DataInputStream) this._b.response;
          }

          if (this._h == 1) {
            final Socket var2 = (Socket) this._i.response;
            var2.setSoTimeout(10000);
            final OutputStream var3 = var2.getOutputStream();
            var3.write(17);
            var3.write(Strings.encode1252String("JAGGRAB " + this._e.getFile() + "\n\n"));
            this._g = new DataInputStream(var2.getInputStream());
          }

          this._k.pos = 0;
        } catch (final IOException var4) {
          //noinspection FinalizeCalledExplicitly
          this.finalize();
          ++this._h;
        }
      }

      if (this._a == null) {
        this._a = this._l.sendSpawnThreadMessage(this, 5);
      }

      if (this._a.status != MailboxMessage.Status.PENDING) {
        if (this._a.status != MailboxMessage.Status.SUCCESS) {
          //noinspection FinalizeCalledExplicitly
          this.finalize();
          ++this._h;
        }

      }
      return false;
    } else {
      return true;
    }
  }

}
