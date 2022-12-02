package funorb.commonui;

import funorb.commonui.form.validator.InputValidator;
import funorb.commonui.form.validator.ValidationState;
import funorb.graphics.Drawing;
import funorb.graphics.Sprite;

public final class hl_ extends Button {
  private static Sprite _gge;
  private final InputValidator _G;
  private int _F;

  public hl_(final InputValidator var1) {
    this._G = var1;
  }

  @Override
  public String getCurrentTooltip() {
    return this.isMouseOver ? this._G.a983() : null;
  }

  @Override
  public boolean focus(final Component focusRoot) {
    return false;
  }

  @Override
  public void tick(final int x, final int y, final Component root) {
    ++this._F;
    super.tick(x, y, root);
  }

  @Override
  public void draw(final int x, final int y) {
    super.draw(x, y);
    final int var5 = (this.width >> 1) + x + this.x;
    final int var6 = (this.height >> 1) + y + this.y;
    final ValidationState var8 = this._G.validate();
    final Sprite var7;
    if (var8 == ValidationState.CHECKING_2 || var8 == ValidationState.CHECKING_1) {
      var7 = Resources.VALIDATION[0];
      final int var9 = var7.offsetX << 1;
      final int var10 = var7.offsetY << 1;
      if (_gge != null && var9 <= _gge.width && _gge.height >= var10) {
        _gge.withInstalledForDrawingUsingOffsets(() -> {
          Drawing.clear();
          var7.b669(112, 144, var7.offsetX << 4, var7.offsetY << 4, -this._F << 10, 4096);
        });
      } else {
        _gge = new Sprite(var9, var10);
        _gge.withInstalledForDrawingUsingOffsets(() ->
            var7.b669(112, 144, var7.offsetX << 4, var7.offsetY << 4, -this._F << 10, 4096));
      }
      _gge.drawAdd(var5 - var7.offsetX, -var7.offsetY + var6, 256);
    } else if (var8 == ValidationState.INVALID) {
      var7 = Resources.VALIDATION[2];
      var7.drawAdd(-(var7.width >> 1) + var5, var6 - (var7.height >> 1), 256);
    } else if (var8 == ValidationState.C2) {
      var7 = Resources.VALIDATION[1];
      var7.drawAdd(-(var7.width >> 1) + var5, -(var7.height >> 1) + var6, 256);
    }
  }
}
