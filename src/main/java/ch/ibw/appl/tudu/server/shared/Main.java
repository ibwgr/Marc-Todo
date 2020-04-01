package ch.ibw.appl.tudu.server.shared;

import ch.ibw.appl.tudu.server.shared.infra.HttpServer;

public class Main {

    //http://localhost:4567/hello
    public static void main(String[] args) {

        new HttpServer("4567", false).start();
    }
}