package funorb.client.lobby;

import funorb.cache.ResourceLoader;
import funorb.data.LRUCache;
import funorb.io.Buffer;

public final class QuickChatResponses {
  private final ResourceLoader loader;
  private final LRUCache<Integer, QuickChatResponse> _f = new LRUCache<>();

  public QuickChatResponses(final ResourceLoader loader) {
    this.loader = loader;
    if (this.loader != null) {
      this.loader.itemCount(1);
    }
  }

  public QuickChatResponse get(final int id) {
    final QuickChatResponse loadedResponse = this._f.get(id);
    if (loadedResponse != null) {
      return loadedResponse;
    }

    final byte[] data = this.loader.getResource(1, id & 0x7fff);
    final QuickChatResponse response = new QuickChatResponse();
    if (data != null) {
      response.load(new Buffer(data));
    }
    if (id >= 0x8000) {
      response.markHigh();
    }

    this._f.put(id, response);
    return response;
  }
}
