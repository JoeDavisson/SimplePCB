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

  String[] h_iconFiles =
  {
    "/data/h_select.png",
    "/data/h_edit.png",
    "/data/h_pad.png",
    "/data/h_trace.png",
    "/data/h_rect.png",
    "/data/h_text.png"
  };

  ImageIcon[] icon;
  ImageIcon[] h_icon;

  Tools()
  {
    super("Tools", VERTICAL);
    setFloatable(false);

    icon = new ImageIcon[iconFiles.length];
    h_icon = new ImageIcon[h_iconFiles.length];

    Border border = BorderFactory.createRaisedBevelBorder();

    int i;

    for(i = 0; i < iconFiles.length; i++)
    {
      icon[i] = getIcon(iconFiles[i]);
      h_icon[i] = getIcon(h_iconFiles[i]);
    }

    select = new JToggleButton();
    select.setToolTipText("Select");
    select.setSelected(true);
    select.setBorder(border);
    select.setContentAreaFilled(false);
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
    edit.setBorder(border);
    edit.setContentAreaFilled(false);
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
    pad.setBorder(border);
    pad.setContentAreaFilled(false);
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
    trace.setBorder(border);
    trace.setContentAreaFilled(false);
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
    rect.setBorder(border);
    rect.setContentAreaFilled(false);
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
    text.setBorder(border);
    text.setContentAreaFilled(false);
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
    select.setIcon(icon[0]);
    edit.setIcon(icon[1]);
    pad.setIcon(icon[2]);
    trace.setIcon(icon[3]);
    rect.setIcon(icon[4]);
    text.setIcon(icon[5]);

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
        select.setIcon(h_icon[0]);
        break;
      case 1:
        edit.setSelected(true);
        edit.setIcon(h_icon[1]);
        break;
      case 2:
        pad.setSelected(true);
        pad.setIcon(h_icon[2]);
        break;
      case 3:
        trace.setSelected(true);
        trace.setIcon(h_icon[3]);
        break;
      case 4:
        rect.setSelected(true);
        rect.setIcon(h_icon[4]);
        break;
      case 5:
        text.setSelected(true);
        text.setIcon(h_icon[5]);
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

