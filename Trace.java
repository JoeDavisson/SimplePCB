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
  boolean selected = false;
  int id = 0;

  Trace(int temp_layer, double temp_size, boolean temp_filled)
  {
    x = new double[256];
    y = new double[256];
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

    for(i = 0; i < 256; i++)
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
    //if(temp_x == x[length] && temp_y == y[length])
    //  return;

    x[length] = temp_x;
    y[length] = temp_y;
    if(length < 256)
      length++;
  }

  public void insert(int pos, double temp_x, double temp_y)
  {
    int i;

    if(length < 256)
      length++;

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

  public boolean inbox(double x1, double y1, double x2, double y2)
  {
    int i;

    int count = 0;

    if(filled)
    {
      for(i = 0; i < length; i++)
      {
        if(x[i] > x1 && x[i] < x2 && y[i] > y1 && y[i] < y2)
        {
          count++;
        }
      }
    }
    else
    {
      for(i = 0; i < length; i++)
      {
        if( (x[i] - size / 2) > x1 && (x[i] + size / 2) < x2
         && (y[i] - size / 2) > y1 && (y[i] + size / 2) < y2)
        {
          count++;
        }
      }
    }

    if(count == length)
      return true;
    else
      return false;
  }
}

