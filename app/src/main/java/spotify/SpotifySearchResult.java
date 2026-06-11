package spotify;

import androidx.annotation.NonNull;

import com.undefinedbehaviourgames.grizzly.SearchResult;

import java.util.List;

public class SpotifySearchResult implements SearchResult {

    private Track tracks;

    @Override
    public String getId() {
        return tracks.items.get(0).id;
    }
    @Override
    public String getUri() {
        return tracks.items.get(0).uri;
    }

    public class Track {
        private List<Item> items;

        public class Item {

            private String id;
            private String name;
            private String type;
            private String uri;
            private List<Artist> artists;

            public class Artist {
                private String id;
                private String name;

                @Override
                public String toString() {
                    return "Artist{" +
                            "id='" + id + '\'' +
                            ", name='" + name + '\'' +
                            '}';
                }
            }

            @Override
            public String toString() {
                return "Item{" +
                        "id='" + id + '\'' +
                        ", name='" + name + '\'' +
                        ", type='" + type + '\'' +
                        ", artists=" + artists +
                        '}';
            }
        }

        @Override
        public String toString() {
            return "Track{" +
                    "items=" + items +
                    '}';
        }
    }

    @NonNull
    @Override
    public String toString() {
        return tracks.toString();
    }
}
