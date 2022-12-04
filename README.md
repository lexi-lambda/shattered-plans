# Shattered Plans

Shattered Plans is a turn-based strategy game by [Jagex Ltd.][jagex] It was originally released in 2008 as part of [FunOrb][], which was sadly shut down in 2018 without any successor. This repository is a fan project that restores Shattered Plans to a playable state. Based on a decompiled archive of the original Shattered Plans client, it includes a reimplementation of the original FunOrb server, which makes Shattered Plans playable again in both singleplayer and multiplayer modes.

[![Screenshot of Shattered Plans gameplay.][screenshot.png]][screenshot.png]

## Running the game

Running Shattered Plans requires [Java 17 or newer][download-java] and [the latest version of the game JAR][download-shattered-plans]. You can then run the game by running the following command:

```sh
java -jar shattered-plans-0.0.5.jar --local-server
```

As the name suggests, passing the `--local-server` flag will run a Shattered Plans server locally, allowing you to play the game in singleplayer mode. Other players can connect to your server by running

```sh
java -jar shattered-plans-0.0.5.jar --host <HOST>
```

replacing `<HOST>` with your IP address. By default, the server runs on port 43594, but you can specify a different port using the `--port` flag. A few other options are also available, pass `--help` to see them all.

### Experimental: Adjusting the user interface scale

Limited support for adjusting the UI scale is available by setting the `funorb.shatteredplans.client.uiScale` JVM property to a numeric value. The default is `1.0`. Larger values increase the internal resolution of the game’s renderer, which effectively makes individual user interface elements smaller. For example, the following command will force the game to render at 1.5× standard resolution:

```sh
java -Dfunorb.shatteredplans.client.uiScale=1.5 -jar shattered-plans-0.0.5.jar
```

This is particularly useful on large screens, as it makes the in-game user interface less cramped. However, the game was originally designed to run at a fixed resolution of 640×480, so altering the UI scale can cause layout glitches and other visual issues. The in-game user interface has been patched to automatically adapt to larger UI scales, but some visual issues remain.

## What works, what doesn’t, and other limitations

All singleplayer functionality should work flawlessly. Most multiplayer functionality works as well. However, there are some bugs, unimplemented features, and other limitations:

  * When you start the game, you will be presented with a login screen. You may enter any username/password combination you like, and the server will not check it. In fact, attempting to create an account will not work: the server does not record any persistent user state.

  * Lobby and in-game chat are supported, but adding users to your friends or ignore list is not, so there is no way to send private messages.

  * Resigning is implemented, but offering a draw or a rematch is not currently supported.

  * Rated games are not implemented.

  * Achievements are not implemented.

  * There may be bugs in the server protocol, which can lead to disconnects or desyncs. Please report any bugs you come across, providing as much information as possible, and I will do my best to fix them.

There are also some very minor improvements over the original game, but these will most likely not even be noticeable to most players.

## More information

The game includes both an in-game tutorial and in-game instructions to teach you how to play the game. Some additional information is also available [on the FunOrb wiki][funorb-wiki:shattered-plans].

If you’d like to find people to play with, or if you have any further questions, consider joining [the FunOrb discord server][funorb-discord]. This is obviously a small hobby project, so I cannot guarantee my time, but I’ll do my best to be helpful.

## Acknowledgments and disclaimers

Shattered Plans and FunOrb are the property of [Jagex Ltd.][jagex] This project is a fan effort to keep a wonderful, creative game from being permanently lost. I do not commercially benefit from this project in any way, and it does not compete in any way with any of Jagex’s current commercial offerings, none of which are even remotely similar to Shattered Plans.

[download-java]: https://www.oracle.com/java/technologies/downloads/
[download-shattered-plans]: https://github.com/lexi-lambda/shattered-plans/releases/download/v0.0.5/shatteredplans-0.0.5.jar
[FunOrb]: https://en.wikipedia.org/wiki/FunOrb
[funorb-discord]: https://discord.gg/MGfDrDf
[funorb-wiki:shattered-plans]: https://funorb.fandom.com/wiki/Shattered_Plans
[jagex]: https://www.jagex.com/
[screenshot.png]: docs/screenshot.png
