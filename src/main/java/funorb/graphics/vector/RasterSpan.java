package funorb.graphics.vector;

public final class RasterSpan {
  private static RasterSpan pool;

  public RasterSpan next = null;
  public int x;
  public int width;
  public int alpha1;
  public int alpha2;
  public int alphaStep;

  public static RasterSpan create(final RasterSpan next, final int x, final int width, final int alpha1, final int alpha2, final int alphaStep) {
    RasterSpan span = pool;
    if (span == null) {
      span = new RasterSpan();
    } else {
      pool = pool.next;
    }

    span.next = next;
    span.x = x;
    span.width = width;
    span.alpha1 = alpha1;
    span.alpha2 = alpha2;
    span.alphaStep = alphaStep;
    if (span.width < 0) {
      throw new RuntimeException();
    } else if (span.alpha2 == 0 && span.alphaStep == 0 && (span.alpha1 == 0 || span.width != 1)) {
      throw new RuntimeException();
    }

    return span;
  }

  public void release() {
    this.next = pool;
    pool = this;
  }
}
