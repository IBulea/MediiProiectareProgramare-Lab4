package library.network.json;

import com.google.gson.*;
import library.model.Product;
import library.model.User;
import library.network.dto.ProductBorrowedDTO;
import library.network.rpcprotocol.Response;
import library.network.rpcprotocol.ResponseType;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Sergiu on 11.04.2016.
 */
public class ResponseDeserializer implements JsonDeserializer<Response> {
  @Override
  public Response deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
    Response response = null;

    JsonObject jsonObject = jsonElement.getAsJsonObject();

    String responseType = jsonObject.get("type").getAsString();

    if (responseType.equals(ResponseType.LOGIN_SUCCESSFULLY.name())) {
      User user = jsonDeserializationContext.deserialize(jsonObject.get("data"), User.class);
      response = new Response.Builder().type(ResponseType.LOGIN_SUCCESSFULLY).data(user).build();
    } else if (responseType.equals(ResponseType.LOGOUT_SUCCESSFULLY.name())) {
      response = new Response.Builder().type(ResponseType.LOGOUT_SUCCESSFULLY).build();
    } else if (responseType.equals(ResponseType.GET_AVAILABLE_PRODUCTS.name())) {
      JsonArray jsonArray = jsonObject.get("data").getAsJsonArray();
      List<Product> availableProducts = new ArrayList<>();
      for (int i = 0; i < jsonArray.size(); i++) {
        availableProducts.add((Product) jsonDeserializationContext.deserialize(jsonArray.get(i), Product.class));
      }
      response = new Response.Builder().type(ResponseType.GET_AVAILABLE_PRODUCTS).data(availableProducts).build();
    } else if (responseType.equals(ResponseType.GET_USER_PRODUCTS.name())) {
      JsonArray jsonArray = jsonObject.get("data").getAsJsonArray();
      List<Product> userProducts = new ArrayList<>();
      for (int i = 0; i < jsonArray.size(); i++) {
        userProducts.add((Product) jsonDeserializationContext.deserialize(jsonArray.get(i), Product.class));
      }
      response = new Response.Builder().type(ResponseType.GET_USER_PRODUCTS).data(userProducts).build();
    } else if (responseType.equals(ResponseType.SEARCH_PRODUCTS.name())) {
      JsonArray jsonArray = jsonObject.get("data").getAsJsonArray();
      List<Product> foundProducts = new ArrayList<>();
      for (int i = 0; i < jsonArray.size(); i++) {
        foundProducts.add((Product) jsonDeserializationContext.deserialize(jsonArray.get(i), Product.class));
      }
      response = new Response.Builder().type(ResponseType.SEARCH_PRODUCTS).data(foundProducts).build();
    } else if (responseType.equals(ResponseType.OK.name())) {
      response = new Response.Builder().type(ResponseType.OK).build();
    } else if (responseType.equals(ResponseType.ERROR.name())) {
      response = new Response.Builder().type(ResponseType.ERROR).build();	
    } else if (responseType.equals(ResponseType.BUY_PRODUCT.name())) {
      ProductBorrowedDTO productBorrowedDTO = jsonDeserializationContext.deserialize(jsonObject.get("data"), ProductBorrowedDTO.class);
      response = new Response.Builder().type(ResponseType.BUY_PRODUCT).data(productBorrowedDTO).build();
    }

    return response;
  }
}
