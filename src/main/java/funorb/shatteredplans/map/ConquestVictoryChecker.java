package funorb.shatteredplans.map;

import funorb.io.WritableBuffer;
import funorb.shatteredplans.game.GameState;
import funorb.shatteredplans.client.GameUI;
import funorb.shatteredplans.game.Player;
import funorb.io.Buffer;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public final class ConquestVictoryChecker extends VictoryChecker {
  public ConquestVictoryChecker(final @NotNull Player @NotNull [] players) {
    super(players);
  }

  @Override
  public void updateVictoryPanel(final GameState state, final GameUI ui) {}

  @Override
  public void write(final WritableBuffer buffer) {}

  @Override
  public int victoryPanelHeight() {
    return -1;
  }

  @Override
  @Contract(pure = true)
  public @NotNull Player @NotNull [] currentObjectiveLeaders() {
    return new Player[0];
  }

  @Override
  public void initializeFromServer(final Buffer packet) {}
}
