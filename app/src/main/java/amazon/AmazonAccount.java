package amazon;

import com.undefinedbehaviourgames.grizzly.Account;
import com.undefinedbehaviourgames.grizzly.MusicPlatform;
import com.undefinedbehaviourgames.grizzly.R;

import spotify.SpotifyAccount;

public class AmazonAccount extends Account {

    private Data data;

    private class Data {

        private User user;

        private class User {
            private String id;
            private String name;
            private String handle;
        }
    }



    public AmazonAccount() {
        mAccountName = "My Amazon Account";
        mMusicServiceName = MusicPlatform.Platforms.amazon;
        mIconResourceId = R.drawable.amazon;
    }
    public static AmazonAccount getInstance() {
        AmazonAccount account = new AmazonAccount();
        account.setIconResourceId(R.drawable.amazon);
        account.setAccountName("My Amazon Account");
        account.setMusicServiceName(MusicPlatform.Platforms.amazon);
        return account;
    }

    @Override
    public String getID() {
        return data.user.id;
    }

    @Override
    public String getAccountName() {
        return data.user.name;
    }
}
