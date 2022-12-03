package funorb.audio;

import funorb.cache.ResourceLoader;

import java.util.HashMap;
import java.util.Map;

public final class SoundLoader {
  public static SoundLoader globalLoader;
  private final Map<Long, fd_> _a = new HashMap<>();
  private final Map<Long, AudioSampleData_idk> _d = new HashMap<>();
  private final ResourceLoader loader1;
  private final ResourceLoader loader2;

  public SoundLoader(final ResourceLoader loader1, final ResourceLoader loader2) {
    this.loader1 = loader1;
    this.loader2 = loader2;
  }

  private AudioSampleData_idk load1(final int groupId, final int itemId) {
    final int var5 = (itemId ^ (((groupId << 4) & 0xfff3) | (groupId >>> 12))) | (groupId << 16);
    final AudioSampleData_idk var8 = this._d.get((long) var5);
    if (var8 == null) {
      final dq_ var9 = dq_.load(this.loader1, groupId, itemId);
      if (var9 == null) {
        return null;
      } else {
        final AudioSampleData_idk kk_ = var9.b720();
        this._d.put((long) var5, kk_);
        return kk_;
      }
    } else {
      return var8;
    }
  }

  public AudioSampleData_idk loadSingleton2(final int var3) {
    if (this.loader2.groupCount() == 1) {
      return this.load2(0, var3);
    } else if (this.loader2.itemCount(var3) == 1) {
      return this.load2(var3, 0);
    } else {
      throw new RuntimeException();
    }
  }

  public AudioSampleData_idk loadSingleton1(final int var1) {
    if (this.loader1.groupCount() == 1) {
      return this.load1(0, var1);
    } else if (this.loader1.itemCount(var1) == 1) {
      return this.load1(var1, 0);
    } else {
      throw new RuntimeException();
    }
  }

  private AudioSampleData_idk load2(final int var3, final int var2) {
    final int var5 = (((var3 >>> 12) | (0xfff0 & (var3 << 4))) ^ var2) | (var3 << 16);
    final long var6 = 0x100000000L ^ (long) var5;
    AudioSampleData_idk var8 = this._d.get(var6);
    if (var8 == null) {
      fd_ var9 = this._a.get(var6);
      if (var9 == null) {
        var9 = fd_.a740(this.loader2, var3, var2);
        if (var9 == null) {
          return null;
        }

        this._a.put(var6, var9);
      }

      var8 = var9.a582();
      this._a.remove(var6);
      this._d.put(var6, var8);
    }
    return var8;
  }

  @SuppressWarnings("SameParameterValue")
  public AudioSampleData_idk load2(final String item) {
    final int groupId = this.loader2.lookupGroup("");
    if (groupId < 0) {
      return null;
    } else {
      final int itemId = this.loader2.lookupItem(groupId, item);
      return itemId < 0 ? null : this.load2(groupId, itemId);
    }
  }

  public AudioSampleData_idk load1(final String item) {
    final int groupId = this.loader1.lookupGroup("");
    if (groupId >= 0) {
      final int itemId = this.loader1.lookupItem(groupId, item);
      return itemId >= 0 ? this.load1(groupId, itemId) : null;
    } else {
      return null;
    }
  }
}
