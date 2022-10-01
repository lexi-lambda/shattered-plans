package funorb.shatteredplans.game.ai;

import funorb.shatteredplans.map.StarSystem;

public final class HoldTask extends AITask {
  HoldTask(final StarSystem system, final boolean simpleGarrisoning, final boolean[] systemBorderedByHostile) {
    super(system);
    this.fleetCost = system.minimumGarrison;
    if (!simpleGarrisoning) {
      for (final StarSystem neighbor : system.neighbors) {
        if (systemBorderedByHostile[neighbor.index]) {
          ++this.fleetCost;
        }
      }
    }
  }
}
