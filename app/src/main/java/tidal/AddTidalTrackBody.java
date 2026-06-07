package tidal;

import java.util.ArrayList;
import java.util.List;

public class AddTidalTrackBody {

    private List<Item> data;
    private Meta meta;

    private class Item {
        private String id;
        private String type;
        private Meta meta;
        private class Meta {
            private String addedAt;
        }

    }

    private class Meta {
        private String positionBefore;
    }

    public class Builder {
        private AddTidalTrackBody mBody;

        public Builder() {
            mBody = new AddTidalTrackBody();
            mBody.data = new ArrayList<>();
        }

        public Builder addItem(String id) {
            Item item = new Item();
            item.type = "tracks";
            item.id = id;
            mBody.data.add(item);
            return Builder.this;
        }

        public Builder addPositionBefore(String pos) {
            mBody.meta.positionBefore = pos;
            return Builder.this;
        }
        public AddTidalTrackBody build() {
            return mBody;
        }
    }

}
