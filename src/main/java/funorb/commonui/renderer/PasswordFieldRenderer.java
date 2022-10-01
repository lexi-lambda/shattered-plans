package funorb.commonui.renderer;

import funorb.commonui.Component;
import funorb.commonui.Resources;
import funorb.graphics.Font;

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

    for (int var3 = 0; var3 < var1; ++var3) {
      var21[var3] = '*';
    }

    return new String(var21);
  }
}
