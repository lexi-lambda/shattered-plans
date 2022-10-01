package funorb.shatteredplans.client.ui;

import funorb.graphics.Drawing;
import funorb.graphics.Font;
import funorb.shatteredplans.client.Menu;
import funorb.shatteredplans.client.ShatteredPlansClient;

public final class MultilineLabel extends Label {
  private Font.HorizontalAlignment horizontalAlignment;

  @SuppressWarnings("SameParameterValue")
  public MultilineLabel(final int x, final int y, final int width, final int height, final Font.HorizontalAlignment horizontalAlignment) {
    super(x, y, width, height, null);
    this.horizontalAlignment = horizontalAlignment;
  }

  @Override
  public void draw() {
    if (this.visible) {
      if (this.text != null) {
        Menu.SMALL_FONT.drawParagraph(this.text, this.x, this.y, this.width, ShatteredPlansClient.SCREEN_HEIGHT, Drawing.WHITE, this.horizontalAlignment, Font.VerticalAlignment.TOP, Menu.SMALL_FONT.ascent);
      }
    }
  }

  public void setTextAndLeftAlign(final String text) {
    this.text = text;
    this.horizontalAlignment = Font.HorizontalAlignment.LEFT;
  }
}
