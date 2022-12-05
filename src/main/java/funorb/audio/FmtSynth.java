package funorb.audio;

import funorb.cache.ResourceLoader;
import funorb.io.Buffer;

public final class FmtSynth {
  private final Oscillator[] oscs = new Oscillator[10];
  private final int loopStartMs;
  private final int loopEndMs;

  private FmtSynth(final Buffer data) {
    for (int i = 0; i < 10; ++i) {
      final int peekByte = data.readUByte();
      if (peekByte != 0) {
        --data.pos;
        this.oscs[i] = new Oscillator();
        this.oscs[i].initialize(data);
      }
    }

    this.loopStartMs = data.readUShort();
    this.loopEndMs = data.readUShort();
  }

  public static FmtSynth load(final ResourceLoader loader, final int groupId, final int itemId) {
    final byte[] data = loader.getResource(groupId, itemId);
    return data == null ? null : new FmtSynth(new Buffer(data));
  }

  private byte[] toSampleDataS8() {
    int totalDurMs = 0;

    for (int i = 0; i < 10; ++i) {
      if (this.oscs[i] != null && totalDurMs < this.oscs[i].durMs + this.oscs[i].delayMs) {
        totalDurMs = this.oscs[i].durMs + this.oscs[i].delayMs;
      }
    }

    if (totalDurMs == 0) {
      return new byte[0];
    }
    final int len = SampledAudioChannel.SAMPLES_PER_SECOND * totalDurMs / 1000;
    final byte[] dataS8 = new byte[len];

    for (int i = 0; i < 10; ++i) {
      if (this.oscs[i] != null) {
        final int durSamples = this.oscs[i].durMs * SampledAudioChannel.SAMPLES_PER_SECOND / 1000;
        final int delaySamples = this.oscs[i].delayMs * SampledAudioChannel.SAMPLES_PER_SECOND / 1000;
        final int[] s16buf = this.oscs[i].generateS16(durSamples, this.oscs[i].durMs);

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
      SampledAudioChannel.SAMPLES_PER_SECOND * this.loopStartMs / 1000,
      SampledAudioChannel.SAMPLES_PER_SECOND * this.loopEndMs / 1000
    );
  }
}
