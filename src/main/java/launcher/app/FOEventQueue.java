package launcher.app;

import org.intellij.lang.annotations.MagicConstant;

import java.awt.AWTEvent;
import java.awt.EventQueue;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.InputEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;

public final class FOEventQueue extends EventQueue {
  @MagicConstant(flagsFromClass = InputEvent.class)
  private static int modifiers(final MouseEvent event) {
    int modifiers = event.getModifiers() | event.getModifiersEx();
    if (event.getButton() != 0) modifiers |= InputEvent.getMaskForButton(event.getButton());
    // add incidentally same modifiers in new representation
    modifiers |= (modifiers & (MouseEvent.META_MASK | MouseEvent.ALT_MASK)) << 6;
    return modifiers;
  }

  private static MouseEvent fixEvent(final MouseEvent me) {
    if (me instanceof MouseWheelEvent mwe) {
      return new MouseWheelEvent(me.getComponent(), me.getID(), me.getWhen(), modifiers(me),
          me.getX(), me.getY(), me.getXOnScreen(), me.getYOnScreen(), me.getClickCount(),
          me.isPopupTrigger(), mwe.getScrollType(), mwe.getScrollAmount(),
          mwe.getWheelRotation(), mwe.getPreciseWheelRotation());
    } else {
      return new MouseEvent(me.getComponent(), me.getID(), me.getWhen(), modifiers(me),
          me.getX(), me.getY(), me.getXOnScreen(), me.getYOnScreen(), me.getClickCount(),
          me.isPopupTrigger(), me.getButton());
    }
  }

  private static FOEventQueue foQueue;

  @SuppressWarnings("UnusedReturnValue")
  public static synchronized FOEventQueue getQueue() {
    if (foQueue == null) {
      foQueue = new FOEventQueue();
      Toolkit.getDefaultToolkit().getSystemEventQueue().push(foQueue);
    }
    return foQueue;
  }

  @SuppressWarnings("InterfaceNeverImplemented")
  public interface IOnRefresh {
    @SuppressWarnings("unused")
    void onRefresh();
  }

  private IOnRefresh onRefresh;

  @SuppressWarnings("unused")
  public void setOnRefresh(final IOnRefresh onRefresh) {
    this.onRefresh = onRefresh;
  }

  @Override
  public void postEvent(final AWTEvent event) {
    if (this.onRefresh != null && event instanceof ActionEvent) {
      if ("dummy".equals(((ActionEvent) event).getActionCommand())) {
        try {
          this.onRefresh.onRefresh();
        } catch (final Exception e) {
          e.printStackTrace();
          this.onRefresh = null;
        }
      }
    }
    super.postEvent(event);
  }

  @Override
  protected void dispatchEvent(final AWTEvent event) {
    if (event instanceof MouseEvent me) {
      final MouseEvent fake = fixEvent(me);
      super.dispatchEvent(fake);
      if (fake.isConsumed()) me.consume();
    } else super.dispatchEvent(event);
  }
}
