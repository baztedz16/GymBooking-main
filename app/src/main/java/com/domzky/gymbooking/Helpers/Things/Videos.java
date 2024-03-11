package com.domzky.gymbooking.Helpers.Things;

public class Videos {
    String title,link,thumbnail;

    public Videos(String title,String link,String thumbnail){
      this.title = title;
      this.link = link;
      this.thumbnail = thumbnail;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }
}
