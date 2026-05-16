package com.undefinedbehaviourgames.grizzly;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;

import spotify.SpotifyMusicPlatform;

public class MusicPlatformLab {


    private static MusicPlatformLab sMusicPlatformLab;
    private List<MusicPlatform> mMusicPlatforms;
    private MusicPlatformLab(Context context) {
        mMusicPlatforms = new ArrayList<>();
        mMusicPlatforms.add(SpotifyMusicPlatform.getInstance(context));
        mMusicPlatforms.add(YoutubeMusicPlatform.getInstance(context));
        mMusicPlatforms.add(AppleMusicPlatform.getInstance(context));
    }

    public static MusicPlatformLab get(Context context) {

        if (sMusicPlatformLab == null) {
            sMusicPlatformLab = new MusicPlatformLab(context);
        }

        return sMusicPlatformLab;
    }

    public List<MusicPlatform> getMusicPlatforms() {
        return mMusicPlatforms;
    }
}
