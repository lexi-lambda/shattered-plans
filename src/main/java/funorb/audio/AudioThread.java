package funorb.audio;

import funorb.client.JagexBaseApplet;
import funorb.shatteredplans.client.MessagePumpThread;

public final class AudioThread implements Runnable {
  public static final int NUM_CHANNELS = 2;
  public static AudioThread instance;

  public final SampledAudioChannel[] channels = new SampledAudioChannel[NUM_CHANNELS];
  private final MessagePumpThread messagePumpThread;
  public volatile boolean shutdownRequested = false;
  public volatile boolean isRunning = false;

  public AudioThread(final MessagePumpThread messagePumpThread) {
    this.messagePumpThread = messagePumpThread;
  }

  @Override
  public void run() {
    this.isRunning = true;
    try {
      while (!this.shutdownRequested) {
        for (int i = 0; i < NUM_CHANNELS; ++i) {
          final SampledAudioChannel channel = this.channels[i];
          if (channel != null) {
            channel.doSomethingThatSeemsRelatedToAudio();
          }
        }

        JagexBaseApplet.maybeSleep(10L);
        this.messagePumpThread.postEvent(null);
      }
    } finally {
      this.isRunning = false;
    }
  }
}
