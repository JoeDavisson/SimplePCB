// Copyright (c) 2013 Joe Davisson.

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.text.*;

public class MainMenu extends JMenuBar
{
  public SimplePCB simplepcb;

  public MainMenu(SimplePCB spcb)
  {
    simplepcb = spcb;

    // file menu
    JMenu fileMenu = new JMenu("File");

    // quit
    JMenuItem quitItem = new JMenuItem("Quit");
    quitItem.addActionListener(
      new ActionListener()
      {
        public void actionPerformed(ActionEvent e)
        {
          simplepcb.quit();
        }
      } );

    fileMenu.add(quitItem);

    add(fileMenu);

    // Edit menu
    JMenu editMenu = new JMenu("Edit");

    JMenuItem traceSizeItem = new JMenuItem("Trace Size...");
    traceSizeItem.addActionListener(
      new ActionListener()
      {
        public void actionPerformed(ActionEvent e)
        {
          new TraceSizeDialog(simplepcb, simplepcb.win);
        }
      } );

    JMenuItem padSizeItem = new JMenuItem("Pad Size...");
    padSizeItem.addActionListener(
      new ActionListener()
      {
        public void actionPerformed(ActionEvent e)
        {
          new PadSizeDialog(simplepcb, simplepcb.win);
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

    editMenu.add(padSizeItem);
    editMenu.add(traceSizeItem);
    editMenu.add(groupItem);
    editMenu.add(ungroupItem);

    add(editMenu);

    // view menu
    JMenu viewMenu = new JMenu("View");

    JCheckBoxMenuItem yellow = new JCheckBoxMenuItem("Graphics", true);
    JCheckBoxMenuItem red = new JCheckBoxMenuItem("Top Traces", true);
    JCheckBoxMenuItem green = new JCheckBoxMenuItem("Bottom Traces", true);

    viewMenu.add(yellow);
    viewMenu.add(red);
    viewMenu.add(green);

    add(viewMenu);

    // help menu
    JMenu helpMenu = new JMenu("Help");

    JMenuItem aboutItem = new JMenuItem("About...");
    aboutItem.addActionListener(
      new ActionListener()
      {
        public void actionPerformed(ActionEvent e)
        {
          simplepcb.about();
        }
      } );

    helpMenu.add(aboutItem);

    add(helpMenu);
  }
}

