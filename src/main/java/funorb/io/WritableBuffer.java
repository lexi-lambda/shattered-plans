package funorb.io;

@SuppressWarnings({"SameParameterValue", "unused"})
public interface WritableBuffer {
  void writeByte(int val);

  void writeShort(int val);

  void writeInt(int val);

  void writeLong(long val);

  default void writeU24(final int val) {
    if (val < 0 || val > 0xffffff) {
      throw new IllegalArgumentException("argument does not fit into 24 bits: 0x" + Integer.toHexString(val));
    }
    this.writeByte((val >>> 16) & 0xff);
    this.writeByte((val >>> 8) & 0xff);
    this.writeByte(val & 0xff);
  }

  void writeVariableInt(int val);

  default void writeVariable8_16(final int val) {
    if (val >= 0 && val < 128) {
      this.writeByte(val);
    } else if (val >= 0 && val < 0x8000) {
      this.writeShort(val + 0x8000);
    } else {
      throw new IllegalArgumentException("argument does not fit into 15 bits: 0x" + Integer.toHexString(val));
    }
  }

  void writeBytes(byte[] data);

  void writeBytes(byte[] data, int len);

  void writeNullTerminatedString(String str);

  void writeNullBracketedString(String str);

  void writeCRC(int len);
}
