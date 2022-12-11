package net.harieo.schematics.animation.impl.serializable;

import com.google.gson.JsonObject;
import net.harieo.schematics.animation.Transition;
import net.harieo.schematics.animation.impl.basic.EmptyTransition;
import net.harieo.schematics.animation.serialization.TransitionJsonSerializable;
import net.harieo.schematics.serialization.Serializer;
import net.harieo.schematics.serialization.impl.transition.TransitionJsonSerializer;
import org.jetbrains.annotations.NotNull;

/**
 * A {@link Transition} which has no action when run to add a delay to the animation sequence.
 */
public class SerializableEmptyTransition extends EmptyTransition implements TransitionJsonSerializable {

    // There are no configurable or overridable values, so this can just be constructed
    private final EmptyTransitionJsonSerializer emptyTransitionJsonSerializer = new EmptyTransitionJsonSerializer();

    /**
     * A transition with an amount of milliseconds before it should run and an amount of milliseconds before a proceeding
     * transition should run.
     *
     * @param millisecondsBefore the milliseconds before this transition should run
     * @param millisecondsAfter  the milliseconds before the next transition should run proceeding this one
     */
    public SerializableEmptyTransition(long millisecondsBefore, long millisecondsAfter) {
        super(millisecondsBefore, millisecondsAfter);
    }

    @Override
    public @NotNull Serializer<Transition, JsonObject> getSerializer() {
        return emptyTransitionJsonSerializer;
    }

    public static class EmptyTransitionJsonSerializer extends TransitionJsonSerializer {

        @Override
        public final void addExtraData(@NotNull Transition transition, @NotNull JsonObject serializedObject) {
            // No extra data exists for this transition type
        }

    }

}
