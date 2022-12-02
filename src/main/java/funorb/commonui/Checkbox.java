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
  public void handleClicked(final int button, final int x, final int y) {
    this.active = !this.active;
    super.handleClicked(button, x, y);
  }
}
