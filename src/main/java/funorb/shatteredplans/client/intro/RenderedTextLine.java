package funorb.shatteredplans.client.intro;

import funorb.graphics.Drawing;
import funorb.graphics.Font;
import funorb.graphics.Sprite;

public final class RenderedTextLine {
  public final Sprite _o;
  public final Sprite _i;
  public int _h;

  public RenderedTextLine(final String str, final Font font, final int var3) {
    int width = font.measureLineWidth(str);
    final int height = font.ascent + font.descent;
    final boolean var6 = width > 500;
    if (var6) {
      width = 600;
    }

    this._i = new Sprite(width, height);
    this._o = new Sprite(width + 6, height + 6);
    Drawing.saveContext();

    this._i.installForDrawing();
    if (var6) {
      font.drawJustified(str, 0, font.ascent, 600);
    } else {
      font.draw(str, 0, font.ascent, Drawing.WHITE);
    }

    this._o.installForDrawing();
    this._i.c093(3, 3);
    Drawing.b669(1, 1, width + 6, height + 6);

    for (int i = (width + 6) * (height + 6) - 1; i >= 0; --i) {
      if ((Drawing.screenBuffer[i] & 0x80) == 0) {
        Drawing.screenBuffer[i] <<= 1;
      }
    }
    Drawing.restoreContext();

    this._h = -var3;
  }
}
