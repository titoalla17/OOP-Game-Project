package com.mygame;

import com.mygame.utils.FontLoader; // <-- IMPORT kelas baru kita

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class MainMenuPanel extends JPanel {

    private JButton startButton;
    private JButton highScoreButton;
    private JButton exitButton;

    private Image backgroundImage;

    private Font titleFont;
    private Font buttonFont;

    public MainMenuPanel() {
        // --- MEMUAT FONT PIXEL DI AWAL ---
        // Path dimulai dengan '/' untuk menandakan root dari classpath (folder res)
        titleFont = FontLoader.loadFont("/fonts/PressStart2P-Regular.ttf", 48f);
        buttonFont = FontLoader.loadFont("/fonts/PressStart2P-Regular.ttf", 24f);

        try {
            backgroundImage = new ImageIcon("res/images/background.png").getImage();
        } catch (Exception e) {
            System.err.println("Gagal memuat gambar latar menu: " + e.getMessage());
            backgroundImage = null;
        }

        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.insets = new Insets(15, 0, 15, 0);

        // --- STYLING JUDUL GAME DENGAN FONT BARU ---
        JLabel titleLabel = new JLabel("Jakarta Rampage");
        titleLabel.setFont(titleFont); // Gunakan font pixel
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setUI(new javax.swing.plaf.basic.BasicLabelUI() {
            @Override
            protected void paintEnabledText(JLabel l, Graphics g, String s, int textX, int textY) {
                g.setColor(Color.BLACK);
                g.drawString(s, textX + 4, textY + 4); // Bayangan lebih tebal
                g.setColor(l.getForeground());
                g.drawString(s, textX, textY);
            }
        });

        startButton = new JButton("Mulai Game");
        highScoreButton = new JButton("Skor Tertinggi");
        exitButton = new JButton("Keluar");

        styleButton(startButton);
        styleButton(highScoreButton);
        styleButton(exitButton);

        gbc.anchor = GridBagConstraints.CENTER;
        add(titleLabel, gbc);
        add(Box.createVerticalStrut(40), gbc);
        add(startButton, gbc);
        add(highScoreButton, gbc);
        add(exitButton, gbc);
    }

    private void styleButton(JButton button) {
        button.setFont(buttonFont); // Gunakan font pixel
        button.setForeground(Color.WHITE);

        button.setOpaque(false);
        button.setContentAreaFilled(false);
        button.setBorderPainted(false);
        button.setFocusPainted(false);

        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setForeground(Color.YELLOW);
            }
            @Override
            public void mouseExited(MouseEvent e) {
                button.setForeground(Color.WHITE);
            }
        });
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (backgroundImage != null) {
            g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
        }
    }

    // Method listener tidak berubah
    public void addStartButtonListener(ActionListener listener) { startButton.addActionListener(listener); }
    public void addHighScoreButtonListener(ActionListener listener) { highScoreButton.addActionListener(listener); }
    public void addExitButtonListener(ActionListener listener) { exitButton.addActionListener(listener); }
}