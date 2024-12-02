package com.example.tetris;

import android.content.Context;
import android.graphics.Paint;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.view.View;
import androidx.annotation.Nullable;
import android.util.AttributeSet;

public class TetrisView extends View {
    private Paint paint;
    private Tetromino currentTetromino;
    private Tetromino nextTetromino;
    private OnScoreUpdateListener scoreListener;
    private Handler gameLoopHandler;
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
}
