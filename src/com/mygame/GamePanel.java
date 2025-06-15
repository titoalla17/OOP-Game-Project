package com.mygame;

import com.mygame.objects.*;
import com.mygame.utils.HighScoreManager;
import com.mygame.utils.SoundManager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GamePanel extends JPanel implements ActionListener, KeyListener {

    // --- KONSTANTA ---
    private final int SCREEN_WIDTH = 800;
    public static final int SCREEN_HEIGHT = 600;
    private final int DELAY = 16;

    // Batas Area Jalan Raya (dibuat public static agar bisa diakses kelas lain)
    public static final int ROAD_START_X = 100;
    public static final int ROAD_WIDTH = 600;
    public static final int ROAD_END_X = ROAD_START_X + ROAD_WIDTH;

    // Konstanta untuk lajur
    private final int NUMBER_OF_LANES = 5;
    private final int LANE_WIDTH = ROAD_WIDTH / NUMBER_OF_LANES;


    // --- KOMPONEN GAME ---
    private Timer timer;
    private Player player;
    private List<Obstacle> obstacles;
    private List<Bullet> bullets;
    private List<PowerUp> powerUps;
    private List<Explosion> explosions;
    private Random random;

    // --- STATUS GAME ---
    private boolean isRunning = false;
    private int score;
    private int level;
    private int scoreToNextLevel;
    private int obstacleSpawnChance;
    private int scrollSpeed;

    // --- KONTROL INPUT ---
    private boolean isMovingLeft, isMovingRight, isMovingUp, isMovingDown;

    // --- REFERENSI EKSTERNAL ---
    private GameFrame gameFrame;
    private SoundManager soundManager;
    private HighScoreManager highScoreManager;

    // --- ASET VISUAL ---
    private Image backgroundImage;
    private int bgY1, bgY2;
    private Image lifeIcon;


    public GamePanel(GameFrame frame, SoundManager sm, HighScoreManager hsm) {
        this.gameFrame = frame;
        this.soundManager = sm;
        this.highScoreManager = hsm;

        setPreferredSize(new Dimension(SCREEN_WIDTH, SCREEN_HEIGHT));
        setFocusable(true);
        addKeyListener(this);
        random = new Random();

        try {
            backgroundImage = new ImageIcon("res/images/background.png").getImage();
            lifeIcon = new ImageIcon("res/images/alive.png").getImage();
        } catch (Exception e) {
            System.err.println("Gagal memuat gambar aset: " + e.getMessage());
        }
    }

    public void startGame() {
        player = new Player(SCREEN_WIDTH / 2, SCREEN_HEIGHT - 100);
        obstacles = new ArrayList<>();
        bullets = new ArrayList<>();
        powerUps = new ArrayList<>();
        explosions = new ArrayList<>();
        score = 0;
        level = 1;
        scoreToNextLevel = 500;
        obstacleSpawnChance = 30;
        isRunning = true;
        this.scrollSpeed = 5 + this.level;

        isMovingLeft = false;
        isMovingRight = false;
        isMovingUp = false;
        isMovingDown = false;

        bgY1 = 0;
        bgY2 = -SCREEN_HEIGHT;

        soundManager.startBackgroundMusic("res/sounds/backsound.wav");

        if (timer != null) {
            timer.restart();
        } else {
            timer = new Timer(DELAY, this);
            timer.start();
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (isRunning) {
            updateGame();
        }
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;

        if (backgroundImage != null) {
            g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR);
            g2d.drawImage(backgroundImage, 0, bgY1, SCREEN_WIDTH, SCREEN_HEIGHT, this);
            g2d.drawImage(backgroundImage, 0, bgY2, SCREEN_WIDTH, SCREEN_HEIGHT, this);
        }

        if (isRunning) {
            drawGame(g2d);
        } else {
            if (player != null) {
                drawGameOver(g2d);
            }
        }
        Toolkit.getDefaultToolkit().sync();
    }

    private void drawGame(Graphics2D g) {
        for (Obstacle obstacle : obstacles) {
            if (obstacle.getType() == ObstacleType.HOLE) obstacle.draw(g);
        }
        for (PowerUp powerUp : powerUps) {
            powerUp.draw(g);
        }
        for (Bullet bullet : bullets) {
            bullet.draw(g);
        }
        for (Obstacle obstacle : obstacles) {
            if (obstacle.getType() != ObstacleType.HOLE) obstacle.draw(g);
        }
        for (Explosion explosion : explosions) {
            explosion.draw(g);
        }
        player.draw(g);

        g.setColor(Color.WHITE);
        g.setFont(new Font("Helvetica", Font.BOLD, 20));
        g.drawString("Skor: " + score, 10, 25);
        g.drawString("Level: " + level, 10, 50);

        if (lifeIcon != null) {
            int iconWidth = lifeIcon.getWidth(null);
            for (int i = 0; i < player.getLives(); i++) {
                g.drawImage(lifeIcon, SCREEN_WIDTH - 40 - (i * (iconWidth + 5)), 15, null);
            }
        }
    }

    private void drawGameOver(Graphics2D g) {
        String msg = "Game Over";
        String finalScoreMsg = "Skor Akhir: " + score;
        String backToMenuMsg = "Tekan Enter untuk Kembali ke Menu";

        g.setColor(Color.WHITE);
        g.setFont(new Font("Helvetica", Font.BOLD, 40));
        FontMetrics fmTitle = g.getFontMetrics();
        g.drawString(msg, (SCREEN_WIDTH - fmTitle.stringWidth(msg)) / 2, SCREEN_HEIGHT / 2 - 100);

        g.setFont(new Font("Helvetica", Font.PLAIN, 24));
        FontMetrics fmScore = g.getFontMetrics();
        g.drawString(finalScoreMsg, (SCREEN_WIDTH - fmScore.stringWidth(finalScoreMsg)) / 2, SCREEN_HEIGHT / 2 - 50);

        g.setFont(new Font("Helvetica", Font.ITALIC, 20));
        FontMetrics fmMenu = g.getFontMetrics();
        g.drawString(backToMenuMsg, (SCREEN_WIDTH - fmMenu.stringWidth(backToMenuMsg)) / 2, SCREEN_HEIGHT / 2 + 50);
    }

    private void updateExplosions() {
        // Update setiap ledakan dan hapus yang sudah selesai animasinya
        explosions.removeIf(explosion -> {
            explosion.update();
            return explosion.isFinished();
        });
    }

    private void updateGame() {
        updateBackground();
        player.update();
        player.move(isMovingLeft, isMovingRight, isMovingUp, isMovingDown);
        updateBullets();
        updateObstacles();
        updatePowerUps();
        updateExplosions();
        checkLevelUp();
        checkCollisions();
    }

    private void updateBackground() {
        bgY1 += scrollSpeed;
        bgY2 += scrollSpeed;
        if (bgY1 >= SCREEN_HEIGHT) bgY1 = bgY2 - SCREEN_HEIGHT;
        if (bgY2 >= SCREEN_HEIGHT) bgY2 = bgY1 - SCREEN_HEIGHT;
    }

    private void updateBullets() {
        bullets.removeIf(bullet -> {
            bullet.update();
            return bullet.getY() < 0;
        });
    }

    private void updateObstacles() {
        if (random.nextInt(obstacleSpawnChance) == 1) spawnObstacle();
        obstacles.removeIf(obstacle -> {
            obstacle.update();
            return obstacle.getY() > SCREEN_HEIGHT;
        });
    }

    private void updatePowerUps() {
        if (random.nextInt(600) == 1) spawnPowerUp();
        powerUps.removeIf(powerUp -> {
            powerUp.update();
            return powerUp.getY() > SCREEN_HEIGHT;
        });
    }

    private void spawnObstacle() {
        int randomLane = random.nextInt(NUMBER_OF_LANES);
        int laneCenterX = ROAD_START_X + (randomLane * LANE_WIDTH) + (LANE_WIDTH / 2);
        int x = laneCenterX - (50 / 2);

        ObstacleType type;
        if (random.nextInt(10) == 0) {
            type = ObstacleType.HOLE;
        } else {
            ObstacleType[] carTypes = {
                    ObstacleType.CAR_YELLOW, ObstacleType.CAR_GREEN, ObstacleType.CARGO,
                    ObstacleType.CAR_GREEN, ObstacleType.TRUCK, ObstacleType.VAN, ObstacleType.CONVERTIBLE_CAR
            };
            type = carTypes[random.nextInt(carTypes.length)];
        }
        obstacles.add(new Obstacle(x, -100, type, this.level, this.scrollSpeed));
    }

    private void spawnPowerUp() {
        int randomLane = random.nextInt(NUMBER_OF_LANES);
        int laneCenterX = ROAD_START_X + (randomLane * LANE_WIDTH) + (LANE_WIDTH / 2);
        int x = laneCenterX - (30 / 2);

        PowerUpType type = PowerUpType.SHIELD;
        powerUps.add(new PowerUp(x, -100, type));
    }


    private void checkLevelUp() {
        if (score > scoreToNextLevel) {
            level++;
            scoreToNextLevel *= 1.5;
            if (obstacleSpawnChance > 10) obstacleSpawnChance -= 4;
            this.scrollSpeed = 5 + this.level;
            soundManager.playSound("res/sounds/level.wav");
        }
    }

    private void checkCollisions() {
        List<Bullet> bulletsToRemove = new ArrayList<>();
        List<Obstacle> obstaclesToRemove = new ArrayList<>();
        List<PowerUp> powerUpsToRemove = new ArrayList<>();
        Rectangle playerBounds = player.getBounds();

        for (Bullet bullet : bullets) {
            for (Obstacle enemy : obstacles) {
                if (enemy.getType() != ObstacleType.HOLE && bullet.getBounds().intersects(enemy.getBounds())) {
                    enemy.takeDamage(10);
                    bulletsToRemove.add(bullet);
                    if (enemy.isDestroyed()) {
                        if (!obstaclesToRemove.contains(enemy)) {
                            obstaclesToRemove.add(enemy);
                            score += 50;
                            soundManager.playSound("res/sounds/meledak.wav");

                            // --- BUAT LEDAKAN DI SINI ---
                            // Ambil posisi tengah dari musuh yang hancur
                            int centerX = enemy.getX() + (enemy.getBounds().width / 2);
                            int centerY = enemy.getY() + (enemy.getBounds().height / 2);
                            explosions.add(new Explosion(centerX, centerY));
                            // -------------------------
                        }
                    }
                }
            }
        }

        for (Obstacle enemy : obstacles) {
            if (playerBounds.intersects(enemy.getBounds())) {
                player.takeDamage(1);
                if (enemy.getType() == ObstacleType.HOLE) {
                    if (!obstaclesToRemove.contains(enemy)) {
                        obstaclesToRemove.add(enemy);
                    }
                }
                if (player.getLives() <= 0) {
                    isRunning = false;
                }
            }
        }

        for (PowerUp powerUp : powerUps) {
            if (playerBounds.intersects(powerUp.getBounds())) {
                if (powerUp.getType() == PowerUpType.SHIELD) {
                    player.activateShield(300);
                    soundManager.playSound("res/sounds/powerup.wav");
                }
                powerUpsToRemove.add(powerUp);
            }
        }

        if (!isRunning && timer.isRunning()) {
            timer.stop();
            highScoreManager.saveScore(score);
            soundManager.playSound("res/sounds/crash.wav");
            soundManager.stopBackgroundMusic();
        }

        bullets.removeAll(bulletsToRemove);
        obstacles.removeAll(obstaclesToRemove);
        powerUps.removeAll(powerUpsToRemove);
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();
        if (isRunning) {
            if (key == KeyEvent.VK_A || key == KeyEvent.VK_LEFT) isMovingLeft = true;
            if (key == KeyEvent.VK_D || key == KeyEvent.VK_RIGHT) isMovingRight = true;
            if (key == KeyEvent.VK_W || key == KeyEvent.VK_UP) isMovingUp = true;
            if (key == KeyEvent.VK_S || key == KeyEvent.VK_DOWN) isMovingDown = true;

            if (key == KeyEvent.VK_SPACE) {
                bullets.add(player.shoot());
                soundManager.playSound("res/sounds/tembak.wav");
            }
        } else {
            if (key == KeyEvent.VK_ENTER && player != null) {
                gameFrame.showMenu();
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
    public void keyTyped(KeyEvent e) {
        // Tidak digunakan
    }
}