package com.mygame;

import com.mygame.utils.HighScoreManager;
import com.mygame.utils.SoundManager;

import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.CardLayout;

public class GameFrame extends JFrame {

    private CardLayout cardLayout;
    private JPanel mainPanel;
    private MainMenuPanel mainMenuPanel;
    private GamePanel gamePanel;
    private HighScorePanel highScorePanel;

    private SoundManager soundManager;
    private HighScoreManager highScoreManager;

    private static final String MENU_PANEL = "MainMenu";
    private static final String GAME_PANEL = "Game";
    private static final String HIGHSCORE_PANEL = "HighScore";

    public GameFrame() {
        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);

        soundManager = new SoundManager();
        highScoreManager = new HighScoreManager();

        mainMenuPanel = new MainMenuPanel();
        gamePanel = new GamePanel(this, soundManager, highScoreManager);
        highScorePanel = new HighScorePanel();

        mainPanel.add(mainMenuPanel, MENU_PANEL);
        mainPanel.add(gamePanel, GAME_PANEL);
        mainPanel.add(highScorePanel, HIGHSCORE_PANEL);

        setupListeners();

        add(mainPanel);

        setTitle("Jakarta Rampage");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void setupListeners() {
        mainMenuPanel.addStartButtonListener(e -> startGame());
        mainMenuPanel.addHighScoreButtonListener(e -> showHighScores());
        mainMenuPanel.addExitButtonListener(e -> System.exit(0));
        highScorePanel.addBackButtonListener(e -> showMenu());
    }

    private void startGame() {
        gamePanel.startGame();
        cardLayout.show(mainPanel, GAME_PANEL);
        gamePanel.requestFocusInWindow();
    }

    public void showMenu() {
        cardLayout.show(mainPanel, MENU_PANEL);
    }

    private void showHighScores() {
        highScorePanel.updateScores();
        cardLayout.show(mainPanel, HIGHSCORE_PANEL);
    }
}