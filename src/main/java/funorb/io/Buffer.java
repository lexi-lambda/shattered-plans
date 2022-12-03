package funorb.io;

import funorb.Strings;
import funorb.shatteredplans.client.JagexApplet;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.stream.IntStream;
import java.util.zip.CRC32;

public class Buffer implements ReadableBuffer, WritableBuffer {
  private static final int XTEA_MAGIC = 0x9e3779b9;
  private static final int XTEA_ROUNDS = 32;
  private static final int XTEA_BLOCK_SIZE = 8;
  public static BigInteger RSA_EXPONENT = new BigInteger("65537");
  private static final BigInteger RSA_MODULO = new BigInteger("6757747274818513864204534133465045479284128469717186816691454417744823753827902036844748836683348383638677747113757906301249837209713747402067689777172847");

  private static final int[] _vgw = new int[256];
  private static Buffer rsaBuffer;
  private static Buffer xteaBuffer;
  static {
    for (int var1 = 0; var1 < 256; ++var1) {
      int var0 = var1;

      for (int var2 = 0; var2 < 8; ++var2) {
        if ((var0 & 1) == 1) {
          var0 = -306674912 ^ var0 >>> 1;
        } else {
          var0 >>>= 1;
        }
      }

      _vgw[var1] = var0;
    }
  }

  public byte[] data;
  public int pos;

  public Buffer(final int size) {
    this.data = new byte[size];
    this.pos = 0;
  }

  public Buffer(final byte[] data) {
    this.data = data;
    this.pos = 0;
  }

  private static int computeCrc(final byte[] data, final int startPos, final int endPos) {
    int var3 = -1;

    for (int var4 = startPos; var4 < endPos; ++var4) {
      var3 = var3 >>> 8 ^ _vgw[255 & (var3 ^ data[var4])];
    }

    return ~var3;
  }

  public static int computeCrc(final byte[] data, final int len) {
    return computeCrc(data, 0, len);
  }

  private static int alignForXTEA(final int n) {
    if ((n & 7) == 0) {
      return n;
    }
    return n + (8 - (n & 7));
  }

  @Override
  @SuppressWarnings("unused")
  public int readerIndex() {
    return this.pos;
  }

  @Override
  public final void skipBytes(final int len) {
    this.pos += len;
  }

  @Override
  public final @NotNull String readNullBracketedString() {
    final byte var2 = this.data[this.pos++];
    if (var2 == 0) {
      return this.readNullTerminatedString();
    } else {
      throw new IllegalStateException("");
    }
  }

  @Override
  public final void writeNullBracketedString(final String str) {
    final int var3 = str.indexOf(0);
    if (var3 >= 0) {
      throw new IllegalArgumentException("");
    } else {
      this.data[this.pos++] = 0;
      this.pos += Strings.encode1252String(str, this.data, this.pos, str.length());
      this.data[this.pos++] = 0;
    }
  }

  public final void writeSizePrefixBackwards(final int length) {
    this.data[this.pos - length - 4] = (byte) (length >> 24);
    this.data[this.pos - length - 3] = (byte) (length >> 16);
    this.data[this.pos - length - 2] = (byte) (length >> 8);
    this.data[this.pos - length - 1] = (byte) length;
  }

  @Override
  public final void writeByte(final int val) {
    this.data[this.pos++] = (byte) val;
  }

  private void zeroPad(final int len) {
    while (this.pos < len) {
      this.data[this.pos++] = 0;
    }
  }

  public final void writeDigest(final int startPos) {
    this.writeInt(computeCrc(this.data, startPos, this.pos));
  }

  public final int d410() {
    final int nextByte = this.data[this.pos] & 255;
    if (nextByte < 128) {
      return this.readUByte() - 64;
    } else {
      return this.readUShort() - 0xC000;
    }
  }

  public final int readVariableInt() {
    int val = 0;
    byte low = this.data[this.pos++];
    while ((low & 0x80) != 0) {
      val = (val | (low & 0x7f)) << 7;
      low = this.data[this.pos++];
    }

    return val | low;
  }

  @Override
  @SuppressWarnings("unused")
  public final void writeVariableInt(final int val) {
    if (val >= (1 << 21)) this.data[this.pos++] = (byte) ( (val >>> 21)         | 0x80);
    if (val >= (1 << 14)) this.data[this.pos++] = (byte) (((val >>> 14) & 0x7f) | 0x80);
    if (val >= (1 <<  7)) this.data[this.pos++] = (byte) (((val >>>  7) & 0x7f) | 0x80);
    this.data[this.pos++] = (byte) (val & 0x7f);
  }

  public final void writeVariableInt_v2(final int val) {
    if ((val & 0xffffff80) != 0) {
      if ((val & 0xffffc000) != 0) {
        if ((0xffe00000 & val) != 0) {
          if ((0xf0000000 & val) != 0) {
            this.writeByte(val >>> 28 | 0x80);
          }
          this.writeByte(val >>> 21 | 0x80);
        }
        this.writeByte(val >>> 14 | 0x80);
      }
      this.writeByte(val >>> 7 | 0x80);
    }
    this.writeByte(0x7f & val);
  }

  @Override
  public final byte readByte() {
    return this.data[this.pos++];
  }

  @Override
  public final int readVariable8_16() {
    final int nextByte = this.data[this.pos] & 255;
    if ((nextByte & 0x80) == 0) {
      return this.readUByte();
    } else {
      return this.readUShort() - 0x8000;
    }
  }

  @Override
  public final void writeInt(final int val) {
    this.data[this.pos++] = (byte) (val >> 24);
    this.data[this.pos++] = (byte) (val >> 16);
    this.data[this.pos++] = (byte) (val >> 8);
    this.data[this.pos++] = (byte) val;
  }

  @Override
  public final void readBytes(final byte[] dest, final int len) {
    for (int i = 0; i < len; ++i) {
      dest[i] = this.data[this.pos++];
    }
  }

  @Override
  public final int readUShort() {
    this.pos += 2;
    return (this.data[this.pos - 1] & 255)
        + ((this.data[this.pos - 2] & 255) << 8);
  }

  public final int readVariable16_32() {
    if (this.data[this.pos] >= 0) {
      return this.readUShort();
    } else {
      return this.readInt() & Integer.MAX_VALUE;
    }
  }

  @Override
  public final int readUByte() {
    return this.data[this.pos++] & 255;
  }

  @Override
  public final void writeShort(final int val) {
    this.data[this.pos++] = (byte) (val >> 8);
    this.data[this.pos++] = (byte) val;
  }

  @Override
  public final void writeNullTerminatedString(final String str) {
    final int var3 = str.indexOf(0);
    if (var3 < 0) {
      this.pos += Strings.encode1252String(str, this.data, this.pos, str.length());
      this.data[this.pos++] = (byte) 0;
    } else {
      throw new IllegalArgumentException("");
    }
  }

  private void writeU56(final long val) {
    this.data[this.pos++] = (byte) ((int) (val >> 48));
    this.data[this.pos++] = (byte) ((int) (val >> 40));
    this.data[this.pos++] = (byte) ((int) (val >> 32));
    this.data[this.pos++] = (byte) ((int) (val >> 24));
    this.data[this.pos++] = (byte) ((int) (val >> 16));
    this.data[this.pos++] = (byte) ((int) (val >> 8));
    this.data[this.pos++] = (byte) ((int) val);
  }

  @SuppressWarnings("UnusedReturnValue")
  public final long readU56() {
    return ((long) (this.data[this.pos++] & 255) << 48)
         + ((long) (this.data[this.pos++] & 255) << 40)
         + ((long) (this.data[this.pos++] & 255) << 32)
         + ((long) (this.data[this.pos++] & 255) << 24)
         + ((long) (this.data[this.pos++] & 255) << 16)
         + ((long) (this.data[this.pos++] & 255) << 8)
         +  (long) (this.data[this.pos++] & 255);
  }

  @Override
  public final void writeLong(final long val) {
    this.data[this.pos++] = (byte) ((int) (val >> 56));
    this.data[this.pos++] = (byte) ((int) (val >> 48));
    this.data[this.pos++] = (byte) ((int) (val >> 40));
    this.data[this.pos++] = (byte) ((int) (val >> 32));
    this.data[this.pos++] = (byte) ((int) (val >> 24));
    this.data[this.pos++] = (byte) ((int) (val >> 16));
    this.data[this.pos++] = (byte) ((int) (val >> 8));
    this.data[this.pos++] = (byte) ((int) val);
  }

  @Override
  public final void writeBytes(final byte[] data, final int len) {
    for (int i = 0; i < len; ++i) {
      this.data[this.pos++] = data[i];
    }
  }

  @Override
  public final void writeBytes(final byte[] data) {
    this.writeBytes(data, data.length);
  }

  public final void writeBytes(final int... data) {
    for (final int b : data) {
      this.writeByte(b);
    }
  }

  @Override
  public final @NotNull String readNullTerminatedString() {
    final int startPos = this.pos;

    while (this.data[this.pos] != 0) {
      this.pos++;
    }
    this.pos++;

    final int len = this.pos - startPos - 1;
    return len == 0 ? "" : Strings.decode1252String(this.data, startPos, len);
  }

  public final void writePasswordHash(final String var2) {
    long var3 = 0L;
    long var5 = 0L;
    final int var7 = var2.length();

    for (int var8 = 19; var8 >= 0; --var8) {
      var3 *= 38L;
      if (var7 > var8) {
        final char var9 = var2.charAt(var8);
        if (var9 >= 'A' && var9 <= 'Z') {
          var3 += var9 - 63;
        } else if (var9 >= 'a' && var9 <= 'z') {
          var3 += var9 + 2 - 97;
        } else if (var9 >= '0' && var9 <= '9') {
          var3 += 28 + var9 - 48;
        } else {
          ++var3;
        }
      }

      if (var8 == 10) {
        var5 = var3;
        var3 = 0L;
      }
    }

    this.writeU56(var3);
    this.writeU56(var5);
  }

  public final void writeI40(final long val) {
    this.data[this.pos++] = (byte) ((int) (val >> 32));
    this.data[this.pos++] = (byte) ((int) (val >> 24));
    this.data[this.pos++] = (byte) ((int) (val >> 16));
    this.data[this.pos++] = (byte) ((int) (val >> 8));
    this.data[this.pos++] = (byte) ((int) val);
  }

  @Override
  public final long readLong() {
    final long hi = (long) this.readInt() & 0xffffffffL;
    final long lo = (long) this.readInt() & 0xffffffffL;
    return (hi << 32) + lo;
  }

  public final @Nullable String readNullableNullTerminatedString() {
    if (this.data[this.pos] == 0) {
      ++this.pos;
      return null;
    } else {
      return this.readNullTerminatedString();
    }
  }

  @Override
  public final int readInt() {
    this.pos += 4;
    return (this.data[this.pos - 1] & 255)
        + ((this.data[this.pos - 2] & 255) << 8)
        + ((this.data[this.pos - 3] & 255) << 16)
        + ((this.data[this.pos - 4] & 255) << 24);
  }

  private void encryptXTEA(final int[] key) {
    assert this.pos % XTEA_BLOCK_SIZE == 0;
    final int blocks = this.pos / XTEA_BLOCK_SIZE;
    this.pos = 0;

    for (int i = 0; i < blocks; ++i) {
      int value1 = this.readInt();
      int value2 = this.readInt();
      int sum = 0;

      for (int j = 0; j < XTEA_ROUNDS; j++) {
        value1 += (value2 + ((value2 << 4) ^ (value2 >>> 5))) ^ (sum + key[sum & 3]);
        sum += XTEA_MAGIC;
        value2 += (value1 + ((value1 << 4) ^ (value1 >>> 5))) ^ (sum + key[(sum >>> 11) & 3]);
      }

      this.pos -= XTEA_BLOCK_SIZE;
      this.writeInt(value1);
      this.writeInt(value2);
    }
  }

  private void decryptXTEA(final int[] key, final int len) {
    assert len % XTEA_BLOCK_SIZE == 0;
    final int blocks = len / XTEA_BLOCK_SIZE;
    final int startPos = this.pos;
    for (int i = 0; i < blocks; i++) {
      int value1 = this.readInt();
      int value2 = this.readInt();

      @SuppressWarnings("NumericOverflow")
      int sum = XTEA_MAGIC * XTEA_ROUNDS;
      for (int j = 0; j < XTEA_ROUNDS; j++) {
        value2 -= (value1 + ((value1 << 4) ^ (value1 >>> 5))) ^ (sum + key[(sum >>> 11) & 3]);
        sum -= XTEA_MAGIC;
        value1 -= (value2 + ((value2 << 4) ^ (value2 >>> 5))) ^ (sum + key[sum & 3]);
      }

      this.pos -= XTEA_BLOCK_SIZE;
      this.writeInt(value1);
      this.writeInt(value2);
    }
    this.pos = startPos;
  }

  public final void encryptRSA() {
    final int len = this.pos;
    this.pos = 0;
    final byte[] plaintext = new byte[len];
    this.readBytes(plaintext, len);
    final byte[] encrypted = new BigInteger(plaintext).modPow(RSA_EXPONENT, RSA_MODULO).toByteArray();
    this.pos = 0;
    this.writeShort(encrypted.length);
    this.writeBytes(encrypted);
  }

  @SuppressWarnings("SameParameterValue")
  private Buffer decryptRSA(final BigInteger exponent) {
    final int len = this.readUShort();
    final byte[] encrypted = new byte[len];
    this.readBytes(encrypted, len);
    final byte[] plaintext = new BigInteger(encrypted).modPow(exponent, RSA_MODULO).toByteArray();
    return new Buffer(plaintext);
  }

  public final void writeEncrypted(final byte[] plaintext, final int len) {
    final int alignedLen = alignForXTEA(len);
    final int[] key = new int[4];
    Arrays.setAll(key, i -> JagexApplet.secureRandom.nextInt());

    if (xteaBuffer == null || xteaBuffer.data.length < alignedLen) {
      xteaBuffer = new Buffer(alignedLen);
    }

    xteaBuffer.pos = 0;
    xteaBuffer.writeBytes(plaintext, len);
    xteaBuffer.zeroPad(alignedLen);
    xteaBuffer.encryptXTEA(key);

    if (rsaBuffer == null || rsaBuffer.data.length < 100) {
      rsaBuffer = new Buffer(100);
    }

    rsaBuffer.pos = 0;
    rsaBuffer.writeByte(10);
    Arrays.stream(key).forEach(rsaBuffer::writeInt);

    rsaBuffer.writeShort(len);
    rsaBuffer.encryptRSA();
    this.writeBytes(rsaBuffer.data, rsaBuffer.pos);
    this.writeBytes(xteaBuffer.data, xteaBuffer.pos);
  }

  @SuppressWarnings("SameParameterValue")
  public final void decrypt(final BigInteger exponent, final int len) {
    final Buffer keyBuffer = this.decryptRSA(exponent);
    final int keyLen = keyBuffer.readUByte();
    assert keyLen == 10;
    final int[] key = IntStream.range(0, 4).map(i -> keyBuffer.readInt()).toArray();
    this.decryptXTEA(key, len - this.pos);
  }

  public final boolean f427() {
    this.pos -= 4;
    final int var2 = computeCrc(this.data, 0, this.pos);

    final int var3 = this.readInt();
    return var2 == var3;
  }

  public final void insertLengthByte(final int len) {
    this.data[this.pos - len - 1] = (byte) len;
  }

  public final void withLengthByte(final Runnable action) {
    final int startPos = ++this.pos;
    action.run();
    this.insertLengthByte(this.pos - startPos);
  }

  private void insertLengthShort(final int len) {
    this.data[this.pos - len - 2] = (byte) (len >> 8);
    this.data[this.pos - len - 1] = (byte) len;
  }

  public final void withLengthShort(final Runnable action) {
    this.pos += 2;
    final int startPos = this.pos;
    action.run();
    this.insertLengthShort(this.pos - startPos);
  }

  public final void writeI24(@SuppressWarnings("SameParameterValue") final int val) {
    this.data[this.pos++] = (byte) (val >> 16);
    this.data[this.pos++] = (byte) (val >> 8);
    this.data[this.pos++] = (byte) val;
  }

  @Override
  @SuppressWarnings("unused")
  public final void writeCRC(final int len) {
    final CRC32 crc = new CRC32();
    crc.update(this.data, this.pos - len, len);
    this.writeInt((int) crc.getValue());
  }
}
