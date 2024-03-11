package com.domzky.gymbooking.Sessions.Admin.pages.Videos;

public class LinkItem {
    private String title;
    private String thumbnail;
    private String videoUrl;

    public LinkItem(String title,String videoUrl, String thumbnail) {
        this.title = title;
        this.thumbnail = thumbnail;
        this.videoUrl = videoUrl;
    }

    public String getTitle() {
        return title;
    }

    public String getImageUrl() {
        return thumbnail;
    }

    public String getvideoUrl() {
        return videoUrl;
    }
}
