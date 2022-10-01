package funorb.commonui.renderer;

import funorb.commonui.Component;
import funorb.graphics.Drawing;

public interface ComponentRenderer {
  static void drawGradientOutline(final int x, final int y, final int width, final int height) {
    Drawing.horizontalLine(x, y, width + 1, 0x989898);
    Drawing.horizontalLine(x, y + height, width + 1, 0xb8b8b8);
    final int y1 = Math.max(1, Drawing.top - y);
    final int y2 = Math.min(height, Drawing.bottom - y);
    for (int i = y1; i < y2; ++i) {
      final int gray = ((i * 0x30) / height) + 0x98;
      final int color = Drawing.gray(gray);
      final int n = Drawing.width * (i + y) + x;
      Drawing.screenBuffer[n] = color;
      Drawing.screenBuffer[n + width] = color;
    }
  }

  void draw(Component component, int x, int y, boolean enabled);
}
