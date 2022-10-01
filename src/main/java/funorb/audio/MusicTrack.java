package funorb.audio;

import funorb.cache.ResourceLoader;
import funorb.io.Buffer;

import java.util.HashMap;
import java.util.Map;

public final class MusicTrack {
  public byte[] _h;
  public Map<Integer, byte[]> _i;

  private MusicTrack(final Buffer var1) {
    var1.pos = var1.data.length - 3;
    final int var2 = var1.readUByte();
    final int var3 = var1.readUShort();
    int var4 = 14 + var2 * 10;
    var1.pos = 0;
    int var5 = 0;
    int var6 = 0;
    int var7 = 0;
    int var8 = 0;
    int var9 = 0;
    int var10 = 0;
    int var11 = 0;
    int var12 = 0;

    int var13;
    int var14;
    int var15;
    for (var13 = 0; var13 < var2; ++var13) {
      var14 = -1;

      while (true) {
        var15 = var1.readUByte();
        if (var15 != var14) {
          ++var4;
        }

        var14 = var15 & 15;
        if (var15 == 7) {
          break;
        }

        if (var15 == 23) {
          ++var5;
        } else if (var14 == 0) {
          ++var7;
        } else if (var14 == 1) {
          ++var8;
        } else if (var14 == 2) {
          ++var6;
        } else if (var14 == 3) {
          ++var9;
        } else if (var14 == 4) {
          ++var10;
        } else if (var14 == 5) {
          ++var11;
        } else {
          if (var14 != 6) {
            throw new RuntimeException();
          }

          ++var12;
        }
      }
    }

    var4 += 5 * var5;
    var4 += 2 * (var7 + var8 + var6 + var9 + var11);
    var4 += var10 + var12;
    var13 = var1.pos;
    var14 = var2 + var5 + var6 + var7 + var8 + var9 + var10 + var11 + var12;

    for (var15 = 0; var15 < var14; ++var15) {
      var1.readVariableInt();
    }

    var4 += var1.pos - var13;
    var15 = var1.pos;
    int var16 = 0;
    int var17 = 0;
    int var18 = 0;
    int var19 = 0;
    int var20 = 0;
    int var21 = 0;
    int var22 = 0;
    int var23 = 0;
    int var24 = 0;
    int var25 = 0;
    int var26 = 0;
    int var27 = 0;
    int var28 = 0;

    int var29;
    for (var29 = 0; var29 < var6; ++var29) {
      var28 = var28 + var1.readUByte() & 127;
      if (var28 == 0 || var28 == 32) {
        ++var12;
      } else if (var28 == 1) {
        ++var16;
      } else if (var28 == 33) {
        ++var17;
      } else if (var28 == 7) {
        ++var18;
      } else if (var28 == 39) {
        ++var19;
      } else if (var28 == 10) {
        ++var20;
      } else if (var28 == 42) {
        ++var21;
      } else if (var28 == 99) {
        ++var22;
      } else if (var28 == 98) {
        ++var23;
      } else if (var28 == 101) {
        ++var24;
      } else if (var28 == 100) {
        ++var25;
      } else if (var28 == 64 || var28 == 65 || var28 == 120 || var28 == 121 || var28 == 123) {
        ++var26;
      } else {
        ++var27;
      }
    }

    var29 = 0;
    int var30 = var1.pos;
    var1.pos += var26;
    int var31 = var1.pos;
    var1.pos += var11;
    int var32 = var1.pos;
    var1.pos += var10;
    int var33 = var1.pos;
    var1.pos += var9;
    int var34 = var1.pos;
    var1.pos += var16;
    int var35 = var1.pos;
    var1.pos += var18;
    int var36 = var1.pos;
    var1.pos += var20;
    int var37 = var1.pos;
    var1.pos += var7 + var8 + var11;
    int var38 = var1.pos;
    var1.pos += var7;
    int var39 = var1.pos;
    var1.pos += var27;
    int var40 = var1.pos;
    var1.pos += var8;
    int var41 = var1.pos;
    var1.pos += var17;
    int var42 = var1.pos;
    var1.pos += var19;
    int var43 = var1.pos;
    var1.pos += var21;
    int var44 = var1.pos;
    var1.pos += var12;
    int var45 = var1.pos;
    var1.pos += var9;
    int var46 = var1.pos;
    var1.pos += var22;
    int var47 = var1.pos;
    var1.pos += var23;
    int var48 = var1.pos;
    var1.pos += var24;
    int var49 = var1.pos;
    var1.pos += var25;
    int var50 = var1.pos;
    var1.pos += var5 * 3;
    this._h = new byte[var4];
    final Buffer var51 = new Buffer(this._h);
    var51.writeInt(1297377380);
    var51.writeInt(6);
    var51.writeShort(var2 > 1 ? 1 : 0);
    var51.writeShort(var2);
    var51.writeShort(var3);
    var1.pos = var13;
    int var52 = 0;
    int var53 = 0;
    int var54 = 0;
    int var55 = 0;
    int var56 = 0;
    int var57 = 0;
    int var58 = 0;
    final int[] var59 = new int[128];
    int i = 0;

    label221:
    for (int var60 = 0; var60 < var2; ++var60) {
      var51.writeInt(1297379947);
      var51.pos += 4;
      final int var61 = var51.pos;
      int var62 = -1;

      while (true) {
        while (true) {
          final int var63 = var1.readVariableInt();
          var51.a556(var63);
          final int var64 = var1.data[var29++] & 255;
          final boolean var65 = var64 != var62;
          var62 = var64 & 15;
          if (var64 == 7) {
            if (var65) {
              var51.writeByte(255);
            }

            var51.writeByte(47);
            var51.writeByte(0);
            var51.c093(var51.pos - var61);
            continue label221;
          }

          if (var64 == 23) {
            if (var65) {
              var51.writeByte(255);
            }

            var51.writeByte(81);
            var51.writeByte(3);
            var51.writeByte(var1.data[var50++]);
            var51.writeByte(var1.data[var50++]);
            var51.writeByte(var1.data[var50++]);
          } else {
            var52 ^= var64 >> 4;
            if (var62 == 0) {
              if (var65) {
                var51.writeByte(144 + var52);
              }

              var53 += var1.data[var37++];
              var54 += var1.data[var38++];
              var51.writeByte(var53 & 127);
              var51.writeByte(var54 & 127);
            } else if (var62 == 1) {
              if (var65) {
                var51.writeByte(128 + var52);
              }

              var53 += var1.data[var37++];
              var55 += var1.data[var40++];
              var51.writeByte(var53 & 127);
              var51.writeByte(var55 & 127);
            } else if (var62 == 2) {
              if (var65) {
                var51.writeByte(176 + var52);
              }

              i = i + var1.data[var15++] & 127;
              var51.writeByte(i);
              final byte var66;
              if (i == 0 || i == 32) {
                var66 = var1.data[var44++];
              } else if (i == 1) {
                var66 = var1.data[var34++];
              } else if (i == 33) {
                var66 = var1.data[var41++];
              } else if (i == 7) {
                var66 = var1.data[var35++];
              } else if (i == 39) {
                var66 = var1.data[var42++];
              } else if (i == 10) {
                var66 = var1.data[var36++];
              } else if (i == 42) {
                var66 = var1.data[var43++];
              } else if (i == 99) {
                var66 = var1.data[var46++];
              } else if (i == 98) {
                var66 = var1.data[var47++];
              } else if (i == 101) {
                var66 = var1.data[var48++];
              } else if (i == 100) {
                var66 = var1.data[var49++];
              } else if (i == 64 || i == 65 || i == 120 || i == 121 || i == 123) {
                var66 = var1.data[var30++];
              } else {
                var66 = var1.data[var39++];
              }

              final int var67 = var66 + var59[i];
              var59[i] = var67;
              var51.writeByte(var67 & 127);
            } else if (var62 == 3) {
              if (var65) {
                var51.writeByte(224 + var52);
              }

              var56 += var1.data[var45++];
              var56 += var1.data[var33++] << 7;
              var51.writeByte(var56 & 127);
              var51.writeByte(var56 >> 7 & 127);
            } else if (var62 == 4) {
              if (var65) {
                var51.writeByte(208 + var52);
              }

              var57 += var1.data[var32++];
              var51.writeByte(var57 & 127);
            } else if (var62 == 5) {
              if (var65) {
                var51.writeByte(160 + var52);
              }

              var53 += var1.data[var37++];
              var58 += var1.data[var31++];
              var51.writeByte(var53 & 127);
              var51.writeByte(var58 & 127);
            } else {
              if (var62 != 6) {
                throw new RuntimeException();
              }

              if (var65) {
                var51.writeByte(192 + var52);
              }

              var51.writeByte(var1.data[var44++]);
            }
          }
        }
      }
    }

  }

  public static MusicTrack load(final ResourceLoader loader, final String item) {
    final byte[] data = loader.getResource("", item);
    return data == null ? null : new MusicTrack(new Buffer(data));
  }

  public void a797() {
    if (this._i == null) {
      this._i = new HashMap<>();
      final int[] var1 = new int[16];
      final int[] var2 = new int[16];
      var2[9] = 128;
      var1[9] = 128;
      final pi_ var4 = new pi_(this._h);
      final int var5 = var4.c784();

      int var6;
      for (var6 = 0; var6 < var5; ++var6) {
        var4.b150(var6);
        var4.d150(var6);
        var4.e150(var6);
      }

      label53:
      do {
        while (true) {
          var6 = var4.g784();
          final int var7 = var4._b[var6];

          while (var4._b[var6] == var7) {
            var4.b150(var6);
            final int var8 = var4.a137(var6);
            if (var8 == 1) {
              var4.e797();
              var4.e150(var6);
              continue label53;
            }

            final int var9 = var8 & 240;
            int var10;
            int var11;
            int var12;
            if (var9 == 176) {
              var10 = var8 & 15;
              var11 = var8 >> 8 & 127;
              var12 = var8 >> 16 & 127;
              if (var11 == 0) {
                var1[var10] = (var1[var10] & -2080769) + (var12 << 14);
              }

              if (var11 == 32) {
                var1[var10] = (var1[var10] & -16257) + (var12 << 7);
              }
            }

            if (var9 == 192) {
              var10 = var8 & 15;
              var11 = var8 >> 8 & 127;
              var2[var10] = var1[var10] + var11;
            }

            if (var9 == 144) {
              var10 = var8 & 15;
              var11 = var8 >> 8 & 127;
              var12 = var8 >> 16 & 127;
              if (var12 > 0) {
                final int var13 = var2[var10];
                final byte[] var14 = this._i.computeIfAbsent(var13, k -> new byte[128]);
                var14[var11] = 1;
              }
            }

            var4.d150(var6);
            var4.e150(var6);
          }
        }
      } while (!var4.a801());

    }
  }

  public void b797() {
    this._i = null;
  }
}
