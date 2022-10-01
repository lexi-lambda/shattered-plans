package funorb.cache;

import org.intellij.lang.annotations.MagicConstant;

import java.io.EOFException;
import java.io.IOException;

public final class BufferedPageCache {
  private static final int INDEX_ENTRY_SIZE = 6;
  private static final int MAX_DATA_ENTRY_SIZE = 0x200000;
  // an 8-byte header + 512 bytes of data, or a 10-byte header + 510 bytes of data for high indexes
  private static final int DATA_ENTRY_BLOCK_SIZE = 520;

  private static final byte[] buffer = new byte[DATA_ENTRY_BLOCK_SIZE];
  private final BufferedCacheFile indexFile;
  private final BufferedCacheFile dataFile;
  @MagicConstant(valuesFromClass = ResourceLoader.PageId.class)
  private final int pageId;

  public BufferedPageCache(@MagicConstant(valuesFromClass = ResourceLoader.PageId.class) final int pageId, final BufferedCacheFile dataFile, final BufferedCacheFile indexFile) {
    this.pageId = pageId;
    this.dataFile = dataFile;
    this.indexFile = indexFile;
  }

  @Override
  public String toString() {
    return "" + this.pageId;
  }

  public byte[] read(final int groupId) {
    synchronized (this.dataFile) {
      try {
        if ((groupId + 1) * (long) INDEX_ENTRY_SIZE > this.indexFile.length()) {
          return null;
        }

        this.indexFile.seek(groupId * (long) INDEX_ENTRY_SIZE);
        this.indexFile.read(buffer, 6);
        final int len = ((buffer[0] & 0xff) << 16) + ((buffer[1] & 0xff) << 8) + (buffer[2] & 0xff);
        if (len > MAX_DATA_ENTRY_SIZE) {
          return null;
        }

        int slot = ((buffer[3] & 0xff) << 16) + ((buffer[4] & 0xff) << 8) + (buffer[5] & 0xff);
        if (slot <= 0 || (long) slot > this.dataFile.length() / DATA_ENTRY_BLOCK_SIZE) {
          return null;
        }

        final byte[] data = new byte[len];
        int bytesRead = 0;
        int part = 0;
        while (bytesRead < len) {
          if (slot == 0) {
            return null;
          }

          final int bytesLeft = len - bytesRead;
          final boolean isSmallIndex = groupId <= 0xffff;
          final int blockHeaderLen = isSmallIndex ? 8 : 10;
          final int blockDataLen = Math.min(bytesLeft, DATA_ENTRY_BLOCK_SIZE - blockHeaderLen);
          final int blockLen = blockHeaderLen + blockDataLen;

          this.dataFile.seek(slot * (long) DATA_ENTRY_BLOCK_SIZE);
          this.dataFile.read(buffer, blockLen);
          final int entryIndex;
          final int entryPart;
          final int nextSlot;
          final int indexId;
          if (isSmallIndex) {
            entryIndex = ((buffer[0] & 0xff) << 8) + (buffer[1] & 0xff);
            entryPart = ((buffer[2] & 0xff) << 8) + (buffer[3] & 0xff);
            nextSlot = ((buffer[4] & 0xff) << 16) + ((buffer[5] & 0xff) << 8) + (buffer[6] & 0xff);
            indexId = buffer[7] & 0xff;
          } else {
            entryIndex = ((buffer[0] & 0xff) << 24) + ((buffer[1] & 0xff) << 16) + ((buffer[2] & 0xff) << 8) + (buffer[3] & 0xff);
            entryPart = ((buffer[4] & 0xff) << 8) + (buffer[5] & 0xff);
            nextSlot = ((buffer[6] & 0xff) << 16) + ((buffer[7] & 0xff) << 8) + (buffer[8] & 0xff);
            indexId = buffer[9] & 0xff;
          }

          //noinspection MagicConstant
          if (entryIndex != groupId || entryPart != part || this.pageId != indexId) {
            return null;
          }
          if ((long) nextSlot > this.dataFile.length() / DATA_ENTRY_BLOCK_SIZE) {
            return null;
          }

          for (int i = blockHeaderLen; i < blockLen; ++i) {
            data[bytesRead++] = buffer[i];
          }

          slot = nextSlot;
          ++part;
        }

        return data;
      } catch (final IOException e) {
        return null;
      }
    }
  }

  public void write(final int groupId, final byte[] data, final int len) {
    synchronized (this.dataFile) {
      if (len >= 0 && len <= MAX_DATA_ENTRY_SIZE) {
        final boolean var6 = this.write(groupId, data, len, true);
        if (!var6) {
          this.write(groupId, data, len, false);
        }
      } else {
        throw new IllegalArgumentException();
      }
    }
  }

  private boolean write(final int groupId, final byte[] data, final int len, boolean replaceExisting) {
    synchronized (this.dataFile) {
      try {
        int slot;
        if (replaceExisting) {
          if (this.indexFile.length() < (groupId + 1) * 6L) {
            return false;
          }

          this.indexFile.seek(groupId * (long) INDEX_ENTRY_SIZE);
          this.indexFile.read(buffer, INDEX_ENTRY_SIZE);
          slot = ((buffer[3] & 255) << 16)
               + ((buffer[4] & 255) << 8)
               +  (buffer[5] & 255);
          if (slot <= 0 || (long) slot > this.dataFile.length() / DATA_ENTRY_BLOCK_SIZE) {
            return false;
          }
        } else {
          slot = (int) ((this.dataFile.length() + (DATA_ENTRY_BLOCK_SIZE - 1L)) / DATA_ENTRY_BLOCK_SIZE);
          if (slot == 0) {
            slot = 1;
          }
        }

        buffer[0] = (byte) (len >> 16);
        buffer[1] = (byte) (len >> 8);
        buffer[2] = (byte) len;
        buffer[3] = (byte) (slot >> 16);
        buffer[4] = (byte) (slot >> 8);
        buffer[5] = (byte) slot;
        this.indexFile.seek(groupId * (long) INDEX_ENTRY_SIZE);
        this.indexFile.write(buffer, 0, 6);

        int bytesWritten = 0;
        int part = 0;
        while (len > bytesWritten) {
          int nextSlot = 0;
          if (replaceExisting) {
            final int entryIndex;
            this.dataFile.seek(slot * (long) DATA_ENTRY_BLOCK_SIZE);
            final int entryPart;
            final int indexId;
            if (groupId <= 0xffff) {
              try {
                this.dataFile.read(buffer, 8);
              } catch (final EOFException e) {
                return true;
              }

              entryIndex = ((buffer[0] & 0xff) << 8) + (buffer[1] & 0xff);
              entryPart = ((buffer[2] & 0xff) << 8) + (buffer[3] & 0xff);
              nextSlot = ((buffer[4] & 0xff) << 16) + ((buffer[5] & 0xff) << 8) + (buffer[6] & 0xff);
              indexId = buffer[7] & 0xff;
            } else {
              try {
                this.dataFile.read(buffer, 10);
              } catch (final EOFException e) {
                return true;
              }

              entryIndex = ((buffer[0] & 0xff) << 24) + ((buffer[1] & 0xff) << 16) + ((buffer[2] & 0xff) << 8) + (buffer[3] & 0xff);
              entryPart = ((buffer[4] & 0xff) << 8) + (buffer[5] & 0xff);
              nextSlot = ((buffer[6] & 0xff) << 16) + ((buffer[7] & 0xff) << 8) + (buffer[8] & 0xff);
              indexId = buffer[9] & 0xff;
            }

            //noinspection MagicConstant
            if (entryIndex != groupId || entryPart != part || indexId != this.pageId) {
              return false;
            }

            if ((long) nextSlot > this.dataFile.length() / DATA_ENTRY_BLOCK_SIZE) {
              return false;
            }
          }

          if (nextSlot == 0) {
            replaceExisting = false;
            nextSlot = (int) ((this.dataFile.length() + (DATA_ENTRY_BLOCK_SIZE - 1L)) / DATA_ENTRY_BLOCK_SIZE);
            if (nextSlot == 0) {
              ++nextSlot;
            }

            if (nextSlot == slot) {
              ++nextSlot;
            }
          }

          if (len - bytesWritten <= 512) {
            nextSlot = 0; // no next part, so no next slot
          }

          final int entryLen;
          if (groupId <= 0xffff) {
            buffer[0] = (byte) (groupId >> 8);
            buffer[1] = (byte) groupId;
            buffer[2] = (byte) (part >> 8);
            buffer[3] = (byte) part;
            buffer[4] = (byte) (nextSlot >> 16);
            buffer[5] = (byte) (nextSlot >> 8);
            buffer[6] = (byte) nextSlot;
            buffer[7] = (byte) this.pageId;
            this.dataFile.seek(slot * (long) DATA_ENTRY_BLOCK_SIZE);
            this.dataFile.write(buffer, 0, 8);
            entryLen = Math.min(len - bytesWritten, 512);
          } else {
            buffer[0] = (byte) (groupId >> 24);
            buffer[1] = (byte) (groupId >> 16);
            buffer[2] = (byte) (groupId >> 8);
            buffer[3] = (byte) groupId;
            buffer[4] = (byte) (part >> 8);
            buffer[5] = (byte) part;
            buffer[6] = (byte) (nextSlot >> 16);
            buffer[7] = (byte) (nextSlot >> 8);
            buffer[8] = (byte) nextSlot;
            buffer[9] = (byte) this.pageId;
            this.dataFile.seek(slot * (long) DATA_ENTRY_BLOCK_SIZE);
            this.dataFile.write(buffer, 0, 10);
            entryLen = Math.min(len - bytesWritten, 510);
          }
          this.dataFile.write(data, bytesWritten, entryLen);
          bytesWritten += entryLen;

          ++part;
          slot = nextSlot;
        }
        return true;
      } catch (final IOException var18) {
        return false;
      }
    }
  }
}
