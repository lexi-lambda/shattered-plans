package funorb.shatteredplans.game.ai;

import funorb.shatteredplans.map.CaptureAndHoldVictoryChecker;
import funorb.shatteredplans.map.StarSystem;
import funorb.shatteredplans.map.VictoryChecker;

public final class DefenseTask extends AITask {
  DefenseTask(final StarSystem system, final int[] forceWantedResources, final boolean[] allies, final VictoryChecker victoryChecker) {
    super(system);
    this.priority = 0;

    for (int i = 0; i < 4; ++i) {
      this.priority += system.resources[i] * forceWantedResources[i];
    }

    if (this.priority < 0) {
      this.priority = -this.priority;
    }
    if (victoryChecker instanceof CaptureAndHoldVictoryChecker && this.system.index == 36) {
      this.priority += 50;
    }

    this.fleetCost = 0;
    for (final StarSystem neighbor : system.neighbors) {
      if (neighbor.owner != null && neighbor.owner != system.owner && !allies[neighbor.owner.index]) {
        this.fleetCost += neighbor.garrison;
      }
    }

    if (system.hasDefensiveNet && this.fleetCost > 1) {
      this.fleetCost /= 2;
    }
    if (this.fleetCost == 0) {
      this.fleetCost = 1;
    }
  }
}
