package launcher;

import funorb.cache.CacheWorker;
import funorb.cache.MasterIndexLoader;
import funorb.cache.ResourceLoader;
import funorb.commonui.CommonUI;
import funorb.io.Buffer;
import funorb.io.HuffmanCoder;
import funorb.shatteredplans.CacheFiles;
import funorb.shatteredplans.client.JagexApplet;
import funorb.shatteredplans.client.ShatteredPlansClient;
import funorb.shatteredplans.client.Sounds;
import funorb.shatteredplans.map.StarSystem;
import funorb.shatteredplans.server.ShatteredPlansServer;
import launcher.app.FOEventQueue;
import launcher.app.FOStub;
import launcher.app.JFocusFrame;
import launcher.db.DBUtils;
import launcher.db.Database;
import launcher.options.OptionsDatabase;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import java.applet.Applet;
import java.awt.GridLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
import java.net.InetSocketAddress;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.CodeSource;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

public final class ShatteredPlansLauncher {
  private static final String GAME_NAME = "Shattered Plans";
  public static final String GAME_ID = "shatteredplans";

  public static void main(final String[] args) throws Exception {
    final CommandLineOptions options = CommandLineOptions.parse(args);
    JagexApplet.DEBUG_MODE = options.debugMode;

    if (options.runClient) {
      runClient(options);
    } else if (options.runServer) {
      runHeadlessServer(options);
    } else {
      System.out.println("Nothing to do.");
    }
  }

  private static void runClient(final CommandLineOptions options) throws Exception {
    final CodeSource codeSrc = ShatteredPlansLauncher.class.getProtectionDomain().getCodeSource();
    final Path jarPath = Paths.get(codeSrc.getLocation().toURI());
    final ThreadLocalRandom rand = ThreadLocalRandom.current();

    final Path dbPath = jarPath.resolveSibling("persistent.db");
    final Database db = new Database("jdbc:sqlite:" + dbPath);
    System.out.println("Connected to database " + dbPath);
    DBUtils.runSQL(db, ShatteredPlansLauncher.class, "boot.sql")
        .orElseThrow(() -> new RuntimeException("boot script not found"))
        .fail(t -> System.exit(1));
    initializeDatabase(db);

    final OptionsDatabase optionsDb = new OptionsDatabase(db);
    final OptionsDatabase.OptionItem soundVolume = optionsDb.createOption("game.soundVolume", 0.15);
    final OptionsDatabase.OptionItem musicVolume = optionsDb.createOption("game.musicVolume", 0.1);
    final OptionsDatabase.OptionItem username = optionsDb.createOption("game.username", "");

    final Map<String, String> params = createParams();
    params.put("instanceid", Long.toString(rand.nextLong()));
    final String portStr = Integer.toString(options.serverAddress.getPort());
    params.put("gameport1", portStr);
    params.put("gameport2", portStr);

    // disable RSA during login to get the encryption key
    Buffer.RSA_EXPONENT = BigInteger.ONE; // effectively disables encryption

    final ShatteredPlansServer server;
    if (options.runServer) {
      try {
        server = new ShatteredPlansServer();
        final InetSocketAddress addr = server.bind(options.serverAddress.getPort());
        System.out.println("Started server on " + addr.getHostString() + ":" + addr.getPort());
      } catch (final Exception e) {
        throw new RuntimeException("could not create server", e);
      }
    } else {
      server = null;
      final InetSocketAddress addr = options.serverAddress;
      System.out.println("Connecting to existing server on " + addr.getHostString() + ":" + addr.getPort());
    }

    CommonUI.enteredUsername = username.getValue();
    final Applet app = new ShatteredPlansClient();
    app.setName(GAME_NAME);
    app.setFocusable(false);

    FOEventQueue.getQueue();
    final JFrame frame = new JFocusFrame(GAME_NAME);
    try (final InputStream iconRes = ShatteredPlansLauncher.class.getResourceAsStream("icon.png")) {
      if (iconRes != null) frame.setIconImage(ImageIO.read(iconRes));
    } catch (final IOException e) {
      e.printStackTrace();
    }
    frame.setLayout(new GridLayout());

    final FOStub stub = new FOStub(options, frame, app, params);
    app.setStub(stub);

    frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    frame.addWindowListener(new WindowAdapter() {
      @Override
      public void windowClosed(final WindowEvent e) {
        try {
          stub.stop();
          username.setValue(CommonUI.enteredUsername);
          soundVolume.setDouble(Sounds.soundVolume / (double) Sounds.MAX_VOLUME);
          musicVolume.setDouble(Sounds.musicVolume / (double) Sounds.MAX_VOLUME);
        } finally {
          app.destroy();
        }
        if (server != null) {
          try {
            server.shutdown();
          } catch (final InterruptedException e1) {}
        }
        System.exit(0);
        throw new Error("exit failed");
      }
    });
    frame.setContentPane(app);
    stub.appletResize(ShatteredPlansClient.SCREEN_WIDTH, ShatteredPlansClient.SCREEN_HEIGHT);
    app.init();
    app.start();
    // canvas exists now
    frame.setVisible(true);

    Sounds.soundVolume = (int) (soundVolume.getDouble() * Sounds.MAX_VOLUME);
    Sounds.setMusicVolume((int) (musicVolume.getDouble() * Sounds.MAX_VOLUME));
  }

  private static void runHeadlessServer(final CommandLineOptions options) {
    loadStaticResources();

    final ShatteredPlansServer server;
    try {
      server = new ShatteredPlansServer();
      final InetSocketAddress addr = server.bind(options.serverAddress.getPort());
      System.out.println("Started server on " + addr.getHostString() + ":" + addr.getPort());
    } catch (final Exception e) {
      throw new RuntimeException("could not create server", e);
    }

    try {
      Thread.sleep(Long.MAX_VALUE);
    } catch (final InterruptedException e1) {
      try {
        server.shutdown();
      } catch (final InterruptedException e2) {
        System.out.println("Interrupted, aborting.");
      }
    }
  }

  private static Map<String, String> createParams() {
    final Map<String, String> params = new HashMap<>();
    params.put("servernum", "8136");
    params.put("gamecrc", "35924258");
    params.put("member", "no");
    params.put("overxgames", "45");
    params.put("overxachievements", "1000");
    params.put("currency", "0");
    params.put("cookieprefix", "");
    params.put("cookiehost", ".funorb.com");
    return params;
  }

  private static void initializeDatabase(final Database db) throws Exception {
    DBUtils.runSQL(db, "boot.sql").ifPresent(p -> p.fail(t -> System.exit(1)));
  }

  /**
   * When running in headless mode, the usual client initialization process
   * does not run, so static variables do not get initialized. Fortunately,
   * most of the statics are not actually necessary for part of the code the
   * server uses to function. However, {@link StarSystem#NAMES} <i>is</i>
   * necessary, so we have to explicitly load it from the resource files here.
   */
  private static void loadStaticResources() {
    try (final CacheFiles cacheFiles = new CacheFiles();
         final CacheWorker cacheWorker = new CacheWorker(Thread::new)) {
      final MasterIndexLoader masterIndexLoader = new MasterIndexLoader(ShatteredPlansServer.PAGE_SOURCE, cacheWorker);
      if (!masterIndexLoader.loadIndex()) {
        throw new AssertionError("master index was not fully loaded");
      }

      final ResourceLoader loader1 = ResourceLoader.create(masterIndexLoader, cacheFiles, ResourceLoader.PageId.SHATTERED_PLANS_STRINGS_2);
      final ResourceLoader loader2 = ResourceLoader.create(masterIndexLoader, cacheFiles, ResourceLoader.PageId.HUFFMAN_CODES);
      for (int i = 0; true; i++) {
        if (i >= 5) {
          throw new AssertionError("data was not fully loaded");
        }
        if (loader1.loadAllGroups() && loader2.loadAllGroups()) {
          break;
        }
      }
      StarSystem.loadNames(loader1);
      HuffmanCoder.initialize(loader2);
    } catch (final IOException e) {
      throw new RuntimeException(e);
    }
  }
}
