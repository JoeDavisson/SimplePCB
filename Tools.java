import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.io.*;
import javax.imageio.*;

public class Tools extends JToolBar
{
  JToggleButton select = null;
  JToggleButton edit = null;
  JToggleButton pad = null;
  JToggleButton trace = null;
  JToggleButton rect = null;

  Tools()
  {
    super("Tools", VERTICAL);
    setFloatable(false);

    select = new JToggleButton();
    select.setToolTipText("Select");
    select.setIcon(getIcon("/data/select.png"));
    select.setSelected(true);
    select.addActionListener(
      new ActionListener()
      {
        public void actionPerformed(ActionEvent e)
        {
          SimplePCB.setToolMode(0);
        }
      } );

    edit = new JToggleButton();
    edit.setToolTipText("Edit");
    edit.setIcon(getIcon("/data/edit.png"));
    edit.addActionListener(
      new ActionListener()
      {
        public void actionPerformed(ActionEvent e)
        {
          SimplePCB.setToolMode(1);
        }
      } );

    pad = new JToggleButton();
    pad.setToolTipText("Pad");
    pad.setIcon(getIcon("/data/pad.png"));
    pad.addActionListener(
      new ActionListener()
      {
        public void actionPerformed(ActionEvent e)
        {
          SimplePCB.setToolMode(2);
        }
      } );

    trace = new JToggleButton();
    trace.setToolTipText("Trace");
    trace.setIcon(getIcon("/data/trace.png"));
    trace.addActionListener(
      new ActionListener()
      {
        public void actionPerformed(ActionEvent e)
        {
          SimplePCB.setToolMode(3);
        }
      } );

    rect = new JToggleButton();
    rect.setToolTipText("Rectangle");
    rect.setIcon(getIcon("/data/rect.png"));
    rect.addActionListener(
      new ActionListener()
      {
        public void actionPerformed(ActionEvent e)
        {
          SimplePCB.setToolMode(4);
        }
      } );

    ButtonGroup group = new ButtonGroup();
    group.add(select);
    group.add(edit);
    group.add(pad);
    group.add(trace);
    group.add(rect);

    add(select);
    add(edit);
    add(pad);
    add(trace);
    add(rect);
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

