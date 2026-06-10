package com.undefinedbehaviourgames.grizzly;

import java.util.ArrayList;
import java.util.List;

public class SyncTask {

    private String playlistName;
    private String mSyncTo;

    private List<SyncItem> items;

    public SyncTask(String syncTo) {
        mSyncTo = syncTo;
        playlistName = "New Playlist";
        items = new ArrayList<>();
    }

    public void addItem(SyncItem item) {
        items.add(item);
    }

    public void setPlaylistName(String name) {
        playlistName = name;
    }
}
