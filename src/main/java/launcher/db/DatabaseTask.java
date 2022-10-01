package launcher.db;

public interface DatabaseTask<T> {
  T run(Database db) throws Throwable;
}
