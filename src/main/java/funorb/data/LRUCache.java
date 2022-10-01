package funorb.data;

import java.util.LinkedHashMap;
import java.util.Map;

public final class LRUCache<K, V> extends LinkedHashMap<K, V> {
  private static final int CAPACITY = 64;

  public LRUCache() {
    super(32, 0.75F, true);
  }

  @Override
  protected boolean removeEldestEntry(final Map.Entry<K, V> eldest) {
    return this.size() > CAPACITY;
  }
}
