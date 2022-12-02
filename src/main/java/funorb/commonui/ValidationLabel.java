package funorb.commonui;

import funorb.commonui.form.validator.InputValidator;
import funorb.commonui.form.validator.ValidationState;
import funorb.commonui.renderer.ITextRenderer;
import funorb.commonui.renderer.TextRenderer;
import funorb.graphics.Drawing;
import funorb.graphics.Font;
import funorb.graphics.Sprite;
import funorb.shatteredplans.StringConstants;

public final class ValidationLabel extends Label {
  private static TextRenderer globalRenderer;

  private static TextRenderer getTextRenderer() {
    if (globalRenderer == null) {
      globalRenderer = new TextRenderer(Resources.AREZZO_12, 20, 0, 0, 0, 0xb0b0b0, Font.HorizontalAlignment.LEFT, Font.VerticalAlignment.TOP, Resources.AREZZO_12.ascent, true);
    }
    return globalRenderer;
  }

  private final InputValidator validator;
  private final String initialText;
  private Sprite _V;
  private int _N;

  @SuppressWarnings("SameParameterValue")
  public ValidationLabel(final InputValidator validator, final String text, final int x, final int y, final int width, final int height) {
    super(text, getTextRenderer());
    this.validator = validator;
    this.initialText = text;
    this.setBounds(x, y, width, height);
  }

  @Override
  public void tick(final int x, final int y, final Component root) {
    ++this._N;
    super.tick(x, y, root);
  }

  @Override
  public void draw(final int x, final int y) {
    final ValidationState var6 = this.validator.validate();
    String var5;
    if (var6 == ValidationState.CHECKING_2 || var6 == ValidationState.CHECKING_1) {
      var5 = StringConstants.CHECKING;
    } else {
      var5 = this.validator.getTooltip();
      if (var5 == null) {
        var5 = this.initialText;
      }
    }

    if (!var5.equals(this.text)) {
      this.text = var5;
      this.recalculateHotspots();
    }

    super.draw(x, y);
    final ValidationState c226 = this.validator.validate();
    final ITextRenderer var8 = (ITextRenderer) this.renderer;
    final int var9 = this.x + x;
    final int var10 = var8.a754(y, this) + (var8.updateLayout(this).b137() >> 1);
    final Sprite var7;
    if (c226 == ValidationState.CHECKING_2 || c226 == ValidationState.CHECKING_1) {
      var7 = Resources.VALIDATION[0];
      final int var11 = var7.offsetX << 1;
      final int var12 = var7.offsetY << 1;
      if (this._V != null && this._V.width >= var11 && this._V.height >= var12) {
        this._V.withInstalledForDrawingUsingOffsets(() -> {
          Drawing.clear();
          var7.b669(112, 144, var7.offsetX << 4, var7.offsetY << 4, -this._N << 10, 4096);
        });
      } else {
        this._V = new Sprite(var11, var12);
        this._V.withInstalledForDrawingUsingOffsets(() ->
            var7.b669(112, 144, var7.offsetX << 4, var7.offsetY << 4, -this._N << 10, 4096));
      }
      this._V.drawAdd(var9 - (var7.offsetX >> 1), -var7.offsetY + var10, 256);
    } else if (c226 == ValidationState.INVALID) {
      var7 = Resources.VALIDATION[2];
      var7.drawAdd(var9, var10 - (var7.height >> 1), 256);
    } else if (c226 == ValidationState.C2) {
      var7 = Resources.VALIDATION[1];
      var7.drawAdd(var9, -(var7.height >> 1) + var10, 256);
    }

  }

  @Override
  public String getCurrentTooltip() {
    return null;
  }
}
