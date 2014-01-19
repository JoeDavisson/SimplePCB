import java.io.*;

public class Board
{
  int max = 4096;

  public double w, h;

  public Trace trace[];
  public Pad pad[];
  //public Group group[];

  //int group_count;

  Board(double temp_w, double temp_h)
  {
    w = temp_w;
    h = temp_h;

    trace = new Trace[max];
    pad = new Pad[max];
    //group = new Group[max];
    //group_count = 0;

    int i;

    for(i = 0; i < max; i++)
    {
      trace[i] = new Trace(0, 0, false);
      pad[i] = new Pad(0, 0, 0, 0, 0);
    }
  }

  public void addPad(int layer, double x, double y, double innerSize, double outerSize)
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

    pad[use].layer = layer;
    pad[use].x = x;
    pad[use].y = y;
    pad[use].innerSize = innerSize;
    pad[use].outerSize = outerSize;
    pad[use].status = true;
  }

  public Trace addTrace(int layer, double x, double y, double size, boolean filled)
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

    trace[use].layer = layer;
    trace[use].size = size;
    trace[use].add(x, y);
    trace[use].filled = filled;
    trace[use].status = true;

    return trace[use];
  }

  public void save(File file)
  {
    int i, j;

    if(file == null)
      return;

    // open file
    FileIO.open(file, true);

    // write board size
    FileIO.writeString("size");
    FileIO.writeFloat(w);
    FileIO.writeFloat(h);
    FileIO.writeString("");

    // write pads
    for(i = 0; i < max; i++)
    {
      if(pad[i].status)
      {
        FileIO.writeString("pad");
        FileIO.writeInt(pad[i].layer);
        FileIO.writeInt(pad[i].group);
        FileIO.writeFloat(pad[i].x);
        FileIO.writeFloat(pad[i].y);
        FileIO.writeFloat(pad[i].innerSize);
        FileIO.writeFloat(pad[i].outerSize);
        FileIO.writeString("");
      }
    }
  
    // write traces
    for(i = 0; i < max; i++)
    {
      if(trace[i].status)
      {
        FileIO.writeString("trace");
        FileIO.writeInt(trace[i].layer);
        FileIO.writeInt(trace[i].group);
        FileIO.writeInt(trace[i].filled ? 1 : 0);
        FileIO.writeFloat(trace[i].size);
        for(j = 0; j < trace[i].length; j++)
        {
          FileIO.writeFloat(trace[i].x[j]);
          FileIO.writeFloat(trace[i].y[j]);
        }
        FileIO.writeString("end");
        FileIO.writeString("");
      }
    }

    // close file
    FileIO.close();

    SimplePCB.panel.repaint();
  }

  public void load(File file)
  {
    int i, j;
    String s;
    int padCount = 0;
    int traceCount = 0;

    if(file == null)
      return;

    // clear
    w = 2;
    h = 2;
    for(i = 0; i < max; i++)
    {
      pad[i].status = false;
      pad[i].selected = false;
      trace[i].status = false;
      trace[i].selected = false;
      trace[i].selectedVertex = 0;
    }

    // open file
    FileIO.open(file, false);

    // read lines
    while(true)
    {
      s = FileIO.readLine();
      if(s == null)
        break;

      // board size
      if(s.equals("size"))
      {
        s = FileIO.readLine();
        if(s == null)
          break;
        w = Double.parseDouble(s);

        s = FileIO.readLine();
        if(s == null)
          break;
        h = Double.parseDouble(s);
      }

      // pad
      if(s.equals("pad"))
      {
        pad[padCount].status = true;

        s = FileIO.readLine();
        if(s == null)
          break;
        pad[padCount].layer = Integer.parseInt(s);

        s = FileIO.readLine();
        if(s == null)
          break;
        pad[padCount].group = Integer.parseInt(s);

        s = FileIO.readLine();
        if(s == null)
          break;
        pad[padCount].x = Double.parseDouble(s);

        s = FileIO.readLine();
        if(s == null)
          break;
        pad[padCount].y = Double.parseDouble(s);

        s = FileIO.readLine();
        if(s == null)
          break;
        pad[padCount].innerSize = Double.parseDouble(s);

        s = FileIO.readLine();
        if(s == null)
          break;
        pad[padCount].outerSize = Double.parseDouble(s);

        padCount++;
      }

      // trace
      if(s.equals("trace"))
      {
        trace[traceCount].status = true;
        j = 0;

        s = FileIO.readLine();
        if(s == null)
          break;
        trace[traceCount].layer = Integer.parseInt(s);

        s = FileIO.readLine();
        if(s == null)
          break;
        trace[traceCount].group = Integer.parseInt(s);

        s = FileIO.readLine();
        if(s == null)
          break;
        trace[traceCount].filled = Integer.parseInt(s) > 0 ? true : false;

        s = FileIO.readLine();
        if(s == null)
          break;
        trace[traceCount].size = Double.parseDouble(s);

        while(true)
        {
          s = FileIO.readLine();
          if(s.equals("end"))
            break;
          if(s == null)
            break;
          trace[traceCount].x[j] = Double.parseDouble(s);

          s = FileIO.readLine();
          if(s.equals("end"))
            break;
          if(s == null)
            break;
          trace[traceCount].y[j] = Double.parseDouble(s);

          j++;
        }

        trace[traceCount].length = j;
        traceCount++;
      }
    }

    // close file
    FileIO.close();

    SimplePCB.panel.repaint();
  }
}

