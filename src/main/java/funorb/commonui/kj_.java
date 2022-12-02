package funorb.commonui;

import org.intellij.lang.annotations.MagicConstant;

public final class kj_ {
  public final boolean success;
  public final boolean under13;
  public final String[] suggestedUsernames;
  @MagicConstant(valuesFromClass = Code.class)
  public final int code;
  public final String errorMessage;

  private kj_(final boolean success, final boolean under13, final String[] suggestedUsernames, @MagicConstant(valuesFromClass = Code.class) final int code, final String errorMessage) {
    this.success = success;
    this.under13 = under13;
    this.suggestedUsernames = suggestedUsernames;
    this.code = code;
    this.errorMessage = errorMessage;
  }

  public static kj_ createSuccess(final boolean var0) {
    return new kj_(true, var0, null, Code.SUCCESS, null);
  }

  public static kj_ a612tc(final String[] var0) {
    return new kj_(false, false, var0, Code.C100, null);
  }

  public static kj_ a431ck(@MagicConstant(valuesFromClass = Code.class) final int code, final String message) {
    return new kj_(false, false, null, code, message);
  }

  public static final class Code {
    public static final int NONE = -1;
    public static final int C3 = 3;
    public static final int C5 = 5;
    public static final int C6 = 6;
    public static final int C100 = 100;
    public static final int C105 = 105;
    public static final int INELIGIBLE = 248;
    public static final int CONNECTION_FAILED = 249;
    public static final int SUCCESS = 255;
  }
}
