package funorb.commonui.form.validator;

import funorb.util.PseudoMonotonicClock;

public abstract class AbstractInputValidator implements InputValidator {
  private long _b;

  protected abstract ValidationState a083();

  @Override
  public final String a983() {
    if (this.a154()) {
      return null;
    } else {
      return PseudoMonotonicClock.currentTimeMillis() >= this._b + 350L ? this.c983() : null;
    }
  }

  @Override
  public final void b150() {
    this._b = PseudoMonotonicClock.currentTimeMillis();
  }

  @Override
  public final ValidationState validate() {
    if (this.a154()) {
      return ValidationState.C5;
    } else {
      return this._b + 350L <= PseudoMonotonicClock.currentTimeMillis() ? this.a083() : ValidationState.CHECKING_2;
    }
  }

  protected abstract String c983();
}
