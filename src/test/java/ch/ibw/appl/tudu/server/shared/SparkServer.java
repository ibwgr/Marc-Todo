package ch.ibw.appl.tudu.server.shared;

import com.despegar.http.client.DeleteMethod;
import com.despegar.http.client.GetMethod;
import com.despegar.http.client.HeadMethod;
import com.despegar.http.client.HttpClient;
import com.despegar.http.client.HttpClientException;
import com.despegar.http.client.HttpMethod;
import com.despegar.http.client.HttpResponse;
import com.despegar.http.client.OptionsMethod;
import com.despegar.http.client.PatchMethod;
import com.despegar.http.client.PostMethod;
import com.despegar.http.client.PutMethod;
import org.junit.rules.ExternalResource;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;
import spark.Service;
import spark.servlet.SparkApplication;

public class SparkServer<T extends SparkApplication> extends ExternalResource {
  private Class<T> sparkApplicationClass;
  private T sparkApplication;
  private String protocolHostPort;
  private HttpClient httpClient;

  SparkServer(Class<T> sparkApplicationClass) {
    this(sparkApplicationClass, Service.SPARK_DEFAULT_PORT);
  }

  SparkServer(Class<T> sparkApplicationClass, int port) {
    this.sparkApplicationClass = sparkApplicationClass;
    this.protocolHostPort = "http://localhost:" + port;
    this.httpClient = new HttpClient(1);
  }

  @Override
  public Statement apply(Statement base, Description description) {
    return super.apply(base, description);
  }

  @Override
  protected void before() throws Throwable {
    this.sparkApplication = this.sparkApplicationClass.newInstance();
    this.sparkApplication.init();
  }

  public GetMethod get(String path, boolean followRedirect) {
    return new GetMethod(this.protocolHostPort + path, followRedirect);
  }

  public PostMethod post(String path, String body, boolean followRedirect) {
    return new PostMethod(this.protocolHostPort + path, body, followRedirect);
  }

  public PutMethod put(String path, String body, boolean followRedirect) {
    return new PutMethod(this.protocolHostPort + path, body, followRedirect);
  }

  public PatchMethod patch(String path, String body, boolean followRedirect) {
    return new PatchMethod(this.protocolHostPort + path, body, followRedirect);
  }

  public DeleteMethod delete(String path, boolean followRedirect) {
    return new DeleteMethod(this.protocolHostPort + path, followRedirect);
  }

  public OptionsMethod options(String path, boolean followRedirect) {
    return new OptionsMethod(this.protocolHostPort + path, followRedirect);
  }

  public HeadMethod head(String path, boolean followRedirect) {
    return new HeadMethod(this.protocolHostPort + path, followRedirect);
  }

  public HttpResponse execute(HttpMethod httpMethod) throws HttpClientException {
    return this.httpClient.execute(httpMethod);
  }

  @Override
  protected void after() {
    this.sparkApplication.destroy();
  }
}