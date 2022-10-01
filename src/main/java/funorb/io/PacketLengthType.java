package funorb.io;

public enum PacketLengthType {
  FIXED(0),
  VARIABLE_BYTE(1),
  VARIABLE_SHORT(2);

  public static final int VARIABLE_BYTE_I = -1;
  public static final int VARIABLE_SHORT_I = -2;

  public final int length;

  PacketLengthType(final int length) {
    this.length = length;
  }
}
