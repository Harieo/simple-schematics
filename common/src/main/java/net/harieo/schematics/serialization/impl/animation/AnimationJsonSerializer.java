package net.harieo.schematics.serialization.impl.animation;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.harieo.schematics.animation.Animation;
import net.harieo.schematics.animation.Transition;
import net.harieo.schematics.animation.serialization.TransitionJsonSerializable;
import net.harieo.schematics.serialization.Serializer;
import org.jetbrains.annotations.NotNull;

/**
 * A {@link Serializer} for {@link Animation} to JSON.
 *
 * @apiNote This class requires each {@link Transition} to conform to {@link TransitionJsonSerializable} to serialize
 * all the animation's transitions.
 */
public class AnimationJsonSerializer implements Serializer<Animation, JsonObject> {

    /**
     * Checks that all transitions in the animation are serializable.
     *
     * @param animation the animation to be serialized
     * @return whether the animation can be serialized in its entirety.
     */
    public boolean isSerializable(@NotNull Animation animation) {
        return animation.getAllTransitions()
                .stream()
                .noneMatch(transition -> transition instanceof TransitionJsonSerializable);
    }

    @Override
    public JsonObject serialize(@NotNull Animation animation) {
        JsonObject object = new JsonObject();
        animation.getId().ifPresent(id -> object.addProperty("id", id));
        JsonArray transitionArray = new JsonArray();
        for (Transition transition : animation.getAllTransitions()) {
            if (transition instanceof TransitionJsonSerializable transitionJsonSerializable) {
                transitionArray.add(transitionJsonSerializable.serialize(transition));
            } else {
                throw new IllegalStateException("Unserializable transition: " + transition.getId().orElse("Unknown"));
            }
        }
        object.add("transitions", transitionArray);
        return object;
    }

}
