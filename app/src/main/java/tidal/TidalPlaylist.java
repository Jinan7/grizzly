package tidal;

import androidx.annotation.NonNull;

import java.util.List;

public class TidalPlaylist {



        List<Item> included;

        public class Item {
            private String id;
            private String type;
            private Attributes attributes;
            private Relationships relationships;
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
