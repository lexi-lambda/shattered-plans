package funorb.commonui.form.field;

import funorb.commonui.Component;
import funorb.commonui.form.validator.InputValidator;
import funorb.commonui.AbstractTextField;
import funorb.commonui.listener.ComponentListener;
import funorb.commonui.renderer.ComponentRenderer;
import funorb.commonui.renderer.TextFieldRenderer;
import funorb.commonui.TooltipManager;
import funorb.shatteredplans.client.JagexApplet;

public final class TextField extends AbstractTextField implements InputField {
  private InputValidator validator;
  private int _U;

  public TextField(final String text,  final ComponentListener listener, final int var3) {
    this(text, new TextFieldRenderer(), listener, var3);
  }

  public TextField(final String text, final ComponentRenderer renderer, final ComponentListener listener, final int var3) {
    super(text, renderer, listener, var3);
  }

  @Override
  public void tick(final int x, final int y, final Component root) {
    super.tick(x, y, root);
    this._U = JagexApplet.mouseX - (this.x + x);
  }

  @Override
  public String getCurrentTooltip() {
    if (this.isMouseOver && this.tooltip != null) {
      TooltipManager.INSTANCE.setMousePosition(this.width - this._U + JagexApplet.mouseX, JagexApplet.mouseY);
      return this.tooltip;
    } else {
      return null;
    }
  }

  @Override
  protected void i150() {
    super.i150();
    if (this.validator != null) {
      this.validator.b150();
    }
  }

  public void setValidator(final InputValidator validator) {
    this.validator = validator;
  }

  @Override
  public InputValidator getValidator() {
    return this.validator;
  }
}
