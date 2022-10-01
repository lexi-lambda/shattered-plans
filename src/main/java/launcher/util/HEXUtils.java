package launcher.util;

import io.netty.buffer.ByteBuf;

public final class HEXUtils {
  private static final String HEX = "0123456789ABCDEF";

  @SuppressWarnings("unused")
  public static String toHexString(final int value) {
    return toHexString(value, 32);
  }

  public static String toHexString(int value, final int bits) {
    if (bits < 0 || bits > 32) throw new IllegalArgumentException();
    if (bits != 32) value &= (1 << bits) - 1;
    final int len = (bits + 3) / 4;
    final char[] chars = new char[len];
    for (int i = len - 1; i >= 0; i--) {
      chars[i] = HEX.charAt(value & 15);
      value >>= 4;
    }
    return new String(chars);
  }

  @SuppressWarnings("unused")
  public static byte[] getHexBytes(final String hex) {
    if (hex.length() % 2 != 0) throw new IllegalArgumentException("bad hex");
    final byte[] data = new byte[hex.length() / 2];
    for (int i = 0; i < data.length; i++) {
      final int x1 = Character.digit(hex.charAt(2 * i), 16);
      if (x1 < 0) throw new NumberFormatException("at " + 2 * i + " for '" + hex.charAt(2 * i) + "'");
      final int x2 = Character.digit(hex.charAt(2 * i + 1), 16);
      if (x2 < 0) throw new NumberFormatException("at " + (2 * i + 1) + " for '" + hex.charAt(2 * i + 1) + "'");
      data[i] = (byte) (x1 << 4 | x2);
    }
    return data;
  }

  @SuppressWarnings("unused")
  public static String toHexString(final byte[] bytes) {
    return toHexString(bytes, 0, bytes.length, 0);
  }

  @SuppressWarnings("unused")
  public static String toHexString(final byte[] bytes, final int off, final int len) {
    return toHexString(bytes, off, len, 0);
  }

  @SuppressWarnings({"SameParameterValue", "unused"})
  public static String toHexString(final byte[] bytes, final int space) {
    return toHexString(bytes, 0, bytes.length, space);
  }

  @SuppressWarnings("SameParameterValue")
  public static String toHexString(final ByteBuf buf, final int space) {
    return toHexString(buf, buf.readerIndex(), buf.readableBytes(), space);
  }

  @SuppressWarnings({"SameParameterValue", "WeakerAccess"})
  public static String toHexString(final ByteBuf buf, final int off, final int len, final int space) {
    final byte[] bytes = new byte[len];
    buf.getBytes(off, bytes, 0, len);
    return toHexString(bytes, 0, len, space);
  }

  @SuppressWarnings("WeakerAccess")
  public static String toHexString(final byte[] bytes, final int off, final int len, final int space) {
    if (bytes.length == 0) return "";
    final int s = space & 0xFF;
    final int nl = space >> 8 & 0xFF;
    int pos = 0, clen = bytes.length * 2;
    if (s > 0) clen += (bytes.length - 1) / s;
    if (nl > 0) clen += (bytes.length - 1) / nl;
    final char[] chars = new char[clen];
    for (int i = 0; i < len; i++) {
      chars[pos++] = HEX.charAt(bytes[off + i] >> 4 & 15);
      chars[pos++] = HEX.charAt(bytes[off + i] & 15);
      if (i + 1 < len) {
        if (s > 0 && i % s == s - 1) {
          if (nl > 0 && i % nl == nl - 1) chars[pos++] = '\n';
          else chars[pos++] = ' ';
        } else if (nl > 0 && i % nl == nl - 1) chars[pos++] = '\n';
      }
    }
    return new String(chars, 0, pos);
  }

}
