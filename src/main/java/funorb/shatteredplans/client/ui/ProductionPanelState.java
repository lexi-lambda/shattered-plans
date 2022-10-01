package funorb.shatteredplans.client.ui;

import funorb.Strings;
import funorb.shatteredplans.StringConstants;
import funorb.shatteredplans.game.Force;

import java.util.List;

public final class ProductionPanelState implements PanelState {
  public List<ScrollView<Force>> _f;
  public List<Button<Force>> buildFleetsButtons;
  public List<Label> buildFleetsLabels;
  public ScrollBar scrollBar;

  public void deactivateFleetPlacement() {
    for (int i = 0; i < this.buildFleetsButtons.size(); ++i) {
      this.updateFleetPlacement(i, null);
    }
  }

  public void activateFleetPlacement(final Force force) {
    for (int i = 0; i < this.buildFleetsButtons.size(); ++i) {
      this.updateFleetPlacement(i, force);
    }
  }

  private void updateFleetPlacement(final int forceIndex, final Force activeForce) {
    final Button<Force> buildButton = this.buildFleetsButtons.get(forceIndex);
    final Label buildLabel = this.buildFleetsLabels.get(forceIndex);
    final Force force = buildButton.data;
    final boolean isActive = force == activeForce;
    if (isActive != buildButton.isActive()) {
      buildButton.toggle();
      buildButton.tooltip = buildLabel.tooltip = isActive
          ? buildingFleetsTooltip(force.fleetsAvailableToBuild)
          : remainingFleetsTooltip(force.fleetsAvailableToBuild);
    }
  }

  public static String buildingFleetsTooltip(final int fleetsAvailableToBuild) {
    final String remainingMessage = remainingFleetsTooltip(fleetsAvailableToBuild);
    if (fleetsAvailableToBuild == 0) {
      return remainingMessage;
    } else {
      return StringConstants.TOOLTIP_PLACE_FLEET_STOP + " " + remainingMessage;
    }
  }

  private static String remainingFleetsTooltip(final int fleetsAvailableToBuild) {
    if (fleetsAvailableToBuild == 0) {
      return StringConstants.TOOLTIP_ALL_FLEETS_PLACED;
    }

    final String remainingMessage;
    if (fleetsAvailableToBuild == 1) {
      remainingMessage = StringConstants.TOOLTIP_ONE_FLEET_REMAINING;
    } else {
      remainingMessage = Strings.format(StringConstants.TOOLTIP_FLEETS_REMAINING, Integer.toString(fleetsAvailableToBuild));
    }
    return remainingMessage;
  }
}
