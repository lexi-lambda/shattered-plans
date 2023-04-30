package funorb.shatteredplans.game.ai;

import funorb.shatteredplans.StringConstants;
import funorb.shatteredplans.client.ShatteredPlansClient;
import funorb.shatteredplans.game.BuildFleetsOrder;
import funorb.shatteredplans.game.CombinedForce;
import funorb.shatteredplans.game.ContiguousForce;
import funorb.shatteredplans.game.GameSession;
import funorb.shatteredplans.game.GameState;
import funorb.shatteredplans.game.MoveFleetsOrder;
import funorb.shatteredplans.game.Player;
import funorb.shatteredplans.game.ProjectOrder;
import funorb.shatteredplans.game.TurnOrders;
import funorb.shatteredplans.map.CaptureAndHoldVictoryChecker;
import funorb.shatteredplans.map.DerelictsVictoryChecker;
import funorb.shatteredplans.map.StarSystem;
import funorb.util.ArrayUtil;
import org.intellij.lang.annotations.MagicConstant;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.stream.IntStream;

import static funorb.shatteredplans.game.ai.TaskAI.HostilityScore.*;

public final class TaskAI implements AI {
  private static final List<ProjectOrder> projectOrders = new ArrayList<>();
  private static final List<BuildFleetsOrder> buildOrders = new ArrayList<>();
  private static final List<MoveFleetsOrder> moveOrders = new ArrayList<>();
  private static boolean[] systemCloserToMeThanAllies;
  private static StarSystem stellarBombTarget;
  private static boolean _nob;
  private static int[] fleetsNeededToHoldSystem;
  private static int[] systemFleetsAvailableToMove;
  private static boolean[] systemBorderedByHostile;
  private static StarSystem _qoe;
  private static int _ahT = 0;
  private static boolean deployedStellarBomb;
  private static int[][] systemDistancesFromEmpires;
  private final Player player;
  private final int systemCount;
  private final int peacefulness;
  private final List<HoldTask> holdTasks = new ArrayList<>();
  private final GameState gameState;
  private final int aggressiveness;
  private final GameSession gameSession;
  private final List<DefenseTask> defenseTasks = new ArrayList<>();
  private final List<CaptureTask> captureTasks = new ArrayList<>();
  private final int[] _f;
  private final int[] hostilityScores;
  private int desiredAlly;
  private AITask[] combatTasks;
  private int[] forceFleetsAvailableToMove;
  private int[] forceFleetsAvailableToBuild;
  private final boolean isDerelicts;
  private boolean isActive;
  private int[][] forceWantedResources;
  private final boolean isCaptureAndHold;
  private boolean[] cannotHoldForce;
  private int currentObjectiveLeader;

  public TaskAI(final Player player, final GameState gameState, final GameSession gameSession) {
    this.gameSession = gameSession;
    this.gameState = gameState;
    this.player = player;
    this.systemCount = this.gameState.map.systems.length;
    a556sf(this.systemCount);
    this.isActive = true;
    this.peacefulness = ShatteredPlansClient.randomIntBounded(75) + 25;
    this.aggressiveness = ShatteredPlansClient.randomIntBounded(75) + 25;
    this.hostilityScores = new int[this.gameState.playerCount];
    this._f = new int[this.gameState.playerCount];
    this.isCaptureAndHold = this.gameState.victoryChecker instanceof CaptureAndHoldVictoryChecker;
    this.isDerelicts = this.gameState.victoryChecker instanceof DerelictsVictoryChecker;
  }

  private static void a325nm(final int[] var0, final int[] var1) {
    a815eo(var0, var1, var1.length - 1, 0);
  }

  private static void a815eo(final int[] var0, final int[] var1, final int var2, final int var3) {
    if (var2 > var3) {
      final int var4 = (var3 + var2) / 2;
      int var5 = var3;
      final int var6 = var1[var4];
      var1[var4] = var1[var2];
      var1[var2] = var6;
      final int var7 = var0[var4];
      var0[var4] = var0[var2];
      var0[var2] = var7;
      final int var8 = var6 == Integer.MAX_VALUE ? 0 : 1;

      for (int var9 = var3; var9 < var2; ++var9) {
        if (var1[var9] < var6 + (var8 & var9)) {
          final int var10 = var1[var9];
          var1[var9] = var1[var5];
          var1[var5] = var10;
          final int var11 = var0[var9];
          var0[var9] = var0[var5];
          var0[var5++] = var11;
        }
      }

      var1[var2] = var1[var5];
      var1[var5] = var6;
      var0[var2] = var0[var5];
      var0[var5] = var7;
      a815eo(var0, var1, var5 - 1, var3);
      a815eo(var0, var1, var2, var5 + 1);
    }
  }

  private static void a556sf(final int var0) {
    if (var0 >= _ahT) {
      _ahT = var0;
      systemBorderedByHostile = new boolean[var0];
      fleetsNeededToHoldSystem = new int[var0];
      systemFleetsAvailableToMove = new int[var0];
    }
  }

  private void satisfyHoldTasks1() {
    for (final HoldTask task : this.holdTasks) {
      int forceIndex = 0;
      ContiguousForce force = null;
      for (final ContiguousForce force1 : this.player.contiguousForces) {
        if (force1 == task.system.contiguousForce) {
          force = force1;
          break;
        }
        ++forceIndex;
      }
      if (force == null) {
        throw new RuntimeException("Star to be garrisoned (" + task.system.name + ") doesn't appear to be in any of the AI's territories.");
      }

      if (!this.cannotHoldForce[forceIndex]) {
        int netFleetCost = task.fleetCost - task.system.garrison;
        if (netFleetCost <= 0) {
          systemFleetsAvailableToMove[task.system.index] = task.system.garrison - task.fleetCost;
          this.forceFleetsAvailableToMove[forceIndex] -= task.fleetCost - task.system.minimumGarrison;
          if (this.forceFleetsAvailableToMove[forceIndex] < 0) {
            this.moveAllFleetsToMostValuableSystem(force, forceIndex);
          }
        } else {
          systemFleetsAvailableToMove[task.system.index] = 0;
          if (task.system.garrison > task.system.minimumGarrison) {
            this.forceFleetsAvailableToMove[forceIndex] -= task.system.garrison - task.system.minimumGarrison;
            if (this.forceFleetsAvailableToMove[forceIndex] < 0) {
              this.moveAllFleetsToMostValuableSystem(force, forceIndex);
              continue;
            }
          }

          if (netFleetCost <= this.forceFleetsAvailableToBuild[forceIndex]) {
            if (this.gameState.gameOptions.unifiedTerritories) {
              for (int i = 0; i < this.forceFleetsAvailableToBuild.length; ++i) {
                this.forceFleetsAvailableToBuild[i] -= netFleetCost;
              }
            } else {
              this.forceFleetsAvailableToBuild[forceIndex] -= netFleetCost;
            }
            task.system.remainingGarrison += netFleetCost;
            buildOrders.add(new BuildFleetsOrder(task.system, netFleetCost));
          } else {
            final int fleetsAvailableToBuild = this.forceFleetsAvailableToBuild[forceIndex];
            if (fleetsAvailableToBuild > 0) {
              task.system.remainingGarrison += fleetsAvailableToBuild;
              netFleetCost -= fleetsAvailableToBuild;
              buildOrders.add(new BuildFleetsOrder(task.system, fleetsAvailableToBuild));
              if (this.gameState.gameOptions.unifiedTerritories) {
                Arrays.fill(this.forceFleetsAvailableToBuild, 0);
              } else {
                this.forceFleetsAvailableToBuild[forceIndex] = 0;
              }
            }

            if (netFleetCost > this.forceFleetsAvailableToMove[forceIndex]) {
              this.moveAllFleetsToMostValuableSystem(force, forceIndex);
            } else {
              this.forceFleetsAvailableToMove[forceIndex] -= netFleetCost;
              fleetsNeededToHoldSystem[task.system.index] += netFleetCost;
            }
          }
        }
      }
    }
  }

  private void calculateAvailableFleets() {
    this.forceFleetsAvailableToMove = new int[this.player.contiguousForces.size()];
    this.forceFleetsAvailableToBuild = new int[this.player.contiguousForces.size()];
    this.cannotHoldForce = new boolean[this.player.contiguousForces.size()];

    for (final StarSystem system : this.gameState.map.systems) {
      if (system.owner == this.player && system.garrison > system.minimumGarrison) {
        systemFleetsAvailableToMove[system.index] = system.garrison - system.minimumGarrison;
        if (system.remainingGarrison < systemFleetsAvailableToMove[system.index]) {
          throw new RuntimeException(system.name + ": available=" + systemFleetsAvailableToMove[system.index] + ", garrison_next=" + system.remainingGarrison);
        }

        final Iterator<ContiguousForce> it = this.player.contiguousForces.iterator();
        boolean ok = false;
        for (int i = 0; i < this.forceFleetsAvailableToMove.length; ++i) {
          final ContiguousForce force = it.next();
          if (system.contiguousForce == force) {
            ok = true;
            this.forceFleetsAvailableToMove[i] += systemFleetsAvailableToMove[system.index];
            break;
          }
        }

        if (!ok) {
          throw new RuntimeException("Star owned by " + this.player.name + " is not in any of its Territories: " + system.name);
        }
      } else {
        systemFleetsAvailableToMove[system.index] = 0;
      }
    }

    if (this.gameState.gameOptions.unifiedTerritories) {
      Arrays.fill(this.forceFleetsAvailableToBuild, this.player.combinedForce.fleetsAvailableToBuild);
    } else {
      final Iterator<ContiguousForce> it = this.player.contiguousForces.iterator();
      for (int i = 0; i < this.forceFleetsAvailableToBuild.length; ++i) {
        final ContiguousForce force = it.next();
        this.forceFleetsAvailableToBuild[i] = force.fleetsAvailableToBuild;
      }
    }
  }

  private void satisfyDefenseTasks() {
    for (final AITask task : this.combatTasks) {
      if (task instanceof DefenseTask) {
        final int i = task.system.index;
        if (fleetsNeededToHoldSystem[i] >= systemFleetsAvailableToMove[i]) {
          fleetsNeededToHoldSystem[i] -= systemFleetsAvailableToMove[i];
          systemFleetsAvailableToMove[i] = 0;
        } else {
          systemFleetsAvailableToMove[i] -= fleetsNeededToHoldSystem[i];
          fleetsNeededToHoldSystem[i] = 0;
        }
      }
    }

    outer:
    for (final AITask task : this.combatTasks) {
      if (task instanceof DefenseTask) {
        boolean moveFromThreatened = false;
        final int i = task.system.index;

        for (int j = 0; j < 2; ++j) {
          for (final StarSystem system : task.system.contiguousForce) {
            if (task.system != system && systemFleetsAvailableToMove[system.index] != 0 && (moveFromThreatened || !systemBorderedByHostile[system.index])) {
              if (fleetsNeededToHoldSystem[i] < systemFleetsAvailableToMove[system.index]) {
                this.moveFleets(system, task.system, fleetsNeededToHoldSystem[i]);
                systemFleetsAvailableToMove[system.index] -= fleetsNeededToHoldSystem[i];
                fleetsNeededToHoldSystem[i] = 0;
                continue outer;
              }

              this.moveFleets(system, task.system, systemFleetsAvailableToMove[system.index]);
              fleetsNeededToHoldSystem[i] -= systemFleetsAvailableToMove[system.index];
              systemFleetsAvailableToMove[system.index] = 0;
            }
          }

          moveFromThreatened = true;
        }

        if (fleetsNeededToHoldSystem[i] > 0) {
          throw new RuntimeException(this.player.name + " has run out of available fleets, but has not yet met its defender allocation at " + task.system.name + ".");
        }
      }
    }
  }

  private void initializeHoldTasks() {
    this.holdTasks.clear();
    for (final ContiguousForce force : this.player.contiguousForces) {
      for (final StarSystem system : force) {
        this.holdTasks.add(new HoldTask(system, this.gameState.gameOptions.simpleGarrisoning, systemBorderedByHostile));
      }
    }
  }

  @Override
  public Player getPlayer() {
    return this.player;
  }

  @Override
  public void planTurnOrders() {
    this.doPlanTurnOrders();
    this.gameState.addOrders(projectOrders, buildOrders, moveOrders);
  }

  public TurnOrders planAndGetTurnOrders() {
    this.doPlanTurnOrders();

    final TurnOrders orders = new TurnOrders();
    projectOrders.forEach(orders::add);
    buildOrders.forEach(orders::add);
    moveOrders.forEach(orders::add);
    return orders;
  }

  private void doPlanTurnOrders() {
    if (!this.player.contiguousForces.isEmpty()) {
      buildOrders.clear();
      moveOrders.clear();
      projectOrders.clear();
      stellarBombTarget = null;
      _nob = false;
      deployedStellarBomb = false;

      for (int i = 0; i < this.systemCount; ++i) {
        systemBorderedByHostile[i] = false;
        systemFleetsAvailableToMove[i] = 0;
        fleetsNeededToHoldSystem[i] = 0;
      }

      this.calculateAvailableFleets();
      this.calculateSystemsAtRisk();
      this.computeWantedResources();
      this.initializeHoldTasks();
      this.initializeDefenseTasks();
      this.initializeCaptureTasks();
      this.prioritizeCombatTasks();
      this.satisfyHoldTasks1();
      this.satisfyHoldTasks2();
      this.satisfyCombatTasks(true);
      int var2 = 0;

      for (int i = 0; i < this.forceFleetsAvailableToMove.length; ++i) {
        var2 += this.forceFleetsAvailableToMove[i];
        var2 += this.forceFleetsAvailableToBuild[i];
      }

      int var9 = var2;

      label91:
      do {
        while (var2 > 0) {
          this.satisfyCombatTasks(false);
          var2 = 0;

          for (int var4 = 0; var4 < this.forceFleetsAvailableToMove.length; ++var4) {
            var2 += this.forceFleetsAvailableToMove[var4];
            var2 += this.forceFleetsAvailableToBuild[var4];
          }

          if (var9 == var2) {
            boolean var10 = true;
            int var5 = 0;

            for (final ContiguousForce var6 : this.player.contiguousForces) {
              final int var7 = (int) this.defenseTasks.stream()
                  .filter(var8 -> var6 == var8.system.contiguousForce).count();
              if (var7 > 0) {
                var10 = false;
              } else {
                this.a372(var6, var5);
              }

              ++var5;
            }

            var2 = 0;
            if (var10 && this.gameState.gameOptions.unifiedTerritories) {
              this.c423();
            }

            for (int var11 = 0; this.forceFleetsAvailableToMove.length > var11; ++var11) {
              var2 += this.forceFleetsAvailableToMove[var11];
              var2 += this.forceFleetsAvailableToBuild[var11];
            }
            continue label91;
          }

          var9 = var2;
        }

        this.satisfyDefenseTasks();
        this.deployTerraforming();
        this.deployTannhauser();
        this.gameState.validateOrders(this.player, projectOrders, buildOrders, moveOrders);
        return;
      } while (var2 <= 0);
      throw new RuntimeException(this.player.name + " hasn't allocated any more idle fleets this loop, even though there are DefendTasks.");
    }
  }

  private void c423() {
    if (this.gameState.gameOptions.unifiedTerritories) {
      int var2 = 0;
      final StarSystem[] var3 = this.gameState.map.systems;

      int var4;
      for (var4 = 0; var3.length > var4; ++var4) {
        final StarSystem var5 = var3[var4];
        if (this.player == var5.owner) {
          if (Arrays.stream(var5.neighbors).anyMatch(var8 -> this.player != var8.owner)) {
            ++var2;
          }
        }
      }

      if (var2 == 0) {
        this.buildFleets(this.player.combinedForce.getCapital(), this.forceFleetsAvailableToBuild[0]);

        Arrays.fill(this.forceFleetsAvailableToBuild, 0);
      } else {
        final StarSystem[] ln_s = new StarSystem[var2];
        var4 = 0;
        StarSystem[] var12 = this.gameState.map.systems;

        int var14;
        StarSystem var15;
        for (var14 = 0; var14 < var12.length; ++var14) {
          var15 = var12[var14];
          if (this.player == var15.owner) {
            final StarSystem[] var16 = var15.neighbors;

            for (final StarSystem var10 : var16) {
              if (var10.owner != this.player) {
                ln_s[var4] = var15;
                ++var4;
                break;
              }
            }
          }
        }

        while (this.forceFleetsAvailableToBuild[0] > 0) {
          var12 = ln_s;

          for (var14 = 0; var14 < var12.length; ++var14) {
            var15 = var12[var14];
            this.buildFleets(var15, 1);
            if (this.forceFleetsAvailableToBuild[0] == 0) {
              break;
            }
          }
        }

        Arrays.fill(this.forceFleetsAvailableToBuild, 0);
      }

    }
  }

  private void satisfyDefenseTask(final DefenseTask task, final boolean var2) {
    int forceIndex = 0;
    boolean ok = false;
    for (final ContiguousForce force : this.player.contiguousForces) {
      if (force == task.system.contiguousForce) {
        ok = true;
        break;
      }
      ++forceIndex;
    }
    if (!ok) {
      throw new RuntimeException("DefendTask's star does not appear to be in AI's territory: " + task.system.name + ".");
    }

    int adjustedFleetCost = this.adjustFleetCost(task.fleetCost);
    if (var2) {
      if (deployedStellarBomb) {
        for (final StarSystem neighbor : task.system.neighbors) {
          if (neighbor == stellarBombTarget && neighbor.owner != null) {
            task.fleetCost -= neighbor.garrison;
            if (task.fleetCost < 0) {
              task.fleetCost = 0;
            }

            adjustedFleetCost = this.adjustFleetCost(task.fleetCost);
            break;
          }
        }
      }

      if (this.player.researchPoints[GameState.ResourceType.ENERGY] >= 5 && !deployedStellarBomb) {
        for (final StarSystem neighbor : task.system.neighbors) {
          if (neighbor.owner != null && this.player != neighbor.owner && (int) ((double) adjustedFleetCost * 0.8D) <= neighbor.garrison && neighbor.garrison > 10) {
            this.deployStellarBomb(neighbor);
            task.fleetCost -= neighbor.garrison;
            if (task.fleetCost < 0) {
              task.fleetCost = 0;
            }

            adjustedFleetCost = this.adjustFleetCost(task.fleetCost);
            break;
          }
        }
      }

      if (this.player.researchPoints[0] >= 5 && !_nob && !task.system.hasDefensiveNet && (adjustedFleetCost > this.forceFleetsAvailableToMove[forceIndex] || adjustedFleetCost > 20)) {
        this.a815(task.system);
        if (task.fleetCost > 1) {
          task.fleetCost /= 2;
          adjustedFleetCost = this.adjustFleetCost(task.fleetCost);
        }
      }
    }

    if (adjustedFleetCost == 0) {
      this.defenseTasks.remove(task);
      return;
    }

    if (!var2) {
      adjustedFleetCost = adjustedFleetCost * 3 / 10;
      if (adjustedFleetCost == 0) {
        adjustedFleetCost = 1;
      }
    }

    if (adjustedFleetCost > this.forceFleetsAvailableToMove[forceIndex]) {
      if (this.forceFleetsAvailableToMove[forceIndex] > 0) {
        adjustedFleetCost -= this.forceFleetsAvailableToMove[forceIndex];
        fleetsNeededToHoldSystem[task.system.index] += this.forceFleetsAvailableToMove[forceIndex];
        this.forceFleetsAvailableToMove[forceIndex] = 0;
      }

      final int var13;
      if (this.forceFleetsAvailableToBuild[forceIndex] < adjustedFleetCost) {
        var13 = this.forceFleetsAvailableToBuild[forceIndex];
        if (this.gameState.gameOptions.unifiedTerritories) {
          Arrays.fill(this.forceFleetsAvailableToBuild, 0);
        } else {
          this.forceFleetsAvailableToBuild[forceIndex] = 0;
        }
      } else {
        var13 = adjustedFleetCost;
        if (this.gameState.gameOptions.unifiedTerritories) {
          for (int i = 0; i < this.forceFleetsAvailableToBuild.length; ++i) {
            this.forceFleetsAvailableToBuild[i] -= adjustedFleetCost;
          }
        } else {
          this.forceFleetsAvailableToBuild[forceIndex] -= adjustedFleetCost;
        }
      }

      if (var13 > 0) {
        this.buildFleets(task.system, var13);
      }
    } else {
      this.forceFleetsAvailableToMove[forceIndex] -= adjustedFleetCost;
      fleetsNeededToHoldSystem[task.system.index] += adjustedFleetCost;
    }
  }

  private int adjustFleetCost(final int fleetCost) {
    return fleetCost + fleetCost * (50 - this.aggressiveness) / 250;
  }

  @Override
  public void handlePactOffer(final Player offerer) {
    if (this.isActive) {
      final int var3 = this.hostilityScores[offerer.index];

      @MagicConstant(valuesFromClass = StringConstants.AIMessage.class)
      final int messageIndex;
      if (var3 == HOSTILE_PRODUCTION_LEADER || var3 == HOSTILE_OBJECTIVE_LEADER) {
        messageIndex = StringConstants.AIMessage.REJECT_TOO_STRONG;
      } else if (var3 == HOSTILE_ENEMY_ALLY_1 || var3 == HOSTILE_ALLIED_WITH_OBJECTIVE_LEADER) {
        messageIndex = StringConstants.AIMessage.REJECT_ENEMY_ALLY;
      } else if (var3 == HOSTILE_NOT_THREAT) {
        messageIndex = StringConstants.AIMessage.REJECT_TOO_WEAK;
      } else if (var3 == HOSTILE_WANT_CAPTURE) {
        messageIndex = StringConstants.AIMessage.REJECT_WANT_CAPTURE;
      } else if (var3 == HOSTILE_CONFIDENT) {
        messageIndex = StringConstants.AIMessage.AGGRESSIVE_1;
      } else if (var3 == FRIENDLY_INTIMIDATED) {
        messageIndex = StringConstants.AIMessage.ACCEPT_HARMLESS;
      } else if (var3 == FRIENDLY_COMMON_ENEMY) {
        messageIndex = StringConstants.AIMessage.ACCEPT_COMMON_ENEMY;
      } else if (var3 == FRIENDLY_BOTH_STRONG) {
        final int var6 = this.gameState.playerFleetProductionRanks[this.gameState.playerCount - 2];
        final int var5 = this.gameState.playerFleetProductionRanks[this.gameState.playerCount - 1];
        assert this.player != null;
        if (this.player.index != var5 && offerer.index != var5 && 3 * this.gameState.playerFleetProduction[var6] < 2 * this.gameState.playerFleetProduction[var5]) {
          messageIndex = StringConstants.AIMessage.ACCEPT_COMMON_ENEMY;
        } else {
          messageIndex = StringConstants.AIMessage.ACCEPT_NEUTRAL;
        }
      } else {
        //noinspection MagicConstant
        messageIndex = -1;
      }
      //noinspection MagicConstant
      if (messageIndex != -1) {
        this.gameSession.showAIChatMessage(this.player, offerer, messageIndex, 0);
      }

      assert this.player != null;
      if (!this.player.allies[offerer.index] && var3 < 0) {
        this.gameSession.handleAIPactOffer(this.player, offerer);
        this.assessOtherPlayers();
      }
    }
  }

  private void prioritizeCombatTasks() {
    int combatTaskCount = 0;
    for (final DefenseTask task : this.defenseTasks) {
      task.adjustedPriority = (task.priority << 16) * (100 - this.peacefulness) / (task.fleetCost * 100);
      ++combatTaskCount;
    }
    for (final CaptureTask task : this.captureTasks) {
      task.adjustedPriority = (task.priority << 16) * this.peacefulness / (task.fleetCost * 100);
      ++combatTaskCount;
    }

    this.combatTasks = new AITask[combatTaskCount];
    final int[] combatTaskPriorities = new int[combatTaskCount];
    int i = 0;
    for (final DefenseTask task : this.defenseTasks) {
      this.combatTasks[i] = task;
      combatTaskPriorities[i] = task.adjustedPriority;
      ++i;
    }
    for (final CaptureTask task : this.captureTasks) {
      this.combatTasks[i] = task;
      combatTaskPriorities[i] = task.adjustedPriority;
      ++i;
    }

    ArrayUtil.sortScored(this.combatTasks, combatTaskPriorities);
  }

  private void computeWantedResources() {
    this.forceWantedResources = new int[this.player.contiguousForces.size()][4];

    if (this.gameState.gameOptions.unifiedTerritories) {
      for (int j = 0; j < 4; ++j) {
        final int var3 = 1 + this.player.combinedForce.surplusResources[this.player.combinedForce.surplusResourceRanks[0]] - this.player.combinedForce.surplusResources[j];
        for (int i = 0; i < this.forceWantedResources.length; ++i) {
          this.forceWantedResources[i][j] = var3;
        }
      }
    } else {
      final Iterator<ContiguousForce> it = this.player.contiguousForces.iterator();
      for (int i = 0; i < this.forceWantedResources.length; ++i) {
        final ContiguousForce force = it.next();
        for (int j = 0; j < GameState.NUM_RESOURCES; ++j) {
          this.forceWantedResources[i][j] = 1 + force.surplusResources[force.surplusResourceRanks[0]] - force.surplusResources[j];
        }
      }
    }
  }

  private void initializeCaptureTasks() {
    this.captureTasks.clear();
    for (final StarSystem system : this.gameState.map.systems) {
      if (system.owner != this.player && (system.owner == null || !this.player.allies[system.owner.index])) {
        if (Arrays.stream(system.neighbors).anyMatch(neighbor -> neighbor.owner == this.player)) {
          this.captureTasks.add(new CaptureTask(system, this.player.contiguousForces, this.forceWantedResources, this.player, this.gameState.victoryChecker, this.currentObjectiveLeader, this.gameState));
        }
      }
    }
  }

  private void satisfyCombatTasks(final boolean var1) {
    for (final AITask task : this.combatTasks) {
      if (task instanceof final DefenseTask defenseTask) {
        this.satisfyDefenseTask(defenseTask, var1);
      } else if (task instanceof final CaptureTask captureTask) {
        this.satisfyCaptureTask(captureTask, var1);
      } else {
        throw new RuntimeException("prioritizedTasks contains invalid task for Star " + task.system.name + ".");
      }
    }
  }

  @Override
  public int getType() {
    return Type.TASK;
  }

  private void moveFleets(final @NotNull StarSystem source, final @NotNull StarSystem target, int quantity) {
    if (quantity == 0) {
      return;
    }
    if (quantity < 0) {
      throw new RuntimeException();
    }
    if (this.player != source.owner) {
      throw new RuntimeException(this.player.name + " is attempting to move fleets from a system it does not own: " + source.name + ".");
    }
    if (target.contiguousForce != source.contiguousForce && !source.hasNeighbor(target)) {
      throw new RuntimeException(this.player.name + " is attempting an invalid move from " + source.name + " to " + target.name + ".");
    }
    
    final MoveFleetsOrder reverseOrder = source.incomingOrders.stream()
        .filter(order -> order.player == this.player && order.source == target).findFirst().orElse(null);
    if (reverseOrder != null) {
      if (reverseOrder.quantity > quantity) {
        reverseOrder.quantity -= quantity;
        reverseOrder.source.remainingGarrison += quantity;
        return;
      }

      reverseOrder.source.outgoingOrders.remove(reverseOrder);
      reverseOrder.target.incomingOrders.remove(reverseOrder);
      this.gameState.moveOrders.remove(reverseOrder);
      target.remainingGarrison += reverseOrder.quantity;
      quantity -= reverseOrder.quantity;
    }

    if (quantity == 0) {
      return;
    }
    if (source.remainingGarrison < quantity) {
      throw new RuntimeException("Trying to move " + quantity + " fleets from " + source.name + " to " + target.name + " but garrison_next is only " + source.remainingGarrison + ".");
    }
    source.remainingGarrison -= quantity;

    final int adjustedQuantity = quantity;
    moveOrders.stream()
        .filter(order -> order.player == this.player && order.source == source && order.target == target)
        .findFirst().ifPresentOrElse(
            order -> order.quantity += adjustedQuantity,
            () -> moveOrders.add(new MoveFleetsOrder(source, target, adjustedQuantity)));
  }

  private int a587(final boolean var1, final StarSystem[] var2, final StarSystem var3, final int var5) {
    int var6 = 0;
    int var11 = 0;

    while (true) {
      label87:
      {
        if (var11 < var2.length) {
          if (var2[var11].owner != this.player || systemFleetsAvailableToMove[var2[var11].index] == 0) {
            break label87;
          }

          final int var7;
          if (var1) {
            var7 = (int) Arrays.stream(var2[var11].neighbors)
                .filter(var14 -> this.player != var14.owner && (var14.owner == null || !this.player.allies[var14.owner.index]))
                .count();
          } else {
            var7 = 0;
          }

          if (var1 && var7 != 1) {
            break label87;
          }

          int var8 = systemFleetsAvailableToMove[var2[var11].index];
          if (var8 > var5) {
            var8 = var5;
          }

          boolean found = false;
          int var9 = 0;

          for (final ContiguousForce var10 : this.player.contiguousForces) {
            if (var10 == var2[var11].contiguousForce) {
              found = true;
              break;
            }
            ++var9;
          }

          if (!found) {
            throw new RuntimeException("Cannot find territory of star " + this.player.name + " wants to attack from: " + var2[var11].name + ".");
          }

          if (this.forceFleetsAvailableToMove[var9] < var8) {
            var8 = this.forceFleetsAvailableToMove[var9];
          }

          this.moveFleets(var2[var11], var3, var8);
          final int[] var10000 = systemFleetsAvailableToMove;
          final int var10001 = var2[var11].index;
          var10000[var10001] -= var8;
          var6 += var8;
          final int[] n = this.forceFleetsAvailableToMove;
          n[var9] -= var8;
          if (var6 != var5) {
            break label87;
          }
        }

        return var6;
      }

      ++var11;
    }
  }

  @Override
  public void handlePactAccepted(final Player ally) {
    final int var3 = this.hostilityScores[ally.index];
    @MagicConstant(valuesFromClass = StringConstants.AIMessage.class)
    int var4;
    if (var3 == -1) {
      var4 = StringConstants.AIMessage.ACCEPTED_CEASE_CONFLICT;
    } else if (var3 == -2) {
      var4 = StringConstants.AIMessage.ACCEPTED_COMMON_ENEMY;
    } else if (var3 == -3) {
      var4 = StringConstants.AIMessage.ACCEPTED_COORDINATE;
      final int var6 = this.gameState.playerFleetProductionRanks[this.gameState.playerCount - 2];
      final int var5 = this.gameState.playerFleetProductionRanks[this.gameState.playerCount - 1];
      if (var5 != this.player.index && var5 != this.desiredAlly && this.gameState.playerFleetProduction[var5] * 2 > this.gameState.playerFleetProduction[var6] * 3) {
        var4 = StringConstants.AIMessage.ACCEPTED_COMMON_ENEMY;
      }
    } else {
      var4 = StringConstants.AIMessage.NONE;
    }

    if (var4 != StringConstants.AIMessage.NONE) {
      this.gameSession.showAIChatMessage(this.player, ally, var4, 0);
    }
  }

  private void a815(final StarSystem var1) {
    if (this.player.researchPoints[0] < 5) {
      throw new RuntimeException(this.player.name + " is trying to deploy a Defensive Net it doesn't have at star " + var1.name);
    } else if (_nob) {
      final ProjectOrder var3 = projectOrders.stream()
          .filter(var4 -> var4.type == GameState.ResourceType.METAL)
          .findFirst().orElse(null);
      if (var3 == null) {
        throw new RuntimeException(this.player.name + "'s deployedDefensiveNet variable incorrectly set to true.");
      } else {
        throw new RuntimeException(this.player.name + " is trying to deploy a Defensive Net it has already used." + " Already deployed at star " + var3.target.name + ", now trying to deploy ot " + var1.name);
      }
    } else if (this.player != var1.owner) {
      throw new RuntimeException(this.player.name + " is trying to deploy a Defensive Net in a system it doesn't own: " + var1.name);
    } else if (var1.hasDefensiveNet) {
      throw new RuntimeException(this.player.name + " is trying to deploy a Defensive Net in a system that already has one: " + var1.name);
    } else {
      projectOrders.add(new ProjectOrder(GameState.ResourceType.METAL, this.player, var1));
      _nob = true;
      _qoe = var1;

    }
  }

  private int computeHostilityScore(final Player player, final int closerToMeThanAlliesCount) {
    final int i = player.index;
    if (this.player == player || this.player.allies[i]) {
      return NEUTRAL;
    } else if (this.gameState.isPlayerDefeated(i)) {
      return NEUTRAL;
    } else if (this.currentObjectiveLeader == i) {
      return HOSTILE_OBJECTIVE_LEADER;
    } else {
      final Player firstPlace = this.gameState.players[this.gameState.playerFleetProductionRanks[this.gameState.playerCount - 1]];
      final Player secondPlace = this.gameState.players[this.gameState.playerFleetProductionRanks[this.gameState.playerCount - 2]];
      if (this.currentObjectiveLeader == -1 && player == firstPlace) {
        return HOSTILE_PRODUCTION_LEADER;
      } else {
        int theirFleetsOnOurBorder = 0;
        int ourFleetsOnTheirBorder = 0;

        for (final StarSystem system : this.gameState.map.systems) {
          if (this.player == system.owner) {
            boolean bordersUs = false;

            for (final StarSystem neighbor : system.neighbors) {
              if (neighbor.owner == player) {
                bordersUs = true;
                theirFleetsOnOurBorder += neighbor.garrison;
              }
            }

            if (bordersUs) {
              ourFleetsOnTheirBorder += system.garrison;
            }
          }
        }

        theirFleetsOnOurBorder += theirFleetsOnOurBorder * (50 - this.peacefulness) / 250;
        if (theirFleetsOnOurBorder > ourFleetsOnTheirBorder * 2) {
          return FRIENDLY_INTIMIDATED;
        } else if (this.currentObjectiveLeader != -1 && player.allies[this.currentObjectiveLeader]) {
          return HOSTILE_ALLIED_WITH_OBJECTIVE_LEADER;
        } else if (this.currentObjectiveLeader == -1 && firstPlace.allies[i]) {
          return HOSTILE_ENEMY_ALLY_1;
        } else if (this.currentObjectiveLeader != -1 && firstPlace == player) {
          return this.currentObjectiveLeader == this.player.index ? FRIENDLY_BOTH_STRONG : FRIENDLY_COMMON_ENEMY;
        } else if (this.currentObjectiveLeader == -1 && secondPlace == player) {
          return firstPlace == this.player ? FRIENDLY_BOTH_STRONG : FRIENDLY_COMMON_ENEMY;
        } else {
          final int ourProduction = this.gameState.playerFleetProduction[this.player.index];
          final int theirProduction = this.gameState.playerFleetProduction[i];
          final int theirProductionAdjusted = theirProduction + theirProduction * (50 - this.peacefulness) / 250;
          if (ourFleetsOnTheirBorder > theirFleetsOnOurBorder && ourProduction * 2 > theirProductionAdjusted * 3) {
            return HOSTILE_NOT_THREAT;
          } else {
            final int[] systemDistancesFromMe = systemDistancesFromEmpires[this.player.index];
            int systemsNotCompetingFor = closerToMeThanAlliesCount;
            for (int j = 0; j < this.gameState.map.systems.length; ++j) {
              if (systemCloserToMeThanAllies[j] && systemDistancesFromEmpires[i][j] <= systemDistancesFromMe[j]) {
                --systemsNotCompetingFor;
              }
            }

            if (systemsNotCompetingFor < 5 && systemsNotCompetingFor < closerToMeThanAlliesCount) {
              return HOSTILE_WANT_CAPTURE;
            } else {
              return FRIENDLY_BOTH_STRONG;
            }
          }
        }
      }
    }
  }

  private void moveAllFleetsToMostValuableSystem(final ContiguousForce force, final int forceIndex) {
    StarSystem mostValuableSystem = null;
    int mostValuableSystemResources = 0;

    for (final StarSystem system : force) {
      fleetsNeededToHoldSystem[system.index] = 0;
      systemFleetsAvailableToMove[system.index] = 0;
      system.remainingGarrison = system.garrison;

      final int systemResources = Arrays.stream(system.resources).sum();
      if (mostValuableSystemResources < systemResources) {
        mostValuableSystem = system;
        mostValuableSystemResources = systemResources;
      }
    }
    assert mostValuableSystem != null;

    buildOrders.removeIf(order -> force == order.system.contiguousForce);
    for (final StarSystem system : force) {
      if (system != mostValuableSystem) {
        this.moveFleets(system, mostValuableSystem, system.garrison);
      }
    }

    this.buildFleets(mostValuableSystem, force.fleetsAvailableToBuild);
    this.forceFleetsAvailableToBuild[forceIndex] = 0;
    this.forceFleetsAvailableToMove[forceIndex] = 0;
    this.cannotHoldForce[forceIndex] = true;
  }

  private void buildFleets(final @NotNull StarSystem system, final int quantity) {
    if (quantity != 0) {
      system.remainingGarrison += quantity;
      for (final BuildFleetsOrder order : buildOrders) {
        if (order.system == system) {
          order.quantity += quantity;
          return;
        }
      }
      buildOrders.add(new BuildFleetsOrder(system, quantity));
    }
  }

  @Override
  public void makeDesiredPactOffers() {
    if (this.player.contiguousForces.isEmpty()) {
      this.isActive = false;
    }

    if (this.isActive && this.gameState.gameOptions.diplomacyAllowed) {
      this.desiredAlly = -1;

      final Player[] leaders = this.gameState.victoryChecker.currentObjectiveLeaders();
      if (leaders.length == 1) {
        this.currentObjectiveLeader = leaders[0].index;
      } else {
        this.currentObjectiveLeader = -1;
      }

      this.assessOtherPlayers();
      if (this.desiredAlly != -1) {
        final Player var5 = this.gameState.players[this.desiredAlly];
        if (this._f[this.desiredAlly] <= 0) {
          final int var9 = this.hostilityScores[this.desiredAlly];
          @MagicConstant(valuesFromClass = StringConstants.AIMessage.class)
          int message;
          if (var9 == -1) {
            message = StringConstants.AIMessage.ACCEPT_INTIMIDATED;
          } else if (var9 == -2) {
            message = StringConstants.AIMessage.REQUEST_COMMON_ENEMY;
          } else if (var9 == -3) {
            message = StringConstants.AIMessage.REQUEST_LEADERS;
            final int firstPlace = this.gameState.playerFleetProductionRanks[this.gameState.playerCount - 1];
            final int secondPlace = this.gameState.playerFleetProductionRanks[this.gameState.playerCount - 2];
            if (firstPlace != this.player.index && this.desiredAlly != firstPlace
                && this.gameState.playerFleetProduction[secondPlace] * 3 < this.gameState.playerFleetProduction[firstPlace] * 2) {
              message = StringConstants.AIMessage.REQUEST_COMMON_ENEMY;
            }
          } else {
            message = StringConstants.AIMessage.NONE;
          }

          if (message != StringConstants.AIMessage.NONE) {
            this.gameSession.showAIChatMessage(this.player, var5, message, 0);
            this._f[this.desiredAlly] = ShatteredPlansClient.randomIntBounded(3, 6);
          }
        }

        this.gameSession.handleAIPactOffer(this.player, var5);
      }
    }
  }

  private int[] systemDistancesFromEmpire(final Player player) {
    final int[] distances = new int[this.systemCount];
    final boolean[] checkNeighbors = new boolean[this.systemCount];

    for (int i = 0; i < this.systemCount; ++i) {
      final boolean owned = this.gameState.map.systems[i].owner == player;
      checkNeighbors[i] = owned;
      distances[i] = owned ? 0 : Integer.MAX_VALUE;
    }

    boolean again = true;
    while (again) {
      again = false;

      for (int i = 0; i < this.systemCount; ++i) {
        if (checkNeighbors[i]) {
          checkNeighbors[i] = false;
          final StarSystem system = this.gameState.map.systems[i];
          final int distance = 1 + distances[i];

          for (final StarSystem neighbor : system.neighbors) {
            final int j = neighbor.index;
            if (distance < distances[j]) {
              checkNeighbors[j] = true;
              distances[j] = distance;
              again = true;
            }
          }
        }
      }
    }

    return distances;
  }

  @Override
  public void initialize() {
    final Player[] var3 = this.gameState.players;

    for (final Player var5 : var3) {
      if (this.player != var5) {
        @MagicConstant(valuesFromClass = StringConstants.AIMessage.class)
        final int var6 = StringConstants.AIMessage.AGGRESSIVE_2 + ShatteredPlansClient.randomIntBounded(2);
        this.gameSession.showAIChatMessage(this.player, var5, var6, 0);
      }
    }

  }

  private void deployTerraforming() {
    if (this.player.researchPoints[GameState.ResourceType.BIOMASS] >= 5) {
      StarSystem var2 = null;
      int var3 = Integer.MAX_VALUE;
      int var5 = Integer.MAX_VALUE;
      StarSystem var7;
      boolean var9;
      int var10;
      int var11;
      if (this.gameState.gameOptions.unifiedTerritories) {
        final CombinedForce var8 = this.player.combinedForce;
        for (final DefenseTask var6 : this.defenseTasks) {
          var7 = var6.system;
          if (var7.score == StarSystem.Score.NORMAL) {
            var9 = false;
            var10 = 0;

            for (var11 = 0; var11 < 4; ++var11) {
              var10 += var7.resources[var11];
              if (var8.surplusResources[var11] < var7.resources[var11]) {
                var9 = true;
                break;
              }
            }

            if (!var9 && (var2 == null || var10 < var3 || var10 == var3 && var5 > var6.fleetCost)) {
              var3 = var10;
              var5 = var6.fleetCost;
              var2 = var7;
            }
          }
        }
      } else {
        for (final DefenseTask var6 : this.defenseTasks) {
          var7 = var6.system;
          if (var7.score == StarSystem.Score.NORMAL) {
            boolean var14 = false;
            int var17 = 0;

            for (var10 = 0; var10 < 4; ++var10) {
              var17 += var7.resources[var10];
              if (var7.contiguousForce.surplusResources[var10] < var7.resources[var10]) {
                var14 = true;
                break;
              }
            }

            if (!var14 && (var2 == null || var17 < var3 || var3 == var17 && var6.fleetCost < var5)) {
              var5 = var6.fleetCost;
              var2 = var7;
              var3 = var17;
            }
          }
        }
      }

      final StarSystem[] var13 = this.gameState.map.systems;

      for (final StarSystem var16 : var13) {
        if (var16.owner == this.player && !systemBorderedByHostile[var16.index] && var16.score == StarSystem.Score.NORMAL) {
          var9 = false;
          var10 = 0;
          if (this.gameState.gameOptions.unifiedTerritories) {
            for (var11 = 0; var11 < 4; ++var11) {
              var10 += var16.resources[var11];
              if (var16.resources[var11] > this.player.combinedForce.surplusResources[var11]) {
                var9 = true;
                break;
              }
            }
          } else {
            for (var11 = 0; var11 < 4; ++var11) {
              var10 += var16.resources[var11];
              if (var16.resources[var11] > var16.contiguousForce.surplusResources[var11]) {
                var9 = true;
                break;
              }
            }
          }

          if (!var9 && (var2 == null || var3 > var10 || var10 == var3 && var5 > 0)) {
            var5 = 0;
            var3 = var10;
            var2 = var16;
          }
        }
      }

      if (var2 != null) {
        final ProjectOrder var15 = new ProjectOrder(GameState.ResourceType.BIOMASS, this.player, var2);
        projectOrders.add(var15);
      }
    }
  }

  private void initializeDefenseTasks() {
    this.defenseTasks.clear();
    int i = 0;
    for (final ContiguousForce force : this.player.contiguousForces) {
      for (final StarSystem system : force) {
        if (systemBorderedByHostile[system.index]) {
          this.defenseTasks.add(new DefenseTask(system, this.forceWantedResources[i], this.player.allies, this.gameState.victoryChecker));
        }
      }
      ++i;
    }
  }

  private void a372(final ContiguousForce var1, final int var2) {
    int var4 = 0;

    int var7;
    for (final StarSystem var5 : var1) {
      final StarSystem[] var6 = var5.neighbors;

      for (var7 = 0; var7 < var6.length; ++var7) {
        final StarSystem var8 = var6[var7];
        if (this.player != var8.owner) {
          ++var4;
          break;
        }
      }
    }

    if (var4 == 0) {
      this.forceFleetsAvailableToMove[var2] = 0;
      if (!this.gameState.gameOptions.unifiedTerritories) {
        this.buildFleets(var1.getCapital(), this.forceFleetsAvailableToBuild[var2]);
        this.forceFleetsAvailableToBuild[var2] = 0;
      }

    } else {
      final StarSystem[] var11 = new StarSystem[var4];
      int var12 = 0;

      int var9;
      for (final StarSystem var13 : var1) {
        final StarSystem[] var14 = var13.neighbors;

        for (var9 = 0; var9 < var14.length; ++var9) {
          final StarSystem var10 = var14[var9];
          if (var10.owner != this.player) {
            var11[var12] = var13;
            ++var12;
            break;
          }
        }
      }

      var7 = this.forceFleetsAvailableToMove[var2] / var4;
      this.combatTasks = new AITask[var4];
      int var15;
      int[] var10000;
      for (var9 = 0; var9 < var11.length; ++var9) {
        this.combatTasks[var9] = new DefenseTask(var11[var9], new int[4], this.player.allies, this.gameState.victoryChecker);
        var15 = this.forceFleetsAvailableToMove[var2] % var4 != 0 ? 1 : 0;
        fleetsNeededToHoldSystem[var11[var9].index] = var7 + var15;
        var10000 = this.forceFleetsAvailableToMove;
        var10000[var2] -= var15 + var7;
        --var4;
      }

      this.satisfyDefenseTasks();
      if (!this.gameState.gameOptions.unifiedTerritories) {
        int length = var11.length;
        var7 = this.forceFleetsAvailableToBuild[var2] / length;

        for (var9 = 0; var9 < var11.length; ++var9) {
          var15 = this.forceFleetsAvailableToBuild[var2] % length == 0 ? 0 : 1;
          this.buildFleets(var11[var9], var15 + var7);
          var10000 = this.forceFleetsAvailableToBuild;
          var10000[var2] -= var7 + var15;
          --length;
        }
      }

      this.combatTasks = new AITask[0];
    }
  }

  private void assessOtherPlayers() {
    systemDistancesFromEmpires = new int[this.gameState.playerCount][];
    Arrays.setAll(systemDistancesFromEmpires, i -> this.systemDistancesFromEmpire(this.gameState.players[i]));

    this.desiredAlly = -1;
    systemCloserToMeThanAllies = new boolean[this.gameState.map.systems.length];

    final int[] distancesFromMe = systemDistancesFromEmpires[this.player.index];
    int closerToMeThanAlliesCount = 0;
    for (int i = 0; i < this.gameState.map.systems.length; ++i) {
      if (this.gameState.map.systems[i].owner == this.player) {
        systemCloserToMeThanAllies[i] = false;
      } else {
        boolean closerToMeThanAllies = true;

        for (int j = 0; j < this.gameState.playerCount; ++j) {
          if (j != this.player.index && this.player.allies[j] && systemDistancesFromEmpires[j][i] <= distancesFromMe[i]) {
            closerToMeThanAllies = false;
            break;
          }
        }

        systemCloserToMeThanAllies[i] = closerToMeThanAllies;
        if (closerToMeThanAllies) {
          ++closerToMeThanAlliesCount;
        }
      }
    }

    for (int i = 0; i < this.gameState.playerCount; ++i) {
      this.hostilityScores[i] = this.computeHostilityScore(this.gameState.players[i], closerToMeThanAlliesCount);
    }

    for (int i = 0; i < this.gameState.playerCount; ++i) {
      final int j = this.gameState.playerFleetProductionRanks[i];
      if (this.hostilityScores[j] < 0) {
        this.desiredAlly = j;
      }
    }
  }

  private void calculateSystemsAtRisk() {
    for (final StarSystem system : this.gameState.map.systems) {
      systemBorderedByHostile[system.index] = false;
      if (system.owner == this.player) {
        if (Arrays.stream(system.neighbors).anyMatch(neighbor ->
            neighbor.owner != null && neighbor.owner != this.player && !this.player.allies[neighbor.owner.index])) {
          systemBorderedByHostile[system.index] = true;
        }
      }
    }
  }

  @Override
  public void initialize(final boolean isActive) {
    this.isActive = isActive;
  }

  private void satisfyHoldTasks2() {
    for (final HoldTask task : this.holdTasks) {
      final int i = task.system.index;
      if (fleetsNeededToHoldSystem[i] < 0) {
        throw new RuntimeException(this.gameState.map.systems[i].name + " has " + fleetsNeededToHoldSystem[i] + " fleets assigned to garrison it!");
      }

      if (fleetsNeededToHoldSystem[i] >= systemFleetsAvailableToMove[i]) {
        fleetsNeededToHoldSystem[i] -= systemFleetsAvailableToMove[i];
        systemFleetsAvailableToMove[i] = 0;
      } else {
        systemFleetsAvailableToMove[i] -= fleetsNeededToHoldSystem[i];
        fleetsNeededToHoldSystem[i] = 0;
      }
    }

    outer:
    for (final HoldTask task : this.holdTasks) {
      final int i = task.system.index;
      boolean moveFromThreatened = false;

      for (int j = 0; j < 2; ++j) {
        for (final StarSystem system : task.system.contiguousForce) {
          if (system != task.system && systemFleetsAvailableToMove[system.index] != 0 && (moveFromThreatened || !systemBorderedByHostile[system.index])) {
            if (systemFleetsAvailableToMove[system.index] >= fleetsNeededToHoldSystem[i]) {
              this.moveFleets(system, task.system, fleetsNeededToHoldSystem[i]);
              systemFleetsAvailableToMove[system.index] -= fleetsNeededToHoldSystem[i];
              fleetsNeededToHoldSystem[i] = 0;
              continue outer;
            }

            this.moveFleets(system, task.system, systemFleetsAvailableToMove[system.index]);
            fleetsNeededToHoldSystem[i] -= systemFleetsAvailableToMove[system.index];
            systemFleetsAvailableToMove[system.index] = 0;
          }
        }

        moveFromThreatened = true;
      }
    }

    Arrays.fill(fleetsNeededToHoldSystem, 0);
  }

  private void deployTannhauser() {
    if (this.player.researchPoints[3] >= 5) {
      StarSystem var6;
      int var8;
      if (this.aggressiveness < 50 && this.peacefulness < 50 && this.player.contiguousForces.size() > 1) {
        final Iterator<ContiguousForce> it = this.player.contiguousForces.iterator();
        ContiguousForce var3 = it.next();
        ContiguousForce var19 = it.next();
        ContiguousForce var2;
        if (var19.size() > var3.size()) {
          var2 = var3;
          var3 = var19;
        } else {
          var2 = var19;
        }
        while (it.hasNext()) {
          var19 = it.next();
          if (var19.size() > var3.size()) {
            var2 = var3;
            var3 = var19;
          }
        }

        StarSystem var20 = null;
        var6 = null;
        StarSystem var7 = null;
        int var21 = 0;

        for (final StarSystem var10 : var3) {
          if (!systemBorderedByHostile[var10.index]) {
            var20 = var10;
            break;
          }

          var8 = var10.remainingGarrison;
          if (var10.hasDefensiveNet || var10 == _qoe) {
            var8 *= 2;
          }

          if (var21 < var8) {
            var7 = var10;
            var21 = var8;
          }
        }

        if (var20 == null) {
          var20 = var7;
        }

        int i = -1;

        for (final StarSystem var10 : var2) {
          if (!systemBorderedByHostile[var10.index]) {
            var6 = var10;
            break;
          }

          var8 = var10.remainingGarrison;
          if (var10.hasDefensiveNet || _qoe == var10) {
            var8 *= 2;
          }

          if (var8 > i) {
            var7 = var10;
            i = var8;
          }
        }

        if (var6 == null) {
          var6 = var7;
        }

        if (var20 == null || var6 == null) {
          throw new RuntimeException("TaskAI has more than one territory but can't find a star in each to link with a Tannhauser");
        } else {
          projectOrders.add(new ProjectOrder(this.player, var6, var20));
        }
      } else {
        final Iterator<ContiguousForce> it = this.player.contiguousForces.iterator();
        ContiguousForce var2 = it.next();
        while (it.hasNext()) {
          final ContiguousForce var3 = it.next();
          if (var3.size() > var2.size()) {
            var2 = var3;
          }
        }

        int var5 = 0;
        var6 = null;

        for (final StarSystem var7 : var2) {
          int var4 = var7.garrison;
          if (var7.hasDefensiveNet || _qoe == var7) {
            var4 *= 2;
          }

          if (var4 > var5) {
            var6 = var7;
            var5 = var4;
          }
        }

        if (var6 == null) {
          throw new RuntimeException("TaskAI can't find any stars in territory from which to launch a Tannhauser");
        } else {
          var8 = 0;
          final StarSystem[] var9 = this.gameState.map.systems;

          StarSystem var7 = null;
          for (final StarSystem var12 : var9) {
            if (var12.contiguousForce != var6.contiguousForce && (var12.owner == this.player || var12.owner == null || !Objects.requireNonNull(this.player).allies[var12.owner.index]) && (var12.owner != this.player || this.peacefulness < 90)) {
              boolean var13 = false;
              final StarSystem[] var14 = var12.neighbors;

              int var15;
              for (var15 = 0; var15 < var14.length; ++var15) {
                final StarSystem var16 = var14[var15];
                if (var6.contiguousForce == var16.contiguousForce) {
                  var13 = true;
                  break;
                }
              }

              if (!var13) {
                int var22 = 0;

                int var24;
                for (var24 = 0; var24 < 4; ++var24) {
                  var22 += (-var2.surplusResources[var24] + var2.surplusResources[var2.surplusResourceRanks[0]] + 1) * var12.resources[var24];
                }

                if (this.isCaptureAndHold && var12.index == 36) {
                  var22 += this.peacefulness / 2;
                }

                var15 = var12.garrison;
                if (this.isDerelicts && var12.score == StarSystem.Score.NEUTRAL_HOMEWORLD) {
                  if (this.gameState.gameOptions.unifiedTerritories) {
                    var24 = this.player.combinedForce.fleetProduction;
                  } else {
                    var24 = var6.contiguousForce.fleetProduction;
                  }

                  if (-5 * var12.resources[0] > var24) {
                    var22 -= 100;
                  }

                  if (var12.owner != null && var12.owner.index == this.currentObjectiveLeader) {
                    var22 += 100;
                  }
                }

                if (var12.hasDefensiveNet) {
                  var15 *= 2;
                }

                final StarSystem[] var25 = var12.neighbors;

                for (final StarSystem var18 : var25) {
                  if (var18.owner != this.player && var18.owner != null) {
                    var15 += var18.garrison;
                    if (!this.gameState.gameOptions.simpleGarrisoning) {
                      ++var15;
                    }
                  }
                }

                if (var15 < 1) {
                  var15 = 1;
                }

                var24 = (var22 << 16) / var15;
                if (var8 < var24) {
                  var7 = var12;
                  var8 = var24;
                }
              }
            }
          }

          if (var7 != null) {
            projectOrders.add(new ProjectOrder(this.player, var7, var6));
          }
        }
      }
    }
  }

  private void deployStellarBomb(final StarSystem target) {
    if (this.player.researchPoints[GameState.ResourceType.ENERGY] < 5) {
      throw new RuntimeException(this.player.name + " is trying to deploy a Stellar Bomb it doesn't have at star " + target.name);
    }
    if (deployedStellarBomb) {
      final ProjectOrder existingOrder = projectOrders.stream()
          .filter(order -> order.type == GameState.ResourceType.ENERGY)
          .findFirst().orElse(null);
      if (existingOrder == null) {
        throw new RuntimeException(this.player.name + "'s deployedStellarBomb variable incorrectly set to true.");
      } else if (existingOrder.target == stellarBombTarget) {
        throw new RuntimeException(this.player.name + " is trying to deploy a Stellar Bomb it has already used." + " Already deployed at star " + existingOrder.target.name + ", now trying to deploy at " + target.name);
      } else {
        throw new RuntimeException(this.player.name + ": TaskAI.deployStellarBomb: targetStellarBomb is not the star the ProjectUse points at." + " stellarBombTarget: " + stellarBombTarget.name + ", existingOrder.target: " + existingOrder.target);
      }
    } else if (this.player == target.owner) {
      throw new RuntimeException(this.player.name + " is trying to Stellar Bomb itself at star " + target.name);
    } else {
      projectOrders.add(new ProjectOrder(GameState.ResourceType.ENERGY, this.player, target));
      deployedStellarBomb = true;
      stellarBombTarget = target;
    }
  }

  private void satisfyCaptureTask(final CaptureTask task, final boolean var3) {
    final StarSystem[] neighbors = task.system.neighbors;
    final int[] neighboringForceIndexes = new int[neighbors.length];
    boolean ok;

    for (int i = 0; i < neighbors.length; ++i) {
      if (neighbors[i].owner == this.player) {
        ok = false;
        int forceIndex = 0;
        for (final ContiguousForce force : this.player.contiguousForces) {
          if (force == neighbors[i].contiguousForce) {
            ok = true;
            break;
          }
          ++forceIndex;
        }

        if (!ok) {
          throw new RuntimeException("Can't find the Territory of a star owned by the AI: " + neighbors[i].name);
        }

        neighboringForceIndexes[i] = forceIndex;
      } else {
        neighboringForceIndexes[i] = -1;
      }
    }

    final int[] forceFleetsAvailableToAttack = new int[this.player.contiguousForces.size()];
    for (int i = 0; i < neighbors.length; ++i) {
      if (neighboringForceIndexes[i] != -1) {
        forceFleetsAvailableToAttack[neighboringForceIndexes[i]] += systemFleetsAvailableToMove[neighbors[i].index];
      }
    }

    int totalFleetsAvailableToAttack = 0;
    for (int i = 0; i < forceFleetsAvailableToAttack.length; ++i) {
      if (this.forceFleetsAvailableToMove[i] < forceFleetsAvailableToAttack[i]) {
        forceFleetsAvailableToAttack[i] = this.forceFleetsAvailableToMove[i];
      }
      totalFleetsAvailableToAttack += forceFleetsAvailableToAttack[i];
    }

    if (this.gameState.gameOptions.unifiedTerritories) {
      totalFleetsAvailableToAttack += this.forceFleetsAvailableToBuild[0];
    } else {
      totalFleetsAvailableToAttack += IntStream.range(0, forceFleetsAvailableToAttack.length)
          .filter(i -> forceFleetsAvailableToAttack[i] > 0)
          .map(i -> this.forceFleetsAvailableToBuild[i])
          .sum();
    }

    int adjustedFleetCost = this.adjustFleetCost(task.fleetCost);
    if (var3) {
      if (task.system == stellarBombTarget) {
        task.fleetCost = task.system.garrison / 2;
        if (task.system.owner != null) {
          for (final StarSystem neighbor : neighbors) {
            if (neighbor.owner == task.system.owner) {
              task.fleetCost += neighbor.garrison;
            }
          }
        }

        if (task.system.hasDefensiveNet) {
          task.fleetCost *= 2;
        }
      }

      adjustedFleetCost = this.adjustFleetCost(task.fleetCost);
      boolean var26 = totalFleetsAvailableToAttack >= adjustedFleetCost;
      if (!var26 && !deployedStellarBomb && this.player.researchPoints[2] >= 5 && task.system.garrison >= 5) {
        int var12 = task.fleetCost;
        if (task.system.hasDefensiveNet) {
          var12 /= 2;
        }

        var12 -= task.system.garrison / 2;
        if (task.system.hasDefensiveNet) {
          var12 *= 2;
        }

        final int var27 = this.adjustFleetCost(var12);
        if (var27 <= totalFleetsAvailableToAttack) {
          this.deployStellarBomb(task.system);
          var26 = true;
          task.fleetCost = task.system.garrison / 2;
          if (task.system.owner != null) {
            for (final StarSystem neighbor : neighbors) {
              if (task.system.owner == neighbor.owner) {
                task.fleetCost += neighbor.garrison;
              }
            }
          }

          if (task.system.hasDefensiveNet) {
            task.fleetCost *= 2;
          }

          adjustedFleetCost = this.adjustFleetCost(task.fleetCost);
        }
      }

      if (var26 && !deployedStellarBomb && this.player.researchPoints[2] >= 5 && task.system.garrison >= 25) {
        this.deployStellarBomb(task.system);
        task.fleetCost = task.system.garrison / 2;
        if (task.system.owner != null) {
          for (final StarSystem neighbor : neighbors) {
            if (task.system.owner == neighbor.owner) {
              task.fleetCost += neighbor.garrison;
            }
          }
        }

        if (task.system.hasDefensiveNet) {
          task.fleetCost *= 2;
        }

        adjustedFleetCost = this.adjustFleetCost(task.fleetCost);
      }

      if (var26) {
        task._p = true;
      }
    }

    if (task._p) {
      if (!var3) {
        if (totalFleetsAvailableToAttack == 0) {
          return;
        }

        adjustedFleetCost = adjustedFleetCost * 3 / 10;
        if (adjustedFleetCost == 0) {
          adjustedFleetCost = 1;
        }
      }

      final StarSystem[] var30 = neighbors.clone();
      boolean var28 = true;
      while (var28) {
        var28 = false;

        for (int var15 = 0; var15 < var30.length - 1; ++var15) {
          if (systemFleetsAvailableToMove[var30[var15].index] > systemFleetsAvailableToMove[var30[var15 + 1].index]) {
            final StarSystem var29 = var30[var15];
            var30[var15] = var30[var15 + 1];
            var28 = true;
            var30[1 + var15] = var29;
          }
        }
      }

      int var11 = this.a587(true, var30, task.system, adjustedFleetCost);
      if (adjustedFleetCost > var11) {
        var11 += this.a587(false, var30, task.system, adjustedFleetCost - var11);
      }

      adjustedFleetCost -= var11;
      if (adjustedFleetCost > 0) {
        if (this.gameState.gameOptions.unifiedTerritories) {
          if (adjustedFleetCost > this.forceFleetsAvailableToBuild[0] && var3) {
            throw new RuntimeException(this.player.name + " has decided to attack a system, but appears not to have sufficient fleets to carry through.");
          }

          final int var15 = Math.min(adjustedFleetCost, this.forceFleetsAvailableToBuild[0]);

          for (final StarSystem starSystem : var30) {
            if (starSystem.owner == this.player) {
              this.buildFleets(starSystem, var15);
              this.moveFleets(starSystem, task.system, var15);
              break;
            }
          }

          adjustedFleetCost -= var15;

          for (int var31 = 0; this.forceFleetsAvailableToBuild.length > var31; ++var31) {
            this.forceFleetsAvailableToBuild[var31] -= var15;
          }

        } else {
          final int[] var33 = new int[this.forceFleetsAvailableToBuild.length];
          Arrays.setAll(var33, i -> i);
          a325nm(var33, this.forceFleetsAvailableToBuild.clone());

          for (int var312 = 0; var312 < this.forceFleetsAvailableToBuild.length; ++var312) {
            if (this.forceFleetsAvailableToBuild[var33[var312]] != 0) {

              for (final StarSystem var19 : neighbors) {
                if (this.player == var19.owner) {
                  int var20 = 0;
                  boolean found = false;
                  for (final ContiguousForce var21 : this.player.contiguousForces) {
                    if (var21 == var19.contiguousForce) {
                      found = true;
                      break;
                    }
                    ++var20;
                  }
                  if (!found) {
                    throw new RuntimeException("Cannot find Territory that " + this.player.name + "'s star belongs to: " + var19.name);
                  }

                  if (var20 == var33[var312]) {
                    if (this.forceFleetsAvailableToBuild[var20] > adjustedFleetCost) {
                      this.buildFleets(var19, adjustedFleetCost);
                      this.moveFleets(var19, task.system, adjustedFleetCost);
                      this.forceFleetsAvailableToBuild[var20] -= adjustedFleetCost;
                      adjustedFleetCost = 0;
                    } else {
                      this.buildFleets(var19, this.forceFleetsAvailableToBuild[var20]);
                      this.moveFleets(var19, task.system, this.forceFleetsAvailableToBuild[var20]);
                      adjustedFleetCost -= this.forceFleetsAvailableToBuild[var20];
                      this.forceFleetsAvailableToBuild[var20] = 0;
                    }
                    break;
                  }
                }
              }

              if (adjustedFleetCost == 0) {
                break;
              }
            }
          }
        }
      }

      if (adjustedFleetCost > 0 && var3) {
        throw new RuntimeException("After full attack movement, " + this.player.name + " still hasn't sent enough fleets to " + task.system.name);
      }
    }
  }

  @SuppressWarnings("WeakerAccess")
  protected static final class HostilityScore {
    public static final int FRIENDLY_BOTH_STRONG = -3;
    public static final int FRIENDLY_COMMON_ENEMY = -2;
    public static final int FRIENDLY_INTIMIDATED = -1;
    public static final int NEUTRAL = 0;
    public static final int HOSTILE_PRODUCTION_LEADER = 1;
    public static final int HOSTILE_ENEMY_ALLY_1 = 2;
    public static final int HOSTILE_NOT_THREAT = 3;
    public static final int HOSTILE_WANT_CAPTURE = 4;
    public static final int HOSTILE_CONFIDENT = 5;
    public static final int HOSTILE_OBJECTIVE_LEADER = 6;
    public static final int HOSTILE_ALLIED_WITH_OBJECTIVE_LEADER = 7;
  }
}
