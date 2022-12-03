package funorb.audio;

import funorb.cache.ResourceLoader;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public final class MidiPlayer_idk extends MixerInterface_idk {
  public final int[] _t = new int[16];
  public final int[] _F = new int[16];
  private final int[] _p = new int[16];
  private final int[] _y = new int[16];
  private final int[] _q = new int[16];
  private final MixerTrackConfig_idk[][] _N = new MixerTrackConfig_idk[16][128];
  private final int[] _r = new int[16];
  private final int[] _m = new int[16];
  private final Map<Integer, br_> _Q;
  private final int[] _G = new int[16];
  private final MidiReader midiReader = new MidiReader();
  private final int[] _I = new int[16];
  private final int[] _P = new int[16];
  private final int[] _E = new int[16];
  private final int[] _J = new int[16];
  private final int[] _A = new int[16];
  private final int[] _T = new int[16];
  private final rc_ _l = new rc_(this);
  public final int[] _u = new int[16];
  private int volume = 256;
  private final MixerTrackConfig_idk[][] _K = new MixerTrackConfig_idk[16][128];
  private int _M = 1000000;
  private final int[] _s = new int[16];
  private int trackWithSoonestEvent;
  private long _O;
  private int nextEventTicks;
  private long currentPlayTime;
  private boolean _v;
  private SongData _z;

  public MidiPlayer_idk() {
    this._Q = new HashMap<>();
    this.a679();
    this.a430(true);
  }

  @SuppressWarnings("CopyConstructorMissesField")
  public MidiPlayer_idk(final MidiPlayer_idk var1) {
    this._Q = var1._Q;
    this.a679();
    this.a430(true);
  }

  private static br_ a675cf(final ResourceLoader var0, final int var1) {
    final byte[] var2 = var0.getSingletonResource(var1);
    return var2 == null ? null : new br_(var2);
  }

  public synchronized void e150() {
    this.b430(true);
  }

  private void b789(final int var1, final int var3) {
    this._T[var1] = var3;
  }

  @Override
  public @NotNull Iterator<MixerInterface_idk> iterator() {
    return Collections.<MixerInterface_idk>singletonList(this._l).iterator();
  }

  private void a326(final int var2, final int var3) {
    this._E[var3] = var2;
    this._u[var3] = (int) (0.5D + 2097152.0D * Math.pow(2.0D, 5.4931640625E-4D * (double) var2));
  }

  private int b237(final MixerTrackConfig_idk var1) {
    int var3 = (var1._r * var1._G >> 12) + var1._J;
    var3 += this._r[var1._y] * (this._T[var1._y] - 8192) >> 12;
    final kc_ var4 = var1._u;
    int var6;
    if (var4._o > 0 && (var4._f > 0 || this._p[var1._y] > 0)) {
      var6 = var4._f << 2;
      final int var7 = var4._j << 1;
      if (var7 > var1._C) {
        var6 = var1._C * var6 / var7;
      }

      var6 += this._p[var1._y] >> 7;
      final double var8 = Math.sin((double) (var1._i & 511) * 0.01227184630308513D);
      var3 += (int) ((double) var6 * var8);
    }

    var6 = (int) ((double) (256 * var1._M._j) * Math.pow(2.0D, 3.255208333333333E-4D * (double) var3) / (double) SampledAudioChannel.SAMPLES_PER_SECOND + 0.5D);
    return Math.max(var6, 1);
  }

  public synchronized void f150() {
    for (final br_ var2 : this._Q.values()) {
      var2.e150();
    }
  }

  private void a093(final int var1) {
    for (final MixerTrackConfig_idk var3 : this._l._n) {
      if ((var1 < 0 || var3._y == var1) && var3._E < 0) {
        this._N[var3._y][var3._H] = null;
        var3._E = 0;
      }
    }
  }

  private void a172(final int var3, final int var4) {
    final MixerTrackConfig_idk var5 = this._N[var3][var4];
    if (var5 != null) {
      this._N[var3][var4] = null;
      if ((2 & this._F[var3]) == 0) {
        var5._E = 0;
      } else if (this._l._n.stream().anyMatch(var6 -> var5._y == var6._y && var6._E < 0 && var6 != var5)) {
        var5._E = 0;
      }
    }
  }

  public synchronized void c430() {
    this._M = 1000000;
  }

  public synchronized void a679() {
    for (int var4 = 0; var4 < 16; ++var4) {
      this._A[var4] = 256;
    }
  }

  public synchronized void a350(final SoundLoader soundLoader, final ResourceLoader loader, final SongData track) {
    track.analyzeNotesUsedPerProgram();

    boolean var6 = true;

    for (final Map.Entry<Integer, byte[]> var8 : track.notesUsedPerProgram.entrySet()) {
      final int var9 = var8.getKey();
      br_ var10 = this._Q.get(var9);
      if (var10 == null) {
        var10 = a675cf(loader, var9);
        if (var10 == null) {
          var6 = false;
          continue;
        }

        this._Q.put(var9, var10);
      }

      if (!var10.a972(soundLoader, var8.getValue())) {
        var6 = false;
      }
    }

    if (var6) {
      track.resetNotesUsedPerProgram();
    }
  }

  @Override
  public synchronized void generateAudio1_idk(final int[] dest, int offset, int len) {
    if (this.midiReader.isLoaded()) {
      final int var4 = this.midiReader.ticksPerQuarterNote * this._M / SampledAudioChannel.SAMPLES_PER_SECOND;

      do {
        final long var5 = this._O + (long) len * (long) var4;
        if (this.currentPlayTime - var5 >= 0L) {
          this._O = var5;
          break;
        }

        final int var7 = (int) ((-this._O + (this.currentPlayTime - (-((long) var4) + 1L))) / (long) var4);
        this._O += (long) var7 * (long) var4;
        this._l.generateAudio1_idk(dest, offset, var7);
        offset += var7;
        len -= var7;
        this.a423();
      } while (this.midiReader.isLoaded());
    }

    this._l.generateAudio1_idk(dest, offset, len);
  }

  @Override
  public synchronized void generateAudio2_idk(int len) {
    if (this.midiReader.isLoaded()) {
      final int var2 = this.midiReader.ticksPerQuarterNote * this._M / SampledAudioChannel.SAMPLES_PER_SECOND;

      do {
        final long var3 = (long) len * (long) var2 + this._O;
        if (this.currentPlayTime - var3 >= 0L) {
          this._O = var3;
          break;
        }

        final int var5 = (int) ((-1L - this._O + this.currentPlayTime + (long) var2) / (long) var2);
        this._O += (long) var5 * (long) var2;
        this._l.generateAudio2_idk(var5);
        len -= var5;
        this.a423();
      } while (this.midiReader.isLoaded());
    }

    this._l.generateAudio2_idk(len);
  }

  @SuppressWarnings("SameParameterValue")
  public synchronized void setVolume(final int volume) {
    this.volume = volume;
  }

  private synchronized void b430(final boolean var2) {
    this.midiReader.unload();
    this._z = null;
    this.a430(var2);
  }

  private void b366(final int var1) {
    for (final MixerTrackConfig_idk var3 : this._l._n) {
      if (var1 < 0 || var1 == var3._y) {
        if (var3._K != null) {
          var3._K.g150(SampledAudioChannel.SAMPLES_PER_SECOND / 100);
          if (var3._K.e801()) {
            this._l._o.addFirst(var3._K);
          }

          var3.d487();
        }

        if (var3._E < 0) {
          this._N[var3._y][var3._H] = null;
        }

        var3.unlink();
      }
    }
  }

  private void a540(final int var2) {
    if ((2 & this._F[var2]) != 0) {
      for (final MixerTrackConfig_idk var3 : this._l._n) {
        if (var3._y == var2 && this._N[var2][var3._H] == null && var3._E < 0) {
          var3._E = 0;
        }
      }
    }
  }

  public synchronized void a077(final SongData var1, final boolean var3) {
    this.a918(var3, var1, true);
  }

  private void a556(int var2) {
    if (var2 >= 0) {
      this._y[var2] = 12800;
      this._G[var2] = 8192;

      this._s[var2] = 16383;
      this._T[var2] = 8192;
      this._p[var2] = 0;
      this._J[var2] = 8192;
      this.a540(var2);
      this.d093(var2);
      this._F[var2] = 0;
      this._q[var2] = 32767;
      this._r[var2] = 256;
      this._t[var2] = 0;
      this.a326(8192, var2);
    } else {
      for (var2 = 0; var2 < 16; ++var2) {
        this.a556(var2);
      }
    }
  }

  private void a430(final boolean var2) {
    if (var2) {
      this.b366(-1);
    } else {
      this.a093(-1);
    }

    this.a556(-1);

    int var3;
    for (var3 = 0; var3 < 16; ++var3) {
      this._I[var3] = this._m[var3];
    }

    for (var3 = 0; var3 < 16; ++var3) {
      this._P[var3] = this._m[var3] & -128;
    }

  }

  @SuppressWarnings("BooleanMethodIsAlwaysInverted")
  public boolean a258(final MixerTrackConfig_idk var2) {
    if (var2._K == null) {
      if (var2._E >= 0) {
        var2.unlink();
        if (var2._z > 0 && var2 == this._K[var2._y][var2._z]) {
          this._K[var2._y][var2._z] = null;
        }
      }

      return true;
    } else {
      return false;
    }
  }

  private void a366(final int var1) {
    switch (var1 & 240) {
      case 128: {
        this.a172(15 & var1, (32672 & var1) >> 8);
        break;
      }
      case 144: {
        final int var5a = var1 & 15;
        final int var6a = var1 >> 8 & 127;
        final int var7a = 127 & var1 >> 16;
        if (var7a > 0) {
          this.a842(var6a, var5a, var7a);
        } else {
          this.a172(var5a, var6a);
        }
        break;
      }
      case 160:
      case 208:
        break;
      case 176: {
        final int var5a = 15 & var1;
        final int var6a = 127 & var1 >> 8;
        final int var7a = 127 & var1 >> 16;
        if (var6a == 0) {
          this._P[var5a] = (var7a << 14) + (-2080769 & this._P[var5a]);
        }

        if (var6a == 32) {
          this._P[var5a] = (var7a << 7) + (-16257 & this._P[var5a]);
        }

        if (var6a == 1) {
          this._p[var5a] = (-16257 & this._p[var5a]) + (var7a << 7);
        }

        if (var6a == 33) {
          this._p[var5a] = var7a + (this._p[var5a] & -128);
        }

        if (var6a == 5) {
          this._J[var5a] = (-16257 & this._J[var5a]) + (var7a << 7);
        }

        if (var6a == 37) {
          this._J[var5a] = var7a + (this._J[var5a] & -128);
        }

        if (var6a == 7) {
          this._y[var5a] = (var7a << 7) + (-16257 & this._y[var5a]);
        }

        if (var6a == 39) {
          this._y[var5a] = (-128 & this._y[var5a]) + var7a;
        }

        if (var6a == 10) {
          this._G[var5a] = (var7a << 7) + (this._G[var5a] & -16257);
        }

        if (var6a == 42) {
          this._G[var5a] = (this._G[var5a] & -128) + var7a;
        }

        if (var6a == 11) {
          this._s[var5a] = (var7a << 7) + (-16257 & this._s[var5a]);
        }

        if (var6a == 43) {
          this._s[var5a] = var7a + (this._s[var5a] & -128);
        }

        if (var6a == 64) {
          if (var7a >= 64) {
            this._F[var5a] = this._F[var5a] | 1;
          } else {
            this._F[var5a] = this._F[var5a] & -2;
          }
        }

        if (var6a == 65) {
          if (var7a >= 64) {
            this._F[var5a] = this._F[var5a] | 2;
          } else {
            this.a540(var5a);
            this._F[var5a] = this._F[var5a] & -3;
          }
        }

        if (var6a == 99) {
          this._q[var5a] = (var7a << 7) + (127 & this._q[var5a]);
        }

        if (var6a == 98) {
          this._q[var5a] = var7a + (16256 & this._q[var5a]);
        }

        if (var6a == 101) {
          this._q[var5a] = (this._q[var5a] & 127) + 16384 + (var7a << 7);
        }

        if (var6a == 100) {
          this._q[var5a] = 16384 + (this._q[var5a] & 16256) + var7a;
        }

        if (var6a == 120) {
          this.b366(var5a);
        }

        if (var6a == 121) {
          this.a556(var5a);
        }

        if (var6a == 123) {
          this.a093(var5a);
        }

        int var8;
        if (var6a == 6) {
          var8 = this._q[var5a];
          if (var8 == 16384) {
            this._r[var5a] = (this._r[var5a] & -16257) + (var7a << 7);
          }
        }

        if (var6a == 38) {
          var8 = this._q[var5a];
          if (var8 == 16384) {
            this._r[var5a] = var7a + (this._r[var5a] & -128);
          }
        }

        if (var6a == 16) {
          this._t[var5a] = (var7a << 7) + (this._t[var5a] & -16257);
        }

        if (var6a == 48) {
          this._t[var5a] = (this._t[var5a] & -128) + var7a;
        }

        if (var6a == 81) {
          if (var7a >= 64) {
            this._F[var5a] = this._F[var5a] | 4;
          } else {
            this.d093(var5a);
            this._F[var5a] = this._F[var5a] & -5;
          }
        }

        if (var6a == 17) {
          this.a326((var7a << 7) + (-16257 & this._E[var5a]), var5a);
        }

        if (var6a == 49) {
          this.a326((this._E[var5a] & -128) + var7a, var5a);
        }

        break;
      }
      case 192: {
        final int var5a = var1 & 15;
        final int var6a = (32569 & var1) >> 8;
        this.a599(var5a, this._P[var5a] + var6a);
        break;
      }
      case 224: {
        final int var5a = 15 & var1;
        final int var6a = (127 & var1 >> 8) + (var1 >> 9 & 16256);
        this.b789(var5a, var6a);
        break;
      }
      default: {
        if ((var1 & 255) == 255) {
          this.a430(true);
        }
        break;
      }
    }
  }

  @Override
  public synchronized int a784() {
    return 0;
  }

  private synchronized void a918(final boolean var1, final SongData songData, final boolean var4) {
    this.b430(var4);
    this.midiReader.load(songData.midiData);
    this._v = var1;
    this._O = 0L;
    final int numTracks = this.midiReader.numTracks();

    for (int track = 0; track < numTracks; ++track) {
      this.midiReader.setCursorToTrackPlaybackPos(track);
      this.midiReader.advanceTrackTicks(track);
      this.midiReader.setTrackPlaybackPosToCursor(track);
    }

    this.trackWithSoonestEvent = this.midiReader.trackWithSoonestNextTick();
    this.nextEventTicks = this.midiReader.trackNextTick[this.trackWithSoonestEvent];
    this.currentPlayTime = this.midiReader.getPlayTime(this.nextEventTicks);
  }

  private void d093(final int var2) {
    if ((4 & this._F[var2]) != 0) {
      for (final MixerTrackConfig_idk var3 : this._l._n) {
        if (var3._y == var2) {
          var3._j = 0;
        }
      }
    }

  }

  private void a842(final int var1, final int var2, final int var3) {
    this.a172(var2, var1);
    if ((this._F[var2] & 2) != 0) {
      for (final Iterator<MixerTrackConfig_idk> it = this._l._n.descendingIterator(); it.hasNext(); ) {
        final MixerTrackConfig_idk var5 = it.next();
        if (var5._y == var2 && var5._E < 0) {
          this._N[var2][var5._H] = null;
          this._N[var2][var1] = var5;
          final int var6 = (var5._G * var5._r >> 12) + var5._J;
          var5._J += -var5._H + var1 << 8;
          var5._G = 4096;
          var5._r = -var5._J + var6;
          var5._H = var1;
          return;
        }
      }
    }

    final br_ var10 = this._Q.get(this._I[var2]);
    if (var10 != null) {
      final kk_ var11 = var10._h[var1];
      if (var11 != null) {
        final MixerTrackConfig_idk var7 = new MixerTrackConfig_idk();
        var7._y = var2;
        var7._A = var10;
        var7._M = var11;
        var7._u = var10._j[var1];
        var7._z = var10._r[var1];
        var7._H = var1;
        var7._k = var10._s[var1] * var3 * var3 * var10._q + 1024 >> 11;
        var7._q = var10._t[var1] & 255;
        var7._J = (var1 << 8) - (var10._k[var1] & 32767);
        var7._B = 0;
        var7._E = -1;
        var7._F = 0;
        var7._h = 0;
        var7._v = 0;
        if (this._t[var2] == 0) {
          var7._K = al_.a771(var11, this.b237(var7), this.generateSample(var7), this.a237(var7));
        } else {
          var7._K = al_.a771(var11, this.b237(var7), 0, this.a237(var7));
          this.a559(var7, var10._k[var1] < 0);
        }

        if (var10._k[var1] < 0) {
          assert var7._K != null;
          var7._K.f150();
        }

        if (var7._z >= 0) {
          final MixerTrackConfig_idk var9 = this._K[var2][var7._z];
          if (var9 != null && var9._E < 0) {
            this._N[var2][var9._H] = null;
            var9._E = 0;
          }

          this._K[var2][var7._z] = var7;
        }

        this._l._n.addLast(var7);
        this._N[var2][var1] = var7;
      }
    }
  }

  @SuppressWarnings("BooleanMethodIsAlwaysInverted")
  public boolean a543(final int var1, final int[] var2, final MixerTrackConfig_idk var4, final int var5) {
    var4._p = SampledAudioChannel.SAMPLES_PER_SECOND / 100;
    if (var4._E < 0 || var4._K != null && !var4._K.g801()) {
      int var6 = var4._G;
      if (var6 > 0) {
        var6 -= (int) (Math.pow(2.0D, (double) this._J[var4._y] * 4.921259842519685E-4D) * 16.0D + 0.5D);
        if (var6 < 0) {
          var6 = 0;
        }

        var4._G = var6;
      }

      var4._K.d150(this.b237(var4));
      final kc_ var7 = var4._u;
      var4._i += var7._o;
      ++var4._C;
      boolean var8 = false;
      final double var9 = 5.086263020833333E-6D * (double) ((var4._H - 60 << 8) + (var4._G * var4._r >> 12));
      if (var7._h > 0) {
        if (var7._a > 0) {
          var4._h += (int) (Math.pow(2.0D, var9 * (double) var7._a) * 128.0D + 0.5D);
        } else {
          var4._h += 128;
        }

        if (var7._h * var4._h >= 819200) {
          var8 = true;
        }
      }

      if (var7._n != null) {
        if (var7._k <= 0) {
          var4._F += 128;
        } else {
          var4._F += (int) (0.5D + Math.pow(2.0D, var9 * (double) var7._k) * 128.0D);
        }

        while (var4._B < var7._n.length - 2 && ('\uff00' & var7._n[2 + var4._B] << 8) < var4._F) {
          var4._B += 2;
        }

        if (var7._n.length - 2 == var4._B && var7._n[1 + var4._B] == 0) {
          var8 = true;
        }
      }

      if (var4._E >= 0 && var7._e != null && (1 & this._F[var4._y]) == 0 && (var4._z < 0 || this._K[var4._y][var4._z] != var4)) {
        if (var7._c <= 0) {
          var4._E += 128;
        } else {
          var4._E += (int) (0.5D + 128.0D * Math.pow(2.0D, (double) var7._c * var9));
        }

        while (var4._v < var7._e.length - 2 && var4._E > (255 & var7._e[2 + var4._v]) << 8) {
          var4._v += 2;
        }

        if (var7._e.length - 2 == var4._v) {
          var8 = true;
        }
      }

      if (var8) {
        var4._K.g150(var4._p);
        if (var2 == null) {
          var4._K.generateAudio2_idk(var5);
        } else {
          var4._K.generateAudio1_idk(var2, var1, var5);
        }

        if (var4._K.e801()) {
          this._l._o.addFirst(var4._K);
        }

        var4.d487();
        if (var4._E >= 0) {
          var4.unlink();
          if (var4._z > 0 && this._K[var4._y][var4._z] == var4) {
            this._K[var4._y][var4._z] = null;
          }
        }

        return true;
      } else {

        var4._K.a326(var4._p, this.generateSample(var4), this.a237(var4));
        return false;
      }
    } else {
      var4.d487();
      var4.unlink();
      if (var4._z > 0 && this._K[var4._y][var4._z] == var4) {
        this._K[var4._y][var4._z] = null;
      }

      return true;
    }
  }

  private void a599(final int var1, final int var2) {
    if (var2 != this._I[var1]) {
      this._I[var1] = var2;
      for (int i = 0; i < 128; ++i) {
        this._K[var1][i] = null;
      }
    }
  }

  private int a237(final MixerTrackConfig_idk var1) {
    final int var3 = this._G[var1._y];
    return var3 >= 8192 ? -(32 + (128 - var1._q) * (-var3 + 16384) >> 6) + 16384 : 32 + var1._q * var3 >> 6;
  }

  private void a423() {
    int track = this.trackWithSoonestEvent;
    int ticks = this.nextEventTicks;
    long var4 = this.currentPlayTime;

    if (this._z != null && ticks == 0) {
      this.a918(this._v, this._z, false);
      this.a423();
    } else {
      while (ticks == this.nextEventTicks) {
        while (this.midiReader.trackNextTick[track] == ticks) {
          this.midiReader.setCursorToTrackPlaybackPos(track);
          final int event = this.midiReader.readNextTrackEvent(track);

          if (event == MidiReader.EVENT_TRACK_END) {
            this.midiReader.resetCursor();
            this.midiReader.setTrackPlaybackPosToCursor(track);
            if (this.midiReader.allTracksStopped()) {
              if (this._z != null) {
                this.a077(this._z, this._v);
                this.a423();
                return;
              }

              if (!this._v || ticks == 0) {
                this.a430(true);
                this.midiReader.unload();
                return;
              }

              this.midiReader.resetPlayback(var4);
            }
            break;
          }

          if ((event & 128) != 0) {
            this.a366(event);
          }

          this.midiReader.advanceTrackTicks(track);
          this.midiReader.setTrackPlaybackPosToCursor(track);
        }

        track = this.midiReader.trackWithSoonestNextTick();
        ticks = this.midiReader.trackNextTick[track];
        var4 = this.midiReader.getPlayTime(ticks);
      }

      this.trackWithSoonestEvent = track;
      this.nextEventTicks = ticks;
      this.currentPlayTime = var4;
      if (this._z != null && ticks > 0) {
        this.trackWithSoonestEvent = -1;
        this.nextEventTicks = 0;
        this.currentPlayTime = this.midiReader.getPlayTime(this.nextEventTicks);
      }

    }
  }

  public void a559(final MixerTrackConfig_idk var2, final boolean var3) {

    int var4 = var2._M.data.length;
    int var5;
    if (var3 && var2._M._i) {
      final int var6 = -var2._M._l + var4 + var4;
      var5 = (int) ((long) this._t[var2._y] * (long) var6 >> 6);
      var4 <<= 8;
      if (var5 >= var4) {
        var2._K.c487();
        var5 = -var5 + var4 + var4 - 1;
      }
    } else {
      var5 = (int) ((long) this._t[var2._y] * (long) var4 >> 6);
    }

    var2._K.h150(var5);
  }

  private int generateSample(final MixerTrackConfig_idk var1) {
    if (this._A[var1._y] == 0) {
      return 0;
    }

    final kc_ var3 = var1._u;
    int x = this._y[var1._y] * this._s[var1._y] + 4096 >> 13;
    x = x * x + 16384 >> 15;
    x = var1._k * x + 16384 >> 15;
    x = 128 + this.volume * x >> 8;
    x = this._A[var1._y] * x + 128 >> 8;
    if (var3._h > 0) {
      x = (int) (0.5D + (double) x * Math.pow(0.5D, (double) var1._h * 1.953125E-5D * (double) var3._h));
    }

    int var5;
    int var6;
    int var7;
    int var8;
    if (var3._n != null) {
      var5 = var1._F;
      var6 = var3._n[1 + var1._B];
      if (var1._B < var3._n.length - 2) {
        var7 = var3._n[var1._B] << 8 & '\uff00';
        var8 = (255 & var3._n[2 + var1._B]) << 8;
        var6 += (-var6 + var3._n[var1._B + 3]) * (-var7 + var5) / (-var7 + var8);
      }

      x = var6 * x + 32 >> 6;
    }

    if (var1._E > 0 && var3._e != null) {
      var5 = var1._E;
      var6 = var3._e[1 + var1._v];
      if (var3._e.length - 2 > var1._v) {
        var7 = (255 & var3._e[var1._v]) << 8;
        var8 = var3._e[2 + var1._v] << 8 & '\uff00';
        var6 += (var3._e[var1._v + 3] - var6) * (-var7 + var5) / (-var7 + var8);
      }

      x = 32 + x * var6 >> 6;
    }

    return x;
  }

  public synchronized void initialize() {
    this._m[9] = 128;
    this._P[9] = 128;
    this.a599(9, 128);
  }

  @SuppressWarnings("BooleanMethodIsAlwaysInverted")
  public synchronized boolean h154() {
    return this.midiReader.isLoaded();
  }
}
