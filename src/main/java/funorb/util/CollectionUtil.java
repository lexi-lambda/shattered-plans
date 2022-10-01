package funorb.util;

import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.ListIterator;
import java.util.RandomAccess;
import java.util.function.BiPredicate;

public final class CollectionUtil {
  private CollectionUtil() {}

  public static <T> void insertSorted(final List<T> list, final T value, final @NotNull BiPredicate<T, T> lessThan) {
    if (list.isEmpty()) {
      list.add(value);
    } else if (list instanceof RandomAccess) {
      int left = 0;
      int right = list.size() - 1;
      while (left < right) {
        final int mid = (left + right) / 2;
        if (lessThan.test(list.get(mid), value)) {
          left = mid + 1;
        } else {
          right = mid - 1;
        }
      }
      assert left == right || (left == 0 && right == -1);
      if (lessThan.test(list.get(left), value)) {
        list.add(left + 1, value);
      } else {
        list.add(left, value);
      }
    } else {
      for (final ListIterator<T> it = list.listIterator(); it.hasNext(); ) {
        final T elem = it.next();
        if (!lessThan.test(elem, value)) {
          it.previous();
          it.add(value);
        }
      }
    }
  }
}
