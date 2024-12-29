package com.example.tetris;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.model.Video;
import com.google.api.services.youtube.model.VideoListResponse;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Executors;

public class YouTubeApiClient {
    private Context context;
    private YouTube youtube;

    public YouTubeApiClient(Context context) {
        this.context = context;
        // 初始化 YouTube API 客戶端
        youtube = new YouTube.Builder(
                new NetHttpTransport(),
                JacksonFactory.getDefaultInstance(),
                request -> {  // No need to cast to HttpRequestInitializer
                    // 設置超時時間等
                    request.setConnectTimeout(5000);
                    request.setReadTimeout(5000);
                }
        ).setApplicationName("TetrisApp").build();
    }

    public void openYouTubeVideo() {
        // 首先驗證影片和 API key
        new VideoValidationTask().execute(YouTubeConfig.getVideoId());
    }

    private class VideoValidationTask extends AsyncTask<String, Void, Boolean> {
        @Override
        protected Boolean doInBackground(String... params) {
            try {
                // 使用 API 檢查影片狀態
                YouTube.Videos.List request = youtube.videos().list("status");
                request.setKey(YouTubeConfig.getApiKey());
                request.setId(params[0]); // 傳入單個影片 ID

                VideoListResponse response = request.execute();
                List<Video> videos = response.getItems();

                return videos != null && !videos.isEmpty();
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }
        }

        @Override
        protected void onPostExecute(Boolean isValid) {
            if (isValid) {
                try {
                    // 驗證成功，嘗試用 YouTube app 開啟
                    Intent appIntent = new Intent(Intent.ACTION_VIEW,
                            Uri.parse("vnd.youtube:" + YouTubeConfig.getVideoId()));
                    context.startActivity(appIntent);
                } catch (Exception ex) {
                    // 如果沒有安裝 YouTube app，使用瀏覽器開啟
                    Intent webIntent = new Intent(Intent.ACTION_VIEW,
                            Uri.parse(YouTubeConfig.getYouTubeUrl()));
                    context.startActivity(webIntent);
                }
            }
        }
    }
}

