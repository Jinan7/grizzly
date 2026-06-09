package spotify;

import androidx.annotation.NonNull;

import java.util.List;

public class SpotifySearchResult {

    private Track tracks;

    public class Track {
        private List<Item> items;

        public class Item {

            private String id;
            private String name;
            private String type;
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
