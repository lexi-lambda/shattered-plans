package funorb.commonui.form.validator;

import funorb.Strings;
import funorb.commonui.AbstractTextField;
import funorb.shatteredplans.StringConstants;

public final class AgeValidator extends StringValidator {
  public AgeValidator(final AbstractTextField var1) {
    super(var1);
  }

  @Override
  public String a751(final String var2) {
    return this.b492(var2) != ValidationState.INVALID ? null : StringConstants.CREATE_ALERT_INVALID_AGE;
  }

  @Override
  protected ValidationState b492(final String var2) {
    if (Strings.a783wk(var2)) {
      final int var3 = Strings.parseDecimalInteger(var2);
      return var3 > 0 && var3 <= 130 ? ValidationState.C2 : ValidationState.INVALID;
    } else {
      return ValidationState.INVALID;
    }
  }
}
