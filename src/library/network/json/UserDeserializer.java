package library.network.json;

import com.google.gson.*;
import library.model.User;

import java.lang.reflect.Type;

/**
 * Created by Sergiu on 11.04.2016.
 */
public class UserDeserializer implements JsonDeserializer<User> {
  @Override
  public User deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
    JsonObject jsonObject = jsonElement.getAsJsonObject();

    int userId = Integer.parseInt(jsonObject.get("id").getAsString());
    String userName = jsonObject.get("username").getAsString();
    String password = jsonObject.get("password").getAsString();
    String fullName = jsonObject.get("realname").getAsString();

    return new User(userId, userName, password, fullName);
  }
}
