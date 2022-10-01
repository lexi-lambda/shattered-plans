package funorb.commonui;

import funorb.commonui.renderer.CheckboxRenderer;
import funorb.commonui.renderer.ComponentRenderer;

public final class Checkbox extends Button {
  @SuppressWarnings("SameParameterValue")
  public Checkbox(final boolean active) {
    this(new CheckboxRenderer(), active);
  }

  @SuppressWarnings("SameParameterValue")
  private Checkbox(final ComponentRenderer renderer, final boolean active) {
    super("", renderer, null);
    this.active = active;
  }

  @Override
  public void handleClicked(final int var1, final int var2, final int var3) {
    this.active = !this.active;
    super.handleClicked(var1, var2, var3);
  }
}
