package youtube;

import com.undefinedbehaviourgames.grizzly.Account;
import com.undefinedbehaviourgames.grizzly.MusicPlatform;
import com.undefinedbehaviourgames.grizzly.R;

public class YoutubeAccount extends Account{

    private String mAccountName;
    public YoutubeAccount () {
        mAccountName = "My Youtube Account";
        mMusicServiceName = MusicPlatform.Platforms.youtube;
        mIconResourceId = R.drawable.youtube;
    }


    public static YoutubeAccount getInstance() {
        YoutubeAccount account = new YoutubeAccount();
        account.setIconResourceId(R.drawable.youtube);
        account.setAccountName("My Youtube Account");
        account.setMusicServiceName(MusicPlatform.Platforms.youtube);
        return account;
    }


    @Override
    public String getID() {
        return mAccountName;
    }

    @Override
    public String getAccountName() {
        return mAccountName;
    }

    @Override
    public void setAccountName(String accountName) {
        mAccountName = accountName;
    }
}





