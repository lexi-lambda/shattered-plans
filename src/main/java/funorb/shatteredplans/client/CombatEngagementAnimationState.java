package funorb.shatteredplans.client;

import funorb.shatteredplans.client.game.CombatEngagementLog;
import funorb.shatteredplans.game.Player;
import funorb.shatteredplans.map.StarSystem;

import java.util.Arrays;

public final class CombatEngagementAnimationState {
  public final int highestFleetsAtCombatStart;
  public final Player victor;
  public final int totalKills;
  public final int fleetsAtCombatEnd;
  public final Player ownerAtCombatStart;
  public final Player[] players;
  public final int[] fleetsAtCombatStart;
  public final StarSystem system;

  public int totalFleets;
  public int[] fleets;
  public Player selectedPlayer;

  public CombatEngagementAnimationState(final CombatEngagementLog log) {
    this.fleetsAtCombatStart = log.fleetsAtCombatStart;
    this.ownerAtCombatStart = log.ownerAtCombatStart;
    this.fleetsAtCombatEnd = log.fleetsAtCombatEnd;
    this.players = log.players;
    this.victor = log.victor;
    this.system = log.system;
    this.totalKills = log.totalKills;
    this.highestFleetsAtCombatStart = Arrays.stream(this.fleetsAtCombatStart).max().orElse(0);
    this.reset();
  }

  public void reset() {
    this.fleets = this.fleetsAtCombatStart.clone();
    this.totalFleets = Arrays.stream(this.fleetsAtCombatStart).sum();

    if (this.players.length == 1) {
      this.totalFleets = 0;
      Arrays.fill(this.fleets, 0);
    } else {
      this.selectedPlayer = this.players[ShatteredPlansClient.randomIntBounded(this.players.length - 1)];
      if (this.victor == this.selectedPlayer) {
        this.selectedPlayer = this.players[this.players.length - 1];
      }

      final int victorIndex = this.victor == null
          ? this.fleetsAtCombatStart.length - 1
          : this.victor.index;
      this.fleets[victorIndex] -= this.fleetsAtCombatEnd;
      this.totalFleets -= this.fleetsAtCombatEnd;
      --this.totalFleets;
    }
  }
}
