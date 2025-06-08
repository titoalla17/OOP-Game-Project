// File: src/com/mygame/GamePanel.java
package com.mygame;

import com.mygame.objects.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import com.mygame.utils.SoundManager; // Import SoundManager
import com.mygame.objects.PowerUp; // Import PowerUp
import com.mygame.objects.PowerUpType; // Import PowerUpType

public class GamePanel extends JPanel implements ActionListener, KeyListener {

    private final int SCREEN_WIDTH = 800;
    private final int SCREEN_HEIGHT = 600;
    private final int DELAY = 16; // Approx 60 FPS

    private Timer timer;
    private Player player;
    private List<Obstacle> obstacles;
    private boolean isRunning = true;
    private Random random;

    private boolean isMovingLeft, isMovingRight, isMovingUp, isMovingDown;
    private int score;

    private List<PowerUp> powerUps;
    private SoundManager soundManager;

    private int level = 1;
    private int scoreToNextLevel = 200; // Skor untuk naik ke level 2
    private int obstacleSpawnChance = 50; // Peluang spawn rintangan (semakin kecil, semakin sering)

    private GameFrame gameFrame;

    public GamePanel(GameFrame frame) {
        this.gameFrame = frame;
        setPreferredSize(new Dimension(SCREEN_WIDTH, SCREEN_HEIGHT));
        setBackground(Color.DARK_GRAY);
        setFocusable(true);
        addKeyListener(this);
        random = new Random();
        startGame();
        soundManager = new SoundManager();
    }

    void startGame() {
        player = new Player(SCREEN_WIDTH / 2, SCREEN_HEIGHT - 100);
        obstacles = new ArrayList<>();
        powerUps = new ArrayList<>();
        score = 0;
        level = 1;
        scoreToNextLevel = 200;
        obstacleSpawnChance = 50;
        isRunning = true;

        isMovingLeft = false;
        isMovingRight = false;
        isMovingUp = false;
        isMovingDown = false;

        // Jika timer sudah ada, restart. Jika belum, buat baru.
        if (timer != null) {
            timer.restart();
        } else {
            timer = new Timer(DELAY, this);
            timer.start();
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        updateGame();
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (isRunning) {
            drawGame(g);
        } else {
            drawGameOver(g);
        }
        Toolkit.getDefaultToolkit().sync();
    }

    private void drawGame(Graphics g) {
        player.draw(g);
        for (Obstacle obstacle : obstacles) {
            obstacle.draw(g);
        }
        for (PowerUp powerUp : powerUps) {
            powerUp.draw(g);
        }

        // Gambar Skor dan Level
        g.setColor(Color.WHITE);
        g.setFont(new Font("Helvetica", Font.BOLD, 20));
        g.drawString("Skor: " + score, 10, 20);
        g.drawString("Level: " + level, 10, 45);
    }

    private void drawGameOver(Graphics g) {
        String msg = "Game Over";
        String finalScoreMsg = "Skor Akhir: " + score;
        String backToMenuMsg = "Tekan Enter untuk Kembali ke Menu";

        Font titleFont = new Font("Helvetica", Font.BOLD, 30);
        FontMetrics fmTitle = getFontMetrics(titleFont);
        g.setFont(titleFont);
        g.setColor(Color.WHITE);
        g.drawString(msg, (SCREEN_WIDTH - fmTitle.stringWidth(msg)) / 2, SCREEN_HEIGHT / 2 - 100);

        Font scoreFont = new Font("Helvetica", Font.PLAIN, 24);
        FontMetrics fmScore = getFontMetrics(scoreFont);
        g.setFont(scoreFont);
        g.drawString(finalScoreMsg, (SCREEN_WIDTH - fmScore.stringWidth(finalScoreMsg)) / 2, SCREEN_HEIGHT / 2 - 50);

        Font menuFont = new Font("Helvetica", Font.ITALIC, 20);
        FontMetrics fmMenu = getFontMetrics(menuFont);
        g.setFont(menuFont);
        g.drawString(backToMenuMsg, (SCREEN_WIDTH - fmMenu.stringWidth(backToMenuMsg)) / 2, SCREEN_HEIGHT / 2 + 50);
    }

    private void updateGame() {
        if (!isRunning) return;

        // Update player (termasuk timer perisai)
        player.update();
        player.move(isMovingLeft, isMovingRight, isMovingUp, isMovingDown);

        // Update, spawn, dan hapus Obstacles & Powerups
        updateObstacles();
        updatePowerUps();

        // Cek Level Up
        checkLevelUp();

        // Naikkan skor
        score++;
        checkCollisions();
    }

    private void checkLevelUp() {
        if (score >= scoreToNextLevel) {
            level++;
            scoreToNextLevel *= 2; // Target skor berikutnya dua kali lipat
            if (obstacleSpawnChance > 15) { // Batasi agar tidak terlalu sulit
                obstacleSpawnChance -= 5; // Rintangan lebih sering muncul
            }
            soundManager.playSound("res/sounds/levelup.wav"); // Mainkan suara naik level!
        }
    }

    private void updateObstacles() {
        // Spawn
        if (random.nextInt(obstacleSpawnChance) == 1) {
            spawnObstacle();
        }
        // Update posisi & hapus
        obstacles.removeIf(obstacle -> {
            obstacle.update();
            return obstacle.getY() > SCREEN_HEIGHT;
        });
    }

    private void updatePowerUps() {
        // Spawn (lebih jarang dari obstacle)
        if (random.nextInt(500) == 1) {
            spawnPowerUp();
        }
        // Update posisi & hapus
        powerUps.removeIf(powerUp -> {
            powerUp.update();
            return powerUp.getY() > SCREEN_HEIGHT;
        });
    }

    private void spawnPowerUp() {
        int x = random.nextInt(SCREEN_WIDTH - 50);
        int y = -100;
        powerUps.add(new PowerUp(x, y, PowerUpType.SHIELD)); // Selalu spawn SHIELD untuk saat ini
    }

    private void spawnObstacle() {
        int x = random.nextInt(SCREEN_WIDTH - 50);
        int y = -100;
        obstacles.add(new Obstacle(x, y));
    }

    private void checkCollisions() {
        Rectangle playerBounds = player.getBounds();

        // Cek tabrakan dengan Obstacle
        for (Obstacle obstacle : obstacles) {
            if (playerBounds.intersects(obstacle.getBounds())) {
                if (player.isShieldActive()) {
                    // Jika punya perisai, hancurkan rintangannya
                    // obstacles.remove(obstacle); // Hati-hati dengan ConcurrentModificationException
                    // Cara lebih aman adalah menandainya untuk dihapus
                } else {
                    // Jika tidak punya perisai, game over
                    isRunning = false;
                    timer.stop();
                    soundManager.playSound("res/sounds/crash.wav"); // Mainkan suara tabrakan
                }
            }
        }

        // Cek tabrakan dengan PowerUp
        List<PowerUp> powerUpsToRemove = new ArrayList<>();
        for (PowerUp powerUp : powerUps) {
            if (playerBounds.intersects(powerUp.getBounds())) {
                // Terapkan efek power-up
                if (powerUp.getType() == PowerUpType.SHIELD) {
                    player.activateShield(300); // Aktifkan perisai selama 5 detik (300 frame)
                }
                powerUpsToRemove.add(powerUp); // Tandai power-up untuk dihapus
                soundManager.playSound("res/sounds/powerup.wav"); // Mainkan suara power-up
            }
        }
        powerUps.removeAll(powerUpsToRemove); // Hapus power-up yang sudah diambil
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();

        if (isRunning) {
            if (key == KeyEvent.VK_A || key == KeyEvent.VK_LEFT) isMovingLeft = true;
            if (key == KeyEvent.VK_D || key == KeyEvent.VK_RIGHT) isMovingRight = true;
            if (key == KeyEvent.VK_W || key == KeyEvent.VK_UP) isMovingUp = true;
            if (key == KeyEvent.VK_S || key == KeyEvent.VK_DOWN) isMovingDown = true;
        } else {
            // Jika game tidak berjalan (game over), dan user menekan Enter
            if (key == KeyEvent.VK_ENTER) {
                gameFrame.showMenu(); // Panggil method di GameFrame untuk kembali ke menu
            }
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        int key = e.getKeyCode();
        if (key == KeyEvent.VK_A || key == KeyEvent.VK_LEFT) isMovingLeft = false;
        if (key == KeyEvent.VK_D || key == KeyEvent.VK_RIGHT) isMovingRight = false;
        if (key == KeyEvent.VK_W || key == KeyEvent.VK_UP) isMovingUp = false;
        if (key == KeyEvent.VK_S || key == KeyEvent.VK_DOWN) isMovingDown = false;
    }

    @Override
    public void keyTyped(KeyEvent e) {}
}