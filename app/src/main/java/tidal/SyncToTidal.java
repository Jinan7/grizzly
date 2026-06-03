package tidal;

import com.undefinedbehaviourgames.grizzly.MusicPlatform;
import com.undefinedbehaviourgames.grizzly.R;
import com.undefinedbehaviourgames.grizzly.SyncTo;

public class SyncToTidal extends SyncTo {

    public SyncToTidal() {
        mPlatformName = MusicPlatform.Platforms.tidal;
        mIconResourceId = R.drawable.tidal;
    }
}
