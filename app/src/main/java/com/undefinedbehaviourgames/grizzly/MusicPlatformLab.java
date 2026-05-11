package com.undefinedbehaviourgames.grizzly;

import com.undefinedbehaviourgames.grizzly.MusicPlatform.MusicPlatforms;

import java.util.ArrayList;
import java.util.List;

public class MusicPlatformLab {


    private static MusicPlatformLab sMusicPlatformLab;
    private List<MusicPlatform> mMusicPlatforms;
    private MusicPlatformLab() {
        mMusicPlatforms = new ArrayList<>();
        mMusicPlatforms.add(MusicPlatform.getInstance(R.drawable.apple_music, "Apple Music", Platforms.APPLE_MUSIC));
        mMusicPlatforms.add(MusicPlatform.getInstance(R.drawable.spotify, "Spotify", Platforms.SPOTIFY));
        mMusicPlatforms.add(MusicPlatform.getInstance(R.drawable.youtube, "Youtube Music", Platforms.YOUTUBE));
    }

    public static MusicPlatformLab get() {

        if (sMusicPlatformLab == null) {
            sMusicPlatformLab = new MusicPlatformLab();
        }

        return sMusicPlatformLab;
    }

    public List<MusicPlatform> getMusicPlatforms() {
        return mMusicPlatforms;
    }
}
