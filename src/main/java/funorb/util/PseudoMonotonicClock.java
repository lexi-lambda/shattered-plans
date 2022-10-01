package funorb.util;

public final class PseudoMonotonicClock {
  private static long lastMillis;
  private static long totalBackwardsMillis;

  public static synchronized long currentTimeMillis() {
    final long millis = System.currentTimeMillis();
    if (millis < lastMillis) {
      totalBackwardsMillis += lastMillis - millis;
    }

    lastMillis = millis;
    return millis + totalBackwardsMillis;
  }
}
