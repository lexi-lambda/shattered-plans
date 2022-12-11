package funorb.audio;

import funorb.io.Buffer;

import java.util.Arrays;

public final class MidiReader {
  public static final int EVENT_SYSEX = 0;
  public static final int EVENT_TRACK_END = 1;
  public static final int EVENT_TEMPO_CHANGE = 2;
  public static final int EVENT_UNKNOWN_META = 3;

  private static final byte[] STATUS_BYTE_TO_DATA_BYTE_COUNT = new byte[] {
    // channel voice messages
    2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, // 1000nnnn 0kkkkkkk 0vvvvvvv - note off
    2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, // 1001nnnn 0kkkkkkk 0vvvvvvv - note on
    2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, // 1010nnnn 0kkkkkkk 0vvvvvvv - polyphonic aftertouch
    2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, // 1011nnnn 0ccccccc 0vvvvvvv - control change
    1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, // 1100nnnn 0ppppppp - program change
    1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, // 1101nnnn 0vvvvvvv - channel aftertouch
    2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, // 1110nnnn 0lllllll 0hhhhhhh - pitch wheel

    // system common messages
    0, // 11110000 - sysex start
    1, // 11110001 - undefined
    2, // 11110010 0lllllll 0mmmmmmm - song position pointer
    1, // 11110011 0sssssss - song select
    0, // 11110100 - undefined
    0, // 11110101 - undefined
    0, // 11110110 - tune request
    0, // 11110111 - sysex end

    // system realtime messages
    0, // 11111000 - sync clock
    0, // 11111001 - undefined
    0, // 11111010 - start
    0, // 11111011 - continue
    0, // 11111100 - stop
    0, // 11111101 - undefined
    0, // 11111110 - active sensing
    0, // 11111111 - meta event
  };

  private final Buffer midi = new Buffer(null);
  public int ticksPerQn;
  public int[] trackNextTick;
  private int[] trackPlaybackPos;
  private int[] trackStartPos;
  private int[] lastTrackStatusByte;

  /** play time units appear to be "tick-microseconds per quarter note" */
  private long playTimeOrigin;

  /** microseconds per quarter note */
  private int tempo;

  public MidiReader() {
  }

  public MidiReader(final byte[] midiData) {
    this.load(midiData);
  }

  public boolean allTracksStopped() {
    return Arrays.stream(this.trackPlaybackPos).noneMatch(i -> i >= 0);
  }

  public int readNextTrackEvent(final int track) {
    return this.readNextTrackEventInner(track);
  }

  public void resetPlayback(final long playTimeOrigin) {
    this.playTimeOrigin = playTimeOrigin;
    final int numTracks = this.trackPlaybackPos.length;

    for (int track = 0; track < numTracks; ++track) {
      this.trackNextTick[track] = 0;
      this.lastTrackStatusByte[track] = 0;
      this.midi.pos = this.trackStartPos[track];
      this.advanceTrackTicks(track);
      this.trackPlaybackPos[track] = this.midi.pos;
    }
  }

  public long getPlayTime(final int ticks) {
    return this.playTimeOrigin + (long) ticks * (long) this.tempo;
  }

  public void resetCursor() {
    this.midi.pos = -1;
  }

  public void setCursorToTrackPlaybackPos(final int track) {
    this.midi.pos = this.trackPlaybackPos[track];
  }

  public void setTrackPlaybackPosToCursor(final int track) {
    this.trackPlaybackPos[track] = this.midi.pos;
  }

  private int readNextTrackEventWithStatus(final int track, final int statusByte) {
    if (statusByte == 0xff) {
      final int metaEvent = this.midi.readUByte();
      int metaLength = this.midi.readVariableInt();
      if (metaEvent == 0x2f) { // end of track
        this.midi.pos += metaLength;
        return EVENT_TRACK_END;
      } else if (metaEvent == 0x51) { // set tempo
        final int newTempo = this.midi.readU24();
        metaLength -= 3;
        this.playTimeOrigin += (long) this.trackNextTick[track] * (long) (this.tempo - newTempo);
        this.tempo = newTempo;
        this.midi.pos += metaLength;
        return EVENT_TEMPO_CHANGE;
      } else {
        this.midi.pos += metaLength;
        return EVENT_UNKNOWN_META;
      }
    }

    final byte numDataBytes = STATUS_BYTE_TO_DATA_BYTE_COUNT[statusByte - 128];
    int event = statusByte;
    if (numDataBytes >= 1) event |= this.midi.readUByte() << 8;
    if (numDataBytes >= 2) event |= this.midi.readUByte() << 16;
    return event;
  }

  public int trackWithSoonestNextTick() {
    final int numTracks = this.trackPlaybackPos.length;
    int minIndex = -1;
    int minValue = Integer.MAX_VALUE;

    for (int track = 0; track < numTracks; ++track) {
      if (this.trackPlaybackPos[track] >= 0 && this.trackNextTick[track] < minValue) {
        minIndex = track;
        minValue = this.trackNextTick[track];
      }
    }

    return minIndex;
  }

  public int numTracks() {
    return this.trackPlaybackPos.length;
  }

  private int readNextTrackEventInner(final int track) {
    final byte nextTrackByte = this.midi.data[this.midi.pos];
    final int statusByte;

    if (nextTrackByte < 0) { // high bit set
      statusByte = nextTrackByte & 255;
      this.lastTrackStatusByte[track] = statusByte;
      ++this.midi.pos;
    } else {
      statusByte = this.lastTrackStatusByte[track];
    }

    if (statusByte == 0xf0 || statusByte == 0xf7) {
      final int sysexLength = this.midi.readVariableInt();

      if (statusByte == 0xf7 && sysexLength > 0) {
        final int firstSysexByte = this.midi.data[this.midi.pos] & 0xff;
        switch (firstSysexByte) {
          case 241:
          case 242:
          case 243:
          case 246:
          case 248:
          case 250:
          case 252:
          case 254:
            ++this.midi.pos;
            this.lastTrackStatusByte[track] = firstSysexByte;
            return this.readNextTrackEventWithStatus(track, firstSysexByte);
          default:
            break;
        }
      }

      this.midi.pos += sysexLength;
      return EVENT_SYSEX;
    } else {
      return this.readNextTrackEventWithStatus(track, statusByte);
    }
  }

  public void unload() {
    this.midi.data = null;
    this.trackStartPos = null;
    this.trackPlaybackPos = null;
    this.trackNextTick = null;
    this.lastTrackStatusByte = null;
  }

  public boolean isLoaded() {
    return this.midi.data != null;
  }

  public void advanceTrackTicks(final int track) {
    final int timeDelta = this.midi.readVariableInt();
    this.trackNextTick[track] += timeDelta;
  }

  public void load(final byte[] midiData) {
    this.midi.data = midiData;
    this.midi.pos = 10; // skip to count of MTrk chunks in MThd
    final int numTracks = this.midi.readUShort();
    this.ticksPerQn = this.midi.readUShort();
    this.tempo = 500000; // 120 bpm
    this.trackStartPos = new int[numTracks];

    Buffer midi;
    int track;
    int chunkSize;
    for (track = 0; track < numTracks; midi.pos += chunkSize) {
      final int chunkHeader = this.midi.readInt();
      chunkSize = this.midi.readInt();
      if (chunkHeader == 0x4d54726b) { // 'MTrk'
        this.trackStartPos[track] = this.midi.pos;
        ++track;
      }

      midi = this.midi;
    }

    this.playTimeOrigin = 0L;
    this.trackPlaybackPos = new int[numTracks];

    for (track = 0; track < numTracks; ++track) {
      this.trackPlaybackPos[track] = this.trackStartPos[track];
    }

    this.trackNextTick = new int[numTracks];
    this.lastTrackStatusByte = new int[numTracks];
  }
}
