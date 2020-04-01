package ch.ibw.appl.tudu.server.user.infra;

import ch.ibw.appl.tudu.server.item.model.Item;
import ch.ibw.appl.tudu.server.item.service.ItemService;
import ch.ibw.appl.tudu.server.shared.service.JSONSerializer;
import ch.ibw.appl.tudu.server.user.model.User;
import ch.ibw.appl.tudu.server.user.service.UserService;
import com.fasterxml.jackson.core.type.TypeReference;
import org.eclipse.jetty.http.HttpStatus;
import spark.Service;

public class UserController {
    UserService service;

    public UserController(boolean isTest) {
        service = new UserService(isTest);
    }

    public void createRoutes(Service server) {
        JSONSerializer jsonSerializer = new JSONSerializer();

        server.get("/users", (request, response) -> {

            String filter = request.queryParamOrDefault("filter", "");
            if(!filter.isEmpty()) {
                return service.geByFilter(filter);
            }
            return service.all();


        }, jsonSerializer::serialize);

        server.post("/users", (request, response) -> {
            User user = jsonSerializer.deserialize(request.body(), new TypeReference<User>() {
            });
            User newUser = service.create(user);
            response.status(HttpStatus.CREATED_201);
            return newUser;

        }, jsonSerializer::serialize);

        server.get("/users/:id", (request, response) -> {
            long requestedId = Long.parseLong(request.params("id"));
            return service.getById(requestedId);

        }, jsonSerializer::serialize);

        server.delete("/users/:id", (request, response) -> {
            long requestedId = Long.parseLong(request.params("id"));

            return service.deleteById(requestedId);
        }, jsonSerializer::serialize);

    }
}
