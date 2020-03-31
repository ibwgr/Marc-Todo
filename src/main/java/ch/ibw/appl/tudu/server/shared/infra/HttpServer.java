package ch.ibw.appl.tudu.server.shared.infra;

import ch.ibw.appl.tudu.server.item.infra.ItemController;
import ch.ibw.appl.tudu.server.shared.model.ValidationError;
import ch.ibw.appl.tudu.server.user.infra.UserController;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import org.eclipse.jetty.http.HttpStatus;
import spark.Service;

import java.util.Arrays;

import static spark.Spark.get;

public class HttpServer {

    private final String httpPort;
    private boolean isTest;
    private Service server;

    public HttpServer(String httpPort, boolean isTest) {
        this.httpPort = httpPort;
        this.isTest = isTest;
    }

    public void start() {
        server = Service.ignite();
        server.port(Integer.parseInt(httpPort));

        server.before((request, response) -> {
            if (!request.headers("Accept").contains("json")) {
                response.status(406);
            }
        });

        server.afterAfter((request, response) -> {
            response.type("application/json");
        });

        server.exception(RuntimeException.class, (exception, request, response) -> {
            if(exception instanceof ValidationError){
                String message = exception.getMessage();
                JsonNode node = JsonNodeFactory.instance.objectNode().set("message", JsonNodeFactory.instance.textNode(message));
                response.body(node.toString());
                response.status(HttpStatus.BAD_REQUEST_400);
            } else {
                // z.B. im Falle einer NullPointerException
                System.out.println(Arrays.toString(exception.getStackTrace()));
                response.body("");
                response.status(HttpStatus.INTERNAL_SERVER_ERROR_500);
            }
        });

        new ItemController(this.isTest).createRoutes(server);
        new UserController(this.isTest).createRoutes(server);

        server.awaitInitialization();
    }

    public void stop() {
        server.stop();
        server.awaitStop();
    }
}
