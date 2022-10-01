package funorb.commonui;

import funorb.commonui.container.WrapperContainer;
import funorb.graphics.Sprite;

public final class cg_ extends WrapperContainer {
  public int _J;

  public cg_() {
    super(0, 0, 0, 0);
    this._J = 256;
  }

  public cg_(final Component var1) {
    super(var1.x, var1.y, var1.width, var1.height);
    var1.setBounds(0, 0, this.width, this.height);
    this.child = var1;
    this._J = 256;
  }

  @Override
  public void draw(final int x, final int y) {
    if (this.child != null) {
      if (this._J != 0) {
        if (this._J == 256) {
          this.child.draw(x + this.x, y + this.y);
        } else {
          final Sprite var5 = new Sprite(this.child.width, this.child.height);
          var5.withInstalledForDrawingUsingOffsets(() -> this.child.draw(0, 0));
          var5.draw(x + this.x, y + this.y, this._J);
        }
      }
    }
  }
}
