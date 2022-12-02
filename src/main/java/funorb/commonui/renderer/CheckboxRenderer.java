package funorb.commonui.renderer;

import funorb.commonui.Button;
import funorb.commonui.Component;
import funorb.commonui.Resources;
import funorb.graphics.Sprite;

public final class CheckboxRenderer implements ComponentRenderer {
  @Override
  public void draw(final Component component, final int x, final int y, final boolean enabled) {
    final int x1 = x + component.x;
    final int y1 = y + component.y;
    ComponentRenderer.drawGradientOutline(x1, y1, component.width, component.height);
    final Sprite var8 = Resources.VALIDATION[1];
    if (component instanceof Button && ((Button) component).active) {
      var8.drawAdd(1 + x1 + (component.width - var8.offsetX >> 1), (-var8.offsetY + component.height >> 1) + 1 + y1, 256);
    }

    if (component.hasFocus()) {
      Button.drawFocusRect(x1 + 2, y1 + 2, component.width - 4, component.height - 4);
    }
  }
}
