public class Trace
{
  double x[];
  double y[];
  int length;
  double size;
  boolean status = false;
  int layer;
  int selectedVertex = 0;
  boolean filled = false;
  int group = -1;
  int id = 0;

  Trace(double temp_size, int temp_layer, boolean temp_filled)
  {
    x = new double[64];
    y = new double[64];
    length = 0;
    size = temp_size;
    status = false;
    layer = temp_layer;
    selectedVertex = 0;
    filled = temp_filled;
    group = -1;
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
    selectedVertex = trace.selectedVertex;
    filled = trace.filled;
    group = -1;
  }

  public void add(double temp_x, double temp_y)
  {
    // dont put a new point over the last one
    if(temp_x == x[length] && temp_y == y[length])
      return;

    x[length] = temp_x;
    y[length] = temp_y;
    length++;
    if(length > 64)
      length = 64;
  }

  public void insert(int pos, double temp_x, double temp_y)
  {
    int i;

    length++;
    if(length > 64)
      length = 64;

    for(i = length - 1; i >= pos; i--)
    {
      x[i] = x[i - 1]; 
      y[i] = y[i - 1]; 
    }

    x[pos] = temp_x;
    y[pos] = temp_y;
  }

  public void delete(int pos)
  {
    int i;

    for(i = pos; i < length - 1; i++)
    {
      x[i] = x[i + 1];
      y[i] = y[i + 1];
    }

    length--;
    if(length < 2)
    {
      length = 0;
      status = false;
    }
  }
}

