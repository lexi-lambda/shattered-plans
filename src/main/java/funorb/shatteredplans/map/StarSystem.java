package funorb.shatteredplans.map;

import funorb.Strings;
import funorb.cache.ResourceLoader;
import funorb.graphics.Point;
import funorb.graphics.Sprite;
import funorb.io.ReadableBuffer;
import funorb.io.WritableBuffer;
import funorb.shatteredplans.client.ShatteredPlansClient;
import funorb.shatteredplans.game.ContiguousForce;
import funorb.shatteredplans.game.MoveFleetsOrder;
import funorb.shatteredplans.game.Player;
import org.intellij.lang.annotations.MagicConstant;

import java.util.Arrays;
import java.util.Comparator;
import java.util.Random;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.function.Predicate;

public final class StarSystem {
  public static String[] NAMES;
  public static Sprite PLANET_SCORCHED_EARTH;
  public static Sprite PLANET_BURNT;
  public static Sprite PLANET_EARTH_LIKE;
  public static Sprite PLANET_EXOTIC;
  public static Sprite PLANET_GAS;
  public static Sprite PLANET_RINGED;
  public static Sprite PLANET_ROCK;
  public static Sprite ALIEN_MINER;
  public static Sprite ALIEN_SHIP;
  public static Sprite ALIEN_BASE;

  public static void loadNames(final ResourceLoader loader) {
    NAMES = Strings.splitLines(Strings.decode1252String(loader.getResource("", "starnames.txt")));
  }

  public final int _z;
  public final int posnY;
  public final int index;
  public final SortedSet<MoveFleetsOrder> outgoingOrders;
  public final int posnX;
  public int retreatedFleets;
  public int retreatingFleets;
  public ContiguousForce contiguousForce;
  public String name;
  public StarSystem[] neighbors;
  public int garrison;
  @MagicConstant(valuesFromClass = Type.class)
  public int type;
  public Point[] hexPoints;
  public StarSystem[] potentialWormholeConnections;
  public int minimumGarrison;
  public boolean hasDefensiveNet;
  public Player lastOwner; // non-null if the system was ever owned, even if the system later becomes neutral again
  public Player owner;
  @MagicConstant(valuesFromClass = Score.class)
  public int score;
  public int remainingGarrison; // garrison strength minus any fleet movement orders out of this system
  public final SortedSet<MoveFleetsOrder> incomingOrders;
  public int[] resources = new int[4];

  public StarSystem(final int index, final int posnX, final int posnY, final int var4) {
    this._z = var4;
    this.index = index;
    this.posnX = posnX;
    this.posnY = posnY;
    this.outgoingOrders = new TreeSet<>(OUTGOING_ORDERS_COMPARATOR);
    this.incomingOrders = new TreeSet<>(INCOMING_ORDERS_COMPARATOR);
  }

  private static final Comparator<MoveFleetsOrder> OUTGOING_ORDERS_COMPARATOR =
      Comparator.comparingDouble((MoveFleetsOrder order) -> {
        final int dx1 = order.target.posnX - order.source.posnX;
        final int dy1 = order.target.posnY - order.source.posnY;
        double angle = Math.atan2(dy1, dx1);
        if (dx1 < 0 && dy1 > -40 && dy1 < 0) {
          angle += Math.PI * 2;
        }
        return angle;
      }).reversed().thenComparingInt(Object::hashCode);

  private static final Comparator<MoveFleetsOrder> INCOMING_ORDERS_COMPARATOR =
      Comparator.comparingInt(MoveFleetsOrder::euclideanDistanceSquared).thenComparingInt(Object::hashCode);

  public static void write(final WritableBuffer buffer, final StarSystem system) {
    buffer.writeByte(system == null ? -1 : system.index);
  }

  public static StarSystem read(final ReadableBuffer packet, final StarSystem[] systems) {
    final int index = packet.readUByte();
    return index < systems.length ? systems[index] : null;
  }

  public static void linkNeighbors(final StarSystem system1, final StarSystem system2) {
    system1.addNeighbor(system2);
    system2.addNeighbor(system1);
  }

  public Sprite getSprite() {
    if (this.name.equalsIgnoreCase("Sol")) {
      return PLANET_SCORCHED_EARTH;
    } else if (this.type == Type.PLANET_BURNT) {
      return PLANET_BURNT;
    } else if (this.type == Type.PLANET_EARTH_LIKE) {
      return PLANET_EARTH_LIKE;
    } else if (this.type == Type.PLANET_EXOTIC) {
      return PLANET_EXOTIC;
    } else if (this.type == Type.PLANET_GAS) {
      return PLANET_GAS;
    } else if (this.type == Type.PLANET_RINGED) {
      return PLANET_RINGED;
    } else if (this.type == Type.PLANET_ROCK) {
      return PLANET_ROCK;
    } else if (this.type == Type.ALIEN_MINER) {
      return ALIEN_MINER;
    } else if (this.type == Type.ALIEN_SHIP) {
      return ALIEN_SHIP;
    } else if (this.type == Type.ALIEN_BASE) {
      return ALIEN_BASE;
    } else {
      throw new RuntimeException("No image for this type of system: " + this.type);
    }
  }

  public void write(final WritableBuffer buffer) {
    write(buffer, this);
  }

  public void sortNeighbors() {
    if (this.neighbors != null) {
      boolean var2 = false;
      int neighborCount = this.neighbors.length;

      while (!var2) {
        --neighborCount;
        var2 = true;

        for (int i = 0; i < neighborCount; ++i) {
          if (this.neighbors[i].index > this.neighbors[i + 1].index) {
            final StarSystem var5 = this.neighbors[i];
            this.neighbors[i] = this.neighbors[i + 1];
            this.neighbors[i + 1] = var5;
            var2 = false;
          }
        }
      }
    }
  }

  public void removeNeighbor(final StarSystem other) {
    if (this.hasNeighbor(other)) {
      if (this.neighbors.length == 1) {
        this.neighbors = new StarSystem[0];
        other.removeNeighbor(this);
      } else {
        final StarSystem[] newNeighbors = new StarSystem[this.neighbors.length - 1];
        int i = 0;
        for (final StarSystem neighbor : this.neighbors) {
          if (other != neighbor) {
            newNeighbors[i++] = neighbor;
          }
        }
        this.neighbors = newNeighbors;
        other.removeNeighbor(this);
      }
    }
  }

  private void addNeighbor(final StarSystem neighbor) {
    if (this.neighbors == null) {
      this.neighbors = new StarSystem[]{neighbor};
    } else {
      final StarSystem[] neighbors = new StarSystem[this.neighbors.length + 1];
      System.arraycopy(this.neighbors, 0, neighbors, 0, this.neighbors.length);
      neighbors[this.neighbors.length] = neighbor;
      this.neighbors = neighbors;
    }
  }

  public void assignType(final Random random) {
    if (this.resources[0] < 0) {
      this.type = Type.ALIEN_MINER;
    } else if (this.resources[0] == 0 || this.resources[1] == 0 || this.resources[2] == 0 || this.resources[3] == 0) {
      final int[] scores = new int[6];
      scores[0] = (this.resources[0] + this.resources[3]) * 3;
      scores[1] = this.resources[1] * 4;
      scores[2] = this.resources[3] * 4;
      scores[3] = this.resources[2] * 4;
      scores[5] = this.resources[0] * 4;
      scores[4] = (this.resources[1] + this.resources[2] + this.resources[3]) * 2;

      final int totalScore = Arrays.stream(scores).sum();
      int roll = ShatteredPlansClient.randomIntBounded(random, totalScore);
      for (int i = 0; i < scores.length; ++i) {
        roll -= scores[i];
        if (roll < 0) {
          //noinspection MagicConstant
          this.type = i;
          return;
        }
      }

      throw new RuntimeException();
    } else {
      this.type = Type.PLANET_EARTH_LIKE;
    }
  }

  public boolean hasNeighbor(final StarSystem system) {
    if (this.neighbors != null) {
      return Arrays.stream(this.neighbors).anyMatch(Predicate.isEqual(system));
    }
    return false;
  }

  @Override
  public String toString() {
    return "StarSystem[" + this.index + ", " + this.name + "]";
  }

  @SuppressWarnings("WeakerAccess")
  public static final class Type {
    public static final int PLANET_BURNT = 0;
    public static final int PLANET_EARTH_LIKE = 1;
    public static final int PLANET_EXOTIC = 2;
    public static final int PLANET_GAS = 3;
    public static final int PLANET_RINGED = 4;
    public static final int PLANET_ROCK = 5;
    public static final int ALIEN_MINER = 6;
    public static final int ALIEN_SHIP = 7;
    public static final int ALIEN_BASE = 8;
  }

  public static final class Score {
    public static final int NORMAL = 0;
    public static final int TERRAFORMED = 1;
    public static final int NEUTRAL_HOMEWORLD = 2;
    public static final int PLAYER_HOMEWORLD = 3;
  }
}
