package tidal;

import com.undefinedbehaviourgames.grizzly.Playlist;

import java.util.List;

import spotify.SpotifyPlaylist;

public class TidalLibrary {

    private Data data;
    private String owner;

    public class Data {

        private List<Playlist> included;

        public class Playlist  extends com.undefinedbehaviourgames.grizzly.Playlist {
            private String id;
            private Attributes attributes;
            public class Attributes {
                private String name;
                private String description;
            }

            @Override
            public void init() {
                super.mName = attributes.name;
                super.mDescription = attributes.description;
                super.mID = id;
                super.mOwner = "";
            }
        }
    }


    public List<Data.Playlist> getItems() {
        return data.included;
    }
    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getNext() {
        return null;
    }
}
