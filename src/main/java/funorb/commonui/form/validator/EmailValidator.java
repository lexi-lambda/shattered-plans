package funorb.commonui.form.validator;

import funorb.commonui.AbstractTextField;
import funorb.shatteredplans.StringConstants;

public final class EmailValidator extends StringValidator {
  private static final String _hrc = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789!#$%&'*+-/=?^_{}~";
  private static final String _maq = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";

  public EmailValidator(final AbstractTextField var1) {
    super(var1);
  }

  private static Enum5 a621(final String var0) {
    final int var1 = var0.length();
    if (var1 == 0) {
      return Enum5.C2;
    } else if (var1 > 255) {
      return Enum5.C1;
    } else {
      final String[] var2 = var0.split("\\.");
      if (var2.length >= 2) {

        for (final String var5 : var2) {
          final Enum5 var6 = a761w(var5);
          if (var6 != null) {
            return var6;
          }
        }

        return a003oq(var2[var2.length - 1]);
      } else {
        return Enum5.C2;
      }
    }
  }

  private static Enum5 a264vn(final String var1) {
    if (var1 == null || var1.length() == 0) {
      return Enum5.C4;
    } else {
      final int var2 = var1.indexOf((char) 64);
      if (var2 == -1) {
        return Enum5.C2;
      } else {
        final String var3 = var1.substring(0, var2);
        final String var4 = var1.substring(var2 + 1);
        final Enum5 var5 = a264uc(var3);
        return var5 != null ? var5 : a621(var4);
      }
    }
  }

  private static Enum5 a761w(final String var0) {
    final int var1 = var0.length();
    if (var1 == 0) {
      return Enum5.C2;
    } else if (var1 > 63) {
      return Enum5.C1;
    } else {
      for (int var3 = 0; var3 < var1; ++var3) {
        final char var4 = var0.charAt(var3);
        if (var4 != '-') {
          if (_maq.indexOf(var4) == -1) {
            return Enum5.C3;
          }
        } else if (var3 == 0 || var1 - 1 == var3) {
          return Enum5.C3;
        }
      }

      return null;
    }
  }

  private static Enum5 a003oq(final String var0) {
    final int var1 = var0.length();

    for (int var2 = 0; var1 > var2; ++var2) {
      final char var3 = var0.charAt(var2);
      if (var3 < '0' || var3 > '9') {
        return null;
      }
    }

    return Enum5.C3;
  }

  private static Enum5 a264uc(final String var0) {
    final int var1 = var0.length();
    if (var1 == 0) {
      return Enum5.C2;
    } else if (var1 > 64) {
      return Enum5.C1;
    } else {
      boolean var2;
      int var3;
      char var4;
      if (var0.charAt(0) == '"') {
        if (var0.charAt(var1 - 1) == '"') {
          var2 = false;

          for (var3 = 1; var1 - 1 > var3; ++var3) {
            var4 = var0.charAt(var3);
            if (var4 == '\\') {
              var2 = !var2;
            } else {
              if (var4 == '"' && !var2) {
                return Enum5.C3;
              }

              var2 = false;
            }
          }

          return null;
        } else {
          return Enum5.C3;
        }
      } else {
        var2 = false;

        for (var3 = 0; var3 < var1; ++var3) {
          var4 = var0.charAt(var3);
          if (var4 == '.') {
            if (var3 == 0 || var3 == var1 - 1 || var2) {
              return Enum5.C3;
            }

            var2 = true;
          } else {
            if (_hrc.indexOf(var4) == -1) {
              return Enum5.C3;
            }

            var2 = false;
          }
        }

        return null;
      }
    }
  }

  @Override
  public String a751(final String var2) {
    if (this.b492(var2) == ValidationState.INVALID) {
      return StringConstants.CREATE_ALERT_INVALID_EMAIL;
    } else {
      return StringConstants.CREATE_EMAIL_VALID;
    }
  }

  @Override
  protected ValidationState b492(final String var2) {
    final boolean var3 = a264vn(var2) == null;
    return var3 ? ValidationState.C2 : ValidationState.INVALID;
  }

  private enum Enum5 {
    C1, C2, C3, C4
  }
}
