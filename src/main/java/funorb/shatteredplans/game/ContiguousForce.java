package funorb.shatteredplans.game;

import funorb.shatteredplans.map.StarSystem;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Objects;

public final class ContiguousForce extends Force {
  public ContiguousForce(final Player player, final @NotNull StarSystem capital) {
    super(player, capital);
  }

  @SuppressWarnings("EmptyMethod")
  @Override
  public @NotNull StarSystem getCapital() {
    return super.getCapital();
  }

  @Override
  public void setCapital(final @NotNull StarSystem capital) {
    super.setCapital(Objects.requireNonNull(capital, "capital must not be null"));
  }

  public void setSystems(final @NotNull List<StarSystem> systems) {
    this.systems = systems;
  }
}
