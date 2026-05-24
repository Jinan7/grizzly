package amazon;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;

import androidx.annotation.Nullable;

import com.undefinedbehaviourgames.grizzly.MusicPlatform;
import com.undefinedbehaviourgames.grizzly.PlaylistLab;
import com.undefinedbehaviourgames.grizzly.R;
import com.undefinedbehaviourgames.grizzly.State;

import net.openid.appauth.AuthState;
import net.openid.appauth.AuthorizationException;
import net.openid.appauth.AuthorizationRequest;
import net.openid.appauth.AuthorizationResponse;
import net.openid.appauth.AuthorizationService;
import net.openid.appauth.AuthorizationServiceConfiguration;
import net.openid.appauth.ResponseTypeValues;
import net.openid.appauth.TokenResponse;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Query;
import spotify.SpotifyAccount;
import spotify.SpotifyLibrary;
import spotify.SpotifyMusicPlatform;

public class AmazonMusicPlatform extends MusicPlatform {

    private static AmazonMusicPlatform mSingleton;

    private final String BASE_URL = "https://openapi.tidal.com/v2/";
    private static final String CLIENT_ID = "amzn1.application-oa2-client.a57779056dda4f05848d401a64fe2481";
    private static final String REDIRECT_URI = "https://grizzly.undefinedbehaviourgames.com/oauth2redirect";
    private static final String AUTH_URL = "https://www.amazon.com/ap/oa";
    private static final String TOKEN_URL = "https://api.amazon.co.uk/auth/o2";
    private static final String SCOPE = "profile profile:user_id postal_code";
    public static final int AMAZON_SIGN_IN_REQUEST_CODE = 2;

    private AuthorizationServiceConfiguration mServiceConfig;
    private AuthorizationRequest mAuthorizationRequest;
    private AuthorizationService mAuthorizationService;
    private AuthState mAuthState;
    private AmazonService mAmazonService;
    private Context mContext;

    private boolean cancelSignIn;


    private AmazonMusicPlatform(Context context) {
        mContext = context.getApplicationContext();
        cancelSignIn = false;
    }
    public static AmazonMusicPlatform getInstance(Context context) {
        if (mSingleton == null) {
            mSingleton = new AmazonMusicPlatform(context);
            mSingleton.setIconResourceId(R.drawable.amazon);
            mSingleton.setPlatformName(Platforms.amazon);
            mSingleton.TAG = Platforms.amazon;
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
        mAmazonService = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(AmazonService.class);

    }

    @Override
    public void authorize(Activity activity) {
        super.authorize(activity);
        mAuthorizationService = new AuthorizationService(mContext);
        Intent authIntent = mAuthorizationService.getAuthorizationRequestIntent(mAuthorizationRequest);
        activity.startActivityForResult(authIntent, AMAZON_SIGN_IN_REQUEST_CODE);

    }

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
                                    Log.d(TAG, token);
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

    public void getUser(String token, Callbacks callbacks) {
        mAmazonService.getUser(token).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                //check for cancellation flag
                if(!cancelSignIn) {
                    if (response.isSuccessful()) {

                        try {
                            Log.d(TAG, response.body().string());
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
//                        cancelSignIn = false;
//                        if(callbacks != null) callbacks.onSignInFinished(response.body());

                    } else {
                        Log.d(TAG, response.toString());
                        cancelSignIn = false;
                        if(callbacks != null) callbacks.onSignInError();
                    }


                    //display a toast if sign in is not successful

                } else {
                    //cancel sign in and reset flag
                    if( callbacks != null) callbacks.cancelSignIn();
                    cancelSignIn = false;
                }

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                if(callbacks != null) callbacks.onSignInError();
            }
        });
    }

    @Override
    public void fetchPlaylists(String userId, SpotifyMusicPlatform.PlaylistFetchTask.Callbacks callbacks) {
        super.fetchPlaylists(userId, callbacks);

        mAuthState.performActionWithFreshTokens(mAuthorizationService, new AuthState.AuthStateAction() {
            @Override
            public void execute(@Nullable String accessToken, @Nullable String idToken, @Nullable AuthorizationException ex) {

                if (ex != null) {
                    return;
                }

                String token = "Bearer " + accessToken;
                Log.d(TAG, token);
                getPlaylists(token, userId, callbacks);
            }
        });
    }

    public void getPlaylists(String token, String userId, SpotifyMusicPlatform.PlaylistFetchTask.Callbacks callbacks) {

        String playlistLabUserId = PlaylistLab.getInstance().getUserId();
        String playListLabPlatform = PlaylistLab.getInstance().getPlatform();
        //start a new request if the current loaded playlist does not belong to the requesting user or
        //the current loaded playlist belongs to a null user
        //the current fetched playlist is incomplete
        if (playListLabPlatform == null || !playListLabPlatform.equals(TAG)|| playlistLabUserId == null || !playlistLabUserId.equals(userId) || PlaylistLab.getInstance().getState() != State.FETCHED)
        {
            //set total to -1 for pass fetchComplete() test in case anything goes wrong
            //if this is not set and an error occurs before any fetch is made
            //incase app try to fetch playlist with this same user consecutively
            //the first two conditions will fail
            //the last condition will be 0 == 0 which is true and will negate to false which will also fail
            //so it will never be possible to fetch again unless another user tries to fetch and resets the first two conditions of the 'if' statement
            //setting to -1 will ensure that any consecutive attempt will yield -1 == 0 which will pass

            PlaylistLab.getInstance().setTotal(-1);
            PlaylistLab.getInstance().setState(State.FETCHING);
            PlaylistLab.getInstance().setUserId(userId);
            PlaylistLab.getInstance().setPlaylist(new ArrayList<>());

//            getPlaylistsRecursive(token, userId, callbacks, 0, 0);

            mAmazonService.getPlaylists(token, "me").enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    if (response.isSuccessful()) {

                        try {
                            Log.d(TAG, response.body().string());
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
//                        SpotifyLibrary library = response.body();
//                        library.setOwner(userId);
//                        new SpotifyMusicPlatform.PlaylistFetchTask(library, callbacks).execute(library);

//                        Log.d(TAG, String.valueOf(library.getOffset()));
//                        if (library.getNext() != null) {
//                            getPlaylistsRecursive(token, userId, callbacks, library.getOffset() + library.getItems().size(), depth+1);
//                        }
                    } else {
                        Log.d(TAG, response.toString());
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {

                }
            });
        }

    }

    //spotify service interface
    public interface AmazonService {

        //get user profile
        @GET("users/me")
        Call<ResponseBody> getUser(@Header("Authorization") String token);

        //get user playlist
        @GET("playlists")
        Call<ResponseBody> getPlaylists(@Header("Authorization") String token, @Query("filter[owners.id]") String userId);

    }
}
