package funorb.audio;

import funorb.data.NodeList;
import org.jetbrains.annotations.NotNull;

import java.util.Iterator;
import java.util.Objects;

public final class MidiPlayerNoteSet_idk extends AudioSource {
  public final NodeList<MidiPlayerNoteState_idk> notes = new NodeList<>();
  public final SoundManager sum = new SoundManager();
  private final MidiPlayer midiPlayer;

  public MidiPlayerNoteSet_idk(final MidiPlayer midiPlayer) {
    this.midiPlayer = midiPlayer;
  }

  @Override
  public @NotNull Iterator<AudioSource> iterator() {
    return this.notes.stream().<AudioSource>map(var1 -> var1.playback).filter(Objects::nonNull).iterator();
  }

  @Override
  public int returns_0_1_or_2() {
    return 0;
  }

  @Override
  public void processAndDiscard(final int len) {
    this.sum.processAndDiscard(len);

    for (final MidiPlayerNoteState_idk note : this.notes) {
      if (!this.midiPlayer.a258(note)) {
        int var2 = len;

        do {
          if (note.lenRemaining_idk >= var2) {
            this.a222(note, var2);
            note.lenRemaining_idk -= var2;
            break;
          }

          this.a222(note, note.lenRemaining_idk);
          var2 -= note.lenRemaining_idk;
        } while (!this.midiPlayer.a543(0, null, note, var2));
      }
    }

  }

  @Override
  public void processAndWrite(final int[] dataS16P8, final int totalOffset, final int totalLen) {
    this.sum.processAndWrite(dataS16P8, totalOffset, totalLen);

    for (final MidiPlayerNoteState_idk note : this.notes) {
      if (this.midiPlayer.a258(note)) {
        continue;
      }

      int offset = totalOffset;
      int len = totalLen;

      do {
        if (note.lenRemaining_idk >= len) {
          this.a829(dataS16P8, offset, note, len, offset + len);
          note.lenRemaining_idk -= len;
          break;
        }

        this.a829(dataS16P8, offset, note, note.lenRemaining_idk, offset + len);
        len -= note.lenRemaining_idk;
        offset += note.lenRemaining_idk;
      } while (!this.midiPlayer.a543(offset, dataS16P8, note, len));
    }

  }

  private void a222(final MidiPlayerNoteState_idk note, int totalLen) {
    if ((MidiPlayer.FLAG_GENERAL_6 & this.midiPlayer.chFlags[note.channel]) != 0 && note.notePlaying_idfk < 0) {
      final int var4 = this.midiPlayer._u[note.channel] / SampledAudioChannelS16.SAMPLE_RATE;
      final int var5 = (1048575 + var4 - note._j) / var4;
      note._j = 1048575 & note._j + totalLen * var4;
      if (var5 <= totalLen) {
        if (this.midiPlayer.chGeneral1[note.channel] == 0) {
          note.playback = RawSamplePlayer.start(note.sampleData, note.playback.getSpeed_p8(), note.playback.getVol_p14(), note.playback.getPan_p14());
        } else {
          note.playback = RawSamplePlayer.start(note.sampleData, note.playback.getSpeed_p8(), 0, note.playback.getPan_p14());
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

  private void a829(final int[] dest, int offset, final MidiPlayerNoteState_idk note, int lenToWrite, final int totalLen) {
    if ((MidiPlayer.FLAG_GENERAL_6 & this.midiPlayer.chFlags[note.channel]) != 0 && note.notePlaying_idfk < 0) {
      final int var7 = this.midiPlayer._u[note.channel] / SampledAudioChannelS16.SAMPLE_RATE;

      while (true) {
        final int len = (-note._j + 0xfffff + var7) / var7;
        if (len > lenToWrite) {
          note._j += var7 * lenToWrite;
          break;
        }

        note.playback.processAndWrite(dest, offset, len);
        note._j += len * var7 - 0x100000;
        offset += len;
        lenToWrite -= len;
        int var9 = SampledAudioChannelS16.SAMPLE_RATE / 100;
        final int var10 = 0x40000 / var7;
        if (var10 < var9) {
          var9 = var10;
        }

        final RawSamplePlayer playback = note.playback;
        if (this.midiPlayer.chGeneral1[note.channel] == 0) {
          note.playback = RawSamplePlayer.start(note.sampleData, playback.getSpeed_p8(), playback.getVol_p14(), playback.getPan_p14());
        } else {
          note.playback = RawSamplePlayer.start(note.sampleData, playback.getSpeed_p8(), 0, playback.getPan_p14());
          this.midiPlayer.a559(note, note.instrument.noteTuning_idk[note.note] < 0);
          note.playback.setVolRamped_p14(var9, playback.getVol_p14());
        }

        if (note.instrument.noteTuning_idk[note.note] < 0) {
          assert note.playback != null;
          note.playback.f150();
        }

        playback.setVolZeroRamped(var9);
        playback.processAndWrite(dest, offset, totalLen - offset);
        if (playback.isRampTimeNonzero()) {
          this.sum.addFirst(playback);
        }
      }
    }

    note.playback.processAndWrite(dest, offset, lenToWrite);
  }
}
