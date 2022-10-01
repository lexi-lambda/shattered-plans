package funorb.io;

import org.jetbrains.annotations.NotNull;

@SuppressWarnings({"SameParameterValue", "unused"})
public interface ReadableBuffer {
  int readerIndex();

  void skipBytes(int len);

  byte readByte();

  int readUByte();

  int readUShort();

  int readInt();

  long readLong();

  default int readU24() {
    final int b1 = this.readUByte();
    final int b2 = this.readUByte();
    final int b3 = this.readUByte();
    return (b1 << 16) + (b2 << 8) + b3;
  }

  int readVariable8_16();

  void readBytes(byte[] dest, int len);

  default void readBytes(final byte[] dest) {
    this.readBytes(dest, dest.length);
  }

  @NotNull String readNullTerminatedString();

  @NotNull String readNullBracketedString();
}
