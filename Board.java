public class Board
{
  int max = 4096;

  public double w, h;

  public Trace trace[];
  public Pad pad[];

  Board(double temp_w, double temp_h)
  {
    w = temp_w;
    h = temp_h;

    trace = new Trace[max];
    pad = new Pad[max];

    int i;

    for(i = 0; i < max; i++)
    {
      trace[i] = new Trace(0);
      pad[i] = new Pad(0, 0, 0, 0);
    }
  }

  public void addPad(double x, double y, double innerSize, double outerSize)
  {
    int i;
    int use = -1;

    for(i = 0; i < max; i++)
    {
      if(pad[i].status == false)
      {
        use = i;
        break;
      }
    }

    if(use == -1)
      return;

    pad[use].x = x;
    pad[use].y = y;
    pad[use].innerSize = innerSize;
    pad[use].outerSize = outerSize;
    pad[use].status = true;
  }

  public Trace addTrace(double x, double y, double size)
  {
    int i;
    int use = -1;

    for(i = 0; i < max; i++)
    {
      if(trace[i].status == false)
      {
        use = i;
        break;
      }
    }

    if(use == -1)
      return trace[0];

    trace[use].size = size;
    trace[use].add(x, y);
    trace[use].status = true;

    return trace[use];
  }
}

