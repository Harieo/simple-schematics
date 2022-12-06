package net.harieo.schematics.serialization.impl.schematic;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.harieo.schematics.modification.Modification;
import net.harieo.schematics.modification.RelativeModification;
import net.harieo.schematics.position.Coordinate;
import net.harieo.schematics.schematic.Schematic;
import net.harieo.schematics.serialization.Deserializer;
import net.harieo.schematics.serialization.impl.coordinate.CoordinateJsonBlueprint;
import net.harieo.schematics.serialization.impl.modification.RelativeModificationJsonBlueprint;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * A deserializer from JSON format for {@link Schematic}.
 */
public class SchematicJsonDeserializer implements Deserializer<Schematic, JsonObject> {

    private final Deserializer<Coordinate, JsonObject> coordinateJsonDeserializer;

    private final Set<Deserializer<Modification, JsonObject>> modificationDeserializers = new HashSet<>();

    /**
     * A deserializer from JSON format for {@link Schematic} with a specified {@link Deserializer} for
     * deserializing coordinates.
     *
     * @param coordinateJsonDeserializer the deserializer for deserializing coordinates
     */
    @SafeVarargs
    public SchematicJsonDeserializer(@NotNull Deserializer<Coordinate, JsonObject> coordinateJsonDeserializer,
                                     @NotNull Deserializer<Modification, JsonObject>... modificationDeserializers) {
        this.coordinateJsonDeserializer = coordinateJsonDeserializer;
        this.modificationDeserializers.addAll(Arrays.asList(modificationDeserializers));
    }

    /**
     * A default deserializer from JSON format for {@link Schematic} which uses a default {@link CoordinateJsonBlueprint}
     * to provide the coordinate {@link Deserializer}.
     */
    @SafeVarargs
    public SchematicJsonDeserializer(@NotNull Deserializer<Modification, JsonObject>... modificationDeserializers) {
        this(new CoordinateJsonBlueprint().getDeserializer(), modificationDeserializers);
    }

    public void addModificationDeserializer(@NotNull Deserializer<Modification, JsonObject> deserializer) {
        modificationDeserializers.add(deserializer);
    }

    @Override
    public Schematic deserialize(@NotNull JsonObject serializedObject) {
        String id = null;
        if (serializedObject.has("id")) {
            id = serializedObject.get("id").getAsString();
        }

        Coordinate initialPosition = coordinateJsonDeserializer.deserialize(
                serializedObject.getAsJsonObject("initial-position"));

        Set<RelativeModification<Modification>> modifications = new HashSet<>();
        JsonArray rawModificationArray = serializedObject.getAsJsonArray("modifications");
        rawModificationArray.asList().stream()
                .filter(JsonElement::isJsonObject)
                .map(JsonElement::getAsJsonObject)
                .forEach(serializedRelativeModification ->
                    modificationDeserializers.forEach(actualModificationDeserializer -> {
                        RelativeModificationJsonBlueprint.RelativeModificationJsonDeserializer<Modification>
                                relativeModificationJsonDeserializer = new RelativeModificationJsonBlueprint
                                .RelativeModificationJsonDeserializer<>(actualModificationDeserializer);

                        if (relativeModificationJsonDeserializer.isValidObject(serializedRelativeModification)) {
                            modifications.add(relativeModificationJsonDeserializer.deserialize(serializedRelativeModification));
                        }
                    })
                );

        return new Schematic(id, initialPosition, modifications);
    }

    @Override
    public boolean isValidObject(@NotNull JsonObject serializedObject) {
        return serializedObject.has("initial-position") && serializedObject.has("modifications");
    }

}
