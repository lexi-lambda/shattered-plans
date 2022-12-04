package funorb.shatteredplans.client;

import funorb.shatteredplans.client.game.TutorialObjective;
import org.intellij.lang.annotations.MagicConstant;

public final class TutorialMessage {
  public String body = "";
  public int _q;
  public int _j;
  String nextId;
  public String[] _m;
  public TutorialMessage next;
  @MagicConstant(valuesFromClass = Anchor.class)
  public int anchor;
  public int _t;
  public TutorialObjective[] objectives;
  public boolean clearStack;
  boolean endThread;
  public int _b;
  public boolean noStack;
  String tag = null;
  private String[] _e;

  void a984(final String var2) {
    if (this._m == null) {
      this._m = new String[]{var2};
    } else {
      final int var3 = this._m.length;
      final String[] var4 = new String[var3 + 1];
      System.arraycopy(this._m, 0, var4, 0, var3);
      this._m = var4;
      this._m[var3] = var2;
    }
  }

  public void a627(final String var1) {
    if (this._e == null) {
      this._e = new String[]{var1};
    } else {
      final int var3 = this._e.length;
      final String[] var4 = new String[1 + var3];

      System.arraycopy(this._e, 0, var4, 0, var3);

      this._e = var4;
      this._e[var3] = var1;
    }
  }

  public void addObjective(final TutorialObjective objective) {
    if (this.objectives == null) {
      this.objectives = new TutorialObjective[]{objective};
    } else {
      final int prevLen = this.objectives.length;
      final TutorialObjective[] objectives = new TutorialObjective[prevLen + 1];
      System.arraycopy(this.objectives, 0, objectives, 0, prevLen);
      this.objectives = objectives;
      this.objectives[prevLen] = objective;
    }
  }

  public static final class Anchor {
    public static final int TOP_LEFT = 0;
    public static final int TOP_RIGHT = 1;
    public static final int BOTTOM_LEFT = 2;
    public static final int BOTTOM_RIGHT = 3;
  }
}
