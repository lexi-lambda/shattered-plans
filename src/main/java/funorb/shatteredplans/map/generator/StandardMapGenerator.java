package funorb.shatteredplans.map.generator;

import funorb.graphics.Point;
import funorb.shatteredplans.client.ShatteredPlansClient;
import funorb.shatteredplans.game.GameState;
import funorb.shatteredplans.map.Map;
import funorb.shatteredplans.map.StarSystem;
import funorb.util.MathUtil;
import org.intellij.lang.annotations.MagicConstant;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Queue;
import java.util.Random;
import java.util.stream.Collectors;

public class StandardMapGenerator implements MapGenerator {
  protected final int seed;
  protected final int playerCount;
  private final int tilesPerSystemPercentage;
  protected int neutralHomeworldCount;
  protected int systemCount;
  protected StarSystem[] tiles;
  protected int tileWidth;
  protected int tileHeight;

  public StandardMapGenerator(final int tileWidth, final int tileHeight, final int seed, final int systemCount, final int playerCount) {
    this.tileWidth = tileWidth;
    this.tileHeight = tileHeight;
    this.seed = seed;
    this.systemCount = systemCount;
    this.playerCount = playerCount;
    this.neutralHomeworldCount = this.playerCount - 1;
    if (this.tileWidth * this.tileHeight < this.systemCount) {
      throw new RuntimeException("Requested starcount (" + this.systemCount + ") is greater than map size (" + this.tileWidth * this.tileHeight + " hexes).");
    }
    this.tilesPerSystemPercentage = this.tileHeight * this.tileWidth * 100 / this.systemCount;
  }

  protected static StarSystem createStarSystem(final int index, final int mapIndex, final int mapWidth) {
    final int row = mapIndex / mapWidth;
    final int column = mapIndex % mapWidth;
    final boolean isEvenRow = row % 2 == 0;
    final int posnX = (Map.TILE_SIZE * column) + (isEvenRow ? Map.TILE_SIZE / 2 : Map.TILE_SIZE);
    final int posnY = (int) (0.5D * Map.TILE_SIZE * Map.COS_THIRTY_DEGREES_RECIP)
        + (int) ((double) row * 0.75D * Map.COS_THIRTY_DEGREES_RECIP * Map.TILE_SIZE);
    return new StarSystem(index, posnX, posnY, 0);
  }

  private static void linkConnectedComponents(final Random random, final StarSystem[] tiles, final int tileWidth, final StarSystem[] systems, final int connectedComponentCount) {
    final int systemCount = systems.length;
    for (int ci = 0; ci < connectedComponentCount; ++ci) {
      componentLoop:
      for (int cj = ci + 1; cj < connectedComponentCount; ++cj) {
        final int roll = ShatteredPlansClient.randomIntBounded(random, systemCount);

        for (int si = 0; si < systemCount; ++si) {
          final StarSystem system1 = systems[(si + roll) % systemCount];
          if (system1.garrison == ci) {
            connectionLoop:
            for (final StarSystem system2 : system1.potentialWormholeConnections) {
              if (system2.garrison == cj) {
                if (system1.neighbors != null) {
                  for (final StarSystem neighbor : system1.neighbors) {
                    if (neighbor == system2) {
                      continue connectionLoop;
                    }
                  }
                }

                int tile1 = 0;
                int tile2 = 0;
                for (int i = 0; i < tiles.length; ++i) {
                  if (tiles[i] != null) {
                    if (system1.index == tiles[i].index) {
                      tile1 = i;
                    }
                    if (system2.index == tiles[i].index) {
                      tile2 = i;
                    }
                  }
                }

                if (tile1 > tile2) {
                  final int tmp = tile1;
                  tile1 = tile2;
                  tile2 = tmp;
                }

                final int dy = (tile2 / tileWidth) - (tile1 / tileWidth);
                if (dy != 0 && tile2 < (dy * tileWidth) + tile1) {
                  final int tmp = tile2;
                  tile2 = tile1 + dy * tileWidth;
                  tile1 = tmp - (tileWidth * dy);
                }

                for (int i = tile1 + 1; i < tile2; ++i) {
                  if (tiles[i] != null
                      && tiles[i].index != system1.index
                      && tiles[i].index != system2.index
                      && !connectionWouldNotIntersect(system1, system2, tiles[i])) {
                    continue connectionLoop;
                  }
                }

                StarSystem.linkNeighbors(system1, system2);
                continue componentLoop;
              }
            }
          }
        }
      }
    }
  }

  private static boolean connectionWouldNotIntersect(final StarSystem system1, final StarSystem system2, final StarSystem system) {
    final int var2 = (int) (Map.COS_THIRTY_DEGREES_RECIP * 200.0D / 2.0D);
    if ((system.posnX - var2 > system1.posnX && system2.posnX < system.posnX - var2)
        || (system1.posnX > var2 + system.posnX && system.posnX + var2 < system2.posnX)) {
      return true;
    }
    final int var4 = system2.posnX - system1.posnX;
    final int var5 = system2.posnY - system1.posnY;
    if (var4 == 0) {
      return system.posnX - system1.posnX > var2;
    } else if (var5 == 0) {
      return var2 < system.posnY - system1.posnY;
    } else {
      final double var6 = Math.sqrt(MathUtil.euclideanDistanceSquared(var5, var4));
      final double var8 = (double) (-((system.posnY - system1.posnY) * var4) + (system.posnX - system1.posnX) * var5) / var6;
      return var2 < (int) var8;
    }
  }

  protected static void linkAdjacentNeighbors(final StarSystem[] tiles, final int tileWidth, final int tileHeight) {
    for (int i = 0; i < tiles.length; ++i) {
      if (tiles[i] == null) continue;

      if ((i + 1) % tileWidth != 0
          && tiles[i + 1] != null
          && tiles[i].index < tiles[i + 1].index) {
        StarSystem.linkNeighbors(tiles[i], tiles[i + 1]);
      }
      if (i % tileWidth != 0
          && tiles[i - 1] != null
          && tiles[i].index < tiles[i - 1].index) {
        StarSystem.linkNeighbors(tiles[i], tiles[i - 1]);
      }

      final int row = i / tileWidth;
      final boolean isEvenRow = row % 2 == 0;
      if (row > 0) {
        if (!isEvenRow || i % tileWidth != 0) {
          final int j = isEvenRow ? i - tileWidth - 1 : i - tileWidth;
          if (tiles[j] != null && tiles[j].index > tiles[i].index) {
            StarSystem.linkNeighbors(tiles[i], tiles[j]);
          }
        }
        if (isEvenRow || (i + 1) % tileWidth != 0) {
          final int j = isEvenRow ? i - tileWidth : i - tileWidth + 1;
          if (tiles[j] != null && tiles[j].index > tiles[i].index) {
            StarSystem.linkNeighbors(tiles[i], tiles[j]);
          }
        }
      }

      if (row < tileHeight - 1) {
        if (!isEvenRow || i % tileWidth != 0) {
          final int j = isEvenRow ? i + tileWidth - 1 : i + tileWidth;
          if (tiles[j] != null && tiles[j].index > tiles[i].index) {
            StarSystem.linkNeighbors(tiles[i], tiles[j]);
          }
        }

        if (isEvenRow || (i + 1) % tileWidth != 0) {
          final int j = isEvenRow ? tileWidth + i : tileWidth + i + 1;
          if (tiles[j] != null && tiles[i].index < tiles[j].index) {
            StarSystem.linkNeighbors(tiles[i], tiles[j]);
          }
        }
      }
    }
  }

  private static int markConnectedComponents(final StarSystem[] systems) {
    for (final StarSystem system : systems) {
      system.garrison = -1;
    }

    final Queue<StarSystem> pending = new ArrayDeque<>();
    int nextComponentIndex = 0;

    for (StarSystem system : systems) {
      if (system.garrison != -1) continue;

      pending.add(system);
      while ((system = pending.poll()) != null) {
        system.garrison = nextComponentIndex;

        if (system.neighbors != null) {
          for (final StarSystem neighbor : system.neighbors) {
            if (neighbor.garrison == -1) {
              pending.add(neighbor);
            }
          }
        }
      }

      ++nextComponentIndex;
    }

    return nextComponentIndex;
  }

  protected static void computePotentialWormholeConnections(final StarSystem[] systems) {
    for (final StarSystem system1 : systems) {
      final ArrayList<WormholeConnection> ncs = Arrays.stream(systems)
          .filter(system2 -> system2 != system1)
          .map(system2 -> new WormholeConnection(system1, system2))
          .collect(Collectors.toCollection(ArrayList::new));

      for (int i = 0; i < ncs.size(); ++i) {
        for (int j = i + 1; j < ncs.size(); ++j) {
          WormholeConnection.a680eo(ncs.get(i), ncs.get(j));
        }
      }
      WormholeConnection.a379ad(ncs);

      system1.potentialWormholeConnections = WormholeConnection.sortedSystems(ncs);
    }
  }

  private void linkNeighbors(final Map map) {
    linkAdjacentNeighbors(this.tiles, this.tileWidth, this.tileHeight);
    final int connectedComponentCount = markConnectedComponents(map.systems);
    linkConnectedComponents(map.random, this.tiles, this.tileWidth, map.systems, connectedComponentCount);

    Arrays.stream(map.systems).forEach(StarSystem::sortNeighbors);
    for (final StarSystem system : map.systems) {
      if (system.neighbors == null) {
        system.neighbors = new StarSystem[0];
      }
    }

    map.recalculateDistances();
  }

  private void assignSystemTypes(final Map map) {
    for (final StarSystem system : map.systems) {
      system.assignType(map.random);
    }
  }

  protected final void calculateHexPoints() {
    for (final StarSystem system : this.tiles) {
      if (system != null) {
        system.hexPoints = new Point[6];
        system.hexPoints[0] = new Point(system.posnX, (int) (-(0.5D * 200.0D * Map.COS_THIRTY_DEGREES_RECIP) + (double) system.posnY));
        system.hexPoints[1] = new Point(100 + system.posnX, (int) (-(0.25D * Map.COS_THIRTY_DEGREES_RECIP * 200.0D) + (double) system.posnY));
        system.hexPoints[2] = new Point(100 + system.posnX, (int) (0.25D * Map.COS_THIRTY_DEGREES_RECIP * 200.0D + (double) system.posnY));
        system.hexPoints[3] = new Point(system.posnX, (int) (Map.COS_THIRTY_DEGREES_RECIP * 200.0D * 0.5D + (double) system.posnY));
        system.hexPoints[4] = new Point(system.posnX - 100, (int) ((double) system.posnY + 0.25D * Map.COS_THIRTY_DEGREES_RECIP * 200.0D));
        system.hexPoints[5] = new Point(system.posnX - 100, (int) ((double) system.posnY - 200.0D * Map.COS_THIRTY_DEGREES_RECIP * 0.25D));
      }
    }
  }

  @Override
  public Map generate() throws MapGenerationFailure {
    final Map map = new Map(this.tileWidth * 200 + 100, Map.drawingHeight(this.tileHeight), this.seed, this.systemCount);
    this.placeSystems(map);
    this.linkNeighbors(map);

    final int[] playerHomeworldIndexes = this.selectHomeworlds(map.random, map.systems, map.distances, this.playerCount, 4, 4, 4, null);
    final int[] allHomeworlds = this.selectHomeworlds(map.random, map.systems, map.distances, this.neutralHomeworldCount + this.playerCount, 2, 16, 2, playerHomeworldIndexes);
    final int[] neutralHomeworldIndexes = new int[this.neutralHomeworldCount];
    System.arraycopy(allHomeworlds, this.playerCount, neutralHomeworldIndexes, 0, this.neutralHomeworldCount);

    this.setScores(map, playerHomeworldIndexes, StarSystem.Score.PLAYER_HOMEWORLD);
    this.setScores(map, neutralHomeworldIndexes, StarSystem.Score.NEUTRAL_HOMEWORLD);
    this.assignResources(map, playerHomeworldIndexes, neutralHomeworldIndexes);

    this.assignGarrisons(map);
    this.assignSystemTypes(map);
    return map;
  }

  private void assignResources(final Map map, final int[] playerHomeworldIndexes, final int[] neutralHomeworldIndexes) throws MapGenerationFailure {
    this.assignPlayerHomeworldResources(map, playerHomeworldIndexes);
    this.assignNeutralHomeworldResources(map, neutralHomeworldIndexes);
    this.b501(map);
  }

  private void setScores(final Map map, final int[] indexes, @MagicConstant(valuesFromClass = StarSystem.Score.class) final int score) {
    for (final int i : indexes) {
      map.systems[i].score = score;
    }
  }

  public void placeSystems(final Map map) throws MapGenerationFailure {
    int[] namePermutation = null;
    if (StarSystem.NAMES != null) {
      final int nameCount = StarSystem.NAMES.length;
      namePermutation = new int[nameCount];

      for (int i = 0; i < nameCount; i++) {
        namePermutation[i] = i;
      }

      for (int i = 0; i < nameCount - 1; ++i) {
        final int j = i + 1 + ShatteredPlansClient.randomIntBounded(map.random, nameCount - i - 1);
        final int tmp = namePermutation[j];
        namePermutation[j] = namePermutation[i];
        namePermutation[i] = tmp;
      }
    }

    int emptyTiles = this.tileHeight * this.tileWidth;
    if (this.tiles == null) {
      this.tiles = new StarSystem[this.tileHeight * this.tileWidth];
    } else {
      for (final StarSystem system : this.tiles) {
        if (system != null) {
          if (system == MapGenerator.DUMMY_SYSTEM) {
            --emptyTiles;
          }
        }
      }
    }

    map.systems = new StarSystem[this.systemCount];
    assert this.tilesPerSystemPercentage >= 50;
    while (emptyTiles > this.systemCount) {
      final int i = ShatteredPlansClient.randomIntBounded(map.random, this.tiles.length);
      if (this.tiles[i] == null) {
        --emptyTiles;
        this.tiles[i] = MapGenerator.DUMMY_SYSTEM;
      }
    }

    int nextSystemIndex = 0;
    for (int i = 0; i < this.tiles.length; ++i) {
      if (this.tiles[i] == null) {
        this.tiles[i] = createStarSystem(nextSystemIndex, i, this.tileWidth);
        ++nextSystemIndex;
      }
    }

    final StarSystem[] systems = new StarSystem[this.systemCount];
    for (int i = 0; i < this.tiles.length; ++i) {
      if (this.tiles[i] != null) {
        if (this.tiles[i] == MapGenerator.DUMMY_SYSTEM) {
          this.tiles[i] = null;
        } else {
          assert StarSystem.NAMES != null;
          assert namePermutation != null;
          this.tiles[i].name = StarSystem.NAMES[namePermutation[this.tiles[i].index]];
          systems[this.tiles[i].index] = this.tiles[i];
        }
      }
    }

    map.systems = systems;
    computePotentialWormholeConnections(map.systems);
    this.calculateHexPoints();
  }

  private void assignNeutralHomeworldResources(final Map map, final int[] indexes) {
    for (final int i : indexes) {
      Arrays.fill(map.systems[i].resources, 1);
    }
  }

  private void assignPlayerHomeworldResources(final Map map, final int[] indexes) throws MapGenerationFailure {
    for (final int i : indexes) {
      Arrays.fill(map.systems[i].resources, 2);
      for (int j = 0; j < 2; ++j) {
        map.systems[i].resources[ShatteredPlansClient.randomIntBounded(map.random, GameState.NUM_RESOURCES)]++;
      }
    }
    this.assignPlayerHomeworldSurroundingResources(map, indexes, 0, 4, 1, 2);
    this.assignPlayerHomeworldSurroundingResources(map, indexes, 4, 12, 4, 4);
  }

  private void assignPlayerHomeworldSurroundingResources(final Map map, final int[] indexes, final int close, final int far, final int minimum, final int extra) throws MapGenerationFailure {
    for (final int i : indexes) {
      final int[] resources = new int[GameState.NUM_RESOURCES];

      Arrays.fill(resources, minimum);
      for (int j = 0; j < extra; ++j) {
        resources[ShatteredPlansClient.randomIntBounded(map.random, resources.length)]++;
      }
      int totalResources = Arrays.stream(resources).sum();

      final int[] distanceRanks = GameState.calculateRanksAscending(map.distances[i]);
      for (int j = 1; j < far; ++j) {
        final StarSystem nearbySystem = map.systems[distanceRanks[j]];
        for (int k = 0; k < GameState.NUM_RESOURCES; ++k) {
          int nearbyResource = nearbySystem.resources[k];
          if (resources[k] < nearbyResource) {
            nearbyResource = resources[k];
          }
          resources[k] -= nearbyResource;
          totalResources -= nearbyResource;
        }
      }

      label104:
      for (int j = close; j < far; ++j) {
        for (int k = 0; k < GameState.NUM_RESOURCES; ++k) {
          if (map.systems[distanceRanks[j]].resources[k] > 0) {
            continue label104;
          }
        }

        if (totalResources == 0) {
          throw new MapGenerationFailure("Insufficient resources to give production to each star. Close: " + close + ", Far: " + far + ", Minimum: " + minimum + ", Extra: " + extra + ".");
        }

        int k;
        do {
          k = ShatteredPlansClient.randomIntBounded(map.random, GameState.NUM_RESOURCES);
        } while (resources[k] == 0);

        map.systems[distanceRanks[j]].resources[k]++;
        resources[k]--;
        totalResources--;
      }

      final int var12 = close == 0 ? 1 : close;
      for (; totalResources > 0; totalResources--) {
        final int var17 = distanceRanks[var12 + ShatteredPlansClient.randomIntBounded(map.random, 1 - var12 + far - 1)];
        final StarSystem system = map.systems[var17];
        int var15 = ShatteredPlansClient.randomIntBounded(map.random, GameState.NUM_RESOURCES);
        if (ShatteredPlansClient.randomIntBounded(map.random, 2) != 0) {
          while (system.resources[var15] == 0) {
            var15 = ShatteredPlansClient.randomIntBounded(map.random, GameState.NUM_RESOURCES);
          }
        }

        while (resources[var15] == 0) {
          var15 = ShatteredPlansClient.randomIntBounded(map.random, GameState.NUM_RESOURCES);
        }

        system.resources[var15]++;
        resources[var15]--;
      }
    }
  }

  private void b501(final Map map) {
    for (final StarSystem system : map.systems) {
      final int var6 = Arrays.stream(system.resources).sum();

      if (var6 == 0) {
        final int var7 = ShatteredPlansClient.randomIntBounded(map.random, 100);
        final int var8 = ShatteredPlansClient.randomIntBounded(map.random, 4);
        int var9 = ShatteredPlansClient.randomIntBounded(map.random, 3);
        if (var9 == var8) {
          var9 = 3;
        }

        int var10 = ShatteredPlansClient.randomIntBounded(map.random, 2);
        if (var10 == var8 || var10 == var9) {
          var10 = var8 != 2 && var9 != 2 ? 2 : 3;
        }

        if (var7 < 5) {
          if (var7 >= 2) {
            if (var7 < 3) {
              system.resources[var8] = 4;
              system.resources[var9] = 1;
            } else {
              system.resources[var8] = 5;
            }
          } else {
            system.resources[var8] = 3;
            system.resources[var9] = 1;
            system.resources[var10] = 1;
          }
        } else if (var7 >= 15) {
          if (var7 < 30) {
            if (var7 < 25) {
              system.resources[var8] = 2;
              system.resources[var9] = 1;
            } else {
              system.resources[var8] = 3;
            }
          } else if (var7 < 50) {
            if (var7 < 45) {
              system.resources[var8] = 1;
              system.resources[var9] = 1;
            } else {
              system.resources[var8] = 2;
            }
          } else {
            system.resources[var8] = 1;
          }
        } else if (var7 < 9) {
          system.resources[var8] = 2;
          system.resources[var9] = 1;
          system.resources[var10] = 1;
        } else if (var7 < 12) {
          system.resources[var8] = 3;
          system.resources[var9] = 1;
        } else {
          system.resources[var8] = 4;
        }
      }
    }

  }

  private void assignGarrisons(final Map map) {
    for (final StarSystem system : map.systems) {
      if (system.score == StarSystem.Score.PLAYER_HOMEWORLD) {
        system.garrison = 20;
      } else {
        final int var6 = Arrays.stream(system.resources).sum();
        final int var7 = var6 - 1;
        final int var8 = 2 * var6 - 1;
        system.garrison = var7 + ShatteredPlansClient.randomIntBounded(map.random, 1 - var7 + var8);
      }
    }
  }

  protected int[] selectHomeworlds(final Random random, final StarSystem[] systems, final int[][] distances, final int count, final int minNeighbors, final int maxNeighbors, final int minDistance, final int[] alreadySelected) throws MapGenerationFailure {
    final int[] candidateIndexes = Arrays.stream(systems)
        .filter(system -> system.neighbors.length >= minNeighbors && system.neighbors.length <= maxNeighbors)
        .mapToInt(system -> system.index).toArray();
    if (candidateIndexes.length < count) {
      throw new MapGenerationFailure("Not enough highly-connected systems!");
    }

    final int[] selections = new int[count];
    if (alreadySelected != null) {
      System.arraycopy(alreadySelected, 0, selections, 0, alreadySelected.length);
    }
    final int alreadySelectedCount = alreadySelected == null ? 0 : alreadySelected.length;

    for (int i = alreadySelectedCount; i < count; ++i) {
      int selection = -1;

      while (selection == -1) {
        selection = candidateIndexes[ShatteredPlansClient.randomIntBounded(random, candidateIndexes.length)];
        for (int j = 0; j < i; ++j) {
          if (selection == selections[j]) {
            selection = -1;
            break;
          }
        }
      }
      selections[i] = selection;
    }

    for (int i = 0; i < systems.length; ++i) {
      for (int j = alreadySelectedCount; j < count; ++j) {
        int var17 = 0;
        int var18 = -1;

        for (final int index : candidateIndexes) {
          final StarSystem candidate = systems[index];
          int var19 = Integer.MAX_VALUE;

          for (int k = 0; k < count; ++k) {
            final int distance = distances[candidate.index][selections[k]];
            if (k != j && distance != -1 && distance < var19) {
              var19 = distance;
            }
          }

          if (var19 > var17) {
            var17 = var19;
            var18 = candidate.index;
          }
        }

        selections[j] = var18;
      }
    }

    int smallestDistance = distances[selections[0]][selections[1]];
    for (int i = 0; i < count; ++i) {
      for (int j = i + 1; j < count; ++j) {
        if (smallestDistance > distances[selections[i]][selections[j]]) {
          smallestDistance = distances[selections[i]][selections[j]];
        }
      }
    }

    if (smallestDistance < minDistance) {
      throw new MapGenerationFailure("Start locations cannot be placed far enough apart!");
    }
    return selections;
  }
}
