// MainActivity.java
package com.example.tetris;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    private TetrisView tetrisView;
    private TextView scoreText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tetrisView = findViewById(R.id.tetrisView);
        scoreText = findViewById(R.id.scoreText);

        Button leftButton = findViewById(R.id.leftButton);
        Button rightButton = findViewById(R.id.rightButton);
        Button rotateButton = findViewById(R.id.rotateButton);
        Button dropButton = findViewById(R.id.dropButton);
        Button slowDropButton = findViewById(R.id.slowDropButton);

        leftButton.setOnClickListener(v -> tetrisView.moveTetromino(TetrominoDirection.LEFT));
        rightButton.setOnClickListener(v -> tetrisView.moveTetromino(TetrominoDirection.RIGHT));
        rotateButton.setOnClickListener(v -> tetrisView.moveTetromino(TetrominoDirection.ROTATE));
        dropButton.setOnClickListener(v -> tetrisView.dropTetromino());
        slowDropButton.setOnClickListener(v -> tetrisView.toggleSlowDrop());

        tetrisView.setOnScoreUpdateListener((score, level) ->
                scoreText.setText(String.format("Score: %d Level: %d", score, level)));

        tetrisView.startGame();
    }
}