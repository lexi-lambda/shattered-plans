package funorb.io;

import funorb.Strings;
import funorb.shatteredplans.S2CPacket;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.util.ReferenceCounted;
import org.jetbrains.annotations.NotNull;

import java.util.zip.CRC32;

public final class Packet implements ReferenceCounted, ReadableBuffer, WritableBuffer {
  public static final Packet S2C_KEEPALIVE = new Packet(S2CPacket.Type.KEEPALIVE, PacketLengthType.FIXED, Unpooled.EMPTY_BUFFER);
  public static final Packet S2C_ENTER_MP = new Packet(S2CPacket.Type.ENTER_MP, PacketLengthType.FIXED, Unpooled.EMPTY_BUFFER);
  public static final Packet S2C_LEAVE_MP = new Packet(S2CPacket.Type.LEAVE_MP, PacketLengthType.FIXED, Unpooled.EMPTY_BUFFER);
  public static final Packet S2C_LEAVE_GAME = new Packet(S2CPacket.Type.LEAVE_GAME, PacketLengthType.FIXED, Unpooled.EMPTY_BUFFER);

  public final int type;
  public final boolean typeIsCiphered;
  public final PacketLengthType lengthType;
  public final ByteBuf payload;

  @SuppressWarnings("WeakerAccess")
  public Packet(final int type, final boolean typeIsCiphered, final PacketLengthType lengthType, final ByteBuf payload) {
    this.type = type;
    this.typeIsCiphered = typeIsCiphered;
    this.lengthType = lengthType;
    this.payload = payload;
  }

  public Packet(final int type, final PacketLengthType lengthType, final ByteBuf payload) {
    this(type, true, lengthType, payload);
  }

  public Packet retainedSlice() {
    return new Packet(this.type, this.typeIsCiphered, this.lengthType, this.payload.retainedSlice());
  }

  @Override
  public int refCnt() {
    return this.payload.refCnt();
  }

  @Override
  public Packet retain() {
    this.payload.retain();
    return this;
  }

  @Override
  public Packet retain(final int increment) {
    this.payload.retain(increment);
    return this;
  }

  @Override
  public Packet touch() {
    return this;
  }

  @Override
  public Packet touch(final Object hint) {
    return this;
  }

  @Override
  public boolean release() {
    return this.payload.release();
  }

  @Override
  public boolean release(final int decrement) {
    return this.payload.release(decrement);
  }

  @Override
  public int readerIndex() {
    return this.payload.readerIndex();
  }

  public int readableBytes() {
    return this.payload.readableBytes();
  }

  @SuppressWarnings("unused")
  @Override
  public void skipBytes(final int len) {
    this.payload.skipBytes(len);
  }

  @SuppressWarnings("unused")
  @Override
  public byte readByte() {
    return this.payload.readByte();
  }

  @Override
  public int readUByte() {
    return this.payload.readUnsignedByte();
  }

  @Override
  public int readUShort() {
    return this.payload.readUnsignedShort();
  }

  @Override
  public int readInt() {
    return this.payload.readInt();
  }

  @SuppressWarnings("unused")
  @Override
  public long readLong() {
    return this.payload.readLong();
  }

  @Override
  public int readVariable8_16() {
    final int nextByte = this.payload.getByte(this.payload.readerIndex());
    if ((nextByte & 0x80) == 0) {
      return this.readUByte();
    } else {
      return this.readUShort() - 0x8000;
    }
  }

  @SuppressWarnings("unused")
  @Override
  public void readBytes(final byte[] dest, final int len) {
    this.payload.readBytes(dest, 0, len);
  }

  @Override
  public @NotNull String readNullTerminatedString() {
    final int len = this.payload.bytesBefore((byte) 0);
    final byte[] bytes = new byte[len];
    this.payload.readBytes(bytes);
    this.payload.readByte();
    return Strings.decode1252String(bytes);
  }

  @SuppressWarnings("unused")
  @Override
  public @NotNull String readNullBracketedString() {
    if (this.payload.readByte() != 0) {
      throw new IllegalStateException("expected leading 0");
    }
    return this.readNullTerminatedString();
  }

  @Override
  public void writeByte(final int val) {
    this.payload.writeByte(val);
  }

  @Override
  public void writeShort(final int val) {
    this.payload.writeShort(val);
  }

  @Override
  public void writeInt(final int val) {
    this.payload.writeInt(val);
  }

  @Override
  public void writeLong(final long val) {
    this.payload.writeLong(val);
  }

  @SuppressWarnings("unused")
  @Override
  public void writeBytes(final byte[] data) {
    this.payload.writeBytes(data);
  }

  @Override
  public void writeBytes(final byte[] data, final int len) {
    this.payload.writeBytes(data, 0, len);
  }

  @Override
  public void writeVariableInt(final int val) {
    if (val >= (1 << 21)) this.payload.writeByte( (val >>> 21)         | 0x80);
    if (val >= (1 << 14)) this.payload.writeByte(((val >>> 14) & 0x7f) | 0x80);
    if (val >= (1 <<  7)) this.payload.writeByte(((val >>>  7) & 0x7f) | 0x80);
    this.payload.writeByte(val & 0x7f);
  }

  @Override
  public void writeNullTerminatedString(final String str) {
    this.payload.writeBytes(Strings.encode1252String(str));
    this.writeByte(0);
  }

  @Override
  public void writeNullBracketedString(final String str) {
    this.writeByte(0);
    this.writeNullTerminatedString(str);
  }

  @Override
  public void writeCRC(final int len) {
    final CRC32 crc = new CRC32();
    crc.update(this.payload.nioBuffer(this.payload.writerIndex() - len, len));
    this.writeInt((int) crc.getValue());
  }
}
