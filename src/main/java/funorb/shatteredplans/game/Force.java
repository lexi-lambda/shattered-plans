package funorb.shatteredplans.game;

import funorb.io.WritableBuffer;
import funorb.shatteredplans.map.StarSystem;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

public abstract class Force implements Iterable<StarSystem> {
  public final Player player;
  private StarSystem capital;
  protected @NotNull List<StarSystem> systems = new ArrayList<>();
  public int fleetProduction;
  public int fleetsAvailableToBuild;
  public int[] surplusResources;
  public int[] surplusResourceRanks;

  protected Force(final Player player, final StarSystem capital) {
    this.player = player;
    this.setCapital(capital);
  }

  /**
   * The “capital” of a force is somewhat arbitrarily selected, but it is used
   * to name the force in the user interface and is where unbuilt fleets are
   * placed at the end of each turn.
   */
  public StarSystem getCapital() {
    return this.capital;
  }

  public void setCapital(final StarSystem capital) {
    this.capital = capital;
  }

  public final void add(final StarSystem system) {
    this.systems.add(system);
  }

  public final void remove(final StarSystem system) {
    this.systems.remove(system);
  }

  public final Optional<StarSystem> getFirst() {
    return this.isEmpty() ? Optional.empty() : Optional.of(this.systems.get(0));
  }

  public final boolean isEmpty() {
    return this.systems.isEmpty();
  }

  public final int size() {
    return this.systems.size();
  }

  @NotNull
  @Override
  public final Iterator<StarSystem> iterator() {
    return this.systems.iterator();
  }

  @SuppressWarnings({"unused", "WeakerAccess"})
  public final Stream<StarSystem> stream() {
    return this.systems.stream();
  }

  public final void write(final WritableBuffer buffer) {
    assert !this.isEmpty();
    this.player.write(buffer);
    this.getCapital().write(buffer);
    buffer.writeByte(this.size());
    for (final StarSystem system : this) {
      if (system != this.getCapital()) {
        StarSystem.write(buffer, system);
      }
    }
  }

  @SuppressWarnings("unused")
  public final boolean contains(final StarSystem system) {
    return this.systems.contains(system);
  }
}
