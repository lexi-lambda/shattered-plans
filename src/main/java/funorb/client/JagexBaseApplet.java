package funorb.client;

import funorb.awt.CanvasScreenBuffer;
import funorb.awt.ImageProducerScreenBuffer;
import funorb.awt.ScreenBuffer;
import funorb.awt.ComponentCanvas;
import funorb.shatteredplans.client.FrameClock;
import funorb.shatteredplans.client.MessagePumpThread;
import funorb.shatteredplans.client.ShatteredPlansClient;
import funorb.util.PseudoMonotonicClock;

import java.applet.Applet;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.Rectangle;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.awt.geom.AffineTransform;
import java.net.URL;

public abstract class JagexBaseApplet extends Applet implements Runnable, FocusListener, WindowListener {
  protected static final long[] recentFrameTimes = new long[32];
  protected static JagexBaseApplet instance; // we are a singleton
  public static Canvas canvas;
  protected static volatile boolean _ncc = false;
  protected static boolean hadFocus; // honestly not quite sure how this differs from hasFocus
  protected static FrameClock frameClock;
  protected static int framesToAdvance;
  public static int lastFrameScore = 0; // not entirely sure how the absolute value of this is supposed to be interpreted
  protected static int[] _f;
  public static volatile boolean _oqe = true;
  private static volatile long _pvcm = 0L;
  public static ScreenBuffer screenBuffer;
  private static boolean isShutdown = false;
  private static long shutdownTimestamp = 0L;
  private static int recentFrameIndex = 0;
  public int gameCrc;
  public boolean didHandleError = false;
  private volatile boolean hasFocus = true;
  private int canvasSyncTimer = 0;

  protected JagexBaseApplet() {}

  public static JagexBaseApplet getInstance() {
    return instance;
  }

  public static int[] a612(final int[] var0) {
    final int var1 = var0.length;
    final int[] var2 = new int[var1];

    int var3 = 0;
    while (var3 < var1) {
      var2[var0[var3]] = var3++;
    }

    return var2;
  }

  public static void maybeSleep(final long var0) {
    if (var0 > 0L) {
      try {
        Thread.sleep(var0);
      } catch (final InterruptedException var3) {
      }
    }
  }

  @SuppressWarnings("SameParameterValue")
  private static ScreenBuffer initializeScreenBuffer(final Component canvas, final int width, final int height) {
    try {
      final ScreenBuffer sb = new CanvasScreenBuffer();
      sb.initialize(canvas, width, height);
      return sb;
    } catch (final Throwable e) {
      final ImageProducerScreenBuffer sb = new ImageProducerScreenBuffer();
      sb.initialize(canvas, width, height);
      return sb;
    }
  }

  public static AffineTransform getTransform(final Component canvas) {
    final double scaleX = (double) canvas.getWidth() / ShatteredPlansClient.SCREEN_WIDTH;
    final double scaleY = (double) canvas.getHeight() / ShatteredPlansClient.SCREEN_HEIGHT;
    final double scale = Math.min(scaleX, scaleY);

    final double offsetX = (canvas.getWidth() - (ShatteredPlansClient.SCREEN_WIDTH * scale)) / 2;
    final double offsetY = (canvas.getHeight() - (ShatteredPlansClient.SCREEN_HEIGHT * scale)) / 2;

    final AffineTransform transform = new AffineTransform();
    transform.translate(offsetX, offsetY);
    transform.scale(scale, scale);
    return transform;
  }

  public static void paint(final Canvas canvas) {
    try {
      final Graphics2D graphics = (Graphics2D) canvas.getGraphics();
      graphics.transform(getTransform(canvas));
      screenBuffer.paint(graphics);
      graphics.dispose();
    } catch (final Exception e) {
      canvas.repaint();
    }
  }

  @Override
  public final void start() {
    if (instance == this && !isShutdown) {
      shutdownTimestamp = 0L;
    }
  }

  @Override
  public final void windowActivated(final WindowEvent var1) {
  }

  @Override
  public final void stop() {
    if (instance == this && !isShutdown) {
      shutdownTimestamp = PseudoMonotonicClock.currentTimeMillis() + 4000L;
    }
  }

  @Override
  public final void windowIconified(final WindowEvent var1) {
  }

  @Override
  public final void windowDeiconified(final WindowEvent var1) {
  }

  protected abstract void tick();

  protected abstract void render();

  @Override
  public final void update(final Graphics var1) {
    this.paint(var1);
  }

  @Override
  public final void windowClosing(final WindowEvent var1) {
    this.destroy();
  }

  @Override
  public final void run() {
    instance.setFocusCycleRoot(true);

    this.initializeCanvas();
    screenBuffer = initializeScreenBuffer(canvas, ShatteredPlansClient.SCREEN_WIDTH, ShatteredPlansClient.SCREEN_HEIGHT);
    this.initialize();
    frameClock = new FrameClock();

    while (shutdownTimestamp == 0L || shutdownTimestamp > PseudoMonotonicClock.currentTimeMillis()) {
      maybeSleep(frameClock.finishFrame());
      framesToAdvance = frameClock.advanceFrame();

      for (int i = 0; i < framesToAdvance; ++i) {
        synchronized (this) {
          hadFocus = this.hasFocus;
        }
        this.tick();
      }

      this.syncAndRender();
      MessagePumpThread.instance.postEvent(canvas);
    }

    this.performShutdown(true);
  }

  @Override
  public final void windowOpened(final WindowEvent var1) {
  }

  @Override
  public final String getParameter(final String var1) {
    return super.getParameter(var1);
  }

  @Override
  public final void focusLost(final FocusEvent var1) {
    this.hasFocus = false;
  }

  @Override
  public final void windowClosed(final WindowEvent var1) {
  }

  @Override
  public final synchronized void paint(final Graphics var1) {
    if (instance == this && !isShutdown) {
      _oqe = true;
      if (PseudoMonotonicClock.currentTimeMillis() - _pvcm > 1000L) {
        final Rectangle var2 = var1.getClipBounds();
        if (var2 == null || var2.width >= ShatteredPlansClient.SCREEN_WIDTH && var2.height >= ShatteredPlansClient.SCREEN_HEIGHT) {
          _ncc = true;
        }
      }
    }
  }

  @Override
  public abstract void init();

  public final void redirectToErrorPage(final String which) {
    if (!this.didHandleError) {
      this.didHandleError = true;
      System.out.println("error_game_" + which);

      try {
        throw new NullPointerException("no Applet#getWindow anymore");
      } catch (final Throwable var5) {
      }
      try {
        this.getAppletContext().showDocument(new URL(this.getCodeBase(), "error_game_" + which + ".ws"), "_top");
      } catch (final Exception var4) {
      }
    }
  }

  protected abstract void initialize();

  private void syncAndRender() {
    final long now = PseudoMonotonicClock.currentTimeMillis();
    final long lastFrameTime = recentFrameTimes[recentFrameIndex];
    recentFrameTimes[recentFrameIndex] = now;
    recentFrameIndex = (recentFrameIndex + 1) % recentFrameTimes.length;
    if (lastFrameTime != 0L && lastFrameTime < now) {
      final int lastFrameDuration = (int) (now - lastFrameTime);
      lastFrameScore = ((lastFrameDuration / 2) + 32000) / lastFrameDuration;
    }

    if (--this.canvasSyncTimer <= 0) {
      this.canvasSyncTimer = 50;
      _oqe = true;
      canvas.setSize(instance.getSize());
      canvas.setVisible(true);
      canvas.setLocation(0, 0);
    }

    this.render();
  }

  @Override
  public final void destroy() {
    if (this == instance && !isShutdown) {
      shutdownTimestamp = PseudoMonotonicClock.currentTimeMillis();
      maybeSleep(5000L);
      MessagePumpThread.instance = null;
      this.performShutdown(false);
    }
  }

  private void performShutdown(final boolean isClean) {
    synchronized (this) {
      if (isShutdown) {
        return;
      }

      isShutdown = true;
    }

    try {
      this.shutdown();
    } catch (final Exception var8) {
    }

    if (canvas != null) {
      try {
        canvas.removeFocusListener(this);
        canvas.getParent().remove(canvas);
      } catch (final Exception var7) {
      }
    }

    if (MessagePumpThread.instance != null) {
      try {
        MessagePumpThread.instance.shutdown();
      } catch (final Exception var6) {
      }
    }

    System.out.println("Shutdown complete - clean:" + isClean);
  }

  @Override
  public final void windowDeactivated(final WindowEvent var1) {
  }

  @Override
  public final void focusGained(final FocusEvent var1) {
    this.hasFocus = true;
    _oqe = true;
  }

  protected abstract void shutdown();

  protected final synchronized void initializeCanvas() {
    if (canvas != null) {
      canvas.removeFocusListener(this);
      canvas.getParent().setBackground(Color.black);
      canvas.getParent().remove(canvas);
    }

    instance.setLayout(new GridLayout());
    canvas = new ComponentCanvas(this);
    instance.add(canvas);
    canvas.setVisible(true);
    canvas.setLocation(0, 0);
    canvas.addFocusListener(this);
    canvas.requestFocus();

    hadFocus = true;
    this.hasFocus = true;

    _oqe = true;
    _ncc = false;
    _pvcm = PseudoMonotonicClock.currentTimeMillis();
  }
}
