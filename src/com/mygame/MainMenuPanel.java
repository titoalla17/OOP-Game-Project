// File: src/com/mygame/MainMenuPanel.java
package com.mygame;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class MainMenuPanel extends JPanel {

    private JButton startButton;
    private JButton exitButton;

    public MainMenuPanel() {
        // Menggunakan GridBagLayout untuk kontrol posisi yang presisi
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 0, 10, 0); // Jarak antar komponen

        // Judul Game
        JLabel titleLabel = new JLabel("Jakarta Rush Hour", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Helvetica", Font.BOLD, 40));
        titleLabel.setForeground(Color.ORANGE);

        // Tombol Mulai
        startButton = new JButton("Mulai Game");
        startButton.setFont(new Font("Helvetica", Font.PLAIN, 24));

        // Tombol Keluar
        exitButton = new JButton("Keluar");
        exitButton.setFont(new Font("Helvetica", Font.PLAIN, 24));

        // Menambahkan komponen ke panel
        add(titleLabel, gbc);
        add(Box.createVerticalStrut(50), gbc); // Spasi vertikal
        add(startButton, gbc);
        add(exitButton, gbc);

        setBackground(Color.DARK_GRAY);
    }

    // Method untuk 'menyerahkan' action listener ke kelas lain (GameFrame)
    public void addStartButtonListener(ActionListener listener) {
        startButton.addActionListener(listener);
    }

    public void addExitButtonListener(ActionListener listener) {
        exitButton.addActionListener(listener);
    }
}