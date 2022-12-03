package funorb.shatteredplans.client.game;

import funorb.Strings;
import funorb.awt.MouseState;
import funorb.client.TemplateDictionary;
import funorb.graphics.Drawing;
import funorb.graphics.Rect;
import funorb.graphics.Sprite;
import funorb.shatteredplans.StringConstants;
import funorb.shatteredplans.client.GameUI;
import funorb.shatteredplans.client.JagexApplet;
import funorb.shatteredplans.client.Menu;
import funorb.shatteredplans.client.ShatteredPlansClient;
import funorb.shatteredplans.client.TutorialMessage;
import funorb.shatteredplans.client.TutorialMessageId;
import funorb.shatteredplans.client.TutorialMessages;
import funorb.shatteredplans.game.CombinedForce;
import funorb.shatteredplans.game.ContiguousForce;
import funorb.shatteredplans.game.GameOptions;
import funorb.shatteredplans.game.GameState;
import funorb.shatteredplans.game.Player;
import funorb.shatteredplans.map.Map;
import funorb.shatteredplans.map.StarSystem;
import funorb.shatteredplans.map.generator.MapGenerationFailure;
import funorb.shatteredplans.map.generator.TutorialMapGenerator;
import funorb.util.MathUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public final class TutorialState {
  public static int stage;
  public static boolean _jau;
  public static boolean _tdL;
  public static boolean _erg;
  public static boolean _phg;
  public static boolean _vcj;
  public static boolean _sek;
  public static Player _hod;

  private static ClientGameSession session;
  private static Sprite[][] _spm;
  private static TutorialMessage _hmq;
  private static int _eie;
  private static TutorialMessage _olg;
  private static int _ef;
  private static int _fkk;
  private static int _klo;
  private static TemplateDictionary templateDictionary;
  private static List<TutorialMessageId> _aja;
  private static List<TutorialMessageId> _qih;
  private static boolean _isb;
  private static int _gei;
  private static TutorialMessage[] _jsb;
  private static TutorialMapGenerator mapGenerator;
  private static int _beh;
  private static int _feB;
  private static int[] _rgf;
  private static int _dmgq;
  private static int _oia;
  private static Sprite[][] _oii;
  private static Sprite[] _ehL;
  private static int _tec;
  private static int _tpb;
  private static int _kpj;
  private static int _jcr;
  private static int _jbd;
  private static StarSystem _idd;
  private static int _ejm;
  private static StarSystem _dd;

  private TutorialState() {}

  static void initialize(final ClientGameSession session) {
    TutorialState.session = session;
    templateDictionary = new TemplateDictionary(ShatteredPlansClient.templateDictionary);
    mapGenerator = new TutorialMapGenerator();
    stage = 1;
    _hmq = null;
    _eie = -1;
    _olg = null;
    _ef = -1;
    _fkk = -1;
    _klo = -1;
    _aja = new ArrayList<>();
    _qih = new ArrayList<>();
    _isb = false;
    _jau = false;
    _tdL = false;
    _gei = 0;
    _erg = false;
    _phg = false;
    _vcj = false;
    _sek = false;
    _jsb = new TutorialMessage[8];
  }

  public static void a529lp(final TutorialMessage var1) {
    if (!_isb) {
      _hmq = var1;
      if (var1 == null) {
        _tpb = _dmgq;
        _feB = _jbd = _kpj + _jcr >> 1;
        _oia = _beh;
        a195qj(null);
      } else {
        a150sj();
        int var2 = var1._t;
        int var3 = var1._b;
        final int var4 = var1._j;
        int var5 = var1._q;

        if (var1._q == -1) {
          String var6 = templateDictionary.expand(var1.body);
          int var7 = GameUI.breakLines(var6, Menu.SMALL_FONT, new int[]{var4 - 6 - 20});
          var5 = 34 + Menu.SMALL_FONT.descent + var7 * 13;
          _ehL = new Sprite[var7];
          if (var1.objectives != null) {
            final TutorialObjective[] var8 = var1.objectives;

            for (final TutorialObjective var10 : var8) {
              var6 = templateDictionary.expand(var10.description);
              var7 = GameUI.breakLines(var6, Menu.SMALL_FONT, new int[]{var4 - 6 - 20});
              var5 += 13 * var7 + Menu.SMALL_FONT.descent;
            }
          }
        }

        if ((1 & var1.anchor) == 0) {
          var2 += 3;
        } else {
          var2 = 637 - var2;
        }

        if ((var1.anchor & 2) == 0) {
          var3 += 55;
        } else {
          var3 = 477 - var3;
        }

        if ((2 & var1.anchor) != 0) {
          var3 -= var5;
        }

        if ((var1.anchor & 1) != 0) {
          var2 -= var4;
        }

        _jbd = var4 + var2;
        _tpb = var3 + var5;
        _oia = var3;
        _feB = var2;
        a195qj(var1);
        if (var1.body.contains("<%tabresizehint>")) {
          templateDictionary.put("tabresizehint", "");
        }

        if (_olg == null) {
          _dmgq = _tpb;
          _beh = _oia;
          _jcr = _kpj = _jbd + _feB >> 1;
        }

      }
    }
  }

  private static void a195qj(final TutorialMessage var0) {
    _tec = 0;
    if (var0 == null) {
      _spm = null;
      _ehL = null;
      _oii = null;
      _rgf = null;
    } else {
      final int var2 = Menu.SMALL_FONT.ascent + Menu.SMALL_FONT.descent;
      final int var3 = var0._j - 6 - 20;
      String var4 = templateDictionary.expand(var0.body);
      String[] var5 = GameUI.breakLinesWithColorTags(Menu.SMALL_FONT, var4, new int[]{var3});
      assert var5 != null;
      int var6 = var5.length;
      Drawing.saveContext();
      int var7;
      if (var6 > 0) {
        _ehL = new Sprite[var6];
        _ehL[0] = new Sprite(var3, var2);
        _ehL[0].installForDrawing();
        Menu.SMALL_FONT.draw(var5[0], 0, Menu.SMALL_FONT.ascent, Drawing.WHITE);

        for (var7 = 1; var7 < var6; ++var7) {
          _ehL[var7] = new Sprite(var3, var2);
          _ehL[var7].installForDrawing();
          Menu.SMALL_FONT.draw(var5[var7], 0, Menu.SMALL_FONT.ascent, Drawing.WHITE);
        }
      } else {
        _ehL = null;
      }

      if (var0.objectives == null) {
        _rgf = null;
        _oii = null;
        _spm = null;
      } else {
        var7 = var0.objectives.length;
        _rgf = new int[var7];
        _spm = new Sprite[var7][];
        _oii = new Sprite[var7][];

        for (int var8 = 0; var7 > var8; ++var8) {
          final TutorialObjective var9 = var0.objectives[var8];
          var4 = templateDictionary.expand(var9.description);
          var5 = GameUI.breakLinesWithColorTags(Menu.SMALL_FONT, var4, new int[]{var3});
          assert var5 != null;
          var6 = var5.length;
          _oii[var8] = new Sprite[var6];
          _spm[var8] = new Sprite[var6];

          for (int var10 = 0; var10 < var6; ++var10) {
            _oii[var8][var10] = new Sprite(var3, var2);
            _oii[var8][var10].installForDrawing();
            Menu.SMALL_FONT.draw(var5[var10], 0, Menu.SMALL_FONT.ascent, Drawing.WHITE);
            _spm[var8][var10] = _oii[var8][var10].copy();
            _spm[var8][var10].installForDrawing();
            Drawing.b669(1, 1, var3, var2);
          }
        }
      }

      Drawing.restoreContext();
    }
  }

  private static void a529ac(final TutorialMessage var1) {
    if (_hmq != null) {
      final TutorialMessage var2 = !_hmq.noStack ? _hmq : _hmq.next;
      if (var2 != null) {
        _jsb[_gei++] = var2;
      }
    }

    a529lp(var1);
  }

  private static void b900(final String var0) {
    int var5;
    if (a517qj("stage", var0)) {
      var5 = Integer.parseInt(var0.substring(6));
      a093no(var5);
      session.recalculateSystemState();
    } else if (var0.equalsIgnoreCase("zoomtohome")) {
      session.gameView.a021(39, _hod.combinedForce.getCapital(), 200.0F);
    } else if (var0.equalsIgnoreCase("closetohome")) {
      session.gameView.a815(_hod.combinedForce.getCapital());
    } else {
      final StarSystem[] var2;
      int var3;
      StarSystem var4;
      if (var0.equalsIgnoreCase("zoomtowarning")) {
        var2 = session.gameState.map.systems;

        for (var3 = 0; var2.length > var3; ++var3) {
          var4 = var2[var3];
          if (var4.owner == _hod && !session.systemsWillOwn[var4.index]) {
            session.gameView.a021(92, var4, 300.0F);
            return;
          }
        }

      } else if (var0.equalsIgnoreCase("zoomOut")) {
        session.gameView.c487();
      } else {
        if (var0.equalsIgnoreCase("zoom12")) {
          session.gameView.a815(session.gameState.map.systems[12]);
        }

        if (var0.equalsIgnoreCase("zoomDerelict")) {
          var2 = session.gameState.map.systems;

          for (var3 = 0; var2.length > var3; ++var3) {
            var4 = var2[var3];
            if (var4.type >= 6) {
              session.gameView.a815(var4);
              break;
            }
          }
        }

        if (var0.equalsIgnoreCase("block")) {
          _isb = true;
        } else if (var0.equalsIgnoreCase("capture1")) {
          assert false;
        } else if (!var0.equalsIgnoreCase("showtabs")) {
          if (var0.equalsIgnoreCase("startai")) {
            for (var5 = 1; var5 < session.ais.length; ++var5) {
              session.ais[var5].initialize(true);
            }

          } else {
            String var6;
            if (a517qj("enable", var0)) {
              var6 = var0.substring(7).trim();
              if (var6.endsWith("Button")) {
                session.ui.a950(var6, true);
              }

              if (var6.equalsIgnoreCase("animationControls")) {
                session.ui.animationControlsPanel.visible = true;
                session.ui.animationAutoPlayButton.activate();
              }

            } else {
              if (a517qj("disable", var0)) {
                var6 = var0.substring(8).trim();
                if (var6.endsWith("Button")) {
                  session.ui.a950(var6, false);
                }
              }

              if (a517qj("hide", var0)) {
                var6 = var0.substring(4).trim();
                session.ui.a223(var6, false);
              } else if (a517qj("show", var0)) {
                var6 = var0.substring(4).trim();
                session.ui.a223(var6, true);
              } else if (a517qj("highlight", var0)) {
                var6 = var0.substring(10).trim();
                if (var6.equalsIgnoreCase("wormholes")) {
                  _erg = true;
                  return;
                }

                if (var6.equalsIgnoreCase("borders")) {
                  _jau = true;
                  return;
                }

                if (var6.equalsIgnoreCase("garrison")) {
                  _vcj = true;
                  return;
                }

                if (var6.equalsIgnoreCase("resources")) {
                  _tdL = true;
                  return;
                }

                if (var6.equalsIgnoreCase("productionHammer")) {
                  _sek = true;
                  return;
                }

                if (var6.equalsIgnoreCase("ready")) {
                  _phg = true;
                }
              }
            }
          }
        }
      }
    }
  }

  public static void tick() {
    if (_hmq != _olg) {
      if (++_ejm >= 32) {
        a423js();
      }
    } else if (_olg != null) {
      _tec += 16;
      final TutorialObjective[] var0 = _olg.objectives;
      if (var0 != null) {
        for (int var1 = 0; var0.length > var1; ++var1) {
          if (_rgf[var1] == 0 && a623sp(var0[var1].key)) {
            _rgf[var1] = 1;
            a154vn();
          }
        }
      }
    }

    if (!a988sr("unplaced")) {
      if (a988sr("losegame")) {
        a529ac(TutorialMessages.get("lose"));
      }
    } else if (a896qc("unplaced")) {
      a529ac(TutorialMessages.get("unplaced"));
    }

    _aja.clear();
    if (_olg == _hmq) {
      _eie = _kpj;
      _ef = _jcr;
      _fkk = _beh;
      _klo = _dmgq;
    } else {
      _ef = MathUtil.ease(_ejm, 32, _jcr, _feB);
      _fkk = MathUtil.ease(_ejm, 32, _beh, _oia);
      _eie = MathUtil.ease(_ejm, 32, _kpj, _jbd);
      _klo = MathUtil.ease(_ejm, 32, _dmgq, _tpb);
    }

    if (_oii != null) {
      boolean var3 = true;

      for (int var1 = 0; var1 < _rgf.length; ++var1) {
        if (_rgf[var1] > 0) {
          _rgf[var1]++;
        }

        if (_rgf[var1] < 48) {
          var3 = false;
        }
      }

      if (var3) {
        TutorialMessage var4 = _olg.next;
        if (var4 == null && _gei > 0) {
          var4 = _jsb[--_gei];
        }

        if (_isb) {
          _isb = false;
          final TutorialMessage var2 = _jsb[_gei];
          if (var2 == null || var2 == _olg) {
            a529lp(var4);
          } else {
            a529lp(var2);
          }
        } else {
          a529lp(var4);
        }
      }
    }
  }

  private static boolean a517qj(final CharSequence var0, final CharSequence var1) {
    final int var2 = var1.length();
    final int var3 = var0.length();
    if (var3 > var2) {
      return false;
    } else {
      for (int var4 = 0; var4 < var3; ++var4) {
        final char var5 = var1.charAt(var4);
        final char var6 = var0.charAt(var4);
        if (var6 != var5 && Character.toLowerCase(var5) != Character.toLowerCase(var6) && Character.toUpperCase(var5) != Character.toUpperCase(var6)) {
          return false;
        }
      }

      return true;
    }
  }

  private static boolean a896qc(final String var0) {
    for (final TutorialMessageId var1 : _qih) {
      if (var0.equalsIgnoreCase(var1.key)) {
        return false;
      }
    }

    _qih.add(new TutorialMessageId(var0));
    return true;
  }

  private static void a423js() {
    _beh = _oia;
    _kpj = _jbd;
    _phg = false;
    _erg = false;
    _dmgq = _tpb;
    _olg = _hmq;
    _jcr = _feB;
    _vcj = false;
    _tdL = false;
    _sek = false;
    _jau = false;
    _ejm = 0;
    if (_olg._m != null) {
      final String[] var1 = _olg._m;

      for (final String var3 : var1) {
        b900(var3);
      }
    }

    if (_olg.clearStack) {
      _gei = 0;
    }

  }

  private static boolean a623sp(final String var0) {
    if (var0.equalsIgnoreCase("openProduction")) {
      return session.ui.isProductionWindowOpen();
    } else if (var0.equalsIgnoreCase("placeAllFleets")) {
      return _hod.combinedForce.fleetsAvailableToBuild == 0;
    } else if (var0.equalsIgnoreCase("homeworldEmpty")) {
      return _hod.combinedForce.getCapital().remainingGarrison == 0;
    } else if (var0.equalsIgnoreCase("homeworldOne")) {
      return _hod.combinedForce.getCapital().remainingGarrison == 1;
    } else {
      final StarSystem[] var2;
      int var3;
      StarSystem var4;
      if (var0.equalsIgnoreCase("captureAll")) {
        var2 = session.gameState.map.systems;

        for (var3 = 0; var2.length > var3; ++var3) {
          var4 = var2[var3];
          if (_hod != var4.owner) {
            return false;
          }
        }

        return true;
      } else if (var0.equalsIgnoreCase("terraforming")) {
        var2 = session.gameState.map.systems;

        for (var3 = 0; var2.length > var3; ++var3) {
          var4 = var2[var3];
          if (var4.score == StarSystem.Score.TERRAFORMED) {
            return true;
          }
        }

        return false;
      } else if (var0.equalsIgnoreCase("placeTannhauser")) {
        return session.gameState.projectOrders.stream()
            .anyMatch(var6 -> var6.type == GameState.ResourceType.EXOTICS && session.gameState.map.systems[12] == var6.source);
      } else if (var0.equalsIgnoreCase("fleetmove")) {
        return session.gameState.moveOrders.stream().anyMatch(var5 -> _hod == var5.player);
      } else {
        return a988sr(var0);
      }
    }
  }

  private static void a093no(final int var0) {
    if (stage < var0) {
      final Map var2;
      try {
        stage = var0;
        var2 = mapGenerator.a572(stage, true);
      } catch (final MapGenerationFailure var13) {
        throw new RuntimeException("Failed to advance Tutorial to stage " + stage + ". ");
      }

      final StarSystem[] var3 = session.gameState.map.systems;

      for (int var4 = 0; var4 < var3.length; ++var4) {
        final StarSystem var5 = var3[var4];
        final StarSystem var6 = var2.systems[var4];
        var6.hasDefensiveNet = var5.hasDefensiveNet;
        var6.resources = var5.resources;
        var6.garrison = var5.garrison;
        var6.remainingGarrison = var5.remainingGarrison;
        var6.score = var5.score;

        final int var7 = (int) Arrays.stream(var5.neighbors).filter(var10 -> !var6.hasNeighbor(var10)).count();

        if (var7 > 0) {
          final StarSystem[] ln_s = new StarSystem[var6.neighbors.length + var7];
          int i = 0;

          int var9;
          for (var9 = 0; var9 < var6.neighbors.length; ++var9) {
            ln_s[var9] = var6.neighbors[var9];
          }

          for (final StarSystem var12 : var5.neighbors) {
            if (!var6.hasNeighbor(var12)) {
              ln_s[i + var9] = var2.systems[var12.index];
              ++i;
            }
          }

          var6.neighbors = ln_s;
        }

        var6.lastOwner = var5.lastOwner;
        var6.owner = var5.owner;
        var6.contiguousForce = var5.contiguousForce;
        if (var5.owner != null) {
          assert var6.contiguousForce != null;

          var6.contiguousForce.add(var6);
          if (var5 == var5.contiguousForce.getCapital()) {
            var6.contiguousForce.setCapital(var6);
          }

          var6.owner.combinedForce.add(var6);
          if (var5 == var5.owner.combinedForce.getCapital()) {
            var6.owner.combinedForce.setCapital(var6);
          }

          var5.contiguousForce.remove(var5);
          var5.owner.combinedForce.remove(var5);
        }
      }

      session.setMap(var2);
      if (_idd != null) {
        _idd = var2.systems[_idd.index];
      }

      if (_dd != null) {
        _dd = var2.systems[_dd.index];
      }

      final StarSystem[] var14 = var2.systems;

      for (final StarSystem ln_ : var14) {
        templateDictionary.put("star" + ln_.index, a865pa(ln_, -31));
      }

      Player var15 = null;
      if (stage == 5) {
        var15 = new Player(1, StringConstants.EMPIRE_NAMES[1], GameUI.PLAYER_COLORS_DARK[1], GameUI.PLAYER_COLORS_1[1], GameUI.PLAYER_COLORS_2[1]);
        var2.systems[6].lastOwner = var15;
        var2.systems[6].owner = var15;
        var2.systems[7].lastOwner = var15;
        var2.systems[7].owner = var15;
        var15.combinedForce = new CombinedForce(var15, var2.systems[7]);
        var15.combinedForce.add(var2.systems[7]);
        var15.combinedForce.add(var2.systems[6]);
        final ContiguousForce var17 = new ContiguousForce(var15, var2.systems[7]);
        var17.add(var2.systems[7]);
        var17.add(var2.systems[6]);
        var2.systems[6].contiguousForce = var17;
        var2.systems[7].contiguousForce = var17;
        var15.contiguousForces.add(var17);
        var15.stats = new PlayerStats(var2.systems[7].garrison + var2.systems[6].garrison);
      }

      if (stage == 6) {
        var15 = new Player(2, StringConstants.EMPIRE_NAMES[2], GameUI.PLAYER_COLORS_DARK[2], GameUI.PLAYER_COLORS_1[2], GameUI.PLAYER_COLORS_2[2]);
        final StarSystem var5 = var2.systems[17];
        var5.owner = var15;
        var5.lastOwner = var15;
        var15.combinedForce = new CombinedForce(var15, var5);
        var15.combinedForce.add(var5);
        final ContiguousForce var18 = new ContiguousForce(var15, var5);
        var18.add(var5);
        var5.contiguousForce = var18;
        var15.contiguousForces.add(var18);
        var15.stats = new PlayerStats(var5.garrison);
      }

      if (stage == 7) {
        var15 = new Player(3, StringConstants.EMPIRE_NAMES[3], GameUI.PLAYER_COLORS_DARK[3], GameUI.PLAYER_COLORS_1[3], GameUI.PLAYER_COLORS_2[3]);
        final StarSystem var5 = var2.systems[37];
        final StarSystem var6 = var2.systems[36];
        final StarSystem var19 = var2.systems[35];
        final StarSystem var21 = var2.systems[34];
        var5.lastOwner = var15;
        var5.owner = var15;
        final StarSystem var25 = var2.systems[33];
        var6.lastOwner = var15;
        var6.owner = var15;
        var19.lastOwner = var15;
        var19.owner = var15;
        var21.owner = var15;
        var21.lastOwner = var15;
        var25.lastOwner = var15;
        var25.owner = var15;
        var15.combinedForce = new CombinedForce(var15, var5);
        var15.combinedForce.add(var5);
        var15.combinedForce.add(var6);
        var15.combinedForce.add(var19);
        var15.combinedForce.add(var21);
        var15.combinedForce.add(var25);
        final ContiguousForce var24 = new ContiguousForce(var15, var5);
        var24.add(var5);
        var24.add(var6);
        var24.add(var19);
        var24.add(var21);
        var24.add(var25);
        var5.contiguousForce = var24;
        var6.contiguousForce = var24;
        var19.contiguousForce = var24;
        var21.contiguousForce = var24;
        var25.contiguousForce = var24;
        var15.contiguousForces.add(var24);
        var15.stats = new PlayerStats(var21.garrison + var19.garrison + var6.garrison + var5.garrison + var25.garrison);
      }

      if (var15 != null) {
        session.addTutorialAIPlayer(var15);
        session.gameView.a073(session.gameState.players);
        session.ui.a735(session.gameState.players);
        templateDictionary.put("player" + var15.index, Strings.format("<col=<%0>>" + var15.name + "</col>", Integer.toString(var15.color2, 16)));
      }

      a195qj(_olg);
    }
  }

  private static String a865pa(final StarSystem var0, final int var1) {
    final int var2 = var0.owner == null ? 8421504 : var0.owner.color2;
    if (var1 > -3) {
      StringConstants.FS_BUTTON_ACCEPT = null;
    }

    return Strings.format("<col=<%0>>" + var0.name + "</col>", Integer.toString(var2, 16));
  }

  private static boolean a988sr(final String var1) {
    return _aja.stream().anyMatch(var2 -> var1.equalsIgnoreCase(var2.key))
        || _qih.stream().anyMatch(var2 -> var1.equalsIgnoreCase(var2.key));
  }

  private static boolean a154vn() {
    int var0 = _ehL != null ? _ehL.length : 0;
    int var1;
    if (_oii != null) {
      for (var1 = 0; _oii.length > var1; ++var1) {
        var0 += _oii[var1].length;
      }
    }

    var1 = var0 * _olg._j;
    if (var1 <= _tec) {
      return true;
    } else {
      _tec = var1;
      return false;
    }
  }

  public static void k150pe() {
    a984fl("endturn");
    if (session.gameState.map.systems.length == 2) {
      if (_hod == session.gameState.map.systems[1].owner) {
        if (a896qc("combatSuccess")) {
          a529ac(TutorialMessages.get("combatSuccess"));
        }
      } else {
        final CombatEngagementLog var0 = a471tl(session.gameState.map.systems[1]);
        if (var0 != null && a896qc("combatFailed")) {
          a529ac(TutorialMessages.get("combatFailed"));
        }
      }

      final CombatEngagementLog var0 = a471tl(session.gameState.map.systems[1]);
      if (var0 != null) {
        templateDictionary.put("combatreport1", a495cm(var0));
      }

      templateDictionary.put("garrison1", Integer.toString(session.gameState.map.systems[1].garrison));
    }

    if (session.gameState.hasEnded && session.gameState.winnerIndex != _hod.index) {
      a529ac(TutorialMessages.get("lose"));
    }

    final StarSystem[] var4 = session.gameState.map.systems;

    for (final StarSystem var2 : var4) {
      final int var3 = var2.owner != null ? var2.owner.color2 : 8421504;
      templateDictionary.put("star" + var2.index, Strings.format("<col=<%0>>" + var2.name + "</col>", Integer.toString(var3, 16)));
    }
  }

  private static CombatEngagementLog a471tl(final StarSystem var1) {
    if (session.turnEventLog != null) {
      for (final TurnEventLog.Event var2 : session.turnEventLog.events) {
        if (var2 instanceof CombatEngagementLog var3) {
          if (var1 == var3.system) {
            final Player[] var4 = var3.players;

            for (final Player var6 : var4) {
              if (var6 == _hod) {
                return var3;
              }
            }

            return null;
          }
        }
      }

    }
    return null;
  }

  private static String a495cm(final CombatEngagementLog var0) {
    int var1 = 0;
    int var2 = 0;

    for (final CombatLogEvent var3 : var0.events) {
      if (_hod == var3.player) {
        if (var3.fleetsRetreated != 0 && var3.source != null) {
          ++var2;
        }

        var1 += var3.fleetsDestroyed;
      }
    }

    if (var1 == 0) {
      return StringConstants.TUTORIAL_LOST_NONE;
    } else {
      String var4 = var1 != 1 ? Strings.format(StringConstants.TUTORIAL_LOST_MULTIPLE, Integer.toString(var1)) : StringConstants.TUTORIAL_LOST_SINGLE;
      if (var2 != 0) {
        final String[] var5 = new String[1 + var2];
        int i = 1;
        var5[0] = var4;

        for (final CombatLogEvent var3 : var0.events) {
          if (var3.player == _hod && var3.fleetsRetreated != 0 && var3.source != null) {
            final String var6;
            if (var3.fleetsRetreated == 1) {
              var6 = Strings.format(StringConstants.FLEET_HAS_RETREATED_TO, a865pa(var3.source, -110));
            } else {
              var6 = Strings.format(StringConstants.TUTORIAL_RETREATING_MULTIPLE, a865pa(var3.source, -97), Integer.toString(var3.fleetsRetreated));
            }

            var5[i++] = var6;
          }
        }

        var4 = a399va(var5);
      }

      return var4;
    }
  }

  private static String a399va(final String[] var1) {
    if (var1 == null || var1.length == 0) {
      return null;
    } else if (var1.length == 1) {
      return var1[0];
    } else {
      final int var2 = var1.length - 2;
      final StringBuilder var3 = new StringBuilder(20);

      for (int var4 = 0; var2 > var4; ++var4) {
        var3.append(var1[var4]);
        var3.append(StringConstants.TEXT_JOIN);
      }

      var3.append(var1[var2]);
      var3.append(StringConstants.TEXT_JOIN_FINAL);
      var3.append(var1[1 + var2]);
      return var3.toString();
    }
  }

  public static void a423mq() {
    if (_olg != null || _hmq != null) {
      if (-_ef + _eie > 20 && -_fkk + _klo > 20) {
        Menu.drawShine(_ef, _fkk, -_ef + _eie, -_fkk + _klo, Drawing.alphaOver(0, 3974311, 128), true);
      }

      if (_olg == _hmq) {
        final int var1 = _ef + 13;
        int var2 = 6 + _fkk;
        int var3 = _tec;
        int var4;
        if (_ehL != null) {
          if (_tec <= 0) {
            return;
          }

          a833sa(_tec, var1, 16, _ehL[0], var2);

          for (var4 = 1; _ehL.length > var4; ++var4) {
            var3 -= _olg._j;
            if (var3 <= 0) {
              return;
            }

            var2 += 13;
            a833sa(var3, var1, 16, _ehL[var4], var2);
          }

          var2 += 13;
        }

        if (_oii != null) {
          for (var4 = 0; _oii.length > var4; ++var4) {
            final Sprite[] var5 = _oii[var4];
            int var6 = _rgf[var4];
            if (var6 <= 0) {
              for (final Sprite bi_ : var5) {
                var3 -= _olg._j;
                if (var3 <= 0) {
                  return;
                }

                var2 += 13;
                a833sa(var3, var1, 16, bi_, var2);
              }
            } else {
              final Sprite[] var7 = _spm[var4];
              int var8;
              if (var6 > 32) {
                var6 = -var6 + 48;
                if (var6 >= 0) {
                  var6 = (var6 << 8) / 16;
                } else {
                  var6 = 0;
                }

                for (var8 = 0; var5.length > var8; ++var8) {
                  var3 -= _olg._j;
                  var2 += 13;
                  var5[var8].drawAdd(var1, var2, var6);
                  var7[var8].drawAdd(var1, var2, var6);
                }
              } else {
                var6 = (var6 << 8) / 32;

                for (var8 = 0; var5.length > var8; ++var8) {
                  var2 += 13;
                  var3 -= _olg._j;
                  var5[var8].drawAdd(var1, var2, 256);
                  var7[var8].drawAdd(var1, var2, var6);
                }
              }

              if (var3 < 0) {
                _tec -= var3;
                var3 = 0;
              }
            }

            var2 += 13;
          }
        }
      }
    }
  }

  public static void a833sa(final int var0, final int var1, final int var2, final Sprite var3, final int var4) {
    final int var5 = Drawing.top;
    final int var6 = Drawing.bottom;
    final int var7 = Drawing.left;
    final int var8 = Drawing.right;
    if (var0 >= var2) {
      Drawing.expandBoundsToInclude(var1, var5, -var2 + var1 + var0, var6);
      var3.drawAdd(var1, var4, 256);
    }

    final int var9 = var8 - var7 - 1;
    int var10 = var0 >= var2 ? var0 - var2 : 0;
    int var11 = var0;
    if (var10 < -var1 + var7) {
      var10 = var7 - var1;
    }

    if (var9 < var0) {
      var11 = var9;
    }

    for (int var12 = var10; var12 < var11; ++var12) {
      final int var13 = (var0 - var12 << 8) / var2;
      Drawing.setBounds(var12 + var1, var5, var12 + var1 + 1, var6);
      var3.drawAdd(var1, var4, var13);
    }

    Drawing.setBounds(var7, var5, var8, var6);
  }

  public static boolean a881ks(final boolean var0) {
    if ((_olg != null || _hmq != null) && JagexApplet.mouseX >= _ef && JagexApplet.mouseX < _eie && _fkk <= JagexApplet.mouseY && _klo > JagexApplet.mouseY) {
      if (JagexApplet.mouseButtonJustClicked != MouseState.Button.NONE && var0) {
        if (_olg != _hmq) {
          a423js();
        }

        if (a154vn()) {
          a984fl("enter");
        }
      }

      return true;
    } else {
      return false;
    }
  }

  public static Rect b520b() {
    final Rect var2 = new Rect(0, 50, ShatteredPlansClient.SCREEN_WIDTH, 430);
    final Rect var4 = new Rect(_ef, _fkk, _eie, _klo);
    final List<Rect> var5 = new ArrayList<>();
    if (var2.x1 < var4.x1) {
      var5.add(new Rect(var2.x1, var2.y1, Math.min(var4.x1, var2.x2), var2.y2));
    }

    if (var4.x2 < var2.x2) {
      var5.add(new Rect(Math.max(var4.x2, var2.x1), var2.y1, var2.x2, var2.y2));
    }

    if (var4.y1 > var2.y1) {
      var5.add(new Rect(var2.x1, var2.y1, var2.x2, Math.min(var2.y2, var4.y1)));
    }

    if (var2.y2 > var4.y2) {
      var5.add(new Rect(var2.x1, Math.max(var2.y1, var4.y2), var2.x2, var2.y2));
    }

    int var6 = 0;
    Rect var7 = null;

    for (final Rect var8 : var5) {
      final int var9 = -var8.x1 + var8.x2;
      final int var10 = -var8.y1 + var8.y2;
      final int var11 = var10 * var9 * (Math.min(var9, var10));
      if (var11 > var6) {
        var7 = var8;
        var6 = var11;
      }
    }

    return var7;
  }

  @SuppressWarnings("StringConcatenationInLoop")
  private static void a150sj() {
    final Player var1 = _hod;
    final CombinedForce var2 = var1.combinedForce;

    if (var2 != null) {
      final int var4 = var2.surplusResources[var2.surplusResourceRanks[3]];
      if (var4 == 0) {
        templateDictionary.put("generateshortfall", StringConstants.TUTORIAL_BALANCED);
      } else {
        String neededResources = null;
        int neededResourcesSoFar = 0;

        for (int i = 0; i < GameState.NUM_RESOURCES; ++i) {
          final int resource = var2.surplusResourceRanks[i];
          if (var2.surplusResources[resource] == 0) {
            final String resourceName = Strings.format("<col=<%0>><%1></col>", Integer.toString(GameView.RESOURCE_COLORS[resource], 16), StringConstants.RESOURCE_NAMES[resource]);
            if (neededResourcesSoFar == 0) {
              neededResources = resourceName;
            } else if (neededResourcesSoFar == 1) {
              neededResources = resourceName + StringConstants.TEXT_JOIN_FINAL + neededResources;
            } else {
              neededResources = resourceName + StringConstants.TEXT_JOIN + neededResources;
            }

            ++neededResourcesSoFar;
          }
        }

        String var11 = Strings.format(StringConstants.TUTORIAL_SHORTFALL1, neededResources);
        if (neededResourcesSoFar == 1) {
          var11 = var11 + StringConstants.TUTORIAL_SHORTFALL2A;
        } else {
          var11 = var11 + StringConstants.TUTORIAL_SHORTFALL_2B;
        }

        templateDictionary.put("generateshortfall", var11);
      }
    }

    templateDictionary.put("generateprojectprogress", null);
  }

  public static void a018jr(final String var0, final String var2) {
    templateDictionary.put(var0, var2);
  }

  public static void a984fl(final String var0) {
    _aja.add(new TutorialMessageId(var0));
  }

  static GameState createTutorialGameState(final int turnLengthIndex, final GameOptions options, final String[] playerNames) {
    final GameState var4 = new GameState(turnLengthIndex, options, GameState.GameType.TUTORIAL, playerNames);

    final TutorialMapGenerator var5 = new TutorialMapGenerator();
    var4.map = var5.generate();
    var4.map.assignPlayerHomeworlds(var4.players, options);
    var4.recalculateFleetProduction();
    var4.recalculatePlayerFleetProduction();
    return var4;
  }
}
