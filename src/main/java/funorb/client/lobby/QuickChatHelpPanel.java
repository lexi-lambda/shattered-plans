package funorb.client.lobby;

import funorb.awt.MouseState;
import funorb.graphics.Font;
import funorb.shatteredplans.StringConstants;
import funorb.shatteredplans.client.JagexApplet;
import funorb.shatteredplans.client.ShatteredPlansClient;

public final class QuickChatHelpPanel extends Component<Component<?>> {
  public static QuickChatHelpPanel openInstance;
  private final Component<?> closeButton;

  public QuickChatHelpPanel(final Component<?> var2, final Component<?> var3, final Component<?> var4) {
    final Component<Component<?>> var7 = new Component<>(var2, StringConstants.QUICK_CHAT_HELP.toUpperCase());
    var7.textAlignment = Font.HorizontalAlignment.CENTER;
    this.closeButton = new Component<>(var3);
    final Component<Component<?>> var8 = new Component<>(var4);
    final Component<Component<?>> var9 = new Component<>(var4, StringConstants.QUICK_CHAT_HELP_TITLE);
    var9.textAlignment = Font.HorizontalAlignment.CENTER;
    int var10 = 50;
    int var11 = 0;

    for (int var12 = 0; var12 < StringConstants.QUICK_CHAT_SHORTCUT_HELP.length; ++var12) {
      final Component<?> var13 = new Component<>(var4, StringConstants.QUICK_CHAT_SHORTCUT_KEYS[var12]);
      final Component<?> var14 = new Component<>(var4, StringConstants.QUICK_CHAT_SHORTCUT_HELP[var12]);
      final int var15 = var4.font.measureLineWidth(StringConstants.QUICK_CHAT_SHORTCUT_HELP[var12]);
      var13.setBounds(20, var10, 65, 15);
      if (var15 > var11) {
        var11 = var15;
      }

      var14.setBounds(90, var10, ShatteredPlansClient.SCREEN_WIDTH, 15);
      var8.addChild(var13);
      var8.addChild(var14);
      var10 += 30;
    }

    var10 += 15;
    var7.setBounds(0, 0, 20 + var11 + 90, 24);
    this.setBounds(100, 100, var7.width, var10 + var7.height);
    this.closeButton.setBounds(var7.width - 20, 5, 15, 15);
    var8.setBounds(0, var7.height, this.width, -var7.height + this.height);
    var9.setBounds(0, 20, this.width, 15);
    var8.nineSliceSprites = Component.createGradientOutlineSprites(var8.height, 11579568, 8421504, 2105376);
    var7.addChild(this.closeButton);
    var8.addChild(var9);
    this.addChild(var7);
    this.addChild(var8);
    this.x = 320 - (this.width >> 1);
  }

  public static void tick() {
    if (openInstance != null && openInstance.isCloseRequested()) {
      openInstance = null;
    }
  }

  private boolean isCloseRequested() {
    this.rootProcessMouseEvents(true);
    return (JagexApplet.mouseButtonJustClicked != MouseState.Button.NONE && this.clickButton == MouseState.Button.NONE)
        || this.closeButton.clickButton != MouseState.Button.NONE;
  }
}
