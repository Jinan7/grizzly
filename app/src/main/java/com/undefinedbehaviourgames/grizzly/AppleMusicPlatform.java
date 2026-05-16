package com.undefinedbehaviourgames.grizzly;

import android.content.Context;

public class AppleMusicPlatform extends MusicPlatform {

    private  static final String TAG = "apple music";
    public static AppleMusicPlatform getInstance(Context context) {
        return new AppleMusicPlatform();
    }
}
