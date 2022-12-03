package funorb.audio;

import funorb.shatteredplans.client.Sounds;

public final class PlayingSound {
  public final AudioSamplePlayback_idk _p;
  public int volume;

  public PlayingSound(final AudioSamplePlayback_idk var1) {
    this._p = var1;
    this.volume = var1.getVolume();
    this._p.setVolume((128 + (this.volume * Sounds.soundVolume)) >> 8);
  }

  public void setVolume(final int volume) {
    this.volume = volume;
    this._p.setVolume((128 + (volume * Sounds.soundVolume)) >> 8);
  }
}
