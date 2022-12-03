package funorb.audio;

import funorb.cache.ResourceLoader;
import funorb.io.Buffer;

import java.util.HashMap;
import java.util.Map;

/** Parses a custom song format into a standard MIDI file */
public final class SongData {
  public byte[] midiData;
  public Map<Integer, byte[]> notesUsedPerProgram;

  private SongData(final Buffer input) {
    input.pos = input.data.length - 3;
    final int numTracks = input.readUByte();
    final int pulsesPerQuarterNote = input.readUShort();
    input.pos = 0;

    int midiSize = 14 + numTracks * 10;
    int nTempos = 0;
    int nCommands = 0;
    int nVelocityDeltas = 0;
    int nNoteOffVelocityDeltas = 0;
    int nPitchWheelMsbDeltas = 0;
    int nChannelAftertouchDeltas = 0;
    int nPolyAftertouchDeltas = 0;
    int nPatchChangeDeltas = 0;
    int nModWheelMsbDeltas = 0;
    int nModWheelLsbDeltas = 0;
    int nVolumeMsbDeltas = 0;
    int nVolumeLsbDeltas = 0;
    int nPanMsbDeltas = 0;
    int nPanLsbDeltas = 0;
    int nNrpnMsbDeltas = 0;
    int nNrpnLsbDeltas = 0;
    int nRpnMsbDeltas = 0;
    int nRpnLsbDeltas = 0;
    int nOnOffDeltas = 0;
    int nOtherControlDeltas = 0;

    for (int track = 0; track < numTracks; ++track) {
      int inputCommand;
      int command = -1;

      while (true) {
        inputCommand = input.readUByte();
        if (inputCommand != command) {
          ++midiSize;
        }

        command = inputCommand & 15;
        if (inputCommand == 7) {
          break;
        }

        if (inputCommand == 23) {
          ++nTempos;
        } else if (command == 0) {
          ++nVelocityDeltas;
        } else if (command == 1) {
          ++nNoteOffVelocityDeltas;
        } else if (command == 2) {
          ++nCommands;
        } else if (command == 3) {
          ++nPitchWheelMsbDeltas;
        } else if (command == 4) {
          ++nChannelAftertouchDeltas;
        } else if (command == 5) {
          ++nPolyAftertouchDeltas;
        } else {
          if (command != 6) {
            throw new RuntimeException();
          }

          ++nPatchChangeDeltas;
        }
      }
    }

    midiSize += 5 * nTempos;
    midiSize += 2 * (
      nVelocityDeltas
      + nNoteOffVelocityDeltas
      + nCommands
      + nPitchWheelMsbDeltas
      + nPolyAftertouchDeltas);
    midiSize += nChannelAftertouchDeltas + nPatchChangeDeltas;

    int startOfTicksTable = input.pos;
    int numEvents =
      numTracks
      + nTempos
      + nCommands
      + nVelocityDeltas
      + nNoteOffVelocityDeltas
      + nPitchWheelMsbDeltas
      + nChannelAftertouchDeltas
      + nPolyAftertouchDeltas
      + nPatchChangeDeltas;
    for (int i = 0; i < numEvents; ++i) {
      input.readVariableInt();
    }
    midiSize += input.pos - startOfTicksTable;

    int posControlDeltas = input.pos;
    int cmd = 0;
    for (int i = 0; i < nCommands; ++i) {
      cmd = cmd + input.readUByte() & 127;
      if (cmd == 0 || cmd == 32) {
        ++nPatchChangeDeltas;
      } else if (cmd == 1) {
        ++nModWheelMsbDeltas;
      } else if (cmd == 33) {
        ++nModWheelLsbDeltas;
      } else if (cmd == 7) {
        ++nVolumeMsbDeltas;
      } else if (cmd == 39) {
        ++nVolumeLsbDeltas;
      } else if (cmd == 10) {
        ++nPanMsbDeltas;
      } else if (cmd == 42) {
        ++nPanLsbDeltas;
      } else if (cmd == 99) {
        ++nNrpnMsbDeltas;
      } else if (cmd == 98) {
        ++nNrpnLsbDeltas;
      } else if (cmd == 101) {
        ++nRpnMsbDeltas;
      } else if (cmd == 100) {
        ++nRpnLsbDeltas;
      } else if (cmd == 64 || cmd == 65 || cmd == 120 || cmd == 121 || cmd == 123) {
        ++nOnOffDeltas;
      } else {
        ++nOtherControlDeltas;
      }
    }

    int posCommands = 0;
    int posOnOffDeltas = input.pos;
    input.pos += nOnOffDeltas;
    int posPolyAftertouchDeltas = input.pos;
    input.pos += nPolyAftertouchDeltas;
    int posChannelAftertouchDeltas = input.pos;
    input.pos += nChannelAftertouchDeltas;
    int posPitchWheelMsbDeltas = input.pos;
    input.pos += nPitchWheelMsbDeltas;
    int posModWheelMsbDeltas = input.pos;
    input.pos += nModWheelMsbDeltas;
    int posVolumeMsbDeltas = input.pos;
    input.pos += nVolumeMsbDeltas;
    int posPanMsbDeltas = input.pos;
    input.pos += nPanMsbDeltas;
    int posKeyDeltas = input.pos;
    input.pos += nVelocityDeltas + nNoteOffVelocityDeltas + nPolyAftertouchDeltas;
    int posVelocityDeltas = input.pos;
    input.pos += nVelocityDeltas;
    int posOtherControllerDeltas = input.pos;
    input.pos += nOtherControlDeltas;
    int posNoteOffVelocityDeltas = input.pos;
    input.pos += nNoteOffVelocityDeltas;
    int posModWheelLsbDeltas = input.pos;
    input.pos += nModWheelLsbDeltas;
    int posVolumeLsbDeltas = input.pos;
    input.pos += nVolumeLsbDeltas;
    int posPanLsbDeltas = input.pos;
    input.pos += nPanLsbDeltas;
    int posPatchChangeDeltas = input.pos;
    input.pos += nPatchChangeDeltas;
    int posPitchWheelLsbDeltas = input.pos;
    input.pos += nPitchWheelMsbDeltas;
    int posNrpnMsbDeltas = input.pos;
    input.pos += nNrpnMsbDeltas;
    int posNrpnLsbDeltas = input.pos;
    input.pos += nNrpnLsbDeltas;
    int posRpnMsbDeltas = input.pos;
    input.pos += nRpnMsbDeltas;
    int posRpnLsbDeltas = input.pos;
    input.pos += nRpnLsbDeltas;
    int posTempos = input.pos;
    input.pos += nTempos * 3;

    this.midiData = new byte[midiSize];
    final Buffer midi = new Buffer(this.midiData);
    midi.writeInt(0x4d546864); // MThd
    midi.writeInt(6);
    midi.writeShort(numTracks > 1 ? 1 : 0);
    midi.writeShort(numTracks);
    midi.writeShort(pulsesPerQuarterNote);

    input.pos = startOfTicksTable;
    int midiChannel = 0;
    int key = 0;
    int velocity = 0;
    int noteOffVelocity = 0;
    int pitchWheel = 0;
    int channelAftertouch = 0;
    int polyAftertouch = 0;
    final int[] controllerValues = new int[128];
    int controller = 0;

    label221:
    for (int track = 0; track < numTracks; ++track) {
      midi.writeInt(0x4d54726b); // MTrk
      midi.pos += 4; // skip MTrk length field
      final int trackStartPos = midi.pos;
      int command = -1;

      while (true) {
        while (true) {
          final int ticks = input.readVariableInt();
          midi.writeVariableInt_v2(ticks);

          final int inputCommand = input.data[posCommands++] & 255;
          final boolean statusChanged = inputCommand != command;
          command = inputCommand & 15;

          if (inputCommand == 7) {
            if (statusChanged) {
              midi.writeByte(0xff); // meta event
            }
            midi.writeByte(0x2f); // track end
            midi.writeByte(0);
            midi.writeSizePrefixBackwards(midi.pos - trackStartPos);
            continue label221;
          }

          if (inputCommand == 23) {
            if (statusChanged) {
              midi.writeByte(0xff); // meta event
            }
            midi.writeByte(0x51); // set tempo
            midi.writeByte(3);
            midi.writeByte(input.data[posTempos++]);
            midi.writeByte(input.data[posTempos++]);
            midi.writeByte(input.data[posTempos++]);
          } else {
            midiChannel ^= inputCommand >> 4; // ????

            if (command == 0) {
              if (statusChanged) {
                midi.writeByte(0x90 + midiChannel); // note on
              }

              key += input.data[posKeyDeltas++];
              velocity += input.data[posVelocityDeltas++];
              midi.writeByte(key & 127);
              midi.writeByte(velocity & 127);

            } else if (command == 1) {
              if (statusChanged) {
                midi.writeByte(0x80 + midiChannel); // note off
              }

              key += input.data[posKeyDeltas++];
              noteOffVelocity += input.data[posNoteOffVelocityDeltas++];
              midi.writeByte(key & 127);
              midi.writeByte(noteOffVelocity & 127);

            } else if (command == 2) {
              if (statusChanged) {
                midi.writeByte(0xb0 + midiChannel); // control change
              }

              controller += input.data[posControlDeltas++] & 127;
              midi.writeByte(controller);
              final byte valueDelta;
              if (controller == 0 || controller == 32) { // bank switch msb/lsb
                valueDelta = input.data[posPatchChangeDeltas++];
              } else if (controller == 1) { // mod wheel msb
                valueDelta = input.data[posModWheelMsbDeltas++];
              } else if (controller == 33) { // mod wheel lsb
                valueDelta = input.data[posModWheelLsbDeltas++];
              } else if (controller == 7) { // channel volume msb
                valueDelta = input.data[posVolumeMsbDeltas++];
              } else if (controller == 39) { // channel volume lsb
                valueDelta = input.data[posVolumeLsbDeltas++];
              } else if (controller == 10) { // pan msb
                valueDelta = input.data[posPanMsbDeltas++];
              } else if (controller == 42) { // pan lsb
                valueDelta = input.data[posPanLsbDeltas++];
              } else if (controller == 99) { // nrpn msb
                valueDelta = input.data[posNrpnMsbDeltas++];
              } else if (controller == 98) { // nrpn lsb
                valueDelta = input.data[posNrpnLsbDeltas++];
              } else if (controller == 101) { // rpn msb
                valueDelta = input.data[posRpnMsbDeltas++];
              } else if (controller == 100) { // rpn lsb
                valueDelta = input.data[posRpnLsbDeltas++];
              } else if (
                  controller == 64 // sustain pedal on/off
                  || controller == 65 // portamento on/of
                  || controller == 120 // all sound off
                  || controller == 121 // reset all controllers
                  || controller == 123) { // all notes off
                valueDelta = input.data[posOnOffDeltas++];
              } else {
                valueDelta = input.data[posOtherControllerDeltas++];
              }

              final int value = valueDelta + controllerValues[controller];
              controllerValues[controller] = value;
              midi.writeByte(value & 127);
            } else if (command == 3) {
              if (statusChanged) {
                midi.writeByte(0xe0 + midiChannel); // pitch wheel
              }

              pitchWheel += input.data[posPitchWheelLsbDeltas++];
              pitchWheel += input.data[posPitchWheelMsbDeltas++] << 7;
              midi.writeByte(pitchWheel & 127);
              midi.writeByte(pitchWheel >> 7 & 127);
            } else if (command == 4) {
              if (statusChanged) {
                midi.writeByte(0xd0 + midiChannel); // channel aftertouch
              }

              channelAftertouch += input.data[posChannelAftertouchDeltas++];
              midi.writeByte(channelAftertouch & 127);
            } else if (command == 5) {
              if (statusChanged) {
                midi.writeByte(0xa0 + midiChannel); // polyphonic aftertouch
              }

              key += input.data[posKeyDeltas++];
              polyAftertouch += input.data[posPolyAftertouchDeltas++];
              midi.writeByte(key & 127);
              midi.writeByte(polyAftertouch & 127);
            } else {
              if (command != 6) {
                throw new RuntimeException();
              }

              if (statusChanged) {
                midi.writeByte(0xc0 + midiChannel); // program change
              }

              midi.writeByte(input.data[posPatchChangeDeltas++]);
            }
          }
        }
      }
    }

  }

  public static SongData load(final ResourceLoader loader, final String item) {
    final byte[] data = loader.getResource("", item);
    return data == null ? null : new SongData(new Buffer(data));
  }

  public void analyzeNotesUsedPerProgram() {
    if (this.notesUsedPerProgram == null) {
      this.notesUsedPerProgram = new HashMap<>();
      final int[] bank = new int[16];
      final int[] program = new int[16];
      program[9] = 128;
      bank[9] = 128;
      final MidiReader midiReader = new MidiReader(this.midiData);
      final int numTracks = midiReader.numTracks();

      int track;
      for (track = 0; track < numTracks; ++track) {
        midiReader.setCursorToTrackPlaybackPos(track);
        midiReader.advanceTrackTicks(track);
        midiReader.setTrackPlaybackPosToCursor(track);
      }

      label53:
      do {
        while (true) {
          track = midiReader.trackWithSoonestNextTick();
          final int thisTick = midiReader.trackNextTick[track];

          while (midiReader.trackNextTick[track] == thisTick) {
            midiReader.setCursorToTrackPlaybackPos(track);
            final int event = midiReader.readNextTrackEvent(track);
            if (event == MidiReader.EVENT_TRACK_END) {
              midiReader.resetCursor();
              midiReader.setTrackPlaybackPosToCursor(track);
              continue label53;
            }

            final int nibble = event & 0xf0;
            int channel;
            int param1;
            int param2;

            if (nibble == 0xb0) { // control change
              channel = event & 0xf;
              param1 = event >> 8 & 0x7f; // controller number
              param2 = event >> 16 & 0x7f; // value
              if (param1 == 0) { // CC0 = bank select, high 7 bits
                bank[channel] = (bank[channel] & 0xffe03fff) + (param2 << 14);
              }

              if (param1 == 32) { // CC32 = bank select, low 7 bits
                bank[channel] = (bank[channel] & 0xffffc07f) + (param2 << 7);
              }
            }

            if (nibble == 0xc0) { // program change
              channel = event & 0xf;
              param1 = event >> 8 & 127; // program number
              program[channel] = bank[channel] + param1;
            }

            if (nibble == 0x90) { // note on
              channel = event & 0xf;
              param1 = event >> 8 & 127;
              param2 = event >> 16 & 127;
              if (param2 > 0) {
                final int prog = program[channel];
                final byte[] notesUsed = this.notesUsedPerProgram.computeIfAbsent(prog, k -> new byte[128]);
                notesUsed[param1] = 1;
              }
            }

            midiReader.advanceTrackTicks(track);
            midiReader.setTrackPlaybackPosToCursor(track);
          }
        }
      } while (!midiReader.allTracksStopped());

    }
  }

  public void resetNotesUsedPerProgram() {
    this.notesUsedPerProgram = null;
  }
}
