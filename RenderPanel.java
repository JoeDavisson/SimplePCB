import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class RenderPanel extends JPanel
{
  RenderPanel()
  {
    super();

    setSize(640, 480);
    setPreferredSize(new Dimension(640, 480));
    setLayout(new BorderLayout());

  }

  public void paintComponent(Graphics g)
  {
    Dimension d = getSize();
    int w = d.width;
    int h = d.height;

    // clear
    g.setColor(Color.BLACK);
    g.fillRect(0, 0, w, h);

    // draw grid
    g.setColor(new Color(64, 64, 64));

    int x, y;

    for(x = 0; x < w; x += 8)
      g.drawLine(x, 0, x, h);
    for(y = 0; y < h; y += 8)
      g.drawLine(0, y, w, y);
  }
}

