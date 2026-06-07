package tidal;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import androidx.annotation.Nullable;

import com.undefinedbehaviourgames.grizzly.MusicPlatform;
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
import java.util.ArrayList;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;
import spotify.SpotifyPlaylist;

public class TidalMusicPlatform extends MusicPlatform {

    private static TidalMusicPlatform mSingleton;

    private final String BASE_URL = "https://openapi.tidal.com/v2/";
    private static final String CLIENT_ID = "Jn5OXRt4RWBjwi0q";
    private static final String REDIRECT_URI = "https://grizzly.undefinedbehaviourgames.com/oauth2redirect";
    private static final String AUTH_URL = "https://login.tidal.com/authorize";
    private static final String TOKEN_URL = "https://auth.tidal.com/v1/oauth2/token";
    private static final String SCOPE = "user.read playlists.write playlists.read";
    public static final int TIDAL_SIGN_IN_REQUEST_CODE = 1;

    private AuthorizationServiceConfiguration mServiceConfig;
    private AuthorizationRequest mAuthorizationRequest;
    private AuthorizationService mAuthorizationService;
    private AuthState mAuthState;
    private TidalService mTidalService;
    private Context mContext;

    private boolean cancelSignIn;


    private TidalMusicPlatform(Context context) {
        mContext = context.getApplicationContext();
        cancelSignIn = false;
    }
    public static TidalMusicPlatform getInstance(Context context) {
        if (mSingleton == null) {
            mSingleton = new TidalMusicPlatform(context);
            mSingleton.setIconResourceId(R.drawable.tidal);
            mSingleton.setPlatformName(Platforms.tidal);
            mSingleton.TAG = Platforms.tidal;
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
        mTidalService = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(TidalService.class);

    }

    @Override
    public void authorize(Activity activity) {
        super.authorize(activity);
        mAuthorizationService = new AuthorizationService(mContext);
        Intent authIntent = mAuthorizationService.getAuthorizationRequestIntent(mAuthorizationRequest);
        activity.startActivityForResult(authIntent, TIDAL_SIGN_IN_REQUEST_CODE);

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
        mTidalService.getUser(token).enqueue(new Callback<TidalAccount>() {
            @Override
            public void onResponse(Call<TidalAccount> call, Response<TidalAccount> response) {

                //check for cancellation flag
                if(!cancelSignIn) {
                    if (response.isSuccessful()) {

//                        try {
//                            Log.d(TAG, response.body().string());
//                        } catch (IOException e) {
//                            throw new RuntimeException(e);
//                        }
                        cancelSignIn = false;
                        if(callbacks != null) callbacks.onSignInFinished(response.body());

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
            public void onFailure(Call<TidalAccount> call, Throwable t) {
                if(callbacks != null) callbacks.onSignInError();
            }
        });
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

    public void getPlaylistItems(String token, String playlistId, FetchPlaylistItemsCallbacks callbacks) {

        String labPlaylistId = PlaylistItemLab.getInstance().getPlaylistId();
        String labPlaylistPlatform = PlaylistItemLab.getInstance().getPlatform();

        if (labPlaylistPlatform == null || !labPlaylistPlatform.equals(Platforms.spotify) || labPlaylistId == null || !labPlaylistId.equals(playlistId) || PlaylistLab.getInstance().getState() != State.FETCHED) {

            PlaylistItemLab.getInstance().setPlaylistItems(new ArrayList<>());
            PlaylistItemLab.getInstance().setPlaylistId(playlistId);
            PlaylistItemLab.getInstance().setPlaylistId(Platforms.tidal);
            PlaylistItemLab.getInstance().setState(State.FETCHING);
            mTidalService.getPlaylistItems(token, playlistId, new String[]{"items", "items.artists"}).enqueue(new Callback<TidalPlaylist>() {
                @Override
                public void onResponse(Call<TidalPlaylist> call, Response<TidalPlaylist> response) {
                    if (response.isSuccessful()) {

                        TidalPlaylist playlist = response.body();
                        new PlaylistItemsFetchTask(callbacks).execute(playlist);
                    }
                }

                @Override
                public void onFailure(Call<TidalPlaylist> call, Throwable t) {
                    Log.d(TAG, t.toString());
                }
            });

        }
    }
    public void getPlaylists(String token, String userId, FetchPlaylistCallbacks callbacks) {

        String playlistLabUserId = PlaylistLab.getInstance().getUserId();
        String playListLabPlatform = PlaylistLab.getInstance().getPlatform();
        //start a new request if the current loaded playlist does not belong to the requesting user or
        //the current loaded playlist belongs to a null user
        //the current fetched playlist is incomplete
        if (playListLabPlatform == null || !playListLabPlatform.equals(Platforms.tidal)|| playlistLabUserId == null || !playlistLabUserId.equals(userId) || PlaylistLab.getInstance().getState() != State.FETCHED)
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
            PlaylistLab.getInstance().setPlatform(Platforms.tidal);
            PlaylistLab.getInstance().setPlaylist(new ArrayList<>());

//            getPlaylistsRecursive(token, userId, callbacks, 0, 0);

            mTidalService.getPlaylists(token, "me").enqueue(new Callback<TidalLibrary>() {
                @Override
                public void onResponse(Call<TidalLibrary> call, Response<TidalLibrary> response) {
                    if (response.isSuccessful()) {


                        TidalLibrary library = response.body();
                        library.setOwner(userId);

                        Log.d(TAG, library.toString());
                        new TidalMusicPlatform.PlaylistFetchTask(library, callbacks).execute(library);
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
                public void onFailure(Call<TidalLibrary> call, Throwable t) {
                    Log.d(TAG, t.toString());
                }
            });
        }

    }

    //spotify service interface
    public interface TidalService {

        //get user profile
        @GET("users/me")
        Call<TidalAccount> getUser(@Header("Authorization") String token);

        //get user playlist
        @GET("playlists")
        Call<TidalLibrary> getPlaylists(@Header("Authorization") String token, @Query("filter[owners.id]") String userId);

        //get playlist items
        @GET("playlists/{playlist_id}/relationships/items")
        Call<TidalPlaylist> getPlaylistItems(@Header("Authorization") String token, @Path("playlist_id") String id, @Query("include") String[] include);

        @POST("playlist")
        Call<ResponseBody> createPlaylist(@Body CreateTidalPlaylistBody body);
        @POST("playlists/{id}/relationships/items")
        Call<ResponseBody> addToPlaylist(@Path("id") String id, @Body AddTidalTrackBody body);

        @GET("searchResults/{id}")
        Call<ResponseBody> searchTracks(@Path("id") String id, @Query("include") String[] include);

    }


    public static class PlaylistFetchTask extends AsyncTask<TidalLibrary, TidalLibrary.Playlist, Void> {

        private static final String TAG = "PlaylistFetchTaskLogger";
        private TidalLibrary mTidalLibrary;
        private FetchPlaylistCallbacks mCallbacks;
        public PlaylistFetchTask(TidalLibrary tidalLibrary, FetchPlaylistCallbacks callbacks) {
            mTidalLibrary = tidalLibrary;
            mCallbacks = callbacks;

        }
        @Override
        protected void onPostExecute(Void unused) {
            super.onPostExecute(unused);
            if (mTidalLibrary.getOwner().equals(PlaylistLab.getInstance().getUserId()) && mTidalLibrary.getNext() == null) {
                PlaylistLab.getInstance().setState(State.FETCHED);
            }
            Log.d(TAG, PlaylistLab.getInstance().getState().toString());
            mCallbacks = null;
            mTidalLibrary = null;
        }

        @Override
        protected void onProgressUpdate(TidalLibrary.Playlist... playlist) {
            super.onProgressUpdate(playlist);

            if (PlaylistLab.getInstance().getUserId().equals(mTidalLibrary.getOwner())) {
                PlaylistLab.getInstance().add(playlist[0]);
                if (mCallbacks != null) mCallbacks.onFetchPlaylist();

            }
        }

        @Override
        protected Void doInBackground(TidalLibrary... library) {


            for (TidalLibrary.Playlist tidalPlaylist : library[0].getItems()) {
                tidalPlaylist.init();
                publishProgress(tidalPlaylist);
            }
            return null;
        }


    }

    public static class PlaylistItemsFetchTask extends AsyncTask<TidalPlaylist, TidalPlaylist.Item , Void> {

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
        protected void onProgressUpdate(TidalPlaylist.Item... values) {
            super.onProgressUpdate(values);

            PlaylistItemLab.getInstance().add(values[0]);
            mCallbacks.onFetchPlaylistItems();


        }

        @Override
        protected Void doInBackground(TidalPlaylist... playlists) {

            for (TidalPlaylist.Item item : playlists[0].getTracks()) {
                publishProgress(item);
            }

            return null;
        }
    }
}
