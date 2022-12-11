package funorb.shatteredplans.game;

import funorb.Strings;
import funorb.io.Buffer;
import funorb.io.CipheredBuffer;
import funorb.io.ReadableBuffer;
import funorb.io.WritableBuffer;
import funorb.net.ProtocolException;
import funorb.shatteredplans.StringConstants;
import funorb.shatteredplans.client.GameUI;
import funorb.shatteredplans.client.JagexApplet;
import funorb.shatteredplans.client.ShatteredPlansClient;
import funorb.shatteredplans.client.game.CombatEngagementLog;
import funorb.shatteredplans.client.game.TurnEventLog;
import funorb.shatteredplans.map.CaptureAndHoldVictoryChecker;
import funorb.shatteredplans.map.ConquestVictoryChecker;
import funorb.shatteredplans.map.DerelictsVictoryChecker;
import funorb.shatteredplans.map.Map;
import funorb.shatteredplans.map.PointsVictoryChecker;
import funorb.shatteredplans.map.StarSystem;
import funorb.shatteredplans.map.TannhauserLink;
import funorb.shatteredplans.map.TannhauserUnconnectedException;
import funorb.shatteredplans.map.TutorialVictoryChecker;
import funorb.shatteredplans.map.VictoryChecker;
import funorb.shatteredplans.map.generator.CaptureAndHoldMapGenerator;
import funorb.shatteredplans.map.generator.DerelictsMapGenerator;
import funorb.shatteredplans.map.generator.MapGenerationFailure;
import funorb.shatteredplans.map.generator.MapGenerator;
import funorb.shatteredplans.map.generator.StandardMapGenerator;
import funorb.util.IsaacRandom;
import org.intellij.lang.annotations.MagicConstant;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.Queue;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public final class GameState {
  public static final int NUM_RESOURCES = 4;
  public static final int MAX_RESEARCH_POINTS = 5;
  @MagicConstant(valuesFromClass = ResourceType.class)
  public static final int[] RESOURCE_TYPES = {ResourceType.METAL, ResourceType.BIOMASS, ResourceType.ENERGY, ResourceType.EXOTICS};
  public static final int MILLIS_PER_TICK = 1000 / 50;

  private final Random random = new IsaacRandom();
  public final @NotNull GameType gameType;
  public final @NotNull GameOptions gameOptions;
  public final int turnLengthIndex;

  public Map map;
  public final List<TannhauserLink> tannhauserLinks = new ArrayList<>();
  public Player[] players;
  public String[] playerNames;
  public int playerCount;

  public final List<ProjectOrder> projectOrders = new ArrayList<>();
  public final List<BuildFleetsOrder> buildOrders = new ArrayList<>();
  public final List<MoveFleetsOrder> moveOrders = new ArrayList<>();

  public int turnNumber = 0;
  private int turnName1Index = 0;
  private int turnName2Index = 0;
  public boolean hasEnded = false;
  public int winnerIndex = -1;
  public VictoryChecker victoryChecker;

  private int defeatedPlayersBitmap;
  private int resignedPlayersBitmap;
  private int playersOfferingDrawBitmap;
  private int playersOfferingRematchBitmap;

  public int[] playerFleetProductionRanks;
  public int[] playerFleetProduction;
  private int[] playerFleetsAtCombatStart;
  private int numCombatants;
  private int[] combatFleetsRemaining;
  private int[] alliedFleetsRemaining;
  private int[] enemyCounts;
  private int[][] combatantEnemies;
  private int[] combatantPlayerIndexes;

  private int _b;
  private int[] _N;
  private int[] _j;
  private int[] _d;

  public GameState(final int turnLengthIndex,
                   final @NotNull GameOptions gameOptions,
                   final @NotNull GameType gameType,
                   final @NotNull String @NotNull [] playerNames) {
    this.turnLengthIndex = turnLengthIndex;
    this.gameOptions = gameOptions;
    this.gameType = gameType;
    this.playerCount = playerNames.length;
    this.players = new Player[this.playerCount];
    this.playerFleetProduction = new int[this.playerCount];
    this.playerNames = playerNames;

    for (int i = 0; i < this.playerCount; ++i) {
      this.players[i] = new Player(i, this.playerNames[i], GameUI.PLAYER_COLORS_DARK[i], GameUI.PLAYER_COLORS_1[i], GameUI.PLAYER_COLORS_2[i]);
      this.players[i].pactTurnsRemaining = new int[this.playerCount];
      this.players[i].allies = new boolean[this.playerCount];
    }

    this.victoryChecker = switch (this.gameType) {
      case TUTORIAL -> new TutorialVictoryChecker(this.players);
      case CONQUEST -> new ConquestVictoryChecker(this.players);
      case CAPTURE_AND_HOLD -> new CaptureAndHoldVictoryChecker(this.players);
      case POINTS -> new PointsVictoryChecker(this.players);
      case DERELICTS -> new DerelictsVictoryChecker(this.players);
    };

    this.alliedFleetsRemaining = new int[this.playerCount + 1];
    this.enemyCounts = new int[this.playerCount + 1];
    this.playerFleetsAtCombatStart = new int[this.playerCount];
    this.combatantPlayerIndexes = new int[this.playerCount + 1];
    this.combatFleetsRemaining = new int[this.playerCount + 1];
  }

  public static GameState generate(final int turnLengthIndex,
                                   final @NotNull String @NotNull [] playerNames,
                                   final @NotNull GameOptions options,
                                   final @NotNull GameType gameType) {
    return generate(turnLengthIndex, playerNames, options, gameType, GalaxySize.MEDIUM);
  }

  public static GameState generate(final int turnLengthIndex,
                                    final @NotNull String @NotNull [] playerNames,
                                    final @NotNull GameOptions options,
                                    final @NotNull GameType gameType,
                                    final @NotNull GalaxySize galaxySize) {
    final GameState game = new GameState(turnLengthIndex, options, gameType, playerNames);
    final int playerCount = playerNames.length;
    final int systemCount = playerCount * galaxySize.systemsPerPlayer;
    @SuppressWarnings("IntegerDivisionInFloatingPointContext")
    final int mapSize = (int) Math.sqrt((systemCount * 100) / 45);

    for (int attempt = 0; attempt < 50; ++attempt) {
      try {
        final int seed = ShatteredPlansClient.globalRandom.nextInt();
        final MapGenerator mapGenerator = switch (gameType) {
          case CONQUEST, POINTS ->
              new StandardMapGenerator(mapSize, mapSize, seed, systemCount, game.players.length);
          case CAPTURE_AND_HOLD -> new CaptureAndHoldMapGenerator(seed, game.players.length);
          case DERELICTS ->
              new DerelictsMapGenerator(mapSize, mapSize, seed, systemCount, game.players.length);
          default -> throw new RuntimeException("Cannot generate map for unknown gametype " + gameType);
        };

        game.map = mapGenerator.generate();
        game.map.assignPlayerHomeworlds(game.players, options);
        game.recalculateFleetProduction();
        game.recalculatePlayerFleetProduction();
        return game;
      } catch (final MapGenerationFailure var14) {
      }
    }

    throw new RuntimeException("Failed to generate map");
  }

  public @NotNull GalaxySize getGalaxySize() {
    final int systemsPerPlayer = this.map.systems.length / this.playerCount;
    return GalaxySize.lookup(systemsPerPlayer)
        .orElseThrow(() -> new IllegalStateException("no known galaxy size corresponding to " + systemsPerPlayer + " systems per player"));
  }

  public @NotNull Stream<? extends Force> streamForces(final Player player) {
    if (this.gameOptions.unifiedTerritories) {
      return Stream.of(player.combinedForce);
    } else {
      return player.contiguousForces.stream();
    }
  }

  public static void recalculateFleetsRemaining(final Force force) {
    final int[] resources = new int[NUM_RESOURCES];
    addResourceProduction(resources, force);

    int fleets = resources[0];
    for (int i = 1; i < NUM_RESOURCES; ++i) {
      if (resources[i] < fleets) {
        fleets = resources[i];
      }
    }

    if (fleets < 0) {
      fleets = 0;
    }

    force.fleetsAvailableToBuild = fleets;
  }

  private static void addResourceProduction(final int[] resources, final Force force) {
    for (final StarSystem system : force) {
      for (int i = 0; i < NUM_RESOURCES; ++i) {
        resources[i] += system.resources[i];
      }
    }
  }

  public static void recalculateFleetProduction(final Force force) {
    final int[] resources = new int[NUM_RESOURCES];
    addResourceProduction(resources, force);
    int minResourceAmount = resources[0];

    for (int i = 1; i < NUM_RESOURCES; ++i) {
      if (minResourceAmount > resources[i]) {
        minResourceAmount = resources[i];
      }
    }

    for (int i = 0; i < NUM_RESOURCES; ++i) {
      resources[i] -= minResourceAmount;
    }

    force.fleetProduction = minResourceAmount;
    force.surplusResources = resources;
    force.surplusResourceRanks = calculateRanksDescending(resources);
  }

  private static int[] calculateRanksDescending(final int[] data) {
    final int[] ranks = new int[data.length];

    for (int i = 1; i < data.length; i++) {
      int j;
      for (j = i - 1; j >= 0 && data[ranks[j]] < data[i]; j--) {
        ranks[j + 1] = ranks[j];
      }
      ranks[j + 1] = i;
    }

    return ranks;
  }

  /**
   * Returns an array the same length as the input whose values are indexes
   * into the input array, ordered such that earlier indexes have lower values.
   */
  public static int[] calculateRanksAscending(final int[] data) {
    final int[] ranks = new int[data.length];

    for (int i = 1; i < data.length; i++) {
      int j;
      for (j = i - 1; j >= 0 && data[ranks[j]] > data[i]; --j) {
        ranks[j + 1] = ranks[j];
      }
      ranks[j + 1] = i;
    }

    return ranks;
  }

  public static void recalculateMinimumGarrison(final GameOptions options, final StarSystem system, final boolean[] systemsCollapsed) {
    if (options.noChainCollapsing) {
      system.minimumGarrison = 0;
    } else if (options.simpleGarrisoning) {
      system.minimumGarrison = 1;
    } else {
      system.minimumGarrison = 0;
      for (final StarSystem neighbor : system.neighbors) {
        if (system.owner != neighbor.owner || (systemsCollapsed != null && systemsCollapsed[neighbor.index])) {
          ++system.minimumGarrison;
        }
      }
    }
  }

  private StarSystem readSystem(final ReadableBuffer buffer) {
    return StarSystem.read(buffer, this.map.systems);
  }

  public Player readPlayer(final ReadableBuffer buffer) {
    return Player.read(buffer, this.players);
  }

  private void buildFleetsAndAwardBonusResearch(final TurnEventLog turnLog, final int seed) {
    for (final BuildFleetsOrder order : this.buildOrders) {
      if (this.gameOptions.unifiedTerritories) {
        order.system.owner.combinedForce.fleetsAvailableToBuild -= order.quantity;
      } else {
        order.system.contiguousForce.fleetsAvailableToBuild -= order.quantity;
      }

      order.system.garrison += order.quantity;
    }

    for (final Player player : this.players) {
      if (this.gameOptions.unifiedTerritories) {
        final CombinedForce combinedForce = player.combinedForce;
        if (combinedForce.fleetsAvailableToBuild > 0) {
          combinedForce.getCapital().garrison += combinedForce.fleetsAvailableToBuild;
        }
      } else {
        for (final ContiguousForce var8 : player.contiguousForces) {
          if (var8.fleetsAvailableToBuild > 0) {
            var8.getCapital().garrison += var8.fleetsAvailableToBuild;
          }
        }
      }
    }

    if (turnLog != null) {
      for (final BuildFleetsOrder order : this.buildOrders) {
        turnLog.addBuildFleetsEvent(order.system, order.quantity);
      }

      for (final Player player : this.players) {
        if (!this.gameOptions.unifiedTerritories) {
          for (final ContiguousForce force : player.contiguousForces) {
            if (force.fleetsAvailableToBuild > 0) {
              turnLog.addBuildFleetsEvent(force.getCapital(), force.fleetsAvailableToBuild);
            }
          }
        } else if (player.combinedForce.fleetsAvailableToBuild > 0) {
          turnLog.addBuildFleetsEvent(player.combinedForce.getCapital(), player.combinedForce.fleetsAvailableToBuild);
        }
      }
    }

    if (this.gameOptions.projectsAllowed && !(this.victoryChecker instanceof TutorialVictoryChecker)) {
      final int[] leftover = new int[NUM_RESOURCES];

      for (final Player player : this.players) {
        if (!this.isPlayerDefeated(player.index)) {
          Arrays.fill(leftover, 0);

          int totalLeftover = 0;
          int maxLeftover = 0;
          if (this.gameOptions.unifiedTerritories) {
            for (int i = 0; i < NUM_RESOURCES; ++i) {
              leftover[i] = player.combinedForce.surplusResources[i];
              if (maxLeftover < leftover[i]) {
                maxLeftover = leftover[i];
              }

              leftover[i]++;
              totalLeftover += leftover[i];
            }
          } else {
            for (final ContiguousForce force : player.contiguousForces) {
              for (int i = 0; i < NUM_RESOURCES; ++i) {
                leftover[i] += force.surplusResources[i];
                if (leftover[i] > maxLeftover) {
                  maxLeftover = leftover[i];
                }
              }
            }

            for (int i = 0; i < NUM_RESOURCES; ++i) {
              leftover[i]++;
              totalLeftover += leftover[i];
            }
          }

          this.random.setSeed(1 + player.index ^ seed);
          int roll = ShatteredPlansClient.randomIntBounded(this.random, totalLeftover);

          for (int i = 0; i < NUM_RESOURCES; ++i) {
            if (roll < leftover[i]) {
              if (player.researchPoints[i] >= 5) {
                if (player.stats != null) {
                  ++player.stats.wastedResearch;
                }
              } else {
                player.researchPoints[i]++;
              }
              break;
            }

            roll -= leftover[i];
          }
        }
      }
    }
  }

  public void setTurnNameIndexes(final int index1, final int index2) {
    this.turnName1Index = index1;
    this.turnName2Index = index2;
  }

  public void writeTurnNameIndexes(final WritableBuffer buffer) {
    buffer.writeByte(this.turnName2Index);
    buffer.writeByte(this.turnName1Index);
  }

  private void kickOutCombatantsWithoutEnemies() {
    int var2 = 0;

    while (var2 < this.numCombatants) {
      if (this.enemyCounts[var2] == 0) {
        this.kickOutCombatant(var2);
      } else {
        ++var2;
      }
    }
  }

  public boolean movementInRange(final StarSystem source, final StarSystem destination) {
    return this.gameOptions.movementRange == 0
        || this.gameOptions.movementRange >= this.map.movementCosts[source.index][destination.index];
  }

  private void kickOutCombatant(final int combatantIndex) {
    final int playerIndex = this.combatantPlayerIndexes[combatantIndex];
    --this.numCombatants;
    this.combatantPlayerIndexes[combatantIndex] = this.combatantPlayerIndexes[this.numCombatants];
    this.combatantEnemies[combatantIndex] = this.combatantEnemies[this.numCombatants];
    this.enemyCounts[combatantIndex] = this.enemyCounts[this.numCombatants];

    for (int i = 0; i < this.numCombatants; ++i) {
      for (int j = 0; j < this.enemyCounts[i]; ++j) {
        if (playerIndex == this.combatantEnemies[i][j]) {
          this.enemyCounts[i]--;
          this.combatantEnemies[i][j] = this.combatantEnemies[i][this.enemyCounts[i]];
        }
      }
    }
  }

  public void recalculateFleetsRemaining() {
    for (final Player player : this.players) {
      if (this.gameOptions.unifiedTerritories) {
        recalculateFleetsRemaining(player.combinedForce);
      } else {
        player.contiguousForces.iterator()
            .forEachRemaining(GameState::recalculateFleetsRemaining);
      }
    }
  }

  public void recalculatePlayerFleetProduction() {
    for (int i = 0; i < this.playerCount; ++i) {
      final Player player = this.players[i];
      this.playerFleetProduction[i] = 0;
      if (this.gameOptions.unifiedTerritories) {
        this.playerFleetProduction[i] = player.combinedForce.fleetProduction;
      } else {
        for (final ContiguousForce force : player.contiguousForces) {
          this.playerFleetProduction[i] += force.fleetProduction;
        }
      }
    }

    for (final StarSystem system : this.map.systems) {
      if (system.owner != null) {
        this.playerFleetProduction[system.owner.index] += system.garrison;
      }
    }

    this.playerFleetProductionRanks = calculateRanksAscending(this.playerFleetProduction);
  }

  @SuppressWarnings("SameParameterValue")
  public void readMap(final CipheredBuffer buffer, final Player localPlayer) throws MapGenerationFailure, TannhauserUnconnectedException {
    this.map = Map.read(buffer, this.gameType, this, this.gameOptions, this.players, localPlayer, this.tannhauserLinks);
  }

  private void simulatePeaceNegotiations() {
    while (this.numCombatants > 1) {
      int rolledCombatant = 0;
      int roll1 = this.random.nextInt();

      for (int combatant = 1; combatant < this.numCombatants; ++combatant) {
        final int roll2 = this.random.nextInt();
        if (roll1 < roll2) {
          roll1 = roll2;
          rolledCombatant = combatant;
        }
      }

      final int rolledPlayer = this.combatantPlayerIndexes[rolledCombatant];
      if (--this.combatFleetsRemaining[rolledPlayer] == 0) {
        this.kickOutCombatant(rolledCombatant);
      }
    }
  }

  private void initializeCombatantEnemies(final boolean[][] allies) {
    this.combatantEnemies = new int[this.playerCount + 1][this.playerCount];
    Arrays.fill(this.enemyCounts, 0, this.numCombatants, 0);

    for (int combatant1 = 0; combatant1 < this.numCombatants; ++combatant1) {
      final int player1 = this.combatantPlayerIndexes[combatant1];
      for (int combatant2 = combatant1 + 1; combatant2 < this.numCombatants; ++combatant2) {
        final int player2 = this.combatantPlayerIndexes[combatant2];
        if (player1 == this.playerCount || player2 == this.playerCount || !allies[player1][player2]) {
          this.combatantEnemies[combatant1][this.enemyCounts[combatant1]++] = player2;
          this.combatantEnemies[combatant2][this.enemyCounts[combatant2]++] = player1;
        }
      }
    }

    this.kickOutCombatantsWithoutEnemies();
  }

  public void setWinner(final int winner) {
    this.hasEnded = true;
    this.winnerIndex = winner;
  }

  public void simulateTurn(final TurnEventLog turnLog, final int seed) {
    JagexApplet.printDebug("SIMULATE TURN " + Integer.toBinaryString(this.getAlliancesBitmap()) + " | " + Integer.toHexString(seed));
    final boolean[][] allies = new boolean[this.playerCount][];
    if (this.gameOptions.diplomacyAllowed) {
      for (int i = 0; i < this.playerCount; ++i) {
        allies[i] = this.players[i].allies;
      }
    } else {
      for (int i = 0; i < this.playerCount; ++i) {
        allies[i] = new boolean[this.playerCount];
      }
    }

    this.buildFleetsAndAwardBonusResearch(turnLog, seed);
    this.deployDefensiveNetsAndStellarBombs(turnLog);
    this.resolveCombatEngagements(turnLog, seed, allies);
    this.processCollapsesAndRetreats(turnLog, seed);
    this.deployTannhauserAndTerraformingProjects(turnLog, seed);
    this.expirePactsAndTannhauserLinks();
    this.recalculateFleetProduction();
    this.checkDefeat();
    this.checkVictory();

    for (final StarSystem system : this.map.systems) {
      system.retreatingFleets = 0;
      system.retreatedFleets = 0;
    }

    this.recalculatePlayerFleetProduction();
    ++this.turnNumber;
    this.map.recalculateMovementCosts();
  }

  private void deployDefensiveNetsAndStellarBombs(final TurnEventLog turnLog) {
    if (this.gameOptions.projectsAllowed) {
      for (final ProjectOrder order : this.projectOrders) {
        if (order.type == ResourceType.METAL) {
          order.target.hasDefensiveNet = true;
          if (turnLog != null) {
            turnLog.addDefensiveNetEvent(order.player, order.target);
          }
        }
      }

      for (final ProjectOrder order : this.projectOrders) {
        if (order.type == ResourceType.ENERGY
            && order.target.owner != order.player
            && (order.target.owner == null || !order.player.allies[order.target.owner.index])) {
          order.player.researchPoints[ResourceType.ENERGY] = 0;
          final int kills = this.deployStellarBomb(order.target);
          if (turnLog != null) {
            turnLog.addStellarBombEvent(order.player, order.target, kills);
          }
        }

        if (order.type != ResourceType.ENERGY) {
          order.player.researchPoints[order.type] = 0;
        }
      }
    }
  }

  public void setMap(final Map var2) {
    this.map = var2;

    for (final BuildFleetsOrder var3 : this.buildOrders) {
      var3.system = this.map.systems[var3.system.index];
    }

    for (final MoveFleetsOrder order : this.moveOrders) {
      order.source = this.map.systems[order.source.index];
      order.target = this.map.systems[order.target.index];
      order.target.incomingOrders.add(order);
      order.source.outgoingOrders.add(order);
    }

    for (final ProjectOrder var5 : this.projectOrders) {
      var5.target = this.map.systems[var5.target.index];
      if (var5.source != null) {
        var5.source = this.map.systems[var5.source.index];
      }
    }

    for (final TannhauserLink link : this.tannhauserLinks) {
      link.system1 = this.map.systems[link.system1.index];
      link.system2 = this.map.systems[link.system2.index];
    }
  }

  public void checkVictory() {
    if (this.victoryChecker.checkVictory(this)) {
      final Player[] winners = this.victoryChecker.getVictors();
      if (winners == null) {
        throw new RuntimeException("VictoryChecker claims someone has won, but winners array is null.");
      }

      if (winners.length == 1) {
        this.setWinner(winners[0].index);
      } else {
        this.setWinner(-1);
      }
    }
  }

  private void a244(final StarSystem var1, final CombatEngagementLog var2) {
    for (final MoveFleetsOrder var4 : var1.incomingOrders) {
      if (var4.player != var1.owner) {
        final int var5 = this.playerFleetsAtCombatStart[var4.player.index];
        final int var6 = this.combatFleetsRemaining[var4.player.index];
        final int var7 = var6 * var4.quantity / var5;
        if (var7 != 0 && var2 != null) {
          var2.a631(var4, 0, var7);
        }

        var4.source.retreatedFleets += var7;
      }
    }
  }

  private void disownSystem(final StarSystem system) {
    this.detachSystemFromForce(system);
    system.owner = null;
    system.minimumGarrison = 0;
    system.garrison = 0;
  }

  public String turnName() {
    if (this.turnNumber > 0) {
      return Strings.format(StringConstants.TURN_NAME, Strings.format(StringConstants.TURN_NAMES_2[this.turnName2Index], StringConstants.TURN_NAMES_1[this.turnName1Index]));
    } else {
      return StringConstants.TURN_NAME_FIRST;
    }
  }

  private void resolveCombatEngagements(final TurnEventLog turnLog, final int seed, final boolean[][] allies) {
    for (final StarSystem system : this.map.systems) {
      this.resolveCombatEngagement(seed, system, turnLog, allies);
    }
  }

  private void captureSystem(final StarSystem system, final Player newOwner) {
    if (system.owner != newOwner) {
      system.owner = newOwner;
      if (newOwner != system.lastOwner && system.resources[0] >= 0) {
        for (int i = 0; i < NUM_RESOURCES; ++i) {
          newOwner.researchPoints[i] += system.resources[i];
          if (newOwner.researchPoints[i] > MAX_RESEARCH_POINTS) {
            if (newOwner.stats != null) {
              newOwner.stats.wastedResearch += newOwner.researchPoints[i] - MAX_RESEARCH_POINTS;
            }
            newOwner.researchPoints[i] = MAX_RESEARCH_POINTS;
          }
        }
      }

      if (system.contiguousForce != null) {
        this.detachSystemFromForce(system);
      }

      this.assignSystemForce(system);
      this.recalculateMinimumGarrison(system, null);
      for (final StarSystem neighbor : system.neighbors) {
        this.recalculateMinimumGarrison(neighbor, null);
      }

      system.owner.combinedForce.add(system);
      if (system.owner.combinedForce.getCapital() == null) {
        system.owner.combinedForce.setCapital(system);
      }
      recalculateFleetProduction(system.owner.combinedForce);
    }
  }

  public int getAlliancesBitmap() {
    int alliances = 0;
    for (int i = 0; i < this.playerCount; ++i) {
      for (int j = i + 1; j < this.playerCount; ++j) {
        alliances <<= 1;
        if (this.players[i].allies[j]) {
          alliances |= 1;
        }
      }
    }
    return alliances;
  }

  public void setAlliancesBitmap(int alliances) {
    for (int i = this.playerCount - 2; i >= 0; --i) {
      for (int j = this.playerCount - 1; j > i; --j) {
        this.players[i].allies[j] = (alliances & 1) != 0;
        alliances >>= 1;
      }
    }
  }

  private void calculateAlliedVictoryRetreats(final CombatEngagementLog engagementLog, final StarSystem system) {
    for (final MoveFleetsOrder order : system.incomingOrders) {
      final int fleetsAtStart = this.playerFleetsAtCombatStart[order.player.index];
      final int fleetsRemaining = this.alliedFleetsRemaining[order.player.index];
      int retreatingFleets = order.quantity * fleetsRemaining / fleetsAtStart;
      if (system.owner == order.player) {
        retreatingFleets = order.quantity * (fleetsRemaining - system.garrison) / fleetsAtStart;
      }

      if (retreatingFleets != 0 && engagementLog != null) {
        engagementLog.a631(order, 0, retreatingFleets);
      }

      order.source.retreatedFleets += retreatingFleets;
    }
  }

  private boolean isSystemContested(final TurnEventLog turnLog, final StarSystem system) {
    for (int i = 0; i < this.playerCount; ++i) {
      this.playerFleetsAtCombatStart[i] = 0;
    }

    if (system.owner != null) {
      this.playerFleetsAtCombatStart[system.owner.index] = system.garrison;
    }

    boolean result = false;
    for (final MoveFleetsOrder order : system.incomingOrders) {
      if (order.player != null) {
        this.playerFleetsAtCombatStart[order.player.index] += order.quantity;
        if (system.owner != order.player) {
          result = true;
        }
      }
    }

    if (turnLog != null) {
      for (final MoveFleetsOrder order : system.incomingOrders) {
        turnLog.addMoveFleetsEvent(order.source, order.target, order.player, order.quantity);
      }
    }

    for (final MoveFleetsOrder order : system.outgoingOrders) {
      if (order.player != null) {
        this.playerFleetsAtCombatStart[order.player.index] -= order.quantity;
      }
    }

    return result;
  }

  public void readTurnOrders(@SuppressWarnings("SameParameterValue") final Buffer buffer, int len) {
    this.resetTurnState();

    while (len > 0) {
      final int type = buffer.readUByte();
      --len;
      if (type == 255) {
        throw new RuntimeException();
      }

      if (type >= 251) {
        final ProjectOrder order = this.readProjectOrder(buffer, type);
        this.projectOrders.add(order);
        len -= order.type == ResourceType.EXOTICS ? 3 : 2;
      } else if (type >= 192) {
        final int count = type - 191;
        for (int i = 0; i < count; ++i) {
          this.buildOrders.add(this.readBuildOrder(buffer));
        }
        len -= 3 * count;
      } else {
        final int count = type + 1;
        for (int i = 0; i < count; ++i) {
          final MoveFleetsOrder moveOrder = this.readMoveOrder(buffer);
          this.moveOrders.add(moveOrder);
          moveOrder.source.outgoingOrders.add(moveOrder);
          moveOrder.target.incomingOrders.add(moveOrder);
        }
        len -= 4 * count;
      }
    }
  }

  public void writeTurnOrders(final WritableBuffer buffer) {
    TurnOrders.write(buffer, this.projectOrders, this.buildOrders, this.moveOrders);
  }

  public ProjectOrder readProjectOrder(final ReadableBuffer buffer, final int type) {
    @MagicConstant(valuesFromClass = ResourceType.class)
    final int projectType = type - 251;
    final Player player = this.readPlayer(buffer);
    final StarSystem target = this.readSystem(buffer);
    if (projectType == ResourceType.EXOTICS) {
      final StarSystem source = this.readSystem(buffer);
      return new ProjectOrder(player, source, target);
    } else {
      return new ProjectOrder(projectType, player, target);
    }
  }

  public MoveFleetsOrder readMoveOrder(final ReadableBuffer buffer) {
    final StarSystem source = this.readSystem(buffer);
    final StarSystem target = this.readSystem(buffer);
    final int quantity = buffer.readUShort();
    return new MoveFleetsOrder(source, target, quantity);
  }

  public BuildFleetsOrder readBuildOrder(final ReadableBuffer buffer) {
    final StarSystem system = this.readSystem(buffer);
    final int quantity = buffer.readUShort();
    return new BuildFleetsOrder(system, quantity);
  }

  private void terraformSystem(final Random random, final StarSystem system) {
    if (system.score <= 0) {
      for (int i = 0; i < NUM_RESOURCES; ++i) {
        system.resources[i] = 1;
      }

      system.resources[ShatteredPlansClient.randomIntBounded(random, 4)]++;
      system.resources[ShatteredPlansClient.randomIntBounded(random, 4)]++;
      system.score = StarSystem.Score.TERRAFORMED;
    }
  }

  private void resolveCombatEngagement(final int seed, final StarSystem system, final TurnEventLog turnLog, final boolean[][] allies) {
    if (this.isSystemContested(turnLog, system)) {
      this.random.setSeed(system.index ^ seed);
      this.numCombatants = 0;

      for (int i = 0; i < this.playerCount; ++i) {
        final int fleetsAtCombatStart = this.playerFleetsAtCombatStart[i];
        this.combatFleetsRemaining[i] = fleetsAtCombatStart;
        if (fleetsAtCombatStart > 0) {
          this.combatantPlayerIndexes[this.numCombatants++] = i;
        }
      }

      final int ownerAtCombatStart = system.owner != null ? system.owner.index : this.playerCount;
      if (system.owner == null) {
        this.combatFleetsRemaining[this.playerCount] = system.garrison;
        if (system.garrison > 0) {
          this.combatantPlayerIndexes[this.numCombatants++] = this.playerCount;
        }
      }

      CombatEngagementLog engagementLog = null;
      if (turnLog != null) {
        final Player[] combatantPlayers = new Player[this.numCombatants];

        for (int i = 0; i < this.numCombatants; ++i) {
          final int playerId = this.combatantPlayerIndexes[i];
          combatantPlayers[i] = playerId < this.playerCount ? this.players[playerId] : null;
        }

        engagementLog = turnLog.addCombatEngagement(system, combatantPlayers, this.combatFleetsRemaining);
      }

      if (system.hasDefensiveNet) {
        this.combatFleetsRemaining[ownerAtCombatStart] *= 2;
      }

      this.initializeCombatantEnemies(allies);
      final int[] playerKills = this.simulateCombat();
      if (engagementLog != null) {
        engagementLog.setPlayerKills(playerKills);
      }

      if (system.hasDefensiveNet) {
        final int minFleetsRemaining = (system.owner == null || this.combatFleetsRemaining[ownerAtCombatStart] <= 0) ? 0 : 1;
        final int realFleetsRemaining = this.combatFleetsRemaining[ownerAtCombatStart] / 2;
        this.combatFleetsRemaining[ownerAtCombatStart] = Math.max(minFleetsRemaining, realFleetsRemaining);
      }

      this.calculateCombatRetreats(system, engagementLog);
      if (this.combatFleetsRemaining[ownerAtCombatStart] <= 0) {
        System.arraycopy(this.combatFleetsRemaining, 0, this.alliedFleetsRemaining, 0, this.playerCount + 1);

        this.numCombatants = 0;
        for (int i = 0; i < this.playerCount; ++i) {
          if (this.alliedFleetsRemaining[i] != 0) {
            this.combatantPlayerIndexes[this.numCombatants++] = i;
          }
        }

        if (this.numCombatants == 0) {
          this.disownSystem(system);
        } else {
          this.simulatePeaceNegotiations();
          final int victorIndex = this.combatantPlayerIndexes[0];
          final Player victor = this.players[victorIndex];
          this.captureSystem(system, victor);
          system.garrison = this.combatFleetsRemaining[victorIndex];
          if (engagementLog != null) {
            engagementLog.victor = victor;
            engagementLog.fleetsAtCombatEnd = system.garrison;
          }
          this.calculateAlliedVictoryRetreats(engagementLog, system);
        }
      } else {
        system.garrison = this.combatFleetsRemaining[ownerAtCombatStart];
        this.a244(system, engagementLog);
        if (engagementLog != null) {
          engagementLog.victor = system.owner;
          engagementLog.fleetsAtCombatEnd = system.garrison;
        }
      }
    } else if (system.owner != null) {
      system.garrison = this.playerFleetsAtCombatStart[system.owner.index];
    }
  }

  public void resetTurnState() {
    this.buildOrders.clear();
    this.moveOrders.clear();
    this.projectOrders.clear();

    for (final StarSystem system : this.map.systems) {
      system.outgoingOrders.clear();
      system.incomingOrders.clear();
    }

    for (final Player player : this.players) {
      player.outgoingPactOffersBitmap = 0;
      player.incomingPactOffersBitmap = 0;
    }
  }

  private void deployTannhauserAndTerraformingProjects(final TurnEventLog turnLog, final int seed) {
    if (this.gameOptions.projectsAllowed) {
      for (final ProjectOrder order : this.projectOrders) {
        if (order.type == ResourceType.EXOTICS) {
          this.createTannhauserLink(order.source, order.target);
          if (turnLog != null) {
            turnLog.addTannhauserEvent(order.player, order.target, order.source);
          }
        } else if (order.type == ResourceType.BIOMASS) {
          this.random.setSeed(order.target.index ^ seed);
          this.terraformSystem(this.random, order.target);
          if (turnLog != null) {
            turnLog.addTerraformingEvent(order.player, order.target);
          }
        }
      }
    }
  }

  private void createTannhauserLink(StarSystem source, StarSystem target) {
    if (!target.hasNeighbor(source)) {
      if (target.index < source.index) {
        final StarSystem tmp = target;
        target = source;
        source = tmp;
      }

      this.tannhauserLinks.add(new TannhauserLink(source, target, 4));
      StarSystem.linkNeighbors(source, target);
      source.sortNeighbors();
      target.sortNeighbors();
      if (source.owner == target.owner && source.contiguousForce != target.contiguousForce) {
        ContiguousForce force1 = source.contiguousForce;
        ContiguousForce force2 = target.contiguousForce;
        if (force2.fleetProduction < force1.fleetProduction) {
          final ContiguousForce tmp = force2;
          force2 = force1;
          force1 = tmp;
        }

        for (final StarSystem system : force1) {
          system.contiguousForce = force2;
          force2.add(system);
        }

        force1.player.contiguousForces.remove(force1);
        recalculateFleetProduction(force2);
      }

      this.recalculateMinimumGarrison(source, null);
      this.recalculateMinimumGarrison(target, null);
      this.map.recalculateDistances();
      this.map.recalculateMovementCosts();
    }
  }

  private void assignSystemForce(final StarSystem system) {
    assert system.contiguousForce == null;
    for (final StarSystem neighbor : system.neighbors) {
      if (system.owner == neighbor.owner) {
        system.contiguousForce = neighbor.contiguousForce;
        system.contiguousForce.add(system);
        break;
      }
    }

    if (system.contiguousForce == null) {
      system.contiguousForce = new ContiguousForce(system.owner, system);
      system.contiguousForce.add(system);
      system.owner.contiguousForces.add(system.contiguousForce);
    } else {
      for (final StarSystem neighbor : system.neighbors) {
        if (system.owner == neighbor.owner && system.contiguousForce != neighbor.contiguousForce) {
          final ContiguousForce forceToMerge = neighbor.contiguousForce;
          for (final StarSystem connectedSystem : forceToMerge) {
            connectedSystem.contiguousForce = system.contiguousForce;
            system.contiguousForce.add(connectedSystem);
          }
          forceToMerge.player.contiguousForces.remove(forceToMerge);
        }
      }
    }

    recalculateFleetProduction(system.contiguousForce);
  }

  public void generateNewTurnName() {
    final int turnNames1Half = StringConstants.TURN_NAMES_1.length / 2;
    final int turnNames2Half = StringConstants.TURN_NAMES_2.length / 2;
    final int turnNameCombosHalf = StringConstants.TURN_NAMES_2.length * StringConstants.TURN_NAMES_1.length / 2;
    if (this._d == null) {
      this._b = 0;
      this._N = new int[turnNames2Half];
      this._j = new int[turnNames1Half];
      this._d = new int[turnNameCombosHalf];
    }

    if (this.turnNumber != 0) {
      final int var10 = Math.min(this._b, turnNameCombosHalf);
      final int var11 = Math.min(this._b, turnNames2Half);
      final int var12 = Math.min(this._b, turnNames1Half);

      label51:
      while (true) {
        final int name2Index = ShatteredPlansClient.randomIntBounded(StringConstants.TURN_NAMES_2.length);
        for (int i = 0; i < var11; ++i) {
          if (this._N[i] == name2Index) {
            continue label51;
          }
        }

        label60:
        while (true) {
          final int name1Index = ShatteredPlansClient.randomIntBounded(StringConstants.TURN_NAMES_1.length);
          for (int i = 0; i < var12; ++i) {
            if (this._j[i] == name1Index) {
              continue label60;
            }
          }

          final int var9 = StringConstants.TURN_NAMES_1.length * name2Index + name1Index;
          for (int i = 0; i < var10; ++i) {
            if (this._d[i] == var9) {
              continue label51;
            }
          }

          this._N[this._b % turnNames2Half] = name2Index;
          this._j[this._b % turnNames1Half] = name1Index;
          this._d[this._b % turnNameCombosHalf] = var9;
          ++this._b;
          this.turnName1Index = name1Index;
          this.turnName2Index = name2Index;
          return;
        }
      }
    }
  }

  public void addOrders(final Collection<ProjectOrder> projectOrders, final Collection<BuildFleetsOrder> buildOrders, final Collection<MoveFleetsOrder> moveOrders) {
    this.projectOrders.addAll(projectOrders);
    this.buildOrders.addAll(buildOrders);

    for (final MoveFleetsOrder order : moveOrders) {
      this.moveOrders.add(order);
      order.source.outgoingOrders.add(order);
      order.target.incomingOrders.add(order);
    }

    projectOrders.clear();
    buildOrders.clear();
    moveOrders.clear();
  }

  private void recalculateMinimumGarrison(final StarSystem system, final boolean[] systemsCollapsed) {
    recalculateMinimumGarrison(this.gameOptions, system, systemsCollapsed);
  }

  public void writeDesyncReport(@SuppressWarnings("SameParameterValue") final Buffer packet, final Player player) {
    this.map.writeDesyncReport(packet);
    int var8 = 0;

    int var9;
    for (var9 = 0; var9 < this.players.length; ++var9) {
      var8 += this.players[var9].contiguousForces.size();
    }

    packet.writeByte(var8);

    int var11;
    for (var9 = 0; this.players.length > var9; ++var9) {
      for (final ContiguousForce var10 : this.players[var9].contiguousForces) {
        Player.write(packet, var10.player);
        StarSystem.write(packet, var10.getCapital());
        var11 = var10.size();
        packet.writeByte(var11);

        for (final StarSystem var12 : var10) {
          if (var12 != var10.getCapital()) {
            StarSystem.write(packet, var12);
          }
        }
      }
    }

    var9 = 0;

    int var14;
    for (var14 = 0; this.players.length > var14; ++var14) {
      if (this.players[var14].combinedForce != null && this.players[var14].combinedForce.size() > 0) {
        ++var9;
      }
    }

    packet.writeByte(var9);

    for (var14 = 0; var14 < this.players.length; ++var14) {
      final CombinedForce var15 = this.players[var14].combinedForce;
      if (var15 != null && var15.size() > 0) {
        Player.write(packet, this.players[var14]);
        StarSystem.write(packet, var15.getCapital());
        final int var16 = var15.size();
        packet.writeByte(var16);

        for (final StarSystem var13 : var15) {
          if (var13 != var15.getCapital()) {
            StarSystem.write(packet, var13);
          }
        }
      }
    }

    for (var14 = 0; var14 < this.players.length; ++var14) {
      for (var11 = var14 + 1; var11 < this.players.length; ++var11) {
        if (!this.players[var14].allies[var11]) {
          packet.writeByte(127);
        } else {
          packet.writeByte(this.players[var14].pactTurnsRemaining[var11]);
        }
      }
    }

    if (player != null) {
      packet.writeByte(player.outgoingPactOffersBitmap);
    }

    for (var14 = 0; this.players.length > var14; ++var14) {
      for (var11 = 0; var11 < 4; ++var11) {
        packet.writeByte(this.players[var14].researchPoints[var11]);
      }
    }

    packet.writeByte(this.tannhauserLinks.size());
    for (final TannhauserLink link : this.tannhauserLinks) {
      StarSystem.write(packet, link.system1);
      StarSystem.write(packet, link.system2);
      packet.writeByte(link.turnsLeft);
    }

    this.victoryChecker.write(packet);
    packet.writeByte(this.turnName2Index);
    packet.writeByte(this.turnName1Index);
  }

  private int[] simulateCombat() {
    final int[] kills = new int[this.playerCount];

    int loser;
    do {
      if (this.numCombatants <= 1) {
        return kills;
      }

      int rolledCombatant = 0;
      int roll1 = this.random.nextInt();

      for (int combatant = 1; combatant < this.numCombatants; ++combatant) {
        final int roll2 = this.random.nextInt();
        if (roll2 > roll1) {
          roll1 = roll2;
          rolledCombatant = combatant;
        }
      }

      final int[] rolledEnemies = this.combatantEnemies[rolledCombatant];
      if (this.enemyCounts[rolledCombatant] == 0) {
        throw new RuntimeException();
      }

      loser = rolledEnemies[ShatteredPlansClient.randomIntBounded(this.random, this.enemyCounts[rolledCombatant])];
      if (this.combatantPlayerIndexes[rolledCombatant] < this.playerCount) {
        ++kills[this.combatantPlayerIndexes[rolledCombatant]];
      }

      if (--this.combatFleetsRemaining[loser] == 0) {
        this.kickOutCombatant(this.combatantIndexForPlayer(loser));
        this.kickOutCombatantsWithoutEnemies();
      }
    } while (this.combatFleetsRemaining[loser] >= 0);

    throw new RuntimeException();
  }

  private int combatantIndexForPlayer(final int playerIndex) {
    for (int i = 0; i < this.numCombatants; ++i) {
      if (this.combatantPlayerIndexes[i] == playerIndex) {
        return i;
      }
    }
    throw new AssertionError("the given player is not currently a combatant");
  }

  // only used by the tutorial
  public void addPlayer(final Player newPlayer) {
    for (final Player player : this.players) {
      final boolean[] allies = new boolean[this.players.length + 1];
      final int[] pactTurnsRemaining = new int[this.players.length + 1];
      for (int i = 0; i < player.allies.length; ++i) {
        allies[i] = player.allies[i];
        pactTurnsRemaining[i] = player.pactTurnsRemaining[i];
      }
      player.allies = allies;
      player.pactTurnsRemaining = pactTurnsRemaining;
    }

    newPlayer.pactTurnsRemaining = new int[this.playerCount + 1];
    newPlayer.allies = new boolean[this.playerCount + 1];
    recalculateFleetsRemaining(newPlayer.combinedForce);
    recalculateFleetProduction(newPlayer.combinedForce);
    final Player[] players = new Player[this.playerCount + 1];
    final String[] playerNames = new String[this.playerCount + 1];
    final int[] playerFleetProduction = new int[this.playerCount + 1];

    for (int i = 0; i < this.players.length; ++i) {
      players[i] = this.players[i];
      playerNames[i] = this.playerNames[i];
      playerFleetProduction[i] = this.playerFleetProduction[i];
    }

    players[this.players.length] = newPlayer;
    playerNames[this.players.length] = newPlayer.name;
    this.playerFleetProduction = playerFleetProduction;
    ++this.playerCount;
    this.players = players;
    this.playerNames = playerNames;
    this.alliedFleetsRemaining = new int[this.playerCount + 1];
    this.combatFleetsRemaining = new int[this.playerCount + 1];
    this.enemyCounts = new int[this.playerCount + 1];
    this.combatantPlayerIndexes = new int[this.playerCount + 1];
    this.playerFleetsAtCombatStart = new int[this.playerCount];
  }

  private void expirePactsAndTannhauserLinks() {
    if (this.gameOptions.diplomacyAllowed) {
      for (final Player player : this.players) {
        for (int i = 0; i < this.playerCount; ++i) {
          if (player.pactTurnsRemaining[i] > 0 && --player.pactTurnsRemaining[i] == 0) {
            player.allies[i] = false;
          }
        }
      }
    }

    if (this.gameOptions.projectsAllowed) {
      for (final Iterator<TannhauserLink> it = this.tannhauserLinks.iterator(); it.hasNext(); ) {
        final TannhauserLink link = it.next();
        --link.turnsLeft;
        if (link.turnsLeft <= 0) {
          it.remove();
          link.system1.removeNeighbor(link.system2);
          link.system1.sortNeighbors();
          link.system2.sortNeighbors();
          final ContiguousForce force1 = link.system1.contiguousForce;
          if (force1 != null && force1 == link.system2.contiguousForce) {
            this.splitForceIfNecessary(force1);
          }

          this.recalculateMinimumGarrison(link.system1, null);
          this.recalculateMinimumGarrison(link.system2, null);
          this.map.recalculateDistances();
          this.map.recalculateMovementCosts();
        }
      }
    }
  }

  private void eliminatePlayer(final Player player) {
    for (final StarSystem system : this.map.systems) {
      if (system.owner == player) {
        system.contiguousForce = null;
        system.owner = null;
      }
    }

    player.contiguousForces.clear();
    if (player.combinedForce != null) {
      player.combinedForce.clear();
    }
  }

  private int deployStellarBomb(final StarSystem target) {
    final int kills;
    if (target.hasDefensiveNet && this.gameOptions.destructibleDefenceNets) {
      kills = 0;
      target.hasDefensiveNet = false;
    } else {
      kills = (target.garrison + 1) / 2;
      target.garrison -= kills;
    }

    for (final MoveFleetsOrder order : target.outgoingOrders) {
      order.target.incomingOrders.remove(order);
      this.moveOrders.remove(order);
    }
    target.outgoingOrders.clear();

    return kills;
  }

  public boolean isStellarBombTarget(final Player player, final StarSystem system) {
    return this.projectOrders.stream().anyMatch(order ->
        order.player == player && order.type == ResourceType.ENERGY && order.target == system);
  }

  public void recalculateFleetProduction() {
    for (final Player player : this.players) {
      this.recalculateFleetProduction(player);
    }
  }

  private void recalculateFleetProduction(final Player player) {
    if (this.gameOptions.unifiedTerritories) {
      recalculateFleetsRemaining(player.combinedForce);
      recalculateFleetProduction(player.combinedForce);
    } else {
      for (final ContiguousForce force : player.contiguousForces) {
        recalculateFleetsRemaining(force);
        recalculateFleetProduction(force);
      }
    }
  }

  public int checksum() {
    int var2 = 305494461;
    var2 ^= this.resignedPlayersBitmap << 11;
    var2 ^= this.defeatedPlayersBitmap * 5 << 7;
    var2 ^= this.playersOfferingDrawBitmap * 13 << 19;
    final Player[] var3 = this.players;

    JagexApplet.printDebug("CHECKSUM 1: " + var2);

    int var4;
    int var6;
    for (var4 = 0; var3.length > var4; ++var4) {
      final Player var5 = var3[var4];
      JagexApplet.printDebug("CHECKSUM |  " + var2 + "  |  " + var5 + ": " + Arrays.toString(var5.researchPoints) + ", " + Arrays.toString(var5.pactTurnsRemaining));
      var2 += var5.researchPoints[0] << var5.index;
      var2 += var5.researchPoints[1] << var5.index + 3;
      var2 += var5.researchPoints[2] << var5.index + 7;
      var2 += var5.researchPoints[3] << var5.index + 19;

      for (var6 = 0; var6 < this.players.length; ++var6) {
        var2 += var5.pactTurnsRemaining[var6] << (var6 * 5 + 7 * var5.index) % 11;
      }
    }

    JagexApplet.printDebug("CHECKSUM 2: " + var2);

    final StarSystem[] var9 = this.map.systems;

    for (var4 = 0; var9.length > var4; ++var4) {
      final StarSystem var11 = var9[var4];
      JagexApplet.printDebug("CHECKSUM |  " + var2 + "  |  " + var11 + ": "
          + var11.garrison + ", " + Arrays.toString(var11.neighbors) + ", " + Arrays.toString(var11.resources));
      var6 = 1 + var11.index;
      int var7 = !var11.hasDefensiveNet ? 0 : 79;

      int var8;
      for (var8 = 0; var11.neighbors.length > var8; ++var8) {
        var7 += 23 * (var8 + 1) * var11.neighbors[var8].index;
      }

      for (var8 = 0; var8 < 4; ++var8) {
        var7 += var11.resources[var8] * (13 << var8 * 3);
      }

      var7 += 37 * var11.garrison;
      var2 += 17 * var6 * var7;
    }

    JagexApplet.printDebug("CHECKSUM 3: " + var2);

    for (final TannhauserLink link : this.tannhauserLinks) {
      var4 = (link.system2.index + 17) * (13 + link.system1.index) ^ link.turnsLeft * 31;
      var2 += 23 * var4;
    }

    var2 ^= 47 * this.turnName2Index;
    var2 ^= 61 * this.turnName1Index;

    JagexApplet.printDebug("CHECKSUM 4: " + var2);
    return var2;
  }

  public void validateOrders(final Player player, final Collection<ProjectOrder> projectOrders, final Collection<BuildFleetsOrder> buildOrders, final Collection<MoveFleetsOrder> moveOrders) {
    for (final Iterator<BuildFleetsOrder> it = buildOrders.iterator(); it.hasNext(); ) {
      final BuildFleetsOrder order = it.next();
      boolean ok = true;
      if (order.system.owner == player) {
        final Force force = order.getForce(this.gameOptions);
        force.fleetsAvailableToBuild -= order.quantity;
        if (force.fleetsAvailableToBuild < 0) {
          ok = false;
        }
      } else {
        ok = false;
      }

      if (!ok) {
        it.remove();
      }
    }
    this.recalculateFleetProduction(player);

    final int[] garrisons = Arrays.stream(this.map.systems).mapToInt(starSystem -> starSystem.garrison).toArray();
    for (final BuildFleetsOrder order : buildOrders) {
      garrisons[order.system.index] += order.quantity;
    }

    for (final Iterator<ProjectOrder> it = projectOrders.iterator(); it.hasNext(); ) {
      final ProjectOrder order = it.next();
      boolean ok = true;
      if (!this.gameOptions.projectsAllowed) {
        ok = false;
      } else if (order.player != player) {
        ok = false;
      } else if (order.target == null) {
        ok = false;
      } else if (player.researchPoints[order.type] < 5) {
        ok = false;
      } else if (order.type == ResourceType.METAL) {
        if (order.target.owner != player) {
          ok = false;
        }
      } else if (order.type == ResourceType.BIOMASS) {
        if (order.target.owner != player) {
          ok = false;
        }
        if (order.target.score != StarSystem.Score.NORMAL) {
          ok = false;
        }
      } else if (order.type == ResourceType.ENERGY) {
        final boolean inRange = player == order.target.owner
            || Arrays.stream(order.target.neighbors).anyMatch(neighbor -> neighbor.owner == player);
        if (!inRange) {
          ok = false;
        }
      } else if (order.type == ResourceType.EXOTICS) {
        if (order.source == null) {
          ok = false;
        } else if (order.target.owner == player) {
          if (order.target == order.source) {
            ok = false;
          }
        } else {
          ok = false;
        }
      }

      if (!ok) {
        it.remove();
      }
    }

    for (final Iterator<MoveFleetsOrder> iterator = moveOrders.iterator(); iterator.hasNext(); ) {
      final MoveFleetsOrder order = iterator.next();
      boolean ok = true;
      if (order.source != null && order.target != null && order.source.owner == player) {
        garrisons[order.source.index] -= order.quantity;
        final int minGarrison = this.gameOptions.garrisonsCanBeRemoved ? 0 : order.source.minimumGarrison;
        if (order.quantity <= 0) {
          ok = false;
        } else if (minGarrison > garrisons[order.source.index]) {
          ok = false;
        } else if (order.source.contiguousForce == order.target.contiguousForce) {
          if (!this.movementInRange(order.source, order.target)) {
            ok = false;
          }
        } else if (!order.source.hasNeighbor(order.target)) {
          ok = false;
        } else if (order.target.owner != null && order.source.owner.allies[order.target.owner.index]) {
          ok = false;
        }
      } else {
        ok = false;
      }

      if (!ok) {
        iterator.remove();
      }
    }
  }

  private void checkDefeat() {
    for (int i = 0; i < this.playerCount; ++i) {
      if (!this.isPlayerDefeated(i) && this.players[i].contiguousForces.isEmpty()) {
        this.markPlayerDefeated(i);
      }
    }
  }

  /**
   * Finds any systems that are no longer connected to the given force’s
   * capital (see {@link ContiguousForce#getCapital()}) and splits them off
   * into new, independent forces.
   */
  private void splitForceIfNecessary(final ContiguousForce force) {
    final Set<StarSystem> systemsLeft = force.stream().collect(Collectors.toCollection(HashSet::new));
    final List<StarSystem> systemsToKeep = new ArrayList<>();
    final Queue<StarSystem> connected = new ArrayDeque<>();

    // first, find all the systems that are still connected to the capital
    final StarSystem capital = force.getCapital();
    capital.contiguousForce = null;
    for (StarSystem system = capital; system != null; system = connected.poll()) {
      if (system.neighbors != null) {
        for (final StarSystem neighbor : system.neighbors) {
          if (neighbor.contiguousForce == force) {
            // temporarily set the system's force to `null` so we don't infinitely loop
            neighbor.contiguousForce = null;
            connected.add(neighbor);
          }
        }
      }
      systemsLeft.remove(system);
      systemsToKeep.add(system);
    }

    // next, split off systems we aren’t keeping into their own forces
    while (!systemsLeft.isEmpty()) {
      final StarSystem newCapital = systemsLeft.iterator().next();
      assert newCapital.contiguousForce == force;

      // create a new force for the disconnected systems
      final ContiguousForce disconnectedForce = new ContiguousForce(force.player, newCapital);
      newCapital.contiguousForce = disconnectedForce;

      // add all the systems connected to the new capital
      for (StarSystem system = newCapital; system != null; system = connected.poll()) {
        if (system.neighbors != null) {
          for (final StarSystem neighbor : system.neighbors) {
            if (neighbor.contiguousForce == force) {
              neighbor.contiguousForce = disconnectedForce;
              connected.add(neighbor);
            }
          }
        }
        systemsLeft.remove(system);
        disconnectedForce.add(system);
      }

      recalculateFleetProduction(disconnectedForce);
      force.player.contiguousForces.add(disconnectedForce);
    }

    // reassign kept systems to the original force
    for (final StarSystem system : systemsToKeep) {
      system.contiguousForce = force;
    }
    force.setSystems(systemsToKeep);

    for (final StarSystem system : force.player.combinedForce) {
      assert system.owner == force.player;
    }
    recalculateFleetProduction(force.player.combinedForce);
  }

  /**
   * Detaches the given system from its containing force, if any.
   */
  private void detachSystemFromForce(final StarSystem system) {
    final ContiguousForce force = system.contiguousForce;
    if (force != null) {
      system.contiguousForce = null;
      force.remove(system);
      force.player.combinedForce.remove(system);

      if (system == force.player.combinedForce.getCapital()) {
        force.player.combinedForce.setCapital(force.player.combinedForce.getFirst().orElse(null));
      }

      if (force.getCapital() == system) {
        force.getFirst().ifPresentOrElse(force::setCapital, () -> force.player.contiguousForces.remove(force));
      }
      if (!force.isEmpty()) {
        this.splitForceIfNecessary(force);
      }
    }
  }

  private void processCollapsesAndRetreats(final TurnEventLog turnLog, final int seed) {
    final boolean[] systemsCollapsed = new boolean[this.map.systems.length];
    boolean anySystemsCollapsed;
    do {
      anySystemsCollapsed = false;
      for (final StarSystem system : this.map.systems) {
        if (system.owner != null && !systemsCollapsed[system.index] && system.garrison < system.minimumGarrison) {
          systemsCollapsed[system.index] = true;
          anySystemsCollapsed = true;

          for (final StarSystem neighbor : system.neighbors) {
            if (system.owner == neighbor.owner && !systemsCollapsed[neighbor.index]) {
              this.recalculateMinimumGarrison(neighbor, systemsCollapsed);
            }
          }
        }
      }
    } while (anySystemsCollapsed && !this.gameOptions.noChainCollapsing);

    StarSystem[] retreatTargets = new StarSystem[16];
    for (final StarSystem system : this.map.systems) {
      if (system.retreatingFleets > 0) {
        int retreatTargetCount = 0;
        for (final StarSystem neighbor : system.neighbors) {
          if (neighbor.owner == system.lastOwner && !systemsCollapsed[neighbor.index]) {
            retreatTargets[retreatTargetCount++] = neighbor;
          }
        }

        if (retreatTargetCount != 0) {
          this.random.setSeed(seed ^ system.index);
          for (int i = 0; i < system.retreatingFleets; ++i) {
            ++retreatTargets[ShatteredPlansClient.randomIntBounded(this.random, retreatTargetCount)].garrison;
          }
        }
      }

      if (system.retreatedFleets > 0
          && system.owner == system.lastOwner
          && !systemsCollapsed[system.index]) {
        system.garrison += system.retreatedFleets;
      }

      if (system.owner != null) {
        system.lastOwner = system.owner;
      }
    }

    for (final StarSystem system : this.map.systems) {
      if (retreatTargets.length < system.neighbors.length) {
        retreatTargets = new StarSystem[system.neighbors.length];
      }

      if (system.owner != null && systemsCollapsed[system.index]) {
        int retreatTargetCount = 0;
        for (final StarSystem neighbors : system.neighbors) {
          if (system.owner == neighbors.owner && !systemsCollapsed[neighbors.index]) {
            retreatTargets[retreatTargetCount++] = neighbors;
          }
        }

        if (retreatTargetCount == 0) {
          if (turnLog != null) {
            turnLog.addFleetRetreatEvent(system);
          }
        } else {
          this.random.setSeed(seed ^ system.index);
          if (turnLog == null) {
            for (int var18 = 0; var18 < system.garrison; ++var18) {
              ++retreatTargets[ShatteredPlansClient.randomIntBounded(this.random, retreatTargetCount)].garrison;
            }
          } else {
            final int[] retreatedFleets = new int[retreatTargetCount];
            for (int i = 0; i < system.garrison; ++i) {
              final int retreatTarget = ShatteredPlansClient.randomIntBounded(this.random, retreatTargetCount);
              retreatedFleets[retreatTarget]++;
              retreatTargets[retreatTarget].garrison++;
            }

            turnLog.addFleetRetreatEvent(system, retreatTargets, retreatedFleets);
          }
        }

        this.disownSystem(system);
        if (system.contiguousForce != null) {
          throw new RuntimeException();
        }
      }
    }
  }

  public void setTurnNumber(final int turnNumber) {
    this.turnNumber = turnNumber;
  }

  private void calculateCombatRetreats(final StarSystem system, final CombatEngagementLog engagementLog) {
    if (system.owner != null) {
      final int fleetsAtStart = this.playerFleetsAtCombatStart[system.owner.index];
      final int fleetsRemaining = this.combatFleetsRemaining[system.owner.index];
      final int fleetsDefeated = fleetsAtStart - fleetsRemaining;
      if (fleetsRemaining <= 0) {
        if (engagementLog != null) {
          engagementLog.a326(fleetsAtStart, fleetsDefeated);
        }
        system.retreatingFleets = fleetsAtStart / 2;
      } else {
        final int retreatedFleets = fleetsDefeated / 2;
        if (engagementLog != null) {
          engagementLog.a115(fleetsAtStart, fleetsDefeated, retreatedFleets);
        }
        system.retreatedFleets += retreatedFleets;
      }
    }

    for (final MoveFleetsOrder order : system.incomingOrders) {
      if (order.player != system.owner) {
        final int fleetsAtStart = this.playerFleetsAtCombatStart[order.player.index];
        final int fleetsRemaining = this.combatFleetsRemaining[order.player.index];
        final int totalFleetsDefeated = fleetsAtStart - fleetsRemaining;
        final int orderFleetsDefeated = order.quantity * totalFleetsDefeated / fleetsAtStart;
        final int retreatedFleets = orderFleetsDefeated / 2;
        final int destroyedFleets = orderFleetsDefeated - retreatedFleets;
        if (engagementLog != null) {
          engagementLog.a631(order, destroyedFleets, retreatedFleets);
        }
        order.source.retreatedFleets += retreatedFleets;
      }
    }
  }

  public boolean anyPlayersDefeated() {
    return this.defeatedPlayersBitmap != 0;
  }
  public boolean isPlayerDefeated(final int index) {
    return (this.defeatedPlayersBitmap & (1 << index)) != 0;
  }
  public void markPlayerDefeated(final int index) {
    this.defeatedPlayersBitmap |= 1 << index;
  }

  public int getResignedPlayersBitmap() {
    return this.resignedPlayersBitmap;
  }
  public boolean didPlayerResign(final int index) {
    return (this.resignedPlayersBitmap & (1 << index)) != 0;
  }
  public void receiveResignation(final int index) {
    this.resignedPlayersBitmap |= 1 << index;
    this.markPlayerDefeated(index);
    this.eliminatePlayer(this.players[index]);
  }

  public void receiveResignedPlayersBitmap(final int resignedPlayersBitmap) {
    this.resignedPlayersBitmap = resignedPlayersBitmap;
    this.defeatedPlayersBitmap |= resignedPlayersBitmap;
    for (int i = 0; i < this.playerCount; ++i) {
      if (this.didPlayerResign(i)) {
        this.eliminatePlayer(this.players[i]);
      }
    }
  }

  public boolean isAnyoneOfferingDraw() {
    return this.playersOfferingDrawBitmap != 0;
  }
  public boolean isPlayerOfferingDraw(final int index) {
    return (this.playersOfferingDrawBitmap & (1 << index)) != 0;
  }
  public void receivePlayersOfferingDrawBitmap(final int playersOfferingDrawBitmap) {
    this.playersOfferingDrawBitmap = playersOfferingDrawBitmap;
  }

  public boolean isAnyoneOfferingRematch() {
    return this.playersOfferingRematchBitmap != 0;
  }
  public boolean isPlayerOfferingRematch(final int index) {
    return (this.playersOfferingRematchBitmap & (1 << index)) != 0;
  }
  public void receivePlayersOfferingRematchBitmap(final int playersOfferingRematchBitmap) {
    this.playersOfferingRematchBitmap = playersOfferingRematchBitmap;
  }
  public void setPlayerOfferingRematch(final int playerIndex) {
    this.playersOfferingRematchBitmap |= 1 << playerIndex;
  }

  public int getTurnDurationTicks() {
    return GameSession.TURN_DURATIONS[this.turnLengthIndex];
  }

  public int getTurnDurationMillis() {
    return this.getTurnDurationTicks() * MILLIS_PER_TICK;
  }

  public int ordersChecksum() {
    return TurnOrders.checksum(this.projectOrders, this.buildOrders, this.moveOrders);
  }

  public enum GalaxySize {
    HUGE(32, 800),
    LARGE(24, 400),
    MEDIUM(16, 200),
    SMALL(12, 120),
    TINY(10, 80);

    @SuppressWarnings("WeakerAccess")
    public final int systemsPerPlayer;

    /**
     * The number of points needed to win in the Points game mode.
     */
    public final int targetPoints;

    GalaxySize(final int systemsPerPlayer, final int targetPoints) {
      this.systemsPerPlayer = systemsPerPlayer;
      this.targetPoints = targetPoints;
    }

    private static Optional<GalaxySize> lookup(final int systemsPerPlayer) {
      return switch (systemsPerPlayer) {
        case 32 -> Optional.of(HUGE);
        case 24 -> Optional.of(LARGE);
        case 16 -> Optional.of(MEDIUM);
        case 12 -> Optional.of(SMALL);
        case 10 -> Optional.of(TINY);
        default -> Optional.empty();
      };
    }
  }

  public static final class ResourceType {
    public static final int METAL = 0;
    public static final int BIOMASS = 1;
    public static final int ENERGY = 2;
    public static final int EXOTICS = 3;
  }

  public enum GameType {
    CONQUEST,
    CAPTURE_AND_HOLD,
    POINTS,
    DERELICTS,
    TUTORIAL;

    public @NotNull GameType next() {
      return switch (this) {
        case CONQUEST         -> CAPTURE_AND_HOLD;
        case CAPTURE_AND_HOLD -> POINTS;
        case POINTS           -> DERELICTS;
        case DERELICTS        -> CONQUEST;
        case TUTORIAL         -> throw new IllegalArgumentException();
      };
    }

    public int encode() {
      return switch (this) {
        case CONQUEST         -> 0;
        case CAPTURE_AND_HOLD -> 1;
        case POINTS           -> 2;
        case DERELICTS        -> 3;
        case TUTORIAL         -> throw new IllegalArgumentException("no encoding for the TUTORIAL game type");
      };
    }

    public static @NotNull GameType decode(final int val) {
      return switch (val) {
        case 0  -> CONQUEST;
        case 1  -> CAPTURE_AND_HOLD;
        case 2  -> POINTS;
        case 3  -> DERELICTS;
        default -> throw new ProtocolException("unknown game type: 0x" + Integer.toHexString(val));
      };
    }
  }
}
