import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class RenderPanel extends JPanel
{
  SimplePCB simplepcb;

  RenderPanel(SimplePCB s)
  {
    super();

    simplepcb = s;

    setSize(640, 480);
    setPreferredSize(new Dimension(640, 480));
    setLayout(new BorderLayout());
  }

  private void draw_trace(Graphics2D g, Trace trace, Color color)
  {
    int zoom = simplepcb.zoom;
    int ox = simplepcb.offsetx;
    int oy = simplepcb.offsety;

    g.setStroke(new BasicStroke((int)(trace.size * zoom),
                                BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
    g.setColor(color);

    int i;

    for(i = 1; i < trace.length; i++)
    {
      g.drawLine(ox + (int)(trace.x[i] * zoom),
                 oy + (int)(trace.y[i] * zoom),
                 ox + (int)(trace.x[i - 1] * zoom),
                 oy + (int)(trace.y[i - 1] * zoom));
    }

    if(trace == simplepcb.selectedTrace && trace.status)
    {
      g.setColor(Color.WHITE);
      for(i = 0; i < trace.length; i++)
      {
        if(i == trace.selectedVertex)
        {
          g.fillRect(ox + (int)(trace.x[i] * zoom) - 2 * zoom / 100,
                     oy + (int)(trace.y[i] * zoom) - 2 * zoom / 100,
                     (4 * zoom) / 100,
                     (4 * zoom) / 100);
        }
        else
        {
          g.fillRect(ox + (int)(trace.x[i] * zoom) - zoom / 100,
                     oy + (int)(trace.y[i] * zoom) - zoom / 100,
                     (2 * zoom) / 100,
                     (2 * zoom) / 100);
        }
      }
    }
  }

  private void draw_pad(Graphics2D g, Pad pad, Color color)
  {
    int zoom = simplepcb.zoom;
    int ox = simplepcb.offsetx;
    int oy = simplepcb.offsety;

    double xx = pad.x;
    double yy = pad.y;
    double r1 = pad.outerSize / 2;
    double r2 = pad.innerSize / 2;

    if(pad == simplepcb.selectedPad)
      g.setColor(new Color(255, 255, 255));
    else
      g.setColor(color);

    g.fillOval(ox + (int)((xx - r1) * zoom), oy + (int)((yy - r1) * zoom), (int)(r1 * 2 * zoom), (int)(r1 * 2 * zoom));
    g.setColor(new Color(0, 0, 0));
    g.fillOval(ox + (int)((xx - r2) * zoom), oy + (int)((yy - r2) * zoom), (int)(r2 * 2 * zoom), (int)(r2 * 2 * zoom));
  }

  private void draw_board(Graphics2D g, Board board, Color color)
  {
    int zoom = simplepcb.zoom;
    int ox = simplepcb.offsetx;
    int oy = simplepcb.offsety;

    g.setStroke(new BasicStroke(2));
    g.setColor(color);
    g.drawRect(ox, oy, (int)(board.w * zoom), (int)(board.h * zoom));
  }
 
  private void draw_grid(Graphics2D g, int w, int h, Color color)
  {
    int zoom = simplepcb.zoom;
    int ox = simplepcb.offsetx;
    int oy = simplepcb.offsety;
    w = ((w + zoom) / zoom) * zoom;
    h = ((h + zoom) / zoom) * zoom;
    int fix = zoom * 100;

    // draw grid
    g.setStroke(new BasicStroke(1));
    g.setColor(color);

    int x, y;

    for(x = 0; x < w; x += zoom / 10)
      g.drawLine((x + ox + fix) % w, 0, (x + ox + fix) % w, h);
    for(y = 0; y < h; y += zoom / 10)
      g.drawLine(0, (y + oy + fix) % h, w, (y + oy + fix) % h);
  }

  public void draw_segment_preview(Graphics2D g, Color color)
  {
    int zoom = simplepcb.zoom;
    int ox = simplepcb.offsetx;
    int oy = simplepcb.offsety;
    double x = (double)(simplepcb.input.mousex - ox) / zoom;
    double y = (double)(simplepcb.input.mousey - oy) / zoom;
    x = (float)((int)((x + .025) * 20)) / 20;
    y = (float)((int)((y + .025) * 20)) / 20;

    Trace trace = simplepcb.currentTrace;
    if(trace.status && trace.length > 0)
    {

      g.setStroke(new BasicStroke((int)(trace.size * zoom),
                                BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
      g.setColor(color);

      g.drawLine(ox + (int)(trace.x[trace.length - 1] * zoom),
                 oy + (int)(trace.y[trace.length - 1] * zoom),
                 ox + (int)(x * zoom),
                 oy + (int)(y * zoom));
    }
  }

  public void paintComponent(Graphics g1)
  {
    super.paintComponent(g1);

    Graphics2D g = (Graphics2D)g1;
    g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

    Dimension d = getSize();
    int w = d.width;
    int h = d.height;

    // clear
    g.setColor(Color.BLACK);
    g.fillRect(0, 0, w, h);

    draw_grid(g, w, h, new Color(64, 64, 64));

    // render board
    draw_board(g, simplepcb.board, new Color(255, 255, 0));

    int i;
    Board board = simplepcb.board;

    // green traces
    for(i = 0; i < board.max; i++)
    {
      if(board.trace[i].status && board.trace[i].layer == 2)
        draw_trace(g, board.trace[i], new Color(0, 255, 0));
    }

    // green pads
    for(i = 0; i < board.max; i++)
    {
      if(board.pad[i].status)
        draw_pad(g, board.pad[i], new Color(0, 255, 0));
    } 

    // red traces
    for(i = 0; i < board.max; i++)
    {
      if(board.trace[i].status && board.trace[i].layer == 1)
        draw_trace(g, board.trace[i], new Color(255, 0, 0));
    }

    // yellow traces
    for(i = 0; i < board.max; i++)
    {
      if(board.trace[i].status && board.trace[i].layer == 0)
        draw_trace(g, board.trace[i], new Color(255, 255, 0));
    }

    if(simplepcb.currentTrace != null)
    {
      draw_segment_preview(g, new Color(128, 255, 128));  
    }
  }
}

