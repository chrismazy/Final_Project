package com.example.tetris;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

public class MainMenuActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        TextView titleText = findViewById(R.id.titleText);
        Button startButton = findViewById(R.id.startButton);
        Button aboutButton = findViewById(R.id.aboutButton);
        Button exitButton = findViewById(R.id.exitButton);

        // 載入動畫
        Animation fadeIn = AnimationUtils.loadAnimation(this, R.anim.fade_in);
        titleText.startAnimation(fadeIn);

        // 設置按鈕點擊事件
        startButton.setOnClickListener(v -> {
            Intent intent = new Intent(MainMenuActivity.this, MainActivity.class);
            startActivity(intent);
        });

        aboutButton.setOnClickListener(v -> {
            showAboutDialog();
        });

        exitButton.setOnClickListener(v -> {
            finish();
        });
    }

    private void showAboutDialog() {
        new android.app.AlertDialog.Builder(this)
                .setTitle("關於我們")
                .setMessage("俄羅斯方塊 v1.0\n\n" +
                        "遊戲說明：\n" +
                        "← →：左右移動\n" +
                        "↻：旋轉\n" +
                        "▼：慢速下落\n" +
                        "⚡：快速下落\n\n"
                       )
                .setPositiveButton("確定", null)
                .show();
    }
}