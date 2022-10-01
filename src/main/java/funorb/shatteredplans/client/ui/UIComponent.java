package funorb.shatteredplans.client.ui;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public abstract class UIComponent<T> {
  public int x;
  public int y;
  public int width;
  public int height;

  public String tooltip;
  public boolean visible = true;
  public boolean enabled = true;

  public final List<UIComponent<?>> children = new ArrayList<>();
  public T data = null; // arbitrary data associated with the element

  protected UIComponent(final int x, final int y, final int width, final int height) {
    this.x = x;
    this.y = y;
    this.width = width;
    this.height = height;
  }

  public static UIComponent<?> findMouseTarget(final Collection<UIComponent<?>> components, final int x, final int y) {
    if (components != null) {
      return components.stream()
          .map(component -> component.findMouseTarget(x, y))
          .filter(target -> target != null && target.visible)
          .findFirst().orElse(null);
    }
    return null;
  }

  public final void translate(final int dx, final int dy) {
    if (dx != 0 || dy != 0) {
      this.x += dx;
      this.y += dy;

      for (final UIComponent<?> child : this.children) {
        child.translate(dx, dy);
      }
    }
  }

  protected void removeChildren() {
    this.children.clear();
  }

  public final boolean hasChild(final UIComponent<?> component) {
    return component == this || this.children.stream().anyMatch(child -> child.hasChild(component));
  }

  protected final boolean hitTest(final int x, final int y) {
    if (this.enabled && this.visible) {
      return x >= this.x && y >= this.y && x < this.x + this.width && y < this.y + this.height;
    } else {
      return false;
    }
  }

  public final void destroy() {
    this.children.forEach(UIComponent::destroy);
    this.removeChildren();
  }

  public void addChild(final UIComponent<?> child) {
    assert !this.children.contains(child);
    this.children.add(0, child);
  }

  @SuppressWarnings("unused")
  public void handleClick(final int x, final int y) {
  }

  public void a183(final int var2, final int var3) {
    this.height = var2;
    this.width = var3;
  }

  protected UIComponent<?> findMouseTarget(final int x, final int y) {
    if (this.hitTest(x, y)) {
      return this;
    } else {
      return null;
    }
  }

  public final void setPosition(final int x, final int y) {
    if (this.x != x || this.y != y) {
      this.translate(x - this.x, y - this.y);
    }
  }

  public void draw() {
  }

  public void handleDrag(final int mouseX, final int mouseY, final int originX, final int originY) {
  }
}
