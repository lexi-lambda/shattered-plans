package funorb.audio;

import funorb.cache.ResourceLoader;
import funorb.io.Buffer;

public final class SynthFormat {
  private final Synth[] oscs = new Synth[10];
  private final int loopStartMs;
  private final int loopEndMs;

  private SynthFormat(final Buffer data) {
    for (int i = 0; i < 10; ++i) {
      final int peekByte = data.readUByte();
      if (peekByte != 0) {
        --data.pos;
        this.oscs[i] = new Synth();
        this.oscs[i].initialize(data);
      }
    }

    this.loopStartMs = data.readUShort();
    this.loopEndMs = data.readUShort();
  }

  public static SynthFormat load(final ResourceLoader loader, final int groupId, final int itemId) {
    final byte[] data = loader.getResource(groupId, itemId);
    return data == null ? null : new SynthFormat(new Buffer(data));
  }

  private byte[] toSampleDataS8() {
    int totalDurMs = 0;

    for (int i = 0; i < 10; ++i) {
      if (this.oscs[i] != null && totalDurMs < this.oscs[i].lengthMs + this.oscs[i].posMs) {
        totalDurMs = this.oscs[i].lengthMs + this.oscs[i].posMs;
      }
    }

    if (totalDurMs == 0) {
      return new byte[0];
    }
    final int len = SampledAudioChannelS16.SAMPLE_RATE * totalDurMs / 1000;
    final byte[] dataS8 = new byte[len];

    for (int i = 0; i < 10; ++i) {
      if (this.oscs[i] != null) {
        final int durSamples = this.oscs[i].lengthMs * SampledAudioChannelS16.SAMPLE_RATE / 1000;
        final int delaySamples = this.oscs[i].posMs * SampledAudioChannelS16.SAMPLE_RATE / 1000;
        final int[] s16buf = this.oscs[i].generateS16(durSamples, this.oscs[i].lengthMs);

        for (int j = 0; j < durSamples; ++j) {
          int sample = dataS8[j + delaySamples] + (s16buf[j] >> 8);
          if ((sample + 128 & 0xffffff00) != 0) {
            // clamp to s8
            sample = sample >> 31 ^ 127;
          }
          dataS8[j + delaySamples] = (byte) sample;
        }
      }
    }

    return dataS8;
  }

  public RawSampleS8 toRawSample() {
    final byte[] sampleData = this.toSampleDataS8();
    return new RawSampleS8(
      sampleData,
      SampledAudioChannelS16.SAMPLE_RATE * this.loopStartMs / 1000,
      SampledAudioChannelS16.SAMPLE_RATE * this.loopEndMs / 1000
    );
  }
}
