package net.harieo.schematics.serialization;

import org.jetbrains.annotations.NotNull;

/**
 * A basic interface which takes a serialized object <i>V</i> and deserializes it into a regular object <i>T</i>
 *
 * @param <T> the type of the deserialized object
 * @param <V> the type of the serialized object
 */
public interface Deserializer<T, V> {

    /**
     * Deserializes the serialized object <i>V</i> into the deserialized object <i>T</i>
     *
     * @param serializedObject the serialized object
     * @return the deserialized object
     */
    T deserialize(@NotNull V serializedObject);

    /**
     * Decides whether the serialized object is valid for the deserialized object type and, therefore, can be
     * deserialized by this class.
     *
     * @param serializedObject the serialized object to be checked
     * @return whether this deserializer can deserialize the provided serialized object
     */
    boolean isValidObject(@NotNull V serializedObject);

}
