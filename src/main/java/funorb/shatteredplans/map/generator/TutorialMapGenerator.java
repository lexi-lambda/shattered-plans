package funorb.shatteredplans.map.generator;

import funorb.shatteredplans.StringConstants;
import funorb.shatteredplans.map.Map;
import funorb.shatteredplans.map.StarSystem;

public final class TutorialMapGenerator extends StandardMapGenerator {
  public TutorialMapGenerator() {
    super(1, 1, 0, 1, 1);
  }

  private void e150() {
    final StarSystem[] var2 = this.tiles;
    for (final StarSystem var4 : var2) {
      if (var4 != null) {
        final int var5 = var4.index;
        var4.minimumGarrison = 1;
        if (var5 == 0) {
          var4.garrison = 20;
          var4.type = StarSystem.Type.PLANET_EARTH_LIKE;
          var4.score = StarSystem.Score.PLAYER_HOMEWORLD;
          var4.resources = new int[]{2, 2, 2, 4};
        } else if (var5 == 1) {
          var4.garrison = 3;
          var4.type = StarSystem.Type.PLANET_BURNT;
          var4.resources = new int[]{1, 1, 0, 0};
          var4.score = StarSystem.Score.NORMAL;
        } else if (var5 == 2) {
          var4.garrison = 4;
          var4.score = StarSystem.Score.NORMAL;
          var4.type = StarSystem.Type.PLANET_GAS;
          var4.resources = new int[]{0, 1, 1, 0};
        } else if (var5 == 3) {
          var4.score = StarSystem.Score.NORMAL;
          var4.resources = new int[]{1, 0, 0, 0};
          var4.type = StarSystem.Type.PLANET_ROCK;
          var4.garrison = 1;
        } else if (var5 == 4) {
          var4.score = StarSystem.Score.NORMAL;
          var4.type = StarSystem.Type.PLANET_EARTH_LIKE;
          var4.garrison = 4;
          var4.resources = new int[]{0, 3, 1, 0};
        } else if (var5 == 5) {
          var4.garrison = 7;
          var4.score = StarSystem.Score.NORMAL;
          var4.type = StarSystem.Type.PLANET_RINGED;
          var4.resources = new int[]{3, 0, 3, 0};
        } else if (var5 == 6) {
          var4.garrison = 3;
          var4.score = StarSystem.Score.NORMAL;
          var4.type = StarSystem.Type.PLANET_EXOTIC;
          var4.resources = new int[]{0, 0, 0, 2};
        } else if (var5 == 7) {
          var4.garrison = 15;
          var4.type = StarSystem.Type.PLANET_EARTH_LIKE;
          var4.resources = new int[]{2, 3, 2, 3};
          var4.score = StarSystem.Score.PLAYER_HOMEWORLD;
        } else if (var5 == 8) {
          var4.score = StarSystem.Score.NORMAL;
          var4.garrison = 0;
          var4.type = StarSystem.Type.PLANET_BURNT;
          var4.resources = new int[]{1, 0, 0, 0};
        } else if (var5 == 9) {
          var4.garrison = 3;
          var4.resources = new int[]{0, 1, 1, 0};
          var4.score = StarSystem.Score.NORMAL;
          var4.type = StarSystem.Type.PLANET_GAS;
        } else if (var5 == 10) {
          var4.resources = new int[]{0, 2, 0, 2};
          var4.garrison = 6;
          var4.score = StarSystem.Score.NORMAL;
          var4.type = StarSystem.Type.PLANET_EXOTIC;
        } else if (var5 == 11) {
          var4.type = StarSystem.Type.PLANET_RINGED;
          var4.score = StarSystem.Score.NORMAL;
          var4.resources = new int[]{1, 0, 1, 0};
          var4.garrison = 2;
        } else if (var5 == 12) {
          var4.garrison = 10;
          var4.type = StarSystem.Type.PLANET_EARTH_LIKE;
          var4.score = StarSystem.Score.NEUTRAL_HOMEWORLD;
          var4.resources = new int[]{2, 2, 1, 1};
        } else if (var5 == 13) {
          var4.score = StarSystem.Score.NORMAL;
          var4.resources = new int[]{0, 0, 1, 0};
          var4.type = StarSystem.Type.PLANET_ROCK;
          var4.garrison = 1;
        } else if (var5 == 14) {
          var4.score = StarSystem.Score.NORMAL;
          var4.garrison = 2;
          var4.resources = new int[]{0, 0, 0, 1};
          var4.type = StarSystem.Type.PLANET_GAS;
        } else if (var5 == 15) {
          var4.score = StarSystem.Score.NORMAL;
          var4.resources = new int[]{1, 1, 0, 0};
          var4.type = StarSystem.Type.PLANET_BURNT;
          var4.garrison = 2;
        } else if (var5 == 16) {
          var4.score = StarSystem.Score.NORMAL;
          var4.garrison = 3;
          var4.type = StarSystem.Type.PLANET_RINGED;
          var4.resources = new int[]{0, 0, 2, 0};
        } else if (var5 == 17) {
          var4.resources = new int[]{2, 2, 4, 2};
          var4.type = StarSystem.Type.PLANET_EARTH_LIKE;
          var4.garrison = 20;
          var4.score = StarSystem.Score.PLAYER_HOMEWORLD;
        } else if (var5 == 18) {
          var4.type = StarSystem.Type.PLANET_EXOTIC;
          var4.garrison = 2;
          var4.resources = new int[]{0, 0, 0, 2};
          var4.score = StarSystem.Score.NORMAL;
        } else if (var5 == 19) {
          var4.resources = new int[]{1, 0, 0, 0};
          var4.type = StarSystem.Type.PLANET_ROCK;
          var4.score = StarSystem.Score.NORMAL;
          var4.garrison = 0;
        } else if (var5 == 20) {
          var4.resources = new int[]{1, 0, 0, 1};
          var4.score = StarSystem.Score.NORMAL;
          var4.type = StarSystem.Type.PLANET_BURNT;
          var4.garrison = 3;
        } else if (var5 == 21) {
          var4.garrison = 4;
          var4.score = StarSystem.Score.NORMAL;
          var4.type = StarSystem.Type.PLANET_GAS;
          var4.resources = new int[]{0, 0, 3, 0};
        } else if (var5 == 22) {
          var4.garrison = 5;
          var4.type = StarSystem.Type.PLANET_EARTH_LIKE;
          var4.resources = new int[]{1, 1, 1, 0};
          var4.score = StarSystem.Score.NORMAL;
        } else if (var5 == 23) {
          var4.garrison = 1;
          var4.resources = new int[]{0, 0, 0, 1};
          var4.type = StarSystem.Type.PLANET_EXOTIC;
          var4.score = StarSystem.Score.NORMAL;
        } else if (var5 == 24) {
          var4.garrison = 15;
          var4.type = StarSystem.Type.ALIEN_SHIP;
          var4.score = StarSystem.Score.NEUTRAL_HOMEWORLD;
          var4.resources = new int[]{-2, -2, -2, -2};
        } else if (var5 == 25) {
          var4.resources = new int[]{1, 2, 0, 0};
          var4.score = StarSystem.Score.NORMAL;
          var4.type = StarSystem.Type.PLANET_ROCK;
          var4.garrison = 4;
        } else if (var5 == 26) {
          var4.type = StarSystem.Type.PLANET_EXOTIC;
          var4.score = StarSystem.Score.NORMAL;
          var4.garrison = 3;
          var4.resources = new int[]{1, 0, 0, 2};
        } else if (var5 == 27) {
          var4.garrison = 1;
          var4.type = StarSystem.Type.PLANET_RINGED;
          var4.score = StarSystem.Score.NORMAL;
          var4.resources = new int[]{0, 0, 1, 0};
        } else if (var5 == 28) {
          var4.score = StarSystem.Score.NORMAL;
          var4.type = StarSystem.Type.PLANET_BURNT;
          var4.garrison = 2;
          var4.resources = new int[]{0, 1, 0, 1};
        } else if (var5 == 29) {
          var4.resources = new int[]{2, 0, 0, 0};
          var4.garrison = 3;
          var4.score = StarSystem.Score.NORMAL;
          var4.type = StarSystem.Type.PLANET_ROCK;
        } else if (var5 == 30) {
          var4.type = StarSystem.Type.PLANET_EARTH_LIKE;
          var4.score = StarSystem.Score.NEUTRAL_HOMEWORLD;
          var4.garrison = 9;
          var4.resources = new int[]{2, 2, 1, 1};
        } else if (var5 == 31) {
          var4.garrison = 1;
          var4.type = StarSystem.Type.PLANET_GAS;
          var4.resources = new int[]{0, 0, 1, 0};
          var4.score = StarSystem.Score.NORMAL;
        } else if (var5 == 32) {
          var4.type = StarSystem.Type.PLANET_BURNT;
          var4.resources = new int[]{0, 1, 0, 0};
          var4.score = StarSystem.Score.NORMAL;
          var4.garrison = 0;
        } else if (var5 == 33) {
          var4.type = StarSystem.Type.PLANET_ROCK;
          var4.resources = new int[]{1, 0, 1, 0};
          var4.score = StarSystem.Score.NORMAL;
          var4.garrison = 15;
        } else if (var5 == 34) {
          var4.resources = new int[]{1, 0, 0, 0};
          var4.score = StarSystem.Score.NORMAL;
          var4.garrison = 15;
          var4.type = StarSystem.Type.PLANET_RINGED;
        } else if (var5 == 35) {
          var4.garrison = 15;
          var4.score = StarSystem.Score.NORMAL;
          var4.resources = new int[]{0, 0, 1, 1};
          var4.type = StarSystem.Type.PLANET_EXOTIC;
        } else if (var5 == 36) {
          var4.type = StarSystem.Type.PLANET_ROCK;
          var4.garrison = 15;
          var4.score = StarSystem.Score.TERRAFORMED;
          var4.resources = new int[]{1, 2, 1, 2};
        } else if (var5 == 37) {
          var4.score = StarSystem.Score.PLAYER_HOMEWORLD;
          var4.resources = new int[]{2, 3, 2, 3};
          var4.type = StarSystem.Type.PLANET_EARTH_LIKE;
          var4.garrison = 5;
        }
      }
    }

  }

  private void a093(final int var2) {
    StandardMapGenerator.linkAdjacentNeighbors(this.tiles, this.tileWidth, this.tileHeight);
    if (var2 >= 3) {
      StarSystem.linkNeighbors(this.tiles[1], this.tiles[3]);
    }

    if (var2 >= 4) {
      StarSystem.linkNeighbors(this.tiles[this.tileWidth + 3], this.tiles[3 * this.tileWidth + 4]);
    }

    if (var2 >= 6) {
      StarSystem.linkNeighbors(this.tiles[this.tileWidth * 4 + 3], this.tiles[this.tileWidth * 6 + 2]);
    }

    if (var2 >= 7) {
      StarSystem.linkNeighbors(this.tiles[this.tileWidth * 5 + 6], this.tiles[this.tileWidth * 3 + 7]);
    }

  }

  @Override
  public Map generate() {
    final Map var2 = new Map(this.tileWidth * 200 + 100, Map.drawingHeight(this.tileHeight), this.seed, this.systemCount);
    this.tiles = new StarSystem[1];
    this.tiles[0] = StandardMapGenerator.createStarSystem(0, 0, this.tileWidth);
    this.tiles[0].name = StarSystem.NAMES[21];
    this.tiles[0].neighbors = new StarSystem[0];
    this.e150();
    var2.systems = this.tiles;
    var2.recalculateDistances();
    var2.recalculateMovementCosts();
    StandardMapGenerator.computePotentialWormholeConnections(var2.systems);
    this.calculateHexPoints();
    return var2;
  }

  public Map a572(final int var2, final boolean var3) throws MapGenerationFailure {
    final Map var4;
    if (var2 == 1) {
      if (var3) {
        this.tileWidth = 1;
        this.tiles = new StarSystem[1];
        this.systemCount = 1;
        this.tileHeight = 1;
      }

      var4 = new Map(this.tileWidth * 200 + 100, Map.drawingHeight(this.tileHeight), this.seed, this.systemCount);
      this.tiles[0] = StandardMapGenerator.createStarSystem(0, 0, this.tileWidth);
      this.tiles[0].name = StarSystem.NAMES[21];
    } else if (var2 == 2) {
      if (var3) {
        this.tiles = new StarSystem[2];
        this.tileHeight = 1;
        this.tileWidth = 2;
        this.systemCount = 2;
      }

      var4 = this.a572(1, false);
      this.tiles[1] = StandardMapGenerator.createStarSystem(1, 1, this.tileWidth);
      this.tiles[1].name = StarSystem.NAMES[247];
    } else if (var2 == 3) {
      if (var3) {
        this.tileWidth = 5;
        this.systemCount = 5;
        this.tileHeight = 2;
        this.tiles = new StarSystem[this.tileHeight * this.tileWidth];
      }

      var4 = this.a572(2, false);
      this.tiles[3] = StandardMapGenerator.createStarSystem(2, 3, this.tileWidth);
      this.tiles[3].name = StarSystem.NAMES[206];
      this.tiles[4] = StandardMapGenerator.createStarSystem(3, 4, this.tileWidth);
      this.tiles[4].name = StarSystem.NAMES[220];
      this.tiles[3 + this.tileWidth] = StandardMapGenerator.createStarSystem(4, this.tileWidth + 3, this.tileWidth);
      this.tiles[this.tileWidth + 3].name = StarSystem.NAMES[94];
    } else if (var2 == 4) {
      if (var3) {
        this.systemCount = 6;
        this.tileWidth = 5;
        this.tileHeight = 4;
        this.tiles = new StarSystem[this.tileWidth * this.tileHeight];
      }

      var4 = this.a572(3, false);
      this.tiles[this.tileWidth * 3 + 4] = StandardMapGenerator.createStarSystem(5, 3 * this.tileWidth + 4, this.tileWidth);
      this.tiles[this.tileWidth * 3 + 4].name = StarSystem.NAMES[186];
    } else if (var2 == 5) {
      if (var3) {
        this.systemCount = 8;
        this.tileHeight = 5;
        this.tileWidth = 5;
        this.tiles = new StarSystem[this.tileHeight * this.tileWidth];
      }

      var4 = this.a572(4, false);
      this.tiles[this.tileWidth * 3 + 3] = StandardMapGenerator.createStarSystem(6, 3 + this.tileWidth * 3, this.tileWidth);
      this.tiles[3 + this.tileWidth * 3].name = StarSystem.NAMES[231];
      this.tiles[this.tileWidth * 4 + 3] = StandardMapGenerator.createStarSystem(7, 4 * this.tileWidth + 3, this.tileWidth);
      this.tiles[3 + 4 * this.tileWidth].name = StarSystem.NAMES[86];
    } else if (var2 == 6) {
      if (var3) {
        this.tileWidth = 8;
        this.tileHeight = 8;
        this.systemCount = 19;
        this.tiles = new StarSystem[this.tileHeight * this.tileWidth];
      }

      var4 = this.a572(5, false);
      this.tiles[this.tileWidth * 6 + 2] = StandardMapGenerator.createStarSystem(8, this.tileWidth * 6 + 2, this.tileWidth);
      this.tiles[6 * this.tileWidth + 2].name = StarSystem.NAMES[149];
      this.tiles[3 + this.tileWidth * 6] = StandardMapGenerator.createStarSystem(9, 3 + 6 * this.tileWidth, this.tileWidth);
      this.tiles[3 + 6 * this.tileWidth].name = StarSystem.NAMES[244];
      this.tiles[2 + 7 * this.tileWidth] = StandardMapGenerator.createStarSystem(10, 7 * this.tileWidth + 2, this.tileWidth);
      this.tiles[2 + 7 * this.tileWidth].name = StarSystem.NAMES[82];
      this.tiles[3 + this.tileWidth * 7] = StandardMapGenerator.createStarSystem(11, 7 * this.tileWidth + 3, this.tileWidth);
      this.tiles[3 + this.tileWidth * 7].name = StarSystem.NAMES[245];
      this.tiles[4 + this.tileWidth * 7] = StandardMapGenerator.createStarSystem(12, this.tileWidth * 7 + 4, this.tileWidth);
      this.tiles[4 + this.tileWidth * 7].name = StarSystem.NAMES[242];
      this.tiles[5 + 6 * this.tileWidth] = StandardMapGenerator.createStarSystem(13, 5 + 6 * this.tileWidth, this.tileWidth);
      this.tiles[5 + this.tileWidth * 6].name = StarSystem.NAMES[204];
      this.tiles[7 * this.tileWidth + 5] = StandardMapGenerator.createStarSystem(14, 5 + 7 * this.tileWidth, this.tileWidth);
      this.tiles[this.tileWidth * 7 + 5].name = StarSystem.NAMES[226];
      this.tiles[6 * this.tileWidth + 6] = StandardMapGenerator.createStarSystem(15, 6 * this.tileWidth + 6, this.tileWidth);
      this.tiles[6 + this.tileWidth * 6].name = StarSystem.NAMES[165];
      this.tiles[6 + 5 * this.tileWidth] = StandardMapGenerator.createStarSystem(16, 6 + this.tileWidth * 5, this.tileWidth);
      this.tiles[6 + 5 * this.tileWidth].name = StarSystem.NAMES[216];
      this.tiles[6 * this.tileWidth + 7] = StandardMapGenerator.createStarSystem(17, 7 + 6 * this.tileWidth, this.tileWidth);
      this.tiles[this.tileWidth * 6 + 7].name = StarSystem.NAMES[109];
      this.tiles[this.tileWidth * 5 + 5] = StandardMapGenerator.createStarSystem(18, 5 * this.tileWidth + 5, this.tileWidth);
      this.tiles[this.tileWidth * 5 + 5].name = StarSystem.NAMES[255];
    } else {
      if (var2 != 7) {
        throw new MapGenerationFailure("No Tutorial Map setup for stage " + var2 + ".");
      }

      if (var3) {
        this.systemCount = 38;
        this.tileWidth = 14;
        this.tileHeight = 8;
        this.tiles = new StarSystem[this.tileHeight * this.tileWidth];
      }

      var4 = this.a572(6, false);
      this.tiles[7 + 3 * this.tileWidth] = StandardMapGenerator.createStarSystem(19, 3 * this.tileWidth + 7, this.tileWidth);
      this.tiles[3 * this.tileWidth + 7].name = StarSystem.NAMES[170];
      this.tiles[this.tileWidth * 4 + 8] = StandardMapGenerator.createStarSystem(20, 4 * this.tileWidth + 8, this.tileWidth);
      this.tiles[8 + this.tileWidth * 4].name = StarSystem.NAMES[195];
      this.tiles[8 + this.tileWidth * 2] = StandardMapGenerator.createStarSystem(21, this.tileWidth * 2 + 8, this.tileWidth);
      this.tiles[8 + 2 * this.tileWidth].name = StarSystem.NAMES[228];
      this.tiles[8 + this.tileWidth * 3] = StandardMapGenerator.createStarSystem(22, 8 + 3 * this.tileWidth, this.tileWidth);
      this.tiles[3 * this.tileWidth + 8].name = StarSystem.NAMES[148];
      this.tiles[9 + this.tileWidth * 4] = StandardMapGenerator.createStarSystem(23, 9 + 4 * this.tileWidth, this.tileWidth);
      this.tiles[9 + 4 * this.tileWidth].name = StarSystem.NAMES[181];
      this.tiles[9 + this.tileWidth * 3] = StandardMapGenerator.createStarSystem(24, this.tileWidth * 3 + 9, this.tileWidth);
      this.tiles[this.tileWidth * 3 + 9].name = StringConstants.DERELICT + " WHL-2";
      this.tiles[10 + 2 * this.tileWidth] = StandardMapGenerator.createStarSystem(25, 10 + this.tileWidth * 2, this.tileWidth);
      this.tiles[2 * this.tileWidth + 10].name = StarSystem.NAMES[214];
      this.tiles[3 * this.tileWidth + 10] = StandardMapGenerator.createStarSystem(26, 10 + this.tileWidth * 3, this.tileWidth);
      this.tiles[3 * this.tileWidth + 10].name = StarSystem.NAMES[241];
      this.tiles[4 * this.tileWidth + 10] = StandardMapGenerator.createStarSystem(27, 4 * this.tileWidth + 10, this.tileWidth);
      this.tiles[this.tileWidth * 4 + 10].name = StarSystem.NAMES[250];
      this.tiles[4 * this.tileWidth + 11] = StandardMapGenerator.createStarSystem(28, this.tileWidth * 4 + 11, this.tileWidth);
      this.tiles[this.tileWidth * 4 + 11].name = StarSystem.NAMES[169];
      this.tiles[10 + 5 * this.tileWidth] = StandardMapGenerator.createStarSystem(29, this.tileWidth * 5 + 10, this.tileWidth);
      this.tiles[5 * this.tileWidth + 10].name = StarSystem.NAMES[28];
      this.tiles[5 * this.tileWidth + 11] = StandardMapGenerator.createStarSystem(30, this.tileWidth * 5 + 11, this.tileWidth);
      this.tiles[11 + 5 * this.tileWidth].name = StarSystem.NAMES[212];
      this.tiles[this.tileWidth * 4 + 12] = StandardMapGenerator.createStarSystem(31, 4 * this.tileWidth + 12, this.tileWidth);
      this.tiles[12 + 4 * this.tileWidth].name = StarSystem.NAMES[190];
      this.tiles[12 + 3 * this.tileWidth] = StandardMapGenerator.createStarSystem(32, 12 + this.tileWidth * 3, this.tileWidth);
      this.tiles[3 * this.tileWidth + 12].name = StarSystem.NAMES[122];
      this.tiles[4 * this.tileWidth + 13] = StandardMapGenerator.createStarSystem(33, this.tileWidth * 4 + 13, this.tileWidth);
      this.tiles[13 + 4 * this.tileWidth].name = StarSystem.NAMES[125];
      this.tiles[5 * this.tileWidth + 12] = StandardMapGenerator.createStarSystem(34, 12 + this.tileWidth * 5, this.tileWidth);
      this.tiles[12 + 5 * this.tileWidth].name = StarSystem.NAMES[246];
      this.tiles[11 + this.tileWidth * 6] = StandardMapGenerator.createStarSystem(35, 6 * this.tileWidth + 11, this.tileWidth);
      this.tiles[this.tileWidth * 6 + 11].name = StarSystem.NAMES[253];
      this.tiles[this.tileWidth * 6 + 12] = StandardMapGenerator.createStarSystem(36, 6 * this.tileWidth + 12, this.tileWidth);
      this.tiles[6 * this.tileWidth + 12].name = StarSystem.NAMES[91];
      this.tiles[13 + 6 * this.tileWidth] = StandardMapGenerator.createStarSystem(37, 13 + 6 * this.tileWidth, this.tileWidth);
      this.tiles[this.tileWidth * 6 + 13].name = StarSystem.NAMES[152];
    }

    if (var3) {
      this.e150();
      final StarSystem[] var5 = new StarSystem[this.systemCount];

      for (final StarSystem ln_ : this.tiles) {
        if (ln_ != null) {
          var5[ln_.index] = ln_;
        }
      }

      var4.systems = var5;
      if (var5.length > 1) {
        this.a093(var2);
      }

      var4.recalculateDistances();
      var4.recalculateMovementCosts();
      StandardMapGenerator.computePotentialWormholeConnections(var4.systems);
      this.calculateHexPoints();
    }

    return var4;
  }
}
