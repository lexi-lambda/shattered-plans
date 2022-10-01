package funorb.shatteredplans.map.generator;

import funorb.shatteredplans.game.GameState;
import funorb.io.Buffer;
import funorb.shatteredplans.map.Map;
import funorb.shatteredplans.map.StarSystem;

public final class StandardMapReader implements MapGenerator {
  private final Buffer packet;
  private final int seed;
  private final int mapWidth;
  private final int mapHeight;
  private final int systemCount;

  public StandardMapReader(final Buffer packet, final int seed, final int mapWidth, final int mapHeight, final int systemCount) {
    this.packet = packet;
    this.seed = seed;
    this.mapWidth = mapWidth;
    this.mapHeight = mapHeight;
    this.systemCount = systemCount;
  }

  @Override
  public Map generate() throws MapGenerationFailure {
    final Map var2 = new Map(this.mapWidth * 200 + 100, Map.drawingHeight(this.mapHeight), this.seed, this.systemCount);
    final StandardMapGenerator generator = new StandardMapGenerator(this.mapWidth, this.mapHeight, this.seed, this.systemCount, 1);
    generator.placeSystems(var2);

    for (final StarSystem var6 : var2.systems) {
      final int neighborCount = this.packet.readUByte();

      for (int i = 0; i < neighborCount; ++i) {
        final int var9 = this.packet.readUByte();
        final StarSystem var10 = var2.systems[var9];
        StarSystem.linkNeighbors(var6, var10);
      }

      for (int var8 = 0; var8 < GameState.NUM_RESOURCES; ++var8) {
        final int var9 = this.packet.readUByte();
        var6.resources[var8] = var9 & 15;
        ++var8;
        if (var8 >= 4) {
          break;
        }

        var6.resources[var8] = (var9 & 240) >> 4;
      }

      var6.garrison = this.packet.readUShort();
      if ((var6.garrison & 0x8000) != 0) {
        var6.hasDefensiveNet = true;
        var6.garrison &= 0x7fff;
      }

      //noinspection MagicConstant
      var6.type = this.packet.readUByte();
      //noinspection MagicConstant
      var6.score = var6.type >>> 6;
      //noinspection MagicConstant
      var6.type &= 63;
    }

    var2.recalculateDistances();
    return var2;
  }
}
