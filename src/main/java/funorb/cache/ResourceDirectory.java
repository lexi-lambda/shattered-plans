package funorb.cache;

import funorb.Strings;

public final class ResourceDirectory {
  private final int[] data;

  public ResourceDirectory(final int[] var1) {
    int var2 = 1;
    while (var2 <= var1.length + (var1.length >> 1)) {
      var2 <<= 1;
    }

    this.data = new int[var2 + var2];

    for (int var3 = 0; var3 < var2 + var2; ++var3) {
      this.data[var3] = -1;
    }

    for (int var3 = 0; var1.length > var3; var3++) {
      int var4 = var1[var3] & (var2 - 1);
      while (this.data[1 + var4 + var4] != -1) {
        var4 = (var2 - 1) & (var4 + 1);
      }

      this.data[var4 + var4] = var1[var3];
      this.data[1 + var4 + var4] = var3;
    }
  }

  private static int resourceKeyHash(final CharSequence key) {
    int hash = 0;
    for (int var4 = 0; var4 < key.length(); ++var4) {
      hash = Strings.encode1252Char(key.charAt(var4)) + (hash << 5) - hash;
    }
    return hash;
  }

  public int lookup(final String key) {
    final int hash = resourceKeyHash(key.toLowerCase());
    final int var3 = (this.data.length / 2) - 1;

    int var4 = var3 & hash;
    while (true) {
      final int var5 = this.data[1 + var4 + var4];
      if (var5 == -1) {
        return -1;
      }

      if (this.data[var4 + var4] == hash) {
        return var5;
      }

      var4 = var4 + 1 & var3;
    }
  }
}
