public class Trace
{
  double x[];
  double y[];
  int length;
  double size;
  boolean status = false;
  int layer;

  Trace(double temp_size, int temp_layer)
  {
    x = new double[64];
    y = new double[64];
    length = 0;
    size = temp_size;
    status = false;
    layer = temp_layer;
  }

  public void copy(Trace trace)
  {
    int i;

    for(i = 0; i < 64; i++)
    {
      x[i] = trace.x[i];
      y[i] = trace.y[i];
    }

    length = trace.length;
    size = trace.size;
    status = trace.status;
    layer = trace.layer;
  }

  public void add(double temp_x, double temp_y)
  {
    x[length] = temp_x;
    y[length] = temp_y;
    length++;
  }
}

