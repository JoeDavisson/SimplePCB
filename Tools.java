import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.*;
import java.io.*;
import javax.imageio.*;

public class Tools extends JToolBar
{
  JToggleButton select = null;
  JToggleButton edit = null;
  JToggleButton pad = null;
  JToggleButton trace = null;
  JToggleButton rect = null;
  JToggleButton text = null;

  String[] iconFiles =
  {
    "/data/select.png",
    "/data/edit.png",
    "/data/pad.png",
    "/data/trace.png",
    "/data/rect.png",
    "/data/text.png"
  };

  ImageIcon[] icon;

  Tools()
  {
    super("Tools", VERTICAL);
    setFloatable(false);

    icon = new ImageIcon[iconFiles.length];

    Border border = BorderFactory.createRaisedBevelBorder();

    int i;

    for(i = 0; i < iconFiles.length; i++)
      icon[i] = getIcon(iconFiles[i]);

    select = new JToggleButton();
    select.setToolTipText("Select");
    select.setIcon(icon[0]);
    select.setSelected(true);
    select.setBorder(border);
    select.addActionListener(
      new ActionListener()
      {
        public void actionPerformed(ActionEvent e)
        {
          setMode(0);
        }
      } );

    edit = new JToggleButton();
    edit.setToolTipText("Edit");
    edit.setIcon(icon[1]);
    edit.setBorder(border);
    edit.addActionListener(
      new ActionListener()
      {
        public void actionPerformed(ActionEvent e)
        {
          setMode(1);
        }
      } );

    pad = new JToggleButton();
    pad.setToolTipText("Pad");
    pad.setIcon(icon[2]);
    pad.setBorder(border);
    pad.addActionListener(
      new ActionListener()
      {
        public void actionPerformed(ActionEvent e)
        {
          setMode(2);
        }
      } );

    trace = new JToggleButton();
    trace.setToolTipText("Trace");
    trace.setIcon(icon[3]);
    trace.setBorder(border);
    trace.addActionListener(
      new ActionListener()
      {
        public void actionPerformed(ActionEvent e)
        {
          setMode(3);
        }
      } );

    rect = new JToggleButton();
    rect.setToolTipText("Rectangle");
    rect.setIcon(icon[4]);
    rect.setBorder(border);
    rect.addActionListener(
      new ActionListener()
      {
        public void actionPerformed(ActionEvent e)
        {
          setMode(4);
        }
      } );

    text = new JToggleButton();
    text.setToolTipText("Text");
    text.setIcon(icon[5]);
    text.setBorder(border);
    text.addActionListener(
      new ActionListener()
      {
        public void actionPerformed(ActionEvent e)
        {
          setMode(5);
          new TextDialog(SimplePCB.win);
        }
      } );

    ButtonGroup group = new ButtonGroup();
    group.add(select);
    group.add(edit);
    group.add(pad);
    group.add(trace);
    group.add(rect);
    group.add(text);

    add(select);
    add(edit);
    add(pad);
    add(trace);
    add(rect);
    add(text);
  }

  public void setMode(int mode)
  {
    select.setSelected(false);
    edit.setSelected(false);
    pad.setSelected(false);
    trace.setSelected(false);
    rect.setSelected(false);
    text.setSelected(false);

    switch(mode)
    {
      case 0:
        select.setSelected(true);
        break;
      case 1:
        edit.setSelected(true);
        break;
      case 2:
        pad.setSelected(true);
        break;
      case 3:
        trace.setSelected(true);
        break;
      case 4:
        rect.setSelected(true);
        break;
      case 5:
        text.setSelected(true);
        break;
    }

    SimplePCB.setToolMode(mode);
    repaint();
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

