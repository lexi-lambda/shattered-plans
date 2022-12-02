package funorb.commonui.renderer;

import funorb.commonui.Button;
import funorb.commonui.Component;
import funorb.commonui.Resources;
import funorb.graphics.Font;
import org.jetbrains.annotations.NotNull;

public final class LinkRenderer implements ComponentRenderer {
  private final Font font;
  private final @NotNull Font.HorizontalAlignment horizontalAlignment;
  private final @NotNull Font.VerticalAlignment verticalAlignment;

  public LinkRenderer() {
    this.font = Resources.AREZZO_14;
    this.horizontalAlignment = Font.HorizontalAlignment.CENTER;
    this.verticalAlignment = Font.VerticalAlignment.MIDDLE;
  }

  public LinkRenderer(final Font var1) {
    this.font = var1;
    this.horizontalAlignment = Font.HorizontalAlignment.LEFT;
    this.verticalAlignment = Font.VerticalAlignment.MIDDLE;
  }

  @Override
  public void draw(final Component component, final int x, final int y, final boolean enabled) {
    final int var6 = !component.isMouseOver && !component.hasFocus() ? 2188450 : 3249872;
    this.font.drawParagraph("<u=" + Integer.toString(var6, 16) + ">" + component.text + "</u>", x + component.x, y + component.y, component.width, component.height, var6, this.horizontalAlignment, this.verticalAlignment, this.font.descent + this.font.ascent);
    if (component.hasFocus()) {
      final int var7 = this.font.measureLineWidth(component.text);
      final int var8 = this.font.ascent + this.font.descent;
      int var9 = component.x + x;
      if (this.horizontalAlignment == Font.HorizontalAlignment.RIGHT) {
        var9 += -var7 + component.width;
      } else if (this.horizontalAlignment == Font.HorizontalAlignment.CENTER) {
        var9 += -var7 + component.width >> 1;
      }

      int var10 = component.y + y;
      if (this.verticalAlignment == Font.VerticalAlignment.BOTTOM) {
        var10 += component.height - var8;
      } else if (this.verticalAlignment == Font.VerticalAlignment.MIDDLE) {
        var10 += -var8 + component.height >> 1;
      }

      Button.drawFocusRect(var9 - 2, var10 + 2, var7 + 4, var8);
    }

  }
}
