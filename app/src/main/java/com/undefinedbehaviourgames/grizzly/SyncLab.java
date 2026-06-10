package com.undefinedbehaviourgames.grizzly;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.SystemClock;

import androidx.annotation.NonNull;

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
    private Handler mHandler;
    private SyncLab(Callbacks callbacks) {

        mCallbacks = new WeakReference<>(callbacks);
        mSyncTasks = new ArrayList<>();
        mExecutorService = Executors.newCachedThreadPool();

        mHandler = new Handler(Looper.getMainLooper()) {

            @Override
            public void handleMessage(@NonNull Message msg) {
                super.handleMessage(msg);

                switch (msg.what) {
                    case 0:
                        mCallbacks.get().onProgressUpdate();
                }
            }
        };
    }

    public static SyncLab getInstance(Callbacks callbacks) {

        if (sSingleton == null) {
            sSingleton = new SyncLab(callbacks);
        } else {
            sSingleton.mCallbacks = new WeakReference<>(callbacks);
        }
        return sSingleton;
    }

    public void addSyncTask(SyncTask task) {
        mSyncTasks.add(task);

        mCallbacks.get().onProgressUpdate();
        mExecutorService.submit(
               new Runnable() {

                   @Override
                   public void run() {

                       for (int i = 0; i < 50; i ++) {

                           task.addProgress();
                           mHandler.sendEmptyMessage(0);
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
