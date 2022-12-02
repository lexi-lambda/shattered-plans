package funorb.commonui;

import java.util.Arrays;
import java.util.Objects;

public abstract class AbstractTextLayout {
  public TextLineMetrics[] lineMetrics;

  /**
   * @param charIndex the index of the character in the original text
   * @return the index of the line in which the character appears
   */
  public final int getCharacterLine(int charIndex) {
    for (int i = 0; i < this.lineMetrics.length; ++i) {
      final TextLineMetrics metrics = this.lineMetrics[i];
      if (charIndex < metrics.charXs.length) {
        return i;
      }
      charIndex -= metrics.getCharCount();
    }

    return this.lineMetrics.length;
  }

  public final int b137() {
    return this.lineMetrics != null && this.lineMetrics.length > 0 ? this.lineMetrics[this.lineMetrics.length - 1].bottom - this.lineMetrics[0].top : 0;
  }

  public final int getWidth() {
    if (this.lineMetrics == null) {
      return -1;
    } else {
      return Arrays.stream(this.lineMetrics).filter(Objects::nonNull)
          .mapToInt(TextLineMetrics::getWidth).max()
          .orElse(-1);
    }
  }

  protected final int a947(final int var2, final int var3, final String var4) {
    int var5 = 0;
    boolean var6 = false;
    final int var7 = var4.length();

    for (int var8 = 0; var7 > var8; ++var8) {
      final char var9 = var4.charAt(var8);
      if (var9 == '<') {
        var6 = true;
      } else if (var9 == '>') {
        var6 = false;
      } else if (!var6 && var9 == ' ') {
        ++var5;
      }
    }

    if (var5 <= 0) {
      return 0;
    } else {
      return (var2 - var3 << 8) / var5;
    }
  }

  public final int a313(final int var1, final int var3) {
    if (this.lineMetrics != null && this.lineMetrics.length != 0 && this.lineMetrics[0].top <= var3) {
      if (this.lineMetrics[this.lineMetrics.length - 1].bottom >= var3) {
        if (this.lineMetrics.length == 1) {
          return this.lineMetrics[0].a527(var1);
        } else {
          int var4 = 0;

          for (final TextLineMetrics var6 : this.lineMetrics) {
            if (var3 >= var6.top && var6.bottom >= var3) {
              final int var7 = var6.a527(var1);
              if (var7 != -1) {
                return var4 + var7;
              }

              return -1;
            }

            var4 += var6.getCharCount();
          }

          return -1;
        }
      } else {
        return -1;
      }
    } else {
      return -1;
    }
  }

  public final int getCharacterX(int charIndex) {
    for (final TextLineMetrics metrics : this.lineMetrics) {
      if (charIndex < metrics.charXs.length) {
        return metrics.charXs[charIndex];
      }
      charIndex -= metrics.getCharCount();
    }
    return 0;
  }
}
