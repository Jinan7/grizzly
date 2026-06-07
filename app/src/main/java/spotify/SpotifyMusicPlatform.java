package spotify;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Base64;
import android.util.Log;


import androidx.annotation.Nullable;

import com.undefinedbehaviourgames.grizzly.MusicPlatform;
import com.undefinedbehaviourgames.grizzly.Playlist;
import com.undefinedbehaviourgames.grizzly.PlaylistItemLab;
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
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public class SpotifyMusicPlatform extends MusicPlatform {



    private static SpotifyMusicPlatform mSingleton;

    private final String BASE_URL = "https://api.spotify.com/v1/";
    private static final String AUTH_URL = "https://accounts.spotify.com/authorize";
    private static final String TOKEN_URL = "https://accounts.spotify.com/api/token";
    private static final String CLIENT_ID = "d463534ec77b4599be1f8178143905d0";
    private static final String REDIRECT_URI = "com.undefinedbehaviourgames.grizzly://callback";
    private static final String SCOPE = "user-read-private user-read-email playlist-read-private playlist-read-collaborative";
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
            mSingleton.setPlatformName(Platforms.spotify);
            mSingleton.TAG = Platforms.spotify;
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

    @Override
    public void fetchPlaylists(String userId, FetchPlaylistCallbacks callbacks) {
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

    @Override
    public void fetchPlaylistsItems(String playlistId, FetchPlaylistItemsCallbacks callbacks) {
        super.fetchPlaylistsItems(playlistId, callbacks);

        mAuthState.performActionWithFreshTokens(mAuthorizationService, new AuthState.AuthStateAction() {
            @Override
            public void execute(@Nullable String accessToken, @Nullable String idToken, @Nullable AuthorizationException ex) {

                if (ex != null) {
                    return;
                }

                String token = "Bearer " + accessToken;
                Log.d(TAG, token);
                getPlaylistItems(token, playlistId, callbacks);
            }
        });
    }

    public void cancel() {
        cancelSignIn = true;
    }





    //get user
    public void getUser(String token, Callbacks callbacks) {
        mSpotifyService.getUser(token).enqueue(new Callback<SpotifyAccount>() {
            @Override
            public void onResponse(Call<SpotifyAccount> call, Response<SpotifyAccount> response) {

                //check for cancellation flag
                if(!cancelSignIn) {
                    if (response.isSuccessful()) {

                        Log.d(TAG, response.body().toString());
                        cancelSignIn = false;
                        if(callbacks != null) callbacks.onSignInFinished(response.body());

                    } else {
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
            public void onFailure(Call<SpotifyAccount> call, Throwable t) {
                if(callbacks != null) callbacks.onSignInError();
            }
        });
    }

    public void getPlaylistItems(String token, String playlistId, FetchPlaylistItemsCallbacks callbacks) {

        String labPlaylistId = PlaylistItemLab.getInstance().getPlaylistId();
        String labPlaylistPlatform = PlaylistItemLab.getInstance().getPlatform();

        if (labPlaylistPlatform == null || !labPlaylistPlatform.equals(Platforms.spotify) || labPlaylistId == null || !labPlaylistId.equals(playlistId) || PlaylistLab.getInstance().getState() != State.FETCHED){

            PlaylistItemLab.getInstance().setPlaylistItems(new ArrayList<>());
            PlaylistItemLab.getInstance().setPlaylistId(playlistId);
            PlaylistItemLab.getInstance().setPlaylistId(Platforms.spotify);
            PlaylistItemLab.getInstance().setState(State.FETCHING);
            mSpotifyService.getPlaylistItems(token, playlistId).enqueue(new Callback<SpotifyPlaylist>() {
                @Override
                public void onResponse(Call<SpotifyPlaylist> call, Response<SpotifyPlaylist> response) {
                    if (response.isSuccessful()) {

                        SpotifyPlaylist playlist = response.body();
                        new PlaylistItemsFetchTask(callbacks).execute(playlist);
                    }
                }

                @Override
                public void onFailure(Call<SpotifyPlaylist> call, Throwable t) {
                    Log.d(TAG, t.toString());
                }
            });
        }



    }
    public void getPlaylists(String token, String userId, FetchPlaylistCallbacks callbacks) {

        String playlistLabUserId = PlaylistLab.getInstance().getUserId();
        String platform = PlaylistLab.getInstance().getPlatform();
        //start a new request if the current loaded playlist does not belong to the requesting user or
        //the current loaded playlist belongs to a null user
        //the current fetched playlist is incomplete
        if (platform == null || !platform.equals(Platforms.spotify) || playlistLabUserId == null || !playlistLabUserId.equals(userId) || PlaylistLab.getInstance().getState() != State.FETCHED)
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
            PlaylistLab.getInstance().setPlatform(Platforms.spotify);
            PlaylistLab.getInstance().setUserId(userId);
            PlaylistLab.getInstance().setPlaylist(new ArrayList<>());

            getPlaylistsRecursive(token, userId, callbacks, 0, 0);
        }

    }

    public void getPlaylistsRecursive(String token, String userId, FetchPlaylistCallbacks callbacks, int offset, int depth) {

        //this is a recursive function
        //before executing it check how far call has gone in the recursive tree
        //if equal to or more than 50 return
        if (depth > 50) return;

        mSpotifyService.getPlaylists(token, offset).enqueue(new Callback<SpotifyLibrary>() {
            @Override
            public void onResponse(Call<SpotifyLibrary> call, Response<SpotifyLibrary> response) {
                if (response.isSuccessful()) {

                    SpotifyLibrary library = response.body();
                    library.setOwner(userId);
                    new PlaylistFetchTask(library, callbacks).execute(library);

                    Log.d(TAG, String.valueOf(library.getOffset()));
                    if (library.getNext() != null) {
                        getPlaylistsRecursive(token, userId, callbacks, library.getOffset() + library.getItems().size(), depth+1);
                    }
                } else {
                    Log.d(TAG, response.toString());
                }
            }

            @Override
            public void onFailure(Call<SpotifyLibrary> call, Throwable t) {

            }
        });
    }

    //spotify service interface
     public interface SpotifyService {

        //get user profile
        @GET("me")
        Call<SpotifyAccount> getUser(@Header("Authorization") String token);

        //get user playlist
        @GET("me/playlists")
        Call<SpotifyLibrary> getPlaylists(@Header("Authorization") String token, @Query("offset") int offset);

        //get playlist items
        @GET("playlists/{playlist_id}")
        Call<SpotifyPlaylist> getPlaylistItems(@Header("Authorization") String token, @Path("playlist_id") String id);

        /**
         *
         * @param name
         * @return
         *
         *
         * body:
         *    name: String (Required)
         *    public: boolean
         *    collaborative: boolean
         *    description: String
         *
         * Only name field used for now
         */
        @POST("me/playlists")
        Call<ResponseBody> createPlaylist(@Field("name") String name);

        /**
         *
         * @param uris
         * @return
         *
         *
         * uris - comma seperated list of spotify uris
         */
        @POST("playlists/{playlist_id}/items")
        Call<ResponseBody> addItemstoPlaylist(String[] uris);

        /**
         *
         * @param searchQuery
         * @param track
         * @param artist
         * @param type  - list of item types to return, could be artist, track, album. This call is only interested in items labeled as track
         * @return
         */
        @GET("search")
        Call<ResponseBody> search(@Query("q") String searchQuery, @Query("track") String track, @Query("artist") String artist, @Query("type") String[] type);



    }


    public static class PlaylistFetchTask extends AsyncTask<SpotifyLibrary, SpotifyLibrary.Playlist, Void> {

        private static final String TAG = "PlaylistFetchTaskLogger";
        private SpotifyLibrary mSpotifyLibrary;
        private FetchPlaylistCallbacks mCallbacks;
        public PlaylistFetchTask(SpotifyLibrary spotifyLibrary, FetchPlaylistCallbacks callbacks) {
            mSpotifyLibrary = spotifyLibrary;
            mCallbacks = callbacks;

        }
        @Override
        protected void onPostExecute(Void unused) {
            super.onPostExecute(unused);
            if (mSpotifyLibrary.getOwner().equals(PlaylistLab.getInstance().getUserId()) && mSpotifyLibrary.getNext() == null) {
                PlaylistLab.getInstance().setState(State.FETCHED);
            }
            Log.d(TAG, PlaylistLab.getInstance().getState().toString());
            mCallbacks = null;
            mSpotifyLibrary = null;
        }

        @Override
        protected void onProgressUpdate(SpotifyLibrary.Playlist... playlist) {
            super.onProgressUpdate(playlist);

            if (PlaylistLab.getInstance().getUserId().equals(mSpotifyLibrary.getOwner())) {
                PlaylistLab.getInstance().add(playlist[0]);
                if (mCallbacks != null) mCallbacks.onFetchPlaylist();

            }
        }

        @Override
        protected Void doInBackground(SpotifyLibrary... library) {


            for (SpotifyLibrary.Playlist spotifyPlaylist : library[0].getItems()) {
                spotifyPlaylist.init();
                publishProgress(spotifyPlaylist);
            }
            return null;
        }


    }


    public static class PlaylistItemsFetchTask extends AsyncTask<SpotifyPlaylist, SpotifyPlaylist.Items.Item.Track, Void> {

        FetchPlaylistItemsCallbacks mCallbacks;
        public PlaylistItemsFetchTask(FetchPlaylistItemsCallbacks callbacks) {
            mCallbacks = callbacks;
        }


        @Override
        protected void onPostExecute(Void unused) {
            super.onPostExecute(unused);
            mCallbacks = null;
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
            mCallbacks = null;
        }

        @Override
        protected void onProgressUpdate(SpotifyPlaylist.Items.Item.Track... values) {
            super.onProgressUpdate(values);

            PlaylistItemLab.getInstance().add(values[0]);
            mCallbacks.onFetchPlaylistItems();


        }

        @Override
        protected Void doInBackground(SpotifyPlaylist... playlists) {

            for (SpotifyPlaylist.Items.Item item : playlists[0].getItems()) {
                publishProgress(item.getTrack());
            }

            return null;
        }
    }

}
