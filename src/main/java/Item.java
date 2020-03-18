import java.util.Date;

public class Item {
    public String description;
    public Date createdAt;

    @Override
    public String toString() {
        return description;
    }
}
