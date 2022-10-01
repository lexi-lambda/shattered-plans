package funorb.awt;

import funorb.graphics.Drawing;

import java.awt.Component;
import java.awt.Graphics;
import java.awt.Image;

public abstract class ScreenBuffer {
  protected int[] screenBuffer;
  public int width;
  protected Image screenBufferImage;
  public int height;

  public abstract void initialize(Component canvas, int width, int height);

  protected final void makeGlobal() {
    Drawing.initialize(this.screenBuffer, this.width, this.height);
  }

  public abstract void paint(Graphics var1);
}
