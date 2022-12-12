# The resource cache

All game resources are identified by an integer triple consisting of a page id, a group id, and an item id. A *page id* is an integer in the range [0, 254] that identifies a *resource page*, which is a group of broadly related resources. Shattered Plans uses the following resource pages:

| page id | contents                                                         |
| ------: | ---------------------------------------------------------------- |
| `0x00`  | common string constants                                          |
| `0x01`  | common graphics                                                  |
| `0x02`  | common font data                                                 |
| `0x03`  | huffman codes                                                    |
| `0x04`  | Shattered Plans string constants                                 |
| `0x05`  | Shattered Plans graphics                                         |
| `0x06`  | Shattered Plans JPEG graphics                                    |
| `0x07`  | Shattered Plans font data                                        |
| `0x08`  | Shattered Plans sound effects 1                                  |
| `0x09`  | Shattered Plans sound effects 2                                  |
| `0x0A`  | Shattered Plans music 1                                          |
| `0x0B`  | Shattered Plans music 2                                          |
| `0x0C`  | Shattered Plans extra strings (tutorial messages and star names) |
| `0x0D`  | Quick Chat data                                                  |
| `0x0E`  | Jagex logo animation data                                        |

On the server, pages are stored in a set of `res_xx_yyyy.dat` files, where `xx` is a (hexadecimal) page id and `yyyy` is a group id. Many pages only have a single group, which is usually (but not always) `0000`, but some pages are split into multiple groups.

Each page has an associated *page index*, which maps string group and item names to integer group and item ids. On disk, page indexes are stored in special `res_FF_00xx.dat` files, where `xx` is the page id. This is why there are only 254 page ids: the special `0xFF` pseudo-page id is reserved for index data.

Finally, the special `res_FF_00FF.dat` file stores the *master index*. The master index stores a table of all the page ids known to the server, and it includes a version number for the page, along with a CRC32 and a Whirlpool hash of the page index file.

## `MasterIndex` format

| type                            | description                                |
| ------------------------------- | ------------------------------------------ |
| 5 bytes                         | unknown                                    |
| `u8`                            | `pageCount`                                |
| `MasterIndexEntry[pageCount]`   |                                            |
| 1 byte                          | unknown                                    |
| `u8[64]`                        | whirlpool hash of the master index entries |

### `MasterIndexEntry` format

| type               | description                                |
| ------------------ | ------------------------------------------ |
| `u32`              | CRC32 of the page index data               |
| `u32`              | page version                               |
| `u8[64]`           | whirlpool hash of the page index data      |
