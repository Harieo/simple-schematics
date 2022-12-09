package net.harieo.schematics.animation.serialization;

import com.google.gson.JsonObject;
import net.harieo.schematics.animation.Transition;
import net.harieo.schematics.animation.impl.basic.ScheduledAnimation;
import net.harieo.schematics.serialization.impl.animation.AnimationJsonDeserializer;
import net.harieo.schematics.serialization.registry.BlueprintRegistry;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 * An extension of {@link AnimationJsonDeserializer} which deserializes to {@link ScheduledAnimation}.
 */
public class ScheduledAnimationJsonDeserializer extends AnimationJsonDeserializer<ScheduledAnimation> {


    /**
     * Creates a deserializer with a {@link BlueprintRegistry} for all the blueprints available to
     * deserialize {@link Transition}.
     *
     * @param transitionBlueprintRegistry the registry containing {@link Transition} blueprints.
     */
    public ScheduledAnimationJsonDeserializer(@NotNull BlueprintRegistry<Transition, JsonObject> transitionBlueprintRegistry) {
        super(transitionBlueprintRegistry);
    }

    @Override
    public ScheduledAnimation deserializeToExactForm(@Nullable String id, @NotNull List<Transition> transitions) {
        return new ScheduledAnimation(id, transitions);
    }

}
