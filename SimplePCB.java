/*
Copyright (c) 2012 Joe Davisson.

This file is part of JavaBrot.

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
  Frame win = null;
  Tools tools = null;
  Layers layers = null;
  RenderPanel panel = null;
  boolean applet = false;

  double traceSize = .02;
  double padInnerSize = .03;
  double padOuterSize = .08;

  int offsetx = 0;
  int offsety = 0;

  Input input;

  Board board = null;

  int zoom = 100;
  Trace currentTrace = null;
  Pad selectedPad = null;
  Trace selectedTrace = null;

  // start standalone version
  public SimplePCB()
  {
  }

  public void about()
  {
    JOptionPane.showMessageDialog(win, "SimplePCB",
      "About", JOptionPane.INFORMATION_MESSAGE);
  }

  public void quit() 
  {
    if(!applet)
    {
      System.exit(0);
    }
  }

  public static void main(String[] args)
  {
    final SimplePCB simplepcb = new SimplePCB();
    simplepcb.input = new Input();

    simplepcb.board = new Board(6.0, 4.0);

    // create window
    JFrame frame = new JFrame("SimplePCB");
    frame.setLayout(new BorderLayout());
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.setPreferredSize(new Dimension(640, 480));
    frame.setMinimumSize(new Dimension(640, 480));
    frame.setResizable(true);
    frame.setFocusable(true);
    MainMenu menu = new MainMenu(simplepcb);
    frame.setJMenuBar(menu);

    simplepcb.tools = new Tools(); 
    frame.add(simplepcb.tools, BorderLayout.WEST);

    simplepcb.layers = new Layers(); 
    frame.add(simplepcb.layers, BorderLayout.EAST);

    simplepcb.panel = new RenderPanel(simplepcb);

    simplepcb.panel.addMouseListener(simplepcb.input);
    simplepcb.panel.addMouseMotionListener(simplepcb.input);
    simplepcb.panel.addMouseWheelListener(simplepcb.input);
    //simplepcb.panel.addKeyListener(simplepcb.input);
    simplepcb.panel.setFocusable(true);

    simplepcb.panel.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_DELETE, 0), "key_delete");
    simplepcb.panel.getActionMap().put("key_delete",
      new AbstractAction()
      {
        @Override
        public void actionPerformed(ActionEvent e)
        {
System.out.println("got here");
          switch(simplepcb.tools.mode)
          {
            case 0:
              // delete trace
              if(simplepcb.selectedTrace != null)
              {
                simplepcb.selectedTrace.status = false;
                simplepcb.selectedTrace.length = 0;
              }
              // delete pad
              if(simplepcb.selectedPad != null)
              {
                simplepcb.selectedPad.status = false;
              }
              break;
            case 1:
              // delete vertex
              if(simplepcb.selectedTrace != null)
              {
                simplepcb.selectedTrace.delete(simplepcb.selectedTrace.selectedVertex);
              }
              break;
          }

          simplepcb.panel.repaint();
        }
      } );

    frame.add(simplepcb.panel, BorderLayout.CENTER);

    frame.pack();
    frame.setVisible(true);
    simplepcb.win = (Frame)frame;

    simplepcb.currentTrace = null;

    int i, j;

    while(true)
    {
      int mousex = simplepcb.input.mousex;
      int mousey = simplepcb.input.mousey;
      int ox = simplepcb.offsetx;
      int oy = simplepcb.offsety;
      int zoom = simplepcb.zoom;
      double x = (double)(mousex - ox) / zoom;
      double y = (double)(mousey - oy) / zoom;
      double gridx = (float)((int)((x + .025) * 20)) / 20;
      double gridy = (float)((int)((y + .025) * 20)) / 20;

      // zoom in
      if(simplepcb.input.wheelup)
      {
        simplepcb.input.wheelup = false;
        simplepcb.zoom += 100;

        if(simplepcb.zoom > (100 * 8))
          simplepcb.zoom = (100 * 8);
        simplepcb.panel.repaint();

        continue;
      }

      // zoom out
      if(simplepcb.input.wheeldown)
      {
        simplepcb.input.wheeldown = false;
        simplepcb.zoom -= 100;

        if(simplepcb.zoom < 100)
          simplepcb.zoom = 100;
        simplepcb.panel.repaint();

        continue;
      }

      // pan view
      if(simplepcb.input.button2)
      {
        int last_offsetx = mousex - ox;
        int last_offsety = mousey - oy;

        while(simplepcb.input.button2)
        {
          simplepcb.offsetx = simplepcb.input.mousex - last_offsetx;
          simplepcb.offsety = simplepcb.input.mousey - last_offsety;

          simplepcb.panel.repaint();

          simplepcb.sleep();
        }

        continue;
      }

      // continue trace
      if(simplepcb.currentTrace != null)
      {
        simplepcb.panel.repaint();
              
        // next segment
        if(simplepcb.input.button1)
        {
          simplepcb.input.button1 = false;
          simplepcb.currentTrace.add(gridx, gridy);
          simplepcb.panel.repaint();
        } 

        // done
        if(simplepcb.input.button3)
        {
          simplepcb.input.button3 = false;

          // turn completed trace off if no length
          if(simplepcb.currentTrace.length < 2)
          {
            simplepcb.currentTrace.length = 0;
            simplepcb.currentTrace.status = false;
            simplepcb.selectedTrace = null;
          }
          else
          {
            simplepcb.selectedTrace = simplepcb.currentTrace;
          }

          simplepcb.currentTrace = null;
          simplepcb.panel.repaint();
        }

        continue;
      }

      // double click
      if(simplepcb.input.doubleclicked)
      {
        simplepcb.input.doubleclicked = false;

        switch(simplepcb.tools.mode)
        {
          case 1:
            // add vertex
            if(simplepcb.selectedTrace != null)
            {
              for(i = 1; i < simplepcb.selectedTrace.length; i++)
              {
                double x1 = simplepcb.selectedTrace.x[i];
                double x2 = simplepcb.selectedTrace.x[i - 1];
                double y1 = simplepcb.selectedTrace.y[i];
                double y2 = simplepcb.selectedTrace.y[i - 1];

                boolean pointOnLine = true;

                double crossProduct = (y - y1) * (x2 - x1) - (x - x1) * (y2 - y1);
                 if(Math.abs(crossProduct) > simplepcb.selectedTrace.size)
                  pointOnLine = false;
                double dotProduct = (x - x1) * (x2 - x1) + (y - y1) * (y2 - y1);
                if(dotProduct < 0)
                  pointOnLine = false;
                double squaredLength = (x2 - x1) * (x2 - x1) + (y2 - y1) * (y2 - y1);
                if(dotProduct > squaredLength)
                  pointOnLine = false;

                if(pointOnLine)
                {
                  simplepcb.selectedTrace.insert(i, gridx, gridy);
                  simplepcb.selectedTrace.selectedVertex = i;
                  simplepcb.panel.repaint();
                  break;
                }
              }
            }
            break;
        }
      }

      // left button tool
      if(simplepcb.input.button1)
      {
        switch(simplepcb.tools.mode)
        {
          case 0:
            // select trace
            simplepcb.selectedTrace = null;

            for(i = 0; i < simplepcb.board.max; i++)
            {
              if(simplepcb.board.trace[i].status)
              {
                for(j = 1; j < simplepcb.board.trace[i].length; j++)
                {
                  double x1 = simplepcb.board.trace[i].x[j];
                  double x2 = simplepcb.board.trace[i].x[j - 1];
                  double y1 = simplepcb.board.trace[i].y[j];
                  double y2 = simplepcb.board.trace[i].y[j - 1];

                  boolean pointOnLine = true;
                  double crossProduct = (y - y1) * (x2 - x1) - (x - x1) * (y2 - y1);
                  if(Math.abs(crossProduct) > simplepcb.board.trace[i].size)
                    pointOnLine = false;
                  double dotProduct = (x - x1) * (x2 - x1) + (y - y1) * (y2 - y1);
                  if(dotProduct < 0)
                    pointOnLine = false;
                  double squaredLength = (x2 - x1) * (x2 - x1) + (y2 - y1) * (y2 - y1);
                  if(dotProduct > squaredLength)
                    pointOnLine = false;

                  if(pointOnLine)
                  {
                    simplepcb.selectedTrace = simplepcb.board.trace[i];
                    break;
                  }
                }
              }
            }

            // select pad
            simplepcb.selectedPad = null;

            for(i = 0; i < simplepcb.board.max; i++)
            {
              double px = x - simplepcb.board.pad[i].x;
              double py = y - simplepcb.board.pad[i].y;
              double pr = simplepcb.board.pad[i].outerSize / 2;
              
              if(simplepcb.board.pad[i].status && (px * px + py * py < pr * pr))
              {
                simplepcb.selectedPad = simplepcb.board.pad[i];
                simplepcb.selectedTrace = null;
                break;
              }
            }

            simplepcb.panel.repaint();

            // move trace
            if(simplepcb.selectedTrace != null)
            {
              boolean move_trace = false;

              for(i = 1; i < simplepcb.selectedTrace.length; i++)
              {
                double x1 = simplepcb.selectedTrace.x[i];
                double x2 = simplepcb.selectedTrace.x[i - 1];
                double y1 = simplepcb.selectedTrace.y[i];
                double y2 = simplepcb.selectedTrace.y[i - 1];

                boolean pointOnLine = true;

                double crossProduct = (y - y1) * (x2 - x1) - (x - x1) * (y2 - y1);
                 if(Math.abs(crossProduct) > simplepcb.selectedTrace.size)
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
                tempTrace.copy(simplepcb.selectedTrace);

                while(simplepcb.input.button1)
                {
                  mousex = simplepcb.input.mousex;
                  mousey = simplepcb.input.mousey;
                  ox = simplepcb.offsetx;
                  oy = simplepcb.offsety;
                  zoom = simplepcb.zoom;
                  x = (double)(mousex - ox) / zoom;
                  y = (double)(mousey - oy) / zoom;
                  gridx = (float)((int)((x + .025) * 20)) / 20;
                  gridy = (float)((int)((y + .025) * 20)) / 20;

                  double deltax = gridx - oldgridx;
                  double deltay = gridy - oldgridy;

                  for(i = 0; i < simplepcb.selectedTrace.length; i++)
                  {
                    simplepcb.selectedTrace.x[i] = tempTrace.x[i] + deltax;
                    simplepcb.selectedTrace.y[i] = tempTrace.y[i] + deltay;
                  }

                  simplepcb.panel.repaint();
                  simplepcb.sleep();
                }
              }
            }

            // move pad
            if(simplepcb.selectedPad != null)
            {
              double px = x - simplepcb.selectedPad.x;
              double py = y - simplepcb.selectedPad.y;
              double pr = simplepcb.selectedPad.outerSize / 2;
              
              if(simplepcb.selectedPad.status && (px * px + py * py < pr * pr))
              {
                while(simplepcb.input.button1)
                {
                  mousex = simplepcb.input.mousex;
                  mousey = simplepcb.input.mousey;
                  ox = simplepcb.offsetx;
                  oy = simplepcb.offsety;
                  zoom = simplepcb.zoom;
                  x = (double)(mousex - ox) / zoom;
                  y = (double)(mousey - oy) / zoom;
                  gridx = (float)((int)((x + .025) * 20)) / 20;
                  gridy = (float)((int)((y + .025) * 20)) / 20;

                  simplepcb.selectedPad.x = gridx;
                  simplepcb.selectedPad.y = gridy;

                  simplepcb.panel.repaint();
                  simplepcb.sleep();
                }

                simplepcb.panel.repaint();
              }
            }
            break;
          case 1:
            // edit trace
            int use = -1;

            if(simplepcb.selectedTrace != null)
            {
              for(i = 0; i < simplepcb.selectedTrace.length; i++)
              {
                if( (Math.abs(x - simplepcb.selectedTrace.x[i]) < .05) &&
                    (Math.abs(y - simplepcb.selectedTrace.y[i]) < .05) )
                {
                  simplepcb.selectedTrace.selectedVertex = i;

                  while(simplepcb.input.button1)
                  {
                    mousex = simplepcb.input.mousex;
                    mousey = simplepcb.input.mousey;
                    ox = simplepcb.offsetx;
                    oy = simplepcb.offsety;
                    zoom = simplepcb.zoom;
                    x = (double)(mousex - ox) / zoom;
                    y = (double)(mousey - oy) / zoom;
                    gridx = (float)((int)((x + .025) * 20)) / 20;
                    gridy = (float)((int)((y + .025) * 20)) / 20;

                    simplepcb.selectedTrace.x[i] = gridx;
                    simplepcb.selectedTrace.y[i] = gridy;

                    simplepcb.panel.repaint();
                    simplepcb.sleep();
                  }
                }
              }
              simplepcb.panel.repaint();
            }
            break;
          case 2:
            simplepcb.board.addPad(gridx, gridy, simplepcb.padInnerSize, simplepcb.padOuterSize);
            simplepcb.panel.repaint();
            break;
          case 3:
            // new trace
            if(simplepcb.currentTrace == null)
            {
              // add first segment
              simplepcb.currentTrace = simplepcb.board.addTrace(gridx, gridy, simplepcb.traceSize, simplepcb.layers.current);
              simplepcb.panel.repaint();
            }
            break;
          case 4:
            break;
        }

        simplepcb.input.button1 = false;
      }

      simplepcb.sleep();
    }
  }

  private void sleep()
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
}
