package launcher.db;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.sqlite.SQLiteConfig;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Optional;
import java.util.Properties;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public final class Database {
  private static final Properties PROPS;

  static {
    final SQLiteConfig config = new SQLiteConfig();
    config.enforceForeignKeys(true);
    PROPS = config.toProperties();
  }

  private final Connection conn;
  private final Statement statement;
  private boolean stopping;
  private final Thread executor;
  private final BlockingQueue<DatabasePromise<?>> tasks;

  public Database(final String url) throws SQLException {
    this(DriverManager.getConnection(url, PROPS));
  }

  private Database(final Connection conn) throws SQLException {
    this.conn = conn;
    this.statement = conn.createStatement();
    conn.setAutoCommit(false);
    this.executor = new Thread(this::run);
    this.executor.setDaemon(true);
    this.tasks = new LinkedBlockingQueue<>();
    this.executor.start();
  }

  public Connection getConnection() {
    return this.conn;
  }

  public Statement getStatement() throws SQLException {
    this.statement.clearBatch();
    this.statement.clearWarnings();
    return this.statement;
  }

  public PreparedStatement prepareStatement(final String sql) throws SQLException {
    return this.conn.prepareStatement(sql);
  }

  public <T> Optional<DatabasePromise<T>> schedule(final DatabaseTask<T> task) {
    return Optional.ofNullable(this.schedule(task, false));
  }

  @Contract("_ , true -> !null")
  public <T> @Nullable DatabasePromise<T> schedule(final @NotNull DatabaseTask<T> task, final boolean require) {
    synchronized (this.tasks) {
      if (!this.stopping) {
        final DatabasePromise<T> promise = new DatabasePromise<>(task);
        this.tasks.add(promise);
        return promise;
      } else if (require) {
        throw new IllegalStateException("database closed");
      } else {
        return null;
      }
    }
  }

  @SuppressWarnings("unused")
  public boolean isRunning() {
    return !this.stopping;
  }

  private void run() {
    while (!this.stopping || !this.tasks.isEmpty()) {
      final DatabasePromise<?> task;
      try {
        task = this.tasks.take();
      } catch (final InterruptedException e) {
        break;
      }
      try {
        task.execute(this);
      } catch (final ThreadDeath d) {
        break;
      }
    }
    System.out.println("Closing database connection");
    this.tasks.forEach(DatabasePromise::cancel);
    try {
      this.conn.close();
    } catch (final SQLException e) {
      e.printStackTrace();
    }
  }

  public void stop() {
    synchronized (this.tasks) {
      if (this.stopping) return;
      this.stopping = true;
    }
    this.executor.interrupt();
  }
}
