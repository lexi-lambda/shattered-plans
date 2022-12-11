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
  private static final double PHASE_512_TO_RADIANS = 0.01227184630308513D;

  public static final int FLAG_SUSTAIN = 0x0001;
  public static final int FLAG_PORTAMENTO = 0x0002;
  public static final int FLAG_GENERAL_6 = 0x0004;

  public final int[] chGeneral1 = new int[16];
  public final int[] chFlags = new int[16];
  private final int[] chModWheel = new int[16];
  private final int[] chVolume = new int[16];
  private final int[] chRpn = new int[16];
  private final MidiPlayerNoteState_idk[][] noteStates = new MidiPlayerNoteState_idk[16][128];
  private final int[] chPitchBendRange = new int[16];
  private final int[] chDefaultBank_idk = new int[16];
  private final Map<Integer, MidiInstrument> instruments;
  private final int[] chPan = new int[16];
  private final MidiReader midiReader = new MidiReader();
  private final int[] chCurrentProgram = new int[16];
  private final int[] chBank = new int[16];
  private final int[] chGeneral2 = new int[16];
  private final int[] chPortaTime = new int[16];
  private final int[] chVolumeAgainForSomeReason_idk = new int[16];
  private final int[] chPitchWheel = new int[16];
  private final MidiPlayerNoteSet_idk noteSet = new MidiPlayerNoteSet_idk(this);
  public final int[] _u = new int[16];
  private int volume = 256;
  private final MidiPlayerNoteState_idk[][] noteOffStates_idk = new MidiPlayerNoteState_idk[16][128];
  private int microsecondsPerSecond = 1000000;
  private final int[] chExpression = new int[16];
  private int trackWithSoonestEvent;
  private long outputPt;
  private int nextEventTicks;
  private long playerPt;
  private boolean looped;
  private SongData songData;

  public MidiPlayer() {
    this.instruments = new HashMap<>();
    this.a679();
    this.handleReset(true);
  }

  @SuppressWarnings("CopyConstructorMissesField")
  public MidiPlayer(final MidiPlayer copyInstruments) {
    this.instruments = copyInstruments.instruments;
    this.a679();
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
    this._u[channel] = (int) (0.5D + 2097152.0D * Math.pow(2.0D, 5.4931640625E-4D * (double) value));
  }

  private int calcPitchX_idk(final MidiPlayerNoteState_idk note) {
    int var3 = (note._pitch_fac_1 * note._pitch_fac_2 >> 12) + note.pitch_idk;
    var3 += this.chPitchBendRange[note.channel] * (this.chPitchWheel[note.channel] - 8192) >> 12;
    final KeyParams_idk var4 = note.keyParams_idk;
    int pitch;
    if (var4.vibratoPhaseSpeed_idk > 0 && (var4._f > 0 || this.chModWheel[note.channel] > 0)) {
      pitch = var4._f << 2;
      final int var7 = var4._j << 1;
      if (var7 > note._C) {
        pitch = note._C * pitch / var7;
      }

      pitch += this.chModWheel[note.channel] >> 7;
      final double vibrato = Math.sin((double) (note.vibratoPhase_idk & 0x1ff) * PHASE_512_TO_RADIANS);
      var3 += (int) ((double) pitch * vibrato);
    }

    pitch = (int) ((double) (256 * note.sampleData.sampleRate) * Math.pow(2.0D, 3.255208333333333E-4D * (double) var3) / (double) SampledAudioChannelS16.SAMPLE_RATE + 0.5D);
    return Math.max(pitch, 1);
  }

  public synchronized void f150() {
    for (final MidiInstrument instr : this.instruments.values()) {
      instr.e150();
    }
  }

  private void handleAllNotesOff(final int channel) {
    for (final MidiPlayerNoteState_idk state : this.noteSet.notes) {
      if ((channel < 0 || state.channel == channel) && state.notePlaying_idfk < 0) {
        this.noteStates[state.channel][state.note] = null;
        state.notePlaying_idfk = 0;
      }
    }
  }

  private void handleNoteOff(final int channel, final int note) {
    final MidiPlayerNoteState_idk state = this.noteStates[channel][note];
    if (state != null) {
      this.noteStates[channel][note] = null;
      if ((FLAG_PORTAMENTO & this.chFlags[channel]) == 0) {
        state.notePlaying_idfk = 0;
      } else if (this.noteSet.notes.stream().anyMatch(var6 -> state.channel == var6.channel && var6.notePlaying_idfk < 0 && var6 != state)) {
        state.notePlaying_idfk = 0;
      }
    }
  }

  public synchronized void c430() {
    this.microsecondsPerSecond = 1000000;
  }

  public synchronized void a679() {
    for (int channel = 0; channel < 16; ++channel) {
      this.chVolumeAgainForSomeReason_idk[channel] = 256;
    }
  }

  public synchronized void a350(final SoundLoader soundLoader, final ResourceLoader loader, final SongData track) {
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
  public synchronized void processAndWrite(final int[] dataS16P8, int offset, int len) {
    if (this.midiReader.isLoaded()) {
      final long ptPerSample = this.midiReader.ticksPerQuarterNote * this.microsecondsPerSecond / SampledAudioChannelS16.SAMPLE_RATE;

      do {
        final long bufferPt = this.outputPt + len * ptPerSample;
        if (this.playerPt - bufferPt >= 0L) {
          this.outputPt = bufferPt;
          break;
        }

        final long samples = (this.playerPt - this.outputPt + ptPerSample - 1L) / ptPerSample;
        this.outputPt += samples * ptPerSample;
        this.noteSet.processAndWrite(dataS16P8, offset, (int) samples);
        offset += samples;
        len -= samples;
        this.pumpEvents();
      } while (this.midiReader.isLoaded());
    }

    this.noteSet.processAndWrite(dataS16P8, offset, len);
  }

  @Override
  public synchronized void processAndDiscard(int len) {
    if (this.midiReader.isLoaded()) {
      final long ptPerSample = this.midiReader.ticksPerQuarterNote * this.microsecondsPerSecond / SampledAudioChannelS16.SAMPLE_RATE;

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
  public synchronized void setVolume(final int volume) {
    this.volume = volume;
  }

  private synchronized void reset(final boolean doSoundOff) {
    this.midiReader.unload();
    this.songData = null;
    this.handleReset(doSoundOff);
  }

  private void handleAllSoundOff(final int channel) {
    for (final MidiPlayerNoteState_idk note : this.noteSet.notes) {
      if (channel < 0 || channel == note.channel) {
        if (note.playback != null) {
          note.playback.setVolZeroRamped(SampledAudioChannelS16.SAMPLE_RATE / 100);
          if (note.playback.isRampTimeNonzero()) {
            this.noteSet.sum.addFirst(note.playback);
          }

          note.reset_idk();
        }

        if (note.notePlaying_idfk < 0) {
          this.noteStates[note.channel][note.note] = null;
        }

        note.unlink();
      }
    }
  }

  private void handlePortamentoOff(final int channel) {
    if ((FLAG_PORTAMENTO & this.chFlags[channel]) != 0) {
      for (final MidiPlayerNoteState_idk var3 : this.noteSet.notes) {
        if (var3.channel == channel && this.noteStates[channel][var3.note] == null && var3.notePlaying_idfk < 0) {
          var3.notePlaying_idfk = 0;
        }
      }
    }
  }

  public synchronized void a077(final SongData songData, final boolean looped) {
    this.changeSong(looped, songData, true);
  }

  private void handleResetAllControllers(int channel) {
    if (channel >= 0) {
      this.chVolume[channel] = 12800;
      this.chPan[channel] = 8192;

      this.chExpression[channel] = 16383;
      this.chPitchWheel[channel] = 8192;
      this.chModWheel[channel] = 0;
      this.chPortaTime[channel] = 8192;
      this.handlePortamentoOff(channel);
      this.handleGeneral6Off(channel);
      this.chFlags[channel] = 0;
      this.chRpn[channel] = 32767;
      this.chPitchBendRange[channel] = 256;
      this.chGeneral1[channel] = 0;
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
  public boolean a258(final MidiPlayerNoteState_idk note) {
    if (note.playback == null) {
      if (note.notePlaying_idfk >= 0) {
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
          this.chVolume[channel] = (value << 7) + (0xffffc07f & this.chVolume[channel]);
        }
        if (controller == 39) { // volume lsb
          this.chVolume[channel] = (0xffffff80 & this.chVolume[channel]) + value;
        }

        if (controller == 10) { // pan msb
          this.chPan[channel] = (value << 7) + (this.chPan[channel] & 0xffffc07f);
        }
        if (controller == 42) { // pan lsb
          this.chPan[channel] = (this.chPan[channel] & 0xffffff80) + value;
        }

        if (controller == 11) { // expression msb
          this.chExpression[channel] = (value << 7) + (0xffffc07f & this.chExpression[channel]);
        }

        if (controller == 43) { // expression lsb
          this.chExpression[channel] = value + (this.chExpression[channel] & 0xffffff80);
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
          int rpn = this.chRpn[channel];
          if (rpn == 0x4000) {
            this.chPitchBendRange[channel] = (this.chPitchBendRange[channel] & 0xffffc07f) + (value << 7);
          }
        }
        if (controller == 38) { // data entry lsb
          int rpn = this.chRpn[channel];
          if (rpn == 0x4000) {
            this.chPitchBendRange[channel] = value + (this.chPitchBendRange[channel] & 0xffffff80);
          }
        }

        if (controller == 16) { // gp controller #1 msb
          this.chGeneral1[channel] = (value << 7) + (this.chGeneral1[channel] & 0xffffc07f);
        }
        if (controller == 48) { // gp controller #1 lsb
          this.chGeneral1[channel] = (this.chGeneral1[channel] & 0xffffff80) + value;
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
      for (final MidiPlayerNoteState_idk var3 : this.noteSet.notes) {
        if (var3.channel == var2) {
          var3._j = 0;
        }
      }
    }

  }

  private void handleNoteOn(final int noteNumber, final int channel, final int velocity) {
    this.handleNoteOff(channel, noteNumber);
    if ((this.chFlags[channel] & FLAG_PORTAMENTO) != 0) {
      for (final Iterator<MidiPlayerNoteState_idk> it = this.noteSet.notes.descendingIterator(); it.hasNext(); ) {
        final MidiPlayerNoteState_idk note = it.next();
        if (note.channel == channel && note.notePlaying_idfk < 0) {
          this.noteStates[channel][note.note] = null;
          this.noteStates[channel][noteNumber] = note;
          final int var6 = (note._pitch_fac_2 * note._pitch_fac_1 >> 12) + note.pitch_idk;
          note.pitch_idk += -note.note + noteNumber << 8;
          note._pitch_fac_2 = 4096;
          note._pitch_fac_1 = -note.pitch_idk + var6;
          note.note = noteNumber;
          return;
        }
      }
    }

    final MidiInstrument instrument = this.instruments.get(this.chCurrentProgram[channel]);
    if (instrument != null) {
      final RawSampleS8 sample = instrument.noteSample[noteNumber];
      if (sample != null) {
        final MidiPlayerNoteState_idk note = new MidiPlayerNoteState_idk();
        note.channel = channel;
        note.instrument = instrument;
        note.sampleData = sample;
        note.keyParams_idk = instrument.keyParams_idk[noteNumber];
        note.noteOffNote_idk = instrument.noteOffNote_idk[noteNumber];
        note.note = noteNumber;
        note.volume_idk = instrument.noteVolume_idk[noteNumber] * velocity * velocity * instrument.mainVolume_idk + 1024 >> 11;
        note.pan_idk = instrument.notePan_idk[noteNumber] & 255;
        note.pitch_idk = (noteNumber << 8) - (instrument.noteTuning_idk[noteNumber] & 32767);
        note._B = 0;
        note.notePlaying_idfk = -1;
        note._F = 0;
        note._h = 0;
        note._v = 0;
        if (this.chGeneral1[channel] == 0) {
          note.playback = RawSamplePlayer.start(sample, this.calcPitchX_idk(note), this.calcVolumeX_idk(note), this.calcPanX_idk(note));
        } else {
          note.playback = RawSamplePlayer.start(sample, this.calcPitchX_idk(note), 0, this.calcPanX_idk(note));
          this.a559(note, instrument.noteTuning_idk[noteNumber] < 0);
        }

        if (instrument.noteTuning_idk[noteNumber] < 0) {
          assert note.playback != null;
          note.playback.f150();
        }

        if (note.noteOffNote_idk >= 0) {
          final MidiPlayerNoteState_idk noteOff = this.noteOffStates_idk[channel][note.noteOffNote_idk];
          if (noteOff != null && noteOff.notePlaying_idfk < 0) {
            this.noteStates[channel][noteOff.note] = null;
            noteOff.notePlaying_idfk = 0;
          }

          this.noteOffStates_idk[channel][note.noteOffNote_idk] = note;
        }

        this.noteSet.notes.addLast(note);
        this.noteStates[channel][noteNumber] = note;
      }
    }
  }

  @SuppressWarnings("BooleanMethodIsAlwaysInverted")
  public boolean a543(final int var1, final int[] var2, final MidiPlayerNoteState_idk var4, final int var5) {
    var4.lenRemaining_idk = SampledAudioChannelS16.SAMPLE_RATE / 100;
    if (var4.notePlaying_idfk < 0 || var4.playback != null && !var4.playback.isPlayheadOutOfBounds()) {
      int var6 = var4._pitch_fac_2;
      if (var6 > 0) {
        var6 -= (int) (Math.pow(2.0D, (double) this.chPortaTime[var4.channel] * 4.921259842519685E-4D) * 16.0D + 0.5D);
        if (var6 < 0) {
          var6 = 0;
        }

        var4._pitch_fac_2 = var6;
      }

      var4.playback.setAbsSpeed_p8(this.calcPitchX_idk(var4));
      final KeyParams_idk var7 = var4.keyParams_idk;
      var4.vibratoPhase_idk += var7.vibratoPhaseSpeed_idk;
      ++var4._C;
      boolean var8 = false;
      final double var9 = 5.086263020833333E-6D * (double) ((var4.note - 60 << 8) + (var4._pitch_fac_2 * var4._pitch_fac_1 >> 12));
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

      if (var4.notePlaying_idfk >= 0 && var7._e != null && (FLAG_SUSTAIN & this.chFlags[var4.channel]) == 0 && (var4.noteOffNote_idk < 0 || this.noteOffStates_idk[var4.channel][var4.noteOffNote_idk] != var4)) {
        if (var7._c <= 0) {
          var4.notePlaying_idfk += 128;
        } else {
          var4.notePlaying_idfk += (int) (0.5D + 128.0D * Math.pow(2.0D, (double) var7._c * var9));
        }

        while (var4._v < var7._e.length - 2 && var4.notePlaying_idfk > (255 & var7._e[2 + var4._v]) << 8) {
          var4._v += 2;
        }

        if (var7._e.length - 2 == var4._v) {
          var8 = true;
        }
      }

      if (var8) {
        var4.playback.setVolZeroRamped(var4.lenRemaining_idk);
        if (var2 == null) {
          var4.playback.processAndDiscard(var5);
        } else {
          var4.playback.processAndWrite(var2, var1, var5);
        }

        if (var4.playback.isRampTimeNonzero()) {
          this.noteSet.sum.addFirst(var4.playback);
        }

        var4.reset_idk();
        if (var4.notePlaying_idfk >= 0) {
          var4.unlink();
          if (var4.noteOffNote_idk > 0 && this.noteOffStates_idk[var4.channel][var4.noteOffNote_idk] == var4) {
            this.noteOffStates_idk[var4.channel][var4.noteOffNote_idk] = null;
          }
        }

        return true;
      } else {

        var4.playback.setVolAndPanRamped_p14(var4.lenRemaining_idk, this.calcVolumeX_idk(var4), this.calcPanX_idk(var4));
        return false;
      }
    } else {
      var4.reset_idk();
      var4.unlink();
      if (var4.noteOffNote_idk > 0 && this.noteOffStates_idk[var4.channel][var4.noteOffNote_idk] == var4) {
        this.noteOffStates_idk[var4.channel][var4.noteOffNote_idk] = null;
      }

      return true;
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

  private int calcPanX_idk(final MidiPlayerNoteState_idk note) {
    final int midiPan = this.chPan[note.channel];
    return midiPan >= 8192
      ? -(32 + (128 - note.pan_idk) * (-midiPan + 16384) >> 6) + 16384
      : 32 + note.pan_idk * midiPan >> 6;
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
                this.a077(this.songData, this.looped);
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

  public void a559(final MidiPlayerNoteState_idk note, final boolean var3) {

    int sampleLength = note.sampleData.data_s8.length;
    int playhead;
    if (var3 && note.sampleData.isLooped_idk) {
      final int var6 = -note.sampleData.loopStart_idfk + sampleLength + sampleLength;
      playhead = (int) ((long) this.chGeneral1[note.channel] * (long) var6 >> 6);
      sampleLength <<= 8;
      if (playhead >= sampleLength) {
        note.playback.setBackwards();
        playhead = -playhead + sampleLength + sampleLength - 1;
      }
    } else {
      playhead = (int) ((long) this.chGeneral1[note.channel] * (long) sampleLength >> 6);
    }

    note.playback.setPlayhead_p8(playhead);
  }

  private int calcVolumeX_idk(final MidiPlayerNoteState_idk note) {
    if (this.chVolumeAgainForSomeReason_idk[note.channel] == 0) {
      return 0;
    }

    final KeyParams_idk keyParams = note.keyParams_idk;
    int x = this.chVolume[note.channel] * this.chExpression[note.channel] + 4096 >> 13;
    x = x * x + 16384 >> 15;
    x = note.volume_idk * x + 16384 >> 15;
    x = 128 + this.volume * x >> 8;
    x = this.chVolumeAgainForSomeReason_idk[note.channel] * x + 128 >> 8;
    if (keyParams._h > 0) {
      x = (int) (0.5D + (double) x * Math.pow(0.5D, (double) note._h * 1.953125E-5D * (double) keyParams._h));
    }

    int var5;
    int var6;
    int var7;
    int var8;
    if (keyParams._n != null) {
      var5 = note._F;
      var6 = keyParams._n[1 + note._B];
      if (note._B < keyParams._n.length - 2) {
        var7 = keyParams._n[note._B] << 8 & '\uff00';
        var8 = (255 & keyParams._n[2 + note._B]) << 8;
        var6 += (-var6 + keyParams._n[note._B + 3]) * (-var7 + var5) / (-var7 + var8);
      }

      x = var6 * x + 32 >> 6;
    }

    if (note.notePlaying_idfk > 0 && keyParams._e != null) {
      var5 = note.notePlaying_idfk;
      var6 = keyParams._e[1 + note._v];
      if (keyParams._e.length - 2 > note._v) {
        var7 = (255 & keyParams._e[note._v]) << 8;
        var8 = keyParams._e[2 + note._v] << 8 & '\uff00';
        var6 += (keyParams._e[note._v + 3] - var6) * (-var7 + var5) / (-var7 + var8);
      }

      x = 32 + x * var6 >> 6;
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
