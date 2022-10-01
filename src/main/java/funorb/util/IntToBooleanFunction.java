package funorb.util;

@FunctionalInterface
public interface IntToBooleanFunction {
  boolean applyAsBoolean(int value);
}
