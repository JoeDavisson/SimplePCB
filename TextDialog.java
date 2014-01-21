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

public class TextDialog extends JDialog
{
  public JTextField textField;
  public JTextField sizeField;

  // ok/cancel
  public JButton okButton;
  public JButton cancelButton;

  public TextDialog(Window owner)
  {
    super(owner, "Text");

    // main window
    setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    setLayout(new BorderLayout());

    // panels
    JPanel topPanel = new JPanel(new GridLayout(2, 1, 0, 0));
    JPanel buttonFlowPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
    JPanel buttonGridPanel = new JPanel(new GridLayout(1, 2, 8, 0));

    // text
    topPanel.add(new JLabel("Text"));
    textField = new JTextField(8);
    textField.setEditable(true);
    textField.setText("Text");
    topPanel.add(textField);

    // text
    topPanel.add(new JLabel("Size"));
    sizeField = new JTextField(8);
    sizeField.setEditable(true);
    sizeField.setText("10");
    topPanel.add(sizeField);

    // ok button
    okButton = new JButton("OK");
    okButton.addActionListener(
      new ActionListener()
      {
        public void actionPerformed(ActionEvent e)
        {
          SimplePCB.addText(textField.getText(), Double.parseDouble(sizeField.getText()));
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

