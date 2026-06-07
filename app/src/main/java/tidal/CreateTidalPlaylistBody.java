package tidal;

public class CreateTidalPlaylistBody {
    private Data data;
    private String type;
    public class Builder {

        private CreateTidalPlaylistBody mBody;

        public Builder() {
            mBody = new CreateTidalPlaylistBody();
            mBody.type = "playlists";
        }
        public CreateTidalPlaylistBody build() {
            return mBody;
        }

        public Builder setAccessType(String AccessType){
            mBody.data.attributes.accessType = AccessType;
            return Builder.this;
        }
        public Builder setCreatedAt(String createdAt) {
            mBody.data.attributes.createdAt = createdAt;
            return Builder.this;
        }

        public Builder setDescription(String description) {
            mBody.data.attributes.description = description;
            return Builder.this;
        }

        public Builder setName(String name) {
            mBody.data.attributes.name = name;
            return Builder.this;
        }

    }

    public class Data {

        private Attributes attributes;

        public class Attributes {

            private String accessType;
            private String createdAt;
            private String description;
            private String name;
        }
    }
}
