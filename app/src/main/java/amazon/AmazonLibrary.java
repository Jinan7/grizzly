package amazon;

import com.undefinedbehaviourgames.grizzly.Playlist;

import java.util.List;

public class AmazonLibrary {

    private Data data;

    public boolean hasNext() {
        return data.user.playlists.pageInfo.hasNextPage;
    }

    public String getOwner() {
        return data.user.id;
    }

    public void setOwner(String owner) {
    }

    public List<Data.User.Playlists.Playlist> getItems() {
        return data.user.playlists.edges;
    }

    public class Data {

        private User user;
        public class User {
            private String id;
            private Playlists playlists;

            public class Playlists {

                private PageInfo pageInfo;
                private List<Playlist> edges;

                public class PageInfo {
                    public boolean hasNextPage;
                }

                public class Playlist extends com.undefinedbehaviourgames.grizzly.Playlist {
                    private Node node;

                    @Override
                    public void init() {
                        super.mName = node.title;
                        super.mDescription = "";
                        super.mID = node.id;
                        super.mOwner = "";
                    }
                    public class Node {
                        private String id;
                        private String curator;
                        private String title;
                    }
                }
            }
        }
    }
}
