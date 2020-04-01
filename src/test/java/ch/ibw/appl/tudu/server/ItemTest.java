package ch.ibw.appl.tudu.server;

import ch.ibw.appl.tudu.server.item.model.Item;
import ch.ibw.appl.tudu.server.shared.FunctionalTest;
import ch.ibw.appl.tudu.server.shared.service.JSONSerializer;
import com.despegar.http.client.HttpResponse;
import com.fasterxml.jackson.core.type.TypeReference;
import org.eclipse.jetty.http.HttpStatus;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class ItemTest extends FunctionalTest {

    @Test
    public void getItemsIsOK(){
        HttpResponse httpResponse = this.executeGet("/items");

        Assert.assertEquals(200, httpResponse.code());

        String body = new String(httpResponse.body());
        List<Item> deserializedItems = new JSONSerializer().deserialize(body, new TypeReference<ArrayList<Item>>() {});

        Assert.assertEquals( "Hallo World Item", deserializedItems.get(0).description);
    }

    @Test
    public void getItemsIs406iftext(){
        HttpResponse httpResponse = this.executeGet("/items", "text/plain");
        Assert.assertEquals(406, httpResponse.code());
    }

    @Test
    public void getItemsByIdIsOK_IfExists(){
        HttpResponse httpResponse = this.executeGet("/items/1");

        Assert.assertEquals(200, httpResponse.code());

        String body = new String(httpResponse.body());
        Item deserializedItem = new JSONSerializer().deserialize(body, new TypeReference<Item>() {});

        Assert.assertEquals( "Hallo World Item", deserializedItem.description);
    }

    @Test
    public void getItemsByIdIsNotFound_IfItemUnexisting(){
        HttpResponse httpResponse = this.executeGet("/items/22");

        Assert.assertEquals(404, httpResponse.code());
    }

    @Test
    public void createUnexistingItemIsOK(){
        HttpResponse httpResponse = this.executePost("/items", new Item("new Item"));

        Assert.assertEquals(HttpStatus.CREATED_201, httpResponse.code());

        httpResponse = this.executeGet("/items/3");
        Assert.assertEquals(HttpStatus.OK_200, httpResponse.code());
    }

    @Test
    public void deleteExistingItemisOK(){
        HttpResponse httpResponse = this.executeDelete("/items/1");

        Assert.assertEquals(HttpStatus.OK_200, httpResponse.code());

        httpResponse = this.executeGet("/items/1");
        Assert.assertEquals(HttpStatus.NOT_FOUND_404, httpResponse.code());
    }

    @Test
    public void searchByDescriptionWithMatches(){
        HttpResponse httpResponse = this.executeGet("/items?filter=description:füR");

        Assert.assertEquals(HttpStatus.OK_200, httpResponse.code());

        String body = new String(httpResponse.body());
        List<Item> deserializedItems = new JSONSerializer().deserialize(body, new TypeReference<ArrayList<Item>>() {});

        Assert.assertEquals(1, deserializedItems.size());
    }

    @Test
    public void searchByDescriptionWithoutMatches(){
        HttpResponse httpResponse = this.executeGet("/items?filter=description:abcsrf3");

        Assert.assertEquals(HttpStatus.OK_200, httpResponse.code());

        String body = new String(httpResponse.body());
        List<Item> deserializedItems = new JSONSerializer().deserialize(body, new TypeReference<ArrayList<Item>>() {});

        Assert.assertEquals(0, deserializedItems.size());
    }

    @Test
    public void create_todo_validationSuccess() {
        Item item = new Item("Hans Jockel");
        HttpResponse response = executePost("/items", item);

        Assert.assertEquals(HttpStatus.CREATED_201, response.code());
        Assert.assertEquals("application/json", response.headers().get("Content-Type").get(0));

        String body = new String(response.body());
        Item deserializedItem = new JSONSerializer().deserialize(body, new TypeReference<Item>() {});
        Assert.assertEquals(item.description, deserializedItem.description);
    }

    @Test
    public void create_todo_validationFailed() {
        Object item = new Item("");
        HttpResponse response = executePost("/items", item);

        Assert.assertEquals(HttpStatus.BAD_REQUEST_400, response.code());
        Assert.assertEquals("application/json", response.headers().get("Content-Type").get(0));

        String body = new String(response.body());
        Assert.assertTrue(body.contains("message"));
    }
}
