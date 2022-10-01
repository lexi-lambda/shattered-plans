package funorb.util;

import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.function.IntUnaryOperator;

@SuppressWarnings("SameParameterValue")
public final class ArrayUtil {
  private ArrayUtil() {}

  public static boolean[] create(final int len, final boolean value) {
    final boolean[] arr = new boolean[len];
    if (value) {
      Arrays.fill(arr, true);
    }
    return arr;
  }

  public static int[] create(final int len, final int value) {
    final int[] arr = new int[len];
    if (value != 0) {
      Arrays.fill(arr, value);
    }
    return arr;
  }

  public static int[] build(final int len, final IntUnaryOperator generator) {
    final int[] arr = new int[len];
    Arrays.setAll(arr, generator);
    return arr;
  }

  public static int[] range(final int endExclusive) {
    return build(endExclusive, i -> i);
  }

  public static void setAll(final boolean @NotNull [] array, final @NotNull IntToBooleanFunction generator) {
    for (int i = 0; i < array.length; i++) {
      array[i] = generator.applyAsBoolean(i);
    }
  }

  public static void sortScored(final int[] array, final int[] scores) {
    sortScored(array, scores, 0, scores.length - 1);
  }

  private static void sortScored(final int[] array, final int[] scores, final int start, final int end) {
    if (end > start) {
      final int var4 = (start + end) / 2;
      int var5 = start;
      final int var6 = scores[var4];
      scores[var4] = scores[end];
      scores[end] = var6;
      final int var7 = array[var4];
      array[var4] = array[end];
      array[end] = var7;
      final int var8 = var6 == Integer.MAX_VALUE ? 0 : 1;

      for (int var9 = start; end > var9; ++var9) {
        if (var6 - (var8 & var9) < scores[var9]) {
          final int var10 = scores[var9];
          scores[var9] = scores[var5];
          scores[var5] = var10;
          final int var11 = array[var9];
          array[var9] = array[var5];
          array[var5++] = var11;
        }
      }

      scores[end] = scores[var5];
      scores[var5] = var6;
      array[end] = array[var5];
      array[var5] = var7;
      sortScored(array, scores, start, var5 - 1);
      sortScored(array, scores, 1 + var5, end);
    }
  }

  public static void sortScored(final Object[] array, final int[] scores) {
    sortScored(array, scores, 0, scores.length - 1);
  }

  private static void sortScored(final Object[] array, final int[] scores, final int start, final int end) {
    if (end > start) {
      final int var4 = (start + end) / 2;
      int var5 = start;
      final int var6 = scores[var4];
      scores[var4] = scores[end];
      scores[end] = var6;
      final Object var7 = array[var4];
      array[var4] = array[end];
      array[end] = var7;
      final int var8 = var6 != Integer.MAX_VALUE ? 1 : 0;

      for (int var9 = start; end > var9; ++var9) {
        if (var6 - (var8 & var9) < scores[var9]) {
          final int var10 = scores[var9];
          scores[var9] = scores[var5];
          scores[var5] = var10;
          final Object var11 = array[var9];
          array[var9] = array[var5];
          array[var5++] = var11;
        }
      }

      scores[end] = scores[var5];
      scores[var5] = var6;
      array[end] = array[var5];
      array[var5] = var7;
      sortScored(array, scores, start, var5 - 1);
      sortScored(array, scores, var5 + 1, end);
    }
  }
}
