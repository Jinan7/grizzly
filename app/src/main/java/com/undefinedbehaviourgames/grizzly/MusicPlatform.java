package com.undefinedbehaviourgames.grizzly;

public class MusicPlatform {

    int mIconResourceId;
    String mPlatformName;

    private MusicPlatform() {

    }

    public static MusicPlatform getInstance(int iconResourceId, String platformName) {
        MusicPlatform musicPlatform = new MusicPlatform();
        musicPlatform.setIconResourceId(iconResourceId);
        musicPlatform.setPlatformName(platformName);
        return musicPlatform;
    }

    public int getIconResourceId() {
        return mIconResourceId;
    }

    public void setIconResourceId(int iconResourceId) {
        mIconResourceId = iconResourceId;
    }

    public String getPlatformName() {
        return mPlatformName;
    }

    public void setPlatformName(String platformName) {
        mPlatformName = platformName;
    }
}
