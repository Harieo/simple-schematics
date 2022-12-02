package net.harieo.schematics.serialization.impl.schematic;

import com.google.gson.JsonObject;
import net.harieo.schematics.schematic.Schematic;
import net.harieo.schematics.serialization.Blueprint;
import net.harieo.schematics.serialization.Deserializer;
import net.harieo.schematics.serialization.Serializer;
import org.jetbrains.annotations.NotNull;

/**
 * A {@link Blueprint} for the storage of {@link Schematic} to and from JSON.
 */
public class SchematicJsonBlueprint extends Blueprint<Schematic, JsonObject> {

    /**
     * A {@link Schematic} blueprint where both the serializer and deserializer are provided.
     *
     * @param serializer the serializer for the schematic
     * @param deserializer the deserializer for the schematic
     */
    public SchematicJsonBlueprint(@NotNull Serializer<Schematic, JsonObject> serializer,
                                  @NotNull Deserializer<Schematic, JsonObject> deserializer) {
        super(serializer, deserializer);
    }

    /**
     * A {@link Schematic} blueprint where the serializer is a default created {@link SchematicJsonSerializer} and the
     * deserializer is provided to allow the inner deserializers to be added.
     *
     * @param schematicJsonDeserializer the schematic deserializer with the necessary values provided
     * @apiNote See {@link SchematicJsonDeserializer} for an explanation on why this cannot be created by default
     */
    public SchematicJsonBlueprint(@NotNull SchematicJsonDeserializer schematicJsonDeserializer) {
        this(new SchematicJsonSerializer(), schematicJsonDeserializer);
    }

}
