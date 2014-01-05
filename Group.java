public class Group
{
  Trace trace[];
  Pad pad[];

  int trace_count;
  int pad_count;
  
  Group()
  {
    int i;

    trace = new Trace[256];
    pad = new Pad[256];
  }

  public void addTrace(Trace t)
  {
    trace[trace_count] = t;
    trace_count++;

    if(trace_count > 255)
      trace_count = 255;
  }

  public void addPad(Pad p)
  {
    pad[pad_count] = p;
    pad_count++;

    if(pad_count > 255)
      pad_count = 255;
  }
}
