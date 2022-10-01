package funorb.graphics;

import funorb.Strings;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public abstract class Font {
  /**
   * Set by {@link #breakLines(String, int)}.
   */
  private static final String[] BROKEN_LINES = new String[100];

  private static int globalShadowColor = -1;
  private static int spaceWidth = 0;
  private static int spaceWidthError = 0;
  private static int underlineColor = -1;
  private static int strikethroughColor = -1;
  private static int globalAlpha = 256;
  private static int localAlpha = 256;
  private static int localShadowColor = -1;
  private static int localColor = 0;
  private static int globalColor = 0;

  public int baseline = 0;
  public final int ascent;
  public final int descent;
  public final int capHeight;
  private int[] advanceWidths;
  private byte[] kerns;

  private final int[] glyphOriginsX;
  private final int[] glyphOriginsY;
  private final int[] inkWidths;
  private final int[] inkHeights;

  private Symbol[] symbols;
  private int[] symbolAscents;

  protected Font(final byte[] metricsData, final int[] glyphOriginsX, final int[] glyphOriginsY, final int[] inkWidths, final int[] inkHeights) {
    this.glyphOriginsX = glyphOriginsX;
    this.glyphOriginsY = glyphOriginsY;
    this.inkWidths = inkWidths;
    this.inkHeights = inkHeights;
    this.parseMetrics(metricsData);

    int inkBoundAbove = Integer.MAX_VALUE;
    int inkBoundBelow = Integer.MIN_VALUE;
    for (int i = 0; i < 256; ++i) {
      if (inkBoundAbove > this.glyphOriginsY[i] && this.inkHeights[i] != 0) {
        inkBoundAbove = this.glyphOriginsY[i];
      }

      if (inkBoundBelow < this.glyphOriginsY[i] + this.inkHeights[i]) {
        inkBoundBelow = this.glyphOriginsY[i] + this.inkHeights[i];
      }
    }

    this.ascent = this.baseline - inkBoundAbove;
    this.descent = inkBoundBelow - this.baseline;
    this.capHeight = this.baseline - this.glyphOriginsY[88];
  }

  public final void setSymbols(final Symbol[] symbols, final int[] ascents) {
    if (ascents == null || ascents.length == symbols.length) {
      this.symbols = symbols;
      this.symbolAscents = ascents;
    } else {
      throw new IllegalArgumentException();
    }
  }

  //<editor-fold desc="Tags">
  public static String escapeTags(final String text) {
    final int var1 = text.length();
    int var2 = 0;

    for (int var3 = 0; var3 < var1; ++var3) {
      final char var4 = text.charAt(var3);
      if (var4 == '<' || var4 == '>') {
        var2 += 3;
      }
    }

    final StringBuilder var6 = new StringBuilder(var1 + var2);

    for (int var7 = 0; var7 < var1; ++var7) {
      final char var5 = text.charAt(var7);
      if (var5 == '<') {
        var6.append("<lt>");
      } else if (var5 == '>') {
        var6.append("<gt>");
      } else {
        var6.append(var5);
      }
    }

    return var6.toString();
  }

  private static void parseAttrTag(final String str) {
    try {
      if (str.startsWith("col=")) {
        localColor = Strings.parseHexInteger(str.substring(4));
      } else if (str.equals("/col")) {
        localColor = globalColor;
      } else if (str.startsWith("trans=")) {
        localAlpha = Strings.parseDecimalInteger(str.substring(6));
      } else if (str.equals("/trans")) {
        localAlpha = globalAlpha;
      } else if (str.startsWith("str=")) {
        strikethroughColor = Strings.parseHexInteger(str.substring(4));
      } else if (str.equals("str")) {
        strikethroughColor = 0x800000;
      } else if (str.equals("/str")) {
        strikethroughColor = -1;
      } else if (str.startsWith("u=")) {
        underlineColor = Strings.parseHexInteger(str.substring(2));
      } else if (str.equals("u")) {
        underlineColor = 0;
      } else if (str.equals("/u")) {
        underlineColor = -1;
      } else if (str.startsWith("shad=")) {
        localShadowColor = Strings.parseHexInteger(str.substring(5));
      } else if (str.equals("shad")) {
        localShadowColor = 0;
      } else if (str.equals("/shad")) {
        localShadowColor = globalShadowColor;
      } else if (str.equals("br")) {
        setFormattingParams(globalColor, globalShadowColor, globalAlpha);
      }
    } catch (final Exception var3) {
    }
  }
  //</editor-fold>

  //<editor-fold desc="Measuring text">
  public final int getAdvanceWidth(final char c) {
    return this.advanceWidths[Strings.encode1252Char(c) & 255];
  }

  private int getKerning(final char glyph1, final char glyph2) {
    if (this.kerns == null || glyph1 == 0) {
      return 0;
    } else {
      return this.kerns[(glyph1 << 8) + glyph2];
    }
  }

  @Contract(pure = true)
  public final int measureLineWidth(final String str) {
    if (str == null) {
      return 0;
    }

    int openBracketPos = -1;
    char prevChar = 0;
    int width = 0;
    for (int i = 0; i < str.length(); ++i) {
      char c = str.charAt(i);
      if (c == '<') {
        openBracketPos = i;
      } else {
        if (c == '>' && openBracketPos != -1) {
          final String command = str.substring(openBracketPos + 1, i).toLowerCase();
          openBracketPos = -1;
          switch (command) {
            case "lt":
              c = '<';
              break;
            case "gt":
              break;
            case "nbsp":
              c = 160;
              break;
            case "shy":
              c = 173;
              break;
            case "times":
              c = 215;
              break;
            case "euro":
              c = 8364;
              break;
            case "copy":
              c = 169;
              break;
            case "reg":
              c = 174;
              break;
            default:
              if (command.startsWith("img=")) {
                try {
                  final int symbolId = Strings.parseDecimalInteger(command.substring(4));
                  width += this.symbols[symbolId].advanceX;
                  prevChar = 0;
                } catch (final Exception var10) {}
              }
          }
        }

        if (openBracketPos == -1) {
          c = (char) (Strings.encode1252Char(c) & 255);
          width += this.advanceWidths[c];
          if (this.kerns != null && prevChar != 0) {
            width += this.kerns[(prevChar << 8) + c];
          }

          prevChar = c;
        }
      }
    }

    return width;
  }

  public final int breakLines(final String text, final int lineWidth) {
    return this.breakLines(text, new int[]{lineWidth}, BROKEN_LINES);
  }

  /**
   * @return the number of lines
   */
  public final int breakLines(final String text, final int[] lineWidths, final String[] lines) {
    if (text == null) {
      return 0;
    }

    final StringBuilder sb = new StringBuilder();
    int width = 0;
    int lastBreakIndex = 0;
    int breakIndex = -1;
    int breakWidth = 0;
    byte breakSkip = 0;
    char lastGlyphIndex = 0;
    int lineIndex = 0;

    int tagStartIndex = -1;
    for (int i = 0; i < text.length(); ++i) {
      char glyphIndex = text.charAt(i);
      if (glyphIndex == '<') {
        tagStartIndex = i;
      } else {
        if (glyphIndex == '>' && tagStartIndex != -1) {
          final String tag = text.substring(tagStartIndex + 1, i).toLowerCase();
          tagStartIndex = -1;
          sb.append('<');
          sb.append(tag);
          sb.append('>');
          if (tag.equals("br")) {
            lines[lineIndex] = sb.substring(lastBreakIndex, sb.length());
            ++lineIndex;
            lastBreakIndex = sb.length();
            width = 0;
            breakIndex = -1;
            lastGlyphIndex = 0;
          } else if (tag.equals("lt")) {
            width += this.getAdvanceWidth('<');
            if (this.kerns != null && lastGlyphIndex != 0) {
              width += this.kerns[(lastGlyphIndex << 8) + 60];
            }

            lastGlyphIndex = '<';
          } else if (tag.equals("gt")) {
            width += this.getAdvanceWidth('>');
            if (this.kerns != null && lastGlyphIndex != 0) {
              width += this.kerns[(lastGlyphIndex << 8) + 62];
            }

            lastGlyphIndex = '>';
          } else if (tag.equals("nbsp")) {
            width += this.getAdvanceWidth(' ');
            if (this.kerns != null && lastGlyphIndex != 0) {
              width += this.kerns[(lastGlyphIndex << 8) + 160];
            }

            lastGlyphIndex = 160;
          } else if (tag.equals("shy")) {
            width += this.getAdvanceWidth('\u00ad');
            if (this.kerns != null && lastGlyphIndex != 0) {
              width += this.kerns[(lastGlyphIndex << 8) + 173];
            }

            lastGlyphIndex = 173;
          } else if (tag.equals("times")) {
            width += this.getAdvanceWidth('×');
            if (this.kerns != null && lastGlyphIndex != 0) {
              width += this.kerns[(lastGlyphIndex << 8) + 215];
            }

            lastGlyphIndex = 215;
          } else if (tag.equals("euro")) {
            width += this.getAdvanceWidth('€');
            if (this.kerns != null && lastGlyphIndex != 0) {
              width += this.kerns[(lastGlyphIndex << 8) + 128];
            }

            lastGlyphIndex = 8364;
          } else if (tag.equals("copy")) {
            width += this.getAdvanceWidth('©');
            if (this.kerns != null && lastGlyphIndex != 0) {
              width += this.kerns[(lastGlyphIndex << 8) + 169];
            }

            lastGlyphIndex = 169;
          } else if (tag.equals("reg")) {
            width += this.getAdvanceWidth('®');
            if (this.kerns != null && lastGlyphIndex != 0) {
              width += this.kerns[(lastGlyphIndex << 8) + 174];
            }

            lastGlyphIndex = 174;
          } else if (tag.startsWith("img=")) {
            try {
              final int var16 = Strings.parseDecimalInteger(tag.substring(4));
              width += this.symbols[var16].advanceX;
              lastGlyphIndex = 0;
            } catch (final Exception var17) {
            }
          }
          glyphIndex = 0;
        }

        if (tagStartIndex == -1) {
          if (glyphIndex != 0) {
            sb.append(glyphIndex);
            glyphIndex = (char) (Strings.encode1252Char(glyphIndex) & 255);
            width += this.advanceWidths[glyphIndex];
            if (this.kerns != null && lastGlyphIndex != 0) {
              width += this.kerns[(lastGlyphIndex << 8) + glyphIndex];
            }

            lastGlyphIndex = glyphIndex;
          }

          if (glyphIndex == ' ') {
            breakIndex = sb.length();
            breakWidth = width;
            breakSkip = 1;
          }

          if (lineWidths != null) {
            final int widthIndex = lineIndex < lineWidths.length ? lineIndex : lineWidths.length - 1;
            if (width > lineWidths[widthIndex] && breakIndex >= 0) {
              lines[lineIndex] = sb.substring(lastBreakIndex, breakIndex - breakSkip);
              ++lineIndex;
              lastBreakIndex = breakIndex;
              breakIndex = -1;
              width -= breakWidth;
              lastGlyphIndex = 0;
            }
          }

          if (glyphIndex == '-') {
            breakIndex = sb.length();
            breakWidth = width;
            breakSkip = 0;
          }
        }
      }
    }

    if (sb.length() > lastBreakIndex) {
      lines[lineIndex] = sb.substring(lastBreakIndex, sb.length());
      ++lineIndex;
    }

    return lineIndex;
  }

  public final int measureParagraphWidth(final String text, final int targetLineWidth) {
    final int lineCount = this.breakLines(text, new int[]{targetLineWidth}, BROKEN_LINES);

    int width = 0;
    for (int i = 0; i < lineCount; ++i) {
      final int lineWidth = this.measureLineWidth(BROKEN_LINES[i]);
      if (lineWidth > width) {
        width = lineWidth;
      }
    }

    return width;
  }

  public final int measureParagraphHeight(final String text, final int lineWidth, int leading) {
    if (leading == 0) {
      leading = this.baseline;
    }
    final int lineCount = this.breakLines(text, new int[]{lineWidth}, BROKEN_LINES);
    return this.ascent + this.descent + leading * (lineCount - 1);
  }

  public final String truncateWithEllipsisToFit(final String text, final int width) {
    if (this.measureLineWidth(text) <= width) {
      return text;
    } else {
      final int ellipsisWidth = this.measureLineWidth("...");
      final int availableWidth = width - ellipsisWidth;
      int usedWidth = 0;

      for (int i = 0; i < text.length(); ++i) {
        final int charWidth = this.getAdvanceWidth(text.charAt(i));
        if (usedWidth + charWidth > availableWidth) {
          return text.substring(0, i - 1) + "...";
        }

        usedWidth += charWidth;
      }

      return null;
    }
  }

  private void calculateJustifiedSpaceWidth(final String str, final int targetWidth) {
    int spaceCount = 0;
    boolean inEscape = false;
    for (int i = 0; i < str.length(); ++i) {
      final char c = str.charAt(i);
      if (c == '<') {
        inEscape = true;
      } else if (c == '>') {
        inEscape = false;
      } else if (!inEscape && c == ' ') {
        ++spaceCount;
      }
    }

    if (spaceCount > 0) {
      spaceWidth = ((targetWidth - this.measureLineWidth(str)) << 8) / spaceCount;
    }
  }
  //</editor-fold>

  //<editor-fold desc="Drawing">
  public final void draw(final String text, final int x, final int y, final int color) {
    this.draw(text, x, y, color, Drawing.MAX_ALPHA);
  }

  public final void draw(final String text, final int x, final int y, final int color, final int alpha) {
    if (text != null) {
      setFormattingParams(color, alpha);
      this.executeDraw(text, x, y);
    }
  }

  public final void drawRightAligned(final String text, final int x, final int y, final int color) {
    this.drawRightAligned(text, x, y, color, Drawing.MAX_ALPHA);
  }

  public final void drawRightAligned(final String text, final int x, final int y, final int color, final int alpha) {
    if (text != null) {
      setFormattingParams(color, alpha);
      this.executeDraw(text, x - this.measureLineWidth(text), y);
    }
  }

  public final void drawCentered(final String text, final int x, final int y, final int color) {
    this.drawCentered(text, x, y, color, Drawing.MAX_ALPHA);
  }

  public final void drawCentered(final String text, final int x, final int y, final int color, final int alpha) {
    if (text != null) {
      setFormattingParams(color, alpha);
      this.executeDraw(text, x - this.measureLineWidth(text) / 2, y);
    }
  }

  public final void drawJustified(final String str, final int x, final int y, final int targetWidth) {
    if (str != null) {
      setFormattingParams(Drawing.WHITE);
      this.calculateJustifiedSpaceWidth(str, targetWidth);
      this.executeDraw(str, x, y);
    }
  }

  @SuppressWarnings("SameParameterValue")
  public final void drawVertical(final String text, final int x, final int y, final int color) {
    if (text != null) {
      setFormattingParams(color);
      this.executeDrawVertical(text, x, y);
    }
  }

  public final int drawParagraph(final String text,
                                 final int x,
                                 final int y,
                                 final int width,
                                 final int height,
                                 final int color,
                                 final @NotNull HorizontalAlignment horizontalAlignment,
                                 final @NotNull VerticalAlignment verticalAlignment,
                                 final int leading) {
    return this.drawParagraph(text, x, y, width, height, color, 256, horizontalAlignment, verticalAlignment, leading);
  }

  public final int drawParagraph(final String text,
                                 final int x,
                                 final int y,
                                 final int width,
                                 final int height,
                                 final int color,
                                 final int alpha,
                                 final @NotNull HorizontalAlignment horizontalAlignment,
                                 @NotNull VerticalAlignment verticalAlignment,
                                 int leading) {
    if (text == null) {
      return 0;
    }

    setFormattingParams(color, alpha);
    if (leading == 0) {
      leading = this.baseline;
    }

    int[] var12 = new int[]{width};
    if (height < this.ascent + this.descent + leading && height < leading + leading) {
      var12 = null;
    }

    final int lineCount = this.breakLines(text, var12, BROKEN_LINES);
    if (verticalAlignment == VerticalAlignment.DISTRIBUTE && lineCount == 1) {
      verticalAlignment = VerticalAlignment.MIDDLE;
    }

    int y1;
    if (verticalAlignment == VerticalAlignment.TOP) {
      y1 = y + this.ascent;
    } else if (verticalAlignment == VerticalAlignment.MIDDLE) {
      y1 = y + this.ascent + (height - this.ascent - this.descent - (lineCount - 1) * leading) / 2;
    } else if (verticalAlignment == VerticalAlignment.BOTTOM) {
      y1 = y + height - this.descent - (lineCount - 1) * leading;
    } else {
      final int var15 = Math.max((height - this.ascent - this.descent - (lineCount - 1) * leading) / (lineCount + 1), 0);
      y1 = y + this.ascent + var15;
      leading += var15;
    }

    for (int i = 0; i < lineCount; ++i) {
      if (horizontalAlignment == HorizontalAlignment.LEFT) {
        this.executeDraw(BROKEN_LINES[i], x, y1);
      } else if (horizontalAlignment == HorizontalAlignment.CENTER) {
        this.executeDraw(BROKEN_LINES[i], x + (width - this.measureLineWidth(BROKEN_LINES[i])) / 2, y1);
      } else if (horizontalAlignment == HorizontalAlignment.RIGHT) {
        this.executeDraw(BROKEN_LINES[i], x + width - this.measureLineWidth(BROKEN_LINES[i]), y1);
      } else if (i == lineCount - 1) {
        this.executeDraw(BROKEN_LINES[i], x, y1);
      } else {
        this.calculateJustifiedSpaceWidth(BROKEN_LINES[i], width);
        this.executeDraw(BROKEN_LINES[i], x, y1);
        spaceWidth = 0;
      }

      y1 += leading;
    }

    return lineCount;
  }

  protected abstract void drawGlyph(int index, int x, int y, int var4, int var5, int color, boolean var7);
  protected abstract void drawGlyph(int index, int x, int y, int var4, int var5, int color, int alpha, boolean var8);
  protected abstract void drawVerticalGlyph(int index, int x, int y, int height, int width, int color, boolean var7);
  protected abstract void drawVerticalGlyph(int index, int x, int y, int height, int width, int color, int alpha, boolean var8);

  private static void setFormattingParams(final int color) {
    setFormattingParams(color, 256);
  }

  private static void setFormattingParams(final int color, final int alpha) {
    setFormattingParams(color, -1, alpha);
  }

  private static void setFormattingParams(final int color, final int shadowColor, final int alpha) {
    strikethroughColor = -1;
    underlineColor = -1;
    globalShadowColor = shadowColor;
    localShadowColor = shadowColor;
    globalColor = color;
    localColor = color;
    globalAlpha = alpha;
    localAlpha = alpha;
    spaceWidth = 0;
    spaceWidthError = 0;
  }

  /**
   * The {@link DrawExecutor} class extracts the common logic for drawing a
   * line of text in an arbitrary orientation.
   */
  private abstract class DrawExecutor {
    abstract protected void drawGlyph(int index, int x, int y, int width, int height, int color, boolean var7);
    abstract protected void drawGlyph(int index, int x, int y, int width, int height, int color, int alpha, boolean var8);
    abstract protected int drawSymbol(int index, int x);
    abstract protected void drawRule(int x, int y, int width, int color);

    @SuppressWarnings("WeakerAccess")
    public final void execute(final String text) {
      int x = 0;
      int escapeStartPos = -1;
      char lastGlyphIndex = 0;
      for (int i = 0; i < text.length(); ++i) {
        char glyphIndex = text.charAt(i);
        if (glyphIndex == '<') {
          escapeStartPos = i;
        } else {
          if (glyphIndex == '>' && escapeStartPos != -1) {
            final String tag = text.substring(escapeStartPos + 1, i).toLowerCase();
            escapeStartPos = -1;
            switch (tag) {
              case "lt":
                glyphIndex = '<';
                break;
              case "gt":
                break;
              case "nbsp":
                glyphIndex = 160;
                break;
              case "shy":
                glyphIndex = 173;
                break;
              case "times":
                glyphIndex = 215;
                break;
              case "euro":
                glyphIndex = 8364;
                break;
              case "copy":
                glyphIndex = 169;
                break;
              case "reg":
                glyphIndex = 174;
                break;
              default:
                if (tag.startsWith("img=")) {
                  x += this.drawSymbol(Strings.parseDecimalInteger(tag.substring(4)), x);
                  lastGlyphIndex = 0;
                } else {
                  parseAttrTag(tag);
                }
                continue;
            }
          }

          if (escapeStartPos == -1) {
            glyphIndex = (char) (Strings.encode1252Char(glyphIndex) & 255);
            x += Font.this.getKerning(lastGlyphIndex, glyphIndex);

            final int inkWidth = Font.this.inkWidths[glyphIndex];
            final int inkHeight = Font.this.inkHeights[glyphIndex];
            final int startX = x;
            if (glyphIndex != ' ') {
              final int originX = Font.this.glyphOriginsX[glyphIndex];
              final int originY = Font.this.glyphOriginsY[glyphIndex];
              if (localAlpha == 256) {
                if (localShadowColor != -1) {
                  this.drawGlyph(glyphIndex, x + originX + 1, originY + 1, inkWidth, inkHeight, localShadowColor, true);
                }
                this.drawGlyph(glyphIndex, x + originX, originY, inkWidth, inkHeight, localColor, false);
              } else {
                if (localShadowColor != -1) {
                  this.drawGlyph(glyphIndex, x + originX + 1, originY + 1, inkWidth, inkHeight, localShadowColor, localAlpha, true);
                }
                this.drawGlyph(glyphIndex, x + originX, originY, inkWidth, inkHeight, localColor, localAlpha, false);
              }
            } else if (spaceWidth > 0) {
              spaceWidthError += spaceWidth;
              x += spaceWidthError >> 8;
              spaceWidthError &= 255;
            }

            x += Font.this.advanceWidths[glyphIndex];
            if (strikethroughColor != -1) {
              this.drawRule(startX, (int) ((double) Font.this.baseline * 0.7D), x - startX, strikethroughColor);
            }
            if (underlineColor != -1) {
              this.drawRule(startX, Font.this.baseline + 1, x - startX, underlineColor);
            }

            lastGlyphIndex = glyphIndex;
          }
        }
      }
    }
  }

  private void executeDraw(final String text, final int x0, final int y0) {
    new DrawExecutor() {
      @Override
      protected void drawGlyph(final int index, final int x, final int y, final int width, final int height, final int color, final boolean var7) {
        Font.this.drawGlyph(index, x0 + x, y0 + y - Font.this.baseline, width, height, color, var7);
      }

      @Override
      protected void drawGlyph(final int index, final int x, final int y, final int width, final int height, final int color, final int alpha, final boolean var8) {
        Font.this.drawGlyph(index, x0 + x, y0 + y - Font.this.baseline, width, height, color, alpha, var8);
      }

      @Override
      protected int drawSymbol(final int index, final int x) {
        final Symbol symbol = Font.this.symbols[index];
        final int ascent = Font.this.symbolAscents != null ? Font.this.symbolAscents[index] : symbol.advanceY;
        if (localAlpha == 256) {
          symbol.draw(x0 + x, y0 - ascent);
        } else {
          symbol.draw(x0 + x, y0 - ascent, localAlpha);
        }
        return symbol.advanceX;
      }

      @Override
      protected void drawRule(final int x, final int y, final int width, final int color) {
        Drawing.horizontalLine(x0 + x, y0 + y - Font.this.baseline, width, color);
      }
    }.execute(text);
  }

  private void executeDrawVertical(final String text, final int x0, final int y0) {
    new DrawExecutor() {
      @Override
      protected void drawGlyph(final int index, final int x, final int y, final int width, final int height, final int color, final boolean var7) {
        Font.this.drawVerticalGlyph(index, x0 + y - Font.this.baseline, y0 - x - width, height, width, color, var7);
      }

      @Override
      protected void drawGlyph(final int index, final int x, final int y, final int width, final int height, final int color, final int alpha, final boolean var8) {
        Font.this.drawVerticalGlyph(index, x0 + y - Font.this.baseline, y0 - x - width, height, width, color, alpha, var8);
      }

      @Override
      protected int drawSymbol(final int index, final int x) {
        throw new UnsupportedOperationException("symbols are not supported in vertical drawing mode");
      }

      @Override
      protected void drawRule(final int x, final int y, final int width, final int color) {
        //noinspection SuspiciousNameCombination
        Drawing.verticalLine(x0 + y - Font.this.baseline, y0 + x, width, color);
      }
    }.execute(text);
  }
  //</editor-fold>

  //<editor-fold desc="Parsing font metrics">
  private void parseMetrics(final byte[] metricsData) {
    this.advanceWidths = new int[256];
    if (metricsData.length == 257) {
      for (int i = 0; i < this.advanceWidths.length; ++i) {
        this.advanceWidths[i] = metricsData[i] & 255;
      }
      this.baseline = metricsData[256] & 255;
    } else {
      int i = 0;

      for (int j = 0; j < 256; ++j) {
        this.advanceWidths[j] = metricsData[i++] & 255;
      }

      final int[] var10 = new int[256];
      for (int j = 0; j < 256; ++j) {
        var10[j] = metricsData[i++] & 255;
      }
      final int[] var4 = new int[256];
      for (int j = 0; j < 256; ++j) {
        var4[j] = metricsData[i++] & 255;
      }

      final byte[][] var11 = new byte[256][];
      for (int j = 0; j < 256; ++j) {
        var11[j] = new byte[var10[j]];
        byte var7 = 0;

        for (int k = 0; k < var11[j].length; ++k) {
          var7 += metricsData[i++];
          var11[j][k] = var7;
        }
      }

      final byte[][] var12 = new byte[256][];

      for (int j = 0; j < 256; ++j) {
        var12[j] = new byte[var10[j]];
        byte var14 = 0;

        for (int k = 0; k < var12[j].length; ++k) {
          var14 += metricsData[i++];
          var12[j][k] = var14;
        }
      }

      this.kerns = new byte[65536];

      for (int glyph1 = 0; glyph1 < 256; ++glyph1) {
        if (glyph1 != 32 && glyph1 != 160) {
          for (int glyph2 = 0; glyph2 < 256; ++glyph2) {
            if (glyph2 != 32 && glyph2 != 160) {
              this.kerns[(glyph1 << 8) + glyph2] = (byte) calculateKerning(var11, var12, var4, this.advanceWidths, var10, glyph1, glyph2);
            }
          }
        }
      }

      this.baseline = var4[32] + var10[32];
    }
  }

  private static int calculateKerning(final byte[][] var0, final byte[][] var1, final int[] var2, final int[] advanceWidths, final int[] var4, final int glyph1, final int glyph2) {
    final int var7 = var2[glyph1];
    final int var8 = var7 + var4[glyph1];
    final int var9 = var2[glyph2];
    final int var10 = var9 + var4[glyph2];
    final int var11 = Math.max(var9, var7);
    final int var12 = Math.min(var10, var8);

    int var13 = advanceWidths[glyph1];
    if (advanceWidths[glyph2] < var13) {
      var13 = advanceWidths[glyph2];
    }

    final byte[] var14 = var1[glyph1];
    final byte[] var15 = var0[glyph2];
    int var16 = var11 - var7;
    int var17 = var11 - var9;

    for (int var18 = var11; var18 < var12; ++var18) {
      final int var19 = var14[var16++] + var15[var17++];
      if (var19 < var13) {
        var13 = var19;
      }
    }

    return -var13;
  }
  //</editor-fold>

  public enum HorizontalAlignment {
    LEFT,
    CENTER,
    RIGHT,
    JUSTIFY,
  }

  public enum VerticalAlignment {
    TOP,
    MIDDLE,
    BOTTOM,
    DISTRIBUTE,
  }
}
