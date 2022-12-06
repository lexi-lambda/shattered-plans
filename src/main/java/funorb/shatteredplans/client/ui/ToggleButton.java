package funorb.shatteredplans.client.ui;

import funorb.graphics.Sprite;
import funorb.shatteredplans.client.Menu;

public final class ToggleButton<T> extends UIComponent<T> {
  private final Sprite inactiveIcon;
  private final String inactiveLabel;
  private final int inactiveLabelColor;

  private final Sprite activeIcon;
  private final String activeLabel;
  private final int activeLabelColor;

  private boolean isActive = false;

  public ToggleButton(final int x,
                      final int y,
                      final int width,
                      final int height,
                      final Sprite inactiveIcon,
                      final String inactiveLabel,
                      final int inactiveLabelColor,
                      final Sprite activeIcon,
                      final String activeLabel,
                      final int activeLabelColor) {
    super(x, y, width, height);
    this.inactiveIcon = inactiveIcon;
    this.inactiveLabel = inactiveLabel;
    this.inactiveLabelColor = inactiveLabelColor;
    this.activeIcon = activeIcon;
    this.activeLabel = activeLabel;
    this.activeLabelColor = activeLabelColor;
  }

  public boolean isActive() {
    return this.isActive;
  }

  public void deactivate() {
    this.isActive = false;
  }

  public void activate() {
    this.isActive = true;
  }

  public void toggle() {
    this.isActive = !this.isActive;
  }

  @Override
  public void draw() {
    if (this.visible) {
      if (this.isActive) {
        if (this.activeIcon != null) {
          this.activeIcon.draw(this.x + (this.width - this.activeIcon.width) / 2, (this.height - this.activeIcon.height) / 2 + this.y);
        }
        if (this.activeLabel != null) {
          Menu.SMALL_FONT.drawCentered(this.activeLabel, this.width / 2 + 1 + this.x, Menu.SMALL_FONT.ascent / 2 + this.height / 2 + this.y, this.activeLabelColor);
        }
      } else {
        if (this.inactiveIcon != null) {
          this.inactiveIcon.draw((-this.inactiveIcon.width + this.width) / 2 + this.x, (this.height - this.inactiveIcon.height) / 2 + this.y);
        }
        if (this.inactiveLabel != null) {
          Menu.SMALL_FONT.drawCentered(this.inactiveLabel, this.width / 2 + 1 + this.x, Menu.SMALL_FONT.ascent / 2 + this.height / 2 + this.y, this.inactiveLabelColor);
        }
      }
    }
  }
}
