package funorb.commonui.form.field;

import funorb.commonui.form.validator.DateOfBirthValidator;
import funorb.commonui.form.validator.InputValidator;
import funorb.commonui.container.ListContainer;

public abstract class AbstractDateField extends ListContainer implements InputField {
  private DateOfBirthValidator _H;

  protected AbstractDateField() {
    super(0, 0, 0, 0);
  }

  public abstract int d474();

  public abstract int f410();

  @Override
  public final InputValidator getValidator() {
    return this._H;
  }

  protected void a890(final DateOfBirthValidator var1) {
    this._H = var1;
    this._H._m = this;
  }

  public abstract boolean k154();

  public abstract int l137();
}
