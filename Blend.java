public class Blend
{
  public static final int NORMAL = 0;
  public static final int INVERT = 1;
  public static final int LIGHTEN = 2;
  public static final int DARKEN = 3;
  public static final int COLORIZE = 4;

  private static int current_mode = NORMAL;

  public static void setMode(int mode)
  {
    current_mode = mode;
  }

  public static int normal(int c1, int c2, int t)
  {
    int r = ((Col.getr(c1) * t) + (Col.getr(c2) * (255 - t))) / 255;
    int g = ((Col.getg(c1) * t) + (Col.getg(c2) * (255 - t))) / 255;
    int b = ((Col.getb(c1) * t) + (Col.getb(c2) * (255 - t))) / 255;

    return Col.makeRGB(r, g, b);
  }

  public static int invert(int c1, int c2, int t)
  {
    return Col.makeRGB(255 - Col.getr(c1),
      255 - Col.getg(c1), 255 - Col.getb(c1));
  }

  public static int current(int c1, int c2, int t)
  {
    switch(current_mode)
    {
      case NORMAL:
        return normal(c1, c2, t);
      case INVERT:
        return invert(c1, c2, t);
      case LIGHTEN:
        break;
      case DARKEN:
        break;
      case COLORIZE:
        break;
    }

    return 0;
  }
}

