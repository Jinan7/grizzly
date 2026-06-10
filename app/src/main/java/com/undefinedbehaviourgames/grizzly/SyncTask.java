package com.undefinedbehaviourgames.grizzly;

import java.util.ArrayList;
import java.util.List;

public class SyncTask {

    private int total;
    private int progress;
    private String playlistName;
    private String mSyncTo;

    private List<SyncItem> items;

    public SyncTask(String syncTo) {
        mSyncTo = syncTo;
        playlistName = "New Playlist";
        items = new ArrayList<>();
        total = 50;
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

    public int getTotal() { return total;}
    public int getProgress() { return progress;}
}
