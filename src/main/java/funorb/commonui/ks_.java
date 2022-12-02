package funorb.commonui;

public final class ks_ {
  private final String label;
  private boolean _d = false;
  private boolean _g = false;

  public ks_(final String label) {
    this.label = label;
  }

  public boolean b154() {
    return this._g;
  }

  public void a540(final boolean var1) {
    this._g = true;
    this._d = var1;
  }

  public boolean a491() {
    return this._d;
  }

  public String getLabel() {
    return this.label;
  }
}
