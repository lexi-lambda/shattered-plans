package funorb.commonui;

import funorb.graphics.Font;
import org.jetbrains.annotations.NotNull;

public final class TextLayout extends AbstractTextLayout {
  private boolean needsRelayout = false;
  private boolean isSingleLine;
  private Font font;
  private String text;
  private @NotNull Font.HorizontalAlignment horizontalAlignment = Font.HorizontalAlignment.LEFT;
  private @NotNull Font.VerticalAlignment verticalAlignment = Font.VerticalAlignment.TOP;
  private int availableWidth;
  private int availableHeight;
  private int leading;

  public void invalidate() {
    this.needsRelayout = true;
  }

  public void layoutLineAlignedLeft(final Font font, final String text, final int baseline) {
    if (text == null) {
      this.lineMetrics = null;
    } else if (this.needsRelayout || this.font != font || !this.isSingleLine || this.horizontalAlignment != Font.HorizontalAlignment.LEFT || this.text == null || !this.text.equals(text)) {
      this.font = font;
      this.text = text;
      this.horizontalAlignment = Font.HorizontalAlignment.LEFT;
      final TextLineMetrics var6 = this.initializeSingleLineLayout(font, text, baseline);
      var6.charXs[0] = 0;
      var6.charXs[text.length()] = font.measureLineWidth(text);
      var6.a137ta(font, text, 0);
    }
    this.needsRelayout = false;
  }

  public void layoutLineAlignedRight(final int var2, final Font font, final String text, final int var5) {
    if (text == null) {
      this.lineMetrics = null;
    } else if (this.needsRelayout || font != this.font || !this.isSingleLine || this.horizontalAlignment != Font.HorizontalAlignment.RIGHT || this.text == null || !this.text.equals(text)) {
      this.font = font;
      this.horizontalAlignment = Font.HorizontalAlignment.RIGHT;
      this.text = text;
      final TextLineMetrics var6 = this.initializeSingleLineLayout(font, text, var2);
      var6.charXs[0] = var5 - font.measureLineWidth(text);
      var6.charXs[text.length()] = var5;
      var6.a137ta(font, text, 0);
    }
    this.needsRelayout = false;
  }

  public void layoutLineCentered(final int var1, final String text, final Font font, final int var5) {
    if (text == null) {
      this.lineMetrics = null;
    } else if (this.needsRelayout || this.font != font || !this.isSingleLine || this.horizontalAlignment != Font.HorizontalAlignment.CENTER || this.text == null || !this.text.equals(text)) {
      this.horizontalAlignment = Font.HorizontalAlignment.CENTER;
      this.font = font;
      final TextLineMetrics var6 = this.initializeSingleLineLayout(font, text, var1);
      final int var7 = font.measureLineWidth(text);
      var6.charXs[0] = var5 - (var7 / 2);
      var6.charXs[text.length()] = var5 + (var7 >> 1);
      var6.a137ta(font, text, 0);
    }
    this.needsRelayout = false;
  }

  private TextLineMetrics initializeSingleLineLayout(final Font font, final String text, final int baseline) {
    this.isSingleLine = true;
    final TextLineMetrics var5 = new TextLineMetrics(baseline - font.ascent, baseline + font.descent, text.length());
    this.lineMetrics = new TextLineMetrics[]{var5};
    return var5;
  }

  public void layoutParagraph(final Font font,
                              final String text,
                              final @NotNull Font.HorizontalAlignment horizontalAlignment,
                              final @NotNull Font.VerticalAlignment verticalAlignment,
                              final int availableWidth,
                              final int availableHeight,
                              int leading) {
    if (leading == 0) {
      leading = font.baseline;
    }

    if (text == null) {
      this.lineMetrics = null;
    } else if (this.needsRelayout
        || this.isSingleLine
        || this.font != font
        || this.horizontalAlignment != horizontalAlignment
        || this.verticalAlignment != verticalAlignment
        || this.leading != leading
        || this.availableHeight != availableHeight
        || this.availableWidth != availableWidth
        || this.text == null || !this.text.equals(text)) {
      this.isSingleLine = false;
      this.font = font;
      this.verticalAlignment = verticalAlignment;
      this.availableWidth = availableWidth;
      this.horizontalAlignment = horizontalAlignment;
      this.text = text;
      this.availableHeight = availableHeight;
      this.leading = leading;
      final String[] lines = new String[font.breakLines(text, availableWidth) + 1];
      final int lineCount = Math.max(1, font.breakLines(text, new int[]{availableWidth}, lines));
      if (this.verticalAlignment == Font.VerticalAlignment.DISTRIBUTE && lineCount == 1) {
        this.verticalAlignment = Font.VerticalAlignment.MIDDLE;
      }

      int baseline;
      if (this.verticalAlignment == Font.VerticalAlignment.TOP) {
        baseline = font.ascent;
      } else if (this.verticalAlignment == Font.VerticalAlignment.MIDDLE) {
        baseline = font.ascent + (this.availableHeight - lineCount * this.leading >> 1);
      } else if (this.verticalAlignment == Font.VerticalAlignment.BOTTOM) {
        baseline = this.availableHeight - font.descent - this.leading * lineCount;
      } else {
        final int var12 = Math.max((this.availableHeight - this.leading * lineCount) / (lineCount + 1), 0);
        baseline = var12 + font.ascent;
        this.leading += var12;
      }

      this.lineMetrics = new TextLineMetrics[lineCount];
      for (int i = 0; i < lineCount; ++i) {
        final String var13 = lines[i];
        final TextLineMetrics var14 = new TextLineMetrics(baseline - font.ascent, baseline + font.descent, var13 == null ? 0 : var13.length());
        var14.charXs[0] = 0;
        if (var13 != null) {
          var14.charXs[var13.length()] = font.measureLineWidth(var13);
          var14.a137ta(font, var13, horizontalAlignment != Font.HorizontalAlignment.JUSTIFY ? 0 : this.a947(availableWidth, font.measureLineWidth(var13), var13));
        }

        this.lineMetrics[i] = var14;
        baseline += leading;
      }
    }

    this.needsRelayout = false;
  }
}
