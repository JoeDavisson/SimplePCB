public class Group
{
  public Trace trace[];
  public Pad pad[];
  public int traceCount;
  public int padCount;
  public Group parent;

  Group()
  {
    traceCount = 0;
    padCount = 0;
    parent = null;
  }

  public void addTrace(Trace addedTrace)
  {
    if(traceCount < 256)
      trace[traceCount++] = addedTrace;
  }

  public void addPad(Pad addedPad)
  {
    if(padCount < 256)
      pad[padCount++] = addedPad;
  }
}

