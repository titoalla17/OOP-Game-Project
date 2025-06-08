// File: src/com/mygame/objects/PowerUp.java
package com.mygame.objects;

import javax.swing.ImageIcon;
import java.awt.Graphics;

public class PowerUp extends GameObject {

    private PowerUpType type;
    private int fallSpeed = 2;

    public PowerUp(int x, int y, PowerUpType type) {
        super(x, y);
        this.type = type;
        loadImage();
    }

    private void loadImage() {
        // Gambar bisa berbeda tergantung tipenya
        switch (type) {
            case SHIELD:
                ImageIcon ii = new ImageIcon("res/images/shield_powerup.png");
                image = ii.getImage();
                break;
            // case lain bisa ditambahkan di sini
        }
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