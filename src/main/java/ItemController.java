import spark.Service;

import java.util.ArrayList;
import java.util.List;

public class ItemController {
    List<Item> items;
    public ItemController() {
        items = new ArrayList<>();
        Item item1 = new Item();
        item1.description = "Hans Jockel";
        items.add(item1);
    }


    public void createRoutes(Service server) {
        server.get("/items", (request, response) -> {

            if (!request.headers("Accept").contains("json")) {
                response.status(406);
                return response.status();
            }
            response.type("application/json");
            return items;
        }, items -> new JSONSerializer().serialize(items));
    }

}

