package funorb.shatteredplans.game;

import funorb.graphics.Sprite;
import funorb.io.ReadableBuffer;
import funorb.io.WritableBuffer;
import funorb.shatteredplans.client.game.PlayerStats;

import java.util.ArrayList;
import java.util.List;

public final class Player {
  public final int index;
  public String name;
  public final int color1;
  public final int color2;
  public final int darkColor;
  public CombinedForce combinedForce;
  public final List<ContiguousForce> contiguousForces = new ArrayList<>();
  public final int[] researchPoints = new int[4];
  public int incomingPactOffersBitmap;
  public int outgoingPactOffersBitmap;
  public int[] pactTurnsRemaining;
  public boolean[] allies;
  public PlayerStats stats;
  public Sprite _c;
  public Sprite _e;
  public Sprite _b;
  public Sprite _r;
  public Sprite _p;
  public Sprite _v;
  public Sprite _o;
  public Sprite _n;
  public Sprite _d;

  public Player(final int index, final String name, final int darkColor, final int color1, final int color2) {
    this.index = index;
    this.name = name;
    this.color1 = color1;
    this.color2 = color2;
    this.darkColor = darkColor;
  }

  public static void establishPact(final Player offerer, final Player offeree) {
    offerer.pactTurnsRemaining[offeree.index] = 3;
    offerer.allies[offeree.index] = true;
    offeree.pactTurnsRemaining[offerer.index] = 3;
    offeree.allies[offerer.index] = true;
    offerer.incomingPactOffersBitmap &= ~(1 << offeree.index);
    offerer.outgoingPactOffersBitmap &= ~(1 << offeree.index);
    offeree.incomingPactOffersBitmap &= ~(1 << offerer.index);
    offeree.outgoingPactOffersBitmap &= ~(1 << offerer.index);
  }

  public static void offerPact(final Player offerer, final Player offeree) {
    offerer.outgoingPactOffersBitmap |= 1 << offeree.index;
    offeree.incomingPactOffersBitmap |= 1 << offerer.index;
  }

  public boolean hasPactOfferFrom(final Player other) {
    return (this.incomingPactOffersBitmap & (1 << other.index)) != 0;
  }

  @SuppressWarnings("BooleanMethodIsAlwaysInverted")
  public boolean isOfferingPactTo(final Player other) {
    return (this.outgoingPactOffersBitmap & (1 << other.index)) != 0;
  }

  public static Player read(final ReadableBuffer packet, final Player[] players) {
    final int index = packet.readUByte();
    return (index >= 0 && index < players.length) ? players[index] : null;
  }

  public static void write(final WritableBuffer buffer, final Player player) {
    buffer.writeByte(player != null ? player.index : -1);
  }

  public void write(final WritableBuffer buffer) {
    write(buffer, this);
  }

  @Override
  public String toString() {
    return "Player[" + this.index + ", " + this.name + "]";
  }
}
