package youtube;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import androidx.annotation.Nullable;

import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.ExponentialBackOff;
import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.YouTubeScopes;
import com.google.api.services.youtube.model.Channel;
import com.google.api.services.youtube.model.ChannelListResponse;
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
import net.openid.appauth.ClientSecretBasic;
import net.openid.appauth.ResponseTypeValues;
import net.openid.appauth.TokenResponse;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import okhttp3.Request;
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

public class YoutubeMusicPlatform extends MusicPlatform {

    private static YoutubeMusicPlatform mSingleton;
    private GoogleAccountCredential mCredential;

    public static final int YOUTUBE_SIGN_IN_REQUEST_CODE = 3;

    private YoutubeService mYoutubeService;
    private static final String[] SCOPES = { YouTubeScopes.YOUTUBE_READONLY };
    private Context mContext;

    private boolean cancelSignIn;


    private YoutubeMusicPlatform(Context context) {
        mContext = context.getApplicationContext();
        cancelSignIn = false;
    }
    public static YoutubeMusicPlatform getInstance(Context context) {
        if (mSingleton == null) {
            mSingleton = new YoutubeMusicPlatform(context);
            mSingleton.setIconResourceId(R.drawable.youtube);
            mSingleton.setPlatformName(Platforms.youtube);
            mSingleton.TAG = Platforms.youtube;
            mSingleton.configure();
        }
        return mSingleton;
    }

    public void updateAuthState(AuthorizationResponse res, AuthorizationException ex) {

    }
    @Override
    public void configure() {
        super.configure();
        mCredential = GoogleAccountCredential.usingOAuth2(mContext, Arrays.asList(SCOPES)).setBackOff(new ExponentialBackOff());


    }

    @Override
    public void authorize(Activity activity) {
        super.authorize(activity);

        activity.startActivityForResult(mCredential.newChooseAccountIntent(), YOUTUBE_SIGN_IN_REQUEST_CODE);


    }

    public void signIn(String accountName, Callbacks callbacks) {
        mCredential.setSelectedAccountName(accountName);


        new RequestTask(mCredential, callbacks).execute();
    }

    public void signIn(AuthorizationResponse response, Callbacks callbacks) {
        super.signIn(response, callbacks);

        //after first authorization request with the code verifier
        //use the code obtained in the response to get token
        //auth service provides this convenience method
        //to make the request directly with the response object
        //instead of having to extract request token

        //check for cancellation flag





    }

    public void getUser(String token, Callbacks callbacks) {
        mYoutubeService.getUser(token).enqueue(new Callback<ResponseBody>() {
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
    public void fetchPlaylists(String userId, FetchPlaylistCallbacks callbacks) {
        super.fetchPlaylists(userId, callbacks);


    }

    public void getPlaylists(String token, String userId, FetchPlaylistCallbacks callbacks) {

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

            mYoutubeService.getPlaylists(token, "me").enqueue(new Callback<ResponseBody>() {
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
    public interface YoutubeService {

        //get user profile
        @GET("channels")
        Call<ResponseBody> getUser(@Header("Authorization") String token);

        //get user playlist
        @GET("playlists")
        Call<ResponseBody> getPlaylists(@Header("Authorization") String token, @Query("filter[owners.id]") String userId);

    }

    private static class RequestTask extends AsyncTask<Void, Void, List<String>> {

        private static final String TAG = "RequestTaskLogger";
        private YouTube mService = null;
        private Callbacks mCallbacks = null;

        public RequestTask(GoogleAccountCredential credential, Callbacks callbacks) {

            HttpTransport transport = AndroidHttp.newCompatibleTransport();
            JsonFactory jsonFactory = JacksonFactory.getDefaultInstance();
            mService = new YouTube.Builder(
                    transport, jsonFactory, credential
            ).setApplicationName("Grizzly").build();
            mCallbacks = callbacks;
        }
        @Override
        protected List<String> doInBackground(Void... voids) {
            try {
                return getChannelInfo();

            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }

        }

        @Override
        protected void onPostExecute(List<String> result) {
            super.onPostExecute(result);
            if (result != null) {
                YoutubeAccount account = new YoutubeAccount();
                account.setAccountName(result.get(0));
                mCallbacks.onSignInFinished(account);
            } else {
                mCallbacks.onSignInError();
            }
        }

        private List<String> getChannelInfo() throws IOException {
            List<String> channelInfo = new ArrayList<>();
            ChannelListResponse response = mService.channels().list("snippet,contentDetails,statistics").setMine(true).execute();
            List<Channel> channels = response.getItems();

            if (channels != null) {
                channelInfo.add(channels.get(0).getSnippet().getTitle());
            }

            return channelInfo;
        }

    }
}
