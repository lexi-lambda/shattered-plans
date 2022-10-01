package funorb.commonui;

import funorb.awt.KeyState;
import funorb.awt.MouseState;
import funorb.commonui.listener.ComponentListener;
import funorb.commonui.renderer.ComponentRenderer;
import funorb.commonui.renderer.ITextRenderer;
import funorb.graphics.Sprite;
import funorb.shatteredplans.client.JagexApplet;
import org.intellij.lang.annotations.MagicConstant;

import java.util.Arrays;
import java.util.Hashtable;

public class Component {
  private static int mouseButtonDown = 0;

  public TextLayout textLayout;
  public ComponentListener listener;
  public String tooltip;
  public int width;
  public int height;
  public int x;
  public int y;
  public ComponentRenderer renderer;
  protected int _o;
  public String text;
  public int _l = 0;
  public int _h = 0;
  public boolean isMouseOver;

  protected Component() {
  }

  public Component(final String text, final ComponentRenderer renderer, final ComponentListener listener) {
    this.text = text;
    this.renderer = renderer;
    this.listener = listener;
    this.pack();
  }

  public final void pack() {
    if (this.renderer instanceof ITextRenderer textRenderer) {
      this.width = textRenderer.getPreferredWidth(this);
      this.height = textRenderer.getPreferredHeight(this);
    }
  }

  protected Component(final int x, final int y, final int width, final int height) {
    this.x = x;
    this.y = y;
    this.width = width;
    this.height = height;
  }

  public static Sprite createSolidSquareSprite(final int size, final int color) {
    final Sprite var2 = new Sprite(size, size);
    Arrays.fill(var2.pixels, color);
    return var2;
  }

  public final void d423() {
    this.setBounds(this.x, this.y, this.width, this.height);
  }

  public final void tickRoot(final int x, final int y, final boolean var2) {
    this.tick(x, y, this);
    final boolean var5 = this.isFocused();
    if (var2) {
      if (JagexApplet.mouseWheelRotation != 0 && var5) {
        this.a931(JagexApplet.mouseY, JagexApplet.mouseX, this, y, x, JagexApplet.mouseWheelRotation);
      }

      if (JagexApplet.mouseButtonJustClicked != MouseState.Button.NONE) {
        if (!this.a446(JagexApplet.mouseButtonJustClicked, JagexApplet.mousePressX, JagexApplet.mousePressY, x, y, this) && var5) {
          this.unfocus();
        }
      }

      if (JagexApplet.mouseButtonDown == MouseState.Button.NONE && mouseButtonDown != 0) {
        this.a132(JagexApplet.mouseX, y, x, JagexApplet.mouseY, this);
      }
    } else if (var5 && JagexApplet.mouseButtonJustClicked != MouseState.Button.NONE) {
      this.unfocus();
    }

    mouseButtonDown = JagexApplet.mouseButtonDown;
    final String tooltip = this.getCurrentTooltip();
    if (TooltipManager.INSTANCE != null) {
      TooltipManager.INSTANCE.tick(tooltip);
    }
  }

  /**
   * @param previouslyFocused the component that previously had focus
   * @return {@code true} if focus was successfully transferred, {@code false} otherwise
   */
  public boolean focus(final Component previouslyFocused) {
    return false;
  }

  public void a132(final int var1, final int var2, final int var3, final int var4, final Component var6) {
    this._o = 0;
  }

  public void unfocus() {
  }

  public final void keyTyped(final int keyCode, final char keyChar) {
    if (!this.isFocused() || !this.a686(keyCode, keyChar, this)) {
      if (keyCode == KeyState.Code.TAB) {
        this.focus(this);
      }
    }
  }

  public void draw(final int x, final int y) {
    if (this.renderer != null) {
      this.renderer.draw(this, x, y, true);
    }
  }

  public boolean a446(final int var1, final int var2, final int var4, final int var5, final int var6, final Component var7) {
    if (this.a046(var2, var4, var6, var5)) {
      this._o = var1;
    }
    return false;
  }

  public void tick(final int x, final int y, final Component root) {
    final boolean var5 = this.a046(JagexApplet.mouseX, JagexApplet.mouseY, y, x);
    if (!this.isMouseOver == var5) {
      this.isMouseOver = var5;
    }
  }

  protected final boolean a046(final int var1, final int var3, final int var4, final int var5) {
    return this.x + var5 <= var1 && var3 >= var4 + this.y && var1 < this.width + this.x + var5 && var4 + this.y + this.height > var3;
  }

  public void setBounds(final int x, final int y, final int width, final int height) {
    this.x = x;
    this.y = y;
    this.width = width;
    this.height = height;
    if (this.textLayout != null) {
      this.textLayout.invalidate();
    }
  }

  public boolean a931(final int var2, final int var3, final Component var4, final int var5, final int var6, final int var7) {
    return false;
  }

  public final void drawRoot(final int x, final int y) {
    this.draw(x, y);
    TooltipManager.INSTANCE.maybeDrawTooltip();
  }

  public String getCurrentTooltip() {
    return this.isMouseOver ? this.tooltip : null;
  }

  public boolean isFocused() {
    return false;
  }

  public boolean a686(@MagicConstant(valuesFromClass = KeyState.Code.class) final int keyCode, final char keyChar, final Component var4) {
    return false;
  }

  @Override
  public final String toString() {
    return this.buildDebugString(new Hashtable<>(), 0, new StringBuilder()).toString();
  }

  public StringBuilder buildDebugString(final Hashtable<Component, Component> cycles, final int nestingLevel, final StringBuilder builder) {
    if (this.debugStringCycleCheck(cycles, builder)) {
      this.putDefaultDebugString(cycles, nestingLevel, builder);
    }
    return builder;
  }

  protected final boolean debugStringCycleCheck(final Hashtable<Component, Component> cycles, final StringBuilder builder) {
    if (cycles.containsKey(this)) {
      builder.append("<circular [0x").append(Integer.toHexString(this.hashCode())).append("]>");
      return false;
    } else {
      cycles.put(this, this);
      return true;
    }
  }

  protected final void putDefaultDebugString(final Hashtable<Component, Component> cycles, final int nestingLevel, StringBuilder builder) {
    builder.append(this.getClass().getName())
        .append("[0x").append(Integer.toHexString(this.hashCode())).append("] @")
        .append(this.x).append(",").append(this.y).append(" ")
        .append(this.width).append("x").append(this.height);
    if (this.text != null) {
      builder.append(" text=\"").append(this.text).append('"');
    }

    if (this.isMouseOver) {
      builder.append(" mouseover");
    }

    if (this.isFocused()) {
      builder.append(" focused");
    }

    if (this.renderer != null) {
      builder.append(" renderer=");
      if (this.renderer instanceof Component) {
        builder = this.buildDebugString(cycles, nestingLevel + 1, builder);
      } else {
        builder.append(this.renderer);
      }
    }

    if (this.listener != null) {
      builder.append(" listener=");
      if (this.listener instanceof Component) {
        this.buildDebugString(cycles, nestingLevel + 1, builder);
      } else {
        builder.append(this.listener);
      }
    }
  }
}
