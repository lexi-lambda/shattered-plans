package funorb.audio;

public final class VorbisMapping {
  public final int[] floor;
  public final int[] residues;
  public final int submaps;
  public int mux;

  public VorbisMapping() {
    VorbisFormat.readBits(16); // map type (unused)

    this.submaps = VorbisFormat.readBit() != 0 ? VorbisFormat.readBits(4) + 1 : 1;

    // vorbis mapping coupling steps (unused by this decoder)
    if (VorbisFormat.readBit() != 0) {
      VorbisFormat.readBits(8);
    }

    VorbisFormat.readBits(2); // reserved

    if (this.submaps > 1) {
      this.mux = VorbisFormat.readBits(4);
    }

    this.floor = new int[this.submaps];
    this.residues = new int[this.submaps];

    for (int i = 0; i < this.submaps; ++i) {
      VorbisFormat.readBits(8);
      this.floor[i] = VorbisFormat.readBits(8);
      this.residues[i] = VorbisFormat.readBits(8);
    }

  }
}
