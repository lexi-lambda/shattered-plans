package funorb.commonui.form;

import funorb.commonui.Button;
import funorb.commonui.CommonUI;
import funorb.commonui.container.ListContainer;
import funorb.commonui.frame.AccountFrame;
import funorb.commonui.listener.ButtonListener;
import funorb.commonui.listener.ComponentListener;
import funorb.graphics.Drawing;
import funorb.graphics.Font;
import org.jetbrains.annotations.Nullable;

public final class DialogForm extends ListContainer implements ButtonListener {
  private final AccountFrame accountFrame;
  private final Font font;
  private final String statusMessage;
  private int buttonCount = 0;
  private Button[] buttons;
  private @Nullable CommonUI.TickResult[] buttonActions;

  public DialogForm(final AccountFrame accountFrame, final Font font, final String statusMessage) {
    super(0, 0, 288, 0);
    this.font = font;
    this.accountFrame = accountFrame;
    this.statusMessage = statusMessage;
    final int statusMessageHeight = this.statusMessage != null ? this.font.measureParagraphHeight(this.statusMessage, 260, this.font.ascent) : 0;
    this.setBounds(0, 0, 288, 22 + statusMessageHeight);
  }

  public Button addButton(final String label, final ComponentListener listener) {
    final Button button = new Button(label, listener);
    this.setBounds(0, 0, this.width, this.height + 34);
    button.setBounds(7, this.height - 2, this.width - 14, 30);
    this.addChild(button);
    return button;
  }

  public void addActionButton(final String label, final @Nullable CommonUI.TickResult action) {
    final int newButtonCount = this.buttonCount + 1;
    final Button[] buttons = new Button[newButtonCount];
    final CommonUI.TickResult[] buttonActions = new CommonUI.TickResult[newButtonCount];
    if (this.buttonCount > 0) {
      System.arraycopy(this.buttons, 0, buttons, 0, this.buttonCount);
      System.arraycopy(this.buttonActions, 0, buttonActions, 0, this.buttonCount);
    }

    this.buttonCount = newButtonCount;
    this.buttons = buttons;
    this.buttonActions = buttonActions;

    this.buttons[this.buttonCount - 1] = this.addButton(label, this);
    this.buttonActions[this.buttonCount - 1] = action;
  }

  @Override
  public void draw(final int x, final int y) {
    super.draw(x, y);
    this.font.drawParagraph(this.statusMessage, x + this.x + 14, 10 + this.y + y, this.width - 28, this.height, Drawing.WHITE, Font.HorizontalAlignment.LEFT, Font.VerticalAlignment.TOP, this.font.ascent);
  }

  @Override
  public void handleButtonClicked(final Button button) {
    for (int i = 0; i < this.buttonCount; ++i) {
      if (button == this.buttons[i]) {
        final CommonUI.TickResult action = this.buttonActions[i];
        if (action == null) {
          this.accountFrame.i423();
        } else {
          CommonUI.nextTickResult = this.buttonActions[i];
        }
        break;
      }
    }
  }
}
