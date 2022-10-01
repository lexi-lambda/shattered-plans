package funorb.commonui;

import funorb.graphics.Drawing;
import funorb.graphics.Sprite;

public final class ProgressBar extends Component {
  public static final int MAX_PROGRESS = 65536;
  private final int STRIPE_WIDTH = 15;

  public static final int LOADING_COLOR = 0x204060;
  public static final int SUCCESS_COLOR = 0x206040;
  public static final int FAILURE_COLOR = 0x804020;

  private static final int EMPTY_BASE_COLOR = 0x202020;
  private static final int FILLED_BASE_COLOR = 0x404040;

  private int emptyAccentColor = 0x102030;
  private int filledAccentColor = 0x204060;

  public boolean animateStripes;
  public int progress;
  private Sprite _S;
  private Sprite _R;
  private int stripeOffset;
  private Sprite _N;
  private Sprite[] _z;

  public ProgressBar() {
    this.setBounds(13, 50, 274, 30);
  }

  private Sprite a119(final int color1, final int color2) {
    final Sprite sprite = new Sprite(this.STRIPE_WIDTH * 2, this.height);
    sprite.withInstalledForDrawingUsingOffsets(() -> {
      final int halfHeight = this.height / 2;
      for (int i = 0; i < this.height; ++i) {
        final int var8 = (((this.STRIPE_WIDTH * 2) - 1) * (i / 2)) % (2 * this.STRIPE_WIDTH);
        final int var9 = color1 & 0xff00ff;
        final int var10 = color1 & 0x00ff00;
        final int var11 = i - halfHeight;
        final int var12 = 128 + (int) (Math.sqrt((halfHeight * halfHeight) - (var11 * var11)) / (double) halfHeight * 128.0D);
        final int var13 = var12 >= 256 ? var9 | var10 : (var12 * var10 & 16711680 | -16711936 & var12 * var9) >>> 8;
        Drawing.horizontalLine(var8, i, this.STRIPE_WIDTH, var13);
        Drawing.horizontalLine(var8 - this.STRIPE_WIDTH * 2, i, this.STRIPE_WIDTH, var13);
        final int i3 = '\uff00' & color2;
        final int i1 = 16711935 & color2;
        final int i2 = (var12 >= 256) ? (i3 | i1) : (((0xff0000 & (var12 * i3)) | ((i1 * var12) & 0xff00ff00)) >>> 8);
        Drawing.horizontalLine(var8 + this.STRIPE_WIDTH, i, this.STRIPE_WIDTH, i2);
        Drawing.horizontalLine(-this.STRIPE_WIDTH + var8, i, this.STRIPE_WIDTH, i2);
      }
    });
    return sprite;
  }

  private void buildSprites() {
    this._z = new Sprite[]{this.a119(this.filledAccentColor, FILLED_BASE_COLOR), this.a119(this.emptyAccentColor, EMPTY_BASE_COLOR)};
    this._R = this.h432();
    this._N = this._R.horizontallyFlipped();
    this._S = new Sprite(this.height / 2, this.height);
  }

  @Override
  public void setBounds(final int x, final int y, final int width, final int height) {
    super.setBounds(x, y, width, height);
    this.buildSprites();
  }

  private void a854(final Sprite var1, final int var2, final int var3) {
    final int var5 = var3 + this.width;
    Drawing.withLocalContext(() -> {
      Drawing.expandBoundsToInclude(var3 + this._R.width, var2, var5 - this._R.width, this.height + var2);
      for (int var6 = -this.stripeOffset + var3; var5 > var6; var6 += var1.width) {
        var1.draw(var6, var2);
      }
    });

    if (Drawing.left <= var3 + this._R.width) {
      this._S.withInstalledForDrawingUsingOffsets(() -> {
        var1.draw(-this.stripeOffset, 0);
        var1.draw(-this.stripeOffset + this.STRIPE_WIDTH * 2, 0);
        this._N.e093();
      });
      this._S.draw(var3, var2);
    }

    if (Drawing.right >= -this._R.width + var5) {
      this._S.withInstalledForDrawingUsingOffsets(() -> {
        int var7 = -this._R.width + this.width + this.stripeOffset;
        while (2 * this.STRIPE_WIDTH < var7) {
          var7 -= 2 * this.STRIPE_WIDTH;
        }

        var1.draw(-var7, 0);
        var1.draw(this.STRIPE_WIDTH * 2 - var7, 0);
        this._R.e093();
      });
      this._S.draw(-this._R.width + var5, var2);
    }

  }

  @Override
  public void tick(final int x, final int y, final Component root) {
    if (this.animateStripes) {
      ++this.stripeOffset;
      if (this.stripeOffset > this.STRIPE_WIDTH * 2) {
        this.stripeOffset -= this.STRIPE_WIDTH * 2;
      }
    }
  }

  @Override
  public void draw(final int x, final int y) {
    final int var5 = x + this.x;
    final int var6 = this.y + y;
    this.a854(this._z[0], var6, var5);
    if (this.progress < MAX_PROGRESS) {
      Drawing.withLocalContext(() -> {
        Drawing.expandBoundsToInclude(var5 + (this.progress * this.width >> 16), var6, var5 + this.width, var6 + this.height);
        this.a854(this._z[1], var6, var5);
      });
    }
  }

  private Sprite h432() {
    final int width = this.height / 2;
    final Sprite sprite = new Sprite(width, this.height);
    sprite.withInstalledForDrawingUsingOffsets(() -> {
      for (int y = 0; y < this.height; ++y) {
        for (int x = 0; x < width; ++x) {
          double var7 = ((double) x * (double) x) / (double) (y * (this.height - y));
          int gray = 1;
          if (var7 < 1.0D) {
            var7 = Math.sqrt(1.0D - var7);
            gray = var7 < 1.0D ? (int) (255.0D * var7) : 255;
          }

          Drawing.setPixel(x, y, Drawing.gray(gray));
        }
      }
    });
    return sprite;
  }

  public void setColor(final int color) {
    this.emptyAccentColor = (color >> 1) & 0x7f7f7f;
    this.filledAccentColor = color;
    this.buildSprites();
  }
}
