package funorb.shatteredplans.client;

public final class FrameClock {
  private static final long NANOS_PER_MILLI = 1_000_000L;
  private static final long NANOS_PER_SECOND = 1000L * NANOS_PER_MILLI;
  private static final long TARGET_FRAME_DURATION = 20L * NANOS_PER_MILLI;

  private final long[] frameDurations = new long[10];
  private long totalElapsed;
  private long nextFrameTarget;
  private int frameIndex = 0;
  private long frameStart = 0L;

  public FrameClock() {
    this.totalElapsed = System.nanoTime();
    this.nextFrameTarget = System.nanoTime();
  }

  /**
   * @return how long to sleep until the next frame, in milliseconds
   */
  public long finishFrame() {
    final long now = System.nanoTime();
    final long frameDuration = now - this.frameStart;
    this.frameStart = now;

    if (frameDuration > (-5 * NANOS_PER_SECOND) && frameDuration < (5 * NANOS_PER_SECOND)) {
      this.frameDurations[this.frameIndex] = frameDuration;
      this.frameIndex = (this.frameIndex + 1) % this.frameDurations.length;
    }

    final int lastFrameIndex = (this.frameIndex + (this.frameDurations.length - 1)) % this.frameDurations.length;
    this.totalElapsed += this.frameDurations[lastFrameIndex];

    return this.totalElapsed >= this.nextFrameTarget
        ? 0L // we’re over our budget!
        : (this.nextFrameTarget - this.totalElapsed) / NANOS_PER_MILLI;
  }

  public int advanceFrame() {
    if (this.totalElapsed < this.nextFrameTarget) {
      this.frameStart += this.nextFrameTarget - this.totalElapsed;
      this.totalElapsed = this.nextFrameTarget;
      this.nextFrameTarget += TARGET_FRAME_DURATION;
      return 1;
    } else {
      int framesAdvanced = 0;

      do {
        ++framesAdvanced;
        this.nextFrameTarget += TARGET_FRAME_DURATION;
      } while (framesAdvanced < 10 && this.nextFrameTarget < this.totalElapsed);

      if (this.totalElapsed > this.nextFrameTarget) {
        this.nextFrameTarget = this.totalElapsed;
      }

      return framesAdvanced;
    }
  }

  public void reset() {
    if (this.nextFrameTarget > this.totalElapsed) {
      this.totalElapsed = this.nextFrameTarget;
    }

    this.frameStart = 0L;
  }
}
