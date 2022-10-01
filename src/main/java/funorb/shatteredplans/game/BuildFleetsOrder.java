package funorb.shatteredplans.game;

import funorb.io.WritableBuffer;
import funorb.shatteredplans.map.StarSystem;
import org.jetbrains.annotations.NotNull;

public final class BuildFleetsOrder {
  public @NotNull StarSystem system;
  public int quantity;

  public BuildFleetsOrder(final @NotNull StarSystem system, final int quantity) {
    this.system = system;
    this.quantity = quantity;
  }

  public Force getForce(final GameOptions options) {
    return options.unifiedTerritories ? this.system.owner.combinedForce : this.system.contiguousForce;
  }

  public boolean replaces(final BuildFleetsOrder other) {
    return this.system == other.system;
  }

  public void appendTo(final BuildFleetsOrder other) {
    assert this.replaces(other);
    other.quantity += this.quantity;
  }

  public void write(final WritableBuffer buffer) {
    StarSystem.write(buffer, this.system);
    buffer.writeShort(this.quantity);
  }

  @Override
  public String toString() {
    return "BuildFleetsOrder[" + this.quantity + " @ " + this.system + "]";
  }
}
