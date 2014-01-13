public class Grid
{
  public static double div;
  public static double inc;
  public static int mag = 10;
  public static int line = 4;
  public static boolean inches = false;

  public static void setInches(double spacing)
  {
    inc = spacing * 25.4;
    div = 1.0 / inc;
    inches = true;
  }

  public static void setMetric(double spacing)
  {
    inc = spacing;
    div = 1.0 / inc;
    inches = false;
  }

  public static double getInches()
  {
    return inc / 25.4;
  }

  public static double getMetric()
  {
    return inc;
  }

  public static double snap(double coord)
  {
    return (double)((int)((coord + (inc / 2)) * div)) / div;
  }
}
