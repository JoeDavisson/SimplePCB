import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.*;
import java.io.*;
import javax.imageio.*;

public class Layers extends JToolBar
{
  JToggleButton yellow = null;
  JToggleButton red = null;
  JToggleButton green = null;
  JToggleButton grid = null;

  ImageIcon yellowIcon;
  ImageIcon redIcon;
  ImageIcon greenIcon;
  ImageIcon gridIcon;

  int current = 2;

  Layers()
  {
    super("Layers", VERTICAL);
    setFloatable(false);

    yellowIcon = getIcon("/data/yellow.png");
    redIcon = getIcon("/data/red.png");
    greenIcon = getIcon("/data/green.png");
    gridIcon = getIcon("/data/grid.png");

    Border border = BorderFactory.createRaisedBevelBorder();

    yellow = new JToggleButton();
    yellow.setToolTipText("Artwork");
    yellow.setIcon(yellowIcon);
    yellow.setBorder(border);
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
    red.setIcon(redIcon);
    red.setBorder(border);
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
    green.setIcon(greenIcon);
    green.setBorder(border);
    green.setSelected(true);
    green.addActionListener(
      new ActionListener()
      {
        public void actionPerformed(ActionEvent e)
        {
          current = 2;
        }
      } );

    grid = new JToggleButton();
    grid.setToolTipText("Grid");
    grid.setIcon(gridIcon);
    grid.setBorder(border);
    grid.setSelected(true);
    grid.addActionListener(
      new ActionListener()
      {
        public void actionPerformed(ActionEvent e)
        {
          Grid.use = grid.isSelected();
          SimplePCB.panel.repaint();
        }
      } );

    ButtonGroup group = new ButtonGroup();
    group.add(yellow);
    group.add(red);
    group.add(green);

    add(yellow);
    add(red);
    add(green);
    addSeparator();
    add(grid);

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

