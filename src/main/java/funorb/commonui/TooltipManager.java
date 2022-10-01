package funorb.commonui;

import funorb.graphics.Drawing;
import funorb.graphics.Font;
import funorb.shatteredplans.client.JagexApplet;

import java.util.Arrays;

public final class TooltipManager {
  public static TooltipManager INSTANCE;

  public static void initialize(final Font font) {
    if (INSTANCE == null) {
      INSTANCE = new TooltipManager(font);
    }
  }

  private static final int SHOW_MIN_TICKS = 50;
  private static final int SHOW_MAX_TICKS = 300;

  private static final int OUTLINE_COLOR = 0x757575;
  private static final int BACKGROUND_COLOR = 0x2a2a2a;
  private static final int TEXT_COLOR = 0xeeeeee;
  private static final int PADDING_RIGHT = 5;
  private static final int PADDING_LEFT = 6;
  private static final int PADDING_TOP = 4;
  private static final int LEADING = 14;

  private final Font font;
  private final String[] tooltipLines = new String[16];

  private int mouseX = -1;
  private int mouseY = -1;
  private int tooltipX = -1;
  private int tooltipY = -1;
  private int tooltipTimer = 0;
  private boolean hideTooltip = false;
  private String currentTooltip = null;

  private TooltipManager(final Font font) {
    this.font = font;
  }

  public void setMousePosition(final int x, final int y) {
    this.mouseX = x;
    this.mouseY = y;
  }

  public void reset() {
    this.currentTooltip = null;
    this.hideTooltip = false;
    this.tooltipY = -1;
    this.tooltipX = -1;
    this.tooltipTimer = 0;
  }

  public void tick(final String tooltip) {
    if (this.mouseX == -1 && this.mouseY == -1) {
      this.setMousePosition(JagexApplet.mouseX, JagexApplet.mouseY);
    }

    ++this.tooltipTimer;
    if (this.currentTooltip == null) {
      final boolean timeToShowTooltip = this.isTimeToShowTooltip();
      if (tooltip == null || (!this.hideTooltip && !timeToShowTooltip)) {
        this.tooltipTimer = 0;
      } else {
        this.tooltipTimer = SHOW_MIN_TICKS;
      }
      if (tooltip != null) {
        this.hideTooltip = false;
      } else if (!this.hideTooltip && timeToShowTooltip) {
        this.hideTooltip = true;
      }
      this.tooltipY = this.mouseY;
      this.tooltipX = this.mouseX;
    }

    this.currentTooltip = tooltip;
    if (!this.hideTooltip && this.tooltipTimer < SHOW_MIN_TICKS && JagexApplet.mouseEventReceived) {
      this.tooltipTimer = 0;
      this.tooltipY = this.mouseY;
      this.tooltipX = this.mouseX;
    }

    this.setMousePosition(-1, -1);
    if (this.hideTooltip && this.tooltipTimer == 10) {
      this.hideTooltip = false;
      this.tooltipTimer = 0;
    }
  }

  public void maybeDrawTooltip() {
    final String tooltip = (!this.hideTooltip && this.isTimeToShowTooltip()) ? this.currentTooltip : null;
    if (tooltip != null) {
      this.drawTooltip(this.tooltipX, this.tooltipY, tooltip);
    }
  }

  private boolean isTimeToShowTooltip() {
    return this.tooltipTimer >= SHOW_MIN_TICKS && this.tooltipTimer < SHOW_MAX_TICKS;
  }

  private void drawTooltip(final int x, final int y, final String text) {
    final int quarterWidth = Drawing.width / 4;
    int textWidth = this.font.measureLineWidth(text);
    int textHeight = this.font.ascent + this.font.descent;
    if (quarterWidth < textWidth || text.contains("<br>")) {

      final int maxWidth;
      if (textWidth > quarterWidth) {
        final int var13 = textWidth / quarterWidth;
        maxWidth = quarterWidth + 2 * ((var13 + textWidth % quarterWidth - 1) / var13);
      } else {
        maxWidth = quarterWidth;
      }

      final int lineCount = this.font.breakLines(text, new int[]{maxWidth}, this.tooltipLines);
      textHeight += LEADING * (lineCount - 1);
      textWidth = Arrays.stream(this.tooltipLines).mapToInt(this.font::measureLineWidth).max().orElseThrow();
    }

    final int padding = PADDING_LEFT + PADDING_RIGHT;
    final int x1 = Math.min(x, Drawing.width - padding - textWidth);
    int y1 = y - this.font.capHeight + 32;
    if (y1 > Drawing.height - textHeight - PADDING_TOP) {
      y1 = y - textHeight - PADDING_TOP;
    }

    Drawing.strokeRectangle(x1, y1, textWidth + padding, textHeight + PADDING_TOP, OUTLINE_COLOR);
    Drawing.fillRect(x1 + 1, y1 + 1, textWidth + padding - 2, textHeight + PADDING_TOP - 2, BACKGROUND_COLOR);
    this.font.drawParagraph(text, x1 + PADDING_LEFT, y1, textWidth, textHeight, TEXT_COLOR, Font.HorizontalAlignment.LEFT, Font.VerticalAlignment.TOP, LEADING);
  }
}
