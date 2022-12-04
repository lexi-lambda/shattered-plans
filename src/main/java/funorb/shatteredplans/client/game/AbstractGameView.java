package funorb.shatteredplans.client.game;

import funorb.graphics.Point;
import funorb.graphics.Rect;
import funorb.graphics.Sprite;
import funorb.shatteredplans.StringConstants;
import funorb.shatteredplans.client.CombatEngagementAnimationState;
import funorb.shatteredplans.client.GameUI;
import funorb.shatteredplans.client.ShatteredPlansClient;
import funorb.shatteredplans.game.ContiguousForce;
import funorb.shatteredplans.game.GameState;
import funorb.shatteredplans.game.MoveFleetsOrder;
import funorb.shatteredplans.game.Player;
import funorb.shatteredplans.game.ProjectOrder;
import funorb.shatteredplans.map.Map;
import funorb.shatteredplans.map.StarSystem;
import funorb.shatteredplans.map.TannhauserLink;
import funorb.util.IsaacRandom;
import org.intellij.lang.annotations.MagicConstant;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.Random;

public abstract class AbstractGameView {
  public static final float TWO_HUNDRED_F = 200.0F;
  protected final int[][] projectHighlightColors;
  public int lastTickMouseX;
  protected Player[] _ib;
  protected Map map;
  protected GameUI gameUI;
  protected int[][] _cb;
  public float mapScrollPosnY;
  protected float targetZoomFactor;
  public float mapScrollPosnX;
  public float unitScalingFactor;
  public float maxUnitScalingFactor;
  protected final Player localPlayer;
  protected float targetScrollPosnY;
  public ContiguousForce _fb;
  public int lastTickMouseY;
  protected float targetScrollPosnX;
  public boolean isAnimatingViewport;
  public StarSystem targetedSystem;
  public MoveFleetsOrder selectedFleetOrder;
  protected int[] possibleSystemCollapseStages;
  protected Random random;
  protected int animationTick;
  public SystemHighlight[] highlightedSystems;
  protected int[] systemDrawX;
  protected List<CombatEngagementAnimationState> combatEngagements;
  protected int[] clonedRemainingGarrisons;
  protected boolean[] willOwnSystem;
  protected int _n;
  protected int[] systemCollapseStages;
  private int largestFleetMovement;
  protected int largestFleetBuildQuantity;
  protected List<MoveFleetsAnimationState> fleetMovements;
  protected List<MoveFleetsAnimationState> collapseRetreats;
  protected Rect[] systemBounds;
  protected List<CombatExplosion> combatExplosions;
  protected int[][] systemHexes;
  protected List<BuildEvent> buildEvents;
  protected Player[] clonedSystemOwners;
  protected Collection<ProjectOrder> projectOrders;
  protected List<MoveFleetsAnimationState> combatRetreats;
  protected boolean[] canOwnSystem;
  protected int[] systemDrawY;
  protected Player[] systemOwners;
  @MagicConstant(valuesFromClass = GameView.AnimationPhase.class)
  public int animationPhase;
  protected int buildPhases;
  public int turnSeed;
  protected boolean[] retreatTargets;
  protected Collection<TannhauserLink> tannhauserLinks;
  private Player[] sessionSystemOwners;
  private ContiguousForce[] systemForces;
  private int[] remainingGarrisons;
  private int[] systemGarrisons;

  protected AbstractGameView(final Player localPlayer) {
    this.localPlayer = localPlayer;
    this.projectHighlightColors = new int[4][];
  }

  private static boolean hitTest(final int[] points, final int x, final int y) {
    int x1 = points[points.length - 2];
    int y1 = points[points.length - 1];
    int var6 = 0;

    for (int i = 0; i < points.length; i += 2) {
      final int x2 = points[i];
      final int y2 = points[i + 1];
      if (hitTestHelper(x1, y1, x2, y2, x, y)) {
        ++var6;
      }

      x1 = x2;
      y1 = y2;
    }

    return (var6 & 1) == 1;
  }

  private static boolean hitTestHelper(int x1, int y1, int x2, int y2, final int px, final int py) {
    if (y1 == y2) {
      return false;
    } else {
      if (y1 > y2) {
        final int tmp1 = y1;
        y1 = y2;
        y2 = tmp1;
        final int tmp2 = x1;
        x1 = x2;
        x2 = tmp2;
      }

      if (y1 < py && py <= y2) {
        return (py - y1) * (x2 - x1) > (px - x1) * (y2 - y1);
      } else {
        return false;
      }
    }
  }

  private void addBuildProjectEvent(final StarSystem system,
                                    final Player player,
                                    @MagicConstant(valuesFromClass = GameState.ResourceType.class) final int type) {
    final int phase = this.nextBuildPhase(system);
    this.buildEvents.add(new BuildEvent(system, player, type, phase));
    if (phase >= this.buildPhases) {
      this.buildPhases = phase + 1;
    }
  }

  private int nextBuildPhase(final StarSystem system) {
    int phase = 0;
    while (this.haveBuildEvent(system, phase)) {
      ++phase;
    }
    return phase;
  }

  public final void resetSystemState() {
    final int systemCount = this.map.systems.length;

    if (this.systemOwners == null || this.systemOwners.length < this.map.systems.length) {
      this.systemGarrisons = new int[systemCount];
      this.systemOwners = new Player[systemCount];
      this.retreatTargets = new boolean[systemCount];
    }

    for (int i = 0; i < systemCount; ++i) {
      final StarSystem system = this.map.systems[i];
      this.systemOwners[i] = system.owner;
      this.systemGarrisons[i] = system.garrison;
      this.retreatTargets[i] = false;
    }
  }

  public final void setMap(final Map map) {
    this.map = map;
    this.maxUnitScalingFactor = (float) (this.map.drawingWidth * 300 / 450);
    final SystemHighlight[] var3 = new SystemHighlight[this.map.systems.length];
    final boolean[] var4 = new boolean[this.map.systems.length];

    for (int i = 0; i < this.highlightedSystems.length; ++i) {
      var3[i] = this.highlightedSystems[i];
      var4[i] = this.retreatTargets[i];
    }

    this.highlightedSystems = var3;
    this.retreatTargets = var4;
    this.a487();
  }

  private void clearTurnEvents() {
    this.fleetMovements.clear();
    this.combatEngagements.clear();
    this.buildEvents.clear();
    this.combatRetreats.clear();
    this.collapseRetreats.clear();
    this.largestFleetMovement = 0;
    this.largestFleetBuildQuantity = 0;
    this.buildPhases = 0;
  }

  public final void setGameUI(final GameUI var1) {
    this.gameUI = var1;
  }

  public final void a968(final ContiguousForce var1) {
    if (!var1.isEmpty()) {
      int var5;
      if (this._fb != var1) {
        final int var3 = this.gameUI.getHeight();
        final short var4 = 320;
        var5 = var3 / 2;

        for (final StarSystem var6 : var1) {
          final int var7 = (int) ((-this.mapScrollPosnX + (float) var6.posnX) * (300.0F / (this.unitScalingFactor + (float) var6._z))) + var4;
          final int var8 = var5 + (int) ((-this.mapScrollPosnY + (float) var6.posnY) * (300.0F / ((float) var6._z + this.unitScalingFactor)));
          if (var7 >= 0 && var8 >= 0 && var7 < ShatteredPlansClient.SCREEN_WIDTH && var3 > var8) {
            this._fb = var1;
            return;
          }
        }
      }

      final Rect var9 = this.a677(var1);
      final Rect var10 = this.gameUI.b520();
      var5 = -var10.x1 + var10.x2;
      final int var11 = var10.y2 - var10.y1;
      float var12 = (float) ((var9.x2 - var9.x1) / var5);
      if ((float) ((-var9.y1 + var9.y2) / var11) > var12) {
        var12 = (float) ((var9.y2 - var9.y1) / var11);
      }

      var12 = (float) ((double) var12 + 0.2D);
      if (var12 < (float) 1) {
        var12 = 1.0F;
      }

      float var13 = (float) (var10.x1 + var10.x2 - ShatteredPlansClient.SCREEN_WIDTH >> 1);
      var13 *= var12;
      this.targetScrollPosnX = -var13 + (float) (var9.x2 + var9.x1 >> 1);
      float v = (float) (var10.y1 + var10.y2 - this.gameUI.getHeight() >> 1);
      v *= var12;
      this.isAnimatingViewport = true;
      this._fb = var1;
      this.targetScrollPosnY = (float) (var9.y2 + var9.y1 >> 1) - v;
      this.targetZoomFactor = var12 * 300.0F;
    }
  }

  private void addBuildFleetsEvent(final StarSystem system, final Player player, final int quantity) {
    final int phase = this.nextBuildPhase(system);
    this.buildEvents.add(new BuildEvent(system, player, -1, quantity, phase));
    if (phase >= this.buildPhases) {
      this.buildPhases = phase + 1;
    }
  }

  public final void setTacticalOverlay(final boolean[] canOwnSystem,
                                       final boolean[] willOwnSystem,
                                       final int[] collapseStages,
                                       final int[] possibleCollapseStages) {
    this.possibleSystemCollapseStages = possibleCollapseStages;
    this.systemCollapseStages = collapseStages;
    this.canOwnSystem = canOwnSystem;
    this.willOwnSystem = willOwnSystem;
  }

  private void addBuildTannhauserEvent(final Player player, final StarSystem source, final StarSystem target) {
    final int phase = this.nextTannhauserPhase(source, target);
    this.buildEvents.add(new BuildEvent(target, player, GameState.ResourceType.EXOTICS, phase));
    this.buildEvents.add(new BuildEvent(source, player, GameState.ResourceType.EXOTICS, phase));
    if (phase >= this.buildPhases) {
      this.buildPhases = phase + 1;
    }
  }

  private int nextTannhauserPhase(final StarSystem source, final StarSystem target) {
    int phase = 0;
    while (this.haveTannhauserEvent(phase) || this.haveBuildEvent(target, phase) || this.haveBuildEvent(source, phase)) {
      ++phase;
    }
    return phase;
  }

  private Rect a677(final ContiguousForce var2) {
    final Rect var4 = new Rect(Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MIN_VALUE, Integer.MIN_VALUE);

    for (final StarSystem var3 : var2) {
      for (final Point var7 : var3.hexPoints) {
        if (var4.y1 > var7.y) {
          var4.y1 = var7.y;
        }

        if (var4.x1 > var7.x) {
          var4.x1 = var7.x;
        }

        if (var7.y > var4.y2) {
          var4.y2 = var7.y;
        }

        if (var4.x2 < var7.x) {
          var4.x2 = var7.x;
        }
      }
    }

    return var4;
  }

  final void drawSystemIcon(final StarSystem system) {
    final Sprite sprite = system.getSprite();
    sprite.b115(
        this.systemDrawX[system.index] - 1 - (int) (TWO_HUNDRED_F * (float) sprite.width / (this.unitScalingFactor * 2.0F)),
        this.systemDrawY[system.index] - 1 - (int) ((float) sprite.width * TWO_HUNDRED_F / (this.unitScalingFactor * 2.0F)),
        (int) (TWO_HUNDRED_F * (float) sprite.width / this.unitScalingFactor),
        (int) (TWO_HUNDRED_F * (float) sprite.width / this.unitScalingFactor));
  }

  public final void a815(final StarSystem var1) {
    final Rect var3 = this.gameUI.b520();
    float var4 = 24000.0F / (float) (var3.x2 - var3.x1) - 3.0F * (float) var1._z;
    if (var4 < TWO_HUNDRED_F) {
      var4 = TWO_HUNDRED_F;
    }

    this.a021(78, var1, var4);
  }

  public final void stopCombatAnimations() {
    this.animationPhase = AnimationPhase.NOT_PLAYING;
    this.animationTick = 0;
    this.gameUI.animationPlayingButton.deactivate();
    this.gameUI.animationPlayingButton.tooltip = StringConstants.TOOLTIP_ANIM_CLICK_TO_PLAY;
  }

  private boolean haveBuildEvent(final StarSystem system, final int phase) {
    return this.buildEvents.stream().anyMatch(event -> event.system == system && event.phase == phase);
  }

  final StarSystem systemHexAtPoint(final int x, final int y) {
    if (x >= 0 && y >= 0) {
      return Arrays.stream(this.map.systems)
          .filter(system -> this.systemBounds[system.index].contains(x, y)
              && hitTest(this.systemHexes[system.index], x, y))
          .findFirst().orElse(null);
    }
    return null;
  }

  public final void a021(final int var1, final StarSystem var2, final float var3) {
    final Rect var4 = this.gameUI.b520();
    float var5 = (float) (var4.x2 - ShatteredPlansClient.SCREEN_WIDTH + var4.x1 >> 1);
    var5 *= ((float) var2._z + var3) / 300.0F;
    this.targetScrollPosnX = -var5 + (float) var2.posnX;
    if (this.targetScrollPosnX < 0.0F) {
      this.targetScrollPosnX = 0.0F;
    }

    float v = (float) (var4.y2 + (var4.y1 - this.gameUI.getHeight()) >> 1);
    v *= (var3 + (float) var2._z) / 300.0F;
    this.targetScrollPosnY = (float) var2.posnY - v;
    this.targetZoomFactor = var3;
    if (var1 >= 7) {
      this.isAnimatingViewport = true;
      this._fb = null;
      if (this.targetScrollPosnY < 0.0F) {
        this.targetScrollPosnY = 0.0F;
      }

    }
  }

  final void drawBackground() {
    final double x = (double) -this.mapScrollPosnX * (ShatteredPlansClient.STAR_FIELD.width - ShatteredPlansClient.ORIGINAL_SCREEN_WIDTH) / this.map.drawingWidth;
    final double y = (double) -this.mapScrollPosnY * (ShatteredPlansClient.STAR_FIELD.height - ShatteredPlansClient.ORIGINAL_SCREEN_HEIGHT) / this.map.drawingHeight;
    ShatteredPlansClient.drawStarField(x, y);
  }

  public final void c487() {
    this._fb = null;
    this.isAnimatingViewport = true;
    this.targetScrollPosnY = (float) (this.map.drawingHeight / 2);
    this.targetZoomFactor = this.maxUnitScalingFactor;
    this.targetScrollPosnX = (float) (this.map.drawingWidth / 2);
  }

  public final void assignSystemState(final int[] remainingGarrisons, final ContiguousForce[] systemForces, final Player[] systemOwners, final boolean shouldClone) {
    if (shouldClone) {
      this.clonedSystemOwners = systemOwners.clone();
      this.clonedRemainingGarrisons = remainingGarrisons.clone();
    }

    this.systemForces = systemForces;
    this.remainingGarrisons = remainingGarrisons;
    this.sessionSystemOwners = systemOwners;
  }

  public final void advanceAnimationPhase(@MagicConstant(valuesFromClass = AnimationPhase.class) final int phase,
                                          final Collection<TurnEventLog.Event> turnEvents) {
    if (turnEvents != null) {
      if (phase == AnimationPhase.BUILD) {
        final int var13 = this.systemOwners.length;

        for (int var9 = 0; var9 < var13; ++var9) {
          this.clonedSystemOwners[var9] = this.systemOwners[var9];
          this.clonedRemainingGarrisons[var9] = this.systemGarrisons[var9];
        }

        for (final TurnEventLog.Event var10 : turnEvents) {
          if (var10 instanceof BuildFleetsEvent var16) {
            this.clonedRemainingGarrisons[var16.system.index] += var16.quantity;
          } else if (var10 instanceof StellarBombEvent var15) {
            this.clonedRemainingGarrisons[var15.target.index] -= var15.kill;
          } else if (var10 instanceof MoveFleetsOrder var14) {
            this.clonedRemainingGarrisons[var14.source.index] -= var14.quantity;
          }
        }

        this.gameUI.setActionHint(StringConstants.TEXT_ANIMATING_MOVES);
      } else if (phase == AnimationPhase.COMBAT) {
        for (final TurnEventLog.Event var4 : turnEvents) {
          if (var4 instanceof MoveFleetsOrder var5) {
            final int var6 = var5.target.index;
            if (var5.player == this.systemOwners[var6]) {
              this.clonedRemainingGarrisons[var6] += var5.quantity;
            }
          }
        }

        this.random = new IsaacRandom(this.turnSeed);

        for (final CombatEngagementAnimationState var8 : this.combatEngagements) {
          var8.reset();
        }

        this.gameUI.setActionHint(StringConstants.TEXT_ANIMATING_COMBAT);
      } else if (phase == AnimationPhase.POST_COMBAT) {
        for (final CombatEngagementAnimationState var8 : this.combatEngagements) {
          final int var9 = var8.system.index;
          this.clonedSystemOwners[var9] = var8.victor;
          this.clonedRemainingGarrisons[var9] = var8.fleetsAtCombatEnd;
        }

        for (final TurnEventLog.Event var10 : turnEvents) {
          if (var10 instanceof CombatEngagementLog var12) {

            for (final CombatLogEvent var7 : var12.events) {
              if (var7.source != null && var7.source.owner == var7.player) {
                this.clonedRemainingGarrisons[var7.source.index] += var7.fleetsRetreated;
              }
            }
          }
        }

        this.gameUI.setActionHint(StringConstants.SHOWING_COMBAT_RESULTS);
      } else if (phase == AnimationPhase.RETREAT) {
        for (final TurnEventLog.Event var4 : turnEvents) {
          if (var4 instanceof FleetRetreatEvent var11) {
            final int var6 = var11.source.index;
            this.clonedSystemOwners[var6] = null;
            this.clonedRemainingGarrisons[var6] = 0;
          }
        }

        this.gameUI.setActionHint(StringConstants.TEXT_ANIMATING_COLLAPSE);
      } else if (phase == AnimationPhase.NOT_PLAYING) {
        this.assignSystemState(this.remainingGarrisons, this.systemForces, this.sessionSystemOwners, true);
        this.gameUI.setActionHint(null);
      }

      this.animationPhase = phase;
      this.animationTick = 0;
    }
  }

  protected void a487() {
    final int offsetX = ShatteredPlansClient.SCREEN_WIDTH / 2;
    final int offsetY = this.gameUI.getHeight() / 2;

    if (this.systemHexes == null || this.map.systems.length != this.systemHexes.length) {
      final int systemCount = this.map.systems.length;
      this.systemDrawX = new int[systemCount];
      this.systemBounds = new Rect[systemCount];
      this.systemDrawY = new int[systemCount];
      this.systemHexes = new int[systemCount][];

      for (int var5 = 0; var5 < systemCount; ++var5) {
        this.systemBounds[var5] = new Rect();
        this.systemHexes[var5] = new int[2 * this.map.systems[var5].hexPoints.length];
      }
    }

    this._n = (int) (12000.0F / this.unitScalingFactor);

    for (final StarSystem system : this.map.systems) {
      final int i = system.index;
      this.systemDrawX[i] = (int) (((float) system.posnX - this.mapScrollPosnX) * 300.0F / ((float) system._z + this.unitScalingFactor)) + offsetX;
      this.systemDrawY[i] = (int) (((float) system.posnY - this.mapScrollPosnY) * 300.0F / ((float) system._z + this.unitScalingFactor)) + offsetY;

      for (int j = 0; j < system.hexPoints.length; ++j) {
        final Point p = system.hexPoints[j];

        final float x = (((float) p.x - this.mapScrollPosnX) * 300.0F / this.unitScalingFactor) + offsetX;
        final float y = (((float) p.y - this.mapScrollPosnY) * 300.0F / this.unitScalingFactor) + (float) offsetY;
        this.systemHexes[i][2 * j] = (int) x;
        this.systemHexes[i][2 * j + 1] = (int) y;

        if (x < (float) this.systemBounds[i].x1) {
          this.systemBounds[i].x1 = (int) x;
        }
        if (y < (float) this.systemBounds[i].y1) {
          this.systemBounds[i].y1 = (int) y;
        }
        if (x > (float) this.systemBounds[i].x2) {
          this.systemBounds[i].x2 = (int) x;
        }
        if (y > (float) this.systemBounds[i].y2) {
          this.systemBounds[i].y2 = (int) y;
        }
      }
    }
  }

  private boolean haveTannhauserEvent(final int phase) {
    return this.buildEvents.stream().anyMatch(event -> event.projectType == GameState.ResourceType.EXOTICS && event.phase == phase);
  }

  public final Optional<String> setTurnEvents(final TurnEventLog eventLog) {
    boolean errorOccurred = false;
    final StringBuilder errorMessage = new StringBuilder();

    this.clearTurnEvents();

    for (final TurnEventLog.Event event : eventLog.events) {
      if (event instanceof MoveFleetsOrder moveOrder) {
        if (moveOrder.player == null) {
          errorOccurred = true;
          errorMessage.append("Fleet from ").append(moveOrder.source.name)
              .append(" to ").append(moveOrder.target.name).append(" has no owner.\n");
        } else {
          this.fleetMovements.add(new MoveFleetsAnimationState(moveOrder));
          if (moveOrder.quantity > this.largestFleetMovement) {
            this.largestFleetMovement = moveOrder.quantity;
          }
        }
      } else if (event instanceof CombatEngagementLog combatLog) {
        this.combatEngagements.add(new CombatEngagementAnimationState(combatLog));

        for (final CombatLogEvent combatEvent : combatLog.events) {
          if (combatEvent.source != null && combatEvent.fleetsRetreated != 0 && combatEvent.player == combatEvent.source.owner) {
            final MoveFleetsAnimationState moveState = new MoveFleetsAnimationState(new MoveFleetsOrder(combatEvent.player, combatLog.system, combatEvent.source, combatEvent.fleetsRetreated));
            this.combatRetreats.add(moveState);
            if (moveState.quantity > this.largestFleetMovement) {
              this.largestFleetMovement = moveState.quantity;
            }
          }
        }
      } else if (event instanceof ProjectOrder projectEvent) {
        if (projectEvent.type == GameState.ResourceType.METAL) {
          this.addBuildProjectEvent(projectEvent.target, projectEvent.player, GameState.ResourceType.METAL);
        } else if (projectEvent.type == GameState.ResourceType.BIOMASS) {
          this.addBuildProjectEvent(projectEvent.target, projectEvent.player, GameState.ResourceType.BIOMASS);
        } else if (projectEvent.type == GameState.ResourceType.EXOTICS) {
          this.addBuildTannhauserEvent(projectEvent.player, projectEvent.source, projectEvent.target);
        }
      } else if (event instanceof StellarBombEvent bombEvent) {
        this.addBuildProjectEvent(bombEvent.target, bombEvent.player, GameState.ResourceType.ENERGY);
      } else if (event instanceof FleetRetreatEvent retreatEvent) {
        if (retreatEvent.targets != null) {
          for (int i = 0; i < retreatEvent.targets.length; ++i) {
            final MoveFleetsOrder moveOrder = new MoveFleetsOrder(retreatEvent.source.lastOwner, retreatEvent.source, retreatEvent.targets[i], retreatEvent.quantities[i]);
            final MoveFleetsAnimationState moveState = new MoveFleetsAnimationState(moveOrder);
            this.collapseRetreats.add(moveState);
            if (moveOrder.quantity > this.largestFleetMovement) {
              this.largestFleetMovement = moveOrder.quantity;
            }
          }
        }
        this.retreatTargets[retreatEvent.source.index] = true;
      }
    }

    for (final TurnEventLog.Event event : eventLog.events) {
      if (event instanceof BuildFleetsEvent buildEvent) {
        if (buildEvent.player == null) {
          errorOccurred = true;
          errorMessage.append("Build event at ").append(buildEvent.system.name).append(" has no owner.\n");
        } else if (buildEvent.player != this.localPlayer) {
          this.addBuildFleetsEvent(buildEvent.system, buildEvent.player, buildEvent.quantity);
          if (this.largestFleetBuildQuantity < buildEvent.quantity) {
            this.largestFleetBuildQuantity = buildEvent.quantity;
          }
        }
      }
    }

    return errorOccurred ? Optional.of(errorMessage.toString()) : Optional.empty();
  }

  @SuppressWarnings("WeakerAccess")
  public static final class AnimationPhase {
    public static final int NOT_PLAYING = -1;
    public static final int BUILD = 0;
    public static final int COMBAT = 1;
    public static final int POST_COMBAT = 2;
    public static final int RETREAT = 3;
  }

  public enum SystemHighlight {
    NONE, SOURCE, TARGET
  }
}
