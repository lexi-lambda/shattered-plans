package funorb.client.lobby;

public final class PlayerList extends Component<Component<?>> {
  public final Component<PlayerListEntry> entries;
  public final ScrollPane<?> scrollPane;
  public final Component<?> addButton;
  public final Component<?> removeButton;

  public PlayerList(final PlayerList other, final String addLabel, final String removeLabel) {
    this(other, other.scrollPane.viewport, other.scrollPane.scrollBar, other.addButton, addLabel, removeLabel);
  }

  public PlayerList(final Component<?> attrsSrc, final Component<?> viewportAttrsSrc, final ScrollBar scrollBar, final Component<?> buttonAttrsSrc, final String addLabel, final String removeLabel) {
    super(attrsSrc);
    this.entries = new Component<>(null);
    this.scrollPane = new ScrollPane<>(this.entries, viewportAttrsSrc, scrollBar);
    this.addButton = new Component<>(buttonAttrsSrc);
    this.removeButton = new Component<>(buttonAttrsSrc);
    this.addButton.label = addLabel;
    this.removeButton.label = removeLabel;
    this.addChild(this.scrollPane);
    this.addChild(this.addButton);
    this.addChild(this.removeButton);
  }

  @SuppressWarnings("SameParameterValue")
  public void updateBounds(final int x, final int y, final int width, final int height) {
    this.x = x;
    this.y = y;
    this.width = width;
    this.height = height;
    this.updateChildBounds();
  }

  public void updateChildBounds() {
    final int buttonWidth = (this.width + 2) / 2;
    final int paneHeight = this.height - 20;
    //noinspection SuspiciousNameCombination
    this.scrollPane.setBounds(0, 0, this.width, paneHeight - 2, LABEL_HEIGHT);
    this.addButton.height = 20;
    this.addButton.x = 0;
    this.addButton.width = buttonWidth - 2;
    this.addButton.y = paneHeight;
    this.removeButton.width = this.width - buttonWidth;
    this.removeButton.x = buttonWidth;
    this.removeButton.y = paneHeight;
    this.removeButton.height = 20;
  }
}
