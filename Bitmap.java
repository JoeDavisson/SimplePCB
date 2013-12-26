import java.awt.*;
import java.io.*;
import java.awt.image.*;
import javax.imageio.*;
//import java.util.Arrays;

public class Bitmap extends Component
{
  public int w, h;
  public int cl, cr, ct, cb;
  public int data[];
  public int row[];

  public Bitmap(int ww, int hh)
  {
    int i;

    w = ww;
    h = hh;
    cl = 0;
    ct = 0;
    cr = w - 1;
    cb = h - 1;
    data = new int[w * h];
    row = new int[h];

    for(i = 0; i < h; i++)
        row[i] = w * i;

    //Arrays.fill(data, Col.makeRGB(0, 0, 0));
  }

  public Bitmap(String s)
  {
    int i;

    BufferedImage src = null;
    try
    {
      src = ImageIO.read(getClass().getResource(s));
    }
    catch (IOException e)
    {
    }

    w = src.getWidth(null);
    h = src.getHeight(null);

    cl = 0;
    ct = 0;
    cr = w - 1;
    cb = h - 1;
    data = new int[w * h];
    row = new int[h];

    for(i = 0; i < h; i++)
      row[i] = w * i;
	
    int x, y;

    for(y = 0; y < h; y++)
      for(x = 0; x < w; x++)
        data[x + row[y]] = src.getRGB(x, y) | 0xFF000000;
  }

  public void clear(int c)
  {
    int i;

    for(i = 0; i < w * h; i++)
      data[i] = c;

    //Arrays.fill(data, c);
  }

  public void setpixel(int x, int y, int c, int t)
  {
    if(x < cl || x > cr  || y < ct || y > cb)
      return;

    data[x + row[y]] = Blend.current(data[x + row[y]], c, t);
  }

  public int getpixel(int x, int y)
  {
    if(x < cl || x > cr  || y < ct || y > cb)
      return 0;

    return data[x + row[y]];
  }

  public void hline(int x1, int y, int x2, int c, int t)
  {
    int swap;

    if(x2 < x1)
    {
      swap = x1;
      x1 = x2;
      x2 = swap;
    }
		
    if(x1 < cl)
      x1 = cl;
    if(x2 > cr)
      x2 = cr;
    if(y < ct)
      return;
    if(y > cb)
      return;

    while(x1 <= x2)
    {
      setpixel(x1, y, c, t);
      x1++;
    }
  }

  public void rect(int x1, int y1, int x2, int y2, int c, int t)
  {
    int swap;

    if(x2 < x1)
    {
      swap = x1;
      x1 = x2;
      x2 = swap;
    }

    if(y2 < y1)
    {
      swap = y1;
      y1 = y2;
      y2 = swap;
    }

    if(x1 < cl)
      x1 = cl;
    if(y1 < ct)
      y1 = ct;
    if(x2 > cr)
      x2 = cr;
    if(y2 > cb)
      y2 = cb;

    hline(x1, y1, x2, c, t);

    while(y1 < y2)
    {
      setpixel(x1, y1, c, t);
      setpixel(x2, y1, c, t);
      y1++;
    }
		
    hline(x1, y1, x2, c, t);
  }

  public void rectfill(int x1, int y1, int x2, int y2, int c, int t)
  {
    int swap;

    if(x2 < x1) {
      swap = x1;
      x1 = x2;
      x2 = swap;
    }

    if(y2 < y1) {
      swap = y1;
      y1 = y2;
      y2 = swap;
    }

    if(x1 < cl)
      x1 = cl;
    if(y1 < ct)
      y1 = ct;
    if(x2 > cr)
      x2 = cr;
    if(y2 > cb)
      y2 = cb;

    while(y1 <= y2)
    {
      hline(x1, y1, x2, c, t);
      y1++;
    }
  }

  // warning: does not clip
  public void stretchBlit(Bitmap dest,
      int xs1, int ys1, int xs2, int ys2,
      int xd1, int yd1, int xd2, int yd2)
  {
    xs2 += xs1;
    xs2--;
    ys2 += ys1;
    ys2--;
    xd2 += xd1;
    xd2--;
    yd2 += yd1;
    yd2--;

    int dx = Int.abs(yd2 - yd1);
    int dy = Int.abs(ys2 - ys1) << 1;
    int sx = Int.sign(yd2 - yd1);
    int sy = Int.sign(ys2 - ys1);
    int e = dy - dx;
    int dx2 = dx << 1;
    int d;

    for(d = 0; d <= dx; d++)
    {
      int ddx = Int.abs(xd2 - xd1);
      int ddy = Int.abs(xs2 - xs1) << 1;
      int ssx = Int.sign(xd2 - xd1);
      int ssy = Int.sign(xs2 - xs1);
      int ee = ddy - ddx;
      int ddx2 = ddx << 1;

      int p = dest.row[yd1] + xd1;
      int q = row[ys1] + xs1;
      int dd;
	
      for(dd = 0; dd <= ddx; dd++)
      {
        dest.data[p] = data[q];
        while(ee >= 0)
        {
          q += ssy;
          ee -= ddx2;
        }
        p += ssx;
        ee += ddy;
      }

      while(e >= 0)
      {
        ys1 += sy;
        e -= dx2;
      }

      yd1 += sx;
      e += dy;
    }
  }

  public void blit(Bitmap dest,
    int sx, int sy, int dx, int dy, int ww, int hh)
  {
    int x, y;

    if((sx >= w) || (sy >= h) ||
    (dx >= dest.cr) || (dy >= dest.cb))
      return;

    // clip src left
    if(sx < 0)
    {
      ww += sx;
      dx -= sx;
      sx = 0;
    }

    // clip src top
    if(sy < 0)
    {
      hh += sy;
      dy -= sy;
      sy = 0;
    }

    // clip src right
    if((sx + ww) > w)
      ww = w - sx;

    // clip src bottom
    if((sy + hh) > h)
      hh = h - sy;

    // clip dest left
    if(dx < dest.cl)
    {
      dx -= dest.cl;
      ww += dx;
      sx -= dx;
      dx = dest.cl;
    }

    // clip dest top
    if(dy < dest.ct)
    {
      dy -= dest.ct;
      hh += dy;
      sy -= dy;
      dy = dest.ct;
    }

    // clip dest right
    if((dx + ww - 1) > dest.cr)
      ww = dest.cr - dx;

    // clip dest bottom
    if((dy + hh - 1) > dest.cb)
      hh = dest.cb - dy;

    if(ww < 1 || hh < 1)
      return;

    int sy1 = sy;
    int dy1 = dy;
    for(y = 0; y < hh; y++)
    {
      int sx1 = sx + row[sy1];
      int dx1 = dx + dest.row[dy1];
      for(x = 0; x < ww; x++, sx1++, dx1++)
        dest.data[dx1] = data[sx1];
      sy1++;
      dy1++;
      // could maybe use this but its a function call
      //System.arraycopy(data, sx + row[sy + y],
      //                 dest.data, dx + dest.row[dy + y], ww);
    }
  }
}

