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

public class BoardSizeDialog extends JDialog
{
  public JTextField widthField;
  public JTextField heightField;
  public JRadioButton mmButton;
  public JRadioButton inchesButton;

  // ok/cancel
  public JButton okButton;
  public JButton cancelButton;

  private boolean inches = false;

  public BoardSizeDialog(Window owner)
  {
    super(owner, "Board Size");

    inches = Grid.inches;

    // main window
    setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    setLayout(new BorderLayout());

    // panels
    JPanel topPanel = new JPanel(new GridLayout(4, 1, 0, 0));
    JPanel buttonFlowPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
    JPanel buttonGridPanel = new JPanel(new GridLayout(1, 2, 8, 0));

    // width
    topPanel.add(new JLabel("Width"));
    widthField = new JTextField(6);
    widthField.setEditable(true);
    if(inches)
      widthField.setText(Double.toString(SimplePCB.board.w / 25.4));
    else
      widthField.setText(Double.toString(SimplePCB.board.w));
    topPanel.add(widthField);

    // height
    topPanel.add(new JLabel("Height"));
    heightField = new JTextField(6);
    heightField.setEditable(true);
    if(inches)
      heightField.setText(Double.toString(SimplePCB.board.h / 25.4));
    else
      heightField.setText(Double.toString(SimplePCB.board.h));
    topPanel.add(heightField);

    // mm
    mmButton = new JRadioButton("Millimeters", !Grid.inches);
    mmButton.addActionListener(
      new ActionListener()
      {
        public void actionPerformed(ActionEvent e)
        {
          inches = false;
        }
      } );

    // inches
    inchesButton = new JRadioButton("Inches", Grid.inches);
    inchesButton.addActionListener(
      new ActionListener()
      {
        public void actionPerformed(ActionEvent e)
        {
          inches = true;
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
          if(inches)
          {
            SimplePCB.board.w = Double.parseDouble(widthField.getText()) * 25.4;
            SimplePCB.board.h = Double.parseDouble(heightField.getText()) * 25.4;
          }
          else
          {
            SimplePCB.board.w = Double.parseDouble(widthField.getText());
            SimplePCB.board.h = Double.parseDouble(heightField.getText());
          }

          if(SimplePCB.board.w > 1000)
            SimplePCB.board.w = 1000;

          if(SimplePCB.board.w < 10)
            SimplePCB.board.w = 10;

          if(SimplePCB.board.h > 1000)
            SimplePCB.board.h = 1000;

          if(SimplePCB.board.h < 10)
            SimplePCB.board.h = 10;

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

