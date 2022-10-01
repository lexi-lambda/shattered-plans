package funorb.shatteredplans.client.ui;

import funorb.graphics.Sprite;

public final class Icon extends UIComponent<Object> {
  private Sprite sprite;

  public Icon(final int x, final int y, final Sprite sprite) {
    this(x, y, sprite.width, sprite.height, sprite);
  }

  public Icon(final int x, final int y, final int width, final int height, final Sprite sprite) {
    super(x, y, width, height);
    this.sprite = sprite;
  }

  public void setSprite(final Sprite sprite) {
    this.sprite = sprite;
  }

  public boolean isEmpty() {
    return this.sprite == null;
  }

  @Override
  public void draw() {
    if (this.visible) {
      if (this.sprite != null) {
        if (this.width == this.sprite.width && this.height == this.sprite.height) {
          this.sprite.draw(this.x, this.y);
        } else {
          this.sprite.b115(this.x, this.y, this.width, this.height);
        }
      }
    }
  }
}
