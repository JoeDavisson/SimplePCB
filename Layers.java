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

  int current = 0;

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
          current = 0;
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
          current = 1;
        }
      } );

    green = new JToggleButton();
    green.setToolTipText("Bottom Traces");
    green.setIcon(getIcon("/data/green.png"));
    green.setSelected(true);
    green.addActionListener(
      new ActionListener()
      {
        public void actionPerformed(ActionEvent e)
        {
          current = 2;
        }
      } );

    ButtonGroup group = new ButtonGroup();
    group.add(yellow);
    group.add(red);
    group.add(green);

    add(yellow);
    add(red);
    add(green);

    // set default button to green
    current = 2;
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

