package net.harieo.schematics.serialization;

import org.jetbrains.annotations.NotNull;

/**
 * A record of both a {@link Serializer} and {@link Deserializer} which handle the same types.
 *
 * @param <T> the type of deserialized object
 * @param <V> the type of serialized object
 * @apiNote A blueprint can be used to serialize and deserialize an object independently. Therefore, any object which
 * is serialized via a blueprint should be identical if it flows through the blueprint in the opposite direction.
 */
public class Blueprint<T, V> {

    private final Serializer<T, V> serializer;
    private final Deserializer<T, V> deserializer;

    public Blueprint(@NotNull Serializer<T, V> serializer, @NotNull Deserializer<T, V> deserializer) {
        this.serializer = serializer;
        this.deserializer = deserializer;
    }

    public Serializer<T, V> getSerializer() {
        return serializer;
    }

    public V serialize(@NotNull T deserializedObject) {
        return getSerializer().serialize(deserializedObject);
    }

    public Deserializer<T, V> getDeserializer() {
        return deserializer;
    }

    public T deserialize(@NotNull V serializedObject) {
        return getDeserializer().deserialize(serializedObject);
    }

}
