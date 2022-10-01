package funorb.client.lobby;

import funorb.graphics.Font;
import funorb.graphics.Sprite;

public final class ActionButton extends Component<Component<?>> {
  public Component<?> _Bb;
  private Component<?> _Db;

  public ActionButton(final Component<?> attrsSrc, final Component<?> tooltipAttrsSrc, final Sprite var6, final String tooltipLabel) {
    super(attrsSrc);
    if (var6 != null) {
      this._Db = new Component<>(null);
      this._Db.sprite = var6;
      this.addChild(this._Db);
    }

    if (tooltipLabel != null) {
      this._Bb = new Component<>(tooltipAttrsSrc, tooltipLabel);
      this.addChild(this._Bb);
    }

    this.recursivelySet_H();
  }

  private void a326(final int var2, int var3) {
    int var4 = -var2;

    if (this._Db != null) {
      var4 = this._Db.e474();
    }

    int var5 = -var2;
    if (this._Bb != null) {
      var5 = this._Bb.a353(-var3 + (-var2 - var3) + (this.width - var4));
    }

    int var6 = var3 + var4 + var3 - (-var2 - var5);
    if (this.width < var6) {
      var5 += this.width - var6;
      var6 = this.width;
    }

    if (this.textAlignment == Font.HorizontalAlignment.CENTER) {
      var3 += (this.width - var6) / 2;
    }

    if (this.textAlignment == Font.HorizontalAlignment.RIGHT) {
      var3 += this.width - var6;
    }

    if (this._Db != null) {
      this._Db.setBounds(var3, 0, var4, this.height);
      this._Db.verticalAlignment = this.verticalAlignment;
    }

    if (this._Bb != null) {
      this._Bb.setBounds(var3 + var4 + var2, 0, var5, this.height);
      if (this._Db == null) {
        this._Bb.textAlignment = this.textAlignment;
      } else {
        this._Bb.textAlignment = Font.HorizontalAlignment.LEFT;
      }

      this._Bb.verticalAlignment = this.verticalAlignment;
    }

  }

  public void a370(final int var2, final int var3, final int var4, final int var5, final int var6, final int var7) {
    this.setBounds(var7, var3, var6, var2);
    this.a326(var5, var4);
  }

  public int a776(final int var1, final int var3) {
    int var4 = -var1;
    int var5 = -var1;
    if (this._Db != null) {
      var4 = this._Db.e474();
    }

    if (this._Bb != null) {
      var5 = this._Bb.e474();
    }

    return var5 + var4 + var3 + var1 + var3;
  }
}
