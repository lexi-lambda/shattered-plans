package funorb.awt;

import java.awt.Component;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.ColorModel;
import java.awt.image.DirectColorModel;
import java.awt.image.ImageConsumer;
import java.awt.image.ImageObserver;
import java.awt.image.ImageProducer;

public final class ImageProducerScreenBuffer extends ScreenBuffer implements ImageProducer, ImageObserver {
  private ImageConsumer consumer;
  private ColorModel colorModel;

  @Override
  public void requestTopDownLeftRightResend(final ImageConsumer consumer) {
  }

  @Override
  public synchronized boolean isConsumer(final ImageConsumer consumer) {
    return consumer == this.consumer;
  }

  @Override
  public boolean imageUpdate(final Image var1, final int var2, final int var3, final int var4, final int var5, final int var6) {
    return true;
  }

  private synchronized void pushFrame() {
    if (this.consumer != null) {
      this.consumer.setPixels(0, 0, this.width, this.height, this.colorModel, this.screenBuffer, 0, this.width);
      this.consumer.imageComplete(ImageConsumer.SINGLEFRAMEDONE);
    }
  }

  @Override
  public void initialize(final Component canvas, final int width, final int height) {
    this.height = height;
    this.width = width;
    this.screenBuffer = new int[height * width + 1];
    this.colorModel = new DirectColorModel(32, 16711680, 65280, 255);
    this.screenBufferImage = canvas.createImage(this);
    this.pushFrame();
    canvas.prepareImage(this.screenBufferImage, this);
    this.pushFrame();
    canvas.prepareImage(this.screenBufferImage, this);
    this.pushFrame();
    canvas.prepareImage(this.screenBufferImage, this);
    this.makeGlobal();
  }

  @Override
  public synchronized void removeConsumer(final ImageConsumer consumer) {
    if (consumer == this.consumer) {
      this.consumer = null;
    }
  }

  @Override
  public void startProduction(final ImageConsumer consumer) {
    this.addConsumer(consumer);
  }

  @Override
  public synchronized void addConsumer(final ImageConsumer consumer) {
    this.consumer = consumer;
    consumer.setDimensions(this.width, this.height);
    consumer.setProperties(null);
    consumer.setColorModel(this.colorModel);
    consumer.setHints(14);
  }

  @Override
  public void paint(final Graphics g) {
    this.pushFrame();
    g.drawImage(this.screenBufferImage, 0, 0, this);
  }
}
