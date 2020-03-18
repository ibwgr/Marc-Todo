
import spark.Response;
import spark.ResponseTransformer;

import java.util.ArrayList;
import java.util.List;

import static spark.Spark.*;

public class Main {

    //http://localhost:4567/hello
    public static void main(String[] args) {

        new HttpServer("4567").start();
    }
}