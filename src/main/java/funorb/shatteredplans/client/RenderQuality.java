package funorb.shatteredplans.client;

import org.intellij.lang.annotations.MagicConstant;

public final class RenderQuality {
  public final boolean antialiasWormholeConnections;
  public final boolean antialiasStarfieldBackground;
  @MagicConstant(valuesFromClass = Level.class)
  public final int statsScreenFadeQuality;
  @MagicConstant(valuesFromClass = Level.class)
  public final int transitionQuality;

  private RenderQuality(final boolean antialiasWormholeConnections,
                        final boolean antialiasStarfieldBackground,
                        @MagicConstant(valuesFromClass = Level.class) final int statsScreenFadeQuality,
                        @MagicConstant(valuesFromClass = Level.class) final int transitionQuality) {
    this.antialiasWormholeConnections = antialiasWormholeConnections;
    this.antialiasStarfieldBackground = antialiasStarfieldBackground;
    this.statsScreenFadeQuality = statsScreenFadeQuality;
    this.transitionQuality = transitionQuality;
  }

  public static RenderQuality low() {
    return new RenderQuality(false, false, Level.LOW, Level.LOW);
  }

  public static RenderQuality medium() {
    return new RenderQuality(true, false, Level.MEDIUM, Level.MEDIUM);
  }

  public static RenderQuality high() {
    return new RenderQuality(true, true, Level.HIGH, Level.HIGH);
  }

  public static final class Level {
    public static final int LOW = 0;
    public static final int MEDIUM = 1;
    public static final int HIGH = 2;
  }
}
