public class Pad
{
  double x;
  double y;
  double innerSize;
  double outerSize;
  boolean status = false;
  int group = -1;
  int id = 0;

  Pad(double temp_x, double temp_y,
      double temp_innerSize, double temp_outerSize)
  {
    x = temp_x;
    y = temp_y;
    innerSize = temp_innerSize;
    outerSize = temp_outerSize;
    status = false;
    group = -1;
  }

  public void copy(Pad pad)
  {
    x = pad.x;
    y = pad.y;
    innerSize = pad.innerSize;
    outerSize = pad.outerSize;
    status = pad.status;
    group = -1;
  }

}

