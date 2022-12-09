package net.harieo.schematics.serialization.impl.schematic;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.harieo.schematics.modification.Modification;
import net.harieo.schematics.modification.RelativeModification;
import net.harieo.schematics.position.Coordinate;
import net.harieo.schematics.schematic.Schematic;
import net.harieo.schematics.serialization.Blueprint;
import net.harieo.schematics.serialization.Deserializer;
import net.harieo.schematics.serialization.impl.modification.RelativeModificationJsonBlueprint;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.StreamSupport;

/**
 * A deserializer from JSON format for {@link Schematic}.
 *
 * @param <T> the type of {@link Coordinate} used for the initial position.
 */
public class SchematicJsonDeserializer<T extends Coordinate> implements Deserializer<Schematic, JsonObject> {

    private final Deserializer<T, JsonObject> coordinateJsonDeserializer;

    private final Set<Blueprint<? extends Modification, JsonObject>> modificationBlueprints = new HashSet<>();

    /**
     * A deserializer from JSON format for {@link Schematic} with a specified {@link Deserializer} for
     * deserializing coordinates.
     *
     * @param coordinateJsonDeserializer the deserializer for deserializing coordinates
     */
    @SafeVarargs
    public SchematicJsonDeserializer(@NotNull Deserializer<T, JsonObject> coordinateJsonDeserializer,
                                     @NotNull Blueprint<? extends Modification, JsonObject>... modificationBlueprints) {
        this.coordinateJsonDeserializer = coordinateJsonDeserializer;
        this.modificationBlueprints.addAll(Arrays.asList(modificationBlueprints));
    }

    /**
     * Adds a {@link Modification} {@link Blueprint} to the list of blueprints which can be used for deserializing actual
     * modifications.
     *
     * @param deserializer the blueprint containing the relevant deserializer
     */
    public void addModificationBlueprint(@NotNull Blueprint<? extends Modification, JsonObject> deserializer) {
        modificationBlueprints.add(deserializer);
    }

    @Override
    @SuppressWarnings("unchecked")
    public Schematic deserialize(@NotNull JsonObject serializedObject) {
        // Get the id, if it is present (optional)
        String id = null;
        if (serializedObject.has("id")) {
            id = serializedObject.get("id").getAsString();
        }

        // Get the initial position coordinate
        Coordinate initialPosition = coordinateJsonDeserializer.deserialize(
                serializedObject.getAsJsonObject("initial-position"));

        // Load and deserialize all the relative modifications (complicated part)
        Set<RelativeModification<? extends Modification>> modifications = new HashSet<>();
        // Get the array of serialized modifications
        JsonArray rawModificationArray = serializedObject.getAsJsonArray("modifications");
        // Stream the modification JSON element and ensure they are formatted as JsonObjects
        StreamSupport.stream(rawModificationArray.spliterator(), false) // Older method to provide legacy support of JsonArray
                .filter(JsonElement::isJsonObject)
                .map(JsonElement::getAsJsonObject)
                .forEach(serializedRelativeModification ->
                    modificationBlueprints.forEach(actualModificationBlueprint -> { // Check all actual modification blueprints
                        // Get the deserializer, as our primary function is deserialization of this modification
                        Deserializer<? extends Modification, JsonObject> actualModificationDeserializer =
                                actualModificationBlueprint.getDeserializer();
                        // Load a relative modification deserializer with the actual modification deserializer
                        // Remember: We are deserializing a relative modification with an unknown actual modification inside it
                        RelativeModificationJsonBlueprint.RelativeModificationJsonDeserializer<Modification>
                                relativeModificationJsonDeserializer = (RelativeModificationJsonBlueprint.RelativeModificationJsonDeserializer<Modification>) new RelativeModificationJsonBlueprint
                                .RelativeModificationJsonDeserializer<>(actualModificationDeserializer);

                        // If this deserializer is capable of deserializing our relative modification
                        if (relativeModificationJsonDeserializer.isValidObject(serializedRelativeModification)) {
                            // Then do actually deserialize it
                            RelativeModification<Modification> deserializedModification =
                                    relativeModificationJsonDeserializer.deserialize(serializedRelativeModification);
                            // Set the blueprint while we have it so that we can re-serialize if necessary
                            deserializedModification.setActualModificationJsonBlueprint(
                                    (Blueprint<Modification, JsonObject>) actualModificationBlueprint);
                            // Add to the list of deserialized modifications
                            modifications.add(deserializedModification);
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
