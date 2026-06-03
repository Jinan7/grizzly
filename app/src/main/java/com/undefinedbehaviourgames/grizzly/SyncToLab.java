package com.undefinedbehaviourgames.grizzly;

import java.util.ArrayList;
import java.util.List;

import spotify.SyncToSpotify;
import tidal.SyncToTidal;

public class SyncToLab {

    private List<SyncTo> mSyncToList;

    public SyncToLab() {
        mSyncToList = new ArrayList<>();
        mSyncToList.add(new SyncToSpotify());
        mSyncToList.add(new SyncToTidal());
    }

    public List<SyncTo> getSyncToList() {
        return mSyncToList;
    }
}
