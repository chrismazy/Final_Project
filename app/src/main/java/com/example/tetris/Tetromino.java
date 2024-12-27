package com.example.tetris;

import android.graphics.Color;

public class Tetromino {
    private int[][] shape;
    private int color;

    /**
     * 複製方塊的形狀
     * 確保每個新方塊都有自己獨立的形狀
     */
    public Tetromino(int[][] shape, int color) {
        // 複製方塊的形狀
        this.shape = new int[shape.length][shape[0].length];
        for (int i = 0; i < shape.length; i++) {
            System.arraycopy(shape[i], 0, this.shape[i], 0, shape[i].length);
        }
        this.color = color;
    }

    // 其他方法保持不變
    public int[][] getShape() {
        return shape;
    }

    public int getColor() {
        return color;
    }

    public Tetromino rotate() {
        // 取得原始形狀的行數和列數
        int rows = shape.length;
        int cols = shape[0].length;

        // 創建新的形狀陣列，注意行列互換
        int[][] newShape = new int[cols][rows];

        // 執行旋轉：將原始陣列順時針旋轉
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                newShape[j][rows - 1 - i] = shape[i][j];
            }
        }

        // 返回新的方塊實例
        return new Tetromino(newShape, color);
    }


    public static Tetromino createLine() {
        return new Tetromino(new int[][]{{1,1,1,1}}, Color.CYAN);
    }

    public static Tetromino createSquare() {
        return new Tetromino(new int[][]{
                {1,1},
                {1,1}}, Color.YELLOW);
    }

    public static Tetromino createTShape() {
        return new Tetromino(new int[][]{
                {0,1,0},
                {1,1,1}
        }, Color.MAGENTA);
    }

    public static Tetromino createLShape() {
        return new Tetromino(new int[][]{
                {1,0},
                {1,0},
                {1,1}
        }, Color.BLUE);
    }

    public static Tetromino createZShape() {
        return new Tetromino(new int[][]{
                {1,1,0},
                {0,1,1}
        }, Color.RED);
    }
}