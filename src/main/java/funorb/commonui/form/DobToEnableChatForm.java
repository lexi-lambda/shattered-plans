package funorb.commonui.form;

import funorb.commonui.container.ArrayContainer;
import funorb.commonui.Button;
import funorb.commonui.Component;
import funorb.commonui.Resources;
import funorb.commonui.form.field.DateField;
import funorb.commonui.form.validator.DateOfBirthValidator;
import funorb.commonui.form.validator.ValidationState;
import funorb.commonui.listener.ButtonListener;
import funorb.graphics.Drawing;
import funorb.shatteredplans.C2SPacket;
import funorb.shatteredplans.StringConstants;
import funorb.shatteredplans.client.JagexApplet;

public final class DobToEnableChatForm extends ArrayContainer implements ButtonListener {
  public static boolean _vaj;
  public static DobToEnableChatForm instance;
  private final DateField _J;
  private final Button continueButton;

  public DobToEnableChatForm() {
    super();
    this._J = new DateField(_vaj);
    this._J.a890(new DateOfBirthValidator());
    this.continueButton = new Button(StringConstants.CONT, this);
    this.children = new Component[]{this._J, this.continueButton};
    this.d423();
  }

  private static void a115vf(final int var0, final int var1, final int var3) {
    JagexApplet.loginPacket.pos = 0;
    JagexApplet.loginPacket.writeByte(12);
    JagexApplet.loginPacket.writeInt(JagexApplet.cipherIVGen.nextInt());
    JagexApplet.loginPacket.writeInt(JagexApplet.cipherIVGen.nextInt());
    JagexApplet.loginPacket.writeByte(var3);
    JagexApplet.loginPacket.writeByte(var1);
    JagexApplet.loginPacket.writeShort(var0);
    JagexApplet.loginPacket.encryptRSA();
    C2SPacket.a115vf2();
  }

  @Override
  public void tick(final int x, final int y, final Component root) {
    super.tick(x, y, root);
    this.continueButton.enabled = this._J.getValidator().validate() == ValidationState.C2;
  }

  @Override
  public void h150() {
    final short var2 = 250;
    this._J.setBounds(-250 + this.width >> 1, this.height + 20 - 25 >> 1, 140, 25);
    this.continueButton.setBounds(140 + (this.width - var2 >> 1) + 10, (this.height - 10 >> 1) + 2, 100, 30);
  }

  @Override
  public void draw(final int x, final int y) {
    super.draw(x, y);
    Resources.AREZZO_12.draw(StringConstants.DOB_ENTER_FOR_CHAT, this.x + x + 4, this.y + y + Resources.AREZZO_12.ascent + 4, Drawing.WHITE);
  }

  @Override
  public void handleButtonClicked(final Button button) {
    if (this._J.getValidator().validate() == ValidationState.C2) {
      a115vf(this._J.l137(), this._J.f410(), this._J.d474());
    }
  }
}
