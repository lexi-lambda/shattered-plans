package funorb.client.lobby;

import funorb.awt.KeyState;
import funorb.awt.MouseState;
import funorb.graphics.Drawing;
import funorb.graphics.Font;
import funorb.graphics.Sprite;
import funorb.shatteredplans.StringConstants;
import funorb.shatteredplans.client.JagexApplet;
import org.intellij.lang.annotations.MagicConstant;

public final class PopupMenu extends Component<Component<?>> {
  private final int _yb;
  private final Component<?> _Nb;
  private final Sprite[] _Lb;
  private final Component<?> _Mb;
  private final int _Eb;
  private final int _Kb;
  private final int _zb;
  private final int _Jb;
  private final ActionButton[] items;
  @MagicConstant(valuesFromClass = ContextMenu.ClickAction.class)
  private final int[] actions;
  private int _Bb;
  private int clickTargetCount;

  public PopupMenu(final PopupMenu attrsSrc) {
    this(attrsSrc, attrsSrc._Lb, attrsSrc._Nb, attrsSrc._Mb, attrsSrc._Kb, attrsSrc._Eb, attrsSrc._Jb, attrsSrc._yb, attrsSrc._zb);
  }

  public PopupMenu(final Component<?> attrsSrc, final Sprite[] var2, final Component<?> var3, final Component<?> var4, final int var5, final int var6, final int var7, final int var8, final int var9) {
    super(attrsSrc);
    this.actions = new int[256];
    this.items = new ActionButton[256];
    this._Bb = -2;
    this._Kb = var5;
    this._Lb = var2;
    this._zb = var9;
    this._Mb = var4;
    this._Jb = var7;
    this._Nb = var3;
    this._Eb = var6;
    this._yb = var8;
  }

  public static int positionPopupX(final int targetX, final int targetWidth, final int popupWidth) {
    if (targetX + popupWidth <= Drawing.width) {
      return targetX;
    } else if (targetX + targetWidth - popupWidth >= 0) {
      return targetX + targetWidth - popupWidth;
    } else {
      return Drawing.width - popupWidth;
    }
  }

  public static int positionPopupY(final int targetY, final int targetHeight, final int popupHeight) {
    if (popupHeight + targetHeight + targetY <= Drawing.height) {
      return targetY + targetHeight;
    } else if (targetY - popupHeight >= 0) {
      return targetY - popupHeight;
    } else {
      return Drawing.height - popupHeight;
    }
  }

  public boolean f427() {
    if (this._Bb == -2) {
      if (JagexApplet.lastTypedKeyCode == KeyState.Code.ESCAPE) {
        this._Bb = -1;
      }
      return true;
    } else {
      return false;
    }
  }

  public void addItem(final Sprite sprite, final String tooltip, @MagicConstant(valuesFromClass = ContextMenu.ClickAction.class) final int action) {
    this.items[this.clickTargetCount] = new ActionButton(null, this._Mb, sprite, tooltip);
    this.items[this.clickTargetCount].mouseOverNineSliceSprites = this._Lb;
    this.items[this.clickTargetCount]._r = true;
    this.items[this.clickTargetCount].verticalAlignment = Font.VerticalAlignment.MIDDLE;
    this.addChild(this.items[this.clickTargetCount]);
    this.actions[this.clickTargetCount] = action;
    ++this.clickTargetCount;
  }

  public void addItem(final String tooltip, @MagicConstant(valuesFromClass = ContextMenu.ClickAction.class) final int action) {
    this.addItem(null, tooltip, action);
  }

  public void positionRelativeToTarget(final int targetX, final int targetY) {
    this.positionRelativeToTarget(targetX, targetY, 0, 0);
  }

  public void positionRelativeToTarget(final int targetX, final int targetY, final int targetWidth, final int targetHeight) {
    if (this.clickTargetCount == 0) {
      this.items[this.clickTargetCount] = new ActionButton(null, this._Nb, null, StringConstants.NO_OPTIONS_AVAILABLE);
      this.items[this.clickTargetCount].verticalAlignment = Font.VerticalAlignment.MIDDLE;
      this.addChild(this.items[this.clickTargetCount]);
      this.actions[this.clickTargetCount] = -1;
      ++this.clickTargetCount;
    }

    int width = 0;
    for (int i = 0; i < this.clickTargetCount; ++i) {
      final int var8 = this.items[i].a776(this._Jb, this._Eb);
      if (width < var8) {
        width = var8;
      }
    }

    width += this._Kb * 2;
    final int height = this._yb + this._yb + this.clickTargetCount * this._zb;
    final int x = positionPopupX(targetX, targetWidth, width);
    final int y = positionPopupY(targetY, targetHeight, height);
    this.setBounds(x, y, width, height);

    for (int i = 0; i < this.clickTargetCount; ++i) {
      this.items[i].a370(this._zb, this._yb + i * this._zb, this._Eb, this._Jb, width - (2 * this._Kb), this._Kb);
    }
  }

  @MagicConstant(valuesFromClass = ContextMenu.ClickAction.class)
  public int getClickAction(final boolean mouseNotYetHandled) {
    this.rootProcessMouseEvents(mouseNotYetHandled);
    if (mouseNotYetHandled) {
      for (int i = 0; i < this.clickTargetCount; ++i) {
        if (this.items[i].clickButton != MouseState.Button.NONE) {
          //noinspection MagicConstant
          return this.actions[i];
        }
      }

      if (JagexApplet.mouseButtonJustClicked == MouseState.Button.NONE) {
        return this._Bb;
      } else {
        return ContextMenu.ClickAction.NONE;
      }
    } else {
      return ContextMenu.ClickAction.PASS;
    }
  }
}
