package tidal;

import androidx.annotation.NonNull;

import com.undefinedbehaviourgames.grizzly.MusicPlatform;
import com.undefinedbehaviourgames.grizzly.PlaylistItem;

import java.util.ArrayList;
import java.util.List;

public class TidalPlaylist {



        List<Item> included;


        public List<Item> getTracks() {

            List<Item> tracks = new ArrayList<>();
            List<Item> artists = new ArrayList<>();

            for (Item item : included) {
                if (item.getType().equals("tracks")) {
                    tracks.add(item);
                } else if (item.getType().equals("artists")) {
                    artists.add(item);
                }
            }

            for (Item track : tracks) {
                for (Item.Relationships.Artists.Artist a : track.getRelatedArtists()) {

                    for (Item artist : artists) {
                        if (artist.id.equals(a.id)) {
                            track.addArtist(artist.attributes.name);
                        }
                    }
                }
            }
            return tracks;
        }
        public class Item implements PlaylistItem {
            private String id;
            private String type;
            private Attributes attributes;
            private Relationships relationships;
            private List<String> artists;

            @Override
            public String getId() {
                return id;
            }
            public String getType() {
                return type;
            }
            @Override
            public String getTitle() {
                return attributes.title;
            }

            @Override
            public String getArtist() {
                if (artists == null) return "";
                return String.join(", ", artists);
            }

            public void addArtist(String artist) {
                if (artists == null) artists = new ArrayList<>();
                artists.add(artist);
            }

            @Override
            public String getMusicPlatform() {
                return MusicPlatform.Platforms.tidal;
            }
            public List<Relationships.Artists.Artist> getRelatedArtists() {
                return relationships.artists.data;
            }

            public class Attributes {
                private String title;
                private String name;

                @Override
                public String toString() {

                    if (title != null) {
                        return "attributes: {" + "\n"  +
                                "   title: " + title + "\n" +
                                "}\n";
                    } else {
                        return "attributes: {" + "\n"  +
                                "   name: " + name + "\n" +
                                "}\n";
                    }

                }
            }

            public class Relationships {

                private Artists artists;
                public class Artists {
                    private List<Artist> data;

                    public class Artist {
                        String id;
                        String type;

                        public String getId() {
                            return id;
                        }

                        @Override
                        public String toString() {
                            return "artist: {" +
                                    "id: " + id + '\n' +
                                    "type: " + type + '\n' +
                                    "}\n";
                        }
                    }

                    @Override
                    public String toString() {
                        return "artist {\n" +
                                "data: " + data.toString() + "\n"
                                ;
                    }
                }

                @NonNull
                @Override
                public String toString() {
                    return artists.toString();
                }
            }

            @NonNull
            @Override
            public String toString() {

                if (relationships == null) {
                    return "id: " + id + "\n" +
                            attributes.toString();
                } else {
                    return "id: " + id + "\n" +
                            attributes.toString() +
                            relationships.toString();
                }


            }
        }

    @NonNull
    @Override
    public String toString() {
        return included.toString();
    }


    //        public class Artist {
//            private String id;
//            private Attributes attributes;
//
//            public class Attributes {
//                private String name;
//            }
//        }



}
