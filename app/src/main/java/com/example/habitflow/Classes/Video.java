package com.example.habitflow.Classes;

public class Video {
    private String videoURL;
    private String videoTitle;
    private String videoDescription;

    public Video(String videoURL, String videoTitle, String videoDescription) {
        this.videoURL = videoURL;
        this.videoTitle = videoTitle;
        this.videoDescription = videoDescription;
    }

    public String getVideoURL() {
        return videoURL;
    }

    public void setVideoURL(String videoURL) {
        this.videoURL = videoURL;
    }

    public String getVideoTitle() {
        return videoTitle;
    }

    public void setVideoTitle(String videoTitle) {
        this.videoTitle = videoTitle;
    }

    public String getVideoDescription() {
        return videoDescription;
    }

    public void setVideoDescription(String videoDescription) {
        this.videoDescription = videoDescription;
    }
}
