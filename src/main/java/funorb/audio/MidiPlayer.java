package funorb.audio;

import funorb.cache.ResourceLoader;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

// "play time" has units of tick-microseconds per quarter note, i.e. it has an extra factor of ticks per
// quarter note in it, which is constant for any given song. "play time" is abbreviated to "pt" in many places
public final class MidiPlayer extends AudioSource {
  private static final double P9_TURNS_TO_RADIANS = 0.01227184630308513D;
  private static final double P8_NOTE_TO_OCTAVE = 3.255208333333333E-4D;

  private static final int FLAG_SUSTAIN = 0x0001;
  private static final int FLAG_PORTAMENTO = 0x0002;
  public static final int FLAG_GENERAL_6 = 0x0004;

  public final int[] chStartOffset_p6 = new int[16]; // GP controller 1
  private final int[] chGeneral2 = new int[16]; // GP controller 2
  public final int[] chGeneral2Exp = new int[16];
  public final int[] chFlags = new int[16];
  private final int[] chModWheel = new int[16];
  private final int[] chVol_p14 = new int[16];
  private final int[] chRpn = new int[16];
  private final MidiPlayingNote[][] noteStates = new MidiPlayingNote[16][128];
  private final int[] chPitchBendRange = new int[16];
  private final int[] chDefaultBank_idk = new int[16];
  private final Map<Integer, MidiInstrument> instruments;
  private final int[] chPan_p14 = new int[16];
  private final MidiReader midiReader = new MidiReader();
  private final int[] chCurrentProgram = new int[16];
  private final int[] chBank = new int[16];
  private final int[] chPortaTime = new int[16];
  private final int[] chGlobalAmp_p8 = new int[16];
  private final int[] chPitchWheel = new int[16];
  private final MidiPlayingNoteSet noteSet = new MidiPlayingNoteSet(this);
  private int amp_p8 = 256;
  private final MidiPlayingNote[][] noteOffStates_idk = new MidiPlayingNote[16][128];
  private int usPerSec = 1000000;
  private final int[] chExpression_p14 = new int[16];
  private int trackWithSoonestEvent;
  private long outputPt;
  private int nextEventTicks;
  private long playerPt;
  private boolean looped;
  private SongData songData;

  public MidiPlayer() {
    this.instruments = new HashMap<>();
    this.initChGlobalAmp();
    this.handleReset(true);
  }

  @SuppressWarnings("CopyConstructorMissesField")
  public MidiPlayer(final MidiPlayer copyInstruments) {
    this.instruments = copyInstruments.instruments;
    this.initChGlobalAmp();
    this.handleReset(true);
  }

  private static MidiInstrument loadInstrument(final ResourceLoader loader, final int instrumentId) {
    final byte[] instrumentData = loader.getSingletonResource(instrumentId);
    return instrumentData == null ? null : new MidiInstrument(instrumentData);
  }

  public synchronized void e150() {
    this.reset(true);
  }

  private void handlePitchWheel(final int channel, final int value) {
    this.chPitchWheel[channel] = value;
  }

  @Override
  public @NotNull Iterator<AudioSource> iterator() {
    return Collections.<AudioSource>singletonList(this.noteSet).iterator();
  }

  private void handleGeneral2(final int value, final int channel) {
    this.chGeneral2[channel] = value;
    this.chGeneral2Exp[channel] = (int) (0.5D + 2097152.0D * Math.pow(2.0D, 5.4931640625E-4D * (double) value));
  }

  private int calcSpeed_p8(final MidiPlayingNote note) {
    int relNote_p8 = (note.portaRange_p8 * note.portaMag_p12 >> 12) + note.relNote_p8;
    relNote_p8 += this.chPitchBendRange[note.channel] * (this.chPitchWheel[note.channel] - 8192) >> 12;
    final MidiKeyParams params = note.params;

    int vibrato_p8;
    if (params.vibratoPhaseSpeed_p9 > 0 && (params.vibrato_p6 > 0 || this.chModWheel[note.channel] > 0)) {
      vibrato_p8 = params.vibrato_p6 << 2;
      final int vibratoAttack = params.vibratoAttack << 1;
      if (note.vibratoTime < vibratoAttack) {
        vibrato_p8 = note.vibratoTime * vibrato_p8 / vibratoAttack;
      }

      vibrato_p8 += this.chModWheel[note.channel] >> 7;
      relNote_p8 += (int) (vibrato_p8 * Math.sin((note.vibratoPhase_p9 & 0x1ff) * P9_TURNS_TO_RADIANS));
    }

    final int speed_p8 = (int)(
      (double)(256 * note.sampleData.sampleRate)
      * Math.pow(2.0D, P8_NOTE_TO_OCTAVE * relNote_p8) / (double) SampledAudioChannelS16.SAMPLE_RATE
      + 0.5D);
    return Math.max(speed_p8, 1);
  }

  public synchronized void f150() {
    for (final MidiInstrument instr : this.instruments.values()) {
      instr.e150();
    }
  }

  private void handleAllNotesOff(final int channel) {
    for (final MidiPlayingNote state : this.noteSet.notes) {
      if ((channel < 0 || state.channel == channel) && state.relEnvTime < 0) {
        this.noteStates[state.channel][state.note] = null;
        state.relEnvTime = 0;
      }
    }
  }

  private void handleNoteOff(final int channel, final int note) {
    final MidiPlayingNote state = this.noteStates[channel][note];
    if (state != null) {
      this.noteStates[channel][note] = null;
      if ((FLAG_PORTAMENTO & this.chFlags[channel]) == 0) {
        state.relEnvTime = 0;
      } else if (this.noteSet.notes.stream().anyMatch(n -> state.channel == n.channel && n.relEnvTime < 0 && n != state)) {
        state.relEnvTime = 0;
      }
    }
  }

  public synchronized void initMicrosecondsPerSecond() {
    this.usPerSec = 1000000;
  }

  public synchronized void initChGlobalAmp() {
    for (int channel = 0; channel < 16; ++channel) {
      this.chGlobalAmp_p8[channel] = 256;
    }
  }

  public synchronized void loadNoteSamplesForSong(final SoundLoader soundLoader, final ResourceLoader loader, final SongData track) {
    track.analyzeNotesUsedPerProgram();

    boolean success = true;

    for (final Map.Entry<Integer, byte[]> notesUsed : track.notesUsedPerProgram.entrySet()) {
      final int programNumber = notesUsed.getKey();
      MidiInstrument instrument = this.instruments.get(programNumber);
      if (instrument == null) {
        instrument = loadInstrument(loader, programNumber);
        if (instrument == null) {
          success = false;
          continue;
        }

        this.instruments.put(programNumber, instrument);
      }

      if (!instrument.loadNoteSamples(soundLoader, notesUsed.getValue())) {
        success = false;
      }
    }

    if (success) {
      track.resetNotesUsedPerProgram();
    }
  }

  @Override
  public synchronized void processAndWrite(final int[] data_s16p8, int offset, int len) {
    if (this.midiReader.isLoaded()) {
      final long ptPerSample = (long) this.midiReader.ticksPerQn * this.usPerSec / SampledAudioChannelS16.SAMPLE_RATE;

      do {
        final long bufferPt = this.outputPt + len * ptPerSample;
        if (this.playerPt - bufferPt >= 0L) {
          this.outputPt = bufferPt;
          break;
        }

        final long samples = (this.playerPt - this.outputPt + ptPerSample - 1L) / ptPerSample;
        this.outputPt += samples * ptPerSample;
        this.noteSet.processAndWrite(data_s16p8, offset, (int) samples);
        offset += samples;
        len -= samples;
        this.pumpEvents();
      } while (this.midiReader.isLoaded());
    }

    this.noteSet.processAndWrite(data_s16p8, offset, len);
  }

  @Override
  public synchronized void processAndDiscard(int len) {
    if (this.midiReader.isLoaded()) {
      final long ptPerSample = (long) this.midiReader.ticksPerQn * this.usPerSec / SampledAudioChannelS16.SAMPLE_RATE;

      do {
        final long bufferPt = len * ptPerSample + this.outputPt;
        if (this.playerPt - bufferPt >= 0L) {
          this.outputPt = bufferPt;
          break;
        }

        final long samples = (this.playerPt - this.outputPt + ptPerSample - 1) / ptPerSample;
        this.outputPt += samples * ptPerSample;
        this.noteSet.processAndDiscard((int) samples);
        len -= samples;
        this.pumpEvents();
      } while (this.midiReader.isLoaded());
    }

    this.noteSet.processAndDiscard(len);
  }

  @SuppressWarnings("SameParameterValue")
  public synchronized void setAmp_p8(final int amp_p8) {
    this.amp_p8 = amp_p8;
  }

  private synchronized void reset(final boolean doSoundOff) {
    this.midiReader.unload();
    this.songData = null;
    this.handleReset(doSoundOff);
  }

  private void handleAllSoundOff(final int channel) {
    for (final MidiPlayingNote note : this.noteSet.notes) {
      if (channel < 0 || channel == note.channel) {
        if (note.playback != null) {
          note.playback.setVolZeroRamped(SampledAudioChannelS16.SAMPLE_RATE / 100);
          if (note.playback.isRampTimeNonzero()) {
            this.noteSet.sum.addFirst(note.playback);
          }

          note.reset();
        }

        if (note.relEnvTime < 0) {
          this.noteStates[note.channel][note.note] = null;
        }

        note.unlink();
      }
    }
  }

  private void handlePortamentoOff(final int channel) {
    if ((FLAG_PORTAMENTO & this.chFlags[channel]) != 0) {
      for (final MidiPlayingNote var3 : this.noteSet.notes) {
        if (var3.channel == channel && this.noteStates[channel][var3.note] == null && var3.relEnvTime < 0) {
          var3.relEnvTime = 0;
        }
      }
    }
  }

  public synchronized void changeSong(final SongData songData, final boolean looped) {
    this.changeSong(looped, songData, true);
  }

  private void handleResetAllControllers(int channel) {
    if (channel >= 0) {
      this.chVol_p14[channel] = 12800; // not a typo, many midi impls default to vol=100
      this.chPan_p14[channel] = 8192;

      this.chExpression_p14[channel] = 16383;
      this.chPitchWheel[channel] = 8192;
      this.chModWheel[channel] = 0;
      this.chPortaTime[channel] = 8192;
      this.handlePortamentoOff(channel);
      this.handleGeneral6Off(channel);
      this.chFlags[channel] = 0;
      this.chRpn[channel] = 32767;
      this.chPitchBendRange[channel] = 256;
      this.chStartOffset_p6[channel] = 0;
      this.handleGeneral2(8192, channel);
    } else {
      for (channel = 0; channel < 16; ++channel) {
        this.handleResetAllControllers(channel);
      }
    }
  }

  private void handleReset(final boolean doSoundOff) {
    if (doSoundOff) {
      this.handleAllSoundOff(-1);
    } else {
      this.handleAllNotesOff(-1);
    }

    this.handleResetAllControllers(-1);

    int channel;
    for (channel = 0; channel < 16; ++channel) {
      this.chCurrentProgram[channel] = this.chDefaultBank_idk[channel];
    }

    for (channel = 0; channel < 16; ++channel) {
      this.chBank[channel] = this.chDefaultBank_idk[channel] & 0xffffff80;
    }

  }

  @SuppressWarnings("BooleanMethodIsAlwaysInverted")
  public boolean noteIgnored(final MidiPlayingNote note) {
    if (note.playback == null) {
      if (note.relEnvTime >= 0) {
        note.unlink();
        if (note.noteOffNote_idk > 0 && note == this.noteOffStates_idk[note.channel][note.noteOffNote_idk]) {
          this.noteOffStates_idk[note.channel][note.noteOffNote_idk] = null;
        }
      }

      return true;
    } else {
      return false;
    }
  }

  private void handleMidiEvent(final int event) {
    switch (event & 0xf0) {
      case 0x80: { // note off
        this.handleNoteOff(0xf & event, (0x7fa0 & event) >> 8);
        break;
      }
      case 0x90: { // note on
        final int channel = event & 0xf;
        final int note = (event >> 8) & 0x7f;
        final int velocity = 0x7f & (event >> 16);
        if (velocity > 0) {
          this.handleNoteOn(note, channel, velocity);
        } else {
          this.handleNoteOff(channel, note);
        }
        break;
      }
      case 0xa0: // polyphonic aftertouch
      case 0xd0: // channel aftertouch
        break;
      case 0xb0: { // control change
        final int channel = 0xf & event;
        final int controller = 0x7f & (event >> 8);
        final int value = 0x7f & (event >> 16);

        if (controller == 0) { // bank switch msb
          this.chBank[channel] = (value << 14) + (0xffe03fff & this.chBank[channel]);
        }
        if (controller == 32) { // bank switch lsb
          this.chBank[channel] = (value << 7) + (0xffffc07f & this.chBank[channel]);
        }

        if (controller == 1) { // mod wheel msb
          this.chModWheel[channel] = (0xffffc07f & this.chModWheel[channel]) + (value << 7);
        }
        if (controller == 33) { // mod wheel lsb
          this.chModWheel[channel] = value + (this.chModWheel[channel] & 0xffffff80);
        }

        if (controller == 5) { // portamento time msb
          this.chPortaTime[channel] = (0xffffc07f & this.chPortaTime[channel]) + (value << 7);
        }
        if (controller == 37) { // portamento time lsb
          this.chPortaTime[channel] = value + (this.chPortaTime[channel] & 0xffffff80);
        }

        if (controller == 7) { // volume msb
          this.chVol_p14[channel] = (value << 7) + (0xffffc07f & this.chVol_p14[channel]);
        }
        if (controller == 39) { // volume lsb
          this.chVol_p14[channel] = (0xffffff80 & this.chVol_p14[channel]) + value;
        }

        if (controller == 10) { // pan msb
          this.chPan_p14[channel] = (value << 7) + (this.chPan_p14[channel] & 0xffffc07f);
        }
        if (controller == 42) { // pan lsb
          this.chPan_p14[channel] = (this.chPan_p14[channel] & 0xffffff80) + value;
        }

        if (controller == 11) { // expression msb
          this.chExpression_p14[channel] = (value << 7) + (0xffffc07f & this.chExpression_p14[channel]);
        }

        if (controller == 43) { // expression lsb
          this.chExpression_p14[channel] = value + (this.chExpression_p14[channel] & 0xffffff80);
        }

        if (controller == 64) { // sustain on/off
          if (value >= 64) {
            this.chFlags[channel] = this.chFlags[channel] | FLAG_SUSTAIN;
          } else {
            this.chFlags[channel] = this.chFlags[channel] & ~FLAG_SUSTAIN;
          }
        }

        if (controller == 65) { // portamento on/off
          if (value >= 64) {
            this.chFlags[channel] = this.chFlags[channel] | FLAG_PORTAMENTO;
          } else {
            this.handlePortamentoOff(channel);
            this.chFlags[channel] = this.chFlags[channel] & ~FLAG_PORTAMENTO;
          }
        }

        if (controller == 99) { // nrpn msb
          this.chRpn[channel] = (value << 7) + (0x7f & this.chRpn[channel]);
        }
        if (controller == 98) { // nrpn lsb
          this.chRpn[channel] = value + (0x3f80 & this.chRpn[channel]);
        }

        if (controller == 101) { // rpn msb
          this.chRpn[channel] = (this.chRpn[channel] & 0x7f) + 0x4000 + (value << 7);
        }
        if (controller == 100) { // rpn lsb
          this.chRpn[channel] = 0x4000 + (this.chRpn[channel] & 0x3f80) + value;
        }

        if (controller == 120) {
          this.handleAllSoundOff(channel);
        }
        if (controller == 121) {
          this.handleResetAllControllers(channel);
        }
        if (controller == 123) {
          this.handleAllNotesOff(channel);
        }

        if (controller == 6) { // data entry msb
          final int rpn = this.chRpn[channel];
          if (rpn == 0x4000) {
            this.chPitchBendRange[channel] = (this.chPitchBendRange[channel] & 0xffffc07f) + (value << 7);
          }
        }
        if (controller == 38) { // data entry lsb
          final int rpn = this.chRpn[channel];
          if (rpn == 0x4000) {
            this.chPitchBendRange[channel] = value + (this.chPitchBendRange[channel] & 0xffffff80);
          }
        }

        if (controller == 16) { // gp controller #1 msb
          this.chStartOffset_p6[channel] = (value << 7) + (this.chStartOffset_p6[channel] & 0xffffc07f);
        }
        if (controller == 48) { // gp controller #1 lsb
          this.chStartOffset_p6[channel] = (this.chStartOffset_p6[channel] & 0xffffff80) + value;
        }

        if (controller == 81) { // gp controller #6 lsb
          if (value >= 64) {
            this.chFlags[channel] = this.chFlags[channel] | FLAG_GENERAL_6;
          } else {
            this.handleGeneral6Off(channel);
            this.chFlags[channel] = this.chFlags[channel] & ~FLAG_GENERAL_6;
          }
        }

        if (controller == 17) { // gp controller #2 msb
          this.handleGeneral2((value << 7) + (0xffffc07f & this.chGeneral2[channel]), channel);
        }
        if (controller == 49) { // gp controller #2 lsb
          this.handleGeneral2((this.chGeneral2[channel] & 0xffffff80) + value, channel);
        }

        break;
      }
      case 0xc0: { // program change
        final int channel = event & 0xf;
        final int program = (0x7f39 & event) >> 8;
        this.handleProgramChange(channel, this.chBank[channel] + program);
        break;
      }
      case 0xe0: { // pitch wheel
        final int channel = 0xf & event;
        final int value = (0x7f & event >> 8) + (event >> 9 & 0x3f80);
        this.handlePitchWheel(channel, value);
        break;
      }
      default: {
        if ((event & 255) == 255) {
          this.handleReset(true);
        }
        break;
      }
    }
  }

  @Override
  public synchronized int returns_0_1_or_2() {
    return 0;
  }

  private synchronized void changeSong(final boolean looped, final SongData songData, final boolean doSoundOff) {
    this.reset(doSoundOff);
    this.midiReader.load(songData.midiData);
    this.looped = looped;
    this.outputPt = 0L;
    final int numTracks = this.midiReader.numTracks();

    for (int track = 0; track < numTracks; ++track) {
      this.midiReader.setCursorToTrackPlaybackPos(track);
      this.midiReader.advanceTrackTicks(track);
      this.midiReader.setTrackPlaybackPosToCursor(track);
    }

    this.trackWithSoonestEvent = this.midiReader.trackWithSoonestNextTick();
    this.nextEventTicks = this.midiReader.trackNextTick[this.trackWithSoonestEvent];
    this.playerPt = this.midiReader.getPlayTime(this.nextEventTicks);
  }

  private void handleGeneral6Off(final int var2) {
    if ((FLAG_GENERAL_6 & this.chFlags[var2]) != 0) {
      for (final MidiPlayingNote var3 : this.noteSet.notes) {
        if (var3.channel == var2) {
          var3._j = 0;
        }
      }
    }

  }

  private void handleNoteOn(final int noteNumber, final int channel, final int velocity) {
    this.handleNoteOff(channel, noteNumber);
    if ((this.chFlags[channel] & FLAG_PORTAMENTO) != 0) {
      for (final Iterator<MidiPlayingNote> it = this.noteSet.notes.descendingIterator(); it.hasNext(); ) {
        final MidiPlayingNote note = it.next();
        if (note.channel == channel && note.relEnvTime < 0) {
          this.noteStates[channel][note.note] = null;
          this.noteStates[channel][noteNumber] = note;
          final int curNote_p8 = (note.portaMag_p12 * note.portaRange_p8 >> 12) + note.relNote_p8;
          note.relNote_p8 += -note.note + noteNumber << 8;
          note.portaMag_p12 = 4096;
          note.portaRange_p8 = curNote_p8 - note.relNote_p8;
          note.note = noteNumber;
          return;
        }
      }
    }

    final MidiInstrument instrument = this.instruments.get(this.chCurrentProgram[channel]);
    if (instrument == null) return;

    final RawSampleS8 sample = instrument.noteSample[noteNumber];
    if (sample == null) return;

    final MidiPlayingNote note = new MidiPlayingNote();
    note.channel = channel;
    note.instrument = instrument;
    note.sampleData = sample;
    note.params = instrument.keyParams[noteNumber];
    note.noteOffNote_idk = instrument.noteOffNote_idk[noteNumber];
    note.note = noteNumber;
    note.amp_p15 = instrument.noteAmp[noteNumber] * velocity * velocity * instrument.mainAmp + 1024 >> 11;
    note.pan_p7 = instrument.notePan_p7[noteNumber] & 255;
    note.relNote_p8 = (noteNumber << 8) - (instrument.baseNote_p8[noteNumber] & 32767);
    note.relEnvTime = -1;
    note.volEnvTime = 0;
    note.expEnvTime = 0;
    note.volEnvIdx = 0;
    note.relEnvIdx = 0;

    if (this.chStartOffset_p6[channel] == 0) {
      note.playback = RawSamplePlayer.of(
        sample,
        this.calcSpeed_p8(note),
        this.calcVol_p14(note),
        this.calcPan_p14(note)
      );
    } else {
      note.playback = RawSamplePlayer.of(sample, this.calcSpeed_p8(note), 0, this.calcPan_p14(note));
      this.initPlayhead(note, instrument.baseNote_p8[noteNumber] < 0);
    }

    if (instrument.baseNote_p8[noteNumber] < 0) {
      assert note.playback != null;
      note.playback.setLooped();
    }

    if (note.noteOffNote_idk >= 0) {
      final MidiPlayingNote noteOff = this.noteOffStates_idk[channel][note.noteOffNote_idk];
      if (noteOff != null && noteOff.relEnvTime < 0) {
        this.noteStates[channel][noteOff.note] = null;
        noteOff.relEnvTime = 0;
      }

      this.noteOffStates_idk[channel][note.noteOffNote_idk] = note;
    }

    this.noteSet.notes.addLast(note);
    this.noteStates[channel][noteNumber] = note;
  }

  @SuppressWarnings("BooleanMethodIsAlwaysInverted")
  public boolean processNote(final int offset, final int[] dest_s16p8, final MidiPlayingNote note, final int len) {
    note.timeLeft = SampledAudioChannelS16.SAMPLE_RATE / 100;

    if (note.relEnvTime >= 0 && (note.playback == null || note.playback.isPlayheadOutOfBounds())) {
      note.reset();
      note.unlink();
      if (note.noteOffNote_idk > 0 && this.noteOffStates_idk[note.channel][note.noteOffNote_idk] == note) {
        this.noteOffStates_idk[note.channel][note.noteOffNote_idk] = null;
      }

      return true;
    }

    int porta = note.portaMag_p12;
    if (porta > 0) {
      porta -= (int) (Math.pow(2.0D, (double) this.chPortaTime[note.channel] * 4.921259842519685E-4D) * 16.0D + 0.5D);
      if (porta < 0) {
        porta = 0;
      }

      note.portaMag_p12 = porta;
    }

    note.playback.setAbsSpeed_p8(this.calcSpeed_p8(note));
    final MidiKeyParams params = note.params;
    note.vibratoPhase_p9 += params.vibratoPhaseSpeed_p9;
    ++note.vibratoTime;

    boolean envEnded = false;

    // relative to middle C (note=60), used for keytracking
    final double relOctave = 5.086263020833333E-6D * (double) ((note.note - 60 << 8) + (note.portaMag_p12 * note.portaRange_p8 >> 12));

    if (params.expEnv > 0) {
      if (params.expEnvKeytrack > 0) {
        note.expEnvTime += (int) (Math.pow(2.0D, relOctave * (double) params.expEnvKeytrack) * 128.0D + 0.5D);
      } else {
        note.expEnvTime += 128;
      }

      if (params.expEnv * note.expEnvTime >= 819200) {
        envEnded = true;
      }
    }

    if (params.volEnv != null) {
      if (params.volEnvKeytrack <= 0) {
        note.volEnvTime += 128;
      } else {
        note.volEnvTime += (int) (0.5D + Math.pow(2.0D, relOctave * (double) params.volEnvKeytrack) * 128.0D);
      }

      while (note.volEnvIdx < params.volEnv.length - 2 && ('\uff00' & params.volEnv[2 + note.volEnvIdx] << 8) < note.volEnvTime) {
        note.volEnvIdx += 2;
      }

      if (params.volEnv.length - 2 == note.volEnvIdx && params.volEnv[1 + note.volEnvIdx] == 0) {
        envEnded = true;
      }
    }

    if (note.relEnvTime >= 0 && params.relEnv != null && (FLAG_SUSTAIN & this.chFlags[note.channel]) == 0 && (note.noteOffNote_idk < 0 || this.noteOffStates_idk[note.channel][note.noteOffNote_idk] != note)) {
      if (params.relEnvKeytrack <= 0) {
        note.relEnvTime += 128;
      } else {
        note.relEnvTime += (int) (0.5D + 128.0D * Math.pow(2.0D, (double) params.relEnvKeytrack * relOctave));
      }

      while (note.relEnvIdx < params.relEnv.length - 2 && note.relEnvTime > (255 & params.relEnv[2 + note.relEnvIdx]) << 8) {
        note.relEnvIdx += 2;
      }

      if (params.relEnv.length - 2 == note.relEnvIdx) {
        envEnded = true;
      }
    }

    if (envEnded) {
      note.playback.setVolZeroRamped(note.timeLeft);
      if (dest_s16p8 == null) {
        note.playback.processAndDiscard(len);
      } else {
        note.playback.processAndWrite(dest_s16p8, offset, len);
      }

      if (note.playback.isRampTimeNonzero()) {
        this.noteSet.sum.addFirst(note.playback);
      }

      note.reset();
      if (note.relEnvTime >= 0) {
        note.unlink();
        if (note.noteOffNote_idk > 0 && this.noteOffStates_idk[note.channel][note.noteOffNote_idk] == note) {
          this.noteOffStates_idk[note.channel][note.noteOffNote_idk] = null;
        }
      }

      return true;
    } else {

      note.playback.setVolAndPanRamped_p14(note.timeLeft, this.calcVol_p14(note), this.calcPan_p14(note));
      return false;
    }
  }

  private void handleProgramChange(final int channel, final int program) {
    if (program != this.chCurrentProgram[channel]) {
      this.chCurrentProgram[channel] = program;
      for (int i = 0; i < 128; ++i) {
        this.noteOffStates_idk[channel][i] = null;
      }
    }
  }

  private int calcPan_p14(final MidiPlayingNote note) {
    final int chPan_p14 = this.chPan_p14[note.channel];
    return chPan_p14 >= 8192
      ? 16384 - (32 + (128 - note.pan_p7) * (16384 - chPan_p14) >> 6)
      : 32 + note.pan_p7 * chPan_p14 >> 6;
  }

  private void pumpEvents() {
    int track = this.trackWithSoonestEvent;
    int ticks = this.nextEventTicks;
    long playTime = this.playerPt;

    if (this.songData != null && ticks == 0) {
      this.changeSong(this.looped, this.songData, false);
      this.pumpEvents();
    } else {
      while (ticks == this.nextEventTicks) {
        while (this.midiReader.trackNextTick[track] == ticks) {
          this.midiReader.setCursorToTrackPlaybackPos(track);
          final int event = this.midiReader.readNextTrackEvent(track);

          if (event == MidiReader.EVENT_TRACK_END) {
            this.midiReader.resetCursor();
            this.midiReader.setTrackPlaybackPosToCursor(track);
            if (this.midiReader.allTracksStopped()) {
              if (this.songData != null) {
                this.changeSong(this.songData, this.looped);
                this.pumpEvents();
                return;
              }

              if (!this.looped || ticks == 0) {
                this.handleReset(true);
                this.midiReader.unload();
                return;
              }

              this.midiReader.resetPlayback(playTime);
            }
            break;
          }

          if ((event & 128) != 0) {
            this.handleMidiEvent(event);
          }

          this.midiReader.advanceTrackTicks(track);
          this.midiReader.setTrackPlaybackPosToCursor(track);
        }

        track = this.midiReader.trackWithSoonestNextTick();
        ticks = this.midiReader.trackNextTick[track];
        playTime = this.midiReader.getPlayTime(ticks);
      }

      this.trackWithSoonestEvent = track;
      this.nextEventTicks = ticks;
      this.playerPt = playTime;
      if (this.songData != null && ticks > 0) {
        this.trackWithSoonestEvent = -1;
        this.nextEventTicks = 0;
        this.playerPt = this.midiReader.getPlayTime(this.nextEventTicks);
      }

    }
  }

  public void initPlayhead(final MidiPlayingNote note, final boolean isLooped) {

    int sampleLength = note.sampleData.data_s8.length;
    int playhead;
    if (isLooped && note.sampleData.isPingPongLoop) {
      final int var6 = -note.sampleData.loopStart + sampleLength + sampleLength;
      playhead = (int) ((long) this.chStartOffset_p6[note.channel] * (long) var6 >> 6);
      sampleLength <<= 8;
      if (playhead >= sampleLength) {
        note.playback.setBackwards();
        playhead = -playhead + sampleLength + sampleLength - 1;
      }
    } else {
      playhead = (int) ((long) this.chStartOffset_p6[note.channel] * (long) sampleLength >> 6);
    }

    note.playback.setPlayhead_p8(playhead);
  }

  private int calcVol_p14(final MidiPlayingNote note) {
    if (this.chGlobalAmp_p8[note.channel] == 0) {
      return 0;
    }

    final MidiKeyParams keyParams = note.params;
    int x;
    x = 4096 + this.chVol_p14[note.channel] * this.chExpression_p14[note.channel] >> 13;
    x = 16384 + x * x >> 15;
    x = 16384 + note.amp_p15 * x >> 15;
    x = 128 + this.amp_p8 * x >> 8;
    x = 128 + this.chGlobalAmp_p8[note.channel] * x >> 8;
    if (keyParams.expEnv > 0) {
      x = (int) (0.5D + (double) x * Math.pow(0.5D, (double) note.expEnvTime * 1.953125E-5D * (double) keyParams.expEnv));
    }

    int t_p8;
    int env;
    int t0_p8;
    int t1_p8;

    if (keyParams.volEnv != null) {
      t_p8 = note.volEnvTime;
      env = keyParams.volEnv[note.volEnvIdx + 1];
      if (note.volEnvIdx < keyParams.volEnv.length - 2) {
        t0_p8 = (255 & keyParams.volEnv[note.volEnvIdx]) << 8;
        t1_p8 = (255 & keyParams.volEnv[note.volEnvIdx + 2]) << 8;
        env += (keyParams.volEnv[note.volEnvIdx + 3] - env) * (t_p8 - t0_p8) / (t1_p8 - t0_p8);
      }

      x = 32 + env * x >> 6;
    }

    if (note.relEnvTime > 0 && keyParams.relEnv != null) {
      t_p8 = note.relEnvTime;
      env = keyParams.relEnv[note.relEnvIdx + 1];
      if (note.relEnvIdx < keyParams.relEnv.length - 2) {
        t0_p8 = (255 & keyParams.relEnv[note.relEnvIdx]) << 8;
        t1_p8 = (255 & keyParams.relEnv[note.relEnvIdx + 2]) << 8;
        env += (keyParams.relEnv[note.relEnvIdx + 3] - env) * (t_p8 - t0_p8) / (-t0_p8 + t1_p8);
      }

      x = 32 + env * x >> 6;
    }

    return x;
  }

  public synchronized void initialize() {
    this.chDefaultBank_idk[9] = 128;
    this.chBank[9] = 128;
    this.handleProgramChange(9, 128);
  }

  @SuppressWarnings("BooleanMethodIsAlwaysInverted")
  public synchronized boolean h154() {
    return this.midiReader.isLoaded();
  }
}
