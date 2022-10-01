package funorb.io;

public final class Inflater {
  private static final java.util.zip.Inflater INFLATER = new java.util.zip.Inflater(true);

  private Inflater() {}

  public static synchronized void inflate(final Buffer src, final byte[] dest) {
    if (src.data[src.pos] != 31 || src.data[src.pos + 1] != -117) {
      throw new RuntimeException("bad magic");
    }
    try {
      INFLATER.setInput(src.data, src.pos + 10, src.data.length - (src.pos + 8) - 10);
      INFLATER.inflate(dest);
    } catch (final Exception e) {
      throw new RuntimeException(e);
    } finally {
      INFLATER.reset();
    }
  }
}
