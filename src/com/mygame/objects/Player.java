package com.mygame.objects;

import javax.swing.ImageIcon;
import java.awt.*;

public class Player extends GameObject {
    private int speed = 5;
    private boolean shieldActive = false; // Status perisai
    private int shieldTimer = 0; // Durasi perisai

    public Player(int x, int y) {
        super(x, y);
        loadImage();
    }

    private void loadImage() {
        // Muat gambar dari folder res/images
        ImageIcon ii = new ImageIcon("res/images/player.png");
        image = ii.getImage();
        width = image.getWidth(null);
        height = image.getHeight(null);
    }

    public void move(boolean left, boolean right, boolean up, boolean down) {
        if (left) x -= speed;
        if (right) x += speed;
        if (up) y -= speed;
        if (down) y += speed;

        // Tambahkan batas agar tidak keluar layar
        // ... (logika batas)
    }

    public void activateShield(int duration) {
        this.shieldActive = true;
        this.shieldTimer = duration; // durasi dalam hitungan frame (misal: 300 frame = 5 detik @60fps)
    }

    public boolean isShieldActive() {
        return shieldActive;
    }

    @Override
    public void draw(Graphics g) {
        g.drawImage(image, x, y, null);
        // Gambar visual untuk perisai jika aktif
        if (shieldActive) {
            g.setColor(new Color(0, 200, 255, 100)); // Warna biru transparan
            g.fillOval(x - 10, y - 10, width + 20, height + 20);
        }
    }

    public void update() {
        // Mengurangi durasi perisai setiap frame
        if (shieldActive) {
            shieldTimer--;
            if (shieldTimer <= 0) {
                shieldActive = false;
            }
        }
    }
}