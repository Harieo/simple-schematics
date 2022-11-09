package net.harieo.schematics.serialization.impl;

import com.google.gson.JsonObject;
import net.harieo.schematics.modification.Modification;
import net.harieo.schematics.serialization.Blueprint;
import org.jetbrains.annotations.NotNull;

/**
 * A blueprint for JSON serialization.
 *
 * @param <T> the type of modification being serialized
 */
public class ModificationJsonBlueprint<T extends Modification> extends Blueprint<T, JsonObject> {


    /**
     * A combination of {@link ModificationJsonSerializer} and {@link ModificationJsonDeserializer} for serialization
     * to/from JSON format.
     *
     * @param serializer   the method of serialization into JSON
     * @param deserializer the method of deserialization from JSON
     */
    public ModificationJsonBlueprint(@NotNull ModificationJsonSerializer<T> serializer,
                                     @NotNull ModificationJsonDeserializer<T> deserializer) {
        super(serializer, deserializer);
    }

}
