package com.domzky.gymbooking.Sessions.Admin.pages.Videos;

public class LinkItem {
    private String title;
    private String thumbnail;
    private String videoUrl,focusarea,equipment,preparation,execution;

    public LinkItem(String title, String thumbnail, String videoUrl, String focusarea, String equipment, String preparation, String execution) {
        this.title = title;
        this.thumbnail = thumbnail;
        this.videoUrl = videoUrl;
        this.focusarea = focusarea;
        this.equipment = equipment;
        this.preparation = preparation;
        this.execution = execution;
    }

    public String getTitle() {
        return title;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public String getVideoUrl() {
        return videoUrl;
    }

    public String getFocusarea() {
        return focusarea;
    }

    public String getEquipment() {
        return equipment;
    }

    public String getPreparation() {
        return preparation;
    }

    public String getExecution() {
        return execution;
    }
}
