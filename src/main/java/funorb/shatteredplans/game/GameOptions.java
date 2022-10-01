package funorb.shatteredplans.game;

import funorb.io.Buffer;
import funorb.io.WritableBuffer;

public final class GameOptions {
  public static final GameOptions CLASSIC_GAME_OPTIONS = new GameOptions(true, true, false, true, false, false, false, 0);
  public static final GameOptions STREAMLINED_GAME_OPTIONS = new GameOptions(true, true, true, true, false, true, true, 0);

  public final boolean diplomacyAllowed;
  public final boolean projectsAllowed;
  public final boolean simpleGarrisoning;
  public final boolean garrisonsCanBeRemoved;
  public final boolean noChainCollapsing;
  public final boolean unifiedTerritories;
  public final boolean destructibleDefenceNets;
  public final int movementRange;

  @SuppressWarnings("WeakerAccess")
  public GameOptions(final boolean diplomacyAllowed,
                     final boolean projectsAllowed,
                     final boolean simpleGarrisoning,
                     final boolean garrisonsCanBeRemoved,
                     final boolean noChainCollapsing,
                     final boolean unifiedTerritories,
                     final boolean destructibleDefenceNets,
                     final int movementRange) {
    this.diplomacyAllowed = diplomacyAllowed;
    this.projectsAllowed = projectsAllowed;
    this.simpleGarrisoning = simpleGarrisoning;
    this.garrisonsCanBeRemoved = garrisonsCanBeRemoved;
    this.noChainCollapsing = noChainCollapsing;
    this.unifiedTerritories = unifiedTerritories;
    this.destructibleDefenceNets = destructibleDefenceNets;
    this.movementRange = movementRange;
  }

  @SuppressWarnings("SameParameterValue")
  public static GameOptions read(final Buffer buffer) {
    final int movementRange = buffer.readUByte();
    final int flags = buffer.readUByte();
    return new GameOptions(
        (flags & 1) != 0,
        (flags & 2) != 0,
        (flags & 4) != 0,
        (flags & 8) != 0,
        (flags & 16) != 0,
        (flags & 32) != 0,
        (flags & 64) != 0,
        movementRange);
  }

  public void write(final WritableBuffer buffer) {
    buffer.writeByte(this.movementRange);
    buffer.writeByte(
        (this.diplomacyAllowed ? 1 : 0)
            | (this.projectsAllowed ? 1 : 0) << 1
            | (this.simpleGarrisoning ? 1 : 0) << 2
            | (this.garrisonsCanBeRemoved ? 1 : 0) << 3
            | (this.noChainCollapsing ? 1 : 0) << 4
            | (this.unifiedTerritories ? 1 : 0) << 5
            | (this.destructibleDefenceNets ? 1 : 0) << 6
    );
  }

  @Override
  public String toString() {
    return "Diplomacy Allowed: " + (this.diplomacyAllowed ? "Yes" : "No") + "\n" +
        "Projects Allowed: " + (this.projectsAllowed ? "Yes" : "No") + "\n" +
        "Simple Garrisoning: " + (this.simpleGarrisoning ? "Yes" : "No") + "\n" +
        "Garrisons Can Be Removed: " + (this.garrisonsCanBeRemoved ? "Yes" : "No") + "\n" +
        "Chain Collapsing: " + (!this.noChainCollapsing ? "Yes" : "No") + "\n" +
        "Unified Territories: " + (this.unifiedTerritories ? "Yes" : "No") + "\n" +
        "Destructable Defence Nets: " + (this.destructibleDefenceNets ? "Yes" : "No") + "\n" +
        "Movement Range: " + this.movementRange;
  }
}
