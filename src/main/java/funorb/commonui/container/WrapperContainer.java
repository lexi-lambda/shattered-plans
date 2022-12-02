package funorb.commonui.container;

import funorb.awt.KeyState;
import funorb.commonui.Component;
import funorb.shatteredplans.client.JagexApplet;

import java.util.Hashtable;

public abstract class WrapperContainer extends Component implements Container {
  protected Component child;

  protected WrapperContainer(final int x, final int y, final int width, final int height) {
    super(x, y, width, height);
  }

  private boolean a948(final Component var1) {
    return this.child != null && !this.child.hasFocus() && this.child.focus(var1);
  }

  private void putChildDebugString(final Hashtable<Component, Component> cycles, final int nestingLevel, final StringBuilder builder) {
    builder.append('\n');
    builder.append(" ".repeat(Math.max(0, nestingLevel + 1)));
    if (this.child == null) {
      builder.append("null");
    } else {
      this.child.buildDebugString(cycles, 1 + nestingLevel, builder);
    }
  }

  @Override
  public final boolean focus(final Component focusRoot) {
    return this.child != null && this.child.focus(focusRoot);
  }

  @Override
  public final void unfocus() {
    if (this.child != null) {
      this.child.unfocus();
    }

  }

  @Override
  public void a132(final int var1, final int var2, final int var3, final int var4, final Component var6) {
    if (this.child != null) {
      this.child.a132(var1, this.y + var2, var3 + this.x, var4, var6);
    }

  }

  private void h150() {
    if (this.child != null) {
      this.child.d423();
    }

  }

  @Override
  public final boolean a931(final int var2, final int var3, final Component var4, final int var5, final int var6, final int var7) {
    return this.child != null && this.child.hasFocus() && this.child.a931(var2, var3, var4, var5, var6, var7);
  }

  private boolean a872(final Component var2) {

    return this.child != null && !this.child.hasFocus() && this.child.focus(var2);
  }

  @Override
  public final boolean hasFocus() {
    return this.getFocusedChild() != null;
  }

  @Override
  public final void setBounds(final int x, final int y, final int width, final int height) {
    super.setBounds(x, y, width, height);
    this.h150();
  }

  @Override
  public String getCurrentTooltip() {
    assert true;

    final String var2 = super.getCurrentTooltip();
    if (this.child != null) {
      final String var3 = this.child.getCurrentTooltip();
      if (var3 != null) {
        return var3;
      }
    }

    return var2;
  }

  @Override
  public StringBuilder buildDebugString(final Hashtable<Component, Component> cycles, final int nestingLevel, final StringBuilder builder) {
    if (this.debugStringCycleCheck(cycles, builder)) {
      this.putDefaultDebugString(cycles, nestingLevel, builder);
      this.putChildDebugString(cycles, nestingLevel, builder);
    }
    return builder;
  }

  @Override
  public boolean a446(final int var1, final int var2, final int var4, final int var5, final int var6, final Component var7) {
    return this.child != null && this.child.a446(var1, var2, var4, var5 + this.x, this.y + var6, var7);
  }

  protected Component getFocusedChild() {
    return this.child != null && this.child.hasFocus() ? this.child : null;
  }

  @Override
  public void tick(final int x, final int y, final Component root) {
    super.tick(x, y, root);
    if (this.child != null) {
      this.child.tick(this.x + x, this.y + y, root);
    }

  }

  @Override
  public void draw(final int x, final int y) {
    if (this.renderer != null) {
      this.renderer.draw(this, x, y, true);
    }

    if (this.child != null) {
      this.child.draw(this.x + x, this.y + y);
    }
  }

  @Override
  public final boolean keyTyped(final int keyCode, final char keyChar, final Component focusRoot) {
    if (this.child != null && this.child.hasFocus() && this.child.keyTyped(keyCode, keyChar, focusRoot)) {
      return true;
    } else if (keyCode == KeyState.Code.TAB) {
      return !JagexApplet.keysDown[81] ? this.a948(focusRoot) : this.a872(focusRoot);
    } else {
      return false;
    }
  }
}
