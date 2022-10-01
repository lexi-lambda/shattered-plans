package funorb.shatteredplans.client;

import funorb.awt.GraphicsBackend;
import funorb.awt.MouseControlBackend;
import funorb.cache.CacheFile;
import funorb.client.JagexBaseApplet;
import funorb.shatteredplans.CacheFiles;
import funorb.client.pa_;
import funorb.util.PseudoMonotonicClock;
import org.intellij.lang.annotations.MagicConstant;

import java.awt.Component;
import java.awt.EventQueue;
import java.awt.Frame;
import java.awt.Toolkit;
import java.awt.datatransfer.Transferable;
import java.awt.event.ActionEvent;
import java.io.DataInputStream;
import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.URL;

public final class MessagePumpThread implements Runnable {
  public static final String GAME_ID = "shatteredplans";
  public static final String JAVA_VENDOR = System.getProperty("java.vendor");
  public static final String JAVA_VERSION = System.getProperty("java.version");
  private static final String OS_NAME = System.getProperty("os.name");
  private static final String OS_NAME_LOWER_CASE = OS_NAME.toLowerCase();
  private static final String USER_HOME;
  private static final String[] PREFERENCE_FILE_SEARCH_DIRS;
  public static MessagePumpThread instance;

  static {
    final String userHome = System.getProperty("user.home");
    USER_HOME = userHome == null ? "~/" : userHome + "/";
    PREFERENCE_FILE_SEARCH_DIRS = new String[]{"c:/rscache/", "/rscache/", USER_HOME, "c:/windows/", "c:/winnt/", "c:/", "/tmp/", ""};
  }

  private final Thread thread;
  private final GraphicsBackend graphicsBackend = new GraphicsBackend();
  private final MouseControlBackend mouseControlBackend = new MouseControlBackend();
  private EventQueue eventQueue;
  public CacheFiles cacheFiles;
  private boolean isShutdownRequested = false;
  private MailboxMessage mailboxHead = null;
  private MailboxMessage mailboxTail = null;

  public MessagePumpThread() throws Exception {
    try {
      this.eventQueue = Toolkit.getDefaultToolkit().getSystemEventQueue();
    } catch (final Throwable var11) {
    }

    this.cacheFiles = new CacheFiles();

    this.thread = new Thread(this);
    this.thread.setPriority(10);
    this.thread.setDaemon(true);
    this.thread.start();
  }

  private static CacheFile openPreferencesCache(final String name, final String suffix) {
    final String filename = "jagex_" + name + "_preferences" + suffix + ".dat";
    for (final String dir : PREFERENCE_FILE_SEARCH_DIRS) {
      if (dir.length() == 0 || (new File(dir)).exists()) {
        try {
          return new CacheFile(new File(dir, filename), 10000L);
        } catch (final Exception var8) {
        }
      }
    }
    return null;
  }

  public void postEvent(final Object source) {
    if (this.eventQueue != null) {
      for (int i = 0; i < 50 && this.eventQueue.peekEvent() != null; ++i) {
        JagexBaseApplet.maybeSleep(1L);
      }

      try {
        if (source != null) {
          this.eventQueue.postEvent(new ActionEvent(source, ActionEvent.ACTION_FIRST, "dummy"));
        }
      } catch (final Exception e) {}
    }
  }

  public MailboxMessage sendGetDeclaredMethodMessage(final Class<?> targetClass, final String methodName, final Class<?>[] paramType) {
    return this.sendMessage(MailboxMessage.Type.GET_DECLARED_METHOD, new Object[]{targetClass, methodName, paramType}, 0, 0);
  }

  public MailboxMessage sendSpawnThreadMessage(final Runnable target, final int priority) {
    return this.sendMessage(MailboxMessage.Type.SPAWN_THREAD, target, priority, 0);
  }

  public MailboxMessage sendOpenSocketMessage(final String var3, final int var2) {
    return this.sendMessage(MailboxMessage.Type.OPEN_SOCKET, var3, var2, 0);
  }

  public MailboxMessage sendEnterFullScreenMessage(final int bitDepth) {
    return this.sendMessage(MailboxMessage.Type.ENTER_FULL_SCREEN, null, 41943520, (bitDepth << 16));
  }

  public MailboxMessage sendOpenUrlStreamMessage(final URL var1) {
    return this.sendMessage(MailboxMessage.Type.OPEN_URL_STREAM, var1, 0, 0);
  }

  private MailboxMessage sendMessage(
      @MagicConstant(valuesFromClass = MailboxMessage.Type.class) final int type,
      final Object opayload,
      final int ipayload1,
      final int ipayload2) {
    final MailboxMessage msg = new MailboxMessage();
    msg.type = type;
    msg.opayload = opayload;
    msg.ipayload1 = ipayload1;
    msg.ipayload2 = ipayload2;
    synchronized (this) {
      if (this.mailboxTail == null) {
        this.mailboxTail = this.mailboxHead = msg;
      } else {
        this.mailboxTail.next = msg;
        this.mailboxTail = msg;
      }

      this.notify();
      return msg;
    }
  }

  private MailboxMessage receiveMessage() {
    final MailboxMessage msg;
    synchronized (this) {
      while (true) {
        if (this.isShutdownRequested) {
          return null;
        }

        if (this.mailboxHead != null) {
          msg = this.mailboxHead;
          this.mailboxHead = this.mailboxHead.next;
          if (this.mailboxHead == null) {
            this.mailboxTail = null;
          }
          break;
        }

        try {
          this.wait();
        } catch (final InterruptedException ignored) {
        }
      }
    }
    return msg;
  }

  @Override
  public void run() {
    while (true) {
      final MailboxMessage msg = this.receiveMessage();
      if (msg == null) return; // shutdown requested

      try {
        switch (msg.type) {
          case MailboxMessage.Type.OPEN_SOCKET -> {
            if (PseudoMonotonicClock.currentTimeMillis() < 0L) {
              throw new IOException();
            }
            msg.response = new Socket(InetAddress.getByName((String) msg.opayload), msg.ipayload1);
          }
          case MailboxMessage.Type.SPAWN_THREAD -> {
            final Thread var24 = new Thread((Runnable) msg.opayload);
            var24.setDaemon(true);
            var24.start();
            var24.setPriority(msg.ipayload1);
            msg.response = var24;
          }
          case MailboxMessage.Type.DNS_REVERSE_LOOKUP -> {
            if (PseudoMonotonicClock.currentTimeMillis() < 0L) {
              throw new IOException();
            }

            final String var14 = (255 & msg.ipayload1 >> 24) + "." + ((16756253 & msg.ipayload1) >> 16) + "." + ((msg.ipayload1 & '＇') >> 8) + "." + (255 & msg.ipayload1);
            msg.response = InetAddress.getByName(var14).getHostName();
          }
          case MailboxMessage.Type.OPEN_URL_STREAM -> {
            if (PseudoMonotonicClock.currentTimeMillis() < 0L) {
              throw new IOException();
            }
            msg.response = new DataInputStream(((URL) msg.opayload).openStream());
          }
          case MailboxMessage.Type.LIST_DISPLAY_MODES -> msg.response = this.graphicsBackend.listmodes();
          case MailboxMessage.Type.ENTER_FULL_SCREEN -> {
            final Frame frame = new Frame("Jagex Full Screen");
            msg.response = frame;
            frame.setResizable(false);
            this.graphicsBackend.enter(frame, msg.ipayload1 >>> 16, msg.ipayload1 & 0xffff, msg.ipayload2 >> 16, msg.ipayload2 & 0xffff);
          }
          case MailboxMessage.Type.EXIT_FULL_SCREEN -> this.graphicsBackend.exit();
          case MailboxMessage.Type.GET_DECLARED_METHOD -> {
            final Object[] var15 = (Object[]) msg.opayload;
            if (((Class<?>) var15[0]).getClassLoader() == null) {
              throw new SecurityException();
            }

            msg.response = ((Class<?>) var15[0]).getDeclaredMethod((String) var15[1], (Class<?>[]) var15[2]);
          }
          case MailboxMessage.Type.GET_DECLARED_FIELD -> {
            final Object[] var15 = (Object[]) msg.opayload;
            if (((Class<?>) var15[0]).getClassLoader() == null) {
              throw new SecurityException();
            }

            msg.response = ((Class<?>) var15[0]).getDeclaredField((String) var15[1]);
          }
          case MailboxMessage.Type.OPEN_GAME_PREFERENCES_CACHE ->
              msg.response = openPreferencesCache(GAME_ID, (String) msg.opayload);
          case MailboxMessage.Type.OPEN_GLOBAL_PREFERENCES_CACHE ->
              msg.response = openPreferencesCache("", (String) msg.opayload);
          case MailboxMessage.Type.MOVE_MOUSE -> this.mouseControlBackend.movemouse(msg.ipayload1, msg.ipayload2);
          case MailboxMessage.Type.SHOW_CURSOR ->
              this.mouseControlBackend.showcursor((Component) msg.opayload, msg.ipayload1 != 0);
          case MailboxMessage.Type.WINDOWS_START -> {
            try {
              if (!OS_NAME_LOWER_CASE.startsWith("win")) {
                throw new Exception();
              }

              final String var14 = (String) msg.opayload;
              if (!var14.startsWith("http://") && !var14.startsWith("https://")) {
                throw new Exception();
              }

              final String var17 = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789?&=,.%+-_#:/*";

              for (int var5 = 0; var5 < var14.length(); ++var5) {
                if (var17.indexOf(var14.charAt(var5)) == -1) {
                  throw new Exception();
                }
              }

              Runtime.getRuntime().exec("cmd /c start \"j\" \"" + var14 + "\"");
              msg.response = null;
            } catch (final Exception var10) {
              msg.response = var10;
              throw var10;
            }
          }
          case MailboxMessage.Type.SET_CUSTOM_CURSOR -> {
            final Object[] payload = (Object[]) msg.opayload;
            this.mouseControlBackend.setcustomcursor((Component) payload[0], (int[]) payload[1], msg.ipayload1, msg.ipayload2, (java.awt.Point) payload[2]);
          }
          case MailboxMessage.Type.GET_CLIPBOARD ->
              msg.response = Toolkit.getDefaultToolkit().getSystemClipboard().getContents(null);
          case MailboxMessage.Type.SET_CLIPBOARD ->
              Toolkit.getDefaultToolkit().getSystemClipboard().setContents((Transferable) msg.opayload, null);
          case MailboxMessage.Type.DNS_LOOKUP -> {
            if (PseudoMonotonicClock.currentTimeMillis() < 0L) {
              throw new IOException();
            }
            msg.response = InetAddress.getByName((String) msg.opayload).getAddress();
          }
          case MailboxMessage.Type.A287 -> {
            if (PseudoMonotonicClock.currentTimeMillis() < 0L) {
              throw new IOException();
            }
            msg.response = pa_.a287ho(msg.ipayload1, (String) msg.opayload).b693();
          }
          default -> throw new Exception("unknown message type");
        }

        msg.status = MailboxMessage.Status.SUCCESS;
      } catch (final ThreadDeath var11) {
        throw var11;
      } catch (final Throwable var12) {
        msg.status = MailboxMessage.Status.FAILURE;
      }

      //noinspection SynchronizationOnLocalVariableOrMethodParameter
      synchronized (msg) {
        msg.notify();
      }
    }
  }

  public void shutdown() {
    synchronized (this) {
      this.isShutdownRequested = true;
      this.notifyAll();
    }

    try {
      this.thread.join();
    } catch (final InterruptedException var8) {}
    try {
      this.cacheFiles.close();
    } catch (final IOException e) {}
  }

  public MailboxMessage sendExitFullScreenMessage(final Frame var1) {
    return this.sendMessage(MailboxMessage.Type.EXIT_FULL_SCREEN, var1, 0, 0);
  }

  public MailboxMessage sendListDisplayModesMessage() {
    return this.sendMessage(MailboxMessage.Type.LIST_DISPLAY_MODES, null, 0, 0);
  }

  public MailboxMessage sendGetDeclaredFieldMessage(final Class<?> var1, final String var3) {
    return this.sendMessage(MailboxMessage.Type.GET_DECLARED_FIELD, new Object[]{var1, var3}, 0, 0);
  }
}
