package net.harieo.schematics.modification;

import com.google.gson.JsonObject;
import net.harieo.schematics.modification.serialization.ModificationDeserializer;
import net.harieo.schematics.modification.serialization.ModificationSerializer;
import net.harieo.schematics.modification.serialization.json.ModificationJsonBlueprint;
import net.harieo.schematics.modification.serialization.json.ModificationJsonDeserializer;
import net.harieo.schematics.modification.serialization.json.ModificationJsonSerializer;
import net.harieo.schematics.position.Coordinate;
import net.harieo.schematics.position.Vector;
import org.jetbrains.annotations.NotNull;

public class RelativeModificationJsonBlueprint<T extends Modification> extends ModificationJsonBlueprint<RelativeModification<T>> {

    /**
     * A JSON blueprint for {@link RelativeModification} which is effectively a {@link Vector} and another type of {@link Modification}.
     *
     * @param actualModificationBlueprint a {@link ModificationJsonBlueprint} for the serialization of the inner {@link Modification}
     */
    public RelativeModificationJsonBlueprint(@NotNull ModificationJsonBlueprint<T> actualModificationBlueprint) {
        super(
                new RelativeModificationJsonSerializer<>(actualModificationBlueprint.getSerializer()),
                new RelativeModificationJsonDeserializer<>(actualModificationBlueprint.getDeserializer())
        );
    }

    public static class RelativeModificationJsonSerializer<T extends Modification>
            extends ModificationJsonSerializer<RelativeModification<T>> {

        private final ModificationSerializer<T, JsonObject> serializer;

        public RelativeModificationJsonSerializer(@NotNull ModificationSerializer<T, JsonObject> jsonSerializer) {
            this.serializer = jsonSerializer;
        }

        @Override
        public void addSerializationData(@NotNull RelativeModification<T> relativeModification,
                                         @NotNull JsonObject serializedObject) {
            serializedObject.add("vector", relativeModification.getVector().serializeToJson());
            serializedObject.add("actual-modification", serializer.serialize(relativeModification.getActualModification()));
        }

    }

    public static class RelativeModificationJsonDeserializer<T extends Modification>
            extends ModificationJsonDeserializer<RelativeModification<T>> {

        private final ModificationDeserializer<T, JsonObject> deserializer;

        public RelativeModificationJsonDeserializer(@NotNull ModificationDeserializer<T, JsonObject> deserializer) {
            this.deserializer = deserializer;
        }

        @Override
        public RelativeModification<T> deserialize(@NotNull JsonObject serializedObject) {
            JsonObject serializedActualModification = serializedObject.getAsJsonObject("actual-modification");
            T deserializedModification = deserializer.deserialize(serializedActualModification);
            Vector deserializedVector = Coordinate.deserialize(serializedObject.getAsJsonObject("vector")).toVector();
            return new RelativeModification<T>(deserializedModification, deserializedVector);
        }

    }

}
