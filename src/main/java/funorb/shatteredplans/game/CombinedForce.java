package funorb.shatteredplans.game;

import funorb.shatteredplans.map.StarSystem;

public final class CombinedForce extends Force {
  public CombinedForce(final Player player, final StarSystem capital) {
    super(player, capital);
  }

  public void clear() {
    this.setCapital(null);
    this.systems.clear();
    this.fleetProduction = 0;
    this.fleetsAvailableToBuild = 0;
    this.surplusResources = new int[4];
    this.surplusResourceRanks = new int[4];
  }
}
