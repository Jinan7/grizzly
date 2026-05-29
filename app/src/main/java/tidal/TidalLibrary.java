package tidal;

import androidx.annotation.NonNull;

import com.undefinedbehaviourgames.grizzly.MusicPlatform;
import com.undefinedbehaviourgames.grizzly.Playlist;

import java.util.List;


public class TidalLibrary {

    private List<Playlist> data;
    private String owner;


    public class Playlist  extends com.undefinedbehaviourgames.grizzly.Playlist {
        private String id;
        private Attributes attributes;
        public class Attributes {
            private String name;
            private String description;

            @Override
            public String toString() {
                return "Attributes{" +
                        "name='" + name + '\'' +
                        ", description='" + description + '\'' +
                        '}';
            }
        }


        @Override
        public void init() {
            super.mMusicServiceName = MusicPlatform.Platforms.tidal;
            super.mName = attributes.name;
            super.mDescription = attributes.description;
            super.mID = id;
            super.mOwner = "";
        }

        @Override
        public String toString() {
            return "Playlist{" +
                    "id='" + id + '\'' +
                    ", attributes=" + attributes +
                    '}';
        }
    }





    public List<Playlist> getItems() {
        return data;
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

    @NonNull
    @Override
    public String toString() {
        return owner.toString() + "\n"
                + data.toString();
    }
}
