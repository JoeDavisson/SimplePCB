import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.io.*;
import javax.imageio.*;

public class Layers extends JToolBar
{
  JToggleButton yellow = null;
  JToggleButton red = null;
  JToggleButton green = null;

  Layers()
  {
    super("Layers", VERTICAL);
    setFloatable(false);

    yellow = new JToggleButton();
    yellow.setToolTipText("Graphics");
    yellow.setIcon(getIcon("/data/yellow.png"));
    yellow.addActionListener(
      new ActionListener()
      {
        public void actionPerformed(ActionEvent e)
        {
        }
      } );

    red = new JToggleButton();
    red.setToolTipText("Top Traces");
    red.setIcon(getIcon("/data/red.png"));
    red.addActionListener(
      new ActionListener()
      {
        public void actionPerformed(ActionEvent e)
        {
        }
      } );

    green = new JToggleButton();
    green.setToolTipText("Bottom Traces");
    green.setIcon(getIcon("/data/green.png"));
    green.addActionListener(
      new ActionListener()
      {
        public void actionPerformed(ActionEvent e)
        {
        }
      } );

    ButtonGroup group = new ButtonGroup();
    group.add(yellow);
    group.add(red);
    group.add(green);

    add(yellow);
    add(red);
    add(green);
  }

  private ImageIcon getIcon(String s)
  {
    ImageIcon temp = null;

    try
    {
      temp = new ImageIcon(ImageIO.read(getClass().getResource(s)));
    }
    catch(IOException e)
    {
    }

    return temp;
  }
}

