/*
Copyright (c) 2012 Joe Davisson.

This file is part of SimplePCB.

This program is free software; you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation; either version 2 of the License, or
(at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with this program; if not, write to the Free Software
Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301 USA
*/

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.text.*;

public class GridDialog extends JDialog
{
  public JTextField spacingField;
  public JTextField lineField;
  public JRadioButton mmButton;
  public JRadioButton inchesButton;

  // ok/cancel
  public JButton okButton;
  public JButton cancelButton;

  public GridDialog(Window owner)
  {
    super(owner, "Grid");

    // main window
    setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    setLayout(new BorderLayout());

    // panels
    JPanel topPanel = new JPanel(new GridLayout(4, 1, 0, 0));
    JPanel buttonFlowPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
    JPanel buttonGridPanel = new JPanel(new GridLayout(1, 2, 8, 0));

    // grid spacing
    topPanel.add(new JLabel("Spacing"));
    spacingField = new JTextField(6);
    spacingField.setEditable(true);
    if(Grid.inches)
      spacingField.setText(Double.toString(Grid.inc / 25.4));
    else
      spacingField.setText(Double.toString(Grid.inc));
    topPanel.add(spacingField);

    // grid line
    topPanel.add(new JLabel("Line Every"));
    lineField = new JTextField(3);
    lineField.setEditable(true);
    lineField.setText(Integer.toString(Grid.line));
    topPanel.add(lineField);

    // mm
    mmButton = new JRadioButton("Millimeters", !Grid.inches);
    mmButton.addActionListener(
      new ActionListener()
      {
        public void actionPerformed(ActionEvent e)
        {
          Grid.inches = false;
        }
      } );

    // inches
    inchesButton = new JRadioButton("Inches", Grid.inches);
    inchesButton.addActionListener(
      new ActionListener()
      {
        public void actionPerformed(ActionEvent e)
        {
          Grid.inches = true;
        }
      } );

    ButtonGroup unitGroup = new ButtonGroup();
    unitGroup.add(mmButton);
    unitGroup.add(inchesButton);
    topPanel.add(mmButton);
    topPanel.add(inchesButton);

    // ok button
    okButton = new JButton("OK");
    okButton.addActionListener(
      new ActionListener()
      {
        public void actionPerformed(ActionEvent e)
        {
          
          if(Grid.inches)
            Grid.setInches(Double.parseDouble(spacingField.getText()));
          else
            Grid.setMetric(Double.parseDouble(spacingField.getText()));

          if(Grid.inc > 10)
            Grid.inc = 10;

          if(Grid.inc < .01)
            Grid.inc = .01;

          Grid.line = Integer.parseInt(lineField.getText());

          if(Grid.line > 10)
            Grid.line = 10;

          if(Grid.line < 1)
            Grid.line = 1;

          SimplePCB.panel.repaint();
          dispose();
        }
      } );
    buttonGridPanel.add(okButton);

    // cancel button
    cancelButton = new JButton("CANCEL");
    cancelButton.addActionListener(
      new ActionListener()
      {
        public void actionPerformed(ActionEvent e)
        {
          dispose();
        }
      } );
    buttonGridPanel.add(cancelButton);

    // add panels to main window
    add(topPanel, BorderLayout.NORTH);
    buttonFlowPanel.add(buttonGridPanel, BorderLayout.EAST);
    add(buttonFlowPanel, BorderLayout.SOUTH);

    // display
    setSize(300, 160);
    setLocationRelativeTo(owner);
    setResizable(false);
    setVisible(true);
  }  
}

