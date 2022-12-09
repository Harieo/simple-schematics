package net.harieo.schematics.serialization.serializable;

import net.harieo.schematics.serialization.Serializer;
import org.jetbrains.annotations.NotNull;

/**
 * An object which has a {@link Serializer}
 *
 * @param <T> the type of oeserialized object
 * @param <V> the type of serialized object
 */
public interface Serializable<T, V> {

    /**
     * @return the serializer
     */
    @NotNull Serializer<T, V> getSerializer();

    default V serialize(@NotNull T object) {
        return getSerializer().serialize(object);
    }

}
