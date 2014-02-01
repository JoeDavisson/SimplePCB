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

    // export gerber
    JMenu exportGerberMenu = new JMenu("Export Gerber");

    JMenuItem gerberItem0 = new JMenuItem("Artwork Layer...");
    gerberItem0.addActionListener(
      new ActionListener()
      {
        public void actionPerformed(ActionEvent e)
        {
          SimplePCB.exportGerberLayer(0);
        }
      } );

    JMenuItem gerberItem1 = new JMenuItem("Top Layer...");
    gerberItem1.addActionListener(
      new ActionListener()
      {
        public void actionPerformed(ActionEvent e)
        {
          SimplePCB.exportGerberLayer(1);
        }
      } );

    JMenuItem gerberItem2 = new JMenuItem("Bottom Layer...");
    gerberItem2.addActionListener(
      new ActionListener()
      {
        public void actionPerformed(ActionEvent e)
        {
          SimplePCB.exportGerberLayer(2);
        }
      } );

    exportGerberMenu.add(gerberItem0);
    exportGerberMenu.add(gerberItem1);
    exportGerberMenu.add(gerberItem2);

    // export PNG
    JMenu exportPngMenu = new JMenu("Export PNG");

    JMenuItem pngItem0 = new JMenuItem("Artwork Layer...");
    pngItem0.addActionListener(
      new ActionListener()
      {
        public void actionPerformed(ActionEvent e)
        {
          SimplePCB.exportPngLayer(0);
        }
      } );

    JMenuItem pngItem1 = new JMenuItem("Top Layer...");
    pngItem1.addActionListener(
      new ActionListener()
      {
        public void actionPerformed(ActionEvent e)
        {
          SimplePCB.exportPngLayer(1);
        }
      } );

    JMenuItem pngItem2 = new JMenuItem("Bottom Layer...");
    pngItem2.addActionListener(
      new ActionListener()
      {
        public void actionPerformed(ActionEvent e)
        {
          SimplePCB.exportPngLayer(2);
        }
      } );

    exportPngMenu.add(pngItem0);
    exportPngMenu.add(pngItem1);
    exportPngMenu.add(pngItem2);

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
    fileMenu.addSeparator();
    fileMenu.add(exportGerberMenu);
    fileMenu.add(exportPngMenu);
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

