package funorb.client;

import funorb.io.Buffer;
import funorb.shatteredplans.client.AuthMode;
import org.jetbrains.annotations.Contract;

public abstract class LoginCredentials {
  @SuppressWarnings("SameParameterValue")
  public abstract void writeToPacket(Buffer packet);

  @Contract(pure = true)
  public abstract AuthMode getAuthMode();
}
