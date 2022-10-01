package funorb.util;

public final class BitMath {
  private BitMath() {}

  public static int nextLowestPowerOf2(int n) {
    --n;
    n |= n >>> 1;
    n |= n >>> 2;
    n |= n >>> 4;
    n |= n >>> 8;
    n |= n >>> 16;
    return n + 1;
  }

  /**
   * Returns the index, starting from 1, of the most significant set bit
   * (which is <code>log<sub>2</sub>(x) + 1</code>). If the argument is
   * {@code 0}, returns {@code 0}.
   */
  public static int lastSet(int x) {
    int y = 0;
    if (x < 0 || x >= 65536) {
      x >>>= 16;
      y += 16;
    }
    if (x >= 256) {
      x >>>= 8;
      y += 8;
    }
    if (x >= 16) {
      x >>>= 4;
      y += 4;
    }
    if (x >= 4) {
      x >>>= 2;
      y += 2;
    }
    if (x >= 1) {
      x >>>= 1;
      y += 1;
    }
    return x + y;
  }
}
