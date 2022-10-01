package funorb.shatteredplans.map;

import funorb.Strings;
import funorb.io.Buffer;
import funorb.io.WritableBuffer;
import funorb.shatteredplans.StringConstants;
import funorb.shatteredplans.client.GameUI;
import funorb.shatteredplans.client.Menu;
import funorb.shatteredplans.client.ui.Label;
import funorb.shatteredplans.game.GameState;
import funorb.shatteredplans.game.Player;
import funorb.util.ArrayUtil;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.stream.IntStream;

import static funorb.shatteredplans.map.generator.CaptureAndHoldMapGenerator.SOL_INDEX;

public final class CaptureAndHoldVictoryChecker extends VictoryChecker {
  private static final int TURN_COUNT = 20;

  private final int[] _n;
  private int _r;
  private Player _o;
  private Player _l;

  public CaptureAndHoldVictoryChecker(final @NotNull Player @NotNull [] players) {
    super(players);
    this._n = new int[this.players.length];
  }

  @Override
  public boolean checkVictory(final @NotNull GameState state) {
    if (super.checkVictory(state)) {
      return true;
    }

    this._l = state.map.systems[SOL_INDEX].owner;
    if (this._l != null) {
      if (this._o == null) {
        this._o = this._l;
      }
    }

    IntStream.range(0, this.players.length)
        .filter(state::isPlayerDefeated)
        .forEach(i -> this._n[i] = -1);

    if (state.turnNumber < TURN_COUNT - 1) {
      return false;
    }

    int var3 = 0;
    int var4 = 0;
    Player var5 = null;

    for (int i = 0; i < this._n.length; ++i) {
      if (var3 == this._n[i]) {
        ++var4;
      }

      if (var3 < this._n[i]) {
        var4 = 1;
        var5 = this.players[i];
        var3 = this._n[i];
      }
    }

    if (var4 == 1) {
      assert var5 != null;
      this.victors = new Player[]{var5};
    } else {
      for (int var6 = 0; this.players.length > var6; ++var6) {
        if (this._n[var6] == var3 && this.players[var6] == this._l) {
          this.victors = new Player[]{this._l};
          return true;
        }
      }

      this.victors = new Player[var4];
      int var6 = 0;

      for (int var7 = 0; this.players.length > var7; ++var7) {
        if (this._n[var7] == var3) {
          this.victors[var6] = this.players[var7];
          ++var6;
        }
      }
    }
    return true;
  }

  @Override
  @Contract(pure = true)
  public @NotNull Player @NotNull [] currentObjectiveLeaders() {
    if (this.victors != null) {
      return this.victors;
    }

    int var2 = 0;
    int var3 = 1;

    for (int i = 0; i < this.players.length; ++i) {
      final int var5 = (this._l == this.players[i] ? this._r : 0) + this._n[i];
      if (var5 == var3) {
        ++var2;
      }

      if (var3 < var5) {
        var2 = 1;
        var3 = var5;
      }
    }

    final Player[] var7 = new Player[var2];
    for (int var5 = 0; var5 < this.players.length; ++var5) {
      final int var6 = this._n[var5] + (this.players[var5] == this._l ? this._r : 0);
      if (var3 == var6) {
        --var2;
        var7[var2] = this.players[var5];
      }
    }

    return var7;
  }

  @Override
  public void initializeFromServer(final Buffer packet) {
    Arrays.setAll(this._n, i -> packet.readByte());
  }

  @Override
  public void write(final WritableBuffer buffer) {
    for (final int i : this._n) {
      buffer.writeByte(i);
    }
  }

  @Override
  public int victoryPanelHeight() {
    return Menu.SMALL_FONT.ascent * 3 + this._n.length * GameUI.VICTORY_PANEL_ROW_HEIGHT;
  }

  @Override
  public void updateVictoryPanel(final GameState state, final GameUI ui) {
    final Player var4 = state.map.systems[SOL_INDEX].owner;
    final int[] var5 = new int[this.players.length];

    int var6 = 0;
    while (var6 < var5.length) {
      var5[var6] = var6++;
    }

    final int i = Arrays.stream(this._n).sum();
    if (i > 0) {
      ArrayUtil.sortScored(var5, this._n.clone());
    }

    final Label[] var12 = new Label[]{new Label(0, 0, GameUI.INFO_PANEL_CONTENT_WIDTH, Menu.SMALL_FONT.ascent, Strings.format(StringConstants.VICTORY_TURNS_REMAINING, Integer.toString(20 - state.turnNumber))), null, null};
    if (var4 == null) {
      var12[1] = new Label(0, 0, GameUI.INFO_PANEL_CONTENT_WIDTH, Menu.SMALL_FONT.ascent, StringConstants.VICTORY_SOL_EMPTY);
      var12[2] = new Label(0, 0, GameUI.INFO_PANEL_CONTENT_WIDTH, Menu.SMALL_FONT.ascent, null);
    } else {
      var12[1] = new Label(0, 0, GameUI.INFO_PANEL_CONTENT_WIDTH, Menu.SMALL_FONT.ascent, StringConstants.VICTORY_SOL_OWNED);
      var12[2] = new Label(0, 0, GameUI.INFO_PANEL_CONTENT_WIDTH, Menu.SMALL_FONT.ascent, var4.name, var4.color1);
    }

    final Player[] var13 = new Player[this.players.length];
    final int[] var14 = new int[this.players.length];
    final int[] var10 = new int[this.players.length];

    for (int var11 = 0; var11 < this.players.length; ++var11) {
      var13[var11] = this.players[var5[var11]];
      var14[var11] = this._n[var5[var11]];
      var10[var11] = var4 == this.players[var5[var11]] ? 1 : 0;
    }

    final Player[] var15 = this.currentObjectiveLeaders();
    if (this.victors == null) {
      this._r = TURN_COUNT - state.turnNumber;
    } else {
      this._r = 0;
    }

    ui.updateVictoryPanel(20, var13, var10, var14, var15, var12, this._r);
  }
}
