package funorb.shatteredplans.client.game;

import funorb.shatteredplans.game.Player;
import funorb.shatteredplans.map.StarSystem;

public final class StellarBombEvent implements TurnEventLog.Event {
  public final Player player;
  public final StarSystem target;
  public final int kill;

  public StellarBombEvent(final Player player, final StarSystem target, final int kills) {
    this.player = player;
    this.target = target;
    this.kill = kills;
  }
}
