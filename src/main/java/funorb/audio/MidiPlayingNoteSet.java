package funorb.audio;

import funorb.data.NodeList;
import org.jetbrains.annotations.NotNull;

import java.util.Iterator;
import java.util.Objects;

public final class MidiPlayingNoteSet extends AudioSource {
  public final NodeList<MidiPlayingNote> notes = new NodeList<>();
  public final SoundManager sum = new SoundManager();
  private final MidiPlayer midiPlayer;

  public MidiPlayingNoteSet(final MidiPlayer midiPlayer) {
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
  public void processAndDiscard(final int totalLen) {
    this.sum.processAndDiscard(totalLen);

    for (final MidiPlayingNote note : this.notes) {
      if (this.midiPlayer.noteIgnored(note)) {
        continue;
      }

      int len = totalLen;

      do {
        if (note.timeLeft >= len) {
          this.processAndDiscardNote(note, len);
          note.timeLeft -= len;
          break;
        }

        this.processAndDiscardNote(note, note.timeLeft);
        len -= note.timeLeft;
      } while (!this.midiPlayer.processNote(0, null, note, len));
    }

  }

  @Override
  public void processAndWrite(final int[] data_s16p8, final int totalOffset, final int totalLen) {
    this.sum.processAndWrite(data_s16p8, totalOffset, totalLen);

    for (final MidiPlayingNote note : this.notes) {
      if (this.midiPlayer.noteIgnored(note)) {
        continue;
      }

      int offset = totalOffset;
      int len = totalLen;

      do {
        if (note.timeLeft >= len) {
          this.processAndWriteNote(data_s16p8, offset, note, len, offset + len);
          note.timeLeft -= len;
          break;
        }

        this.processAndWriteNote(data_s16p8, offset, note, note.timeLeft, offset + len);
        len -= note.timeLeft;
        offset += note.timeLeft;
      } while (!this.midiPlayer.processNote(offset, data_s16p8, note, len));
    }

  }

  private void processAndDiscardNote(final MidiPlayingNote note, int totalLen) {
    if ((MidiPlayer.FLAG_GENERAL_6 & this.midiPlayer.chFlags[note.channel]) != 0 && note.relEnvTime < 0) {
      final int var4 = this.midiPlayer.chGeneral2Exp[note.channel] / SampledAudioChannelS16.SAMPLE_RATE;
      final int var5 = (0xfffff + var4 - note._j) / var4;
      note._j = 0xfffff & note._j + totalLen * var4;
      if (var5 <= totalLen) {
        if (this.midiPlayer.chStartOffset_p6[note.channel] == 0) {
          note.playback = RawSamplePlayer.of(note.sampleData, note.playback.getAbsSpeed_p8(), note.playback.getVol_p14(), note.playback.getPan_p14());
        } else {
          note.playback = RawSamplePlayer.of(note.sampleData, note.playback.getAbsSpeed_p8(), 0, note.playback.getPan_p14());
          this.midiPlayer.initPlayhead(note, note.instrument.baseNote_p8[note.note] < 0);
        }

        if (note.instrument.baseNote_p8[note.note] < 0) {
          assert note.playback != null;
          note.playback.setLooped();
        }

        totalLen = note._j / var4;
      }
    }

    assert note.playback != null;
    note.playback.processAndDiscard(totalLen);
  }

  private void processAndWriteNote(final int[] dest_s16p8, int offset, final MidiPlayingNote note, int lenToWrite, final int totalLen) {
    if ((MidiPlayer.FLAG_GENERAL_6 & this.midiPlayer.chFlags[note.channel]) != 0 && note.relEnvTime < 0) {
      final int var7 = this.midiPlayer.chGeneral2Exp[note.channel] / SampledAudioChannelS16.SAMPLE_RATE;

      while (true) {
        final int len = (-note._j + 0xfffff + var7) / var7;
        if (len > lenToWrite) {
          note._j += var7 * lenToWrite;
          break;
        }

        note.playback.processAndWrite(dest_s16p8, offset, len);
        note._j += len * var7 - 0x100000;
        offset += len;
        lenToWrite -= len;
        int timeLeft = SampledAudioChannelS16.SAMPLE_RATE / 100;
        final int var10 = 0x40000 / var7;
        if (var10 < timeLeft) {
          timeLeft = var10;
        }

        final RawSamplePlayer playback = note.playback;
        if (this.midiPlayer.chStartOffset_p6[note.channel] == 0) {
          note.playback = RawSamplePlayer.of(note.sampleData, playback.getAbsSpeed_p8(), playback.getVol_p14(), playback.getPan_p14());
        } else {
          note.playback = RawSamplePlayer.of(note.sampleData, playback.getAbsSpeed_p8(), 0, playback.getPan_p14());
          this.midiPlayer.initPlayhead(note, note.instrument.baseNote_p8[note.note] < 0);
          note.playback.setVolRamped_p14(timeLeft, playback.getVol_p14());
        }

        if (note.instrument.baseNote_p8[note.note] < 0) {
          assert note.playback != null;
          note.playback.setLooped();
        }

        playback.setVolZeroRamped(timeLeft);
        playback.processAndWrite(dest_s16p8, offset, totalLen - offset);
        if (playback.isRampTimeNonzero()) {
          this.sum.addFirst(playback);
        }
      }
    }

    note.playback.processAndWrite(dest_s16p8, offset, lenToWrite);
  }
}
