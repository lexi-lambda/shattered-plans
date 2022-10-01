package funorb.shatteredplans.game.ai;

import funorb.shatteredplans.StringConstants;
import funorb.shatteredplans.client.ShatteredPlansClient;
import funorb.shatteredplans.client.game.ClientGameSession;
import funorb.shatteredplans.game.BuildFleetsOrder;
import funorb.shatteredplans.game.CombinedForce;
import funorb.shatteredplans.game.ContiguousForce;
import funorb.shatteredplans.game.Force;
import funorb.shatteredplans.game.GameState;
import funorb.shatteredplans.game.MoveFleetsOrder;
import funorb.shatteredplans.game.Player;
import funorb.shatteredplans.game.ProjectOrder;
import funorb.shatteredplans.map.StarSystem;
import org.intellij.lang.annotations.MagicConstant;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.stream.IntStream;

public class TutorialAI1 implements AI {
  private static final List<MoveFleetsOrder> _lhv = new ArrayList<>();
  private static final int[] _lhy = new int[4];
  private static final List<ProjectOrder> _lhG = new ArrayList<>();
  private static final List<BuildFleetsOrder> _lhD = new ArrayList<>();
  private static int[][] _ffx;
  private static int[] _igc;
  private static boolean[] _rlb;
  static ContiguousForce _mjyb;
  private static boolean[] _acq;
  private static int[] _eid;
  private static int _cqE;
  static int[] _jai;
  private static int _ahU = 0;
  private static int[] _uee;
  private static int[] _ola;
  private static int[] _smgi;
  private static int[] _cbo;
  private static int[] _oei;
  private static int[] _ud;
  private static int[] _uc;
  static int[] _nlb;
  final int _o;
  final Player _n;
  final int[] _b;
  public final int[] _i;
  final GameState gameState;
  private final ClientGameSession _j;
  int _k;
  private boolean isActive = true;
  private final int[] _c;

  public TutorialAI1(final GameState var1, final Player var2, final ClientGameSession var3) {
    this.gameState = var1;
    this._j = var3;
    this._n = var2;
    this._o = this.gameState.map.systems.length;
    this._i = new int[this.gameState.playerCount];
    this._b = new int[this.gameState.playerCount];
    this._c = new int[this.gameState.playerCount];
    this._k = -1;
    a093kd(this._o);
  }

  private static int[] a728ik(final int[] var0, final int var1) {
    final int[] var2 = new int[var1];

    int var4;
    for (int var3 = 1; var3 < var1; var2[1 + var4] = var3++) {
      var4 = var3;

      while (true) {
        --var4;
        if (var4 < 0 || var0[var3] >= var0[var2[var4]]) {
          break;
        }

        var2[var4 + 1] = var2[var4];
      }
    }

    return var2;
  }

  private static void a093kd(final int var0) {
    if (_ahU < var0) {
      _uc = new int[var0];
      _cbo = new int[var0];
      _ahU = var0;
      _nlb = new int[var0];
      _rlb = new boolean[var0];
      _ola = new int[var0];
      _smgi = new int[var0];
      _oei = new int[var0];
      _ud = new int[var0];
      _eid = new int[var0];
      _igc = new int[var0];
      _uee = new int[var0];
      _jai = new int[var0];
    }
  }

  private int a000(final int var2, int var3, final int[] var4) {
    int var5 = 0;
    final int var6 = var3;

    int var8;
    int var11;
    for (var8 = this._o - 1; var8 >= 0 && var3 != 0; --var8) {
      final int var9 = var4[var8];
      if (_ud[var9] == 0) {
        break;
      }

      if (_ola[var9] != 0) {
        int var10 = _ola[var9] * var6 / var2;
        if (var10 != 0) {
          if (var10 > var3) {
            var10 = var3;
          }

          this.c326(var3, _cqE);
          var11 = this.a259(true, var10, this.gameState.map.systems[var9]);
          if (var10 < var11) {
            throw new RuntimeException();
          }

          int[] var10000;
          if (var11 < var10) {
            var10000 = _uc;
            var10000[var9] += -var11 + var10;
          }

          var5 += var10;
          var3 -= var10;
          var10000 = _igc;
          var10000[var9] += var11;
          this.c326(var3, _cqE);
        }
      }
    }

    if (var3 > 0) {
      var8 = 0;

      StarSystem var12;
      StarSystem[] var15;
      for (final StarSystem var14 : _mjyb) {
        var15 = var14.neighbors;

        for (var11 = 0; var11 < var15.length; ++var11) {
          var12 = var15[var11];
          if (var12.owner != this._n) {
            ++var8;
            break;
          }
        }
      }

      if (var8 > 0) {
        for (final StarSystem var14 : _mjyb) {
          var15 = var14.neighbors;

          for (var11 = 0; var15.length > var11; ++var11) {
            var12 = var15[var11];
            if (this._n != var12.owner) {
              final int var13 = var3 / var8;
              this.a170(var13, var14);
              var3 -= var13;
              var5 += var13;
              --var8;
              break;
            }
          }
        }
      }
    }

    return var5;
  }

  private int a562(final StarSystem var2) {
    int var3 = (int) Arrays.stream(var2.neighbors)
        .filter(neighbor -> neighbor.owner != this._n)
        .filter(neighbor -> _lhv.stream().noneMatch(var7 -> var7.target == neighbor))
        .count();

    if (this.gameState.gameOptions.simpleGarrisoning && var3 > 1) {
      var3 = 1;
    }

    return var3;
  }

  private void a254(final ContiguousForce var1, final int[] var2) {
    if (this.gameState.gameOptions.unifiedTerritories) {
      this.c812(this._n.combinedForce);
    } else {
      this.c812(var1);
    }

    final StarSystem[] var4 = this.gameState.map.systems;

    for (final int var6 : var2) {
      final StarSystem var7 = var4[var6];
      final int var8 = this.c828(var7);
      final int var9 = this.a555(var7);
      _cbo[var6] = var8;
      _smgi[var6] = var9;
      _eid[var6] = this.d828(var7);
      _ud[var6] = (var8 << 16) / var9;
    }

  }

  private int a259(final boolean var2, final int var3, final StarSystem var4) {
    int var5 = var3;
    final StarSystem[] var6 = var4.neighbors;
    int var7 = 0;

    while (true) {
      if (var6.length > var7) {
        final StarSystem var8 = var6[var7];
        final int var9 = var8.index;
        if (_jai[var9] >= var5) {
          this.a847(var4, var8, var5);
          final int[] var10000 = _jai;
          var10000[var9] -= var5;
          return var3;
        }

        if (_jai[var9] != 0) {
          this.a847(var4, var8, _jai[var9]);
          var5 -= _jai[var9];
          _jai[var9] = 0;
        }

        if (var5 != 0) {
          ++var7;
          continue;
        }
      }

      if (var5 != 0 && var2 && _cqE > 0) {
        final int var10 = Math.min(var5, _cqE);
        var5 -= var10;
        final StarSystem[] var11 = var4.neighbors;

        for (final StarSystem var13 : var11) {
          if (var13.contiguousForce == _mjyb) {
            this.a583((byte) -67, var10, var13);
            this.a847(var4, var13, var10);
            break;
          }
        }
      }

      return var3 - var5;
    }
  }

  int d828(final StarSystem var1) {

    final int var3 = this.a562(var1);
    return var3 == 0 ? 1 : var3;
  }

  private void c812(final Force var2) {
    for (int var3 = 0; var3 < 4; ++var3) {
      _lhy[var3] = var2.surplusResources[var2.surplusResourceRanks[0]] - var2.surplusResources[var3] + 1;
    }
  }

  private int e137() {
    int var3 = 0;
    for (final StarSystem var2 : _mjyb) {
      final int var4 = var2.index;
      final int var5 = this.b263(var2);
      var3 += var5;
      _uee[var4] = var5;
    }
    return var3;
  }

  private int a986(final int var1, final Player var3) {
    final int var4 = var3.index;
    if (this._n == var3 || this._n.allies[var4]) {
      return 0;
    } else if (!this.gameState.isPlayerDefeated(var4)) {
      final Player var5 = this.gameState.players[this.gameState.playerFleetProductionRanks[this.gameState.playerCount - 1]];
      final Player var6 = this.gameState.players[this.gameState.playerFleetProductionRanks[this.gameState.playerCount - 2]];
      if (var5 == var3) {
        return 1;
      } else {
        int var7 = 0;
        int var8 = 0;
        final StarSystem[] var9 = this.gameState.map.systems;

        int var10;
        for (var10 = 0; var9.length > var10; ++var10) {
          final StarSystem var11 = var9[var10];
          if (var11.owner == this._n) {
            boolean var12 = false;
            final StarSystem[] var13 = var11.neighbors;

            for (final StarSystem var15 : var13) {
              if (var15.owner == var3) {
                var12 = true;
                var7 += var15.garrison;
              }
            }

            if (var12) {
              var8 += var11.garrison;
            }
          }
        }

        if (var7 > var8 * 2) {
          return -1;
        } else if (var5.allies[var4]) {
          return 2;
        } else if (var6 == var3) {
          return var5 != this._n ? -2 : -3;
        } else if (var7 < var8 && 2 * this.gameState.playerFleetProduction[this._n.index] > this.gameState.playerFleetProduction[var4] * 3) {
          return 3;
        } else {
          final int[] var16 = _ffx[this._n.index];
          var10 = var1;

          for (int var17 = 0; var17 < this._o; ++var17) {
            if (_acq[var17] && _ffx[var4][var17] <= var16[var17]) {
              --var10;
            }
          }

          if (var10 < 5 && var10 < var1) {
            return 4;
          } else {
            return -3;
          }
        }
      }
    } else {
      return 0;
    }
  }

  @Override
  public Player getPlayer() {
    return this._n;
  }

  @Override
  public final void planTurnOrders() {
    if (!this._n.contiguousForces.isEmpty()) {
      this.d150();
      _lhD.clear();
      _lhG.clear();
      _lhv.clear();

      boolean var3 = true;
      for (final ContiguousForce var2 : this._n.contiguousForces) {
        if (this.gameState.gameOptions.unifiedTerritories) {
          int var4 = this._n.combinedForce.fleetProduction / this._n.contiguousForces.size();
          if (this._n.combinedForce.fleetProduction % this._n.contiguousForces.size() != 0 && var3) {
            ++var4;
            var3 = false;
          }

          if (var4 < 0) {
            var4 = 0;
          }

          this.a024(var4, var2);
        } else {
          this.a024(var2.fleetsAvailableToBuild, var2);
        }
      }

      if (this.gameState.gameOptions.projectsAllowed) {
        if (this._n.researchPoints[0] >= 5) {
          this.h150();
        }

        if (this._n.researchPoints[1] >= 5) {
          this.f150();
        }

        if (this._n.researchPoints[2] >= 5) {
          this.b487();
        }

        if (this._n.researchPoints[3] >= 5) {
          this.e423();
        }
      }

      this.gameState.validateOrders(this._n, _lhG, _lhD, _lhv);

      this.gameState.addOrders(_lhG, _lhD, _lhv);
    }
  }

  @Override
  public final void handlePactAccepted(final Player ally) {
    final int var3 = this._b[ally.index];
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
      if (this._n.index != var5 && this._k != var5 && this.gameState.playerFleetProduction[var5] * 2 > 3 * this.gameState.playerFleetProduction[var6]) {
        var4 = StringConstants.AIMessage.ACCEPTED_COMMON_ENEMY;
      }
    } else {
      var4 = StringConstants.AIMessage.NONE;
    }

    if (var4 != StringConstants.AIMessage.NONE) {
      this._j.showAIChatMessage(this._n, ally, var4, 0);
    }

  }

  private int a313(int var1, final int var3) {
    int var5 = 0;
    int[] var10000;
    int var6;
    int var7;
    if (var3 > var1) {
      for (final StarSystem var4 : _mjyb) {
        var6 = var4.index;
        var7 = _uee[var6] * var1 / var3;
        var10000 = _oei;
        var10000[var6] += var7;
        var1 -= var7;
        var10000 = _nlb;
        var10000[var6] += var7;
        var5 += var7;
      }
    } else {
      for (final StarSystem var4 : _mjyb) {
        var6 = var4.index;
        var7 = _uee[var6];
        var10000 = _nlb;
        var10000[var6] += var7;
        var10000 = _oei;
        var10000[var6] += var7;
      }

      var5 += var3;
      var1 -= var3;
    }

    this.c326(var1, _cqE);
    return var5;
  }

  private int c828(final StarSystem var1) {
    return _cbo[var1.index] + IntStream.range(0, GameState.NUM_RESOURCES).map(i -> var1.resources[i] * _lhy[i]).sum();
  }

  private void h150() {
    StarSystem var2 = null;
    int var3 = 0;

    for (int var5 = 0; var5 < this._o; ++var5) {
      final StarSystem var6 = this.gameState.map.systems[var5];
      if (var6.owner == this._n && !var6.hasDefensiveNet) {
        int var7 = 0;

        int var8;
        for (var8 = 0; var8 < 4; ++var8) {
          var7 += var6.resources[var8];
        }

        var8 = (_oei[var5] * 10 + 1) * var7;
        if (var3 < var8) {
          var3 = var8;
          var2 = var6;
        }
      }
    }

    if (var2 != null) {
      final ProjectOrder var4 = new ProjectOrder(GameState.ResourceType.METAL, this._n, var2);
      _lhG.add(var4);
    }

  }

  private void c326(final int var1, final int var2) {
    if (var2 >= 0) {
      if (var1 < 0) {
        throw new RuntimeException();
      } else {
        int var4 = var2;

        for (int var5 = 0; var5 < this._o; ++var5) {
          final StarSystem var6 = this.gameState.map.systems[var5];
          var4 += _jai[var5];
          var4 -= _nlb[var5];
          var4 -= _uc[var5];
          if (_jai[var5] > var6.remainingGarrison) {
            throw new RuntimeException();
          }
        }

        if (var4 != var1) {
          throw new RuntimeException();
        }
      }
    } else {
      throw new RuntimeException();
    }
  }

  final int[] a931(final Player var2) {
    final int[] var3 = new int[this._o];
    final boolean[] var4 = new boolean[this._o];
    final StarSystem[] var5 = this.gameState.map.systems;

    for (int var6 = 0; var6 < this._o; ++var6) {
      final boolean var7 = var2 == var5[var6].owner;
      var4[var6] = var7;
      var3[var6] = var7 ? 0 : Integer.MAX_VALUE;
    }

    boolean var14 = true;

    while (var14) {
      var14 = false;

      for (int var15 = 0; this._o > var15; ++var15) {
        if (var4[var15]) {
          var4[var15] = false;
          final StarSystem var8 = var5[var15];
          final int var9 = var3[var15] + 1;
          final StarSystem[] var10 = var8.neighbors;

          for (final StarSystem var12 : var10) {
            final int var13 = var12.index;
            if (var3[var13] > var9) {
              var3[var13] = var9;
              var4[var13] = true;
              var14 = true;
            }
          }
        }
      }
    }

    return var3;
  }

  private void a024(int var1, final ContiguousForce var2) {
    _cqE = var1;
    _mjyb = var2;
    this.c150();
    if (this.gameState.gameOptions.unifiedTerritories) {
      this.c812(this._n.combinedForce);
    } else {
      this.c812(var2);
    }

    final int[] var4 = this.b536(var2);
    this.a254(var2, var4);
    this.c326(var1, _cqE);
    var1 -= this.a543();
    if (var1 >= 0) {
      this.c326(var1, _cqE);
      if (!this.gameState.gameOptions.simpleGarrisoning && !this.gameState.gameOptions.noChainCollapsing) {
        var1 -= this.b080(var1);
      }

      this.c326(var1, _cqE);
      if (var1 == 0) {
        this.a812(var2);
      } else {
        final int var12 = this.e137();
        int[] var13 = null;

        int var8 = 0;
        if (this.isActive) {
          int var9 = this.a353(var1);
          var13 = a728ik(_ud, this._o);
          if (var9 > var12) {
            var9 = var12;
          }

          this.c326(var1, _cqE);
          var1 -= var9;
          var8 = this.a365(var9, var1, var4, var13);
          var1 -= var8;
          var1 += var9;
          this.c326(var1, _cqE);
        }

        var1 -= this.a313(var1, var12);
        this.c326(var1, _cqE);
        if (this.isActive && var1 > 0) {
          var1 -= this.a000(var8, var1, var13);
        }

        if (var1 != 0) {
          this.a170(var1, var2.getCapital());
        }

        this.a812(var2);
      }
    } else {
      final Iterator<StarSystem> it = var2.iterator();
      StarSystem var6 = it.next();
      int var7 = Arrays.stream(var6.resources).sum();

      while (it.hasNext()) {
        final StarSystem var5 = it.next();
        final int var8 = var5.index;
        var1 += _nlb[var8];
        _nlb[var8] = 0;

        final int var9 = Arrays.stream(var5.resources).sum();
        if (var9 > var7) {
          var7 = var9;
          var6 = var5;
        }
      }

      _nlb[var6.index] = var1;
      this.a812(var2);
    }
  }

  private void c150() {
    for (int var2 = 0; var2 < this._o; ++var2) {
      _ola[var2] = 0;
      _uee[var2] = 0;
      _nlb[var2] = 0;
      _jai[var2] = 0;
      _uc[var2] = 0;
      _rlb[var2] = false;
    }

  }

  @Override
  public final void initialize() {
    final int[] var2 = this.b878();
    if (var2.length != 0) {
      final Player[] var3 = this.gameState.players;

      int var4;
      for (var4 = 0; var4 < var3.length; ++var4) {
        final Player var5 = var3[var4];
        if (var5 != this._n) {
          @MagicConstant(valuesFromClass = StringConstants.AIMessage.class)
          final int var6 = var2[ShatteredPlansClient.randomIntBounded(var2.length)];
          this._j.showAIChatMessage(this._n, var5, var6, 0);
        }
      }

    }
  }

  int a543() {
    int var3 = 0;
    for (final StarSystem var4 : _mjyb) {
      final int var5 = var4.index;
      final int var6;
      if (this.gameState.gameOptions.simpleGarrisoning) {
        var6 = 1;
      } else {
        var6 = (int) Arrays.stream(var4.neighbors).filter(neighbor -> neighbor.owner != this._n).count();
      }

      final int[] var10000 = _jai;
      var10000[var5] += var4.garrison;
      _nlb[var5] = var6;
      final int var10 = -var4.garrison + var6;
      var3 += var10;
    }

    return var3;
  }

  int a353(final int var1) {

    return 3 * var1 >> 2;
  }

  private int[] b878() {
    return new int[]{StringConstants.AIMessage.AGGRESSIVE_2, StringConstants.AIMessage.DEFENSIVE_1};
  }

  private void a847(final StarSystem var1, final StarSystem var2, int var3) {
    if (var2 == null || var1 == null) {
      throw new RuntimeException();
    } else if (var3 <= 0) {
      throw new RuntimeException();
    } else if (var2.owner != this._n) {
      throw new RuntimeException();
    } else if (var1.contiguousForce == var2.contiguousForce || var2.hasNeighbor(var1)) {
      final MoveFleetsOrder var5 = var2.incomingOrders.stream()
          .filter(var6 -> var6.player == this._n && var1 == var6.source).findFirst().orElse(null);

      if (var5 != null) {
        final StarSystem var10000;
        if (var5.quantity > var3) {
          var5.quantity -= var3;
          var10000 = var5.source;
          var10000.remainingGarrison += var3;
          return;
        }

        var5.source.outgoingOrders.remove(var5);
        var5.target.incomingOrders.remove(var5);
        this.gameState.moveOrders.remove(var5);
        var10000 = var5.source;
        var10000.remainingGarrison += var5.quantity;
        var3 -= var5.quantity;
      }

      if (var3 != 0) {
        if (var3 > var2.remainingGarrison) {
          throw new RuntimeException();
        } else {
          var2.remainingGarrison -= var3;

          final MoveFleetsOrder mg_ = _lhv.stream()
              .filter(mg1 -> mg1.player == this._n && var2 == mg1.source && var1 == mg1.target)
              .findFirst().orElse(null);
          if (mg_ == null) {
            final MoveFleetsOrder mg = new MoveFleetsOrder(var2, var1, var3);
            _lhv.add(mg);
          } else {
            mg_.quantity += var3;
          }
        }
      }
    } else {
      throw new RuntimeException();
    }
  }

  private void a812(final ContiguousForce var2) {
    this.c326(0, _cqE);
    int var3 = 0;

    int var4;
    for (; this._o > var3; ++var3) {
      if (_uc[var3] != 0) {
        final StarSystem var5 = this.gameState.map.systems[var3];
        StarSystem var6 = null;
        final StarSystem[] var7 = var5.neighbors;

        for (final StarSystem var9 : var7) {
          if (var2 == var9.contiguousForce && (var6 == null || var6.remainingGarrison < var9.remainingGarrison)) {
            var6 = var9;
          }
        }

        this.a170(_uc[var3], var6);
        _uc[var3] = 0;
        this.c326(0, _cqE);
      }
    }

    for (final StarSystem var10 : var2) {
      var4 = var10.index;
      final int var11 = _nlb[var4];
      if (var11 != 0) {
        this.a170(var11, var10);
      }

      _nlb[var4] = 0;
      this.c326(0, _cqE);
    }

  }

  private void f150() {
    StarSystem var2 = null;
    int var3 = 0;
    int var4 = 0;
    boolean var7;
    int var8;
    int var9;
    if (this.gameState.gameOptions.unifiedTerritories) {
      final CombinedForce var10 = this._n.combinedForce;

      for (final StarSystem var6 : var10) {
        var7 = false;
        var8 = 0;

        for (var9 = 0; var9 < 4; ++var9) {
          var8 += var6.resources[var9];
          if (var10.surplusResources[var9] < var6.resources[var9]) {
            var7 = true;
            break;
          }
        }

        if (!var7) {
          var9 = this.a263(var6);
          if (var2 == null || var8 < var3 || var8 == var3 && var4 > var9) {
            var3 = var8;
            var4 = var9;
            var2 = var6;
          }
        }
      }
    } else {
      for (final ContiguousForce var5 : this._n.contiguousForces) {
        for (final StarSystem var6 : var5) {
          var7 = false;
          var8 = 0;

          for (var9 = 0; var9 < 4; ++var9) {
            var8 += var6.resources[var9];
            if (var5.surplusResources[var9] < var6.resources[var9]) {
              var7 = true;
              break;
            }
          }

          if (!var7) {
            var9 = this.a263(var6);
            if (var2 == null || var8 < var3 || var8 == var3 && var9 < var4) {
              var4 = var9;
              var3 = var8;
              var2 = var6;
            }
          }
        }
      }
    }

    if (var2 != null) {
      final ProjectOrder var11 = new ProjectOrder(GameState.ResourceType.BIOMASS, this._n, var2);
      _lhG.add(var11);
    }

  }

  private int a365(final int var1, int var2, final int[] var3, final int[] var4) {
    int var6 = 0;

    for (int var7 = -1 + this._o; var7 >= 0; --var7) {
      final int var8 = var4[var7];
      final StarSystem var9 = this.gameState.map.systems[var8];

      int var10 = 0;
      while (var10 < var3.length && var3[var10] != var8) {
        ++var10;
      }

      if (var10 != var3.length) {
        this.c326(var1 + var2, _cqE);
        if (_ud[var8] == 0) {
          break;
        }

        if (_eid[var8] <= var2) {
          final int var11 = this.a398(var9, var2);
          var2 -= var11;
          var6 += var11;
          this.c326(var1 + var2, _cqE);
          if (var2 == 0) {
            break;
          }
        }
      }
    }

    return var6;
  }

  int b080(int var1) {
    int var3 = 0;

    for (final StarSystem var4 : _mjyb) {
      boolean var5 = false;
      final StarSystem[] var6 = var4.neighbors;

      int var7;
      StarSystem var8;
      for (var7 = 0; var6.length > var7; ++var7) {
        var8 = var6[var7];
        if (var8.owner != null && this._n != var8.owner && !this._n.allies[var8.owner.index]) {
          var5 = true;
          break;
        }
      }

      if (var5) {

        for (var7 = 0; var6.length > var7; ++var7) {
          var8 = var6[var7];
          if (this._n == var8.owner) {
            if (var1 == 0) {
              return var3;
            }

            --var1;
            _nlb[var8.index]++;
            ++var3;
          }
        }
      }
    }

    return var3;
  }

  @Override
  public final void handlePactOffer(final Player offerer) {
    this._j.handleAIPactOffer(this._n, offerer);

    final int var3 = this._b[offerer.index];
    @MagicConstant(valuesFromClass = StringConstants.AIMessage.class)
    int var4;
    if (var3 == 1) {
      var4 = StringConstants.AIMessage.REJECT_TOO_STRONG;
    } else if (var3 == 2) {
      var4 = StringConstants.AIMessage.REJECT_ENEMY_ALLY;
    } else if (var3 == 3) {
      var4 = StringConstants.AIMessage.REJECT_TOO_WEAK;
    } else if (var3 == 4) {
      var4 = StringConstants.AIMessage.REJECT_WANT_CAPTURE;
    } else if (var3 == 5) {
      var4 = StringConstants.AIMessage.AGGRESSIVE_1;
    } else if (var3 == -1) {
      var4 = StringConstants.AIMessage.ACCEPT_HARMLESS;
    } else if (var3 == -2) {
      var4 = StringConstants.AIMessage.ACCEPT_COMMON_ENEMY;
    } else if (var3 == -3) {
      final int var5 = this.gameState.playerFleetProductionRanks[this.gameState.playerCount - 1];
      final int var6 = this.gameState.playerFleetProductionRanks[this.gameState.playerCount - 2];
      var4 = StringConstants.AIMessage.ACCEPT_NEUTRAL;
      if (this._n.index != var5 && this._k != var5 && 2 * this.gameState.playerFleetProduction[var5] > 3 * this.gameState.playerFleetProduction[var6]) {
        var4 = StringConstants.AIMessage.ACCEPT_COMMON_ENEMY;
      }
    } else {
      var4 = StringConstants.AIMessage.NONE;
    }

    if (var4 != StringConstants.AIMessage.NONE) {
      this._j.showAIChatMessage(this._n, offerer, var4, 0);
    }

    if (this.isActive && !this._n.allies[offerer.index] && var3 < 0) {
      this._j.handleAIPactOffer(this._n, offerer);
    }

  }

  private int[] b536(final ContiguousForce var2) {
    int var3 = 0;

    int var6;
    for (final StarSystem var4 : var2) {
      final StarSystem[] var5 = var4.neighbors;

      for (var6 = 0; var6 < var5.length; ++var6) {
        final StarSystem var7 = var5[var6];
        if (this._n != var7.owner && (var7.owner == null || !this._n.allies[var7.owner.index]) && !_rlb[var7.index]) {
          _rlb[var7.index] = true;
          ++var3;
        }
      }
    }

    final int[] var8 = new int[var3];

    for (var6 = 0; this._o > var6; ++var6) {
      if (_rlb[var6]) {
        --var3;
        var8[var3] = var6;
      }
    }

    return var8;
  }

  private void b487() {
    StarSystem var2 = null;
    int var3 = 0;

    for (int var4 = 0; this._o > var4; ++var4) {
      final int var5 = _smgi[var4];
      if (var5 >= 6) {
        final int var6 = _cbo[var4];
        final int var7 = var5 * var6 * var6;
        if (var3 < var7) {
          var3 = var7;
          var2 = this.gameState.map.systems[var4];
        }
      }
    }

    if (var2 != null) {
      final ProjectOrder var8 = new ProjectOrder(GameState.ResourceType.ENERGY, this._n, var2);
      _lhG.add(var8);
    }

  }

  private void e423() {
    StarSystem var2 = null;
    int var3 = 0;

    StarSystem var5;
    int var6;
    for (int var4 = 0; var4 < this._o; ++var4) {
      var5 = this.gameState.map.systems[var4];
      if (var5.owner == this._n) {
        var6 = _oei[var4];
        if (var5.hasDefensiveNet) {
          var6 = 2 * var6 + 5;
        }

        if (var3 < var6) {
          var2 = var5;
          var3 = var6;
        }
      }
    }

    if (var2 != null) {
      final ContiguousForce var14 = var2.contiguousForce;
      if (var14 != null) {
        var5 = null;
        var6 = 0;

        for (int var7 = 0; this._o > var7; ++var7) {
          final StarSystem var8 = this.gameState.map.systems[var7];
          if (var8.contiguousForce != var2.contiguousForce && (var8.owner == this._n || var8.owner == null || !this._n.allies[var8.owner.index])) {
            boolean var9 = false;
            final StarSystem[] var10 = var8.neighbors;

            int var11;
            for (var11 = 0; var11 < var10.length; ++var11) {
              final StarSystem var12 = var10[var11];
              if (var12.contiguousForce == var2.contiguousForce) {
                var9 = true;
                break;
              }
            }

            if (!var9) {
              int var16 = 0;

              int var18;
              for (var18 = 0; var18 < 4; ++var18) {
                var16 += (1 + (var14.surplusResources[var18] - var14.surplusResourceRanks[0])) * var8.resources[var18];
              }

              var11 = var8.garrison;
              if (var8.hasDefensiveNet) {
                var11 = 5 + 2 * var11;
              }

              var18 = this.a562(var8);
              var11 += var18;
              var11 += this.a263(var8);
              if (var11 < 1) {
                var11 = 1;
              }

              final int var13 = (var16 << 16) / var11;
              if (var6 < var13) {
                var5 = var8;
                var6 = var13;
              }
            }
          }
        }

        if (var5 != null) {
          final ProjectOrder var15 = new ProjectOrder(this._n, var5, var2);
          _lhG.add(var15);
        }

      }
    }
  }

  final int a263(final StarSystem var2) {
    return Arrays.stream(var2.neighbors)
        .filter(neighbor -> neighbor.owner != null && this._n != neighbor.owner && !this._n.allies[neighbor.owner.index])
        .mapToInt(neighbor -> neighbor.garrison)
        .sum();
  }

  private int a828(final StarSystem var1) {
    return Arrays.stream(var1.neighbors).mapToInt(neighbor -> _jai[neighbor.index]).sum();
  }

  private void a170(int var1, final StarSystem var3) {
    if (var1 < 0) {
      throw new RuntimeException();
    }
    if (var1 != 0) {
      final int[] var4 = this.gameState.map.distances[var3.index];
      final int[] var6 = GameState.calculateRanksAscending(var4);

      for (int var7 = 0; this._o > var7; ++var7) {
        if (var6[var7] != -1) {
          final int var8 = var6[var7];
          final StarSystem var9 = this.gameState.map.systems[var8];
          final int var10 = _jai[var8];
          if (var1 <= var10) {
            if (var3 != var9) {
              this.a847(var3, var9, var1);
            }

            _jai[var8] = var10 - var1;
            return;
          }

          if (var10 != 0) {
            if (var9 != var3) {
              this.a847(var3, var9, var10);
            }

            var1 -= var10;
            _jai[var8] = 0;
          }
        }
      }

      if (var1 != 0) {
        if (_cqE < var1) {
          throw new RuntimeException();
        }

        this.a583((byte) -113, var1, var3);
      }
    }
  }

  int b263(final StarSystem var2) {
    int var4 = this.a263(var2);
    if (var2.hasDefensiveNet) {
      var4 >>= 1;
    }

    return var4;
  }

  private int a398(final StarSystem var1, final int var2) {
    final int var4 = var1.index;
    int var5 = this.b828(var1);
    if (var5 > var2) {
      var5 = var2;
    }

    if (this.a828(var1) < _eid[var4]) {
      _uc[var4] = var5;
    } else {
      _ola[var4] = var5;
      final int var6 = this.a259(false, var5, var1);
      if (var5 < var6) {
        throw new RuntimeException();
      }

      if (var6 < var5) {
        _uc[var4] = var5 - var6;
      }

      final int[] var10000 = _igc;
      var10000[var4] += var6;
    }

    return var5;
  }

  int a555(final StarSystem var1) {

    int var3 = var1.garrison;
    if (var1.hasDefensiveNet) {
      var3 = 2 * (var3 + 5);
    }

    var3 += this.a562(var1);
    var3 += this.a263(var1);
    if (var3 < 1) {
      var3 = 1;
    }

    return var3;
  }

  private void d150() {
    int var2 = 0;

    while (var2 < this._o) {
      _cbo[var2] = 0;
      _smgi[var2] = 0;
      _eid[var2] = 0;
      _ud[var2] = 0;
      _oei[var2] = 0;
      _igc[var2] = 0;
      ++var2;
    }
  }

  @Override
  public final void initialize(final boolean isActive) {
    this.isActive = isActive;
  }

  @Override
  public final int getType() {
    return Type.TUTORIAL;
  }

  @Override
  public void makeDesiredPactOffers() {
    if (this._n.contiguousForces.isEmpty()) {
      this.isActive = false;
    }

    final int var2 = this.gameState.playerFleetProductionRanks[this.gameState.playerCount - 1];
    final int var3 = this.gameState.playerFleetProduction[var2];

    for (int var4 = 0; var4 < this.gameState.playerCount; ++var4) {
      this._c[var4]--;
      if (var4 == var2) {
        this._i[var4] = 3;
      } else if (this.gameState.playerFleetProduction[var4] * 2 < var3) {
        this._i[var4] = 0;
      } else {
        this._i[var4]--;
      }
    }

    if (this.isActive && this.gameState.gameOptions.diplomacyAllowed) {
      this.g150();
      if (this._k != -1) {
        final Player var9 = this.gameState.players[this._k];
        if (this._c[this._k] <= 0) {
          final int var8 = this._b[this._k];
          @MagicConstant(valuesFromClass = StringConstants.AIMessage.class)
          int var5;
          if (var8 == -1) {
            var5 = StringConstants.AIMessage.ACCEPT_INTIMIDATED;
          } else if (var8 == -2) {
            var5 = StringConstants.AIMessage.REQUEST_COMMON_ENEMY;
          } else if (var8 == -3) {
            var5 = StringConstants.AIMessage.REQUEST_LEADERS;
            final int var7 = this.gameState.playerFleetProductionRanks[this.gameState.playerCount - 2];
            final int var6 = this.gameState.playerFleetProductionRanks[this.gameState.playerCount - 1];
            if (this._n.index != var6 && var6 != this._k && 2 * this.gameState.playerFleetProduction[var6] > this.gameState.playerFleetProduction[var7] * 3) {
              var5 = StringConstants.AIMessage.REQUEST_COMMON_ENEMY;
            }
          } else {
            var5 = StringConstants.AIMessage.NONE;
          }

          if (var5 != StringConstants.AIMessage.NONE) {
            this._j.showAIChatMessage(this._n, var9, var5, 0);
            this._c[this._k] = ShatteredPlansClient.randomIntBounded(3, 6);
          }
        }

        this._j.handleAIPactOffer(this._n, var9);
      }
    }

  }

  private int b828(final StarSystem var1) {

    return _smgi[var1.index];
  }

  void g150() {
    final int var2 = this.gameState.playerCount;
    _ffx = new int[var2][];
    this._k = -1;
    _acq = new boolean[this._o];

    for (int var3 = 0; var3 < var2; ++var3) {
      _ffx[var3] = this.a931(this.gameState.players[var3]);
    }

    final int[] var8 = _ffx[this._n.index];
    int var4 = 0;

    int var5;
    for (var5 = 0; var5 < this._o; ++var5) {
      if (this.gameState.map.systems[var5].owner == this._n) {
        _acq[var5] = false;
      } else {
        boolean var6 = true;

        for (int var7 = 0; var7 < var2; ++var7) {
          if (var7 != this._n.index && this._n.allies[var7] && var8[var5] >= _ffx[var7][var5]) {
            var6 = false;
            break;
          }
        }

        if (var6) {
          ++var4;
        }

        _acq[var5] = var6;
      }
    }

    for (var5 = 0; var2 > var5; ++var5) {
      this._b[var5] = this.a986(var4, this.gameState.players[var5]);
    }

    for (var5 = 0; var2 > var5; ++var5) {
      final int var9 = this.gameState.playerFleetProductionRanks[var5];
      if (this._b[var9] < 0) {
        this._k = var9;
      }
    }

  }

  private void a583(final byte var1, final int var2, final StarSystem var3) {
    if (var1 < -37) {
      var3.remainingGarrison += var2;
      _cqE -= var2;

      for (final BuildFleetsOrder var4 : _lhD) {
        if (var4.system == var3) {
          var4.quantity += var2;
          return;
        }
      }

      final BuildFleetsOrder var4 = new BuildFleetsOrder(var3, var2);
      _lhD.add(var4);
    }
  }
}
