package funorb.client.lobby;

import funorb.graphics.Font;
import funorb.graphics.Sprite;

public final class Checkbox extends Component<Component<?>> {
  private final Component<?> _Ab;
  private final Component<?> _xb;

  private Checkbox(final Component<?> var3, final Component<?> var4, final String var5) {
    super(null);
    this._xb = new Component<>(var3);
    this._Ab = new Component<>(var4);
    this._Ab.label = var5;
    this.addChild(this._xb);
    this.addChild(this._Ab);
    this.recursivelySet_H();
  }

  public Checkbox(final Checkbox var3, final String var4) {
    this(var3._xb, var3._Ab, var4);
  }

  public Checkbox(final Sprite var3, final Sprite var4, final Component<?> var6) {
    this(null, var6, null);
    this._xb.sprite = var4;
    this._xb.verticalAlignment = Font.VerticalAlignment.MIDDLE;
    this._xb._Z = var3;
  }

  private void b366() {
    this._xb.setBounds(0, 0, this._xb.e474(), this.height);
    final int var3 = this._xb.width + 4;
    this._Ab.setBounds(var3, 0, -var3 + this.width, this.height);
  }

  public int c080() {
    return this._xb.e474() + 4 + this._Ab.e474();
  }

  public void a669(final int var1, final int var2, final int var6) {
    this.setBounds(var2, var6, var1, Component.LABEL_HEIGHT);
    this.b366();
  }
}
