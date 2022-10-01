package funorb.shatteredplans.client.ui;

import funorb.graphics.Drawing;
import funorb.shatteredplans.client.GameUI;

public final class ChatMessage {
  private final String text;
  private final int width;
  private int age = 0;
  private int alpha = 256;

  public ChatMessage(final String text) {
    this.text = text;
    this.width = Math.max(GameUI._ssb.measureLineWidth(this.text) + 20, 400);
  }

  public void tick() {
    if (this.age >= 600) {
      this.alpha = 0;
    } else {
      ++this.age;
      if (this.age > 500) {
        final int var2 = 100 - this.age + 500;
        this.alpha = (var2 << 8) / 100;
      }
    }
  }

  public void draw(final int y) {
    if (this.alpha != 0) {
      Drawing.fillRoundedRect(88, y + 1, this.width, 17, 6, Drawing.BLACK, this.alpha);

      if (this.alpha == 256) {
        GameUI._ssb.draw(this.text, 100, y + 14, Drawing.WHITE);
      } else {
        GameUI._ssb.draw(this.text, 100, y + 14, Drawing.WHITE, this.alpha);
      }
    }
  }
}
