package net.harieo.schematics.animation.impl.serializable;

import com.google.gson.JsonObject;
import net.harieo.schematics.animation.Transition;
import net.harieo.schematics.animation.impl.basic.SchematicTransition;
import net.harieo.schematics.animation.serialization.TransitionJsonSerializable;
import net.harieo.schematics.position.Coordinate;
import net.harieo.schematics.schematic.Schematic;
import net.harieo.schematics.serialization.Serializer;
import net.harieo.schematics.serialization.impl.schematic.SchematicJsonSerializer;
import net.harieo.schematics.serialization.impl.transition.TransitionJsonSerializer;
import net.harieo.schematics.serialization.serializable.Serializable;
import org.jetbrains.annotations.NotNull;

/**
 * An extension of {@link SchematicTransition} which is also {@link Serializable}.
 *
 * @param <T> the type of {@link Coordinate} used for the initial position of the {@link Schematic} used.
 */
public class SerializableSchematicTransition<T extends Coordinate> extends SchematicTransition implements TransitionJsonSerializable {

    private final SchematicTransitionJsonSerializer schematicTransitionJsonSerializer;

    /**
     * A transition which applies the specified {@link Schematic} when run.
     *
     * @param schematic the schematic to be applied
     * @param schematicJsonSerializer the serializer for the given schematic
     * @param millisecondsBefore the milliseconds before this transition should run
     * @param millisecondsAfter  the milliseconds before the next transition should run proceeding this one
     */
    public SerializableSchematicTransition(@NotNull Schematic schematic,
                                           @NotNull SchematicJsonSerializer<T> schematicJsonSerializer,
                                           long millisecondsBefore,
                                           long millisecondsAfter) {
        super(schematic, millisecondsBefore, millisecondsAfter);
        this.schematicTransitionJsonSerializer = new SchematicTransitionJsonSerializer(schematicJsonSerializer);
    }

    @Override
    public @NotNull Serializer<Transition, JsonObject> getSerializer() {
        return schematicTransitionJsonSerializer;
    }

    public class SchematicTransitionJsonSerializer extends TransitionJsonSerializer {

        private final SchematicJsonSerializer<T> schematicJsonSerializer;

        public SchematicTransitionJsonSerializer(SchematicJsonSerializer<T> schematicJsonSerializer) {
            this.schematicJsonSerializer = schematicJsonSerializer;
        }

        @Override
        public void addExtraData(@NotNull Transition transition, @NotNull JsonObject serializedObject) {
            if (transition instanceof SchematicTransition schematicTransition) {
                serializedObject.add("schematic", schematicJsonSerializer.serialize(schematicTransition.getSchematic()));
            }
        }

    }

}
