package funorb.commonui.renderer;

import funorb.commonui.Button;
import funorb.commonui.Component;
import funorb.commonui.Resources;
import funorb.graphics.Drawing;
import funorb.graphics.Font;
import funorb.graphics.Sprite;

public final class ButtonRenderer extends TextRenderer {
  public static Sprite[] SPRITES;

  private static final int _y = 0x2164a2;
  private static final int _u = 9543;
  private static final int _x = 0x2789f5;

  public ButtonRenderer() {
    this.font = Resources.AREZZO_14_BOLD;
  }

  @Override
  public void draw(final Component component, final int x, final int y, boolean enabled) {
    final boolean var6 = component.isMouseOver || component.hasFocus();
    if (component instanceof Button) {
      enabled &= ((Button) component).enabled;
    }

    final int var7 = !enabled ? _u : (!var6 ? _y : _x);
    final int var41 = component.x + x;
    if (SPRITES != null && component.width > 0) {
      final int var61 = SPRITES[0].offsetX;
      final int var71 = SPRITES[2].offsetX;
      final int var81 = SPRITES[1].offsetX;
      SPRITES[0].drawTinted(var41, y + component.y + (component.height - SPRITES[0].offsetY >> 1), var7);
      SPRITES[2].drawTinted(-var71 + var41 + component.width, y + component.y + (component.height - SPRITES[0].offsetY >> 1), var7);
      Drawing.withSavedBounds(() -> {
        Drawing.expandBoundsToInclude(var61 + var41, y + component.y + (component.height - SPRITES[0].offsetY >> 1), var41 + (component.width - var71), SPRITES[1].offsetY + y + component.y + (component.height - SPRITES[0].offsetY >> 1));
        final int var9 = var41 + var61;
        final int var10 = component.width + var41 - var71;

        for (int var42 = var9; var42 < var10; var42 += var81) {
          SPRITES[1].drawTinted(var42, y + component.y + (component.height - SPRITES[0].offsetY >> 1), var7);
        }
      });
    }
    final int var8 = !enabled ? 7105644 : Drawing.WHITE;
    this.font.drawParagraph(component.text, x + component.x, component.y - 2 + y, component.width, component.height, var8, Font.HorizontalAlignment.CENTER, Font.VerticalAlignment.MIDDLE, this.font.ascent);
  }
}
