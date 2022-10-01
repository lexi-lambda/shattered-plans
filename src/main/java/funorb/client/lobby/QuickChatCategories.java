package funorb.client.lobby;

import funorb.cache.ResourceLoader;
import funorb.data.LRUCache;
import funorb.io.Buffer;

public final class QuickChatCategories {
  private final ResourceLoader loader;
  private final LRUCache<Integer, QuickChatCategory> _b = new LRUCache<>();

  public QuickChatCategories(final ResourceLoader loader) {
    this.loader = loader;
    if (this.loader != null) {
      this.loader.itemCount(0);
    }
  }

  public QuickChatCategory get(final int id) {
    final QuickChatCategory loadedCategory = this._b.get(id);
    if (loadedCategory != null) {
      return loadedCategory;
    }

    final byte[] data = this.loader.getResource(0, id & 0x7fff);
    final QuickChatCategory category = new QuickChatCategory();
    if (data != null) {
      category.load(new Buffer(data));
    }
    if (id >= 0x8000) {
      category.markHigh();
    }

    this._b.put(id, category);
    return category;
  }
}
