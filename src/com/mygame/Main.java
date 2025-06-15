package com.mygame;

import javax.swing.SwingUtilities;

public class Main {
    public static void main(String[] args) {
        // Menjalankan GUI di Event Dispatch Thread (praktik terbaik di Swing)
        SwingUtilities.invokeLater(() -> {
            new GameFrame();
        });
    }
}