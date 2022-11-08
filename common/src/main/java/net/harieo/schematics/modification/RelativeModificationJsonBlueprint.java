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
     * @param actualModificationDeserializer the deserializer for the actual {@link Modification} contained within the {@link RelativeModification}
     */
    public RelativeModificationJsonBlueprint(@NotNull ModificationDeserializer<T, JsonObject> actualModificationDeserializer) {
        super(
                new RelativeModificationJsonSerializer<>(),
                new RelativeModificationJsonDeserializer<>(actualModificationDeserializer)
        );
    }

    public static class RelativeModificationJsonSerializer<T extends Modification>
            extends ModificationJsonSerializer<RelativeModification<T>> {

        @Override
        public void addSerializationData(@NotNull RelativeModification<T> relativeModification,
                                         @NotNull JsonObject serializedObject) {
            Modification actualModification = relativeModification.getActualModification();
            serializedObject.add("vector", relativeModification.getVector().serializeToJson());
            serializedObject.add(
                    "actual-modification",
                    actualModification.getJsonBlueprint().getSerializer().serialize(actualModification)
            );
        }

    }

    public static class RelativeModificationJsonDeserializer<T extends Modification>
            extends ModificationJsonDeserializer<RelativeModification<T>> {

        private final ModificationDeserializer<T, JsonObject> deserializer;

        public RelativeModificationJsonDeserializer(ModificationDeserializer<T, JsonObject> deserializer) {
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
