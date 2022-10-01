package funorb.commonui.form.validator;

import funorb.Strings;
import funorb.commonui.form.field.AbstractDateField;
import funorb.commonui.g_;
import funorb.commonui.AbstractTextField;
import funorb.commonui.listener.TextFieldListener;
import funorb.shatteredplans.StringConstants;

import java.util.Date;

public final class DateOfBirthValidator extends AbstractInputValidator implements TextFieldListener, g_ {
  private static final int[] _jab = new int[]{31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};
  public AbstractDateField _m;

  public DateOfBirthValidator() {
  }

  private static boolean a648tg(final int var0, final int var1, final int var2) {
    if (var1 >= 0 && var1 <= 11) {
      return var0 >= 1 && a666oq(var2, var1) >= var0;
    } else {
      return false;
    }
  }

  private static int a666oq(final int var0, final int var1) {
    return var1 == 1 && a560qe(var0) ? 29 : _jab[var1];
  }

  private static boolean a560qe(final int var0) {
    if (var0 < 0) {
      return (1 + var0) % 4 == 0;
    } else if (var0 < 1582) {
      return var0 % 4 == 0;
    } else if (var0 % 4 != 0) {
      return false;
    } else if (var0 % 100 == 0) {
      return var0 % 400 == 0;
    } else {
      return true;
    }
  }

  private static int e137() {
    return (new Date()).getYear() + 1900;
  }

  @Override
  public void handleTextFieldChanged() {
    this.b150();
  }

  @Override
  public String c983() {
    try {
      final int var2 = this._m.d474();
      final int var3 = this._m.f410();

      final int var4 = this._m.l137();
      final int var5 = e137();
      if (var4 < 1890 || var4 > var5 - 3) {
        return Strings.format(StringConstants.CREATE_ALERT_YEAR_RANGE, "1890", Integer.toString(var5 - 3));
      }

      if (a648tg(var2, var3, var4)) {
        return null;
      }
    } catch (final NumberFormatException var6) {
    }

    return StringConstants.CREATE_ALERT_INVALID_DATE;
  }

  @Override
  public ValidationState a083() {
    try {
      final int var2 = this._m.d474();
      final int var3 = this._m.f410();
      final int var4 = this._m.l137();
      final int var5 = e137();
      if (var4 < 1890) {
        return ValidationState.INVALID;
      }

      if (var5 - 3 < var4) {
        return ValidationState.INVALID;
      }

      if (!a648tg(var2, var3, var4)) {
        return ValidationState.INVALID;
      }
    } catch (final NumberFormatException var6) {
      return ValidationState.INVALID;
    }

    return ValidationState.C2;
  }

  @Override
  public void handleTextFieldEnterPressed(final AbstractTextField textField) {
  }

  @Override
  public boolean a154() {
    return this._m.k154();
  }
}
