package funorb.audio;

import funorb.cache.ResourceLoader;

import java.util.HashMap;
import java.util.Map;

public final class SoundLoader {
  public static SoundLoader globalLoader;
  private final Map<Long, VorbisFormat> cache2 = new HashMap<>();
  private final Map<Long, RawSampleS8> cache1 = new HashMap<>();
  private final ResourceLoader loader1;
  private final ResourceLoader loader2;

  public SoundLoader(final ResourceLoader loader1, final ResourceLoader loader2) {
    this.loader1 = loader1;
    this.loader2 = loader2;
  }

  private RawSampleS8 load1(final int groupId, final int itemId) {
    final int cacheKey = (itemId ^ (((groupId << 4) & 0xfff3) | (groupId >>> 12))) | (groupId << 16);
    final RawSampleS8 cachedRaw = this.cache1.get((long) cacheKey);
    if (cachedRaw == null) {
      final SynthFormat file = SynthFormat.load(this.loader1, groupId, itemId);
      if (file == null) {
        return null;
      } else {
        final RawSampleS8 raw = file.toRawSample();
        this.cache1.put((long) cacheKey, raw);
        return raw;
      }
    } else {
      return cachedRaw;
    }
  }

  public RawSampleS8 loadSingleton2(final int id) {
    if (this.loader2.groupCount() == 1) {
      return this.load2(0, id);
    } else if (this.loader2.itemCount(id) == 1) {
      return this.load2(id, 0);
    } else {
      throw new RuntimeException();
    }
  }

  public RawSampleS8 loadSingleton1(final int id) {
    if (this.loader1.groupCount() == 1) {
      return this.load1(0, id);
    } else if (this.loader1.itemCount(id) == 1) {
      return this.load1(id, 0);
    } else {
      throw new RuntimeException();
    }
  }

  private RawSampleS8 load2(final int groupId, final int itemId) {
    final int var5 = (((groupId >>> 12) | (0xfff0 & (groupId << 4))) ^ itemId) | (groupId << 16);
    final long cacheKey = 0x100000000L ^ (long) var5;
    RawSampleS8 raw = this.cache1.get(cacheKey);
    if (raw == null) {
      VorbisFormat file = this.cache2.get(cacheKey);
      if (file == null) {
        file = VorbisFormat.load(this.loader2, groupId, itemId);
        if (file == null) {
          return null;
        }

        this.cache2.put(cacheKey, file);
      }

      raw = file.toRawSample();
      this.cache2.remove(cacheKey);
      this.cache1.put(cacheKey, raw);
    }
    return raw;
  }

  @SuppressWarnings("SameParameterValue")
  public RawSampleS8 load2(final String item) {
    final int groupId = this.loader2.lookupGroup("");
    if (groupId < 0) {
      return null;
    } else {
      final int itemId = this.loader2.lookupItem(groupId, item);
      return itemId < 0 ? null : this.load2(groupId, itemId);
    }
  }

  public RawSampleS8 load1(final String item) {
    final int groupId = this.loader1.lookupGroup("");
    if (groupId >= 0) {
      final int itemId = this.loader1.lookupItem(groupId, item);
      return itemId < 0 ? null : this.load1(groupId, itemId);
    } else {
      return null;
    }
  }
}
