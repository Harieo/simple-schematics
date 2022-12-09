package net.harieo.schematics.serialization.impl.animation;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.harieo.schematics.animation.Animation;
import net.harieo.schematics.animation.Transition;
import net.harieo.schematics.serialization.Deserializer;
import net.harieo.schematics.serialization.registry.BlueprintRegistry;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

/**
 * A framework for deserializing the core data of {@link Animation}.
 *
 * @param <T> the type of {@link Animation} which will be constructed using this deserializer
 * @apiNote As {@link Animation} is abstract, this deserializer is also abstract and the final {@link Animation} will
 * be an implementing subclass {@code <T>}
 */
public abstract class AnimationJsonDeserializer<T extends Animation> implements Deserializer<T, JsonObject> {

    private final BlueprintRegistry<Transition, JsonObject> transitionBlueprintRegistry;

    /**
     * Creates a deserializer with a {@link BlueprintRegistry} for all the blueprints available to
     * deserialize {@link Transition}.
     *
     * @param transitionBlueprintRegistry the registry containing {@link Transition} blueprints.
     */
    public AnimationJsonDeserializer(@NotNull BlueprintRegistry<Transition, JsonObject> transitionBlueprintRegistry) {
        this.transitionBlueprintRegistry = transitionBlueprintRegistry;
    }

    /**
     * Deserializes the data for {@link Animation} then passes this data to {@link #deserializeToExactForm(String, List)}
     * to be instantiated as an implementation of {@link Animation}.
     *
     * @param serializedObject the serialized object
     * @return the deserialized animation.
     * @throws NoSuchElementException if a serialized {@link Transition} cannot be deserialized from any provided blueprint
     */
    @Override
    public T deserialize(@NotNull JsonObject serializedObject) {
        String id = null;
        if (serializedObject.has("id")) {
            id = serializedObject.get("id").getAsString();
        }

        JsonArray rawTransitionsArray = serializedObject.getAsJsonArray("transitions");
        List<Transition> transitions = new ArrayList<>();
        for (JsonElement rawTransitionElement : rawTransitionsArray) {
            if (rawTransitionElement.isJsonObject()) {
                JsonObject rawTransition = rawTransitionElement.getAsJsonObject();
                Transition transition = transitionBlueprintRegistry.findDeserializer(rawTransition)
                        .orElseThrow(() -> new NoSuchElementException("No deserializer for transition in register"))
                        .deserialize(rawTransition);
                transitions.add(transition);
            }
        }
        return deserializeToExactForm(id, transitions);
    }

    public abstract T deserializeToExactForm(@Nullable String id, @NotNull List<Transition> transitions);

    @Override
    public boolean isValidObject(@NotNull JsonObject serializedObject) {
        return serializedObject.has("transitions");
    }

}
