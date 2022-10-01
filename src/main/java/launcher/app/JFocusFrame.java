package launcher.app;

import javax.swing.JFrame;
import java.awt.Window;
import java.io.Serial;

public final class JFocusFrame extends JFrame {
  @Serial
  private static final long serialVersionUID = 8553246263288991190L;

  private Window preferFocus;

  @SuppressWarnings("SameParameterValue")
  public JFocusFrame(final String title) {
    super(title);
  }

  @SuppressWarnings("unused")
  public void setPreferFocus(final Window preferFocus) {
    this.preferFocus = preferFocus;
  }

  @Override
  public boolean getFocusableWindowState() {
    return super.getFocusableWindowState() && (this.preferFocus == null || !this.preferFocus.isFocused());
  }
}
