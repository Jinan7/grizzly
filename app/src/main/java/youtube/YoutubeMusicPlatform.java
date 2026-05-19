package youtube;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;

import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
import com.google.api.client.util.ExponentialBackOff;
import com.google.api.services.youtube.YouTubeScopes;
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
import java.util.Arrays;

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
    static final int REQUEST_PERMISSION_GET_ACCOUNTS = 0;
    public static final int GOOGLE_REQUEST_SELECT_ACCOUNT = 3;
    private static final String[] SCOPES = { YouTubeScopes.YOUTUBE_READONLY };
    private Context mContext;

    private GoogleAccountCredential mCredential;
//    private GoogleAccountCredential mCredential;



    private YoutubeMusicPlatform(Context context) {
        mContext = context.getApplicationContext();

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

    @Override
    public void configure() {
        super.configure();

        mCredential = GoogleAccountCredential
                .usingOAuth2(mContext, Arrays.asList(SCOPES))
                .setBackOff(new ExponentialBackOff());

    }

    @Override
    public void authorize(Activity activity) {


        if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.GET_ACCOUNTS) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(activity, new String[] {Manifest.permission.GET_ACCOUNTS}, REQUEST_PERMISSION_GET_ACCOUNTS);
        } else {
            activity.startActivityForResult(
                    mCredential.newChooseAccountIntent(),
                    GOOGLE_REQUEST_SELECT_ACCOUNT
                    );
        }

    }




    //spotify service interface
    public interface TidalService {

        //get user profile
        @GET("users/me")
        Call<ResponseBody> getUser(@Header("Authorization") String token);

        //get user playlist
        @GET("playlists")
        Call<ResponseBody> getPlaylists(@Header("Authorization") String token, @Query("filter[owners.id]") String userId);

    }
}
