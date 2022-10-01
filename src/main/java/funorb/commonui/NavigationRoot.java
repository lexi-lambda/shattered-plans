package funorb.commonui;

import funorb.commonui.container.WrapperContainer;
import funorb.shatteredplans.client.ShatteredPlansClient;
import org.jetbrains.annotations.Contract;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Iterator;

public final class NavigationRoot extends WrapperContainer {
  private final Deque<NavigationPage> children = new ArrayDeque<>();

  public NavigationRoot() {
    super(0, 0, ShatteredPlansClient.SCREEN_WIDTH, ShatteredPlansClient.SCREEN_HEIGHT);
  }

  public void startTransitioningOut() {
    for (final NavigationPage child : this.children) {
      child.isAlive = false;
    }
    this.child = null;
  }

  public void tick2() {
    for (final Iterator<NavigationPage> it = this.children.iterator(); it.hasNext();) {
      final NavigationPage child = it.next();
      child.tick2();
      if (child.isReadyToBeRemoved()) {
        it.remove();
      }
    }
    this.child = this.getActive();
  }

  @Contract(pure = true)
  public NavigationPage getActive() {
    return this.children.stream().filter(var3 -> var3.isAlive).findFirst().orElse(null);
  }

  @Override
  public void draw(final int x, final int y) {
    if (this.renderer != null) {
      this.renderer.draw(this, x, y, true);
    }

    for (final Iterator<NavigationPage> it = this.children.descendingIterator(); it.hasNext();) {
      final NavigationPage child = it.next();
      child.draw(x + this.x, this.y + y);
    }
  }

  public void pushChild(final NavigationPage child) {
    this.children.push(child);
    child.isAlive = true;
    child.focus(this);
  }

  public void switchToLoadingScreen() {
    this.children.removeIf(NavigationPage::k154);
  }

  @Override
  public Component a274() {
    return this.children.stream().filter(var3 -> var3.isAlive)
        .findFirst().map(NavigationPage::getFocusedChild).orElse(null);
  }
}
