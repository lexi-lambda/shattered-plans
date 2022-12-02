package funorb.commonui.container;

import funorb.awt.KeyState;
import funorb.commonui.Component;
import funorb.shatteredplans.client.JagexApplet;

import java.util.Arrays;
import java.util.Hashtable;
import java.util.Objects;

public abstract class ArrayContainer extends Component implements Container {
  protected Component[] children;

  protected ArrayContainer() {
    super(0, 0, 0, 0);
  }

  private boolean a847(final Component var3) {
    if (this.children != null) {
      for (int var5 = this.children.length - 1; var5 >= 0; --var5) {
        final Component var6 = this.children[var5];
        if (var6 != null && var6.hasFocus()) {
          for (var5 -= 1; var5 >= 0; var5 -= 1) {
            final Component var7 = this.children[var5];
            if (var7 != null && var7.focus(var3)) {
              return true;
            }
          }
        }
      }

    }
    return false;
  }

  @Override
  public final boolean a931(final int var2, final int var3, final Component var4, final int var5, final int var6, final int var7) {
    if (this.children != null) {
      return Arrays.stream(this.children).anyMatch(var10 ->
          var10 != null && var10.hasFocus() && var10.a931(var2, var3, var4, var5, var6, var7));
    }
    return false;
  }

  @Override
  public final boolean keyTyped(final int keyCode, final char keyChar, final Component focusRoot) {
    if (this.children == null) {
      return false;
    } else {

      final Component[] var5 = this.children;

      for (final Component var7 : var5) {
        if (var7 != null && var7.hasFocus() && var7.keyTyped(keyCode, keyChar, focusRoot)) {
          return true;
        }
      }

      if (keyCode == KeyState.Code.TAB) {
        return !JagexApplet.keysDown[81] ? this.a948(focusRoot) : this.a872(focusRoot);
      } else {
        return false;
      }
    }
  }

  private boolean a948(final Component var1) {

    return this.a766(var1);
  }

  private void a801(final int var1, final StringBuilder var2, final Hashtable<Component, Component> var3) {
    if (this.children != null) {
      final Component[] var5 = this.children;

      for (final Component var7 : var5) {
        var2.append('\n');

        var2.append(" ".repeat(Math.max(0, var1 + 1)));

        if (var7 == null) {
          var2.append("null");
        } else {
          var7.buildDebugString(var3, 1 + var1, var2);
        }
      }

    }
  }

  @Override
  public final void setBounds(final int x, final int y, final int width, final int height) {
    super.setBounds(x, y, width, height);
    this.h150();
  }

  @Override
  public final void unfocus() {
    final Component[] var2 = this.children;

    for (final Component var5 : var2) {
      if (var5 != null) {
        var5.unfocus();
      }
    }

  }

  @Override
  public void draw(final int x, final int y) {
    if (this.renderer != null) {
      this.renderer.draw(this, x, y, true);
    }

    if (this.children != null) {
      for (int var5 = this.children.length - 1; var5 >= 0; --var5) {
        final Component var6 = this.children[var5];
        if (var6 != null) {
          var6.draw(this.x + x, this.y + y);
        }
      }
    }

  }

  @Override
  public final boolean a446(final int var1, final int var2, final int var4, final int var5, final int var6, final Component var7) {
    if (this.children != null) {
      return Arrays.stream(this.children).anyMatch(var10 ->
          var10 != null && var10.a446(var1, var2, var4, this.x + var5, this.y + var6, var7));
    }
    return false;
  }

  private Component a331() {
    if (this.children != null) {
      return Arrays.stream(this.children)
          .filter(var4 -> var4 != null && var4.hasFocus())
          .findFirst().orElse(null);
    }
    return null;
  }

  @Override
  public final boolean hasFocus() {
    return this.a331() != null;
  }

  @Override
  public final void a132(final int var1, final int var2, final int var3, final int var4, final Component var6) {
    if (this.children != null) {
      final Component[] var7 = this.children;

      for (final Component var9 : var7) {
        if (var9 != null) {
          var9.a132(var1, this.y + var2, var3 + this.x, var4, var6);
        }
      }

    }
  }

  protected abstract void h150();

  @Override
  public final boolean focus(final Component focusRoot) {
    return Arrays.stream(this.children).anyMatch(var6 -> var6 != null && var6.focus(focusRoot));
  }

  private boolean a766(final Component var1) {
    if (this.children != null) {
      for (int var4 = 0; this.children.length > var4; ++var4) {
        final Component var5 = this.children[var4];
        if (var5 != null && var5.hasFocus()) {
          for (var4 += 1; var4 < this.children.length; var4 += 1) {
            final Component var6 = this.children[var4];
            if (var6 != null && var6.focus(var1)) {
              return true;
            }
          }
        }
      }

    }
    return false;
  }

  @Override
  public void tick(final int x, final int y, final Component root) {
    super.tick(x, y, root);
    if (this.children != null) {
      final Component[] var5 = this.children;

      for (final Component var7 : var5) {
        if (var7 != null) {
          var7.tick(this.x + x, y + this.y, root);
        }
      }
    }
  }

  private boolean a872(final Component var2) {
    return this.a847(var2);
  }

  @Override
  public final StringBuilder buildDebugString(final Hashtable<Component, Component> cycles, final int nestingLevel, final StringBuilder builder) {
    if (this.debugStringCycleCheck(cycles, builder)) {
      this.putDefaultDebugString(cycles, nestingLevel, builder);
      this.a801(nestingLevel, builder, cycles);
    }

    return builder;
  }

  @Override
  public final String getCurrentTooltip() {
    if (this.children != null) {
      return Arrays.stream(this.children)
          .filter(Objects::nonNull)
          .map(Component::getCurrentTooltip)
          .filter(Objects::nonNull)
          .findFirst().orElse(null);
    }
    return null;
  }
}
