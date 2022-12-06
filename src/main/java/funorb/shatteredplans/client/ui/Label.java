package funorb.shatteredplans.client.ui;

import funorb.graphics.Drawing;
import funorb.shatteredplans.client.Menu;
import funorb.shatteredplans.game.Force;

public class Label extends UIComponent<Force> {
  private final int color;
  public String text;

  public Label(final int x, final int y, final String text) {
    this(x, y, Menu.SMALL_FONT.measureLineWidth(text), Menu.SMALL_FONT.ascent, text);
  }

  public Label(final int x, final int y, final int width, final int height, final String text) {
    this(x, y, width, height, text, Drawing.WHITE);
  }

  public Label(final int x, final int y, final int width, final int height, final String text, final int color) {
    super(x, y, width, height);
    this.text = text;
    this.color = color;
  }

  protected int getColor() {
    return this.color;
  }

  @Override
  public void draw() {
    if (this.visible) {
      if (this.text != null) {
        Menu.SMALL_FONT.drawCentered(this.text, this.width / 2 + this.x, 3 * Menu.SMALL_FONT.ascent / 4 + this.y, this.getColor());
      }
    }
  }
}
