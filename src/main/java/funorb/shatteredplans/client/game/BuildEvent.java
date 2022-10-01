package funorb.shatteredplans.client.game;

import funorb.shatteredplans.game.Player;
import funorb.shatteredplans.map.StarSystem;

public final class BuildEvent {
  public final StarSystem system;
  public final Player player;
  public final int projectType;
  public final int fleetsBuilt;
  public final int phase;

  public BuildEvent(final StarSystem system,
                    final Player player,
                    final int projectType,
                    final int phase) {
    this(system, player, projectType, 0, phase);
  }

  public BuildEvent(final StarSystem system,
                    final Player player,
                    final int projectType,
                    final int fleetsBuilt,
                    final int phase) {
    this.system = system;
    this.player = player;
    this.projectType = projectType;
    this.fleetsBuilt = fleetsBuilt;
    this.phase = phase;
  }
}
