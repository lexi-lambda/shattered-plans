package funorb.shatteredplans.map;

import funorb.io.WritableBuffer;

public final class TannhauserLink {
  public StarSystem system1;
  public StarSystem system2;
  public int turnsLeft;

  public TannhauserLink(final StarSystem system1, final StarSystem system2, final int turnsLeft) {
    this.system1 = system1;
    this.system2 = system2;
    this.turnsLeft = turnsLeft;
  }

  public void write(final WritableBuffer buffer) {
    this.system1.write(buffer);
    this.system2.write(buffer);
    buffer.writeByte(this.turnsLeft);
  }
}
