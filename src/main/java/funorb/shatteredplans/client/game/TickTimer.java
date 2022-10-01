package funorb.shatteredplans.client.game;

public final class TickTimer {
  public final int createdTick;
  public TickTimer next;

  public TickTimer(final int tick) {
    this.createdTick = tick;
    this.next = null;
  }

  public void addNext(final int tick) {
    TickTimer node = this;
    while (node.next != null) {
      node = node.next;
    }
    node.next = new TickTimer(tick);
  }
}
