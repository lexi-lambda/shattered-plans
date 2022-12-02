package funorb.commonui.form.validator;

import funorb.Strings;
import funorb.commonui.CommonUI;
import funorb.commonui.Enum1;
import funorb.commonui.AbstractTextField;
import funorb.commonui.AccountResponse;
import funorb.shatteredplans.StringConstants;

import java.util.stream.IntStream;

public final class UsernameValidator extends StringValidator {
  public static AccountResponse _ija;
  public static String _gpb;
  private String _n;
  private boolean _o = false;

  public UsernameValidator(final AbstractTextField var1) {
    super(var1);
  }

  private static String a150nn(final CharSequence var0) {
    final String var2 = a615wp(var0);
    if (var2 == null) {
      for (int var4 = 0; var0.length() > var4; ++var4) {
        if (!isAllowedInUsername(var0.charAt(var4))) {
          return StringConstants.CREATE_ALERT_NAME_CHARS;
        }
      }

      return null;
    } else {
      return var2;
    }
  }

  private static boolean isValidUsername(final CharSequence var0) {
    if (!isValidUsername(var0, false)) {
      return false;
    }
    return IntStream.range(0, var0.length()).allMatch(i -> isAllowedInUsername(var0.charAt(i)));
  }

  public static boolean isValidUsername(final CharSequence str, final boolean allowConsecutiveSpaceLikeChars) {
    // usernames must be between 1 and 12 characters
    if (str == null || str.length() < 1 || str.length() > 12) {
      return false;
    }

    final String normalized = Strings.normalize(str);
    if (normalized == null || normalized.length() < 1) {
      return false;
    }
    if (Strings.isSpaceLikeUsernameChar(normalized.charAt(0)) || Strings.isSpaceLikeUsernameChar(normalized.charAt(normalized.length() - 1))) {
      return false;
    }

    if (!allowConsecutiveSpaceLikeChars) {
      int consecutiveSpaceLikeChars = 0;
      for (int i = 0; i < str.length(); ++i) {
        final char c = str.charAt(i);
        if (Strings.isSpaceLikeUsernameChar(c)) {
          ++consecutiveSpaceLikeChars;
        } else {
          consecutiveSpaceLikeChars = 0;
        }

        if (consecutiveSpaceLikeChars >= 2) {
          return false;
        }
      }
    }

    return true;
  }

  @SuppressWarnings("BooleanMethodIsAlwaysInverted")
  private static boolean isAllowedInUsername(final char var0) {
    if (Character.isISOControl(var0)) {
      return false;
    } else if (Strings.isAlphanumeric(var0)) {
      return true;
    } else {
      return var0 == '-' || var0 == Strings.NON_BREAKING_SPACE || var0 == ' ' || var0 == '_';
    }
  }

  private static AccountResponse a382ji(final String var1) {
    if (CommonUI._fjs == Enum1.C3) {
      return null;
    } else if (CommonUI._fjs == Enum1.C2 && var1.equals(_gpb)) {
      CommonUI._fjs = Enum1.C1;
      return _ija;
    } else {
      _gpb = var1;
      _ija = null;
      CommonUI._fjs = Enum1.C3;
      return null;
    }
  }

  private static String a615wp(final CharSequence var0) {
    if (var0 == null) {
      return StringConstants.CREATE_ALERT_NAME_LENGTH;
    } else {
      final int var2 = var0.length();
      if (var2 >= 1 && var2 <= 12) {
        final String var3 = Strings.normalize(var0);
        if (var3 != null && var3.length() >= 1) {
          if (Strings.isSpaceLikeUsernameChar(var3.charAt(0)) || Strings.isSpaceLikeUsernameChar(var3.charAt(var3.length() - 1))) {
            return StringConstants.CREATE_ALERT_NAME_LEADING_SPACE;
          } else {
            int var4 = 0;

            for (int var5 = 0; var0.length() > var5; ++var5) {
              final char var6 = var0.charAt(var5);
              if (Strings.isSpaceLikeUsernameChar(var6)) {
                ++var4;
              } else {
                var4 = 0;
              }

              if (var4 >= 2) {
                return StringConstants.CREATE_ALERT_DOUBLE_SPACE;
              }
            }

            if (var4 > 0) {
              return StringConstants.CREATE_ALERT_NAME_LEADING_SPACE;
            } else {
              return null;
            }
          }
        } else {
          return StringConstants.CREATE_ALERT_NAME_LENGTH;
        }
      } else {
        return StringConstants.CREATE_ALERT_NAME_LENGTH;
      }
    }
  }

  @Override
  public ValidationState b492(final String username) {
    if (!isValidUsername(username)) {
      return ValidationState.INVALID;
    }

    if (!username.equals(this._n)) {
      final AccountResponse var3 = a382ji(username);
      if (var3 == null || var3.errorMessage != null) {
        return ValidationState.CHECKING_1;
      }

      this._o = var3.success;
      this._n = username;
    }

    return this._o ? ValidationState.C2 : ValidationState.INVALID;
  }

  public void d150() {
    this._n = null;
  }

  @Override
  public String a751(final String var2) {
    final String var3 = a150nn(var2);
    if (var3 == null) {
      if (!var2.equals(this._n)) {
        final AccountResponse var4 = a382ji(var2);
        if (var4 == null || var4.errorMessage != null) {
          return null;
        }

        this._n = var2;
        this._o = var4.success;
      }

      return !this._o ? StringConstants.CREATE_USERNAME_UNAVAILABLE : StringConstants.CREATE_USERNAME_AVAILABLE;
    } else {
      return var3;
    }
  }
}
