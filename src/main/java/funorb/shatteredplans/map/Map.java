package funorb.shatteredplans.map;

import funorb.io.Buffer;
import funorb.io.WritableBuffer;
import funorb.shatteredplans.client.game.GameView;
import funorb.shatteredplans.game.CombinedForce;
import funorb.shatteredplans.game.ContiguousForce;
import funorb.shatteredplans.game.GameOptions;
import funorb.shatteredplans.game.GameState;
import funorb.shatteredplans.game.Player;
import funorb.shatteredplans.map.generator.CaptureAndHoldMapReader;
import funorb.shatteredplans.map.generator.DerelictsMapReader;
import funorb.shatteredplans.map.generator.MapGenerationFailure;
import funorb.shatteredplans.map.generator.MapGenerator;
import funorb.shatteredplans.map.generator.StandardMapReader;
import funorb.util.IsaacRandom;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Random;

public final class Map {
  public static final int TILE_SIZE = 200; // in drawing units
  public static final double COS_THIRTY_DEGREES_RECIP = 1.0D / GameView.COS_THIRTY_DEGREES; // 2/sqrt(3)

  public final int drawingWidth;
  public final int drawingHeight;
  private final int seed;
  public final Random random;
  public StarSystem[] systems;
  public int[][] movementCosts;
  public int[][] distances;

  public Map(final int drawingWidth, final int drawingHeight, final int seed, final int systemCount) {
    this.drawingWidth = drawingWidth;
    this.drawingHeight = drawingHeight;
    this.seed = seed;
    this.random = new IsaacRandom(seed);
    this.systems = new StarSystem[systemCount];
  }

  public static Map read(@SuppressWarnings("SameParameterValue") final Buffer buffer,
                         final @NotNull GameState.GameType gameType,
                         final GameState gameState,
                         final GameOptions options,
                         final Player[] players,
                         final Player localPlayer,
                         final List<TannhauserLink> tannhauserLinks) throws MapGenerationFailure, TannhauserUnconnectedException {
    final int seed = buffer.readInt();
    final int mapWidth = (buffer.readUShort() - 100) / 200;
    final int mapHeight = a080sa(buffer.readUShort());
    final int systemCount = buffer.readUShort();

    final MapGenerator generator = switch (gameType) {
      case CONQUEST, POINTS -> new StandardMapReader(buffer, seed, mapWidth, mapHeight, systemCount);
      case CAPTURE_AND_HOLD -> new CaptureAndHoldMapReader(buffer, seed);
      case DERELICTS -> new DerelictsMapReader(buffer, seed, mapWidth, mapHeight, systemCount, players.length);
      default -> throw new RuntimeException("No Received MapGenerator for gametype " + gameType);
    };
    final Map map = generator.generate();

    for (final StarSystem system : map.systems) {
      system.lastOwner = Player.read(buffer, players);
    }

    final ContiguousForce[] contiguousForces = new ContiguousForce[buffer.readUByte()];
    for (int i = 0; i < contiguousForces.length; ++i) {
      final Player player = Objects.requireNonNull(Player.read(buffer, players));
      final StarSystem capital = Objects.requireNonNull(StarSystem.read(buffer, map.systems));
      contiguousForces[i] = new ContiguousForce(player, capital);
      contiguousForces[i].add(capital);
      player.contiguousForces.add(contiguousForces[i]);
      capital.contiguousForce = contiguousForces[i];
      capital.owner = player;

      final int forceSystemCount = buffer.readUByte() - 1;
      for (int j = 0; j < forceSystemCount; ++j) {
        final StarSystem system = map.systems[buffer.readUByte()];
        contiguousForces[i].add(system);
        system.contiguousForce = contiguousForces[i];
        system.owner = player;
      }
    }

    final CombinedForce[] combinedForces = new CombinedForce[buffer.readUByte()];
    for (int i = 0; i < combinedForces.length; ++i) {
      final Player player = players[buffer.readUByte()];
      final StarSystem capital = map.systems[buffer.readUByte()];
      combinedForces[i] = new CombinedForce(player, capital);
      combinedForces[i].add(capital);
      player.combinedForce = combinedForces[i];

      final int forceSystemCount = buffer.readUByte() - 1;
      for (int j = 0; j < forceSystemCount; ++j) {
        final StarSystem system = map.systems[buffer.readUByte()];
        combinedForces[i].add(system);
      }
    }

    map.recalculateMovementCosts();

    for (final Player player : players) {
      if (options.unifiedTerritories) {
        if (player.combinedForce != null) {
          GameState.recalculateFleetsRemaining(player.combinedForce);
          GameState.recalculateFleetProduction(player.combinedForce);

          for (final StarSystem system : player.combinedForce) {
            GameState.recalculateMinimumGarrison(options, system, null);
          }
        }
      } else {
        for (final ContiguousForce force : player.contiguousForces) {
          GameState.recalculateFleetsRemaining(force);
          GameState.recalculateFleetProduction(force);

          for (final StarSystem var34 : force) {
            GameState.recalculateMinimumGarrison(options, var34, null);
          }
        }
      }
    }

    for (final Player player : players) {
      for (int i = 0; i < players.length; ++i) {
        player.allies[i] = false;
      }
    }
    for (int i = 0; i < players.length; ++i) {
      for (int j = 1 + i; j < players.length; ++j) {
        final byte pactTurns = buffer.readByte();
        if (pactTurns != 127) {
          players[i].allies[j] = true;
          players[j].allies[i] = true;
          players[i].pactTurnsRemaining[j] = pactTurns;
          players[j].pactTurnsRemaining[i] = pactTurns;
        }
      }
    }

    if (localPlayer != null) {
      localPlayer.outgoingPactOffersBitmap = buffer.readUByte();
    }

    for (final Player player : players) {
      for (int i = 0; i < GameState.NUM_RESOURCES; ++i) {
        player.researchPoints[i] = buffer.readUByte();
      }
    }

    tannhauserLinks.clear();
    final int tannhauserCount = buffer.readUByte();
    for (int i = 0; i < tannhauserCount; ++i) {
      final StarSystem system1 = StarSystem.read(buffer, map.systems);
      final StarSystem system2 = StarSystem.read(buffer, map.systems);
      if (system1 == null || system2 == null) {
        throw new TannhauserUnconnectedException();
      }
      final TannhauserLink link = new TannhauserLink(system1, system2, buffer.readUByte());
      tannhauserLinks.add(link);
    }

    gameState.victoryChecker.initializeFromServer(buffer);
    final int index2 = buffer.readUByte();
    final int index1 = buffer.readUByte();
    gameState.setTurnNameIndexes(index1, index2);

    for (final Player player : players) {
      if (player.combinedForce == null) {
        player.combinedForce = new CombinedForce(player, null);
      }
    }

    return map;
  }

  public static int drawingHeight(final int tileHeight) {
    return (int) (
        (tileHeight % 2 == 0 ? 0.25D * COS_THIRTY_DEGREES_RECIP * 200.0D : 0.75D * 200.0D * COS_THIRTY_DEGREES_RECIP)
            + (double) (tileHeight / 2) * 1.5D * COS_THIRTY_DEGREES_RECIP * 200.0D);
  }

  private static void a316b(final Buffer buffer, final String var1) {
    final int len = Math.min(var1.length(), 255);
    buffer.writeByte(len);
    for (int i = 0; i < len; ++i) {
      buffer.writeByte(var1.charAt(i));
    }
  }

  public void write(final WritableBuffer buffer,
                    final Player[] players,
                    final @Nullable Player localPlayer,
                    final List<TannhauserLink> tannhauserLinks) {
    buffer.writeInt(this.seed);
    buffer.writeShort(this.drawingWidth);
    buffer.writeShort(this.drawingHeight);
    buffer.writeShort(this.systems.length);

    for (final StarSystem system : this.systems) {
      // only include the neighbor for one side of the relationship, since the
      //  client adds the neighbor to both systems when it receives it
      final List<StarSystem> filteredNeighbors = Arrays.stream(system.neighbors)
          .filter(neighbor -> system.index < neighbor.index).toList();
      buffer.writeByte(filteredNeighbors.size());
      filteredNeighbors.forEach(neighbor -> neighbor.write(buffer));

      for (int i = 0; i < GameState.NUM_RESOURCES; i += 2) {
        buffer.writeByte(system.resources[i] | system.resources[i + 1] << 4);
      }
      buffer.writeShort(system.garrison | (system.hasDefensiveNet ? 0x8000 : 0));
      buffer.writeByte(system.type | (system.score << 6));
    }

    for (final StarSystem system : this.systems) {
      Player.write(buffer, system.lastOwner);
    }

    final List<ContiguousForce> allContiguousForces = Arrays.stream(players)
        .flatMap(player -> player.contiguousForces.stream()).toList();
    buffer.writeByte(allContiguousForces.size());
    allContiguousForces.forEach(force -> force.write(buffer));

    buffer.writeByte(players.length);
    Arrays.stream(players).forEach(player -> player.combinedForce.write(buffer));

    for (int i = 0; i < players.length; i++) {
      for (int j = i + 1; j < players.length; j++) {
        if (players[i].allies[j]) {
          buffer.writeByte(players[i].pactTurnsRemaining[j]);
        } else {
          buffer.writeByte(127);
        }
      }
    }

    if (localPlayer != null) {
      buffer.writeByte(localPlayer.outgoingPactOffersBitmap);
    }

    for (final Player player : players) {
      for (int i = 0; i < GameState.NUM_RESOURCES; ++i) {
        buffer.writeByte(player.researchPoints[i]);
      }
    }

    buffer.writeByte(tannhauserLinks.size());
    tannhauserLinks.forEach(link -> link.write(buffer));
  }

  private static int a080sa(final int var0) {
    return 1 + (int) (((double) var0 - 50.0D * COS_THIRTY_DEGREES_RECIP) / (COS_THIRTY_DEGREES_RECIP * 150.0D));
  }

  public void recalculateMovementCosts() {
    final int systemCount = this.systems.length;
    final int[][] movementCosts = new int[systemCount][systemCount];

    for (int i = 0; i < systemCount; ++i) {
      for (int j = 0; j < systemCount; ++j) {
        movementCosts[i][j] = -1;
      }

      movementCosts[i][i] = 0;
      final StarSystem system = this.systems[i];
      final StarSystem[] neighbors = system.neighbors;

      for (final StarSystem neighbor : neighbors) {
        if (neighbor.contiguousForce == system.contiguousForce) {
          movementCosts[i][neighbor.index] = 1;
        }
      }
    }

    for (int i = 0; i < systemCount; ++i) {
      for (int j = 0; j < systemCount; ++j) {
        for (int k = 0; k < systemCount; ++k) {
          if (movementCosts[i][k] != -1 && movementCosts[j][i] != -1) {
            final int transitiveCost = movementCosts[i][k] + movementCosts[j][i];
            if (movementCosts[j][k] == -1 || transitiveCost < movementCosts[j][k]) {
              movementCosts[j][k] = transitiveCost;
            }
          }
        }
      }
    }

    this.movementCosts = movementCosts;
  }

  public void recalculateDistances() {
    final int systemCount = this.systems.length;
    final int[][] distances = new int[systemCount][systemCount];

    for (int i = 0; i < systemCount; ++i) {
      for (int j = 0; j < systemCount; ++j) {
        distances[i][j] = -1;
      }

      distances[i][i] = 0;
      for (final StarSystem system : this.systems[i].neighbors) {
        distances[i][system.index] = 1;
      }
    }

    for (int i = 0; i < systemCount; ++i) {
      for (int j = 0; j < systemCount; ++j) {
        for (int k = 0; k < systemCount; ++k) {
          if (distances[j][i] != -1 && distances[i][k] != -1) {
            final int transitiveDistance = distances[j][i] + distances[i][k];
            if (distances[j][k] == -1 || transitiveDistance < distances[j][k]) {
              distances[j][k] = transitiveDistance;
            }
          }
        }
      }
    }

    this.distances = distances;
  }

  private void assignPlayerHomeworld(final Player player, final GameOptions options, final StarSystem system) {
    system.owner = system.lastOwner = player;

    final ContiguousForce contiguousForce = new ContiguousForce(player, system);
    contiguousForce.add(system);
    system.contiguousForce = contiguousForce;
    player.contiguousForces.add(contiguousForce);

    final CombinedForce combinedForce = new CombinedForce(player, system);
    combinedForce.add(system);
    player.combinedForce = combinedForce;

    GameState.recalculateMinimumGarrison(options, system, null);
  }

  public void assignPlayerHomeworlds(final Player[] players, final GameOptions options) {
    final StarSystem[] systems = this.systems;

    int assigned = 0;
    for (final StarSystem system : systems) {
      if (system.score == StarSystem.Score.PLAYER_HOMEWORLD) {
        this.assignPlayerHomeworld(players[assigned], options, system);
        ++assigned;
        if (assigned == players.length) {
          break;
        }
      }
    }

    this.recalculateMovementCosts();
  }

  public void writeDesyncReport(final Buffer buffer) {
    buffer.writeShort(this.drawingWidth);
    buffer.writeShort(this.drawingHeight);
    final int var4 = this.systems.length;
    buffer.writeShort(var4);
    final StarSystem[] var5 = this.systems;

    for (final StarSystem var7 : var5) {
      a316b(buffer, var7.name);
      buffer.writeShort(var7.posnX);
      buffer.writeShort(var7.posnY);
      buffer.writeByte(var7._z);
      final int var8 = var7.neighbors.length;
      buffer.writeByte(var8);

      int var9;
      for (var9 = 0; var9 < var8; ++var9) {
        StarSystem.write(buffer, var7.neighbors[var9]);
      }

      for (var9 = 0; var9 < 4; ++var9) {
        int var10 = var7.resources[var9];
        if (var10 < 0) {
          var10 = -var10;
        }

        ++var9;
        if (var9 < 4) {
          int var11 = var7.resources[var9];
          if (var11 < 0) {
            var11 = -var11;
          }

          var10 |= var11 << 4;
        }

        buffer.writeByte(var10);
      }

      var9 = var7.garrison;
      if (var7.hasDefensiveNet) {
        var9 |= 32768;
      }

      buffer.writeShort(var9);
      buffer.writeByte(var7.type | var7.score << 6);
    }

    for (final StarSystem var6 : this.systems) {
      Player.write(buffer, var6.lastOwner);
    }
  }
}
