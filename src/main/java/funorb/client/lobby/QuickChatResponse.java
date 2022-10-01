package funorb.client.lobby;

import funorb.io.Buffer;

import java.util.Arrays;
import java.util.stream.Collectors;

public final class QuickChatResponse {
  private static final int[] SHORTS_TO_SKIP = {1, 0, 0, 0, 1, 0, 2, 1, 1, 1, 0, 2, 0, 0, 1, 0};

  public int[] ids;
  private String[] strings;

  public void load(final Buffer buffer) {
    while (true) {
      final int type = buffer.readUByte();
      if (type == 0) {
        return;
      }

      this.loadEntry(buffer, type);
    }
  }

  private void loadEntry(final Buffer buffer, final int type) {
    if (type == 1) {
      this.strings = buffer.readNullTerminatedString().split("<");
    } else if (type == 2) {
      final int len = buffer.readUByte();
      this.ids = new int[len];
      for (int i = 0; i < len; ++i) {
        this.ids[i] = buffer.readUShort();
      }
    } else if (type == 3) {
      final int len = buffer.readUByte();
      for (int i = 0; i < len; ++i) {
        final int shortsToSkip = SHORTS_TO_SKIP[buffer.readUShort()];
        for (int j = 0; j < shortsToSkip; ++j) {
          buffer.readUShort();
        }
      }
    }
  }

  public void markHigh() {
    if (this.ids != null) {
      Arrays.setAll(this.ids, i -> this.ids[i] | 0x8000);
    }
  }

  public String joinStrings() {
    if (this.strings == null) {
      return "";
    } else {
      return Arrays.stream(this.strings, 1, this.strings.length)
          .collect(Collectors.joining("...", this.strings[0], ""));
    }
  }
}
