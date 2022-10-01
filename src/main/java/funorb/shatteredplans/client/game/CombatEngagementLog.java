package funorb.shatteredplans.client.game;

import funorb.shatteredplans.game.MoveFleetsOrder;
import funorb.shatteredplans.game.Player;
import funorb.shatteredplans.map.StarSystem;

import java.util.ArrayList;
import java.util.List;

public final class CombatEngagementLog implements TurnEventLog.Event {
  public final StarSystem system;
  public final Player[] players;
  public final Player ownerAtCombatStart;
  public final int[] fleetsAtCombatStart;
  public final List<CombatLogEvent> events = new ArrayList<>();
  public int totalKills;
  public int[] playerKills;
  public Player victor;
  public int fleetsAtCombatEnd;

  public CombatEngagementLog(final StarSystem system, final Player[] players, final int[] fleetsAtCombatStart) {
    this.system = system;
    this.players = players;
    this.fleetsAtCombatStart = fleetsAtCombatStart.clone();
    this.ownerAtCombatStart = this.system.owner;
    this.totalKills = 0;
  }

  public void setPlayerKills(final int[] playerKills) {
    this.playerKills = playerKills;
  }

  public void a115(final int var2, final int var1, final int var3) {
    this.totalKills += var1;
    this.events.add(new CombatLogEvent(this.ownerAtCombatStart, var2, var1, var3));
  }

  public void a326(final int fleetsAtStart, final int fleetsKilled) {
    this.totalKills += fleetsKilled;
    this.events.add(new CombatLogEvent(this.ownerAtCombatStart, fleetsAtStart, fleetsKilled, 0));
  }

  public void a631(final MoveFleetsOrder order, final int destroyed, final int retreated) {
    this.events.add(new CombatLogEvent(order, destroyed, retreated));
    this.totalKills += destroyed;
  }
}
