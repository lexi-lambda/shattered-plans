package funorb.shatteredplans.client;

import funorb.audio.MusicTrack;
import funorb.audio.PlayingSound;
import funorb.audio.SampledAudioChannel;
import funorb.audio.SoundEffect;
import funorb.audio.SoundLoader;
import funorb.audio.al_;
import funorb.audio.h_;
import funorb.audio.kk_;
import funorb.audio.vk_;
import funorb.cache.ResourceLoader;

import java.util.ArrayList;
import java.util.List;

public final class Sounds {
  public static final int MAX_VOLUME = 256;

  public static SoundEffect SFX_FACTORY_NOISE;
  public static SoundEffect SFX_SHIP_SELECTION;
  public static SoundEffect SFX_SHIP_MOVE_ORDER;
  public static SoundEffect SFX_SHIP_ATTACK_ORDER;
  public static SoundEffect SFX_EXPLOSION;
  public static SoundEffect SFX_NEXT_OPEN;
  public static SoundEffect SFX_NEXT_CLOSE;
  public static MusicTrack MUSIC_INTRO;
  public static MusicTrack MUSIC_IN_GAME_2;
  public static MusicTrack MUSIC_IN_GAME_1;
  public static MusicTrack MUSIC_WIN;
  public static MusicTrack MUSIC_LOSE;

  public static int soundVolume = 256;
  public static int musicVolume = 256;

  public static final List<PlayingSound> playingSounds = new ArrayList<>();
  public static vk_ soundsTn;
  public static h_ musicTn;
  static SampledAudioChannel soundsChannel;
  static SampledAudioChannel musicChannel;

  public static void loadSoundEffects(final ResourceLoader loader1, final ResourceLoader loader2) {
    SoundLoader.globalLoader = new SoundLoader(loader1, loader2);

    SFX_SHIP_SELECTION = SoundEffect.load1("shatteredplans_ship_selection", 256);
    SFX_SHIP_MOVE_ORDER = SoundEffect.load1("shatteredplans_ship_move_order", 256);
    SFX_SHIP_ATTACK_ORDER = SoundEffect.load1("shatteredplans_ship_attack_order", 256);
    SFX_FACTORY_NOISE = SoundEffect.load1("shatteredplans_factory_noise", 256);
    SFX_EXPLOSION = SoundEffect.load2("shatteredplans_explosion", 120);
    SFX_NEXT_OPEN = SoundEffect.load1("shatteredplans_next_open", 256);
    SFX_NEXT_CLOSE = SoundEffect.load1("shatteredplans_next_close", 256);
  }

  public static void loadMusic(final ResourceLoader loader1, final ResourceLoader loader2) {
    MUSIC_INTRO = MusicTrack.load(loader1, "shattered_plans_intro");
    MUSIC_IN_GAME_1 = MusicTrack.load(loader1, "shattered_plans_ingame");
    MUSIC_IN_GAME_2 = MusicTrack.load(loader1, "shattered_plans_ingame_two");
    MUSIC_WIN = MusicTrack.load(loader1, "shattered_plans_win");
    MUSIC_LOSE = MusicTrack.load(loader1, "shattered_plans_lose");

    ShatteredPlansClient.currentTrack = MUSIC_IN_GAME_2;
    musicTn._u.a350(SoundLoader.globalLoader, loader2, MUSIC_INTRO);
    musicTn._u.a350(SoundLoader.globalLoader, loader2, MUSIC_IN_GAME_1);
    musicTn._u.a350(SoundLoader.globalLoader, loader2, MUSIC_IN_GAME_2);
    musicTn._u.f150();

    SoundLoader.globalLoader = null;
  }

  public static void play(final SoundEffect effect) {
    play(effect._f, effect.volume);
  }

  public static PlayingSound play(final SoundEffect effect, final int volume) {
    return play(effect._f, effect.volume * volume / 96);
  }

  private static PlayingSound play(final kk_ var1, final int volume) {
    final al_ var01 = al_.a638(var1, volume);
    assert var01 != null;
    final PlayingSound sound = new PlayingSound(var01);
    playingSounds.add(sound);
    soundsTn.addFirst(var01);
    return sound;
  }

  static void setSoundVolume(final int volume) {
    soundVolume = volume;
    playingSounds.removeIf(sound -> !sound._p.isLinked());
    playingSounds.forEach(sound -> sound._p.setVolume(sound.volume * soundVolume + 128 >> 8));
  }

  public static void setMusicVolume(final int volume) {
    musicVolume = volume;
    if (musicTn != null) {
      musicTn.setVolume(volume);
    }
  }
}
