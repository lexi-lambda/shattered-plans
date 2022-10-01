package funorb.shatteredplans.map.generator;

import funorb.io.Buffer;
import funorb.shatteredplans.map.Map;
import funorb.shatteredplans.map.StarSystem;

public final class CaptureAndHoldMapReader implements MapGenerator {
  private final Buffer _a;
  private final int _d;

  public CaptureAndHoldMapReader(final Buffer var1, final int var2) {
    this._d = var2;
    this._a = var1;
  }

  @Override
  public Map generate() {
    final int var2 = 11 * 200 + 100;
    final int var3 = Map.drawingHeight(11);
    final Map var4 = new Map(var2, var3, this._d, 73);
    final CaptureAndHoldMapGenerator var6 = new CaptureAndHoldMapGenerator(this._d, 2);
    var6.placeSystems(var4);
    final StarSystem[] var7 = var4.systems;

    for (final StarSystem var9 : var7) {
      final int var10 = this._a.readUByte();

      int var11;
      int var12;
      for (var11 = 0; var10 > var11; ++var11) {
        var12 = this._a.readUByte();
        final StarSystem var13 = var4.systems[var12];
        StarSystem.linkNeighbors(var9, var13);
      }

      for (var11 = 0; var11 < 4; ++var11) {
        var12 = this._a.readUByte();
        var9.resources[var11] = var12 & 15;
        ++var11;
        if (var11 >= 4) {
          break;
        }

        var9.resources[var11] = (var12 & 240) >> 4;
      }

      var9.garrison = this._a.readUShort();
      if ((var9.garrison & '耀') != 0) {
        var9.garrison &= 32767;
        var9.hasDefensiveNet = true;
      }

      //noinspection MagicConstant
      var9.type = this._a.readUByte();
      //noinspection MagicConstant
      var9.score = var9.type >>> 6;
      //noinspection MagicConstant
      var9.type &= 63;
    }

    var4.recalculateDistances();
    return var4;
  }
}
