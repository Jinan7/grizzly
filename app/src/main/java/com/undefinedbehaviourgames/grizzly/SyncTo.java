package com.undefinedbehaviourgames.grizzly;

public abstract class SyncTo {

    protected String mPlatformName;
    protected Boolean mSync;
    protected int mIconResourceId;

    public SyncTo() {
        mSync = false;
    }

    public void setPlatformName(String platformName) {
        mPlatformName = platformName;
    }
    public String getPlatformName() {
        return mPlatformName;
    }
    public void setSync(boolean sync) {
        mSync = sync;
    }
    public boolean shouldSync() {
        return mSync;
    }

    public int getIconResourceId() {
        return mIconResourceId;
    }

    public void setIconResourceId(int iconResourceId) {
        mIconResourceId = iconResourceId;
    }
}
