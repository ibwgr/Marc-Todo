import com.despegar.http.client.GetMethod;
import com.despegar.http.client.HttpClientException;
import com.despegar.http.client.HttpResponse;
import org.junit.Rule;
import spark.servlet.SparkApplication;

public class FunctionalTest {

  public static class TodoApplication implements SparkApplication {
    private HttpServer httpServer;

    @Override
    public void init() {
      httpServer = new HttpServer("4567");
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
}

