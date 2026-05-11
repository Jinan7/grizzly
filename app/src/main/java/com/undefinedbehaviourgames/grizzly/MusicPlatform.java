package com.undefinedbehaviourgames.grizzly;

enum Platforms {APPLE_MUSIC, SPOTIFY, YOUTUBE}
public class MusicPlatform {

    public static class MusicPlatforms {

        public static String youtube = "youtube";
        public static String appleMusic = "apple music";
        public static String spotify = "spotify";
    }

    String platform;
    String TAG;
    int mIconResourceId;
    String mPlatformName;

    private MusicPlatform() {

    }

    public static MusicPlatform getInstance(int iconResourceId, String platformName, Platforms platform) {
        MusicPlatform musicPlatform = new MusicPlatform();
        musicPlatform.setIconResourceId(iconResourceId);
        musicPlatform.setPlatformName(platformName);

        switch (platform){
            case SPOTIFY:
                musicPlatform.setTAG(MusicPlatforms.spotify);
                break;
            case APPLE_MUSIC:
                musicPlatform.setTAG(MusicPlatforms.appleMusic);
                break;
            case YOUTUBE:
                musicPlatform.setTAG(MusicPlatforms.youtube);
                break;
        }
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

    public String getTAG() {
        return TAG;
    }

    public void setTAG(String TAG) {
        this.TAG = TAG;
    }

    public String getPlatform() {
        return platform;
    }

    public void setPlatform(String platform) {
        this.platform = platform;
    }
}
