package com.undefinedbehaviourgames.grizzly;

public class SyncItem {

    private String mTitle;
    private String mArtist;

    public SyncItem(String title, String artist) {
        mArtist = artist;
        mTitle = title;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        mTitle = title;
    }

    public String getArtist() {
        return mArtist;
    }

    public void setArtist(String artist) {
        mArtist = artist;
    }
}
