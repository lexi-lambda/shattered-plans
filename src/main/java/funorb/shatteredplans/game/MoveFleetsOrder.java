package funorb.shatteredplans.game;

import funorb.io.WritableBuffer;
import funorb.shatteredplans.client.game.TurnEventLog;
import funorb.shatteredplans.map.StarSystem;
import funorb.util.MathUtil;

public final class MoveFleetsOrder implements TurnEventLog.Event {
  public final Player player;
  public StarSystem source;
  public StarSystem target;
  public int quantity;

  public MoveFleetsOrder(final Player player, final StarSystem source, final StarSystem target, final int quantity) {
    this.player = player;
    this.source = source;
    this.target = target;
    this.quantity = quantity;
  }

  public MoveFleetsOrder(final StarSystem source, final StarSystem target, final int quantity) {
    this(source.owner, source, target, quantity);
  }

  public int euclideanDistanceSquared() {
    return MathUtil.euclideanDistanceSquared(this.source.posnX - this.target.posnX, this.source.posnY - this.target.posnY);
  }

  public boolean replaces(final MoveFleetsOrder other) {
    return this.player == other.player && this.source == other.source && this.target == other.target;
  }

  public void appendTo(final MoveFleetsOrder other) {
    assert this.replaces(other);
    other.quantity += this.quantity;
  }

  public void write(final WritableBuffer buffer) {
    StarSystem.write(buffer, this.source);
    StarSystem.write(buffer, this.target);
    buffer.writeShort(this.quantity);
  }

  @Override
  public String toString() {
    return "MoveFleetsOrder{" +
        "player=" + this.player +
        ", quantity=" + this.quantity +
        ", source=" + this.source +
        ", target=" + this.target +
        "}";
  }
}
