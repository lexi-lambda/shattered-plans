package funorb.audio;

import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;

public final class MusicManager extends AudioSource {
  private static final int P20_ONE = 0x100000;
  private static final int P20_ONE_HALF = 0x80000;

  public final MidiPlayer midiPlayer1;
  public final MidiPlayer midiPlayer2;
  private int[] midiBuffer2;
  private SongData song1;
  private int _l;
  private int crossfade_p20 = P20_ONE;
  private SongData song2;
  private int[] midiBuffer1;
  private int volume_p8 = 256;
  private boolean _C;

  public MusicManager() {
    final MidiPlayer midiPlayer = new MidiPlayer();
    this.midiPlayer2 = midiPlayer;
    this.midiPlayer1 = new MidiPlayer(midiPlayer);
  }

  public boolean a419(final SongData var1) {
    return var1 == this.song2 || var1 == this.song1;
  }

  @Override
  public void processAndWrite(final int[] data_s16p8, final int offset, final int len) {
    if (this.volume_p8 <= 0) {
      this.processAndDiscard(len);
      return;
    }

    if (this._C) {
      if (this._l > 0 && !this.midiPlayer2.h154()) {
        this._C = false;
        this._l = -this._l;
        this.song2 = null;
      } else if (this._l < 0 && !this.midiPlayer1.h154()) {
        this.song1 = null;
        this._C = false;
        this._l = -this._l;
      }
    }

    final int midiAmp2_p8 = (this.crossfade_p20 >> 12) * this.volume_p8 / 256;
    final int midiAmp1_p8 = this.volume_p8 - midiAmp2_p8;

    if (this._l != 0) {
      this.crossfade_p20 += this._l * len;
      if (this.crossfade_p20 >= P20_ONE) {
        this.crossfade_p20 = P20_ONE;
        if (!this._C) {
          this._l = 0;
          if (this.song1 != null) {
            this.midiPlayer1.e150();
          }

          this.song1 = null;
        }
      } else if (this.crossfade_p20 <= 0) {
        this.crossfade_p20 = 0;
        if (!this._C) {
          this._l = 0;
          if (this.song2 != null) {
            this.midiPlayer2.e150();
          }

          this.song2 = null;
        }
      }
    }

    final int lenSamples = len << 1;
    if (this.song2 != null || this.song1 != null) {
      if (midiAmp2_p8 == 256) {
        this.midiPlayer2.processAndWrite(data_s16p8, offset, len);
      } else if (midiAmp1_p8 == 256) {
        this.midiPlayer1.processAndWrite(data_s16p8, offset, len);
      } else {
        if (this.midiBuffer2 != null && this.midiBuffer2.length >= lenSamples) {
          Arrays.fill(this.midiBuffer2, 0, lenSamples, 0);
          Arrays.fill(this.midiBuffer1, 0, lenSamples, 0);
        } else {
          this.midiBuffer1 = new int[lenSamples];
          this.midiBuffer2 = new int[lenSamples];
        }

        this.midiPlayer2.processAndWrite(this.midiBuffer2, 0, len);
        this.midiPlayer1.processAndWrite(this.midiBuffer1, 0, len);
        final int offsetSamples = offset << 1;

        for (int i = 0; i < lenSamples; ++i) {
          data_s16p8[offsetSamples + i] += this.midiBuffer1[i] * midiAmp1_p8 + this.midiBuffer2[i] * midiAmp2_p8 >> 8;
        }
      }
    }

  }

  @Override
  public synchronized void processAndDiscard(final int len) {
    if (this.crossfade_p20 > 0 && this.song2 != null) {
      this.midiPlayer2.processAndDiscard(len);
    }

    if (this.crossfade_p20 < P20_ONE && this.song1 != null) {
      this.midiPlayer1.processAndDiscard(len);
    }

    if (this._C) {
      if (this._l > 0 && !this.midiPlayer2.h154()) {
        this._C = false;
        this.song2 = null;
        this._l = -this._l;
      } else if (this._l < 0 && !this.midiPlayer1.h154()) {
        this._C = false;
        this._l = -this._l;
        this.song1 = null;
      }
    }

    if (this._l != 0) {
      this.crossfade_p20 += this._l * len;
      if (this.crossfade_p20 >= P20_ONE) {
        this.crossfade_p20 = P20_ONE;
        if (!this._C) {
          this._l = 0;
          if (this.song1 != null) {
            this.midiPlayer1.e150();
          }

          this.song1 = null;
        }
      } else if (this.crossfade_p20 <= 0) {
        this.crossfade_p20 = 0;
        if (!this._C) {
          this._l = 0;
          if (this.song2 != null) {
            this.midiPlayer2.e150();
          }

          this.song2 = null;
        }
      }
    }

  }

  private void a633(final MidiPlayer midiPlayer) {
    midiPlayer.initChGlobalAmp();
    midiPlayer.initMicrosecondsPerSecond();
  }

  public synchronized void setVolume_p8(final int volume_p8) {
    this.volume_p8 = volume_p8;
  }

  @Override
  public synchronized int returns_0_1_or_2() {
    return 2;
  }

  @Override
  public @NotNull Iterator<AudioSource> iterator() {
    return Collections.emptyIterator();
  }

  public synchronized void a180(final SongData track, final int var5, final boolean var4) {
    if (this._C && var4) {
      if (this._l > 0) {
        if (this.song2 != null) {
          this.midiPlayer2.e150();
        }

        this.song2 = track;
        if (track != null) {
          this.midiPlayer2.changeSong(track, false);
          this.a633(this.midiPlayer2);
        }
      } else {
        if (this.song1 != null) {
          this.midiPlayer1.e150();
        }

        this.song1 = track;
        if (track != null) {
          this.midiPlayer1.changeSong(track, false);
          this.a633(this.midiPlayer1);
        }
      }

    } else {
      this._C = var4;
      if (this.song2 == track) {
        this._l = var5;
        this.a633(this.midiPlayer2);
      } else if (track == this.song1) {
        this._l = -var5;
        this.a633(this.midiPlayer1);
      } else {
        final boolean var7;
        if (this.song2 == null) {
          var7 = true;
        } else if (this.song1 == null) {
          var7 = false;
        } else {
          var7 = this.crossfade_p20 < P20_ONE_HALF;
        }

        if (var7) {
          if (this.song2 != null) {
            this.midiPlayer2.e150();
          }

          this.song2 = track;
          if (track != null) {
            this.midiPlayer2.changeSong(track, !var4);
            this.a633(this.midiPlayer2);
          }

          this._l = var5;
        } else {
          if (this.song1 != null) {
            this.midiPlayer1.e150();
          }

          this.song1 = track;
          if (track != null) {
            this.midiPlayer1.changeSong(track, !var4);
            this.a633(this.midiPlayer1);
          }

          this._l = -var5;
        }
      }
    }
  }
}
