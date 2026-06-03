package com.undefinedbehaviourgames.grizzly;

public abstract class PlaylistItem {

    private boolean mChecked;

    public PlaylistItem() {
        mChecked = false;
    }
    public abstract String getMusicPlatform();
    public abstract String getId();
    public abstract String getTitle();
    public abstract String getArtist();
    public boolean isChecked() {
        return mChecked;
    }

    public void setChecked(boolean checked) {
        mChecked = checked;
    }
}
