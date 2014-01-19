import java.io.*;

public class Gerber
{
  public static void save(Board board, File file, int layer)
  {
    int i, j;

    if(file == null)
      return;

    // open file
    FileIO.open(file, true);

    // header
    if(layer == 2)
      FileIO.writeString("G04 Bottom Copper Layer*");
    if(layer == 1)
      FileIO.writeString("G04 Top Copper Layer*");
    if(layer == 0)
      FileIO.writeString("G04 Artwork Layer*");
    FileIO.writeString("%FSLAX26Y26*%");
    FileIO.writeString("%MOIN*%");
    FileIO.writeString("%IPPOS*%");
    FileIO.writeString("");

    int pad_id = 0;
    int trace_id = 0;
    String padString[] = new String[board.max];
    String traceString[] = new String[board.max];

    for(i = 0; i < board.max; i++)
    {
      padString[i] = "";
      traceString[i] = "";
    }

    // assign pad apertures
    for(i = 0; i < board.max; i++)
    {
      Pad pad = board.pad[i];

      if(pad.status && pad.layer == layer)
      {
        String temp = String.format("%2.6fX%2.6f", pad.outerSize, pad.innerSize);
        int use = -1;
        for(j = 0; j < pad_id; j++)
        {
          if(padString[j].equals(temp))
          {
            use = j;
            pad.id = use;
            break;
          } 
        }

        if(use == -1)
        {
          padString[pad_id] = temp;
          pad.id = pad_id;
          pad_id++;
        }
      }
    }

    // assign trace apertures
    for(i = 0; i < board.max; i++)
    {
      Trace trace = board.trace[i];

      if(trace.status && trace.layer == layer)
      {
        String temp = String.format("%2.6f", trace.size);
        int use = -1;
        for(j = 0; j < trace_id; j++)
        {
          if(traceString[j].equals(temp))
          {
            use = j;
            trace.id = use;
            break;
          } 
        }

        if(use == -1)
        {
          traceString[trace_id] = temp;
          trace.id = trace_id;
          trace_id++;
        }
      }
    }

    // print aperture pad list
    FileIO.writeString("G04 Pad Aperture List*");
    for(i = 0; i < pad_id; i++)
      FileIO.writeString("%ADD" + (100 + i) + "C," + padString[i] + "*%");
    FileIO.writeString("");

    FileIO.writeString("G04 Trace Aperture List*");
    for(i = 0; i < trace_id; i++)
      FileIO.writeString("%ADD" + (200 + i) + "C," + traceString[i] + "*%");
    FileIO.writeString("");

    // draw traces
    for(i = 0; i < board.max; i++)
    {
      Trace trace = board.trace[i];

      if(trace.status && trace.layer == layer)
      {
        if(trace.filled)
        {
          FileIO.writeString("G36*");
          FileIO.writeString(String.format("X%2.6fY%2.6fD02*", trace.x[0], board.h - trace.y[0]).replace(".", ""));
          for(j = 1; j < trace.length; j++)
          {
            FileIO.writeString(String.format("G1X%2.6fY%2.6fD01*", trace.x[j], board.h - trace.y[j]).replace(".", ""));
          }
          FileIO.writeString("G37*");
        }
        else
        {
          FileIO.writeString("D" + (200 + trace.id) + "*");
          FileIO.writeString(String.format("X%2.6fY%2.6fD02*", trace.x[0], board.h - trace.y[0]).replace(".", ""));
          for(j = 1; j < trace.length; j++)
          {
            FileIO.writeString(String.format("X%2.6fY%2.6fD01*", trace.x[j], board.h - trace.y[j]).replace(".", ""));
          }
        }
      }
    }

    // draw pads
    for(i = 0; i < board.max; i++)
    {
      Pad pad = board.pad[i];

      if(pad.status && pad.layer == layer)
      {
        FileIO.writeString("D" + (100 + pad.id) + "*");
        FileIO.writeString(String.format("X%2.6fY%2.6fD03*", pad.x, board.h - pad.y).replace(".", ""));
      }
    }

    // footer
    FileIO.writeString("G04 End Program*");
    FileIO.writeString("M02*");

    // close file
    FileIO.close();
  }
}

