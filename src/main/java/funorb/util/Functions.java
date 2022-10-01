package funorb.util;

import java.util.function.Consumer;
import java.util.function.Function;

public final class Functions {
  public static <A> Function<A, A> tee(final Consumer<A> f) {
    return a -> { f.accept(a); return a; };
  }
}
