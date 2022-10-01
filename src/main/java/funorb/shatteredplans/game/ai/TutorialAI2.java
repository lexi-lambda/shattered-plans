package funorb.shatteredplans.game.ai;

import funorb.client.lobby.ChatMessage;
import funorb.shatteredplans.game.Player;
import funorb.shatteredplans.client.game.ClientGameSession;
import funorb.shatteredplans.client.ShatteredPlansClient;
import funorb.shatteredplans.game.GameState;
import funorb.shatteredplans.map.StarSystem;
import org.jetbrains.annotations.NotNull;

import java.util.stream.IntStream;

public class TutorialAI2 extends TutorialAI1 {

  TutorialAI2(final GameState var1, final Player var2, final ClientGameSession var3) {
    super(var1, var2, var3);
  }

  public static boolean a051(final int[] var0, final String var1, final @NotNull ChatMessage.Channel var2, final long var3) {
    return var0 != null && (var2 != ChatMessage.Channel.PRIVATE || ShatteredPlansClient.a788(var3, var1));
  }

  @Override
  void g150() {
    final int playerCount = this.gameState.playerCount;

    this._k = -1;
    final boolean[] var4 = new boolean[this._o];

    final int[][] var3 = IntStream.range(0, playerCount)
        .mapToObj(var5 -> this.a931(this.gameState.players[var5]))
        .toArray(int[][]::new);

    final int[] var13 = var3[this._n.index];
    int var6 = 0;

    int var7;
    int var9;
    for (var7 = 0; var7 < this._o; ++var7) {
      if (this.gameState.map.systems[var7].owner == this._n) {
        var4[var7] = false;
      } else {
        boolean var8 = true;

        for (var9 = 0; playerCount > var9; ++var9) {
          if (var9 != this._n.index && this._n.allies[var9] && var13[var7] >= var3[var9][var7]) {
            var8 = false;
            break;
          }
        }

        var4[var7] = var8;
        if (var8) {
          ++var6;
        }
      }
    }

    for (var7 = 0; playerCount > var7; ++var7) {
      final int var14 = this.gameState.playerFleetProductionRanks[-var7 + (playerCount - 1)];
      if (this._n.index != var14 && !this._n.allies[var14] && !this.gameState.isPlayerDefeated(var14)) {
        var9 = var6;

        for (int var10 = 0; var10 < this._o; ++var10) {
          if (var4[var10] && var13[var10] >= var3[var14][var10]) {
            --var9;
          }
        }

        final boolean var15 = var9 >= 5;
        this._b[var7] = !var15 ? 4 : -3;
        if (var15 && this._k == -1) {
          this._k = var7;

          for (int var11 = 0; var11 < this._o; ++var11) {
            if (var4[var11]) {
              final boolean var12 = var3[this._k][var11] > var13[var11];
              var4[var11] = var12;
              if (!var12) {
                --var6;
              }
            }
          }
        }
      }
    }

  }

  @Override
  final int a353(final int var1) {
    return var1 * 3 >> 2;
  }

  @Override
  final int b263(final StarSystem var2) {
    return super.b263(var2);
  }
}
