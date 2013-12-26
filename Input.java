// Copyright (c) 2012 Joe Davisson. All Rights Reserved.

import java.awt.event.*;

public class Input implements KeyListener, MouseListener, MouseMotionListener
{
  // access through methods
  private boolean keyList[];

  // access directly
  public int mousex = 0;
  public int mousey = 0;
  public boolean button1;
  public boolean button2;
  public boolean button3;
  public boolean dragged;
  public boolean keydown;
  public int lastkey = 0;

  private boolean initialized = false;

  public Input()
  {
    // only initialize everything once
    if(initialized)
      return;

    keyList = new boolean[65536];

    int i;

    for(i = 0; i < 65536; i++)
      keyList[i] = false;

    mousex = 0;
    mousey = 0;
    button1 = false;
    button2 = false;
    button3 = false;
    dragged = false;
    keydown = false;
    lastkey = 0;

    // initialization complete
    initialized = true;
  }

  public boolean getKey(int num)
  {
     if(num >= 0 && num < 65536)
       return keyList[num];

     return false;
  }

  public void setKey(int num, boolean status)
  {
     if(num >= 0 && num < 65536)
       keyList[num] = status;
  }

  // keyListener
  public void keyPressed(KeyEvent e)
  {
    int key = e.getKeyCode();
    if(key >= 0 && key < 65536)
      keyList[key] = true;
    keydown = true;
    lastkey = key;
  }

  public void keyReleased(KeyEvent e)
  {
    int key = e.getKeyCode();
    if(key >= 0 && key < 65536)
      keyList[key] = false;
  }

  public void keyTyped(KeyEvent e)
  {
  }

  // mouseListener
  public void mousePressed(MouseEvent e)
  {
    if(e.getButton() == MouseEvent.BUTTON1)
      button1 = true;
    if(e.getButton() == MouseEvent.BUTTON2)
      button2 = true;
    if(e.getButton() == MouseEvent.BUTTON3)
      button3 = true;
  }

  public void mouseReleased(MouseEvent e)
  {
    if(e.getButton() == MouseEvent.BUTTON1)
      button1 = false;
    if(e.getButton() == MouseEvent.BUTTON2)
      button2 = false;
    if(e.getButton() == MouseEvent.BUTTON3)
      button3 = false;
  }

  public void mouseEntered(MouseEvent e)
  {
    mouseMoved(e);
  }

  public void mouseExited(MouseEvent e)
  {
    mouseMoved(e);
  }

  public void mouseClicked(MouseEvent e)
  {
  }

  // mouseMotionListener
  public void mouseMoved(MouseEvent e)
  {
    mousex = (e.getX() - Screen.xpos) / Screen.scale;
    mousey = (e.getY() - Screen.ypos) / Screen.scale;

    if(mousex < 0)
      mousex = 0;
    if(mousey < 0)
      mousey = 0;
    if(mousex > Screen.w - 1)
      mousex = Screen.w - 1;
    if(mousey > Screen.h - 1)
      mousey = Screen.h - 1;
  }

  public void mouseDragged(MouseEvent e)
  {
    dragged = true;
    mouseMoved(e);
  }
}

