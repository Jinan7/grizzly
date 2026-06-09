package com.undefinedbehaviourgames.grizzly;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;

public class PlaylistActivity extends SingleFragmentActivity {

    private static final String EXTRA_PLAYLIST_ID = "com.undefinedbehaviourgames.grizzly.playlist_id";
    private static final String EXTRA_PLATFORM = "com.undefinedbehaviourgames.grizzly.playlist_activity.platform";
    private static final String EXTRA_PLAYLIST_NAME = "com.undefinedbehaviourgames.grizzly.playlist_name";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public Fragment createFragment() {
        String playlistId = getIntent().getStringExtra(EXTRA_PLAYLIST_ID);
        String musicServiceName = getIntent().getStringExtra(EXTRA_PLATFORM);
        String playlistName = getIntent().getStringExtra(EXTRA_PLAYLIST_NAME);
        return PlaylistFragment.newInstance(playlistId, musicServiceName, playlistName);
    }

    public static Intent newIntent(Context context, String playlistId, String musicServiceName, String playlistName) {
        Intent intent = new Intent(context, PlaylistActivity.class);
        intent.putExtra(EXTRA_PLAYLIST_ID, playlistId);
        intent.putExtra(EXTRA_PLATFORM, musicServiceName);
        intent.putExtra(EXTRA_PLAYLIST_NAME, playlistName);
        return intent;
    }
}