package com.undefinedbehaviourgames.grizzly;


import android.app.Activity;
import android.content.Context;
import android.util.Base64;

import net.openid.appauth.AuthorizationResponse;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

import spotify.SpotifyMusicPlatform;

public class MusicPlatform {

    public static class Platforms {
        public static final String spotify = "Spotify";
        public static final String tidal = "Tidal";
        public static final String youtube = "Youtube Music";
        public static final String amazon = "Amazon Music";
    }

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

    public void fetchPlaylists(String userId, FetchPlaylistCallbacks callbacks) {

    }
    public void fetchPlaylistsItems(String playlistId, FetchPlaylistItemsCallbacks callbacks) {}
    public int getIconResourceId() {
        return mIconResourceId;
    }

    public void setIconResourceId(int iconResourceId) {
        mIconResourceId = iconResourceId;
    }

    public String getPlatformName() {
        return mPlatformName;
    }

    //helper method to generate code verifier
    public String getCodeVerifier() {
        SecureRandom sr = new SecureRandom();
        byte[] code = new byte[32];
        sr.nextBytes(code);
        String codeVerifier = Base64.encodeToString(code, Base64.URL_SAFE | Base64.NO_WRAP | Base64.NO_PADDING);

        return codeVerifier;
    }

    //helper method to encrpyt code verifier to create code challenge
    public String getCodeChallenge(String codeVerifier) throws UnsupportedEncodingException, NoSuchAlgorithmException {
        byte[] bytes = codeVerifier.getBytes("US-ASCII");
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        md.update(bytes, 0, bytes.length);
        byte[] digest = md.digest();
        String codeChallenge = org.apache.commons.codec.binary.Base64.encodeBase64URLSafeString(digest);
        return codeChallenge;
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

    public interface  FetchPlaylistCallbacks {
        public void onFetchPlaylist();
    }

    public interface  FetchPlaylistItemsCallbacks {
        public void onFetchPlaylistItems();
    }

}
