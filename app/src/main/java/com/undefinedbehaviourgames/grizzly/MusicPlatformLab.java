package com.undefinedbehaviourgames.grizzly;

import java.util.ArrayList;
import java.util.List;

public class MusicPlatformLab {


    private static MusicPlatformLab sMusicPlatformLab;
    private List<MusicPlatform> mMusicPlatforms;
    private MusicPlatformLab() {
        mMusicPlatforms = new ArrayList<>();
        mMusicPlatforms.add(MusicPlatform.getInstance(R.drawable.apple_music, "Apple Music"));
        mMusicPlatforms.add(MusicPlatform.getInstance(R.drawable.spotify, "Spotify"));
        mMusicPlatforms.add(MusicPlatform.getInstance(R.drawable.youtube, "Youtube Music"));
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
