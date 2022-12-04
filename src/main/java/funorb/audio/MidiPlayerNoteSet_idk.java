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

  private void a222(final MidiPlayerNoteState_idk note, int totalLen) {
    if ((MidiPlayer.FLAG_GENERAL_6 & this.midiPlayer.chFlags[note.channel]) != 0 && note.notePlaying_idfk < 0) {
      final int var4 = this.midiPlayer._u[note.channel] / SampledAudioChannel.SAMPLES_PER_SECOND;
      final int var5 = (1048575 + var4 - note._j) / var4;
      note._j = 1048575 & note._j + totalLen * var4;
      if (var5 <= totalLen) {
        if (this.midiPlayer.chGeneral1[note.channel] == 0) {
          note.playback = AudioSamplePlayback_idk.start(note.sampleData, note.playback.getPitchX(), note.playback.getVolX(), note.playback.getPanX());
        } else {
          note.playback = AudioSamplePlayback_idk.start(note.sampleData, note.playback.getPitchX(), 0, note.playback.getPanX());
          this.midiPlayer.a559(note, note.instrument.noteTuning_idk[note.note] < 0);
        }

        if (note.instrument.noteTuning_idk[note.note] < 0) {
          assert note.playback != null;
          note.playback.f150();
        }

        totalLen = note._j / var4;
      }
    }

    assert note.playback != null;
    note.playback.processAndDiscard(totalLen);
  }

  private void a829(final int[] dest, int offset, final MidiPlayerNoteState_idk note, int totalLen, final int var6) {
    if ((MidiPlayer.FLAG_GENERAL_6 & this.midiPlayer.chFlags[note.channel]) != 0 && note.notePlaying_idfk < 0) {
      final int var7 = this.midiPlayer._u[note.channel] / SampledAudioChannel.SAMPLES_PER_SECOND;

      while (true) {
        final int len = (-note._j + 0xfffff + var7) / var7;
        if (len > totalLen) {
          note._j += var7 * totalLen;
          break;
        }

        note.playback.processAndWrite(dest, offset, len);
        note._j += len * var7 - 0x100000;
        offset += len;
        totalLen -= len;
        int var9 = SampledAudioChannel.SAMPLES_PER_SECOND / 100;
        final int var10 = 0x40000 / var7;
        if (var10 < var9) {
          var9 = var10;
        }

        final AudioSamplePlayback_idk playback = note.playback;
        if (this.midiPlayer.chGeneral1[note.channel] == 0) {
          note.playback = AudioSamplePlayback_idk.start(note.sampleData, playback.getPitchX(), playback.getVolX(), playback.getPanX());
        } else {
          note.playback = AudioSamplePlayback_idk.start(note.sampleData, playback.getPitchX(), 0, playback.getPanX());
          this.midiPlayer.a559(note, note.instrument.noteTuning_idk[note.note] < 0);
          note.playback.a093(var9, playback.getVolX());
        }

        if (note.instrument.noteTuning_idk[note.note] < 0) {
          assert note.playback != null;
          note.playback.f150();
        }

        playback.g150(var9);
        playback.processAndWrite(dest, offset, var6 - offset);
        if (playback.volDeltaNonZero()) {
          this.sum.addFirst(playback);
        }
      }
    }

    note.playback.processAndWrite(dest, offset, totalLen);
  }
}
