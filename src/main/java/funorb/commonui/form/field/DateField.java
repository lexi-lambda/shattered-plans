package funorb.commonui.form.field;

import funorb.commonui.AbstractTextField;
import funorb.commonui.Component;
import funorb.commonui.Resources;
import funorb.commonui.form.validator.DateOfBirthValidator;
import funorb.graphics.Drawing;
import funorb.shatteredplans.StringConstants;

public final class DateField extends AbstractDateField {
  private final AbstractTextField _R;
  private final AbstractTextField _O;
  private final boolean _J;
  private final AbstractTextField _M;

  public DateField(final boolean var2) {
    super();
    this._O = new TextField("", null, 2);
    this._M = new TextField("", null, 2);
    this._R = new TextField("", null, 4);
    this._J = var2;
    if (this._J) {
      this.addChild(this._M);
      this.addChild(this._O);
    } else {
      this.addChild(this._O);
      this.addChild(this._M);
    }

    this.addChild(this._R);
    this.setBounds(0, 0, 140, 25);
  }

  private void a811(final int var1, final Component var3, final String var4, final int var5) {
    Resources.AREZZO_12.drawCentered(var4, var1 - (-var3.x - (var3.width >> 1)), var3.y + var5 - 5, Drawing.WHITE);
  }

  @Override
  public void setBounds(final int x, final int y, final int width, final int height) {
    super.setBounds(x, y, width, height);
    final int var6 = width - 130 >> 1;
    if (this._J) {
      assert this._M != null;
      this._M.setBounds(var6, 0, 25, height);
      this._O.setBounds(45 + var6, 0, 25, height);
    } else {
      this._O.setBounds(var6, 0, 25, height);
      this._M.setBounds(var6 + 45, 0, 25, height);
    }

    this._R.setBounds(90 + var6, 0, 40, height);
  }

  @Override
  public String getCurrentTooltip() {
    assert true;

    final String var2 = this._M.getCurrentTooltip();
    if (var2 != null) {
      return var2;
    } else if (this.isMouseOver) {
      return this.tooltip == null ? this.text : this.tooltip;
    } else {
      return null;
    }
  }

  @Override
  public boolean k154() {
    if (this._O.text == null || this._O.text.length() == 0) {
      return true;
    } else if (this._M.text == null || this._M.text.length() == 0) {
      return true;
    } else {
      return this._R.text == null || this._R.text.length() == 0;
    }
  }

  @Override
  public int l137() {
    try {
      return Integer.parseInt(this._R.text);
    } catch (final NumberFormatException var3) {
      return -1;
    }
  }

  @Override
  public int f410() {
    try {
      return Integer.parseInt(this._M.text) - 1;
    } catch (final NumberFormatException var4) {
      return -1;
    }
  }

  @Override
  public void a890(final DateOfBirthValidator var1) {
    super.a890(var1);

    this._O.listener = var1;
    assert this._M != null;
    this._M.listener = var1;
    this._R.listener = var1;
  }

  @Override
  public void draw(int x, int y) {
    super.draw(x, y);
    y += this.y;
    x += this.x;
    this.a811(x, this._O, StringConstants.DAY, y);
    this.a811(x, this._M, StringConstants.MONTH, y);
    this.a811(x, this._R, StringConstants.YEAR, y);
  }

  @Override
  public int d474() {
    try {
      return Integer.parseInt(this._O.text);
    } catch (final NumberFormatException var3) {
      return -1;
    }
  }
}
