package net.harieo.schematics.serialization.impl;

import com.google.gson.JsonObject;
import net.harieo.schematics.modification.Modification;
import net.harieo.schematics.serialization.Serializer;
import org.jetbrains.annotations.NotNull;

/**
 * A serializer to serialize {@link Modification} into JSON.
 *
 * @param <T> the type of modification being serialized
 */
public abstract class ModificationJsonSerializer<T extends Modification> implements Serializer<T, JsonObject> {

    @Override
    public JsonObject serialize(@NotNull T modification) {
        JsonObject serializedObject = new JsonObject();
        serializedObject.addProperty("type", modification.getType());
        addSerializationData(modification, serializedObject);
        return serializedObject;
    }

    /**
     * Adds serialization data to the JSON object for the specific modification.
     *
     * @param modification the object being serialized
     * @param serializedObject the JSON object containing serialized data
     */
    public abstract void addSerializationData(@NotNull T modification, @NotNull JsonObject serializedObject);

}
