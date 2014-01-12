import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.io.*;
import javax.imageio.*;

public class TopBar extends JToolBar
{
  String traceSizeStrings[] =
  {
    ".25 mm (.01 inches)",
    ".50 mm (.02 inches)",
    ".75 mm (.03 inches)",
    "1.0 mm (.04 inches)",
    "1.25 mm (.05 inches)",
    "1.5 mm (.06 inches)",
    "2.0 mm (.08 inches)",
    "3.0 mm (.1 inches)",
    "4.0 mm (.15 inches)",
    "5.0 mm (.2 inches)",
    "6.0 mm (.25 inches)",
    "7.0 mm (.275 inches)",
    "8.0 mm (.3 inches)",
    "9.0 mm (.35 inches)",
    "10.0 mm (.4 inches)"
  };

  String padSizeStrings[] =
  {
    ".2548 mm (30 AWG)",
    ".3211 mm (28 AWG)",
    ".4049 mm (26 AWG)",
    ".5105 mm (24 AWG)",
    ".6439 mm (22 AWG)",
    ".8118 mm (20 AWG)",
    "1.024 mm (18 AWG)",
    "1.291 mm (16 AWG)",
    "1.628 mm (14 AWG)",
    "2.053 mm (12 AWG)",
    "2.588 mm (10 AWG)",
    "3.264 mm (8 AWG)"
  };

  double traceSize[] =
  {
    .25,
    .50,
    .75,
    1.0,
    1.25,
    1.5,
    2.0,
    3.0,
    4.0,
    5.0,
    6.0,
    7.0,
    8.0,
    9.0,
    10.0
  };

  double padInnerSize[] =
  {
    .2548,
    .3211,
    .4049,
    .5105,
    .6439,
    .8118,
    1.024,
    1.291,
    1.628,
    2.053,
    2.588,
    3.264
  };

  JComboBox traceSizeBox = null;
  JComboBox padSizeBox = null;

  TopBar()
  {
    super("TopBar", HORIZONTAL);

    setFloatable(false);

    traceSizeBox = new JComboBox<String>(traceSizeStrings);
    traceSizeBox.setToolTipText("Pad Size");
    traceSizeBox.setSelectedIndex(1);
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
    padSizeBox.setSelectedIndex(4);
    padSizeBox.addActionListener(
      new ActionListener()
      {
        public void actionPerformed(ActionEvent e)
        {
          int i = padSizeBox.getSelectedIndex();
          SimplePCB.padInnerSize = padInnerSize[i];
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
    SimplePCB.padInnerSize = padInnerSize[i];
    SimplePCB.padOuterSize = SimplePCB.padInnerSize * 2;
  }
}

