package funorb.audio;

import funorb.data.NodeList;

public final class MidiPlayerNoteState_idk extends NodeList.Node {
  /** seems to be -1 for active and 0 for inactive... ??? */
  public int notePlaying_idfk;
  public int _pitch_fac_2;
  public int _v;
  public int _F;
  public int note;
  public RawSampleS8 sampleData;
  public KeyParams_idk keyParams_idk;
  public RawSamplePlayer playback;
  public MidiInstrument instrument;
  public int _j;
  public int _pitch_fac_1;
  public int _C;
  public int pan_idk;
  public int channel;
  public int pitch_idk;
  public int _h;
  public int noteOffNote_idk;
  public int volume_idk;
  public int _p;
  public int _B;
  public int vibratoPhase_idk;

  public void reset_idk() {
    this.instrument = null;
    this.playback = null;
    this.sampleData = null;
    this.keyParams_idk = null;
  }
}
