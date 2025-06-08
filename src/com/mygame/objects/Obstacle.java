// File: src/com/mygame/objects/Obstacle.java
package com.mygame.objects;

import javax.swing.ImageIcon;
import java.awt.Graphics;
import java.util.Random;

public class Obstacle extends GameObject {
    private int fallSpeed;

    public Obstacle(int x, int y) {
        super(x, y);
        // Kecepatan jatuh bisa dibuat acak agar lebih dinamis
        this.fallSpeed = new Random().nextInt(3) + 2; // Kecepatan antara 2 s/d 4
        loadImage();
    }

    private void loadImage() {
        // Di sini Anda bisa membuat logika untuk memuat gambar obstacle secara acak
        // Untuk saat ini, kita gunakan satu gambar saja.
        ImageIcon ii = new ImageIcon("res/images/obstacle1.png");
        image = ii.getImage();
        width = image.getWidth(null);
        height = image.getHeight(null);
    }

    // Method untuk menggerakkan obstacle ke bawah
    public void update() {
        this.y += fallSpeed;
    }

    @Override
    public void draw(Graphics g) {
        g.drawImage(image, x, y, null);
    }
}