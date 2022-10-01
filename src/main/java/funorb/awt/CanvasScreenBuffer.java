package funorb.awt;

import java.awt.Component;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.awt.image.DirectColorModel;
import java.awt.image.Raster;
import java.awt.image.WritableRaster;
import java.util.Hashtable;

public final class CanvasScreenBuffer extends ScreenBuffer {
  private Component canvas;

  public CanvasScreenBuffer() {
  }

  @Override
  public void paint(final Graphics g) {
    g.drawImage(this.screenBufferImage, 0, 0, this.canvas);
  }

  @Override
  public void initialize(final Component canvas, final int width, final int height) {
    this.canvas = canvas;
    this.width = width;
    this.height = height;
    this.screenBuffer = new int[width * height + 1];
    final DataBufferInt dataBuffer = new DataBufferInt(this.screenBuffer, this.screenBuffer.length);
    final DirectColorModel colorModel = new DirectColorModel(32, 0xff0000, 0x00ff00, 0x0000ff);
    final WritableRaster raster = Raster.createWritableRaster(colorModel.createCompatibleSampleModel(super.width, super.height), dataBuffer, null);
    this.screenBufferImage = new BufferedImage(colorModel, raster, false, new Hashtable<>());
    this.makeGlobal();
  }
}
