package com.mygame;

import com.mygame.utils.HighScoreManager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.List;

public class HighScorePanel extends JPanel {
    private JButton backButton;
    private JLabel[] scoreLabels;
    private HighScoreManager highScoreManager;

    public HighScorePanel() {
        highScoreManager = new HighScoreManager();
        scoreLabels = new JLabel[5];

        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.insets = new Insets(10, 0, 10, 0);

        JLabel titleLabel = new JLabel("Skor Tertinggi", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Helvetica", Font.BOLD, 40));
        titleLabel.setForeground(Color.YELLOW);
        add(titleLabel, gbc);

        add(Box.createVerticalStrut(30), gbc);

        for (int i = 0; i < 5; i++) {
            scoreLabels[i] = new JLabel((i + 1) + ". ---", SwingConstants.CENTER);
            scoreLabels[i].setFont(new Font("Helvetica", Font.PLAIN, 24));
            scoreLabels[i].setForeground(Color.WHITE);
            add(scoreLabels[i], gbc);
        }

        add(Box.createVerticalStrut(30), gbc);

        backButton = new JButton("Kembali ke Menu");
        backButton.setFont(new Font("Helvetica", Font.PLAIN, 24));
        add(backButton, gbc);

        setBackground(Color.DARK_GRAY);
    }

    public void updateScores() {
        List<Integer> scores = highScoreManager.loadScores();
        for (int i = 0; i < 5; i++) {
            if (i < scores.size()) {
                scoreLabels[i].setText((i + 1) + ". " + scores.get(i));
            } else {
                scoreLabels[i].setText((i + 1) + ". ---");
            }
        }
    }

    public void addBackButtonListener(ActionListener listener) {
        backButton.addActionListener(listener);
    }
}