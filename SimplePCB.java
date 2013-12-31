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

  double traceSize = .0625;
  double padInnerSize = .0625;
  double padOuterSize = .1875;

  int offsetx = 0;
  int offsety = 0;

  Input input;

  Board board = null;

  int zoom = 100;
  Trace currentTrace = null;

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
    frame.add(simplepcb.panel, BorderLayout.CENTER);

    simplepcb.panel.setFocusable(true);
    simplepcb.panel.addKeyListener(simplepcb.input);
    simplepcb.panel.addMouseListener(simplepcb.input);
    simplepcb.panel.addMouseMotionListener(simplepcb.input);
    simplepcb.panel.addMouseWheelListener(simplepcb.input);

    frame.pack();
    frame.setVisible(true);
    simplepcb.win = (Frame)frame;

    boolean trace_started = false;

    while(true)
    {
      int mousex = simplepcb.input.mousex;
      int mousey = simplepcb.input.mousey;
      int ox = simplepcb.offsetx;
      int oy = simplepcb.offsety;
      int zoom = simplepcb.zoom;
      double x = (double)(mousex - ox) / zoom;
      double y = (double)(mousey - oy) / zoom;
      x = (float)((int)(x * 20)) / 20;
      y = (float)((int)(y * 20)) / 20;

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
      if(trace_started)
      {
        simplepcb.panel.repaint();
              
        // next segment
        if(simplepcb.input.button1)
        {
          simplepcb.input.button1 = false;
          simplepcb.currentTrace.add(x, y);
          simplepcb.panel.repaint();
        } 

        // done
        if(simplepcb.input.button3)
        {
          simplepcb.input.button3 = false;
          simplepcb.currentTrace = null;
          simplepcb.panel.repaint();
          trace_started = false; 
        }

        continue;
      }

      // tool
      if(simplepcb.input.button1)
      {
        simplepcb.input.button1 = false;

        switch(simplepcb.tools.mode)
        {
          case 0:
            break;
          case 1:
            break;
          case 2:
            simplepcb.board.addPad(x, y, simplepcb.padInnerSize, simplepcb.padOuterSize);
            simplepcb.panel.repaint();
            break;
          case 3:
            if(trace_started == false)
            {
              // add first segment
              simplepcb.currentTrace = simplepcb.board.addTrace(x, y, simplepcb.traceSize);
              simplepcb.panel.repaint();
              trace_started = true;
            }
            break;
          case 4:
            break;
        }
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
