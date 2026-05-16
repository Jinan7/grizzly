package com.undefinedbehaviourgames.grizzly;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Base64;
import android.util.Log;


import androidx.annotation.Nullable;

import net.openid.appauth.AuthState;
import net.openid.appauth.AuthorizationException;
import net.openid.appauth.AuthorizationRequest;
import net.openid.appauth.AuthorizationResponse;
import net.openid.appauth.AuthorizationService;
import net.openid.appauth.AuthorizationServiceConfiguration;
import net.openid.appauth.ResponseTypeValues;
import net.openid.appauth.TokenResponse;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;

public class SpotifyMusicPlatform extends MusicPlatform {



    private static SpotifyMusicPlatform mSingleton;

    private final String BASE_URL = "https://api.spotify.com/v1/";
    private static final String AUTH_URL = "https://accounts.spotify.com/authorize";
    private static final String TOKEN_URL = "https://accounts.spotify.com/api/token";
    private static final String CLIENT_ID = "d463534ec77b4599be1f8178143905d0";
    private static final String REDIRECT_URI = "com.undefinedbehaviourgames.grizzly://callback";
    private static final String SCOPE = "user-read-private user-read-email";
    public static final int SPOTIFY_SIGN_IN_REQUEST_CODE = 0;

    private AuthorizationServiceConfiguration mServiceConfig;
    private AuthorizationRequest mAuthorizationRequest;
    private AuthorizationService mAuthorizationService;
    private AuthState mAuthState;
    private SpotifyService mSpotifyService;
    private Context mContext;

    private boolean cancelSignIn;



    private SpotifyMusicPlatform(Context context) {
        mContext = context.getApplicationContext();
        cancelSignIn = false;
    }
    public static SpotifyMusicPlatform getInstance(Context context) {
        if (mSingleton == null) {
            mSingleton = new SpotifyMusicPlatform(context);
            mSingleton.setIconResourceId(R.drawable.spotify);
            mSingleton.setPlatformName("Spotify");
            mSingleton.TAG = "spotify";
            mSingleton.configure();
        }
        return mSingleton;
    }

    public void updateAuthState(AuthorizationResponse res, AuthorizationException ex) {
        mAuthState.update(res, ex);
    }
    @Override
    public void configure() {
        super.configure();

        //create authorization service object
        mServiceConfig = new AuthorizationServiceConfiguration( Uri.parse(AUTH_URL), Uri.parse(TOKEN_URL));
        mAuthState = new AuthState(mServiceConfig);


        //create authorization request object
        AuthorizationRequest.Builder authRequestBuilder =
                new AuthorizationRequest.Builder(
                        mServiceConfig,
                        CLIENT_ID,
                        ResponseTypeValues.CODE,
                        Uri.parse(REDIRECT_URI)
                )
                        .setScope(SCOPE);

        try {

            //generatate code verifier and code challenge and add it to authorization request builder
            String codeVerifier = getCodeVerifier();
            String codeVerifierChallenge = getCodeChallenge(codeVerifier);
            authRequestBuilder.setCodeVerifier(codeVerifier, codeVerifierChallenge, AuthorizationRequest.CODE_CHALLENGE_METHOD_S256);
        } catch (Exception e){
            e.printStackTrace();
        }

        mAuthorizationRequest = authRequestBuilder.build();

        //use retrofit to create spotify service class
        //which will be used to query spotify api after
        //access token is gotten
        mSpotifyService = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(SpotifyService.class);

    }


    @Override
    public void authorize(Activity activity) {
        super.authorize(activity);
        mAuthorizationService = new AuthorizationService(mContext);
        Intent authIntent = mAuthorizationService.getAuthorizationRequestIntent(mAuthorizationRequest);
        activity.startActivityForResult(authIntent, SPOTIFY_SIGN_IN_REQUEST_CODE);

    }

    @Override
    public void signIn(AuthorizationResponse response, Callbacks callbacks) {
        super.signIn(response, callbacks);

        //after first authorization request with the code verifier
        //use the code obtained in the response to get token
        //auth service provides this convenience method
        //to make the request directly with the response object
        //instead of having to extract request token

        //check for cancellation flag
        if (!cancelSignIn) {
            mAuthorizationService.performTokenRequest(response.createTokenExchangeRequest(), new AuthorizationService.TokenResponseCallback() {
                @Override
                public void onTokenRequestCompleted(@Nullable TokenResponse response, @Nullable AuthorizationException ex) {

                    if (!cancelSignIn) {
                        if (response != null) {
                            mAuthState.update(response, ex);

                            mAuthState.performActionWithFreshTokens(mAuthorizationService, new AuthState.AuthStateAction() {
                                @Override
                                public void execute(@Nullable String accessToken, @Nullable String idToken, @Nullable AuthorizationException ex) {

                                    if (ex != null) {
                                        //for now cancel sign in if there is an error
                                        //later show a toast
                                        cancelSignIn = false;
                                        callbacks.cancelSignIn();
                                        return;
                                    }

                                    String token = "Bearer " + accessToken;
                                    getUser(token, callbacks);
                                }
                            });
                        } else {

                            //show toast message on sign in error
                            cancelSignIn = false;
                            callbacks.onSignInError();
                        }
                    } else {
                        callbacks.cancelSignIn();
                        cancelSignIn = false;
                    }



                }
            });
        } else {
            //reset cancel sign in flag and dismiss call fragment cancel implementation
            callbacks.cancelSignIn();
            cancelSignIn = false;
        }




    }

    public void cancel() {
        cancelSignIn = true;
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

    //get user
    public void getUser(String token, Callbacks callbacks) {
        mSpotifyService.getUser(token).enqueue(new Callback<SpotifyAccount>() {
            @Override
            public void onResponse(Call<SpotifyAccount> call, Response<SpotifyAccount> response) {

                //check for cancellation flag
                if(!cancelSignIn) {
                    if (response.isSuccessful()) {

//                        try {
                        Log.d(TAG, response.body().toString());
                        cancelSignIn = false;
                        callbacks.onSignInFinished(response.body());
//                        }
//                        catch (IOException e) {
//                            cancelSignIn = false;
//                            callbacks.onSignInError();
//                            e.printStackTrace();
//                        }
                    } else {
                        cancelSignIn = false;
                        callbacks.onSignInError();
                    }


                    //display a toast if sign in is not successful

                } else {
                    //cancel sign in and reset flag
                    callbacks.cancelSignIn();
                    cancelSignIn = false;
                }

            }

            @Override
            public void onFailure(Call<SpotifyAccount> call, Throwable t) {
                callbacks.onSignInError();
            }
        });
    }



    //spotify service interface
     public interface SpotifyService {

        //get user profile
        @GET("me")
        Call<SpotifyAccount> getUser(@Header("Authorization") String token);

        //get user playlist
        @GET("users/{user_id}/playlists")
        Call<ResponseBody> getPlaylists(@Path("user_id") String id);

    }




}
