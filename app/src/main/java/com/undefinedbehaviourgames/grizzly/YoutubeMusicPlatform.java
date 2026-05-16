package com.undefinedbehaviourgames.grizzly;

import android.content.Context;

public class YoutubeMusicPlatform extends MusicPlatform {
    private  static final String TAG = "youtube music";
    public static YoutubeMusicPlatform getInstance(Context context) {
        return new YoutubeMusicPlatform();
    }
}
