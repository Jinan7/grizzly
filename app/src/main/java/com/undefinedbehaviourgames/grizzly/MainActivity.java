package com.undefinedbehaviourgames.grizzly;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;

import net.openid.appauth.AuthorizationException;
import net.openid.appauth.AuthorizationResponse;

public class MainActivity extends SingleFragmentActivity {


    private static final String TAG = "MainActivityLogger";
    @Override
    public Fragment createFragment() {
        return MainFragment.newInstance();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Log.d(TAG, Integer.toString(resultCode));
        if (requestCode == SpotifyMusicPlatform.SPOTIFY_SIGN_IN_REQUEST_CODE) {
            AuthorizationResponse response = AuthorizationResponse.fromIntent(data);
            AuthorizationException exception = AuthorizationException.fromIntent(data);
            SpotifyMusicPlatform.getInstance(this).updateAuthState(response, exception);
            if (response != null) {

                MainFragment frag = (MainFragment) getSupportFragmentManager().findFragmentById(R.id.single_fragment_container);

                if (frag != null) {
                    frag.signInNewUser(response);
                }

            }



        }
    }

}