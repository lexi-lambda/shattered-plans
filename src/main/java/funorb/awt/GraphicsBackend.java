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

  public void enter(final Frame var1, final int var2, final int var3, final int var4, int var5) {
    this.mode = this.device.getDisplayMode();
    if (this.mode == null) {
      throw new NullPointerException();
    } else {
      var1.setUndecorated(true);
      var1.enableInputMethods(false);

      this.device.setFullScreenWindow(var1);
      if (var5 == 0) {
        final int var6 = this.mode.getRefreshRate();
        final DisplayMode[] var7 = this.device.getDisplayModes();
        boolean var8 = false;

        for (final DisplayMode displayMode : var7) {
          if (var2 == displayMode.getWidth() && var3 == displayMode.getHeight() && displayMode.getBitDepth() == var4) {
            final int var10 = displayMode.getRefreshRate();
            if (!var8 || Math.abs(-var6 + var10) < Math.abs(-var6)) {
              var8 = true;
              var5 = var10;
            }
          }
        }

        if (!var8) {
          var5 = var6;
        }
      }

      this.device.setDisplayMode(new DisplayMode(var2, var3, var4, var5));
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
