package funorb.awt;

import java.awt.Canvas;
import java.awt.Component;
import java.awt.Graphics;

public final class ComponentCanvas extends Canvas {
  private final Component component;

  public ComponentCanvas(final Component component) {
    this.component = component;
  }

  @Override
  public void paint(final Graphics g) {
    this.component.paint(g);
  }

  @Override
  public void update(final Graphics g) {
    this.component.update(g);
  }
}
