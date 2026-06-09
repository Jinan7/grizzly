package com.undefinedbehaviourgames.grizzly;

import java.util.ArrayList;
import java.util.List;

public class PlaylistItemLab {

    private static PlaylistItemLab sPlaylistItemLab;
    private List<PlaylistItem> mPlaylistItems;
    private String mPlatform;
    private String mPlaylistId;
    private String mPlaylistName;
    private State mState;
    private PlaylistItemLab() {
        mPlaylistItems = new ArrayList<>();
    }

    public static PlaylistItemLab getInstance() {

        if (sPlaylistItemLab == null) {
            sPlaylistItemLab = new PlaylistItemLab();
        }

        return sPlaylistItemLab;
    }

    public List<PlaylistItem> getPlaylistItems() {
        return mPlaylistItems;
    }

    public void setPlaylistItems(List<PlaylistItem> playlistItems) {
        mPlaylistItems = playlistItems;
    }

    public void add(PlaylistItem item) {
        mPlaylistItems.add(item);
    }

    public String getPlatform() {
        return mPlatform;
    }

    public void setPlatform(String platform) {
        mPlatform = platform;
    }

    public String getPlaylistId() {
        return mPlaylistId;
    }

    public void setPlaylistId(String playlistId) {
        mPlaylistId = playlistId;
    }

    public void setPlaylistName(String playlistName) { mPlaylistName = playlistName;}

    public String getPlaylistName() {return mPlaylistName;}

    public State getState() {
        return mState;
    }

    public void setState(State state) {
        mState = state;
    }

    public void checkAll(boolean checked) {
        for (PlaylistItem item : mPlaylistItems) {
            item.setChecked(checked);
        }
    }
}
