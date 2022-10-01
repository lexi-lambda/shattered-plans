package funorb.graphics;

import org.jetbrains.annotations.Contract;

public final class Rect {
  public int y1;
  public int x1;
  public int x2;
  public int y2;

  @Contract(pure = true)
  public Rect() {
    this(0, 0, 0, 0);
  }

  @Contract(pure = true)
  public Rect(final int x1, final int y1, final int x2, final int y2) {
    this.x1 = x1;
    this.y1 = y1;
    this.x2 = x2;
    this.y2 = y2;
  }

  public boolean contains(final int x, final int y) {
    return x >= this.x1 && x < this.x2
        && y >= this.y1 && y < this.y2;
  }
}
