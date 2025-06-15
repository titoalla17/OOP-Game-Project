package com.mygame.utils;

import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class HighScoreManager {
    private static final String HIGHSCORE_FILE = "highscores.dat";
    private static final int MAX_SCORES = 5;

    @SuppressWarnings("unchecked")
    public List<Integer> loadScores() {
        List<Integer> scores = new ArrayList<>();
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(HIGHSCORE_FILE))) {
            scores = (List<Integer>) ois.readObject();
        } catch (FileNotFoundException e) {
            // File belum ada, normal untuk pertama kali.
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return scores;
    }

    public void saveScore(int newScore) {
        List<Integer> scores = loadScores();
        scores.add(newScore);
        scores.sort(Collections.reverseOrder());

        List<Integer> topScores = new ArrayList<>(scores.subList(0, Math.min(scores.size(), MAX_SCORES)));

        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(HIGHSCORE_FILE))) {
            oos.writeObject(topScores);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}