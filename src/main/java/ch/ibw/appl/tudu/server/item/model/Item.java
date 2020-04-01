package ch.ibw.appl.tudu.server.item.model;

import java.util.Date;

public class Item {
    public String description;
    public Date createdAt;
    public Long id;
    public Long userId;

    public Item() {}

    public Item(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return description;
    }
}
