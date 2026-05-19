package spotify;

import com.undefinedbehaviourgames.grizzly.Account;
import com.undefinedbehaviourgames.grizzly.MusicPlatform;
import com.undefinedbehaviourgames.grizzly.R;

public class SpotifyAccount extends Account {

    String display_name;
    String email;
    String id;

    public SpotifyAccount() {
        mAccountName = "My Spotify Account";
        mMusicServiceName = MusicPlatform.Platforms.spotify;
        mIconResourceId = R.drawable.spotify;
    }
    public static SpotifyAccount getInstance() {
        SpotifyAccount account = new SpotifyAccount();
        account.setIconResourceId(R.drawable.spotify);
        account.setAccountName("My Spotify Account");
        account.setMusicServiceName(MusicPlatform.Platforms.spotify);
        return account;
    }

    @Override
    public String getID() {
        return id;
    }

    @Override
    public String getAccountName() {
        return display_name;
    }
}
