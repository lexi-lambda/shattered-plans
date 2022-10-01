package funorb.client;

import funorb.awt.KeyState;
import funorb.awt.MouseState;
import funorb.shatteredplans.client.JagexApplet;

public final class MenuInputState {
  public int itemCount;
  public int selectedItem = 0;

  private boolean isMouseSelection = false;
  private int lastTypedKeyCode;
  private int mouseButtonDown;
  private int mouseButtonJustClicked;
  private int mouseButtonJustClickedOrRepeated;
  private int mouseClickRepeatCounter;

  public MenuInputState(final int itemCount) {
    this.itemCount = itemCount;
  }

  public void setSelectedItem(final int mouseItem, final int keyboardItem, final boolean isMouseSelection) {
    this.mouseButtonDown = 0;
    this.isMouseSelection = isMouseSelection;
    if (this.isMouseSelection) {
      this.selectedItem = mouseItem;
    } else {
      this.selectedItem = keyboardItem;
    }
  }

  public void processKeyInputVertical() {
    this.processKeyInput(KeyState.Code.UP, KeyState.Code.DOWN);
  }

  @SuppressWarnings("SameParameterValue")
  private void processKeyInput(final int prevCode, final int nextCode) {
    this.lastTypedKeyCode = 0;
    this.mouseButtonJustClickedOrRepeated = 0;
    this.mouseButtonJustClicked = 0;

    if (this.mouseButtonDown == 0) {
      this.lastTypedKeyCode = JagexApplet.lastTypedKeyCode;
      if (this.lastTypedKeyCode == prevCode) {
        this.isMouseSelection = false;
        if (this.selectedItem <= 0) {
          this.selectedItem = this.itemCount;
        }
        --this.selectedItem;
      } else if (this.lastTypedKeyCode == nextCode) {
        this.isMouseSelection = false;
        ++this.selectedItem;
        if (this.selectedItem >= this.itemCount) {
          this.selectedItem = 0;
        }
      }
    }
  }

  public boolean isHomeTyped() {
    return this.lastTypedKeyCode == KeyState.Code.HOME;
  }

  public boolean isEndTyped() {
    return this.lastTypedKeyCode == KeyState.Code.END;
  }

  public boolean isLeftTyped() {
    return this.lastTypedKeyCode == KeyState.Code.LEFT;
  }

  public boolean isRightTyped() {
    return this.lastTypedKeyCode == KeyState.Code.RIGHT;
  }

  public void processMouseInput(final int hoveredItem, final int clickedItem) {
    if (hoveredItem >= this.itemCount) {
      throw new IndexOutOfBoundsException("hovered item is not in range [0," + this.itemCount + "): " + hoveredItem);
    }
    if (clickedItem >= this.itemCount) {
      throw new IndexOutOfBoundsException("clicked item is not in range [0," + this.itemCount + "): " + clickedItem);
    }

    this.lastTypedKeyCode = 0;
    this.mouseButtonJustClickedOrRepeated = 0;
    this.mouseButtonJustClicked = 0;
    if (JagexApplet.mouseButtonJustClicked != MouseState.Button.NONE) {
      this.mouseClickRepeatCounter = 20;
      this.selectedItem = clickedItem;
      this.mouseButtonJustClicked = this.mouseButtonJustClickedOrRepeated = JagexApplet.mouseButtonJustClicked;
      this.mouseButtonDown = JagexApplet.mouseButtonJustClicked;
      this.isMouseSelection = true;
    }

    if (this.mouseButtonDown != 0 && JagexApplet.mouseButtonDown != MouseState.Button.NONE) {
      if (this.mouseClickRepeatCounter <= 0) {
        this.mouseClickRepeatCounter = 4;
        this.mouseButtonJustClickedOrRepeated = this.mouseButtonDown;
      }
      --this.mouseClickRepeatCounter;
    }

    if (JagexApplet.mouseButtonJustClicked == MouseState.Button.NONE
        && JagexApplet.mouseButtonDown == MouseState.Button.NONE) {
      this.mouseButtonDown = 0;
    }

    if (this.mouseButtonDown == 0 && (this.isMouseSelection || JagexApplet.mouseEventReceived)) {
      if (hoveredItem >= 0) {
        this.selectedItem = hoveredItem;
        this.isMouseSelection = true;
      } else if (this.isMouseSelection) {
        this.selectedItem = -1;
      }
    }
  }

  public boolean isMouseButtonDown() {
    return this.mouseButtonDown != 0;
  }

  public boolean isItemActive() {
    return this.mouseButtonJustClicked != 0 || this.lastTypedKeyCode == KeyState.Code.ENTER || this.lastTypedKeyCode == KeyState.Code.SPACE;
  }

  public boolean isItemActiveAllowRepeat() {
    return this.mouseButtonJustClickedOrRepeated != 0 || this.lastTypedKeyCode == KeyState.Code.ENTER || this.lastTypedKeyCode == KeyState.Code.SPACE;
  }
}
