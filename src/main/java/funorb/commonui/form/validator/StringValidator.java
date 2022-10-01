package funorb.commonui.form.validator;

import funorb.commonui.AbstractTextField;
import funorb.commonui.listener.TextFieldListener;

public abstract class StringValidator extends AbstractInputValidator implements TextFieldListener {
  private final AbstractTextField _i;

  protected StringValidator(final AbstractTextField var1) {
    this._i = var1;
  }

  @Override
  public final boolean a154() {
    return this._i.text == null || this._i.text.length() == 0;
  }

  @Override
  public final ValidationState a083() {
    return this.b492(this._i.text);
  }

  protected abstract ValidationState b492(String var2);

  protected abstract String a751(String var2);

  @Override
  public final void handleTextFieldEnterPressed(final AbstractTextField textField) {
  }

  @Override
  public final void handleTextFieldChanged() {
    this.b150();
  }

  @Override
  public final String c983() {
    return this.a751(this._i.text);
  }
}
