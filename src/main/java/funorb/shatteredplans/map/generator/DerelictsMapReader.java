package funorb.shatteredplans.map.generator;

import funorb.io.Buffer;
import funorb.shatteredplans.map.Map;
import funorb.shatteredplans.map.StarSystem;

public final class DerelictsMapReader implements MapGenerator {
  private final Buffer _m;
  private final int _l;
  private final int _b;
  private final int _k;
  private final int _j;
  private int _e;

  public DerelictsMapReader(final Buffer var1, final int var2, final int var3, final int var4, final int var5, final int var6) {
    this._j = var4;
    this._l = var2;
    this._k = var6;
    this._e = var5;
    this._m = var1;
    this._b = var3;
  }

  @Override
  public Map generate() throws MapGenerationFailure {
    --this._e;
    final DerelictsMapGenerator var2 = new DerelictsMapGenerator(this._b, this._j, this._l, this._e, this._k);
    final Map var3 = var2.generate();
    final StarSystem[] var4 = var3.systems;

    for (final StarSystem var7 : var4) {
      final int var8 = this._m.readUByte();

      int var9;
      int var10;
      for (var9 = 0; var9 < var8; ++var9) {
        var10 = this._m.readUByte();
        final StarSystem var11 = var3.systems[var10];
        if (!var7.hasNeighbor(var11)) {
          StarSystem.linkNeighbors(var7, var11);
        }
      }

      for (var9 = 0; var9 < 4; ++var9) {
        var10 = this._m.readUByte();
        var7.resources[var9] = 15 & var10;
        ++var9;
        if (var9 >= 4) {
          break;
        }

        var7.resources[var9] = (var10 & 240) >> 4;
      }

      if (var7.score == StarSystem.Score.NEUTRAL_HOMEWORLD) {
        for (var9 = 0; var9 < 4; ++var9) {
          var7.resources[var9] = -var7.resources[var9];
        }
      }

      var7.garrison = this._m.readUShort();
      if (('耀' & var7.garrison) != 0) {
        var7.garrison &= 32767;
        var7.hasDefensiveNet = true;
      }

      //noinspection MagicConstant
      var7.type = this._m.readUByte();
      //noinspection MagicConstant
      var7.score = var7.type >>> 6;
      //noinspection MagicConstant
      var7.type &= 63;
    }

    return var3;
  }
}
