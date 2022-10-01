package funorb.shatteredplans.client.game;

import funorb.audio.PlayingSound;
import funorb.shatteredplans.client.ShatteredPlansClient;
import funorb.shatteredplans.client.Sounds;
import funorb.shatteredplans.map.StarSystem;

public final class CombatExplosion {
  public static final int LIFETIME = 130;
  public final int x;
  public final int y;
  public final StarSystem system;
  public final PlayingSound sound;
  public int ticksAlive = 0;

  public CombatExplosion(final StarSystem system) {
    this.system = system;
    this.sound = Sounds.play(Sounds.SFX_EXPLOSION, 0);

    final double angle = Math.random() * Math.PI * 2.0D;
    final int fac = 0x4000 + ShatteredPlansClient.randomIntBounded(0x4000);
    this.x = (int) (Math.sin(angle) * (double) fac);
    this.y = (int) (Math.cos(angle) * (double) fac);
  }
}
