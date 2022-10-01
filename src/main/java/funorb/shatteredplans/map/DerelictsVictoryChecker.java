package funorb.shatteredplans.map;

import funorb.Strings;
import funorb.io.WritableBuffer;
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

public final class DerelictsVictoryChecker extends VictoryChecker {
  private final int[] _q;
  private final int[] _o;

  public DerelictsVictoryChecker(final Player[] var1) {
    super(var1);
    this._o = new int[this.players.length];
    this._q = new int[this.players.length];
  }

  @Override
  public int victoryPanelHeight() {
    return Menu.SMALL_FONT.ascent + this._o.length * GameUI.VICTORY_PANEL_ROW_HEIGHT;
  }

  @Override
  public boolean checkVictory(final @NotNull GameState state) {
    if (super.checkVictory(state)) {
      return true;
    } else {
      int var3;
      for (var3 = 0; var3 < this.players.length; ++var3) {
        this._q[var3] = 0;
      }

      final StarSystem[] var6 = state.map.systems;

      int[] var10000;
      int var4;
      for (var4 = 0; var4 < var6.length; ++var4) {
        final StarSystem var5 = var6[var4];
        if (var5.score == StarSystem.Score.NEUTRAL_HOMEWORLD && var5.owner != null) {
          var10000 = this._q;
          final int var10001 = var5.owner.index;
          var10000[var10001] -= var5.resources[0];
        }
      }

      for (var3 = 0; this.players.length > var3; ++var3) {
        var10000 = this._o;
        var10000[var3] += this._q[var3];
      }

      var3 = 0;
      var4 = 0;

      int var7;
      for (var7 = 0; this._o.length > var7; ++var7) {
        if (state.isPlayerDefeated(var7)) {
          this._o[var7] = -1;
        } else if (this._o[var7] >= 50) {
          if (this._o[var7] > var4) {
            var4 = this._o[var7];
            var3 = 1;
          } else if (var4 == this._o[var7]) {
            ++var3;
          }
        }
      }

      if (var3 == 0) {
        return false;
      } else {
        this.victors = new Player[var3];

        for (var7 = 0; var7 < this.players.length; ++var7) {
          if (this._o[var7] == var4) {
            --var3;
            this.victors[var3] = this.players[var7];
          }
        }

        return true;
      }
    }
  }

  @Override
  public void write(final WritableBuffer buffer) {
    for (final int i : this._o) {
      buffer.writeByte(i);
    }

  }

  @Override
  @Contract(pure = true)
  public @NotNull Player @NotNull [] currentObjectiveLeaders() {

    if (this.victors == null) {
      int var2 = 0;
      double var3 = Double.MAX_VALUE;

      for (int var5 = 0; this.players.length > var5; ++var5) {
        if (this._q[var5] != 0 && this._o[var5] != 0) {
          final double var6 = (double) (50 - this._o[var5]) / (double) this._q[var5];
          if (var6 == var3) {
            ++var2;
          } else if (var3 > var6) {
            var2 = 1;
            var3 = var6;
          }
        }
      }

      if (var2 == 0) {
        return new Player[0];
      } else {
        final Player[] var9 = new Player[var2];

        for (int var10 = 0; var10 < this.players.length; ++var10) {
          if (this._q[var10] != 0) {
            final double var7 = (double) (50 - this._o[var10]) / (double) this._q[var10];
            if (var3 == var7) {
              --var2;
              var9[var2] = this.players[var10];
            }
          }
        }

        return var9;
      }
    } else {
      return this.victors;
    }
  }

  @Override
  public void initializeFromServer(final Buffer packet) {
    Arrays.setAll(this._o, i -> packet.readByte());
  }

  @Override
  public void updateVictoryPanel(final GameState state, final GameUI ui) {
    final int[] var4 = new int[this.players.length];
    int var5 = 0;
    while (var4.length > var5) {
      var4[var5] = var5++;
    }

    final int var5a = Arrays.stream(this._o).sum();
    if (var5a > 0) {
      ArrayUtil.sortScored(var4, this._o.clone());
    }

    final Label[] var15 = new Label[]{new Label(0, 0, GameUI.INFO_PANEL_CONTENT_WIDTH, Menu.SMALL_FONT.ascent, Strings.format(StringConstants.VICTORY_TARGET_POINTS, Integer.toString(50)))};
    final Player[] var16 = new Player[this.players.length];
    final int[] var17 = new int[this.players.length];
    final int[] var9 = new int[this.players.length];

    for (int var10 = 0; var10 < this.players.length; ++var10) {
      var16[var10] = this.players[var4[var10]];
      var17[var10] = this._o[var4[var10]];
      var9[var10] = this._q[var4[var10]];
    }

    final Player[] var18 = this.currentObjectiveLeaders();
    final int var11;
    if (this.victors == null && var18.length != 0) {
      final int var12 = var18[0].index;
      final double var13 = (double) (-this._o[var12] + 50) / (double) this._q[var12];
      var11 = (int) Math.ceil(var13);
    } else {
      var11 = 0;
    }

    ui.updateVictoryPanel(50, var16, var9, var17, var18, var15, var11);
  }
}
