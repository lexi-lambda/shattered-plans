package launcher.app;

import java.applet.Applet;
import java.applet.AppletContext;
import java.applet.AudioClip;
import java.awt.Image;
import java.io.InputStream;
import java.net.URL;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Objects;

public final class FOContext implements AppletContext {
  private final FOStub stub;
  private final Applet app;

  public FOContext(final FOStub stub, final Applet app) {
    this.stub = stub;
    this.app = app;
  }

  @Override
  public AudioClip getAudioClip(final URL url) {
    System.out.println("CLIP " + url);
    throw new SecurityException();
  }

  @Override
  public Image getImage(final URL url) {
    System.out.println("IMG " + url);
    throw new SecurityException();
  }

  @Override
  public Applet getApplet(final String name) {
    System.out.println("APPLET " + name);
    if (Objects.equals(name, this.app.getName())) return this.app;
    else return null;
  }

  @Override
  public Enumeration<Applet> getApplets() {
    return new Enumeration<>() {
      private boolean gone;

      @Override
      public Applet nextElement() {
        if (this.gone) throw new NoSuchElementException();
        this.gone = true;
        return FOContext.this.getApplet(null);
      }

      @Override
      public boolean hasMoreElements() {
        return !this.gone;
      }
    };
  }

  @Override
  public void showDocument(final URL url) {
    System.out.println("SHOW: " + url);
    this.checkRedirect(url);
  }

  @Override
  public void showDocument(final URL url, final String target) {
    System.out.println("SHOW (" + target + "): " + url);
    this.checkRedirect(url);
  }

  private void checkRedirect(final URL url) {
    if (url.getProtocol().equals("http") || url.getProtocol().equals("https")) {
      if (url.getHost().equals(this.stub.getDocumentBase().getHost()) ||
          url.getHost().equals(this.stub.getCodeBase().getHost())) {
        String file = url.getFile();
        final int idx = file.lastIndexOf('/');
        if (idx >= 0) file = file.substring(idx + 1);
        if ("quit.ws".equals(file)) {
          this.stub.getFrame().dispose();
        }
      }
    }
  }

  @Override
  public void showStatus(final String status) {
    System.out.println("STATUS " + status);
  }

  @Override
  public void setStream(final String key, final InputStream stream) {
    System.out.println("STREAM " + key + " = " + stream);
  }

  @Override
  public InputStream getStream(final String key) {
    System.out.println("STREAM " + key);
    return null;
  }

  @Override
  public Iterator<String> getStreamKeys() {
    return Collections.emptyIterator();
  }
}
