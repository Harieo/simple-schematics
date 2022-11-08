package net.harieo.schematics.modification;

import com.google.gson.*;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Type;

/**
 * A {@link JsonDeserializer} for a {@link Modification}.
 *
 * @param <T> the type of {@link Modification} being deserialized
 * @apiNote By default, this class expects to be deserializing a simple {@link JsonObject} with no special type. However,
 * you can override {@link #deserialize(JsonElement, Type, JsonDeserializationContext)} to fully utilise GSON if you know how.
 */
public abstract class ModificationDeserializer<T extends Modification> implements JsonDeserializer<T> {

    @Override
    public T deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        if (jsonElement.isJsonObject()) {
            return deserialize(jsonElement.getAsJsonObject());
        } else {
            throw new JsonParseException("Invalid JSON data type for default modification deserializer");
        }
    }

    public abstract T deserialize(@NotNull JsonObject jsonObject);

}
