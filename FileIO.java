import java.io.*;

public class FileIO
{
  private static FileWriter fw;
  private static FileReader fr;
  private static BufferedWriter bw;
  private static BufferedReader br;

  public static void writeString(String s)
  {
    //System.out.println(s);

    try
    {
      bw.write(s + "\n");
    }
    catch(IOException e)
    {
    }
  }

  public static void writeFloat(double num)
  {
    try
    {
      bw.write(String.format("%.6f\n", num));
    }
    catch(IOException e)
    {
    }
  }

  public static void writeInt(int num)
  {
    try
    {
      bw.write(String.format("%d\n", num));
    }
    catch(IOException e)
    {
    }
  }

  public static String readLine()
  {
    try
    {
      return br.readLine();
    }
    catch(IOException e)
    {
    }

    return null;
  }

  public static void open(File file, boolean write)
  {
    try
    {
      if(write)
      {
        fw = new FileWriter(file.getAbsoluteFile());
        bw = new BufferedWriter(fw);
      }
      else
      {
        fr = new FileReader(file.getAbsoluteFile());
        br = new BufferedReader(fr);
      }
    }
    catch(IOException e)
    {
    }
  }

  public static void close()
  {
    try
    {
      bw.close();
    }
    catch(IOException e)
    {
    }
  }
}
