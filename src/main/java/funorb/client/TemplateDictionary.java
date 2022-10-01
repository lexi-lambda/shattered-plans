package funorb.client;

public final class TemplateDictionary {
  private final TemplateDictionary next;
  private Entry entry;

  public TemplateDictionary() {
    this(null);
  }

  public TemplateDictionary(final TemplateDictionary var1) {
    this.next = var1;
  }

  private String get(final String key) {
    for (Entry entry = this.entry; entry != null; entry = entry.next) {
      if (key.equals(entry.key)) {
        return entry.value;
      }
    }
    if (this.next == null) {
      return null;
    } else {
      return this.next.get(key);
    }
  }

  public void put(final String key, final String value) {
    for (Entry entry = this.entry; entry != null; entry = entry.next) {
      if (key.equals(entry.key)) {
        entry.value = value;
        return;
      }
    }
    this.entry = new Entry(key, value, this.entry);
  }

  public String expand(final String template) {
    final StringBuilder var3 = new StringBuilder(template.length());
    int var4 = 0;

    final int var5 = template.length();

    while (var4 < var5) {
      int var6 = template.indexOf("<%", var4);
      if (var6 >= 0) {
        var3.append(template, var4, var6);
        var4 = var6;
        var6 = template.indexOf(">", var6 + 2);
        if (var6 >= 0) {
          final String var7 = template.substring(var4 + 2, var6);
          final String var8 = this.get(var7);
          if (var8 != null) {
            var3.append(var8);
          }

          var4 = 1 + var6;
          continue;
        }

        var3.append(template, var4, var5);
        break;
      }

      var3.append(template, var4, var5);
      break;
    }

    return var3.toString();
  }

  @SuppressWarnings("WeakerAccess")
  private static final class Entry {
    public final Entry next;
    public final String key;
    public String value;

    public Entry(final String key, final String value, final Entry next) {
      this.key = key;
      this.value = value;
      this.next = next;
    }
  }
}
