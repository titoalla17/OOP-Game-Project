package com.mygame.objects;

import com.mygame.GamePanel;
import javax.swing.ImageIcon;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;

public class Player extends GameObject {
    private int speed = 5;
    private int lives;

    private boolean shieldActive = false;
    private int shieldTimer = 0;

    // Variabel shieldImage sudah dihapus dari sini

    // Variabel untuk status kebal setelah tertabrak
    private boolean invincible = false;
    private int invincibilityTimer = 0;

    public Player(int x, int y) {
        super(x, y);
        this.lives = 3;
        loadImage();
    }

    private void loadImage() {
        try {
            // Hanya memuat gambar pemain
            ImageIcon playerIcon = new ImageIcon("res/images/player.png");
            image = playerIcon.getImage();
            width = image.getWidth(null);
            height = image.getHeight(null);

            // Kode untuk memuat 'active_shield.png' sudah dihapus
        } catch (Exception e) {
            System.err.println("Gagal memuat gambar pemain: " + e.getMessage());
        }
    }

    public void update() {
        if (shieldActive) {
            shieldTimer--;
            if (shieldTimer <= 0) shieldActive = false;
        }

        if (invincible) {
            invincibilityTimer--;
            if (invincibilityTimer <= 0) invincible = false;
        }
    }

    public void move(boolean left, boolean right, boolean up, boolean down) {
        if (left) x -= speed;
        if (right) x += speed;
        if (up) y -= speed;
        if (down) y += speed;

        if (x < GamePanel.ROAD_START_X) x = GamePanel.ROAD_START_X;
        if (x + width > GamePanel.ROAD_END_X) x = GamePanel.ROAD_END_X - width;
        if (y < 0) y = 0;
        if (y + height > GamePanel.SCREEN_HEIGHT) y = GamePanel.SCREEN_HEIGHT - height;
    }

    public Bullet shoot() {
        int bulletX = this.x + (this.width / 2) - 2;
        int bulletY = this.y;
        return new Bullet(bulletX, bulletY);
    }

    public void takeDamage(int amount) {
        if (invincible) return;

        if (shieldActive) {
            shieldActive = false;
            return;
        }

        if (amount > 0) {
            this.lives--;
            if (this.lives < 0) this.lives = 0;

            this.invincible = true;
            this.invincibilityTimer = 120;
        }
    }

    public int getLives() {
        return lives;
    }

    public void activateShield(int duration) {
        this.shieldActive = true;
        this.shieldTimer = duration;
    }

    public boolean isShieldActive() {
        return shieldActive;
    }

    @Override
    public void draw(Graphics g) {
        // Logika untuk membuat sprite berkedip saat kebal
        if (invincible) {
            if (invincibilityTimer % 10 < 5) {
                return;
            }
        }

        g.drawImage(image, x, y, null);

        // --- KODE EFEK PERISAI DIKEMBALIKAN KE VERSI LINGKARAN BIRU ---
        if (shieldActive) {
            Graphics2D g2d = (Graphics2D) g;
            // Warna biru transparan untuk efek perisai
            g2d.setColor(new Color(0, 200, 255, 100));
            // Gambar oval di sekeliling pemain
            g2d.fillOval(x - 10, y - 10, width + 20, height + 20);
        }
    }
}