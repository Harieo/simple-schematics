package net.harieo.schematics.serialization.impl.modification;

import com.google.gson.JsonObject;
import net.harieo.schematics.modification.Modification;
import net.harieo.schematics.modification.RelativeModification;
import net.harieo.schematics.serialization.Deserializer;
import net.harieo.schematics.serialization.Serializer;
import net.harieo.schematics.position.Coordinate;
import net.harieo.schematics.position.Vector;
import org.jetbrains.annotations.NotNull;

/**
 * A JSON blueprint for {@link RelativeModification}.
 *
 * @param <T> the inner {@link Modification} for the {@link RelativeModification}
 */
public class RelativeModificationJsonBlueprint<T extends Modification> extends ModificationJsonBlueprint<RelativeModification<T>> {

    /**
     * A JSON blueprint for {@link RelativeModification}.
     *
     * @param actualModificationBlueprint a {@link ModificationJsonBlueprint} for the serialization of the inner {@link Modification}
     */
    public RelativeModificationJsonBlueprint(@NotNull ModificationJsonBlueprint<T> actualModificationBlueprint) {
        super(
                new RelativeModificationJsonSerializer<>(actualModificationBlueprint.getSerializer()),
                new RelativeModificationJsonDeserializer<>(actualModificationBlueprint.getDeserializer())
        );
    }

    /**
     * A {@link Serializer} for {@link RelativeModification} into JSON.
     *
     * @param <T> the type of the inner {@link Modification}
     */
    public static class RelativeModificationJsonSerializer<T extends Modification>
            extends ModificationJsonSerializer<RelativeModification<T>> {

        private final Serializer<T, JsonObject> serializer;

        /**
         * A {@link Serializer} for {@link RelativeModification} into JSON.
         *
         * @param jsonSerializer the serializer for serializing the inner {@link Modification}
         */
        public RelativeModificationJsonSerializer(@NotNull Serializer<T, JsonObject> jsonSerializer) {
            this.serializer = jsonSerializer;
        }

        @Override
        public void addSerializationData(@NotNull RelativeModification<T> relativeModification,
                                         @NotNull JsonObject serializedObject) {
            serializedObject.add("vector", Coordinate.DEFAULT_JSON_BLUEPRINT.serialize(relativeModification.getVector()));
            serializedObject.add("actual-modification", serializer.serialize(relativeModification.getActualModification()));
        }

    }

    /**
     * A {@link Deserializer} for {@link RelativeModification} from JSON.
     *
     * @param <T> the type of inner {@link Modification}
     */
    public static class RelativeModificationJsonDeserializer<T extends Modification>
            extends ModificationJsonDeserializer<RelativeModification<T>> {

        private final Deserializer<T, JsonObject> deserializer;

        /**
         * A {@link Deserializer} for {@link RelativeModification} from JSON.
         *
         * @param deserializer the deserializer for the inner {@link Modification}
         */
        public RelativeModificationJsonDeserializer(@NotNull Deserializer<T, JsonObject> deserializer) {
            this.deserializer = deserializer;
        }

        @Override
        public RelativeModification<T> deserialize(@NotNull JsonObject serializedObject) {
            JsonObject serializedActualModification = serializedObject.getAsJsonObject("actual-modification");
            T deserializedModification = deserializer.deserialize(serializedActualModification);
            Vector deserializedVector = Coordinate.DEFAULT_JSON_BLUEPRINT
                    .deserialize(serializedObject.getAsJsonObject("vector"))
                    .toVector();
            return new RelativeModification<T>(deserializedModification, deserializedVector);
        }

    }

}
