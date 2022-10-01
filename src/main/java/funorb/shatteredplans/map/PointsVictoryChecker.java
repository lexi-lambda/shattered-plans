package funorb.shatteredplans.map;

import funorb.Strings;
import funorb.io.Buffer;
import funorb.io.WritableBuffer;
import funorb.shatteredplans.StringConstants;
import funorb.shatteredplans.client.GameUI;
import funorb.shatteredplans.client.Menu;
import funorb.shatteredplans.client.ui.Label;
import funorb.shatteredplans.game.Force;
import funorb.shatteredplans.game.GameState;
import funorb.shatteredplans.game.Player;
import funorb.util.ArrayUtil;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;

public final class PointsVictoryChecker extends VictoryChecker {
  private final int[] points;
  private final int[] pointsPerTurn;
  private int targetPoints = 0;

  public PointsVictoryChecker(final Player[] players) {
    super(players);
    this.points = new int[this.players.length];
    this.pointsPerTurn = ArrayUtil.create(this.players.length, 3);
  }

  private void initializeTargetPoints(final GameState state) {
    if (this.targetPoints == 0) {
      this.targetPoints = state.getGalaxySize().targetPoints;
    }
  }

  @Override
  @Contract(pure = true)
  public @NotNull Player @NotNull [] currentObjectiveLeaders() {

    if (this.victors == null) {
      int var2 = 0;
      double var3 = Double.MAX_VALUE;

      for (int var5 = 0; var5 < this.players.length; ++var5) {
        if (this.pointsPerTurn[var5] != 0 && this.points[var5] != 0) {
          final double var6 = (double) (this.targetPoints - this.points[var5]) / (double) this.pointsPerTurn[var5];
          if (var3 == var6) {
            ++var2;
          } else if (var3 > var6) {
            var3 = var6;
            var2 = 1;
          }
        }
      }

      if (var2 == 0) {
        return new Player[0];
      } else {
        final Player[] var9 = new Player[var2];

        for (int var10 = 0; this.players.length > var10; ++var10) {
          if (this.pointsPerTurn[var10] != 0) {
            final double var7 = (double) (this.targetPoints - this.points[var10]) / (double) this.pointsPerTurn[var10];
            if (var7 == var3) {
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
    this.targetPoints = packet.readInt();
    Arrays.setAll(this.points, var3 -> packet.readInt());

  }

  @Override
  public int victoryPanelHeight() {
    return GameUI.VICTORY_PANEL_ROW_HEIGHT * this.points.length + Menu.SMALL_FONT.ascent;
  }

  @Override
  public boolean checkVictory(final @NotNull GameState state) {
    if (super.checkVictory(state)) {
      return true;
    }
    this.initializeTargetPoints(state);

    for (int i = 0; i < this.players.length; ++i) {
      this.pointsPerTurn[i] = this.players[i].contiguousForces.stream()
          .flatMap(Force::stream)
          .mapToInt(PointsVictoryChecker::systemPoints)
          .sum();
      this.points[i] += this.pointsPerTurn[i];
    }

    int var3 = 0;
    int var4 = 0;

    for (int i = 0; i < this.points.length; ++i) {
      if (state.isPlayerDefeated(i)) {
        this.points[i] = -1;
      } else if (this.targetPoints <= this.points[i]) {
        if (this.points[i] <= var4) {
          if (var4 == this.points[i]) {
            ++var3;
          }
        } else {
          var3 = 1;
          var4 = this.points[i];
        }
      }
    }

    if (var3 == 0) {
      return false;
    }

    this.victors = new Player[var3];

    for (int i = 0; i < this.players.length; ++i) {
      if (this.points[i] == var4) {
        --var3;
        this.victors[var3] = this.players[i];
      }
    }

    return true;
  }

  private static int systemPoints(final StarSystem system) {
    if (system.score != StarSystem.Score.NORMAL && system.score != StarSystem.Score.TERRAFORMED) {
      if (system.score == StarSystem.Score.NEUTRAL_HOMEWORLD) {
        return 2;
      } else if (system.score == StarSystem.Score.PLAYER_HOMEWORLD) {
        return 3;
      }
    }
    return 0;
  }

  @Override
  public void updateVictoryPanel(final GameState state, final GameUI ui) {
    this.initializeTargetPoints(state);

    final int[] var15 = ArrayUtil.range(this.players.length);
    final int var5a = Arrays.stream(this.points).sum();
    if (var5a > 0) {
      ArrayUtil.sortScored(var15, this.points.clone());
    }

    final Label[] var16 = new Label[]{new Label(0, 0, GameUI.INFO_PANEL_CONTENT_WIDTH, Menu.SMALL_FONT.ascent, Strings.format(StringConstants.VICTORY_TARGET_POINTS, Integer.toString(this.targetPoints)))};
    final Player[] var17 = new Player[this.players.length];
    final int[] var18 = new int[this.players.length];
    final int[] var9 = new int[this.players.length];

    for (int var10 = 0; this.players.length > var10; ++var10) {
      var17[var10] = this.players[var15[var10]];
      var18[var10] = this.points[var15[var10]];
      var9[var10] = this.pointsPerTurn[var15[var10]];
    }

    final Player[] leaders = this.currentObjectiveLeaders();
    final int var11;
    if (this.victors == null && leaders.length != 0) {
      final int var12 = leaders[0].index;
      final double var13 = (double) (-this.points[var12] + this.targetPoints) / (double) this.pointsPerTurn[var12];
      var11 = (int) Math.ceil(var13);
    } else {
      var11 = 0;
    }

    ui.updateVictoryPanel(this.targetPoints, var17, var9, var18, leaders, var16, var11);
  }

  @Override
  public void write(final WritableBuffer buffer) {
    buffer.writeInt(this.targetPoints);
    Arrays.stream(this.points).forEach(buffer::writeInt);
  }
}
