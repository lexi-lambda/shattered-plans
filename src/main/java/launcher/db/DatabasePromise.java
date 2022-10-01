package launcher.db;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;

public final class DatabasePromise<T> {
  private final DatabaseTask<T> task;
  private boolean consumed;
  private boolean completed;
  private boolean canceled;
  private T result;
  private Throwable error;
  private List<Consumer<? super T>> then;
  private List<Consumer<? super Throwable>> fail;

  DatabasePromise(final DatabaseTask<T> task) {
    this.task = task;
    this.then = Collections.emptyList();
    this.fail = Collections.emptyList();
  }

  void execute(final Database db) {
    synchronized (this) {
      if (this.consumed) return;
      this.consumed = true;
    }

    try {
      this.result = this.task.run(db);
      db.getConnection().commit();
      synchronized (this) {
        this.completed = true;
        this.notifyAll();
      }
      this.then.forEach(c ->
      {
        try {
          c.accept(this.result);
        } catch (final Throwable t) {
          t.printStackTrace();
        }
      });
    } catch (final ThreadDeath d) {
      System.out.println("Received ThreadDeath, stopping DB");
      db.stop();
    } catch (final Throwable t) {
      try {
        db.getConnection().rollback();
      } catch (final SQLException e) {
        e.printStackTrace();
        db.stop();
      }
      synchronized (this) {
        this.completed = true;
        this.error = t;
        this.notifyAll();
      }
      this.fail.forEach(c ->
      {
        try {
          c.accept(t);
        } catch (final Throwable tt) {
          tt.printStackTrace();
        }
      });
    }
  }

  private boolean isCompleted() {
    return this.completed;
  }

  private boolean isFailed() {
    return this.error != null;
  }

  private boolean isCanceled() {
    return this.canceled;
  }

  @SuppressWarnings("UnusedReturnValue")
  public synchronized boolean cancel() {
    if (this.consumed) return this.canceled;
    this.consumed = true;
    this.canceled = true;
    this.notifyAll();
    return true;
  }

  @SuppressWarnings("unused")
  public synchronized T get() {
    if (this.isCanceled()) throw new IllegalStateException("canceled");
    if (this.isCompleted()) {
      if (this.isFailed()) throw new IllegalStateException("failed", this.error);
      return this.result;
    } else throw new IllegalStateException("not completed");
  }

  @SuppressWarnings("unused")
  public synchronized boolean waitFor() throws InterruptedException {
    while (!this.isCompleted() && !this.isCanceled()) {
      this.wait();
    }
    return this.isCompleted();
  }

  @SuppressWarnings("unused")
  public synchronized boolean waitFor(long timeout) throws InterruptedException {
    if (timeout < 0) throw new IllegalArgumentException("timeout " + timeout);
    if (timeout > 0) {
      final long deadline = System.currentTimeMillis() + timeout;
      while (!this.isCompleted() && !this.isCanceled()) {
        timeout = deadline - System.currentTimeMillis();
        if (timeout <= 0) break;
        this.wait(timeout);
      }
    } else {
      while (!this.isCompleted() && !this.isCanceled()) {
        this.wait();
      }
    }
    return this.isCompleted();
  }

  public synchronized T await() {
    while (!this.isCompleted() && !this.isCanceled()) {
      try {
        this.wait();
      } catch (final InterruptedException e) {
      }
    }
    if (this.isCanceled()) throw new IllegalStateException("canceled");
    if (this.isFailed()) throw new IllegalStateException("failed", this.error);
    return this.result;
  }

  @SuppressWarnings("UnusedReturnValue")
  public DatabasePromise<T> then(final Consumer<? super T> f) {
    synchronized (this) {
      if (!this.isCompleted()) {
        if (this.then.isEmpty()) this.then = new ArrayList<>();
        this.then.add(f);
        return this;
      }
      if (this.isFailed()) return this;
    }
    f.accept(this.result);
    return this;
  }

  @SuppressWarnings("UnusedReturnValue")
  public DatabasePromise<T> fail(final Consumer<? super Throwable> f) {
    synchronized (this) {
      if (!this.isCompleted()) {
        if (this.fail.isEmpty()) this.fail = new ArrayList<>();
        this.fail.add(f);
        return this;
      }
      if (!this.isFailed()) return this;
    }
    f.accept(this.error);
    return this;
  }
}
