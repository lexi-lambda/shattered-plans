package funorb.awt;

import java.awt.Component;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;

public final class MouseWheelState implements MouseWheelListener {
  public static final MouseWheelState instance = new MouseWheelState();
  private int rotation = 0;

  public void attach(final Component c) {
    c.addMouseWheelListener(this);
  }

  public void detach(final Component c) {
    c.removeMouseWheelListener(this);
  }

  @Override
  public synchronized void mouseWheelMoved(final MouseWheelEvent e) {
    this.rotation += e.getWheelRotation();
    e.consume();
  }

  public synchronized int poll() {
    final int rotation = this.rotation;
    this.rotation = 0;
    return rotation;
  }
}
