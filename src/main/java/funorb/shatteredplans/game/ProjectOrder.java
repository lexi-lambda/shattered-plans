package funorb.shatteredplans.game;

import funorb.io.WritableBuffer;
import funorb.shatteredplans.StringConstants;
import funorb.shatteredplans.client.game.TurnEventLog;
import funorb.shatteredplans.map.StarSystem;
import org.intellij.lang.annotations.MagicConstant;
import org.jetbrains.annotations.NotNull;

public final class ProjectOrder implements TurnEventLog.Event {
  @MagicConstant(valuesFromClass = GameState.ResourceType.class)
  public final int type;
  public final @NotNull Player player;
  public StarSystem source; // null for non-Tannhauser projects
  public StarSystem target; // null when canceling an order on the server

  private ProjectOrder(@MagicConstant(valuesFromClass = GameState.ResourceType.class) final int type,
                       final @NotNull Player player,
                       final StarSystem source,
                       final StarSystem target) {
    this.type = type;
    this.player = player;
    this.source = source;
    this.target = target;
  }

  public ProjectOrder(@MagicConstant(valuesFromClass = GameState.ResourceType.class) final int type,
               final Player player,
               final StarSystem system) {
    this(type, player, null, system);
  }

  public ProjectOrder(final Player player, final StarSystem source, final StarSystem target) {
    this(GameState.ResourceType.EXOTICS, player, source, target);
  }

  public boolean replaces(final ProjectOrder other) {
    return this.player == other.player && this.type == other.type;
  }

  public void write(final WritableBuffer buffer) {
    this.player.write(buffer);
    StarSystem.write(buffer, this.target);
    if (this.type == GameState.ResourceType.EXOTICS) {
      StarSystem.write(buffer, this.source);
    }
  }

  @Override
  public String toString() {
    return "ProjectOrder{" + StringConstants.PROJECT_NAMES[this.type] +
        ", player=" + this.player +
        (this.type == GameState.ResourceType.EXOTICS ? ", target=" + this.source : "") +
        ", source=" + this.target + "}";
  }
}
