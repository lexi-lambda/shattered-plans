package funorb.commonui;

import funorb.commonui.container.WrapperContainer;
import funorb.shatteredplans.client.ShatteredPlansClient;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Iterator;
import java.util.Optional;

/**
 * A {@link RootFrame} serves as the root {@link Component} of a CommonUI view
 * hierarchy. It generally contains a single active {@link Frame}, which
 * contains the actual view content. However, when transitioning between frames,
 * previous frames must continue to exist while they animate out, so the
 * {@link RootFrame} continues to render these inactive frames until they are
 * ready to be removed. 
 */
public final class RootFrame extends WrapperContainer {
  private final Deque<Frame> children = new ArrayDeque<>();

  public RootFrame() {
    super(0, 0, ShatteredPlansClient.SCREEN_WIDTH, ShatteredPlansClient.SCREEN_HEIGHT);
  }

  @Contract(pure = true)
  public @Nullable Frame getActive() {
    return this.children.stream().filter(child -> child.isActive).findFirst().orElse(null);
  }

  public void pushActive(final Frame child) {
    this.children.push(child);
    child.isActive = true;
    child.focus(this);
  }

  public void popAll() {
    this.children.forEach(child -> child.isActive = false);
    this.child = null;
  }

  @Override
  public Component getFocusedChild() {
    return Optional.ofNullable(this.getActive())
        .map(Frame::getFocusedChild).orElse(null);
  }

  public void expediteRemoval() {
    this.children.removeIf(Frame::expediteRemoval);
  }

  public void tickAnimations() {
    for (final Iterator<Frame> it = this.children.iterator(); it.hasNext();) {
      final Frame child = it.next();
      child.tickAnimations();
      if (child.isReadyForRemoval()) {
        it.remove();
      }
    }
    this.child = this.getActive();
  }

  @Override
  public void draw(final int x, final int y) {
    if (this.renderer != null) {
      this.renderer.draw(this, x, y, true);
    }

    for (final Iterator<Frame> it = this.children.descendingIterator(); it.hasNext();) {
      final Frame child = it.next();
      child.draw(x + this.x, this.y + y);
    }
  }
}
