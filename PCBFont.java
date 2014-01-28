import java.io.*;
import java.util.*;

public class PCBFont
{
  static Letter letter[];

  PCBFont()
  {
    String s, temp;
    int i, j;
    int x1, y1, x2, y2;
    int c = 0;
    int index = 0;

    letter = new Letter[256];
    for(i = 0; i < 256; i++)
      letter[i] = new Letter();

    FileIO.open(new File("default_font"), false);

    while(true)
    {
      s = FileIO.readLine();
      if(s == null)
        break;


      if(s.contains("Symbol("))
      {
        c = s.charAt(s.indexOf("'") + 1);

        StringTokenizer token = new StringTokenizer(s, " \t");
        if(token.hasMoreTokens())
          temp = token.nextToken();
        if(token.hasMoreTokens())
        {
          temp = token.nextToken().replace(")", "");
          letter[c].spacing = Integer.parseInt(temp);
          index = 0;
        }
      }

      if(s.contains("SymbolLine("))
      {
        StringTokenizer token = new StringTokenizer(s, " ()\t");
        if(token.hasMoreTokens())
          temp = token.nextToken();
        if(token.hasMoreTokens())
          letter[c].x1[index] = Integer.parseInt(token.nextToken());
        if(token.hasMoreTokens())
          letter[c].y1[index] = Integer.parseInt(token.nextToken());
        if(token.hasMoreTokens())
          letter[c].x2[index] = Integer.parseInt(token.nextToken());
        if(token.hasMoreTokens())
          letter[c].y2[index] = Integer.parseInt(token.nextToken());
        if(token.hasMoreTokens())
          letter[c].size[index] = Integer.parseInt(token.nextToken());
        index++;
        letter[c].length = index;
      }
    }

    FileIO.close();

    letter[' '].spacing = 18;

    /*
    for(i = 0; i < 256; i++)
    {
      if(letter[i].spacing > 0)
      {
        System.out.println(String.format("%c, %d", i, letter[i].spacing));
        for(j = 0; j < letter[i].length; j++)
        {
          System.out.println(String.format("%d, %d, %d, %d, %d",
            letter[i].x1[j], letter[i].y1[j], letter[i].x2[j], letter[i].y2[j],
            letter[i].size[j]));
        }
      }
    }
    */
  }
}

