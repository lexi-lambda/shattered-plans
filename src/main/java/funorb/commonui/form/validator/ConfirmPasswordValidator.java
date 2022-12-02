package funorb.commonui.form.validator;

import funorb.commonui.form.field.InputField;
import funorb.commonui.AbstractTextField;
import funorb.shatteredplans.StringConstants;

public final class ConfirmPasswordValidator extends StringValidator {
  private final AbstractTextField _n;

  public ConfirmPasswordValidator(final AbstractTextField var1, final AbstractTextField var2) {
    super(var1);
    this._n = var2;
  }

  @Override
  public String a751(final String var2) {
    if (this._n instanceof InputField) {
      final InputValidator var3 = ((InputField) this._n).getValidator();
      if (var3 != null) {
        if (var3.validate() == ValidationState.C2 && !var2.equals(this._n.text)) {
          return StringConstants.CREATE_ALERT_MISMATCH;
        }

        return var3.getTooltip();
      }
    }

    return var2.equals(this._n.text) ? null : StringConstants.CREATE_ALERT_MISMATCH;
  }

  @Override
  public ValidationState b492(final String var2) {
    if (this._n instanceof InputField) {
      final InputValidator var3 = ((InputField) this._n).getValidator();
      if (var3 != null && var3.validate() != ValidationState.C2) {
        return ValidationState.INVALID;
      }
    }

    return var2.equals(this._n.text) ? ValidationState.C2 : ValidationState.INVALID;
  }

}
