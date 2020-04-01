package ch.ibw.appl.tudu.server.user.model;

public class User {
    public String name;
    public Long id;

    public User(){
    }

    public User(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }
}

