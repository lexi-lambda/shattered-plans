package launcher.db;

import funorb.shatteredplans.server.ShatteredPlansServer;
import launcher.ShatteredPlansLauncher;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public final class DBUtils {
  @SuppressWarnings("unused")
  public static List<String> readSQL(final InputStream in, final String delim) throws IOException {
    return readSQL(new InputStreamReader(in, StandardCharsets.UTF_8), delim);
  }

  private static List<String> readSQL(final Reader in, final String delim) throws IOException {
    final StringBuilder build = new StringBuilder();
    final char[] temp = new char[1024];
    while (true) {
      final int n = in.read(temp);
      if (n < 0) break;
      build.append(temp, 0, n);
    }

    final List<String> statements = new ArrayList<>();
    int pos = 0;
    while (pos < build.length()) {
      int next = build.indexOf(delim, pos);
      if (next < 0) next = build.length();
      int start = pos, end = next;
      while (end > start && build.charAt(start) <= ' ') {
        start++;
      }
      while (end > start && build.charAt(end - 1) <= ' ') {
        end--;
      }
      if (end > start) statements.add(build.substring(start, end));
      pos = next + delim.length();
    }
    return statements;
  }

  private static Optional<DatabasePromise<Void>> runSQL(final Database db, final InputStream in, @SuppressWarnings("SameParameterValue") final String delim) throws IOException {
    if (in != null) {
      return runSQL(db, new InputStreamReader(in, StandardCharsets.UTF_8), delim);
    } else return Optional.empty();
  }

  private static Optional<DatabasePromise<Void>> runSQL(final Database db, final Reader in, final String delim) throws IOException {
    if (in != null) {
      final List<String> statements;
      try (in) {
        statements = readSQL(in, delim);
      }

      return db.schedule(d ->
      {
        final Statement stmt = d.getStatement();
        for (final String sql : statements) {
          stmt.addBatch(sql);
        }
        stmt.executeBatch();
        return null;
      });
    } else return Optional.empty();
  }

  private static Optional<DatabasePromise<Void>> checkRunSQL(final Optional<DatabasePromise<Void>> opt, final String name) {
    opt.ifPresent(p ->
    {
      p.then(r -> System.out.println("Executed " + name));
      p.fail(t ->
      {
        synchronized (System.err) {
          System.err.println("Error in " + name);
          t.printStackTrace();
        }
      });
    });
    if (opt.isEmpty()) System.out.println("No script " + name);
    return opt;
  }

  public static Optional<DatabasePromise<Void>> runSQL(final Database db, @SuppressWarnings("SameParameterValue") final Class<?> cls, @SuppressWarnings("SameParameterValue") final String name) throws IOException {
    return checkRunSQL(runSQL(db, cls.getResourceAsStream(name), ";"), cls.getName() + ":" + name);
  }

  public static Optional<DatabasePromise<Void>> runSQL(final Database db, @SuppressWarnings("SameParameterValue") final String name) throws IOException {
    return checkRunSQL(runSQL(db, ShatteredPlansServer.getResource(name), ";"), ShatteredPlansLauncher.GAME_ID + ":" + name);
  }

  private DBUtils() {
    throw new Error();
  }
}
