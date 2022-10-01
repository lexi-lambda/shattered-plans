package funorb.shatteredplans.game.ai;

import funorb.shatteredplans.game.ContiguousForce;
import funorb.shatteredplans.game.GameState;
import funorb.shatteredplans.game.Player;
import funorb.shatteredplans.map.CaptureAndHoldVictoryChecker;
import funorb.shatteredplans.map.DerelictsVictoryChecker;
import funorb.shatteredplans.map.StarSystem;
import funorb.shatteredplans.map.VictoryChecker;

import java.util.Arrays;
import java.util.Collection;
import java.util.function.Predicate;

final class CaptureTask extends AITask {
  boolean _p = false;

  CaptureTask(final StarSystem system, final Collection<ContiguousForce> forces, final int[][] forceWantedResources, final Player player, final VictoryChecker victoryChecker, final int currentObjectiveLeader, final GameState gameState) {
    super(system);
    final ContiguousForce[] neighboringForces = new ContiguousForce[system.neighbors.length];
    int neighboringForceCount = 0;
    for (final StarSystem neighbor : system.neighbors) {
      if (neighbor.owner == player) {
        final boolean alreadyAdded = Arrays.stream(neighboringForces, 0, neighboringForceCount)
            .anyMatch(Predicate.isEqual(neighbor.contiguousForce));
        if (!alreadyAdded) {
          neighboringForces[neighboringForceCount] = neighbor.contiguousForce;
          ++neighboringForceCount;
        }
      }
    }

    if (neighboringForceCount == 0) {
      throw new RuntimeException("Target star " + system.name + " does not appear to be connected to any of AI's stars.");
    }

    ContiguousForce mostInterestedForce = null;
    this.priority = 0;

    int var10 = 0;
    for (final ContiguousForce force : forces) {
      for (int i = 0; i < neighboringForceCount; ++i) {
        if (neighboringForces[i] == force) {
          int var21 = 0;

          for (int j = 0; j < GameState.NUM_RESOURCES; j++) {
            var21 += system.resources[j] * forceWantedResources[var10][j];
            if (var21 < 0) {
              var21 = -var21;
            }

            if (var21 > this.priority) {
              this.priority = var21;
              mostInterestedForce = force;
            }
          }

          break;
        }
      }

      ++var10;
    }

    this.priority += 50 * (neighboringForceCount - 1);
    if (victoryChecker instanceof CaptureAndHoldVictoryChecker && this.system.index == 36) {
      this.priority += 100;
    }

    if (victoryChecker instanceof DerelictsVictoryChecker && this.system.score == StarSystem.Score.NEUTRAL_HOMEWORLD) {
      final int forceProduction;
      if (gameState.gameOptions.unifiedTerritories) {
        forceProduction = player.combinedForce.fleetProduction;
      } else {
        assert mostInterestedForce != null;
        forceProduction = mostInterestedForce.fleetProduction;
      }

      if (this.system.resources[0] * -5 > forceProduction) {
        this.priority -= 100;
      }

      if (this.system.owner != null && this.system.owner.index == currentObjectiveLeader) {
        this.priority += 100;
      }
    }

    this.fleetCost = system.garrison;
    if (system.owner == null) {
      for (final StarSystem var19 : system.neighbors) {
        if (player != var19.owner && var19.owner != null) {
          this.fleetCost += var19.garrison;
        }
      }
    } else {
      for (final StarSystem var19 : system.neighbors) {
        if (player != var19.owner && (system.owner == var19.owner || var19.owner != null && !system.owner.allies[var19.owner.index])) {
          this.fleetCost += var19.garrison;
        }
      }
    }

    if (system.hasDefensiveNet) {
      this.fleetCost = 2 * this.fleetCost;
    }

    if (!gameState.gameOptions.simpleGarrisoning) {
      int requiredToHold = (int) Arrays.stream(this.system.neighbors)
          .filter(neighbors -> player != neighbors.owner).count();
      if (this.fleetCost > 0 && requiredToHold > 0) {
        ++requiredToHold;
      }
      this.fleetCost += requiredToHold;
    }

    if (this.fleetCost == 0) {
      this.fleetCost = 1;
    }
  }
}
