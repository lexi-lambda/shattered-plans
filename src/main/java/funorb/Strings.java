package funorb;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Range;

import java.util.Arrays;
import java.util.Objects;

public final class Strings {
  @SuppressWarnings("WeakerAccess")
  public static final char NON_BREAKING_SPACE = '\u00A0';
  public static final char EM_DASH = '\u2014';

  /**
   * Contains the characters encoded as {@code 0x80}–{@code 0x9F} under
   * Windows-1252 (commonly known as “Latin-1”). These are the only characters
   * in the Windows-1252 code page that are not encoded the same way in Unicode.
   */
  public static final char[] WINDOWS_1252_CHARS = {'€', 0, '‚', 'ƒ', '„', '…', '†', '‡', 'ˆ', '‰', 'Š', '‹', 'Œ', 0, 'Ž', 0, 0, '‘', '’', '“', '”', '•', '–', '—', '˜', '™', 'š', '›', 'œ', 0, 'ž', 'Ÿ'};
  private static final int WINDOWS_1252_SPECIALS_START = 0x80;
  private static final int WINDOWS_1252_SPECIALS_END   = 0x9f;

  private static final char[] NORMALIZABLE_CHARS = {' ', NON_BREAKING_SPACE, '_', '-', 'à', 'á', 'â', 'ä', 'ã', 'À', 'Á', 'Â', 'Ä', 'Ã', 'è', 'é', 'ê', 'ë', 'È', 'É', 'Ê', 'Ë', 'í', 'î', 'ï', 'Í', 'Î', 'Ï', 'ò', 'ó', 'ô', 'ö', 'õ', 'Ò', 'Ó', 'Ô', 'Ö', 'Õ', 'ù', 'ú', 'û', 'ü', 'Ù', 'Ú', 'Û', 'Ü', 'ç', 'Ç', 'ÿ', 'Ÿ', 'ñ', 'Ñ', 'ß'};
  private static final char[] EXTRA_NORMALIZABLE_CHARS = {'[', ']', '#'};

  private static boolean isSpecialWindows1252Byte(final int n) {
    return n >= WINDOWS_1252_SPECIALS_START && n <= WINDOWS_1252_SPECIALS_END;
  }

  @Contract(pure = true)
  public static char decode1252Char(final byte b) {
    final int n = b & 255;
    if (n == 0) {
      throw new IllegalArgumentException("unexpected NUL char");
    }
    if (isSpecialWindows1252Byte(n)) {
      final char c = WINDOWS_1252_CHARS[n - 128];
      if (c == 0) {
        return '?';
      } else {
        return c;
      }
    }

    return (char) n;
  }

  @Contract(value = "_, _, _ -> new", pure = true)
  public static @NotNull String decode1252String(final byte[] bytes, final int startPos, final int byteLen) {
    final char[] chars = new char[byteLen];
    int strLen = 0;
    for (int i = 0; i < byteLen; ++i) {
      final byte encoded = bytes[i + startPos];
      if (encoded == 0) continue; // ignore NULs
      chars[strLen++] = decode1252Char(encoded);
    }
    return new String(chars, 0, strLen);
  }

  public static String decode1252String(final byte[] bytes) {
    return decode1252String(bytes, 0, bytes.length);
  }

  public static byte encode1252Char(final char c) {
    if (c == 0) {
      return '?';
    } else if (c < WINDOWS_1252_SPECIALS_START || (c > WINDOWS_1252_SPECIALS_END && c <= 0xff)) {
      return (byte) c;
    } else {
      return switch (c) {
        case 0x20ac -> 0xffffff80;
        case 0x201a -> 0xffffff82;
        case 0x0192 -> 0xffffff83;
        case 0x201e -> 0xffffff84;
        case 0x2026 -> 0xffffff85;
        case 0x2020 -> 0xffffff86;
        case 0x2021 -> 0xffffff87;
        case 0x02c6 -> 0xffffff88;
        case 0x2030 -> 0xffffff89;
        case 0x0160 -> 0xffffff8a;
        case 0x2039 -> 0xffffff8b;
        case 0x0152 -> 0xffffff8c;
        case 0x017d -> 0xffffff8e;
        case 0x2018 -> 0xffffff91;
        case 0x2019 -> 0xffffff92;
        case 0x201c -> 0xffffff93;
        case 0x201d -> 0xffffff94;
        case 0x2022 -> 0xffffff95;
        case 0x2013 -> 0xffffff96;
        case 0x2014 -> 0xffffff97;
        case 0x02dc -> 0xffffff98;
        case 0x2122 -> 0xffffff99;
        case 0x0161 -> 0xffffff9a;
        case 0x203a -> 0xffffff9b;
        case 0x0153 -> 0xffffff9c;
        case 0x017e -> 0xffffff9e;
        case 0x0178 -> 0xffffff9f;
        default     -> '?';
      };
    }
  }

  public static int encode1252String(final CharSequence src, final byte[] dest, final int destOffset, final int len) {
    for (int i = 0; i < len; ++i) {
      final char c = src.charAt(i);
      dest[destOffset + i] = encode1252Char(c);
    }
    return len;
  }

  @Contract(value = "_ -> new", pure = true)
  public static byte[] encode1252String(final CharSequence cs) {
    final byte[] bs = new byte[cs.length()];
    encode1252String(cs, bs, 0, cs.length());
    return bs;
  }

  public static boolean isAlphanumeric(final char c) {
    return c >= '0' && c <= '9' || c >= 'A' && c <= 'Z' || c >= 'a' && c <= 'z';
  }

  private static char normalize(final char c) {
    if (c == ' ' || c == NON_BREAKING_SPACE || c == '_' || c == '-') {
      return '_';
    } else if (c == '[' || c == ']' || c == '#') {
      return c;
    } else if (c == 224 || c == 225 || c == 226 || c == 228 || c == 227 || c == 192 || c == 193 || c == 194 || c == 196 || c == 195) {
      return 'a';
    } else if (c == 232 || c == 233 || c == 234 || c == 235 || c == 200 || c == 201 || c == 202 || c == 203) {
      return 'e';
    } else if (c == 237 || c == 238 || c == 239 || c == 205 || c == 206 || c == 207) {
      return 'i';
    } else if (c == 242 || c == 243 || c == 244 || c == 246 || c == 245 || c == 210 || c == 211 || c == 212 || c == 214 || c == 213) {
      return 'o';
    } else if (c == 249 || c == 250 || c == 251 || c == 252 || c == 217 || c == 218 || c == 219 || c == 220) {
      return 'u';
    } else if (c == 231 || c == 199) {
      return 'c';
    } else if (c == 255 || c == 376) {
      return 'y';
    } else if (c == 241 || c == 209) {
      return 'n';
    } else if (c == 223) {
      return 'b';
    } else {
      return Character.toLowerCase(c);
    }
  }

  public static boolean isNormalizable(final char c) {
    if (Character.isISOControl(c)) {
      return false;
    } else if (isAlphanumeric(c)) {
      return true;
    } else {
      for (final char item : NORMALIZABLE_CHARS) {
        if (c == item) {
          return true;
        }
      }

      for (final char item : EXTRA_NORMALIZABLE_CHARS) {
        if (c == item) {
          return true;
        }
      }

      return false;
    }
  }

  @Contract(value = "null -> null", pure = true)
  public static String normalize(final CharSequence str) {
    if (str == null) {
      return null;
    }

    int startPos = 0;
    int endPos = str.length();
    while (startPos < endPos && isSpaceLikeUsernameChar(str.charAt(startPos))) {
      ++startPos;
    }
    while (endPos > startPos && isSpaceLikeUsernameChar(str.charAt(endPos - 1))) {
      --endPos;
    }

    final int len = endPos - startPos;
    if (len < 1 || len > 12) {
      return null;
    }

    final StringBuilder sb = new StringBuilder(len);
    for (int i = startPos; i < endPos; ++i) {
      final char c = str.charAt(i);
      if (isNormalizable(c)) {
        final char normalized = normalize(c);
        if (normalized != 0) {
          sb.append(normalized);
        }
      }
    }

    if (sb.isEmpty()) {
      return null;
    } else {
      return sb.toString();
    }
  }

  public static @NotNull String normalizeIfPossible(final @NotNull String str) {
    return Objects.requireNonNullElse(normalize(str), str);
  }

  public static boolean isSpaceLikeUsernameChar(final char c) {
    return c == ' ' || c == NON_BREAKING_SPACE || c == '_' || c == '-';
  }

  public static boolean isAlpha(final char var1) {
    return var1 >= 'A' && var1 <= 'Z' || var1 >= 'a' && var1 <= 'z';
  }

  public static boolean isDigit(final char c) {
    return c >= '0' && c <= '9';
  }

  @Contract(pure = true)
  public static String format(final String template, final String... params) {
    final int var3 = template.length();
    int var4 = var3;
    int var5 = 0;

    while (true) {
      final int var6 = template.indexOf("<%", var5);
      int var8;
      if (var6 < 0) {

        final StringBuilder var11 = new StringBuilder(var4);
        var5 = 0;
        int var12 = 0;

        while (true) {
          var8 = template.indexOf("<%", var5);
          if (var8 < 0) {
            var11.append(template.substring(var12));
            return var11.toString();
          }

          var5 = 2 + var8;
          while (var3 > var5 && isDigit(template.charAt(var5))) {
            ++var5;
          }

          final String var9 = template.substring(var8 + 2, var5);
          if (a783wk(var9) && var5 < var3 && template.charAt(var5) == '>') {
            ++var5;
            final int var10 = parseDecimalInteger(var9);
            var11.append(template, var12, var8);
            var11.append(params[var10]);
            var12 = var5;
          }
        }
      }

      var5 = var6 + 2;
      while (var5 < var3 && isDigit(template.charAt(var5))) {
        ++var5;
      }

      final String var7 = template.substring(2 + var6, var5);
      if (a783wk(var7) && var5 < var3 && template.charAt(var5) == '>') {
        ++var5;
        var8 = parseDecimalInteger(var7);
        var4 += params[var8].length() - var5 + var6;
      }
    }
  }

  public static int parseDecimalInteger(final CharSequence var0) {
    return parseInteger(10, var0);
  }

  @Contract(pure = true)
  private static int parseInteger(@Range(from = 2, to = 36) final int base, final CharSequence var1) {
    boolean var3 = false;
    boolean var5 = false;
    int var6 = 0;
    final int var7 = var1.length();

    for (int var8 = 0; var7 > var8; ++var8) {
      final char var9 = var1.charAt(var8);
      if (var8 == 0) {
        if (var9 == '-') {
          var3 = true;
          continue;
        }

        if (var9 == '+') {
          continue;
        }
      }

      int var11;
      if (var9 >= '0' && var9 <= '9') {
        var11 = var9 - 48;
      } else if (var9 >= 'A' && var9 <= 'Z') {
        var11 = var9 - 55;
      } else {
        if (var9 < 'a' || var9 > 'z') {
          throw new NumberFormatException();
        }

        var11 = var9 - 87;
      }

      if (var11 >= base) {
        throw new NumberFormatException();
      }

      if (var3) {
        var11 = -var11;
      }

      final int var10 = var6 * base + var11;
      if (var10 / base != var6) {
        throw new NumberFormatException();
      }

      var5 = true;
      var6 = var10;
    }

    if (var5) {
      return var6;
    } else {
      throw new NumberFormatException();
    }
  }

  @Contract(pure = true)
  public static boolean a783wk(final CharSequence var1) {
    boolean var4 = false;
    boolean var5 = false;
    int var6 = 0;
    final int var7 = var1.length();

    for (int var9 = 0; var9 < var7; ++var9) {
      final char var10 = var1.charAt(var9);
      if (var9 == 0) {
        if (var10 == '-') {
          var4 = true;
          continue;
        }

        if (var10 == '+') {
          continue;
        }
      }

      int var12;
      if (var10 >= '0' && var10 <= '9') {
        var12 = var10 - 48;
      } else if (var10 >= 'A' && var10 <= 'Z') {
        var12 = var10 - 55;
      } else {
        if (var10 < 'a' || var10 > 'z') {
          return false;
        }

        var12 = var10 - 87;
      }

      if (var12 >= 10) {
        return false;
      }

      if (var4) {
        var12 = -var12;
      }

      final int var11 = var6 * 10 + var12;
      if (var11 / 10 != var6) {
        return false;
      }

      var6 = var11;
      var5 = true;
    }

    return var5;
  }

  public static int parseHexInteger(final CharSequence var0) {
    return parseInteger(16, var0);
  }

  public static String formatNamed(final String template, final String paramName, final String paramValue) {
    int var5 = template.length();
    int var6 = 0;
    final String var7 = "<%" + paramName + ">";

    while (true) {
      final int var8 = template.indexOf(var7, var6);
      if (var8 < 0) {
        var6 = 0;
        final StringBuilder var10 = new StringBuilder(var5);

        while (true) {
          final int var9 = template.indexOf(var7, var6);
          if (var9 < 0) {
            var10.append(template, var6, template.length());
            return var10.toString();
          }

          var10.append(template, var6, var9);
          var10.append(paramValue);
          var6 = var7.length() + var9;
        }
      }

      var6 = var8 + var7.length();
      var5 += paramValue.length() - var7.length();
    }
  }

  public static String[] splitLines(final String str) {
    final String[] lines = str.split("\n");
    Arrays.setAll(lines, var4 -> lines[var4].trim());
    return lines;
  }

  public static int lastIndexOf(final String str, final String sub) {
    int nextIndex = str.indexOf(sub);
    int prevIndex = nextIndex;
    while (nextIndex != -1) {
      prevIndex = nextIndex;
      nextIndex = str.indexOf(sub, 1 + nextIndex);
    }
    return prevIndex;
  }
}
