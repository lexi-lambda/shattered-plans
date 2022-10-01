package funorb.commonui.form;

import funorb.awt.KeyState;
import funorb.commonui.Button;
import funorb.commonui.Component;
import funorb.commonui.Resources;
import funorb.commonui.container.ListContainer;
import funorb.commonui.listener.ButtonListener;
import funorb.commonui.renderer.LinkRenderer;
import funorb.graphics.Drawing;
import funorb.graphics.Font;
import funorb.shatteredplans.StringConstants;
import funorb.shatteredplans.client.ShatteredPlansClient;

public final class CreateFormListener extends ListContainer implements ButtonListener {
  private final CreateForm _F;
  private Button[] _I;
  private String[] _G;

  public CreateFormListener(final CreateForm var1) {
    super(0, 0, 0, 0);
    this._F = var1;
  }

  @Override
  public void draw(final int x, final int y) {
    super.draw(x, y);
    final Font var5 = Resources.AREZZO_14;
    if (this._G != null) {
      var5.drawParagraph(StringConstants.CREATE_SUGGESTIONS, x + this.x, this.y + y, this.width, 20, Drawing.WHITE, Font.HorizontalAlignment.LEFT, Font.VerticalAlignment.TOP, var5.descent + var5.ascent);
    }
  }

  @Override
  public void handleButtonClicked(final Button button) {
    for (int i = 0; i < this._G.length; ++i) {
      if (button == this._I[i]) {
        this._F.a984(this._G[i]);
      }
    }
    if (button == this._I[this._G.length]) {
      this._F.a150();
    }
  }

  public void a449(final String[] var1) {
    this.children.clear();
    if (var1 == null || var1.length == 0) {
      this._G = null;
    } else {
      final int var3 = var1.length;
      this._G = new String[var3];

      for (int var4 = 0; var3 > var4; ++var4) {
        this._G[var4] = ShatteredPlansClient.a034ih(var1[var4]).replace(' ', ' ');
      }

      final LinkRenderer var6 = new LinkRenderer(Resources.AREZZO_14);
      this._I = new Button[var3 + 1];

      for (int var5 = 0; var3 > var5; ++var5) {
        this._I[var5] = new Button(this._G[var5], var6, this);
        this._I[var5].tooltip = StringConstants.CREATE_SELECT_ALTERNATIVE;
        this._I[var5].setBounds(0, 20 + var5 * 16, 80, 15);
        this.addChild(this._I[var5]);
      }

      this._I[var3] = new Button(StringConstants.CREATE_MORE_SUGGESTIONS, var6, this);
      this._I[var3].setBounds(0, 16 + var3 * 16 + 20, 100, 15);
      this.addChild(this._I[var3]);
    }
  }

  @Override
  public boolean a686(final int keyCode, final char keyChar, final Component var4) {
    if (super.a686(keyCode, keyChar, var4)) {
      return true;
    } else if (keyCode == KeyState.Code.UP) {
      return this.a611(var4);
    } else {
      return keyCode == KeyState.Code.DOWN && this.a948(var4);
    }
  }
}
