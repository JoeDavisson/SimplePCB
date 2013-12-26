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
import javax.swing.*;
import javax.swing.text.*;
import java.io.*;
import java.net.*;

public class SimplePCBApplet extends JApplet
{
  public static Frame getParentFrame(Component child)
  {
    Container c = child.getParent();

    while(c != null)
    {
      if(c instanceof Frame)
      {
        return (Frame)c;
      }
      c = c.getParent();
    }

    return null;
  }
  // start applet version
  public void init()
  {
    // size taken from applet tag in html
    Dimension dim = getSize();

    // create client
    SimplePCB simplepcb = new SimplePCB();

    // main window
    simplepcb.win = getParentFrame(getContentPane());

    // add main window to applet
    add(simplepcb.panel);

    // add menu to applet
    MainMenu menu = new MainMenu(simplepcb);
    setJMenuBar(menu);

    // report if applet
    simplepcb.applet = true;
  }
}
