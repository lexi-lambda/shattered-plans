package funorb.audio;

import funorb.cache.ResourceLoader;
import funorb.io.Buffer;

public final class dq_ {
  private final pk_[] _b = new pk_[10];
  private final int _c;
  private final int _a;

  private dq_(final Buffer data) {
    for (int i = 0; i < 10; ++i) {
      final int var3 = data.readUByte();
      if (var3 != 0) {
        --data.pos;
        this._b[i] = new pk_();
        this._b[i].initialize(data);
      }
    }

    this._c = data.readUShort();
    this._a = data.readUShort();
  }

  public static dq_ load(final ResourceLoader loader, final int groupId, final int itemId) {
    final byte[] data = loader.getResource(groupId, itemId);
    return data == null ? null : new dq_(new Buffer(data));
  }

  private byte[] a928() {
    int var1 = 0;

    for (int i = 0; i < 10; ++i) {
      if (this._b[i] != null && var1 < this._b[i]._a + this._b[i]._s) {
        var1 = this._b[i]._a + this._b[i]._s;
      }
    }

    if (var1 == 0) {
      return new byte[0];
    }
    final int var2 = SampledAudioChannel.SAMPLES_PER_SECOND * var1 / 1000;
    final byte[] var3 = new byte[var2];

    for (int i = 0; i < 10; ++i) {
      if (this._b[i] != null) {
        final int var5 = this._b[i]._a * SampledAudioChannel.SAMPLES_PER_SECOND / 1000;
        final int var6 = this._b[i]._s * SampledAudioChannel.SAMPLES_PER_SECOND / 1000;
        final int[] var7 = this._b[i].a111(var5, this._b[i]._a);

        for (int j = 0; j < var5; ++j) {
          int var9 = var3[j + var6] + (var7[j] >> 8);
          if ((var9 + 128 & -256) != 0) {
            var9 = var9 >> 31 ^ 127;
          }

          var3[j + var6] = (byte) var9;
        }
      }
    }

    return var3;
  }

  public kk_ b720() {
    final byte[] var1 = this.a928();
    return new kk_(var1, SampledAudioChannel.SAMPLES_PER_SECOND * this._c / 1000, SampledAudioChannel.SAMPLES_PER_SECOND * this._a / 1000);
  }
}
