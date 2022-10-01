package funorb.awt;

import funorb.shatteredplans.client.JagexApplet;

import java.awt.Canvas;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

public final class FullScreenCanvas extends Canvas implements FocusListener {
  public Frame frame;
  public volatile boolean focusWasLost;

  @Override
  public void update(final Graphics var1) {
  }

  @Override
  public void paint(final Graphics var1) {
  }

  public void repeatedlyTryToExitFullScreen() {
    JagexApplet.repeatedlyTryToExitFullScreen(this.frame);
  }

  @Override
  public void focusLost(final FocusEvent var1) {
    this.focusWasLost = true;
  }

  @Override
  public void focusGained(final FocusEvent var1) {
  }
}
