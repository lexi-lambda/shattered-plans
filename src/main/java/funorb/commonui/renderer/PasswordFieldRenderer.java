package funorb.commonui.renderer;

import funorb.commonui.Component;
import funorb.commonui.Resources;
import funorb.graphics.Font;

import java.util.Arrays;

public final class PasswordFieldRenderer extends TextFieldRenderer {
  private PasswordFieldRenderer(final Font font) {
    super(font);
  }

  public PasswordFieldRenderer() {
    this(Resources.AREZZO_14);
  }

  @Override
  protected String getText(final Component component) {
    final int var1 = component.text.length();
    final char[] var21 = new char[var1];
    Arrays.fill(var21, '*');
    return new String(var21);
  }
}
