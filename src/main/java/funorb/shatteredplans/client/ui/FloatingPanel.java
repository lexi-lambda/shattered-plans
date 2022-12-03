package funorb.shatteredplans.client.ui;

import funorb.graphics.Drawing;
import funorb.shatteredplans.client.Menu;
import funorb.shatteredplans.client.ShatteredPlansClient;

import java.util.ListIterator;

public final class FloatingPanel<T extends PanelState> extends UIComponent<Object> {
  private final String title;
  public T state;
  public boolean flashing;
  public ScrollView<?> content;

  public FloatingPanel(final int x, final int y, final int width, final int height, final String title) {
    super(x, y, width, height);
    this.title = title;
    this.a183(height, width);
    this.enabled = true;
    this.visible = false;
    this.flashing = false;
  }

  public static void a669am(final int var0, final int var1, final int var3, final int var4, final int var5) {
    final int var6 = var3 / 10 + 1;
    final int var7 = 96 / var6;
    a676kfl(96, var0, 96 - var7, var4, var5, false, var1);
    int var8 = 96;

    for (int var9 = 1; var9 < var6; ++var9) {
      var8 -= var7;
      a676kfl(var8, var0, -var7 + var8, var4, var9 * 10 + var5, true, var1);
    }
  }

  private static void a676kfl(final int var0, final int var2, final int var3, final int var4, final int var5, final boolean var6, final int var7) {
    int var8;
    int var9;
    for (var9 = 7; var2 > var9; var9 += 16) {
      var8 = var6 ? 4 : 6;
      if (1 + var9 + var8 >= var2) {
        var8 = var2 - var9 - 1;
      }

      Drawing.horizontalLine((!var6 ? 0 : 1) + var4 + var9, var5 + 1, var8, var7, var0 * (var2 - var9 * var9 / var2) / var2);
    }

    for (var9 = 4; var9 < var2; var9 += 16) {
      Drawing.line(var4 + var9, var5 + 6, var4 + var9 + 3, var5 + 1, var7, var0 * (var2 - var9 * var9 / var2) / var2);
      Drawing.line(3 + var4 + var9, 11 + var5, var9 + var4, 6 + var5, var7, (var3 + var0) * (-(var9 * var9 / var2) + var2) / 2 / var2);
    }

    for (var9 = 12; var2 > var9; var9 += 16) {
      Drawing.line(3 + var4 + var9, 6 + var5, var4 + var9, 1 + var5, var7, var0 * (-(var9 * var9 / var2) + var2) / var2);
      Drawing.line(var4 + var9, var5 + 11, var4 + var9 + 3, var5 + 6, var7, (var0 + var3) * (-(var9 * var9 / var2) + var2) / 2 / var2);
    }

    Drawing.h669(1 + var4, var5 + 6, var4 + 3, var5 + 6, var7, (var0 + var3) / 2);

    for (var9 = 16; var9 < var2; var9 += 16) {
      var8 = 4;
      if (var2 <= 1 + var8 + var9) {
        var8 = var2 - var9 - 1;
      }

      Drawing.horizontalLine(var9 + var4, 6 + var5, var8, var7, (var0 + var3) * (var2 - var9 * var9 / var2) / 2 / var2);
    }

  }

  @Override
  public void draw() {
    if (this.visible) {
      int var3 = 2052949;
      int var4 = 1125164;
      int var5 = 3974311;
      int var6;
      if (this.flashing) {
        var6 = ShatteredPlansClient.currentTick % 64;
        if (var6 > 24 && var6 <= 40) {
          var6 = 24;
        }

        if (var6 > 40) {
          var6 = 64 - var6;
        }

        var6 *= 10;
        var3 = Drawing.alphaOver(12993090, 2052949, var6);
        var4 = Drawing.alphaOver(12993090, var4, var6);
        var5 = Drawing.alphaOver(12993090, var5, var6);
      }

      Drawing.fillRoundedRect(this.x, this.y, this.width, this.height, 10, 0, 200);
      a669am(this.width - 10, var5, 15, this.x, this.y);
      Drawing.horizontalLine(10 + this.x, this.y, this.width - 20, var3);
      Drawing.horizontalLine(10 + this.x, this.height + this.y, this.width - 20, 0);
      Drawing.setBounds(this.x, this.y, this.x + 10, this.y + 10);
      Drawing.strokeCircle(this.x + 10, this.y + 10, 10, var3);
      Drawing.setBounds(this.x + this.width - 10, this.y, this.width + this.x, 10 + this.y);
      Drawing.strokeCircle(this.width - 1 + (this.x - 10), this.y + 10, 10, var3);
      Drawing.setBounds(this.x, this.y - 10 + this.height, this.x + 10, this.y + this.height);
      Drawing.strokeCircle(10 + this.x, this.y + this.height - 10 - 1, 10, 0);
      Drawing.setBounds(this.width + this.x - 10, this.y + this.height - 10, this.width + this.x, this.y + this.height);
      Drawing.strokeCircle(this.x + this.width - 11, this.height + (this.y - 10 - 1), 10, 0);
      Drawing.a797();

      int var7;
      for (var6 = 0; var6 < this.height - 20; ++var6) {
        var7 = Drawing.alphaOver(0, var3, var6 * 256 / (this.height - 20));
        Drawing.setPixel(this.x, var6 + this.y + 10, var7);
        Drawing.setPixel(this.width + (this.x - 1), this.y + 10 + var6, var7);
      }

      Drawing.fillRoundedRect(3 + this.x, 15 + this.y, this.width - 6, this.height - 15 - 3, 10, 0);
      Drawing.horizontalLine(3 + 10 + this.x, 15 + this.y, this.width - 20 - 6, var3);
      Drawing.horizontalLine(10 + this.x + 3, this.y - 3 - (-this.height + 1), this.width - 20 - 6, var4);
      Drawing.setBounds(this.x + 3, this.y + 15, 3 + this.x + 10, this.y + 25);
      Drawing.strokeCircle(13 + this.x, 25 + this.y, 10, var3);
      Drawing.setBounds(this.width - 3 + (this.x - 10), 15 + this.y, this.x - 3 + this.width, this.y + 15 + 10);
      Drawing.strokeCircle(this.width + (this.x - 14), this.y + 25, 10, var3);
      Drawing.setBounds(3 + this.x, this.height + this.y - 13, this.x + 10 + 3, this.y + this.height - 3);
      Drawing.strokeCircle(3 + this.x + 10, this.y - 10 + this.height - 4, 10, var4);
      Drawing.setBounds(this.width + this.x - 13, this.height + this.y - 13, this.width - 3 + this.x, this.y - (-this.height + 3));
      Drawing.strokeCircle(this.width - 10 + (this.x - 4), this.y - 3 + this.height - 10 - 1, 10, var4);
      Drawing.a797();

      for (var6 = 0; 2 * (this.height - 38) / 3 > var6; ++var6) {
        var7 = Drawing.alphaOver(0, var3, 256 * var6 / ((2 * this.height - 76) / 3));
        Drawing.setPixel(this.x + 3, this.y + 25 + var6, var7);
        Drawing.setPixel(this.x - 1 - (-this.width + 3), var6 + this.y + 15 + 10, var7);
      }

      var6 = (this.height * 2 - 76) / 3;

      int var8;
      for (var7 = 0; -var6 + this.height - 38 > var7; ++var7) {
        var8 = Drawing.alphaOver(var4, 0, 256 * var7 / (this.height - 38 - var6));
        Drawing.setPixel(this.x + 3, var6 + var7 + 15 + this.y + 10, var8);
        Drawing.setPixel(this.x + this.width - 3 - 1, 25 + this.y - (-var7 - var6), var8);
      }

      Menu.SHINE_LEFT.drawAdd(4 + this.x, this.y + 3, 256);
      var7 = Menu.SHINE_LEFT.width + 1 + this.x + 3;
      var8 = -Menu.SHINE_RIGHT.width - 3 + this.width + this.x;
      Menu.SHINE_RIGHT.drawAdd(var8, 3 + this.y, 64);

      for (int var10 = var7; var10 < var8; ++var10) {
        final int var9 = 64 + 192 * (-var10 + var8) / (var8 - var7);
        Menu.SHINE_MID.drawAdd(var10, this.y + 3, var9);
      }

      Menu.SMALL_FONT.drawCentered(this.title, this.x + this.width / 2, this.y + 12, Drawing.WHITE);

      for (final ListIterator<UIComponent<?>> it = this.children.listIterator(this.children.size()); it.hasPrevious(); ) {
        final UIComponent<?> var11 = it.previous();
        var11.draw();
      }
    }
  }

  @Override
  public UIComponent<?> findMouseTarget(final int x, final int y) {
    if (this.visible) {
      final UIComponent<?> var4 = UIComponent.findMouseTarget(this.children, x, y);
      return var4 == null ? super.findMouseTarget(x, y) : var4;
    } else {
      return null;
    }
  }

  @Override
  public void a183(int var2, int var3) {
    if (var2 < 39) {
      var2 = 39;
    }

    if (var3 < 27) {
      var3 = 27;
    }

    if (var3 < 40 + Menu.SMALL_FONT.measureLineWidth(this.title + "X")) {
      var3 = Menu.SMALL_FONT.measureLineWidth(this.title + "X") + 40;
    }

    this.width = var3;
    this.height = var2;
  }

  @Override
  public void handleDrag(final int mouseX, final int mouseY, final int originX, final int originY) {
    final int newX = mouseX + originX;
    final int newY = mouseY + originY;

    int dx = newX - this.x;
    if (this.x + dx < -this.width / 2) {
      dx = -this.x - (this.width / 2);
    }
    if (ShatteredPlansClient.SCREEN_WIDTH - (this.width / 2) < dx + this.x) {
      dx = (ShatteredPlansClient.SCREEN_WIDTH - this.x) - (this.width / 2);
    }

    int dy = newY - this.y;
    if (this.y + dy < 0) {
      dy = -this.y;
    }
    if (this.y + dy > ShatteredPlansClient.SCREEN_HEIGHT - 15) {
      dy = 465 - this.y;
    }

    this.translate(dx, dy);
  }
}
