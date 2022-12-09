package net.harieo.schematics.serialization.impl.schematic;

import com.google.gson.JsonObject;
import net.harieo.schematics.position.Coordinate;
import net.harieo.schematics.schematic.Schematic;
import net.harieo.schematics.serialization.Blueprint;
import net.harieo.schematics.serialization.Deserializer;
import net.harieo.schematics.serialization.Serializer;
import org.jetbrains.annotations.NotNull;

/**
 * A {@link Blueprint} for the storage of {@link Schematic} to and from JSON.
 */
public class SchematicJsonBlueprint<T extends Coordinate> extends Blueprint<Schematic, JsonObject> {

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

}
