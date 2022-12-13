package funorb.shatteredplans.client.game;

import funorb.shatteredplans.game.GameState;
import funorb.shatteredplans.game.MoveFleetsOrder;
import funorb.shatteredplans.game.Player;
import funorb.shatteredplans.map.StarSystem;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.List;

/**
 * This class performs some simple analyses of the current map from the
 * perspective of the local player. The resulting information is used to drive
 * the “tactical overlay” in the user interface, which renders the information
 * using cross-hatching of various colors.
 */
public final class TacticalAnalysis {
  /**
   * {@code true} for systems owned by the local player that could be attacked
   * by a hostile force on this turn (and therefore could be lost regardless of
   * its garrison at the start of combat on this turn).
   */
  private final boolean[] systemThreatened;

  /**
   * {@code true} for systems that could be the target of a stellar bomb on
   * this turn.
   */
  private final boolean[] systemBombable;

  /**
   * {@code true} for systems that the local player is <i>guaranteed</i> to own
   * after this turn. Implies {@link #systemCanOwn}.
   */
  private final boolean[] systemWillOwn;

  /**
   * {@code true} for systems that the local play <i>may</i> own after this
   * turn. Implied by {@link #systemWillOwn}.
   */
  private final boolean[] systemCanOwn;

  /**
   * The minimum number of fleets (owned by the local player) that could be
   * garrisoned in each system after this turn, assuming the worst possible
   * combat outcomes.
   */
  private final int[] minGarrisonAtTurnEnd;

  /**
   * The maximum number of fleets (owned by the local player) that could be
   * garrisoned in each system after this turn, assuming the best possible
   * combat outcomes.
   */
  private final int[] maxGarrisonAtTurnEnd;

  /**
   * The absolute minimum number of fleets needed to be garrisoned in each
   * system at the end of this turn to have any chance of holding it, assuming
   * the best possible combat outcomes in neighboring systems.
   */
  private final int[] minGarrisonToHold;

  /**
   * The minimum number of fleets needed to be garrisoned in each system at the
   * end of this turn to <i>guarantee</i> it will be held, assuming the worst
   * possible combat outcomes in neighboring systems.
   */
  private final int[] safeGarrisonToHold;

  /**
   * Tracks the index of the earliest “wave” in which each system <i>might</i>
   * collapse. For systems that cannot be lost (see {@link #systemWillOwn}),
   * the value is arbitrary.
   */
  private final int[] possibleCollapseWave;

  /**
   * Tracks the index of the earliest “wave” in which each system is
   * <i>guaranteed</i> to collapse. For systems that might not be lost (see
   * {@link #systemCanOwn}), the value is arbitrary.
   */
  private final int[] guaranteedCollapseWave;

  public TacticalAnalysis(final int systemCount) {
    this.systemThreatened = new boolean[systemCount];
    this.systemBombable = new boolean[systemCount];
    this.systemCanOwn = new boolean[systemCount];
    this.systemWillOwn = new boolean[systemCount];
    this.guaranteedCollapseWave = new int[systemCount];
    this.possibleCollapseWave = new int[systemCount];
    this.minGarrisonAtTurnEnd = new int[systemCount];
    this.safeGarrisonToHold = new int[systemCount];
    this.maxGarrisonAtTurnEnd = new int[systemCount];
    this.minGarrisonToHold = new int[systemCount];
  }

  public boolean isThreatened(final @NotNull StarSystem system) {
    return this.systemThreatened[system.index];
  }

  public boolean isOwnershipGuaranteed(final @NotNull StarSystem system) {
    return this.systemWillOwn[system.index];
  }

  @SuppressWarnings("BooleanMethodIsAlwaysInverted")
  public boolean isOwnershipPossible(final @NotNull StarSystem system) {
    return this.systemCanOwn[system.index];
  }

  public int getEarliestPossibleCollapseWave(final @NotNull StarSystem system) {
    return this.possibleCollapseWave[system.index];
  }

  public int getEarliestGuaranteedCollapseWave(final @NotNull StarSystem system) {
    return this.guaranteedCollapseWave[system.index];
  }

  public void analyze(final @NotNull GameState gameState, final @Nullable Player localPlayer) {
    for (final StarSystem system : gameState.map.systems) {
      this.systemThreatened[system.index] = false;
      this.guaranteedCollapseWave[system.index] = 0;
      this.possibleCollapseWave[system.index] = 0;
      if (system.owner == localPlayer) {
        this.systemCanOwn[system.index] = true;
        this.maxGarrisonAtTurnEnd[system.index] = system.remainingGarrison;
        this.systemWillOwn[system.index] = true;
        this.minGarrisonAtTurnEnd[system.index] = system.remainingGarrison;
      } else {
        this.systemCanOwn[system.index] = false;
        this.maxGarrisonAtTurnEnd[system.index] = 0;
        this.systemWillOwn[system.index] = false;
        this.minGarrisonAtTurnEnd[system.index] = 0;
      }

      this.systemBombable[system.index] = Arrays.stream(system.neighbors)
          .anyMatch(neighbor -> neighbor.owner != null
              && system.owner != neighbor.owner
              && (system.owner == null || !neighbor.owner.allies[system.owner.index]));

      for (final MoveFleetsOrder incomingOrder : system.incomingOrders) {
        if (incomingOrder.player == localPlayer) {
          this.maxGarrisonAtTurnEnd[system.index] += incomingOrder.quantity;
          this.systemCanOwn[system.index] = true;
          if ((incomingOrder.target.owner == localPlayer || incomingOrder.target.garrison == 0)
              && !this.systemBombable[incomingOrder.source.index]) {
            this.minGarrisonAtTurnEnd[system.index] += incomingOrder.quantity;
            this.systemWillOwn[system.index] = true;
          }
        }
      }

      if (localPlayer != null) {
        for (final StarSystem neighbor : system.neighbors) {
          if (neighbor.owner != null && neighbor.owner != localPlayer
              && (system.owner != localPlayer || !neighbor.owner.allies[localPlayer.index])
              && !gameState.isStellarBombTarget(localPlayer, neighbor)) {
            this.systemThreatened[system.index] = true;
            this.systemWillOwn[system.index] = false;
            this.minGarrisonAtTurnEnd[system.index] = 0;
            break;
          }
        }
      }
    }

    if (gameState.gameOptions.noChainCollapsing) {
      for (final StarSystem system : gameState.map.systems) {
        this.minGarrisonToHold[system.index] = 0;
        this.safeGarrisonToHold[system.index] = 0;
      }
    } else if (gameState.gameOptions.simpleGarrisoning) {
      for (final StarSystem system : gameState.map.systems) {
        this.minGarrisonToHold[system.index] = 1;
        this.safeGarrisonToHold[system.index] = 1;
      }
    } else {
      for (final StarSystem system : gameState.map.systems) {
        this.minGarrisonToHold[system.index] = (int) Arrays.stream(system.neighbors).filter(neighbor -> !this.systemCanOwn[neighbor.index]).count();
        this.safeGarrisonToHold[system.index] = (int) Arrays.stream(system.neighbors).filter(neighbor -> !this.systemWillOwn[neighbor.index]).count();
      }
    }

    if (!gameState.gameOptions.noChainCollapsing) {
      analyzeCollapse(gameState.gameOptions.simpleGarrisoning, gameState.map.systems, this.systemCanOwn, this.minGarrisonToHold, this.maxGarrisonAtTurnEnd, this.guaranteedCollapseWave);
      analyzeCollapse(gameState.gameOptions.simpleGarrisoning, gameState.map.systems, this.systemWillOwn, this.safeGarrisonToHold, this.minGarrisonAtTurnEnd, this.possibleCollapseWave);
    }
  }

  private static void analyzeCollapse(final boolean simpleGarrisoning,
                                      final StarSystem[] systems,
                                      final boolean[] willOwn,
                                      final int[] garrisonToHold,
                                      final int[] garrisonAtTurnEnd,
                                      final int[] collapseWave) {
    for (int currentWave = 0;; currentWave++) {
      final List<StarSystem> collapsed = Arrays.stream(systems)
          .filter(system -> willOwn[system.index] && garrisonToHold[system.index] > garrisonAtTurnEnd[system.index])
          .toList();
      if (collapsed.isEmpty()) break;

      for (final StarSystem system : collapsed) {
        willOwn[system.index] = false;
        collapseWave[system.index] = currentWave;
        for (final StarSystem neighbor : system.neighbors) {
          if (simpleGarrisoning) {
            garrisonToHold[neighbor.index] = 1;
          } else {
            garrisonToHold[neighbor.index]++;
          }
        }
      }
    }
  }
}
