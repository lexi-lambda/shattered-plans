package funorb.client;

import funorb.client.LoginCredentials;
import funorb.io.Buffer;
import funorb.shatteredplans.client.AuthMode;
import funorb.shatteredplans.client.JagexApplet;

public final class EmailLoginCredentials extends LoginCredentials {
  private static final AuthMode emailAuthMode = new AuthMode(AuthMode.EMAIL);
  private final String email;
  private final String password;

  public EmailLoginCredentials(final String email, final String password) {
    this.email = email;
    this.password = password;
  }

  public static boolean containsPassword(final String message) {
    if (JagexApplet.lastLoginPassword == null) {
      return false;
    } else {
      return message.toLowerCase().contains(JagexApplet.lastLoginPassword.toLowerCase());
    }
  }

  @Override
  public void writeToPacket(final Buffer packet) {
    JagexApplet.loginPacket.writeNullBracketedString(this.email);
    JagexApplet.loginPacket.writePasswordHash(this.password);
  }

  @Override
  public AuthMode getAuthMode() {
    return emailAuthMode;
  }
}
