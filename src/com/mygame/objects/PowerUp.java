package com.mygame.objects;

import javax.swing.ImageIcon;
import java.awt.Graphics;

// Tidak ada deklarasi enum di sini
public class PowerUp extends GameObject {

    private PowerUpType type;
    private int fallSpeed = 2;

    public PowerUp(int x, int y, PowerUpType type) {
        super(x, y);
        this.type = type;
        loadImage();
    }

    private void loadImage() {
        String imagePath = "res/images/shield.png";
        // Logika untuk memuat gambar berdasarkan tipe bisa ditambahkan di sini
        ImageIcon ii = new ImageIcon(imagePath);
        image = ii.getImage();
        width = image.getWidth(null);
        height = image.getHeight(null);
    }

    public void update() {
        this.y += fallSpeed;
    }

    @Override
    public void draw(Graphics g) {
        g.drawImage(image, x, y, null);
    }

    public PowerUpType getType() {
        return type;
    }
}