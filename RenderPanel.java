import java.awt.*;
import java.awt.geom.*;
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

  private void drawTrace(Graphics2D g, Trace trace, Color color)
  {
    int i;

    // dont draw polyon if being created
    if(trace == SimplePCB.currentTrace && trace.filled)
      return;

    g.setColor(color);

    if(trace.filled)
    {
      // polygon
      Path2D.Double path = new Path2D.Double(Path2D.WIND_EVEN_ODD, trace.length);
      path.moveTo(ox + trace.x[0] * zoom, oy + trace.y[0] * zoom);

      for(i = 0; i < trace.length; i++)
      {
        path.lineTo(ox + trace.x[i] * zoom, oy + trace.y[i] * zoom);
      }

      path.closePath();
      g.fill(path);
    }
    else
    {
      // trace
      g.setStroke(new BasicStroke((float)trace.size * zoom,
                                BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
      for(i = 1; i < trace.length; i++)
      {
        g.draw(new Line2D.Double(ox + (trace.x[i] * zoom),
                   oy + (trace.y[i] * zoom),
                   ox + (trace.x[i - 1] * zoom),
                   oy + (trace.y[i - 1] * zoom)));
      }
    }
  }

  private void highlightTrace(Graphics2D g, Trace trace)
  {
    if(trace.selected/* || trace.group == SimplePCB.currentGroup*/)
    {
      g.setStroke(new BasicStroke((float)2.0));
      g.setColor(Color.WHITE);

      int i;

      // line
      for(i = 1; i < trace.length; i++)
      {
        g.draw(new Line2D.Double(ox + (trace.x[i] * zoom),
                   oy + (trace.y[i] * zoom),
                   ox + (trace.x[i - 1] * zoom),
                   oy + (trace.y[i - 1] * zoom)));
      }

      // if polygon draw line back to start
      if(trace.filled)
      {
        g.draw(new Line2D.Double(ox + (trace.x[0] * zoom),
                   oy + (trace.y[0] * zoom),
                   ox + (trace.x[trace.length - 1] * zoom),
                   oy + (trace.y[trace.length - 1] * zoom)));
      }

      // handles
      if(SimplePCB.toolMode == 1)
      {
        for(i = 0; i < trace.length; i++)
        {
          if(i == trace.selectedVertex)
          {
            g.fill(new Rectangle2D.Double(ox + (trace.x[i] * zoom) - 3 * zoom / Grid.mag,
                       oy + (trace.y[i] * zoom) - 3 * zoom / Grid.mag,
                       (6 * zoom) / Grid.mag,
                       (6 * zoom) / Grid.mag));
          }
          else
          {
            g.fill(new Rectangle2D.Double(ox + (trace.x[i] * zoom) - 2 * zoom / Grid.mag,
                       oy + (trace.y[i] * zoom) - 2 * zoom / Grid.mag,
                       (4 * zoom) / Grid.mag,
                       (4 * zoom) / Grid.mag));
          }
        }
      }
    }
  }

  private void drawPad(Graphics2D g, Pad pad, Color color)
  {
    double xx = pad.x;
    double yy = pad.y;
    double w1 = pad.outerSize;
    double w2 = pad.innerSize;

    if(pad.selected/* || pad.group == SimplePCB.currentGroup*/)
      g.setColor(new Color(255, 255, 255));
    else
      g.setColor(color);

    g.fill(new Ellipse2D.Double(ox + ((xx - w1 / 2.0) * zoom), oy + ((yy - w1 / 2.0) * zoom), (w1 * zoom), (w1 * zoom)));
    g.setColor(new Color(0, 0, 0));
    g.fill(new Ellipse2D.Double(ox + ((xx - w2 / 2.0) * zoom), oy + ((yy - w2 / 2.0) * zoom), (w2 * zoom), (w2 * zoom)));
  }

  private void drawBoard(Graphics2D g, Board board, Color color)
  {
    g.setStroke(new BasicStroke((float)2.0));
    g.setColor(color);
    g.draw(new Rectangle2D.Double(ox, oy, (board.w * zoom), (board.h * zoom)));
  }
 
  private void drawGrid(Graphics2D g, int w, int h)
  {
    int x, y;
    int step = (int)(Grid.inc * 256 * zoom);
    int gray = 64 + (zoom / Grid.mag) * 4;

    g.setColor(new Color(gray, gray, gray, 128));
    g.setStroke(new BasicStroke((float)1.0));

    int gw = (int)(Grid.snap((double)w / zoom) * 256 * zoom);
    int gh = (int)(Grid.snap((double)h / zoom) * 256 * zoom);

    gw = ((gw / step) * Grid.line) * (step * Grid.line);
    gh = ((gh / step) * Grid.line) * (step * Grid.line);

    for(x = 0; x < gw / step; x += Grid.line)
    {
      int gx = (ox * 256) + x * step;
      gx = (gx % gw + gw) % gw;
      g.draw(new Line2D.Double(gx / 256, 0, gx / 256, h));
    }

    for(y = 0; y < gh / step; y += Grid.line)
    {
      int gy = (oy * 256) + y * step;
      gy = (gy % gh + gh) % gh;
      g.draw(new Line2D.Double(0, gy / 256, w, gy / 256));
    }
  }

  public void drawSegmentPreview(Graphics2D g, Color color)
  {
    SimplePCB.updateMouse();

    double x = SimplePCB.x;
    double y = SimplePCB.y;
    double gridx = SimplePCB.gridx;
    double gridy = SimplePCB.gridy;
    int i;

    g.setColor(color);

    Trace trace = SimplePCB.currentTrace;
    if(trace.status && trace.length > 0)
    {
      if(trace.filled)
      {
        // polygon
        Path2D.Double path = new Path2D.Double(Path2D.WIND_EVEN_ODD, trace.length + 1);
        path.moveTo(ox + trace.x[0] * zoom, oy + trace.y[0] * zoom);

        for(i = 0; i < trace.length; i++)
        {
          path.lineTo(ox + trace.x[i] * zoom, oy + trace.y[i] * zoom);
        }

        path.lineTo(ox + gridx * zoom, oy + gridy * zoom);
        path.closePath();
        g.fill(path);
      }
      else
      {
        // trace
        g.setStroke(new BasicStroke((float)(trace.size * zoom),
                                  BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        g.draw(new Line2D.Double(ox + (trace.x[trace.length - 1] * zoom),
                   oy + (trace.y[trace.length - 1] * zoom),
                   ox + (gridx * zoom),
                   oy + (gridy * zoom)));
      }

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
    drawBoard(g, SimplePCB.board, new Color(255, 255, 0));

    int i;
    Board board = SimplePCB.board;

    // green traces
    for(i = 0; i < board.max; i++)
    {
      if(board.trace[i].status && board.trace[i].layer == 2)
        drawTrace(g, board.trace[i], new Color(0, 255, 0));
    }

    // green pads
    for(i = 0; i < board.max; i++)
    {
      if(board.pad[i].status && board.pad[i].layer == 2)
        drawPad(g, board.pad[i], new Color(0, 255, 0));
    } 

    // red traces
    for(i = 0; i < board.max; i++)
    {
      if(board.trace[i].status && board.trace[i].layer == 1)
        drawTrace(g, board.trace[i], new Color(255, 0, 0));
    }

    // red pads
    for(i = 0; i < board.max; i++)
    {
      if(board.pad[i].status && board.pad[i].layer == 1)
        drawPad(g, board.pad[i], new Color(255, 0, 0));
    } 

    // yellow traces
    for(i = 0; i < board.max; i++)
    {
      if(board.trace[i].status && board.trace[i].layer == 0)
        drawTrace(g, board.trace[i], new Color(255, 255, 0));
    }

    // yellow pads
    for(i = 0; i < board.max; i++)
    {
      if(board.pad[i].status && board.pad[i].layer == 0)
        drawPad(g, board.pad[i], new Color(255, 255, 0));
    } 

    if(SimplePCB.currentTrace != null)
    {
      drawSegmentPreview(g, new Color(192, 192, 192));  
    }

    // trace highlight & handles
    for(i = 0; i < board.max; i++)
      if(board.trace[i].status)
        highlightTrace(g, board.trace[i]);

    // grid
    drawGrid(g, w, h);

    // rubber band select
    if(SimplePCB.selectRectStatus)
    {
      int x1 = SimplePCB.selectRectX1;
      int y1 = SimplePCB.selectRectY1;
      int x2 = SimplePCB.selectRectX2;
      int y2 = SimplePCB.selectRectY2;

      if(x1 > x2)
      {
        int temp = x1;
        x1 = x2;
        x2 = temp;
      }

      if(y1 > y2)
      {
        int temp = y1;
        y1 = y2;
        y2 = temp;
      }

      g.setColor(Color.white);
      g.setStroke(new BasicStroke(2));
      g.draw(new Rectangle2D.Double(x1, y1, x2 - x1, y2 - y1));
    }
  }
}

