package com.stefanolupo.websocketbackend;

public class GameObject {

  private int x;
  private int y;

  public GameObject(int x, int y) {
    this.x = x;
    this.y = y;

  }

  public GameObject() {
  }

  public void setX(int x) {
    this.x = x;
  }

  public void setY(int y) {
    this.y = y;
  }

  public int getX() {
    return x;
  }

  public int getY() {
    return y;
  }
}
