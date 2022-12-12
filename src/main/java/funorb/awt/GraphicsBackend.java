package funorb.awt;

import java.awt.DisplayMode;
import java.awt.Frame;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;

public final class GraphicsBackend {
  private GraphicsDevice device;
  private DisplayMode mode;

  public GraphicsBackend() throws Exception {
    final GraphicsEnvironment environment = GraphicsEnvironment.getLocalGraphicsEnvironment();
    this.device = environment.getDefaultScreenDevice();
    if (!this.device.isFullScreenSupported()) {
      final GraphicsDevice[] devices = environment.getScreenDevices();
      for (final GraphicsDevice device : devices) {
        if (device != null && device.isFullScreenSupported()) {
          this.device = device;
          return;
        }
      }

      throw new Exception();
    }
  }

  public int[] listmodes() {
    final DisplayMode[] modes = this.device.getDisplayModes();
    final int[] values = new int[modes.length << 2];

    for (int i = 0; i < modes.length; ++i) {
      values[i << 2] = modes[i].getWidth();
      values[(i << 2) + 1] = modes[i].getHeight();
      values[(i << 2) + 2] = modes[i].getBitDepth();
      values[(i << 2) + 3] = modes[i].getRefreshRate();
    }

    return values;
  }

  public void enter(final Frame frame, final int width, final int height, final int bitDepth, int refreshRate) {
    this.mode = this.device.getDisplayMode();
    if (this.mode == null) {
      throw new NullPointerException();
    } else {
      frame.setUndecorated(true);
      frame.enableInputMethods(false);

      this.device.setFullScreenWindow(frame);
      if (refreshRate == 0) {
        final int defaultRefresh = this.mode.getRefreshRate();
        final DisplayMode[] modes = this.device.getDisplayModes();
        boolean foundRefresh = false;

        for (final DisplayMode mode : modes) {
          if (width == mode.getWidth() && height == mode.getHeight() && mode.getBitDepth() == bitDepth) {
            final int modeRefresh = mode.getRefreshRate();
            if (!foundRefresh || Math.abs(-defaultRefresh + modeRefresh) < Math.abs(-defaultRefresh)) {
              foundRefresh = true;
              refreshRate = modeRefresh;
            }
          }
        }

        if (!foundRefresh) {
          refreshRate = defaultRefresh;
        }
      }

      this.device.setDisplayMode(new DisplayMode(width, height, bitDepth, refreshRate));
    }
  }

  public void exit() {
    if (this.mode != null) {
      this.device.setDisplayMode(this.mode);
      if (!this.device.getDisplayMode().equals(this.mode)) {
        throw new RuntimeException("");
      }

      this.mode = null;
    }

    this.device.setFullScreenWindow(null);
  }
}
