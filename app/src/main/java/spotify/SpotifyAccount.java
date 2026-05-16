package spotify;

import com.undefinedbehaviourgames.grizzly.Account;
import com.undefinedbehaviourgames.grizzly.R;

public class SpotifyAccount extends Account {

    String display_name;
    String email;
    String id;

    public SpotifyAccount() {
        mAccountName = "My Spotify Account";
        mMusicServiceName = "Spotify";
        mIconResourceId = R.drawable.spotify;
    }
    public static SpotifyAccount getInstance() {
        SpotifyAccount account = new SpotifyAccount();
        account.setIconResourceId(R.drawable.spotify);
        account.setAccountName("Spotify");
        account.setMusicServiceName("Spotify");
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
