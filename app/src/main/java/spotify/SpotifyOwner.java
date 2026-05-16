package spotify;

import androidx.annotation.NonNull;

public class SpotifyOwner {

    String display_name;
    String id;

    public String getDisplay_name() {
        return display_name;
    }

    public void setDisplay_name(String display_name) {
        this.display_name = display_name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @NonNull
    @Override
    public String toString() {
        return "display_name: " + display_name;
    }
}
