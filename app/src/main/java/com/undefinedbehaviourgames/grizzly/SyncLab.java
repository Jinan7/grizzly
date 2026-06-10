package com.undefinedbehaviourgames.grizzly;

public class SyncLab {

    private static SyncLab sSingleton;

    private SyncLab() {}

    private static SyncLab getInstance() {

        if (sSingleton == null) {
            sSingleton = new SyncLab();
        }

        return sSingleton;
    }
}
