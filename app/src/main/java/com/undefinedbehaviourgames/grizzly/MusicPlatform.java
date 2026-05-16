package com.undefinedbehaviourgames.grizzly;


import android.app.Activity;

import net.openid.appauth.AuthorizationResponse;

import spotify.SpotifyMusicPlatform;

public class MusicPlatform {


    public String TAG;
    public int mIconResourceId;
    public String mPlatformName;

    protected MusicPlatform() {

    }
    public static MusicPlatform getInstance() {
        return new MusicPlatform();
    }

    public void authorize(Activity activity) {

    }


    public void configure() {

    }

    public void signIn(AuthorizationResponse response, Callbacks callbacks){
    }

    public void fetchPlaylists(String userId, SpotifyMusicPlatform.PlaylistFetchTask.Callbacks callbacks) {

    }
    public int getIconResourceId() {
        return mIconResourceId;
    }

    public void setIconResourceId(int iconResourceId) {
        mIconResourceId = iconResourceId;
    }

    public String getPlatformName() {
        return mPlatformName;
    }

    public void setPlatformName(String platformName) {
        mPlatformName = platformName;
    }

    public String getTAG() {
        return TAG;
    }

    public void setTAG(String TAG) {
        this.TAG = TAG;
    }

    public interface  Callbacks {
        public void cancelSignIn();
        public void onSignInFinished(Account account);
        public void onSignInError();
    }

}
