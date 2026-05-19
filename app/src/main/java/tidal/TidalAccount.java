package tidal;

import com.undefinedbehaviourgames.grizzly.Account;
import com.undefinedbehaviourgames.grizzly.MusicPlatform;
import com.undefinedbehaviourgames.grizzly.R;

import spotify.SpotifyAccount;

public class TidalAccount extends Account {

    Data data;
    public TidalAccount () {
        mAccountName = "My Tidal Account";
        mMusicServiceName = MusicPlatform.Platforms.tidal;
        mIconResourceId = R.drawable.tidal;
    }

    public static TidalAccount getInstance() {
        TidalAccount account = new TidalAccount();
        account.setIconResourceId(R.drawable.tidal);
        account.setAccountName("My Tidal Account");
        account.setMusicServiceName(MusicPlatform.Platforms.tidal);
        return account;
    }

    @Override
    public String getID() {
        return data.id;
    }

    @Override
    public String getAccountName() {
        return data.attributes.username;
    }

    private static class Data {
        String id;
        Attributes attributes;
        private static class Attributes {
            String username;
            String email;
        }
    }
}
