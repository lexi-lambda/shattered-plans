package funorb.commonui;

import funorb.graphics.Font;

public final class TextLineMetrics {
  public final int top;
  public final int bottom;
  public final int[] charXs;
  
  public TextLineMetrics(final int top, final int bottom, final int charCount) {
    this.top = top;
    this.bottom = bottom;
    this.charXs = new int[charCount + 1];
  }

  public void a137ta(final Font font, final String text, final int spaceWidth) {
    int totalSpaceWidth = 0;
    int var6 = -1;

    for (int i = 1; i < text.length(); ++i) {
      final char c = text.charAt(i);
      if (c == '<') {
        var6 = (totalSpaceWidth >> 8) + this.charXs[0] + font.measureLineWidth(text.substring(0, i));
      }

      if (var6 == -1) {
        if (c == ' ') {
          totalSpaceWidth += spaceWidth;
        }

        this.charXs[i] = (totalSpaceWidth >> 8) + this.charXs[0] + font.measureLineWidth(text.substring(0, i + 1)) - font.getAdvanceWidth(c);
      } else {
        this.charXs[i] = var6;
      }

      if (c == '>') {
        var6 = -1;
      }
    }
  }

  public int getCharCount() {
    return this.charXs.length - 1;
  }

  public int a527(final int var2) {
    for (int i = 1; i < this.charXs.length; ++i) {
      if (var2 < this.charXs[i] + this.charXs[i - 1] >> 1) {
        return i - 1;
      }
    }
    return this.getCharCount();
  }

  public int getWidth() {
    return this.charXs[this.getCharCount()];
  }
}
