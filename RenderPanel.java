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
  }

  private void draw_pad(Graphics2D g, Pad pad, Color color)
  {
    int zoom = simplepcb.zoom;
    int ox = simplepcb.offsetx;
    int oy = simplepcb.offsety;

    double xx = pad.x;
    double yy = pad.y;
    double w1 = pad.outerSize;
    double w2 = pad.innerSize;

    g.setColor(color);
    g.fillOval(ox + (int)((xx - w1 / 2) * zoom), oy + (int)((yy - w1 / 2) * zoom), (int)(w1 * zoom), (int)(w1 * zoom));
    g.setColor(new Color(0, 0, 0));
    g.fillOval(ox + (int)((xx - w2 / 2) * zoom), oy + (int)((yy - w2 / 2) * zoom), (int)(w2 * zoom), (int)(w2 * zoom));
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
    int fix = zoom * 64;

    // draw grid
    g.setStroke(new BasicStroke(1));
    g.setColor(color);

    int x, y;

    for(x = 0; x < w; x += zoom / 8)
      g.drawLine((x + ox + fix) % w, 0, (x + ox + fix) % w, h);
    for(y = 0; y < h; y += zoom / 8)
      g.drawLine(0, (y + oy + fix) % h, w, (y + oy + fix) % h);
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

    // testing
    Trace testTrace = new Trace(simplepcb.traceSize);
    testTrace.add(1.0, 1.0);
    testTrace.add(1.0, 2.0);
    testTrace.add(2.0, 3.0);
    testTrace.add(2.0, 4.0);

    draw_board(g, simplepcb.board, new Color(255, 255, 0));
    Pad testPad = new Pad(1.0, 1.0, simplepcb.padInnerSize, simplepcb.padOuterSize);

    draw_trace(g, testTrace, new Color(0, 255, 0));
    draw_pad(g, testPad, new Color(0, 255, 0));
  }
}

