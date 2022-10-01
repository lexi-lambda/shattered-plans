package funorb.shatteredplans.client.game;

import funorb.shatteredplans.map.StarSystem;

public final class FleetRetreatEvent implements TurnEventLog.Event {
  public final StarSystem source;
  public final StarSystem[] targets;
  public int[] quantities;

  public FleetRetreatEvent(final StarSystem source) {
    this.source = source;
    this.targets = null;
  }

  public FleetRetreatEvent(final StarSystem source, final StarSystem[] targets, final int[] quantities) {
    this.source = source;
    this.targets = targets;
    this.quantities = quantities;
  }
}
