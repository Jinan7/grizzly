package spotify;

import com.undefinedbehaviourgames.grizzly.NewPlaylist;

public class SpotifyNewPlaylist implements NewPlaylist {

    private String id;
    private String name;
    private String description;

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getId() {
        return id;
    }
}
