package funorb.commonui;

import funorb.awt.KeyState;

public class FormNavigationPage extends FramedNavigationPage {
  private final int _jb;
  private final int _db;
  private final int _mb;
  private final int _X;
  private Enum4 _ab;
  private Component _ib;
  private int _Z;
  private cg_ _eb;

  protected FormNavigationPage(final NavigationRoot var1, final Component var2, final int var3, final int var4, final int var5) {
    super(var1, 12 + var2.width, var2.height + var3 + 12);
    this._db = var3;
    this._mb = var5;
    this._X = this._jb = var4;
    this.a876(var2);
  }

  private void a876(final Component var2) {
    if (this._eb != null) {
      this.removeChild(this._eb);
    }

    if (var2 == null) {
      this._eb = new cg_();
    } else {
      var2.setBounds(6, 6 + this._db, var2.width, var2.height);
      this._eb = new cg_(var2);
    }

    this.addChild(this._eb);
    this._ib = null;
  }

  @Override
  public boolean a686(final int keyCode, final char keyChar, final Component var4) {
    if (super.a686(keyCode, keyChar, var4)) {
      return true;
    } else {
      if (this._eb != null) {
        if (keyCode == KeyState.Code.UP) {
          this._eb.focus(var4);
        }

        if (keyCode == KeyState.Code.DOWN) {
          this._eb.focus(var4);
        }
      }

      return false;
    }
  }

  @Override
  public final void g423() {
    if (this._ab != Enum4.C2) {
      this._Z = 0;
      this._ab = Enum4.C3;
      this.a876(this._ib);
      this._eb._J = 0;
      this._ib = null;
    }
  }

  @Override
  public void tick2() {
    if (this._ab != null) {
      if (this._ab == Enum4.C2) {
        if (this._X == ++this._Z) {
          this._ab = Enum4.C1;
          this.b115(this._mb, 12 + this._ib.width, this._ib.height + this._db + 12);
          this._Z = 0;
          this._eb._J = 0;
        } else {
          this._eb._J = -((this._Z << 8) / this._X) + 256;
        }
      } else if (this._ab == Enum4.C3) {
        if (++this._Z == this._jb) {
          this._eb._J = 256;
          this._ab = null;
        } else {
          this._eb._J = (this._Z << 8) / this._jb;
        }
      }
    }

    super.tick2();
  }

  public final void b952(final Component var1) {
    this._ib = var1;
    if (this._ab == Enum4.C1) {
      this.b115(this._mb, this._ib.width + 12, this._ib.height + this._db + 12);
      this._Z = 0;
    } else if (this._ab != Enum4.C2) {
      this._ab = Enum4.C2;
      this._Z = 0;
    }
  }

  @Override
  protected final void n150() {
    if (this._ab != null) {
      if (this._ab != Enum4.C3) {
        this.b599(12 + this._db + this._ib.height, 12 + this._ib.width);
        this.a876(this._ib);
      }

      this._eb._J = 256;
      this._ab = null;
    }

    super.n150();
  }

  @Override
  public final boolean k154() {
    this.n150();
    return super.k154();
  }

  private enum Enum4 {
    C1, C2, C3
  }
}
