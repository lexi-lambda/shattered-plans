package funorb.commonui.form.validator;

import funorb.Strings;
import funorb.commonui.AbstractTextField;
import funorb.shatteredplans.StringConstants;

import java.util.stream.IntStream;

public final class PasswordValidator extends StringValidator {
  private static final int _npo = 5;
  private final AbstractTextField _o;
  private final AbstractTextField _q;

  public PasswordValidator(final AbstractTextField var1, final AbstractTextField var2, final AbstractTextField var3) {
    super(var1);
    this._o = var3;
    this._q = var2;
  }

  private static boolean a623nc(final String var0) {
    final char var1 = var0.charAt(0);
    return IntStream.range(1, var0.length()).noneMatch(var2 -> var1 != var0.charAt(var2));
  }

  @SuppressWarnings("BooleanMethodIsAlwaysInverted")
  private static boolean a014na(final String var0, final String var2a) {
    final String var2 = a229fr(var2a);
    final String var3 = a005dn(var2);
    return var0.contains(var2) || var0.contains(var3);
  }

  private static boolean a014gi(final String var0, final String var2) {
    if (a896hb(var0)) {
      return false;
    } else if (a623nc(var0)) {
      return false;
    } else if (a988cg(var0)) {
      return false;
    } else if (var2.length() == 0) {
      return true;
    } else if (a014va(var2, var0)) {
      return false;
    } else if (a405ms(var0, var2)) {
      return false;
    } else {
      return !a014na(var0, var2);
    }
  }

  private static boolean a896hb(final String var0) {
    for (int var1 = 0; var1 < var0.length(); ++var1) {
      final char var2 = var0.charAt(var1);
      if (!Strings.isAlpha(var2) && !Strings.isDigit(var2)) {
        return true;
      }
    }

    return false;
  }

  private static String a005dn(final String var0) {
    final int var1 = var0.length();
    final char[] var2 = new char[var1];

    for (int var3 = 0; var1 > var3; ++var3) {
      var2[-var3 + var1 - 1] = var0.charAt(var3);
    }

    return new String(var2);
  }

  private static boolean a988cg(final String var0) {
    return var0 == null || var0.length() < _npo || var0.length() > 20;
  }

  private static boolean a014va(final String var0, final String var2) {
    final String var3 = a005dn(var0);
    if (var2.contains(var0) || var2.contains(var3)) {
      return true;
    } else {
      return var2.startsWith(var0) || var2.startsWith(var3) || var2.endsWith(var0) || var2.endsWith(var3);
    }
  }

  private static boolean a405ms(final String var0, String var2) {
    var2 = a229fr(var2);
    final String var3 = a005dn(var0);
    return var2.contains(var0) || var2.contains(var3);
  }

  private static String a229fr(final String var0) {
    final int var4 = var0.length();
    final int var5 = "".length();
    int var6 = var4;
    final int var7 = var5 - 1;
    int var8 = 0;

    while (true) {
      var8 = var0.indexOf('_', var8);
      if (var8 < 0) {
        break;
      }

      var6 += var7;
      ++var8;
    }

    final StringBuilder var11 = new StringBuilder(var6);
    int var9 = 0;

    while (true) {
      final int var10 = var0.indexOf('_', var9);
      if (var10 < 0) {
        var11.append(var0.substring(var9));
        return var11.toString();
      }

      var11.append(var0, var9, var10);
      var9 = var10 + 1;
    }
  }

  private boolean b213(final String var2) {
    final String var3 = this._o.text.toLowerCase();
    final String var4 = var2.toLowerCase();
    if (var3.length() > 0 && var4.length() > 0) {
      final int var5 = var3.lastIndexOf("@");
      if (var5 >= 0 && var5 < var3.length() - 1) {
        final String var6 = var3.substring(0, var5);
        final String var7 = var3.substring(var5 + 1);
        if (var4.contains(var6)) {
          return true;
        }

        return var4.contains(var7);
      }
    }

    return false;
  }

  @Override
  public ValidationState b492(final String var2) {
    final String var3 = this._q.text.toLowerCase();
    final String var4 = var2.toLowerCase();
    if (var4.length() == 0) {
      return ValidationState.INVALID;
    } else if (a014gi(var4, var3)) {
      return !this.b213(var2) ? ValidationState.C2 : ValidationState.INVALID;
    } else {
      return ValidationState.INVALID;
    }
  }

  @Override
  public String a751(final String var2) {
    final String var3 = this._q.text.toLowerCase();
    final String var4 = var2.toLowerCase();
    if (var4.length() == 0) {
      return null;
    } else if (a988cg(var4)) {
      return StringConstants.CREATE_ALERT_PASS_LENGTH;
    } else if (a896hb(var4)) {
      return StringConstants.CREATE_ALERT_PASSCHARS;
    } else if (a623nc(var4)) {
      return StringConstants.CREATE_ALERT_PASS_REPEATED;
    } else if (this.b213(var2)) {
      return StringConstants.CREATE_ALERT_PASS_CONTAINS_EMAIL;
    } else if (var3.length() > 0) {
      if (a014va(var3, var4)) {
        return StringConstants.CREATE_ALERT_PASS_CONTAINS_NAME;
      } else if (a405ms(var4, var3)) {
        return StringConstants.CREATE_ALERT_PASS_CONTAINS_NAME_PARTIAL;
      } else {
        return !a014na(var4, var3) ? StringConstants.CREATE_ALERT_PASS_LENGTH : StringConstants.CREATE_ALERT_PASS_CONTAINS_NAME;
      }
    } else {
      return StringConstants.PASSWORD_IS_VALID;
    }
  }
}
