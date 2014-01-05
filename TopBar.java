import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.io.*;
import javax.imageio.*;

public class TopBar extends JToolBar
{
  String traceSizeStrings[] =
  {
    "0.0125\"",
    "0.025\"",
    "0.05\"",
    "0.075\"",
    "0.1\"",
    "0.25\"",
    "0.5\"",
    "0.75\"",
    "1.0\""
  };

  String padSizeStrings[] =
  {
    "10 AWG",
    "12 AWG",
    "14 AWG",
    "16 AWG",
    "18 AWG",
    "20 AWG",
    "22 AWG",
    "24 AWG",
    "26 AWG",
    "28 AWG",
    "30 AWG"
  };

  double traceSize[] =
  {
    0.0125,
    0.025,
    0.05,
    0.075,
    0.1,
    0.25,
    0.5,
    0.75,
    1.0
  };

  double padInnerSize[] =
  {
    0.120, 
    0.100, 
    0.080, 
    0.060, 
    0.048, 
    0.040, 
    0.030, 
    0.025, 
    0.016, 
    0.013, 
    0.012, 
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
    padSizeBox.setSelectedIndex(6);
    padSizeBox.addActionListener(
      new ActionListener()
      {
        public void actionPerformed(ActionEvent e)
        {
          int i = padSizeBox.getSelectedIndex();
          SimplePCB.padInnerSize = padInnerSize[i] + .01;
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
    SimplePCB.padInnerSize = padInnerSize[i] + .01;
    SimplePCB.padOuterSize = SimplePCB.padInnerSize * 2;
  }
}

