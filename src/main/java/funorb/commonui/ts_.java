package funorb.commonui;

import funorb.commonui.renderer.ComponentRenderer;
import funorb.commonui.renderer.ITextRenderer;
import funorb.shatteredplans.client.JagexApplet;

import java.util.ArrayList;
import java.util.List;

public class ts_ extends Button {
  private List<ql_> _D;
  private ql_ _I = null;
  private String[] _J;

  public ts_(final String text, final ComponentRenderer renderer) {
    super(text, renderer, null);
  }

  @Override
  public void draw(final int x, final int y) {
    super.draw(x, y);
    final ITextRenderer var5 = (ITextRenderer) this.renderer;
    ql_ var6 = this._I;
    if (var6 != null) {
      final int var7 = var5.b754(this, x);
      final int var8 = var5.a754(y, this);

      do {
        Button.drawFocusRect(var6._k + var7 - 2, var8 + (var6._m - 2), 2 + var6._j, 2 + var6._l);
        var6 = var6._h;
      } while (var6 != null);
    }
  }

  public final void a096(final int var1, final String var3) {
    if (this._J == null || this._J.length <= var1) {
      final String[] var4 = new String[1 + var1];
      if (this._J != null) {
        System.arraycopy(this._J, 0, var4, 0, this._J.length);
      }

      this._J = var4;
    }

    this._J[var1] = var3;

  }

  @Override
  public final void setBounds(final int x, final int y, final int width, final int height) {
    super.setBounds(x, y, width, height);
    this.e487();
  }

  @Override
  public boolean focus(final Component previouslyFocused) {
    return false;
  }

  public final void a652(final int var1, final int var2, final int var4) {
    this.setBounds(var1, var2, var4, ((ITextRenderer) this.renderer).getPreferredHeight(this));
  }

  protected final void e487() {
    this._D = new ArrayList<>();

    int var2 = 0;
    final ITextRenderer var3 = (ITextRenderer) this.renderer;
    final AbstractTextLayout var4 = var3.updateLayout(this);

    while (true) {
      final int var5 = this.text.indexOf("<hotspot=", var2);
      if (var5 == -1) {
        return;
      }

      final int var7 = this.text.indexOf(">", var5);
      final String var6 = this.text.substring(var5 + 9, var7);
      final int i = Integer.parseInt(var6);
      var2 = this.text.indexOf("</hotspot>", var5);
      final int var8 = var4.a543(var5);
      final int var9 = var4.a543(var2);
      ql_ var10 = null;

      for (int var11 = var8; var9 >= var11; ++var11) {
        final TextLineMetrics var12 = var4.lineMetrics[var11];
        final int var13 = var8 != var11 ? var12._b[0] : var4.a527(var5);
        final int var14 = var11 != var9 ? (var12 == null ? 0 : var12._b[var12.getCharCount()]) : var4.a527(var2);
        assert var12 != null;
        final ql_ var15 = new ql_(i, var13, var12.top, var14 - var13, Math.max(var3.a474(), -var12.top + var12.bottom));
        if (var10 != null) {
          var10._h = var15;
        }

        var10 = var15;
        this._D.add(var15);
      }
    }
  }

  private ql_ b803(final int var1, final int var2) {
    for (final ql_ var4 : this._D) {
      for (ql_ var5 = var4; var5 != null; var5 = var5._h) {
        if (var5._k <= var1 && var5._m <= var2 && var5._k + var5._j > var1 && var5._l + var5._m >= var2) {
          return var4;
        }
      }
    }

    return null;
  }

  @Override
  public String getCurrentTooltip() {
    if (this._I == null || this._J == null) {
      return null;
    } else if (this._J.length <= this._I._i) {
      return null;
    } else {

      return this._J[this._I._i];
    }
  }

  @Override
  public void tick(final int x, final int y, final Component root) {
    super.tick(x, y, root);
    this._I = null;
    if (this.isMouseOver) {
      final int var5 = -this.x - x + JagexApplet.mouseX;
      final int var6 = JagexApplet.mouseY - y - this.y;
      this._I = this.b803(var5, var6);
    }

  }

  @Override
  public final void handleClicked(final int var1, final int var2, final int var3) {
    super.handleClicked(var1, var2, var3);
    final int var5 = -this.x + var2;
    final int var6 = -this.y + var3;
    final ql_ var7 = this.b803(var5, var6);
    if (var7 != null && this.listener != null) {
      ((op_) this.listener).a746(var7._i);
    }

  }
}
