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

    JMenuItem newItem = new JMenuItem("New Project");
    newItem.addActionListener(
      new ActionListener()
      {
        public void actionPerformed(ActionEvent e)
        {
          int response = JOptionPane.showConfirmDialog(SimplePCB.win, "Are You Sure?", "New Project", JOptionPane.YES_NO_OPTION);
          if(response == JOptionPane.YES_OPTION)
            SimplePCB.newProject();
        }
      } );

    JMenuItem loadItem = new JMenuItem("Load Project...");
    loadItem.addActionListener(
      new ActionListener()
      {
        public void actionPerformed(ActionEvent e)
        {
          SimplePCB.loadProject();
        }
      } );

    JMenuItem saveItem = new JMenuItem("Save Project...");
    saveItem.addActionListener(
      new ActionListener()
      {
        public void actionPerformed(ActionEvent e)
        {
          SimplePCB.saveProject();
        }
      } );

    // export
    JMenu exportMenu = new JMenu("Export");

    JMenuItem yellowItem = new JMenuItem("Artwork Layer...");
    yellowItem.addActionListener(
      new ActionListener()
      {
        public void actionPerformed(ActionEvent e)
        {
          SimplePCB.exportLayer(0);
        }
      } );

    JMenuItem redItem = new JMenuItem("Top Layer...");
    redItem.addActionListener(
      new ActionListener()
      {
        public void actionPerformed(ActionEvent e)
        {
          SimplePCB.exportLayer(1);
        }
      } );

    JMenuItem greenItem = new JMenuItem("Bottom Layer...");
    greenItem.addActionListener(
      new ActionListener()
      {
        public void actionPerformed(ActionEvent e)
        {
          SimplePCB.exportLayer(2);
        }
      } );

    exportMenu.add(yellowItem);
    exportMenu.add(redItem);
    exportMenu.add(greenItem);

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

    fileMenu.add(newItem);
    fileMenu.add(loadItem);
    fileMenu.add(saveItem);
    fileMenu.add(exportMenu);
    fileMenu.addSeparator();
    fileMenu.add(quitItem);

    add(fileMenu);

    // edit menu
    JMenu editMenu = new JMenu("Edit");

    JMenuItem duplicateItem = new JMenuItem("Duplicate");
    duplicateItem.addActionListener(
      new ActionListener()
      {
        public void actionPerformed(ActionEvent e)
        {
          SimplePCB.duplicate();
        }
      } );

    JMenuItem groupItem = new JMenuItem("Group");
    groupItem.addActionListener(
      new ActionListener()
      {
        public void actionPerformed(ActionEvent e)
        {
          SimplePCB.finishGroup();
        }
      } );

    JMenuItem ungroupItem = new JMenuItem("Ungroup");
    ungroupItem.addActionListener(
      new ActionListener()
      {
        public void actionPerformed(ActionEvent e)
        {
          SimplePCB.unGroup();
        }
      } );

    JMenuItem boardSizeItem = new JMenuItem("Board Size...");
    boardSizeItem.addActionListener(
      new ActionListener()
      {
        public void actionPerformed(ActionEvent e)
        {
          new BoardSizeDialog(SimplePCB.win);
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

    JMenuItem gridItem = new JMenuItem("Grid...");
    gridItem.addActionListener(
      new ActionListener()
      {
        public void actionPerformed(ActionEvent e)
        {
          new GridDialog(SimplePCB.win);
        }
      } );

    editMenu.add(duplicateItem);
    editMenu.addSeparator();
    editMenu.add(groupItem);
    editMenu.add(ungroupItem);
    editMenu.addSeparator();
    editMenu.add(boardSizeItem);
    editMenu.add(padSizeItem);
    editMenu.add(traceSizeItem);
    editMenu.addSeparator();
    editMenu.add(gridItem);

    add(editMenu);

    // transform menu
    JMenu transformMenu = new JMenu("Transform");

    JMenuItem rotateItem = new JMenuItem("Rotate");
    rotateItem.addActionListener(
      new ActionListener()
      {
        public void actionPerformed(ActionEvent e)
        {
          SimplePCB.rotate();
        }
      } );

    JMenuItem flipItem = new JMenuItem("Flip");
    flipItem.addActionListener(
      new ActionListener()
      {
        public void actionPerformed(ActionEvent e)
        {
          SimplePCB.flip();
        }
      } );

    JMenuItem mirrorItem = new JMenuItem("Mirror");
    mirrorItem.addActionListener(
      new ActionListener()
      {
        public void actionPerformed(ActionEvent e)
        {
          SimplePCB.mirror();
        }
      } );

    transformMenu.add(rotateItem);
    transformMenu.add(flipItem);
    transformMenu.add(mirrorItem);

    add(transformMenu);

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

