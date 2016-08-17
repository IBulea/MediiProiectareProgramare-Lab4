package library.network.json;

import com.google.gson.*;
import library.model.Product;

import java.lang.reflect.Type;

public class ProductDeserializer implements JsonDeserializer<Product> {
  @Override
  public Product deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
    JsonObject jsonObject = jsonElement.getAsJsonObject();

    int productId = Integer.parseInt(jsonObject.get("id").getAsString());
    String author = jsonObject.get("name").getAsString();
    int title = jsonObject.get("quantity").getAsInt();
    int available = Integer.parseInt(jsonObject.get("price").getAsString());

    return new Product(productId, author, title, available);
  }
}
