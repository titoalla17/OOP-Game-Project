package com.mygame.utils;

import java.awt.Font;
import java.awt.FontFormatException;
import java.io.IOException;
import java.io.InputStream;

public class FontLoader {

    public static Font loadFont(String path, float size) {
        try {
            // Muat file font sebagai stream dari dalam resources
            InputStream fontStream = FontLoader.class.getResourceAsStream(path);
            if (fontStream == null) {
                throw new IOException("Font not found at: " + path);
            }
            // Buat font dari stream tersebut
            Font customFont = Font.createFont(Font.TRUETYPE_FONT, fontStream);
            // Kembalikan font dengan ukuran yang sudah disesuaikan
            return customFont.deriveFont(size);
        } catch (IOException | FontFormatException e) {
            System.err.println("Gagal memuat font kustom. Menggunakan font fallback.");
            e.printStackTrace();
            // Jika gagal, kembalikan font standar agar program tidak crash
            return new Font("Arial", Font.PLAIN, (int) size);
        }
    }
}