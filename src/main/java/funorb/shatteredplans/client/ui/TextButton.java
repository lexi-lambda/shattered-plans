package funorb.shatteredplans.client.ui;

import funorb.graphics.Drawing;
import funorb.graphics.Sprite;
import funorb.shatteredplans.client.Menu;

public final class TextButton<T> extends UIComponent<T> {
  private final Sprite sprite;
  private final int backgroundColor;
  private String text;
  private int textColor;

  public TextButton(final int x, final int y, final int width, final int height, final int var5, final Sprite sprite, final String text, final int textColor) {
    super(x, y, width, height);
    this.backgroundColor = var5;
    this.sprite = sprite;
    this.text = text;
    this.textColor = textColor;
  }

  @Override
  public void draw() {
    if (this.visible) {
      if (this.backgroundColor != -1) {
        Drawing.fillRect(this.x, this.y, this.width, this.height, this.backgroundColor);
      }
      if (this.sprite != null) {
        this.sprite.draw((this.width - this.sprite.width) / 2 + this.x, (-this.sprite.height + this.height) / 2 + this.y);
      }
      if (this.text != null) {
        Menu.SMALL_FONT.drawCentered(this.text, this.width / 2 + this.x + 1, (Menu.SMALL_FONT.ascent + Menu.SMALL_FONT.descent) / 4 + this.y + this.height / 2, this.textColor);
      }
    }
  }

  public void setText(final String text, final int color) {
    this.text = text;
    this.textColor = color;
  }
}
