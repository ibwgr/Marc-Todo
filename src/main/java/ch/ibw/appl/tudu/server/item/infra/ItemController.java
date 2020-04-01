package ch.ibw.appl.tudu.server.item.infra;

import ch.ibw.appl.tudu.server.item.model.Item;
import ch.ibw.appl.tudu.server.item.service.ItemService;
import ch.ibw.appl.tudu.server.shared.service.JSONSerializer;
import com.fasterxml.jackson.core.type.TypeReference;
import org.eclipse.jetty.http.HttpStatus;
import spark.Service;

public class ItemController {
    ItemService service;

    public ItemController(boolean isTest) {
        service = new ItemService(isTest);
    }

    public void createRoutes(Service server) {
        JSONSerializer jsonSerializer = new JSONSerializer();

        server.get("/items", (request, response) -> {

            String filter = request.queryParamOrDefault("filter", "");
            if(!filter.isEmpty()) {
                return service.geByFilter(filter);
            }
            return service.all();


        }, jsonSerializer::serialize);

        server.get("/users/:id/items", (request, response) -> {
            long requestedUserId = Long.parseLong(request.params("id"));
            String filter = request.queryParamOrDefault("filter", "");
            if(!filter.isEmpty()) {
                return service.geByUserIdAndFilter(requestedUserId, filter);
            }
            return service.getByUserId(requestedUserId);


        }, jsonSerializer::serialize);

        server.post("/items", (request, response) -> {
            Item item = jsonSerializer.deserialize(request.body(), new TypeReference<Item>() {
            });
            Item newItem = service.create(item.userId, item);
            response.status(HttpStatus.CREATED_201);
            return newItem;

        }, jsonSerializer::serialize);

        server.post("/users/:id/items", (request, response) -> {
            Item item = jsonSerializer.deserialize(request.body(), new TypeReference<Item>() {
            });
            long requestedUserId = Long.parseLong(request.params("id"));
            Item newItem = service.create(requestedUserId, item);
            response.status(HttpStatus.CREATED_201);
            return newItem;

        }, jsonSerializer::serialize);

        server.get("/items/:id", (request, response) -> {
            long requestedId = Long.parseLong(request.params("id"));
            return service.getById(requestedId);

        }, jsonSerializer::serialize);

        server.delete("/items/:id", (request, response) -> {
            long requestedId = Long.parseLong(request.params("id"));

            return service.deleteById(requestedId);
        }, jsonSerializer::serialize);

    }
}
