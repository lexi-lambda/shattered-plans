package funorb.util;

import java.util.Random;

public final class IsaacRandom extends Random {
  private IsaacCipher isaac;

  public IsaacRandom(final long seed) {
    this.setSeed(seed);
  }

  public IsaacRandom() {
    this(42L);
  }

  @Override
  protected int next(final int var1) {
    return this.isaac.nextInt() >> -var1 + 32;
  }

  @Override
  public void setSeed(final long var1) {
    final int[] var3 = new int[]{(int) (65535L & var1), (int) (var1 >> 32 & 65535L)};
    this.isaac = new IsaacCipher(var3);
  }
}
