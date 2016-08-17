package library.network.json;

import com.google.gson.*;
import library.network.dto.UserProductDTO;

import java.lang.reflect.Type;

/**
 * Created by Sergiu on 11.04.2016.
 */
public class UserProductDTODeserializer implements JsonDeserializer<UserProductDTO> {
  @Override
  public UserProductDTO deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
    JsonObject jsonObject = jsonElement.getAsJsonObject();

    int userId = Integer.parseInt(jsonObject.get("user_id").getAsString());
    int productId = Integer.parseInt(jsonObject.get("product_id").getAsString());
    int q=Integer.parseInt(jsonObject.get("quantity").getAsString());

    return new UserProductDTO(userId, productId,q);
  }
}
