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
  double padInnerSize = .02;
  double padOuterSize = .04;

  Input input;

  //public BufferedImage bi;

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
/*
    final int w = 1024;
    final int h = 768;
*/

    final SimplePCB simplepcb = new SimplePCB();

/*
    simplepcb.backbuf = new Bitmap(w, h);
    simplepcb.backbuf.clear(Col.makeRGB(0, 0, 0));
    simplepcb.backbuf.rectfill(0, 0, 64, 64, Col.makeRGB(255, 0, 0), 0);
    DataBuffer data_buffer = new DataBufferInt(simplepcb.backbuf.data, w * h);
    int band_masks[] = { 0xFF0000, 0xFF00, 0xFF, 0xFF000000 };
    WritableRaster write_raster =
      Raster.createPackedRaster(data_buffer, w, h, w, band_masks, null);
    ColorModel color_model = ColorModel.getRGBdefault();
    simplepcb.bi = new BufferedImage(color_model, write_raster, false, null);
*/

    // input class needs these set
/*
    Screen.w = w;
    Screen.h = h;
*/

    JFrame frame = new JFrame("SimplePCB");
    frame.setLayout(new BorderLayout());
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.setPreferredSize(new Dimension(640, 480));
    frame.setMinimumSize(new Dimension(640, 480));
    frame.setResizable(true);
    MainMenu menu = new MainMenu(simplepcb);
    frame.setJMenuBar(menu);

    simplepcb.tools = new Tools(); 
    frame.add(simplepcb.tools, BorderLayout.WEST);

    simplepcb.layers = new Layers(); 
    frame.add(simplepcb.layers, BorderLayout.EAST);

/*
    simplepcb.panel = new JPanel()
    {
      public void paintComponent(Graphics g)
      {
        Dimension d = simplepcb.panel.getSize();

        g.setColor(Color.BLACK);
        g.fillRect(0, 0, d.width, d.height);
        //g.drawImage(simplepcb.bi, 0, 0, w, h, null);
      }
    };
*/

/*
    simplepcb.panel.setSize(640, 480);
    simplepcb.panel.setPreferredSize(new Dimension(640, 480));
    simplepcb.panel.setLayout(new BorderLayout());
*/
    simplepcb.panel = new RenderPanel();
    frame.add(simplepcb.panel, BorderLayout.CENTER);

    simplepcb.input = new Input();
    simplepcb.panel.addKeyListener(simplepcb.input);
    simplepcb.panel.addMouseListener(simplepcb.input);
    simplepcb.panel.addMouseMotionListener(simplepcb.input);
    simplepcb.panel.setFocusable(true);

    frame.pack();
    frame.setVisible(true);
    simplepcb.win = (Frame)frame;
  }
}
