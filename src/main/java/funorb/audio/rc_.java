package funorb.audio;

import funorb.data.NodeList;
import org.jetbrains.annotations.NotNull;

import java.util.Iterator;
import java.util.Objects;

public final class rc_ extends MixerInterface_idk {
  public final NodeList<MixerTrackConfig_idk> _n = new NodeList<>();
  public final vk_ _o = new vk_();
  private final MidiPlayer_idk _m;

  public rc_(final MidiPlayer_idk var1) {
    this._m = var1;
  }

  @Override
  public @NotNull Iterator<MixerInterface_idk> iterator() {
    return this._n.stream().<MixerInterface_idk>map(var1 -> var1._K).filter(Objects::nonNull).iterator();
  }

  @Override
  public int a784() {
    return 0;
  }

  @Override
  public void generateAudio2_idk(final int len) {
    this._o.generateAudio2_idk(len);

    for (final MixerTrackConfig_idk var3 : this._n) {
      if (!this._m.a258(var3)) {
        int var2 = len;

        do {
          if (var3._p >= var2) {
            this.a222(var3, var2);
            var3._p -= var2;
            break;
          }

          this.a222(var3, var3._p);
          var2 -= var3._p;
        } while (!this._m.a543(0, null, var3, var2));
      }
    }

  }

  @Override
  public void generateAudio1_idk(final int[] dest, final int offset, final int len) {
    this._o.generateAudio1_idk(dest, offset, len);

    for (final MixerTrackConfig_idk var6 : this._n) {
      if (!this._m.a258(var6)) {
        int var4 = offset;
        int var5 = len;

        do {
          if (var6._p >= var5) {
            this.a829(dest, var4, var6, var5, var4 + var5);
            var6._p -= var5;
            break;
          }

          this.a829(dest, var4, var6, var6._p, var4 + var5);
          var5 -= var6._p;
          var4 += var6._p;
        } while (!this._m.a543(var4, dest, var6, var5));
      }
    }

  }

  private void a222(final MixerTrackConfig_idk var2, int var3) {
    if ((4 & this._m._F[var2._y]) != 0 && var2._E < 0) {
      final int var4 = this._m._u[var2._y] / SampledAudioChannel.SAMPLES_PER_SECOND;
      final int var5 = (1048575 + var4 - var2._j) / var4;
      var2._j = 1048575 & var2._j + var3 * var4;
      if (var5 <= var3) {
        if (this._m._t[var2._y] == 0) {
          var2._K = al_.a771(var2._M, var2._K.f784(), var2._K.getVolume(), var2._K.l784());
        } else {
          var2._K = al_.a771(var2._M, var2._K.f784(), 0, var2._K.l784());
          this._m.a559(var2, var2._A._k[var2._H] < 0);
        }

        if (var2._A._k[var2._H] < 0) {
          assert var2._K != null;
          var2._K.f150();
        }

        var3 = var2._j / var4;
      }
    }

    assert var2._K != null;
    var2._K.generateAudio2_idk(var3);
  }

  private void a829(final int[] var1, int var2, final MixerTrackConfig_idk var3, int var4, final int var6) {
    if ((4 & this._m._F[var3._y]) != 0 && var3._E < 0) {
      final int var7 = this._m._u[var3._y] / SampledAudioChannel.SAMPLES_PER_SECOND;

      while (true) {
        final int var8 = (-var3._j + 1048575 + var7) / var7;
        if (var8 > var4) {
          var3._j += var7 * var4;
          break;
        }

        var3._K.generateAudio1_idk(var1, var2, var8);
        var3._j += var8 * var7 - 1048576;
        var2 += var8;
        var4 -= var8;
        int var9 = SampledAudioChannel.SAMPLES_PER_SECOND / 100;
        final int var10 = 262144 / var7;
        if (var10 < var9) {
          var9 = var10;
        }

        final al_ var11 = var3._K;
        if (this._m._t[var3._y] == 0) {
          var3._K = al_.a771(var3._M, var11.f784(), var11.getVolume(), var11.l784());
        } else {
          var3._K = al_.a771(var3._M, var11.f784(), 0, var11.l784());
          this._m.a559(var3, var3._A._k[var3._H] < 0);
          var3._K.a093(var9, var11.getVolume());
        }

        if (var3._A._k[var3._H] < 0) {
          assert var3._K != null;
          var3._K.f150();
        }

        var11.g150(var9);
        var11.generateAudio1_idk(var1, var2, var6 - var2);
        if (var11.e801()) {
          this._o.addFirst(var11);
        }
      }
    }

    var3._K.generateAudio1_idk(var1, var2, var4);
  }
}
