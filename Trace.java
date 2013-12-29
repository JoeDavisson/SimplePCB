public class Trace
{
  double x[];
  double y[];
  int length;
  double size;
  boolean status = false;

  Trace(double temp_size)
  {
    x = new double[64];
    y = new double[64];
    length = 0;
    size = temp_size;
    status = false;
  }

  public void add(double temp_x, double temp_y)
  {
    x[length] = temp_x;
    y[length] = temp_y;
    length++;
    
  }
}

