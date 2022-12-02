package funorb.commonui.container;

import funorb.awt.KeyState;
import funorb.commonui.Component;
import funorb.shatteredplans.client.JagexApplet;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.ListIterator;
import java.util.Optional;

public class ListContainer extends Component implements Container {
  protected final List<Component> children = new ArrayList<>();

  protected ListContainer(final int x, final int y, final int width, final int height) {
    super(x, y, width, height);
  }

  @Override
  public final boolean a931(final int var2, final int var3, final Component var4, final int var5, final int var6, final int var7) {
    return this.children.stream().anyMatch(child -> child.hasFocus() && child.a931(var2, var3, var4, var5, var6, var7));
  }

  @Override
  public final boolean a446(final int var1, final int var2, final int var4, final int var5, final int var6, final Component var7) {
    return this.children.stream().anyMatch(child -> child.a446(var1, var2, var4, var5 + this.x, var6 + this.y, var7));
  }

  protected final void removeChild(final Component child) {
    this.children.remove(child);
  }

  @Override
  public boolean keyTyped(final int keyCode, final char keyChar, final Component focusRoot) {
    if (this.children.stream().anyMatch(child -> child.hasFocus() && child.keyTyped(keyCode, keyChar, focusRoot))) {
      return true;
    }
    if (keyCode == KeyState.Code.TAB) {
      return !JagexApplet.keysDown[81] ? this.a948(focusRoot) : this.a611(focusRoot);
    }
    return false;
  }

  protected Component getFocusedChild() {
    return this.children.stream().filter(Component::hasFocus).findFirst().orElse(null);
  }

  @Override
  public void setBounds(final int x, final int y, final int width, final int height) {
    super.setBounds(x, y, width, height);
    this.h150();
  }

  private void a938(final StringBuilder var1, final Hashtable<Component, Component> var2, final int var4) {
    for (final Component child : this.children) {
      var1.append('\n');
      var1.append(" ".repeat(Math.max(0, var4 + 1)));
      child.buildDebugString(var2, var4 + 1, var1);
    }
  }

  @Override
  public final boolean focus(final Component focusRoot) {
    return this.children.stream().anyMatch(child -> child.focus(focusRoot));
  }

  protected final boolean a611(final Component var1) {
    if (!this.children.isEmpty()) {
      for (final ListIterator<Component> it1 = this.children.listIterator(this.children.size()); it1.hasPrevious();) {
        final Component child1 = it1.previous();
        if (child1.hasFocus()) {
          for (final ListIterator<Component> it2 = this.children.listIterator(it1.nextIndex()); it2.hasPrevious();) {
            final Component child2 = it2.previous();
            if (child2.focus(var1)) {
              return true;
            }
          }
        }
      }
    }
    return false;
  }

  protected final boolean a948(final Component var1) {
    if (!this.children.isEmpty()) {
      for (final ListIterator<Component> it1 = this.children.listIterator(); it1.hasNext(); ) {
        final Component child1 = it1.next();
        if (child1.hasFocus()) {
          final ListIterator<Component> it2 = this.children.listIterator(it1.nextIndex());
          while (it2.hasNext()) {
            final Component child2 = it2.next();
            if (child2.focus(var1)) {
              return true;
            }
          }
        }
      }
    }
    return false;
  }

  @Override
  public void draw(final int x, final int y) {
    super.draw(x, y);

    for (final ListIterator<Component> it = this.children.listIterator(this.children.size()); it.hasPrevious();) {
      final Component child = it.previous();
      child.draw(this.x + x, this.y + y);
    }
  }

  @Override
  public final StringBuilder buildDebugString(final Hashtable<Component, Component> cycles, final int nestingLevel, final StringBuilder builder) {
    if (this.debugStringCycleCheck(cycles, builder)) {
      this.putDefaultDebugString(cycles, nestingLevel, builder);
      this.a938(builder, cycles, nestingLevel);
    }

    return builder;
  }

  private void h150() {
    this.children.forEach(Component::d423);
  }

  protected final void addChild(final Component child) {
    assert !this.children.contains(child);
    this.children.add(child);
  }

  @Override
  public final void a132(final int var1, final int var2, final int var3, final int var4, final Component var6) {
    this.children.forEach(child -> child.a132(var1, this.y + var2, var3 + this.x, var4, var6));
  }

  @Override
  public final void unfocus() {
    this.children.forEach(Component::unfocus);
  }

  @Override
  public String getCurrentTooltip() {
    return this.children.stream().flatMap(var3 -> Optional.ofNullable(var3.getCurrentTooltip()).stream()).findFirst().orElse(null);
  }

  @Override
  public void tick(final int x, final int y, final Component root) {
    super.tick(x, y, root);
    this.children.forEach(child -> child.tick(this.x + x, this.y + y, root));
  }

  @Override
  public final boolean hasFocus() {
    return this.getFocusedChild() != null;
  }
}
