package launcher.app;

import launcher.CommandLineOptions;
import launcher.ShatteredPlansLauncher;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import java.applet.Applet;
import java.applet.AppletContext;
import java.applet.AppletStub;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public final class FOStub implements AppletStub {
  private final JFrame frame;
  private final Applet app;
  private boolean active;
  private Insets insets;

  private final Map<String, String> params;
  private final URL docBase;
  private final URL codeBase;
  private final Set<String> visited;
  private final FOContext context;

  public FOStub(final CommandLineOptions options, final JFrame frame, final Applet app, final Map<String, String> params) {
    this.frame = frame;
    this.app = app;
    this.active = true;
    frame.addComponentListener(new ComponentAdapter() {
      @Override
      public void componentResized(final ComponentEvent e) {
        FOStub.this.checkInsets(frame.getInsets());
      }
    });

    this.params = params;
    try {
      final String game = ShatteredPlansLauncher.GAME_ID;
      this.docBase = new URL("http://funorb.com/g=" + game + "/game.ws?js=1");
      this.codeBase = new URL("http://" + options.serverAddress.getHostString() + "/g=" + game + "/client.jar");
    } catch (final MalformedURLException e) {
      throw new RuntimeException("invalid host", e);
    }
    this.visited = new HashSet<>();
    this.context = new FOContext(this, app);
  }

  public Frame getFrame() {
    return this.frame;
  }

  public void stop() {
    if (this.active) {
      synchronized (this) {
        if (!this.active) return;
        else this.active = false;
      }
      this.app.stop();
    }
  }

  @Override
  public boolean isActive() {
    return this.active;
  }

  @Override
  public String getParameter(final String name) {
    if (this.visited.add(name)) {
      System.out.println("PARAM " + name + " = " + this.params.get(name));
    }
    return this.params.get(name);
  }

  @Override
  public URL getDocumentBase() {
    return this.docBase;
  }

  @Override
  public URL getCodeBase() {
    return this.codeBase;
  }

  @Override
  public AppletContext getAppletContext() {
    return this.context;
  }

  @Override
  public void appletResize(final int width, final int height) {
    SwingUtilities.invokeLater(() ->
    {
      System.out.println("RESIZE " + width + " " + height);
      this.app.setPreferredSize(new Dimension(width, height));
      this.adjustFrameBounds();
    });
  }

  private void checkInsets(final Insets ins) {
    if (this.insets != null && !ins.equals(this.insets)) {
      final int dw = ins.left - this.insets.left + ins.right - this.insets.right;
      final int dh = ins.top - this.insets.top + ins.bottom - this.insets.bottom;
      final Rectangle bounds = this.frame.getBounds();
      bounds.x -= dw / 2;
      bounds.y -= dh / 2;
      bounds.width += dw;
      bounds.height += dh;
      this.frame.setBounds(bounds);
    }
    this.insets = ins;
  }

  private void adjustFrameBounds() {
    if (SwingUtilities.isEventDispatchThread()) {
      final Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
      int x = this.frame.getX(), y = this.frame.getY();
      final Dimension pSize = this.frame.isVisible() ? this.frame.getSize() : screen;
      this.frame.pack();
      this.insets = this.frame.getInsets();
      final Dimension nSize = this.frame.getSize();
      x += (pSize.width - nSize.width) / 2;
      y += (pSize.height - nSize.height) / 2;
      x = Math.max(0, Math.min(x, screen.width - nSize.width));
      y = Math.max(0, Math.min(y, screen.height - nSize.height));
      this.checkInsets(this.frame.getInsets());
      this.frame.setLocation(x, y);
    } else {
      SwingUtilities.invokeLater(this::adjustFrameBounds);
    }
  }
}
