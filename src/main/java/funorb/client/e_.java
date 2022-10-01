package funorb.client;

public final class e_ {
  private String _c;
  private boolean _a;

  public e_(final String var1) {
    this(var1, false);
  }

  public e_(final String var1, final boolean var2) {
    this._c = var1;
    this._a = var2;
    if (this._c == null) {
      this._c = "";
    }

    if (this._c.length() == 0) {
      this._a = false;
    }

  }

  public String a983() {
    return this._c;
  }

  public boolean a154() {
    return this._a;
  }
}
