package spotify;

import androidx.annotation.NonNull;

import java.util.List;

public class SpotifyPlaylist {

    private String id;
    private String name;
    private String description;
    private Items items;

    private class Items {

        private List<Item> items;

        private class Item {

            private Track item;
            private class Track {

                private String id;
                private String name;
                private Album album;
                private List<Artists> artists;


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
