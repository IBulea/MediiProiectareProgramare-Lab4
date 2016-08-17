package library.network.json;

import com.google.gson.*;
import library.network.dto.UserProductDTO;
import library.network.dto.UserDTO;
import library.network.rpcprotocol.Request;
import library.network.rpcprotocol.RequestType;

import java.lang.reflect.Type;

/**
 * Created by Sergiu on 11.04.2016.
 */
public class RequestDeserializer implements JsonDeserializer<Request> {
  @Override
  public Request deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
    Request request = null;

    JsonObject jsonObject = jsonElement.getAsJsonObject();

    String requestType = jsonObject.get("type").getAsString();

    if (requestType.equals(RequestType.LOGIN.name())) {
      UserDTO userDTO = jsonDeserializationContext.deserialize(jsonObject.get("data"), UserDTO.class);
      request = new Request.Builder().type(RequestType.LOGIN).data(userDTO).build();
    } else if (requestType.equals(RequestType.LOGOUT.name())) {
      int userId = Integer.parseInt(jsonObject.get("data").getAsString());
      request = new Request.Builder().type(RequestType.LOGOUT).data(userId).build();
    } else if (requestType.equals(RequestType.GET_AVAILABLE_PRODUCTS.name())) {
      request = new Request.Builder().type(RequestType.GET_AVAILABLE_PRODUCTS).build();
    } else if (requestType.equals(RequestType.GET_USER_PRODUCTS.name())) {
      int userId = Integer.parseInt(jsonObject.get("data").getAsString());
      request = new Request.Builder().type(RequestType.GET_USER_PRODUCTS).data(userId).build();
    } else if (requestType.equals(RequestType.SEARCH_PRODUCTS.name())) {
      String searchKey = jsonObject.get("data").getAsString();
      request = new Request.Builder().type(RequestType.SEARCH_PRODUCTS).data(searchKey).build();
    } else if (requestType.equals(RequestType.BUY_PRODUCT.name())) {
      UserProductDTO userProductDTO = jsonDeserializationContext.deserialize(jsonObject.get("data"), UserProductDTO.class);
      request = new Request.Builder().type(RequestType.BUY_PRODUCT).data(userProductDTO).build();
    }

    return request;
  }
}
