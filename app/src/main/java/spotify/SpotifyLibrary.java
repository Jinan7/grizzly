package spotify;

import androidx.annotation.NonNull;

import com.undefinedbehaviourgames.grizzly.MusicPlatform;
import com.undefinedbehaviourgames.grizzly.Playlist;

import java.util.List;

public class SpotifyLibrary {

    String href;
    int limit;
    String next;
    int offset;
    String previous;
    String total;
    String owner;

    List<Playlist> items;

    public String getHref() {
        return href;
    }

    public void setHref(String href) {
        this.href = href;
    }

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }

    public String getNext() {
        return next;
    }

    public void setNext(String next) {
        this.next = next;
    }

    public int getOffset() {
        return offset;
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }

    public String getPrevious() {
        return previous;
    }

    public void setPrevious(String previous) {
        this.previous = previous;
    }

    public List<Playlist> getItems() {
        return items;
    }

    public void setItems(List<Playlist> items) {
        this.items = items;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    @NonNull
    @Override
    public String toString() {
        return "href: " + href +
                "\nlimit: " + limit +
                "\nnext: " + next +
                "\noffset: " + offset +
                "\nprevious: " + previous +
                "\ntotal: " + total +
                "\nitems: " + items.toString() +
                "\n";
    }

    public class Playlist extends com.undefinedbehaviourgames.grizzly.Playlist {

        boolean collaborative;
        String description;
        String href;
        String id;
        String name;

        SpotifyOwner owner;

        public Playlist() {
            mMusicServiceName = MusicPlatform.Platforms.spotify;
        }
        @Override
        public void init() {
            super.mMusicServiceName = MusicPlatform.Platforms.spotify;
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
    }
}
