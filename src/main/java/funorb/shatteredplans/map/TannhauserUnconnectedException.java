package funorb.shatteredplans.map;

public final class TannhauserUnconnectedException extends Exception {
  public TannhauserUnconnectedException() {
    super("Tannhauser Gate is unconnected at at least one end.");
  }
}
