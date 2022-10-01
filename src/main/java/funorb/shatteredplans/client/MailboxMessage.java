package funorb.shatteredplans.client;

import funorb.client.JagexBaseApplet;
import org.intellij.lang.annotations.MagicConstant;

public final class MailboxMessage {
  @MagicConstant(valuesFromClass = Status.class)
  public volatile int status = 0;
  public int ipayload1;
  public volatile Object response;
  public MailboxMessage next;
  @MagicConstant(valuesFromClass = Type.class)
  public
  int type;
  public Object opayload;
  public int ipayload2;

  /**
   * Busy-waits until {@link #status} is no longer {@link Status#PENDING}.
   *
   * @return the final status
   */
  @MagicConstant(intValues = {Status.SUCCESS, Status.FAILURE})
  public int busyAwait() {
    while (this.status == Status.PENDING) {
      JagexBaseApplet.maybeSleep(10L);
    }
    //noinspection MagicConstant
    return this.status;
  }

  public static final class Status {
    public static final int PENDING = 0;
    public static final int SUCCESS = 1;
    public static final int FAILURE = 2;
  }

  public static final class Type {
    public static final int OPEN_SOCKET = 1;
    public static final int SPAWN_THREAD = 2;
    public static final int DNS_REVERSE_LOOKUP = 3;
    public static final int OPEN_URL_STREAM = 4;
    public static final int LIST_DISPLAY_MODES = 5;
    public static final int ENTER_FULL_SCREEN = 6;
    public static final int EXIT_FULL_SCREEN = 7;
    public static final int GET_DECLARED_METHOD = 8;
    public static final int GET_DECLARED_FIELD = 9;
    public static final int OPEN_GAME_PREFERENCES_CACHE = 12;
    public static final int OPEN_GLOBAL_PREFERENCES_CACHE = 13;
    public static final int MOVE_MOUSE = 14;
    public static final int SHOW_CURSOR = 15;
    public static final int WINDOWS_START = 16;
    public static final int SET_CUSTOM_CURSOR = 17;
    public static final int GET_CLIPBOARD = 18;
    public static final int SET_CLIPBOARD = 19;
    public static final int DNS_LOOKUP = 21;
    public static final int A287 = 22;
  }
}
