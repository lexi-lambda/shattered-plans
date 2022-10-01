package funorb.shatteredplans.client.game;

import funorb.shatteredplans.game.Player;
import funorb.shatteredplans.map.StarSystem;

public final class BuildFleetsEvent implements TurnEventLog.Event {
  public final Player player;
  public final StarSystem system;
  public final int quantity;

  public BuildFleetsEvent(final Player player, final StarSystem system, final int quantity) {
    this.player = player;
    this.system = system;
    this.quantity = quantity;
  }
}
