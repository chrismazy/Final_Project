package com.example.tetris;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class GameOverActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_over);

        // 取得遊戲結果
        int score = getIntent().getIntExtra("SCORE", 0);
        int level = getIntent().getIntExtra("LEVEL", 1);

        // 找到畫面元件
        TextView scoreText = findViewById(R.id.scoreText);
        TextView levelText = findViewById(R.id.levelText);
        TextView highScoreText = findViewById(R.id.highScoreText);
        Button restartButton = findViewById(R.id.restartButton);
        Button mainMenuButton = findViewById(R.id.mainMenuButton);

        // 更新分數顯示
        scoreText.setText(String.format("得分：%d", score));
        levelText.setText(String.format("等級：%d", level));

        // 管理最高分
        SharedPreferences prefs = getSharedPreferences("TetrisPrefs", MODE_PRIVATE);
        int highScore = prefs.getInt("HIGH_SCORE", 0);

        if (score > highScore) {
            // 更新最高分
            SharedPreferences.Editor editor = prefs.edit();
            editor.putInt("HIGH_SCORE", score);
            editor.apply();
            highScore = score;
        }

        highScoreText.setText(String.format("最高分：%d", highScore));

        // 重新開始遊戲
        restartButton.setOnClickListener(v -> {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            finish();
        });

        // 返回主選單
        mainMenuButton.setOnClickListener(v -> {
            Intent intent = new Intent(this, MainMenuActivity.class);
            startActivity(intent);
            finish();
        });
    }
}