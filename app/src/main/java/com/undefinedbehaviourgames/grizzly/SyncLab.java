package com.undefinedbehaviourgames.grizzly;

import android.content.Context;
import android.os.SystemClock;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SyncLab {

    private static SyncLab sSingleton;

    private List<SyncTask> mSyncTasks;
    private ExecutorService mExecutorService;

    private WeakReference<Callbacks> mCallbacks;
    private SyncLab(Callbacks callbacks) {
        mCallbacks = new WeakReference<>(callbacks);
        mSyncTasks = new ArrayList<>();
        mExecutorService = Executors.newCachedThreadPool();
    }

    public static SyncLab getInstance(Callbacks callbacks) {

        if (sSingleton == null) {
            sSingleton = new SyncLab(callbacks);
        }

        return sSingleton;
    }

    public void addSyncTask(SyncTask task) {
        mSyncTasks.add(task);

        mExecutorService.submit(
               new Runnable() {

                   @Override
                   public void run() {

                       for (int i = 0; i < 50; i ++) {

                           task.addProgress();
                           mCallbacks.get().onProgressUpdate();
                           SystemClock.sleep(1000);
                       }
                   }
               }
        );
    }

    public List<SyncTask> getSyncTasks() {
        return mSyncTasks;
    }

    public interface Callbacks {

        void onProgressUpdate();
    }
}
