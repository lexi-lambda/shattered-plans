package funorb.shatteredplans.game.ai;

import funorb.shatteredplans.map.StarSystem;

class AITask {
  final StarSystem system;
  int priority;
  int adjustedPriority;
  int fleetCost;

  AITask(final StarSystem system) {
    this.system = system;
  }
}
