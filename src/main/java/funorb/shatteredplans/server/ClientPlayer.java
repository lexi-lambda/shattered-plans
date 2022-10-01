package funorb.shatteredplans.server;

import funorb.shatteredplans.game.BuildFleetsOrder;
import funorb.shatteredplans.game.GameState;
import funorb.shatteredplans.game.MoveFleetsOrder;
import funorb.shatteredplans.game.Player;
import funorb.shatteredplans.game.ProjectOrder;
import funorb.shatteredplans.game.TurnOrders;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiPredicate;

public final class ClientPlayer {
  public final @NotNull ClientConnection client;
  public final @NotNull Player player;

  private final List<ProjectOrder> projectOrders = new ArrayList<>();
  private final List<BuildFleetsOrder> buildOrders = new ArrayList<>();
  private final List<MoveFleetsOrder> moveOrders = new ArrayList<>();

  public boolean endedTurn = false;

  public ClientPlayer(final @NotNull ClientConnection client, final @NotNull Player player) {
    this.client = client;
    this.player = player;
  }

  public int ordersChecksum() {
    return TurnOrders.checksum(this.projectOrders, this.buildOrders, this.moveOrders);
  }

  public void addOrder(final ProjectOrder order) {
    if (order.player != this.player) {
      throw new RuntimeException("client sent a project order that doesn't belong to them");
    }
    this.projectOrders.removeIf(order::replaces);
    if (order.target != null) {
      this.projectOrders.add(order);
    }
  }

  public void addOrder(final BuildFleetsOrder order)  {
    if (order.quantity == 0) {
      this.buildOrders.removeIf(order::replaces);
    } else {
      addOrReplace(this.buildOrders, order, BuildFleetsOrder::replaces);
    }
  }

  public void addOrder(final MoveFleetsOrder order)  {
    if (order.player != this.player) {
      throw new RuntimeException("client sent a move order that doesn't belong to them");
    }
    if (order.quantity == 0) {
      this.moveOrders.removeIf(order::replaces);
    } else {
      addOrReplace(this.moveOrders, order, MoveFleetsOrder::replaces);
    }
  }

  public void resetForTurnStart() {
    this.endedTurn = false;
    this.projectOrders.clear();
    this.buildOrders.clear();
    this.moveOrders.clear();
  }

  public void submitOrders(final GameState state) {
    state.validateOrders(this.player, this.projectOrders, this.buildOrders, this.moveOrders);
    state.addOrders(this.projectOrders, this.buildOrders, this.moveOrders);
  }

  private static <T> void addOrReplace(final List<T> list, final T newValue, final BiPredicate<T, T> predicate) {
    final var iterator = list.listIterator();
    while (iterator.hasNext()) {
      final T oldValue = iterator.next();
      if (predicate.test(oldValue, newValue)) {
        iterator.set(newValue);
        return;
      }
    }
    list.add(newValue);
  }
}
