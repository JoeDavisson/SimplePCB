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
  ImageIcon h_yellowIcon;
  ImageIcon h_redIcon;
  ImageIcon h_greenIcon;
  ImageIcon h_gridIcon;

  int current = 0;

  Layers()
  {
    super("Layers", VERTICAL);
    setFloatable(false);

    yellowIcon = getIcon("/data/yellow.png");
    redIcon = getIcon("/data/red.png");
    greenIcon = getIcon("/data/green.png");
    gridIcon = getIcon("/data/grid.png");
    h_yellowIcon = getIcon("/data/h_yellow.png");
    h_redIcon = getIcon("/data/h_red.png");
    h_greenIcon = getIcon("/data/h_green.png");
    h_gridIcon = getIcon("/data/h_grid.png");

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
          setLayer(0);
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
          setLayer(1);
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
          setLayer(2);
        }
      } );

    grid = new JToggleButton();
    grid.setToolTipText("Grid");
    grid.setIcon(h_gridIcon);
    grid.setBorder(border);
    grid.setSelected(true);
    grid.addActionListener(
      new ActionListener()
      {
        public void actionPerformed(ActionEvent e)
        {
          Grid.use = grid.isSelected();
          if(Grid.use)
            grid.setIcon(h_gridIcon);
          else
            grid.setIcon(gridIcon);

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

  public void setLayer(int layer)
  {
    current = layer;

    yellow.setIcon(yellowIcon);
    red.setIcon(redIcon);
    green.setIcon(greenIcon);

    switch(layer)
    {
      case 0:
        yellow.setIcon(h_yellowIcon);
        break;
      case 1:
        red.setIcon(h_redIcon);
        break;
      case 2:
        green.setIcon(h_greenIcon);
        break;
    }
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

