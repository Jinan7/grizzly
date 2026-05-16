package spotify;

import androidx.annotation.NonNull;

import com.undefinedbehaviourgames.grizzly.Playlist;

public class SpotifyPlaylist extends Playlist {

    boolean collaborative;
    String description;
    String href;
    String id;
    String name;

    SpotifyOwner owner;

    @Override
    public void init() {
        super.mName = name;
        super.mDescription = description;
        super.mID = id;
        super.mOwner = owner.display_name;
    }


    public SpotifyOwner getSpotifyOwner() {
        return owner;
    }

    @NonNull
    @Override
    public String toString() {
        return "collaborative: " + collaborative +
                "\ndescription: " + description +
                "\nhref: " + href +
                "\nid: " + id +
                "\nname: " + name +
                "\ndisplay_name: " + owner.display_name +
                "\n";
    }
}
