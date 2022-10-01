package funorb.shatteredplans.client;

import org.intellij.lang.annotations.MagicConstant;

public final class AuthMode {
  public static final int USER_ID = 2;
  public static final int EMAIL = 3;

  @MagicConstant(valuesFromClass = AuthMode.class)
  public final int value;

  public AuthMode(@MagicConstant(valuesFromClass = AuthMode.class) final int value) {
    this.value = value;
  }
}
