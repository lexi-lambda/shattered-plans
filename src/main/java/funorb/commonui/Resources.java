package funorb.commonui;

import funorb.cache.ResourceLoader;
import funorb.client.JagexBaseApplet;
import funorb.commonui.renderer.ButtonRenderer;
import funorb.graphics.Drawing;
import funorb.graphics.Font;
import funorb.graphics.PalettedSymbol;
import funorb.graphics.Sprite;
import funorb.graphics.SpriteResource;

public final class Resources {
  public static Sprite[] FRAME_TOP;
  public static Sprite[] FRAME_BOTTOM;
  public static Sprite JAGEX_LOGO_GREY;
  public static Sprite[] VALIDATION;
  public static Font AREZZO_12;
  public static Font AREZZO_14;
  public static Font AREZZO_14_BOLD;

  public static void load(final ResourceLoader spritesLoader, final ResourceLoader fontsLoader, final ResourceLoader dataLoader) {
    FRAME_TOP = SpriteResource.loadSprites(spritesLoader, "commonui", "frame_top");
    FRAME_BOTTOM = SpriteResource.loadSprites(spritesLoader, "commonui", "frame_bottom");
    JAGEX_LOGO_GREY = SpriteResource.loadSprite(spritesLoader, "commonui", "jagex_logo_grey");
    ButtonRenderer.SPRITES = SpriteResource.loadSprites(spritesLoader, "commonui", "button");
    VALIDATION = SpriteResource.loadSprites(spritesLoader, "commonui", "validation");

    AREZZO_12 = SpriteResource.loadPalettedSpriteFont(spritesLoader, fontsLoader, "commonui", "arezzo12");
    AREZZO_14 = SpriteResource.loadPalettedSpriteFont(spritesLoader, fontsLoader, "commonui", "arezzo14");
    AREZZO_14_BOLD = SpriteResource.loadPalettedSpriteFont(spritesLoader, fontsLoader, "commonui", "arezzo14bold");

    final Sprite buttonGif = new Sprite(dataLoader.getResource("button.gif", ""), JagexBaseApplet.canvas);

    final PalettedSymbol[] screenOptions = SpriteResource.loadPalettedSprites(spritesLoader, "commonui", "screen_options");
    final PalettedSymbol[][] var5 = new PalettedSymbol[][]{new PalettedSymbol[4], new PalettedSymbol[4], new PalettedSymbol[4]};
    final int[][] var6 = new int[4][];
    var6[0] = screenOptions[0].palette;

    for (int i = 1; i < var6.length; ++i) {
      var6[i] = var6[0].clone();
    }

    final byte var12 = screenOptions[0].pixels[0];
    var6[1][var12] = 0x2488e6;
    var6[2][var12] = Drawing.WHITE;
    var6[3][var12] = 0x48c0ff;

    for (int i = 0; i < 3; ++i) {
      final PalettedSymbol[] var9 = var5[i];
      for (int j = 0; j < var9.length; ++j) {
        var9[j] = alternatePalette(screenOptions[i], var6[j]);
      }
    }

    Drawing.withLocalContext(() -> {
      buttonGif.installForDrawing();
      Drawing.e115(Drawing.width, Drawing.height);

      final Sprite var13 = new Sprite(buttonGif.height, buttonGif.height);
      var13.installForDrawing();
      buttonGif.c093(0, 0);

      final Sprite var14 = new Sprite(buttonGif.height, buttonGif.height);
      var14.installForDrawing();
      buttonGif.c093(buttonGif.height - buttonGif.width, 0);

      final Sprite var11 = new Sprite(buttonGif.width - 2 * buttonGif.height, buttonGif.height);
      var11.installForDrawing();
      buttonGif.c093(-buttonGif.height, 0);

      ButtonRenderer.SPRITES = new Sprite[]{var13, var11, var14};
    });
  }

  private static PalettedSymbol alternatePalette(final PalettedSymbol symbol, final int[] palette) {
    final PalettedSymbol newSymbol = new PalettedSymbol();
    newSymbol.height = symbol.height;
    newSymbol.width = symbol.width;
    newSymbol.palette = palette;
    newSymbol.advanceY = symbol.advanceY;
    newSymbol.y = symbol.y;
    newSymbol.x = symbol.x;
    newSymbol.pixels = symbol.pixels;
    newSymbol.advanceX = symbol.advanceX;
    return newSymbol;
  }
}
