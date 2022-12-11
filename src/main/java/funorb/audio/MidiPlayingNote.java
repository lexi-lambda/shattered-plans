package funorb.audio;

import funorb.data.NodeList;

public final class MidiPlayingNote extends NodeList.Node {
  public int relEnvTime;
  public int portaMag_p12;
  public int relEnvIdx;
  public int volEnvTime;
  public int note;
  public RawSampleS8 sampleData;
  public MidiKeyParams params;
  public RawSamplePlayer playback;
  public MidiInstrument instrument;
  public int _j;
  public int portaRange_p8;
  public int vibratoTime;
  public int pan_p7; // 0=left, 0.5=center, 1=right (128)
  public int channel;
  public int relNote_p8;
  public int expEnvTime;
  public int noteOffNote_idk;
  public int amp_p15;
  public int timeLeft;
  public int volEnvIdx;
  public int vibratoPhase_p9;

  public void reset() {
    this.instrument = null;
    this.playback = null;
    this.sampleData = null;
    this.params = null;
  }
}
