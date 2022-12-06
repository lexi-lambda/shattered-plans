package funorb.shatteredplans.client.ui;

import funorb.shatteredplans.game.Player;

public final class DiplomacyPanelState implements PanelState {
  public final ScrollView<Player>[] _h;
  public final TextButton<?>[] _i;
  public final Icon[][] _f;

  @SuppressWarnings("unchecked")
  public DiplomacyPanelState(final int var1) {
    this._h = new ScrollView[var1];
    this._f = new Icon[var1][var1 - 1];
    this._i = new TextButton[var1 - 1];
  }
}
