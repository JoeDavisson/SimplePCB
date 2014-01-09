import java.io.*;

public class Gerber
{
  private static FileWriter fw;
  private static BufferedWriter bw;

  private static void print(String s)
  {
    System.out.println(s); 

    try
    {
      bw.write(s + "\n");
    }
    catch(IOException e)
    {
    }
  }

  public static void save(Board board, File file, int layer)
  {
    int i, j;

    // open file
    try
    {
      fw = new FileWriter(file.getAbsoluteFile());
      bw = new BufferedWriter(fw);
    }
    catch(IOException e)
    {
    }

    // header
    if(layer == 2)
      print("G04 Bottom Copper Layer*");
    if(layer == 1)
      print("G04 Top Copper Layer*");
    if(layer == 0)
      print("G04 Artwork Layer*");
    print("%FSLAX26Y26*%");
    print("%MOIN*%");
    print("%IPPOS*%");
    print("");

    int pad_id = 0;
    int trace_id = 0;
    String padString[] = new String[board.max];
    String traceString[] = new String[board.max];

    for(i = 0; i < board.max; i++)
    {
      padString[i] = "";
      traceString[i] = "";
    }

    // assign pad apertures, no pads on artwork layer
    if(layer != 0)
    {
      for(i = 0; i < board.max; i++)
      {
        Pad pad = board.pad[i];

        if(pad.status && layer != 0)
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
    print("G04 Pad Aperture List*");
    for(i = 0; i < pad_id; i++)
      print("%ADD" + (100 + i) + "C," + padString[i] + "*%");
    print("");

    print("G04 Trace Aperture List*");
    for(i = 0; i < trace_id; i++)
      print("%ADD" + (200 + i) + "C," + traceString[i] + "*%");
    print("");

    // draw traces
    for(i = 0; i < board.max; i++)
    {
      Trace trace = board.trace[i];

      if(trace.status && trace.layer == layer)
      {
        if(trace.filled)
        {
          print("G36*");
          print(String.format("X%2.6fY%2.6fD02*", trace.x[0], board.h - trace.y[0]).replace(".", ""));
          for(j = 1; j < trace.length; j++)
          {
            print(String.format("G1X%2.6fY%2.6fD01*", trace.x[j], board.h - trace.y[j]).replace(".", ""));
          }
          print("G37*");
        }
        else
        {
          print("D" + (200 + trace.id) + "*");
          print(String.format("X%2.6fY%2.6fD02*", trace.x[0], board.h - trace.y[0]).replace(".", ""));
          for(j = 1; j < trace.length; j++)
          {
            print(String.format("X%2.6fY%2.6fD01*", trace.x[j], board.h - trace.y[j]).replace(".", ""));
          }
        }
      }
    }

    // draw pads
    for(i = 0; i < board.max; i++)
    {
      Pad pad = board.pad[i];

      if(pad.status && layer != 0)
      {
        print("D" + (100 + pad.id) + "*");
        print(String.format("X%2.6fY%2.6fD03*", pad.x, board.h - pad.y).replace(".", ""));
      }
    }

    // footer
    print("G04 End Program*");
    print("M02*");

    // close file
    try
    {
      bw.close();
    }
    catch(IOException e)
    {
    }
  }
}

