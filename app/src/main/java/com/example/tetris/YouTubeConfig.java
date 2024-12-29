package com.example.tetris;

public class YouTubeConfig {
    // Replace with your actual API key
    private static final String API_KEY = "";
    // Replace with your actual video ID
    private static final String VIDEO_ID = "NmCCQxVBfyM";

    public static String getApiKey() {
        return API_KEY;
    }

    public static String getVideoId() {
        return VIDEO_ID;
    }

    public static String getYouTubeUrl() {
        return "https://www.youtube.com/watch?v=" + VIDEO_ID;
    }
}
