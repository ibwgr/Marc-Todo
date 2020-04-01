package ch.ibw.appl.tudu.server.shared;

import ch.ibw.appl.tudu.server.shared.infra.HttpServer;
import ch.ibw.appl.tudu.server.shared.service.JSONSerializer;
import com.despegar.http.client.*;
import org.junit.Rule;
import spark.servlet.SparkApplication;

public class FunctionalTest {

  public static class TodoApplication implements SparkApplication {
    private HttpServer httpServer;

    @Override
    public void init() {
      httpServer = new HttpServer("4567", true);
      httpServer.start();
    }

    @Override
    public void destroy() {
      httpServer.stop();
    }
  }

  @Rule
  public SparkServer<TodoApplication> httpClient = new SparkServer<>(TodoApplication.class);

  public HttpResponse executeGet(String path, String acceptType){
    GetMethod method = httpClient.get(path, false);
    method.addHeader("Accept", acceptType);
    try {
      return httpClient.execute(method);
    } catch (HttpClientException e) {
      throw new RuntimeException(e);
    }
  }

  public HttpResponse executeGet(String path) {
    return executeGet(path, "application/json");
  }

  public HttpResponse executePost(String path, Object body){
    return executePost(path, body, "application/json");
  }

  public HttpResponse executePost(String path, Object body, String acceptType) {
    PostMethod method = httpClient.post(path, new JSONSerializer().serialize(body),false);
    method.addHeader("Accept", acceptType);
    try {
      return httpClient.execute(method);
    } catch (HttpClientException e) {
      throw new RuntimeException(e);
    }
  }

  public HttpResponse executeDelete(String path, String acceptType) {
    DeleteMethod method = httpClient.delete(path, false);
    method.addHeader("Accept", acceptType);
    try {
      return httpClient.execute(method);
    } catch (HttpClientException e) {
      throw new RuntimeException(e);
    }
  }

  public HttpResponse executeDelete(String path) {
    return executeDelete(path, "application/json");
  }
}

