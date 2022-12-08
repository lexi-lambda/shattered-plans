package funorb.audio;

import funorb.util.BitMath;

public final class VorbisFloor1 {
  private static final float[] INVERSE_DB_TABLE = new float[]{
          1.0649863E-7F, 1.1341951E-7F, 1.2079015E-7F, 1.2863978E-7F,
          1.369995E-7F,  1.459025E-7F,  1.5538409E-7F, 1.6548181E-7F,
          1.7623574E-7F, 1.8768856E-7F, 1.998856E-7F,  2.128753E-7F,
          2.2670913E-7F, 2.4144197E-7F, 2.5713223E-7F, 2.7384212E-7F,
          2.9163792E-7F, 3.1059022E-7F, 3.307741E-7F,  3.5226967E-7F,
          3.7516213E-7F, 3.995423E-7F,  4.255068E-7F,  4.5315863E-7F,
          4.8260745E-7F, 5.1397E-7F,    5.4737063E-7F, 5.829419E-7F,
          6.208247E-7F,  6.611694E-7F,  7.041359E-7F,  7.4989464E-7F,

          7.98627E-7F,   8.505263E-7F,  9.057983E-7F,  9.646621E-7F,
          1.0273513E-6F, 1.0941144E-6F, 1.1652161E-6F, 1.2409384E-6F,
          1.3215816E-6F, 1.4074654E-6F, 1.4989305E-6F, 1.5963394E-6F,
          1.7000785E-6F, 1.8105592E-6F, 1.9282195E-6F, 2.053526E-6F,
          2.1869757E-6F, 2.3290977E-6F, 2.4804558E-6F, 2.6416496E-6F,
          2.813319E-6F,  2.9961443E-6F, 3.1908505E-6F, 3.39821E-6F,
          3.619045E-6F,  3.8542307E-6F, 4.1047006E-6F, 4.371447E-6F,
          4.6555283E-6F, 4.958071E-6F,  5.280274E-6F,  5.623416E-6F,

          5.988857E-6F,  6.3780467E-6F, 6.7925284E-6F, 7.2339453E-6F,
          7.704048E-6F,  8.2047E-6F,    8.737888E-6F,  9.305725E-6F,
          9.910464E-6F,  1.0554501E-5F, 1.1240392E-5F, 1.1970856E-5F,
          1.2748789E-5F, 1.3577278E-5F, 1.4459606E-5F, 1.5399271E-5F,
          1.6400005E-5F, 1.7465769E-5F, 1.8600793E-5F, 1.9809577E-5F,
          2.1096914E-5F, 2.2467912E-5F, 2.3928002E-5F, 2.5482977E-5F,
          2.7139005E-5F, 2.890265E-5F,  3.078091E-5F,  3.2781227E-5F,
          3.4911533E-5F, 3.718028E-5F,  3.9596467E-5F, 4.2169668E-5F,

          4.491009E-5F,  4.7828602E-5F, 5.0936775E-5F, 5.424693E-5F,
          5.7772202E-5F, 6.152657E-5F,  6.552491E-5F,  6.9783084E-5F,
          7.4317984E-5F, 7.914758E-5F,  8.429104E-5F,  8.976875E-5F,
          9.560242E-5F,  1.0181521E-4F, 1.0843174E-4F, 1.1547824E-4F,
          1.2298267E-4F, 1.3097477E-4F, 1.3948625E-4F, 1.4855085E-4F,
          1.5820454E-4F, 1.6848555E-4F, 1.7943469E-4F, 1.9109536E-4F,
          2.0351382E-4F, 2.167393E-4F,  2.3082423E-4F, 2.4582449E-4F,
          2.6179955E-4F, 2.7881275E-4F, 2.9693157E-4F, 3.1622787E-4F,

          3.3677815E-4F, 3.5866388E-4F, 3.8197188E-4F, 4.0679457E-4F,
          4.3323037E-4F, 4.613841E-4F,  4.913675E-4F,  5.2329927E-4F,
          5.573062E-4F,  5.935231E-4F,  6.320936E-4F,  6.731706E-4F,
          7.16917E-4F,   7.635063E-4F,  8.1312325E-4F, 8.6596457E-4F,
          9.2223985E-4F, 9.821722E-4F,  0.0010459992F, 0.0011139743F,
          0.0011863665F, 0.0012634633F, 0.0013455702F, 0.0014330129F,
          0.0015261382F, 0.0016253153F, 0.0017309374F, 0.0018434235F,
          0.0019632196F, 0.0020908006F, 0.0022266726F, 0.0023713743F,

          0.0025254795F, 0.0026895993F, 0.0028643848F, 0.0030505287F,
          0.003248769F,  0.0034598925F, 0.0036847359F, 0.0039241905F,
          0.0041792067F, 0.004450795F,  0.004740033F,  0.005048067F,
          0.0053761187F, 0.005725489F,  0.0060975635F, 0.0064938175F,
          0.0069158226F, 0.0073652514F, 0.007843887F,  0.008353627F,
          0.008896492F,  0.009474637F,  0.010090352F,  0.01074608F,
          0.011444421F,  0.012188144F,  0.012980198F,  0.013823725F,
          0.014722068F,  0.015678791F,  0.016697686F,  0.017782796F,

          0.018938422F,  0.020169148F, 0.021479854F, 0.022875736F,
          0.02436233F,   0.025945531F, 0.027631618F, 0.029427277F,
          0.031339627F,  0.03337625F,  0.035545226F, 0.037855156F,
          0.0403152F,    0.042935107F, 0.045725275F, 0.048696756F,
          0.05186135F,   0.05523159F,  0.05882085F,  0.062643364F,
          0.06671428F,   0.07104975F,  0.075666964F, 0.08058423F,
          0.08582105F,   0.09139818F,  0.097337745F, 0.1036633F,
          0.11039993F,   0.11757434F,  0.12521498F,  0.13335215F,

          0.14201812F,   0.15124726F,  0.16107617F,  0.1715438F,
          0.18269168F,   0.19456401F,  0.20720787F,  0.22067343F,
          0.23501402F,   0.25028655F,  0.26655158F,  0.28387362F,
          0.3023213F,    0.32196787F,  0.34289113F,  0.36517414F,
          0.3889052F,    0.41417846F,  0.44109413F,  0.4697589F,
          0.50028646F,   0.53279793F,  0.5674221F,   0.6042964F,
          0.64356697F,   0.6853896F,   0.72993004F,  0.777365F,
          0.8278826F,    0.88168305F,  0.9389798F,   1.0F};
  private static final int[] FLOOR_MULTIPLIER_LOOKUP = new int[]{256, 128, 86, 64};
  private static boolean[] step2Flag;
  private static int[] floorY;
  private static int[] floorX;
  private final int[] xList;
  private final int[] classSubclasses;
  private final int[][] subclassBooks;
  private final int[] partitionClasses;
  private final int[] classMasterbooks;
  private final int multiplier;
  private final int[] classDims;

  public VorbisFloor1() {
    final int floorType = VorbisFormat.readBits(16);
    if (floorType != 1) {
      throw new RuntimeException();
    }

    final int numPartitions = VorbisFormat.readBits(5);
    this.partitionClasses = new int[numPartitions];

    int maxClassPlusOne = 0;
    for (int i = 0; i < numPartitions; ++i) {
      int cls = VorbisFormat.readBits(4);
      this.partitionClasses[i] = cls;
      if (cls >= maxClassPlusOne) {
        maxClassPlusOne = cls + 1;
      }
    }

    this.classDims = new int[maxClassPlusOne];
    this.classSubclasses = new int[maxClassPlusOne];
    this.classMasterbooks = new int[maxClassPlusOne];
    this.subclassBooks = new int[maxClassPlusOne][];

    for (int i = 0; i < maxClassPlusOne; ++i) {
      this.classDims[i] = VorbisFormat.readBits(3) + 1;
      int subclasses = this.classSubclasses[i] = VorbisFormat.readBits(2);
      if (subclasses != 0) {
        this.classMasterbooks[i] = VorbisFormat.readBits(8);
      }

      subclasses = 1 << subclasses;
      final int[] book = new int[subclasses];
      this.subclassBooks[i] = book;

      for (int j = 0; j < subclasses; ++j) {
        book[j] = VorbisFormat.readBits(8) - 1;
      }
    }

    this.multiplier = VorbisFormat.readBits(2) + 1;
    int rangeBits = VorbisFormat.readBits(4);

    int xListSize = 2;
    for (int i = 0; i < numPartitions; ++i) {
      xListSize += this.classDims[this.partitionClasses[i]];
    }

    this.xList = new int[xListSize];
    this.xList[0] = 0;
    this.xList[1] = 1 << rangeBits;

    int values = 2;
    for (int i = 0; i < numPartitions; ++i) {
      int cls = this.partitionClasses[i];
      for (int j = 0; j < this.classDims[cls]; ++j) {
        this.xList[values++] = VorbisFormat.readBits(rangeBits);
      }
    }
    if (floorX == null || floorX.length < values) {
      floorX = new int[values];
      floorY = new int[values];
      step2Flag = new boolean[values];
    }
  }

  private static int highNeighbor(final int[] vec, final int x) {
    final int target = vec[x];
    int minIndex = -1;
    int minValue = Integer.MAX_VALUE;

    for (int i = 0; i < x; ++i) {
      final int value = vec[i];
      if (target < value && value < minValue) {
        minIndex = i;
        minValue = value;
      }
    }

    return minIndex;
  }

  private static int lowNeighbor(final int[] vec, final int x) {
    final int target = vec[x];
    int maxIndex = -1;
    int maxValue = Integer.MIN_VALUE;

    for (int i = 0; i < x; ++i) {
      final int value = vec[i];
      if (value < target && maxValue < value) {
        maxIndex = i;
        maxValue = value;
      }
    }

    return maxIndex;
  }

  public boolean decode() {
    final boolean nonzero = VorbisFormat.readBit() != 0;
    if (!nonzero) {
      return false;
    }

    for (int i = 0; i < this.xList.length; ++i) {
      floorX[i] = this.xList[i];
    }

    int range = FLOOR_MULTIPLIER_LOOKUP[this.multiplier - 1];
    final int yBits = BitMath.lastSet(range - 1);
    floorY[0] = VorbisFormat.readBits(yBits);
    floorY[1] = VorbisFormat.readBits(yBits);

    int offset = 2;
    for (final int cls : this.partitionClasses) {
      final int cDim = this.classDims[cls];
      final int cBits = this.classSubclasses[cls];
      final int cSub = (1 << cBits) - 1;
      int cVal = 0;
      if (cBits > 0) {
        cVal = VorbisFormat.codebooks[this.classMasterbooks[cls]].decodeScalar();
      }
      for (int i = 0; i < cDim; ++i) {
        final int book = this.subclassBooks[cls][cVal & cSub];
        cVal >>>= cBits;
        floorY[offset++] = book >= 0 ? VorbisFormat.codebooks[book].decodeScalar() : 0;
      }
    }

    return true;
  }

  private void sortFloor(final int a, final int b) {
    if (a >= b) {
      return;
    }

    int split = a;
    final int pivotX = floorX[a];
    final int pivotY = floorY[a];
    final boolean pivotStep2 = step2Flag[a];

    for (int i = a + 1; i <= b; ++i) {
      final int value = floorX[i];
      if (value >= pivotX) {
        continue;
      }

      floorX[split] = value;
      floorY[split] = floorY[i];
      step2Flag[split] = step2Flag[i];
      ++split;
      floorX[i] = floorX[split];
      floorY[i] = floorY[split];
      step2Flag[i] = step2Flag[split];
    }

    floorX[split] = pivotX;
    floorY[split] = pivotY;
    step2Flag[split] = pivotStep2;

    this.sortFloor(a, split - 1);
    this.sortFloor(split + 1, b);
  }

  private int renderPoint(
    final int x0,
    final int y0,
    final int x1,
    final int y1,
    final int x
  ) {
    final int dy = y1 - y0;
    final int adx = x1 - x0;
    final int ady = dy < 0 ? -dy : dy;
    final int err = ady * (x - x0);
    final int off = err / adx;
    return dy < 0 ? y0 - off : y0 + off;
  }

  private void renderLinePremultiplied(
    final int lx,
    final int ly,
    int hx,
    final int hy,
    final float[] floor,
    final int n
  ) {
    final int dy = hy - ly;
    final int adx = hx - lx;
    int ady = dy < 0 ? -dy : dy;
    final int base = dy / adx;
    int x = lx;
    int y = ly;
    int err = 0;
    final int sy = dy < 0 ? base - 1 : base + 1;

    ady -= (base < 0 ? -base : base) * adx;
    floor[lx] *= INVERSE_DB_TABLE[ly];
    if (hx > n) {
      hx = n;
    }

    for (x = lx + 1; x < hx; ++x) {
      err += ady;
      if (err >= adx) {
        err -= adx;
        y += sy;
      } else {
        y += base;
      }
      floor[x] *= INVERSE_DB_TABLE[y];
    }
  }

  public void computeCurve(final float[] floor, final int n) {
    final int numValues = this.xList.length;
    final int range = FLOOR_MULTIPLIER_LOOKUP[this.multiplier - 1];

    final boolean[] step2Flag = VorbisFloor1.step2Flag;
    step2Flag[0] = true;
    step2Flag[1] = true;

    for (int i = 2; i < numValues; ++i) {
      int lowOffset = lowNeighbor(floorX, i);
      int highOffset = highNeighbor(floorX, i);
      int predicted = this.renderPoint(
        floorX[lowOffset],
        floorY[lowOffset],
        floorX[highOffset],
        floorY[highOffset],
        floorX[i]
      );
      int val = floorY[i];
      final int highroom = range - predicted;
      final int room = (Math.min(highroom, predicted)) << 1;
      if (val != 0) {
        final boolean[] var14 = VorbisFloor1.step2Flag;
        VorbisFloor1.step2Flag[highOffset] = true;
        var14[lowOffset] = true;
        VorbisFloor1.step2Flag[i] = true;
        if (val >= room) {
          floorY[i] = highroom > predicted ? val - predicted + predicted : predicted - val + highroom - 1;
        } else {
          floorY[i] = (val & 1) != 0 ? predicted - (val + 1) / 2 : predicted + val / 2;
        }
      } else {
        VorbisFloor1.step2Flag[i] = false;
        floorY[i] = predicted;
      }
    }

    this.sortFloor(0, numValues - 1);

    int lx = 0;
    int ly = floorY[0] * this.multiplier;
    for (int i = 1; i < numValues; ++i) {
      if (!VorbisFloor1.step2Flag[i]) {
        continue;
      }
      int hx = floorX[i];
      int hy = floorY[i] * this.multiplier;
      this.renderLinePremultiplied(lx, ly, hx, hy, floor, n);
      if (hx >= n) {
        return;
      }
      lx = hx;
      ly = hy;
    }

    final float coeff = INVERSE_DB_TABLE[ly];
    for (int i = lx; i < n; ++i) {
      floor[i] *= coeff;
    }
  }
}
