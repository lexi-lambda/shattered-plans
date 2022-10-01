package funorb.shatteredplans.map.generator;

import funorb.shatteredplans.client.ShatteredPlansClient;
import funorb.shatteredplans.map.Map;
import funorb.shatteredplans.map.StarSystem;

import java.util.Random;

public final class CaptureAndHoldMapGenerator extends StandardMapGenerator {
  public static final int SOL_INDEX = 36;
  private static final boolean[] _act = new boolean[]{false, false, false, true, true, true, true, true, true, false, false, false, false, true, true, true, false, true, true, true, false, false, false, false, true, true, false, true, true, false, true, true, false, false, true, false, true, false, true, false, true, false, true, false, false, true, true, true, true, true, true, true, true, true, true, true, true, false, false, true, true, true, false, false, true, true, false, true, true, true, true, true, true, true, true, true, true, false, true, false, true, false, true, false, true, false, true, false, false, false, true, true, false, true, true, false, true, true, false, false, false, true, true, true, false, true, true, true, false, false, false, false, false, true, true, true, true, true, true, false, false};
  private static final int[] _ppif = new int[]{0, 5, 39, 72, 67, 33};

  public CaptureAndHoldMapGenerator(final int var1, final int var2) {
    super(11, 11, var1, 73, var2);
    this.neutralHomeworldCount = 1;
  }

  @Override
  public int[] selectHomeworlds(final Random random, final StarSystem[] systems, final int[][] distances, final int count, final int minNeighbors, final int maxNeighbors, final int minDistance, final int[] alreadySelected) throws MapGenerationFailure {
    final int[] var10 = new int[count];
    int var11;
    if (alreadySelected == null) {
      if (count == 2) {
        var10[0] = _ppif[0];
        var10[1] = _ppif[3];
      } else if (count == 3) {
        var10[0] = _ppif[0];
        var10[1] = _ppif[2];
        var10[2] = _ppif[4];
      } else if (count == 4) {
        var10[1] = _ppif[1];
        var10[2] = _ppif[3];
        var10[0] = _ppif[0];
        var10[3] = _ppif[4];
      } else {
        int var13;
        if (count == 5) {
          byte var12 = 0;
          var11 = ShatteredPlansClient.randomIntBounded(random, 6);

          for (var13 = 0; count > var13; ++var13) {
            if (var11 <= var13) {
              var12 = 1;
            }

            var10[var13] = _ppif[var12 + var13];
          }
        } else {
          if (count != 6) {
            throw new MapGenerationFailure("Sol Map does not support " + count + " players.");
          }

          for (var13 = 0; var13 < count; ++var13) {
            var10[var13] = _ppif[var13];
          }
        }
      }

    } else {
      for (var11 = 0; alreadySelected.length > var11; ++var11) {
        var10[var11] = alreadySelected[var11];
      }

      var10[count - 1] = SOL_INDEX;
    }
    return var10;
  }

  @Override
  public void placeSystems(final Map map) {
    int[] var3 = null;
    if (StarSystem.NAMES != null) {
      final int var4 = StarSystem.NAMES.length;
      var3 = new int[var4];

      int var5a = 0;
      while (var4 > var5a) {
        var3[var5a] = var5a++;
      }

      for (var5a = 0; var5a < var4 - 1; ++var5a) {
        final int var6 = var5a + 1 + ShatteredPlansClient.randomIntBounded(map.random, 1 - (var5a + 1) + var4 - 1);
        final int var7 = var3[var6];
        var3[var6] = var3[var5a];
        var3[var5a] = var7;
      }
    }

    this.tiles = new StarSystem[this.tileWidth * this.tileHeight];
    final StarSystem[] var8 = new StarSystem[this.systemCount];
    int var5 = 0;

    for (int var6a = 0; this.tiles.length > var6a; ++var6a) {
      if (_act[var6a]) {
        var8[var5] = StandardMapGenerator.createStarSystem(var5, var6a, this.tileWidth);
        assert var3 != null;
        var8[var5].name = StarSystem.NAMES[var3[var5]];
        if (var5 == SOL_INDEX) {
          var8[SOL_INDEX].name = "SOL";
        }

        this.tiles[var6a] = var8[var5];
        ++var5;
      }
    }

    map.systems = var8;
    StandardMapGenerator.computePotentialWormholeConnections(map.systems);
    this.calculateHexPoints();
  }
}
