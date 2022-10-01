package funorb.client.lobby;

import funorb.awt.MouseState;

public final class TabbedPlayerList extends Component<Component<?>> {
  private final Component<?>[] _xb;
  private final Component<?>[] _Db;
  private final Component<Component<?>> _yb;
  private int _zb;

  public TabbedPlayerList(final Component<?> var3, final String[] var4, final Component<?> var5, final Component<?>[] var6) {
    super(null);
    this._Db = new Component[var4.length];
    this._yb = new Component<>(var5);
    this._xb = var6;

    int var8;
    for (var8 = 0; var4.length > var8; ++var8) {
      final Component<?> var9 = new Component<>(var3);
      var9.label = var4[var8];
      this._Db[var8] = var9;
      this.addChild(var9);
    }

    this.addChild(this._yb);

    for (var8 = 0; var8 < var6.length; ++var8) {
      this._yb.addChild(var6[var8]);
    }

    this._zb = 0;
    this._Db[0].selected = true;
  }

  public void updateBounds(final int var1, final int var3) {
    this.height = var3;
    this.x = 0;
    this.width = var1;
    this.y = 0;
    this.updateChildBounds();
  }

  public void f487() {
    for (int i = 0; i < this._Db.length; ++i) {
      if (this._zb != i && this._Db[i].clickButton != MouseState.Button.NONE) {
        this._Db[this._zb].selected = false;
        this._xb[this._zb].x += 10000;
        this._zb = i;
        this._Db[i].selected = true;
        this._xb[i].x -= 10000;
      }
    }
  }

  private void updateChildBounds() {
    for (int var4 = 0; var4 < this._Db.length; ++var4) {
      final int var5 = this.width * var4 / this._Db.length;
      final int var6 = (1 + var4) * this.width / this._Db.length;
      this._Db[var4].x = var5;
      this._Db[var4].y = 0;
      this._Db[var4].width = var6 - var5;
      this._Db[var4].height = 24;
    }

    this._yb.setBounds(0, 24, this.width, this.height - 24);

    for (int var4 = 0; var4 < this._xb.length; ++var4) {
      this._xb[var4].setBounds(5, 5, -10 + this._yb.width, this._yb.height - 10);
      if (this._zb != var4) {
        final Component<?> var10000 = this._xb[var4];
        var10000.x += 10000;
      }
    }
  }
}
