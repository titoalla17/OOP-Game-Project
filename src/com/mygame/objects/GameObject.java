package com.mygame.objects;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;

public abstract class GameObject {
    protected int x, y, width, height;
    protected Image image;

    public GameObject(int x, int y) {
        this.x = x;
        this.y = y;
    }

    // Abstract method, harus diimplementasikan oleh subclass
    public abstract void draw(Graphics g);

    // Method untuk deteksi tabrakan
    public Rectangle getBounds() {
        return new Rectangle(x, y, width, height);
    }

    // Getters dan Setters
    public int getX() { return x; }
    public int getY() { return y; }
}