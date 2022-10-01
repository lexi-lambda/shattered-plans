package funorb.shatteredplans.client.game;

import funorb.shatteredplans.game.MoveFleetsOrder;
import funorb.shatteredplans.game.Player;
import funorb.shatteredplans.map.StarSystem;

public final class MoveFleetsAnimationState {
  public final Player player;
  public final int quantity;
  public final StarSystem source;
  public final StarSystem target;

  public MoveFleetsAnimationState(final MoveFleetsOrder order) {
    this.player = order.player;
    this.quantity = order.quantity;
    this.source = order.source;
    this.target = order.target;
  }
}
