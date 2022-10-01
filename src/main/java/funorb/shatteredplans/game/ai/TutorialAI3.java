package funorb.shatteredplans.game.ai;

import funorb.shatteredplans.game.Player;
import funorb.shatteredplans.client.game.ClientGameSession;
import funorb.shatteredplans.game.GameState;
import funorb.shatteredplans.map.StarSystem;

import java.util.Arrays;

public final class TutorialAI3 extends TutorialAI2 {

  public TutorialAI3(final GameState var1, final Player var2, final ClientGameSession var3) {
    super(var1, var2, var3);
  }

  @Override
  int a555(final StarSystem var1) {
    int var3 = var1.garrison;
    if (var1.hasDefensiveNet) {
      var3 = var3 * 2 + 5;
    }

    var3 += var1.neighbors.length;

    var3 += this.a263(var1);
    if (var3 < 1) {
      var3 = 1;
    }

    return var3;
  }

  @Override
  int b080(int var1) {
    int var3 = 0;
    for (final StarSystem var4 : TutorialAI1._mjyb) {
      if (var1 == 0) {
        return var3;
      }

      int var5 = 0;
      final StarSystem[] var6 = var4.neighbors;

      for (final StarSystem var8 : var6) {
        if (var8.owner == this._n) {
          final boolean var9 = Arrays.stream(var8.neighbors)
              .anyMatch(neighbor -> this._n != neighbor.owner && neighbor.owner != null && !this._n.allies[neighbor.owner.index]);
          var5 += var9 ? 2 : 1;
        }
      }

      var5 >>= 1;
      if (var1 < var5) {
        var5 = var1;
      }

      final int[] var10000 = TutorialAI1._nlb;
      final int var10001 = var4.index;
      var10000[var10001] += var5;
      var1 -= var5;
      var3 += var5;
    }

    return var3;
  }

  @Override
  int d828(final StarSystem var1) {

    final int var3 = var1.garrison + var1.neighbors.length;
    return var3 != 0 ? var3 : 1;
  }

  @Override
  void g150() {
    this._k = -1;
    final int var2 = this.gameState.playerCount;

    for (int var3 = 0; var3 < var2; ++var3) {
      final int var4 = this.gameState.playerFleetProductionRanks[var2 - 1 - var3];
      if (this._n.index != var4 && !this._n.allies[var4] && !this.gameState.isPlayerDefeated(var4)) {
        boolean var5 = true;

        for (int var6 = 0; this.gameState.playerCount > var6; ++var6) {
          if (var3 != var6 && this.gameState.players[var3].allies[var6] || this.gameState.isPlayerDefeated(var6)) {
            var5 = false;
            break;
          }
        }

        this._b[var3] = var5 ? -3 : 4;
        if (this.gameState.playerFleetProduction[this._n.index] > 2 * this.gameState.playerFleetProduction[var3]) {
          this._b[var3] = 3;
          var5 = false;
        }

        if (var5 && this._k == -1) {
          this._k = var3;
        }
      }
    }

  }

  @Override
  int a543() {
    int var3 = 0;
    for (final StarSystem var4 : TutorialAI1._mjyb) {
      final int var5 = var4.index;
      final int var6 = (int) Arrays.stream(var4.neighbors).filter(neighbor -> neighbor.owner != this._n).count();

      final int[] var10000 = TutorialAI1._jai;
      var10000[var5] += var4.garrison;
      TutorialAI1._nlb[var5] = var6;
      var3 += var6 - var4.garrison;
    }
    return var3;
  }
}
