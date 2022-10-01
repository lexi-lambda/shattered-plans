package funorb.client.lobby;

import funorb.shatteredplans.StringConstants;

public final class TabbedPlayerListWrapper {
  public final TabbedPlayerList view;

  public TabbedPlayerListWrapper(final String firstTabLabel, final Component<?> var2) {
    final String[] labels = new String[]{firstTabLabel, StringConstants.MU_CHAT_FRIENDS, StringConstants.MU_CHAT_IGNORE};
    final Component<?>[] views = new Component[]{var2, Component.FRIEND_LIST_PANEL, Component.IGNORE_LIST};
    this.view = new TabbedPlayerList(Component.TAB_INACTIVE, labels, Component._tmt, views);
  }

  public void updateBounds(final int width, final int height) {
    this.view.updateBounds(width, height);
    Component.SERVER_INFO_LABEL.setBounds(0, 0, Component.FRIEND_LIST_PANEL.width, Component.LABEL_HEIGHT);
    Component.NAME_LABEL_2.setBounds(0, 2 + Component.LABEL_HEIGHT, -15 + (Component.FRIEND_LIST_PANEL.width - 2 - 82), 18);
    Component.LOCATION_LABEL.setBounds(-95 + (Component.FRIEND_LIST_PANEL.width - 2), 2 + Component.LABEL_HEIGHT, 97, 18);
    Component.FRIEND_LIST.updateBounds(0, 20 + Component.LABEL_HEIGHT + 2, Component.FRIEND_LIST_PANEL.width, Component.FRIEND_LIST_PANEL.height - Component.LABEL_HEIGHT - 22);
    Component.IGNORE_LIST.updateChildBounds();
  }
}
