package funorb.commonui.renderer;

import funorb.commonui.AbstractTextLayout;
import funorb.commonui.Component;
import funorb.commonui.TextLayout;
import funorb.commonui.TextLineMetrics;
import funorb.graphics.Drawing;
import funorb.graphics.Font;
import org.jetbrains.annotations.NotNull;

public class TextRenderer implements ITextRenderer {
  protected Font font;
  private final int _p;
  private final int _o;
  private final int _b;
  private final int _g;
  private final int _f;
  private final int _e;
  private final @NotNull Font.HorizontalAlignment horizontalAlignment;
  private final @NotNull Font.VerticalAlignment verticalAlignment;
  private final int leading;
  private final boolean isMultiline;

  protected TextRenderer() {
    this(null, 0, 0, 0, 0, 0, Font.HorizontalAlignment.LEFT, Font.VerticalAlignment.TOP, 0, false);
  }

  public TextRenderer(final Font font,
                      final int _o,
                      final int _g,
                      final int _b,
                      final int _f,
                      final int _p,
                      final @NotNull Font.HorizontalAlignment horizontalAlignment,
                      final @NotNull Font.VerticalAlignment verticalAlignment,
                      final int leading,
                      final boolean isMultiline) {
    this.font = font;
    this._o = _o;
    this._g = _g;
    this._b = _b;
    this._f = _f;
    this._p = _p;
    this.horizontalAlignment = horizontalAlignment;
    this.verticalAlignment = verticalAlignment;
    this.leading = leading;
    this.isMultiline = isMultiline;
    this._e = Integer.MAX_VALUE;
  }

  @Override
  public final int getPreferredWidth(final Component component) {
    this.updateLayout(component);
    return component.textLayout.getWidth() - (this._g + this._o);
  }

  @Override
  public final void a403(final int var1, final int var2, final int var4, final Component var5) {
    if (var5.hasFocus()) {
      final AbstractTextLayout var6 = this.updateLayout(var5);
      final int var7 = var6.a543(var1);
      final TextLineMetrics var8 = var6.lineMetrics[var7];
      final int var9 = var6.a527(var1);
      final int var10 = this.b896(var5, var9, var2);
      final int var11 = this.a754(var4, var5) + Math.max(0, var8.top);
      final int var12 = this.a754(var4, var5) + Math.min(this.getAvailableHeight(var5), Math.min(var8.bottom, var6.lineMetrics.length > 1 + var7 ? var6.lineMetrics[1 + var7].top : var8.bottom));
      Drawing.withLocalContext(() -> {
        Drawing.expandBoundsToInclude(var2 + var5.x, var5.y + var4, var5.width + var2 + var5.x, var5.height + var5.y + var4);
        Drawing.line(var10, var11, var10, var12, -1);
      });
    }
  }

  @Override
  public final AbstractTextLayout updateLayout(final Component component) {
    if (component.textLayout == null) {
      component.textLayout = new TextLayout();
    }
    if (this.isMultiline) {
      component.textLayout.layoutParagraph(
          this.font,
          this.getText(component),
          this.horizontalAlignment,
          this.verticalAlignment,
          this.getAvailableWidth(component),
          this.getAvailableHeight(component),
          this.leading);
    } else {
      this.updateLayoutSingleLine(component);
    }
    return component.textLayout;
  }

  @Override
  public final int a242(final int var1, final int var2, final int var3, final Component var4, final int var6) {
    this.updateLayout(var4);
    return var4.textLayout.a313(-this.b754(var4, var1) + var2, var6 - this.a754(var3, var4));
  }

  private int a896(final Component var2, final int var4) {
    return var2._l + this._b + var2.y + var4;
  }

  @Override
  public final void a132(final int var1, final int var2, final int var3, final int var4, final Component var6) {
    if (var1 != var4) {
      if (var6.hasFocus()) {
        final AbstractTextLayout var7 = this.updateLayout(var6);
        final int var8;
        final int var9;
        if (var1 >= var4) {
          var9 = var1;
          var8 = var4;
        } else {
          var8 = var1;
          var9 = var4;
        }

        final int var10 = var7.a543(var8);
        final int var11 = var7.a543(var9);
        Drawing.withLocalContext(() -> {
          Drawing.expandBoundsToInclude(var3 + var6.x, var2 + var6.y, var3 + var6.x + var6.width, var2 + var6.y + var6.height);
          for (int var12 = var10; var11 >= var12; ++var12) {
            final TextLineMetrics var13 = var7.lineMetrics[var12];
            assert var13 != null;
            final int var14 = var12 == var10 ? var7.a527(var8) : var13._b[0];
            final int var15 = var12 != var11 ? var13._b[var13.getCharCount()] : var7.a527(var9);
            Drawing.fillRect(this.b896(var6, var14, var3), var13.top + var2 + var6.y + this._b + var6._l, var15 - var14, var13.bottom, this._e, this._e >>> 24);
          }
        });
      }
    }
  }

  @Override
  public final int a754(final int var1, final Component var2) {
    return this.a896(var2, var1);
  }

  @Override
  public final int b754(final Component var2, final int var3) {
    return this.b896(var2, 0, var3);
  }

  protected String getText(final Component component) {
    return component.text;
  }

  @Override
  public final int getAvailableWidth(final Component component) {
    return component.width - this._o - this._g;
  }

  @Override
  public final int getPreferredHeight(final Component component) {
    this.updateLayout(component);
    return component.textLayout.b137() + this._b + this._f;
  }

  private void updateLayoutSingleLine(final Component component) {
    if (component.textLayout == null) {
      component.textLayout = new TextLayout();
    }

    final int var3 = this.getAvailableWidth(component);
    final int var4 = this.getAvailableHeight(component);
    final int var5;
    if (this.verticalAlignment == Font.VerticalAlignment.TOP) {
      var5 = this.font.ascent;
    } else if (this.verticalAlignment == Font.VerticalAlignment.BOTTOM) {
      var5 = var4 - this.font.descent;
    } else {
      var5 = ((var4 - this.font.ascent - this.font.descent) / 2) + this.font.ascent;
    }

    if (this.horizontalAlignment == Font.HorizontalAlignment.LEFT || this.horizontalAlignment == Font.HorizontalAlignment.JUSTIFY) {
      component.textLayout.layoutLineAlignedLeft(this.font, this.getText(component), var5);
    } else if (this.horizontalAlignment == Font.HorizontalAlignment.CENTER) {
      component.textLayout.layoutLineCentered(var5, this.getText(component), this.font, var3 / 2);
    } else if (this.horizontalAlignment == Font.HorizontalAlignment.RIGHT) {
      component.textLayout.layoutLineAlignedRight(var5, this.font, this.getText(component), var3);
    }
  }

  private void a768(final int var1, final int var2, final int var3, final Component var5) {
    Drawing.withLocalContext(() -> {
      Drawing.expandBoundsToInclude(var1 + var5.x, var5.y + var2, var5.x + var1 + var5.width, var5.y + var2 + var5.height);
      final int var9 = this.getAvailableWidth(var5);
      final int var10 = this.getAvailableHeight(var5);
      if (this.isMultiline) {
        this.font.drawParagraph(this.getText(var5), this.b896(var5, 0, var1), this.a896(var5, var2), var9, var10, var3, this.horizontalAlignment, this.verticalAlignment, this.leading);
      } else {
        final Font.VerticalAlignment var12 = this.verticalAlignment;
        final int var11;
        if (var12 == Font.VerticalAlignment.TOP) {
          var11 = this.font.ascent;
        } else if (var12 == Font.VerticalAlignment.BOTTOM) {
          var11 = -this.font.descent + var10;
        } else {
          var11 = (-this.font.descent - this.font.ascent + var10 >> 1) + this.font.ascent;
        }

        final Font.HorizontalAlignment r = this.horizontalAlignment;
        if (r == Font.HorizontalAlignment.LEFT || r == Font.HorizontalAlignment.JUSTIFY) {
          this.font.draw(this.getText(var5), this.b896(var5, 0, var1), var11 + this.a896(var5, var2), var3);
        } else if (r == Font.HorizontalAlignment.CENTER) {
          this.font.drawCentered(this.getText(var5), (var9 >> 1) + this.b896(var5, 0, var1), var11 + this.a896(var5, var2), var3);
        } else if (r == Font.HorizontalAlignment.RIGHT) {
          this.font.drawRightAligned(this.getText(var5), var9 + this.b896(var5, 0, var1), this.a896(var5, var2) + var11, var3);
        }
      }
    });
  }

  private int b896(final Component var2, final int var3, final int var4) {
    return var3 + this._o + var2.x + var4 + var2._h;
  }

  @Override
  public final int a474() {

    return this.font.descent + this.font.ascent;
  }

  private int getAvailableHeight(final Component var1) {
    return var1.height - this._f - this._b;
  }

  @Override
  public void draw(final Component component, final int x, final int y, final boolean enabled) {
    if (this.font != null) {
      this.a768(x, y, this._p, component);
    }
  }
}
