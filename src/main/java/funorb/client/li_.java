package funorb.client;

import java.io.IOException;
import java.net.Socket;

public abstract class li_ {
  protected int _a;
  protected String _f;

  protected final Socket a693() throws IOException {
    return new Socket(this._f, this._a);
  }

  public abstract Socket b693() throws IOException;
}
