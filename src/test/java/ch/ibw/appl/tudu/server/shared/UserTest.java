package ch.ibw.appl.tudu.server.shared;

import ch.ibw.appl.tudu.server.shared.service.JSONSerializer;
import ch.ibw.appl.tudu.server.user.model.User;
import com.despegar.http.client.HttpResponse;
import com.fasterxml.jackson.core.type.TypeReference;
import org.eclipse.jetty.http.HttpStatus;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class UserTest extends FunctionalTest {

    @Test
    public void getUsersIsOK(){
        HttpResponse httpResponse = this.executeGet("/users");

        Assert.assertEquals(200, httpResponse.code());

        String body = new String(httpResponse.body());
        List<User> deserializedUsers = new JSONSerializer().deserialize(body, new TypeReference<ArrayList<User>>() {});

        Assert.assertEquals( "Hans Jockel", deserializedUsers.get(0).name);
    }

    @Test
    public void getUsersIs406iftext(){
        HttpResponse httpResponse = this.executeGet("/users", "text/plain");
        Assert.assertEquals(406, httpResponse.code());
    }

    @Test
    public void getUsersByIdIsOK_IfExists(){
        HttpResponse httpResponse = this.executeGet("/users/1");

        Assert.assertEquals(200, httpResponse.code());

        String body = new String(httpResponse.body());
        User deserializedUser = new JSONSerializer().deserialize(body, new TypeReference<User>() {});

        Assert.assertEquals( "Hans Jockel", deserializedUser.name);
    }

    @Test
    public void getUsersByIdIsNotFound_IfUserUnexisting(){
        HttpResponse httpResponse = this.executeGet("/users/22");

        Assert.assertEquals(404, httpResponse.code());
    }

    @Test
    public void createUnexistingUserIsOK(){
        HttpResponse httpResponse = this.executePost("/users", new User("Peter Mafiosi"));

        Assert.assertEquals(HttpStatus.CREATED_201, httpResponse.code());

        String body = new String(httpResponse.body());
        User deserializedUser = new JSONSerializer().deserialize(body, new TypeReference<User>() {});

        httpResponse = this.executeGet("/users/" + deserializedUser.id);
        Assert.assertEquals(HttpStatus.OK_200, httpResponse.code());
    }

    @Test
    public void deleteExistingUserisOK(){
        HttpResponse httpResponse = this.executeDelete("/users/1");

        Assert.assertEquals(HttpStatus.OK_200, httpResponse.code());

        httpResponse = this.executeGet("/users/1");
        Assert.assertEquals(HttpStatus.NOT_FOUND_404, httpResponse.code());
    }

    @Test
    public void searchByNameWithMatches(){
        HttpResponse httpResponse = this.executeGet("/users?filter=name:OcK");

        Assert.assertEquals(HttpStatus.OK_200, httpResponse.code());

        String body = new String(httpResponse.body());
        List<User> deserializedUsers = new JSONSerializer().deserialize(body, new TypeReference<ArrayList<User>>() {});

        Assert.assertEquals(1, deserializedUsers.size());
    }

    @Test
    public void searchByNameWithoutMatches(){
        HttpResponse httpResponse = this.executeGet("/users?filter=name:abcsrf3");

        Assert.assertEquals(HttpStatus.OK_200, httpResponse.code());

        String body = new String(httpResponse.body());
        List<User> deserializedUsers = new JSONSerializer().deserialize(body, new TypeReference<ArrayList<User>>() {});

        Assert.assertEquals(0, deserializedUsers.size());
    }

    @Test
    public void create_user_validationSuccess() {
        User user = new User("Hans Schwinsbrota");
        HttpResponse response = executePost("/users", user);

        Assert.assertEquals(HttpStatus.CREATED_201, response.code());
        Assert.assertEquals("application/json", response.headers().get("Content-Type").get(0));

        String body = new String(response.body());
        User deserializedUser = new JSONSerializer().deserialize(body, new TypeReference<User>() {});
        Assert.assertEquals(user.name, deserializedUser.name);
    }

    @Test
    public void create_user_validationFailed() {
        User user = new User("");
        HttpResponse response = executePost("/users", user);

        Assert.assertEquals(HttpStatus.BAD_REQUEST_400, response.code());
        Assert.assertEquals("application/json", response.headers().get("Content-Type").get(0));

        String body = new String(response.body());
        Assert.assertTrue(body.contains("message"));
    }
}
