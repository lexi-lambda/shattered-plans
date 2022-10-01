package launcher.options;

import launcher.db.Database;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public final class OptionsDatabase {
  @SuppressWarnings("unused")
  public final class OptionItem {
    private final String name;
    private final OptionType type;
    private final Object defVal;

    private OptionItem(final String name, final OptionType type, final Object defVal) {
      this.name = name;
      this.type = type;
      this.defVal = defVal;
    }

    public String getName() {
      return this.name;
    }

    public OptionType getType() {
      return this.type;
    }

    public boolean getBoolean() {
      if (this.type != OptionType.BOOLEAN) throw new IllegalStateException("type is " + this.type);
      return OptionsDatabase.this.db.schedule(d -> {
        OptionsDatabase.this.get.setString(1, this.name);
        try (final ResultSet res = OptionsDatabase.this.get.executeQuery()) {
          if (res.next()) return res.getBoolean(1);
        }
        OptionsDatabase.this.set.setString(1, this.name);
        OptionsDatabase.this.set.setBoolean(2, (Boolean) this.defVal);
        OptionsDatabase.this.set.executeUpdate();
        return (Boolean) this.defVal;
      }, true).await();
    }

    public int getInt() {
      if (this.type != OptionType.INTEGER) throw new IllegalStateException("type is " + this.type);
      return OptionsDatabase.this.db.schedule(d ->
      {
        OptionsDatabase.this.get.setString(1, this.name);
        try (final ResultSet res = OptionsDatabase.this.get.executeQuery()) {
          if (res.next()) return res.getInt(1);
        }
        OptionsDatabase.this.set.setString(1, this.name);
        OptionsDatabase.this.set.setInt(2, (Integer) this.defVal);
        OptionsDatabase.this.set.executeUpdate();
        return (Integer) this.defVal;
      }, true).await();
    }

    public double getDouble() {
      if (this.type != OptionType.DOUBLE) throw new IllegalStateException("type is " + this.type);
      return OptionsDatabase.this.db.schedule(d ->
      {
        OptionsDatabase.this.get.setString(1, this.name);
        try (final ResultSet res = OptionsDatabase.this.get.executeQuery()) {
          if (res.next()) return res.getDouble(1);
        }
        OptionsDatabase.this.set.setString(1, this.name);
        OptionsDatabase.this.set.setDouble(2, (Double) this.defVal);
        OptionsDatabase.this.set.executeUpdate();
        return (Double) this.defVal;
      }, true).await();
    }

    public String getValue() {
      if (this.type != OptionType.STRING) throw new IllegalStateException("type is " + this.type);
      return OptionsDatabase.this.db.schedule(d ->
      {
        OptionsDatabase.this.get.setString(1, this.name);
        try (final ResultSet res = OptionsDatabase.this.get.executeQuery()) {
          if (res.next()) return res.getString(1);
        }
        OptionsDatabase.this.set.setString(1, this.name);
        OptionsDatabase.this.set.setString(2, (String) this.defVal);
        OptionsDatabase.this.set.executeUpdate();
        return (String) this.defVal;
      }, true).await();
    }

    public void setBoolean(final boolean value) {
      if (this.type != OptionType.BOOLEAN) throw new IllegalStateException("type is " + this.type);
      OptionsDatabase.this.db.schedule(d ->
      {
        OptionsDatabase.this.set.setString(1, this.name);
        OptionsDatabase.this.set.setBoolean(2, value);
        OptionsDatabase.this.set.executeUpdate();
        return null;
      }, true).fail(Throwable::printStackTrace);
    }

    public void setInt(final int value) {
      if (this.type != OptionType.INTEGER) throw new IllegalStateException("type is " + this.type);
      OptionsDatabase.this.db.schedule(d ->
      {
        OptionsDatabase.this.set.setString(1, this.name);
        OptionsDatabase.this.set.setInt(2, value);
        OptionsDatabase.this.set.executeUpdate();
        return null;
      }, true).fail(Throwable::printStackTrace);
    }

    public void setDouble(final double value) {
      if (this.type != OptionType.DOUBLE) throw new IllegalStateException("type is " + this.type);
      OptionsDatabase.this.db.schedule(d ->
      {
        OptionsDatabase.this.set.setString(1, this.name);
        OptionsDatabase.this.set.setDouble(2, value);
        OptionsDatabase.this.set.executeUpdate();
        return null;
      }, true).fail(Throwable::printStackTrace);
    }

    public void setValue(@SuppressWarnings("SameParameterValue") final String value) {
      if (this.type != OptionType.STRING) throw new IllegalStateException("type is " + this.type);
      OptionsDatabase.this.db.schedule(d ->
      {
        OptionsDatabase.this.set.setString(1, this.name);
        OptionsDatabase.this.set.setString(2, value);
        OptionsDatabase.this.set.executeUpdate();
        return null;
      }, true).fail(Throwable::printStackTrace);
    }
  }

  private final Database db;
  private final PreparedStatement get;
  private final PreparedStatement set;
  private final Map<String, OptionItem> items;

  public OptionsDatabase(final Database db) throws SQLException {
    this.db = db;
    this.items = new HashMap<>();
    this.get = db.prepareStatement("SELECT val FROM Options WHERE name = ?");
    this.set = db.prepareStatement("INSERT OR REPLACE INTO Options VALUES (?, ?)");
  }

  private synchronized OptionItem getOption(final String name, final OptionType type, final Object defVal) {
    if (name == null || type == null) throw new NullPointerException();
    OptionItem item = this.items.get(name);
    if (item == null) {
      item = new OptionItem(name, type, defVal);
      this.items.put(name, item);
    } else throw new IllegalArgumentException("option " + name + " already defined");
    return item;
  }

  @SuppressWarnings({"SameParameterValue", "unused"})
  public OptionItem getOption(final String name) {
    if (name == null) throw new NullPointerException();
    final OptionItem item = this.items.get(name);
    if (item == null) throw new IllegalArgumentException("option " + name + " not defined");
    return item;
  }

  @SuppressWarnings({"UnusedReturnValue", "unused"})
  public OptionItem createOption(@SuppressWarnings("SameParameterValue") final String name, @SuppressWarnings("SameParameterValue") final boolean def) {
    return this.getOption(name, OptionType.BOOLEAN, def);
  }

  @SuppressWarnings("unused")
  public OptionItem createOption(@SuppressWarnings("SameParameterValue") final String name, @SuppressWarnings("SameParameterValue") final int def) {
    return this.getOption(name, OptionType.INTEGER, def);
  }

  @SuppressWarnings("UnusedReturnValue")
  public OptionItem createOption(final String name, final double def) {
    return this.getOption(name, OptionType.DOUBLE, def);
  }

  @SuppressWarnings("SameParameterValue")
  public OptionItem createOption(final String name, final String def) {
    if (def == null) throw new NullPointerException();
    return this.getOption(name, OptionType.STRING, def);
  }
}
