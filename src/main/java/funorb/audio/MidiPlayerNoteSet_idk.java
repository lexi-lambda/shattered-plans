package funorb.audio;

import funorb.data.NodeList;
import org.jetbrains.annotations.NotNull;

import java.util.Iterator;
import java.util.Objects;

public final class MidiPlayerNoteSet_idk extends AudioSource_idk {
  public final NodeList<MidiPlayerNoteState_idk> notes = new NodeList<>();
  public final AudioSourceSum_idk sum = new AudioSourceSum_idk();
  private final MidiPlayer midiPlayer;

  public MidiPlayerNoteSet_idk(final MidiPlayer midiPlayer) {
    this.midiPlayer = midiPlayer;
  }

  @Override
  public @NotNull Iterator<AudioSource_idk> iterator() {
    return this.notes.stream().<AudioSource_idk>map(var1 -> var1.playback).filter(Objects::nonNull).iterator();
  }

  @Override
  public int a784() {
    return 0;
  }

  @Override
  public void processAndDiscard(final int len) {
    this.sum.processAndDiscard(len);

    for (final MidiPlayerNoteState_idk note : this.notes) {
      if (!this.midiPlayer.a258(note)) {
        int var2 = len;

        do {
          if (note._p >= var2) {
            this.a222(note, var2);
            note._p -= var2;
            break;
          }

          this.a222(note, note._p);
          var2 -= note._p;
        } while (!this.midiPlayer.a543(0, null, note, var2));
      }
    }

  }

  @Override
  public void processAndWrite(final int[] dest, final int offset, final int len) {
    this.sum.processAndWrite(dest, offset, len);

    for (final MidiPlayerNoteState_idk note : this.notes) {
      if (!this.midiPlayer.a258(note)) {
        int var4 = offset;
        int var5 = len;

        do {
          if (note._p >= var5) {
            this.a829(dest, var4, note, var5, var4 + var5);
            note._p -= var5;
            break;
          }

          this.a829(dest, var4, note, note._p, var4 + var5);
          var5 -= note._p;
          var4 += note._p;
        } while (!this.midiPlayer.a543(var4, dest, note, var5));
      }
    }

  }

  private void a222(final MidiPlayerNoteState_idk var2, int var3) {
    if ((4 & this.midiPlayer.chFlags[var2.channel]) != 0 && var2.notePlaying_idfk < 0) {
      final int var4 = this.midiPlayer._u[var2.channel] / SampledAudioChannel.SAMPLES_PER_SECOND;
      final int var5 = (1048575 + var4 - var2._j) / var4;
      var2._j = 1048575 & var2._j + var3 * var4;
      if (var5 <= var3) {
        if (this.midiPlayer.chGeneral1[var2.channel] == 0) {
          var2.playback = AudioSamplePlayback_idk.start(var2.sampleData, var2.playback.f784(), var2.playback.getVolume(), var2.playback.panClampedX());
        } else {
          var2.playback = AudioSamplePlayback_idk.start(var2.sampleData, var2.playback.f784(), 0, var2.playback.panClampedX());
          this.midiPlayer.a559(var2, var2.instrument.noteTuning_idk[var2.note] < 0);
        }

        if (var2.instrument.noteTuning_idk[var2.note] < 0) {
          assert var2.playback != null;
          var2.playback.f150();
        }

        var3 = var2._j / var4;
      }
    }

    assert var2.playback != null;
    var2.playback.processAndDiscard(var3);
  }

  private void a829(final int[] var1, int var2, final MidiPlayerNoteState_idk var3, int var4, final int var6) {
    if ((4 & this.midiPlayer.chFlags[var3.channel]) != 0 && var3.notePlaying_idfk < 0) {
      final int var7 = this.midiPlayer._u[var3.channel] / SampledAudioChannel.SAMPLES_PER_SECOND;

      while (true) {
        final int var8 = (-var3._j + 1048575 + var7) / var7;
        if (var8 > var4) {
          var3._j += var7 * var4;
          break;
        }

        var3.playback.processAndWrite(var1, var2, var8);
        var3._j += var8 * var7 - 1048576;
        var2 += var8;
        var4 -= var8;
        int var9 = SampledAudioChannel.SAMPLES_PER_SECOND / 100;
        final int var10 = 262144 / var7;
        if (var10 < var9) {
          var9 = var10;
        }

        final AudioSamplePlayback_idk var11 = var3.playback;
        if (this.midiPlayer.chGeneral1[var3.channel] == 0) {
          var3.playback = AudioSamplePlayback_idk.start(var3.sampleData, var11.f784(), var11.getVolume(), var11.panClampedX());
        } else {
          var3.playback = AudioSamplePlayback_idk.start(var3.sampleData, var11.f784(), 0, var11.panClampedX());
          this.midiPlayer.a559(var3, var3.instrument.noteTuning_idk[var3.note] < 0);
          var3.playback.a093(var9, var11.getVolume());
        }

        if (var3.instrument.noteTuning_idk[var3.note] < 0) {
          assert var3.playback != null;
          var3.playback.f150();
        }

        var11.g150(var9);
        var11.processAndWrite(var1, var2, var6 - var2);
        if (var11.volDeltaNonZero()) {
          this.sum.addFirst(var11);
        }
      }
    }

    var3.playback.processAndWrite(var1, var2, var4);
  }
}
