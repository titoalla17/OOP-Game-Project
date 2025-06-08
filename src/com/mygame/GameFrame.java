// File: src/com/mygame/GameFrame.java
package com.mygame;

import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.CardLayout;

public class GameFrame extends JFrame {

    private CardLayout cardLayout;
    private JPanel mainPanel; // Panel yang menggunakan CardLayout
    private MainMenuPanel mainMenuPanel;
    private GamePanel gamePanel;

    // Nama konstanta untuk setiap "kartu"
    private static final String MENU_PANEL = "MainMenu";
    private static final String GAME_PANEL = "Game";

    public GameFrame() {
        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);

        mainMenuPanel = new MainMenuPanel();
        gamePanel = new GamePanel(this); // Berikan referensi frame ini ke game panel

        // Tambahkan panel-panel ke mainPanel dengan nama unik
        mainPanel.add(mainMenuPanel, MENU_PANEL);
        mainPanel.add(gamePanel, GAME_PANEL);

        // Tambahkan listener ke tombol-tombol menu
        mainMenuPanel.addStartButtonListener(e -> startGame());
        mainMenuPanel.addExitButtonListener(e -> System.exit(0));

        add(mainPanel); // Tambahkan mainPanel ke frame

        setTitle("Jakarta Rush Hour");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        pack(); // Atur ukuran frame sesuai konten
        setLocationRelativeTo(null); // Tampilkan di tengah layar
        setVisible(true);
    }

    private void startGame() {
        // Panggil method untuk mereset dan memulai game
        gamePanel.startGame();
        // Tukar kartu untuk menampilkan GamePanel
        cardLayout.show(mainPanel, GAME_PANEL);
        // Penting: Pindahkan fokus ke GamePanel agar KeyListener berfungsi
        gamePanel.requestFocusInWindow();
    }

    // Method ini akan dipanggil dari GamePanel saat game over
    public void showMenu() {
        cardLayout.show(mainPanel, MENU_PANEL);
    }
}