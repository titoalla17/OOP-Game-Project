package com.mygame.objects;

import javax.swing.ImageIcon;
import java.awt.Graphics;

public class Bullet extends GameObject {
    private int speed = 10;

    public Bullet(int x, int y) {
        super(x, y);
        loadImage();
    }

    private void loadImage() {
        try {
            ImageIcon ii = new ImageIcon("res/images/bullet.png");
            image = ii.getImage();
            width = image.getWidth(null);
            height = image.getHeight(null);
        } catch(Exception e) {
            System.err.println("Gagal memuat gambar peluru: res/images/bullet.png");
        }
    }

    public void update() {
        y -= speed;
    }

    @Override
    public void draw(Graphics g) {
        g.drawImage(image, x, y, null);
    }
}