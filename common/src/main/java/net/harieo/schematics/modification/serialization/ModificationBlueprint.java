package net.harieo.schematics.modification.serialization;

import net.harieo.schematics.modification.Modification;
import org.jetbrains.annotations.NotNull;

/**
 * A record of the methods of serialization <b>and</b> deserialization for a {@link Modification}. Through this record,
 * it should be possible to save, unload, read and reload a {@link Modification}.
 *
 * @param <T> the type of modification subject to this blueprint
 * @param <V> the type of serialized object used to interact with storage
 */
public class ModificationBlueprint<T extends Modification, V> {

    private final ModificationSerializer<T, V> serializer;
    private final ModificationDeserializer<T, V> deserializer;

    /**
     * A record of the methods of serialization <b>and</b> deserialization for a {@link Modification}. Through this record,
     * it should be possible to save, unload, read and reload a {@link Modification}.
     *
     * @param serializer   the method of serialization
     * @param deserializer the method of deserialization
     */
    public ModificationBlueprint(@NotNull ModificationSerializer<T, V> serializer,
                                 @NotNull ModificationDeserializer<T, V> deserializer) {
        this.serializer = serializer;
        this.deserializer = deserializer;
    }

    public ModificationSerializer<T, V> getSerializer() {
        return serializer;
    }

    public ModificationDeserializer<T, V> getDeserializer() {
        return deserializer;
    }

}
