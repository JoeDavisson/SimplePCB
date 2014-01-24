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
import java.awt.image.*;
import javax.swing.*;
import javax.swing.text.*;
import java.io.*;
import java.net.*;

public class SimplePCB
{
  public static Frame win = null;
  public static TopBar topbar = null;
  public static Tools tools = null;
  public static Layers layers = null;
  public static RenderPanel panel = null;

  public static double traceSize = .02;
  public static double padInnerSize = .03;
  public static double padOuterSize = .08;

  public static PCBFont font;
  public static Input input;
  public static Board board = null;

  public static int zoom = Grid.mag;
  public static Trace currentTrace = null;
  public static Pad selectedPad = null;
  public static Trace selectedTrace = null;

  public static int mousex, mousey;
  public static int ox, oy;
  public static double x, y;
  public static double gridx, gridy;

  public static int nextGroup = 0;

  public static int toolMode = 0;

  public static boolean selectRectStatus = false;
  public static int selectRectX1; 
  public static int selectRectY1; 
  public static int selectRectX2; 
  public static int selectRectY2; 
  public static boolean selectionStarted = false;

  public static double centerx = 0;
  public static double centery = 0;
  public static double angle = Math.toRadians(90);

  // about dialog
  public static void about()
  {
    JOptionPane.showMessageDialog(win, "SimplePCB",
      "About", JOptionPane.INFORMATION_MESSAGE);
  }

  // new project
  public static void newProject() 
  {
    cancelSelection();
    unselectAll();
    board = new Board(3.0 * 25.4, 2.0 * 25.4);
    panel.repaint();
  }

  // load project
  public static void loadProject() 
  {
    final JFileChooser fc = new JFileChooser();

    if(fc.showOpenDialog(win) == JFileChooser.APPROVE_OPTION)
    {
      File file = fc.getSelectedFile();
      board.load(file);
    }
  }

  // save project
  public static void saveProject() 
  {
    final JFileChooser fc = new JFileChooser();

    if(fc.showSaveDialog(win) == JFileChooser.APPROVE_OPTION)
    {
      File file = fc.getSelectedFile();
      if(file != null && file.exists())
      {
        int response = JOptionPane.showConfirmDialog(win, "\"" + file.getName() + "\" exists, overwrite?", "File Exists", JOptionPane.YES_NO_OPTION);
        if(response != JOptionPane.YES_OPTION)
          return;
      }
      board.save(file);
    }
  }

  // save gerber
  public static void exportLayer(int layer) 
  {
    final JFileChooser fc = new JFileChooser();

    if(fc.showSaveDialog(win) == JFileChooser.APPROVE_OPTION)
    {
      File file = fc.getSelectedFile();
      if(file != null && file.exists())
      {
        int response = JOptionPane.showConfirmDialog(win, "\"" + file.getName() + "\" exists, overwrite?", "File Exists", JOptionPane.YES_NO_OPTION);
        if(response != JOptionPane.YES_OPTION)
          return;
      }
      Gerber.save(board, file, layer);
    }
  }

  // find center of selection
  public static void findCenter()
  {
    int i, j;

    double x1 = 1000, y1 = 1000, x2 = -1000, y2 = -1000;

    for(i = 0; i < board.max; i++)
    {
      Trace trace = board.trace[i];
      Pad pad = board.pad[i];

      if(trace.selected)
      {
        for(j = 0; j < trace.length; j++)
        {
          if(trace.x[j] < x1)
            x1 = trace.x[j];
          if(trace.y[j] < y1)
            y1 = trace.y[j];
          if(trace.x[j] > x2)
            x2 = trace.x[j];
          if(trace.y[j] > y2)
            y2 = trace.y[j];
        }
      }
      if(pad.selected)
      {
        if(pad.x < x1)
          x1 = pad.x;
        if(pad.y < y1)
          y1 = pad.y;
        if(pad.x > x2)
          x2 = pad.x;
        if(pad.y > y2)
          y2 = pad.y;
      }

      centerx = x1 + (x2 - x1) / 2;
      centery = y1 + (y2 - y1) / 2;
    }
  }

  // rotate selection
  public static void rotate()
  {
    int i, j;
    findCenter();

    for(i = 0; i < board.max; i++)
    {
      Trace trace = board.trace[i];
      Pad pad = board.pad[i];

      if(trace.selected)
      {
        for(j = 0; j < trace.length; j++)
        {
          double x = trace.x[j];
          double y = trace.y[j];
          double gx = centerx + (x - centerx) * Math.cos(angle)
                               - (y - centery) * Math.sin(angle);
          double gy = centery + (x - centerx) * Math.sin(angle)
                               + (y - centery) * Math.cos(angle);
          trace.x[j] = gx;
          trace.y[j] = gy;
        }
      }
      if(pad.selected)
      {
        double x = pad.x;
        double y = pad.y;
        double gx = centerx + (x - centerx) * Math.cos(angle)
                               - (y - centery) * Math.sin(angle);
        double gy = centery + (x - centerx) * Math.sin(angle)
                               - (y - centery) * Math.cos(angle);
        pad.x = gx;
        pad.y = gy;
      }
    }

    panel.repaint();
  }

  // flip selection
  public static void flip()
  {
    int i, j;
    findCenter();

    for(i = 0; i < board.max; i++)
    {
      Trace trace = board.trace[i];
      Pad pad = board.pad[i];

      if(trace.selected)
      {
        for(j = 0; j < trace.length; j++)
        {
          trace.x[j] = centerx - (trace.x[j] - centerx);
        }
      }
      if(pad.selected)
      {
        pad.x = centerx - (pad.x - centerx);
      }
    }

    panel.repaint();
  }

  // mirror selection
  public static void mirror()
  {
    int i, j;
    findCenter();

    for(i = 0; i < board.max; i++)
    {
      Trace trace = board.trace[i];
      Pad pad = board.pad[i];

      if(trace.selected)
      {
        for(j = 0; j < trace.length; j++)
        {
          trace.y[j] = centery - (trace.y[j] - centery);
        }
      }
      if(pad.selected)
      {
        pad.y = centery - (pad.y - centery);
      }
    }

    panel.repaint();
  }

  // quit
  public static void quit() 
  {
    System.exit(0);
  }

  // sleep
  public static void sleep()
  {
    try
    {
      Thread.sleep(16);
    }
    catch(InterruptedException e)
    {
    }
  }

  // change tool
  public static void setToolMode(int mode)
  {
    toolMode = mode;
    currentTrace = null;
    panel.repaint();
  }

  // mouse related math
  public static void updateMouse()
  {
    mousex = input.mousex;
    mousey = input.mousey;
    x = (double)(mousex - ox) / zoom;
    y = (double)(mousey - oy) / zoom;

    if(Grid.use)
    {
      gridx = Grid.snap(x);
      gridy = Grid.snap(y);
    }
    else
    {
      gridx = x;
      gridy = y;
    }
  }

  // point on line test
  public static boolean pointOnLine(double x, double y, double x1, double y1, double x2, double y2, double size)
  {
    double px = x2 - x1;
    double py = y2 - y1;

    double len = px * px + py * py;

    double u = ((x - x1) * px + (y - y1) * py) / len;

    if(u > 1.0)
      u = 1.0;
    if(u < 0)
      u = 0;

    double x3 = x1 + u * px;
    double y3 = y1 + u * py;

    double dx = x3 - x;
    double dy = y3 - y;

    if(Math.sqrt(dx * dx + dy * dy) < size / 2)
      return true;
    else
      return false;
  }

  // point in circle test
  public static boolean pointInCircle(double x, double y, double r)
  {
    if(x * x + y * y < r * r)
      return true;
    else
     return false;
  }

  public static double isLeft(double x1, double y1, double x2, double y2,
                      double x3, double y3)
  {
    return (x2 - x1) * (y3 - y1) - (x3 - x1) * (y2 - y1);
  }

  public static boolean pointInPoly(Trace trace, double x, double y)
  {
    int i;
    int inside = 0;

    double px1, py1, px2, py2;

    for(i = 0; i < trace.length - 1; i++)
    {
      px1 = trace.x[i];
      py1 = trace.y[i];
      px2 = trace.x[i + 1];
      py2 = trace.y[i + 1];

      if(py1 <= y)
      {
        if((py2 > y) && (isLeft(px1, py1, px2, py2, x, y) > 0))
          inside++;
      }
      else
      {
        if((py2 <= y) && (isLeft(px1, py1, px2, py2, x, y) < 0))
          inside++;
      }
    }

    px1 = trace.x[0];
    py1 = trace.y[0];
    px2 = trace.x[trace.length - 1];
    py2 = trace.y[trace.length - 1];

    if(py1 <= y)
    {
      if((py2 > y) && (isLeft(px1, py1, px2, py2, x, y) > 0))
        inside++;
    }
    else
    {
      if((py2 <= y) && (isLeft(px1, py1, px2, py2, x, y) < 0))
        inside++;
    }
 
    if((inside & 1) == 1)
      return true;
    else
      return false;
  }

  // unselect all
  public static void unselectAll()
  {
    currentTrace = null;
    selectedTrace = null;
    selectedPad = null;
  }

  // cancel selection
  public static void cancelSelection()
  {
    int i;

    for(i = 0; i < board.max; i++)
    {
      board.trace[i].selected = false;
      board.pad[i].selected = false;
    }

    selectionStarted = false;
    currentTrace = null;
  }

  // finish group
  public static void finishGroup()
  {
    int i;

    for(i = 0; i < board.max; i++)
    {
      if(board.trace[i].selected)
        board.trace[i].group = nextGroup;
      if(board.pad[i].selected)
        board.pad[i].group = nextGroup;
    }

    cancelSelection();
   
    nextGroup++;
    panel.repaint();
  }

  public static void addGroupToSelection(int group)
  {
    int i;

    for(i = 0; i < board.max; i++)
    {
      if(board.trace[i].group == group)
        board.trace[i].selected = true;
      if(board.pad[i].group == group)
        board.pad[i].selected = true;
    }
  }

  public static void unGroup()
  {
    int i;

    for(i = 0; i < board.max; i++)
    {
      if(board.pad[i].selected)
        board.pad[i].group = -1;

      if(board.trace[i].selected)
        board.trace[i].group = -1;
    }

    panel.repaint();
  }

  // move entire group
  public static void moveGroup()
  {
    int i, j;

    while(input.button1)
    {
      double oldgridx = gridx;
      double oldgridy = gridy;

      updateMouse();

      double deltax = gridx - oldgridx;
      double deltay = gridy - oldgridy;

      for(i = 0; i < board.max; i++)
      {
        if(board.pad[i].selected)
        {
          board.pad[i].x += deltax;
          board.pad[i].y += deltay;
        }

        if(board.trace[i].selected)
        {
          for(j = 0; j < board.trace[i].length; j++)
          {
            board.trace[i].x[j] += deltax;
            board.trace[i].y[j] += deltay;
          }
        }
      }

      panel.repaint();
      sleep();
    }
  }

  // add text
  public static void addText(String s, double height, double size) 
  {
    // make a new trace group for this text
    cancelSelection();
    unselectAll();

    int i, j;

    double pos = 0;

    double scale = .01 * height;

    Trace tempTrace;
    Letter letter;

    for(i = 0; i < s.length(); i++)
    {
      letter = PCBFont.letter[s.charAt(i)];

      double highest = 0;

      for(j = 0; j < letter.length; j++)
      {
        double x1 = (double)letter.x1[j];
        double y1 = (double)letter.y1[j];
        double x2 = (double)letter.x2[j];
        double y2 = (double)letter.y2[j];

        if(x1 > highest)
          highest = x1;
        if(x2 > highest)
          highest = x2;

        tempTrace = board.addTrace(layers.current, (pos + x1) * scale, (y1) * scale, size, false);
        tempTrace.add((pos + x2) * scale, (y2) * scale);
        tempTrace.group = nextGroup;
      }

      pos += highest + letter.spacing;
    }

    nextGroup++;
  }
 
  // program entry
  public static void main(String[] args)
  {
    // load font
    font = new PCBFont();

    // mouse input
    input = new Input();

    // default grid
    Grid.setInches(.025);

    // pcb board outline
    board = new Board(3.0 * 25.4, 2.0 * 25.4);

    // create window
    JFrame frame = new JFrame("SimplePCB");
    frame.setLayout(new BorderLayout());
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.setPreferredSize(new Dimension(800, 600));
    frame.setMinimumSize(new Dimension(800, 600));
    frame.setResizable(true);
    frame.setFocusable(true);

    // menu
    MainMenu menu = new MainMenu();
    frame.setJMenuBar(menu);

    // top toolbar
    topbar = new TopBar(); 
    frame.add(topbar, BorderLayout.NORTH);

    // left toolbar
    tools = new Tools(); 
    frame.add(tools, BorderLayout.WEST);

    // right toolbar
    layers = new Layers(); 
    frame.add(layers, BorderLayout.EAST);

    // main viewport
    panel = new RenderPanel();

    panel.addMouseListener(input);
    panel.addMouseMotionListener(input);
    panel.addMouseWheelListener(input);
    //panel.addKeyListener(input);
    panel.setFocusable(true);

    // misc
    tools.setMode(0);

    // key bindings
    panel.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_DELETE, 0), "key_delete");
    panel.getActionMap().put("key_delete",
      new AbstractAction()
      {
        public void actionPerformed(ActionEvent e)
        {
          int i;

          switch(toolMode)
          {
            case 0:
              // delete selection
              for(i = 0; i < board.max; i++)
              {
                if(board.trace[i].selected)
                { 
                  board.trace[i].status = false;
                  board.trace[i].group = -1;
                }

                if(board.pad[i].selected)
                { 
                  board.pad[i].status = false;
                  board.pad[i].group = -1;
                }
              }

              // delete trace
              if(selectedTrace != null)
              {
                selectedTrace.status = false;
                selectedTrace.length = 0;
              }

              // delete pad
              if(selectedPad != null)
              {
                selectedPad.status = false;
              }

              unselectAll();
              break;
            case 1:
            case 4:
              // delete vertex
              if(selectedTrace != null)
              {
                selectedTrace.delete(selectedTrace.selectedVertex);
              }
              break;
          }

          panel.repaint();
        }
      } );

    panel.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_R, 0), "key_r");
    panel.getActionMap().put("key_r",
      new AbstractAction()
      {
        public void actionPerformed(ActionEvent e)
        {
          rotate();
        }
      } );

    panel.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_F, 0), "key_f");
    panel.getActionMap().put("key_f",
      new AbstractAction()
      {
        public void actionPerformed(ActionEvent e)
        {
          flip();
        }
      } );

    panel.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_M, 0), "key_m");
    panel.getActionMap().put("key_m",
      new AbstractAction()
      {
        public void actionPerformed(ActionEvent e)
        {
          mirror();
        }
      } );

    panel.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), "key_escape");
    panel.getActionMap().put("key_escape",
      new AbstractAction()
      {
        public void actionPerformed(ActionEvent e)
        {
          tools.setMode(0);
        }
      } );

    frame.add(panel, BorderLayout.CENTER);

    frame.pack();
    frame.setVisible(true);
    win = (Frame)frame;

    int i, j;

    // main loop
    while(true)
    {
      sleep();
      updateMouse();

      // zoom in
      if(input.wheelup)
      {
        input.wheelup = false;
        double oldzoom = zoom;
        zoom += Grid.mag;

        if(zoom > (Grid.mag * 16))
          zoom = (Grid.mag * 16);

        double zoomlevel = (double)zoom / oldzoom;
        ox -= (int)((double)(mousex - ox) * (zoomlevel - 1)); 
        oy -= (int)((double)(mousey - oy) * (zoomlevel - 1)); 

        panel.repaint();
        continue;
      }

      // zoom out
      if(input.wheeldown)
      {
        input.wheeldown = false;
        double oldzoom = zoom;
        zoom -= Grid.mag;

        if(zoom < Grid.mag)
          zoom = Grid.mag;

        double zoomlevel = (double)zoom / oldzoom;
        ox -= (int)((double)(mousex - ox) * (zoomlevel - 1)); 
        oy -= (int)((double)(mousey - oy) * (zoomlevel - 1)); 

        panel.repaint();
        continue;
      }

      // pan view
      if(input.button2)
      {
        int last_ox = mousex - ox;
        int last_oy = mousey - oy;

        while(input.button2)
        {
          ox = input.mousex - last_ox;
          oy = input.mousey - last_oy;

          panel.repaint();

          sleep();
        }
        continue;
      }

      // continue trace
      if(currentTrace != null)
      {
        panel.repaint();
              
        // next segment
        if(input.button1)
        {
          input.button1 = false;
          currentTrace.add(gridx, gridy);
          panel.repaint();

          continue;
        } 

        // done
        if(input.button3)
        {
          input.button3 = false;

          // turn completed trace off if no length
          if( (currentTrace.filled && currentTrace.length < 3)
           || (!currentTrace.filled && currentTrace.length < 2) )
          {
            selectedTrace = null;
            currentTrace.length = 0;
            currentTrace.status = false;
            currentTrace.group = -1;
            currentTrace = null;
          }
          else
          {
            currentTrace.selected = true;
            selectedTrace = currentTrace;
            currentTrace = null;
          }

          panel.repaint();
        }
        continue;
      }

      // double click
      if(input.doubleclicked)
      {
        switch(toolMode)
        {
          case 1:
          case 4:
            // add vertex
            if(selectedTrace != null)
            {
              for(i = 1; i < selectedTrace.length; i++)
              {
                double x1 = selectedTrace.x[i];
                double x2 = selectedTrace.x[i - 1];
                double y1 = selectedTrace.y[i];
                double y2 = selectedTrace.y[i - 1];

                if(pointOnLine(x, y, x1, y1, x2, y2, 1.0))
                {
                  selectedTrace.insert(i, gridx, gridy);
                  selectedTrace.selectedVertex = i;
                  panel.repaint();
                  break;
                }
              }

              if(selectedTrace.filled)
              {
                double x1 = selectedTrace.x[0];
                double x2 = selectedTrace.x[selectedTrace.length - 1];
                double y1 = selectedTrace.y[0];
                double y2 = selectedTrace.y[selectedTrace.length - 1];

                if(pointOnLine(x, y, x1, y1, x2, y2, 1.0))
                {
                  selectedTrace.insert(i, gridx, gridy);
                  selectedTrace.selectedVertex = i;
                  panel.repaint();
                }
              }
            }
            break;
        }

        input.doubleclicked = false;
      }

      // left button click
      if(input.button1)
      {
        switch(toolMode)
        {
          case 0:
            unselectAll();

            // select trace
            for(i = 0; i < board.max; i++)
            {
              if(board.trace[i].status)
              {
                if(board.trace[i].filled)
                {
                  // polygon
                  if(pointInPoly(board.trace[i], x, y))
                  {
                    unselectAll();
                    selectedTrace = board.trace[i];
                  }
                }
                else
                {
                  // trace
                  for(j = 1; j < board.trace[i].length; j++)
                  {
                    double x1 = board.trace[i].x[j];
                    double x2 = board.trace[i].x[j - 1];
                    double y1 = board.trace[i].y[j];
                    double y2 = board.trace[i].y[j - 1];

                    if(pointOnLine(x, y, x1, y1, x2, y2, board.trace[i].size))
                    {
                      unselectAll();
                      selectedTrace = board.trace[i];
                      break;
                    }
                  }
                }

                if(selectedTrace != null)
                {
                  if(input.shiftdown)
                  {
                    input.shiftdown = false;
                    selectedTrace.selected = true;

                    if(selectedTrace.group != -1)
                      addGroupToSelection(selectedTrace.group);
                  }
                  else if(!selectedTrace.selected)
                  {
                    cancelSelection();
                    selectedTrace.selected = true;

                    if(selectedTrace.group != -1)
                      addGroupToSelection(selectedTrace.group);
                  }

                  break;
                }
              }
            }

            // select pad
            for(i = 0; i < board.max; i++)
            {
              double px = x - board.pad[i].x;
              double py = y - board.pad[i].y;
              double pr = board.pad[i].outerSize / 2;
              
              if(board.pad[i].status && pointInCircle(px, py, pr))
              {
                unselectAll();
                selectedPad = board.pad[i];

                if(input.shiftdown)
                {
                  input.shiftdown = false;
                  selectedPad.selected = true;

                  if(selectedPad.group != -1)
                    addGroupToSelection(selectedPad.group);
                }
                else if(!selectedPad.selected)
                {
                  cancelSelection();
                  selectedPad.selected = true;

                  if(selectedPad.group != -1)
                    addGroupToSelection(selectedPad.group);
                }

                break;
              }
            }

            // begin rubber band select
            if(selectedPad == null && selectedTrace == null)
            {
              selectRectStatus = true;
              selectRectX1 = mousex; 
              selectRectY1 = mousey; 

              boolean cancel = true;

              while(input.button1)
              {
                updateMouse();
                selectRectX2 = mousex; 
                selectRectY2 = mousey; 
                panel.repaint();
                sleep();
              }

              selectRectStatus = false;

              double x1 = (double)(SimplePCB.selectRectX1 - ox) / zoom;
              double y1 = (double)(SimplePCB.selectRectY1 - oy) / zoom;
              double x2 = (double)(SimplePCB.selectRectX2 - ox) / zoom;
              double y2 = (double)(SimplePCB.selectRectY2 - oy) / zoom;

              if(x1 > x2)
              {
                double temp = x1;
                x1 = x2;
                x2 = temp;
              }

              if(y1 > y2)
              {
                double temp = y1;
                y1 = y2;
                y2 = temp;
              }

              if(input.shiftdown)
              {
                // add to current selection
                for(i = 0; i < board.max; i++)
                {
                  Trace trace = board.trace[i];

                  if(trace.status)
                  {
                    if(trace.inbox(x1, y1, x2, y2))
                    {
                      trace.selected = true;
                      if(trace.group != -1)
                        addGroupToSelection(trace.group);
                      cancel = false;
                    }
                  }
                }

                for(i = 0; i < board.max; i++)
                {
                  Pad pad = board.pad[i];
                   if(pad.status)
                  {
                    if(pad.inbox(x1, y1, x2, y2))
                    {
                      pad.selected = true;
                      if(pad.group != -1)
                        addGroupToSelection(pad.group);
                      cancel = false;
                    }
                  }
                }
              }
              else
              {
                // add to new selection
                cancelSelection();

                for(i = 0; i < board.max; i++)
                {
                  Trace trace = board.trace[i];

                  if(trace.status)
                  {
                    if(trace.inbox(x1, y1, x2, y2))
                    {
                      trace.selected = true;
                      if(trace.group != -1)
                        addGroupToSelection(trace.group);
                      cancel = false;
                    }
                  }
                }

                for(i = 0; i < board.max; i++)
                {
                  Pad pad = board.pad[i];

                  if(pad.status)
                  {
                    if(pad.inbox(x1, y1, x2, y2))
                    {
                      pad.selected = true;
                      if(pad.group != -1)
                        addGroupToSelection(pad.group);
                      cancel = false;
                    }
                  }
                }
              }

              if(cancel)
                cancelSelection();
            }
            else
            {
              moveGroup();
            }

            panel.repaint();
            break;
          case 1:
            // edit trace path
            if(selectedTrace != null)
            {
              for(i = 0; i < selectedTrace.length; i++)
              {
                if(pointInCircle(x - selectedTrace.x[i], y - selectedTrace.y[i], 0.5))
                {
                  selectedTrace.selectedVertex = i;

                  double oldgridx = gridx;
                  double oldgridy = gridy;
                  double oldx = selectedTrace.x[i];
                  double oldy = selectedTrace.y[i];
         
                  while(input.button1)
                  {
                    updateMouse();

                    double deltax = gridx - oldgridx;
                    double deltay = gridy - oldgridy;

                    selectedTrace.x[i] = oldx + deltax;
                    selectedTrace.y[i] = oldy + deltay;

                    panel.repaint();
                    sleep();
                  }
                  break;
                }
              }
            }
            break;
          case 2:
            unselectAll();
            board.addPad(layers.current, gridx, gridy, padInnerSize, padOuterSize);
            break;
          case 3:
            // new trace
            if(currentTrace == null)
            {
              unselectAll();
              cancelSelection();
              // add first segment
              currentTrace = board.addTrace(layers.current, gridx, gridy, traceSize, false);
              panel.repaint();
            }
            break;
          case 4:
            // new polygon
            if(currentTrace == null)
            {
              unselectAll();
              // add first segment
              currentTrace = board.addTrace(layers.current, gridx, gridy, traceSize, true);
              panel.repaint();
            }
            break;
        }

        panel.repaint();
        input.button1 = false;
      }
    }
  }
}
