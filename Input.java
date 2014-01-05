// Copyright (c) 2012 Joe Davisson. All Rights Reserved.

import java.awt.event.*;

public class Input implements KeyListener, MouseListener, MouseMotionListener, MouseWheelListener
{
  // access through methods
  private boolean keyList[];

  // access directly
  public int mousex = 0;
  public int mousey = 0;
  public boolean moved;
  public boolean dragged;
  public boolean button1;
  public boolean button2;
  public boolean button3;
  public boolean clicked;
  public boolean doubleclicked;
  public boolean wheelup;
  public boolean wheeldown;
  public boolean keydown;
  public boolean shiftdown;
  public int lastkey = 0;

  private boolean initialized = false;

  public Input()
  {
    keyList = new boolean[65536];

    int i;

    for(i = 0; i < 65536; i++)
      keyList[i] = false;

    mousex = 0;
    mousey = 0;
    moved = false;
    dragged = false;
    button1 = false;
    button2 = false;
    button3 = false;
    clicked = false;
    doubleclicked = false;
    wheelup = false;
    wheeldown = false;
    keydown = false;
    shiftdown = false;
    lastkey = 0;
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
    if(e.getButton() == MouseEvent.BUTTON1 && e.getClickCount() == 1)
      clicked = true;

    if(e.getButton() == MouseEvent.BUTTON1 && e.getClickCount() == 2)
      doubleclicked = true;

    if((e.getModifiersEx() & InputEvent.SHIFT_DOWN_MASK) == InputEvent.SHIFT_DOWN_MASK)
    {
      shiftdown = true;
    }
  }

  // mouseMotionListener
  public void mouseMoved(MouseEvent e)
  {
    mousex = e.getX();
    mousey = e.getY();

    moved = true;
  }

  public void mouseDragged(MouseEvent e)
  {
    if(e.getButton() == MouseEvent.BUTTON1)
      button1 = true;
    if(e.getButton() == MouseEvent.BUTTON2)
      button2 = true;
    if(e.getButton() == MouseEvent.BUTTON3)
      button3 = true;

    mouseMoved(e);

    dragged = true;
  }

  // mouseWheelListener
  public void mouseWheelMoved(MouseWheelEvent e)
  {
    wheelup = false;
    wheeldown = false;

    int notches = e.getWheelRotation();

    if(notches < 0)
      wheelup = true;
    else
      wheeldown = true;
  }
}

