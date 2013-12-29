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

  int zoom = 64;

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

    while(true)
    {
      // zoom in
      if(simplepcb.input.wheelup)
      {
        simplepcb.input.wheelup = false;
        simplepcb.zoom += 64;
        if(simplepcb.zoom > (64 * 8))
          simplepcb.zoom = (64 * 8);
        simplepcb.panel.repaint();
      }

      // zoom out
      if(simplepcb.input.wheeldown)
      {
        simplepcb.input.wheeldown = false;
        simplepcb.zoom -= 64;
        if(simplepcb.zoom < 64)
          simplepcb.zoom = 64;
        simplepcb.panel.repaint();
      }

      // move
      int last_offsetx = simplepcb.input.mousex - simplepcb.offsetx;
      int last_offsety = simplepcb.input.mousey - simplepcb.offsety;

      while(simplepcb.input.button2)
      {
        simplepcb.offsetx = simplepcb.input.mousex - last_offsetx;
        simplepcb.offsety = simplepcb.input.mousey - last_offsety;

        simplepcb.panel.repaint();

        simplepcb.sleep();
      }

      // tool
      if(simplepcb.input.button1)
      {
        simplepcb.input.button1 = false;

        int zoom = simplepcb.zoom;
        int ox = simplepcb.offsetx;
        int oy = simplepcb.offsety;
 
        double x = (double)(simplepcb.input.mousex - ox) / zoom;
        double y = (double)(simplepcb.input.mousey - oy) / zoom;

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
            // add first segment
            Trace trace = simplepcb.board.addTrace(x, y, simplepcb.traceSize);
            simplepcb.panel.repaint();
            while(true)
            {
            // next segment
              if(simplepcb.input.button1)
              {
                x = (double)(simplepcb.input.mousex - ox) / zoom;
                y = (double)(simplepcb.input.mousey - oy) / zoom;

                simplepcb.input.button1 = false;
                trace.add(x, y);
                simplepcb.panel.repaint();
              } 
              // done
              if(simplepcb.input.button3)
                break; 

              simplepcb.sleep();
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
