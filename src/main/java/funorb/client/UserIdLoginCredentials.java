package funorb.client;

import funorb.io.Buffer;
import funorb.shatteredplans.client.AuthMode;

public final class UserIdLoginCredentials extends LoginCredentials {
  private static final long USER_ID_LIMIT = 6582952005840035281L;
  private static final AuthMode userIdAuthMode = new AuthMode(AuthMode.USER_ID);

  private final long userId;
  private final String password;

  public UserIdLoginCredentials(final long userId, final String password) {
    this.userId = userId;
    this.password = password;
  }

  public static long encodeUsername(final CharSequence username) {
    long userId = 0L;
    for (int i = 0; i < username.length(); ++i) {
      userId *= 37L;
      final char c = username.charAt(i);
      if (c >= 'A' && c <= 'Z') {
        userId += 1 - (65 - c);
      } else if (c >= 'a' && c <= 'z') {
        userId += 1 + c - 97;
      } else if (c >= '0' && c <= '9') {
        userId += c + 27 - 48;
      }

      if (userId >= USER_ID_LIMIT / 37L) {
        break;
      }
    }

    while (userId != 0L && userId % 37L == 0L) {
      userId /= 37L;
    }

    return userId;
  }

  public static String decodeUsername(long userId) {
    assert userId >= 0 && userId < USER_ID_LIMIT;
    final StringBuilder sb = new StringBuilder(12);
    while (userId != 0) {
      final int x = (int) (userId % 37);
      if (x == 0) {
        sb.append(' ');
      } else if (x < 27) {
        if (userId / 37 % 37 == 0) {
          sb.append((char) ('A' + (x - 1)));
        } else {
          sb.append((char) ('a' + (x - 1)));
        }
      } else {
        sb.append((char) ('0' + (x - 27)));
      }
      userId /= 37;
    }
    return sb.reverse().toString();
  }

  @Override
  public AuthMode getAuthMode() {
    return userIdAuthMode;
  }

  @Override
  public void writeToPacket(final Buffer packet) {
    packet.writeLong(this.userId);
    packet.writePasswordHash(this.password);
  }
}
