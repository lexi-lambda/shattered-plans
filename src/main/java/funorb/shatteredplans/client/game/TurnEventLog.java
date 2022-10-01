package funorb.shatteredplans.client.game;

import funorb.shatteredplans.game.GameState;
import funorb.shatteredplans.game.MoveFleetsOrder;
import funorb.shatteredplans.game.Player;
import funorb.shatteredplans.game.ProjectOrder;
import funorb.shatteredplans.map.StarSystem;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public final class TurnEventLog {
  public final List<Event> events = new ArrayList<>();

  public void addTannhauserEvent(final Player var4, final StarSystem var2, final StarSystem var3) {
    this.events.add(new ProjectOrder(var4, var3, var2));
  }

  public void addMoveFleetsEvent(final StarSystem var6, final StarSystem var1, final Player var2, final int var5) {
    this.events.add(new MoveFleetsOrder(var2, var6, var1, var5));
  }

  public void addFleetRetreatEvent(final StarSystem source, final StarSystem[] targets, final int[] quantities) {
    final int retreatCount = (int) Arrays.stream(quantities).filter(quantity -> quantity != 0).count();
    if (retreatCount == 0) {
      this.events.add(new FleetRetreatEvent(source));
    } else {
      final StarSystem[] targets2 = new StarSystem[retreatCount];
      final int[] quantities2 = new int[retreatCount];
      int i = 0;
      for (int j = 0; j < quantities.length; ++j) {
        if (quantities[j] != 0) {
          targets2[i] = targets[j];
          quantities2[i] = quantities[j];
          ++i;
        }
      }

      this.events.add(new FleetRetreatEvent(source, targets2, quantities2));
    }
  }

  public void addFleetRetreatEvent(final StarSystem system) {
    this.events.add(new FleetRetreatEvent(system));
  }

  public void addDefensiveNetEvent(final Player player, final StarSystem target) {
    this.events.add(new ProjectOrder(GameState.ResourceType.METAL, player, target));
  }

  public void addStellarBombEvent(final Player player, final StarSystem target, final int kills) {
    this.events.add(new StellarBombEvent(player, target, kills));
  }

  public CombatEngagementLog addCombatEngagement(final StarSystem system, final Player[] playersInvolved, final int[] fleetsAtCombatStart) {
    final CombatEngagementLog combatLog = new CombatEngagementLog(system, playersInvolved, fleetsAtCombatStart);
    this.events.add(combatLog);
    return combatLog;
  }

  public void addTerraformingEvent(final Player player, final StarSystem target) {
    this.events.add(new ProjectOrder(GameState.ResourceType.BIOMASS, player, target));
  }

  public void addBuildFleetsEvent(final StarSystem system, final int quantity) {
    this.events.add(new BuildFleetsEvent(system.owner, system, quantity));
  }

  public interface Event {}
}
