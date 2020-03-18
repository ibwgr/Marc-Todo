import com.despegar.http.client.HttpResponse;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonSerializable;
import org.junit.*;

import java.util.ArrayList;
import java.util.List;

public class ItemTest extends FunctionalTest {

    @Test
    public void getItemsisOK() {
        HttpResponse httpResponse = this.executeGet("/items");

        Item expectedItem = new Item();
        expectedItem.description = "Hans Jockel";
        String answer = new String(httpResponse.body());

        List<Item> deserializedItems = new JSONSerializer().deserialize(answer, new TypeReference<ArrayList<Item>>() {});

        Assert.assertEquals(expectedItem.description, deserializedItems.get(0).description);

        int expectedCode = 200;
        Assert.assertEquals(expectedCode, httpResponse.code());

    }

    @Test
    public void responseRetruns406IfRequestNotJSON() {
        HttpResponse httpResponse = this.executeGet("/items", "text/xml");

        int expectedCode = 406;
        Assert.assertEquals(expectedCode, httpResponse.code());

    }
}

