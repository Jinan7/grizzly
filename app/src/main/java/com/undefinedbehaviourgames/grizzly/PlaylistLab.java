package com.undefinedbehaviourgames.grizzly;

import java.util.ArrayList;
import java.util.List;

public class PlaylistLab {

    private static PlaylistLab sPlaylistLab;
    private List<Playlist> mPlaylist;
    private State state; //variable to store the current state of playlistlab
    private String userId; //variable to store whose playlist is  currently loaded in playlistlab
    private int total; //variable to store the total number of playlist to be fetched
    private int fetched; //variable to store the total number of items fetched
    private String platform;
    private PlaylistLab() {
        state = State.IDLE;
        mPlaylist = new ArrayList<>();
    }

    public static PlaylistLab getInstance() {

        if (sPlaylistLab == null) {
            sPlaylistLab = new PlaylistLab();
        }

        return sPlaylistLab;
    }

    public List<Playlist> getPlaylist() {
        return mPlaylist;
    }

    public void setPlaylist(List<Playlist> playlist) {
        mPlaylist = playlist;
    }

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public boolean fetchComplete() {
        return mPlaylist.size() == total;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public int getFetched() {
        return fetched;
    }

    public void setFetched(int fetched) {
        this.fetched = fetched;
    }

    public void add(Playlist playlist) {
        mPlaylist.add(playlist);
    }

    public String getPlatform() {
        return platform;
    }

    public void setPlatform(String platform) {
        this.platform = platform;
    }
}
