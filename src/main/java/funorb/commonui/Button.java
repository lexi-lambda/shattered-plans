package funorb.commonui;

import funorb.awt.KeyState;
import funorb.awt.MouseState;
import funorb.commonui.listener.ComponentListener;
import funorb.commonui.listener.ButtonListener;
import funorb.commonui.renderer.ButtonRenderer;
import funorb.commonui.renderer.ComponentRenderer;
import funorb.graphics.Drawing;
import funorb.shatteredplans.client.JagexApplet;
import org.intellij.lang.annotations.MagicConstant;

import java.util.Hashtable;

public class Button extends Component {
  private final boolean _B;
  public boolean active;
  public boolean enabled;
  private boolean focused;

  public Button(final String text,  final ComponentListener listener) {
    this(text, new ButtonRenderer(), listener);
  }

  public Button(final String text, final ComponentRenderer renderer, final ComponentListener listener) {
    super(text, renderer, listener);
    this._B = true;
    this.focused = false;
    this.enabled = true;
  }

  protected Button() {
    this._B = true;
    this.focused = false;
    this.enabled = true;
    this.renderer = null;
  }

  public static void drawFocusRect(final int x, final int y, final int width, final int height) {
    final int var5 = x + width;
    final int var6 = height + y;
    final int var7 = Math.max(x, Drawing.left);
    final int var8 = Math.max(Drawing.top, y);
    final int var9 = Math.min(Drawing.right, var5);

    final int var10 = Math.min(Drawing.bottom, var6);
    int var11;
    int var12;
    if (Drawing.left <= x && Drawing.right > x) {
      var11 = Drawing.width * var8 + x;
      var12 = var10 + 1 - var8 >> 1;

      while (true) {
        --var12;
        if (var12 < 0) {
          break;
        }

        Drawing.screenBuffer[var11] = Drawing.WHITE;
        var11 += Drawing.width * 2;
      }
    }

    if (y >= Drawing.top && Drawing.bottom > var6) {
      var11 = Drawing.pixelIndex(var7, y);
      var12 = -var7 + 1 + var9 >> 1;

      while (true) {
        --var12;
        if (var12 < 0) {
          break;
        }

        Drawing.screenBuffer[var11] = Drawing.WHITE;
        var11 += 2;
      }
    }

    if (Drawing.left <= var5 && var5 < Drawing.right) {
      var11 = Drawing.width * ((1 & var5 - x) + var8) + var5;
      var12 = 1 - (-var10 + var8) >> 1;

      while (true) {
        --var12;
        if (var12 < 0) {
          break;
        }

        Drawing.screenBuffer[var11] = Drawing.WHITE;
        var11 += Drawing.width * 2;
      }
    }

    if (y >= Drawing.top && Drawing.bottom > var6) {
      var11 = (1 & var6 - y) + var6 * Drawing.width + var7;
      var12 = -var7 + 1 + var9 >> 1;

      while (true) {
        --var12;
        if (var12 < 0) {
          break;
        }

        Drawing.screenBuffer[var11] = Drawing.WHITE;
        var11 += 2;
      }
    }

  }

  @Override
  public final StringBuilder buildDebugString(final Hashtable<Component, Component> cycles, final int nestingLevel, final StringBuilder builder) {
    if (this.debugStringCycleCheck(cycles, builder)) {
      this.putDefaultDebugString(cycles, nestingLevel, builder);
      if (this.active) {
        builder.append(" active");
      }

      if (!this.enabled) {
        builder.append(" disabled");
      }
    }

    return builder;
  }

  @Override
  public boolean focus(final Component focusRoot) {
    if (this.enabled && this._B) {
      focusRoot.unfocus();
      this.focused = true;
      return true;
    } else {
      return false;
    }
  }

  @Override
  public final boolean hasFocus() {
    return this.focused;
  }

  @Override
  public boolean a446(final int var1, final int var2, final int var4, final int var5, final int var6, final Component var7) {
    if (this.enabled && this.a046(var2, var4, var6, var5)) {
      this.focus(var7);
      this._o = var1;

      return true;
    } else {
      return false;
    }
  }

  @Override
  public final void unfocus() {
    if (this.focused) {
      this.focused = false;
    }
  }

  @Override
  public void tick(final int x, final int y, final Component root) {
    super.tick(x, y, root);
    if (this._o != 0 && JagexApplet.mouseButtonDown != this._o) {
      if (this.a046(JagexApplet.mouseX, JagexApplet.mouseY, y, x) && JagexApplet.mouseButtonDown == MouseState.Button.NONE) {
        this.handleClicked(this._o, JagexApplet.mouseX - x, -y + JagexApplet.mouseY);
      }

      this.a132(JagexApplet.mouseX, y, x, JagexApplet.mouseY, root);
    }

  }

  @Override
  public boolean keyTyped(@MagicConstant(valuesFromClass = KeyState.Code.class) final int keyCode, final char keyChar, final Component focusRoot) {
    if (!this.hasFocus() || keyCode != KeyState.Code.ENTER && keyCode != KeyState.Code.SPACE) {
      return false;
    } else {
      this.handleClicked(1, -1, -1);
      return true;
    }
  }

  protected void handleClicked(final int var1, final int var2, final int var3) {
    if (this.listener != null && this.listener instanceof ButtonListener) {
      ((ButtonListener) this.listener).handleButtonClicked(this);
    }
  }

  @Override
  public final void a132(final int var1, final int var2, final int var3, final int var4, final Component var6) {
    this._o = 0;
  }
}
