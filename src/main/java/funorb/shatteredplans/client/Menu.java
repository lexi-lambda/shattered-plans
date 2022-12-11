package funorb.shatteredplans.client;

import funorb.Strings;
import funorb.awt.FullScreenCanvas;
import funorb.awt.KeyState;
import funorb.awt.MouseState;
import funorb.client.JagexBaseApplet;
import funorb.client.MenuInputState;
import funorb.client.RankingsRequest;
import funorb.client.lobby.vm_;
import funorb.graphics.ArgbSprite;
import funorb.graphics.Drawing;
import funorb.graphics.Font;
import funorb.graphics.NineSliceSprite;
import funorb.graphics.PalettedSpriteFont;
import funorb.graphics.Sprite;
import funorb.graphics.mq_;
import funorb.shatteredplans.C2SPacket;
import funorb.shatteredplans.StringConstants;
import funorb.shatteredplans.client.game.ClientGameSession;
import funorb.shatteredplans.client.game.GameView;
import funorb.shatteredplans.client.ui.FloatingPanel;
import funorb.shatteredplans.game.GameOptions;
import funorb.shatteredplans.game.GameState.GameType;
import funorb.util.MathUtil;
import funorb.util.PseudoMonotonicClock;
import org.intellij.lang.annotations.MagicConstant;
import org.jetbrains.annotations.NotNull;

import java.awt.Frame;
import java.util.ArrayDeque;
import java.util.Objects;
import java.util.Queue;
import java.util.stream.IntStream;

public final class Menu {
  private static final int[] MEMBERS_PAUSE_MENU_MULTIPLAYER_ITEMS_1 = {Item.RETURN_TO_GAME, Item.SOUND_VOLUME, Item.MUSIC_VOLUME, Item.FULLSCREEN, Item.INSTRUCTIONS_2, 11, Item.RESIGN, Item.RETURN_TO_LOBBY};
  private static final int[] MEMBERS_PAUSE_MENU_MULTIPLAYER_ITEMS_2 = new int[]{Item.RETURN_TO_GAME, Item.SOUND_VOLUME, Item.MUSIC_VOLUME, Item.FULLSCREEN, Item.INSTRUCTIONS_2, 13, Item.RETURN_TO_LOBBY};
  private static final int[] MEMBERS_PAUSE_MENU_MULTIPLAYER_ITEMS_3 = new int[]{Item.RETURN_TO_GAME, Item.SOUND_VOLUME, Item.MUSIC_VOLUME, Item.FULLSCREEN, Item.INSTRUCTIONS_2, Item.RETURN_TO_LOBBY};
  public static final Menu[] menus = new Menu[14];
  public static final Queue<RankingsRequest> rankingsRequests = new ArrayDeque<>();
  private static final int[] _vnr = new int[6];
  private static final int[] _ndd = new int[]{1, 1, 2, 2, 3, 1, 3, 2, 2, 2, 3, 3, 1, 3, 5, 5, 5, 5, 3, 5, 3, 5, 1, 1, 5};
  private static final int[] _eig = new int[]{100, 100, 200, 200, 300, 100, 300, 200, 200, 200, 300, 300, 100, 300, 500, 500, 500, 500, 300, 500, 300, 500, 100, 100, 500};
  private static final int[] _kcm = new int[4];
  private static final int[] _sf = new int[4];
  private static final int[] MEMBERS_PAUSE_MENU_SINGLEPLAYER_ITEMS = {Item.RETURN_TO_GAME, Item.SOUND_VOLUME, Item.MUSIC_VOLUME, Item.FULLSCREEN, Item.INSTRUCTIONS_2, Item.END_GAME};
  private static final int[] RATINGS_MENU_ITEMS = {Item.RATING_MODE_1, Item.RATING_MODE_2, Item.CURRENT_MENU};
  private static final int[] ACHIEVEMENTS_MENU_ITEMS = {Item.CURRENT_MENU};
  private static final int RED = 0xff0000;
  private static final String[] BROKEN_LINES_LOGIN = new String[16];
  private static final MenuInputState _nsnb = new MenuInputState(1);
  private static final String[] _tsK = new String[16];
  static final MenuInputState _gsl = new MenuInputState(1);
  public static Sprite SHINE_LEFT;
  public static Sprite SHINE_RIGHT;
  public static Sprite SHINE_MID;
  public static int _hgt;
  private static int _ffy;
  private static int _kbA;
  private static int[] _kbD;
  private static int _acw;
  private static int _qBb;
  private static int[] _erq;
  private static int _os;
  public static Sprite _ok;
  public static PalettedSpriteFont FONT;
  @MagicConstant(valuesFromClass = Item.class)
  private static final int[][] items = new int[14][];
  private static final int[] _tvcr = new int[14];
  private static final int[] _tvct = new int[14];
  private static final int[] widths = new int[14];
  private static final int[] _tvcm = new int[14];
  public static GameOptions gameOptions;
  public static Sprite MENU_HEX;
  public static Sprite INSTR_MAIN_VIEW;
  public static Sprite INSTR_STAR_FRAME;
  public static PalettedSpriteFont SMALL_FONT;
  public static Sprite ORB_COIN;
  public static Sprite[] ACHIEVEMENTS;
  public static Sprite[] _gvsb;
  public static Sprite[] _vha;
  public static Sprite[] ACHIEVEMENT_ICONS;
  public static int unlockedAchievementsBitmap;
  @MagicConstant(valuesFromClass = Id.class)
  public static int currentMenu;
  @MagicConstant(valuesFromClass = Id.class)
  public static int nextMenu;
  public static int _ehQ;
  public static Sprite[] _kjf;
  private static int[] _oob;
  private static int _anc;
  private static RankingsRequest _riI = null;
  public static boolean discarding;
  private static int _upd;
  private static int _tvcq = 0;
  private static Sprite _ilgb = null;
  private static int _nba;
  private static int _mgfb;
  private static final int[] _ssH = new int[StringConstants.INSTRUCTIONS_TABNAMES.length];
  public static @NotNull GameType skirmishGameType = GameType.CONQUEST;
  private static int _leb;
  private static int[] _cbi;
  private static int _sbe;
  private static Sprite _glh = null;
  private static int _ekA;
  @MagicConstant(valuesFromClass = Id.class)
  private static int _brm;
  private static int _cleo;
  private static int _nct = 0;
  private static int _onc;
  public static boolean areAchievementsOffline;
  private static int[] _fgc;
  private static int _c;
  private static int[] _py;
  static int _ahR;
  static int _ldj;
  static int _rnb;
  static boolean isLobbyDialogOpen = false;
  private static String[] _tc;
  private static int _vob;
  static long _brp;
  static int _hoa;
  static int[] _ssa;
  static int _uqk;
  static int _dmra;
  static String[] _hmp;
  static String[] _kpo;
  static String[] _kdb;
  static boolean isFullscreenDialogOpen = false;
  static int _dbf;
  static String[] _nld;
  static String[] _tuef;
  static DialogOption[] _E;
  static @NotNull Menu.FullscreenDialog currentFullscreenDialog = FullscreenDialog.MEMBERS_ONLY_NOT_LOGGED_IN;
  static NineSliceSprite buttonSprite;
  @MagicConstant(valuesFromClass = Id.class)
  private final int id;
  private final MenuInputState inputState;
  private int _g;
  private boolean _f;
  private int _h;
  private int _i;
  static {
    final int[] var0 = new int[]{150, 60, 60, 60, 60, 60};
    int var1 = 560;

    int var2;
    for (var2 = 0; var2 < 6; ++var2) {
      var1 -= var0[var2];
    }

    var2 = 560;

    for (int var3 = 5; var3 > 0; --var3) {
      final int var4 = var1 / (2 + var3);
      var1 -= var4;
      var2 -= var4;
      _vnr[var3] = var2 - (var0[var3] >> 1);
      var2 -= var0[var3];
    }

    _vnr[0] = var1 >> 1;

    register(Id.MAIN, 0, 400, 200, new int[]{Item.TUTORIAL, Item.SINGLE_PLAYER_SKIRMISH, Item.ENTER_MULTIPLAYER_LOBBY, Item.INSTRUCTIONS_1, Item.OPTIONS_MENU, Item.RANKINGS, Item.ACHIEVEMENTS, Item.QUIT});
    register(Id.PAUSE_SINGLEPLAYER, 0, 320, 150, new int[]{Item.RETURN_TO_GAME, Item.SOUND_VOLUME, Item.MUSIC_VOLUME, Item.INSTRUCTIONS_2, Item.END_GAME});
    register(Id.PAUSE_MULTIPLAYER_1, 0, 320, 150, new int[]{Item.RETURN_TO_GAME, Item.SOUND_VOLUME, Item.MUSIC_VOLUME, Item.INSTRUCTIONS_2, 11, Item.RESIGN, Item.RETURN_TO_LOBBY});
    register(Id.PAUSE_MULTIPLAYER_2, 0, 370, 150, new int[]{Item.RETURN_TO_GAME, Item.SOUND_VOLUME, Item.MUSIC_VOLUME, Item.INSTRUCTIONS_2, 13, Item.RETURN_TO_LOBBY});
    register(Id.PAUSE_MULTIPLAYER_3, 0, 320, 150, new int[]{Item.RETURN_TO_GAME, Item.SOUND_VOLUME, Item.MUSIC_VOLUME, Item.INSTRUCTIONS_2, Item.RETURN_TO_LOBBY});
    register(Id.RATINGS, 0, 400, 380, new int[]{Item.LOGIN_CREATE_ACCOUNT, Item.CURRENT_MENU});
    register(Id.INSTRUCTIONS_1, 320, 500, 70, new int[]{Item.TUTORIAL, Item.CURRENT_MENU});
    register(Id.INSTRUCTIONS_2, 320, 500, 50, new int[]{Item.CURRENT_MENU});
    register(Id.ACHIEVEMENTS, 0, 500, 400, new int[]{Item.LOGIN_CREATE_ACCOUNT, Item.CURRENT_MENU});
    register(Id.G9, 0, 500, 200, new int[]{Item.LOGIN_CREATE_ACCOUNT, Item.DISCARD});
    register(Id.ERROR, 0, 320, 400, new int[]{Item.MENU});
    register(Id.SKIRMISH_SETUP, 0, 500, 200, new int[]{Item.START_SKIRMISH, Item.GAME_TYPE, Item.RULE_SET, Item.CURRENT_MENU});
    register(Id.OPTIONS, 0, 400, 200, new int[]{Item.SOUND_VOLUME, Item.MUSIC_VOLUME, Item.MUSIC_TRACK, Item.FULLSCREEN, Item.WATCH_INTRODUCTION, Item.CURRENT_MENU});
  }

  public Menu(@MagicConstant(valuesFromClass = Id.class) final int id) {
    this.id = id;
    this.inputState = new MenuInputState(items[this.id].length);
    this.a126(false);
  }

  private static Sprite a367(final int var0) {
    Sprite var2 = _vha[var0];
    if (var2 == null) {
      _vha[var0] = var2 = new Sprite(64, 64);
      Drawing.saveContext();
      var2.installForDrawing();
      achievementIcon(var0).draw(16, 16);
      Drawing.b669(3, 3, 64, 64);
      Drawing.restoreContext();
    }

    return var2;
  }

  private static void c487() {
    if (Sounds.soundVolume > 32) {
      int var1 = Sounds.soundVolume % 32;
      if (var1 == 0) {
        var1 = 32;
      }

      Sounds.setSoundVolume(Sounds.soundVolume - var1);
    } else {
      Sounds.setSoundVolume(0);
    }
  }

  public static void drawPanel(final int x, final int y, final int width, final int height) {
    drawPanel(x, y, width, height, 0x3ca4a7);
  }

  private static void register(@MagicConstant(valuesFromClass = Id.class) final int id,
                               final int var1,
                               final int var2,
                               final int var4,
                               final int[] var3) {
    _tvcr[id] = var2;
    _tvcm[id] = var4;
    widths[id] = 25;
    _tvct[id] = var1;
    items[id] = var3;
  }

  public static void switchTo(@MagicConstant(valuesFromClass = Id.class) final int id, final int var0, final boolean var2) {
    if (nextMenu != id) {
      nextMenu = id;
      _brm = var0;
      if (nextMenu >= 0) {
        menus[nextMenu]._g = -1;
        menus[nextMenu].a126(var2);
      }
      _ehQ = 0;
    }
  }

  private static void c487hd() {
    final int var0 = _ssH.length;
    _kbA = 0;
    _cbi = new int[var0];

    for (int var1 = 0; var0 > var1; ++var1) {
      int var2 = a353aa(var1);
      if (var1 != var0 - 1) {
        var2 += 50;
      }

      _cbi[var1] = var2;
      _kbA += var2;
    }

    _kbA -= 294;
  }

  private static void a150mgf() {
    if (Sounds.musicVolume < 224) {
      final int var0 = Sounds.musicVolume % 32;
      Sounds.setMusicVolume(Sounds.musicVolume + (32 - var0));
    } else {
      Sounds.setMusicVolume(256);
    }
  }

  public static int get_idb() {
    return FONT.capHeight + FONT.descent;
  }

  private static Sprite a367dmr(final int var0) {
    Sprite var1 = _gvsb[var0];
    if (var1 == null) {
      _gvsb[var0] = var1 = new Sprite(256, 256);
      Drawing.saveContext();
      var1.installForDrawing();
      ACHIEVEMENTS[var0].draw(64, 64);
      Drawing.b669(12, 12, 256, 256);
      Drawing.restoreContext();
    }

    return var1;
  }

  private static void a713ir(int var0, int var1, int var2, final int var3) {
    if (Drawing.left <= var1 && var1 < Drawing.right) {
      if (Drawing.top > var2) {
        var0 += var2 - Drawing.top;
        var2 = Drawing.top;
      }

      if (Drawing.bottom < var0 + var2) {
        var0 = -var2 + Drawing.bottom;
      }

      for (int var5 = var1 + Drawing.width * var2; var0 > 0; --var0) {
        var1 = Drawing.screenBuffer[var5];
        var2 = var3 + var1;
        final int var4 = (var3 & 16711935) + (var1 & 16711935);
        var1 = (var4 & 16777472) + (65536 & var2 - var4);
        final int var01 = -(var1 >>> 8) + var1;
        final int var11 = -var1 + var2;
        Drawing.screenBuffer[var5] = var01 | var11;
        var5 += Drawing.width;
      }

    }
  }

  private static RankingsRequest a250jba() {
    return rankingsRequests.stream()
        .filter(var3 -> var3._k == 0).findFirst()
        .orElseGet(() -> {
          final RankingsRequest var3 = new RankingsRequest();
          var3._k = 0;
          var3._j = 10;
          rankingsRequests.add(var3);
          C2SPacket.a970uf(var3);
          return var3;
        });
  }

  private static int a353aa(final int var0) {
    final int var3 = 1 + SMALL_FONT.ascent * 3 / 4;
    final int var4 = _tvcr[6] - 60;
    final byte var5 = 40;
    int var6;
    int var7;
    if (var0 == 0) {
      var6 = GameUI.breakLines(ShatteredPlansClient.templateDictionary.expand(StringConstants.TEXT_INSTRUCTIONS_0), SMALL_FONT, new int[]{var4});
      var7 = var5 + var6 * var3;
    } else if (var0 == 1) {
      var6 = GameUI.breakLines(ShatteredPlansClient.templateDictionary.expand(StringConstants.TEXT_INSTRUCTIONS_GLOSSARY_1), SMALL_FONT, new int[]{var4});
      var7 = var5 + var3 * var6;
    } else if (var0 == 2) {
      var6 = GameUI.breakLines(ShatteredPlansClient.templateDictionary.expand(StringConstants.TEXT_INSTRUCTIONS_GLOSSARY_2), SMALL_FONT, new int[]{var4});
      var7 = var5 + var3 * var6;
    } else {
      if (var0 == 3) {
        return 294;
      }

      if (var0 == 4) {
        return 294;
      }

      String var2;
      if (var0 == 5) {
        var2 = StringConstants.TEXT_INSTRUCTIONS_PLACEMENT;
        var6 = GameUI.breakLines(ShatteredPlansClient.templateDictionary.expand(var2), SMALL_FONT, new int[]{var4 - 80});
        var7 = var5 + (var3 * (3 + 4 * var6) >> 2);
        var2 = StringConstants.TEXT_INSTRUCTIONS_MOVEMENT;
        var6 = GameUI.breakLines(ShatteredPlansClient.templateDictionary.expand(var2), SMALL_FONT, new int[]{var4 - 80});
        var2 = StringConstants.TEXT_INSTRUCTIONS_PROJECTS;
        var7 += (var6 * 4 + 3) * var3 >> 2;
        var6 = GameUI.breakLines(ShatteredPlansClient.templateDictionary.expand(var2), SMALL_FONT, new int[]{var4 - 80});
        var7 += var6 * var3;
      } else if (var0 == 6) {
        var2 = StringConstants.TEXT_INSTRUCTIONS_FLEETSIZE;
        var6 = GameUI.breakLines(ShatteredPlansClient.templateDictionary.expand(var2), SMALL_FONT, new int[]{var4 - 80});
        var2 = StringConstants.TEXT_INSTRUCTIONS_END_TURN;
        var7 = var5 + (var3 * (3 + 4 * var6) >> 2);
        var6 = GameUI.breakLines(ShatteredPlansClient.templateDictionary.expand(var2), SMALL_FONT, new int[]{var4 - 80});
        var2 = StringConstants.TEXT_INSTRUCTIONS_HOTKEYS;
        var7 += (var6 * 4 + 3) * var3 >> 2;
        var6 = GameUI.breakLines(ShatteredPlansClient.templateDictionary.expand(var2), SMALL_FONT, new int[]{var4 - 80});
        var2 = StringConstants.TEXT_INSTRUCTIONS_STATS;
        var7 += (4 * var6 + 3) * var3 >> 2;
        var6 = GameUI.breakLines(ShatteredPlansClient.templateDictionary.expand(var2), SMALL_FONT, new int[]{var4 - 80});
        var7 += var6 * var3;
      } else if (var0 == 7) {
        var2 = StringConstants.TEXT_INSTRUCTIONS_ANIMATION;
        var6 = GameUI.breakLines(ShatteredPlansClient.templateDictionary.expand(var2), SMALL_FONT, new int[]{var4 - 80});
        final short var8 = 294;
        var7 = var8 + var3 * (var6 - 3);
      } else if (var0 == 8) {
        var2 = StringConstants.TEXT_INSTRUCTIONS_PROJECT_METAL;
        var6 = GameUI.breakLines(ShatteredPlansClient.templateDictionary.expand(var2), SMALL_FONT, new int[]{var4 - 40});
        var2 = StringConstants.TEXT_INSTRUCTIONS_PROJECT_BIOMASS;
        var7 = var5 + ((3 + var6 * 4) * var3 >> 2);
        var6 = GameUI.breakLines(ShatteredPlansClient.templateDictionary.expand(var2), SMALL_FONT, new int[]{var4 - 40});
        var7 += (4 * var6 + 3) * var3 >> 2;
        var2 = StringConstants.TEXT_INSTRUCTIONS_PROJECT_ENERGY;
        var6 = GameUI.breakLines(ShatteredPlansClient.templateDictionary.expand(var2), SMALL_FONT, new int[]{var4 - 40});
        var2 = StringConstants.TEXT_INSTRUCTIONS_PROJECT_EXOTICS;
        var7 += (4 * var6 + 3) * var3 >> 2;
        var6 = GameUI.breakLines(ShatteredPlansClient.templateDictionary.expand(var2), SMALL_FONT, new int[]{var4 - 40});
        var7 += var6 * var3;
        var7 += var3;
      } else {
        if (var0 == 9) {
          var6 = GameUI.breakLines(ShatteredPlansClient.templateDictionary.expand(StringConstants.TEXT_INSTRUCTIONS_GAME_TYPE), SMALL_FONT, new int[]{var4});
        } else {
          if (var0 != 10) {
            throw new RuntimeException();
          }

          var6 = GameUI.breakLines(ShatteredPlansClient.templateDictionary.expand(StringConstants.TEXT_INSTRUCTIONS_CLASSIC), SMALL_FONT, new int[]{var4});
        }
        var7 = var5 + var6 * var3;
      }
    }

    return Math.max(var7, 294);
  }

  private static void a587oa() {
    int var0 = ShatteredPlansClient.SCREEN_HEIGHT;
    int var1 = 0;
    int var2 = ShatteredPlansClient.SCREEN_WIDTH;
    final int var3 = 0;

    if (Drawing.height < var0 + var3) {
      var0 = Drawing.height - var3;
    }

    if (Drawing.width < var2 + var1) {
      var2 = Drawing.width - var1;
    }

    --var1;
    final int var4 = var0 + var3;

    for (int var5 = var3; var4 > var5; ++var5) {
      int var7 = var1 + Drawing.width * var5;

      for (int var8 = var2; var8 > 0; --var8) {
        ++var7;
        int var9 = Drawing.screenBuffer[var7];
        var9 = (var9 >> 2 & 4144959) + (8355711 & var9 >> 1);
        Drawing.screenBuffer[var7] = var9;
      }
    }

  }

  private static void f423cc() {
    final int var1;
    if (Sounds.soundVolume >= 224) {
      Sounds.setSoundVolume(256);
    } else {
      var1 = Sounds.soundVolume % 32;
      Sounds.setSoundVolume(-var1 + 32 + Sounds.soundVolume);
    }

  }

  private static void d487qs() {
    if (Sounds.musicVolume > 32) {
      int var1 = Sounds.musicVolume % 32;
      if (var1 == 0) {
        var1 = 32;
      }

      Sounds.setMusicVolume(-var1 + Sounds.musicVolume);
    } else {
      Sounds.setMusicVolume(0);
    }

  }

  @SuppressWarnings("SameParameterValue")
  public static Sprite captureScreenRect(int x, int y, int width, int height) {
    final int offsetX;
    if (x < 0) {
      width += x;
      offsetX = -x;
      x = 0;
    } else {
      offsetX = 0;
    }

    final int offsetY;
    if (y < 0) {
      height += y;
      offsetY = -y;
      y = 0;
    } else {
      offsetY = 0;
    }

    if (x + width > ShatteredPlansClient.SCREEN_WIDTH) {
      width = ShatteredPlansClient.SCREEN_WIDTH - x;
    }
    if (y + height > ShatteredPlansClient.SCREEN_HEIGHT) {
      height = ShatteredPlansClient.SCREEN_HEIGHT - y;
    }

    if (width >= 0 && height >= 0) {
      final Sprite dest = new Sprite(width, height);

      final int screenStride = Drawing.width - width;
      final int destStride = dest.width - width;
      int screenPos = y * Drawing.width + x - 1;
      int destPos = -1;

      for (int i = 0; i < height; ++i) {
        for (int j = 0; j < width; ++j) {
          ++destPos;
          ++screenPos;
          dest.pixels[destPos] = Drawing.screenBuffer[screenPos];
        }

        screenPos += screenStride;
        destPos += destStride;
      }

      dest.y = offsetY;
      dest.x = offsetX;
      return dest;
    } else {
      return null;
    }
  }

  public static void drawPanel(final int x, final int y, final int width, final int height, final int color, final boolean shiny) {
    if (width > 20 && height > 20) {
      final int[] bounds = new int[4];
      Drawing.saveBoundsTo(bounds);
      Drawing.fillRoundedRect(x, y, width, height, 10, 0, 200);
      FloatingPanel.a669am(width - 10, color, height - 10, x, y);
      Drawing.horizontalLine(x + 10, y, width - 20, 2052949);
      Drawing.horizontalLine(10 + x, y + height, width - 20, 0);
      Drawing.expandBoundsToInclude(x, y, x + 10, 10 + y);
      Drawing.strokeCircle(x + 10, 10 + y, 10, 2052949);
      Drawing.restoreBoundsFrom(bounds);
      Drawing.expandBoundsToInclude(width - 10 + x, y, x + width, y + 10);
      Drawing.strokeCircle(x + width - 11, 10 + y, 10, 2052949);
      Drawing.restoreBoundsFrom(bounds);
      Drawing.expandBoundsToInclude(x, y + (height - 10), 10 + x, y + height);
      Drawing.strokeCircle(x + 10, height - 10 + y - 1, 10, 0);
      Drawing.restoreBoundsFrom(bounds);
      Drawing.expandBoundsToInclude(x + width - 10, height - 10 + y, x + width, height + y);
      Drawing.strokeCircle(width + x - 10 - 1, height + y - 11, 10, 0);
      Drawing.restoreBoundsFrom(bounds);

      for (int var8 = 0; var8 < height - 20; ++var8) {
        final int color2 = Drawing.alphaOver(0, 0x1f5355, var8 * 256 / (height - 20));
        Drawing.setPixel(x, 10 + y + var8, color2);
        Drawing.setPixel(width + (x - 1), var8 + 10 + y, color2);
      }

      if (shiny) {
        SHINE_LEFT.drawAdd(x + 4, y + 3, 256);
        final int var8 = x + 4 + SHINE_LEFT.width;
        final int var9 = x + width - 3 - SHINE_RIGHT.width;
        SHINE_RIGHT.drawAdd(var9, 3 + y, 64);

        for (int var11 = var8; var11 < var9; ++var11) {
          final int var10 = 192 * (var9 - var11) / (-var8 + var9) + 64;
          SHINE_MID.drawAdd(var11, 3 + y, var10);
        }
      }
    }
  }

  public static void drawPanel(final int x, final int y, final int width, final int height, final int color) {
    drawPanel(x, y, width, height, color, false);
  }

  private static void a669ks(int var0, final int var2, int var3, int var4) {
    if (Drawing.top > var4) {
      var3 += (var4 - Drawing.top) * var2;
      var0 += var4 - Drawing.top;
      var4 = Drawing.top;
    }

    if (Drawing.bottom < var4 + var0) {
      var0 = Drawing.bottom - var4;
    }

    final int var5 = Drawing.width + var2;
    int var7 = Drawing.width * var4 + var3;

    for (int var8 = var3; var0 > 0; var8 += var2) {
      if (Drawing.left <= var8 && var8 < Drawing.right) {
        var3 = Drawing.screenBuffer[var7];
        var4 = 2458760 + var3;
        final int var6 = (16711935 & var3) + (2424968);
        var3 = (16777472 & var6) + (var4 - var6 & 65536);
        final int var01 = -(var3 >>> 8) + var3;
        Drawing.screenBuffer[var7] = var01 | var4 - var3;
      }

      var7 += var5;
      --var0;
    }

  }

  public static Sprite achievementIcon(final int index) {
    Sprite sprite = ACHIEVEMENT_ICONS[index];
    if (sprite == null) {
      ACHIEVEMENT_ICONS[index] = sprite = new Sprite(32, 32);
      Drawing.saveContext();
      sprite.installForDrawing();
      ACHIEVEMENTS[index].g093();
      Drawing.restoreContext();
    }
    return sprite;
  }

  private static int breakLinesLogin(final Font font, final String text, final String[] lines) {
    //noinspection SuspiciousNameCombination
    final int targetWidth = ShatteredPlansClient.SCREEN_HEIGHT;
    final int singleLineWidth = font.measureLineWidth(text);
    if (singleLineWidth <= targetWidth && !text.contains("<br>")) {
      lines[0] = text;
      return 1;
    }

    final int var6 = (targetWidth + singleLineWidth - 1) / targetWidth;
    final int var100 = singleLineWidth / var6;
    int i = 0;
    int var7 = 0;

    for (int j = 0; j < text.length(); ++j) {
      final char c = text.charAt(j);
      if (c == ' ' || c == '-') {
        final String var11 = text.substring(var7, 1 + j).trim();
        final int var12 = font.measureLineWidth(var11);
        if (var12 >= var100) {
          var7 = 1 + j;
          lines[i++] = var11;
        }
      }

      if (c == '>' && text.regionMatches(j - 3, "<br>", 0, 4)) {
        lines[i++] = text.substring(var7, j - 3).trim();
        var7 = 1 + j;
      }
    }

    if (var7 < text.length()) {
      lines[i++] = text.substring(var7).trim();
    }

    return i;
  }

  private static int a313ch(final int var0, final int var1) {
    int var2 = 0;

    for (int var3 = _vob; _tc.length > var2; ++var2) {
      final int var4 = _kbD[var2];
      if (var4 < 0) {
        var3 += get_idb();
      } else {
        final int var5 = a234or(_tc[var2]);
        var3 += 2;
        final int var6 = -(var5 >> 1) + ShatteredPlansClient.SCREEN_CENTER_X;
        if (a423((8 << 1) + get_idb(), var1, var6 - 8, var3, (8 << 1) + var5, var0)) {
          return var4;
        }

        var3 += get_idb() + 2 + (8 << 1);
      }
    }

    return -1;
  }

  public static void showLobbyDialog(final String text, final boolean var6, final int which) {
    isLobbyDialogOpen = true;
    if (which == 0) {
      final int var7 = breakLinesLogin(FONT, text, BROKEN_LINES_LOGIN);
      final int var8 = var7 + 3;
      _tc = new String[var8];
      _kbD = new int[var8];

      for (int var9 = 0; var9 < var8; ++var9) {
        _kbD[var9] = -1;
      }

      _py = new int[2];

      if (var7 >= 0) {
        System.arraycopy(BROKEN_LINES_LOGIN, 0, _tc, 0, var7);
      }

      _tc[var8 - 3] = "";
      _tc[var8 - 2] = StringConstants.FS_BUTTON_MEMBERS;
      _kbD[var8 - 2] = 0;
      _py[0] = 1;
      _tc[var8 - 1] = StringConstants.FS_BUTTON_CLOSE;
      _kbD[var8 - 1] = 1;
      _py[1] = 2;
    } else if (which == 1) {
      final int var7 = breakLinesLogin(FONT, text, BROKEN_LINES_LOGIN);
      final int var8 = var7 + 2;
      _tc = new String[var8];
      _kbD = new int[var8];

      for (int var9 = 0; var9 < var8; ++var9) {
        _kbD[var9] = -1;
      }

      _py = new int[1];

      if (var7 >= 0) System.arraycopy(BROKEN_LINES_LOGIN, 0, _tc, 0, var7);

      _tc[var8 - 2] = "";
      _tc[var8 - 1] = StringConstants.FS_BUTTON_CLOSE;
      _kbD[var8 - 1] = 0;
      _py[0] = 2;
    } else {
      throw new IllegalArgumentException();
    }

    _nsnb.itemCount = _py.length;
    int var7 = 0;

    for (int var8 = 0; _tc.length > var8; ++var8) {
      int var9 = a234or(_tc[var8]);
      if (_kbD[var8] != -1) {
        var9 += 2 * 8;
      }

      if (var7 < var9) {
        var7 = var9;
      }
    }

    _ahR = -(var7 >> 1) + var7 + ShatteredPlansClient.SCREEN_CENTER_X;
    _ldj = -(var7 >> 1) + ShatteredPlansClient.SCREEN_CENTER_X;
    _rnb = (2 + 8 << 1) * _nsnb.itemCount;

    for (int var8 = 0; var8 < _tc.length; ++var8) {
      _rnb += get_idb();
    }

    _vob = ShatteredPlansClient.SCREEN_CENTER_Y - (_rnb >> 1);
    _nsnb.setSelectedItem(a313ch(JagexApplet.mouseY, JagexApplet.mouseX), 0, var6);
  }

  private static boolean a423(final int var0, final int var1, final int var2, final int var4, final int var5, final int var6) {
    return var2 <= var1 && var2 + var5 > var1 && var6 >= var4 && var6 < var0 + var4;
  }

  private static int a234or(final String var1) {
    return FONT.measureLineWidth(var1);
  }

  static void showFullscreenDialog(final @NotNull Menu.FullscreenDialog which, final boolean isMouseSelection) {
    currentFullscreenDialog = which;
    isFullscreenDialogOpen = true;
    if (currentFullscreenDialog == FullscreenDialog.MEMBERS_ONLY_NOT_LOGGED_IN) {
      final int var3 = breakLinesLogin(FONT, StringConstants.FS_NONMEMBER, _tsK);
      final int var4 = 3 + var3;
      _tuef = new String[var4];
      _ssa = new int[var4];

      for (int i = 0; i < var4; ++i) {
        _ssa[i] = -1;
      }

      _E = new DialogOption[2];

      if (var3 >= 0) System.arraycopy(_tsK, 0, _tuef, 0, var3);

      _tuef[var4 - 3] = "";
      _tuef[var4 - 2] = StringConstants.FS_BUTTON_MEMBERS;
      _ssa[var4 - 2] = 0;
      _E[0] = DialogOption.MEMBERS;
      _tuef[var4 - 1] = StringConstants.FS_BUTTON_CLOSE;
      _ssa[var4 - 1] = 1;
      _E[1] = DialogOption.CLOSE;
    } else if (currentFullscreenDialog == FullscreenDialog.MEMBERS_ONLY_LOGGED_IN) {
      final int var3 = breakLinesLogin(FONT, StringConstants.FS_NONMEMBER, _tsK);
      final int var4 = 2 + var3;
      _tuef = new String[var4];
      _ssa = new int[var4];

      for (int var5 = 0; var4 > var5; ++var5) {
        _ssa[var5] = -1;
      }

      _E = new DialogOption[1];

      if (var3 >= 0) System.arraycopy(_tsK, 0, _tuef, 0, var3);

      _tuef[var4 - 2] = "";
      _tuef[var4 - 1] = StringConstants.FS_BUTTON_CLOSE;
      _ssa[var4 - 1] = 0;
      _E[0] = DialogOption.CLOSE;
    } else if (currentFullscreenDialog == FullscreenDialog.ACCEPT_COUNTDOWN) {
      final int var3 = breakLinesLogin(FONT, Strings.format(StringConstants.FS_ACCEPT_COUNTDOWN_SING, "<br><%0><br>"), _tsK);
      final int var4 = IntStream.range(0, var3).filter(var5 -> "<%0>".equals(_tsK[var5])).findFirst().orElse(-1);
      if (var4 == -1) {
        throw new IllegalStateException();
      }

      _kpo = new String[var4];
      System.arraycopy(_tsK, 0, _kpo, 0, var4);
      _kdb = new String[var3 - var4 - 1];
      final int len = var3 + (-var4 - 1);
      System.arraycopy(_tsK, var4 + 1, _kdb, 0, len);
      final int var3a = breakLinesLogin(FONT, Strings.format(StringConstants.FS_ACCEPT_COUNTDOWN_PL, "<br><%0><br>"), _tsK);
      final int var4a = IntStream.range(0, var3a).filter(var5 -> "<%0>".equals(_tsK[var5])).findFirst().orElse(-1);
      if (var4a == -1) {
        throw new IllegalStateException();
      }

      _nld = new String[var4a];
      System.arraycopy(_tsK, 0, _nld, 0, var4a);
      _hmp = new String[var3a - var4a - 1];
      System.arraycopy(_tsK, var4a + 1, _hmp, 0, var3a - var4a - 1);
      final int var5 = Math.max(_kpo.length, _nld.length);
      final int var6 = Math.max(_hmp.length, _kdb.length);
      final int var7 = var6 + var5 + 7;
      _tuef = new String[var7];
      _ssa = new int[var7];

      for (int var8 = 0; var8 < var7; ++var8) {
        _ssa[var8] = -1;
      }

      _E = new DialogOption[2];
      _tuef[0] = StringConstants.FS_ACCEPT_BEFORE_ACCEPT;
      _ssa[1] = 0;
      _tuef[1] = StringConstants.FS_BUTTON_ACCEPT;
      _tuef[2] = StringConstants.FS_ACCEPT_AFTER_ACCEPT;
      _ssa[3] = 1;
      _tuef[5] = "";
      _E[1] = DialogOption.ACCEPT;
      _E[0] = DialogOption.CLOSE;
      _tuef[3] = StringConstants.FS_BUTTON_CANCEL;
      _tuef[4] = StringConstants.FS_ACCEPT_AFTER_CANCEL;

      for (int var8 = 0; var8 < var5; ++var8) {
        _tuef[var8 + 6] = _nld.length + var8 - var5 >= 0 ? _nld[var8 - (-_nld.length + var5)] : "";
      }

      _tuef[var5 + 6] = null;
      _ssa[6 + var5] = -2;

      for (int var8 = 0; var6 > var8; ++var8) {
        _tuef[7 - (-var5 - var8)] = var8 >= _hmp.length ? "" : _hmp[var8];
      }

      _brp = PseudoMonotonicClock.currentTimeMillis();
    } else if (currentFullscreenDialog == FullscreenDialog.UNAVAILABLE) {
      final int var3 = breakLinesLogin(FONT, StringConstants.FS_UNAVAILABLE, _tsK);

      final int var4 = 2 + var3;
      _tuef = new String[var4];
      _ssa = new int[var4];

      for (int var5 = 0; var5 < var4; ++var5) {
        _ssa[var5] = -1;
      }

      _E = new DialogOption[1];

      if (var3 >= 0) System.arraycopy(_tsK, 0, _tuef, 0, var3);

      _tuef[var4 - 2] = "";
      _tuef[var4 - 1] = StringConstants.FS_BUTTON_CLOSE;
      _ssa[var4 - 1] = 0;
      _E[0] = DialogOption.CLOSE;
    } else if (currentFullscreenDialog == FullscreenDialog.LOST_FOCUS) {
      final int var3 = breakLinesLogin(FONT, StringConstants.FS_FOCUS, _tsK);
      final int var4 = var3 + 2;
      _tuef = new String[var4];
      _ssa = new int[var4];

      for (int var5 = 0; var4 > var5; ++var5) {
        _ssa[var5] = -1;
      }

      _E = new DialogOption[1];

      if (var3 >= 0) System.arraycopy(_tsK, 0, _tuef, 0, var3);

      _tuef[var4 - 2] = "";
      _tuef[var4 - 1] = StringConstants.FS_BUTTON_CLOSE;
      _ssa[var4 - 1] = 0;
      _E[0] = DialogOption.CLOSE;
    } else if (currentFullscreenDialog == FullscreenDialog.TIMEOUT) {
      final int var3 = breakLinesLogin(FONT, StringConstants.FS_TIMEOUT, _tsK);
      final int var4 = 3 + var3;
      _tuef = new String[var4];
      _ssa = new int[var4];

      for (int var5 = 0; var4 > var5; ++var5) {
        _ssa[var5] = -1;
      }

      _E = new DialogOption[2];

      if (var3 >= 0) System.arraycopy(_tsK, 0, _tuef, 0, var3);

      _tuef[var4 - 3] = "";
      _tuef[var4 - 2] = StringConstants.FS_BUTTON_TRY_AGAIN;
      _ssa[var4 - 2] = 0;
      _E[0] = DialogOption.TRY_AGAIN;
      _tuef[var4 - 1] = StringConstants.FS_BUTTON_CLOSE;
      _ssa[var4 - 1] = 1;
      _E[1] = DialogOption.CLOSE;
    } else {
      throw new IllegalArgumentException();
    }

    _gsl.itemCount = _E.length;
    int var3 = 0;

    for (final String item : _tuef) {
      final int var5 = vm_.a827(item);
      if (var3 < var5) {
        var3 = var5;
      }
    }

    if (currentFullscreenDialog == FullscreenDialog.ACCEPT_COUNTDOWN) {
      String var10;
      for (final String value : _kpo) {
        var10 = value;
        final int var7 = vm_.a827(var10);
        if (var7 > var3) {
          var3 = var7;
        }
      }

      for (final String s : _kdb) {
        var10 = s;
        final int var7 = vm_.a827(var10);
        if (var7 > var3) {
          var3 = var7;
        }
      }
    }

    _dmra = (2 + 8 << 1) * _gsl.itemCount;
    _dbf = ShatteredPlansClient.SCREEN_CENTER_X - (var3 >> 1);
    _hoa = -(var3 >> 1) + var3 + ShatteredPlansClient.SCREEN_CENTER_X;

    for (int var4 = 0; var4 < _tuef.length; ++var4) {
      _dmra += get_idb();
    }

    _uqk = ShatteredPlansClient.SCREEN_CENTER_Y - (_dmra >> 1);
    if (currentFullscreenDialog == FullscreenDialog.ACCEPT_COUNTDOWN) {
      _gsl.setSelectedItem(-1, -1, isMouseSelection);
    } else {
      _gsl.setSelectedItem(a313gui(JagexApplet.mouseX, JagexApplet.mouseY), 0, isMouseSelection);
    }
  }

  public static void a540fm(final boolean isMouseSelection) {
    final FullscreenDialog var2;
    if (JagexApplet.membershipLevel <= 0) {
      if (JagexApplet.isAnonymous) {
        var2 = FullscreenDialog.MEMBERS_ONLY_NOT_LOGGED_IN;
      } else {
        var2 = FullscreenDialog.MEMBERS_ONLY_LOGGED_IN;
      }
    } else {
      ShatteredPlansClient.fullScreenCanvas = createFullScreenCanvas();

      if (ShatteredPlansClient.fullScreenCanvas == null) {
        var2 = FullscreenDialog.UNAVAILABLE;
      } else {
        JagexApplet.attachToCanvas(ShatteredPlansClient.fullScreenCanvas);
        var2 = FullscreenDialog.ACCEPT_COUNTDOWN;
      }
    }

    showFullscreenDialog(var2, isMouseSelection);
  }

  private static FullScreenCanvas createFullScreenCanvas() {
    final Frame var5 = JagexApplet.createFullScreenFrame();
    if (var5 == null) {
      return null;
    } else {
      final FullScreenCanvas var6 = new FullScreenCanvas();
      var6.frame = var5;
      var6.frame.add(var6);
      var6.setBounds(0, 0, ShatteredPlansClient.SCREEN_WIDTH, ShatteredPlansClient.SCREEN_HEIGHT);
      var6.addFocusListener(var6);
      var6.requestFocus();
      return var6;
    }
  }

  static int a313gui(final int var0, final int var1) {
    int var2 = 0;

    for (int var3 = _uqk; _tuef.length > var2; ++var2) {
      final int var4 = _ssa[var2];
      if (var4 >= 0) {
        final int var5 = vm_.a827(_tuef[var2]);
        final int var6 = ShatteredPlansClient.SCREEN_CENTER_X - (var5 >> 1);
        var3 += 2;
        if (a423(get_idb() + (8 << 1), var0, var6 - 8, var3, (8 << 1) + var5, var1)) {
          return var4;
        }

        var3 += get_idb() + (8 << 1) + 2;
      } else {
        var3 += get_idb();
      }
    }

    return -1;
  }

  private static void setItems(@MagicConstant(valuesFromClass = Id.class) final int group, final int[] items) {
    Menu.items[group] = items;
    menus[group] = new Menu(group);
  }

  public static void updateAvailableMenuItems() {
    setItems(Id.RATINGS, RATINGS_MENU_ITEMS);
    setItems(Id.ACHIEVEMENTS, ACHIEVEMENTS_MENU_ITEMS);
    if (!JagexApplet.isAnonymous && JagexApplet.membershipLevel > 0) {
      setItems(Id.PAUSE_SINGLEPLAYER, MEMBERS_PAUSE_MENU_SINGLEPLAYER_ITEMS);
      setItems(Id.PAUSE_MULTIPLAYER_1, MEMBERS_PAUSE_MENU_MULTIPLAYER_ITEMS_1);
      setItems(Id.PAUSE_MULTIPLAYER_2, MEMBERS_PAUSE_MENU_MULTIPLAYER_ITEMS_2);
      setItems(Id.PAUSE_MULTIPLAYER_3, MEMBERS_PAUSE_MENU_MULTIPLAYER_ITEMS_3);
    }
  }

  static void closeLobbyDialog() {
    isLobbyDialogOpen = false;
  }

  static int a137ch() {
    boolean var0 = false;

    while (JagexApplet.nextTypedKey()) {
      _nsnb.processKeyInputVertical();
      if (_nsnb.isItemActive()) {
        var0 = true;
      }
    }

    _nsnb.processMouseInput(a313ch(JagexApplet.mouseY, JagexApplet.mouseX), a313ch(JagexApplet.mousePressY, JagexApplet.mousePressX));
    if (_nsnb.isItemActive()) {
      var0 = true;
    }

    int var1 = 0;
    if (var0 && _nsnb.selectedItem >= 0) {
      var1 = _py[_nsnb.selectedItem];
      if (var1 == 2) {
        closeLobbyDialog();
      }
    }

    return var1;
  }

  static void a150rg() {
    final int var0 = _ahR - _ldj;
    _ldj = -(var0 >> 1) + ShatteredPlansClient.SCREEN_CENTER_X;
    _vob = -(_rnb >> 1) + ShatteredPlansClient.SCREEN_CENTER_Y;
    _ahR = _ldj + var0;
    int var1 = _vob;

    for (int var2 = 0; _tc.length > var2; ++var2) {
      final int var3 = _kbD[var2];
      final int var4;
      if (var3 >= 0) {
        if (var3 == _nsnb.selectedItem) {
          var4 = 0x2ad0d6;
        } else {
          var4 = 0x258488;
        }
      } else {
        var4 = 0x258488;
      }

      final String var5 = _tc[var2];
      final int var6 = a234or(var5);
      final int var7 = ShatteredPlansClient.SCREEN_CENTER_X - (var6 >> 1);
      if (var3 >= 0) {
        var1 += 2;
        final NineSliceSprite var8 = buttonSprite;
        if (var8 != null) {
          var8.draw((8 << 1) + get_idb(), -8 + var7, var1, var6 + (8 << 1));
        }

        var1 += 8;
      }

      FONT.draw(var5, var7, var1 + FONT.capHeight, var4);
      if (var3 < 0) {
        var1 += get_idb();
      } else {
        var1 += get_idb() + 2 + 8;
      }
    }

  }

  public void b093(final int var2) {
    final int var3 = this.c137();
    if (var3 != 0) {
      if (ClientGameSession.playSession != null || ClientGameSession.spectateSession != null) {
        GameUI.fadeRect(0, 0, ShatteredPlansClient.SCREEN_WIDTH, ShatteredPlansClient.SCREEN_HEIGHT, 192 + (var2 >> 1));
      }

      final int var4 = (ShatteredPlansClient.SCREEN_WIDTH - var3) / 2;
      final int var5 = this.h137();
      final int var6 = this.b410();

      final int var8 = 20 + var3;
      final int var9 = var5 - 10;
      final int var10 = var6 + 10;
      if (ShatteredPlansClient.renderQuality.transitionQuality == RenderQuality.Level.HIGH
          || ShatteredPlansClient.renderQuality.transitionQuality == RenderQuality.Level.MEDIUM) {
        GameUI.fadeRect(var4, var9, var8, var10, var2 + 128);
      } else {
        Drawing.fillRect(var4, var9, var8, var10, 0x0d0d14, (128 - var2) << 1);
      }

      drawPanel(var4, var9, var8, var10);
      Drawing.saveBoundsTo(_sf);
      Drawing.setBounds(var4, var9, var4 + var8, var9 + var10);
      this.e093(var5, 7903);
      Drawing.restoreBoundsFrom(_sf);
    }
  }

  private void f423() {
    _cleo = this.b313(JagexApplet.mouseX, JagexApplet.mouseY);
    int var2 = 0;
    if (JagexApplet.mouseButtonJustClicked != MouseState.Button.NONE) {
      final int var3 = this.b313(JagexApplet.mousePressX, JagexApplet.mousePressY);
      if (var3 >= 0) {
        this.d093(var3);
      }
    }

    if (JagexApplet.mouseWheelRotation != 0) {
      var2 = JagexApplet.mouseWheelRotation * -20;
    }

    if (JagexApplet.langId == 0) {
      if (JagexApplet.mouseButtonJustClicked != MouseState.Button.NONE) {
        if (_cleo == -2) {
          this.a366(-1);
        } else if (_cleo == -3) {
          this.a366(1);
        }
      }
    } else if (JagexApplet.mouseButtonDown != MouseState.Button.NONE) {
      if (_cleo == -2) {
        var2 += 10;
      } else if (_cleo == -3) {
        var2 -= 10;
      }
    }

    if (JagexApplet.mouseButtonDown != MouseState.Button.NONE && _cleo == -1) {
      if (this._f) {
        var2 = -this._i + JagexApplet.mouseY;
      } else {
        this._f = true;
      }
      this._i = JagexApplet.mouseY;
    } else {
      this._f = false;
    }

    if (var2 != 0) {
      _c -= var2;
      _acw -= var2;
      if (_c < 0) {
        _c = 0;
      }

      if (_acw < 0) {
        _acw = 0;
      }

      if (_kbA < _c) {
        _c = _kbA;
      }

      if (_kbA < _acw) {
        _acw = _kbA;
      }
    }

  }

  private int b474() {
    if (currentMenu != this.id && nextMenu != this.id) {
      return 0;
    } else if (nextMenu != currentMenu && currentMenu >= 0 && nextMenu >= 0) {
      final double var2 = MathUtil.ease(_ehQ, 32);
      final int var4 = (int) ((-var2 + 1.0D) * (double) (80 + _tvcr[currentMenu]));
      int var5 = -(((int) (var2 * (double) (_tvcr[nextMenu] - _tvcr[currentMenu])) + _tvcr[currentMenu]) / 2) + 320;
      var5 += var4;
      return currentMenu != this.id ? var5 : var5 - _tvcr[currentMenu] - 80;
    } else {
      return (ShatteredPlansClient.SCREEN_WIDTH - this.c137()) / 2;
    }
  }

  private void a326(int var1, int var2) {
    int var4 = _mgfb == 0 ? _acw : MathUtil.ease(_mgfb, 32, _c, _acw);
    final int var5 = StringConstants.INSTRUCTIONS_TABNAMES.length;
    if (var4 < 0) {
      var4 = 0;
    }

    if (_kbA < var4) {
      var4 = _kbA;
    }

    final int var6 = var1 - (-_tvcr[this.id] - 18);
    var1 -= 8;
    Drawing.fillRoundedRect(var1 + 20, var2 + 2, 18 + _tvcr[this.id] - 60 + _ok.width, 309, 10, 0, 92);
    Drawing.f669(20 + var1, 2 + var2, _tvcr[this.id] - 60 + 18 + _ok.width, 309, 10, 2458760);
    final int[] var7 = new int[4];
    Drawing.saveBoundsTo(var7);
    Drawing.expandBoundsToInclude(25 + var1, 5 + var2, var6 - 20, var2 + _tvct[this.id] - 14);
    int var8 = -var4;

    int var9;
    for (var9 = 0; var9 < var5; ++var9) {
      if (var9 > 0) {
        Drawing.horizontalLine(var1 + 30, 3 + var2 + var8 - 20, -var1 + var6 - 80, Drawing.WHITE);
        Drawing.horizontalLine(var1 + 30, var8 + var2 + 3 - 40, -var1 + var6 - 80, Drawing.WHITE);
        SMALL_FONT.draw(StringConstants.INSTRUCTIONS_TABNAMES[var9], var1 + 50, SMALL_FONT.ascent / 2 + 3 + var2 + var8 - 30, Drawing.WHITE);
      }

      if (var8 <= 294 && var8 >= -294) {
        this.a258(var8 + var2 + 3, var9, var1 + 30);
      }

      var8 += _cbi[var9];
    }

    Drawing.restoreBoundsFrom(var7);
    if (_kbA > var4) {
      ShatteredPlansClient._bga.drawAdd(var1 - (-_tvcr[this.id] + 26), _tvct[this.id] + var2 - 35, 256);
      if (_cleo == -3) {
        ShatteredPlansClient._bga.drawAdd(_tvcr[this.id] + var1 - 26, var2 + _tvct[this.id] - 35, 256);
      }
    }

    if (var4 > 0) {
      _ok.drawAdd(var1 + _tvcr[this.id] - 26, 5 + var2, 256);
      if (_cleo == -2) {
        _ok.drawAdd(_tvcr[this.id] + (var1 - 26), var2 + 5, 256);
      }
    }

    var9 = -(21 * var5) + 310;
    var2 += 8;
    int var10 = _nct;

    int var11;
    int var12;
    int var13;
    int var16;
    int var18;
    int var19;
    for (var11 = 0; var5 - 1 > var11; ++var11) {
      var12 = _ssH[var11];
      if (var12 > 12) {
        var12 = 12;
      }

      var13 = var10 == 0 ? 0 : var9 * var12 / var10;
      var10 -= var12;
      var9 -= var13;
      var13 += 21;

      int var14;
      for (var14 = 0; var14 <= 16; ++var14) {
        a713ir(var13, var6 - var14, var2 + var14, 65793);
      }

      a669ks(16, -1, var6, var2);
      a713ir(var13, var6 - 16, 16 + var2, 2458760);
      var14 = _ssH[var11] - 12;
      if (var14 > 0) {
        final String var15 = StringConstants.INSTRUCTIONS_TABNAMES[var11];
        var16 = SMALL_FONT.measureLineWidth(var15);
        final int var17 = SMALL_FONT.descent + SMALL_FONT.ascent;
        var18 = var6 - 15;
        var19 = (var13 - var16 >> 1) + 8 + var2;
        final Sprite var20 = new Sprite(var17, var16);
        Drawing.saveContext();
        var20.installForDrawing();
        SMALL_FONT.drawVertical(var15, 1 + SMALL_FONT.ascent * 3 / 4, var16 - 1, Drawing.WHITE);
        Drawing.restoreContext();
        var20.drawAdd(var18, var19, 256 * var14 / 12);
      }

      var2 += var13;
    }

    var11 = var9 + 16;

    for (var12 = 0; var12 <= 16; ++var12) {
      a713ir(-(var12 * 2) + 32 + var11, -var12 + var6, var2 + var12, 65793);
    }

    a669ks(16, -1, var6, var2);
    a713ir(var11, var6 - 16, var2 + 16, 2458760);
    var12 = this.id == Id.INSTRUCTIONS_2 ? 8 : 16;
    a669ks(var12, 1, var6 - 16, var11 + 15 + var2);
    var13 = _ssH[var5 - 1] << 2;
    if (var13 != 0) {
      final String var21 = StringConstants.INSTRUCTIONS_TABNAMES[var5 - 1];
      final int var22 = SMALL_FONT.measureLineWidth(var21);
      var16 = SMALL_FONT.descent + SMALL_FONT.ascent;
      final Sprite var23 = new Sprite(var16, var22);
      Drawing.saveContext();
      var23.installForDrawing();
      SMALL_FONT.drawVertical(var21, SMALL_FONT.ascent * 3 / 4 + 1, var22 - 1, Drawing.WHITE);
      Drawing.restoreContext();
      var18 = var6 - 14;
      var19 = (-var22 + var11 >> 1) + 16 + var2;
      var23.drawAdd(var18, var19, (int) (2.67D * (double) var13));
    }

  }

  private int d313(final int var1, final int var2, final int var3) {
    if (var1 < 53) {
      this.e093(93, -98);
    }

    int var4 = 140;
    int var5 = 52;
    final int[] var6 = new int[4];
    Drawing.saveBoundsTo(var6);
    Drawing.a797();

    for (int var7 = 0; var7 < 25; ++var7) {
      if (var5 <= var2 && var5 + 32 > var2 && var4 <= var3 && var3 < var4 + 32) {
        return var7;
      }

      var5 += 42;
      if (var7 % 13 == 12) {
        var4 = (int) ((double) var4 + Math.sqrt(3.0D) * 21.0D);
        var5 = 73;
      }
    }

    return -1;
  }

  private void d423() {
    final int var2 = _ssH.length;
    int var3 = _acw + 147;
    int var4 = 0;

    while (var4 < var2) {
      var3 -= _cbi[var4];
      if (var3 < 0) {
        break;
      }

      ++var4;
    }

    _os = var4;
    int var5 = _cleo;
    if (var5 < 0) {
      var5 = _os;
    }

    _nct = 0;

    int var6;
    for (var4 = 0; var4 < _ssH.length; ++var4) {
      var6 = _ssH[var4];
      if (var5 == var4) {
        ++var6;
        if (var6 > 24) {
          var6 = 24;
        } else {
          _ssH[var4] = var6;
        }
      } else {
        --var6;
        if (var6 < 0) {
          var6 = 0;
        } else {
          _ssH[var4] = var6;
        }
      }

      if (var6 > 12) {
        var6 = 12;
      }

      _nct += var6;
    }

    if (_mgfb != 0 && ++_mgfb == 32) {
      _mgfb = 0;
      _c = _acw;
    }

    if (_oob == null) {
      _oob = new int[5];
      _erq = new int[5];
      _fgc = new int[5];

      for (var4 = 0; var4 < 5; ++var4) {
        _erq[var4] = ShatteredPlansClient.randomIntBounded(35);
        _fgc[var4] = ShatteredPlansClient.randomIntBounded(35);
      }
    }

    var6 = ShatteredPlansClient.currentTick % 50;
    if (var6 == 0) {
      for (var4 = 0; var4 < 5; ++var4) {
        _erq[var4] = _fgc[var4];
        _fgc[var4] = ShatteredPlansClient.randomIntBounded(35);
      }
    }

  }

  private void e093(final int var1, final int var2) {
    final int var3 = this.b474();
    final int var5 = this._h % (this.inputState.itemCount * 16);
    if (var2 != 7903) {
      StringConstants.STATUS = null;
    }

    for (int var6 = 0; var6 < this.inputState.itemCount; ++var6) {
      final int var7 = (this._h - var6 * 4) * 256 / (this.inputState.itemCount * 4);
      if (var7 > 0) {
        if (var7 < 256) {
          this.a921(var6, var6 == this.inputState.selectedItem, var7);
        } else {
          this.a663(var6, this.inputState.selectedItem == var6);
        }
      }

      if (items[this.id][var6] != 29 && items[this.id][var6] != 28) {
        final int var4 = this.c417(var6);
        if (var6 == this.inputState.selectedItem) {
          MENU_HEX.draw(25 + var3, (widths[this.id] - MENU_HEX.height) / 2 + var4);
        } else {
          final int var8 = this.inputState.itemCount * 6 + var6 * 4;
          final int var9 = Math.abs(var5 - var8);
          if (var9 < this.inputState.itemCount * 2) {
            MENU_HEX.draw(var3 + 25, var4 + (widths[this.id] - MENU_HEX.height) / 2, 256);
          } else if (var9 < 6 * this.inputState.itemCount) {
            final int var10 = (6 * this.inputState.itemCount - var9) * 256 / (this.inputState.itemCount * 4);
            MENU_HEX.draw(25 + var3, (-MENU_HEX.height + widths[this.id]) / 2 + var4, var10);
          }
        }
      }
    }

    if (this.id == Id.INSTRUCTIONS_1 || this.id == Id.INSTRUCTIONS_2) {
      this.a326(this.b474(), var1);
    } else if (this.id == Id.ACHIEVEMENTS) {
      this.j150();
    } else if (this.id == Id.RATINGS) {
      this.c423();
    } else if (this.id == Id.ERROR) {
      this.e423();
    }

  }

  private void e423() {
    Drawing.saveBoundsTo(_kcm);
    Drawing.a797();
    final int var2 = MathUtil.ease(_onc, 32, 0, 320);
    final short var3 = 200;
    final int var4 = this.b474() - (var2 - this.c137() >> 1);
    final int var5 = this.h137() - 48 - var3;
    if (var2 > 20) {
      drawPanel(var4, var5, var2, var3);
    }

    if (_onc == 32) {
      Drawing.setBounds(var4 + 8, 8 + var5, var2 + (var4 - 8), var3 + (var5 - 8));
      FONT.drawCentered(StringConstants.TEXT_ERROR, (var2 >> 1) + var4, FONT.ascent + 10 + var5, 2805974);
      SMALL_FONT.drawParagraph(ShatteredPlansClient.networkErrorMessage, 10 + var4, FONT.ascent + var5 + 10, var2 - 20, -FONT.ascent + (170), 2805974, Font.HorizontalAlignment.CENTER, Font.VerticalAlignment.MIDDLE, SMALL_FONT.ascent);
    }

    Drawing.restoreBoundsFrom(_kcm);
  }

  private void a921(final int var1, final boolean var2, final int var3) {
    @MagicConstant(valuesFromClass = Item.class)
    final int var5 = items[this.id][var1];
    final String var6 = this.lookupLabel(var5);
    final int var7 = !var2 ? 2458760 : 2805974;
    final int var8 = widths[this.id];
    int var9 = this.getX();
    final int var10 = this.c417(var1);
    FONT.draw(var6, var9, FONT.ascent + var10, var7, var3);
    if (var5 == Item.SOUND_VOLUME || var5 == Item.MUSIC_VOLUME) {
      var9 += _hgt;
      Drawing.d669(var9, var10 + (var8 / 2 - 1), 121, 2, var7, var3);
      final int var11 = var5 != Item.SOUND_VOLUME ? Sounds.musicVolume * 120 / 256 : Sounds.soundVolume * 120 / 256;
      Drawing.d669(var9 + var11 - 1, 3 + var10, 3, var8 - 6, var7, var3);
    }

    if (var1 == this._g) {
      this.a527(var1);
    }
  }

  @SuppressWarnings("SameParameterValue")
  private static void drawLabeledHorizontalBracket(final String label,
                                                   final int labelX,
                                                   final int labelY,
                                                   final int labelWidth,
                                                   final int bracketX1,
                                                   final int bracketX2,
                                                   final int bracketTipsY,
                                                   final int bracketHeight,
                                                   final int color) {
    final int bracketSpineY = bracketTipsY + bracketHeight;
    if (bracketX2 > bracketX1) {
      Drawing.horizontalLine(bracketX1, bracketSpineY, 1 - bracketX1 + bracketX2, color);
    } else {
      Drawing.horizontalLine(bracketX2, bracketSpineY, 1 - bracketX2 + bracketX1, color);
    }

    final int bracketTailX = (bracketX1 + bracketX2) / 2;
    if (bracketTailX < labelX) {
      Drawing.horizontalLine(bracketTailX, labelY, labelX - bracketTailX, color);
    } else {
      Drawing.horizontalLine(labelX, labelY, bracketTailX - labelX, color);
    }

    if (bracketHeight > 0) {
      Drawing.verticalLine(bracketX1, bracketTipsY, bracketHeight, color);
      Drawing.verticalLine(bracketX2, bracketTipsY, bracketHeight, color);
    } else {
      Drawing.verticalLine(bracketX1, bracketSpineY, -bracketHeight, color);
      Drawing.verticalLine(bracketX2, bracketSpineY, -bracketHeight, color);
    }

    if (labelY <= bracketSpineY) {
      Drawing.verticalLine(bracketTailX, labelY, bracketSpineY - labelY, color);
    } else {
      Drawing.verticalLine(bracketTailX, bracketSpineY, labelY - bracketSpineY, color);
    }

    final int lineCount = GameUI.breakLines(label, SMALL_FONT, new int[]{labelWidth});
    SMALL_FONT.drawParagraph(
        label,
        labelX + 5,
        labelY - ((SMALL_FONT.ascent * lineCount) / 2),
        labelWidth,
        lineCount * SMALL_FONT.ascent + SMALL_FONT.descent,
        color,
        Font.HorizontalAlignment.LEFT,
        Font.VerticalAlignment.TOP,
        SMALL_FONT.ascent);
  }

  private void a837(final String var1, int var2, final byte var3, final int var4, final int var5, int var6, final int var7, final int var8, final int var9, int var10) {
    final int var11 = var2 - (-var10 - var6);
    if (var11 != 0) {
      var6 = (200 * var6 + var11) / (2 * var11);
      var2 = (var2 * 200 + var11) / (var11 * 2);
      var10 = (200 * var10 + var11) / (var11 * 2);
    }

    String var12;
    if (var8 != -1) {
      var12 = 1 + var8 + ".";
      SMALL_FONT.drawRightAligned(var12, 15 + _vnr[0] + var5, var7, var9);
    }

    SMALL_FONT.draw(var1, var5 - (-_vnr[0] - 20), var7, var9);
    var12 = Integer.toString(var4);
    SMALL_FONT.drawCentered(var12, var5 + _vnr[1], var7, var9);
    var12 = var11 >= 50 ? "50+" : Integer.toString(var11);
    SMALL_FONT.drawCentered(var12, _vnr[2] + var5, var7, var9);
    var12 = var2 + "%";
    if (var3 >= -116) {
      this.a827(-46, 11, -112, -101, -46, -96);
    }

    SMALL_FONT.drawCentered(var12, var5 + _vnr[3], var7, var9);
    var12 = var10 + "%";
    SMALL_FONT.drawCentered(var12, var5 + _vnr[4], var7, var9);
    var12 = var6 + "%";
    SMALL_FONT.drawCentered(var12, _vnr[5] + var5, var7, var9);
  }

  public void c326(final int var1, final int var3) {
    if (ClientGameSession.playSession != null || ClientGameSession.spectateSession != null) {
      a587oa();
    }

    final int var4 = this.c137();
    final int var5 = this.h137();
    final int var6 = this.b410();
    final double var7 = MathUtil.ease(var3, 128);
    final int var9 = (ShatteredPlansClient.SCREEN_WIDTH - var4) / 2;
    final int var10 = (int) ((double) (_tvcr[this.id] + 80) * (1.0D - var7));
    final int var12 = 20 + var4;
    final int var13 = var5 - 10;
    final int var14 = var6 + 10;
    drawPanel(var9, var13, var12, var14);
    Drawing.saveBoundsTo(_sf);
    final int var15 = var10 + (var9 - 20);
    final int var16 = var12 + var9;
    Drawing.setBounds(var9, var13, Math.min(var15, var16), var14 + var13);
    this.e093(var5, 7903);
    Drawing.setBounds(var9, var13, var9 + var12, var14 + var13);
    menus[var1].e093(var5, 7903);
    Drawing.restoreBoundsFrom(_sf);
  }

  private void c423() {
    Drawing.saveBoundsTo(_kcm);
    Drawing.a797();

    if (JagexApplet.isAnonymous) {
      this.d150();
    } else {
      if (_riI == null) {
        _riI = a250jba();
      }

      final int var2 = MathUtil.ease(_nba, 32, 0, 560);
      final short var3 = 200;
      final int var4 = this.b474() - (-this.c137() + var2 >> 1);
      final int var5 = this.h137() - var3 - 24;
      if (var2 > 20) {
        drawPanel(var4, var5, var2, var3);
      }

      Drawing.setBounds(var4 + 8, 8 + var5, var2 + (var4 - 8), var3 + var5 - 8);
      int var6 = 2805974;
      int var7 = 16 + var5 + SMALL_FONT.ascent;
      SMALL_FONT.drawCentered(StringConstants.RATING_RATING, _vnr[1] + var4, var7, var6);
      SMALL_FONT.drawCentered(StringConstants.RATING_PLAYED, _vnr[2] + var4, var7, var6);
      SMALL_FONT.drawCentered(StringConstants.RATING_WON, var4 + _vnr[3], var7, var6);
      SMALL_FONT.drawCentered(StringConstants.RATING_LOST, _vnr[4] + var4, var7, var6);
      SMALL_FONT.drawCentered(StringConstants.RATING_DRAWN, _vnr[5] + var4, var7, var6);
      var7 += 24;
      String var8;
      if (_riI._q) {
        if (_riI._n == null) {
          var8 = StringConstants.SERVICE_UNAVAILABLE;
        } else {
          var8 = StringConstants.RATING_NO_RATINGS;
          final String[] var9 = _riI._n[_ffy];
          final int[] var10 = _riI._m[_ffy];
          boolean var11 = false;

          for (int var12 = 0; var12 < 10; ++var12) {
            if (var9[var12] != null) {
              var6 = 2805974;
              final String var13 = var9[var12];
              if (JagexApplet.a623jp(var13)) {
                var11 = true;
                var6 = Drawing.WHITE;
              }

              this.a837(var13, var10[1 + var12 * 4], (byte) -128, var10[4 * var12], var4, var10[2 + 4 * var12], var7, var12, var6, var10[3 + 4 * var12]);
              var8 = "";
            }

            var7 += 15;
          }

          if (!var11 && _riI._h + _riI._i + _riI._r > 0) {
            this.a837(JagexApplet.playerDisplayName, _riI._i, (byte) -120, _riI._l, var4, _riI._r, var7, -1, var6, _riI._h);
          }
        }
      } else {
        var8 = StringConstants.PLEASE_WAIT;
      }

      final int i = (SMALL_FONT.ascent + var3 >> 1) + var5;
      SMALL_FONT.drawCentered(var8, var4 + (var2 >> 1), i, 2805974);
      Drawing.restoreBoundsFrom(_kcm);
    }
  }

  private void d150() {
    final short var2 = 420;

    final int var3 = MathUtil.ease(_nba, 32, 0, var2);
    final byte var4 = 80;
    final int var5 = this.b474() - (var3 - this.c137() >> 1);
    final int var6 = this.h137() - 60 - var4 - 48;
    if (var3 > 20) {
      drawPanel(var5, var6, var3, var4);
    }

    Drawing.setBounds(8 + var5, 8 + var6, var5 + var3 - 8, var6 - 8 + var4);
    SMALL_FONT.drawCentered(StringConstants.CREATE_TO_USE, 320, (SMALL_FONT.ascent + var4 >> 1) + var6, Drawing.WHITE);
    Drawing.restoreBoundsFrom(_kcm);
  }

  public void a126(final boolean var3) {
    int var1 = 0;
    if (this.inputState.selectedItem != -1) {
      var1 = this.inputState.selectedItem;
    }

    if (this.id == Id.ACHIEVEMENTS && areAchievementsOffline && ShatteredPlansClient.achievementRequest == null) {
      ShatteredPlansClient.achievementRequest = JagexApplet.createAchievementRequest();
    }

    this.inputState.setSelectedItem(this.getHoveredItemIndex(JagexApplet.mouseX, JagexApplet.mouseY), var1, var3);
    this._h = 0;
    _anc = -1;

    _sbe = -1;
    this._g = -1;
    _tvcq = ShatteredPlansClient.currentTick;

    for (int i = 1; i < 6; ++i) {
      _ssH[i] = 0;
    }

    _os = 0;
    if (this.id == Id.ACHIEVEMENTS) {
      _upd = 0;
    }

    _cleo = -1;
    _nct = 24;
    _ssH[0] = 24;
    if (_cbi == null && (this.id == Id.INSTRUCTIONS_1 || this.id == Id.INSTRUCTIONS_2)) {
      c487hd();
    }
  }

  private void a663(final int var1, final boolean var3) {
    @MagicConstant(valuesFromClass = Item.class)
    final int var4 = items[this.id][var1];
    final String var5 = this.lookupLabel(var4);
    final int var6 = var3 ? 2805974 : 2458760;
    final int var7 = widths[this.id];
    int var8 = this.getX();
    final int var9 = this.c417(var1);
    if (var4 == Item.SOUND_VOLUME || var4 == Item.MUSIC_VOLUME) {
      FONT.draw(var5, var8, var9 + FONT.ascent, var6);
      var8 += _hgt;
      Drawing.strokeRectangle(var8, var9 + var7 / 2 - 1, 121, 2, var6);
      final int var10 = var4 != Item.SOUND_VOLUME ? 120 * Sounds.musicVolume / 256 : Sounds.soundVolume * 120 / 256;
      Drawing.strokeRectangle(var10 + (var8 - 1), 3 + var9, 3, var7 - 6, var6);
    } else {
      FONT.draw(var5, var8, FONT.ascent + var9, var6);
    }

    if (var1 == this._g) {
      this.a527(var1);
    }

  }

  private void a487() {
    if (++_leb > 32) {
      _leb = 32;
    }

    int var2;
    for (var2 = 0; ACHIEVEMENTS.length > var2; ++var2) {
      if (var2 != _sbe && _anc != var2) {
        _gvsb[var2] = null;
      }
    }

    if (_ekA != 0 && ++_ekA >= 32) {
      _upd = _qBb;
      _ekA = 0;
    }

    var2 = _anc != -1 ? _anc : _sbe;
    int var3 = var2 != -1 ? 32 + 16 * _ndd[var2] : 0;
    if (var2 == 24) {
      final boolean var4 = (0x1000000 & unlockedAchievementsBitmap) != 0;
      var3 = var4 ? 32 + 16 * _ndd[24] : 0;
    }

    if (_qBb != var3) {
      if (_ekA != 0) {
        _upd = MathUtil.ease(_ekA, 32, _upd, _qBb);
      }

      _ekA = 1;
      _qBb = var3;
    }

  }

  private int a417(final int index) {
    @MagicConstant(valuesFromClass = Item.class)
    final int item = items[this.id][index];
    return item == Item.SOUND_VOLUME || item == Item.MUSIC_VOLUME
        ? _hgt + 120
        : FONT.measureLineWidth(this.lookupLabel(item));
  }

  private void a258(int y, final int var3, int x) {
    final int var6 = _tvcr[this.id] - 60;
    final int var7 = _tvct[this.id] - 50;
    final int var8 = 1 + SMALL_FONT.ascent * 3 / 4;
    String var5;
    if (var3 == 0) {
      var5 = StringConstants.TEXT_INSTRUCTIONS_0;
    } else if (var3 == 1) {
      var5 = StringConstants.TEXT_INSTRUCTIONS_GLOSSARY_1;
    } else if (var3 == 2) {
      var5 = StringConstants.TEXT_INSTRUCTIONS_GLOSSARY_2;
    } else {
      if (var3 == 3) {
        if (JagexApplet.langId == 1) {
          x -= 5;
        }

        INSTR_MAIN_VIEW.c093(x + 5, y + 5);
        final int labelsX = x + 345;
        final int labelsWidth = 120;
        drawLabeledHorizontalBracket(StringConstants.TEXT_INSTRUCTIONS_MV_TIME, labelsX, 8 + y, labelsWidth, x + 315 + 5, x + 5 + 315, y + 8, 0, RED);
        drawLabeledVerticalBracket(StringConstants.TEXT_INSTRUCTIONS_MV_READY, labelsX, 25 + y, labelsWidth, 315 + x + 5, 25 + y, y + 25, 5, RED);
        drawLabeledVerticalBracket(StringConstants.TEXT_INSTRUCTIONS_MV_WORMHOLE, labelsX, 69 + y, labelsWidth, x + 205, 78 + 5 + y, 78 + y + 5, 0, RED);
        drawLabeledVerticalBracket(StringConstants.TEXT_INSTRUCTIONS_MV_ORDER, labelsX, 5 + y + 110, labelsWidth, 5 + x + 195, 95 + 5 + y, y + 115, 0, RED);
        drawLabeledVerticalBracket(StringConstants.TEXT_INSTRUCTIONS_MV_FRAME, labelsX, y + 5 + 160, labelsWidth, 5 + x + 86, y + 5 + 120, 5 + y + 200, 5, RED);
        return;
      }

      final int var13;
      if (var3 == 4) {
        if (JagexApplet.langId == 1) {
          x -= 5;
        }

        INSTR_STAR_FRAME.c093(x + 5, y + 5);
        final int labelsX = x + 295;
        final short labelsWidth = 220;
        drawLabeledVerticalBracket(StringConstants.TEXT_INSTRUCTIONS_SF_OUTGOING, labelsX, y + 5 + 14, labelsWidth, 250 + x, y + 5, 32 + y + 5, 5, RED);
        drawLabeledHorizontalBracket(StringConstants.TEXT_INSTRUCTIONS_SF_GARRISON, labelsX, 40 + y + 5, labelsWidth, 5 + x + 139, 139 + 5 + x, y + 85, -5, RED);
        drawLabeledVerticalBracket(StringConstants.TEXT_INSTRUCTIONS_SF_NAME, labelsX, 70 + y + 5, labelsWidth, 188 + 5 + x, 5 + y + 130, 100 + 5 + y, 5, RED);
        drawLabeledVerticalBracket(StringConstants.TEXT_INSTRUCTIONS_SF_RESOURCES, labelsX, 95 + y, labelsWidth, x + 235, y + 130, 5 + y + 260, 10, RED);
        drawLabeledVerticalBracket(StringConstants.TEXT_INSTRUCTIONS_SF_INCOMING, labelsX, 5 + y + 110, labelsWidth, 210 + 5 + x, 264 + y + 5, 5 + y + 294, 25, RED);
        y += 150;
        short var34 = 400;
        if (JagexApplet.langId == 1) {
          var34 = 390;
        }

        GameView.a487ai();
        Drawing.drawCircleGradientAdd(16 * var34 + GameView.systemScoreOrbXOffsets[0], GameView.systemScoreOrbYOffsets[0] + y * 16, 50, 7, GameUI.SYSTEM_SCORE_ORB_GRADIENT);
        SMALL_FONT.draw(StringConstants.TEXT_INSTRUCTIONS_ICON_TERRAFORMED, var34 + 22, y + SMALL_FONT.ascent / 4, Drawing.WHITE);
        y += 40;
        Drawing.drawCircleGradientAdd(16 * var34 + GameView.systemScoreOrbXOffsets[0], GameView.systemScoreOrbYOffsets[0] + 16 * y, 50, 7, GameUI.SYSTEM_SCORE_ORB_GRADIENT);
        Drawing.drawCircleGradientAdd(GameView.systemScoreOrbXOffsets[1] + 16 * var34, 16 * y + GameView.systemScoreOrbYOffsets[1], 50, 7, GameUI.SYSTEM_SCORE_ORB_GRADIENT);
        SMALL_FONT.draw(StringConstants.TEXT_INSTRUCTIONS_ICON_NEUTRAL, var34 + 22, SMALL_FONT.ascent / 4 + y, Drawing.WHITE);
        y += 40;
        Drawing.drawCircleGradientAdd(16 * var34 + GameView.systemScoreOrbXOffsets[0], y * 16 + GameView.systemScoreOrbYOffsets[0], 50, 7, GameUI.SYSTEM_SCORE_ORB_GRADIENT);
        Drawing.drawCircleGradientAdd(GameView.systemScoreOrbXOffsets[2] + var34 * 16, GameView.systemScoreOrbYOffsets[2] + 16 * y, 50, 7, GameUI.SYSTEM_SCORE_ORB_GRADIENT);
        Drawing.drawCircleGradientAdd(GameView.systemScoreOrbXOffsets[3] + var34 * 16, GameView.systemScoreOrbYOffsets[3] + y * 16, 50, 7, GameUI.SYSTEM_SCORE_ORB_GRADIENT);
        SMALL_FONT.draw(StringConstants.TEXT_INSTRUCTIONS_ICON_HOMEWORLD, var34 + 22, y + SMALL_FONT.ascent / 4, Drawing.WHITE);
        y += 40;
        GameView.DEFNET_ANIM_SMALL[0].b115(var34 - 15, y - 15, 30, 30);
        var13 = ShatteredPlansClient.currentTick % 104;
        if (var13 > 4 && var13 <= 48) {
          GameView.DEFNET_ANIM_SMALL[var13 / 4].c050(var34 - 15, y - 15, 30, 30, 128);
        } else {
          GameView.DEFNET_ANIM_SMALL[12].c050(var34 - 15, y - 15, 30, 30, 128);
        }

        SMALL_FONT.draw(StringConstants.PROJECT_NAMES[0], var34 + 22, y + SMALL_FONT.ascent / 4, Drawing.WHITE);
        return;
      }

      int var10;
      if (var3 == 5) {
        final Sprite var32 = new Sprite(80, 80);
        Drawing.saveContext();
        var32.installForDrawing();
        GameView.HAMMER.a115(ShatteredPlansClient.currentTick % 25 * 200);
        Drawing.restoreContext();
        var32.d093(x + 15, 8 + y);
        var5 = StringConstants.TEXT_INSTRUCTIONS_PLACEMENT;
        var10 = SMALL_FONT.drawParagraph(ShatteredPlansClient.templateDictionary.expand(var5), x + 80, y, var6 - 80, var7, Drawing.WHITE, Font.HorizontalAlignment.JUSTIFY, Font.VerticalAlignment.TOP, var8);
        y += (3 + var10 * 4) * var8 >> 2;
        final int var11 = ShatteredPlansClient.currentTick % 200;
        GameView.a070eo(x, 68 + y, 55 + x, y + 40, var11, false);
        this.a346(y + 40, var11, x + 55, false, x, y + 68, 0, false);
        var5 = StringConstants.TEXT_INSTRUCTIONS_MOVEMENT;
        var10 = SMALL_FONT.drawParagraph(ShatteredPlansClient.templateDictionary.expand(var5), x + 80, y, var6 - 80, var7, Drawing.WHITE, Font.HorizontalAlignment.JUSTIFY, Font.VerticalAlignment.TOP, var8);
        y += (4 * var10 + 3) * var8 >> 2;
        _kjf[ShatteredPlansClient.currentTick % _kjf.length].draw(5 + x, 42 + y);
        var5 = StringConstants.TEXT_INSTRUCTIONS_PROJECTS;
        SMALL_FONT.drawParagraph(ShatteredPlansClient.templateDictionary.expand(var5), 80 + x, y, var6 - 80, var7, Drawing.WHITE, Font.HorizontalAlignment.JUSTIFY, Font.VerticalAlignment.TOP, var8);
        return;
      }

      final int var12;
      int var19;
      int var20;
      int var21;
      int var22;
      int var23;
      int var24;
      int var25;
      if (var3 == 6) {
        final ArgbSprite var30 = (ArgbSprite) GameView.ARROW_SHIP;
        final ArgbSprite var33 = var30.copy();
        var33.f797();
        final int var11 = ShatteredPlansClient.currentTick % 200;
        var12 = x + 35;
        var13 = y + 40;
        int var37;
        int var38;
        if (var11 < 170) {
          Drawing.strokeCircle(var12, var13, 30, Drawing.WHITE);
          Drawing.fillCircle(var12, var13, 30, Drawing.WHITE, 92);
          GameView.a194ie(var33, var33.width << 3, var33.height << 3, var12 * 16, var13 * 16, 0, 4096);
          Drawing.fillCircle(var12, var13, 9, 0, 92);
          GameView.FLEET_BUTTONS[0].draw(-(GameView.FLEET_BUTTONS[0].width / 2) + 30 + var12, var13 - GameView.FLEET_BUTTONS[0].height / 2);
          GameView.FLEET_BUTTONS[1].draw(var12 - 30 - GameView.FLEET_BUTTONS[0].width / 2, var13 - GameView.FLEET_BUTTONS[0].height / 2);
          GameView.FLEET_BUTTONS[2].draw(var12 - GameView.FLEET_BUTTONS[0].width / 2, 30 + (var13 - GameView.FLEET_BUTTONS[0].height / 2));
          final byte var36 = 4;
          if (var11 < 120) {
            var38 = (var11 - 80) / 20;
            if (var38 < 0) {
              var38 = -var38;
            }

            if (var11 >= 80) {
              ++var38;
            }

            var37 = var36 - var38;
            if (var37 == 0) {
              var37 = 1;
            }
          } else {
            var37 = 1;
          }

          SMALL_FONT.drawCentered(Integer.toString(var37), var12, 4 + var13, Drawing.WHITE);
          if (var11 / 20 > 0 && var11 / 20 < 4 && var11 % 20 < 3) {
            Drawing.strokeCircle(var12 + 30, var13, GameView.FLEET_BUTTONS[0].width / 2, Drawing.WHITE);
          }

          if (var11 / 20 > 3 && var11 / 20 < 7 && var11 % 20 < 3) {
            Drawing.strokeCircle(var12 - 30, var13, GameView.FLEET_BUTTONS[1].width / 2, Drawing.WHITE);
          }

          if (var11 > 160 && var11 < 165) {
            Drawing.strokeCircle(var12, 30 + var13, GameView.FLEET_BUTTONS[2].width / 2, Drawing.WHITE);
          }
        }

        var5 = StringConstants.TEXT_INSTRUCTIONS_FLEETSIZE;
        var37 = SMALL_FONT.drawParagraph(ShatteredPlansClient.templateDictionary.expand(var5), 80 + x, y, var6 - 80, var7, Drawing.WHITE, Font.HorizontalAlignment.JUSTIFY, Font.VerticalAlignment.TOP, var8);
        y += var8 * (3 + 4 * var37) >> 2;
        var38 = ShatteredPlansClient.currentTick % 50;
        if (var38 < 25) {
          var38 = 0;
        } else {
          var38 -= 25;
          var38 = var38 * (614400 - 16384 * var38) * var38 / 15625;
        }

        int var39;
        int var40;
        int var41;
        for (var39 = 0; var39 < 6; ++var39) {
          var40 = ((var39 * 8192) - var38) / 6;
          var41 = (20 * mq_.a353je(var40) >> 12) + (x + 35 << 4);
          var19 = (20 * mq_.b080mq(var40) >> 12) + (40 + y << 4);
          Drawing.drawCircleGradientAdd(var41, var19, 64, 255, Drawing.SHADES_OF_GRAY);
        }

        for (var39 = 0; var39 < 10; ++var39) {
          Drawing.horizontalLine(-(var39 >> 1) + x + 36, var39 + y + 30, -2 & var39, Drawing.WHITE);
        }

        var5 = StringConstants.TEXT_INSTRUCTIONS_END_TURN;
        var37 = SMALL_FONT.drawParagraph(ShatteredPlansClient.templateDictionary.expand(var5), x + 80, y, var6 - 80, var7, Drawing.WHITE, Font.HorizontalAlignment.JUSTIFY, Font.VerticalAlignment.TOP, var8);
        y += var8 * (3 + var37 * 4) >> 2;
        var39 = ShatteredPlansClient.currentTick / 100 % 3;
        var40 = ShatteredPlansClient.currentTick % 100;
        var41 = 0;
        if (var40 > 20 && var40 < 40) {
          var41 = var40 <= 30 ? 25 * var40 - 500 : 1000 - var40 * 25;
        }

        var40 = var40 < 21 ? 12 * var40 : (var40 > 80 ? 12 * (100 - var40) : 256);
        if (var39 == 0) {
          var33.drawAdd(15 + x, y + 15, var40);
          var33.drawAdd(15 + x, y + 15, var41);
        } else {
          final Sprite var42;
          if (var39 == 1) {
            var42 = new Sprite(var33.width + 20, var33.height);
            Drawing.saveContext();
            var42.installForDrawing();
            var20 = 0;

            for (var21 = 0; var21 < 5; ++var21) {
              var33.draw(var20, 0);
              var20 += 5;
            }

            Drawing.restoreContext();
            var42.drawAdd(x + 7, y + 15, var40);
            var42.drawAdd(x + 7, 15 + y, var41);
          } else if (var39 == 2) {
            var42 = new Sprite(var33.width + 30, var33.height);
            Drawing.saveContext();
            var42.installForDrawing();
            var20 = 0;

            for (var21 = 0; var21 < 10; var20 += 5) {
              GameView.a194ie(var33, 8 * var33.width, var33.height * 8, (var20 + var33.width / 4) * 16, 4 * var33.height, 0, 2048);
              GameView.a194ie(var33, 8 * var33.width, var33.height * 8, 16 * (var20 + var33.width / 4), var33.height * 12, 0, 2048);
              ++var21;
            }

            Drawing.restoreContext();
            var42.drawAdd(x + 3, 15 + y, var40);
            var42.drawAdd(x + 3, y + 15, var41);
          }
        }

        var5 = StringConstants.TEXT_INSTRUCTIONS_HOTKEYS;
        var37 = SMALL_FONT.drawParagraph(ShatteredPlansClient.templateDictionary.expand(var5), 80 + x, y, var6 - 80, var7, Drawing.WHITE, Font.HorizontalAlignment.JUSTIFY, Font.VerticalAlignment.TOP, var8);
        y += var8 * (3 + var37 * 4) >> 2;
        if (_oob != null) {
          var19 = ShatteredPlansClient.currentTick % 50;

          for (var20 = 0; var20 < 5; ++var20) {
            _oob[var20] = MathUtil.ease(var19, 50, _erq[var20], _fgc[var20]);
          }

          var20 = x + 10;
          var21 = _oob[0] + 10 + y;

          for (var22 = 1; var22 < 5; ++var22) {
            for (var23 = 0; var23 < 10; ++var23) {
              var24 = var22 * 10 + x + var23;
              var25 = 10 + y + MathUtil.ease(var23, 10, _oob[var22 - 1], _oob[var22]);
              Drawing.line(var20, var21, var24, var25, 12632256);
              var21 = var25;
              var20 = var24;
            }
          }
        }

        Drawing.verticalLine(9 + x, y + 10, 35, Drawing.WHITE);
        Drawing.horizontalLine(x + 10, y + 44, 40, Drawing.WHITE);
        var5 = StringConstants.TEXT_INSTRUCTIONS_STATS;
        SMALL_FONT.drawParagraph(ShatteredPlansClient.templateDictionary.expand(var5), x + 80, y, var6 - 80, var7, Drawing.WHITE, Font.HorizontalAlignment.JUSTIFY, Font.VerticalAlignment.TOP, var8);
        return;
      }

      if (var3 == 7) {
        final int var9 = ShatteredPlansClient.currentTick % 200;
        var10 = SMALL_FONT.drawParagraph(ShatteredPlansClient.templateDictionary.expand(StringConstants.TEXT_INSTRUCTIONS_ANIMATION), x, y, var6, var7, Drawing.WHITE, Font.HorizontalAlignment.JUSTIFY, Font.VerticalAlignment.TOP, var8);
        y += (2 * var10 + 3) * var8 >> 1;
        var12 = x + 125;
        var13 = x + 325;
        final short var14 = 150;
        final byte var15 = 125;
        final String[] var16 = new String[16];
        final String var17 = ShatteredPlansClient.templateDictionary.expand(StringConstants.TEXT_INSTRUCTIONS_ANIMATION_FLEETMOVE);
        final String var18 = ShatteredPlansClient.templateDictionary.expand(StringConstants.TEXT_INSTRUCTIONS_ANIMATION_PROJECT);
        SMALL_FONT.breakLines(var17, new int[]{var14}, var16);
        SMALL_FONT.breakLines(var18, new int[]{var15}, var16);
        final byte var31 = 3;
        var21 = var8 + var8 * var31;
        SMALL_FONT.drawParagraph(var17, var12, y, var14, var21, Drawing.WHITE, Font.HorizontalAlignment.LEFT, Font.VerticalAlignment.MIDDLE, var8);
        SMALL_FONT.drawParagraph(var18, var13, y, var15, var21, Drawing.WHITE, Font.HorizontalAlignment.LEFT, Font.VerticalAlignment.MIDDLE, var8);
        GameView.a070eo(x, y + 25, x + 100, 25 + y, var9, false);
        this.a346(y + 25, var9, x + 100, false, x, 25 + y, 8, true);
        var22 = (50 + ShatteredPlansClient.currentTick) % 800 / 200;
        this.a132(x + 295, 16, var22, (var21 >> 1) + y, (ShatteredPlansClient.currentTick + 50) % 200);
        int var11 = y + SMALL_FONT.ascent + var21;
        final String s = ShatteredPlansClient.templateDictionary.expand(StringConstants.TEXT_INSTRUCTIONS_ANIMATION_DAMAGED);
        final String a456 = ShatteredPlansClient.templateDictionary.expand(StringConstants.TEXT_INSTRUCTIONS_ANIMATION_CAPTURED);
        var19 = SMALL_FONT.breakLines(s, new int[]{var14}, var16);
        var20 = SMALL_FONT.breakLines(a456, new int[]{var15}, var16);
        var10 = Math.max(var20, var19);
        var21 = var8 * var10 + var8;
        SMALL_FONT.drawParagraph(s, var12, var11, var14, var21, Drawing.WHITE, Font.HorizontalAlignment.LEFT, Font.VerticalAlignment.MIDDLE, var8);
        SMALL_FONT.drawParagraph(a456, var13, var11, var15, var21, Drawing.WHITE, Font.HorizontalAlignment.LEFT, Font.VerticalAlignment.MIDDLE, var8);
        GameView.a070eo(x + 110, 90 + y, x + 10, 90 + y, var9, false);
        this.a346(90 + y, var9, x + 10, true, 110 + x, 90 + y, 4, true);
        this.a132(x + 295, 16, -1, var11 + (var21 >> 1), (100 + ShatteredPlansClient.currentTick) % 200);
        var11 += var8 + var21;
        final String s1 = ShatteredPlansClient.templateDictionary.expand(StringConstants.TEXT_INSTRUCTIONS_ANIMATION_COMBAT);
        var10 = SMALL_FONT.breakLines(s1, new int[]{200}, var16);
        var21 = var10 * var8 + var8;
        if (var21 < 40) {
          var21 = 40;
        }

        SMALL_FONT.drawParagraph(s1, var12, var11, 200, var21, Drawing.WHITE, Font.HorizontalAlignment.LEFT, Font.VerticalAlignment.MIDDLE, var8);
        var23 = -(var9 / 24) + 15;
        var24 = -(var9 / 17) + 10;
        if (var24 < 0) {
          var24 = 0;
        }

        var25 = 4 - var9 / 30;
        if (var25 < 0) {
          var25 = 0;
        }

        final byte var26 = 80;
        int var28 = x + 18;
        var11 += 10;
        if (JagexApplet.langId == 0) {
          var11 += 20;
        }

        if (var9 >= 0 && var9 < 180) {
          final int var29 = 31;
          this.a827(var28, var26, var25, var29, 8421504, var11);
          var28 += 30;
          final int i = 90;
          this.a827(var28, var26, var23, i, GameUI.PLAYER_COLORS_1[0], var11);
          var28 += 30;
          final int i1 = 63;
          this.a827(var28, var26, var24, i1, GameUI.PLAYER_COLORS_1[2], var11);
        }

        return;
      }

      if (var3 == 8) {
        var5 = StringConstants.TEXT_INSTRUCTIONS_PROJECT_METAL;
        final int var9 = SMALL_FONT.drawParagraph(ShatteredPlansClient.templateDictionary.expand(var5), x + 40, y, var6 - 40, var7, Drawing.WHITE, Font.HorizontalAlignment.JUSTIFY, Font.VerticalAlignment.TOP, var8);
        this.a132(x + 15, 20, 0, y + (SMALL_FONT.ascent * (var9 * 2 + 1) >> 2), ShatteredPlansClient.currentTick % 200);
        y += (3 + var9 * 4) * var8 >> 2;
        var5 = StringConstants.TEXT_INSTRUCTIONS_PROJECT_BIOMASS;
        final int var9a = SMALL_FONT.drawParagraph(ShatteredPlansClient.templateDictionary.expand(var5), x + 40, y, var6 - 40, var7, Drawing.WHITE, Font.HorizontalAlignment.JUSTIFY, Font.VerticalAlignment.TOP, var8);
        this.a132(15 + x, 20, 1, y + (SMALL_FONT.ascent * (1 + var9a * 2) >> 2), (ShatteredPlansClient.currentTick + 185) % 200);
        y += var8 * (3 + 4 * var9a) >> 2;
        var5 = StringConstants.TEXT_INSTRUCTIONS_PROJECT_ENERGY;
        final int var9b = SMALL_FONT.drawParagraph(ShatteredPlansClient.templateDictionary.expand(var5), x + 40, y, var6 - 40, var7, Drawing.WHITE, Font.HorizontalAlignment.JUSTIFY, Font.VerticalAlignment.TOP, var8);
        this.a132(15 + x, 20, 2, (SMALL_FONT.ascent * (2 * var9b + 1) >> 2) + y, (170 + ShatteredPlansClient.currentTick) % 200);
        var5 = StringConstants.TEXT_INSTRUCTIONS_PROJECT_EXOTICS;
        y += var8 * (4 * var9b + 3) >> 2;
        final int var9c = SMALL_FONT.drawParagraph(ShatteredPlansClient.templateDictionary.expand(var5), 40 + x, y, var6 - 40, var7, Drawing.WHITE, Font.HorizontalAlignment.JUSTIFY, Font.VerticalAlignment.TOP, var8);
        this.a132(x + 15, 20, 3, ((var9c * 2 + 1) * SMALL_FONT.ascent >> 2) + y, (155 + ShatteredPlansClient.currentTick) % 200);
        return;
      }

      if (var3 == 9) {
        var5 = StringConstants.TEXT_INSTRUCTIONS_GAME_TYPE;
      } else {
        if (var3 != 10) {
          throw new RuntimeException();
        }

        var5 = StringConstants.TEXT_INSTRUCTIONS_CLASSIC + StringConstants.TEXT_INSTRUCTIONS_CLASSIC_STELLAR_BOMB;
      }
    }

    this.a316(var8, y, x, var6, SMALL_FONT, ShatteredPlansClient.templateDictionary.expand(var5));
  }

  private int a527(final int index) {
    return this.getX() + this.a417(index);
  }

  private int getWidth() {
    return widths[this.id];
  }

  private void a132(final int var1, int var2, final int var3, final int var4, final int var6) {
    if (var6 < 150) {
      int var7 = var6;
      if (var6 > 125) {
        var7 = -var6 + 150;
      }

      int var8 = 0;
      if (var7 < 25) {
        if (var6 <= 125) {
          var8 = MathUtil.ease(var6, 25, var2, 0);
        } else {
          var8 = MathUtil.ease(var6 - 125, 25, 0, -var2);
        }

        var2 = MathUtil.ease(var7, 25, var2 / 2, var2);
      }

      if (var3 == -1) {
        GameView.HAMMER.a050(var8 + var4, 8192 * var2 / GameView.HAMMER.width, var1, 200 * (ShatteredPlansClient.currentTick % 25));
      } else {
        GameView.SYSTEM_ICONS[var3].b115(-var2 + var1, var8 + var4 - var2, 2 * var2, 2 * var2);
      }

    }
  }

  public void prepareToSwitchAwayFrom() {
    if (this.id == Id.RATINGS) {
      _riI = null;
    }
  }

  private int b313(int var2, final int var3) {
    final int var4 = 20 + this.b474() + _tvcr[this.id];
    int var5 = 10 + this.h137();
    final int var6 = _ssH.length;
    if (var2 <= var4) {
      if (var4 - 16 <= var2) {
        var2 = var4 - var2;
        int var7 = -(var6 * 21) + 310;
        int var9 = _nct;

        for (int var10 = 0; var6 > var10; ++var10) {
          int var11 = _ssH[var10];
          if (var11 > 12) {
            var11 = 12;
          }

          int var12;
          if (var10 == var6 - 1) {
            var12 = var7 + 16;
          } else {
            var12 = var9 == 0 ? 0 : var7 * var11 / var9;
            var7 -= var12;
            var9 -= var11;
            var12 += 21;
          }

          if (var6 - 1 == var10) {
            if (var3 >= var2 + var5 && var3 < var5 + 32 - var2 + var12) {
              return var10;
            }
          } else if (var5 + var2 <= var3 && var3 < var5 + var2 + var12) {
            return var10;
          }

          var5 += var12;
        }

      } else if (var2 < var4 - 34 && var2 > var4 - 54) {
        if (var5 - 5 < var3 && var5 + 17 > var3) {
          return -2;
        }

        if (var5 + _tvct[this.id] - 45 < var3 && _tvct[this.id] + (var5 - 23) > var3) {
          return -3;
        }
      }
    }
    return -1;
  }

  public void i150() {
    if (!ShatteredPlansClient._mdQ && !ShatteredPlansClient._isa) {
      while (JagexApplet.nextTypedKey()) {
        if (JagexApplet.lastTypedKeyCode == KeyState.Code.ESCAPE) {
          if (this.id == Id.PAUSE_SINGLEPLAYER || this.id == Id.PAUSE_MULTIPLAYER_1 || this.id == Id.PAUSE_MULTIPLAYER_2 || this.id == Id.PAUSE_MULTIPLAYER_3) {
            switchTo(Id.GAME, 0, false);
          } else if (currentMenu != Id.G9) {
            final int var2 = _brm;
            switchTo(var2, 0, false);
          }
        } else {
          this.k150();
          if (this.inputState.selectedItem != -1) {
            this.a520(false, this.inputState.selectedItem);
          }
        }
      }

      if (this.id == Id.INSTRUCTIONS_1 || this.id == Id.INSTRUCTIONS_2) {
        this.f423();
      }

      if (this.id == Id.ACHIEVEMENTS) {
        this.a150();
      }

      this.inputState.processMouseInput(this.getHoveredItemIndex(JagexApplet.mouseX, JagexApplet.mouseY), this.getHoveredItemIndex(JagexApplet.mousePressX, JagexApplet.mousePressY));
      if (this.inputState.selectedItem != -1) {
        this.a520(true, this.inputState.selectedItem);
      }
    }

    if (this.id == Id.INSTRUCTIONS_1 || this.id == Id.INSTRUCTIONS_2) {
      this.d423();
    }

    if (this.id == Id.ACHIEVEMENTS) {
      this.a487();
    }

    ++this._h;
    if (this.id == Id.ERROR && ++_onc > 32) {
      _onc = 32;
    }

    if (this.id == Id.RATINGS && ++_nba > 32) {
      _nba = 32;
    }
  }

  private void a150() {
    _sbe = this.d313(107, JagexApplet.mouseX, JagexApplet.mouseY);

    if (JagexApplet.mouseButtonJustClicked != MouseState.Button.NONE) {
      final int var2 = this.d313(84, JagexApplet.mousePressX, JagexApplet.mousePressY);
      if (var2 == _anc) {
        _anc = -1;
      } else if (var2 != -1) {
        _anc = var2;
      }
    }

  }

  private int b410() {
    if (currentMenu != this.id && this.id != nextMenu) {
      return 0;
    } else if (currentMenu >= 0 && nextMenu >= 0 && currentMenu != nextMenu) {
      final int var2 = items[currentMenu].length * widths[currentMenu] + _tvct[currentMenu] + 10;
      final int var3 = _tvct[nextMenu] + widths[nextMenu] * items[nextMenu].length + 10;
      return MathUtil.ease(_ehQ, 32, var2, var3);
    } else {

      return widths[this.id] * items[this.id].length + _tvct[this.id] + 10;
    }
  }

  private void d093(final int var2) {
    _os = var2;
    _c = _mgfb != 0 ? MathUtil.ease(_mgfb, 32, _c, _acw) : _acw;
    _acw = 0;

    for (int var3 = 0; var3 < var2; ++var3) {
      _acw += _cbi[var3];
    }

    _mgfb = 1;
  }

  public void g150() {
    if (ClientGameSession.playSession != null || ClientGameSession.spectateSession != null) {
      a587oa();
    }

    final int var2 = this.c137();
    final int var3 = this.b410();
    final int var4 = this.h137();
    final int var5 = (-var2 + ShatteredPlansClient.SCREEN_WIDTH) / 2;
    final int var7 = 20 + var2;
    final int var8 = var4 - 10;
    final int var9 = var3 + 10;
    drawPanel(var5, var8, var7, var9);
    this.e093(var4, 7903);
  }

  private void a520(final boolean var1, final int var2) {
    @MagicConstant(valuesFromClass = Item.class)
    final int var4 = items[this.id][var2];
    if (this.inputState.isItemActiveAllowRepeat()) {
      this.b258(var4, var2, var1);
    }

    if (var4 != Item.SINGLE_PLAYER_SKIRMISH
        && var4 != Item.START_SKIRMISH
        && var4 != Item.ENTER_MULTIPLAYER_LOBBY
        && var4 != Item.RETURN_TO_GAME
        && var4 != Item.MENU
        && var4 != Item.DISCARD
        && var4 != Item.ACHIEVEMENTS
        && var4 != Item.INSTRUCTIONS_1
        && var4 != Item.INSTRUCTIONS_2
        && var4 != Item.CURRENT_MENU
        && var4 != Item.RANKINGS
        && var4 != Item.FULLSCREEN
        && var4 != Item.END_GAME
        && var4 != Item.OFFER_DRAW
        && var4 != Item.RESIGN
        && var4 != Item.OFFER_REMATCH
        && var4 != Item.RETURN_TO_LOBBY
        && var4 != Item.HS_MODE_1
        && var4 != Item.HS_MODE_2
        && var4 != Item.HS_MODE_3
        && var4 != Item.RATING_MODE_1
        && var4 != Item.RATING_MODE_2
        && var4 != Item.LOGIN_CREATE_ACCOUNT
        && var4 != Item.TUTORIAL
        && var4 != Item.QUIT
        && var4 != Item.WATCH_INTRODUCTION
        && var4 != Item.GAME_TYPE
        && var4 != Item.RULE_SET
        && var4 != Item.OPTIONS_MENU
        && var4 != Item.MUSIC_TRACK) {
      final int var6;
      final int var7;
      final int var8;
      if (var4 == Item.SOUND_VOLUME) {
        boolean var5 = false;
        if (this.inputState.isHomeTyped() && Sounds.soundVolume > 0) {
          Sounds.setSoundVolume(0);
          var5 = true;
        }

        if (this.inputState.isEndTyped() && Sounds.soundVolume < 256) {
          var5 = true;
          Sounds.setSoundVolume(256);
        }

        if (this.inputState.isMouseButtonDown()) {
          var6 = this.getX() + this.a527(var2) + _hgt - 120 >> 1;
          var7 = JagexApplet.mouseX - var6;
          var8 = 256 * var7 / 120;
          if (var8 <= 0) {
            Sounds.setSoundVolume(0);
          } else Sounds.setSoundVolume(Math.min(256, var8));

          var5 = true;
        }

        if (this.inputState.isLeftTyped() && Sounds.soundVolume > 0) {
          var5 = true;
          c487();
        }

        if (this.inputState.isRightTyped() && Sounds.soundVolume < 256) {
          var5 = true;
          f423cc();
        }

        if (var5 && (!this.inputState.isMouseButtonDown() || ShatteredPlansClient.currentTick > _tvcq)) {
          Sounds.play(Sounds.SFX_FACTORY_NOISE);
          _tvcq = 25 + ShatteredPlansClient.currentTick;
        }
      } else if (var4 == Item.MUSIC_VOLUME) {
        if (this.inputState.isHomeTyped()) {
          Sounds.setMusicVolume(0);
        }

        if (this.inputState.isEndTyped()) {
          Sounds.setMusicVolume(256);
        }

        if (this.inputState.isMouseButtonDown()) {
          var6 = this.getX() - (-this.a527(var2) - _hgt) - 120 >> 1;
          var7 = JagexApplet.mouseX - var6;
          var8 = 256 * var7 / 120;
          if (var8 > 0) {
            Sounds.setMusicVolume(Math.min(256, var8));
          } else {
            Sounds.setMusicVolume(0);
          }
        }

        if (this.inputState.isLeftTyped()) {
          d487qs();
        }

        if (this.inputState.isRightTyped()) {
          a150mgf();
        }
      }
    }
  }

  private void j150() {
    final byte var2 = 52;
    int var3 = 140;
    Drawing.saveBoundsTo(_kcm);
    int var4 = var2;
    Drawing.a797();

    boolean var5;
    int var6;
    Sprite var8;
    for (var6 = 0; var6 < 25; ++var6) {
      final int var7 = (int) (Math.cos((double) (-(23 * var6)) + Math.PI * (double) (2 * ShatteredPlansClient.currentTick) / 150.0D) * 64.0D) + 64;
      var5 = (1 << var6 & unlockedAchievementsBitmap) != 0;
      var8 = !var5 ? ShatteredPlansClient._msb : a367(var6);
      final Sprite var9 = var5 ? achievementIcon(var6) : ShatteredPlansClient._lqo;
      if (_leb != 32) {
        var8.drawAdd(var4 - 16, var3 - 16, var7 * _leb / 32);
        var9.drawAdd(var4, var3, (_leb << 8) / 32);
      } else if (_anc == var6) {
        var8 = var5 ? a367dmr(var6) : ShatteredPlansClient._jcnk;
        final Sprite bi_ = var5 ? ACHIEVEMENTS[var6] : ShatteredPlansClient._dmgn;
        final Sprite var10 = new Sprite(48, 48);
        Drawing.saveContext();
        var10.installForDrawing();
        bi_.c115(0, 0, 48, 48);
        Drawing.restoreContext();
        var8.c050(var4 - 32, var3 - 32, 96, 96, var7 << 1);
        var10.drawAdd(var4 - 8, var3 - 8, 256);
      } else if (_sbe != var6) {
        if (_anc == -1) {
          var8.drawAdd(var4 - 16, var3 - 16, var7);
          var9.drawAdd(var4, var3, 256);
        } else {
          var8.drawAdd(var4 - 16, var3 - 16, var7 >> 1);
          var9.drawAdd(var4, var3, 128);
        }
      } else if (_anc == -1) {
        var8.drawAdd(var4 - 16, var3 - 16, var7);
        var9.drawAdd(var4, var3, 256);
        var9.drawAdd(var4, var3, 128);
      } else {
        var8.drawAdd(var4 - 16, var3 - 16, var7);
        var9.drawAdd(var4, var3, 256);
      }

      var4 += 42;
      if (var6 % 13 == 12) {
        var3 = (int) ((double) var3 + Math.sqrt(3.0D) * 21.0D);
        var4 = 73;
      }
    }

    if (_glh == null) {
      _glh = new Sprite(256, 256);
    }

    if (_ilgb == null) {
      _ilgb = new Sprite(256, 256);
    }

    var6 = _anc != -1 ? _anc : _sbe;
    var5 = var6 != -1 && (unlockedAchievementsBitmap & 1 << var6) != 0;
    final Sprite var28;
    if (var5) {
      var28 = ACHIEVEMENTS[var6];
      var8 = a367dmr(var6);
    } else {
      var28 = ShatteredPlansClient._dmgn;
      var8 = ShatteredPlansClient._jcnk;
    }

    Drawing.saveContext();
    _glh.installForDrawing();
    Drawing.clear();
    var28.b115(0, 0, 256, 256);
    _ilgb.installForDrawing();
    var8.c093(0, 0);
    Drawing.restoreContext();
    final int var29 = 64 + (int) (64.0D * Math.cos(Math.PI * (double) (ShatteredPlansClient.currentTick * 2) / 150.0D));
    final int var30 = MathUtil.ease(_leb, 32, 0, 600);
    final short var11 = 155;
    final int var12 = (ShatteredPlansClient.SCREEN_WIDTH - var30) / 2;
    final short var13 = 220;
    if (var30 > 20) {
      drawPanel(var12, var13, var30, var11);
    }

    Drawing.setBounds(10 + var12, var13, var12 + var30 - 10, 375);
    final int var15 = (_tvcm[this.id] >> 1) + 24;
    final short var16 = 128;
    Drawing.fillRoundedRect(30, var15 + 6, 136, 136, 10, 0, 128);
    Drawing.f669(30, 6 + var15, 136, 136, 10, Drawing.alphaOver(0, 3974311, 128));
    _ilgb.c050(-30, var15 - 54, 256, 256, var29);
    _glh.c050(34, 10 + var15, var16, 128, 256);
    final short var18 = 350;
    final int var27 = 24 + this.b474() + this.c137() + 16 - var18;
    final short var19 = 136;
    final int var20 = -136 + (this.h137() - 34);
    Drawing.fillRoundedRect(var27, var20, var18, var19, 10, 0, 128);
    Drawing.f669(var27, var20, var18, var19, 10, Drawing.alphaOver(0, 3974311, 128));
    final String var21;
    int var23;
    int var33;
    if (var6 != -1 && var6 != 24) {
      FONT.draw(StringConstants.ACHIEVEMENT_NAMES[var6].toUpperCase(), var27 + 10, var20 + 6 + FONT.ascent, 2805974);
      var21 = "(" + Strings.format(StringConstants.ORB_POINTS, Integer.toString(_eig[var6])) + ")";
      var33 = SMALL_FONT.drawParagraph(var21, 10 + var27, 14 + var20 + SMALL_FONT.ascent, 330, var19, 2805974, Font.HorizontalAlignment.LEFT, Font.VerticalAlignment.TOP, SMALL_FONT.ascent);
      var33 += 1 + SMALL_FONT.drawParagraph(StringConstants.ACHIEVEMENT_CRITERIA[var6], 10 + var27, SMALL_FONT.ascent / 2 + var20 + 14 + (1 + var33) * SMALL_FONT.ascent, 330, var19, 2805974, Font.HorizontalAlignment.LEFT, Font.VerticalAlignment.TOP, SMALL_FONT.ascent);
      SMALL_FONT.drawParagraph(StringConstants.ACHIEVEMENTS_RATED_ONLY, 10 + var27, var20 - (-14 - (var33 + 1) * SMALL_FONT.ascent), 330, var19, 2805974, Font.HorizontalAlignment.LEFT, Font.VerticalAlignment.TOP, SMALL_FONT.ascent);
    } else if (var6 == 24) {
      FONT.draw(StringConstants.ACHIEVEMENT_NAMES[24].toUpperCase(), var27 + 10, 6 + var20 + FONT.ascent, 2805974);
      final String var22;
      if (var5) {
        var21 = "(" + Strings.format(StringConstants.ORB_POINTS, Integer.toString(_eig[24])) + ")";
        var22 = StringConstants.ACHIEVEMENT_CRITERIA[24];
      } else {
        var21 = "(" + StringConstants.ORB_POINTS_COLON + "???)";
        var22 = StringConstants.SECRET_ACHIEVEMENT;
      }

      var23 = SMALL_FONT.drawParagraph(var21, var27 + 10, 14 + var20 + SMALL_FONT.ascent, 330, var19, 2805974, Font.HorizontalAlignment.LEFT, Font.VerticalAlignment.TOP, SMALL_FONT.ascent);
      var23 += 1 + SMALL_FONT.drawParagraph(var22, 10 + var27, (var23 + 1) * SMALL_FONT.ascent + (var20 - (-14 - SMALL_FONT.ascent / 2)), 330, var19, 2805974, Font.HorizontalAlignment.LEFT, Font.VerticalAlignment.TOP, SMALL_FONT.ascent);
      SMALL_FONT.drawParagraph(StringConstants.ACHIEVEMENTS_RATED_ONLY, var27 + 10, (var23 + 1) * SMALL_FONT.ascent + 14 + var20, 330, var19, 2805974, Font.HorizontalAlignment.LEFT, Font.VerticalAlignment.TOP, SMALL_FONT.ascent);
    } else if (areAchievementsOffline) {
      FONT.draw(StringConstants.WARNING.toUpperCase(), 10 + var27, var20 + 6 + FONT.ascent, 2805974);
      SMALL_FONT.drawParagraph(StringConstants.ACHIEVEMENTS_OFFLINE, 10 + var27, SMALL_FONT.ascent + var20 + 14, 330, var19, 2805974, Font.HorizontalAlignment.LEFT, Font.VerticalAlignment.TOP, SMALL_FONT.ascent);
    } else {
      FONT.draw(StringConstants.TEXT_NO_ACHIEVEMENT.toUpperCase(), var27 + 10, FONT.ascent + 6 + var20, 2805974);
      SMALL_FONT.drawParagraph(StringConstants.MOUSE_OVER_AN_ICON, 10 + var27, var20 + 14 + SMALL_FONT.ascent, 330, var19, 2805974, Font.HorizontalAlignment.LEFT, Font.VerticalAlignment.TOP, SMALL_FONT.ascent);
    }

    int var32;
    if (_ekA == 0) {
      var32 = _qBb;
    } else {
      var32 = MathUtil.ease(_ekA, 32, _upd, _qBb);
    }

    var32 = MathUtil.ease(_leb, 32, 0, var32);
    if (var32 > 20) {
      final byte var31 = 52;
      final int i = 187;
      final int i1 = -(var32 >> 1) + var15 + 74;
      Drawing.fillRoundedRect(i, i1, var31, var32, 10, 0, 128);
      Drawing.f669(i, i1, var31, var32, 10, Drawing.alphaOver(0, 3974311, 128));
      Drawing.setBounds(191, 4 + i1, 235, var32 - 4 + i1);
      if (var6 != -1 && (var6 != 24 || var5)) {
        var33 = _ndd[var6];
        if (var33 == 1) {
          ORB_COIN.c115(197, i1 + ((var32 >> 1) - 16), 32, 32);
        } else {
          var23 = i1 + 24;
          final int var24 = Math.max(var32, 48);

          final int var25 = i1 + var24 - 24;

          for (int var26 = 0; var33 > var26; ++var26) {
            ORB_COIN.c115(197, var23 + (var26 * (var25 - var23) / (var33 - 1) - 16), 32, 32);
          }
        }
      }
    }

    Drawing.restoreBoundsFrom(_kcm);
  }

  private int getX() {
    return this.b474() + 65;
  }

  private void k150() {
    if (JagexApplet.lastTypedKeyCode == KeyState.Code.F1) {
      ShatteredPlansClient.drawDebugStats = !ShatteredPlansClient.drawDebugStats;
    }
    if (JagexApplet.lastTypedKeyCode == KeyState.Code.F2) {
      ShatteredPlansClient.renderQuality = RenderQuality.high();
    }
    if (JagexApplet.lastTypedKeyCode == KeyState.Code.F3) {
      ShatteredPlansClient.renderQuality = RenderQuality.medium();
    }
    if (JagexApplet.lastTypedKeyCode == KeyState.Code.F4) {
      ShatteredPlansClient.renderQuality = RenderQuality.low();
    }

    if (currentMenu == Id.INSTRUCTIONS_1) {
      this.e150();
    } else {
      this.inputState.processKeyInputVertical();
    }
  }

  private void a316(final int var1, int var3, final int var4, final int var5, final Font var8, final String var9) {

    for (int var12 = 0; var12 < Objects.requireNonNull(GameUI.breakLinesWithColorTags(var8, var9, new int[]{var5})).length; ++var12) {
      final String var13 = Objects.requireNonNull(GameUI.breakLinesWithColorTags(var8, var9, new int[]{var5}))[var12];
      var3 += var1;
      final int var14 = var8.measureLineWidth(var13);
      if (4 * var14 < var5 * 3) {
        var8.draw(var13, var4, var3, Drawing.WHITE);
      } else {
        var8.drawJustified(var13, var4, var3, var5);
      }
    }

  }

  private void a827(final int var1, final int var3, final int var4, final int var5, final int var6, final int var7) {
    final byte var8 = 20;
    final byte var9 = 5;
    ShatteredPlansClient.a229ch(var6, 7, 24, var1 - 2, var7 - 2 + (var3 - var5), 0, 4 + var5);
    if (var4 > 0) {
      final int var11 = var3 * var4 / 15 + 10;
      Drawing.fillRoundedRect(var1, var3 + (var7 - var11), var8, var11, var9, var6);
      int var12 = var11 / 3;
      if (var12 <= 10) {
        var12 = 11;
      }

      Drawing.b370(var1, var3 + (var7 - var11), var8, var12, var9, var6);
      final int[] var13 = new int[4];
      Drawing.saveBoundsTo(var13);
      Drawing.setBounds(var1, var7, var1 + var8, var3 + var7);
      GameView.a835ie(GameView.COMBAT_HEX_WHITE, var1, var7);
      Drawing.restoreBoundsFrom(var13);
    }

    SMALL_FONT.drawCentered(Integer.toString(var4), 10 + var1, 10 + var7 + var3, Drawing.WHITE);
  }

  private void a366(final int var1) {
    final int var3 = _ssH.length;
    int var4 = _acw + 147;

    int var5;
    for (var5 = 0; var5 < var3; ++var5) {
      var4 -= _cbi[var5];
      if (var4 < 0) {
        break;
      }
    }

    final int var6 = var5 + var1;
    if (var6 >= 0 && _ssH.length > var6) {
      this.d093(var6);
    }

  }

  private void b258(@MagicConstant(valuesFromClass = Item.class) final int which, final int var1, final boolean var2) {
    if (which == Item.SINGLE_PLAYER_SKIRMISH) {
      this._g = var1;
      switchTo(Id.SKIRMISH_SETUP, nextMenu, var2);
    } else if (which == Item.ENTER_MULTIPLAYER_LOBBY) {
      if (JagexApplet.isAnonymous) {
        showLobbyDialog(StringConstants.PLEASE_LOG_IN, var2, 0);
      } else {
        this._g = var1;
        switchTo(Id.LOADING_LOBBY, nextMenu, var2);
      }
    } else if (which == Item.RETURN_TO_GAME) {
      this._g = var1;
      switchTo(Id.GAME, 0, var2);
    } else if (which == Item.RANKINGS) {
      this._g = var1;
      switchTo(Id.RATINGS, nextMenu, var2);
    } else if (which == Item.INSTRUCTIONS_1) {
      this._g = var1;
      switchTo(Id.INSTRUCTIONS_1, nextMenu, var2);
    } else if (which == Item.INSTRUCTIONS_2) {
      this._g = var1;
      switchTo(Id.INSTRUCTIONS_2, nextMenu, var2);
    } else if (which == Item.MENU) {
      this._g = var1;
      switchTo(Id.MAIN, nextMenu, var2);
    } else if (which == Item.CURRENT_MENU) {
      this._g = var1;
      switchTo(_brm, nextMenu, var2);
    } else if (which == Item.FULLSCREEN) {
      if (ShatteredPlansClient.fullScreenCanvas == null) {
        ShatteredPlansClient.accountCreationDisabled = false;
        a540fm(var2);
      } else {
        ShatteredPlansClient.removeFocusLossDetectingCanvas();
      }
    } else if (which == Item.END_GAME) {
      this._g = var1;
      ClientGameSession.playSession.handlePlayerLeft(var2);
      ShatteredPlansClient._lgb = true;
      ShatteredPlansClient._tli = false;
    } else if (which == Item.OFFER_DRAW) {
      this._g = var1;
      ClientGameSession.playSession.sendOfferDraw();
      switchTo(Id.GAME, 0, var2);
    } else if (which == Item.RESIGN) {
      this._g = var1;
      ClientGameSession.playSession.sendResign();
      switchTo(Id.GAME, 0, var2);
    } else if (which == Item.OFFER_REMATCH) {
      this._g = var1;
      ClientGameSession.playSession.offerRematch();
      switchTo(Id.GAME, 0, var2);
    } else if (which == Item.RETURN_TO_LOBBY) {
      this._g = var1;
      if (ShatteredPlansClient.playingGame) {
        switchTo(Id.LEAVE_GAME_TO_LOBBY, nextMenu, var2);
      }
      if (ShatteredPlansClient.spectatingGame) {
        C2SPacket.spectateGame(0);
      }
    } else if (which == Item.RATING_MODE_1) {
      _ffy = 0;
    } else if (which == Item.RATING_MODE_2) {
      _ffy = 1;
    } else if (which == Item.LOGIN_CREATE_ACCOUNT) {
      if (this.id == Id.ACHIEVEMENTS || currentMenu == Id.RATINGS) {
        ShatteredPlansClient.a776qk(this.id);
      }
    } else if (which == Item.ACHIEVEMENTS) {
      this._g = var1;
      switchTo(Id.ACHIEVEMENTS, nextMenu, var2);
    } else if (which == Item.DISCARD) {
      this._g = var1;
      switchTo(Id.MAIN, nextMenu, var2);
      discarding = true;
    } else if (which == Item.TUTORIAL) {
      this._g = var1;
      switchTo(Id.TUTORIAL, 0, var2);
    } else if (which == Item.QUIT) {
      this._g = var1;
      JagexApplet.navigateToQuit(JagexBaseApplet.getInstance());
    } else if (which == Item.WATCH_INTRODUCTION) {
      switchTo(Id.INTRODUCTION, nextMenu, var2);
    } else if (which == Item.GAME_TYPE) {
      skirmishGameType = skirmishGameType.next();
      ShatteredPlansClient.MENU_ITEM_LABELS[28] = switch (skirmishGameType) {
        case CONQUEST         -> StringConstants.TEXT_MAP_HEX;
        case CAPTURE_AND_HOLD -> StringConstants.TEXT_MAP_SOL;
        case POINTS           -> StringConstants.TEXT_MAP_POINTS;
        case DERELICTS        -> StringConstants.TEXT_MAP_DERELICTS;
        case TUTORIAL         -> throw new IllegalStateException();
      };
    } else if (which == Item.RULE_SET) {
      if (gameOptions == GameOptions.STREAMLINED_GAME_OPTIONS) {
        gameOptions = GameOptions.CLASSIC_GAME_OPTIONS;
        ShatteredPlansClient.MENU_ITEM_LABELS[29] = StringConstants.TEXT_GARRISON_YES;
      } else {
        ShatteredPlansClient.MENU_ITEM_LABELS[29] = StringConstants.TEXT_GARRISON_NO;
        gameOptions = GameOptions.STREAMLINED_GAME_OPTIONS;
      }
    } else if (which == Item.START_SKIRMISH) {
      this._g = var1;
      switchTo(Id.SKIRMISH, 0, var2);
    } else if (which == Item.OPTIONS_MENU) {
      this._g = var1;
      switchTo(Id.OPTIONS, nextMenu, var2);
    } else if (which == Item.MUSIC_TRACK) {
      //noinspection StringEquality
      if (ShatteredPlansClient.MENU_ITEM_LABELS[32] == ShatteredPlansClient.MUSIC_OPT_NEW) {
        ShatteredPlansClient.MENU_ITEM_LABELS[32] = ShatteredPlansClient.MUSIC_OPT_OLD;
        ShatteredPlansClient.currentTrack = Sounds.MUSIC_IN_GAME_1;
      } else {
        ShatteredPlansClient.MENU_ITEM_LABELS[32] = ShatteredPlansClient.MUSIC_OPT_NEW;
        ShatteredPlansClient.currentTrack = Sounds.MUSIC_IN_GAME_2;
      }

      ShatteredPlansClient.a827jo(ShatteredPlansClient.currentTrack, 50, true);
    }
  }

  public boolean a427() {
    if (this.id == Id.RATINGS) {
      return --_nba <= 0;
    } else if (this.id == Id.ACHIEVEMENTS) {
      return --_leb <= 0;
    } else if (this.id == Id.ERROR) {
      return --_onc <= 0;
    } else {
      return true;
    }
  }

  private int h137() {
    if (currentMenu == this.id || this.id == nextMenu) {
      return nextMenu != currentMenu && currentMenu >= 0 && nextMenu >= 0 ? MathUtil.ease(_ehQ, 32, _tvcm[currentMenu], _tvcm[nextMenu]) : _tvcm[this.id];
    } else {
      return 0;
    }
  }

  private String lookupLabel(@MagicConstant(valuesFromClass = Item.class) final int item) {
    String label = ShatteredPlansClient.MENU_ITEM_LABELS[item];
    if (item == Item.GAME_TYPE || item == Item.RULE_SET) {
      label = "   " + label;
    }

    if (item == Item.CURRENT_MENU) {
      if (_brm == Id.MAIN || nextMenu == Id.MAIN || _brm == Id.OPTIONS || nextMenu == Id.OPTIONS) {
        label = ShatteredPlansClient.MENU_ITEM_LABELS[Item.MENU];
      } else if (_brm == Id.PAUSE_SINGLEPLAYER || nextMenu == Id.PAUSE_SINGLEPLAYER) {
        label = StringConstants.PAUSE_MENU;
      } else if (_brm == Id.PAUSE_MULTIPLAYER_1 || nextMenu == Id.PAUSE_MULTIPLAYER_1 || _brm == Id.PAUSE_MULTIPLAYER_2 || nextMenu == Id.PAUSE_MULTIPLAYER_2 || _brm == Id.PAUSE_MULTIPLAYER_3 || nextMenu == Id.PAUSE_MULTIPLAYER_3) {
        label = StringConstants.OPTIONS_MENU_NOT_PAUSED;
      }
    }

    if (item == Item.END_GAME && ClientGameSession.playSession != null && ClientGameSession.playSession.isTutorial) {
      label = StringConstants.END_TUTORIAL;
    }

    return label != null ? label.toUpperCase() : null;
  }

  private int c417(final int var1) {
    return this.h137() + _tvct[this.id] + widths[this.id] * var1;
  }

  @SuppressWarnings("SameParameterValue")
  private static void drawLabeledVerticalBracket(final String label, final int labelX, final int labelY, final int labelWidth, final int bracketTipX, final int bracketY1, final int bracketY2, final int bracketWidth, final int color) {
    int bracketSpineX = bracketTipX + bracketWidth;
    // draw the bracket limbs
    if (bracketWidth > 0) {
      Drawing.horizontalLine(bracketTipX, bracketY1, bracketWidth, color);
      Drawing.horizontalLine(bracketTipX, bracketY2, bracketWidth, color);
    } else {
      Drawing.horizontalLine(bracketSpineX, bracketY1, -bracketWidth, color);
      Drawing.horizontalLine(bracketSpineX, bracketY2, -bracketWidth, color);
    }

    final boolean labelIsOutsideBracket;
    // draw the bracket spine
    if (bracketY2 > bracketY1) {
      labelIsOutsideBracket = labelY < bracketY1 || labelY >= bracketY2;
      Drawing.verticalLine(bracketSpineX, bracketY1, bracketY2 - bracketY1 + 1, color);
    } else {
      labelIsOutsideBracket = labelY < bracketY2 || labelY >= bracketY1;
      Drawing.verticalLine(bracketSpineX, bracketY2, bracketY1 - bracketY2 + 1, color);
    }

    if (bracketSpineX < labelX) {
      if (labelIsOutsideBracket) {
        // draw the bracket tail
        final int bracketTailY = (bracketY1 + bracketY2) / 2;
        if (bracketWidth <= 0) {
          bracketSpineX += bracketWidth;
          Drawing.horizontalLine(bracketSpineX, bracketTailY, -bracketWidth, color);
        } else {
          Drawing.horizontalLine(bracketSpineX, bracketTailY, bracketWidth, color);
          bracketSpineX += bracketWidth;
        }

        // draw the vertical pointer
        final int verticalPointerHeight = labelY - bracketTailY;
        if (verticalPointerHeight > 0) {
          Drawing.verticalLine(bracketSpineX, bracketTailY, verticalPointerHeight, color);
        } else {
          Drawing.verticalLine(bracketSpineX, bracketTailY + verticalPointerHeight, -verticalPointerHeight, color);
        }
      }
      // draw the horizontal pointer
      Drawing.horizontalLine(bracketSpineX, labelY, labelX - bracketSpineX, color);
    } else {
      if (labelIsOutsideBracket) {
        // draw the bracket tail
        final int bracketTailY = (bracketY1 + bracketY2) / 2;
        if (bracketWidth > 0) {
          bracketSpineX -= bracketWidth;
          Drawing.horizontalLine(bracketSpineX, bracketTailY, bracketWidth, color);
        } else {
          Drawing.horizontalLine(bracketSpineX, bracketTailY, -bracketWidth, color);
          bracketSpineX -= bracketWidth;
        }

        // draw the vertical pointer
        final int verticalPointerHeight = labelY - bracketTailY;
        if (verticalPointerHeight > 0) {
          Drawing.verticalLine(bracketSpineX, bracketTailY, verticalPointerHeight, color);
        } else {
          Drawing.verticalLine(bracketSpineX, bracketTailY + verticalPointerHeight, -verticalPointerHeight, color);
        }
      }
      // draw the horizontal pointer
      Drawing.horizontalLine(labelX, labelY, bracketSpineX - labelX, color);
    }

    final int lineCount = GameUI.breakLines(label, SMALL_FONT, new int[]{labelWidth});
    SMALL_FONT.drawParagraph(
        label,
        labelX + 5,
        labelY - ((lineCount * SMALL_FONT.ascent) / 2),
        labelWidth,
        (lineCount * SMALL_FONT.ascent) + SMALL_FONT.descent,
        color,
        Font.HorizontalAlignment.LEFT,
        Font.VerticalAlignment.TOP,
        SMALL_FONT.ascent);
  }

  private void a346(final int var1, final int var2, final int var3, final boolean var4, final int var6, final int var7, final int var8, final boolean var9) {
    final double var10 = Math.sqrt(MathUtil.euclideanDistanceSquared(-var7 + var1, var3 - var6));
    final double var12 = (double) (-var6 + var3) / var10;
    final double var14 = (double) (-var7 + var1) / var10;
    final int var16 = var6 * 16 + (-var6 + var3) * 16 * var2 / 200;
    final int var17 = var2 * 16 * (var1 - var7) / 200 + 16 * var7;
    final double var18 = Math.sqrt((var17 - var7 * 16) * (-(var7 * 16) + var17) + (var16 - 16 * var6) * (-(16 * var6) + var16));

    final double sqrt = Math.sqrt(256 * (-var7 + var1) * (-var7 + var1) + (-var6 + var3) * (-var6 + var3) * 256);
    final double var20 = sqrt / 2.0D;
    final double var22 = 1.0D / var20;
    final double var24 = -(0.2D * (-var20 + var18) * (var18 - var20) * var22) + sqrt / 10.0D;
    double var26 = 16 * (var7 - var1);
    double var28 = (var6 - var3) * 16;
    var26 /= sqrt;
    var28 /= sqrt;
    final int var30 = (int) (var24 * var26) + var16;
    final int var31 = -((int) (var28 * var24)) + var17;
    int var35;
    if (var12 != 0.0D) {
      if (var14 != 0.0D) {
        double var33 = 16 * (var1 - var7);
        if (var33 < 0.0D) {
          var33 = -var33;
        }

        var35 = (int) (Math.asin(var33 / sqrt) * 32768.0D / Math.PI);
        if (var12 >= 0.0D) {
          if (var14 > 0.0D) {
            var35 = 65536 - var35;
          }
        } else if (var14 >= 0.0D) {
          var35 += 32768;
        } else {
          var35 = '耀' - var35;
        }
      } else if (var12 > 0.0D) {
        var35 = 0;
      } else {
        var35 = 32768;
      }
    } else if (var14 <= 0.0D) {
      var35 = 16384;
    } else {
      var35 = 49152;
    }

    var35 -= 16384;
    var35 = var35 + var2 * 7600 / 200 - 3800;
    GameView.a194ie((ArgbSprite) (var4 ? GameView.ARROW_SHIP_DAMAGED : GameView.ARROW_SHIP), GameView.ARROW_SHIP.width << 3, GameView.ARROW_SHIP.height << 3, var30, var31, var35, 4096);
    if (var9) {
      Drawing.fillCircle(var30 >> 4, var31 >> 4, 9, 0, 92);
      SMALL_FONT.drawCentered(Integer.toString(var8), var30 >> 4, 4 + (var31 >> 4), Drawing.WHITE);
    }

  }

  private int getHoveredItemIndex(final int mouseX, final int mouseY) {
    for (int i = 0; i < items[this.id].length; ++i) {
      final int var5 = this.c417(i);
      if (mouseX >= this.getX() && mouseX < this.a527(i)
          && mouseY >= var5 && mouseY < var5 + this.getWidth()) {
        return i;
      }
    }

    return -1;
  }

  private void e150() {
    if (JagexApplet.lastTypedKeyCode == KeyState.Code.PAGE_UP) {
      this.a366(-1);
    } else if (JagexApplet.lastTypedKeyCode == KeyState.Code.PAGE_DOWN) {
      this.a366(1);
    } else if (JagexApplet.lastTypedKeyCode == KeyState.Code.HOME) {
      this.d093(0);
    } else if (JagexApplet.lastTypedKeyCode == KeyState.Code.END) {
      this.d093(_ssH.length - 1);
    }

    this.inputState.processKeyInputVertical();
  }

  private int c137() {
    if (currentMenu != this.id && this.id != nextMenu) {
      return 0;
    } else if (nextMenu == currentMenu) {
      return _tvcr[currentMenu];
    } else if (currentMenu >= 0 && nextMenu >= 0) {
      return MathUtil.ease(_ehQ, 32, _tvcr[currentMenu], _tvcr[nextMenu]);
    } else {
      double var2 = MathUtil.ease(_ehQ, 32);
      if (this.id == currentMenu) {
        var2 = -var2 + 1.0D;
      }

      return (int) ((double) _tvcr[this.id] * var2);
    }
  }

  public static final int NUM_GROUPS = 14;

  @SuppressWarnings("WeakerAccess")
  public static final class Id {
    public static final int GAME = -1;
    public static final int LOBBY = -2;
    public static final int INTRODUCTION = -3;
    public static final int SKIRMISH = -4;
    public static final int TUTORIAL = -5;
    public static final int LOADING_LOBBY = -6;
    public static final int LEAVE_GAME_TO_LOBBY = -7;
    public static final int MAIN = 0;
    public static final int PAUSE_SINGLEPLAYER = 1;
    public static final int PAUSE_MULTIPLAYER_1 = 2;
    public static final int PAUSE_MULTIPLAYER_2 = 3;
    public static final int PAUSE_MULTIPLAYER_3 = 4;
    public static final int RATINGS = 5;
    public static final int INSTRUCTIONS_1 = 6;
    public static final int INSTRUCTIONS_2 = 7;
    public static final int ACHIEVEMENTS = 8;
    public static final int G9 = 9;
    public static final int ERROR = 10;
    public static final int SKIRMISH_SETUP = 12;
    public static final int OPTIONS = 13;
  }

  @SuppressWarnings("WeakerAccess")
  public static final class Item {
    public static final int SINGLE_PLAYER_SKIRMISH = 0;
    public static final int ENTER_MULTIPLAYER_LOBBY = 1;
    public static final int RETURN_TO_GAME = 2;
    public static final int RANKINGS = 4;
    public static final int INSTRUCTIONS_1 = 5;
    public static final int INSTRUCTIONS_2 = 6;
    public static final int FULLSCREEN = 7;
    public static final int MENU = 8;
    public static final int CURRENT_MENU = 9;
    public static final int END_GAME = 10;
    public static final int OFFER_DRAW = 11;
    public static final int RESIGN = 12;
    public static final int OFFER_REMATCH = 13;
    public static final int RETURN_TO_LOBBY = 14;
    public static final int HS_MODE_1 = 15;
    public static final int HS_MODE_2 = 16;
    public static final int HS_MODE_3 = 17;
    public static final int RATING_MODE_1 = 18;
    public static final int RATING_MODE_2 = 19;
    public static final int QUIT = 20;
    public static final int SOUND_VOLUME = 21;
    public static final int MUSIC_VOLUME = 22;
    public static final int ACHIEVEMENTS = 23;
    public static final int LOGIN_CREATE_ACCOUNT = 24;
    public static final int DISCARD = 25;
    public static final int TUTORIAL = 26;
    public static final int WATCH_INTRODUCTION = 27;
    public static final int GAME_TYPE = 28;
    public static final int RULE_SET = 29;
    public static final int START_SKIRMISH = 30;
    public static final int OPTIONS_MENU = 31;
    public static final int MUSIC_TRACK = 32;
  }

  public enum DialogOption {
    ACCEPT, TRY_AGAIN, MEMBERS, CLOSE
  }

  public enum FullscreenDialog {
    MEMBERS_ONLY_NOT_LOGGED_IN, MEMBERS_ONLY_LOGGED_IN, ACCEPT_COUNTDOWN, UNAVAILABLE, LOST_FOCUS, TIMEOUT
  }
}
