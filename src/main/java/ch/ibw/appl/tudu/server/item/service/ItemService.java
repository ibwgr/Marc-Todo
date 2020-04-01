package ch.ibw.appl.tudu.server.item.service;

import ch.ibw.appl.tudu.server.item.model.Item;
import ch.ibw.appl.tudu.server.shared.model.ValidationError;
import org.eclipse.jetty.http.HttpStatus;

import java.util.ArrayList;
import java.util.List;

public class ItemService {
    private final List<Item> items;
    private long nextId = 0;

    public ItemService(boolean isTest) {
        items = new ArrayList<>();
        if (isTest) {
            Item item1 = new Item("Hallo World Item");
            Item item2 = new Item("Einkaufen für Geburtstag");
            this.create(007L, item1);
            this.create(007L, item2);
        }
    }

    public Item create(Long userId, Item item) {
        if (item.description == null || item.description.isEmpty()) {
            throw new ValidationError("description can not be empty");
        }
        item.id = ++nextId;
        item.userId = userId;
        items.add(item);
        return item;
    }

    public Item getById(long requestedId) {
        for (Item item : items) {
            if (item.id == requestedId) {
                return item;
            }
        }
        return null;
    }

    public Item deleteById(long requestedId) {
        for (Item item : items) {
            if (item.id == requestedId) {
                items.remove(item);
                return item;
            }
        }
        return null;
    }

    public List<Item> geByFilter(String filter) {
        String[] keyValue = filter.split(":");
        List<Item> matches = new ArrayList<>();

        if (keyValue[0].equalsIgnoreCase("description")) {
            String searchTerm = keyValue[1].toLowerCase();

            for (Item item : items) {
                if (item.description.toLowerCase().contains(searchTerm)) {
                    matches.add(item);
                }
            }

        }
        return matches;
    }

    public List<Item> getByUserId(Long requestedUserId) {
        List<Item> matches = new ArrayList<>();

        for (Item item : items) {
            if (item.userId == requestedUserId) {
                matches.add(item);
            }
        }
        return matches;
    }

    public List<Item> geByUserIdAndFilter(long requestedUserId, String filter) {
        List<Item> matches = new ArrayList<>();
        List<Item> matchesFilter;
        matchesFilter = geByFilter(filter);

            for (Item item : matchesFilter) {
                if (item.userId.equals(requestedUserId)) {
                    matches.add(item);
                }
            }

        return matches;
    }

    public List<Item> all() {
        return items;
    }
}

