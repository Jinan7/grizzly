package spotify;

import com.undefinedbehaviourgames.grizzly.MusicPlatform;
import com.undefinedbehaviourgames.grizzly.R;
import com.undefinedbehaviourgames.grizzly.SyncTo;

public class SyncToSpotify extends SyncTo {

    public SyncToSpotify() {
        mPlatformName = MusicPlatform.Platforms.spotify;
        mIconResourceId = R.drawable.spotify;
    }


}
