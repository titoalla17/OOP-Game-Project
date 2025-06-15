package com.mygame.objects;

import javax.swing.ImageIcon;
import java.awt.Graphics;
import java.util.Random;

public class Obstacle extends GameObject {

    private ObstacleType type;
    private int fallSpeed;
    private int health;

    public Obstacle(int x, int y, ObstacleType type, int level, int scrollSpeed) {
        super(x, y);
        this.type = type;

        // PERBAIKAN: Cek jika tipenya BUKAN HOLE
        if (type != ObstacleType.HOLE) {
            this.health = 30; // Semua mobil punya 30 nyawa
            int carBaseSpeed = 2;
            int carRandomSpeed = new Random().nextInt(2);
            int carOwnSpeed = carBaseSpeed + carRandomSpeed + level;

            this.fallSpeed = carOwnSpeed + scrollSpeed;
        } else { // Jika tipenya adalah HOLE
            this.health = 9999; // Lubang tidak bisa dihancurkan
            this.fallSpeed = scrollSpeed;
        }

        loadImage();
    }

    private void loadImage() {
        String imagePath = "res/images/car_blue.png";
        switch (type) {
            case CARGO:
                imagePath = "res/images/car_blue.png";
                break;
            case CAR_YELLOW:
                imagePath = "res/images/car_yellow.png";
                break;
            case MOTOR:
                imagePath = "res/images/motor.png";
                break;
            case CAR_GREEN:
                imagePath = "res/images/car_green.png";
                break;
            case TRUCK:
                imagePath = "res/images/truck.png";
                break;
            case VAN:
                imagePath = "res/images/van.png";
                break;
            case CONVERTIBLE_CAR:
                imagePath = "res/images/convertible_car.png";
                break;
            case HOLE:
                imagePath = "res/images/obstacle_hole.png";
                break;
        }
        ImageIcon ii = new ImageIcon(imagePath);
        image = ii.getImage();
        width = image.getWidth(null);
        height = image.getHeight(null);
    }

    public void update() {
        y += fallSpeed;
    }

    public void takeDamage(int damage) {
        this.health -= damage;
    }

    public boolean isDestroyed() {
        return health <= 0;
    }

    public ObstacleType getType() {
        return this.type;
    }

    @Override
    public void draw(Graphics g) {
        g.drawImage(image, x, y, null);
    }
}