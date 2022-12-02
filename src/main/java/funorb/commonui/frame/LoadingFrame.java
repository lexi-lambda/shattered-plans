package funorb.commonui.frame;

import funorb.commonui.ProgressBar;
import funorb.commonui.Resources;
import funorb.graphics.Drawing;
import funorb.graphics.Font;

public final class LoadingFrame extends Frame {
  private String progressMessage;
  private final String notificationMessage;
  private final ProgressBar progressBar;
  private boolean failed;
  private boolean animationDisabled;

  public LoadingFrame(final RootFrame root, final String notificationMessage) {
    super(root, 300, 120);
    this.notificationMessage = notificationMessage;
    if (this.notificationMessage != null) {
      final int var3 = Resources.AREZZO_14_BOLD.measureParagraphHeight(this.notificationMessage, 260, Resources.AREZZO_14_BOLD.ascent);
      this.setBoundsCentered(300, 150 + var3);
    }

    this.progressBar = new ProgressBar();
    this.progressBar.animateStripes = true;
    this.animationDisabled = false;
    this.failed = false;
    this.addChild(this.progressBar);
  }

  public void disableAnimation() {
    this.animationDisabled = true;
    this.progressBar.animateStripes = false;
  }

  @Override
  public void drawContent(final int x, final int y) {
    super.drawContent(x, y);
    Resources.AREZZO_14_BOLD.drawCentered(this.progressMessage, (this.width / 2) + x, 103 + y, Drawing.WHITE);
    if (this.notificationMessage != null) {
      Drawing.horizontalLine(x + 20, y + 113, 260, 0x808080);
      Resources.AREZZO_14_BOLD.drawParagraph(this.notificationMessage, x + 20, y + 128, 260, 100, Drawing.WHITE, Font.HorizontalAlignment.CENTER, Font.VerticalAlignment.TOP, Resources.AREZZO_14_BOLD.ascent);
    }
  }

  public void update(final String message, final float percent, final boolean failed) {
    if (this.failed == !failed) {
      this.failed = failed;
      if (this.failed) {
        this.progressBar.setColor(ProgressBar.FAILURE_COLOR);
        this.progressBar.animateStripes = true;
      } else {
        this.progressBar.setColor(ProgressBar.LOADING_COLOR);
        if (this.animationDisabled) {
          this.progressBar.animateStripes = false;
        }
      }
    }

    this.progressMessage = message;
    this.progressBar.progress = (int) (percent / 100.0F * (float) ProgressBar.MAX_PROGRESS);
  }
}
