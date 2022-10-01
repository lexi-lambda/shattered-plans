package funorb.shatteredplans.map;

import funorb.Strings;
import funorb.io.WritableBuffer;
import funorb.shatteredplans.client.game.TutorialState;
import funorb.shatteredplans.game.GameState;
import funorb.shatteredplans.client.GameUI;
import funorb.shatteredplans.client.Menu;
import funorb.shatteredplans.game.Player;
import funorb.shatteredplans.StringConstants;
import funorb.shatteredplans.client.ui.Label;
import funorb.io.Buffer;
import funorb.util.ArrayUtil;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Objects;

public final class TutorialVictoryChecker extends VictoryChecker {
  @SuppressWarnings("MismatchedReadAndWriteOfArray")
  private final int[] _n;

  public TutorialVictoryChecker(final Player[] var1) {
    super(var1);
    this._n = new int[this.players.length];
  }

  @Override
  public void initializeFromServer(final Buffer packet) {
  }

  @Override
  public void updateVictoryPanel(final GameState state, final GameUI ui) {
    final int[] var4 = new int[this.players.length];

    int var5a = 0;
    while (var5a < var4.length) {
      var4[var5a] = var5a++;
    }

    final int var5 = Arrays.stream(this._n).sum();
    if (var5 > 0) {
      ArrayUtil.sortScored(var4, this._n.clone());
    }

    final Label[] var18 = new Label[]{new Label(0, 0, GameUI.INFO_PANEL_CONTENT_WIDTH, Menu.SMALL_FONT.ascent, Strings.format(StringConstants.VICTORY_TARGET_POINTS, Integer.toString(10)))};
    final Player[] var19 = new Player[this.players.length];
    final int[] var20 = new int[this.players.length];
    final int[] var9 = new int[this.players.length];

    for (int var10 = 0; var10 < this.players.length; ++var10) {
      var19[var10] = this.players[var4[var10]];
      var20[var10] = this._n[var4[var10]];
      var9[var10] = 0;
    }

    final Player[] var21 = Objects.requireNonNullElseGet(this.victors, () -> new Player[0]);
    ui.updateVictoryPanel(10, var19, var9, var20, var21, var18, 0);
  }

  @Override
  public int victoryPanelHeight() {
    return 224 + Menu.SMALL_FONT.ascent;
  }

  @Override
  public boolean checkVictory(final @NotNull GameState state) {
    if (state.isPlayerDefeated(0)) {
      this.victors = new Player[1];

      for (int var3 = 1; state.players.length > var3; ++var3) {
        if (!state.isPlayerDefeated(var3)) {
          this.victors[0] = state.players[var3];
        }
      }

      return true;
    } else if (TutorialState.stage < 6) {
      return false;
    } else if (!state.isPlayerDefeated(2)) {
      return false;
    } else {
      this.victors = new Player[1];
      this.victors[0] = state.players[0];
      return true;
    }
  }

  @Override
  @Contract(pure = true)
  public @NotNull Player @NotNull [] currentObjectiveLeaders() {
    return new Player[0];
  }

  @Override
  public void write(final WritableBuffer buffer) {
  }
}
