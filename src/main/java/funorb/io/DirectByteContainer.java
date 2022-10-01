package funorb.io;

import java.nio.ByteBuffer;

public final class DirectByteContainer extends ByteContainer {
  private ByteBuffer data;

  public void put(final byte[] bs) {
    this.data = ByteBuffer.allocateDirect(bs.length);
    this.data.position(0);
    this.data.put(bs);
  }

  @Override
  public byte[] toByteArray() {
    final byte[] bs = new byte[this.data.capacity()];
    this.data.position(0);
    this.data.get(bs);
    return bs;
  }
}
