package funorb.awt;

import funorb.client.JagexBaseApplet;
import funorb.util.PseudoMonotonicClock;
import org.intellij.lang.annotations.MagicConstant;

import javax.swing.SwingUtilities;
import java.awt.Component;
import java.awt.Point;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.geom.AffineTransform;
import java.awt.geom.NoninvertibleTransformException;

public final class MouseState implements MouseListener, MouseMotionListener, FocusListener {
  public static final MouseState instance = new MouseState();

  @MagicConstant(valuesFromClass = Button.class)
  public static volatile int mouseButtonJustClicked = Button.NONE;
  @MagicConstant(valuesFromClass = Button.class)
  public static volatile int mouseButtonDown = Button.NONE;
  public static volatile int mouseX = -1;
  public static volatile int mouseY = -1;
  public static volatile int pressX = 0;
  public static volatile int pressY = 0;
  public static volatile int ticksSinceLastMouseEvent = 0;
  public static volatile boolean mouseEventReceived = false;

  public void attach(final Component c) {
    c.addMouseListener(this);
    c.addMouseMotionListener(this);
    c.addFocusListener(this);
  }

  public void detach(final Component c) {
    c.removeMouseListener(this);
    c.removeMouseMotionListener(this);
    c.removeFocusListener(this);
    mouseButtonDown = Button.NONE;
  }

  private static Point transform(final MouseEvent event) {
    final AffineTransform transform = JagexBaseApplet.getTransform(event.getComponent());
    final Point result = new Point();
    try {
      transform.inverseTransform(event.getPoint(), result);
    } catch (final NoninvertibleTransformException e) {
      throw new RuntimeException(e);
    }
    return result;
  }

  @Override
  public synchronized void mouseEntered(final MouseEvent var1) {
    if (instance != null) {
      ticksSinceLastMouseEvent = 0;
      mouseX = var1.getX();
      mouseY = var1.getY();
      mouseEventReceived = true;
    }
  }

  @Override
  public void focusGained(final FocusEvent var1) {
  }

  @Override
  public void mouseClicked(final MouseEvent var1) {
    if (var1.isPopupTrigger()) {
      var1.consume();
    }

  }

  @Override
  public synchronized void mouseMoved(final MouseEvent var1) {
    if (instance != null) {
      ticksSinceLastMouseEvent = 0;
      final Point point = transform(var1);
      mouseX = point.x;
      mouseY = point.y;
      mouseEventReceived = true;
    }

  }

  @Override
  public synchronized void mouseDragged(final MouseEvent var1) {
    if (instance != null) {
      ticksSinceLastMouseEvent = 0;
      final Point point = transform(var1);
      mouseX = point.x;
      mouseY = point.y;
      mouseEventReceived = true;
    }

  }

  @Override
  public synchronized void mouseExited(final MouseEvent var1) {
    if (instance != null) {
      ticksSinceLastMouseEvent = 0;
      mouseX = -1;
      mouseY = -1;
      mouseEventReceived = true;
    }
  }

  @Override
  public synchronized void mouseReleased(final MouseEvent var1) {
    if (instance != null) {
      ticksSinceLastMouseEvent = 0;
      mouseButtonDown = Button.NONE;
      mouseEventReceived = true;

    }

    if (var1.isPopupTrigger()) {
      var1.consume();
    }
  }

  @Override
  public synchronized void mousePressed(final MouseEvent var1) {
    if (instance != null) {
      ticksSinceLastMouseEvent = 0;
      final Point point = transform(var1);
      pressX = point.x;
      pressY = point.y;
      PseudoMonotonicClock.currentTimeMillis();
      if (SwingUtilities.isRightMouseButton(var1)) {
        mouseButtonJustClicked = Button.RIGHT;
        mouseButtonDown = Button.RIGHT;
      } else {
        mouseButtonJustClicked = Button.LEFT;
        mouseButtonDown = Button.LEFT;
      }

      mouseEventReceived = true;

    }

    if (var1.isPopupTrigger()) {
      var1.consume();
    }
  }

  @Override
  public synchronized void focusLost(final FocusEvent var1) {
    if (instance != null) {
      mouseButtonDown = Button.NONE;
    }

  }

  public static final class Button {
    public static final int NONE = 0;
    public static final int LEFT = 1;
    public static final int RIGHT = 2;
  }
}
