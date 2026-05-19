package com.undefinedbehaviourgames.grizzly;

import static tidal.TidalMusicPlatform.TIDAL_SIGN_IN_REQUEST_CODE;
import static youtube.YoutubeMusicPlatform.GOOGLE_REQUEST_SELECT_ACCOUNT;

import android.accounts.AccountManager;
import android.app.Activity;
import android.content.Intent;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import net.openid.appauth.AuthorizationException;
import net.openid.appauth.AuthorizationResponse;

import spotify.SpotifyMusicPlatform;

public class MainActivity extends SingleFragmentActivity {


    private static final String TAG = "MainActivityLogger";
    @Override
    public Fragment createFragment() {
        return MainFragment.newInstance();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode != Activity.RESULT_OK) return;
        Log.d(TAG, Integer.toString(resultCode));

        switch (requestCode) {
            case SpotifyMusicPlatform.SPOTIFY_SIGN_IN_REQUEST_CODE:
                AuthorizationResponse response = AuthorizationResponse.fromIntent(data);
                AuthorizationException exception = AuthorizationException.fromIntent(data);
                SpotifyMusicPlatform.getInstance(this).updateAuthState(response, exception);
                if (response != null) {

                    MainFragment frag = (MainFragment) getSupportFragmentManager().findFragmentById(R.id.single_fragment_container);

                    if (frag != null) {
                        frag.signInNewUser(response, MusicPlatform.Platforms.spotify);
                    }

                }

                break;

            case TIDAL_SIGN_IN_REQUEST_CODE:
                response = AuthorizationResponse.fromIntent(data);
                exception = AuthorizationException.fromIntent(data);
                SpotifyMusicPlatform.getInstance(this).updateAuthState(response, exception);
                if (response != null) {

                    MainFragment frag = (MainFragment) getSupportFragmentManager().findFragmentById(R.id.single_fragment_container);

                    if (frag != null) {
                        frag.signInNewUser(response, MusicPlatform.Platforms.tidal);
                    }

                }

                break;

            case GOOGLE_REQUEST_SELECT_ACCOUNT:
                String accountName = data.getStringExtra(AccountManager.KEY_ACCOUNT_NAME);
                Log.d(TAG, accountName);
        }





    }

}