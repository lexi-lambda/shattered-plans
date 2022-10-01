package funorb.graphics;

public abstract class Symbol {
  public int x;
  public int y;
  public int height;
  public int width;
  public int advanceY;
  public int advanceX;

  public abstract void draw(int x, int y);

  public abstract void draw(int x, int y, int alpha);
}
