import java.awt.*;
import java.awt.geom.*;
import java.awt.event.*;
import javax.swing.*;

public class RenderPanel extends JPanel
{
  private int ox, oy;
  private int zoom;

  RenderPanel()
  {
    super();

    setSize(640, 480);
    setPreferredSize(new Dimension(640, 480));
    setLayout(new BorderLayout());
  }

  private void draw_trace(Graphics2D g, Trace trace, Color color)
  {
    g.setStroke(new BasicStroke((float)trace.size * zoom,
                                BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
    g.setColor(color);

    int i;

    for(i = 1; i < trace.length; i++)
    {
      g.draw(new Line2D.Double(ox + (trace.x[i] * zoom),
                 oy + (trace.y[i] * zoom),
                 ox + (trace.x[i - 1] * zoom),
                 oy + (trace.y[i - 1] * zoom)));
    }

    if(trace == SimplePCB.selectedTrace && trace.status)
    {
      g.setStroke(new BasicStroke((float)2.0));
      g.setColor(Color.WHITE);

      for(i = 1; i < trace.length; i++)
      {
        g.draw(new Line2D.Double(ox + (trace.x[i] * zoom),
                   oy + (trace.y[i] * zoom),
                   ox + (trace.x[i - 1] * zoom),
                   oy + (trace.y[i - 1] * zoom)));
      }

      for(i = 0; i < trace.length; i++)
      {
        if(i == trace.selectedVertex)
        {
          g.fill(new Rectangle2D.Double(ox + (trace.x[i] * zoom) - 1.5 * zoom / 100,
                     oy + (trace.y[i] * zoom) - 1.5 * zoom / 100,
                     (3 * zoom) / 100,
                     (3 * zoom) / 100));
        }
        else
        {
          g.fill(new Rectangle2D.Double(ox + (trace.x[i] * zoom) - zoom / 100,
                     oy + (trace.y[i] * zoom) - zoom / 100,
                     (2 * zoom) / 100,
                     (2 * zoom) / 100));
        }
      }
    }
  }

  private void draw_pad(Graphics2D g, Pad pad, Color color)
  {
    double xx = pad.x;
    double yy = pad.y;
    double w1 = pad.outerSize;
    double w2 = pad.innerSize;

    if(pad == SimplePCB.selectedPad)
      g.setColor(new Color(255, 255, 255));
    else
      g.setColor(color);

    g.fill(new Ellipse2D.Double(ox + ((xx - w1 / 2.0) * zoom), oy + ((yy - w1 / 2.0) * zoom), (w1 * zoom), (w1 * zoom)));
    g.setColor(new Color(0, 0, 0));
    g.fill(new Ellipse2D.Double(ox + ((xx - w2 / 2.0) * zoom), oy + ((yy - w2 / 2.0) * zoom), (w2 * zoom), (w2 * zoom)));
  }

  private void draw_board(Graphics2D g, Board board, Color color)
  {
    g.setStroke(new BasicStroke((float)2.0));
    g.setColor(color);
    g.draw(new Rectangle2D.Double(ox, oy, (board.w * zoom), (board.h * zoom)));
  }
 
  private void draw_grid(Graphics2D g, int w, int h)
  {
    int x, y;
    w = ((w + zoom) / zoom) * zoom;
    h = ((h + zoom) / zoom) * zoom;
    int fix = zoom * 100;

    // draw grid
    g.setStroke(new BasicStroke((float)1.0));
    g.setXORMode(Color.WHITE);

    g.setColor(new Color(192, 192, 192));
    for(x = 0; x < w; x += zoom / 10)
      g.draw(new Line2D.Double((x + ox + fix) % w, 0, (x + ox + fix) % w, h));
    for(y = 0; y < h; y += zoom / 10)
      g.draw(new Line2D.Double(0, (y + oy + fix) % h, w, (y + oy + fix) % h));
  }

  public void draw_segment_preview(Graphics2D g, Color color)
  {
    SimplePCB.updateMouse();

    double x = SimplePCB.x;
    double y = SimplePCB.y;
    double gridx = SimplePCB.gridx;
    double gridy = SimplePCB.gridy;

    Trace trace = SimplePCB.currentTrace;
    if(trace.status && trace.length > 0)
    {
      g.setStroke(new BasicStroke((float)(trace.size * zoom),
                                BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
      g.setColor(color);
      g.draw(new Line2D.Double(ox + (trace.x[trace.length - 1] * zoom),
                 oy + (trace.y[trace.length - 1] * zoom),
                 ox + (gridx * zoom),
                 oy + (gridy * zoom)));

      g.setStroke(new BasicStroke((float)2.0));
      g.setColor(Color.WHITE);
      g.draw(new Line2D.Double(ox + (trace.x[trace.length - 1] * zoom),
                 oy + (trace.y[trace.length - 1] * zoom),
                 ox + (gridx * zoom),
                 oy + (gridy * zoom)));
    }
  }

  public void paintComponent(Graphics g1)
  {
    super.paintComponent(g1);

    Graphics2D g = (Graphics2D)g1;
    g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
    g.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);

    Dimension d = getSize();
    int w = d.width;
    int h = d.height;

    ox = SimplePCB.ox;
    oy = SimplePCB.oy;
    zoom = SimplePCB.zoom;

    // clear
    g.setColor(Color.BLACK);
    g.fillRect(0, 0, w, h);

    // render board
    draw_board(g, SimplePCB.board, new Color(255, 255, 0));

    int i;
    Board board = SimplePCB.board;

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

    if(SimplePCB.currentTrace != null)
    {
      draw_segment_preview(g, new Color(192, 192, 192));  
    }

    // grid
    draw_grid(g, w, h);
  }
}

