package funorb.shatteredplans.client.ui;

import funorb.graphics.Drawing;
import funorb.shatteredplans.client.JagexApplet;
import funorb.util.MathUtil;

public final class ScrollBar extends UIComponent<Object> {
  private final int enabledColor;
  private final int disabledColor;
  private int scrollPosition = 5;
  public ScrollBarListener listener;

  @SuppressWarnings("SameParameterValue")
  public ScrollBar(final int x, final int y, final int width, final int height) {
    super(x, y, width, height);
    this.enabledColor = 0x3ca4a7;
    this.disabledColor = Drawing.alphaOver(Drawing.BLACK, this.enabledColor, 128);
  }

  @Override
  @SuppressWarnings("unused")
  public void handleClick(final int x, final int y) {
    if (this.visible) {
      final int scrollPosition = MathUtil.clamp(y, 5, this.height - 5);
      if (scrollPosition != this.scrollPosition) {
        this.scrollPosition = scrollPosition;
        this.listener.setScrollPosition((this.scrollPosition - 2) * ScrollBarListener.MAX_SCROLL_POSITION / (this.height - 10));
      }
    }
  }

  @Override
  public void a183(int var2, final int var3) {
    if (var2 < 10) {
      var2 = 10;
    }

    this.scrollPosition = 5;
    this.height = var2;
  }

  @Override
  public void draw() {
    if (this.visible) {
      Drawing.verticalLine(this.x + 5, this.y, this.height, this.enabled ? this.enabledColor : this.disabledColor);
      if (this.enabled) {
        Drawing.fillCircle(this.x + 5, this.scrollPosition + this.y, 5, this.enabledColor);
      }
    }
  }

  @Override
  public void handleDrag(final int mouseX, final int mouseY, final int originX, final int originY) {
    if (this.visible) {
      int var4 = JagexApplet.mouseY - this.y;
      if (var4 < 5) {
        var4 = 5;
      }

      if (var4 > this.height - 5) {
        var4 = this.height - 5;
      }

      if (this.scrollPosition != var4) {
        this.scrollPosition = var4;
        int var5 = (65536 * this.scrollPosition - 327680) / (this.height - 10);
        if (var5 > 65536) {
          var5 = 65536;
        }

        if (var5 < 0) {
          var5 = 0;
        }

        this.listener.setScrollPosition(var5);
      }

    }
  }
}
