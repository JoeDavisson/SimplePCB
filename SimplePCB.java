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
  public static Tools tools = null;
  public static Layers layers = null;
  public static RenderPanel panel = null;

  public static double traceSize = .02;
  public static double padInnerSize = .03;
  public static double padOuterSize = .08;

  public static int offsetx = 0;
  public static int offsety = 0;

  public static Input input;

  public static Board board = null;

  public static int zoom = 100;
  public static Trace currentTrace = null;
  public static Pad selectedPad = null;
  public static Trace selectedTrace = null;
  //public static Group selectionGroup = null;

  public static void about()
  {
    JOptionPane.showMessageDialog(win, "SimplePCB",
      "About", JOptionPane.INFORMATION_MESSAGE);
  }

  public static void quit() 
  {
    System.exit(0);
  }

  private static void sleep()
  {
    // sleep
    try
    {
      Thread.sleep(50);
    }
    catch(InterruptedException e)
    {
    }
  }

  public static void main(String[] args)
  {
    input = new Input();

    board = new Board(6.0, 4.0);

    // create window
    JFrame frame = new JFrame("SimplePCB");
    frame.setLayout(new BorderLayout());
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.setPreferredSize(new Dimension(640, 480));
    frame.setMinimumSize(new Dimension(640, 480));
    frame.setResizable(true);
    frame.setFocusable(true);
    MainMenu menu = new MainMenu();
    frame.setJMenuBar(menu);

    tools = new Tools(); 
    frame.add(tools, BorderLayout.WEST);

    layers = new Layers(); 
    frame.add(layers, BorderLayout.EAST);

    panel = new RenderPanel();

    panel.addMouseListener(input);
    panel.addMouseMotionListener(input);
    panel.addMouseWheelListener(input);
    //panel.addKeyListener(input);
    panel.setFocusable(true);

    panel.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_DELETE, 0), "key_delete");
    panel.getActionMap().put("key_delete",
      new AbstractAction()
      {
        @Override
        public void actionPerformed(ActionEvent e)
        {
System.out.println("got here");
          switch(tools.mode)
          {
            case 0:
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

    currentTrace = null;

    int i, j;

    while(true)
    {
      int mousex = input.mousex;
      int mousey = input.mousey;
      int ox = offsetx;
      int oy = offsety;
      double x = (double)(mousex - ox) / zoom;
      double y = (double)(mousey - oy) / zoom;
      double gridx = (float)((int)((x + .025) * 20)) / 20;
      double gridy = (float)((int)((y + .025) * 20)) / 20;

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
        int last_offsetx = mousex - ox;
        int last_offsety = mousey - oy;

        while(input.button2)
        {
          offsetx = input.mousex - last_offsetx;
          offsety = input.mousey - last_offsety;

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
        input.doubleclicked = false;

        switch(tools.mode)
        {
          case 1:
            // add vertex
            if(selectedTrace != null)
            {
              for(i = 1; i < selectedTrace.length; i++)
              {
                double x1 = selectedTrace.x[i];
                double x2 = selectedTrace.x[i - 1];
                double y1 = selectedTrace.y[i];
                double y2 = selectedTrace.y[i - 1];

                boolean pointOnLine = true;

                double crossProduct = (y - y1) * (x2 - x1) - (x - x1) * (y2 - y1);
                 if(Math.abs(crossProduct) > selectedTrace.size)
                  pointOnLine = false;
                double dotProduct = (x - x1) * (x2 - x1) + (y - y1) * (y2 - y1);
                if(dotProduct < 0)
                  pointOnLine = false;
                double squaredLength = (x2 - x1) * (x2 - x1) + (y2 - y1) * (y2 - y1);
                if(dotProduct > squaredLength)
                  pointOnLine = false;

                if(pointOnLine)
                {
                  selectedTrace.insert(i, gridx, gridy);
                  selectedTrace.selectedVertex = i;
                  panel.repaint();
                  break;
                }
              }
            }
            break;
        }
      }

      // left button tool
      if(input.button1)
      {
        switch(tools.mode)
        {
          case 0:
            // select trace
            selectedTrace = null;

            for(i = 0; i < board.max; i++)
            {
              if(board.trace[i].status)
              {
                for(j = 1; j < board.trace[i].length; j++)
                {
                  double x1 = board.trace[i].x[j];
                  double x2 = board.trace[i].x[j - 1];
                  double y1 = board.trace[i].y[j];
                  double y2 = board.trace[i].y[j - 1];

                  boolean pointOnLine = true;
                  double crossProduct = (y - y1) * (x2 - x1) - (x - x1) * (y2 - y1);
                  if(Math.abs(crossProduct) > board.trace[i].size)
                    pointOnLine = false;
                  double dotProduct = (x - x1) * (x2 - x1) + (y - y1) * (y2 - y1);
                  if(dotProduct < 0)
                    pointOnLine = false;
                  double squaredLength = (x2 - x1) * (x2 - x1) + (y2 - y1) * (y2 - y1);
                  if(dotProduct > squaredLength)
                    pointOnLine = false;

                  if(pointOnLine)
                  {
                    selectedTrace = board.trace[i];
                    break;
                  }
                }
              }
            }

            // select pad
            selectedPad = null;

            for(i = 0; i < board.max; i++)
            {
              double px = x - board.pad[i].x;
              double py = y - board.pad[i].y;
              double pr = board.pad[i].outerSize / 2;
              
              if(board.pad[i].status && (px * px + py * py < pr * pr))
              {
                selectedPad = board.pad[i];
                selectedTrace = null;
                break;
              }
            }

            // add to group
/*
            if(input.shifted)
            {
              if(selectedTrace != null)
                selectedTrace.group = currentGroup;

              if(selectedPad != null)
                selectedPad.group = currentPad;

              break;
            }
*/

            // move trace
            if(selectedTrace != null)
            {
              boolean move_trace = false;

              for(i = 1; i < selectedTrace.length; i++)
              {
                double x1 = selectedTrace.x[i];
                double x2 = selectedTrace.x[i - 1];
                double y1 = selectedTrace.y[i];
                double y2 = selectedTrace.y[i - 1];

                boolean pointOnLine = true;

                double crossProduct = (y - y1) * (x2 - x1) - (x - x1) * (y2 - y1);
                 if(Math.abs(crossProduct) > selectedTrace.size)
                  pointOnLine = false;
                double dotProduct = (x - x1) * (x2 - x1) + (y - y1) * (y2 - y1);
                if(dotProduct < 0)
                  pointOnLine = false;
                double squaredLength = (x2 - x1) * (x2 - x1) + (y2 - y1) * (y2 - y1);
                if(dotProduct > squaredLength)
                  pointOnLine = false;

                if(pointOnLine)
                {
                  move_trace = true;
                  break;
                }
              }

              if(move_trace)
              {
                double oldgridx = gridx;
                double oldgridy = gridy;
         
                Trace tempTrace = new Trace(0, 0);
                tempTrace.copy(selectedTrace);

                while(input.button1)
                {
                  mousex = input.mousex;
                  mousey = input.mousey;
                  ox = offsetx;
                  oy = offsety;
                  x = (double)(mousex - ox) / zoom;
                  y = (double)(mousey - oy) / zoom;
                  gridx = (float)((int)((x + .025) * 20)) / 20;
                  gridy = (float)((int)((y + .025) * 20)) / 20;

                  double deltax = gridx - oldgridx;
                  double deltay = gridy - oldgridy;

                  for(i = 0; i < selectedTrace.length; i++)
                  {
                    selectedTrace.x[i] = tempTrace.x[i] + deltax;
                    selectedTrace.y[i] = tempTrace.y[i] + deltay;
                  }

                  panel.repaint();
                  sleep();
                }
              }
            }

            // move pad
            if(selectedPad != null)
            {
              double px = x - selectedPad.x;
              double py = y - selectedPad.y;
              double pr = selectedPad.outerSize / 2;
              
              if(selectedPad.status && (px * px + py * py < pr * pr))
              {
                while(input.button1)
                {
                  mousex = input.mousex;
                  mousey = input.mousey;
                  ox = offsetx;
                  oy = offsety;
                  x = (double)(mousex - ox) / zoom;
                  y = (double)(mousey - oy) / zoom;
                  gridx = (float)((int)((x + .025) * 20)) / 20;
                  gridy = (float)((int)((y + .025) * 20)) / 20;

                  selectedPad.x = gridx;
                  selectedPad.y = gridy;

                  panel.repaint();
                  sleep();
                }

                panel.repaint();
              }
            }

            panel.repaint();
            break;
          case 1:
            // edit trace
            int use = -1;

            if(selectedTrace != null)
            {
              for(i = 0; i < selectedTrace.length; i++)
              {
                if( (Math.abs(x - selectedTrace.x[i]) < .05) &&
                    (Math.abs(y - selectedTrace.y[i]) < .05) )
                {
                  selectedTrace.selectedVertex = i;

                  while(input.button1)
                  {
                    mousex = input.mousex;
                    mousey = input.mousey;
                    ox = offsetx;
                    oy = offsety;
                    x = (double)(mousex - ox) / zoom;
                    y = (double)(mousey - oy) / zoom;
                    gridx = (float)((int)((x + .025) * 20)) / 20;
                    gridy = (float)((int)((y + .025) * 20)) / 20;

                    selectedTrace.x[i] = gridx;
                    selectedTrace.y[i] = gridy;

                    panel.repaint();
                    sleep();
                  }
                }
              }
              panel.repaint();
            }
            break;
          case 2:
            board.addPad(gridx, gridy, padInnerSize, padOuterSize);
            panel.repaint();
            break;
          case 3:
            // new trace
            if(currentTrace == null)
            {
              // add first segment
              currentTrace = board.addTrace(gridx, gridy, traceSize, layers.current);
              panel.repaint();
            }
            break;
          case 4:
            break;
        }

        input.button1 = false;
      }

      sleep();
    }
  }
}
