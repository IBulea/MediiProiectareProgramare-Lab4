package library.network.json;

import com.google.gson.*;
import library.network.dto.UserDTO;

import java.lang.reflect.Type;

/**
 * Created by Sergiu on 11.04.2016.
 */
public class UserDTODeserializer implements JsonDeserializer<UserDTO> {
  @Override
  public UserDTO deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
    JsonObject jsonObject = jsonElement.getAsJsonObject();

    String userName = jsonObject.get("username").getAsString();
    String password = jsonObject.get("password").getAsString();

    return new UserDTO(userName, password);
  }
}
