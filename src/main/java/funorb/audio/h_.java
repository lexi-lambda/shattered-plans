package funorb.audio;

import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;

public final class h_ extends tn_ {
  public final ga_ _r;
  public final ga_ _u;
  private int[] _D;
  private MusicTrack _E;
  private int _l;
  private int _n = 0x100000;
  private MusicTrack _z;
  private int[] _p;
  private int volume = 256;
  private boolean _C;

  public h_() {
    final ga_ var1 = new ga_();
    this._u = var1;
    this._r = new ga_(var1);
  }

  public boolean a419(final MusicTrack var1) {
    return var1 == this._z || var1 == this._E;
  }

  @Override
  public void b397(final int[] dest, final int offset, final int len) {
    if (this.volume <= 0) {
      this.a150(len);
    } else {
      if (this._C) {
        if (this._l > 0 && !this._u.h154()) {
          this._C = false;
          this._l = -this._l;
          this._z = null;
        } else if (this._l < 0 && !this._r.h154()) {
          this._E = null;
          this._C = false;
          this._l = -this._l;
        }
      }

      final int var4 = (this._n >> 12) * this.volume / 256;
      final int var5 = this.volume - var4;
      if (this._l != 0) {
        this._n += this._l * len;
        if (this._n >= 1048576) {
          this._n = 1048576;
          if (!this._C) {
            this._l = 0;
            if (this._E != null) {
              this._r.e150();
            }

            this._E = null;
          }
        } else if (this._n <= 0) {
          this._n = 0;
          if (!this._C) {
            this._l = 0;
            if (this._z != null) {
              this._u.e150();
            }

            this._z = null;
          }
        }
      }

      final int var6 = len << 1;
      final int var7;
      int var8;
      if (this._z != null || this._E != null) {
        if (var4 == 256) {
          this._u.b397(dest, offset, len);
        } else if (var5 == 256) {
          this._r.b397(dest, offset, len);
        } else {
          if (this._D != null && this._D.length >= var6) {
            Arrays.fill(this._D, 0, var6, 0);
            Arrays.fill(this._p, 0, var6, 0);
          } else {
            this._p = new int[var6];
            this._D = new int[var6];
          }

          this._u.b397(this._D, 0, len);
          this._r.b397(this._p, 0, len);
          var7 = offset << 1;

          for (var8 = 0; var6 > var8; ++var8) {
            dest[var7 + var8] += this._p[var8] * var5 + this._D[var8] * var4 >> 8;
          }
        }
      }

    }
  }

  @Override
  public synchronized void a150(final int len) {
    if (this._n > 0 && this._z != null) {
      this._u.a150(len);
    }

    if (this._n < 1048576 && this._E != null) {
      this._r.a150(len);
    }

    if (this._C) {
      if (this._l > 0 && !this._u.h154()) {
        this._C = false;
        this._z = null;
        this._l = -this._l;
      } else if (this._l < 0 && !this._r.h154()) {
        this._C = false;
        this._l = -this._l;
        this._E = null;
      }
    }

    if (this._l != 0) {
      this._n += this._l * len;
      if (this._n >= 1048576) {
        this._n = 1048576;
        if (!this._C) {
          this._l = 0;
          if (this._E != null) {
            this._r.e150();
          }

          this._E = null;
        }
      } else if (this._n <= 0) {
        this._n = 0;
        if (!this._C) {
          this._l = 0;
          if (this._z != null) {
            this._u.e150();
          }

          this._z = null;
        }
      }
    }

  }

  private void a633(final ga_ var3) {
    var3.a679();
    var3.c430();
  }

  public synchronized void setVolume(final int volume) {
    this.volume = volume;
  }

  @Override
  public synchronized int a784() {
    return 2;
  }

  @Override
  public @NotNull Iterator<tn_> iterator() {
    return Collections.emptyIterator();
  }

  public synchronized void a180(final MusicTrack track, final int var5, final boolean var4) {
    if (this._C && var4) {
      if (this._l > 0) {
        if (this._z != null) {
          this._u.e150();
        }

        this._z = track;
        if (track != null) {
          this._u.a077(track, false);
          this.a633(this._u);
        }
      } else {
        if (this._E != null) {
          this._r.e150();
        }

        this._E = track;
        if (track != null) {
          this._r.a077(track, false);
          this.a633(this._r);
        }
      }

    } else {
      this._C = var4;
      if (this._z == track) {
        this._l = var5;
        this.a633(this._u);
      } else if (track == this._E) {
        this._l = -var5;
        this.a633(this._r);
      } else {
        final boolean var7;
        if (this._z == null) {
          var7 = true;
        } else if (this._E == null) {
          var7 = false;
        } else {
          var7 = this._n < 524288;
        }

        if (var7) {
          if (this._z != null) {
            this._u.e150();
          }

          this._z = track;
          if (track != null) {
            this._u.a077(track, !var4);
            this.a633(this._u);
          }

          this._l = var5;
        } else {
          if (this._E != null) {
            this._r.e150();
          }

          this._E = track;
          if (track != null) {
            this._r.a077(track, !var4);
            this.a633(this._r);
          }

          this._l = -var5;
        }
      }
    }
  }
}
