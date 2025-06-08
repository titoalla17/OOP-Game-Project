// File: src/com/mygame/Main.java
package com.mygame;

import javax.swing.SwingUtilities;

public class Main {
    public static void main(String[] args) {
        // Jalankan GUI di Event Dispatch Thread (cara aman di Swing)
        SwingUtilities.invokeLater(() -> {
            new GameFrame();
        });
    }
}