// TetrisView.java
package com.example.tetris;

import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Handler;
import android.os.Looper;
import android.util.AttributeSet;
import android.view.View;
import androidx.annotation.Nullable;

public class TetrisView extends View {
    private Paint paint;
    private Tetromino currentTetromino;
    private Tetromino nextTetromino;
    private TetrisGame game;
    private int cellSize;
    private int currentX, currentY;
    private Handler gameLoopHandler;
    private boolean isGameRunning = false;
    private static final long INITIAL_DELAY = 1000;
    private OnScoreUpdateListener scoreListener;
    private boolean isSlowDropActive = false;
    private static final long SLOW_DROP_DELAY = 200;

    public interface OnScoreUpdateListener {
        void onScoreUpdate(int score, int level);
    }

    public void setOnScoreUpdateListener(OnScoreUpdateListener listener) {
        this.scoreListener = listener;
    }

    public TetrisView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        paint = new Paint();
        paint.setAntiAlias(true);
        game = new TetrisGame(20, 10);
        gameLoopHandler = new Handler(Looper.getMainLooper());
        nextTetromino = generateRandomTetromino();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        cellSize = Math.min(w / 12, h / 20); // 調整格子大小，預留空間給預覽區
    }

    private Tetromino generateRandomTetromino() {
        double random = Math.random();
        if (random < 0.2) return Tetromino.createLine();
        else if (random < 0.4) return Tetromino.createSquare();
        else if (random < 0.6) return Tetromino.createTShape();
        else if (random < 0.8) return Tetromino.createLShape();
        else return Tetromino.createZShape();
    }

    public void startGame() {
        if (!isGameRunning) {
            isGameRunning = true;
            isSlowDropActive = false;
            game = new TetrisGame(20, 10);
            spawnTetromino();
            startGameLoop();
            if (scoreListener != null) {
                scoreListener.onScoreUpdate(0, 1);
            }
        }
    }

    private void startGameLoop() {
        Runnable gameLoop = new Runnable() {
            @Override
            public void run() {
                if (isGameRunning && !game.isGameOver()) {
                    if (isSlowDropActive) {
                        moveTetromino(TetrominoDirection.DOWN);
                        gameLoopHandler.postDelayed(this, SLOW_DROP_DELAY);
                    } else {
                        moveTetromino(TetrominoDirection.DOWN);
                        long delay = Math.max(100, INITIAL_DELAY - (game.getLevel() - 1) * 50);
                        gameLoopHandler.postDelayed(this, delay);
                    }
                }
            }
        };
        gameLoopHandler.post(gameLoop);
    }

    private boolean canPlaceTetromino(Tetromino tetromino, int x, int y) {
        int[][] shape = tetromino.getShape();
        int[][] grid = game.getGrid();

        for (int row = 0; row < shape.length; row++) {
            for (int col = 0; col < shape[row].length; col++) {
                if (shape[row][col] != 0) {
                    int newY = y + row;
                    int newX = x + col;

                    if (newY >= grid.length || newX < 0 || newX >= grid[0].length || newY < 0) {
                        return false;
                    }

                    if (newY >= 0 && grid[newY][newX] != 0) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    private void lockTetromino() {
        int[][] shape = currentTetromino.getShape();
        int[][] grid = game.getGrid();

        for (int row = 0; row < shape.length; row++) {
            for (int col = 0; col < shape[row].length; col++) {
                if (shape[row][col] != 0) {
                    grid[currentY + row][currentX + col] = currentTetromino.getColor();
                }
            }
        }

        game.checkLines();
        if (scoreListener != null) {
            scoreListener.onScoreUpdate(game.getScore(), game.getLevel());
        }
    }

    private void spawnTetromino() {
        currentTetromino = nextTetromino;
        nextTetromino = generateRandomTetromino();

        currentX = game.getGrid()[0].length / 2 - currentTetromino.getShape()[0].length / 2;
        currentY = 0;

        if (!canPlaceTetromino(currentTetromino, currentX, currentY)) {
            game.setGameOver(true);
            isGameRunning = false;
            showGameOverDialog();
        }
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        // 繪製主遊戲區背景
        paint.setColor(Color.BLACK);
        canvas.drawRect(0, 0, cellSize * 10, cellSize * 20, paint);

        // 繪製網格
        drawGrid(canvas);

        // 繪製當前方塊
        if (currentTetromino != null) {
            drawTetromino(canvas, currentTetromino, currentX, currentY);
        }

        // 繪製預覽區
        paint.setColor(Color.DKGRAY);
        canvas.drawRect(cellSize * 10, 0, cellSize * 14, cellSize * 4, paint);

        // 繪製下一個方塊
        if (nextTetromino != null) {
            drawTetromino(canvas, nextTetromino, 11, 1);
        }
    }

    private void drawGrid(Canvas canvas) {
        int[][] grid = game.getGrid();
        paint.setColor(Color.DKGRAY);

        // 繪製網格線
        for (int i = 0; i <= 20; i++) {
            canvas.drawLine(0, i * cellSize, 10 * cellSize, i * cellSize, paint);
        }
        for (int i = 0; i <= 10; i++) {
            canvas.drawLine(i * cellSize, 0, i * cellSize, 20 * cellSize, paint);
        }

        // 繪製已固定的方塊
        for (int y = 0; y < grid.length; y++) {
            for (int x = 0; x < grid[y].length; x++) {
                if (grid[y][x] != 0) {
                    paint.setColor(grid[y][x]);
                    drawCell(canvas, x, y);
                }
            }
        }
    }

    private void drawTetromino(Canvas canvas, Tetromino tetromino, int offsetX, int offsetY) {
        paint.setColor(tetromino.getColor());
        int[][] shape = tetromino.getShape();

        for (int y = 0; y < shape.length; y++) {
            for (int x = 0; x < shape[y].length; x++) {
                if (shape[y][x] != 0) {
                    drawCell(canvas, x + offsetX, y + offsetY);
                }
            }
        }
    }

    private void drawCell(Canvas canvas, int x, int y) {
        RectF rect = new RectF(
                x * cellSize + 1,
                y * cellSize + 1,
                (x + 1) * cellSize - 1,
                (y + 1) * cellSize - 1
        );
        canvas.drawRect(rect, paint);
    }

    public void moveTetromino(TetrominoDirection direction) {
        if (!isGameRunning || game.isGameOver()) return;

        int newX = currentX;
        int newY = currentY;

        switch (direction) {
            case LEFT:
                newX--;
                break;
            case RIGHT:
                newX++;
                break;
            case DOWN:
                newY++;
                break;
            case ROTATE:
                Tetromino rotated = currentTetromino.rotate();
                if (canPlaceTetromino(rotated, currentX, currentY)) {
                    currentTetromino = rotated;
                    invalidate();
                }
                return;
        }

        if (canPlaceTetromino(currentTetromino, newX, newY)) {
            currentX = newX;
            currentY = newY;
            invalidate();
        } else if (direction == TetrominoDirection.DOWN) {
            lockTetromino();
            spawnTetromino();
        }
    }

    public void dropTetromino() {
        if (!isGameRunning || game.isGameOver()) return;

        while (canPlaceTetromino(currentTetromino, currentX, currentY + 1)) {
            currentY++;
        }

        lockTetromino();
        spawnTetromino();
        invalidate();

        // 給予額外的分數獎勵
        game.addDropBonus();
        if (scoreListener != null) {
            scoreListener.onScoreUpdate(game.getScore(), game.getLevel());
        }
    }

    private void showGameOverDialog() {
        post(() -> {
            Intent intent = new Intent(getContext(), GameOverActivity.class);
            intent.putExtra("SCORE", game.getScore());
            intent.putExtra("LEVEL", game.getLevel());
            getContext().startActivity(intent);

            if (getContext() instanceof android.app.Activity) {
                ((android.app.Activity) getContext()).finish();
            }
        });
    }

    public void toggleSlowDrop() {
        if (!isGameRunning || game.isGameOver()) return;

        isSlowDropActive = !isSlowDropActive;

        // 立即重新啟動遊戲循環以應用新的下落速度
        gameLoopHandler.removeCallbacksAndMessages(null);
        startGameLoop();
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        isGameRunning = false;
        gameLoopHandler.removeCallbacksAndMessages(null);
    }

}