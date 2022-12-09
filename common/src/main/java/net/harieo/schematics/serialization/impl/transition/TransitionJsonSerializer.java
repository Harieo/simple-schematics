package net.harieo.schematics.serialization.impl.transition;

import com.google.gson.JsonObject;
import net.harieo.schematics.animation.Transition;
import net.harieo.schematics.serialization.Serializer;
import org.jetbrains.annotations.NotNull;

/**
 * A {@link Serializer} which serializes the base values for {@link Transition} then allows the addition of extra data
 * for the implementing subclass of {@link Transition}.
 *
 * @param <T> the type of {@link Transition} being serialized
 */
public abstract class TransitionJsonSerializer<T extends Transition> implements Serializer<T, JsonObject> {

    @Override
    public JsonObject serialize(@NotNull T transition) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("milliseconds-before", transition.getMillisecondsBefore());
        jsonObject.addProperty("milliseconds-after", transition.getMillisecondsAfter());
        addExtraData(transition, jsonObject);
        return jsonObject;
    }

    /**
     * Adds extra serialization data to the serialized JSON for the transition.
     *
     * @param transition the transition being serialized
     * @param serializedObject the serialized object containing the base fields of {@link Transition}
     */
    public abstract void addExtraData(@NotNull T transition, @NotNull JsonObject serializedObject);

}
