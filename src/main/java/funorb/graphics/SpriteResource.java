package funorb.graphics;

import funorb.cache.ResourceLoader;
import funorb.io.Buffer;

import java.util.List;
import java.util.stream.IntStream;

public final class SpriteResource {
  public static Sprite loadSprite(final ResourceLoader loader, final String item) {
    return loadSprite(loader, "", item);
  }

  public static Sprite loadSprite(final ResourceLoader loader, final String group, final String item) {
    return parseSprite(loader.getResource(group, item));
  }

  public static Sprite[] loadSprites(final ResourceLoader loader, final String group, final String item) {
    return parseSprites(loader.getResource(group, item));
  }

  public static PalettedSymbol[] loadPalettedSprites(final ResourceLoader loader, final String group, final String item) {
    return parsePalettedSprites(loader.getResource(group, item));
  }

  public static SpriteFont loadSpriteFont(final ResourceLoader spriteLoader, final ResourceLoader fontLoader, final String item) {
    final byte[] spriteData = spriteLoader.getResource("lobby", item);
    final byte[] fontData = fontLoader.getResource("lobby", item);
    return parseSpriteFont(spriteData, fontData);
  }

  public static PalettedSpriteFont loadPalettedSpriteFont(final ResourceLoader spriteLoader, final ResourceLoader fontLoader, final String group, final String item) {
    final byte[] spriteData = spriteLoader.getResource(group, item);
    final byte[] fontData = fontLoader.getResource(group, item);
    return parsePalettedSpriteFont(spriteData, fontData);
  }

  private static Sprite[] parseSprites(final byte[] data) {
    return parseSpriteData(data, spriteConstructor).toArray(Sprite[]::new);
  }

  private static Sprite parseSprite(final byte[] data) {
    final List<Sprite> sprites = parseSpriteData(data, spriteConstructor);
    assert sprites.size() == 1;
    return sprites.get(0);
  }

  private static PalettedSymbol[] parsePalettedSprites(final byte[] data) {
    return parseSpriteData(data, palettedSpriteConstructor).toArray(PalettedSymbol[]::new);
  }

  private static SpriteFont parseSpriteFont(final byte[] spriteData, final byte[] fontData) {
    return parseSpriteData(spriteData,
        (count, offsetX, offsetY, xs, ys, widths, heights, palette, pixels, alphas) ->
            new SpriteFont(fontData, xs, ys, widths, heights, pixels));
  }

  private static PalettedSpriteFont parsePalettedSpriteFont(final byte[] spriteData, final byte[] fontData) {
    return parseSpriteData(spriteData,
        (count, offsetX, offsetY, xs, ys, widths, heights, palette, pixels, alphas) ->
            new PalettedSpriteFont(fontData, xs, ys, widths, heights, palette, pixels));
  }

  private static <T> List<T> parseSpriteData(final byte[] data, final SingleConstructor<T> ctor) {
    return parseSpriteData(data, listConstructor(ctor));
  }

  private static <T> T parseSpriteData(final byte[] data, final BulkConstructor<T> ctor) {
    final Buffer reader = new Buffer(data);

    reader.pos = data.length - 2;
    final int sdImageCount = reader.readUShort();

    reader.pos = data.length - 7 - (sdImageCount * 8);
    final int sdOffsetX = reader.readUShort();
    final int sdOffsetY = reader.readUShort();
    final int paletteSize = reader.readUByte() + 1;

    final int[] sdXs = IntStream.range(0, sdImageCount).map(i -> reader.readUShort()).toArray();
    final int[] sdYs = IntStream.range(0, sdImageCount).map(i -> reader.readUShort()).toArray();
    final int[] sdWidths = IntStream.range(0, sdImageCount).map(i -> reader.readUShort()).toArray();
    final int[] sdHeights = IntStream.range(0, sdImageCount).map(i -> reader.readUShort()).toArray();

    reader.pos = data.length - 7 - (3 * (paletteSize - 1)) - (sdImageCount * 8);
    final int[] sdRgbPalette = new int[paletteSize];
    for (int i = 1; i < paletteSize; ++i) {
      sdRgbPalette[i] = reader.readU24();
      if (sdRgbPalette[i] == 0) {
        sdRgbPalette[i] = 1;
      }
    }

    final byte[][] sdAlphas = new byte[sdImageCount][];
    final byte[][] sdPixels = new byte[sdImageCount][];
    reader.pos = 0;
    for (int i = 0; i < sdImageCount; ++i) {
      final int width = sdWidths[i];
      final int height = sdHeights[i];
      final int var7 = height * width;
      final byte[] pixels = sdPixels[i] = new byte[var7];
      final byte[] alphas = new byte[var7];
      boolean usesAlpha = false;
      final int var11 = reader.readUByte();
      int var12;
      if ((var11 & 1) == 0) {
        for (var12 = 0; var7 > var12; ++var12) {
          pixels[var12] = reader.readByte();
        }

        if ((var11 & 2) != 0) {
          for (var12 = 0; var12 < var7; ++var12) {
            final byte var13 = alphas[var12] = reader.readByte();
            usesAlpha |= var13 != -1;
          }
        }
      } else {
        var12 = 0;

        label90:
        while (true) {
          int var15;
          if (width <= var12) {
            if ((var11 & 2) == 0) {
              break;
            }

            var12 = 0;

            while (true) {
              if (width <= var12) {
                break label90;
              }

              for (var15 = 0; height > var15; ++var15) {
                final byte var14 = alphas[var12 + width * var15] = reader.readByte();
                usesAlpha |= var14 != -1;
              }

              ++var12;
            }
          }

          for (var15 = 0; var15 < height; ++var15) {
            pixels[var12 + width * var15] = reader.readByte();
          }

          ++var12;
        }
      }

      if (usesAlpha) {
        sdAlphas[i] = alphas;
      }
    }

    return ctor.construct(sdImageCount, sdOffsetX, sdOffsetY, sdXs, sdYs, sdWidths, sdHeights, sdRgbPalette, sdPixels, sdAlphas);
  }

  @FunctionalInterface
  public interface SingleConstructor<T> {
    T construct(int offsetX, int offsetY, int x, int y, int width, int height, int[] palette, byte[] pixels, byte[] alphas);
  }

  @FunctionalInterface
  public interface BulkConstructor<T> {
    T construct(int count, int offsetX, int offsetY, int[] xs, int[] ys, int[] widths, int[] heights, int[] palette, byte[][] pixels, byte[][] alphas);
  }

  private static final SingleConstructor<Sprite> spriteConstructor = (offsetX, offsetY, x, y, width, height, palette, pixels, alphas) -> {
    final int pixelCount = height * width;
    if (alphas == null) {
      final int[] rgbs = IntStream.range(0, pixelCount).map(i -> palette[(int) pixels[i] & 255]).toArray();
      return new Sprite(offsetX, offsetY, x, y, width, height, rgbs);
    } else {
      final int[] rgbas = IntStream.range(0, pixelCount).map(i -> palette[(int) pixels[i] & 255] | ((alphas[i] & 0xff) << 24)).toArray();
      return new ArgbSprite(offsetX, offsetY, x, y, width, height, rgbas);
    }
  };

  private static final SingleConstructor<PalettedSymbol> palettedSpriteConstructor = (offsetX, offsetY, x, y, width, height, palette, pixels, alphas) ->
    new PalettedSymbol(offsetX, offsetY, x, y, width, height, pixels, palette);

  private static <T> BulkConstructor<List<T>> listConstructor(final SingleConstructor<T> ctor) {
    return (count, offsetX, offsetY, xs, ys, widths, heights, palette, pixels, alphas) ->
        IntStream.range(0, count)
            .mapToObj(i -> ctor.construct(offsetX, offsetY, xs[i], ys[i], widths[i], heights[i], palette, pixels[i], alphas[i]))
            .toList();
  }
}
