package funorb.shatteredplans.map;

import funorb.io.Buffer;
import funorb.io.WritableBuffer;
import funorb.shatteredplans.client.GameUI;
import funorb.shatteredplans.game.GameState;
import funorb.shatteredplans.game.Player;
import funorb.util.ArrayUtil;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.function.Predicate;
import java.util.stream.IntStream;

public abstract class VictoryChecker {

  protected final @NotNull Player @NotNull [] players;
  private final boolean @NotNull [] undefeatedPlayers;
  protected @NotNull Player @Nullable [] victors;

  protected VictoryChecker(final @NotNull Player @NotNull [] players) {
    this.players = players;
    this.undefeatedPlayers = ArrayUtil.create(players.length, true);
  }

  @Contract(pure = true)
  public abstract @NotNull Player @NotNull [] currentObjectiveLeaders();

  public boolean checkVictory(final @NotNull GameState state) {
    final Player[] playersWithForces = Arrays.stream(this.players)
        .filter(player -> !player.contiguousForces.isEmpty())
        .toArray(Player[]::new);

    if (playersWithForces.length == 0) {
      this.victors = IntStream.range(0, this.players.length)
          .filter(i -> this.undefeatedPlayers[i] && !state.didPlayerResign(i))
          .mapToObj(i -> this.players[i])
          .toArray(Player[]::new);
      assert this.victors.length > 0;
      return true;
    } else if (playersWithForces.length == 1) {
      this.victors = playersWithForces;
      return true;
    } else {
      ArrayUtil.setAll(this.undefeatedPlayers, i -> !this.players[i].contiguousForces.isEmpty());
      return false;
    }
  }

  public final boolean isLoser(final Player player) {
    return this.victors != null && Arrays.stream(this.victors).noneMatch(Predicate.isEqual(player));
  }

  public final @NotNull Player @Nullable [] getVictors() {
    return this.victors;
  }

  public abstract void initializeFromServer(Buffer packet);
  public abstract void write(WritableBuffer buffer);

  @Contract(pure = true)
  public abstract int victoryPanelHeight();
  public abstract void updateVictoryPanel(GameState state, GameUI ui);
}
