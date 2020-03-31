package ch.ibw.appl.tudu.server.user.service;
import ch.ibw.appl.tudu.server.shared.model.ValidationError;
import ch.ibw.appl.tudu.server.user.model.User;

import java.util.ArrayList;
import java.util.List;

public class UserService {
    private final List<User> users;
    private long nextId = 0;

    public UserService(boolean isTest) {
        users = new ArrayList<>();
        if (isTest) {
            User user1 = new User("Hans Jockel");
            User user2 = new User("Rudi Völler");
            this.create(user1);
            this.create(user2);
        }
    }

    public User create(User user) {
        if (user.name == null || user.name.isEmpty()) {
            throw new ValidationError("description can not be empty");
        }
        user.id = ++nextId;
        users.add(user);
        return user;
    }

    public User getById(long requestedId) {
        for (User user : users) {
            if (user.id == requestedId) {
                return user;
            }
        }
        return null;
    }

    public User deleteById(long requestedId) {
        for (User user : this.all()) {
            if (user.id == requestedId) {
                users.remove(user);
                return user;
            }
        }
        return null;
    }

    public List<User> geByFilter(String filter) {

        String[] keyValue = filter.split(":");
        List<User> matches = new ArrayList<>();

        if (keyValue[0].equalsIgnoreCase("name")) {
            String searchTerm = keyValue[1].toLowerCase();

            for (User user : users) {
                if (user.name.toLowerCase().contains(searchTerm)) {
                    matches.add(user);
                }
            }

        }
        return matches;
    }

    public List<User> all() {
        return users;
    }
}
