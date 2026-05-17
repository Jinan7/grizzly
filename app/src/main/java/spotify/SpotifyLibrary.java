package spotify;

import androidx.annotation.NonNull;

import java.util.List;

public class SpotifyLibrary {

    String href;
    int limit;
    String next;
    int offset;
    String previous;
    String total;
    String owner;

    List<SpotifyPlaylist> items;

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

    public List<SpotifyPlaylist> getItems() {
        return items;
    }

    public void setItems(List<SpotifyPlaylist> items) {
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
}
