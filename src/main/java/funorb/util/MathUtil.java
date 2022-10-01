package funorb.util;

import org.jetbrains.annotations.Contract;

@SuppressWarnings({"SameParameterValue", "unused"})
public final class MathUtil {
  private MathUtil() {}

  @Contract(pure = true)
  public static int euclideanDistanceSquared(final int dx, final int dy) {
    return dx * dx + dy * dy;
  }

  @Contract(pure = true)
  public static float euclideanDistanceSquared(final float dx, final float dy) {
    return dx * dx + dy * dy;
  }

  @Contract(pure = true)
  public static int euclideanDistance(final int dx, final int dy) {
    return (int) Math.sqrt(euclideanDistanceSquared(dx, dy));
  }

  @Contract(pure = true)
  public static float euclideanDistance(final float dx, final float dy) {
    return (float) Math.sqrt(dx * dx + dy * dy);
  }

  @Contract(pure = true)
  public static double euclideanDistance(final double dx, final double dy) {
    return Math.sqrt(dx * dx + dy * dy);
  }

  @Contract(pure = true)
  public static float euclideanDistance(final float dx, final float dy, final float dz) {
    return (float) Math.sqrt(dx * dx + dy * dy + dz * dz);
  }

  @Contract(pure = true)
  public static boolean isEuclideanDistanceGreaterThan(final int dx, final int dy, final int limit) {
    return euclideanDistanceSquared(dx, dy) > limit * limit;
  }

  @Contract(pure = true)
  public static boolean isEuclideanDistanceLessThan(final int dx, final int dy, final int limit) {
    return euclideanDistanceSquared(dx, dy) < limit * limit;
  }

  @Contract(pure = true)
  public static int clamp(final int x, final int min, final int max) {
    return Math.max(Math.min(x, max), min);
  }

  @Contract(pure = true)
  public static int square(final int x) {
    return x * x;
  }

  @Contract(pure = true)
  public static double ease(final int x, final int xMax) {
    return (double) (x * x) * (double) (3 * xMax - 2 * x) / (double) (xMax * xMax * xMax);
  }

  @Contract(pure = true)
  public static int ease(final int x, final int xMax, final int yMin, final int yMax) {
    return (int) ((double) (yMax - yMin) * ease(x, xMax)) + yMin;
  }
}
