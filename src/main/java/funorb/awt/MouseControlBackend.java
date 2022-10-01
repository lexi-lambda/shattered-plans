package funorb.awt;

import java.awt.Component;
import java.awt.Robot;
import java.awt.image.BufferedImage;

public final class MouseControlBackend {
  private final Robot robot = new Robot();
  private Component cursorComponent;

  public MouseControlBackend() throws Exception {
  }

  public void movemouse(final int var1, final int var2) {
    this.robot.mouseMove(var1, var2);
  }

  public void showcursor(Component var1, final boolean var2) {
    if (var2) {
      var1 = null;
    } else if (var1 == null) {
      throw new NullPointerException();
    }

    if (this.cursorComponent != var1) {
      if (this.cursorComponent != null) {
        this.cursorComponent.setCursor(null);
        this.cursorComponent = null;
      }

      if (var1 != null) {
        var1.setCursor(var1.getToolkit().createCustomCursor(new BufferedImage(1, 1, 2), new java.awt.Point(0, 0), null));
        this.cursorComponent = var1;
      }

    }
  }

  public void setcustomcursor(final Component var1, final int[] var2, final int var3, final int var4, final java.awt.Point var5) {
    if (var2 == null) {
      var1.setCursor(null);
    } else {
      final BufferedImage var6 = new BufferedImage(var3, var4, 2);
      var6.setRGB(0, 0, var3, var4, var2, 0, var3);
      var1.setCursor(var1.getToolkit().createCustomCursor(var6, var5, null));
    }
  }
}
