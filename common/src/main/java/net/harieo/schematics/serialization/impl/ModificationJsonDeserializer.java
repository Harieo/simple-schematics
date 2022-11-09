package net.harieo.schematics.serialization.impl;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.harieo.schematics.modification.Modification;
import net.harieo.schematics.serialization.Deserializer;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

public abstract class ModificationJsonDeserializer<T extends Modification> implements Deserializer<T, JsonObject> {

    /**
     * Parses the {@link String} type of the serialized {@link Modification}.
     *
     * @param serializedObject the serialized modification
     * @return optionally the type, if present in the serialized object
     */
    public Optional<String> parseType(@NotNull JsonObject serializedObject) {
        JsonElement typeElement = serializedObject.get("type");
        if (typeElement.isJsonNull()) {
            return Optional.empty();
        } else {
            return Optional.of(typeElement.getAsString());
        }
    }

}
