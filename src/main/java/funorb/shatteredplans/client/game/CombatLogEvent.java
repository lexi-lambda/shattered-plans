package funorb.shatteredplans.client.game;

import funorb.shatteredplans.game.MoveFleetsOrder;
import funorb.shatteredplans.game.Player;
import funorb.shatteredplans.map.StarSystem;

public final class CombatLogEvent {
  public final Player player;
  public final StarSystem source;
  public final int fleetsAtStart;
  public final int fleetsDestroyed;
  public final int fleetsRetreated;

  public CombatLogEvent(final MoveFleetsOrder order, final int fleetsDestroyed, final int fleetsRetreated) {
    this.player = order.player;
    this.source = order.source;
    this.fleetsAtStart = order.quantity;
    this.fleetsDestroyed = fleetsDestroyed;
    this.fleetsRetreated = fleetsRetreated;
  }

  public CombatLogEvent(final Player player, final int fleetsAtStart, final int fleetsDestroyed, final int fleetsRetreated) {
    this.player = player;
    this.source = null;
    this.fleetsAtStart = fleetsAtStart;
    this.fleetsRetreated = fleetsRetreated;
    this.fleetsDestroyed = fleetsDestroyed;
  }
}
