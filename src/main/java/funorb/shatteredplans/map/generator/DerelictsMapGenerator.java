package funorb.shatteredplans.map.generator;

import funorb.graphics.Point;
import funorb.shatteredplans.client.ShatteredPlansClient;
import funorb.shatteredplans.StringConstants;
import funorb.shatteredplans.map.Map;
import funorb.shatteredplans.map.StarSystem;

public final class DerelictsMapGenerator extends StandardMapGenerator {
  public DerelictsMapGenerator(final int var1, final int var2, final int var3, final int var4, final int var5) {
    super(var1, var2, var3, var4, var5);
    this.neutralHomeworldCount = this.playerCount;
  }

  @Override
  public Map generate() throws MapGenerationFailure {
    final Map var2 = super.generate();
    int var3 = this.tileWidth * this.tileHeight / 2;
    if (this.tileHeight * this.tileWidth % 2 == 0) {
      var3 -= this.tileWidth / 2;
    }

    final StarSystem var4 = StandardMapGenerator.createStarSystem(var2.systems.length, var3, this.tileWidth);
    var4.score = StarSystem.Score.NEUTRAL_HOMEWORLD;

    for (int var5 = 0; var5 < 4; ++var5) {
      var4.resources[var5] = -5;
    }

    var4.name = StringConstants.DERELICT + " " + (char) (65 + ShatteredPlansClient.randomIntBounded(var2.random, 26)) + (char) (65 + ShatteredPlansClient.randomIntBounded(var2.random, 26)) + (char) (65 + ShatteredPlansClient.randomIntBounded(var2.random, 26)) + "-" + 5;
    var4.garrison = 30;
    var4.type = StarSystem.Type.ALIEN_BASE;
    var4.hasDefensiveNet = true;
    var4.hexPoints = new Point[6];
    var4.hexPoints[0] = new Point(var4.posnX, (int) ((double) var4.posnY - Map.COS_THIRTY_DEGREES_RECIP * 200.0D * 0.5D));
    var4.hexPoints[1] = new Point(100 + var4.posnX, (int) ((double) var4.posnY - 0.25D * 200.0D * Map.COS_THIRTY_DEGREES_RECIP));
    var4.hexPoints[2] = new Point(100 + var4.posnX, (int) ((double) var4.posnY + 0.25D * Map.COS_THIRTY_DEGREES_RECIP * 200.0D));
    var4.hexPoints[3] = new Point(var4.posnX, (int) (Map.COS_THIRTY_DEGREES_RECIP * 200.0D * 0.5D + (double) var4.posnY));
    var4.hexPoints[4] = new Point(var4.posnX - 100, (int) (0.25D * 200.0D * Map.COS_THIRTY_DEGREES_RECIP + (double) var4.posnY));
    var4.hexPoints[5] = new Point(var4.posnX - 100, (int) ((double) var4.posnY - 0.25D * 200.0D * Map.COS_THIRTY_DEGREES_RECIP));

    var4.neighbors = new StarSystem[0];
    final StarSystem[] var13 = new StarSystem[var2.systems.length + 1];
    System.arraycopy(var2.systems, 0, var13, 0, var2.systems.length);
    var13[var2.systems.length] = var4;
    var2.systems = var13;
    var2.recalculateDistances();
    var2.recalculateMovementCosts();
    final StarSystem[] var8 = var2.systems;

    for (final StarSystem var10 : var8) {
      if (var10.score == StarSystem.Score.NEUTRAL_HOMEWORLD && var10 != var4) {
        for (int var11 = 0; var11 < 4; ++var11) {
          var10.resources[var11] = -2;
        }

        var10.type = var10.index % 2 == 0 ? StarSystem.Type.ALIEN_MINER : StarSystem.Type.ALIEN_SHIP;
        var10.name = StringConstants.DERELICT + " " + (char) (ShatteredPlansClient.randomIntBounded(var2.random, 26) + 65) + (char) (ShatteredPlansClient.randomIntBounded(var2.random, 26) + 65) + (char) (65 + ShatteredPlansClient.randomIntBounded(var2.random, 26)) + "-" + 2;
        var10.garrison = 15;
      }
    }

    return var2;
  }

  @Override
  public void placeSystems(final Map map) throws MapGenerationFailure {
    this.tiles = new StarSystem[this.tileWidth * this.tileHeight];
    int var3 = this.tiles.length / 2;
    if (this.tileWidth * this.tileHeight % 2 == 0) {
      var3 -= this.tileWidth / 2;
    }

    this.tiles[var3] = MapGenerator.DUMMY_SYSTEM;
    this.tiles[var3 - 1] = MapGenerator.DUMMY_SYSTEM;
    this.tiles[1 + var3] = MapGenerator.DUMMY_SYSTEM;
    this.tiles[this.tileWidth + var3] = MapGenerator.DUMMY_SYSTEM;
    this.tiles[var3 - this.tileWidth] = MapGenerator.DUMMY_SYSTEM;
    final boolean var4 = var3 / this.tileWidth % 2 == 0;
    if (var4) {
      this.tiles[-this.tileWidth + var3 - 1] = MapGenerator.DUMMY_SYSTEM;
      this.tiles[this.tileWidth + var3 - 1] = MapGenerator.DUMMY_SYSTEM;
    } else {
      this.tiles[1 - this.tileWidth + var3] = MapGenerator.DUMMY_SYSTEM;
      this.tiles[var3 + this.tileWidth + 1] = MapGenerator.DUMMY_SYSTEM;
    }

    super.placeSystems(map);
  }

}
