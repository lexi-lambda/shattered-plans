package funorb.shatteredplans.map.generator;

import funorb.shatteredplans.map.StarSystem;
import funorb.util.MathUtil;

import java.util.Arrays;
import java.util.List;

public final class WormholeConnection {
  private final StarSystem system;
  private final float dx;
  private final float dy;
  private final float midX;
  private final float midY;
  private float _o = 10000.0F;
  private float _j = -10000.0f;

  public WormholeConnection(final StarSystem system1, final StarSystem system2) {
    this.system = system2;
    this.dx = (float) (system1.posnX - system2.posnX);
    this.dy = (float) (system2.posnY - system1.posnY);
    this.midX = (float) (system1.posnX + system2.posnX) / 2.0F;
    this.midY = (float) (system2.posnY + system1.posnY) / 2.0F;
  }

  public static void a680eo(final WormholeConnection nc1, final WormholeConnection nc2) {
    final float area = nc2.dx * nc1.dy - nc1.dx * nc2.dy;
    if (area == 0.0F) {
      // colinear
      final boolean dx1Pos = nc1.dx > 0.0F;
      final boolean dy1Pos = nc1.dy > 0.0F;
      final boolean dx2Pos = nc2.dx > 0.0F;
      final boolean dy2Pos = nc2.dy > 0.0F;
      if (dx1Pos == dx2Pos && dy1Pos == dy2Pos) {
        if (MathUtil.euclideanDistanceSquared(nc1.dx, nc1.dy) <= MathUtil.euclideanDistanceSquared(nc2.dx, nc2.dy)) {
          nc2._j = 10000.0F;
          nc2._o = -10000.0f;
        } else {
          nc1._o = -10000.0f;
          nc1._j = 10000.0F;
        }
      }
    } else {
      final float dx = nc2.midX - nc1.midX;
      final float dy = nc2.midY - nc1.midY;
      final float var10 = ((nc2.dx * dx) - (nc2.dy * dy)) / area;
      final float var11 = ((nc1.dx * dx) - (nc1.dy * dy)) / area;
      if (area >= 0.0F) {
        if (nc2._o > var11) {
          nc2._o = var11;
        }
        if (var10 > nc1._j) {
          nc1._j = var10;
        }
      } else {
        if (var10 < nc1._o) {
          nc1._o = var10;
        }

        if (var11 > nc2._j) {
          nc2._j = var11;
        }
      }
    }
  }

  public static void a379ad(final List<WormholeConnection> connections) {
    connections.removeIf(var2 -> var2._o <= var2._j);
  }

  public static StarSystem[] sortedSystems(final List<WormholeConnection> connections) {
    final int[] distances = connections.stream()
        .mapToInt(connection -> (int) (0.5D + MathUtil.euclideanDistance((double) connection.dx, connection.dy)))
        .toArray();

    final WormholeConnection[] sorted = connections.toArray(WormholeConnection[]::new);
    outer:
    for (int i = 1; i < sorted.length; ++i) {
      for (int j = i - 1; j >= 0; --j) {
        if (distances[i] - distances[j] < 0) {
          sorted[j + 1] = sorted[i];
          continue outer;
        }
        sorted[1 + j] = sorted[j];
      }
      sorted[0] = sorted[i];
    }

    return Arrays.stream(sorted).map(connection -> connection.system).toArray(StarSystem[]::new);
  }
}
