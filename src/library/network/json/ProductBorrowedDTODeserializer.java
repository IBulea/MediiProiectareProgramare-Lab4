package library.network.json;

import com.google.gson.*;
import library.network.dto.ProductBorrowedDTO;

import java.lang.reflect.Type;

/**
 * Created by Sergiu on 11.04.2016.
 */
public class ProductBorrowedDTODeserializer implements JsonDeserializer<ProductBorrowedDTO> {
  @Override
  public ProductBorrowedDTO deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
    JsonObject jsonObject = jsonElement.getAsJsonObject();

    int productId = Integer.parseInt(jsonObject.get("productId").getAsString());
    int newQuantity = Integer.parseInt(jsonObject.get("newQuantity").getAsString());
    boolean byThisUser = jsonObject.get("byThisUser").getAsBoolean();

    return new ProductBorrowedDTO(productId, newQuantity, byThisUser);
  }
}
