import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.io.*;
import javax.imageio.*;

public class TopBar extends JToolBar
{
  String traceSizeStrings[] =
  {
    ".4 mm (~.015\")",
    ".6 mm (~.025\")",
    ".8 mm (~.03\")",
    "1.0 mm (~.04\")",
    "1.2 mm",
    "1.4 mm (~.06\")",
    "1.6 mm",
    "1.8 mm",
    "2.0 mm (~.08\")",
    "2.2 mm",
    "2.4 mm (~.1\")",
    "2.6 mm",
    "2.8 mm",
    "3.0 mm",
    "3.2 mm (~.12\")",
    "3.4 mm",
    "3.6 mm (~.14\")",
    "3.8 mm",
    "4.0 mm (~.16\")"
  };
 
  double traceSize[] =
  {
    .4,
    .6,
    .8,
    1.0,
    1.2,
    1.4,
    1.6,
    1.8,
    2.0,
    2.2,
    2.4,
    2.6,
    2.8,
    3.0,
    3.2,
    3.4,
    3.6,
    3.8,
    4.0
  };

  String padSizeStrings[] =
  {
    ".4 mm (~1/64\")",
    ".6 mm",
    ".8 mm (~1/32\")",
    "1.0 mm",
    "1.2 mm (~3/64\")",
    "1.4 mm",
    "1.6 mm (~1/16\")",
    "1.8 mm",
    "2.0 mm (~5/64\")",
    "2.2 mm",
    "2.4 mm (~3/32\")",
    "2.6 mm",
    "2.8 mm (~7/64\")",
    "3.0 mm (~1/8\")"
  };
 
  double padSize[] =
  {
    .4,
    .6,
    .8,
    1.0,
    1.2,
    1.4,
    1.6,
    1.8,
    2.0,
    2.2,
    2.4,
    2.6,
    2.8,
    3.0
  };

  JComboBox traceSizeBox = null;
  JComboBox padSizeBox = null;

  TopBar()
  {
    super("TopBar", HORIZONTAL);

    setFloatable(false);

    traceSizeBox = new JComboBox<String>(traceSizeStrings);
    traceSizeBox.setToolTipText("Trace Size");
    traceSizeBox.setSelectedIndex(0);
    traceSizeBox.addActionListener(
      new ActionListener()
      {
        public void actionPerformed(ActionEvent e)
        {
          int i = traceSizeBox.getSelectedIndex();
          SimplePCB.traceSize = traceSize[i];
        }
      } );

    padSizeBox = new JComboBox<String>(padSizeStrings);
    padSizeBox.setToolTipText("Pad Size");
    padSizeBox.setSelectedIndex(0);
    padSizeBox.addActionListener(
      new ActionListener()
      {
        public void actionPerformed(ActionEvent e)
        {
          int i = padSizeBox.getSelectedIndex();
          SimplePCB.padInnerSize = padSize[i];
          SimplePCB.padOuterSize = SimplePCB.padInnerSize * 2;
        }
      } );

    add(new JLabel("Trace: "));
    add(traceSizeBox);
    addSeparator();
    add(new JLabel("Pad: "));
    add(padSizeBox);

    // set defaults
    int i = traceSizeBox.getSelectedIndex();
    SimplePCB.traceSize = traceSize[i];

    i = padSizeBox.getSelectedIndex();
    SimplePCB.padInnerSize = padSize[i];
    SimplePCB.padOuterSize = SimplePCB.padInnerSize * 2;
  }

  private void reset()
  {
  }
}

