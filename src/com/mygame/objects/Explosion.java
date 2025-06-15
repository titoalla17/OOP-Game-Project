package com.mygame.objects;

import javax.swing.ImageIcon;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage; // Import penting

public class Explosion extends GameObject {

    private Image[] frames;
    private int currentFrame = 0;
    private int animationDelay = 4;
    private int delayCounter = 0;
    private boolean finished = false;

    private static final int FRAME_COUNT = 7;

    public Explosion(int x, int y) {
        super(x, y);
        loadFrames();
    }

    private void loadFrames() {
        frames = new Image[FRAME_COUNT];
        try {
            ImageIcon ii = new ImageIcon("res/images/explosion.png");
            Image originalImage = ii.getImage();

            int frameWidth = originalImage.getWidth(null) / FRAME_COUNT;
            int frameHeight = originalImage.getHeight(null);
            this.width = frameWidth;
            this.height = frameHeight;

            // Konversi dari Image ke BufferedImage
            BufferedImage spriteSheet = new BufferedImage(
                    originalImage.getWidth(null),
                    originalImage.getHeight(null),
                    BufferedImage.TYPE_INT_ARGB
            );
            Graphics2D g2d = spriteSheet.createGraphics();
            g2d.drawImage(originalImage, 0, 0, null);
            g2d.dispose();

            // Potong-potong BufferedImage menjadi beberapa frame
            for (int i = 0; i < FRAME_COUNT; i++) {
                frames[i] = spriteSheet.getSubimage(i * frameWidth, 0, frameWidth, frameHeight);
            }
        } catch (Exception e) {
            System.err.println("Gagal memuat sprite ledakan: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void update() {
        delayCounter++;
        if (delayCounter >= animationDelay) {
            delayCounter = 0;
            currentFrame++;
            if (currentFrame >= FRAME_COUNT) {
                finished = true;
            }
        }
    }

    public boolean isFinished() {
        return finished;
    }

    @Override
    public void draw(Graphics g) {
        if (!finished) {
            int drawX = x - (width / 2);
            int drawY = y - (height / 2);
            g.drawImage(frames[currentFrame], drawX, drawY, null);
        }
    }
}