package funorb.commonui.form.validator;

import funorb.commonui.AbstractTextField;
import funorb.commonui.ks_;
import funorb.shatteredplans.StringConstants;

public final class ConfirmEmailValidator extends StringValidator {
  public static ks_ _wha;
  private final ConfirmPasswordValidator _s;
  private boolean _n = false;
  private String _t = "";

  public ConfirmEmailValidator(final AbstractTextField var1, final AbstractTextField var2) {
    super(var1);
    this._s = new ConfirmPasswordValidator(var1, var2);
  }

  private static ks_ a661os(final String var1) {
    if (_wha.b154() && !var1.equals(_wha.a738())) {
      _wha = new ks_(var1);
    }
    return _wha;
  }

  @Override
  public String a751(final String var2) {
    if (this._s.b492(var2) == ValidationState.INVALID) {
      return this._s.a751(var2);
    } else {
      return this.b492(var2) != ValidationState.INVALID ? StringConstants.CREATE_EMAIL_VALID : StringConstants.CREATE_ALERT_EMAIL_UNAVAILABLE;
    }
  }

  @Override
  protected ValidationState b492(final String var2) {
    if (this._s.b492(var2) == ValidationState.INVALID) {
      return ValidationState.INVALID;
    } else {
      if (!var2.equals(this._t)) {
        final ks_ var3 = a661os(var2);
        if (!var3.b154()) {
          return ValidationState.CHECKING_1;
        }

        this._t = var2;
        this._n = var3.a491();
      }

      return this._n ? ValidationState.C2 : ValidationState.INVALID;
    }
  }

}
