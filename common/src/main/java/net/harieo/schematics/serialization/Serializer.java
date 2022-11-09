package net.harieo.schematics.serialization;

import org.jetbrains.annotations.NotNull;

/**
 * A basic interface for a function which serializes a value <i>T</i> into a serialized object <i>V</i>
 *
 * @param <T> the type of the deserialized object
 * @param <V> the type of the serialized object
 */
public interface Serializer<T, V> {

    /**
     * Serializes a regular object <i>T</i> into the serialized object <i>V</i>
     *
     * @param object the regular object which should be serialized
     * @return the serialized object
     */
    V serialize(@NotNull T object);

}
