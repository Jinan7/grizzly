package spotify;

import androidx.annotation.NonNull;

import com.undefinedbehaviourgames.grizzly.MusicPlatform;
import com.undefinedbehaviourgames.grizzly.PlaylistItem;

import java.util.ArrayList;
import java.util.List;

public class SpotifyPlaylist {

    private String id;
    private String name;
    private String description;
    private Items items;

    public List<Items.Item> getItems() {
        return items.items;
    }

    public class Items {

        private List<Item> items;

        public class Item {

            private Track item;

            public Track getTrack() {
                return item;
            }
            public class Track extends PlaylistItem{

                private String id;
                private String name;
                private Album album;
                private List<Artists> artists;


                @Override
                public String getId() {
                    return id;
                }

                @Override
                public String getTitle() {
                    return name;
                }

                @Override
                public String getArtist() {
                    ArrayList<String> result = new ArrayList<>();
                    for (Artists artist : artists) {
                        result.add(artist.name);
                    }
                    return String.join(", ", result);
                }

                @Override
                public String getMusicPlatform() {
                    return MusicPlatform.Platforms.spotify;
                }

                private class Album {
                    private String name;

                    @NonNull
                    @Override
                    public String toString() {
                        return "        album: {" + "\n" +
                                "           name: " + name + "\n" +
                                "       }\n";
                    }
                }

                private class Artists {
                    private String name;

                    @NonNull
                    @Override
                    public String toString() {
                        return "        artists: {" + "\n" +
                                "           name: " + name + "\n" +
                                "       }\n";
                    }
                }

                @NonNull
                @Override
                public String toString() {
                    return "\n"   +
                            "       track: {"  + "\n" +
                            "           name: " + name + "\n" +
                            album.toString() +
                            artists.toString() +
                            "       }\n";

                }

            }

            @NonNull
            @Override
            public String toString() {
                return item.toString();
            }
        }

        @NonNull
        @Override
        public String toString() {
            return items.toString();
        }
    }


    @NonNull
    @Override
    public String toString() {
        return "playlist: {" + "\n" +
                "   name: " + name + "\n" +
                "   items: " + items.toString();
    }
}
