package funorb.client.lobby;

import funorb.Strings;
import funorb.io.Buffer;

import java.util.Arrays;

public final class QuickChatCategory {
  public boolean _y = false;
  public String name;
  public char[] _s;
  public char[] _p;
  public int[] _x;
  public int[] _r;

  public void markHigh() {
    if (this._x != null) {
      Arrays.setAll(this._x, i -> this._x[i] | 0x8000);
    }
    if (this._r != null) {
      Arrays.setAll(this._r, i -> this._r[i] | 0x8000);
    }
  }

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
      this.name = buffer.readNullTerminatedString();
    } else if (type == 2) {
      final int len = buffer.readUByte();
      this._p = new char[len];
      this._r = new int[len];

      for (int i = 0; i < len; ++i) {
        this._r[i] = buffer.readUShort();
        final byte var6 = buffer.readByte();
        this._p[i] = var6 == 0 ? 0 : Strings.decode1252Char(var6);
      }
    } else if (type == 3) {
      final int var4 = buffer.readUByte();
      this._s = new char[var4];
      this._x = new int[var4];

      for (int i = 0; i < var4; ++i) {
        this._x[i] = buffer.readUShort();
        final byte var6 = buffer.readByte();
        this._s[i] = var6 == 0 ? 0 : Strings.decode1252Char(var6);
      }
    } else if (type == 4) {
      this._y = true;
    }
  }
}
