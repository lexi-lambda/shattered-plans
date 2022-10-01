package funorb.shatteredplans.client.ui;

import funorb.shatteredplans.game.GameState;

public final class ProjectsPanelState implements PanelState {
  public final Label[] statusLabels = new Label[4];
  public final ScrollView<?>[] _c = new ScrollView[GameState.NUM_RESOURCES];
}
