package funorb.shatteredplans.server;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.stream.ChunkedInput;

import java.io.InputStream;

public final class ResourceStreamInput implements ChunkedInput<ByteBuf> {
  private static final int PART_SIZE = 512;

  private final boolean priority;
  private final int group;
  private final int index;
  private final InputStream in;

  private boolean isEOF = false;
  private int partsWritten = 0;

  public ResourceStreamInput(final boolean priority, final int group, final int index, final InputStream in) {
    this.group = group;
    this.index = index;
    this.priority = priority;
    this.in = in;
  }

  @Override
  public boolean isEndOfInput() {
    return this.isEOF;
  }

  @Override
  public void close() throws Exception {
    this.in.close();
  }

  @Override
  public ByteBuf readChunk(final ChannelHandlerContext ctx) throws Exception {
    return this.readChunk(ctx.alloc());
  }

  @Override
  public ByteBuf readChunk(final ByteBufAllocator allocator) throws Exception {
    if (this.isEOF) return null;

    final ByteBuf buffer = allocator.buffer(PART_SIZE);
    boolean release = true;
    try {
      if (this.partsWritten == 0) {
        buffer.writeByte(this.group);
        buffer.writeInt(this.index);
        if (!this.priority) {
          buffer.writeByte(this.in.read() | 0x80);
        }
      } else {
        buffer.writeByte(0xff);
      }
      while (buffer.readableBytes() < PART_SIZE) {
        if (buffer.writeBytes(this.in, PART_SIZE - buffer.readableBytes()) < 0) {
          this.isEOF = true;
          if (buffer.readableBytes() == 1) {
            return null;
          } else {
            break;
          }
        }
      }
      this.partsWritten++;
      release = false;
      return buffer;
    } finally {
      if (release) {
        buffer.release();
      }
    }
  }

  @Override
  public long length() {
    return -1;
  }

  @Override
  public long progress() {
    return this.partsWritten;
  }
}
