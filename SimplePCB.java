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

  public static Input input;

  public static Board board = null;

  public static int zoom = 100;
  public static Trace currentTrace = null;
  public static Pad selectedPad = null;
  public static Trace selectedTrace = null;

  public static int mousex, mousey;
  public static int ox, oy;
  public static double x, y;
  public static double gridx, gridy;

  public static int currentGroup = 0;
  public static int nextGroup = 0;
  public static boolean groupStarted = false;

  public static int toolMode = 0;

  // about dialog
  public static void about()
  {
    JOptionPane.showMessageDialog(win, "SimplePCB",
      "About", JOptionPane.INFORMATION_MESSAGE);
  }

  // save
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

  // quit
  public static void quit() 
  {
    System.exit(0);
  }

  // sleep
  private static void sleep()
  {
    try
    {
      Thread.sleep(50);
    }
    catch(InterruptedException e)
    {
    }
  }

  // change tool
  public static void setToolMode(int mode)
  {
    toolMode = mode;
    cancelGroup();
    panel.repaint();
  }

  // mouse related math
  public static void updateMouse()
  {
    mousex = input.mousex;
    mousey = input.mousey;
    x = (double)(mousex - ox) / zoom;
    y = (double)(mousey - oy) / zoom;
    gridx = (float)((int)((x + .0125) * 40)) / 40;
    gridy = (float)((int)((y + .0125) * 40)) / 40;
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

  public static void cancelGroup()
  {
    // remove objects from uncompleted group
    int i;

    for(i = 0; i < board.max; i++)
    {
      if(board.trace[i].group == nextGroup)
        board.trace[i].group = -1;

      if(board.pad[i].group == nextGroup)
        board.pad[i].group = -1;
    }

    groupStarted = false;
    currentGroup = nextGroup;
  }

  // finish group
  public static void finishGroup()
  {
    nextGroup++;
    currentGroup = nextGroup;
    groupStarted = false;
  }

  public static void unGroup()
  {
    int i;

    for(i = 0; i < board.max; i++)
    {
      if(board.pad[i].group == currentGroup)
        board.pad[i].group = -1;

      if(board.trace[i].group == currentGroup)
        board.trace[i].group = -1;
    }

    currentGroup = nextGroup;
    groupStarted = false;
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
        if(board.pad[i].status && board.pad[i].group == currentGroup)
        {
          board.pad[i].x += deltax;
          board.pad[i].y += deltay;
        }

        if(board.trace[i].status && board.trace[i].group == currentGroup)
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

  // program entry
  public static void main(String[] args)
  {
    // mouse input
    input = new Input();

    // pcb board outline
    board = new Board(6.0, 4.0);

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
              // delete group
              for(i = 0; i < board.max; i++)
              {
                if(board.trace[i].group == currentGroup)
                { 
                  board.trace[i].status = false;
                  board.trace[i].group = -1;
                }

                if(board.pad[i].group == currentGroup)
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
        zoom += 100;

        if(zoom > (100 * 8))
          zoom = (100 * 8);

        panel.repaint();
        continue;
      }

      // zoom out
      if(input.wheeldown)
      {
        input.wheeldown = false;
        zoom -= 100;

        if(zoom < 100)
          zoom = 100;

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
          if(currentTrace.length < 2)
          {
            currentTrace.length = 0;
            currentTrace.status = false;
            currentTrace.group = -1;
            selectedTrace = null;
          }
          else
          {
            selectedTrace = currentTrace;
          }

          currentTrace = null;
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

                if(pointOnLine(x, y, x1, y1, x2, y2, .025))
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

                if(pointOnLine(x, y, x1, y1, x2, y2, .025))
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
                  // add to group
                  if((!groupStarted || input.shiftdown)
                      && selectedTrace.group == -1)
                  {
                    input.shiftdown = false;
                    selectedTrace.group = nextGroup;
                    currentGroup = nextGroup;
                    groupStarted = true;
                  }
                  else if(selectedTrace.group == -1)
                  {
                    cancelGroup();
                    selectedTrace.group = nextGroup;
                    currentGroup = nextGroup;
                    groupStarted = true;
                  }
                  else if(selectedTrace.group != -1 && selectedTrace.group != currentGroup)
                  {
                    cancelGroup();
                    currentGroup = selectedTrace.group;
                    groupStarted = false;
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

                // add to group
                if((!groupStarted || input.shiftdown)
                    && selectedPad.group == -1)
                {
                  input.shiftdown = false;
                  selectedPad.group = nextGroup;
                  currentGroup = nextGroup;
                  groupStarted = true;
                }
                else if(selectedPad.group == -1)
                {
                  cancelGroup();
                  selectedPad.group = nextGroup;
                  currentGroup = nextGroup;
                  groupStarted = true;
                }
                else if(selectedPad.group != -1 && selectedPad.group != currentGroup)
                {
                  cancelGroup();
                  currentGroup = selectedPad.group;
                  groupStarted = false;
                }
                break;
              }
            }

            if(selectedPad == null && selectedTrace == null)
              cancelGroup();
            else
              moveGroup();

            panel.repaint();
            break;
          case 1:
            // edit trace path
            if(selectedTrace != null)
            {
              for(i = 0; i < selectedTrace.length; i++)
              {
                if(pointInCircle(x - selectedTrace.x[i], y - selectedTrace.y[i], 0.025))
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
            board.addPad(gridx, gridy, padInnerSize, padOuterSize);
            break;
          case 3:
            // new trace
            if(currentTrace == null)
            {
              unselectAll();
              // add first segment
              currentTrace = board.addTrace(gridx, gridy, traceSize, layers.current, false);
              panel.repaint();
            }
            break;
          case 4:
            // new polygon
            if(currentTrace == null)
            {
              unselectAll();
              // add first segment
              currentTrace = board.addTrace(gridx, gridy, traceSize, layers.current, true);
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
