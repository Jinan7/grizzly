package com.undefinedbehaviourgames.grizzly;

import java.util.ArrayList;
import java.util.List;

public class SyncTask {

    private int mTotal;
    private int progress;
    private String playlistName;
    private String mSyncTo;
    private String mSyncFrom;

    private List<SyncItem> items;

    public SyncTask(String syncTo, String syncFrom) {
        mSyncTo = syncTo;
        mSyncFrom = syncFrom;
        playlistName = "New Playlist";
        items = new ArrayList<>();
        progress = 0;
    }

    public void addItem(SyncItem item) {
        items.add(item);
    }

    public void setPlaylistName(String name) {
        playlistName = name;
    }

    public void addProgress() {
        progress += 1;
    }

    public int getTotal() { return mTotal;}
    public void setTotal(int total) {
        mTotal = total;
    }
    public int getProgress() { return progress;}
    public String getSyncTo() {return mSyncTo;}
    public String getSyncFrom() {return mSyncFrom;}
    public String getPlaylistName() {return playlistName;}
    public List<SyncItem> getItems() { return items;}
}
