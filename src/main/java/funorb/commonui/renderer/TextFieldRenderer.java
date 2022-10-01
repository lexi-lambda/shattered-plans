package funorb.commonui.renderer;

import funorb.commonui.Component;
import funorb.commonui.Resources;
import funorb.graphics.Font;

public class TextFieldRenderer extends TextRenderer {
  protected TextFieldRenderer(final Font font) {
    super(font, 4, 2, 2, 2, 10000536, Font.HorizontalAlignment.LEFT, Font.VerticalAlignment.MIDDLE, font.ascent, false);
  }

  public TextFieldRenderer() {
    this(Resources.AREZZO_14);
  }

  @Override
  public final void draw(final Component component, final int x, final int y, final boolean enabled) {
    if (enabled) {
      ComponentRenderer.drawGradientOutline(x + component.x, component.y + y, component.width, component.height);
    }
    super.draw(component, x, y, enabled);
  }
}
