package funorb.audio;

import funorb.cache.ResourceLoader;

import java.util.HashMap;
import java.util.Map;

public final class SoundLoader {
  public static SoundLoader globalLoader;
  private final Map<Long, VorbisFormat> vorbisCache = new HashMap<>();
  private final Map<Long, RawSampleS8> synthCache = new HashMap<>();
  private final ResourceLoader synthLoader;
  private final ResourceLoader vorbisLoader;

  public SoundLoader(final ResourceLoader synthLoader, final ResourceLoader vorbisLoader) {
    this.synthLoader = synthLoader;
    this.vorbisLoader = vorbisLoader;
  }

  private RawSampleS8 loadSynth(final int groupId, final int itemId) {
    final int cacheKey = (itemId ^ (((groupId << 4) & 0xfff3) | (groupId >>> 12))) | (groupId << 16);
    final RawSampleS8 cachedRaw = this.synthCache.get((long) cacheKey);
    if (cachedRaw == null) {
      final SynthFormat file = SynthFormat.load(this.synthLoader, groupId, itemId);
      if (file == null) {
        return null;
      } else {
        final RawSampleS8 raw = file.toRawSample();
        this.synthCache.put((long) cacheKey, raw);
        return raw;
      }
    } else {
      return cachedRaw;
    }
  }

  public RawSampleS8 loadSingletonVorbis(final int id) {
    if (this.vorbisLoader.groupCount() == 1) {
      return this.loadVorbis(0, id);
    } else if (this.vorbisLoader.itemCount(id) == 1) {
      return this.loadVorbis(id, 0);
    } else {
      throw new RuntimeException();
    }
  }

  public RawSampleS8 loadSingletonSynth(final int id) {
    if (this.synthLoader.groupCount() == 1) {
      return this.loadSynth(0, id);
    } else if (this.synthLoader.itemCount(id) == 1) {
      return this.loadSynth(id, 0);
    } else {
      throw new RuntimeException();
    }
  }

  private RawSampleS8 loadVorbis(final int groupId, final int itemId) {
    final int var5 = (((groupId >>> 12) | (0xfff0 & (groupId << 4))) ^ itemId) | (groupId << 16);
    final long cacheKey = 0x100000000L ^ (long) var5;
    RawSampleS8 raw = this.synthCache.get(cacheKey);
    if (raw == null) {
      VorbisFormat file = this.vorbisCache.get(cacheKey);
      if (file == null) {
        file = VorbisFormat.load(this.vorbisLoader, groupId, itemId);
        if (file == null) {
          return null;
        }

        this.vorbisCache.put(cacheKey, file);
      }

      raw = file.toRawSample();
      this.vorbisCache.remove(cacheKey);
      this.synthCache.put(cacheKey, raw);
    }
    return raw;
  }

  @SuppressWarnings("SameParameterValue")
  public RawSampleS8 loadVorbis(final String item) {
    final int groupId = this.vorbisLoader.lookupGroup("");
    if (groupId < 0) {
      return null;
    } else {
      final int itemId = this.vorbisLoader.lookupItem(groupId, item);
      return itemId < 0 ? null : this.loadVorbis(groupId, itemId);
    }
  }

  public RawSampleS8 loadSynth(final String item) {
    final int groupId = this.synthLoader.lookupGroup("");
    if (groupId >= 0) {
      final int itemId = this.synthLoader.lookupItem(groupId, item);
      return itemId < 0 ? null : this.loadSynth(groupId, itemId);
    } else {
      return null;
    }
  }
}
