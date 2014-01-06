// Copyright (c) 2013 Joe Davisson.

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.text.*;

public class MainMenu extends JMenuBar
{
  public MainMenu()
  {
    // file menu
    JMenu fileMenu = new JMenu("File");

    // quit
    JMenuItem quitItem = new JMenuItem("Quit");
    quitItem.addActionListener(
      new ActionListener()
      {
        public void actionPerformed(ActionEvent e)
        {
          SimplePCB.quit();
        }
      } );

    fileMenu.add(quitItem);

    add(fileMenu);

    // Edit menu
    JMenu editMenu = new JMenu("Edit");

    JMenuItem copyItem = new JMenuItem("Copy");
    copyItem.addActionListener(
      new ActionListener()
      {
        public void actionPerformed(ActionEvent e)
        {
        }
      } );

    JMenuItem pasteItem = new JMenuItem("Paste");
    pasteItem.addActionListener(
      new ActionListener()
      {
        public void actionPerformed(ActionEvent e)
        {
        }
      } );

    JMenuItem groupItem = new JMenuItem("Group");
    groupItem.addActionListener(
      new ActionListener()
      {
        public void actionPerformed(ActionEvent e)
        {
        }
      } );

    JMenuItem ungroupItem = new JMenuItem("Ungroup");
    ungroupItem.addActionListener(
      new ActionListener()
      {
        public void actionPerformed(ActionEvent e)
        {
        }
      } );

    JMenuItem traceSizeItem = new JMenuItem("Custom Trace Size...");
    traceSizeItem.addActionListener(
      new ActionListener()
      {
        public void actionPerformed(ActionEvent e)
        {
          new TraceSizeDialog(SimplePCB.win);
        }
      } );

    JMenuItem padSizeItem = new JMenuItem("Custom Pad Size...");
    padSizeItem.addActionListener(
      new ActionListener()
      {
        public void actionPerformed(ActionEvent e)
        {
          new PadSizeDialog(SimplePCB.win);
        }
      } );

    editMenu.add(copyItem);
    editMenu.add(pasteItem);
    editMenu.addSeparator();
    editMenu.add(groupItem);
    editMenu.add(ungroupItem);
    editMenu.addSeparator();
    editMenu.add(padSizeItem);
    editMenu.add(traceSizeItem);

    add(editMenu);

    // view menu
/*
    JMenu viewMenu = new JMenu("View");

    JCheckBoxMenuItem yellow = new JCheckBoxMenuItem("Graphics", true);
    JCheckBoxMenuItem red = new JCheckBoxMenuItem("Top Traces", true);
    JCheckBoxMenuItem green = new JCheckBoxMenuItem("Bottom Traces", true);

    viewMenu.add(yellow);
    viewMenu.add(red);
    viewMenu.add(green);

    add(viewMenu);
*/
    // help menu
    JMenu helpMenu = new JMenu("Help");

    JMenuItem aboutItem = new JMenuItem("About...");
    aboutItem.addActionListener(
      new ActionListener()
      {
        public void actionPerformed(ActionEvent e)
        {
          SimplePCB.about();
        }
      } );

    helpMenu.add(aboutItem);

    add(helpMenu);
  }
}

