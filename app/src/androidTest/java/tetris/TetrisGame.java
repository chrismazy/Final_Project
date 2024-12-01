package com.example.tetris;

import java.util.Arrays;

public class TetrisGame {
    private int[][] grid;
    private int score;
    private int level;
    private boolean gameOver;

    public TetrisGame(int rows, int cols) {
        grid = new int[rows][cols];
        score = 0;
        level = 1;
        gameOver = false;
    }

    public void checkLines() {
        int linesCleared = 0;
        for (int row = grid.length - 1; row >= 0; row--) {
            if (isLineFull(row)) {
                removeLine(row);
                linesCleared++;
                row++; // 重新檢查這一行
            }
        }
        updateScore(linesCleared);
    }

    private boolean isLineFull(int row) {
        for (int col = 0; col < grid[row].length; col++) {
            if (grid[row][col] == 0) {
                return false;
            }
        }
        return true;
    }

    private void updateScore(int linesCleared) {
        switch (linesCleared) {
            case 1: score += 100 * level; break;
            case 2: score += 300 * level; break;
            case 3: score += 500 * level; break;
            case 4: score += 800 * level; break;
        }

        // 每1000分提升一個等級
        level = (score / 1000) + 1;
    }

    public int[][] getGrid() {
        return grid;
    }

    public int getScore() {
        return score;
    }

    public int getLevel() {
        return level;
    }

    public boolean isGameOver() {
        return gameOver;
    }

    public void setGameOver(boolean status) {
        this.gameOver = status;
    }
    public void addDropBonus() {
        // 給予快速下落的額外獎勵分數
        score += 10 * level;
    }
}