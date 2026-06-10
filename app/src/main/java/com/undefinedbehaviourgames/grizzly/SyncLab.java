package com.undefinedbehaviourgames.grizzly;

import java.util.ArrayList;
import java.util.List;

public class SyncLab {

    private static SyncLab sSingleton;

    private List<SyncTask> mSyncTasks;
    private SyncLab() {
        mSyncTasks = new ArrayList<>();
    }

    public static SyncLab getInstance() {

        if (sSingleton == null) {
            sSingleton = new SyncLab();
        }

        return sSingleton;
    }

    public void addSyncTask(SyncTask task) {
        mSyncTasks.add(task);
    }
}
