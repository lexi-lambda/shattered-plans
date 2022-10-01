package funorb.shatteredplans.game;

import funorb.io.WritableBuffer;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

public final class TurnOrders {
  public final @NotNull List<ProjectOrder> projectOrders = new ArrayList<>();
  public final @NotNull List<BuildFleetsOrder> buildOrders = new ArrayList<>();
  public final @NotNull List<MoveFleetsOrder> moveOrders = new ArrayList<>();

  @SuppressWarnings("unused")
  public void clear() {
    this.projectOrders.clear();
    this.buildOrders.clear();
    this.moveOrders.clear();
  }

  public void add(final ProjectOrder order) {
    this.projectOrders.add(order);
  }

  public void add(final BuildFleetsOrder order) {
    this.buildOrders.add(order);
  }

  public void add(final MoveFleetsOrder order) {
    this.moveOrders.add(order);
  }

  @SuppressWarnings("unused")
  public int checksum() {
    return checksum(this.projectOrders, this.buildOrders, this.moveOrders);
  }

  @SuppressWarnings("unused")
  public void write(final @NotNull WritableBuffer buffer) {
    write(buffer, this.projectOrders, this.buildOrders, this.moveOrders);
  }

  public static int checksum(final @NotNull Iterable<ProjectOrder> projectOrders,
                             final @NotNull Iterable<BuildFleetsOrder> buildOrders,
                             final @NotNull Iterable<MoveFleetsOrder> moveOrders) {
    int checksum = 406213746;
    for (final ProjectOrder order : projectOrders) {
      checksum += (17 * order.target.index + order.type) * (order.source == null ? 5 : order.source.index + 7);
    }
    for (final BuildFleetsOrder order : buildOrders) {
      checksum += order.system.index * (order.quantity ^ 11) << 11;
    }
    for (final MoveFleetsOrder order : moveOrders) {
      checksum += (1 + order.source.index) * (1 + order.target.index + 65536) + order.quantity * (1 + order.target.index) * (1 + order.source.index);
    }
    return checksum;
  }

  @SuppressWarnings("SameParameterValue")
  public static void write(final @NotNull WritableBuffer buffer,
                           final @NotNull Iterable<ProjectOrder> projectOrders,
                           final @NotNull Collection<BuildFleetsOrder> buildOrders,
                           final @NotNull Collection<MoveFleetsOrder> moveOrders) {
    int buildOrderCount = buildOrders.size();
    for (final ProjectOrder order : projectOrders) {
      buffer.writeByte(order.type + 251);
      order.write(buffer);
    }

    final Iterator<BuildFleetsOrder> buildIterator = buildOrders.iterator();
    for (; buildOrderCount >= 59; buildOrderCount -= 59) {
      buffer.writeByte(250);
      for (int i = 0; i < 59; ++i) {
        buildIterator.next().write(buffer);
      }
    }
    if (buildOrderCount > 0) {
      buffer.writeByte(191 + buildOrderCount);
      while (buildIterator.hasNext()) {
        buildIterator.next().write(buffer);
      }
    }

    int moveOrderCount = moveOrders.size();
    final Iterator<MoveFleetsOrder> moveIterator = moveOrders.iterator();
    for (; moveOrderCount >= 192; moveOrderCount -= 192) {
      buffer.writeByte(191);
      for (int i = 0; i < 192; ++i) {
        moveIterator.next().write(buffer);
      }
    }
    if (moveOrderCount > 0) {
      buffer.writeByte(moveOrderCount - 1);
      while (moveIterator.hasNext()) {
        moveIterator.next().write(buffer);
      }
    }
  }
}
